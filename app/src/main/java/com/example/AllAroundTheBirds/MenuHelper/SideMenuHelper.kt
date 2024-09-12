package com.example.AllAroundTheBirds.MenuHelper


import android.content.Intent
import android.view.GestureDetector
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.AllAroundTheBirds.BirdInformationActivity
import com.example.AllAroundTheBirds.ChallengesActivity
import com.example.AllAroundTheBirds.FeedActivity
import com.example.AllAroundTheBirds.LoginPage
import com.example.AllAroundTheBirds.MainActivityMap
import com.example.AllAroundTheBirds.R
import com.example.AllAroundTheBirds.SaveBird
import com.example.AllAroundTheBirds.Settings
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlin.math.abs

class SideMenuHelper (
    private val context: AppCompatActivity,
    private val drawerLayout: DrawerLayout
) : NavigationView.OnNavigationItemSelectedListener {

    //private lateinit var contentLayout: LinearLayout
    private lateinit var gestureDetector: GestureDetector
    private lateinit var headerView: View
    private lateinit var nameTextView: TextView
    private lateinit var emailTextView: TextView
    //firebase
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference


    init {
        val toggle = ActionBarDrawerToggle(
            context,
            drawerLayout,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    fun setupMenu(navigationView: NavigationView, contentLayout: ViewGroup) {
        navigationView.setNavigationItemSelectedListener(this)
        //  this.contentLayout = contentLayout

        gestureDetector = GestureDetector(context, SwipeGestureListener())

        contentLayout.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
        }
        // Set navigation header data
        headerView = navigationView.getHeaderView(0)
        nameTextView = headerView.findViewById(R.id.nameTextView)
        emailTextView = headerView.findViewById(R.id.emailTextView)
        setNavigationHeaderData()
    }
    //The following code was taken from stackoverflow
    //Author :  darkblade60
    //Link:https://stackoverflow.com/questions/62902283/how-to-do-a-query-with-that-structure-in-firebase-kotlin
    private fun setNavigationHeaderData() {
        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
       // Get the current user's UID
        val currentUser = firebaseAuth.currentUser
        val currentUserId = currentUser?.uid
        if (currentUserId != null) {
            // Retrieve the user's data from Firebase
            databaseReference.child(currentUserId).get()
                .addOnSuccessListener { snapshot ->
                    if (snapshot.exists()) {
                        val name = snapshot.child("name").value.toString()
                        val email = snapshot.child("email").value.toString()
                        nameTextView.text = name
                        emailTextView.text = email
                    } else {
                        // Handle the case where the user's data doesn't exist
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle any errors that occurred during the fetch
                }
        } else {
            // The user is not authenticated, handle as needed
        }

    }
    //The following code was taken from stackoverflow
    //Author :  Mirek Rusin
    //Link: https://stackoverflow.com/questions/4139288/android-how-to-handle-right-to-left-swipe-gestures
    inner class SwipeGestureListener : GestureDetector.SimpleOnGestureListener() {
        private val SWIPE_THRESHOLD = 100
        private val SWIPE_VELOCITY_THRESHOLD = 100

        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            val diffX = e2.x - e1.x
            val diffY = e2.y - e1.y

            if (abs(diffX) > abs(diffY)) {
                if (abs(diffX) > SWIPE_THRESHOLD && abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        drawerLayout.openDrawer(GravityCompat.END)
                    }
                }
            }

            return super.onFling(e1, e2, velocityX, velocityY)
        }
    }
    //The following code was taken from stackoverflow
    //Author :  Ojonugwa Jude Ochalifu
    //Link: https://stackoverflow.com/questions/32944798/switch-between-fragments-with-onnavigationitemselected-in-new-navigation-drawer
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
           R.id.nav_option1 -> {
                navigateToActivity(FeedActivity::class.java)
            }
            R.id.nav_option2 -> {
                navigateToActivity(BirdInformationActivity::class.java)
            }
            R.id.nav_option3 -> {
                navigateToActivity(MainActivityMap::class.java)
            }
            R.id.nav_option4 -> {
                navigateToActivity(SaveBird::class.java)
            }
            R.id.nav_option5 -> {
                navigateToActivity(ChallengesActivity::class.java)
            }

            R.id.nav_option6 -> {
                navigateToActivity(Settings::class.java)
            }
            R.id.nav_option7 -> {
                //"Logout"
               FirebaseAuth.getInstance().currentUser?.getIdToken(false)
                FirebaseAuth.getInstance().signOut()
                navigateToActivity(LoginPage::class.java)
            }
        }

        drawerLayout.closeDrawer(GravityCompat.END)
        return true
    }

    private fun navigateToActivity(activityClass: Class<*>) {
        val intent = Intent(context, activityClass)
        context.startActivity(intent)
        finishCurrentActivity()
    }

    private fun finishCurrentActivity() {
        context.finish()
    }
    fun openMenu() {
        drawerLayout.openDrawer(GravityCompat.END)
    }

}