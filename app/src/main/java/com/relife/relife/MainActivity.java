package com.relife.relife;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
    String[] drawables;

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
            String description = cursor.getString(0);
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
                img.setImageURI(Uri.parse(drawables[which]));
                img.setScaleX(0.5f);
                img.setScaleY(0.5f);
                TextView text = new TextView(this);
                text.setText("Time: " + hour + " :00 Activity Name: " + description);
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
                LinearLayout.LayoutParams textParam = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.MATCH_PARENT);
                textParam.setMargins(75, 25, 0, 0);
                patchouli.setTextSize(18    );
                patchouli.setLayoutParams(textParam);
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
                img.setImageURI(Uri.parse(drawables[which]));
                img.setScaleX(0.5f);
                img.setScaleY(0.5f);
                TextView text = new TextView(this);
                text.setText("Time: " + hour + ":00 Activity Name: " + description);
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
        Cursor cursor = helper.listActivities(db, getApplicationContext());
        if(cursor != null){
            int size = cursor.getCount();
            if(size == 0){
                helper.insertActivity(db, "Sleep", getPath(Uri.parse("android.resource://"+getApplicationContext().getPackageName()+"/drawable/sleep")));
                helper.insertActivity(db, "Game", getPath(Uri.parse("android.resource://"+getApplicationContext().getPackageName()+"/drawable/gamecontroller")));
                helper.insertActivity(db, "Study", getPath(Uri.parse("android.resource://"+getApplicationContext().getPackageName()+"/drawable/openbook")));
                helper.insertActivity(db, "Chess", getPath(Uri.parse("android.resource://"+getApplicationContext().getPackageName()+"/drawable/knight")));
                helper.insertActivity(db, "Soccer", getPath(Uri.parse("android.resource://"+getApplicationContext().getPackageName()+"/drawable/soccer")));
                helper.insertActivity(db, "Work Out", getPath(Uri.parse("android.resource://"+getApplicationContext().getPackageName()+"/drawable/dumbbell")));
            }
            drawables = new String[size];
            cursor.moveToFirst();
            for(int i = 0; i < size; i++){
                drawables[i] = cursor.getString(1);
                cursor.moveToNext();
            }
        }
        Button btn1 = (Button) findViewById(R.id.button1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intenttt = new Intent(MainActivity.this, Activities.class);
                startActivity(intenttt);
            }
        });
        Button btn2 = (Button) findViewById(R.id.button2);
        btn2.setOnClickListener(new View.OnClickListener() {
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

    public String getPath(Uri uri) {
        // just some safety built in
        if( uri == null ) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        // this is our fallback here
        return uri.getPath();
    }
}
