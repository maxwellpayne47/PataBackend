package com.mwafrikasoftworks.patabackend;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button addserviceprovider,editserviceprovider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addserviceprovider = (Button)findViewById(R.id.addserviceprovider_button);
        addserviceprovider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AddServiceProviderActivity.class);
                startActivity(intent);

            }
        });
    }
}
