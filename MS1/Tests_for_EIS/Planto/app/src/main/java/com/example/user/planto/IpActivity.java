package com.example.user.planto;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.planto.R;
import com.example.user.planto.WeatherActivity;

public class IpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip);

        final EditText getIp = (EditText) findViewById(R.id.ipText);

        Button getIpButton = (Button) findViewById(R.id.getIpButton);

        getIpButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v){
                String ip = getIp.getText().toString();
                Intent intent = new Intent(IpActivity.this, WeatherActivity.class);
                intent.putExtra("IP", ip);
                startActivity(intent);
            }
        });
    }

}
