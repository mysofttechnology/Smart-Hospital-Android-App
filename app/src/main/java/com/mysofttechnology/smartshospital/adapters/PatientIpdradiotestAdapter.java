package com.mysofttechnology.smartshospital.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mysofttechnology.smartshospital.R;
import com.mysofttechnology.smartshospital.utils.Constants;
import com.mysofttechnology.smartshospital.utils.Utility;

import java.util.ArrayList;

public class PatientIpdradiotestAdapter extends RecyclerView.Adapter<PatientIpdradiotestAdapter.MyViewHolder> {

    private Context context;
    private int[] othersHeaderArray;
    private ArrayList<String> radiotestlist;


    public PatientIpdradiotestAdapter(Context applicationContext, ArrayList<String> radiotestlist) {

        this.context = applicationContext;
        this.radiotestlist = radiotestlist;


    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView testname;

        public MyViewHolder(View view) {
            super(view);
            testname = (TextView) view.findViewById(R.id.name);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Utility.setLocale(context,Utility.getSharedPreferences(context.getApplicationContext(),Constants.langCode));
        holder.testname.setText(radiotestlist.get(position));


    }

    @Override
    public int getItemCount() {
        return radiotestlist.size();
    }
}