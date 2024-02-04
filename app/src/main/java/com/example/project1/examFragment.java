package com.example.project1;

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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class examFragment extends Fragment implements RecyclerViewInterface {

    private ArrayList<String> inputArray;
    private ArrayList<ListDataClass> rowData = new ArrayList<>();
    private RecyclerViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exam, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.exams_recycler_view);
        readItems();
        adapter = new RecyclerViewAdapter(getActivity(), rowData, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.exams_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity().getApplicationContext(), examsPop.class);
                startActivityForResult(i,3);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 3) {
            if (resultCode == Activity.RESULT_OK) {
                inputArray = data.getStringArrayListExtra("Exams Array");
                rowData.add(new ListDataClass(inputArray.get(0), inputArray.get(1), inputArray.get(2), inputArray.get(3)));
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
        File assignmentsFile = new File(filesDir, "exams.bin");
        try (FileInputStream fis = new FileInputStream(assignmentsFile);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            rowData = (ArrayList<ListDataClass>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            rowData = new ArrayList<>();
        }
    }

    private void writeItem() {
        File filesDir = requireContext().getFilesDir();
        File assignmentsFile = new File(filesDir, "exams.bin");
        try (FileOutputStream fos = new FileOutputStream(assignmentsFile);
             ObjectOutputStream oos = new ObjectOutputStream(fos)){
            oos.writeObject(rowData);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}