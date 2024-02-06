package com.example.project1.fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class classFragment extends Fragment implements RecyclerViewInterface {

    private ArrayList<ListDataClass> rowData;
    private RecyclerViewAdapter adapter;
    private EditText className, classTime, classInstructor;
    private Button btn_close;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_class, container, false);

        // Code for RecyclerView (List with the cards that have class information).
        RecyclerView recyclerView = view.findViewById(R.id.classes_recycler_view);
        readItems();
        adapter = new RecyclerViewAdapter(getActivity(), rowData, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Code for the Floating Action Button that allows user to add more classes.
        FloatingActionButton fab = view.findViewById(R.id.classes_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            // Code that creates a new Activity (pop-up window) when button is clicked.
            public void onClick(View view) {
                Dialog dialog = dialogHelper(view, false, 0);
                dialog.show();
            }
        });

        return view;
    }




    // Code that will remove the class on long click.
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
        File classesFile = new File(filesDir, "classes.bin");
        try (FileInputStream fis = new FileInputStream(classesFile);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            rowData = (ArrayList<ListDataClass>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            rowData = new ArrayList<>();
        }
    }

    // Code that will write the items to a file to be cached.
    private void writeItem() {
        File filesDir = requireContext().getFilesDir();
        File classesFile = new File(filesDir, "classes.bin");
        try (FileOutputStream fos = new FileOutputStream(classesFile);
             ObjectOutputStream oos = new ObjectOutputStream(fos)){
            oos.writeObject(rowData);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    // Helper method that creates the dialog box and listens for the input from user
    private Dialog dialogHelper(View view, boolean change, int position) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.activity_classes_pop);

        viewInitializer(dialog, change, position);

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameString = className.getText().toString().trim();
                String timeString = classTime.getText().toString().trim();
                String instructorString = classInstructor.getText().toString().trim();

                if (nameString.trim().equals("")) {
                    Toast.makeText(getActivity(), "Class name cannot be empty", Toast.LENGTH_SHORT).show();
                } else if (timeString.trim().equals("")) {
                    Toast.makeText(getActivity(), "Class time cannot be empty", Toast.LENGTH_SHORT).show();
                } else if (instructorString.trim().equals("")) {
                    Toast.makeText(getActivity(), "Class instructor cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    if (change) {
                        rowData.set(position, new ListDataClass(nameString, timeString, instructorString));
                        adapter.notifyItemChanged(position);
                    } else {
                        rowData.add(new ListDataClass(nameString, timeString, instructorString));
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
        className = dialog.findViewById(R.id.classNameInput);
        classTime = dialog.findViewById(R.id.classTimeInput);
        classInstructor = dialog.findViewById(R.id.classInstructorInput);
        btn_close = dialog.findViewById(R.id.class_close);

        if (change) {
            TextView title = dialog.findViewById(R.id.newClass);
            title.setText("Edit Class");
            btn_close.setText("Update");
            className.setText(rowData.get(position).getHeading());
            classTime.setText(rowData.get(position).getSubhead1());
            classInstructor.setText(rowData.get(position).getSubhead2());
        }
    }

}