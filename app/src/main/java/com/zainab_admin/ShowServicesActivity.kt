package com.zainab_admin

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.zainab_admin.databinding.ActivityShowServicesBinding

class ShowServicesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShowServicesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityShowServicesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 👉 CardView Click Listener
        binding.cardView1.setOnClickListener {
            val intent = Intent(this, ShowAllProductsActivity::class.java)
            startActivity(intent)
        }
    }
}
