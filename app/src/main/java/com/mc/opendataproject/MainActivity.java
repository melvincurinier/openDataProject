package com.mc.opendataproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView lv;
    private Button btnRefresh;
    private ArrayList<Association> list;
    private AssociationAdapter adapter;
/*
    public static String COORD = null;
    public static String NOM = null;
    public static  String DESCRIPTION = null;
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = findViewById(R.id.list);
        btnRefresh = findViewById(R.id.btnRefresh);
        list = new ArrayList<>();
        adapter = new AssociationAdapter(this, list);
        lv.setAdapter(adapter);

        btnRefresh.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                new AsyncTaskRNA().execute(list, adapter);
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intentMap = new Intent(MainActivity.this,MapsActivity.class);

                intentMap.putExtra("titre",list.get(i).getTitle().toString());
                intentMap.putExtra("description",list.get(i).getDescription());
                intentMap.putExtra("coord", list.get(i).getCoord());


                startActivity(intentMap);
            }
        });
    }
}