package com.example.pokeapi

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var pokemonImage: ImageView
    private lateinit var pokemonName: TextView
    private lateinit var pokemonType: TextView
    private lateinit var loadButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pokemonImage = findViewById(R.id.pokemonImage)
        pokemonName = findViewById(R.id.pokemonName)
        pokemonType = findViewById(R.id.pokemonType)
        loadButton = findViewById(R.id.loadButton)

        loadButton.setOnClickListener {
            fetchPokemon()
        }
    }

    private fun fetchPokemon() {
        val client = AsyncHttpClient()
        val url = "https://pokeapi.co/api/v2/pokemon/1" // You can change the ID to get different Pokémon

        client.get(url, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, response: JSONObject?) {
                response?.let {
                    val name = it.getString("name")
                    val types = it.getJSONArray("types")
                    val type1 = types.getJSONObject(0).getJSONObject("type").getString("name")
                    val imageUrl = it.getJSONObject("sprites").getString("front_default")

                    // Update UI
                    runOnUiThread {
                        pokemonName.text = "Name: $name"
                        pokemonType.text = "Type: $type1"
                        Glide.with(this@MainActivity)
                            .load(imageUrl)
                            .centerCrop()
                            .into(pokemonImage)
                    }
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, throwable: Throwable?, errorResponse: JSONObject?) {
                // Handle failure
            }
        })
    }
}
