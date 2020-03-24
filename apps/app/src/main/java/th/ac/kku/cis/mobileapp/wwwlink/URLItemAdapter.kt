package th.ac.kku.cis.mobileapp.wwwlink

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.TextView

class URLItemAdapter(context: Context, toDoItemList: MutableList<UserLink>) : BaseAdapter() {

    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private var itemList = toDoItemList

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        // create object from view
        val objectId: String? = itemList.get(position).objID  as String?
        val itemText: String? = itemList.get(position).URL as String?
        val NoteText: String? = itemList.get(position).Note  as String?
        val view: View
        val vh: ListRowHolder

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
        return view
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
    }
}