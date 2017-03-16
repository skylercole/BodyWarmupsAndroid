package com.a300apps.bodywarmups;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        final Button termsButton = (Button)this.findViewById(R.id.termsButton);
        termsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent termsIntent = new Intent(InfoActivity.this, TermsActivity.class);
                startActivity(termsIntent);
            }
        });

        final Button privacyButton = (Button)this.findViewById(R.id.privacyButton);
        privacyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent privacyIntent = new Intent(InfoActivity.this, PrivacyPolicyActivity.class);
                startActivity(privacyIntent);
            }
        });
    }
}
