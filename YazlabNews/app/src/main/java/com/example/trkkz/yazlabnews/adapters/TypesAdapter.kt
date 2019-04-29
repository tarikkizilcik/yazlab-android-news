package com.example.trkkz.yazlabnews.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import com.example.trkkz.yazlabnews.R

class TypesAdapter(private val dataSet: List<String>) : RecyclerView.Adapter<TypesAdapter.ViewHolder>() {
    private lateinit var onItemClickListener: OnCheckBoxClickListener

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val inflater = LayoutInflater.from(p0.context)
        val listItem = inflater.inflate(R.layout.list_item_types, p0, false)

        return ViewHolder(listItem)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val type = dataSet[p1]

        p0.bind(type)
    }

    fun setOnItemClickListener(onItemClickListener: OnCheckBoxClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        private val checkBox: CheckBox = view.findViewById(R.id.checkBox)

        fun bind(type: String) {
            checkBox.text = type

            checkBox.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if (::onItemClickListener.isInitialized)
                onItemClickListener.onCheckBoxClick(adapterPosition, view)
        }
    }
}