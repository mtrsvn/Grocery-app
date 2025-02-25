package com.example.mm_grocery;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText mm_itm_nameEdt, mm_itm_qtyEdt, mm_itm_priceEdt;
    private Button submitCourseBtn, searchBtn, mm_idBtnEdit;
    private String mm_itm_name, mm_itm_qty, mm_itm_price, mm_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mm_id="";
        Bundle bdl = getIntent().getExtras();

        // Initializing EditText fields
        mm_itm_nameEdt = findViewById(R.id.idEdtCourseName);
        mm_itm_priceEdt = findViewById(R.id.idEdtCourseDescription);
        mm_itm_qtyEdt = findViewById(R.id.idEdtCourseDuration);

        // Initializing Buttons
        submitCourseBtn = findViewById(R.id.idBtnSubmitCourse);
        searchBtn = findViewById(R.id.idBtnSearch);

        // Retrieve data from Intent bundle
        if (bdl != null) {
            mm_itm_nameEdt.setText(bdl.getString("mm_itm_name"));
            mm_itm_priceEdt.setText(bdl.getString("mm_itm_price"));
            mm_itm_qtyEdt.setText(bdl.getString("mm_itm_qty"));
            mm_id = bdl.getString("mm_id");
            mm_idBtnEdit.setText(mm_id);
        }

        submitCourseBtn.setOnClickListener(v -> {
            mm_itm_name = mm_itm_nameEdt.getText().toString().trim();
            mm_itm_price = mm_itm_priceEdt.getText().toString().trim();
            mm_itm_qty = mm_itm_qtyEdt.getText().toString().trim();

            if (TextUtils.isEmpty(mm_itm_name)) {
                mm_itm_nameEdt.setError("Please enter Item Name");
            } else if (TextUtils.isEmpty(mm_itm_price)) {
                mm_itm_priceEdt.setError("Please enter Item Price");
            } else if (TextUtils.isEmpty(mm_itm_qty)) {
                mm_itm_qtyEdt.setError("Please enter Item Quantity");
            } else {
                addDataToDatabase(mm_itm_name, mm_itm_price, mm_itm_qty, mm_id);
            }
        });

        searchBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MainActivity2.class);
            startActivity(intent);
        });
    }

    private void addDataToDatabase(String name, String price, String qty, String id) {
        String url = "http://192.168.34.222/mm_grocery/mm_itmslist.php";
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.e("TAG", "RESPONSE IS " + response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        Toast.makeText(MainActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mm_itm_nameEdt.setText("");
                    mm_itm_priceEdt.setText("");
                    mm_itm_qtyEdt.setText("");
                },
                error -> {
                    String message = "Fail to get response = " + error.toString();
                    Log.e("VolleyError", message);
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("mm_itm_name", name);
                params.put("mm_itm_price", price);
                params.put("mm_itm_qty", qty);
                params.put("mm_id", id);
                return params;
            }
        };
        queue.add(request);
    }
}