package com.example.mangaplusapp

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent;
import android.os.Bundle
import android.view.Window
import android.view.View
import android.widget.Button
import android.view.WindowManager
import android.widget.Toast
import com.example.mangaplusapp.databinding.ActivityLoginBinding
import com.example.mangaplusapp.databinding.ActivityStartBinding
import com.google.firebase.auth.FirebaseAuth

class startActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStartBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar()?.hide();
        setContentView(R.layout.activity_start)

        auth = FirebaseAuth.getInstance()

        val loginBtn = findViewById<Button>(R.id.loginBtn)
        loginBtn.setOnClickListener{
            val Intent = Intent(this, loginActivity::class.java)
            startActivity(Intent)
        }

        val googleBtn = findViewById<Button>(R.id.googleBtn)
        googleBtn.setOnClickListener{
            val Intent = Intent(this, mainActivity::class.java)
            startActivity(Intent)
        }
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) {
            Intent(this, mainActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
            }
        }
    }

}