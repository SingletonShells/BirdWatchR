package com.example.AllAroundTheBirds

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.AllAroundTheBirds.Adapters.ChallengesAdapter
import com.example.AllAroundTheBirds.Adapters.CompletedAdapter
import com.example.AllAroundTheBirds.DataClasses.Challenges
import com.example.AllAroundTheBirds.DataClasses.ChallengesProg
import com.example.AllAroundTheBirds.DataClasses.BirdSighting
import com.example.AllAroundTheBirds.MenuHelper.SideMenuHelper
import com.example.AllAroundTheBirds.databinding.ActivityChallengesBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class ChallengesActivity : AppCompatActivity() {
    lateinit var drawerLayout: DrawerLayout
    lateinit var sideMenuHelper: SideMenuHelper
    private lateinit var binding:ActivityChallengesBinding
    private val aut: FirebaseAuth = FirebaseAuth.getInstance()
    private var challengesList: MutableList<Challenges> = mutableListOf()
    private var progchallengesList: MutableList<ChallengesProg> = mutableListOf()
    private var achievedList: MutableList<Challenges> = mutableListOf()
    private var sightingList: MutableList<BirdSighting> = mutableListOf()
    private lateinit var challengesProgAdapter: ChallengesAdapter
    private lateinit var CompletedAdapter: CompletedAdapter
    private var gold: Int = 0
    private var silver: Int = 0
    private var bronze: Int = 0
    private var user = aut.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChallengesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        update()
//side(menu)
        drawerLayout = binding.drawerLayout
        val navigationView: NavigationView = binding.navView
        val contentLayout: ConstraintLayout = binding.contentLayout
        sideMenuHelper = SideMenuHelper(this, drawerLayout)
        sideMenuHelper.setupMenu(navigationView, contentLayout)

        val ChallengesrecyclerView: RecyclerView = findViewById(R.id.ChallengesrecyclerView)
        ChallengesrecyclerView.layoutManager = LinearLayoutManager(this)
        challengesProgAdapter = ChallengesAdapter(progchallengesList)
        ChallengesrecyclerView.adapter = challengesProgAdapter

        val CompletedrecyclerView: RecyclerView =findViewById(R.id.CompletedrecyclerView)
        CompletedrecyclerView.layoutManager = LinearLayoutManager(this)
        CompletedAdapter = CompletedAdapter(achievedList)
        CompletedrecyclerView.adapter = CompletedAdapter
        getchallenges { challenges ->
            if (challenges != null) {
                challengesList = challenges
                Log.d("Main challengesList", "${challengesList}")
                getbirdsighting { BirdSighting ->
                    if (BirdSighting != null) {
                        sightingList = BirdSighting
                        Log.d("Main sightingList", "${sightingList}")
                        getachievedchallenges { achieved ->
                            if (achieved != null) {
                                achievedList = achieved
                                Log.d("Main achievedList", "${achievedList}")
                                progchallengesList = createachieved()
                                Log.d("Main progchallengesList", "${progchallengesList}")
                                postachieved(object : OnTaskCompleteListener {
                                    override fun onTaskComplete() {
                                        getgoals(object : OnTaskCompleteListener {
                                            override fun onTaskComplete() {
                                                challengesProgAdapter.updateChallenges(
                                                    progchallengesList
                                                )
                                                CompletedAdapter.updateAchieved(achievedList)
                                            }
                                        })
                                    }
                                })
                            }
                        }
                    }
                }
            }
        }
    }
    private fun getchallenges(callback: (MutableList<Challenges>?) -> Unit) {
        val challengesLists: MutableList<Challenges> = mutableListOf()
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        if (user != null) {
            val userChallengesRef: DatabaseReference =
                database.getReference("challenges")
            // Read challenges from Firebase
            val valueEventListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (challengeSnapshot in dataSnapshot.children) {
                        val challenge = challengeSnapshot.getValue(Challenges::class.java)
                        challenge?.let {
                            challengesLists.add(it)
                        }
                    }
                    callback(challengesLists)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(
                        applicationContext,
                        "Error occurred while retrieving Challenges",
                        Toast.LENGTH_SHORT
                    ).show()
                    callback(null)

                }

            }
            userChallengesRef.addListenerForSingleValueEvent(valueEventListener)

            userChallengesRef.removeEventListener(valueEventListener)
        }
    }

    private fun getbirdsighting(callback: (MutableList<BirdSighting>?) -> Unit) {

        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val sightingLists: MutableList<BirdSighting> = mutableListOf()

        if (user != null) {
            // Get a reference to the challenges node under the user's UID
            val birdsightingRef: DatabaseReference = database.getReference("users/${user!!.uid}/birds")

            // Read challenges from Firebase
            val valueEventListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (birdsightingSnapshot in dataSnapshot.children) {
                        val birdsighting = birdsightingSnapshot.getValue(BirdSighting::class.java)
                        birdsighting?.let {
                            sightingLists.add(it)
                        }
                    }
                    callback(sightingLists)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(
                        applicationContext,
                        "Error occurred while retrieving sightings",
                        Toast.LENGTH_SHORT
                    ).show()
                    callback(null)
                }
            }
            birdsightingRef.addListenerForSingleValueEvent(valueEventListener)

            birdsightingRef.removeEventListener(valueEventListener)
        }
    }

    private fun getachievedchallenges(callback: (MutableList<Challenges>?) -> Unit){

        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val achievedLists: MutableList<Challenges> = mutableListOf()

        if (user != null) {
            // Get a reference to the challenges node under the user's UID
            val achievedRef: DatabaseReference = database.getReference("users/${user!!.uid}/achieved")

            val valueEventListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (achievedSnapshot in dataSnapshot.children) {
                        val achieved = achievedSnapshot.getValue(Challenges::class.java)
                        achieved?.let {
                            achievedLists.add(it)
                        }
                    }
                    callback(achievedLists)

                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(
                        applicationContext,
                        "Error occurred while retrieving achievements",
                        Toast.LENGTH_SHORT
                    ).show()
                    callback(null)
                }
            }
            achievedRef.addListenerForSingleValueEvent(valueEventListener)

            achievedRef.removeEventListener(valueEventListener)
        }
    }

    @SuppressLint("LongLogTag")
    private fun createachieved(): MutableList<ChallengesProg>{
        val challengesToRemove: MutableList<Challenges> =  mutableListOf()
        val progchallengesLists: MutableList<ChallengesProg> = mutableListOf()

        for (challenge in challengesList) {
            if (challenge.type == "Picture") {
                val sightingCount = sightingList.count { it.birdName == challenge.birdname }
                if (challenge.amount != null && sightingCount.toInt() >= challenge.amount.toInt()) {
                    if(achievedList.any { it == challenge }){
                        challengesToRemove.add(challenge)
                    }
                    else {
                        achievedList.add(challenge)
                        challengesToRemove.add(challenge)
                    }
                }
                else {
                    val explicitChallengeProg = ChallengesProg(
                        type = challenge.type,
                        medal = challenge.medal,
                        challenge = challenge.challenge,
                        birdname = challenge.birdname,
                        amount = challenge.amount?.toInt(),
                        progress = sightingCount.toInt()
                    )
                    progchallengesLists.add(explicitChallengeProg)

                }

            }
            else {
                val sightingCount = sightingList.size
                if (challenge.amount != null && sightingCount.toInt() >= challenge.amount.toInt()) {
                    if(achievedList.any { it == challenge }){
                        challengesToRemove.add(challenge)
                    }
                    else {
                        achievedList.add(challenge)
                        challengesToRemove.add(challenge)
                    }

                }
                else {
                    val explicitChallengeProg = ChallengesProg(
                        type = challenge.type,
                        medal = challenge.medal,
                        challenge = challenge.challenge,
                        birdname = challenge.birdname,
                        amount = challenge.amount?.toInt(),
                        progress = sightingCount.toInt()
                    )
                    progchallengesLists.add(explicitChallengeProg)

                }

            }
        }

        Log.d("to be removed challenges","${challengesToRemove}")
        Log.d("before removed challenges","${challengesList}")
        val iterator = challengesList.iterator()
        while (iterator.hasNext()) {
            val challeng = iterator.next()
            if (challengesToRemove.any { it == challeng }) {
                iterator.remove()
            }
        }
        Log.d("after removed challenges","${challengesList}")
        Log.d("Progress","${progchallengesLists}")
        return progchallengesLists
    }

    private fun postachieved(listener: OnTaskCompleteListener){
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val achievedRef: DatabaseReference = database.getReference("users/${user?.uid}/achieved")

        for (achieved in achievedList) {
            // Check if the entry exists in the database
            val query: Query = achievedRef.orderByChild("challenge").equalTo(achieved.challenge)

            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var exists = false
                    for (snapshot in dataSnapshot.children) {
                        val existingAchieved = snapshot.getValue(Challenges::class.java)
                        if (existingAchieved != null) {
                            // If the entry already exists, set the flag to true
                            exists = true
                            break
                        }
                    }

                    // If the entry doesn't exist, add it to the database
                    if (!exists) {
                        val challenge5Key = achievedRef.push().key
                        if (challenge5Key != null) {
                            achievedRef.child(challenge5Key).setValue(achieved)
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle error
                    Log.e("checkAchieved", "Error: ${databaseError.message}")
                }
            })
        }
        listener.onTaskComplete()
    }

    private fun getgoals(listener: OnTaskCompleteListener) {
        // Initialize Firebase

        val database: FirebaseDatabase = FirebaseDatabase.getInstance()

        if (user != null) {
            // Get a reference to the challenges node under the user's UID
            val achievedRef: DatabaseReference = database.getReference("users/${user!!.uid}/achieved")

            // Read challenges from Firebase
            val valueEventListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    var goldCount = 0
                    var silverCount = 0
                    var bronzeCount = 0

                    for (achievedSnapshot in dataSnapshot.children) {
                        val achieved = achievedSnapshot.getValue(Challenges::class.java)
                        if (achieved != null) {
                            when (achieved.medal) {
                                "Gold" -> goldCount++
                                "Silver" -> silverCount++
                                "Bronze" -> bronzeCount++
                                // Add additional cases as needed
                            }
                        }
                    }
                    // Now you have counts for each medal type
                    gold = goldCount
                    silver = silverCount
                    bronze = bronzeCount

                    val GoldCount: TextView = findViewById(R.id.GoldCount)
                    val SilverCount: TextView = findViewById(R.id.SilverCount)
                    val BronzeCount: TextView = findViewById(R.id.BronzeCount)
                    GoldCount.text = "Count: ${gold}"
                    SilverCount.text = "Count: ${silver}"
                    BronzeCount.text = "Count: ${bronze}"
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle error

                }
            }

            // Add the listener to the reference
            achievedRef.addListenerForSingleValueEvent(valueEventListener)

            // Optionally, remove the listener when it's no longer needed
            achievedRef.removeEventListener(valueEventListener)

            // Optionally, close the FirebaseDatabase instance when it's no longer needed
            // database.goOffline()
        } else {
            // Handle the case when user is null
        }
        listener.onTaskComplete()
    }


    private fun update() {

//        auth.createUserWithEmailAndPassword("email@gmail.com", "password")
//        auth.signInWithEmailAndPassword("email@gmail.com", "password")

        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val challengesRef: DatabaseReference = database.getReference("challenges")
        val achievedRef: DatabaseReference = database.getReference("users/${user?.uid}/achieved")
        val birdsightingRef: DatabaseReference = database.getReference("users/${user?.uid}/birds")

        // Create Challenge 1
        val challenge1 = Challenges(type = "Picture", medal = "Bronze", challenge = "Spot a ", birdname = "Barn Owl", amount = 1)
        val challenge1Key = challengesRef.push().key
        if (challenge1Key != null) {
            challengesRef.child(challenge1Key).setValue(challenge1)
        }

        // Create Challenge 2
        val challenge2 = Challenges(type = "Sightings", medal = "Silver", challenge = "Spot a bird ", birdname = null, amount = 1)
        val challenge2Key = challengesRef.push().key
        if (challenge2Key != null) {
            challengesRef.child(challenge2Key).setValue(challenge2)
        }

        // Create Challenge 3
        val challenge3 = Challenges(type = "Sightings", medal = "Silver", challenge = "Spot 3 birds ", birdname = null, amount = 3)
        val challenge3Key = challengesRef.push().key
        if (challenge3Key != null) {
            challengesRef.child(challenge3Key).setValue(challenge3)
        }

        val challenge4 = Challenges(type = "Sightings",medal = "Gold",challenge = "Spot 4 birds ",birdname = null,amount = 4)
        val challenge4Key = challengesRef.push().key
        if (challenge4Key != null) {
            challengesRef.child(challenge4Key).setValue(challenge4)
        }

        val challenge5 = Challenges(type = "Sightings",medal = "Gold",challenge = "Spot 4 birds ",birdname = null,amount = 4)
        val challenge5Key = achievedRef.push().key
        if (challenge5Key != null) {
            achievedRef.child(challenge5Key).setValue(challenge5)
        }

        val sighted = BirdSighting(birdName = "Barn Owl",imageUrl= null,date="")
        val sightedKey = birdsightingRef.push().key
        if (sightedKey != null) {
            birdsightingRef.child(sightedKey).setValue(sighted)
        }
    }
}
interface OnTaskCompleteListener {
    fun onTaskComplete()
}