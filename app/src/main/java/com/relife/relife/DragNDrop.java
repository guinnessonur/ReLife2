package com.relife.relife;

import android.app.Activity;
import android.content.ClipData;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class DragNDrop extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_ndrop);
        findViewById(R.id.myimage1).setOnTouchListener(new touchListener());
        findViewById(R.id.myimage2).setOnTouchListener(new touchListener());
        findViewById(R.id.myimage3).setOnTouchListener(new touchListener());
        findViewById(R.id.myimage4).setOnTouchListener(new touchListener());
        findViewById(R.id.topleft).setOnDragListener(new dragListener());
        findViewById(R.id.topright).setOnDragListener(new dragListener());
        findViewById(R.id.bottomleft).setOnDragListener(new dragListener());
        findViewById(R.id.bottomright).setOnDragListener(new dragListener());
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
