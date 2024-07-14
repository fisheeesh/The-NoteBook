package com.swamyiphyo.notebook.activity

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.swamyiphyo.notebook.database.DBHelper
import com.swamyiphyo.notebook.model.Note
import com.swamyiphyo.notebook.databinding.ActivityAddBinding

class AddActivity : AppCompatActivity() {
    private lateinit var addBinding: ActivityAddBinding
    private lateinit var db: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addBinding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(addBinding.root)

        db = DBHelper(this)

        addBinding.saveNoteBtn.setOnClickListener {
            val titleInput = addBinding.editTextTitle.text.toString().trim()
            val contentInput = addBinding.editTextContent.text.toString().trim()

            if (titleInput.isEmpty() || contentInput.isEmpty()) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            } else {
                val note = Note(0, titleInput, contentInput)
                if (db.addNote(note) == -1L) {
                    Toast.makeText(this, "Note Insertion Failed!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Note Added Successfully!", Toast.LENGTH_SHORT).show()
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }
        }
    }
}
