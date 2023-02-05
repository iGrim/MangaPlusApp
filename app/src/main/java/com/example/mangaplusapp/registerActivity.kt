package com.example.mangaplusapp;

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent;
import android.os.Bundle
import android.view.Window
import android.view.View
import android.widget.Button
import android.view.WindowManager
import android.widget.Toast
import com.example.mangaplusapp.databinding.ActivityLoginBinding
import com.example.mangaplusapp.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class registerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar()?.hide();

        binding = ActivityRegisterBinding.inflate(layoutInflater)

        setContentView(binding.root)

        // Auth

        auth = FirebaseAuth.getInstance()

        binding.btnRegister.setOnClickListener{
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            registerUser(email, password)
        }

        binding.logText.setOnClickListener{
            startActivity(Intent(this, loginActivity::class.java))
        }
    }

    private fun registerUser(email: String, password: String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    startActivity(Intent(this, loginActivity::class.java))
                    Toast.makeText(this, "Register success!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Register error!", Toast.LENGTH_SHORT).show()
                }
            }
    }
}