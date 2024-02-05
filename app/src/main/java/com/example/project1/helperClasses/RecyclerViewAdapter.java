package com.example.project1.helperClasses;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project1.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private final RecyclerViewInterface recyclerViewInterface;
    private Context context;
    private ArrayList<ListDataClass> dataList;

    public RecyclerViewAdapter(Context context, ArrayList<ListDataClass> dataList, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.dataList = dataList;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_row, parent, false);
        return new RecyclerViewAdapter.MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.header.setText(dataList.get(position).getHeading());
        holder.subheader1.setText(dataList.get(position).getSubhead1());
        holder.subheader2.setText(dataList.get(position).getSubhead2());
        holder.subheader3.setText(dataList.get(position).getSubhead3());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView header;
        TextView subheader1, subheader2, subheader3;
        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            header = itemView.findViewById(R.id.heading);
            subheader1 = itemView.findViewById(R.id.subhead1);
            subheader2 = itemView.findViewById(R.id.subhead2);
            subheader3 = itemView.findViewById(R.id.subhead3);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerViewInterface != null) {
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onItemClick(pos, view);
                        }
                    }
                }
            });


            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (recyclerViewInterface != null) {
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onItemLongClick(pos);
                        }
                    }
                    return true;
                }
            });
        }
    }
}
