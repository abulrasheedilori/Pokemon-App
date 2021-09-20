package com.example.apicall7.get

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.apicall7.R
import com.example.apicall7.model.Result


class PokemonAdapter(private val result: List<Result>, private val context: Context)
    : RecyclerView.Adapter<PokemonAdapter.MyViewHolder>() {

        inner class MyViewHolder(var myViews: View): RecyclerView.ViewHolder(myViews) {
            var pokemonImage: ImageView
            var pokemonName: TextView

            init {
                pokemonImage = myViews.findViewById(R.id.pokemon_image)
                pokemonName = myViews.findViewById(R.id.pokemon_name)
            }

            fun bindingFunction(result: Result){
                pokemonName.text = result.name
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val inflater = LayoutInflater.from(parent.context).inflate(R.layout.pokemon_list_item,parent,false)
            return MyViewHolder(inflater)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val functionResult = SplitImageAddress.splitFunction(result[position].name)
            holder.bindingFunction(result[position])
            /**
             * Implement click function in onBindViewHolder
             */
            holder.myViews.setOnClickListener {
                val intent = Intent(context, PokemonDetail::class.java)
                intent.putExtra("NAME", result[position].name)
                intent.putExtra("IMAGE", functionResult )
                intent.putExtra("IMAGEPOSITION", result[position].name )
                context.startActivity(intent)
            }
            /**
             * Use glide to load images from a network
             */

            Glide
                .with(context)
                .load(functionResult)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade(3000))
                .into(holder.pokemonImage)
        }
        override fun getItemCount(): Int {
            return result.size
        }
    }