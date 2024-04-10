// MainActivity.kt
package com.example.pokeapi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MyAdapter()
        recyclerView.adapter = adapter

        fetchDataFromAPI()
    }

    private fun fetchDataFromAPI() {
        val client = AsyncHttpClient()
        val url = "https://pokeapi.co/api/v2/pokemon"

        client.get(url, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, response: JSONObject?) {
                response?.let {
                    val results = it.getJSONArray("results")
                    val entries = mutableListOf<Entry>()
                    for (i in 0 until results.length()) {
                        val pokemonObject = results.getJSONObject(i)
                        val name = pokemonObject.getString("name")
                        val url = pokemonObject.getString("url")
                        entries.add(Entry(i + 1, name, url))
                    }
                    adapter.setData(entries)
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, throwable: Throwable?, errorResponse: JSONObject?) {
                // Handle failure
            }
        })
    }
}
