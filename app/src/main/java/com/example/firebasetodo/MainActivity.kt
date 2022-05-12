package com.example.firebasetodo

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*


class MainActivity : AppCompatActivity(), UpdateAndDelete {


    private lateinit var database: DatabaseReference
    var todoList: MutableList<TodoModel>? = null
    private lateinit var adapter: TodoAdapter
    private var listViewItem: ListView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val colorDrawable = ColorDrawable(Color.parseColor("#0F9D58"))
        supportActionBar?.setBackgroundDrawable(colorDrawable)
        supportActionBar?.title = "Dew it"


        val fab = findViewById<View>(R.id.fab) as FloatingActionButton
        listViewItem = findViewById(R.id.listView)


        this.database = FirebaseDatabase.getInstance().reference


        fab.setOnClickListener {
            val alertDialog = AlertDialog.Builder(this)
            val textEditText = EditText(this)
            alertDialog.setMessage("Add a todo")
            alertDialog.setTitle("Add yah todo")
            alertDialog.setView(textEditText)
            alertDialog.setPositiveButton("Add") { dialog, _ ->
                val todoItemData = TodoModel.createList()
                todoItemData.itemDataText = textEditText.text.toString()
                todoItemData.done = false


                val newItemData = this.database.child("todo").push()
                todoItemData.uid = newItemData.key


                newItemData.setValue(todoItemData)


                dialog.dismiss()
                Toast.makeText(this, "Item saved", Toast.LENGTH_LONG).show()


            }
            alertDialog.show()
        }
        todoList = mutableListOf()
        adapter = TodoAdapter(this, todoList!!)
        listViewItem!!.adapter = adapter
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                todoList!!.clear()
                addItemToList(snapshot)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "No item added", Toast.LENGTH_LONG).show()
            }


        })
    }


    private fun addItemToList(snapshot: DataSnapshot) {
        val items = snapshot.children.iterator()
        if (items.hasNext()) {
            val todoIndexedValue = items.next()
            val itemsIterator = todoIndexedValue.children.iterator()

            while (itemsIterator.hasNext()) {
                val currentItem = itemsIterator.next()
                val todoItemData = TodoModel.createList()
                val map = currentItem.value as HashMap<*, *>
                todoItemData.uid = currentItem.key
                todoItemData.done = map["done"] as Boolean?
                todoItemData.itemDataText = map["itemDataText"] as String?
                todoList!!.add(todoItemData)
            }
        }
        adapter.notifyDataSetChanged()
    }


    override fun modifyItem(itemUID: String, isDone: Boolean) {
        val itemReference = database.child("todo").child(itemUID)
        itemReference.child("done").setValue(isDone)
    }


    override fun onItemDelete(itemUID: String) {
        val itemReference = database.child("todo").child(itemUID)
        itemReference.removeValue()
        adapter.notifyDataSetChanged()
    }


}

