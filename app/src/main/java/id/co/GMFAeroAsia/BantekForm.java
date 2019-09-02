package id.co.GMFAeroAsia;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;



public class BantekForm extends AppCompatActivity implements View.OnClickListener {

    private Button submitform;
    private Button type_a;
    private Button type_b;
    private String type;
    EditText date1, date2;
    DatePickerDialog datePickerDialog;

    private TextView stDeparture, stArrival, stArrivalCity, stDepartureCity;
    private ProgressBar loading;
    private static String URL_FORM = "http://kodec.id/android_bantek_online/submit_form.php";
    private static final int SECOND_ACTIVITY_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bantek_form);

        submitform = findViewById(R.id.submitform);
        date1 = (EditText) findViewById(R.id.date1);
        date2 = (EditText) findViewById(R.id.date2);
        date1.setInputType(InputType.TYPE_NULL);
        date2.setInputType(InputType.TYPE_NULL);

        type_a = findViewByIdAndCast(R.id.buttontypea);
        type_b = findViewByIdAndCast(R.id.buttontypeb);

        type_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Button button = (Button) v;

                // clear state
                type_a.setSelected(true);
                type_a.setPressed(false);
                type_b.setSelected(false);
                type_b.setPressed(false);

                type = type_a.getText().toString().trim();

//                // change state
//                button.setSelected(true);
//                button.setPressed(false);

            }
        });
        type_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Button button = (Button) v;

                // clear state
                type_a.setSelected(false);
                type_a.setPressed(false);
                type_b.setSelected(true);
                type_b.setPressed(false);

                type = type_b.getText().toString().trim();

//                // change state
//                button.setSelected(true);
//                button.setPressed(false);

            }
        });

        loading = findViewById(R.id.pbLoading);
        stDeparture = findViewById(R.id.tvStDepartureCode);
        stDepartureCity = findViewById(R.id.tvStDepartureCity);
        stArrival = findViewById(R.id.tvStArrivalCode);
        stArrivalCity = findViewById(R.id.tvStArrivalCity);

        submitform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubmitForm();
            }
        });

//        submitform.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(BantekForm.this, FormFileUpload.class));
//            }
//        });

        stDeparture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(BantekForm.this, AirportListActivity.class));
                Intent intent = new Intent(BantekForm.this, AirportListActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        stArrival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BantekForm.this, AirportListActivity.class);
                startActivityForResult(intent, 2);
            }
        });

        date1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(BantekForm.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                date1.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        // perform click event on edit text
        date2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(BantekForm.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                date2.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        Toolbar ToolBarBantekForm = (Toolbar)findViewById(R.id.toolbar_satu);
        setSupportActionBar(ToolBarBantekForm);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void SubmitForm() {

        Intent intent = getIntent();
        final String id = intent.getStringExtra("id");
        final String name = intent.getStringExtra("name");
        final String division = intent.getStringExtra("division");
        final String departure_station = this.stDeparture.getText().toString().trim();
        final String arrival_station = this.stArrival.getText().toString().trim();
        final String departure_city = this.stDepartureCity.getText().toString().trim();
        final String arrival_city = this.stArrivalCity.getText().toString().trim();
        final String departure_date = this.date1.getText().toString().trim();
        final String return_date = this.date2.getText().toString().trim();
        final String type_bantek = this.type;

        if (TextUtils.isEmpty(departure_date)) {
            Toast.makeText(getApplicationContext(), "Please insert departure date", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(return_date)) {
            Toast.makeText(getApplicationContext(), "Please insert return date", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(type_bantek)) {
            Toast.makeText(getApplicationContext(), "Please insert type of bantek", Toast.LENGTH_SHORT).show();
            return;
        } else {
            loading.setVisibility(View.VISIBLE);
            submitform.setVisibility(View.GONE);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_FORM,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");

                                if (success.equals("1")) {
                                    Toast.makeText(BantekForm.this, "Submit Success!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(BantekForm.this, MenuActivity.class));
                                    loading.setVisibility(View.GONE);
                                    submitform.setVisibility(View.VISIBLE);
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(BantekForm.this, "Submit Error! " + e.toString(), Toast.LENGTH_SHORT).show();
                                loading.setVisibility(View.GONE);
                                submitform.setVisibility(View.VISIBLE);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(BantekForm.this, "Submit Error! " + error.toString(), Toast.LENGTH_SHORT).show();
                            loading.setVisibility(View.GONE);
                            submitform.setVisibility(View.VISIBLE);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("id", id);
                    params.put("name", name);
                    params.put("division", division);
                    params.put("departure_station", departure_station);
                    params.put("arrival_station", arrival_station);
                    params.put("departure_city", departure_city);
                    params.put("arrival_city", arrival_city);
                    params.put("departure_date", departure_date);
                    params.put("return_date", return_date);
                    params.put("type_bantek", type_bantek);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);


        }
    }

    // This method is called when the second activity finishes
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check that it is the SecondActivity with an OK result
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                // Get String data from Intent
                String departureCode = data.getStringExtra("DEPARTURE_CODE");
                String departureCity = data.getStringExtra("DEPARTURE_CITY");

                // Set text view with string
                stDeparture.setText(departureCode);
                stDepartureCity.setText(departureCity);
            }
        }else if (requestCode == 2) {
            if (resultCode == RESULT_OK) {

                // Get String data from Intent
                String arrivalCode = data.getStringExtra("ARRIVAL_CODE");
                String arrivalCity = data.getStringExtra("ARRIVAL_CITY");

                // Set text view with string
                stArrival.setText(arrivalCode);
                stArrivalCity.setText(arrivalCity);
            }
        }
    }

    //button
    @Override
    public void onClick(View v) {
        Button button = (Button) v;

        // clear state
        type_a.setSelected(false);
        type_a.setPressed(false);
        type_b.setSelected(false);
        type_b.setPressed(false);

        // change state
        button.setSelected(true);
        button.setPressed(false);

    }

    @SuppressWarnings("unchecked")
    private <T> T findViewByIdAndCast(int id) {
        return (T) findViewById(id);
    }

}
