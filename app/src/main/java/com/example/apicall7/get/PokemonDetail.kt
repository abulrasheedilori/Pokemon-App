package com.example.apicall7.get

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.apipokemonproject.abilitiesApi.PokemonAbilities
import com.bumptech.glide.Glide
import com.example.apicall7.R
import com.example.apicall7.post.UploadImage
import com.google.android.material.floatingactionbutton.FloatingActionButton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PokemonDetail : AppCompatActivity() {

/**
 * declare base URL to access data in the second activity
 */
val DETAILSBASEURL = "https://pokeapi.co/api/v2/"
    /**
     * Create instance of your views
     */
    private lateinit var imageView: ImageView
    private lateinit var name: TextView
    private lateinit var id: TextView
    private lateinit var height:TextView
    private lateinit var weight:TextView
    private lateinit var abilities:TextView
    private lateinit var backBtn:ImageView
    private lateinit var uploadLink:FloatingActionButton



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon_detail)

        /**
         * Grab views
         */

        imageView = findViewById(R.id.imageView)
        name = findViewById(R.id.textView3)
        id = findViewById(R.id.textView5)
        height = findViewById(R.id.textView7)
        weight = findViewById(R.id.textView9)
        abilities = findViewById(R.id.textView11)
        backBtn = findViewById(R.id.backButton)
        uploadLink = findViewById(R.id.floatingActionButton)

        backBtn.setOnClickListener{
            onBackPressed()
        }
        uploadLink.setOnClickListener {
            val intent = Intent(this@PokemonDetail, UploadImage::class.java)
            startActivity(intent)
        }


        /**
         * grab intent passed from main activity
         */

        val putName = intent.getStringExtra("NAME").toString()
        val getURL = intent.getStringExtra("IMAGEPOSITION")

        val functionResult = getURL?.let { SplitImageAddress.splitFunction(it) }

        /**
         * Glide supports fetching, of images from any network
         */

        Glide
            .with(this)
            .load(functionResult)
            .fitCenter()
            //.placeholder(R.drawable.ic_launcher_background)
            .into(imageView)
        name.text = putName

        /**
         * Call functionGetDataInDetailsActivity
         */

        functionGetDataInDetailsActivity(putName)
    }

    /**
     * Create a function that takes advantage of retrofit to move to a new thread that now helps in making
     * network calls to the pokemon network
     */

    private fun functionGetDataInDetailsActivity(putName:String) {
        /**
         * Create a logging interceptor, so you could access the JSON file in the run
         */
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

        /**
         * Create a retrofit builder that helps you access a REST API so you could get data from it
         */
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(DETAILSBASEURL)
            .client(client)
            .build()
            .create(AbilityAPInterface::class.java)

        Log.d("DetailsActivity", SplitNameAddress.splitName(putName))

        val getDataFromRetrofitBuilderInDetailsActivity = retrofit.getData(putName)

        getDataFromRetrofitBuilderInDetailsActivity.enqueue(object :
            retrofit2.Callback<PokemonAbilities> {
            override fun onResponse(
                call: Call<PokemonAbilities>,
                response: Response<PokemonAbilities>
            ) {
                val responseInDetailsActivity = response.body()!!
                Log.d("TAG", "onResponse: ${responseInDetailsActivity.abilities.size}")

                val attributeArrayList = responseInDetailsActivity.abilities[0].ability.name
                val getName = responseInDetailsActivity.name
                val getHeight = responseInDetailsActivity.height
                val getWeight = responseInDetailsActivity.weight
                val getId = responseInDetailsActivity.id

                abilities.text = attributeArrayList
                name.text = getName
                height.text = getHeight.toString()
                weight.text = getWeight.toString()
                id.text = getId.toString()
            }

            override fun onFailure(call: Call<PokemonAbilities>, t: Throwable) {
                Log.d("TAG", "onFailure ${t.message}")
            }

        })

    }
}