package com.tistory.blackjin.birdviewassignment

import com.tistory.blackjin.birdviewassignment.controller.MatchingHobbyController
import org.junit.Before
import org.junit.Test

class Exercise {

    private lateinit var usersHobby: Array<List<String>>

    @Before
    fun setup() {

        val items = mutableListOf<List<String>>()

        //기본 예제
        /*items.add(listOf("E", "H", "R", "A", "D", "W", "Q", "C", "T", "P"))
        items.add(listOf("E", "G", "U", "D", "A", "M", "C", "P", "U", "B"))
        items.add(listOf("E", "H", "R", "D", "A", "Q", "W", "C", "T", "M"))*/

        //취미가 10개 일치하는 커플이 여러 커플 발생하는 경우
        /*items.add(listOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J"))
        items.add(listOf("E", "H", "R", "D", "A", "Q", "W", "C", "T", "M"))
        items.add(listOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J"))
        items.add(listOf("E", "H", "R", "D", "A", "Q", "W", "C", "T", "M"))*/

        //10개의 같은 취미를 가진 대상자가 3명
        /*items.add(listOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J"))
        items.add(listOf("B", "A", "C", "D", "E", "F", "G", "H", "I", "J"))
        items.add(listOf("C", "A", "B", "D", "E", "F", "G", "H", "I", "J"))
        items.add(listOf("E", "H", "R", "D", "A", "Q", "W", "C", "T", "M"))*/

        //예제
        items.add(listOf("A", "B", "C", "K", "N", "F", "G", "H", "I", "Z"))
        items.add(listOf("B", "A", "C", "K", "N", "F", "G", "H", "I", "Z"))
        items.add(listOf("C", "A", "B", "K", "N", "F", "G", "H", "I", "Z"))
        items.add(listOf("K", "A", "B", "C", "N", "F", "G", "H", "I", "Z"))

        usersHobby = items.toTypedArray()
    }

    @Test
    fun solution() {

        val maxCommonHobbies = MatchingHobbyController.getMaxCommonHobbies(usersHobby)
        println("maxCommonHobbies : $maxCommonHobbies")
        println()

        for (maxCommonHobby in maxCommonHobbies) {

            val userMatchingHobbies =
                MatchingHobbyController.getUsersHadMatchingHobbies(usersHobby, maxCommonHobby)
            println("userMatchingHobbies : $userMatchingHobbies")

            val items = mutableListOf<Pair<String, String>>()

            for ((i, commonHobby1) in userMatchingHobbies.withIndex()) {

                for (j in i + 1 until userMatchingHobbies.size) {

                    val commonHobby2 = userMatchingHobbies[j]

                    items.add(
                        Pair(
                            commonHobby1.joinToString(separator = " "),
                            commonHobby2.joinToString(separator = " ")
                        )
                    )
                }
            }

            println("items : $items")
            println()
        }

    }
}