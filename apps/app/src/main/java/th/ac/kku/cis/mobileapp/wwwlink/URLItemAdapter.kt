package th.ac.kku.cis.mobileapp.wwwlink

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import io.github.ponnamkarthik.richlinkpreview.RichLinkViewSkype
import io.github.ponnamkarthik.richlinkpreview.RichLinkViewTwitter
import io.github.ponnamkarthik.richlinkpreview.ViewListener


class URLItemAdapter(context: Context, toDoItemList: MutableList<UserLink>) : BaseAdapter() {

    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    var contextthis: Context = context
    private var itemList = toDoItemList
    lateinit var mDatabase: DatabaseReference
    lateinit var auth: FirebaseAuth
    var uid:String? = null

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        // create object from view
        val objectId: String? = itemList.get(position).objID  as String?
        val itemText: String? = itemList.get(position).URL as String?
        val NoteText: String? = itemList.get(position).Note  as String?
        val view: View
        val vh: ListRowHolder
        auth = FirebaseAuth.getInstance()
        uid= auth.currentUser!!.uid

        // get list view
        if (convertView == null) {
            view = mInflater.inflate(R.layout.content_item_url, parent, false)
            vh = ListRowHolder(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as ListRowHolder
        }

        // add text to view
        vh.label.text = NoteText.toString()
        vh.urlshow.text = itemText.toString()
        //vh.ibDeleteObject.setVisibility(View.GONE)
        vh.richLinkView.setLink(
            vh.urlshow.text.toString(),
            object : ViewListener {
                override fun onSuccess(status: Boolean) {}
                override fun onError(e: Exception) {}
            })
        vh.ibDeleteObject.setOnClickListener {
            editData(view.context,objectId!!)
                //Toast.makeText(view.context,itemText.toString(),Toast.LENGTH_LONG).show()
        }
        return view
    }
    fun delData(objID:String){
        mDatabase.child("URL").child(uid!!).child(objID).child("note").removeValue()
    }
    fun editData(context: Context,objID:String){
        mDatabase = FirebaseDatabase.getInstance().reference
        val dialog = AlertDialog.Builder(context)
        val alert = AlertDialog.Builder(context)
        val itemEditText = EditText(context)
        alert.setMessage("Edit Note")
        //alert.setTitle("Enter To Do Item Text")
        alert.setView(itemEditText)
        // Set submit button dialog
        alert.setPositiveButton("Submit") { dialog, positiveButton ->
            // puch command
            mDatabase.child("URL").child(uid!!).child(objID).child("note").setValue(itemEditText.text.toString())
        }
        alert.show()
    }

    override fun getItem(index: Int): Any {
        return itemList.get(index)
    }

    override fun getItemId(index: Int): Long {
        return index.toLong()
    }

    override fun getCount(): Int {
        return itemList.size
    }

    private class ListRowHolder(row: View?) {
        val label: TextView = row!!.findViewById<TextView>(R.id.tv_item_text) as TextView
        val urlshow:TextView = row!!.findViewById<TextView>(R.id.urlshow) as TextView
        val ibDeleteObject: ImageButton = row!!.findViewById<ImageButton>(R.id.iv_delete) as ImageButton
        val richLinkView = row!!.findViewById(R.id.richLinkView) as RichLinkViewSkype
    }
}