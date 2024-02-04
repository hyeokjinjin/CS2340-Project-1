package com.example.project1.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.project1.R;
import com.example.project1.databinding.FragmentTodoBinding;

import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class todoFragment extends Fragment {
    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;
    private FragmentTodoBinding binding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTodoBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // sets up list view
        lvItems = view.findViewById(R.id.lv_to_do);
        items = new ArrayList<>();
        readItems(); // read cached items
        itemsAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);

        // Call listener for long tap and hold for removing item
        setupListViewListener();

        // Set up the click listener for the "Add" button
        binding.buttonAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddItem(v);
            }
        });


        return view;
    }
//        View view = inflater.inflate(R.layout.fragment_todo, container, false);
//
//        binding = FragmentTodoBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//        //sets up list view
//        lvItems = (ListView) findViewById(R.id.lv_to_do);
//        items = new ArrayList<String>();
//        readItems(); //read cached items
//        itemsAdapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_1, items);
//        lvItems.setAdapter(itemsAdapter);
//
//        //Call listener for long tap and hold for removing item
//        setupListViewListener();
//
//        return view;
//    }

    // Next two blocks of code cache the data by reading and writing to/from memory
    private void readItems() {
        File filesDir = requireContext().getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            items = new ArrayList<>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<>();
        }
    }

    private void writeItems() {
        File filesDir = requireContext().getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Attaches a long click listener to the listview with purpose of removing
    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter,
                                                   View item, int pos, long id) {
                        // Remove the item within array at position
                        items.remove(pos);
                        // Refresh the adapter
                        itemsAdapter.notifyDataSetChanged();
                        writeItems(); //removes from stored data
                        // Return true consumes the long click event (marks it handled)
                        return true;
                    }
                });
    }

    //Updates listview to add item
    public void onAddItem(View v) {
        EditText etNewItem = binding.etAddItem;
        String itemText = etNewItem.getText().toString();
        if (itemText.trim().equals("")) {
            Toast.makeText(getActivity().getBaseContext(), "Task cannot be empty", Toast.LENGTH_SHORT).show();
        } else {
            itemsAdapter.add(itemText);
            etNewItem.setText("");
            writeItems(); // adds to stored data
        }
    }

}