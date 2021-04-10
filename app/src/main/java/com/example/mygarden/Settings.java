package com.example.mygarden;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Locale;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_settings);

        Button previous_button = findViewById(R.id.previous_settings);
        previous_button.setOnClickListener(v -> onBackPressed());

        TextView email = (TextView) findViewById(R.id.email);
        email.setOnClickListener(v -> {
            final Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("text/plain");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"my.garden.app.help@gmail.com"});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Lacking species");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Species: ");
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        });

        ImageButton onoffNotifications = findViewById(R.id.onoffNotifications);
        onoffNotifications.setOnClickListener(v -> {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                Intent intent = new Intent(android.provider.Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                intent.putExtra(android.provider.Settings.EXTRA_APP_PACKAGE,getPackageName());
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
            else{
                Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:"+getPackageName()));
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        ImageButton language = findViewById(R.id.language);
        language.setOnClickListener(v -> showChangeLanguageDialog());
    }

    String lan="";
    private void showChangeLanguageDialog() {
        final String[] listItems = {"Polski", "English", "Deutsch", "Italiano", "Español"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(Settings.this);
        mBuilder.setTitle("Wybierz język");
        mBuilder.setSingleChoiceItems(listItems, -1, (dialog, i) -> {
            if(i == 0){
                lan="pl";
            }
            else if(i == 1){
                lan="en";
            }
            else if(i == 2){
                lan="de";
            }
            else if(i == 3){
                lan="it";
            }
            else if(i == 4){
                lan="es";
            }
        });

        mBuilder.setNeutralButton(
                "Ok",
                (dialog, id) -> {
                    setLocale(lan);
                    dialog.dismiss();
                    recreate();
                });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    private void setLocale(String lang) {
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR1){
            config.setLocale(new Locale(lang.toLowerCase()));
        } else {
            config.locale = new Locale(lang.toLowerCase());
        }
        resources.updateConfiguration(config, dm);

        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("My_Lang", lang).apply();
    }

    public void loadLocale() {
        String language = PreferenceManager.getDefaultSharedPreferences(this).getString("My_Lang", "");
        setLocale(language);
    }

}