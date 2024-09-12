package com.example.AllAroundTheBirds.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.AllAroundTheBirds.DataClasses.Feed
import com.example.AllAroundTheBirds.R
import com.squareup.picasso.Picasso

class FeedAdapter(private val data: MutableList<Feed>) :
    RecyclerView.Adapter<FeedAdapter.ViewHolder>() {

    //The following code was taken from stackoverflow
    //Author :  Amitsharma
    //Link: https://stackoverflow.com/questions/32418945/about-recyclerview-viewholder-and-recyclerview-adapter
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val comNameTextView = itemView.findViewById<TextView>(R.id.Birdname)
        val textdateTextView = itemView.findViewById<TextView>(R.id.textdate)
        val locationtextTextView = itemView.findViewById<TextView>(R.id.locationtext)
        val imageView = itemView.findViewById<ImageView>(R.id.BirdimageView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return ViewHolder(itemView)
    }
    //The following code was taken from stackoverflow
    //Author :  Daniel Bastidas
    //Link: https://stackoverflow.com/questions/41156698/loading-images-in-recyclerview-with-picasso-from-api
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        // Bind data to your views
        holder.comNameTextView.text = item.comName
        holder.textdateTextView.text = item.obsDt
        holder.locationtextTextView.text = item.locName
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
    fun updateData(newData: MutableList<Feed>) {
        data.clear() // Clear the existing data
        data.addAll(newData) // Add the new data
        notifyDataSetChanged() // Notify the adapter of the data change
    }
}