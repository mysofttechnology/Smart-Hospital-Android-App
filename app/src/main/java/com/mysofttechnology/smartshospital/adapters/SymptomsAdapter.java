package com.mysofttechnology.smartshospital.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mysofttechnology.smartshospital.R;

import java.util.ArrayList;


public class SymptomsAdapter extends RecyclerView.Adapter<SymptomsAdapter.MyViewHolder> {

    Context  applicationContext;
    ArrayList<String> symptomslist=new ArrayList<String>();

    public SymptomsAdapter(Context applicationContext, ArrayList<String> symptomslist) {


        this.symptomslist = symptomslist;
        this.applicationContext = applicationContext;


    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView field_name,field_value;



        public MyViewHolder(View view) {
            super(view);
            field_name = (TextView) view.findViewById(R.id.fieldname);
            field_value = (TextView) view.findViewById(R.id.fieldvalue);

        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.findingslist, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder,final int position) {
        holder.field_name.setText(symptomslist.get(position));
    }
    @Override
    public int getItemCount() {
        return symptomslist.size();
    }


}