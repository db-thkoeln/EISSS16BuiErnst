package buiernst.eis.planto;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

import butterknife.ButterKnife;


public class LoginActivity extends AppCompatActivity {

    OkHttp callUser = new OkHttp();
    String userurl = "http://192.168.1.7:8888/user/";
    Integer userid;
    String respUser;
    Button loginbtn;
    EditText name;
    EditText passwort;
    String userName;
    String userPassword;
    String nameString;
    String passwortString;

    JSONObject jsonObjectUser;
    JSONArray jsonArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        name = (EditText) findViewById(R.id.name);
        passwort = (EditText) findViewById(R.id.password);
        loginbtn = (Button) findViewById(R.id.loginbtn);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

    }

    public void login() {

        nameString = name.getText().toString();
        passwortString = passwort.getText().toString();

        try {
            // HTTP Get on User Ressource
            Call get = callUser.doGetRequest(userurl , new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    respUser = response.body().string();
                    try {
                        //converting to an Object
                        //Iterate through the JSON-response to get the Data
                        jsonObjectUser = new JSONObject(respUser);
                        jsonArray = jsonObjectUser.getJSONArray("user");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject obj = jsonArray.getJSONObject(i);
                            userid = obj.getInt("id");
                            userName = obj.getString("name");
                            userPassword = obj.getString("passwort");
                            if(userName.equals(nameString) && userPassword.equals(passwortString)){
                                startNextActivity();
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void startNextActivity() {
        Intent intent = new Intent(LoginActivity.this, PlantlistActivity.class);
        intent.putExtra("UserID", userid);
        startActivity(intent);
    }

}