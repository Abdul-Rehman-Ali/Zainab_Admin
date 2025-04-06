package com.zainab_admin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.zainab_admin.databinding.ActivityShowAllProductsBinding

class ShowAllProductsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShowAllProductsBinding
    private lateinit var adapter: ServiceAdapter
    private val serviceList = ArrayList<ServiceModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowAllProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize the adapter with empty serviceList
        adapter = ServiceAdapter(this, serviceList)
        binding.recyclerView.adapter = adapter

        // Fetch data from Firestore
        fetchDataFromFirestore()
    }

    private fun fetchDataFromFirestore() {
        val db = FirebaseFirestore.getInstance()

        db.collection("Cleaning") // Ensure the collection name is correct
            .get()
            .addOnSuccessListener { result ->
                // Clear the existing list before adding new data
                serviceList.clear()

                // Loop through Firestore documents and add data to serviceList
                for (document in result) {
                    val id = document.id
                    val title = document.getString("Title") ?: ""
                    val price = document.getString("Price") ?: ""
                    val rating = document.getString("Rating") ?: ""

                    // Add ServiceModel objects to serviceList
                    serviceList.add(ServiceModel(id, title, price, rating))
                }

                // Notify the adapter that the data has changed
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                // Display a Toast message if there is an error fetching data
                Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
