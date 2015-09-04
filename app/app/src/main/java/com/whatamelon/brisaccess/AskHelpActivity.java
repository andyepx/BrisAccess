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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

public class AskHelpActivity extends ActionBarActivity
{
    final String HOTLINE_NUMBER = "131617";
    final String SMS_NUMBER = "0428774636";

    ImageView helpIcon;
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
        int iconId = intent.getIntExtra(MainActivity.HELP_ICON, R.drawable.assist_icon);
        String title = intent.getStringExtra(MainActivity.HELP_TITLE);
        String content = intent.getStringExtra(MainActivity.HELP_CONTENT);

        helpIcon = (ImageView) findViewById(R.id.help_icon);
        titleTextView = (TextView) findViewById(R.id.help_title);
        contentTextView = (TextView) findViewById(R.id.help_content);

        helpIcon.setImageDrawable(null);
        helpIcon.setBackgroundResource(iconId);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        onBackPressed();
        return true;
    }
}
