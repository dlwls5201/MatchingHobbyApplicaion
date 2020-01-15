package com.tistory.blackjin.birdviewassignment

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import com.tistory.blackjin.birdviewassignment.base.BaseActivity
import com.tistory.blackjin.birdviewassignment.base.BaseRecyclerViewAdapter
import com.tistory.blackjin.birdviewassignment.base.BaseViewHolder
import com.tistory.blackjin.birdviewassignment.controller.MatchingHobbyController
import com.tistory.blackjin.birdviewassignment.databinding.ActivityHobbyBinding
import com.tistory.blackjin.birdviewassignment.databinding.ItemHobbyBinding
import com.tistory.blackjin.birdviewassignment.util.ReadTextFileUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainActivity : BaseActivity<ActivityHobbyBinding>(R.layout.activity_hobby) {

    private val fileName = "100.txt"

    private val adapter by lazy {
        object : BaseRecyclerViewAdapter<String, ItemHobbyBinding>(
            layoutRes = R.layout.item_hobby,
            bindingVariableId = BR.hobby
        ) {
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): BaseViewHolder<ItemHobbyBinding> {
                return super.onCreateViewHolder(parent, viewType).apply {
                    itemView.setOnClickListener {
                        DetailActivity.startDetailActivity(
                            itemView.context,
                            fileName,
                            binding.hobby!!
                        )
                    }
                }
            }
        }
    }

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupRecyclerView()
        loadData()
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }

    private fun setupRecyclerView() {

        binding.rvHobby.run {

            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

            adapter = this@MainActivity.adapter
        }
    }

    private fun loadData() {

        val usersHobbies = ReadTextFileUtil.getItems(this, fileName)

        MatchingHobbyController.getRxMaxCommonHobbies(usersHobbies)
            .subscribeOn(Schedulers.io())
            .map { it.map { it.joinToString(" ") } }
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
}