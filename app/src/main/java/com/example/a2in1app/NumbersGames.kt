package com.example.a2in1app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.guess_the_phrase.*
import kotlinx.android.synthetic.main.number_game.*
import kotlin.random.Random

class NumbersGames: AppCompatActivity (){
    lateinit var clMainNumber: ConstraintLayout
    lateinit var guessFiled: EditText
    lateinit var guessButton: Button
    lateinit var messages: ArrayList<String>


    var answer = 0
    var guesses = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.number_game)

        answer = Random.nextInt(10)

        clMainNumber = findViewById(R.id.clMainNumber)
        messages = ArrayList()

        rvMessages.adapter = MessageAdapter(this, messages)
        rvMessages.layoutManager = LinearLayoutManager(this)

        guessFiled = findViewById(R.id.GuessField)
        guessButton = findViewById(R.id.GuessButton)

        guessButton.setOnClickListener { add() }

        title = "Numbers Game"

    }
    override fun recreate() {
        super.recreate()
        answer = Random.nextInt(10)
        guesses = 3
        messages.clear()
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt("answer", answer)
        outState.putInt("guesses", guesses)
        outState.putStringArrayList("messages", messages)
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        answer = savedInstanceState.getInt("answer", 0)
        guesses = savedInstanceState.getInt("guesses", 0)
        messages.addAll(savedInstanceState.getStringArrayList("messages")!!)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.game_menu, menu)
        return true
    }
    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val item: MenuItem = menu!!.getItem(1)
        if(item.title == "Other Game"){ item.title = "Guess The Phrase" }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.newGameID -> {
                CustomAlertDialog(this,"Are you sure you want to abandon the current game?")
                return true
            }
            R.id.otherGameID-> {
                changeScreen(GuessThePhrase())
                return true
            }
            R.id.backID -> {
                changeScreen(MainActivity())
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun changeScreen(activity: Activity){
        val intent = Intent(this, activity::class.java)
        startActivity(intent)
    }

    fun add(){
            val msg = guessFiled.text.toString()
            if(msg.isNotEmpty()){
                if(guesses > 0 ){
                    if(msg.toInt() == answer){ // user enter == random number
                        disableEntry()
                        CustomAlertDialog(this,"YOU WIN!!, PLAY AGAIN?")
                    }else{
                        guesses--
                        messages.add("YOU GUESSED $msg")
                        messages.add("YOU HAVE $guesses GUESSES LEFT")
                    }
                    if(guesses == 0){ // your try is over
                        disableEntry()
                        messages.add("YOU ARE LOSE, THE CORRECT ANSWER WAS $answer")
                        messages.add("GAME OVER")
                        CustomAlertDialog(this,"YOU ARE LOSE, THE CORRECT ANSWER WAS $answer")
                    }
                }
                guessFiled.text.clear()
                guessFiled.clearFocus()
                rvMessages.adapter?.notifyDataSetChanged()
            }else{
                Snackbar.make(clMainNumber, "Please enter a number", Snackbar.LENGTH_LONG).show()
            }
        }
        fun disableEntry(){
            guessButton.isEnabled = false
            guessButton.isClickable = false
            guessFiled.isEnabled = false
            guessFiled.isClickable = false
        }
    }
