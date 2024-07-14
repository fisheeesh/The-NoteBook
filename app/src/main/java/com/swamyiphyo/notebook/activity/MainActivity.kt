package com.swamyiphyo.notebook.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.swamyiphyo.notebook.model.Note
import com.swamyiphyo.notebook.R
import com.swamyiphyo.notebook.adapter.BaseAdapter
import com.swamyiphyo.notebook.database.DBHelper
import com.swamyiphyo.notebook.databinding.ActivityMainBinding
import com.swamyiphyo.notebook.databinding.ListNotesBinding

class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var db: DBHelper
    private lateinit var noteAdapter: BaseAdapter<Note>
    private lateinit var noteBinding: ListNotesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        db = DBHelper(this)

        mainBinding.addNoteBtn.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivityForResult(intent, 1)
        }

        mainBinding.deleteAll.setOnClickListener {
            confirmDialog()
        }

        getAllNotes()
    }

    override fun onResume() {
        super.onResume()
        getAllNotes()  // Refresh notes list when activity resumes
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            getAllNotes()  // Refresh notes list when returning from AddActivity or UpdateActivity
        }
    }

    private fun getAllNotes() {
        val notes = db.getAllNotes()
        if (notes.isEmpty()) {
            mainBinding.imageViewEmpty.visibility = View.VISIBLE
            mainBinding.textViewNoData.visibility = View.VISIBLE
        } else {
            mainBinding.imageViewEmpty.visibility = View.GONE
            mainBinding.textViewNoData.visibility = View.GONE

            val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            noteAdapter = BaseAdapter(R.layout.list_notes, notes, false) { _, data, view ->
                noteBinding = ListNotesBinding.bind(view)
                noteBinding.textViewTitle.text = data.title
                noteBinding.textViewContent.text = data.content
                noteBinding.editNoteBtn.setOnClickListener {
                    val intent = Intent(this, UpdateActivity::class.java)
                    intent.putExtra("id", data.id)
                    startActivityForResult(intent, 1)
                }
                val animation = AnimationUtils.loadAnimation(this, R.anim.translate_anim)
                view.animation = animation
            }

            mainBinding.mainRV.apply {
                layoutManager = linearLayoutManager
                setHasFixedSize(true)
                adapter = noteAdapter
            }
        }
    }

    private fun confirmDialog() {
        val alert = AlertDialog.Builder(this).apply {
            setTitle("Delete All Notes?")
            setMessage("Are you sure you want to DELETE all the notes?")
            setPositiveButton("Yes") { _, _ ->
                db.deleteAllNotes()
                Toast.makeText(this@MainActivity, "All Notes are Deleted!", Toast.LENGTH_SHORT).show()
                recreate()
            }
            setNegativeButton("No", null)
        }
        alert.create().show()
    }
}
