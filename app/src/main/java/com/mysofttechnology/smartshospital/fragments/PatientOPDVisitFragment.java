package com.mysofttechnology.smartshospital.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import com.mysofttechnology.smartshospital.adapters.PatientOpdVisitAdapter;
import com.mysofttechnology.smartshospital.model.CustomFieldModel;
import com.mysofttechnology.smartshospital.model.OpdDetailModel;
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

public class PatientOPDVisitFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    ArrayList<OpdDetailModel> opd_detail_list = new ArrayList<>();
    RecyclerView recyclerView;
    PatientOpdVisitAdapter adapter;
    LinearLayout nodata_layout,data_layout;
    public Map<String, String> params = new Hashtable<String, String>();
    public Map<String, String> headers = new HashMap<String, String>();
    public String defaultDatetimeFormat, currency;
    OpdDetailModel opdDetailModel;
    public PatientOPDVisitFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
    }

    private void loadData() {
        if(Utility.isConnectingToInternet(getActivity().getApplicationContext())){
            params.put("patient_id", Utility.getSharedPreferences(getActivity().getApplicationContext(), Constants.patient_id));
            JSONObject obj=new JSONObject(params);
            Log.e("params", obj.toString());
            getDataFromApi(obj.toString());
        }else{
            makeText(getActivity().getApplicationContext(), R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mainView = inflater.inflate(R.layout.fragment_list, container, false);
        recyclerView = (RecyclerView) mainView.findViewById(R.id.patientOpd_listview);
        adapter = new PatientOpdVisitAdapter(getActivity(), opd_detail_list,null);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        nodata_layout =mainView.findViewById(R.id.nodata_layout);
        data_layout =mainView.findViewById(R.id.data_layout);

        defaultDatetimeFormat = Utility.getSharedPreferences(getActivity(), "datetimeFormat");
        currency = Utility.getSharedPreferences(getActivity(), Constants.currency);
        loadData();
        return mainView;
    }

    @Override
    public void onRefresh() {
        loadData();
    }

    private void getDataFromApi (String bodyParams) {
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
        final String requestBody = bodyParams;
        String url = Utility.getSharedPreferences(getActivity().getApplicationContext(), "apiUrl")+Constants.getOPDDetailsUrl;
        Log.e("URL", url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {

                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.e("Result", result);
                        JSONObject obj = new JSONObject(result);
                        JSONArray dataArray = obj.getJSONArray("result");
                        opd_detail_list.clear();
                        if(dataArray.length() != 0) {
                            nodata_layout.setVisibility(View.GONE);
                            data_layout.setVisibility(View.VISIBLE);

                            for(int i = 0; i < dataArray.length(); i++) {
                                 opdDetailModel = new OpdDetailModel();

                                opdDetailModel.setOpdid(dataArray.getJSONObject(i).getString("opdid"));
                                opdDetailModel.setName(dataArray.getJSONObject(i).getString("name")+" "+dataArray.getJSONObject(i).getString("surname")+" ("+dataArray.getJSONObject(i).getString("employee_id")+")");
                                opdDetailModel.setId(dataArray.getJSONObject(i).getString("id"));
                                opdDetailModel.setAppointment_date(Utility.parseDate("yyyy-MM-dd HH:mm", defaultDatetimeFormat,dataArray.getJSONObject(i).getString("appointment_date")));
                                opdDetailModel.setRefference(dataArray.getJSONObject(i).getString("refference"));
                                opdDetailModel.setSymptoms(dataArray.getJSONObject(i).getString("symptoms"));
                                opdDetailModel.setVisitid(dataArray.getJSONObject(i).getString("visitid"));
                                opdDetailModel.setCase_reference_id(dataArray.getJSONObject(i).getString("case_reference_id"));
                                opdDetailModel.setPrescription(dataArray.getJSONObject(i).getString("prescription"));
                                JSONArray customArray = dataArray.getJSONObject(i).getJSONArray("customfield");
                                ArrayList<CustomFieldModel> customArrayList = new ArrayList<>();
                                for(int j = 0; j < customArray.length(); j++) {
                                    CustomFieldModel customFieldModel = new CustomFieldModel();
                                    customFieldModel.setFieldname(customArray.getJSONObject(j).getString("fieldname"));
                                    customFieldModel.setFieldvalue(customArray.getJSONObject(j).getString("fieldvalue"));
                                    customArrayList.add(customFieldModel);
                                }

                                opdDetailModel.setCustomfield(customArrayList);
                                opd_detail_list.add(opdDetailModel);

                            }

                            adapter.notifyDataSetChanged();
                        } else {
                           nodata_layout.setVisibility(View.VISIBLE);
                           data_layout.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    pd.dismiss();
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