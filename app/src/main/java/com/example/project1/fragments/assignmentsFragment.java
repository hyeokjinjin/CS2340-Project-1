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
import java.util.Collections;
import java.util.Comparator;

public class assignmentsFragment extends Fragment implements RecyclerViewInterface {

    private ArrayList<String> inputArray;
    private ArrayList<ListDataClass> rowData;
    private RecyclerViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_assignments, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.assignments_recycler_view);
        readItems();
        adapter = new RecyclerViewAdapter(getActivity(), rowData, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.assignments_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity().getApplicationContext(), assignmentsPop.class);
                startActivityForResult(i, 2);
            }
        });

        Button sortClassButton = view.findViewById(R.id.sortClass);
        sortClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rowData = classSort(rowData);
                adapter.notifyDataSetChanged();
                writeItem();
            }
        });

        Button sortDateButton = view.findViewById(R.id.sortDate);
        sortDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //rowData = dateSort(rowData);
                adapter.notifyDataSetChanged();
                writeItem();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                inputArray = data.getStringArrayListExtra("Assignments Array");
                rowData.add(new ListDataClass(inputArray.get(0), inputArray.get(1), inputArray.get(2)));
                adapter.notifyItemInserted(adapter.getItemCount());
                writeItem();
             }
        }
    }

    @Override
    public void onItemLongClick(int position) {
        rowData.remove(position);
        writeItem();
        adapter.notifyItemRemoved(position);
    }

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

    private ArrayList<ListDataClass> classSort(ArrayList<ListDataClass> list) {
        list.sort(new Comparator<ListDataClass>() {
            @Override
            public int compare(ListDataClass o1, ListDataClass o2) {
                return o1.getSubhead2().compareToIgnoreCase(o2.getSubhead2());
            }
        });
        return list;
    }

}