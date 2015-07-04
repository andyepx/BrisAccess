package com.whatamelon.brisaccess;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AskHelpActivity extends ActionBarActivity
{
    final String HOTLINE_NUMBER = "0405923289";
    final String SMS_NUMBER = "0405923289";

    TextView titleTextView;
    TextView contentTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_help);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String title = intent.getStringExtra(MainActivity.HELP_TITLE);
        String content = intent.getStringExtra(MainActivity.HELP_CONTENT);

        titleTextView = (TextView) findViewById(R.id.help_title);
        contentTextView = (TextView) findViewById(R.id.help_content);
        titleTextView.setText(title);
        contentTextView.setText(content);
    }

    public void tapToCall(View v)
    {
        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + HOTLINE_NUMBER));
        startActivity(callIntent);
    }

    public void sendSMS(View v)
    {
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.putExtra("address", SMS_NUMBER);
        smsIntent.setType("vnd.android-dir/mms-sms");
        startActivity(smsIntent);
    }
}
