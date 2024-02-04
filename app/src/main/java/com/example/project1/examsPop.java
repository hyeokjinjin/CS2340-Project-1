package com.example.project1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class examsPop extends Activity {

    private ArrayList<String> textInput = new ArrayList<>(3);
    private EditText examName;
    private EditText examDate;
    private EditText examLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exams_pop);

        Button btn_close = (Button) findViewById(R.id.exam_close);
        examName = (EditText) findViewById(R.id.examNameInput);
        examDate = (EditText) findViewById(R.id.examDateTimeInput);
        examLocation = (EditText) findViewById(R.id.examLocationInput);


        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameString = examName.getText().toString();
                String dateString = examDate.getText().toString();
                String locationString = examLocation.getText().toString();

                if (nameString.trim().equals("")) {
                    Toast.makeText(getBaseContext(), "Exam name cannot be empty", Toast.LENGTH_SHORT).show();
                } else if (dateString.trim().equals("")) {
                    Toast.makeText(getBaseContext(), "Exam date and time cannot be empty", Toast.LENGTH_SHORT).show();
                } else if (locationString.trim().equals("")) {
                    Toast.makeText(getBaseContext(), "Exam location cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    textInput.add(0, nameString);
                    textInput.add(1, dateString);
                    textInput.add(2, locationString);

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
}