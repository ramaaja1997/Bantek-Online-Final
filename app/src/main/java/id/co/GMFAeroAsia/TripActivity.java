package id.co.GMFAeroAsia;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TripActivity extends AppCompatActivity {

    private static String URL_TRIP = "http://kodec.id/android_bantek_online/form_api.php";

    //a list to store all the products
    List<Trip> productList;

    //the recyclerview
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);

        //getting the recyclerview from xml
        recyclerView = findViewById(R.id.recylcerViewTrips);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        //initializing the productlist
        productList = new ArrayList<>();

        //this method will fetch and parse json
        //to display it in recyclerview
        loadTrips();
    }


    private void loadTrips() {

        /*
         * Creating a String Request
         * The request type is GET defined by first parameter
         * The URL is defined in the second parameter
         * Then we have a Response Listener and a Error Listener
         * In response listener we will get the JSON response as a String
         * */
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_TRIP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);

                                Log.d("fby", product.toString(8));

                                //adding the product to product list
                                productList.add(new Trip(
                                        product.getInt("id"),
                                        product.getInt("id_bantek"),
                                        product.getString("departure_code"),
                                        product.getString("arrival_code"),
                                        product.getString("departure_city"),
                                        product.getString("arrival_city"),
                                        product.getString("departure_date"),
                                        product.getString("return_date"),
                                        product.getString("type_bantek"),
                                        product.getString("sppd_image"),
                                        product.getString("tiket_image"),
                                        product.getString("invoice_image"),
                                        product.getString("voucher_image"),
                                        product.getString("aml_image"),
                                        product.getString("status")

                                ));
                            }

                            //creating adapter object and setting it to recyclerview
                            TripAdapter adapter = new TripAdapter(TripActivity.this, productList);
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest);
    }
//
//    public void onTripSelected(Trip productList) {
//        Toast.makeText(getApplicationContext(), "Selected: " + airportList.getName() + ", " + airportList.getCode(), Toast.LENGTH_LONG).show();
//
//        Integer id = productList.getId();
//        Integer id_bantek = productList.getId_bantek();
//        String departureDate = productList.getDeparture_date();
//        String returnDate = productList.getReturn_date();
//        String departureStation = productList.getDeparture_station();
//        String departureCity = productList.getDeparture_city();
//        String arrivalCity = productList.getArrival_city();
//        String arrivalStation = productList.getArrival_station();
//        String sppdImage = productList.getSppd_image();
//        String tiketImage = productList.getTiket_image();
//        String invoiceImage = productList.getInvoice_image();
//        String voucherImage = productList.getVoucher_image();
//        String amlImage = productList.getAml_image();
//
//        // Put the String to pass
//        Intent intent = new Intent(TripActivity.this, FormFileUpload.class);
//        intent.putExtra("DEPARTURE_STATION", departureStation);
//        intent.putExtra("DEPARTURE_STATION", departureStation);
//        intent.putExtra("DEPARTURE_STATION", departureStation);
//        intent.putExtra("DEPARTURE_CITY", departureCity);
//        intent.putExtra("ARRIVAL_CITY", arrivalCity);
//        intent.putExtra("ARRIVAL_STATION", arrivalStation);
//        setResult(RESULT_OK, intent);
//        finish();
//    }
}
