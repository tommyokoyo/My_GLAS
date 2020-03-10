package com.example.glas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class StudentLoginActivity extends AppCompatActivity {
    private TextView link_signup;
    private Button btn_signin;
    private EditText etadmission, etpassword;
    private ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        link_signup = findViewById(R.id.link_signup);
        btn_signin = findViewById(R.id.btn_signin);
        loading = findViewById(R.id.loading);
        etadmission = findViewById(R.id.etadmission);
        etpassword = findViewById(R.id.etpassword);

        link_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StudentLoginActivity.this, StudentRegisterActivity.class));
            }
        });

        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signin();
            }
        });
    }

    private void signin() {
        loading.setVisibility(View.VISIBLE);
        btn_signin.setVisibility(View.GONE);

        final String admission = this.etadmission.getText().toString().trim();
        final String password = this.etpassword.getText().toString().trim();


        if (TextUtils.isEmpty(admission)){
            etadmission.setError("Enter username");
            etadmission.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)){
            etpassword.setError("Enter password");
            etpassword.requestFocus();
            return;
        }
        RequestQueue requestQueue = Volley.newRequestQueue(StudentLoginActivity.this);
        StringRequest stringRequest= new StringRequest(Request.Method.POST, URLs.URL_STUDENT_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    boolean error = obj.getBoolean("error");
                    //if no error response
                    if (error == false){
                        Toast.makeText(StudentLoginActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent( StudentLoginActivity.this, StudentActivity.class));
                    }
                    //if error response
                    if(error==true) {
                        Toast.makeText(StudentLoginActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                        loading.setVisibility(View.GONE);
                        btn_signin.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(StudentLoginActivity.this,"Register Error" +error.toString(),Toast.LENGTH_SHORT).show();
                loading.setVisibility(View.GONE);
                btn_signin.setVisibility(View.VISIBLE);
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("admission", admission);
                params.put("password", password);
                return params;
            }
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("content type","application/x-www-form-urlencoded");
                return params;
            }
        };
        requestQueue.add(stringRequest);

    }
}
