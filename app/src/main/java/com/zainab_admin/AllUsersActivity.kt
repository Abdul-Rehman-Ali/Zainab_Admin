package com.zainab_admin

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.zainab_admin.databinding.ActivityAllUsersBinding

class AllUsersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAllUsersBinding
    private lateinit var userAdapter: SeeAllUserAdapter
    private val userList = mutableListOf<SeeAllUserModel>()

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAllUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // RecyclerView setup
        binding.recyclerViewUsers.layoutManager = LinearLayoutManager(this)
        userAdapter = SeeAllUserAdapter(this, userList)
        binding.recyclerViewUsers.adapter = userAdapter

        // Fetch users from Firestore
        fetchUsersFromFirestore()
    }

    private fun fetchUsersFromFirestore() {
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                userList.clear()
                for (document in result) {
                    val name = document.getString("name") ?: ""
                    val email = document.getString("email") ?: ""
                    val phone = document.getString("phone") ?: ""

                    val user = SeeAllUserModel(name, email, phone)
                    userList.add(user)
                }
                userAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e("FirestoreError", "Error fetching users", exception)
            }
    }
}
