package com.tom.helper

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tom.helper.homefragment.HomeRecyclerAdapter
import com.tom.helper.source.Task

@BindingAdapter("tasks")
fun bindRecyclerView(recyclerView: RecyclerView, homeItems: List<Task>?) {
    homeItems?.let {
        recyclerView.adapter?.apply {
            when (this) {
                is HomeRecyclerAdapter -> submitList(it)
            }
        }
    }
}