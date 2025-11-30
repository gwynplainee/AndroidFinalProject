package com.example.androidfinal.core.ui.adapter

import android.graphics.Color
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidfinal.R

class CategoryAdapter(private val categories: List<String>, private val onCategoryClick: (String) -> Unit) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>(){
    private var selectedPosition = 0
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryAdapter.CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category,parent,false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: CategoryAdapter.CategoryViewHolder,
        position: Int
    ) {
        holder.bind(categories[position],position)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val categoryText: TextView = itemView.findViewById(R.id.category_text)
        val categoryCard: CardView = itemView.findViewById(R.id.category_card)

        fun bind(category: String, position: Int){
            categoryText.text = category

            if(selectedPosition == position){
                categoryCard.setCardBackgroundColor(Color.DKGRAY)
                categoryText.setTextColor(Color.WHITE)

            }else{
                categoryCard.setCardBackgroundColor(Color.WHITE)
                categoryCard.setCardBackgroundColor(Color.DKGRAY)
            }

            itemView.setOnClickListener {
                val previousPosition = selectedPosition
                selectedPosition =bindingAdapterPosition

                notifyItemChanged(previousPosition)
                notifyItemChanged(selectedPosition)
                onCategoryClick(category)
            }
        }
    }

}