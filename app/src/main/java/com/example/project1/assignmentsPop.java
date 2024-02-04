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

public class assignmentsPop extends Activity {

    Button btn_close;
    ArrayList<String> textInput = new ArrayList<>(3);
    EditText assignmentName;
    EditText assignmentDueDate;
    EditText assignmentClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignments_pop);

        btn_close = (Button) findViewById(R.id.assignments_close);
        assignmentName = (EditText) findViewById(R.id.assignmentsNameInput);
        assignmentDueDate = (EditText) findViewById(R.id.assignmentsDateInput);
        assignmentClass = (EditText) findViewById(R.id.assignmentsClassInput);

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameString = assignmentName.getText().toString();
                String dateString = assignmentDueDate.getText().toString();
                String classString = assignmentClass.getText().toString();

                if (nameString.trim().equals("")) {
                    Toast.makeText(getBaseContext(), "Assignment name cannot be empty", Toast.LENGTH_SHORT).show();
                } else if (dateString.trim().equals("")) {
                    Toast.makeText(getBaseContext(), "Assignment due date cannot be empty", Toast.LENGTH_SHORT).show();
                } else if (classString.trim().equals("")) {
                    Toast.makeText(getBaseContext(), "Assignment's class cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    textInput.add(0, nameString);
                    textInput.add(1, dateString);
                    textInput.add(2, classString);

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("Assignments Array", textInput);
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