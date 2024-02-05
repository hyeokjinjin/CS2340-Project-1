package com.example.project1.fragments;

import android.app.Activity;
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
import java.util.Comparator;

public class assignmentsFragment extends Fragment implements RecyclerViewInterface {

    private ArrayList<ListDataClass> rowData;
    private RecyclerViewAdapter adapter;

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
                Intent i = new Intent(getActivity().getApplicationContext(), assignmentsPop.class);
                startActivityForResult(i, 2);
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

    // Code that will create a new item on the RecyclerView (assignment list) when Activity (pop-up window) is finished.
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                ArrayList<String> inputArray = data.getStringArrayListExtra("Assignments Array");
                String[] date = inputArray.get(1).split("/");
                rowData.add(new ListDataClass(inputArray.get(0), inputArray.get(1), inputArray.get(2),
                        Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2])));
                adapter.notifyItemInserted(adapter.getItemCount());
                writeItem();
             }
        }
    }

    // Code that will remove the assignment on long click.
    @Override
    public void onItemLongClick(int position) {
        rowData.remove(position);
        writeItem();
        adapter.notifyItemRemoved(position);
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

}