package com.example.lastdataapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView

class ItemAdapter(private val itemList: List<Item>, private val onDeleteClick: (Int) -> Unit):
    RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBoxTask)
        val editText: EditText = itemView.findViewById(R.id.editTextTask)
        val deleteButton: Button = itemView.findViewById(R.id.delButtonTask)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_in_list, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = itemList[position]

        holder.checkBox.isChecked = item.isChecked
        holder.editText.setText(item.taskText)

        holder.checkBox.setOnCheckedChangeListener{
                _, isChecked -> item.isChecked = isChecked
        }

        holder.deleteButton.setOnClickListener{
            onDeleteClick(position)
        }

        holder.editText.setOnFocusChangeListener{
                _, _-> item.taskText = holder.editText.text.toString()
        }
    }

}