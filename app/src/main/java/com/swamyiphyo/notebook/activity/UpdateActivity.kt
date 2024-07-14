package com.swamyiphyo.notebook.activity

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.swamyiphyo.notebook.database.DBHelper
import com.swamyiphyo.notebook.databinding.ActivityUpdateBinding
import com.swamyiphyo.notebook.model.Note

class UpdateActivity : AppCompatActivity() {
    private lateinit var updateBinding: ActivityUpdateBinding
    private lateinit var db: DBHelper
    private lateinit var note: Note

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateBinding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(updateBinding.root)

        db = DBHelper(this)

        val noteId = intent.getIntExtra("id", -1)
        getAndSetIntentData(noteId)

        updateBinding.deleteBtn.setOnClickListener {
            confirmDialog(note)
        }
    }

    private fun getAndSetIntentData(noteId: Int) {
        note = db.getNoteById(noteId)
        updateBinding.editTextTitleUpdate.setText(note.title)
        updateBinding.editTextContentUpdate.setText(note.content)

        updateBinding.updateNoteBtn.setOnClickListener {
            val titleUpdate = updateBinding.editTextTitleUpdate.text.toString().trim()
            val contentUpdate = updateBinding.editTextContentUpdate.text.toString().trim()

            if (titleUpdate.isEmpty() || contentUpdate.isEmpty()) {
                Toast.makeText(this, "Please enter all the fields", Toast.LENGTH_SHORT).show()
            } else {
                val updatedNote = Note(noteId, titleUpdate, contentUpdate)
                db.updateNote(updatedNote)
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }

    private fun confirmDialog(note: Note) {
        val alert = AlertDialog.Builder(this).apply {
            setTitle("Delete ${note.title}?")
            setMessage("Are you sure you want to delete ${note.title}?")
            setPositiveButton("Yes") { _, _ ->
                db.deleteNote(note.id)
                Toast.makeText(this@UpdateActivity, "Note ID - ${note.id} is deleted!", Toast.LENGTH_SHORT).show()
                setResult(Activity.RESULT_OK)
                finish()
            }
            setNegativeButton("No", null)
        }
        alert.create().show()
    }
}
