package com.mysofttechnology.smartshospital.patient;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.mysofttechnology.smartshospital.BaseActivity;
import com.mysofttechnology.smartshospital.R;
import com.mysofttechnology.smartshospital.adapters.ViewPagerAdapter;
import com.mysofttechnology.smartshospital.fragments.PatientIPDBedHistoryFragment;
import com.mysofttechnology.smartshospital.fragments.PatientIPDChargeFragment;
import com.mysofttechnology.smartshospital.fragments.PatientIPDConsultantFragment;
import com.mysofttechnology.smartshospital.fragments.PatientIPDLabInvestigationFragment;
import com.mysofttechnology.smartshospital.fragments.PatientIPDLiveFragment;
import com.mysofttechnology.smartshospital.fragments.PatientIPDMedicationFragment;
import com.mysofttechnology.smartshospital.fragments.PatientIPDNurseNoteFragment;
import com.mysofttechnology.smartshospital.fragments.PatientIPDOperationFragment;
import com.mysofttechnology.smartshospital.fragments.PatientIPDPaymentFragment;
import com.mysofttechnology.smartshospital.fragments.PatientIPDPrescriptionFragment;
import com.mysofttechnology.smartshospital.fragments.PatientIPDTimelineFragment;
import com.mysofttechnology.smartshospital.fragments.PatientIPDTreatmentHistoryFragment;
import com.mysofttechnology.smartshospital.utils.Constants;
import com.mysofttechnology.smartshospital.utils.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class PatientIpdDischargeDetailsList extends BaseActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    private int[] tabIcons = {
            R.drawable.ic_timeline,
            R.drawable.ic_timeline,
            R.drawable.prescription,
            R.drawable.ic_profile_plus,
            R.drawable.ic_operation,
            R.drawable.ic_timeline,
            R.drawable.payment,
            R.drawable.ic_liveconsult,
            R.drawable.ic_timeline,
            R.drawable.ic_timeline,
            R.drawable.payment,
            R.drawable.ic_bed,

    };
    FrameLayout dischargelist;
    ArrayList<String> ipdidlist = new ArrayList<String>();
    public TextView ipdno,discharge_status,gender,admdate,phone, bed;
    TextView doctor,name,dischrge_date,payments,age,credit_limit;
    public LinearLayout baseActivity_paymentBtn,dischargedBtn;
    public String defaultDateFormat,defaultDatetimeFormat,currency;
    public Map<String, String> params = new Hashtable<String, String>();
    public Map<String, String> headers = new HashMap<String, String>();
    ImageView discharge_summary;
    String ipdnos="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_patient_ipd_details_list2, null, false);
        mDrawerLayout.addView(contentView, 0);
        ipdnos= getIntent().getStringExtra("IPDNO");

        defaultDatetimeFormat = Utility.getSharedPreferences(getApplicationContext(), "datetimeFormat");
        defaultDateFormat = Utility.getSharedPreferences(getApplicationContext(), "dateFormat");
        currency = Utility.getSharedPreferences(getApplicationContext(), Constants.currency);
        titleTV.setText(getApplicationContext().getString(R.string.IPD));
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        dischargelist = (FrameLayout) findViewById(R.id.dischargelist);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this);
        viewPagerAdapter.addFragment(new PatientIPDMedicationFragment(ipdnos), getApplicationContext().getString(R.string.medication), tabIcons[0]);
        viewPagerAdapter.addFragment(new PatientIPDPrescriptionFragment(ipdnos), getApplicationContext().getString(R.string.prescription), tabIcons[1]);
        viewPagerAdapter.addFragment(new PatientIPDConsultantFragment(ipdnos), getApplicationContext().getString(R.string.consultant), tabIcons[2]);
        viewPagerAdapter.addFragment(new PatientIPDLabInvestigationFragment(ipdnos), getApplicationContext().getString(R.string.labinvestigation), tabIcons[3]);
        viewPagerAdapter.addFragment(new PatientIPDOperationFragment(ipdnos), getApplicationContext().getString(R.string.operation_theatre), tabIcons[4]);
        viewPagerAdapter.addFragment(new PatientIPDChargeFragment(ipdnos), getApplicationContext().getString(R.string.charge), tabIcons[5]);
        viewPagerAdapter.addFragment(new PatientIPDPaymentFragment(ipdnos), getApplicationContext().getString(R.string.payment), tabIcons[6]);
        viewPagerAdapter.addFragment(new PatientIPDLiveFragment(ipdnos), getApplicationContext().getString(R.string.liveconsult), tabIcons[7]);
        viewPagerAdapter.addFragment(new PatientIPDNurseNoteFragment(ipdnos), getApplicationContext().getString(R.string.nurse_note), tabIcons[8]);
        viewPagerAdapter.addFragment(new PatientIPDTimelineFragment(ipdnos), getApplicationContext().getString(R.string.timeline), tabIcons[9]);
        viewPagerAdapter.addFragment(new PatientIPDTreatmentHistoryFragment(ipdnos), getApplicationContext().getString(R.string.treatmenthistory), tabIcons[10]);
        viewPagerAdapter.addFragment(new PatientIPDBedHistoryFragment(ipdnos), getApplicationContext().getString(R.string.bed_history), tabIcons[11]);

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.primaryColour)));
        highLightCurrentTab(0); // for initial selected tab view
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                highLightCurrentTab(position); // for tab change

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    private void highLightCurrentTab(int position) {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            assert tab != null;
            tab.setCustomView(null);
            tab.setCustomView(viewPagerAdapter.getTabView(i));
        }
        TabLayout.Tab tab = tabLayout.getTabAt(position);
        assert tab != null;
        tab.setCustomView(null);
        tab.setCustomView(viewPagerAdapter.getSelectedTabView(position));
    }


    }