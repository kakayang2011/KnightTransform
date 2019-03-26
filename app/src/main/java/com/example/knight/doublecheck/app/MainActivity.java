package com.example.knight.doublecheck.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

public class MainActivity extends Activity implements View.OnClickListener {
    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.text1);

//        button.setOnClickListener(new View.OnClickListener() {
//            @DoubleCheck
//            @Override
//            public void onClick(View v) {
//
//                Toast.makeText(MainActivity.this, "no double check: " + (++i) + "   toast: " + (new Test()).getToast(), Toast.LENGTH_LONG).show();
//            }
//        });
        Button button1 = findViewById(R.id.text2);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "double check: " + (++i), Toast.LENGTH_LONG).show();
            }
        });
//        MyButton button2 = findViewById(R.id.text3);
//        button2.setOnClickListener(new View.OnClickListener() {
//            @DoubleCheck
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "click: " + (++i), Toast.LENGTH_LONG).show();
//                Log.i("123", Test.Status.SCUUESS.toString());
//            }
//        });

        new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        };


        ((ImageView) findViewById(R.id.image)).setImageResource(R.drawable.origin);

    }

    @Override
    public void onClick(View v) {
        Toast.makeText(this, "click", Toast.LENGTH_LONG).show();
    }
}
