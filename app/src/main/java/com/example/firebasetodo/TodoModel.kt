package com.example.firebasetodo


class TodoModel {
    companion object Factory {
        fun createList(): TodoModel = TodoModel()
    }


    var uid: String? = null
    var itemDataText: String? = null
    var done: Boolean? = false


}

