package com.mc.opendataproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class AssociationActivity extends AppCompatActivity {

    private TextView titleTv;
    private TextView cityTv;
    private TextView descriptionTv;
    private TextView regionTv;
    private TextView codePostalTv;
    private TextView addressTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_association);

        titleTv = (TextView) findViewById(R.id.textViewTitleX);
        cityTv = (TextView) findViewById(R.id.textViewCityX);
        descriptionTv = (TextView) findViewById(R.id.textViewDescriptionX);
        regionTv = (TextView) findViewById(R.id.textViewRegionX);
        codePostalTv = (TextView) findViewById(R.id.textViewPostalCodeX);
        addressTv = (TextView) findViewById(R.id.textViewAddressX);

        titleTv.setText(getIntent().getStringExtra("titleX"));
        descriptionTv.setText(getIntent().getStringExtra("descritionX"));
        addressTv.setText(getIntent().getStringExtra("addressX"));
        cityTv.setText(getIntent().getStringExtra("cityX"));
        codePostalTv.setText(getIntent().getStringExtra("postalcodeX"));
        regionTv.setText(getIntent().getStringExtra("regionX"));
    }
}