package com.example.AllAroundTheBirds.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.AllAroundTheBirds.DataClasses.Challenges
import com.example.AllAroundTheBirds.R

class CompletedAdapter(private val completedList: MutableList<Challenges>) : RecyclerView.Adapter<CompletedAdapter.CompletedViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompletedViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.challenge_achieved_item, parent, false)
        return CompletedViewHolder(view)
    }

    override fun onBindViewHolder(holder: CompletedViewHolder, position: Int) {
        val challenge = completedList[position]
        if (challenge.type == "Picture") {
            holder.challengeTitle.text =
                "Challenge: ${challenge.challenge}${challenge.birdname}"
        }
        if (challenge.type == "Sightings") {
            holder.challengeTitle.text ="Challenge: ${challenge.challenge}"
        }
        when (challenge.medal) {
            "Gold" -> holder.Trophy.setImageResource(R.drawable.gold)
            "Silver" -> holder.Trophy.setImageResource(R.drawable.silver)
            "Bronze" -> holder.Trophy.setImageResource(R.drawable.bronze)
//          else -> holder.Trophy.setImageResource(0) // Set default image or hide the ImageView
        }
    }

    override fun getItemCount(): Int {
        return completedList.size
    }
    fun updateAchieved(newAchievedList: MutableList<Challenges>) {
        completedList.clear()
        completedList.addAll(newAchievedList)
        notifyDataSetChanged()
    }
    class CompletedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val challengeTitle: TextView = itemView.findViewById(R.id.challengeTitle)
        val Trophy: ImageView = itemView.findViewById(R.id.Trophy)
    }
}
