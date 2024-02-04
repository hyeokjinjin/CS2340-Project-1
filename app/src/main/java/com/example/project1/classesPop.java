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

public class classesPop extends Activity {

    private ArrayList<String> textInput = new ArrayList<>(3);
    private EditText className;
    private EditText classTime;
    private EditText classInstructor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes_pop);

        Button btn_close = (Button) findViewById(R.id.class_close);
        className = (EditText) findViewById(R.id.classNameInput);
        classTime = (EditText) findViewById(R.id.classTimeInput);
        classInstructor = (EditText) findViewById(R.id.classInstructorInput);

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameString = className.getText().toString();
                String dateString = classTime.getText().toString();
                String locationString = classInstructor.getText().toString();

                if (nameString.trim().equals("")) {
                    Toast.makeText(getBaseContext(), "Class name cannot be empty", Toast.LENGTH_SHORT).show();
                } else if (dateString.trim().equals("")) {
                    Toast.makeText(getBaseContext(), "Class time cannot be empty", Toast.LENGTH_SHORT).show();
                } else if (locationString.trim().equals("")) {
                    Toast.makeText(getBaseContext(), "Class instructor cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    textInput.add(0, nameString);
                    textInput.add(1, dateString);
                    textInput.add(2, locationString);

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("Classes Array", textInput);
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