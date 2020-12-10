package au.edu.jcu.cp3406.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton
import java.util.*
import kotlinx.android.synthetic.main.activity_edit.fab as fab1

class EditActivity : AppCompatActivity() {

    val realm: Realm = Realm.getDefaultInstance()
    private val calendar: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    private fun insertTodo(){
        realm.beginTransaction()

        val newItem=realm.createObject<Todo>(nextId())
        newItem.title=todoEditText.text.toString()
        newItem.date=calendar.timeInMillis

        realm.commitTransaction()
        alert("List has been added"){
            yesButton { finish() }
        }.show()
    }
    private fun nextId():Int{
        val maxId=realm.where<Todo>().max("id")
        if (maxId !=null){
            return maxId.toInt()+1
        }
        return 0
    }

    private fun updateTodo(id: Long){
        realm.beginTransaction()

        val updateItem = realm.where<Todo>().equalTo("id", id).findFirst()!!
        updateItem.title = todoEditText.text.toString()
        updateItem.date = calendar.timeInMillis

        realm.commitTransaction()

        alert("List has been edited") {
            yesButton { finish() }
        }.show()
    }

    private fun deleteTodo(id: Long) {
        realm.beginTransaction()

        val deleteItem = realm.where<Todo>().equalTo("id", id).findFirst()!!
        deleteItem.deleteFromRealm()
        realm.commitTransaction()

        alert("List has been deleted.") {
            yesButton { finish() }
        }.show()
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        val id = intent.getLongExtra("id", -1L)
        if (id==-1L){
            insertMode()}
        else{
            updateMode(id)
        }
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        }
    }

    private fun insertMode(){
        deletefab.hide()
        fab.setOnClickListener {
            insertTodo()
        }
    }

    private fun updateMode(id:Long){
        val todo = realm.where<Todo>().equalTo("id", id).findFirst()!!
        todoEditText.setText(todo.title)
        calendarView.date = todo.date

        fab.setOnClickListener {
            updateTodo(id)
        }
        deletefab.setOnClickListener {
            deleteTodo(id)
        }
    }
}