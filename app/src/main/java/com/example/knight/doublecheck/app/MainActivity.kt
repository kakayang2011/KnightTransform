package com.example.knight.doublecheck.app

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast

class MainActivity1 : Activity(), View.OnClickListener {
    var i =0;
    override fun onClick(v: View?) {
        Toast.makeText(this, "this is first toast! ${i++}", Toast.LENGTH_LONG).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val textview = findViewById<TextView>(R.id.text)
        textview.setOnClickListener(this)
    }
}
