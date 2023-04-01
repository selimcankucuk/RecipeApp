package com.example.recipeapp


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.recipeapp.databinding.ActivityDetailBinding
import com.example.recipeapp.entities.MealResponse
import com.example.recipeapp.`interface`.GetDataService
import com.example.recipeapp.retrofitclient.RetrofitClientInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : BaseActivity() {

    var youtubeLink = ""
    private var binding : ActivityDetailBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        var id = intent.getStringExtra("id")

        getSpecificItem(id!!)

        binding?.ibToolbarBack?.setOnClickListener {
            finish()
        }

        binding?.btnYoutube?.setOnClickListener {
            var uri:Uri =
                Uri.parse(youtubeLink)

            val intent = Intent(Intent.ACTION_VIEW,uri)
            startActivity(intent)
        }


    }

    private fun getSpecificItem(id:String) {

        val service = RetrofitClientInstance.retrofitInstance!!.create(GetDataService::class.java)
        val call = service.getSpecificItem(id)

        call.enqueue(object : Callback<MealResponse> {

            override fun onResponse(
                call: Call<MealResponse>,
                response: Response<MealResponse>
            ) {

                if (response.body()!!.mealsEntity.isEmpty()){
                    Toast.makeText(this@DetailActivity,
                        "Detail not found",
                        Toast.LENGTH_SHORT
                    ).show()
                }else{

                    Glide.with(this@DetailActivity).load(response.body()!!.mealsEntity[0].strMealThumb).into(binding?.imageItem!!)

                    binding?.tvCategory?.text = response.body()!!.mealsEntity[0].strMeal


                    var ingredient = "${response.body()!!.mealsEntity[0].strIngredient1}      ${response.body()!!.mealsEntity[0].strMeasure1}\n" +
                            "${response.body()!!.mealsEntity[0].strIngredient2}      ${response.body()!!.mealsEntity[0].strMeasure2}\n" +
                            "${response.body()!!.mealsEntity[0].strIngredient3}      ${response.body()!!.mealsEntity[0].strMeasure3}\n" +
                            "${response.body()!!.mealsEntity[0].strIngredient4}      ${response.body()!!.mealsEntity[0].strMeasure4}\n" +
                            "${response.body()!!.mealsEntity[0].strIngredient5}      ${response.body()!!.mealsEntity[0].strMeasure5}\n" +
                            "${response.body()!!.mealsEntity[0].strIngredient6}      ${response.body()!!.mealsEntity[0].strMeasure6}\n" +
                            "${response.body()!!.mealsEntity[0].strIngredient7}      ${response.body()!!.mealsEntity[0].strMeasure7}\n" +
                            "${response.body()!!.mealsEntity[0].strIngredient8}      ${response.body()!!.mealsEntity[0].strMeasure8}\n" +
                            "${response.body()!!.mealsEntity[0].strIngredient9}      ${response.body()!!.mealsEntity[0].strMeasure9}\n" +
                            "${response.body()!!.mealsEntity[0].strIngredient10}      ${response.body()!!.mealsEntity[0].strMeasure10}\n" +
                            "${response.body()!!.mealsEntity[0].strIngredient11}      ${response.body()!!.mealsEntity[0].strMeasure11}\n" +
                            "${response.body()!!.mealsEntity[0].strIngredient12}      ${response.body()!!.mealsEntity[0].strMeasure12}\n" +
                            "${response.body()!!.mealsEntity[0].strIngredient13}      ${response.body()!!.mealsEntity[0].strMeasure13}\n" +
                            "${response.body()!!.mealsEntity[0].strIngredient14}      ${response.body()!!.mealsEntity[0].strMeasure14}\n" +
                            "${response.body()!!.mealsEntity[0].strIngredient15}      ${response.body()!!.mealsEntity[0].strMeasure15}\n"



                    binding?.tvIngredients?.text = ingredient
                    binding?.tvInstructions?.text = response.body()!!.mealsEntity[0].strInstructions

                    if (response.body()!!.mealsEntity[0].strYoutube != null){
                        youtubeLink = response.body()!!.mealsEntity[0].strYoutube
                    }else{
                        binding?.btnYoutube?.visibility = View.GONE
                    }

                }

            }

            override fun onFailure(call: Call<MealResponse>, t: Throwable) {
                Toast.makeText(
                    this@DetailActivity,
                    "Something went wrong",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}

