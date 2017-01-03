package com.relife.relife;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Activities extends Activity {

    DatabaseHelper helper;
    SQLiteDatabase db;
    String selectedImagePath;

    void showMyActivities(){
        LinearLayout layout = (LinearLayout) findViewById(R.id.actactact);
        Cursor cursor = helper.listActivities(db, getApplicationContext());
        if(cursor == null)
            return;
        int size = cursor.getCount();
        String[] drawables = new String[size];
        String[] drawableDesc = new String[size];
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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities);
        helper=new DatabaseHelper(this);
        db=helper.getWritableDatabase();

        findViewById(R.id.lordofcinder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Activity Picture"), 1);
            }
        });
        findViewById(R.id.firekeeper).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Add database
                EditText nameText = (EditText) findViewById(R.id.actname);
                String name = nameText.getText().toString();
                if(selectedImagePath == null){
                    Toast toast = Toast.makeText(getApplicationContext(), "Select an image you must", Toast.LENGTH_SHORT);
                    toast.show();
                }
                helper.insertActivity(db, name, selectedImagePath);
                finish();
            }
        });
        showMyActivities();
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
            }
        }
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
