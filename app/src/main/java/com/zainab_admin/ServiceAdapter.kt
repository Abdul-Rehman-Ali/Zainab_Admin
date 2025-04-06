package com.zainab_admin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class ServiceAdapter(
    private val context: Context,
    private val itemList: MutableList<ServiceModel>  // Make the list mutable
) : RecyclerView.Adapter<ServiceAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val detail: TextView = itemView.findViewById(R.id.detail)
        val price: TextView = itemView.findViewById(R.id.price)
        val rating: TextView = itemView.findViewById(R.id.rating)
        val btnDelete: ImageView = itemView.findViewById(R.id.delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.rv_show_services, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.detail.text = item.title
        holder.price.text = item.price
        holder.rating.text = item.rating
        holder.btnDelete.setOnClickListener {
            showDeleteConfirmationDialog(item, position)
        }
    }

    override fun getItemCount(): Int = itemList.size

    private fun showDeleteConfirmationDialog(item: ServiceModel, position: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Delete Item")
        builder.setMessage("Are you sure you want to delete this item?")
        builder.setPositiveButton("Yes") { dialog, _ ->
            // Delete the item from Firestore
            deleteItemFromFirestore(item, position)
            dialog.dismiss()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()  // Dismiss the dialog if "No" is clicked
        }
        builder.show()
    }

    private fun deleteItemFromFirestore(item: ServiceModel, position: Int) {
        // Get a reference to Firestore
        val db = FirebaseFirestore.getInstance()

        // Assuming each service document has a unique ID (e.g., document ID in Firestore)
        val docId = item.id  // Use the actual document ID field here

        db.collection("Cleaning").document(docId)  // Make sure to use the correct collection and document ID
            .delete()
            .addOnSuccessListener {
                // Remove the item from the list and notify the adapter
                itemList.removeAt(position)  // Remove from the mutable list
                notifyItemRemoved(position)  // Notify adapter of removal

                Toast.makeText(context, "Item deleted successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error deleting item: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
