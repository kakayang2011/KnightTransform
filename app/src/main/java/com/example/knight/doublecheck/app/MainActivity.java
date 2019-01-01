package com.example.knight.doublecheck.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.library.MyButton;
import com.example.library.MyView;
import com.knight.doublecheck.library.DoubleCheck;

public class MainActivity extends Activity implements View.OnClickListener {
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.text1);

        button.setOnClickListener(new View.OnClickListener() {
            @DoubleCheck
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "no double check: " + (++i), Toast.LENGTH_LONG).show();
            }
        });
        Button button1 = findViewById(R.id.text2);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "double check: " + (++i), Toast.LENGTH_LONG).show();
            }
        });
        MyButton button2 = findViewById(R.id.text3);
        button2.setOnClickListener(new View.OnClickListener() {
            @DoubleCheck
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "click: " + (++i), Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    public void onClick(View v) {
        Toast.makeText(this, "click", Toast.LENGTH_LONG).show();
    }
}
