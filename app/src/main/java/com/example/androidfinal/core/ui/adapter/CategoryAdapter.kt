package com.example.androidfinal.core.ui.adapter

import android.graphics.Color
import android.graphics.Typeface
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.view.menu.MenuView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidfinal.R
import com.google.android.material.card.MaterialCardView

class CategoryAdapter(private val categories: List<String>, private var selectedCategory: String, private var isSourceSelected: Boolean, private val onCategoryClick: (String) -> Unit) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category,parent,false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: CategoryViewHolder,
        position: Int
    ) {
        holder.bind(categories[position])
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    fun updateSourceState(sourceSelected: Boolean) {
        this.isSourceSelected = sourceSelected
        notifyDataSetChanged()
    }

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val categoryText: TextView = itemView.findViewById(R.id.category_text)
        val categoryCard: CardView = itemView.findViewById(R.id.category_card)

        fun bind(category: String){
            categoryText.text = category
            val cardView = itemView.findViewById<MaterialCardView>(R.id.category_card)

            if (isSourceSelected){
                cardView.setCardBackgroundColor(Color.parseColor("#FFCDD2"))
                categoryText.setTextColor(Color.RED)
                cardView.strokeColor = Color.RED
            }else{
                cardView.setCardBackgroundColor(Color.WHITE)
                if(category.equals(selectedCategory, ignoreCase = true)){
                    categoryCard.setCardBackgroundColor(Color.DKGRAY)
                    categoryText.setTextColor(Color.WHITE)
                    categoryText.setTypeface(null, Typeface.BOLD)
                    cardView.strokeColor = Color.GRAY

                }else{
                    categoryCard.setCardBackgroundColor(Color.WHITE)
                    categoryText.setTextColor(Color.DKGRAY)
                    categoryText.setTypeface(null, Typeface.NORMAL)
                    cardView.strokeColor = Color.DKGRAY
                    cardView.strokeWidth = 4


                }
            }


            itemView.setOnClickListener {
                if (!isSourceSelected) {
                    selectedCategory = category
                    notifyDataSetChanged()
                    onCategoryClick(category)
                } else {
                    Toast.makeText(itemView.context, "Category is disabled when a specific Source is selected", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }



}