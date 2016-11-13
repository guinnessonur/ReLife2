package com.relife.relife;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.TextView;

/**
 * Created by GURKAN32 on 11/6/2016.
 */

public class DragDrop extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_drop);
        Intent activityThatCalled=getIntent();
        findViewById(R.id.textapple).setOnLongClickListener(longListen);
        findViewById(R.id.textorange).setOnLongClickListener(longListen);
        findViewById(R.id.textdragon).setOnLongClickListener(longListen);
        findViewById(R.id.textpear).setOnLongClickListener(longListen);
    }

    View.OnLongClickListener longListen =new View.OnLongClickListener(){
        @Override
        public boolean onLongClick(View v) {

            DragShadow dragShadow=new DragShadow(v);

            ClipData data=ClipData.newPlainText("","");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                v.startDragAndDrop(data,dragShadow,v,0);
            } else {
               v.startDrag(data,dragShadow,v,0);
            }

            return false;
        }
    };

    private class DragShadow extends View.DragShadowBuilder{

        ColorDrawable greyBox;


        public DragShadow(View view) {
            super(view);
            greyBox=new ColorDrawable(Color.LTGRAY);
        }

        @Override
        public void onProvideShadowMetrics(Point outShadowSize, Point ShadowTouchPoint) {
            View v=getView();

            int height =(int) v.getHeight()/2;
            int width=(int) v.getWidth()/2;
            greyBox.setBounds(0,0,width,height);

            outShadowSize.set(width,height);
            ShadowTouchPoint.set((int) width/2, (int) height/2);
        }

        @Override
        public void onDrawShadow(Canvas canvas) {
            greyBox.draw(canvas);
        }
    }
    View.OnDragListener DropListener=new View.OnDragListener(){

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int dragEvent=event.getAction();
            switch (dragEvent){
                case DragEvent.ACTION_DRAG_ENTERED:
                    Log.i("Drag Event","Entered");
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    Log.i("Drag Event","Exited");
                    break;

                case DragEvent.ACTION_DROP:
                    TextView target =(TextView) v;
                    TextView dragged=(TextView)event.getLocalState();
                    target.setText(dragged.getText());
                    break;
            }



            return true;
        }
    };
}
