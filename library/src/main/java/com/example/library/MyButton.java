package com.example.library;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;

public class MyButton extends AppCompatButton implements View.OnClickListener {
    int i = 0;

    public MyButton(Context context) {
        super(context);
        setOnClickListener(this);
    }

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
    }

    public MyButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        Toast.makeText(getContext(), "jar button: " + (i++), Toast.LENGTH_LONG).show();
    }
}
