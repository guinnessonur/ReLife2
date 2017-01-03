package com.relife.relife;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
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
    String[] drawables;
    String[] drawableDesc;

    DatabaseHelper helper;
    SQLiteDatabase db;

    int day;
    int month;
    int year;

    void showMyActivities(){
        LinearLayout layout = new LinearLayout(this);
        LinearLayout.LayoutParams paramm = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layout.setOrientation(LinearLayout.VERTICAL);
        Cursor cursor = helper.listActivities(db, getApplicationContext());
        if(cursor == null)
            return;
        int size = cursor.getCount();
        drawables = new String[size];
        drawableDesc = new String[size];
        cursor.moveToFirst();
        for(int i = 0; i < size; i++){
            drawables[i] = cursor.getString(1);
            drawableDesc[i] = cursor.getString(0);
            cursor.moveToNext();
        }
        cursor.close();
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
            img.setImageURI(Uri.parse(drawables[i]));
            img.setScaleX(0.5f);
            img.setScaleY(0.5f);
            img.setOnTouchListener(new touchListener());
            TextView text = new TextView(this);
            text.setText("Activity Name: " + drawableDesc[i]);
            text.setTextSize(16);
            ll.addView(img);
            ll.addView(text);
            layout.addView(ll);
        }
        ScrollView sv = (ScrollView) findViewById(R.id.activities);
        sv.removeAllViews();
        sv.addView(layout);
    }

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
        showMyActivities();
        Button button = (Button) findViewById(R.id.done);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i < layouts.length; i++){
                    View view = DragNDrop.layouts[i].getChildAt(0);
                    if (view instanceof ImageView) {
                        ImageView imageView = (ImageView) view;
                        int which = -1;
                        //Uri.parse(drawables[which])
                        for(int j = 0; j < drawables.length; j++){
                            ImageView anri = new ImageView(context);
                            anri.setImageURI(Uri.parse(drawables[j]));

                            anri.setDrawingCacheEnabled(true);
                            anri.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                            anri.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
                            anri.buildDrawingCache(true);

                            imageView.setDrawingCacheEnabled(true);
                            imageView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                            imageView.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
                            imageView.buildDrawingCache(true);

                            if(anri.getDrawingCache().sameAs(imageView.getDrawingCache())){
                                which = j;
                                break;
                            }
                            anri.setDrawingCacheEnabled(false);
                            imageView.setDrawingCacheEnabled(false);
                        }
                        Log.v("Which", which + "");
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
