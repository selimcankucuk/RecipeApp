package com.example.recipeapp.entities.conventer

import androidx.room.Entity
import androidx.room.TypeConverter
import com.example.recipeapp.entities.CategoryItems
import com.example.recipeapp.entities.MealsItems
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MealListConventer {
    @TypeConverter
    fun fromCategoryList(category :List<MealsItems>):String?{
        if (category == null){
            return null
        }else{
            val gson = Gson()
            val type = object : TypeToken<MealsItems>(){

            }.type
            return gson.toJson(category,type)
        }
    }


    @TypeConverter
    fun toCategoryList(categoryString : String) :List<MealsItems>?{
        if (categoryString == null){
            return null
        }else{
            val gson = Gson()
            val type = object : TypeToken<MealsItems>(){

            }.type
            return gson.fromJson(categoryString,type)
        }
    }
}