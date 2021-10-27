package com.example.noteappfull.Database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler (context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    //The constructor -> database name and database version
    companion object {
        private const val DATABASE_NAME = "MYDatabase"
        private const val DATABASE_VERSION = 1
        private const val NOTE_TABLE = "NotesTable"

        private const val KEYID = "id"
        private const val KEYNOTE = "note"
    }

    //Query for creating the tables
    override fun onCreate(database: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE $NOTE_TABLE($KEYID INTEGER Primary Key, $KEYNOTE Text)")
        database?.execSQL(createTable)
    }

    //when the version number is increased this will be executed
    override fun onUpgrade(database: SQLiteDatabase?, p1: Int, p2: Int) {
        database!!.execSQL("DROP TABLE IF EXISTS $NOTE_TABLE")
        onCreate(database)
    }

    //function to add the note
    //the note will be form NoteDetails Class (Type)
    fun addMoreNote(note: NoteDetails): Long {
        val database = this.writableDatabase
        //add the note to contentValues
        val contentValues = ContentValues()
        contentValues.put(KEYNOTE, note.noteText)

        //add the note into SQLite database
        val successful = database.insert(NOTE_TABLE, null, contentValues)
        database.close()

        //return the note form the cursor after save it
        return successful
    }

    @SuppressLint("Range")
    fun retrieveNote(): ArrayList<NoteDetails> {
        val database = this.writableDatabase
        var cursor: Cursor? = null

        val noteList: ArrayList<NoteDetails> = ArrayList()
        val query = "SELECT * FROM $NOTE_TABLE"

        try {
            cursor = database.rawQuery(query, null)
        } catch (e: SQLiteException) {
            database.execSQL(query)
            return ArrayList()
        }
        var id: Int
        var noteText: String

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEYID))
                noteText = cursor.getString(cursor.getColumnIndex(KEYNOTE))
                val note = NoteDetails(id = id, noteText = noteText)
                noteList.add(note)
            } while (cursor.moveToNext())
        }
        return noteList
    }

    //update function
    fun updateNote(note: NoteDetails){
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEYNOTE, note.noteText)

        val successful = database.update(NOTE_TABLE, contentValues,
            "$KEYID = ${note.id}", null)

        database.close()

    }

    //Delete function
    fun deleteNote(note: NoteDetails): Int{
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEYID, note.id)

        val successful = database.delete(NOTE_TABLE, "$KEYID = ${note.id}", null)
        database.close()
        return successful
    }
}