package com.example.mangaplusapp;

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent;
import android.os.Bundle
import android.view.Window
import android.view.View
import android.widget.Button
import android.view.WindowManager
import com.example.mangaplusapp.databinding.ActivityLoginBinding
import com.example.mangaplusapp.databinding.ActivityRegisterBinding

class registerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar()?.hide();

        binding = ActivityRegisterBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.btnRegister.setOnClickListener{
            startActivity(Intent(this, mainActivity::class.java))
        }

        binding.logText.setOnClickListener{
            startActivity(Intent(this, loginActivity::class.java))
        }
    }
}