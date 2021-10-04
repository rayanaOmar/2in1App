package com.example.a2in1app

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.guess_the_phrase.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

class GuessThePhrase : AppCompatActivity() {

    //declaration
    lateinit var clMain: ConstraintLayout
    lateinit var guessField: EditText
    lateinit var guessButton: Button
    lateinit var messages: ArrayList<String>
    lateinit var stPhrase: TextView
    lateinit var stLetters: TextView

    var answer = "This is the Secret Phrase"
    var answerDictionary = mutableMapOf<Int, Char>()
    var theAnswer = ""
    var guessLetter = ""
    var count = 0
    var guessPhrase = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.guess_the_phrase)

        for(i in answer.indices){
            if(answer[i] == ' '){
                answerDictionary[i] == ' '
                theAnswer += ' '
            }else{
                answerDictionary[i] = '*'
                theAnswer += '*'
            }
        }
        //Initialization
        clMain = findViewById(R.id.clPhrase)
        guessField = findViewById(R.id.GuessField)
        guessButton = findViewById(R.id.GuessButton)

        stPhrase = findViewById(R.id.stPhrase)
        stLetters = findViewById(R.id.stLetters)

        messages = ArrayList()

        stMessages.adapter = MessageAdapter(this,messages)
        stMessages.layoutManager = LinearLayoutManager(this)

        //setOnClickListener
        guessButton.setOnClickListener { addMessage() }

        update()
        title = "Guess the Phrase"
    }
    override fun recreate() {
        super.recreate()
        answer = "this is the secret phrase"
        answerDictionary.clear()
        theAnswer = ""

        for(i in answer.indices){
            if(answer[i] == ' '){
                answerDictionary[i] = ' '
                theAnswer += ' '
            }else{
                answerDictionary[i] = '*'
                theAnswer += '*'
            }
        }

        guessLetter = ""
        count = 0
        guessPhrase = true
        messages.clear()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("answer", answer)

        val keys = answerDictionary.keys.toIntArray()
        val values = answerDictionary.values.toCharArray()
        outState.putIntArray("keys", keys)
        outState.putCharArray("values", values)

        outState.putString("myAnswer", theAnswer)
        outState.putString("guessedLetters", guessLetter)
        outState.putInt("count", count)
        outState.putBoolean("guessPhrase", guessPhrase)
        outState.putStringArrayList("messages", messages)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        answer = savedInstanceState.getString("answer", "nothing here")

        val keys = savedInstanceState.getIntArray("keys")
        val values = savedInstanceState.getCharArray("values")
        if(keys != null && values != null){
            if(keys.size == values.size){
                answerDictionary = mutableMapOf<Int, Char>().apply {
                    for (i in keys.indices) this [keys[i]] = values[i]
                }
            }
        }

        theAnswer = savedInstanceState.getString("myAnswer", "")
        guessLetter = savedInstanceState.getString("guessedLetters", "")
        count = savedInstanceState.getInt("count", 0)
        guessPhrase = savedInstanceState.getBoolean("guessPhrase", true)
        messages.addAll(savedInstanceState.getStringArrayList("messages")!!)
        update()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.game_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val item: MenuItem = menu!!.getItem(1)
        if(item.title == "Other Game"){ item.title = "Numbers Game" }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.newGameID -> {
                CustomAlertDialog(this,"Are you sure you want to abandon the current game?")
                return true
            }
            R.id.otherGameID -> {
                changeScreen(NumbersGames())
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


    //Function to add more message
    fun addMessage(){
        val msg = guessField.text.toString()

        if(guessPhrase){ // true
            if(msg == answer){
                disableEntry()
                CustomAlertDialog(this,"YOU ARE WIN!! \n play again?")
            }else{
                messages.add("WRONG Guess The message is $msg")
                //update the guessPhrase value
                guessPhrase = false
                update()
            }
        }else{
            //Make sure the user enter only one letter
            if(msg.isNotEmpty() && msg.length == 1){
                theAnswer = ""
                guessPhrase = true
                check(msg[0])
            }else{
                Snackbar.make(clMain, "PLEASE Enter One Letter Only!!!",
                    Snackbar.LENGTH_LONG).show()
            }
        }
        //Clear the filed and stMessage to new Entry
        guessField.text.clear()
        guessField.clearFocus()
        stMessages.adapter?.notifyDataSetChanged()
    }

    fun disableEntry(){
        guessButton.isEnabled = false
        guessButton.isClickable = false
        guessField.isEnabled = false
        guessField.isClickable = false
    }

    //Function to update the letter
    fun update(){
        stPhrase.text =("Phrase: " + theAnswer.toUpperCase())
        stLetters.text = "Guessed Letter is: " + guessLetter
        if(guessPhrase){//true
            guessField.hint = "Try to Guess the full Phrase"
        }else{
            guessField.hint = "Guess a Letter"
        }
    }

    //Function to check the letter
    fun check(guessLetters: Char){
        var isFound = 0
        for( i in answer.indices){
            if(answer[i] == guessLetters){
                answerDictionary[i] = guessLetters
                isFound++
            }
        }
        for(i in answerDictionary){
            theAnswer+=answerDictionary[i.key] // full the * position with the correct letter
        }
        if(theAnswer == answer){
            disableEntry()
            CustomAlertDialog(this,"YOU ARE WIN!! \n play again?")
        }
        if(guessLetter.isEmpty()){
            guessLetter+=guessLetter
        }
        else{
            guessLetter+=", "+guessLetter
        }
        if(isFound > 0 ){
            messages.add("Found $isFound ${guessLetters.toUpperCase()}(s)")
        }else{
            messages.add("NO ${guessLetters.toUpperCase()} s Found")
        }
        count++
        //decrease the number of chances (the guesses remain)
        val guessLeft = 10 - count
        if(count < 10 ){
            messages.add("$guessLeft guesses remaining!!")
        }
        update()
        stMessages.scrollToPosition(messages.size - 1 )
    }


}