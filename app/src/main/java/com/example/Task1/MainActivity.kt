package com.example.Task1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fridaybook2.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val button1 : Button = findViewById(R.id.login)
        button1.setOnClickListener {
            val intent = Intent(this@MainActivity, LoginActivity::class.java )
            startActivity(intent)
        }
        val button2 : Button = findViewById(R.id.register)
        button2.setOnClickListener {
            val intent2= Intent(this@MainActivity,RegisterActivity::class.java)
            startActivity(intent2)
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}