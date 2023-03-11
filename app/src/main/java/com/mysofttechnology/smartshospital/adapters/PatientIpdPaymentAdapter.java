package com.mysofttechnology.smartshospital.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.mysofttechnology.smartshospital.R;
import com.mysofttechnology.smartshospital.utils.Constants;
import com.mysofttechnology.smartshospital.utils.Utility;
import java.util.ArrayList;

public class PatientIpdPaymentAdapter extends RecyclerView.Adapter<PatientIpdPaymentAdapter.MyViewHolder> {

    private FragmentActivity context;
    private ArrayList<String> payment_modeList;
    private ArrayList<String> dateList;
    private ArrayList<String> pnoteList;
    private ArrayList<String> paid_amountList;
    private ArrayList<String> cheque_noList;
    private ArrayList<String> cheque_dateList;
    private ArrayList<String> transactionIDList;
    long downloadID;

    public PatientIpdPaymentAdapter(FragmentActivity fragmentActivity, ArrayList<String> payment_modeList, ArrayList<String> dateList,
                                    ArrayList<String> pnoteList, ArrayList<String> paid_amountList, ArrayList<String> cheque_noList, ArrayList<String> cheque_dateList, ArrayList<String> transactionIDList) {

        this.context = fragmentActivity;
        this.payment_modeList = payment_modeList;
        this.dateList = dateList;
        this.pnoteList = pnoteList;
        this.paid_amountList = paid_amountList;
        this.cheque_noList = cheque_noList;
        this.cheque_dateList = cheque_dateList;
        this.transactionIDList = transactionIDList;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView note,date,paymentmode,paidamount,transactionid,chequedate,chequeno;
        ImageView downloadBtn;
        RelativeLayout detailsBtn,headLay;
        public CardView containerView;

        public MyViewHolder(View view) {
            super(view);
            note = (TextView) view.findViewById(R.id.note);
            date = (TextView) view.findViewById(R.id.adapter_date);
            paymentmode = (TextView) view.findViewById(R.id.paymentmode);
            paidamount = (TextView) view.findViewById(R.id.adapter_amount);
            chequeno = (TextView) view.findViewById(R.id.chequeno);
            chequedate = (TextView) view.findViewById(R.id.chequedate);
            transactionid = (TextView) view.findViewById(R.id.adapter_transactionid);
            headLay = (RelativeLayout)view.findViewById(R.id.adapter_patient_ipd_headLayout);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_patient_ipd_payment, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.headLay.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(context.getApplicationContext(), Constants.secondaryColour)));
        final String currency = Utility.getSharedPreferences(context.getApplicationContext(), Constants.currency);
        holder.date.setText(dateList.get(position));
        holder.note.setText(pnoteList.get(position));
        holder.transactionid.setText(transactionIDList.get(position));

        if(payment_modeList.get(position).equals("UPI")){
            holder.chequeno.setVisibility(View.GONE);
            holder.chequedate.setVisibility(View.GONE);
            holder.paymentmode.setText(payment_modeList.get(position));
        }else if(payment_modeList.get(position).equals("Cheque")){
            holder.chequeno.setVisibility(View.VISIBLE);
            holder.chequedate.setVisibility(View.VISIBLE);
            holder.chequedate.setText("Cheque Date: "+cheque_dateList.get(position));
            holder.chequeno.setText("Cheque No: "+cheque_noList.get(position));
            String str = snakeToCamel(payment_modeList.get(position));
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < str.length(); i++) {
                if(Character.isUpperCase(str.charAt(i))) {
                    sb.append(" ");
                    sb.append(str.charAt(i));
                } else {
                    sb.append(str.charAt(i));
                }
            }
            String result = sb.toString();
            System.out.println(result);
            holder.paymentmode.setText(result);
        }else{
            holder.chequeno.setVisibility(View.GONE);
            holder.chequedate.setVisibility(View.GONE);
            String str = snakeToCamel(payment_modeList.get(position));
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < str.length(); i++) {
                if(Character.isUpperCase(str.charAt(i))) {
                    sb.append(" ");
                    sb.append(str.charAt(i));
                } else {
                    sb.append(str.charAt(i));
                }
            }
            String result = sb.toString();
            System.out.println(result);
            holder.paymentmode.setText(result);
        }
        holder.paidamount.setText("Paid Amount: "+currency+paid_amountList.get(position));

    }
    public static String snakeToCamel(String str) {
        // Capitalize first letter of string
        str = str.substring(0, 1).toUpperCase()
                + str.substring(1);

        // Convert to StringBuilder
        StringBuilder builder
                = new StringBuilder(str);

        // Traverse the string character by
        // character and remove underscore
        // and capitalize next letter
        for (int i = 0; i < builder.length(); i++) {

            // Check char is underscore
            if (builder.charAt(i) == '_') {

                builder.deleteCharAt(i);
                builder.replace(
                        i, i + 1,
                        String.valueOf(
                                Character.toUpperCase(
                                        builder.charAt(i))));
            }
        }

        // Return in String type
        return builder.toString();
    }

    @Override
    public int getItemCount() {
        return dateList.size();
    }
}
