package com.mc.opendataproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView lv;
    private Button btnRefresh;
    private ArrayList<Association> list;
    private AssociationAdapter adapter;
    private int incr = 0;
    private String start = Integer.toString(incr);

    public static String COORD = null;

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
                list.clear();
                incr = 0;
                start = Integer.toString(incr);
                new AsyncTaskRNA().execute(list, adapter, start);
            }
        });

        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int currentVisibleItemCount;
            private int currentScrollState;
            private int currentFirstVisibleItem;
            private int totalItem;

            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                this.currentScrollState = scrollState;
                this.isScrollCompleted();
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                this.currentFirstVisibleItem = firstVisibleItem;
                this.currentVisibleItemCount = visibleItemCount;
                this.totalItem = totalItemCount;
            }

            private void isScrollCompleted() {
                if (totalItem - currentFirstVisibleItem == currentVisibleItemCount
                        && this.currentScrollState == SCROLL_STATE_IDLE) {
                    incr += 10;
                    start = Integer.toString(incr);
                    new AsyncTaskRNA().execute(list, adapter, start);
                }
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intentMap = new Intent(MainActivity.this,MapsActivity.class);
                intentMap.putExtra(COORD, list.get(i).getCoord());
                startActivity(intentMap);
            }
        });
    }
}