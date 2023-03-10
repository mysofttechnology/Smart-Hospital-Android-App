package com.qdocs.smartshospital;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import com.qdocs.smartshospital.patient.PatientDashboard;
import com.qdocs.smartshospital.utils.Constants;
import com.qdocs.smartshospital.utils.Utility;
import java.util.Locale;

public class SplashActivity extends Activity {
    private static final int SPLASH_TIME_OUT = 2000;
    ImageView logoIV;
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        Boolean isLocaleSet;
        try {
            isLocaleSet = Utility.getSharedPreferencesBoolean(getApplicationContext(), "isLocaleSet");
        } catch (NullPointerException e) {
            isLocaleSet = false;
        }
        if(isLocaleSet) {
            setLocale(Utility.getSharedPreferences(getApplicationContext(), Constants.langCode));
        }
        splash();
    }
    private void splash() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Boolean isLoggegIn;
                Boolean isUrlTaken;
                try {
                    isLoggegIn = Utility.getSharedPreferencesBoolean(getApplicationContext(), Constants.isLoggegIn);
                    isUrlTaken = Utility.getSharedPreferencesBoolean(getApplicationContext(), "isUrlTaken");
                } catch (NullPointerException NPE) {
                    isLoggegIn = false;
                    isUrlTaken = false;
                }
                Log.e("loggeg", isLoggegIn.toString());
                Log.e("isUrlTaken", isUrlTaken.toString());

                if(Constants.askUrlFromUser) {
                    if(isUrlTaken) {
                        if(isLoggegIn){
                            Intent i = new Intent(getApplicationContext(), PatientDashboard.class);
                            startActivity(i);
                            finish();
                        }else {
                            Intent i = new Intent(getApplicationContext(),Login.class);
                            startActivity(i);
                            finish();
                        }
                    } else {
                        Intent asd = new Intent(getApplicationContext(),TakeUrl.class);
                        startActivity(asd);
                        finish();
                    }
                } else {
                    if(isLoggegIn){
                        Intent i = new Intent(getApplicationContext(),PatientDashboard.class);
                        startActivity(i);
                        finish();
                    }else {
                        Intent i = new Intent(getApplicationContext(),Login.class);
                        startActivity(i);
                        finish();
                    }
                }
            }
        }, SPLASH_TIME_OUT);
    }
    public void setLocale(String localeName) {
        Locale myLocale = new Locale(localeName);
        Locale.setDefault(myLocale);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Log.e("Status", "Locale updated!");
    }
}
