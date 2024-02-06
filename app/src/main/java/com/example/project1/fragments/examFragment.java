package com.example.project1.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project1.helperClasses.ListDataClass;
import com.example.project1.R;
import com.example.project1.helperClasses.RecyclerViewAdapter;
import com.example.project1.helperClasses.RecyclerViewInterface;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class examFragment extends Fragment implements RecyclerViewInterface {

    private ArrayList<ListDataClass> rowData = new ArrayList<>();
    private RecyclerViewAdapter adapter;
    private DatePickerDialog datePickerDialog;
    private EditText examName, examLocation, examTime;
    private Button examDate, btn_close;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exam, container, false);

        // Code for RecyclerView (List with the cards that have exam information).
        RecyclerView recyclerView = view.findViewById(R.id.exams_recycler_view);
        readItems();
        adapter = new RecyclerViewAdapter(getActivity(), rowData, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Code for the Floating Action Button that allows user to add more exams.
        FloatingActionButton fab = view.findViewById(R.id.exams_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            // Code that creates a new Activity (pop-up window) when button is clicked.
            public void onClick(View view) {
//                Intent i = new Intent(getActivity().getApplicationContext(), examsPop.class);
//                startActivityForResult(i,3);
                Dialog dialog = dialogHelper(view, false, 0);
                dialog.show();
            }
        });

        return view;
    }




    // Code that will remove the exam on long click.
    @Override
    public void onItemLongClick(int position) {
        rowData.remove(position);
        writeItem();
        adapter.notifyItemRemoved(position);
    }

    // Code that will edit the assignment on click.
    @Override
    public void onItemClick(int position, View view) {
        Dialog dialog = dialogHelper(view, true, position);
        dialog.show();
    }




    // Code that will read the cached items from file and add to RecyclerView when fragment opened.
    private void readItems() {
        File filesDir = requireContext().getFilesDir();
        File examsFile = new File(filesDir, "exams.bin");
        try (FileInputStream fis = new FileInputStream(examsFile);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            rowData = (ArrayList<ListDataClass>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            rowData = new ArrayList<>();
        }
    }

    // Code that will write the items to a file to be cached.
    private void writeItem() {
        File filesDir = requireContext().getFilesDir();
        File examsFile = new File(filesDir, "exams.bin");
        try (FileOutputStream fos = new FileOutputStream(examsFile);
             ObjectOutputStream oos = new ObjectOutputStream(fos)){
            oos.writeObject(rowData);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    // Code that will allow the date picker widget to work when dialog box opens
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

        datePickerDialog = new DatePickerDialog(getActivity(), style, dateSetListener, year, month, day);
    }

    // Helper method to create string for date
    private String makeDateString(int dayOfMonth, int month, int year) {
        return month +  "/" + dayOfMonth + "/" + year;
    }

    // Method that shows the date picker
    public void onExamDatePicker(View view) {
        datePickerDialog.show();
    }




    // Helper method that creates the dialog box and listens for the input from user
    private Dialog dialogHelper(View view, boolean change, int position) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.activity_exams_pop);
        initDatePicker();

        viewInitializer(dialog, change, position);

        examDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onExamDatePicker(view);
            }
        });

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameString = examName.getText().toString().trim();
                String locationString = examLocation.getText().toString().trim();
                String timeString = examTime.getText().toString().trim();
                String dateString = examDate.getText().toString().trim();

                if (nameString.trim().equals("")) {
                    Toast.makeText(getActivity(), "Exam name cannot be empty", Toast.LENGTH_SHORT).show();
                } else if (locationString.trim().equals("")) {
                    Toast.makeText(getActivity(), "Exam location cannot be empty", Toast.LENGTH_SHORT).show();
                } else if (timeString.trim().equals("")) {
                    Toast.makeText(getActivity(), "Exam time cannot be empty", Toast.LENGTH_SHORT).show();
                } else if (dateString.trim().equals("")) {
                    Toast.makeText(getActivity(), "Exam date cannot be empty", Toast.LENGTH_SHORT).show();
                }else {
                    if (change) {
                        rowData.set(position, new ListDataClass(nameString, locationString, timeString, dateString));
                        adapter.notifyItemChanged(position);
                    } else {
                        rowData.add(new ListDataClass(nameString, locationString, timeString, dateString));
                        adapter.notifyItemInserted(adapter.getItemCount());
                    }
                    writeItem();
                    dialog.dismiss();
                }

            }
        });
        return dialog;
    }

    // Initializes the dialog box and added all the views needed
    private void viewInitializer(Dialog dialog, boolean change, int position) {
        examName = dialog.findViewById(R.id.examNameInput);
        examLocation = dialog.findViewById(R.id.examLocationInput);
        examTime = dialog.findViewById(R.id.examTimeInput);
        examDate = dialog.findViewById(R.id.examDateInput);
        btn_close = dialog.findViewById(R.id.exam_close);

        if (change) {
            TextView title = dialog.findViewById(R.id.newExam);
            title.setText("Edit Exam");
            btn_close.setText("Update");
            examName.setText(rowData.get(position).getHeading());
            examLocation.setText(rowData.get(position).getSubhead1());
            examTime.setText(rowData.get(position).getSubhead2());
            examDate.setText(rowData.get(position).getSubhead3());
        }
    }

}