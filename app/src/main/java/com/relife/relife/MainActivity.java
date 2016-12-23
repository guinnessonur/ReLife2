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
                Intent intent = new Intent(MainActivity.this, DragNDrop.class);
                startActivityForResult(intent, 1);
            }
        });
        displaySchedule();
    }
    public void displaySchedule(){
        Cursor cursor = helper.list_items(db, getApplicationContext());
        LinearLayout eclair = (LinearLayout) findViewById(R.id.list);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                LinearLayout sakuya = (LinearLayout) findViewById(R.id.list);
                sakuya.removeAllViews();
                String result = data.getStringExtra("result");
                for(int i = 0; i < DragNDrop.layouts.length; i++){
                    View view = DragNDrop.layouts[i].getChildAt(0);
                    if (view instanceof ImageView) {
                        ImageView imageView = (ImageView) view;
                        int which = -1;
                        for(int j = 0; j < DragNDrop.drawables.length; j++){
                            if(DragNDrop.drawables[j].equals(imageView.getDrawable())){
                                which = j;
                                break;
                            }
                        }
                        if(which!=-1){
                            LinearLayout patchouli = (LinearLayout) findViewById(R.id.list);
                            LinearLayout knowledge = new LinearLayout(this);
                            knowledge.setOrientation(LinearLayout.HORIZONTAL);
                            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
                            param.setMargins(50, 50, 30, 0);
                            knowledge.setLayoutParams(param);
                            knowledge.setGravity(Gravity.CENTER_VERTICAL);
                            ImageView img = new ImageView(this);
                            img.setImageDrawable(DragNDrop.drawables[which]);
                            img.setScaleX(0.5f);
                            img.setScaleY(0.5f);
                            TextView text = new TextView(this);
                            text.setText("Time: " + i + ":00 Activity Name: " + DragNDrop.drawableDesc[which]);
                            text.setTextSize(16);
                            knowledge.addView(img);
                            knowledge.addView(text);
                            patchouli.addView(knowledge);
                        }
                    }
                }
            }
            if (resultCode == RESULT_CANCELED) {
                // Write your code if there's no result
            }
        }
    }
}
