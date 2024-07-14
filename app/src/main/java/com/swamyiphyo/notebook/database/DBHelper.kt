package com.swamyiphyo.notebook.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.swamyiphyo.notebook.model.Note

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object{
        private const val DATABASE_NAME = "notes.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "notes"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_CONTENT = "content"
        private lateinit var db : SQLiteDatabase
    }

    override fun onCreate(database: SQLiteDatabase?) {
        val query = "CREATE TABLE $TABLE_NAME($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_TITLE TEXT, $COLUMN_CONTENT TEXT);"
        database?.execSQL(query)
    }

    override fun onUpgrade(database: SQLiteDatabase?, p1: Int, p2: Int) {
        database?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(database)
    }
    fun addNote(note : Note) : Long{
        db = writableDatabase

        val values = ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_CONTENT, note.content)
        }

        return db.insert(TABLE_NAME, null, values)
    }
    fun getAllNotes() : List<Note>{
        db = readableDatabase
        val notes = mutableListOf<Note>()
        val query = "SELECT * FROM $TABLE_NAME;"

        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()){
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
            val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))

            val note = Note(id, title, content)
            notes.add(note)
        }
        return notes
    }
    fun updateNote(note : Note){
        db = writableDatabase

        val values = ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_CONTENT, note.content)
        }

        db.update(TABLE_NAME, values, "$COLUMN_ID=?", arrayOf(note.id.toString()))
    }
    fun getNoteById(noteId : Int) : Note {
        db = readableDatabase

        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = $noteId;"

        val cursor = db.rawQuery(query, null)

        cursor.moveToFirst()
        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
        val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))

        val note = Note(id, title, content)
        return note
    }
    fun deleteNote(noteId : Int){
        db = writableDatabase
        db.delete(TABLE_NAME, "$COLUMN_ID=?", arrayOf(noteId.toString()))
    }
    fun deleteAllNotes(){
        db = writableDatabase
        db.execSQL("DELETE FROM $TABLE_NAME")
    }
}