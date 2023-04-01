package com.example.recipeapp.entities

import androidx.room.*
import com.example.recipeapp.entities.conventer.CategoryListConventer
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "Category")
data class Category(
    @PrimaryKey(autoGenerate = true)
    var id:Int,

    @ColumnInfo(name = "categoryItems")
    @Expose
    @SerializedName("categories")
    @TypeConverters(CategoryListConventer::class)
    var categorieitems: List<CategoryItems>? = null
)
