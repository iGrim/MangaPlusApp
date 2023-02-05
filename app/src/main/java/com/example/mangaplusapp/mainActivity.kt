package com.example.mangaplusapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mangaplusapp.databinding.ActivityLoginBinding
import com.example.mangaplusapp.databinding.ActivityMainBinding
import com.example.mangaplusapp.service.AnimeService
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class mainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar()?.hide();
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnLogout.setOnClickListener {
            auth.signOut()
            Intent(this, loginActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
                Toast.makeText(this, "Logout success!", Toast.LENGTH_SHORT).show()
            }
        }

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

