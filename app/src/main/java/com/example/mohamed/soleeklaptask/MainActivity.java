package com.example.mohamed.soleeklaptask;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.mohamed.soleeklaptask.activities.LoginActivity;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private LinkedList<String> CountryList = new LinkedList<>();
    private ListView listView;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init firebase
        mAuth = FirebaseAuth.getInstance();

        // init views
        listView = findViewById(R.id.list_countries);
        btnLogout = findViewById(R.id.btn_logout);


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                LoginManager.getInstance().logOut();

                finish();
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();


        user = mAuth.getCurrentUser();

        if (user == null) {

            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();

        } else {

            // get countries List
            getCountries();

        }

    }

    private void getCountries() {

        String url = "https://restcountries.eu/rest/v2/all";
        JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                CountryList.clear();
                for (int i = 0; i < response.length(); i++){
                    try {
                        JSONObject object = response.getJSONObject(i);
                        CountryList.add(object.getString("name"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                ListAdapter adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.item_row, R.id.txt_country, CountryList);
                listView.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        Volley.newRequestQueue(MainActivity.this).add(request);


    }
}
