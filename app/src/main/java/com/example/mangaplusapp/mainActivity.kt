package com.example.mangaplusapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mangaplusapp.databinding.ActivityMainBinding
import com.example.mangaplusapp.service.AnimeService
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class mainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            val animeService = AnimeService.create()
            val call = animeService.getTopAnimes()

            call.enqueue(object : Callback<TopAnime> {

                override fun onResponse(call: Call<TopAnime>, response: Response<TopAnime>) {
                    if(response.body() != null){
                        val data = response.body()!!.data
                        animeRecyclerView.adapter = AnimeAdapter(this@mainActivity, data)
                        animeRecyclerView.layoutManager = GridLayoutManager(this@mainActivity, 3)
                        /*try {

                        } catch (e: NullPointerException) {
                            e.printStackTrace();
                        }--*/
                    }
                }
                override fun onFailure(call: Call<TopAnime>, t: Throwable) {
                }
            })

            btnSearch.setOnClickListener{
                val searchedAnime = searchInputEditText.text.toString()
                val callSearchedAnime = animeService.getSearchedAnime(searchedAnime)

                callSearchedAnime.enqueue(object : Callback<SearchedAnime> {
                    override fun onResponse(
                        call: Call<SearchedAnime>,
                        response: Response<SearchedAnime>
                    ) {
                        if(response.body() != null){
                            val searchedAnime = response.body()!!.data
                            animeRecyclerView.adapter = AnimeAdapter(this@mainActivity, searchedAnime)
                            animeRecyclerView.layoutManager = GridLayoutManager(this@mainActivity, 3)
                        }
                    }

                    override fun onFailure(call: Call<SearchedAnime>, t: Throwable) {

                    }
                }
                )
            }
        }
    }

    class AnimeAdapter(
        private val parentActivity: AppCompatActivity,
        private val animes: List<Result>,

    ): RecyclerView.Adapter<AnimeAdapter.CustomViewHolder>() {
        inner class CustomViewHolder(view: View): RecyclerView.ViewHolder(view)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.anime_item_layout, parent,false)
            return CustomViewHolder(view)
        }

        override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
            val anime = animes[position]
            val view = holder.itemView

            val name = view.findViewById<TextView>(R.id.name)
            val image = view.findViewById<ImageView>(R.id.image)

            name.text = anime.title
            Picasso.get().load(anime.images.jpg.imageUrl).into(image)

            view.setOnClickListener {
                AnimeDetailsBottomSheet(anime).apply {
                    show(parentActivity.supportFragmentManager,"AnimeDetailsBottomSheet")
                }
            }
        }

        override fun getItemCount(): Int {
            return animes.size
        }
    }
}

