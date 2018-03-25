package com.mwafrikasoftworks.patabackend;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.*;
import cz.msebera.android.httpclient.Header;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AddServiceProviderActivity extends AppCompatActivity {

    List townslist = new ArrayList();
    List servicetypeslist = new ArrayList();
    Spinner townspinner,servicetypespinner;
    EditText spname,sptel,spaddress,splatitude,splongitude;
    Button save,refresh_coordinates;
    Map<Integer,String> serviceidmap = new HashMap<>();
    Map<Integer,String> townsidmap = new HashMap<>();
    String masterUrl = "http://188.166.145.221:8080/pata/MobileApp.do";
    //String masterUrl = "http://10.0.2.2:8084/pata/MobileApp.do";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service_provider);

        townspinner = (Spinner)findViewById(R.id.town_spinner);
        servicetypespinner = (Spinner)findViewById(R.id.servicetype_spinner);
        spname = (EditText)findViewById(R.id.serviceprovidername_editText);
        sptel = (EditText)findViewById(R.id.telephone_editText);
        spaddress = (EditText)findViewById(R.id.address_editText);
        splatitude = (EditText)findViewById(R.id.latitude_editText);
        splongitude = (EditText)findViewById(R.id.longitude_editText);
        save = (Button)findViewById(R.id.save_button);
        refresh_coordinates = (Button)findViewById(R.id.refreshcoordinates_button);

        try
        {
            LocManager locManager = new LocManager(AddServiceProviderActivity.this);
            double latitude = locManager.getLatitude();
            double longitude = locManager.getLongitude();
            splatitude.setText(""+latitude);
            splongitude.setText(""+longitude);

            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
            RequestParams requestParams = new RequestParams();
            //requestParams.add("mparam","list_towns");
            requestParams.add("mparam","add_sp");
            requestParams.add("countryid","126");



            JsonHttpResponseHandler jsonHttpResponseHandler = new JsonHttpResponseHandler()
            {
                @Override
                public void onSuccess(JSONArray jsonArray)
                {
                    JSONArray townlist=null;
                    JSONArray services=null;

                    try
                    {
                        townlist = jsonArray.getJSONArray(0);
                        services = jsonArray.getJSONArray(1);

                    }
                    catch(Exception ex)
                    {
                        ex.printStackTrace();
                    }

                    for(int i=0;i<services.length();i++)
                    {
                        try
                        {
                            JSONObject jsonObject = services.getJSONObject(i);
                            String id = jsonObject.getString("id");
                            String servicename = jsonObject.getString("servicetype");
                            serviceidmap.put(Integer.parseInt(id),servicename);
                            servicetypeslist.add(servicename);
                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();

                        }

                    }
                    for(int i=0;i<townlist.length();i++)
                    {
                        try
                        {
                            JSONObject jsonObject = townlist.getJSONObject(i);
                            String id = jsonObject.getString("id");
                            String townname = jsonObject.getString("town");
                            townsidmap.put(Integer.parseInt(id),townname);
                            townslist.add(townname);
                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();

                        }

                    }
                    ArrayAdapter townsarrayAdapter = new ArrayAdapter(getApplicationContext(),R.layout.spinner_support_layout,townslist);
                    ArrayAdapter servicesarrayAdapter = new ArrayAdapter(getApplicationContext(),R.layout.spinner_support_layout,servicetypeslist);
                    servicetypespinner.setAdapter(servicesarrayAdapter);
                    townspinner.setAdapter(townsarrayAdapter);




                }
                @Override
                public void onFailure(Throwable e, JSONObject errorResponse) {
                    super.onFailure(e, errorResponse);
                    e.printStackTrace();

                }
            };

            asyncHttpClient.post(masterUrl,requestParams,jsonHttpResponseHandler);



        }
        catch(Exception ex)
        {
            Toast.makeText(getApplicationContext(),ex.toString(),Toast.LENGTH_LONG).show();

        }

        refresh_coordinates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocManager locManager = new LocManager(AddServiceProviderActivity.this);
                double latitude = locManager.getLatitude();
                double longitude = locManager.getLongitude();
                splatitude.setText(""+latitude);
                splongitude.setText(""+longitude);

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try
                {
                    boolean verified=true;
                    String serviceprovidername = spname.getText().toString();
                    String serviceprovideraddress = spaddress.getText().toString();
                    String serviceprovidertel = sptel.getText().toString();
                    String serviceproviderlatitude = splatitude.getText().toString();
                    String serviceproviderlongitude = splongitude.getText().toString();
                    String serviceprovidertownname = townspinner.getSelectedItem().toString();
                    String serviceproviderservicename = servicetypespinner.getSelectedItem().toString();
                    String serviceprovidertownid=null;
                    String serviceproviderserviceid=null;

                    if(serviceprovidername.trim().equals(""))
                    {
                        spname.setError("Service provider name is required");
                        verified=false;

                    }
                    if(serviceprovidertel.trim().equals(""))
                    {
                        sptel.setError("Service provider telephone is required");
                        verified=false;

                    }
                    if(serviceprovideraddress.trim().equals(""))
                    {
                        spaddress.setError("Service provider address is required");
                        verified=false;

                    }


                    if(serviceproviderlatitude.trim().equals(""))
                    {

                        splatitude.setError("Service provider latitude is required");
                        verified=false;

                    }

                    if(serviceproviderlongitude.trim().equals(""))
                    {
                        splongitude.setError("Service provider longitude is required");
                        verified=false;
                        //spname.setBackgroundColor(Color.RED);
                    }

                    if(verified)
                    {


                    //get id of selected service
                    for (Map.Entry<Integer, String> entry : serviceidmap.entrySet()) {
                        if (entry.getValue().equals(serviceproviderservicename)) {
                            serviceproviderserviceid = entry.getKey().toString();
                            //System.out.println(entry.getKey());
                            // Log.e(entry.getKey().toString(),"xcsdc");
                        }
                    }

                    //get id of selected town
                    for (Map.Entry<Integer, String> entry : townsidmap.entrySet()) {
                        if (entry.getValue().equals(serviceprovidertownname)) {
                            serviceprovidertownid= entry.getKey().toString();
                            //System.out.println(entry.getKey());
                            // Log.e(entry.getKey().toString(),"xcsdc");
                        }
                    }

                    AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                    RequestParams requestParams = new RequestParams();
                    requestParams.add("mparam","save_sp");
                    requestParams.add("name",serviceprovidername);
                    requestParams.add("number",serviceprovidertel);
                    requestParams.add("address",serviceprovideraddress);
                    requestParams.add("town",serviceprovidertownid);
                    requestParams.add("servicetype",serviceproviderserviceid);
                    requestParams.add("latitude",serviceproviderlatitude);
                    requestParams.add("longitude",serviceproviderlongitude);

                    /*JsonHttpResponseHandler jsonHttpResponseHandler = new JsonHttpResponseHandler()
                    {
                        @Override
                        public void onSuccess(JSONArray jsonArray)
                        {
                            try
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                Toast.makeText(getApplicationContext(),"Saved Successfully",Toast.LENGTH_LONG).show();

                            }
                            catch (Exception ex)
                            {
                                ex.printStackTrace();

                            }





                        }
                        @Override
                        public void onFailure(Throwable e, JSONObject errorResponse) {
                            super.onFailure(e, errorResponse);
                            e.printStackTrace();

                        }
                    };*/
                    TextHttpResponseHandler textHttpResponseHandler = new TextHttpResponseHandler(){
                        @Override
                        public void onSuccess(String content) {
                            Toast.makeText(getApplicationContext(),content,Toast.LENGTH_LONG).show();
                            spname.setText("");
                            spaddress.setText("");
                            sptel.setText("");

                        }
                    };

                    asyncHttpClient.post(masterUrl,requestParams,textHttpResponseHandler);
                    }
                }

                catch(Exception ex)
                {
                    Toast.makeText(getApplicationContext(),ex.toString(),Toast.LENGTH_LONG).show();

                }

            }
        });


    }
}
