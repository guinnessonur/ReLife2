package com.relife.relife;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

//saaa
//asss
public class DragNDrop extends Activity {

    static LinearLayout[] layouts = new LinearLayout[24];
    static TextView[] textViews = new TextView[24];
    static int lastLayout = -1;
    static Context context;
    static Drawable[] drawables = new Drawable[6];
    static String[] drawableDesc = new String[6];

    DatabaseHelper helper;
    SQLiteDatabase db;

    int day;
    int month;
    int year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_ndrop);
        helper = new DatabaseHelper(this);
        db = helper.getWritableDatabase();
        context = getApplicationContext();
        Intent good_intention = getIntent();
        year = good_intention.getIntExtra("year", 1996);
        month = good_intention.getIntExtra("month", 2);
        day = good_intention.getIntExtra("day", 25);
        TextView textDate = (TextView) findViewById(R.id.textDate);
        textDate.setText(day + " . " + month + " . " + year);
        for(int i = 0; i < layouts.length; i++){
            int resID = getResources().getIdentifier("i"+i, "id", getPackageName());
            layouts[i] = (LinearLayout) findViewById(resID);
        }
        for(int i = 0; i < layouts.length; i++){
            TextView tv = new TextView(this);
            tv.setText(i + "");
            tv.setTextColor(Color.parseColor("#9D5B4D"));
            tv.setTextSize(18);
            tv.setGravity(Gravity.CENTER);
            layouts[i].setGravity(Gravity.CENTER);
            layouts[i].addView(tv);
            textViews[i] = tv;
        }
        for(int i = 0; i < layouts.length; i++){
            layouts[i].setOnDragListener(new dragListener());
        }
        LinearLayout scrollLayout = new LinearLayout(this);
        scrollLayout.setOrientation(LinearLayout.VERTICAL);
        drawables[0] = getDrawable(R.drawable.sleep);
        drawables[1] = getDrawable(R.drawable.gamecontroller);
        drawables[2] = getDrawable(R.drawable.openbook);
        drawables[3] = getDrawable(R.drawable.knight);
        drawables[4] = getDrawable(R.drawable.soccer);
        drawables[5] = getDrawable(R.drawable.dumbbell);
        drawableDesc[0] = "Sleep";
        drawableDesc[1] = "Game";
        drawableDesc[2] = "Study";
        drawableDesc[3] = "Chess";
        drawableDesc[4] = "Soccer";
        drawableDesc[5] = "Work Out";
        for(int i = 0; i < drawables.length; i++){
            LinearLayout ll = new LinearLayout(this);
            ll.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
            param.setMargins(10, 0, 0, 0);
            ll.setLayoutParams(param);
            ll.setGravity(Gravity.CENTER_VERTICAL);
            ImageView img = new ImageView(this);
            img.setImageDrawable(drawables[i]);
            img.setScaleX(0.5f);
            img.setScaleY(0.5f);
            img.setOnTouchListener(new touchListener());
            TextView text = new TextView(this);
            text.setText("Activity Name: " + drawableDesc[i]);
            text.setTextSize(16);
            ll.addView(img);
            ll.addView(text);
            scrollLayout.addView(ll);
        }
        ScrollView scrollView = (ScrollView) findViewById(R.id.activities);
        scrollView.addView(scrollLayout);
        Button button = (Button) findViewById(R.id.done);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i < layouts.length; i++){
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
                        if(which != -1){
                            helper.insertSchedule(db, drawableDesc[which], which, i, day, month, year);
                            Log.v("insertDatabase", drawableDesc[which] + " " + which + " " + i + " " + day + " " + month + " " + year);
                        }
                    }
                }
                finish();
            }
        });
    }
}
class touchListener implements View.OnTouchListener {
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            ClipData data = ClipData.newPlainText("", " ");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
            v.startDrag(data, shadowBuilder, v, 0);
            v.setVisibility(View.VISIBLE);
            return true;
        }
        else{
            return false;
        }
    }
}
class dragListener implements View.OnDragListener {
    @Override
    public boolean onDrag(View v, DragEvent event) {
        int action = event.getAction();
        switch (event.getAction()){
            case DragEvent.ACTION_DRAG_STARTED:
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                break;
            case DragEvent.ACTION_DROP:
                ImageView view = (ImageView) event.getLocalState();
                LinearLayout container = (LinearLayout) v;
                container.removeAllViews();
                ImageView imageView = new ImageView(DragNDrop.context);
                imageView.setImageDrawable(view.getDrawable());
                imageView.setScaleX(0.5f);
                imageView.setScaleY(0.5f);
                container.addView(imageView);
                view.setVisibility(View.VISIBLE);
                break;
            case DragEvent.ACTION_DRAG_ENDED:
            default:
                    break;
        }
        return true;
    }
}
