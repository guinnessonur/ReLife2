package com.relife.relife;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragEvent;
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

    LinearLayout[] layouts = new LinearLayout[24];

    static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_ndrop);
        ImageView image = (ImageView) findViewById(R.id.imageView3);
        image.setImageResource(R.mipmap.ic_launcher);
        for(int i = 0; i < layouts.length; i++){
            int resID = getResources().getIdentifier("i"+i, "id", getPackageName());
            layouts[i] = (LinearLayout) findViewById(resID);
        }
        for(int i = 0; i < layouts.length; i++){
            TextView tv = new TextView(this);
            tv.setText("" + i);
            tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            layouts[i].addView(tv);
        }
        for(int i = 0; i < layouts.length; i++){
            layouts[i].setOnDragListener(new dragListener());
        }
        findViewById(R.id.imageView3).setOnTouchListener(new touchListener());
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
                View view = (View) event.getLocalState();
                ViewGroup owner = (ViewGroup) view.getParent();
                owner.removeView(view);
                LinearLayout container = (LinearLayout) v;
                container.removeAllViews();
                container.addView(view);
                view.setVisibility(View.VISIBLE);
                break;
            case DragEvent.ACTION_DRAG_ENDED:
            default:
                    break;
        }
        return true;
    }



}
