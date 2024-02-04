package com.example.project1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class examsPop extends Activity {

    private ArrayList<String> textInput = new ArrayList<>();
    private EditText examName, examLocation, examTime;
    private Button examDate;
    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exams_pop);
        initDatePicker();

        Button btn_close = findViewById(R.id.exam_close);
        examName = findViewById(R.id.examNameInput);
        examLocation = findViewById(R.id.examLocationInput);
        examTime = findViewById(R.id.examTimeInput);
        examDate = findViewById(R.id.examDateInput);

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameString = examName.getText().toString();
                String locationString = examLocation.getText().toString();
                String timeString = examTime.getText().toString();
                String dateString = examDate.getText().toString();

                if (nameString.trim().equals("")) {
                    Toast.makeText(getBaseContext(), "Exam name cannot be empty", Toast.LENGTH_SHORT).show();
                } else if (locationString.trim().equals("")) {
                    Toast.makeText(getBaseContext(), "Exam location cannot be empty", Toast.LENGTH_SHORT).show();
                } else if (timeString.trim().equals("")) {
                    Toast.makeText(getBaseContext(), "Exam time cannot be empty", Toast.LENGTH_SHORT).show();
                } else if (dateString.trim().equals("")) {
                    Toast.makeText(getBaseContext(), "Exam date cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    textInput.add(0, nameString);
                    textInput.add(1, locationString);
                    textInput.add(2, timeString);
                    textInput.add(3, dateString);

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("Exams Array", textInput);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }

            }
        });

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int)(width * 0.9), (int)(height * 0.8));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -100;

        getWindow().setAttributes(params);

    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = makeDateString(dayOfMonth, month, year);
                examDate.setText(date);

            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_DEVICE_DEFAULT_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    private String makeDateString(int dayOfMonth, int month, int year) {
        return getMonthFormat(month) + " " + dayOfMonth + " " + year;
    }

    private String getMonthFormat(int month) {
        if (month == 1) {
            return "JAN";
        }
        if (month == 2) {
            return "FEB";
        }
        if (month == 3) {
            return "MAR";
        }
        if (month == 4) {
            return "APR";
        }
        if (month == 5) {
            return "MAY";
        }
        if (month == 6) {
            return "JUN";
        }
        if (month == 7) {
            return "JUL";
        }
        if (month == 8) {
            return "AUG";
        }
        if (month == 9) {
            return "SEP";
        }
        if (month == 10) {
            return "AUG";
        }
        if (month == 11) {
            return "NOV";
        }
        if (month == 12) {
            return "DEC";
        }
        return "JAN";
    }

    public void onExamDatePicker(View view) {
        datePickerDialog.show();
    }
}