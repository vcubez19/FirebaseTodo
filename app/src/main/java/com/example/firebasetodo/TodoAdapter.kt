package com.example.firebasetodo


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView


class TodoAdapter(context: Context, todoList: MutableList<TodoModel>) : BaseAdapter() {


    private val inflator: LayoutInflater = LayoutInflater.from(context)
    private val itemList = todoList
    private var updateAndDelete: UpdateAndDelete = context as UpdateAndDelete


    override fun getCount(): Int {
        return itemList.size
    }


    override fun getItem(p0: Int): Any {
        return itemList[p0]
    }


    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }


    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val uid: String = itemList[p0].uid as String
        val itemTextData: String = itemList[p0].itemDataText as String
        val done: Boolean = itemList[p0].done as Boolean


        val view: View
        val viewHolder: ListViewHolder


        if (p1 == null) {
            view = inflator.inflate(R.layout.row_item_layout, p2, false)
            viewHolder = ListViewHolder(view)
            view.tag = viewHolder
        } else {
            view = p1
            viewHolder = view.tag as ListViewHolder
        }


        viewHolder.textLabel.text = itemTextData
        viewHolder.isDone.isChecked = done
        viewHolder.isDone.setOnClickListener {
            updateAndDelete.modifyItem(uid, !done)
        }
        viewHolder.isDeleted.setOnClickListener {
            updateAndDelete.onItemDelete(uid)
        }


        return view

    }


    private class ListViewHolder(row: View?) {
        val textLabel: TextView = row!!.findViewById(R.id.textView) as TextView
        val isDone: CheckBox = row!!.findViewById(R.id.checkBox) as CheckBox
        val isDeleted: ImageButton = row!!.findViewById(R.id.close) as ImageButton
    }


}

