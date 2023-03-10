package com.qdocs.smartshospital.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qdocs.smartshospital.PatientFrontAddAppoinment;
import com.qdocs.smartshospital.R;
import com.qdocs.smartshospital.model.DoseModel;
import com.qdocs.smartshospital.utils.Constants;
import com.qdocs.smartshospital.utils.Utility;

import org.json.JSONObject;

import java.util.ArrayList;

import static android.widget.Toast.makeText;

public class DoselistAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<DoseModel> doselist;
    GridView listView;
    View view;
    public DoselistAdapter(Context context, ArrayList<DoseModel> doselist, GridView listView) {
        this.context = context;
        this.doselist = doselist;
        this.listView = listView;
    }

    @Override
    public int getCount() {
        return doselist.size();
    }

    @Override
    public Object getItem(int position) {
        return doselist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        // If holder not exist then locate all view from UI file.
        if (convertView == null) {
            // inflate UI from XML file
            convertView = inflater.inflate(R.layout.adapter_tag_selection, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }
        String defaultDatetimeFormat = Utility.getSharedPreferences(context.getApplicationContext(), "datetimeFormat");
        final DoseModel doseModel=doselist.get(position);
        holder.slot.setText("Dose "+(position+1));
        holder.slot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.doselist);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
                final ImageView closeBtn = (ImageView) dialog.findViewById(R.id.dialog_crossIcon);
                final RelativeLayout header = dialog.findViewById(R.id.addappoint_dialog_header);
                final TextView headertext = dialog.findViewById(R.id.headertext);
                final TextView remark = dialog.findViewById(R.id.remark);
                final TextView dose = dialog.findViewById(R.id.dose);
                final TextView time = dialog.findViewById(R.id.time);
                final TextView date = dialog.findViewById(R.id.date);
                header.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(context.getApplicationContext(), Constants.primaryColour)));
                headertext.setText(context.getString(R.string.dose)+" "+(position+1));
               remark.setText(doseModel.getRemark());
               time.setText(doseModel.getTime());
               date.setText(doseModel.getDate());
                dose.setText(doseModel.getMedicine_dosage());
                closeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }
        });
        return convertView;
    }



    private class ViewHolder {
        private TextView slot,date,message,id;

        public ViewHolder(View v) {

            slot = (TextView) v.findViewById(R.id.slot);


        }
    }
}
