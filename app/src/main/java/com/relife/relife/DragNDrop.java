package com.relife.relife;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_ndrop);
        context = getApplicationContext();
        //ImageView image = (ImageView) findViewById(R.id.imageView3);
        //image.setImageResource(R.mipmap.ic_launcher);
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
            param.setMargins(50, 50, 30, 0);
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
                Intent bad_intention = new Intent();
                bad_intention.putExtra("result", "your message");
                setResult(RESULT_OK, bad_intention);
                finish();
            }
        });
        //findViewById(R.id.imageView3).setOnTouchListener(new touchListener());
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
//                if(DragNDrop.lastLayout != -1){
//                    owner.addView(DragNDrop.textViews[DragNDrop.lastLayout]);
//                }
//
//                for(int i = 0; i < DragNDrop.layouts.length; i++){
//                    if(container.getId() == DragNDrop.layouts[i].getId()){
//                        DragNDrop.lastLayout = i;
//                        break;
//                    }
//                }
                view.setVisibility(View.VISIBLE);
                break;
            case DragEvent.ACTION_DRAG_ENDED:
            default:
                    break;
        }
        return true;
    }



}
