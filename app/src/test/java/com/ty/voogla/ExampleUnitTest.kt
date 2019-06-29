package com.ty.voogla

import com.ty.voogla.data.RemoveDupData
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
        println(list)
        val set = list.toMutableSet()
        println(set)
        val mutableList = set.toMutableList()

        val removeDup = RemoveDupData.removeDupString(list)

        println(removeDup)
        println(mutableList::class.java)



    }


}
