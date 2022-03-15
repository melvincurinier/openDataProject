package com.mc.opendataproject;

import android.os.AsyncTask;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AsyncTaskRNA extends AsyncTask<Object, Void, String> {

    private ListView lv;
    private String format;
    private AssociationAdapter adapter;
    private ArrayList<Association> list;

    public static final String API_LINK = "https://public.opendatasoft.com/api/records/1.0/search/?dataset=ref-france-association-repertoire-national&q=&facet=management&facet=nature&facet=group&facet=ispublic&facet=position&facet=dep_name&facet=epci_name&facet=reg_name&facet=com_arm_area_code";

    @Override
    protected String doInBackground(Object... params) {
        BufferedReader in = null;
        String temp="", str;

        list = (ArrayList<Association>) params[0];
        adapter = (AssociationAdapter) params[1];

        URL url = null;
        HttpURLConnection urlConnection = null;
        String flux = "";
        try {
            url = new URL(API_LINK);
            urlConnection = (HttpURLConnection) url.openConnection();
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                while((temp = in.readLine()) != null){
                    stringBuilder.append(temp);
                }
                flux = stringBuilder.toString();
                in.close();
            }

            JSONObject jsonObject = new JSONObject(flux);
            Log.d("AsyncTask<<" , "temp = " + jsonObject.toString());

        } catch (IOException e) {
            e.printStackTrace();
            temp = "pb connexion";
        }catch (JSONException e){
            e.printStackTrace();
            temp = "erreur de format de donnée";
        } finally {
            urlConnection.disconnect();
        }
        return temp;
    }

    @Override
    protected void onPostExecute(String result){
        super.onPostExecute(result);
        adapter.notifyDataSetChanged();
    }
}
