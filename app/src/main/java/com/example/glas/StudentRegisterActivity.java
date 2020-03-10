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

public class StudentRegisterActivity extends AppCompatActivity {
    private TextView link_signin;
    private EditText etname, etadmission, etemail,etpassword;
    private Button btn_signup;
    private ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_register);

        btn_signup = findViewById(R.id.btn_signup);
        link_signin = findViewById(R.id.link_signin);
        loading = findViewById(R.id.loading);
        etname = findViewById(R.id.etname);
        etadmission = findViewById(R.id.etadmission);
        etemail = findViewById(R.id.etemail);
        etpassword = findViewById(R.id.etpassword);

        link_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StudentRegisterActivity.this, StudentLoginActivity.class));
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });
    }

    private void signup() {
        loading.setVisibility(View.VISIBLE);
        btn_signup.setVisibility(View.GONE);

        final String name = this.etname.getText().toString().trim();
        final String admission = this.etadmission.getText().toString().trim();
        final String email = this.etemail.getText().toString().trim();
        final String password = this.etpassword.getText().toString().trim();

        if (TextUtils.isEmpty(name)){
            etname.setError("Enter username");
            etname.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(admission)){
            etadmission.setError("Enter username");
            etadmission.requestFocus();
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

        RequestQueue requestQueue = Volley.newRequestQueue(StudentRegisterActivity.this);
        StringRequest stringRequest= new StringRequest(Request.Method.POST, URLs.URL_STUDENT_REGISTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    boolean error = obj.getBoolean("error");
                    //if no error response
                    if (error == false){
                        Toast.makeText(StudentRegisterActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent( StudentRegisterActivity.this, StudentActivity.class));
                    }
                    //if error response
                    if(error==true) {
                        Toast.makeText(StudentRegisterActivity.this, obj.getString("message"), Toast.LENGTH_SHORT).show();
                        loading.setVisibility(View.GONE);
                        btn_signup.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    loading.setVisibility(View.GONE);
                    btn_signup.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(StudentRegisterActivity.this,"Register Error" +error.toString(),Toast.LENGTH_SHORT).show();
                loading.setVisibility(View.GONE);
                btn_signup.setVisibility(View.VISIBLE);

            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("name", name);
                params.put("admission", admission);
                params.put("email", email);
                params.put("password", password);
                return params;
            }
            public Map<String, String> getHeaders() throws AuthFailureError{
                Map<String,String> params = new HashMap<>();
                params.put("content type","application/x-www-form-urlencoded");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
