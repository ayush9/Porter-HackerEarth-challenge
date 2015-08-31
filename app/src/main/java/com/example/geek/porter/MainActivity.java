package com.example.geek.porter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity {
    Context ctx;
  String str;
    ListView lv=null;
    Context context;
    private ProgressDialog pDialog;
    ArrayList<Bitmap> bitmapArray = new ArrayList<Bitmap>();

    // URL to get contacts JSON
    private static String url1 = "http://porter.0x10.info/api/parcel?type=json&query=list_parcel";
   private static String url2 ="http://porter.0x10.info/api/parcel?type=json&query=api_hits";
    // contacts JSONArray
    JSONArray contacts = null;
    JSONArray contact1=null;
 
    // Hashmap for ListView
    ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();
   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        setContentView(R.layout.activity_main);
        showOverLay();
         new GetContacts().execute();

    }


    private void showOverLay(){

        final Dialog dialog = new Dialog(ctx, android.R.style.Theme_Translucent_NoTitleBar);

        dialog.setContentView(R.layout.overlay_view);

        LinearLayout layout = (LinearLayout) dialog.findViewById(R.id.overlayLayout);

        layout.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View arg0) {

                dialog.dismiss();

            }

        });

        dialog.show();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
 
        return super.onCreateOptionsMenu(menu);
    }



private class GetContacts extends AsyncTask<Void, Void, Void> {
	 
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Showing progress dialog
        pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

    }

    @Override
    protected Void doInBackground(Void... arg0) {
        // Creating service handler class instance
        ServiceHandler sh = new ServiceHandler();

        // Making a request to url and getting response
        String jsonStr = sh.makeServiceCall(url1, ServiceHandler.GET);
String aa = sh.makeServiceCall(url2,ServiceHandler.GET);
        Log.d("Response: ", "> " + jsonStr);

        if (jsonStr != null) {
            try {
               // JSONObject jsonObj = new JSONObject(jsonStr);
                 
                // Getting JSON Array node
                JSONObject p=(new JSONObject(jsonStr));
              JSONObject q = (new JSONObject(aa));
                str =  q.getString("api_hits");
                contacts = new JSONArray(p.getString("parcels"));

                // looping through All Contacts
                for (int i = 0; i < contacts.length(); i++) {
                    JSONObject c = contacts.getJSONObject(i);
                     
                    String name = c.getString("name");
                    String imagee = c.getString("image");
                    String urldisplay = imagee;
                    String type = c.getString("type");
                    String date = c.getString("date");
                    String weight = c.getString("weight");
                    String phone = c.getString("phone");
                    String price = c.getString("price");
                    String quantity = c.getString("quantity");
                    String color = c.getString("color");
                    String link = c.getString("link");
                    

                    

                    // tmp hashmap for single contact
                    HashMap<String, String> contact = new HashMap<String, String>();

                    // adding each child node to HashMap key => value
                    contact.put("name", name);
                    contact.put("imagee", imagee);
                    contact.put("type", type);
                    contact.put("price", price);
                    contact.put("date", date);
                    contact.put("weight", weight);
                    contact.put("color", color);
                    contact.put("link", link);
                    contact.put("quantity", quantity);
                    contact.put("phone", phone);
//contact.put("api_hit",str);
                    // adding contact to contact list
                    contactList.add(contact);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        // Dismiss the progress dialog
        if (pDialog.isShowing())
            pDialog.dismiss();

        
        lv=(ListView) findViewById(R.id.listView);
        lv.setAdapter(new CustomAdapter1(MainActivity.this, contactList)); // lv.setAdapter(new CustomAdapter1(MainActivity.this, contactList,bitmapArray));
        TextView t1 = (TextView)findViewById(R.id.str);
        t1.setText(str);
    }

}

}