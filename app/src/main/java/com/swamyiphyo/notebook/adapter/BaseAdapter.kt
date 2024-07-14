package com.swamyiphyo.notebook.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class BaseAdapter<T>(
    private val layout : Int,
    private var items : List<T>,
    private val isRecyclable : Boolean,
    private val bindView : (position : Int, data : T, view : View) -> Unit
) : RecyclerView.Adapter<BaseAdapter<T>.BaseViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =
        BaseViewHolder(LayoutInflater.from(parent.context).inflate(layout, parent, false))

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(position, items[position])
        holder.setIsRecyclable(isRecyclable)
    }
    fun submitList(notes: List<T>) {
        items = notes
        notifyDataSetChanged()
    }
    inner class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(position: Int, data : T) = bindView.invoke(position, data, itemView)
    }
}