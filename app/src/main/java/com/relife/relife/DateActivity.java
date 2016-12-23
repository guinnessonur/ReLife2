package com.relife.relife;

import android.content.Intent;
import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;

public class DateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);
        CalendarView cv = (CalendarView) findViewById(R.id.calendarView);
        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int day) {
                Intent bad_intention = new Intent(DateActivity.this, DragNDrop.class);
                bad_intention.putExtra("day", day);
                bad_intention.putExtra("month", month);
                bad_intention.putExtra("year", year);
                startActivity(bad_intention);
            }
        });
    }
}
