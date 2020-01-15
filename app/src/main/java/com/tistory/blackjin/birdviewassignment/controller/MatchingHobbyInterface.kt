package com.tistory.blackjin.birdviewassignment.controller

import io.reactivex.Single

interface MatchingHobbyInterface {

    fun getMaxCommonHobbies(usersHobbies: Array<List<String>>): List<List<String>>

    fun getRxMaxCommonHobbies(usersHobbies: Array<List<String>>): Single<List<List<String>>>

    fun getUsersHadMatchingHobbies(
        usersHobbies: Array<List<String>>, specificHobbies: List<String>
    ): List<List<String>>

    fun getRxUsersHadMatchingHobbies(
        usersHobbies: Array<List<String>>, specificHobbies: List<String>
    ): Single<List<List<String>>>
}