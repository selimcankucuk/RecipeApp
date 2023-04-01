package com.example.recipeapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.recipeapp.database.RecipeDatabase
import com.example.recipeapp.databinding.ActivitySplashBinding
import com.example.recipeapp.entities.Category
import com.example.recipeapp.entities.Meal
import com.example.recipeapp.entities.MealsItems
import com.example.recipeapp.`interface`.GetDataService
import com.example.recipeapp.retrofitclient.RetrofitClientInstance
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity(),EasyPermissions.PermissionCallbacks,EasyPermissions.RationaleCallbacks {
    private var READ_STORAGE_PER = 123

    private var binding : ActivitySplashBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        readStorageTask()
        binding?.btnGetStarted?.setOnClickListener {
            val intent = Intent(this@SplashActivity,HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    private fun getCategories(){

        val service = RetrofitClientInstance.retrofitInstance!!.create(GetDataService::class.java)
        val call = service.getCategoryList()

        call.enqueue(object : Callback<Category>{

            override fun onResponse(
                call: Call<Category>,
                response: Response<Category>
            ) {
                for (arr in response.body()!!.categorieitems!!){
                    getMeal(arr.strcategory)
                }
                insertDataIntoRoomDb(response.body())
            }


            override fun onFailure(call: Call<Category>, t: Throwable) {
                //binding?.progressBar?.visibility = View.INVISIBLE
                Toast.makeText(this@SplashActivity,
                    "Something went wrong",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }


    private fun getMeal(categoryName:String){

        val service = RetrofitClientInstance.retrofitInstance!!.create(GetDataService::class.java)
        val call = service.getMealList(categoryName)

        call.enqueue(object : Callback<Meal>{

            override fun onResponse(
                call: Call<Meal>,
                response: Response<Meal>
            ) {
                insertMealDataIntoRoomDb(categoryName,response.body())
            }

            override fun onFailure(call: Call<Meal>, t: Throwable) {

                binding?.progressBar?.visibility = View.INVISIBLE
                Toast.makeText(this@SplashActivity,
                    "Something went wrong",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    fun insertDataIntoRoomDb(category : Category?){

        launch {
            this.let {

                for (arr in category!!.categorieitems!!){
                    RecipeDatabase.getDatabase(this@SplashActivity)
                        .recipeDao().insertCategory(arr)
                }
            }
            //binding?.btnGetStarted?.visibility = View.VISIBLE
        }
    }

    /*fun insertMealDataIntoRoomDb(categoryName: String ,meal : Meal?){

        launch {
            this.let {
                for (arr in meal!!.mealsItems!!){
                    val mealItemModel = MealsItems(
                        arr.id,
                        arr.idMeal,
                        arr.categoryName,
                        arr.strMeal,
                        arr.strMealThumb,

                    )
                    RecipeDatabase.getDatabase(this@SplashActivity)
                        .recipeDao().insertMeal(mealItemModel)
                    Log.d("mealData", arr.toString())
                }
            }
            binding?.btnGetStarted?.visibility = View.VISIBLE
        }
    }
    */


    fun insertMealDataIntoRoomDb(categoryName: String, meal: Meal?) {

        launch {
            this.let {


                for (arr in meal!!.mealsItems!!) {
                    var mealItemModel = MealsItems(
                        arr.id,
                        arr.idMeal,
                        categoryName,
                        arr.strMeal,
                        arr.strMealThumb
                    )
                    RecipeDatabase.getDatabase(this@SplashActivity)
                        .recipeDao().insertMeal(mealItemModel)
                    Log.d("mealData", arr.toString())
                }

                binding?.btnGetStarted?.visibility = View.VISIBLE
            }
        }
    }

    private fun clearDataBase(){
        launch {
            this.let {
                RecipeDatabase.getDatabase(this@SplashActivity).recipeDao().clearDb()
            }
            //binding?.btnGetStarted?.visibility = View.VISIBLE
        }
    }

    private fun hasReadStoragePermission():Boolean{
        return EasyPermissions.hasPermissions(this@SplashActivity,android.Manifest.permission.READ_EXTERNAL_STORAGE)
    }


    private fun readStorageTask(){
        if (hasReadStoragePermission()){
            clearDataBase()
            getCategories()
        }else{
            EasyPermissions.requestPermissions(
                this,
                "This app needs access to your storage",
                READ_STORAGE_PER,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {

    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this,perms)){
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    override fun onRationaleAccepted(requestCode: Int) {

    }

    override fun onRationaleDenied(requestCode: Int) {

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}