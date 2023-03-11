package com.mysofttechnology.smartshospital.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.mysofttechnology.smartshospital.R;
import com.mysofttechnology.smartshospital.adapters.PatientOpdVisitChargeAdapter;
import com.mysofttechnology.smartshospital.utils.Constants;
import com.mysofttechnology.smartshospital.utils.Utility;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import static android.widget.Toast.makeText;

public class PatientOPDVisitChargeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener  {

    RecyclerView recyclerView;
    TextView total_charge;
    ArrayList<String> charge_typeList = new ArrayList<String>();
    ArrayList<String> charge_cateList = new ArrayList<String>();
    ArrayList<String> tpa_chargeList = new ArrayList<String>();
    ArrayList<String> taxList = new ArrayList<String>();
    ArrayList<String> qtyList = new ArrayList<String>();
    ArrayList<String> nameList = new ArrayList<String>();
    ArrayList<String> dateList = new ArrayList<String>();
    ArrayList<String> standard_chargeList = new ArrayList<String>();
    ArrayList<String> amountList = new ArrayList<String>();
    ArrayList<String> totalchargelist = new ArrayList<String>();
    ArrayList<String> apply_chargeList = new ArrayList<String>();

    PatientOpdVisitChargeAdapter adapter;
    public Map<String, String> params = new Hashtable<String, String>();
    public Map<String, String> headers = new HashMap<String, String>();
    String opdid;

    public PatientOPDVisitChargeFragment(String opdid) {
       this.opdid=opdid;
    }

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

        View mainView = inflater.inflate(R.layout.fragment_ipd_charge, container, false);
        recyclerView = (RecyclerView) mainView.findViewById(R.id.charge_listview);
        total_charge = (TextView) mainView.findViewById(R.id.total_charge);

        adapter = new PatientOpdVisitChargeAdapter(getActivity(), charge_typeList,dateList,
                charge_cateList,standard_chargeList,amountList,apply_chargeList,totalchargelist,tpa_chargeList,taxList,qtyList,nameList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);


        return mainView;
    }

    @Override
    public void onRefresh() {
        loadData();
    }

    private void getDataFromApi (String bodyParams) {
        final String requestBody = bodyParams;
        String url = Utility.getSharedPreferences(getActivity().getApplicationContext(), "apiUrl")+Constants.getOPDVisitDetailsUrl;
        System.out.println("url=="+url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {

                if (result != null) {
                    try {
                        Log.e("Result", result);
                        JSONObject obj = new JSONObject(result);
                        JSONArray dataArray = obj.getJSONArray("charges_detail");

                        dateList.clear();
                        apply_chargeList.clear();
                        charge_typeList.clear();
                        charge_cateList.clear();
                        standard_chargeList.clear();
                        tpa_chargeList.clear();
                        qtyList.clear();
                        taxList.clear();
                        nameList.clear();

                        final String currency = Utility.getSharedPreferences(getActivity().getApplicationContext(), Constants.currency);
                        String defaultDateFormat = Utility.getSharedPreferences(getActivity().getApplicationContext(), "datetimeFormat");
                        if(dataArray.length() != 0) {
                            double sum=0;
                            for(int i = 0; i < dataArray.length(); i++) {
                                dateList.add(Utility.parseDate("yyyy-MM-dd hh:mm", defaultDateFormat,dataArray.getJSONObject(i).getString("date")));
                                charge_typeList.add(dataArray.getJSONObject(i).getString("charge_type"));
                                charge_cateList.add(dataArray.getJSONObject(i).getString("charge_category_name"));
                                apply_chargeList.add(dataArray.getJSONObject(i).getString("apply_charge"));
                                qtyList.add(dataArray.getJSONObject(i).getString("qty"));
                                nameList.add(dataArray.getJSONObject(i).getString("name")+"\n"+dataArray.getJSONObject(i).getString("note"));
                                standard_chargeList.add(dataArray.getJSONObject(i).getString("standard_charge"));
                                tpa_chargeList.add(dataArray.getJSONObject(i).getString("tpa_charge"));
                                taxList.add(dataArray.getJSONObject(i).getString("tax_amount")+"("+dataArray.getJSONObject(i).getString("tax")+"%)");
                                amountList.add(dataArray.getJSONObject(i).getString("amount"));
                                sum += Double.parseDouble(dataArray.getJSONObject(i).getString("amount"));
                            }

                            total_charge.setText("Total: " +currency+  String.format("%.2f", sum));

                            adapter.notifyDataSetChanged();
                        } else {
                            total_charge.setText("Total:  " + currency + "0.00");
                           // Toast.makeText(getActivity().getApplicationContext(), getActivity().getApplicationContext().getString(R.string.noData), Toast.LENGTH_SHORT).show();
                        }
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
                Toast.makeText(getActivity().getApplicationContext(), R.string.apiErrorMsg, Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());//Creating a Request Queue
        requestQueue.add(stringRequest); //Adding request to the queue
    }
}