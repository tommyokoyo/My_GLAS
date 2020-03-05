package com.example.glas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    TextView link_signin;
    EditText etname, etemail, etpassword;
    Button btn_signup;
    ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //if the user is already logged in we will directly start the profile activity
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, TeacherDashboard.class));
            return;
        }

        etname = findViewById(R.id.etname);
        etemail = findViewById(R.id.etemail);
        etpassword = findViewById(R.id.etpassword);
        btn_signup = findViewById(R.id.btn_signup);
        loading = findViewById(R.id.loading);

        link_signin = findViewById(R.id.link_signin);
        link_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent( MainActivity.this, LoginActivity.class));
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Regist();
            }
        });

    }

    private void Regist(){
        loading.setVisibility(View.VISIBLE);
        btn_signup.setVisibility(View.GONE);

        final String name = this.etname.getText().toString().trim();
        final String email = this.etemail.getText().toString().trim();
        final String password = this.etpassword.getText().toString().trim();

        if (TextUtils.isEmpty(name)){
            etname.setError("Enter username");
            etname.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(email)){
            etemail.setError("Enter email");
            etemail.requestFocus();
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etemail.setError("Enter a valid email");
            etemail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)){
            etpassword.setError("Enter password");
            etpassword.requestFocus();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_REGISTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);

                    if (!obj.getBoolean("error")){
                        Toast.makeText(MainActivity.this, obj.getString("message"),Toast.LENGTH_SHORT).show();

                        //getting the user from the response
                        JSONObject userJson = obj.getJSONObject("user");

                        //creating a new user object
                        User user = new User(
                                userJson.getString("name"),
                                userJson.getString("email")
                        );

                        //storing the user in shared preferences
                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                        //starting the profile activity
                        finish();
                        startActivity(new Intent(MainActivity.this, TeacherDashboard.class));
                    } else {
                        Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this,"Register Error" +e.toString(),Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.GONE);
                    btn_signup.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"Register Error" +error.toString(),Toast.LENGTH_SHORT).show();
                loading.setVisibility(View.GONE);
                btn_signup.setVisibility(View.VISIBLE);
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                getParams().put("name", name);
                getParams().put("email", email);
                getParams().put("password", password);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
