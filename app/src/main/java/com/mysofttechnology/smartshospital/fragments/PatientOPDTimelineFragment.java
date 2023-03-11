package com.mysofttechnology.smartshospital.fragments;


import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;;
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
import com.mysofttechnology.smartshospital.adapters.PatientOpdTimelineAdapter;
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

public class PatientOPDTimelineFragment extends Fragment  implements SwipeRefreshLayout.OnRefreshListener {

    RecyclerView recyclerView;
    ArrayList<String> titlelist = new ArrayList<String>();
    ArrayList<String> timeline_dateList = new ArrayList<String>();
    ArrayList<String> descriptionList = new ArrayList<String>();
    ArrayList<String> idlist = new ArrayList<String>();
    ArrayList<String> documentlist = new ArrayList<String>();
    PatientOpdTimelineAdapter adapter;
    public Map<String, String> params = new Hashtable<String, String>();
    SwipeRefreshLayout pullToRefresh;
    public Map<String, String> headers = new HashMap<String, String>();

    LinearLayout nodata_layout,data_layout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
    }

    private void loadData() {
        if(Utility.isConnectingToInternet(getActivity().getApplicationContext())){
            params.put("patient_id", Utility.getSharedPreferences(getActivity().getApplicationContext(), Constants.patient_id));
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

        View mainView = inflater.inflate(R.layout.fragment_ipd_timeline, container, false);
        recyclerView = (RecyclerView) mainView.findViewById(R.id.patientipd_listview);
        nodata_layout =mainView.findViewById(R.id.nodata_layout);
        data_layout =mainView.findViewById(R.id.data_layout);
        pullToRefresh =  mainView.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullToRefresh.setRefreshing(true);
                loadData();
            }
        });
        adapter = new PatientOpdTimelineAdapter(getActivity(), titlelist,timeline_dateList,
                descriptionList,idlist,documentlist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        loadData();
        return mainView;
    }

    @Override
    public void onRefresh() {
        loadData();
    }

    private void getDataFromApi (String bodyParams) {

        final String requestBody = bodyParams;
        String url = Utility.getSharedPreferences(getActivity().getApplicationContext(), "apiUrl")+Constants.getOPDDetailsUrl;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {

                if (result != null) {
                    pullToRefresh.setRefreshing(false);
                    try {
                         Log.e("Result", result);
                         JSONObject obj = new JSONObject(result);
                         JSONArray dataArray = obj.getJSONArray("timeline_list");
                         titlelist.clear();
                         timeline_dateList.clear();
                         descriptionList.clear();
                         idlist.clear();
                        documentlist.clear();
                        String defaultDateFormat = Utility.getSharedPreferences(getActivity().getApplicationContext(), "dateFormat");
                         if(dataArray.length() != 0) {
                             nodata_layout.setVisibility(View.GONE);
                             data_layout.setVisibility(View.VISIBLE);
                             for(int i = 0; i < dataArray.length(); i++) {
                                 titlelist.add(dataArray.getJSONObject(i).getString("title"));
                                 timeline_dateList.add(dataArray.getJSONObject(i).getString("timeline_date"))    ;
                                 descriptionList.add(dataArray.getJSONObject(i).getString("description"));
                                 idlist.add(dataArray.getJSONObject(i).getString("id"));
                                 documentlist.add(dataArray.getJSONObject(i).getString("document"));
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext()); //Creating a Request Queue
        requestQueue.add(stringRequest); //Adding request to the queue
    }
}