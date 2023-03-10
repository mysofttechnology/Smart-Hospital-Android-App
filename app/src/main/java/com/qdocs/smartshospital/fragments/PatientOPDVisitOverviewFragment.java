package com.qdocs.smartshospital.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.qdocs.smartshospital.R;
import com.qdocs.smartshospital.adapters.AllergyAdapter;
import com.qdocs.smartshospital.adapters.FindingsAdapter;
import com.qdocs.smartshospital.adapters.PatientDoctorAdapter;
import com.qdocs.smartshospital.adapters.PatientIPDMedicationAdapter;
import com.qdocs.smartshospital.adapters.SymptomsAdapter;
import com.qdocs.smartshospital.model.MedicationModel;
import com.qdocs.smartshospital.utils.Constants;
import com.qdocs.smartshospital.utils.Utility;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;

import static android.widget.Toast.makeText;

public class PatientOPDVisitOverviewFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    ArrayList<MedicationModel> medication_list = new ArrayList<>();
    ArrayList<String> doctorlist = new ArrayList<String>();
    ArrayList<String> imagelist = new ArrayList<String>();
    ArrayList<String> allergylist = new ArrayList<String>();
    ArrayList<String> symptomslist = new ArrayList<String>();
    ArrayList<String> findingslist = new ArrayList<String>();
    private String opdid;
    PatientIPDMedicationAdapter adapter;
    PatientDoctorAdapter doctoradapter;
    FindingsAdapter findingadapter;
    public Map<String, String> params = new Hashtable<String, String>();
    public Map<String, String> headers = new HashMap<String, String>();
    PieChart pieChart;
    ImageView doctorimage;
    RecyclerView findings_recyclerview,doctor_recyclerview,allergy_recyclerview,symptoms_recyclerview;
    ProgressBar progressBar,pharmacy_progressBar,pathology_progressBar,radiology_progressBar,bloodbank_progressBar,ambulance_progressBar;
    public String defaultDatetimeFormat,defaultDateFormat, currency;
    public PatientOPDVisitOverviewFragment(String opdid) {
        this.opdid=opdid;
    }
    AllergyAdapter allergyadapter;
    SymptomsAdapter symptomsadapter;
    TextView opdnotv,caseid;
    TextView totalbillratio,pharmacy_totalbillratio,pathology_totalbillratio,radiology_totalbillratio,bloodbank_totalbillratio,ambulance_totalbillratio;
    TextView totalbillpayment,pharmacy_totalbillpayment,pathology_totalbillpayment,radiology_totalbillpayment,bloodbank_totalbillpayment,ambulance_totalbillpayment;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
    }

    private void loadData() {
        if(Utility.isConnectingToInternet(getActivity().getApplicationContext())){
            params.put("opd_id", opdid);
            JSONObject obj=new JSONObject(params);
            Log.e("params ", obj.toString());
            getDataFromApi(obj.toString());
        }else{
            makeText(getActivity().getApplicationContext(), R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mainView = inflater.inflate(R.layout.opdvisit_overview_list, container, false);

        caseid = mainView.findViewById(R.id.caseid);
        opdnotv = mainView.findViewById(R.id.opdnotv);
        loadData();

        progressBar = mainView.findViewById(R.id.progressBar);
        pharmacy_progressBar = mainView.findViewById(R.id.pharmacy_progressBar);
        pathology_progressBar = mainView.findViewById(R.id.pathology_progressBar);
        radiology_progressBar = mainView.findViewById(R.id.radiology_progressBar);
        bloodbank_progressBar = mainView.findViewById(R.id.bloodbank_progressBar);
        ambulance_progressBar = mainView.findViewById(R.id.ambulance_progressBar);

        totalbillpayment = mainView.findViewById(R.id.totalbillpayment);
        pharmacy_totalbillpayment = mainView.findViewById(R.id.pharmacy_totalbillpayment);
        pathology_totalbillpayment = mainView.findViewById(R.id.pathology_totalbillpayment);
        radiology_totalbillpayment = mainView.findViewById(R.id.radiology_totalbillpayment);
        bloodbank_totalbillpayment = mainView.findViewById(R.id.bloodbank_totalbillpayment);
        ambulance_totalbillpayment = mainView.findViewById(R.id.ambulance_totalbillpayment);

        totalbillratio = mainView.findViewById(R.id.totalbillratio);
        pharmacy_totalbillratio = mainView.findViewById(R.id.pharmacy_totalbillratio);
        pathology_totalbillratio = mainView.findViewById(R.id.pathology_totalbillratio);
        radiology_totalbillratio = mainView.findViewById(R.id.radiology_totalbillratio);
        bloodbank_totalbillratio = mainView.findViewById(R.id.bloodbank_totalbillratio);
        ambulance_totalbillratio = mainView.findViewById(R.id.ambulance_totalbillratio);

        allergy_recyclerview = mainView.findViewById(R.id.allergy_recyclerview);
        allergyadapter = new AllergyAdapter(getActivity(), allergylist);
        RecyclerView.LayoutManager aLayoutManager = new LinearLayoutManager(getActivity());
        allergy_recyclerview.setLayoutManager(aLayoutManager);
        allergy_recyclerview.setItemAnimator(new DefaultItemAnimator());
        allergy_recyclerview.setAdapter(allergyadapter);

        symptoms_recyclerview = mainView.findViewById(R.id.symptoms_recyclerview);
        symptomsadapter = new SymptomsAdapter(getActivity(), symptomslist);
        RecyclerView.LayoutManager sLayoutManager = new LinearLayoutManager(getActivity());
        symptoms_recyclerview.setLayoutManager(sLayoutManager);
        symptoms_recyclerview.setItemAnimator(new DefaultItemAnimator());
        symptoms_recyclerview.setAdapter(symptomsadapter);

        findings_recyclerview = mainView.findViewById(R.id.findings_recyclerview);
        findingadapter = new FindingsAdapter(getActivity(), findingslist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        findings_recyclerview.setLayoutManager(mLayoutManager);
        findings_recyclerview.setItemAnimator(new DefaultItemAnimator());
        findings_recyclerview.setAdapter(findingadapter);

        doctor_recyclerview = mainView.findViewById(R.id.doctor_recyclerview);
        doctoradapter = new PatientDoctorAdapter(getActivity(), doctorlist,imagelist);
        RecyclerView.LayoutManager LayoutManager = new LinearLayoutManager(getActivity());
        doctor_recyclerview.setLayoutManager(LayoutManager);
        doctor_recyclerview.setItemAnimator(new DefaultItemAnimator());
        doctor_recyclerview.setAdapter(doctoradapter);


        defaultDatetimeFormat = Utility.getSharedPreferences(getActivity(), "datetimeFormat");
        defaultDateFormat = Utility.getSharedPreferences(getActivity(), "dateFormat");
        currency = Utility.getSharedPreferences(getActivity(), Constants.currency);

        return mainView;
    }

    @Override
    public void onRefresh() {
        loadData();
    }

    private void getDataFromApi (String bodyParams) {

        final String requestBody = bodyParams;

        String url = Utility.getSharedPreferences(getActivity().getApplicationContext(), "apiUrl")+Constants.getOPDVisitDetailsUrl;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {

                if (result != null) {
                    try {
                        Log.e("Result", result);
                        JSONObject obj = new JSONObject(result);
                        JSONObject dataArray = obj.getJSONObject("patientdetails");
                        JSONObject patientArray = dataArray.getJSONObject("patient");

                        allergylist.clear();
                        JSONArray allergyArray = patientArray.getJSONArray("allergy");
                        for(int i = 0; i < allergyArray.length(); i++) {
                            allergylist.add(allergyArray.getJSONObject(i).getString("known_allergies"));
                        }
                        allergyadapter.notifyDataSetChanged();

                        symptomslist.clear();
                        JSONArray symptomsArray = patientArray.getJSONArray("symptoms");
                        for(int i = 0; i < symptomsArray.length(); i++) {
                            symptomslist.add(symptomsArray.getJSONObject(i).getString("symptoms"));
                        }
                        symptomsadapter.notifyDataSetChanged();

                        doctorlist.clear();
                        imagelist.clear();
                        JSONArray doctorArray = patientArray.getJSONArray("doctor");
                        for(int i = 0; i < doctorArray.length(); i++) {
                            doctorlist.add(doctorArray.getJSONObject(i).getString("name")+" "+doctorArray.getJSONObject(i).getString("surname")+" ("+doctorArray.getJSONObject(i).getString("employee_id")+")");
                            imagelist.add(doctorArray.getJSONObject(i).getString("image"));
                        }
                        doctoradapter.notifyDataSetChanged();

                        findingslist.clear();
                        JSONArray findingsArray = patientArray.getJSONArray("findings");
                        for(int j = 0; j < findingsArray.length();j++) {
                            findingslist.add(findingsArray.getJSONObject(j).getString("finding_description"));
                        }
                        findingadapter.notifyDataSetChanged();

                        JSONObject graphArray = obj.getJSONObject("graph");

                        JSONObject opdArray = graphArray.getJSONObject("opd");
                        String bill_payment_ratio = opdArray.getString("opd_bill_payment_ratio");
                        totalbillratio.setText(bill_payment_ratio+"%");

                        JSONObject billArray = opdArray.getJSONObject("bill");
                        String total_bill = billArray.getString("total_bill");
                        JSONObject paymentArray = opdArray.getJSONObject("payment");
                        String total_payment = paymentArray.getString("total_payment");
                        totalbillpayment.setText(currency+total_payment+"/"+currency+total_bill);
                        Integer progressvalueint=(int)(Double.parseDouble(bill_payment_ratio));
                        progressBar.setProgress(progressvalueint);
                        if(progressvalueint==100){
                            progressBar.getProgressDrawable().setColorFilter(
                                    getActivity().getResources().getColor(R.color.hyperLink), android.graphics.PorterDuff.Mode.SRC_IN);
                            //holder.progressBar.setProgressTintList(context.getResources().getDrawable(R.drawable.green_border));
                        }else if(progressvalueint>0 && progressvalueint<100){
                           progressBar.getProgressDrawable().setColorFilter(
                                   getActivity().getResources().getColor(R.color.hyperLink), android.graphics.PorterDuff.Mode.SRC_IN);
                        }


                        JSONObject pharmacyArray = graphArray.getJSONObject("pharmacy");
                        String pharmacy_bill_payment_ratio = pharmacyArray.getString("pharmacy_bill_payment_ratio");
                        pharmacy_totalbillratio.setText(pharmacy_bill_payment_ratio+"%");
                        JSONObject pharmacybillArray = pharmacyArray.getJSONObject("bill");
                        String pharmacytotal_bill = pharmacybillArray.getString("total_bill");
                        JSONObject pharmacypaymentArray = pharmacyArray.getJSONObject("payment");
                        String pharmacytotal_payment = pharmacypaymentArray.getString("total_payment");
                        JSONObject pharmacypayment_refundArray = pharmacyArray.getJSONObject("payment_refund");
                        String pharmacytotalrefund_payment = pharmacypayment_refundArray.getString("total_payment");
                        pharmacy_totalbillpayment.setText(currency+pharmacytotal_payment+"/"+currency+pharmacytotal_bill);
                        Integer pharmacyprogress=(int)(Double.parseDouble(pharmacy_bill_payment_ratio));
                        pharmacy_progressBar.setProgress(pharmacyprogress);
                        if(pharmacyprogress==100){
                            pharmacy_progressBar.getProgressDrawable().setColorFilter(
                                    getActivity().getResources().getColor(R.color.hyperLink), android.graphics.PorterDuff.Mode.SRC_IN);
                            //holder.progressBar.setProgressTintList(context.getResources().getDrawable(R.drawable.green_border));
                        }else if(progressvalueint>0 && progressvalueint<100){
                            pharmacy_progressBar.getProgressDrawable().setColorFilter(
                                    getActivity().getResources().getColor(R.color.hyperLink), android.graphics.PorterDuff.Mode.SRC_IN);
                        }

                        JSONObject pathologyArray = graphArray.getJSONObject("pathology");
                        String pathology_bill_payment_ratio = pathologyArray.getString("pathology_bill_payment_ratio");
                        pathology_totalbillratio.setText(pathology_bill_payment_ratio+"%");
                        JSONObject pathologybillArray = pathologyArray.getJSONObject("bill");
                        String pathologytotal_bill = pathologybillArray.getString("total_bill");
                        JSONObject pathologypaymentArray = pathologyArray.getJSONObject("payment");
                        String pathologytotal_payment = pathologypaymentArray.getString("total_payment");
                        pathology_totalbillpayment.setText(currency+pathologytotal_payment+"/"+currency+pathologytotal_bill);
                        Integer pathologyprogress=(int)(Double.parseDouble(pathology_bill_payment_ratio));
                        pathology_progressBar.setProgress(pathologyprogress);
                        if(pathologyprogress==100){
                            pathology_progressBar.getProgressDrawable().setColorFilter(
                                    getActivity().getResources().getColor(R.color.hyperLink), android.graphics.PorterDuff.Mode.SRC_IN);
                            //holder.progressBar.setProgressTintList(context.getResources().getDrawable(R.drawable.green_border));
                        }else if(progressvalueint>0 && progressvalueint<100){
                            pathology_progressBar.getProgressDrawable().setColorFilter(
                                    getActivity().getResources().getColor(R.color.hyperLink), android.graphics.PorterDuff.Mode.SRC_IN);
                        }

                        JSONObject radiologyArray = graphArray.getJSONObject("radiology");
                        String radiology_bill_payment_ratio = radiologyArray.getString("radiology_bill_payment_ratio");
                        radiology_totalbillratio.setText(radiology_bill_payment_ratio+"%");
                        JSONObject radiologybillArray = radiologyArray.getJSONObject("bill");
                        String radiologytotal_bill = radiologybillArray.getString("total_bill");
                        JSONObject radiologypaymentArray = radiologyArray.getJSONObject("payment");
                        String radiologytotal_payment = radiologypaymentArray.getString("total_payment");
                        radiology_totalbillpayment.setText(currency+radiologytotal_payment+"/"+currency+radiologytotal_bill);
                        Integer radiologyprogress=(int)(Double.parseDouble(radiology_bill_payment_ratio));
                        radiology_progressBar.setProgress(radiologyprogress);
                        if(radiologyprogress==100){
                            radiology_progressBar.getProgressDrawable().setColorFilter(
                                    getActivity().getResources().getColor(R.color.hyperLink), android.graphics.PorterDuff.Mode.SRC_IN);
                            //holder.progressBar.setProgressTintList(context.getResources().getDrawable(R.drawable.green_border));
                        }else if(progressvalueint>0 && progressvalueint<100){
                            radiology_progressBar.getProgressDrawable().setColorFilter(
                                    getActivity().getResources().getColor(R.color.hyperLink), android.graphics.PorterDuff.Mode.SRC_IN);
                        }

                        JSONObject blood_bankArray = graphArray.getJSONObject("blood_bank");
                        String blood_bank_bill_payment_ratio = blood_bankArray.getString("blood_bank_bill_payment_ratio");
                        bloodbank_totalbillratio.setText(blood_bank_bill_payment_ratio+"%");
                        JSONObject blood_bankbillArray = blood_bankArray.getJSONObject("bill");
                        String blood_banktotal_bill = blood_bankbillArray.getString("total_bill");
                        JSONObject blood_bankpaymentArray = blood_bankArray.getJSONObject("payment");
                        String blood_banktotal_payment = blood_bankpaymentArray.getString("total_payment");
                        bloodbank_totalbillpayment.setText(currency+blood_banktotal_payment+"/"+currency+blood_banktotal_bill);
                        Integer blood_bankprogress=(int)(Double.parseDouble(blood_bank_bill_payment_ratio));
                        bloodbank_progressBar.setProgress(blood_bankprogress);
                        if(blood_bankprogress==100){
                            bloodbank_progressBar.getProgressDrawable().setColorFilter(
                                    getActivity().getResources().getColor(R.color.hyperLink), android.graphics.PorterDuff.Mode.SRC_IN);
                            //holder.progressBar.setProgressTintList(context.getResources().getDrawable(R.drawable.green_border));
                        }else if(progressvalueint>0 && progressvalueint<100){
                            bloodbank_progressBar.getProgressDrawable().setColorFilter(
                                    getActivity().getResources().getColor(R.color.hyperLink), android.graphics.PorterDuff.Mode.SRC_IN);
                        }

                        JSONObject ambulanceArray = graphArray.getJSONObject("ambulance");
                        String ambulance_bill_payment_ratio = ambulanceArray.getString("ambulance_bill_payment_ratio");
                        ambulance_totalbillratio.setText(ambulance_bill_payment_ratio+"%");
                        JSONObject ambulancebillArray = ambulanceArray.getJSONObject("bill");
                        String ambulancetotal_bill = ambulancebillArray.getString("total_bill");
                        JSONObject ambulancepaymentArray = ambulanceArray.getJSONObject("payment");
                        String ambulancetotal_payment = ambulancepaymentArray.getString("total_payment");
                        ambulance_totalbillpayment.setText(currency+ambulancetotal_payment+"/"+currency+ambulancetotal_bill);
                        Integer ambulance_bankprogress=(int)(Double.parseDouble(ambulance_bill_payment_ratio));
                        ambulance_progressBar.setProgress(ambulance_bankprogress);
                        if(ambulance_bankprogress==100){
                            ambulance_progressBar.getProgressDrawable().setColorFilter(
                                    getActivity().getResources().getColor(R.color.hyperLink), android.graphics.PorterDuff.Mode.SRC_IN);
                            //holder.progressBar.setProgressTintList(context.getResources().getDrawable(R.drawable.green_border));
                        }else if(progressvalueint>0 && progressvalueint<100){
                            ambulance_progressBar.getProgressDrawable().setColorFilter(
                                    getActivity().getResources().getColor(R.color.hyperLink), android.graphics.PorterDuff.Mode.SRC_IN);
                        }

                        caseid.setText(patientArray.getString("case_reference_id"));
                        opdnotv.setText("OPDN"+patientArray.getString("opd_id"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("Volley Error", volleyError.toString());
                Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", Constants.contentType);
                headers.put("User-ID", Utility.getSharedPreferences(getActivity().getApplicationContext(), "userId"));
                headers.put("Authorization", Utility.getSharedPreferences(getActivity().getApplicationContext(), "accessToken"));
                Log.e("Headers", headers.toString());
                return headers;
            }
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringRequest);
    }
}