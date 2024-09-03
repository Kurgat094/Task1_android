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

class RegisterActivity : AppCompatActivity() {
    private lateinit var emailet: EditText
    private lateinit var passwordet: EditText
    private lateinit var confirmPasswordet: EditText
    private lateinit var createaccountbt: Button
    private lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        emailet = findViewById(R.id.email)
        passwordet = findViewById(R.id.password0)
        confirmPasswordet = findViewById(R.id.password)
        createaccountbt = findViewById(R.id.button)
        progressBar = findViewById(R.id.progressBar)
        createaccountbt.setOnClickListener {
            createAccount()
        }
        val dirlogin: TextView = findViewById(R.id.loginpage)
        dirlogin.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun createAccount() {
        val email = emailet.text.toString().trim()
        val password = passwordet.text.toString().trim()
        val confirm = confirmPasswordet.text.toString().trim()

        val isvalidated = validateData(email, password, confirm)
        if (isvalidated) {
            changeInProgress(true)
            createAccountInFirebase(email, password)
        }
    }
    private fun createAccountInFirebase(email: String, password: String) {
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                changeInProgress(false)
                if (task.isSuccessful) {
                    Toast.makeText(this, "Successfully created account. Check your email for verification.", Toast.LENGTH_SHORT).show()
                    firebaseAuth.currentUser?.sendEmailVerification()
                    FirebaseAuth.getInstance().signOut()
                    finish()


                    var intentmenu = Intent(this@RegisterActivity, HomeActivity::class.java)
                    startActivity(intentmenu)
                } else {
                    Toast.makeText(this, task.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun changeInProgress(inProgress: Boolean) {
        if (inProgress) {
            progressBar.visibility = View.VISIBLE
            createaccountbt.visibility = View.GONE
        } else {
            progressBar.visibility = View.GONE
            createaccountbt.visibility = View.VISIBLE
        }
    }

    private fun validateData(email: String, password: String, confirm: String): Boolean {
        var isValid = true
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailet.error = "Invalid email"
            emailet.requestFocus()
            isValid = false
        }
        if (password.length < 8) {
            passwordet.error = "Password cannot be less than 8"
            passwordet.requestFocus()
            isValid = false
        }
        if (password != confirm) {
            confirmPasswordet.error = "Passwords do not match"
            confirmPasswordet.requestFocus()
            isValid = false
        }
        return isValid
    }
}
