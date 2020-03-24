package th.ac.kku.cis.mobileapp.wwwlink.ui.home

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.brouding.simpledialog.SimpleDialog
import com.brouding.simpledialog.builder.General
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import th.ac.kku.cis.mobileapp.wwwlink.*
import th.ac.kku.cis.mobileapp.wwwlink.R
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment()  {
    lateinit var mDB: DatabaseReference

    private lateinit var homeViewModel: HomeViewModel
    lateinit var adapter: URLItemAdapter
    private var listViewItems: ListView? = null
    lateinit var auth: FirebaseAuth
    var URLItemList: MutableList<UserLink>? = null
    lateinit var mDatabase: DatabaseReference
    var uid:String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        auth = FirebaseAuth.getInstance()
        uid= auth.currentUser!!.uid

        mDB = FirebaseDatabase.getInstance().reference
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        listViewItems = root.findViewById<View>(R.id.items_list) as ListView
        val addbtn:Button = root.findViewById(R.id.AddURL)
        //val fab = root.findViewById<View>(R.id.fab) as FloatingActionButton
        addbtn.setOnClickListener {
            addNewItem()
        }
        mDatabase = FirebaseDatabase.getInstance().reference.child("URL").child(uid!!)//.getReference("URL")//.getRe//.child("URL").child(uid!!).ref
        URLItemList = mutableListOf<UserLink>()
        adapter = URLItemAdapter(requireContext(), URLItemList!!)
        listViewItems!!.setAdapter(adapter)
        mDatabase.orderByKey().addListenerForSingleValueEvent(itemListener)
        return root
    }
    var itemListener: ValueEventListener = object : ValueEventListener {

        override fun onDataChange(dataSnapshot: DataSnapshot) {
            // call function
            addDataToList(dataSnapshot)
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Getting Item failed, display log a message
            Log.w("MainActivity", "loadItem:onCancelled", databaseError.toException())
        }
    }
    private fun addDataToList(dataSnapshot: DataSnapshot) {
        for (datas in dataSnapshot.children) {
            //val name = datas.child("ShrubbedWord").value.toString()
            //val map = datas.getValue() as HashMap<String, Any>
            // add data to object
            val todoItem = UserLink.create()
            todoItem.objID = datas.key
            todoItem.URL = datas.child("url").getValue().toString()
            todoItem.Note = datas.child("note").getValue().toString()
            URLItemList!!.add(todoItem)
        }
        adapter.notifyDataSetChanged()
    }
    fun addNewItem(){
        var c:Cleanurl = Cleanurl()
        val dialog = AlertDialog.Builder(activity)

        val context: Context? = this.context
        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL

        val et = EditText(context)
        et.hint = "URL"
        layout.addView(et) // Notice this is an add method

        val descriptionBox = EditText(context)
        descriptionBox.hint = "Note"
        layout.addView(descriptionBox) // Another add method
        dialog.setView(layout)

        dialog.setPositiveButton("Submit"){
                dialog,positiveButton ->
            var newURL = URLItem.create()
            var url:String =c.clean(et.text.toString())
            var note:String = descriptionBox.text.toString()
            newURL.url = url
            Log.w("URL",newURL.url)
            var newURL2user:UserLink = UserLink.create()
            try{
               // newURL.URLtitle = Content().gettilte(url)
            }
            catch (ex:Exception){
                Log.e("url ->",ex.toString())
                dialog.dismiss()
            }

            var key:String? = ""
            newURL2user.Note = note
            var i =0
            mDB.child("URL_item").orderByChild("url").equalTo(url).limitToFirst(1).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if(i==0){
                    val children = dataSnapshot!!.children
                    try {
                        newURL2user.URLobj=key
                        Log.w("Firebase",key)
                    }
                    catch (ex:Exception){
                        val newItemDB = mDB.child("URL_item").push()
                        newURL.objID = newItemDB.key
                        newItemDB.setValue(newURL)
                        newURL2user.URLobj=newItemDB.key
                    }

                    addDB(newURL,newURL2user)
                    }
                    i+=1
                }

                override fun onCancelled(error: DatabaseError) {
                     Log.e("Firebase","Failed to read value: ${error.toException()}")
                }
            })

            Log.w("Key Key",key)


            dialog.dismiss()
        }
        dialog.show()
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
            SimpleDialog( General(requireContext())
                .setTitle("Add Link")
                .setContent("This is OK. :)", 3)
            ).show()
        }catch (ex:Exception){
            SimpleDialog( General(requireContext())
                .setTitle("Add Link")
                .setContent(ex.toString(), 3)
            ).show()
            Log.e("Error > ",ex.toString())
        }




    }
}
