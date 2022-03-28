package com.mc.opendataproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

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
    private Context context;

    public static String COORD = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = findViewById(R.id.list);
        btnRefresh = findViewById(R.id.btnRefresh);
        list = new ArrayList<>();
        context = this;
        adapter = new AssociationAdapter(context, list);
        lv.setAdapter(adapter);

        btnRefresh.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(isOnline(context)) {
                    list.clear();
                    incr = 0;
                    start = Integer.toString(incr);
                    new AsyncTaskRNA().execute(list, adapter, start);
                } else {
                    Toast toast = Toast.makeText(context, "No connection", Toast.LENGTH_SHORT);
                    toast.show();
                }
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
                if(isOnline(context)) {
                    if (totalItem - currentFirstVisibleItem == currentVisibleItemCount
                            && this.currentScrollState == SCROLL_STATE_IDLE) {
                        incr += 10;
                        start = Integer.toString(incr);
                        new AsyncTaskRNA().execute(list, adapter, start);
                    }
                } else {
                    Toast toast = Toast.makeText(context, "No connection", Toast.LENGTH_SHORT);
                    toast.show();
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

    public boolean isOnline(Context context){
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        }
        @SuppressLint("MissingPermission") NetworkInfo info = connectivity.getActiveNetworkInfo();
        return info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI;
    }
}