package com.relife.relife;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView iv = (ImageView) findViewById(R.id.imageView);
        iv.setImageResource(R.drawable.nature);

        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScrollView sv = (ScrollView) findViewById(R.id.scrollView);
                LinearLayout ll = (LinearLayout) findViewById(R.id.list);
                TextView tv = new TextView(MainActivity.this);
                tv.setText("Hello amigo");
                ll.addView(tv);
            }
        });
    }
}
