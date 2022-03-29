package com.mc.opendataproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView lv;
    private Button btnRefresh;
    private ArrayList<Association> list;
    private AssociationAdapter adapter;
    private EditText filtreTv;

    private int incr = 0;
    private String start = Integer.toString(incr);
    private Context context;

    public static String COORD = null;
    public static final String FILE_NAME = "saveFile";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = findViewById(R.id.list);
        btnRefresh = findViewById(R.id.btnRefresh);
        filtreTv = findViewById(R.id.editTextFiltre);

        File file = new File(this.getFilesDir(), FILE_NAME);
        if(!file.exists()){
            Snackbar.make(lv, "Aucun fichier",Snackbar.LENGTH_LONG).show();
        }
        else{
            try{
                InputStream in = openFileInput(FILE_NAME);
                ObjectInputStream oin = new ObjectInputStream(in);
                list = (ArrayList<Association>) oin.readObject();
                oin.close();
            }
            catch (ClassNotFoundException | IOException e){
                Snackbar.make(lv, "Impossible de récupérer les données du fichier",Snackbar.LENGTH_LONG).show();
            }
        }
        if (list == null) list = new ArrayList<>();
        context = getApplicationContext();
        adapter = new AssociationAdapter(context, list);
        lv.setAdapter(adapter);

        btnRefresh.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(isOnline(context)) {
                    list.clear();
                    incr = 0;
                    start = Integer.toString(incr);
                    new AsyncTaskRNA().execute(list, adapter, start, filtreTv.getText().toString());
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
                        new AsyncTaskRNA().execute(list, adapter, start, filtreTv.getText().toString());
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
                Intent intentAssociation = new Intent(MainActivity.this,AssociationActivity.class);
                intentAssociation.putExtra("titleX", list.get(i).getTitle());
                intentAssociation.putExtra("descritionX", list.get(i).getDescription());
                intentAssociation.putExtra("addressX", list.get(i).getAddress());
                intentAssociation.putExtra("cityX", list.get(i).getCity());
                intentAssociation.putExtra("postalcodeX", list.get(i).getPostal_code());
                intentAssociation.putExtra("regionX", list.get(i).getRegion());
                startActivity(intentAssociation);
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intentMap = new Intent(MainActivity.this,MapsActivity.class);

                intentMap.putExtra("titre",list.get(i).getTitle().toString());
                intentMap.putExtra("description",list.get(i).getDescription());
                intentMap.putExtra("coord", list.get(i).getCoord());


                startActivity(intentMap);
                return true;
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