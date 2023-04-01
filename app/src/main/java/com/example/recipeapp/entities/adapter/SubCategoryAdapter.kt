package com.example.recipeapp.entities.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.databinding.ItemRvSubCategoryBinding
import com.example.recipeapp.entities.Meal
import com.example.recipeapp.entities.MealsItems
import com.example.recipeapp.entities.Recipes


class SubCategoryAdapter : RecyclerView.Adapter<SubCategoryAdapter.RecipeViewHolder>() {
    private var listener : SubCategoryAdapter.OnItemClickListener? = null


    private var ctx : Context? = null
    private var arraySubCategory = ArrayList<MealsItems>()

    class RecipeViewHolder(val binding: ItemRvSubCategoryBinding) : RecyclerView.ViewHolder(binding.root)


    fun setData(arrData : List<MealsItems>){
        arraySubCategory = arrData as ArrayList<MealsItems>
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        ctx = parent.context
        return RecipeViewHolder(ItemRvSubCategoryBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return arraySubCategory.size
    }

    fun setClickListener(listener1 : SubCategoryAdapter.OnItemClickListener){
        listener = listener1
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {

        holder.binding.tvDishName.text = arraySubCategory[position].strMeal
        Glide.with(ctx!!).load(arraySubCategory[position].strMealThumb).into(holder.binding.ivDish)

        holder.itemView.rootView.setOnClickListener {
            listener?.onClicked(arraySubCategory[position].idMeal)
        }

    }

    interface OnItemClickListener{
        fun onClicked(id:String){

        }
    }



}