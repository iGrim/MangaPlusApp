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
import com.google.firebase.auth.FirebaseAuth

class loginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar()?.hide();

        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)

        //Auth

        auth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener{
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            loginUser(email, password)
        }

        binding.regText.setOnClickListener{
            startActivity(Intent(this, registerActivity::class.java))
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                login ->
                if (login.isSuccessful) {
                    Intent(this, mainActivity::class.java).also {
                        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(it)
                        Toast.makeText(this, "Login success!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                        Toast.makeText(this, "Login error!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
}