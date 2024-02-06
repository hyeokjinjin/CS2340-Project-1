package com.example.project1.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
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
import com.example.project1.popupWindows.assignmentsPop;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;

public class assignmentsFragment extends Fragment implements RecyclerViewInterface {

    private ArrayList<ListDataClass> rowData;
    private RecyclerViewAdapter adapter;
    private DatePickerDialog datePickerDialog;
    private EditText assignmentName, assignmentClass;
    private Button dateButton, btn_close;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_assignments, container, false);

        // Code for RecyclerView (List with the cards that have assignments information).
        RecyclerView recyclerView = view.findViewById(R.id.assignments_recycler_view);
        readItems();
        adapter = new RecyclerViewAdapter(getActivity(), rowData, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Code for the Floating Action Button that allows user to add more assignments.
        FloatingActionButton fab = view.findViewById(R.id.assignments_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            // Code that creates a new Activity (pop-up window) when button is clicked.
            public void onClick(View view) {
                Dialog dialog = dialogHelper(view, false, 0);
                dialog.show();
            }
        });



        // Code for button that will sort assignments by class.
        Button sortClassButton = view.findViewById(R.id.sortClass);
        sortClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rowData = classSort(rowData);
                adapter.notifyDataSetChanged();
                writeItem();
            }
        });

        // Code for button that will sort assignments by date.
        Button sortDateButton = view.findViewById(R.id.sortDate);
        sortDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rowData = dateSort(rowData);
                adapter.notifyDataSetChanged();
                writeItem();
            }
        });

        return view;
    }




    // Method that will sort the arrayList by class.
    private ArrayList<ListDataClass> classSort(ArrayList<ListDataClass> list) {
        list.sort(new Comparator<ListDataClass>() {
            @Override
            public int compare(ListDataClass o1, ListDataClass o2) {
                return o1.getSubhead2().compareToIgnoreCase(o2.getSubhead2());
            }
        });
        return list;
    }

    // Method that will sort the arrayList by date.
    private ArrayList<ListDataClass> dateSort(ArrayList<ListDataClass> list) {
        list.sort(new Comparator<ListDataClass>() {
            @Override
            public int compare(ListDataClass o1, ListDataClass o2) {
                if ((o1.getYear().compareTo(o2.getYear()) == 0) && (o1.getMonth().compareTo(o2.getMonth()) == 0)) {
                    return o1.getDay().compareTo(o2.getDay());
                } else if (o1.getYear().compareTo(o2.getYear()) == 0) {
                    return o1.getMonth().compareTo(o2.getMonth());
                } else {
                    return o1.getYear().compareTo(o2.getYear());
                }
            }
        });
        return list;
    }




    // Code that will remove the assignment on long click.
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
        File assignmentsFile = new File(filesDir, "assignments.bin");
        try (FileInputStream fis = new FileInputStream(assignmentsFile);
            ObjectInputStream ois = new ObjectInputStream(fis)) {
            rowData = (ArrayList<ListDataClass>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            rowData = new ArrayList<>();
        }
    }

    // Code that will write the items to a file to be cached.
    private void writeItem() {
        File filesDir = requireContext().getFilesDir();
        File assignmentsFile = new File(filesDir, "assignments.bin");
        try (FileOutputStream fos = new FileOutputStream(assignmentsFile);
             ObjectOutputStream oos = new ObjectOutputStream(fos)){
            oos.writeObject(rowData);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = makeDateString(dayOfMonth, month, year);
                dateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_DEVICE_DEFAULT_LIGHT;

        datePickerDialog = new DatePickerDialog(getActivity(), style, dateSetListener, year, month, day);
    }

    private String makeDateString(int dayOfMonth, int month, int year) {
        return month + "/" + dayOfMonth + "/" + year;
    }

    public void onAssignmentDatePicker(View view) {
        datePickerDialog.show();
    }




    private Dialog dialogHelper(View view, boolean change, int position) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.activity_assignments_pop);
        initDatePicker();

        viewInitializer(dialog, change, position);

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAssignmentDatePicker(view);
            }
        });

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameString = assignmentName.getText().toString().trim();
                String dateString = dateButton.getText().toString().trim();
                String classString = assignmentClass.getText().toString().trim();

                if (nameString.trim().equals("")) {
                    Toast.makeText(getActivity(), "Assignment name cannot be empty", Toast.LENGTH_SHORT).show();
                } else if (dateString.trim().equals("Set Assignment Due Date")) {
                    Toast.makeText(getActivity(), "Assignment due date cannot be empty", Toast.LENGTH_SHORT).show();
                } else if (classString.trim().equals("")) {
                    Toast.makeText(getActivity(), "Assignment's class cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    String[] date = dateString.split("/");
                    if (change) {
                        rowData.set(position, new ListDataClass(nameString, dateString, classString, Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2])));
                        adapter.notifyItemChanged(position);
                    } else {
                        rowData.add(new ListDataClass(nameString, dateString, classString, Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2])));
                        adapter.notifyItemInserted(adapter.getItemCount());
                    }
                    writeItem();
                    dialog.dismiss();
                }

            }
        });
        return dialog;
    }

    private void viewInitializer(Dialog dialog, boolean change, int position) {
        assignmentName = dialog.findViewById(R.id.assignmentsNameInput);
        assignmentClass = dialog.findViewById(R.id.assignmentsClassInput);
        dateButton = dialog.findViewById(R.id.assignmentsDateInput);
        btn_close = dialog.findViewById(R.id.assignments_close);

        if (change) {
            TextView title = dialog.findViewById(R.id.newAssignment);
            title.setText("Edit Assignment");
            btn_close.setText("Update");
            assignmentName.setText(rowData.get(position).getHeading());
            assignmentClass.setText(rowData.get(position).getSubhead2());
            dateButton.setText(rowData.get(position).getSubhead1());
        }
    }

}


