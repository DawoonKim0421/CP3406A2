package au.edu.jcu.cp3406.todolist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.realm.OrderedRealmCollection
import io.realm.RealmBaseAdapter
import io.realm.RealmResults

class ViewHolder(view: View){
    val dataTextView: TextView=view.findViewById(R.id.text1)
    val textTextView: TextView=view.findViewById(R.id.text2)
}

class TodoListAdapter(realmResult: OrderedRealmCollection<Todo>)
    : RealmBaseAdapter<Todo>(realmResult) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val vh: ViewHolder
        val view: View

        if (convertView == null) {
            view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.item_todo, parent, false)
            vh = ViewHolder(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as ViewHolder
        }
        adapterData?. let {
            val item=it[position]
            vh.textTextView.text=item.title
            vh.dataTextView.text=android.text.format.DateFormat.format("yyyy/MM/dd", item.date)
        }
        return view
    }

    override fun getItemId(position: Int): Long {
        adapterData?.let {
            return it[position].id
        }
        return super.getItemId(position)
    }
}