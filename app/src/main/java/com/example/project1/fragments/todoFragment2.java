package com.example.project1.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project1.R;
import com.example.project1.databinding.FragmentTodo2Binding;
import com.example.project1.databinding.FragmentTodoBinding;
import com.example.project1.helperClasses.ListDataClass;
import com.example.project1.helperClasses.RecyclerViewAdapter;
import com.example.project1.helperClasses.RecyclerViewInterface;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


public class todoFragment2 extends Fragment implements RecyclerViewInterface {
    private ArrayList<ListDataClass> rowData = new ArrayList<>();
    private RecyclerViewAdapter adapter;
    private EditText taskTitle;
    private Button btn_close;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo_2, container, false);

        // Code to set up how the recycler view for the list.
        RecyclerView recyclerView = view.findViewById(R.id.list2_recyclerview);
        readItems();
        adapter = new RecyclerViewAdapter(getActivity(), rowData, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        FloatingActionButton fab = view.findViewById(R.id.list2fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = dialogHelper(view, false, 0);
                dialog.show();
            }
        });

        return view;
    }



    // Code that will remove the task on long click.
    @Override
    public void onItemLongClick(int position) {
        rowData.remove(position);
        writeItem();
        adapter.notifyItemRemoved(position);
    }

    // Code that will remove task on click.
    @Override
    public void onItemClick(int position, View view) {
        Dialog dialog = dialogHelper(view, true, position);
        dialog.show();
    }



    // Code that will read the cached items from file and add to RecyclerView when fragment opened.
    private void readItems() {
        File filesDir = requireContext().getFilesDir();
        File todoListTwoFile = new File(filesDir, "todoList2.bin");
        try (FileInputStream fis = new FileInputStream(todoListTwoFile);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            rowData = (ArrayList<ListDataClass>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            rowData = new ArrayList<>();
        }
    }

    // Code that will write the items to a file to be cached.
    private void writeItem() {
        File filesDir = requireContext().getFilesDir();
        File todoListTwoFile = new File(filesDir, "todoList2.bin");
        try (FileOutputStream fos = new FileOutputStream(todoListTwoFile);
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
        dialog.setContentView(R.layout.activity_todo_pop);

        viewInitializer(dialog, change, position);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskString = taskTitle.getText().toString().trim();

                if (taskString.trim().equals("")) {
                    Toast.makeText(getActivity(), "Task cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    if (change) {
                        rowData.set(position, new ListDataClass(taskString, "", ""));
                        adapter.notifyItemChanged(position);
                    } else {
                        rowData.add(new ListDataClass(taskString, "", ""));
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
        taskTitle = dialog.findViewById(R.id.taskInput);
        btn_close = dialog.findViewById(R.id.todo_close);

        if (change) {
            TextView title = dialog.findViewById(R.id.newTask);
            title.setText("Edit Task");
            btn_close.setText("Update");
            taskTitle.setText(rowData.get(position).getHeading());
        }
    }

}