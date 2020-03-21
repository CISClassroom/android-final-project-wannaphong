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
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.jsoup.Jsoup
import th.ac.kku.cis.mobileapp.wwwlink.R
import th.ac.kku.cis.mobileapp.wwwlink.URLItem
import th.ac.kku.cis.mobileapp.wwwlink.UserLink


class HomeFragment : Fragment() {
    lateinit var mDB: DatabaseReference

    private lateinit var homeViewModel: HomeViewModel
    lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        mDB = FirebaseDatabase.getInstance().reference
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        textView.text = "Hi"
        val addbtn:Button = root.findViewById(R.id.AddURL)
        addbtn.setOnClickListener {
            addNewItem()
        }
        return root
    }
    fun addNewItem(){
        val dialog = AlertDialog.Builder(activity)

        val context: Context? = this.context
        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL

        val et = EditText(context)
        et.hint = "Title"
        layout.addView(et) // Notice this is an add method

        val descriptionBox = EditText(context)
        descriptionBox.hint = "Description"
        layout.addView(descriptionBox) // Another add method
        dialog.setView(layout)/*
        val et = EditText(activity)

        dialog.setMessage("Add New URL")
        dialog.setTitle("Enter URL")
        dialog.setView(et)
        dialog.setTitle("Enter Note")
        val note = EditText(activity)
        dialog*/

        dialog.setPositiveButton("Submit"){
                dialog,positiveButton ->
            var uid:String = auth.currentUser!!.uid
            var findurl = mDB.child("URL_item")
            var newURL = URLItem.create()
            var url:String = et.text.toString()
            newURL.url = url
            Log.w("URL",newURL.url)
            var isok=findurl.orderByChild("URL_item").equalTo(url).ref

            val newURL2user:UserLink = UserLink.create()

            var k:String? =null

           // if(isok.toString()==null) {
            try{
                var doc = Jsoup.connect(url).get()
                //newURL.URLtitle = doc.title().toString()
            } catch (ex:Exception){
                Log.w("error",ex)
            }



            val newItemDB = mDB.child("URL_item").push()
            newURL.objID = newItemDB.key
            newItemDB.setValue(newURL)
            k = newItemDB.key
         /*   }
            else{
                k = isok.key.toString()
            }//
*/
            newURL2user.URLobj=k
            val newItemDB2 = mDB.child("URL").child(uid).push()
            newURL2user.objID = newItemDB2.key
            newURL2user.status = false
            newItemDB2.setValue(newURL2user)

            dialog.dismiss()
        }
        dialog.show()
    }
}
