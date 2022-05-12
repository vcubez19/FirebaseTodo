package com.example.firebasetodo


interface UpdateAndDelete {
    fun modifyItem(itemUID: String, isDone: Boolean)
    fun onItemDelete(itemUID: String)
}

