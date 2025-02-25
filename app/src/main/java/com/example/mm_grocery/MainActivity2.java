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
import com.example.mm_grocery.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity2 extends AppCompatActivity {

    private EditText courseIDEdt;
    private Button getCourseDetailsBtn, backButton, deleteButton, BtnEdit;
    private CardView courseCV;
    private TextView courseNameTV, courseDescTV, courseDurationTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        courseNameTV = findViewById(R.id.idTVCourseName);
        courseDescTV = findViewById(R.id.idTVCourseDescription);
        courseDurationTV = findViewById(R.id.idTVCourseDuration);
        getCourseDetailsBtn = findViewById(R.id.idBtnGetCourse);
        backButton = findViewById(R.id.idBtnBack);
        courseIDEdt = findViewById(R.id.idEdtCourseId);
        courseCV = findViewById(R.id.idCVCOurseItem);
        deleteButton = findViewById(R.id.idBtnDelete);
        BtnEdit = findViewById(R.id.idBtnEdit);

        getCourseDetailsBtn.setOnClickListener(v -> {
            if (TextUtils.isEmpty(courseIDEdt.getText().toString())) {
                Toast.makeText(MainActivity2.this, "Please enter product id", Toast.LENGTH_SHORT).show();
                return;
            }
            getCourseDetails(courseIDEdt.getText().toString());
        });

        deleteButton.setOnClickListener(v -> {
            if (TextUtils.isEmpty(courseIDEdt.getText().toString())) {
                Toast.makeText(MainActivity2.this, "Please enter product id to delete", Toast.LENGTH_SHORT).show();
                return;
            }
            deleteGroceryDetails(courseIDEdt.getText().toString());
        });

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity2.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        BtnEdit.setOnClickListener(v -> {
            Intent myIntent = new Intent(MainActivity2.this, MainActivity.class);
            myIntent.putExtra("id", courseIDEdt.getText().toString());
            myIntent.putExtra("name", courseNameTV.getText().toString());
            myIntent.putExtra("description", courseDescTV.getText().toString());
            myIntent.putExtra("quantity", courseDurationTV.getText().toString());
            startActivity(myIntent);
        });
    }

    private void getCourseDetails(String mm_id) {
        String url = "http://192.168.34.222/mm_grocery/mm_itmsread.php";
        RequestQueue queue = Volley.newRequestQueue(MainActivity2.this);

        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("mm_itm_name") == null) {
                    Toast.makeText(MainActivity2.this, "Please enter valid id.", Toast.LENGTH_SHORT).show();
                } else {
                    courseNameTV.setText(jsonObject.getString("mm_itm_name"));
                    courseDescTV.setText(jsonObject.getString("mm_itm_price"));
                    courseDurationTV.setText(jsonObject.getString("mm_itm_qty"));
                    courseCV.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(MainActivity2.this, "Fail to get course" + error, Toast.LENGTH_SHORT).show()) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("mm_id", mm_id);
                return params;
            }
        };
        queue.add(request);
    }

    private void deleteGroceryDetails(String mm_id) {
        String url = "http://192.168.34.222/mm_grocery/mm_itmsdel.php";
        RequestQueue queue = Volley.newRequestQueue(MainActivity2.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (!jsonObject.getBoolean("error")) {
                    Toast.makeText(MainActivity2.this, "Deleted", Toast.LENGTH_SHORT).show();
                    courseCV.setVisibility(View.GONE);
                    courseNameTV.setText("");
                    courseDescTV.setText("");
                    courseDurationTV.setText("");
                } else {
                    Toast.makeText(MainActivity2.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(MainActivity2.this, "Fail to delete" + error, Toast.LENGTH_SHORT).show()) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("mm_id", mm_id);
                return params;
            }
        };
        queue.add(request);
    }
}
