package com.tistory.blackjin.birdviewassignment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import com.tistory.blackjin.birdviewassignment.base.BaseActivity
import com.tistory.blackjin.birdviewassignment.base.BaseRecyclerViewAdapter
import com.tistory.blackjin.birdviewassignment.controller.MatchingHobbyController
import com.tistory.blackjin.birdviewassignment.databinding.ActivityHobbyBinding
import com.tistory.blackjin.birdviewassignment.databinding.ItemHobbyBinding
import com.tistory.blackjin.birdviewassignment.util.ReadTextFileUtil
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DetailActivity : BaseActivity<ActivityHobbyBinding>(R.layout.activity_hobby) {

    private val adapter by lazy {
        object : BaseRecyclerViewAdapter<String, ItemHobbyBinding>(
            layoutRes = R.layout.item_hobby,
            bindingVariableId = BR.hobby
        ) {}
    }

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val fileName = intent.getStringExtra(EXTRA_FILE_NAME)
        val commonHobbies = intent.getStringExtra(EXTRA_COMMON_HOBBIES)

        if (!fileName.isNullOrEmpty() && !commonHobbies.isNullOrEmpty()) {
            setupRecyclerView()
            loadData(fileName, commonHobbies)
        } else {
            error(IllegalArgumentException("fileName or commonHobbies nothing"))
        }
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }

    private fun setupRecyclerView() {

        binding.rvHobby.run {

            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

            adapter = this@DetailActivity.adapter
        }
    }

    private fun loadData(fileName: String, commonHobbies: String) {

        val usersHobbies = ReadTextFileUtil.getItems(this, fileName)

        MatchingHobbyController.getRxUsersHadMatchingHobbies(usersHobbies, commonHobbies.split(" "))
            .subscribeOn(Schedulers.io())
            .flatMap { userMatchingHobbies ->

                val items = mutableListOf<String>()

                for ((i, commonHobby1) in userMatchingHobbies.withIndex()) {

                    for (j in i + 1 until userMatchingHobbies.size) {

                        val commonHobby2 = userMatchingHobbies[j]

                        items.add(
                            commonHobby1.joinToString(separator = " ") + "\n" + commonHobby2.joinToString(separator = " ")
                        )
                    }
                }

                Single.just(items)

            }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                showProgress()
            }
            .doOnSuccess {
                hideProgress()
            }
            .doOnError {
                hideProgress()
            }
            .subscribe({ items ->
                adapter.replaceAll(items)
            }) {

            }.also {
                compositeDisposable.add(it)
            }
    }

    private fun showProgress() {
        binding.pbHobby.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        binding.pbHobby.visibility = View.GONE
    }

    companion object {

        const val EXTRA_FILE_NAME = "file_name"
        const val EXTRA_COMMON_HOBBIES = "common_hobbies"

        fun startDetailActivity(context: Context, fileName: String, commonHobbies: String) {
            context.startActivity(
                Intent(context, DetailActivity::class.java).apply {
                    putExtra(EXTRA_FILE_NAME, fileName)
                    putExtra(EXTRA_COMMON_HOBBIES, commonHobbies)
                }
            )

        }
    }
}
