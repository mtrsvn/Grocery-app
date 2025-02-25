package com.example.mm_grocery;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity2 extends AppCompatActivity {

    private EditText groceryIDEdt;
    private Button getGroceryID_mm, previousPageBtn_mm, deleteGroceryBtn_mm, editButton;
    private CardView groceryCV;
    private TextView mm_itm_nameTV, mm_itm_priceTV, mm_itm_qtyTV;

    private String itemName, itemPrice, itemQty, itemID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Bundle bdl = getIntent().getExtras();
        mm_itm_nameTV = findViewById(R.id.idTVmm_itm_name);
        mm_itm_priceTV = findViewById(R.id.idTVmm_itm_price);
        mm_itm_qtyTV = findViewById(R.id.idTVmm_itm_qty);
        getGroceryID_mm = findViewById(R.id.btnSubmitID_mm);
        previousPageBtn_mm = findViewById(R.id.btnPrev_mm);
        deleteGroceryBtn_mm = findViewById(R.id.idBtnDeleteGroceryDetails);
        editButton = findViewById(R.id.idBtnEditGroceryDetails);
        groceryIDEdt = findViewById(R.id.enterID_mm);
        groceryCV = findViewById(R.id.idCVGroceryItem_mm);

        editButton.setOnClickListener(v -> {
            Intent myIntent = new Intent(MainActivity2.this, MainActivity.class);
            myIntent.putExtra("id", groceryIDEdt.getText().toString());
            myIntent.putExtra("itmName", mm_itm_nameTV.getText().toString());
            myIntent.putExtra("itmPrice", mm_itm_priceTV.getText().toString());
            myIntent.putExtra("itmQty", mm_itm_qtyTV.getText().toString());
            MainActivity2.this.startActivity(myIntent);
            finish();
        });

        getGroceryID_mm.setOnClickListener(v -> {
            if (TextUtils.isEmpty(groceryIDEdt.getText().toString())) {
                Toast.makeText(MainActivity2.this, "Please enter grocery ID", Toast.LENGTH_SHORT).show();
                return;
            }
            getGroceryDetails(groceryIDEdt.getText().toString());
        });

        previousPageBtn_mm.setOnClickListener(v -> {
            Intent myIntent = new Intent(MainActivity2.this, MainActivity.class);
            startActivity(myIntent);
        });

        deleteGroceryBtn_mm.setOnClickListener(v -> {
            if (TextUtils.isEmpty(groceryIDEdt.getText().toString())) {
                Toast.makeText(MainActivity2.this, "Please enter grocery ID", Toast.LENGTH_SHORT).show();
                return;
            }
            deleteGroceryDetails(groceryIDEdt.getText().toString());
        });
    }

    private void getGroceryDetails(String groceryId) {
        String url = "http://172.20.10.6/mm_grocery/mm_read.php";
        RequestQueue queue = Volley.newRequestQueue(MainActivity2.this);

        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (!jsonObject.has("mm_itm_name")) {
                    Toast.makeText(MainActivity2.this, "Please enter a valid ID.", Toast.LENGTH_SHORT).show();
                } else {
                    itemName = jsonObject.getString("mm_itm_name");
                    itemPrice = jsonObject.getString("mm_itm_price");
                    itemQty = jsonObject.getString("mm_itm_qty");
                    itemID = groceryId;
                    mm_itm_nameTV.setText(itemName);
                    mm_itm_priceTV.setText(itemPrice);
                    mm_itm_qtyTV.setText(itemQty);
                    groceryCV.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity2.this, "Error parsing JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity2.this, "Fail to get grocery" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("mm_id", groceryId);
                return params;
            }
        };

        queue.add(request);
    }

    private void deleteGroceryDetails(String groceryId) {
        String url = "http://172.20.10.6/mm_grocery/mm_delete.php";
        RequestQueue queue = Volley.newRequestQueue(MainActivity2.this);

        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                Toast.makeText(MainActivity2.this, "Details Deleted Successfully!", Toast.LENGTH_SHORT).show();
                groceryCV.setVisibility(View.GONE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(MainActivity2.this, "Failed to delete details: " + error, Toast.LENGTH_SHORT).show()) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("mm_id", groceryId);
                return params;
            }
        };

        queue.add(request);
    }
}
