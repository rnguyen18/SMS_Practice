package com.rmn.smsproject;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_SMS = 0;
    String phoneNo = "2569197481";
    String message = "wait stop";
    public static final String OTP_REGEX = "[0-9]{1,6}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button sendBtn = findViewById(R.id.btnSendSMS);

//        checkPermissions();

         // else { }

        final Activity con = this;

        sendBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                /*Toast.makeText(getApplicationContext(), (ContextCompat.checkSelfPermission(con, Manifest.permission.SEND_SMS) + "!=" + PackageManager.PERMISSION_GRANTED), Toast.LENGTH_SHORT).show();

                if (ContextCompat.checkSelfPermission(con, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(getApplicationContext(), "1:" + ActivityCompat.shouldShowRequestPermissionRationale(con, Manifest.permission.SEND_SMS),
                            Toast.LENGTH_SHORT).show();

                    if (ActivityCompat.shouldShowRequestPermissionRationale(con, Manifest.permission.SEND_SMS)) {

                    } else {
                        ActivityCompat.requestPermissions(con,
                                new String[]{Manifest.permission.SEND_SMS},
                                MY_PERMISSIONS_REQUEST_SEND_SMS);


                        Toast.makeText(getApplicationContext(), "Get Permission",
                                Toast.LENGTH_SHORT).show();
                    }

                }*/


                Toast.makeText(getApplicationContext(), (getAllSmsFromProvider()).get(1),
                        Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
//        Toast.makeText(getApplicationContext(), "reqcode",
//                Toast.LENGTH_LONG).show();
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    Toast.makeText(getApplicationContext(), "SMS sent.",
                            Toast.LENGTH_LONG).show();
//                    smsManager.sendTextMessage(phoneNo, null, message, null, null);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS failed, please try again.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    /*protected void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_READ_SMS);
            }
        }
    }*/

    public ArrayList<String> getAllSmsFromProvider() {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_SMS},MY_PERMISSIONS_REQUEST_READ_SMS );

        ArrayList<String> lstSms = new ArrayList<>();
        ContentResolver cr = this.getContentResolver();

        Cursor c = cr.query(Telephony.Sms.Inbox.CONTENT_URI, // Official CONTENT_URI from docs
                new String[] { Telephony.Sms.Inbox.BODY }, // Select body text
                null,
                null,
                Telephony.Sms.Inbox.DEFAULT_SORT_ORDER); // Default sort order

        int totalSMS = c.getCount();

        if (c.moveToFirst()) {
            for (int i = 0; i < totalSMS; i++) {
                lstSms.add(c.getString(0));
                c.moveToNext();
            }
        } else {
            throw new RuntimeException("You have no SMS in Inbox");
        }
        c.close();

        return lstSms;
    }
}
