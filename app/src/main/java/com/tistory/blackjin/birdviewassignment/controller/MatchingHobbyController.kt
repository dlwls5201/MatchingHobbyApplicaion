package com.tistory.blackjin.birdviewassignment.controller

import io.reactivex.Single

object MatchingHobbyController : MatchingHobbyInterface {

    //최대로 일치하는 취미를 리턴
    override fun getMaxCommonHobbies(usersHobbies: Array<List<String>>): List<List<String>> {

        val userCount = usersHobbies.size

        var maxCommonHobbySize = 0

        val commonHobbies = mutableListOf<List<String>>()

        //중복을 제거하기 위한 정렬된 취미 변수
        val sortedCommonHobbies = mutableListOf<List<String>>()

        for ((i, userHobby1) in usersHobbies.withIndex()) {

            for (j in i + 1 until userCount) {

                val userHobby2 = usersHobbies[j]

                //두 유저의 공통 취미를 구합니다.
                val commonHobby = getCommonHobbies(userHobby1, userHobby2)

                //공통 취미의 갯수가 최대인지 확인 후 분기 처리 합니다.
                when {
                    commonHobby.size > maxCommonHobbySize -> {

                        maxCommonHobbySize = commonHobby.size

                        commonHobbies.clear()
                        commonHobbies.add(commonHobby)

                        sortedCommonHobbies.clear()
                        sortedCommonHobbies.add(commonHobby.sorted())
                    }

                    commonHobby.size == maxCommonHobbySize -> {

                        //중복되지 않은 공통의 관심사만 추가해 줍니다.
                        commonHobby.let {
                            val sortedHobby = it.sorted()

                            if (!sortedCommonHobbies.contains(sortedHobby)) {
                                commonHobbies.add(commonHobby)
                                sortedCommonHobbies.add(sortedHobby)
                            }
                        }
                    }
                }
            }
        }

        return commonHobbies
    }

    override fun getRxMaxCommonHobbies(usersHobbies: Array<List<String>>): Single<List<List<String>>> {
        return Single.create { emitter ->
            emitter.onSuccess(getMaxCommonHobbies(usersHobbies))
        }
    }

    //공통 취미를 리턴합니다.
    private fun getCommonHobbies(
        hobbies1: List<String>, hobbies2: List<String>
    ): MutableList<String> {

        val commonHobby = mutableListOf<String>()

        for (hobby1 in hobbies1) {
            for (hobby2 in hobbies2) {
                if (hobby1 == hobby2) {
                    commonHobby.add(hobby1)
                }
            }
        }

        return commonHobby
    }

    //특정 취미를 모두 포함하는 유저를 리턴
    override fun getUsersHadMatchingHobbies(
        usersHobbies: Array<List<String>>, specificHobbies: List<String>
    ): List<List<String>> {

        val usersHadHobbies = mutableListOf<List<String>>()

        //O(N)
        for (userHobbies in usersHobbies) {
            if (isSpecificHobbies(userHobbies, specificHobbies)) {
                usersHadHobbies.add(userHobbies)
            }
        }

        return usersHadHobbies
    }

    override fun getRxUsersHadMatchingHobbies(
        usersHobbies: Array<List<String>>,
        specificHobbies: List<String>
    ): Single<List<List<String>>> {
        return Single.create {emitter ->
            emitter.onSuccess(getUsersHadMatchingHobbies(usersHobbies, specificHobbies))
        }
    }

    //특정 취미를 모두 갖고 있는지 여부
    private fun isSpecificHobbies(
        hobbies: List<String>, specificHobbies: List<String>
    ): Boolean {

        var count = 0

        for (hobby1 in specificHobbies) {
            for (hobby2 in hobbies) {
                if (hobby1 == hobby2) {
                    count++
                    break
                }
            }
        }

        return count == specificHobbies.size
    }
}