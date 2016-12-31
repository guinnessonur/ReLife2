package com.relife.relife;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends Activity {
    DatabaseHelper helper;
    SQLiteDatabase db;

    public void showSchedule(){
        LinearLayout sakuya = (LinearLayout) findViewById(R.id.list);
        sakuya.removeAllViews();
        Cursor cursor = helper.list_items(db, getApplicationContext());
        if(cursor == null)
            return;
        cursor.moveToFirst();
        int last_day = 0;
        int last_month = 0;
        int last_year = 0;
        for(int i = 0; i < cursor.getCount(); i++){
            // 5 == year
            // 4 == month
            // 3 == day
            // 2 == hour
            int which = Integer.parseInt(cursor.getString(1));
            int day = Integer.parseInt(cursor.getString(3));
            int month = Integer.parseInt(cursor.getString(4));
            int year = Integer.parseInt(cursor.getString(5));
            int hour = Integer.parseInt(cursor.getString(2));
            if(last_day == day && last_month == month && last_year == year){
                LinearLayout knowledge = new LinearLayout(this);
                knowledge.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
                param.setMargins(20, 20, 20, 0);
                knowledge.setLayoutParams(param);
                knowledge.setGravity(Gravity.CENTER_VERTICAL);
                ImageView img = new ImageView(this);
                img.setImageDrawable(DragNDrop.drawables[which]);
                img.setScaleX(0.5f);
                img.setScaleY(0.5f);
                TextView text = new TextView(this);
                text.setText("Time: " + hour + " :00 Activity Name: " + DragNDrop.drawableDesc[which]);
                text.setTextSize(16);
                knowledge.addView(img);
                knowledge.addView(text);
                sakuya.addView(knowledge);
                last_day = day;
                last_month = month;
                last_year = year;
            }
            else{
                TextView patchouli = new TextView(this);
                patchouli.setText(day + "/" + month + "/" + year);
                LinearLayout knowledge = new LinearLayout(this);
                knowledge.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
                param.setMargins(20, 20, 20, 0);
                knowledge.setLayoutParams(param);
                knowledge.setGravity(Gravity.CENTER_VERTICAL);
                ImageView img = new ImageView(this);
                img.setImageDrawable(DragNDrop.drawables[which]);
                img.setScaleX(0.5f);
                img.setScaleY(0.5f);
                TextView text = new TextView(this);
                text.setText("Time: " + hour + ":00 Activity Name: " + DragNDrop.drawableDesc[which]);
                text.setTextSize(16);
                knowledge.addView(img);
                knowledge.addView(text);
                sakuya.addView(patchouli);
                sakuya.addView(knowledge);
                last_day = day;
                last_month = month;
                last_year = year;
            }
            cursor.moveToNext();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView iv = (ImageView) findViewById(R.id.imageView);

        helper=new DatabaseHelper(this);
        db=helper.getWritableDatabase();

        iv.setImageResource(R.drawable.nature);

        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DateActivity.class);
                startActivityForResult(intent, 0);
            }
        });
        showSchedule();
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        showSchedule();
    }
}
