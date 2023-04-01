package com.example.recipeapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipeapp.entities.adapter.MainCategoryAdapter
import com.example.recipeapp.entities.adapter.SubCategoryAdapter
import com.example.recipeapp.database.RecipeDatabase
import com.example.recipeapp.databinding.ActivityHomeBinding
import com.example.recipeapp.entities.CategoryItems
import com.example.recipeapp.entities.MealsItems
import kotlinx.coroutines.launch

class HomeActivity : BaseActivity() {

    private var arrMainCategory = ArrayList<CategoryItems>()
    private var arrSubCategory = ArrayList<MealsItems>()

    private var mainCategoryAdapter = MainCategoryAdapter()
    private var subCategoryAdapter = SubCategoryAdapter()

    private var binding : ActivityHomeBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        getDataFromDb()
        mainCategoryAdapter.setClickListener(onClicked)
        subCategoryAdapter.setClickListener(onClickedSubItem)

    }

    private val onClicked = object : MainCategoryAdapter.OnItemClickListener{
        override fun onClicked(categoryName: String) {
            super.onClicked(categoryName)
            getMealDataFromDb(categoryName)
        }
    }

    private val onClickedSubItem = object : SubCategoryAdapter.OnItemClickListener{
        override fun onClicked(id: String) {
            val intent = Intent(this@HomeActivity, DetailActivity::class.java)
            intent.putExtra("id",id)
            startActivity(intent)
        }
    }


    private fun getDataFromDb(){
        launch {
            this.let {
                val cat = RecipeDatabase.getDatabase(this@HomeActivity).recipeDao().getAllCategory()

                arrMainCategory = cat as ArrayList<CategoryItems>
                arrMainCategory.reverse()

                getMealDataFromDb(arrMainCategory[0].strcategory)
                mainCategoryAdapter.setData(arrMainCategory)
                binding?.rvMainCategory?.layoutManager = LinearLayoutManager(this@HomeActivity,LinearLayoutManager.HORIZONTAL,false)
                binding?.rvMainCategory?.adapter = mainCategoryAdapter

            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getMealDataFromDb(categoryName:String){
        binding?.tvCategory?.text = " $categoryName Category "
        launch {
            this.let {
                val cat = RecipeDatabase.getDatabase(this@HomeActivity).recipeDao().getSpecificMealList(categoryName )
                arrSubCategory = cat as ArrayList<MealsItems>
                arrSubCategory.reverse()
                subCategoryAdapter.setData(arrSubCategory)
                binding?.rvSubCategory?.layoutManager = LinearLayoutManager(this@HomeActivity,LinearLayoutManager.HORIZONTAL,false)
                binding?.rvSubCategory?.adapter = subCategoryAdapter

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}