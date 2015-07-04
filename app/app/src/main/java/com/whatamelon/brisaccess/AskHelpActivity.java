package com.whatamelon.brisaccess;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

public class AskHelpActivity extends ActionBarActivity
{
    TextView titleTextView;
    TextView contentTextView;
    Button callButton;
    Button smsButton;

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
}
