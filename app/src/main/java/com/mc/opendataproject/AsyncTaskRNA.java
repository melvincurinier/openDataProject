package com.mc.opendataproject;

import android.os.AsyncTask;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
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
    private String count;
    private String filtre;


    @Override
    protected String doInBackground(Object... params) {
        BufferedReader in = null;
        String temp="", str;

        list = (ArrayList<Association>) params[0];
        adapter = (AssociationAdapter) params[1];
        count = (String) params[2];
        filtre = (String) params[3];

        String API_LINK = "https://public.opendatasoft.com/api/records/1.0/search/?dataset=ref-france-association-repertoire-national&q="+filtre+"&start="+count+"&facet=management&facet=nature&facet=group&facet=ispublic&facet=position&facet=dep_name&facet=epci_name&facet=reg_name&facet=com_arm_area_code";
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

            JSONObject jsonObject = new JSONObject(flux); // récupère le flux
            JSONArray jsonRecordsArray = jsonObject.getJSONArray("records"); // récupère l'array records
            for(int i = 0; i<10; i++){
                JSONObject jsonAssociationObject = jsonRecordsArray.getJSONObject(i); // récupère la première association
                JSONObject jsonFieldsObject = jsonAssociationObject.getJSONObject("fields"); // récupère l'objet fields
                String title;
                String city;
                String address;
                String postal_code;
                String region;
                String description;
                double[] coordTemp;
                try {
                    title = jsonFieldsObject.getString("short_title");
                } catch (JSONException e){
                    title = "Non renseigné";
                }
                try {
                    city = jsonFieldsObject.getString("com_name_asso");
                } catch (JSONException e){
                    city = "Non renseigné";
                }
                try{
                    address = jsonFieldsObject.getString("street_name_manager");
                } catch (JSONException e){
                    address = "Non renseigné";
                }
                try{
                    postal_code = jsonFieldsObject.getString("pc_address_asso");
                } catch (JSONException e){
                    postal_code = "Non renseigné";
                }
                try{
                    region = jsonFieldsObject.getString("reg_name");
                } catch (JSONException e){
                    region = "Non renseigné";
                }
                try{
                    description = jsonFieldsObject.getString("object");
                } catch (JSONException e){
                    description = "Non renseigné";
                }
                try {
                    JSONArray coord = jsonFieldsObject.getJSONArray("geo_point_2d");
                    coordTemp = new double[]{coord.getDouble(0), coord.getDouble(1)};
                } catch (JSONException e){
                    coordTemp = new double[]{0.0, 0.0};
                }

                list.add(new Association(city, address, title, description, postal_code, region, coordTemp));
                Log.d("Coordonnées "+ title, coordTemp[0] + " " + coordTemp[1]);
            }
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
