package com.example.mangaplusapp;

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent;
import android.os.Bundle
import android.view.Window
import android.view.View
import android.widget.Button
import android.view.WindowManager
import com.example.mangaplusapp.databinding.ActivityLoginBinding

class loginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar()?.hide();

        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.btnLogin.setOnClickListener{
            startActivity(Intent(this, mainActivity::class.java))
        }

        binding.regText.setOnClickListener{
            startActivity(Intent(this, registerActivity::class.java))
        }
    }
}