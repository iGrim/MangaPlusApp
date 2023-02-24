package com.example.mangaplusapp

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.net.InetAddresses
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mangaplusapp.databinding.ActivityLoginBinding
import com.example.mangaplusapp.databinding.ActivityMainBinding
import com.example.mangaplusapp.model.UserModel
import com.example.mangaplusapp.service.AnimeService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Url
import java.util.*
import android.os.Build.VERSION_CODES
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import okhttp3.internal.notify


class mainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage
    private lateinit var selectedImg: Uri
    private lateinit var dialog: AlertDialog.Builder

    val CHANNEL_ID = "channelId"
    val CHANNEL_NAME = "channelName"
    val NOTIF_ID=0



    @RequiresApi(VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        getSupportActionBar()?.hide();
        setContentView(binding.root)

        createNotifChannel()

        val intent=Intent(this, mainActivity::class.java)
        val pendingIntent = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_MUTABLE)
        }

        val notif = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Profile image changed!")
            .setContentText("New image from gallery uploaded.")
            .setSmallIcon(R.drawable.android_small___1)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .build()

        val notifManager = NotificationManagerCompat.from(this)



        auth = FirebaseAuth.getInstance()

        dialog = AlertDialog.Builder(this)
            .setMessage("Updating profile...")
            .setCancelable(false)

        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()

        binding.imageAvatar.setOnClickListener {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }

        binding.btnOk.setOnClickListener{
            uploadData()
            notifManager.notify(NOTIF_ID, notif)
        }



        val currentUser = auth.currentUser
        val email = currentUser?.email

        val textView = findViewById<TextView>(R.id.userhello)
        textView.text = "Hello, " + email?.substringBefore("@")





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

    @RequiresApi(VERSION_CODES.O)
    private fun createNotifChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) run {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                lightColor = Color.BLUE
                enableLights(true)
            }
            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    private fun uploadData(){
        val reference = storage.reference.child("Profile").child(Date().time.toString())
        reference.putFile(selectedImg).addOnCompleteListener{
            if(it.isSuccessful){
                reference.downloadUrl.addOnSuccessListener { task ->
                    uploadInfo(task.toString())
                }
            }
        }
    }

    private fun uploadInfo(imgUrl: String) {
        val user = UserModel(auth.uid.toString(), imgUrl)

        database.reference.child("users")
            .child(auth.uid.toString())
            .setValue(user)
            .addOnSuccessListener {
                Toast.makeText(this, "Data inserted", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, mainActivity::class.java))
                finish()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(data != null){
            if (data.data != null){
                selectedImg = data.data!!
                binding = ActivityMainBinding.inflate(layoutInflater)

                binding.imageAvatar.setImageURI(selectedImg)
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

