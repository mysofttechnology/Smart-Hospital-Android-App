package com.qdocs.smartshospital.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.qdocs.smartshospital.R;
import com.qdocs.smartshospital.utils.Constants;
import com.qdocs.smartshospital.utils.Utility;
import java.util.ArrayList;

public class PatientIpdChargeAdapter extends RecyclerView.Adapter<PatientIpdChargeAdapter.MyViewHolder> {

    private FragmentActivity context;
    private ArrayList<String> charge_typeList;
    private ArrayList<String> dateList;
    private ArrayList<String> charge_cateList;
    private ArrayList<String> standard_chargeList;
    private ArrayList<String> amountList;
    private ArrayList<String> apply_chargeList;
    private ArrayList<String> totalchargelist;
    private ArrayList<String> nameList;
    private ArrayList<String> taxList;
    private ArrayList<String> qtyList;
    private ArrayList<String> tpaList;
    long downloadID;

    public PatientIpdChargeAdapter(FragmentActivity fragmentActivity, ArrayList<String> charge_typeList, ArrayList<String> dateList,
                                   ArrayList<String> charge_cateList, ArrayList<String> standard_chargeList,
                                   ArrayList<String> amountList, ArrayList<String> apply_chargeList,ArrayList<String> totalchargelist,
                                   ArrayList<String> nameList,ArrayList<String> taxList,ArrayList<String> qtyList,ArrayList<String> tpaList) {
        this.context = fragmentActivity;
        this.charge_typeList = charge_typeList;
        this.dateList = dateList;
        this.charge_cateList = charge_cateList;
        this.standard_chargeList = standard_chargeList;
        this.amountList = amountList;
        this.apply_chargeList = apply_chargeList;
        this.totalchargelist = totalchargelist;
        this.nameList = nameList;
        this.taxList = taxList;
        this.qtyList = qtyList;
        this.tpaList = tpaList;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView chargetype,date,chargecategory,standardcharge,amount,applycharge,name,quantity,tpa,tax;
        ImageView downloadBtn;
        RelativeLayout detailsBtn,headLay;
        public CardView containerView;

        public MyViewHolder(View view) {
            super(view);
            chargetype = (TextView) view.findViewById(R.id.adapter_chargetype);
            name = (TextView) view.findViewById(R.id.adapter_name);
            date = (TextView) view.findViewById(R.id.adapter_date);
            chargecategory = (TextView) view.findViewById(R.id.adapter_ChargeCategory);
            quantity = (TextView) view.findViewById(R.id.adapter_quantity);
            standardcharge = (TextView) view.findViewById(R.id.adapter_standard);
            tpa = (TextView) view.findViewById(R.id.adapter_tpa);
            amount = (TextView) view.findViewById(R.id.adapter_amount);
            tax = (TextView) view.findViewById(R.id.adapter_tax);
            applycharge = (TextView) view.findViewById(R.id.adapter_appliedcharge);
            headLay = (RelativeLayout)view.findViewById(R.id.adapter_patient_ipd_headLayout);
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_patient_ipd_charge, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.headLay.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(context.getApplicationContext(), Constants.secondaryColour)));
        final String currency = Utility.getSharedPreferences(context.getApplicationContext(), Constants.currency);
         holder.name.setText(nameList.get(position));
         holder.date.setText("Date: "+dateList.get(position));
         holder.tax.setText(currency+taxList.get(position));
         holder.chargetype.setText(charge_typeList.get(position));
         holder.quantity.setText(qtyList.get(position));
         holder.chargecategory.setText(charge_cateList.get(position));
         holder.standardcharge.setText(currency+standard_chargeList.get(position));
         holder.tpa.setText(currency+tpaList.get(position));
         holder.amount.setText("Amount: "+currency+amountList.get(position));
         holder.applycharge.setText(currency+apply_chargeList.get(position));
    }

    @Override
    public int getItemCount() {
        return dateList.size();
    }
}
