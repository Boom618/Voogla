package com.ty.voogla

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun temp(){
        val list = mutableListOf<String>()
        list.add("1")
        list.add("1")
        list.add("2")
        val size = list.size
        val size1 = list.distinct().size

        val timeMillis = System.currentTimeMillis()

        var temp = 0
        for (i in 0..100000000){
//            for (j in 0..10000){
//               temp = i + j
//            }
            temp = i
        }
        println(temp)

        val millis = System.currentTimeMillis()

        println(millis - timeMillis)


    }


}
