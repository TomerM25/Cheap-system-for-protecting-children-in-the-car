package com.example.settimeactivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button dateButton,timeButton;
    TextView dateTextView, timeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dateButton=findViewById(R.id.dateButton);
        timeButton=findViewById(R.id.timeButton);
        dateTextView=findViewById(R.id.dateTextView);
        timeTextView=findViewById(R.id.timeTextView);

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleDateButton();
            }
        });
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleTimeButton();
            }
        });
    }
    private void handleTimeButton() {
        final Calendar calendar = Calendar.getInstance();
        int Hour = calendar.get(Calendar.HOUR);
        int MINUTE =calendar.get(Calendar.MINUTE);

        boolean is24HourFormat = DateFormat.is24HourFormat(this);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                //String timeString = hour + ":" + minute;
                //timeTextView.setText(timeString);

                Calendar calendar1= Calendar.getInstance();
                calendar1.set(Calendar.HOUR , hour);
                calendar1.set(calendar.MINUTE, minute);

                CharSequence timecharSequence = DateFormat.format("hh:mm a",calendar1);
                timeTextView.setText(timecharSequence);
            }
        }, Hour, MINUTE, is24HourFormat);
        timePickerDialog.show();
    }
    private void handleDateButton() {

        final Calendar calendar = Calendar.getInstance();
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH =calendar.get(Calendar.MONTH);
        int DAY =calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String dateString = day + " " + month +" "+ year;
                dateTextView.setText(dateString);

                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.YEAR,year);
                calendar1.set(Calendar.MONTH,month);
                calendar1.set(Calendar.DATE,day);

                CharSequence dateCharSequence = DateFormat.format("d MMM, yyyy",calendar1 );
                dateTextView.setText(dateCharSequence);
            }
        }, DAY, MONTH, YEAR);
        datePickerDialog.show();

    }
}
