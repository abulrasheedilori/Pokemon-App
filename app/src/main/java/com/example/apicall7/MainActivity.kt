package com.example.apicall7

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apicall7.connectivity.ConnectivityLiveData
import com.example.apicall7.get.PokemonAdapter
import com.example.apicall7.model.Pokemon
import com.example.apicall7.network.NetworkClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val BASE_URL = "https://pokeapi.co/api/v2/"

class MainActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: PokemonAdapter

    //limit and offset
    var limit:Int = 100
    var offset = 0

    //creating an instance of connectivity liveData
    private lateinit var connectivityLiveData: ConnectivityLiveData
    private lateinit var progressBar : ProgressBar
    private lateinit var errorStatus : TextView
    private lateinit var search: EditText
    private lateinit var searchPokemon: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //1 - initializing connectivity liveData
        connectivityLiveData = ConnectivityLiveData(application)

        progressBar = findViewById(R.id.progress_Bar)
        errorStatus = findViewById(R.id.error_Status)
        search = findViewById(R.id.searchView)
        searchPokemon = findViewById(R.id.searchPokemon)



        //add observers in initialize observers
        //2
        connectivityLiveData.observe(this, Observer { isAvailable ->
            //2
            when (isAvailable) {
                true -> {
                    //4
                    errorStatus.visibility = View.GONE
                    progressBar.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE

                    getData(limit , offset)
                }
                false -> {
                    //5
                    errorStatus.visibility = View.VISIBLE
                    progressBar.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                }
            }
        })

        //setOnClickListener to search button
        searchPokemon.setOnClickListener(){

            if(search.text.isNotEmpty()){
                limit = search.text.toString().toInt()

            }

            getData(limit, offset)
        }

        //calling the recyclerview object and setting some layout
        recyclerView = findViewById(R.id.pokemon_recyclerview)
        recyclerView.layoutManager = GridLayoutManager(this,2)
        recyclerView.setHasFixedSize(true)

    }

    //function to get response from the api using retrofit
    private fun getData(limit: Int, offset:Int) {
        val getDataFromRetrofitBuilder = NetworkClient.getMyApi().getProperties(limit, offset)

        //onResponse override function
        getDataFromRetrofitBuilder.enqueue(object : Callback<Pokemon> {
            override fun onResponse(call: Call<Pokemon>, response: Response<Pokemon>) {
                val responseBody = response.body()
                if (responseBody != null) {
                    val arrayList = responseBody.results
                    adapter = PokemonAdapter(arrayList,this@MainActivity)
                    recyclerView.adapter = adapter
                }
            }
            //onFailure override to handle response when it fails
            override fun onFailure(call: Call<Pokemon>, t: Throwable) {
                Log.d("TAG" ,"onFailure ${t.message}")
            }
        })
    }
}
