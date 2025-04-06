package com.zainab_admin

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.FirebaseApp
import com.zainab_admin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set up click listener for cardView1
        binding.cardView1.setOnClickListener {
            // Create an Intent to start the AllUsersActivity
            val intent = Intent(this, AllUsersActivity::class.java)
            startActivity(intent)
        }

        binding.cardView3.setOnClickListener {
            // Create an Intent to start the AllUsersActivity
            val intent = Intent(this, AddServiceActivity::class.java)
            startActivity(intent)
        }

        binding.cardView4.setOnClickListener {
            // Create an Intent to start the AllUsersActivity
            val intent = Intent(this, ShowServicesActivity::class.java)
            startActivity(intent)
    }
}
}
