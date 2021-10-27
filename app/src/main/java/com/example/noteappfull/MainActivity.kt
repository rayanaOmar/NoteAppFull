package com.example.noteappfull

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noteappfull.Database.DatabaseHandler
import com.example.noteappfull.Database.NoteDetails

class MainActivity : AppCompatActivity() {
    lateinit var databaseHandler: DatabaseHandler

    lateinit var editText: EditText
    lateinit var submitBtn: Button
    lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        databaseHandler = DatabaseHandler(this)

        editText = findViewById(R.id.tvNewNote)
        recyclerView = findViewById(R.id.rvNotes)
        submitBtn = findViewById(R.id.btSubmit)

        submitBtn.setOnClickListener {
            addNote()
        }
        updateRV()
    }
    private fun updateRV(){
        recyclerView.adapter = RVadapter(this, getNote())
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
    private fun getNote(): ArrayList<NoteDetails>{
        return databaseHandler.retrieveNote()
    }
    private fun updateNote(NoteText: NoteDetails){
        databaseHandler.updateNote(NoteText)
        //update for recycler view
        updateRV()
    }
    private fun addNote(){
        databaseHandler.addMoreNote(NoteDetails(0,editText.text.toString()))
        editText.text.clear()
        Toast.makeText(this,"Note Added", Toast.LENGTH_LONG).show()
        updateRV()
    }

    fun deleteNote(noteId: Int){
        databaseHandler.deleteNote(NoteDetails(noteId, ""))
        updateRV()
    }
    fun raiseDialog(note: NoteDetails){
        val dialogBuilder = AlertDialog.Builder(this)
        val updatedNote = EditText(this)
        updatedNote.hint = "Enter new text"
        dialogBuilder
            .setCancelable(false)
            .setPositiveButton("Save", DialogInterface.OnClickListener {
                    _, _ -> updateNote(NoteDetails(note.id,updatedNote.text.toString()))
            })
            .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                    dialog, _ -> dialog.cancel()
            })
        val alert = dialogBuilder.create()
        alert.setTitle("Update Note")
        alert.setView(updatedNote)
        alert.show()
    }
}