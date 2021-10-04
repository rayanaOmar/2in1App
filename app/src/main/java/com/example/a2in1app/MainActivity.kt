package com.example.a2in1app

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity() {

    lateinit var numbersGameButton: Button
    lateinit var guessThePhraseButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        numbersGameButton = findViewById(R.id.btNumbersGame)
        guessThePhraseButton = findViewById(R.id.btGuessThePhrase)

        //onClickListener
        numbersGameButton.setOnClickListener { startGame(NumbersGames())}
        guessThePhraseButton.setOnClickListener { startGame(GuessThePhrase()) }

        title = "Main Activity"
    }
    //onCreateMenu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.numbersGameID -> {
                startGame(NumbersGames())
                return true
            }
            R.id.guessPhraseID -> {
                startGame(GuessThePhrase())
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun startGame(activity: Activity){
        val intent = Intent(this, activity::class.java)
        startActivity(intent)
    }
}