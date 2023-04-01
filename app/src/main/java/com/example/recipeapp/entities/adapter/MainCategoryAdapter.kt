package com.example.recipeapp.entities.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.databinding.ItemRvMainCategoryBinding
import com.example.recipeapp.entities.CategoryItems



class MainCategoryAdapter : RecyclerView.Adapter<MainCategoryAdapter.RecipeViewHolder>() {

    private var listener : OnItemClickListener? = null
    private var ctx :Context? = null

    private var arrayMainCategory = ArrayList<CategoryItems>()
    class RecipeViewHolder(val binding: ItemRvMainCategoryBinding) : RecyclerView.ViewHolder(binding.root)


    fun setData(arrData : List<CategoryItems>){
        arrayMainCategory = arrData as ArrayList<CategoryItems>
    }

    fun setClickListener(listener1 : OnItemClickListener){
        listener = listener1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        ctx = parent.context
        return RecipeViewHolder(ItemRvMainCategoryBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return arrayMainCategory.size
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {

        holder.binding.tvDishName.text = arrayMainCategory[position].strcategory
        Glide.with(ctx!!).load(arrayMainCategory[position].strcategorythumb).into(holder.binding.ivDish)
        holder.itemView.rootView.setOnClickListener {
            listener?.onClicked(arrayMainCategory[position].strcategory)
        }
    }

    interface OnItemClickListener{
        fun onClicked(categoryName:String){

        }
    }


}