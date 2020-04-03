package com.teapink.ocr_reader.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.teapink.ocr_reader.activities.MainActivity;
import com.teapink.ocr_reader.R;
import com.teapink.ocr_reader.db.entity.Computer;

import java.util.ArrayList;

public class ComputersAdapter extends RecyclerView.Adapter<ComputersAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Computer> computerssList;
    private MainActivity mainActivity;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView emil;


        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            emil = view.findViewById(R.id.serial);

        }
    }


    public ComputersAdapter(Context context, ArrayList<Computer> computers, MainActivity mainActivity) {
        this.context = context;
        this.computerssList = computers;
        this.mainActivity = mainActivity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.computer_list_item, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        final Computer computer = computerssList.get(position);

        holder.name.setText(computer.getName());
        holder.emil.setText(computer.getSerial());

        holder.itemView.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                mainActivity.addAndEditComputers(true, computer, position);
            }
        });

    }

    @Override
    public int getItemCount() {

        return computerssList.size();
    }

}
