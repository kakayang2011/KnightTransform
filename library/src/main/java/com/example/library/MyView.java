package com.example.library;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MyView extends android.support.v7.widget.AppCompatTextView implements View.OnClickListener {
    int i = 0;

    public MyView(Context context) {
        super(context);
        setOnClickListener(this);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        Toast.makeText(getContext(), "jar text: " + (i++), Toast.LENGTH_LONG).show();
    }
}
