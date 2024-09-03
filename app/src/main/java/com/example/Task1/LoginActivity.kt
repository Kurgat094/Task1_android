package com.example.Task1

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fridaybook2.R
import com.google.firebase.auth.FirebaseAuth
class LoginActivity : AppCompatActivity() {
    private lateinit var inputemail: EditText
    private lateinit var inputpassword: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var login_button : Button

    private fun validateData(email: String, password: String): Boolean {
        return when {
            !Patterns.EMAIL_ADDRESS.matcher(email).matches()->
            {
                inputemail.error = "invalid email"
                false
            }
            password.length < 6 ->{
                inputpassword.error = "password is too short"
                false
            }
            else -> true
        }


    }

    private fun loginAccountInFirebase(email: String, password: String) {
        val firebaseAuth = FirebaseAuth.getInstance()
        changeInProgress(true)
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                changeInProgress(false)
                if (task.isSuccessful) {
                    // Login success
                    val currentUser = firebaseAuth.currentUser
                    if (currentUser != null && currentUser.isEmailVerified) {
                        // Go to MainActivity
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish() // Close the LoginActivity
                    } else {
                        Toast.makeText(this, "Email not verified", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Login failed
                    Toast.makeText(this, task.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun changeInProgress(inProgress: Boolean) {
        if(inProgress){
            progressBar.visibility = View.VISIBLE
            login_button.visibility = View.GONE

        }
        else{
            progressBar.visibility = View.GONE
            login_button.visibility = View.VISIBLE
        }

    }

    private fun loginUser() {
        val email = inputemail.text.toString().trim()
        val password = inputpassword.text.toString().trim()
        progressBar = findViewById(R.id.progressBar)

        if(validateData(email, password)){
                loginAccountInFirebase(email, password)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        inputemail =  findViewById(R.id.email)
        inputpassword = findViewById(R.id.password0)
        login_button = findViewById(R.id.button)

        login_button.setOnClickListener {
            loginUser()
//            var intent= Intent(this@LoginActivity,Home::class.java )
//            startActivity(intent)
        }



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val register: TextView = findViewById(R.id.registerPage)
        register.setOnClickListener {
            var openReg = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(openReg)
        }
    }
}