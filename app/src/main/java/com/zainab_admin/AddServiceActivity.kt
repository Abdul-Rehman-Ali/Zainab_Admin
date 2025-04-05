package com.zainab_admin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.zainab_admin.databinding.ActivityAddServiceBinding
import android.net.Uri
import com.google.firebase.storage.UploadTask
import androidx.activity.result.contract.ActivityResultContracts

class AddServiceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddServiceBinding
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private var selectedCategory = ""
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // View Binding
        binding = ActivityAddServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Window Insets for edge-to-edge
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Spinner setup for categories
        val categories = listOf("Cleaning", "AC Services", "Plumbing", "Painting")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categorySpinner.adapter = adapter

        // Set spinner selection listener
        binding.categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedCategory = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle the case where no item is selected if necessary
            }
        }

        // Image upload button click listener
        binding.btnUploadImage.setOnClickListener {
            // Open image picker
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            imageResultLauncher.launch(intent)
        }

        // Add Service button click listener
        binding.btnAddService.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val price = binding.etPrice.text.toString()
            val rating = binding.etRating.text.toString()

            if (title.isEmpty() || price.isEmpty() || rating.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                // If image selected, upload to Firebase Storage
                if (imageUri != null) {
                    uploadImageToStorage(title, price, rating)
                } else {
                    storeServiceData(title, price, rating, null)
                }
            }
        }
    }

    // Image picker result launcher
    private val imageResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            imageUri = result.data?.data
            binding.btnUploadImage.text = "Image Selected"
        }
    }

    // Function to upload image to Firebase Storage
    private fun uploadImageToStorage(title: String, price: String, rating: String) {
        val fileName = "${System.currentTimeMillis()}.jpg"  // You can change the file name logic
        val storageRef: StorageReference = storage.reference.child("$selectedCategory/$fileName")

        imageUri?.let { uri ->
            val uploadTask: UploadTask = storageRef.putFile(uri)
            uploadTask.addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()
                    storeServiceData(title, price, rating, imageUrl)
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Function to store service data in Firestore
    private fun storeServiceData(title: String, price: String, rating: String, imageUrl: String?) {
        val serviceData = hashMapOf(
            "Title" to title,
            "Price" to price,
            "Rating" to rating,
            "Image" to (imageUrl ?: "") // If no image URL, store an empty string
        )

        // Store data in Firestore under the selected category
        firestore.collection(selectedCategory)
            .add(serviceData)
            .addOnSuccessListener {
                Toast.makeText(this, "Service added successfully", Toast.LENGTH_SHORT).show()
                finish() // Close the activity after successful addition
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
