package com.binar.challenge4.utils

object EventTimeConverter {

    fun convert(rawTime: String): String{

        val formattedString = ArrayList<String>()

        rawTime.split(",").map { it.trim() }.forEach {
            when(it){
                "1" -> formattedString.add("Pagi")
                "2" -> formattedString.add("Siang")
                "3" -> formattedString.add("Malam")
            }
        }


        return formattedString.joinToString()

    }
}