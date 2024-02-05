package com.example.project1.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.project1.R;
import com.example.project1.databinding.FragmentTodo3Binding;
import com.example.project1.databinding.FragmentTodoBinding;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class todoFragment3 extends Fragment {
    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;
    private FragmentTodo3Binding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTodo3Binding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // sets up list view
        lvItems = view.findViewById(R.id.lv_to_do_3);
        items = new ArrayList<>();
        readItems(); // read cached items
        itemsAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);

        // Call listener for long tap and hold for removing item
        setupListViewListener();

        // Set up the click listener for the "Add" button
        binding.buttonAddItem3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddItem(v);
            }
        });


        return view;
    }


    //Next two blocks of code cache the data by reading and writing to/from memory
// Next two blocks of code cache the data by reading and writing to/from memory
    private void readItems() {
        File filesDir = requireContext().getFilesDir();
        File todoFile = new File(filesDir, "todo3.txt");
        try {
            items = new ArrayList<>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<>();
        }
    }

    private void writeItems() {
        File filesDir = requireContext().getFilesDir();
        File todoFile = new File(filesDir, "todo3.txt");
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
        EditText etNewItem = binding.etAddItem3;
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(itemText);
        etNewItem.setText("");
        writeItems(); // adds to stored data
    }

}