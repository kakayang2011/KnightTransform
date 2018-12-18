package com.example.knight.doublecheck.app;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.example.library.MyView;

public class MainActivity extends Activity implements View.OnClickListener {
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.text);
        textView.setText("123");
        Log.i("liyachao", "main ");

        textView.setOnClickListener(new View.OnClickListener() {
            @DoubleCheck
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "click: " + (++i), Toast.LENGTH_LONG).show();
                Log.i("liyachao", Log.getStackTraceString(new Throwable()));
            }
        });


        MyView myView = findViewById(R.id.text1);

        myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "click2222: " + (++i), Toast.LENGTH_LONG).show();


            }
        });

    }

    @Override
    public void onClick(View v) {
        Toast.makeText(this, "click", Toast.LENGTH_LONG).show();
    }
}
