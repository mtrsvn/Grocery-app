package com.example.mm_grocery;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

    private EditText mm_itm_nameEdt, mm_itm_priceEdt, mm_itm_qtyEdt;
    private Button submitGrocery, nextButton;
    private String mm_itm_name, mm_itm_price, mm_itm_qty;
    private String mm_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle bdl = getIntent().getExtras();
        mm_itm_nameEdt = findViewById(R.id.idEdtmm_itm_name);
        mm_itm_priceEdt = findViewById(R.id.idEdtmm_itm_price);
        mm_itm_qtyEdt = findViewById(R.id.idEdtmm_itm_qty);
        submitGrocery = findViewById(R.id.btnSubmitGro);
        nextButton = findViewById(R.id.btnNext);

        nextButton.setOnClickListener(view -> {
            Intent myIntent = new Intent(MainActivity.this, MainActivity2.class);
            startActivity(myIntent);
        });

        if (bdl != null) {
            mm_itm_nameEdt.setText(bdl.getString("itmName"));
            mm_itm_priceEdt.setText(bdl.getString("itmPrice"));
            mm_itm_qtyEdt.setText(bdl.getString("itmQty"));
            mm_id = bdl.getString("id");
        }

        submitGrocery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mm_itm_name = mm_itm_nameEdt.getText().toString();
                mm_itm_price = mm_itm_priceEdt.getText().toString();
                mm_itm_qty = mm_itm_qtyEdt.getText().toString();

                if (TextUtils.isEmpty(mm_itm_name)) {
                    mm_itm_nameEdt.setError("Please Enter Grocery Name");
                } else if (TextUtils.isEmpty(mm_itm_price)) {
                    mm_itm_priceEdt.setError("Please Enter Price");
                } else if (TextUtils.isEmpty(mm_itm_qty)) {
                    mm_itm_qtyEdt.setError("Please Enter Quantity");
                } else {
                    addDataToDatabase(mm_itm_name, mm_itm_price, mm_itm_qty);
                }
            }
        });
    }

    private void addDataToDatabase(String mm_itm_name, String mm_itm_price, String mm_itm_qty) {
        String url = "http://172.20.10.6/mm_grocery/mm_insert.php";
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                Toast.makeText(MainActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mm_itm_nameEdt.setText("");
            mm_itm_priceEdt.setText("");
            mm_itm_qtyEdt.setText("");
            mm_id = "";
        }, error -> {
            Toast.makeText(MainActivity.this, "Failed to insert: " + error, Toast.LENGTH_SHORT).show();
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("mm_id", mm_id);
                params.put("mm_itm_name", mm_itm_name);
                params.put("mm_itm_price", mm_itm_price);
                params.put("mm_itm_qty", mm_itm_qty);
                return params;
            }
        };

        queue.add(request);
    }
}
