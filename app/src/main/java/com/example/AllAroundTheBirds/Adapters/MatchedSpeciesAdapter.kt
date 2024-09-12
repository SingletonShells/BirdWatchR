package com.example.AllAroundTheBirds.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.AllAroundTheBirds.DataClasses.MatchedSpecies
import com.example.AllAroundTheBirds.R
import com.squareup.picasso.Picasso

class MatchedSpeciesAdapter(private val data: MutableList<MatchedSpecies>) :
    RecyclerView.Adapter<MatchedSpeciesAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val comNameTextView = itemView.findViewById<TextView>(R.id.ComName1)
        val sciNameTextView = itemView.findViewById<TextView>(R.id.SciName1)
        val categoryTextView = itemView.findViewById<TextView>(R.id.Category1)
        val imageView = itemView.findViewById<ImageView>(R.id.BirdimageView1)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.findspecies_item, parent, false)
        return ViewHolder(itemView)
    }
    //The following code was taken from developers android
    //Author :  Android Dev
    //Link: https://developer.android.com/reference/kotlin/android/util/Log
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]

        // Bind data to your views
        holder.comNameTextView.text = item.comName
        holder.sciNameTextView.text = item.sciName
        holder.categoryTextView.text = item.category
        try {
            if (!item.picture.isNullOrBlank()&&!item.picture!!.isEmpty()) {
                Picasso.get().load(item.picture)
                    .resize(200, 200)
                    .centerCrop()
                    .into(holder.imageView)
                holder.imageView.visibility = View.VISIBLE
            }else {
                // If item.picture is null or empty, load a default image
                holder.imageView.visibility = View.GONE
            }

        }
        catch (e: Exception) {
            Log.d("Picasso", "error: ${e}")
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }
    fun updateData(newData: MutableList<MatchedSpecies>) {
        data.clear() // Clear the existing data
        data.addAll(newData) // Add the new data
        notifyDataSetChanged() // Notify the adapter of the data change
    }
}