package com.binar.challenge4.utils

object SetIdSpinner {

    fun setIdSpinner(value: String):Int{
        return when(value){
            "Wedding" -> 1
            "Gradutaion" -> 2
            "Seminar" -> 3
            "Ceremony" -> 4
            else -> 5
        }
    }

}