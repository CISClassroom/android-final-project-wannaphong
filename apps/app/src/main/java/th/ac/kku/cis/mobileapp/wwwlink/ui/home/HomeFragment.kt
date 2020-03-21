package th.ac.kku.cis.mobileapp.wwwlink.ui.home

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.jsoup.Jsoup
import th.ac.kku.cis.mobileapp.wwwlink.R
import th.ac.kku.cis.mobileapp.wwwlink.URLItem
import java.net.HttpURLConnection
import java.net.URL

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
        val et = EditText(activity)

        dialog.setMessage("Add New URL")
        dialog.setTitle("Enter URL")
        dialog.setView(et)

        dialog.setPositiveButton("Submit"){
                dialog,positiveButton ->
            var findurl = mDB.child("URL_item")
            val newURL = URLItem.create()
            newURL.url = et.text.toString()
            val isok=findurl.orderByChild("url").equalTo(newURL.url).ref
            isok.
            if(isok==null) {


                val doc = Jsoup.connect(newURL.url).get()
                newURL.URLtitle = doc.title().toString()

                val newItemDB = mDB.child("URL_item").push()
                newURL.objID = newItemDB.key
                newItemDB.setValue(newURL)
            }


            dialog.dismiss()
        }
        dialog.show()
    }
}
