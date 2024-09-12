package com.example.AllAroundTheBirds.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.AllAroundTheBirds.DataClasses.ChallengesProg
import com.example.AllAroundTheBirds.R

class ChallengesAdapter(private val challengesList: MutableList<ChallengesProg>) : RecyclerView.Adapter<ChallengesAdapter.ChallengeViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.challenge_item, parent, false)
        return ChallengeViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChallengeViewHolder, position: Int) {
        val challenge = challengesList[position]
        if (challenge.type == "Picture") {
            holder.challengeTitle.text = "Challenge: ${challenge.challenge}${challenge.birdname}"
            holder.endval.text = "${challenge.amount}"
            when (challenge.medal) {
                "Gold" -> holder.Trophy.setImageResource(R.drawable.gold)
                "Silver" -> holder.Trophy.setImageResource(R.drawable.silver)
                "Bronze" -> holder.Trophy.setImageResource(R.drawable.bronze)
                // Add additional cases as needed
                else -> holder.Trophy.setImageResource(0) // Set default image or hide the ImageView
            }
        }
        if (challenge.type == "Sightings") {
            holder.challengeTitle.text = "Challenge: ${challenge.challenge}"
            holder.endval.text = "${challenge.amount}"
            when (challenge.medal) {
                "Gold" -> holder.Trophy.setImageResource(R.drawable.gold)
                "Silver" -> holder.Trophy.setImageResource(R.drawable.silver)
                "Bronze" -> holder.Trophy.setImageResource(R.drawable.bronze)
                // Add additional cases as needed
                else -> holder.Trophy.setImageResource(0) // Set default image or hide the ImageView
            }
        }
        holder.ChallengeProgress.max = challenge.amount?.toInt()!!
        holder.ChallengeProgress.progress = challenge.progress?.toInt()!!
        holder.ChallengeProgress.min = "0".toInt()
    }

    override fun getItemCount(): Int {
        return challengesList.size
    }
    fun updateChallenges(newChallengesList: MutableList<ChallengesProg>) {
        challengesList.clear()
        challengesList.addAll(newChallengesList)
        notifyDataSetChanged()
    }

    class ChallengeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val challengeTitle: TextView = itemView.findViewById(R.id.challengeTitle)
        val endval: TextView = itemView.findViewById(R.id.endval)
        val Trophy: ImageView = itemView.findViewById(R.id.Trophy)
        val ChallengeProgress: ProgressBar = itemView.findViewById(R.id.ChallengeProgress)

    }
}
