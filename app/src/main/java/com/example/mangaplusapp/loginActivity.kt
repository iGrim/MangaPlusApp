package com.example.mangaplusapp;

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent;
import android.os.Bundle
import android.view.Window
import android.view.View
import android.widget.Button
import android.view.WindowManager

class loginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar()?.hide();
        setContentView(R.layout.activity_login)
    }
}