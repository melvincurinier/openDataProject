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

            list.clear();

            JSONObject jsonObject = new JSONObject(flux); // récupère le flux
            JSONArray jsonRecordsArray = jsonObject.getJSONArray("records"); // récupère l'array records
            for(int i = 0; i<10; i++){
                JSONObject jsonAssociationObject = jsonRecordsArray.getJSONObject(i); // récupère la première association
                JSONObject jsonFieldsObject = jsonAssociationObject.getJSONObject("fields"); // récupère l'objet fields
                String title = jsonFieldsObject.getString("short_title"); // récupère le champ short_title de l'objet fields
                String city = jsonFieldsObject.getString("com_name_asso");
                String address = jsonFieldsObject.getString("street_name_manager");
                String postal_code = jsonFieldsObject.getString("pc_address_asso");
                String region = jsonFieldsObject.getString("reg_name");
                String description = jsonFieldsObject.getString("object");
                JSONArray coord = jsonFieldsObject.getJSONArray("geo_point_2d");
                double[] coordTemp = {coord.getDouble(0), coord.getDouble(1)};

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
