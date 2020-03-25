package th.ac.kku.cis.mobileapp.wwwlink

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.util.Patterns
import android.view.MenuItem
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.brouding.simpledialog.SimpleDialog
import com.brouding.simpledialog.builder.General
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class Link : AppCompatActivity() {
    lateinit var mDB: DatabaseReference
    lateinit var auth: FirebaseAuth
    var uid:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_link)
        auth = FirebaseAuth.getInstance()
        uid= auth.currentUser!!.uid
        mDB = FirebaseDatabase.getInstance().reference
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        when {
            intent?.action == Intent.ACTION_SEND -> {
            if ("text/plain" == intent.type) {
                handleSendText(intent) // Handle text being sent
            }
        }}

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_setting//, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.navigation_setting
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
    private fun handleSendText(intent: Intent) {
        intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
            // Update UI to reflect text being shared
            addNewItem(it)

        }
    }

    private fun isValidUrl(url: String): Boolean {
        val p: Pattern = Patterns.WEB_URL
        val m: Matcher = p.matcher(url.toLowerCase())
        return m.matches()
    }
    fun addNewItem(link:String){
        var c = Cleanurl()
        if(isValidUrl(link.toString())) {
            var newURL = URLItem.create()
            var url: String = c.clean(link.toString())
            var note: String = ""
            newURL.url = url
            Log.w("URL", newURL.url)
            var newURL2user: UserLink = UserLink.create()
            /* try{
           newURL.URLtitle = Content().gettilte(url)
        }
        catch (ex:Exception){
            Log.e("url ->",ex.toString())
            dialog.dismiss()
        }*/

            var key: String? = ""
            newURL2user.Note = note
            var i = 0
            mDB.child("URL_item").orderByChild("url").equalTo(url).limitToFirst(1)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (i == 0) {
                            val children = dataSnapshot!!.children
                            try {
                                key = children.first().key.toString()
                                newURL2user.URLobj = key
                                Log.w("Firebase", key)
                            } catch (ex: Exception) {
                                val newItemDB = mDB.child("URL_item").push()
                                newURL.objID = newItemDB.key
                                newItemDB.setValue(newURL)
                                newURL2user.URLobj = newItemDB.key
                            }

                            addDB(newURL, newURL2user)
                        }
                        i += 1
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e("Firebase", "Failed to read value: ${error.toException()}")
                    }
                })

            Log.w("Key Key", key)



        }
        else{
        }
    }
    fun addDB(newURL:URLItem,newURL2user:UserLink){
        try{
            //var uid:String = auth.currentUser!!.uid
            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            val currentDate:String = sdf.format(Date())
            if(newURL2user.URLobj==null){
                val newItemDB = mDB.child("URL_item").push()
                newURL.objID = newItemDB.key
                newItemDB.setValue(newURL)
                newURL2user.URLobj=newItemDB.key
            }
            val newItemDB2 = mDB.child("URL").child(uid!!).push()
            newURL2user.objID = newItemDB2.key
            newURL2user.status = false
            newURL2user.URL = newURL.url
            newURL2user.TimeSave = currentDate

            newItemDB2.setValue(newURL2user)
        }catch (ex:Exception){
            Log.e("Error > ",ex.toString())
        }
    }
}
