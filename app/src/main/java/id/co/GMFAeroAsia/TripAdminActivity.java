package id.co.GMFAeroAsia;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class TripAdminActivity extends AppCompatActivity {

    private static final String TAG = TripAdminActivity.class.getSimpleName(); //getting the info
    private TextView textViewName, textViewEmail, textViewEmployeeDiv, textViewEmployeeId, textViewLogout;
    private Button buttonLogout, buttonUploadFile, buttonCreateBantek;
    private SwipeRefreshLayout swipeRefreshLayoutMenu;
    SessionManager sessionManager;
    String getId;
    private static String URL_READ = "http://kodec.id/android_bantek_online/read_detail.php";
    private static String URL_EDIT = "http://192.168.100.15/android_register_login/edit_detail.php";
    private static String URL_UPLOAD = "http://192.168.100.15/android_register_login/upload.php";
    private static String URL_TRIP = "http://kodec.id/android_bantek_online/tripList_api.php";
    private Menu action;
    private Bitmap bitmap;
    CircleImageView circleImageViewProfileAdmin;

    //a list to store all the products
    List<TripAdmin> productList;

    //the recyclerview
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_admin);


        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(sessionManager.ID);
//
//        Log.d("testid", String.valueOf(getId));
//
        if(!getId.equals("654321")){
            Intent intent = new Intent(TripAdminActivity.this, MenuActivity.class);
            startActivity(intent);
            finish();
        }

        textViewName = findViewById(R.id.tvName);
        textViewEmail = findViewById(R.id.tvEmail);
        textViewLogout = findViewById(R.id.tvLogout);
        textViewEmployeeDiv = findViewById(R.id.tvEmployeeDiv);
        textViewEmployeeId = findViewById(R.id.tvEmployeeId);
        buttonCreateBantek = findViewById(R.id.btnCreateBantek);
        circleImageViewProfileAdmin = findViewById(R.id.cIVProfileAdmin);

        swipeRefreshLayoutMenu = findViewById(R.id.menuSwipeRefresh);

        //getting the recyclerview from xml
        recyclerView = findViewById(R.id.recylcerViewTripAdmin);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        //initializing the productlist
        productList = new ArrayList<>();

        //this method will fetch and parse json
        //to display it in recyclerview
        loadTrips();


        textViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.logoutAdmin();
            }
        });


        swipeRefreshLayoutMenu.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                productList.clear();
                loadTrips(); // your code
                swipeRefreshLayoutMenu.setRefreshing(false);
            }
        });

    }

    //getUserDetail
    private void getUserDetail(){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.i(TAG, response.toString());

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("read");

                            Log.d("tag", jsonObject.toString(8));

                            if (success.equals("1")){

                                for (int i =0; i < jsonArray.length(); i++){

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String strName = object.getString("name").trim();
                                    String strEmail = object.getString("email").trim();
                                    String strId = object.getString("id").trim();
                                    String strDiv = object.getString("division").trim();
                                    String strPhoto = object.getString("photo").trim();

                                    textViewName.setText(strName);
                                    textViewEmail.setText(strEmail);
                                    textViewEmployeeId.setText(strId);
                                    textViewEmployeeDiv.setText(strDiv);
                                    Log.d("cekurl", strPhoto);
                                    String url = strPhoto;

                                    Picasso.with(TripAdminActivity.this).load(url).into(circleImageViewProfileAdmin);

                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(TripAdminActivity.this, "Error Reading Detail "+e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(TripAdminActivity.this, "Error Reading Detail "+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String > params = new HashMap<>();
                params.put("id", getId);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserDetail();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.menu_action, menu);
//
//        action = menu;
//        action.findItem(R.id.menu_save).setVisible(false);
//
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()){
//            case R.id.menu_edit:
//
//                name.setFocusableInTouchMode(true);
//                email.setFocusableInTouchMode(true);
//
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.showSoftInput(name, InputMethodManager.SHOW_IMPLICIT);
//
//                action.findItem(R.id.menu_edit).setVisible(false);
//                action.findItem(R.id.menu_save).setVisible(true);
//
//                return true;
//
//            case R.id.menu_save:
//
//                SaveEditDetail();
//
//                action.findItem(R.id.menu_edit).setVisible(true);
//                action.findItem(R.id.menu_save).setVisible(false);
//
//                name.setFocusableInTouchMode(false);
//                email.setFocusableInTouchMode(false);
//                name.setFocusable(false);
//                email.setFocusable(false);
//
//                return true;
//
//            default:
//
//                return super.onOptionsItemSelected(item);
//
//        }
//    }

    //save
//    private void SaveEditDetail() {
//
//        final String name = this.name.getText().toString().trim();
//        final String email = this.email.getText().toString().trim();
//        final String id = getId;
//
//        final ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Saving...");
//        progressDialog.show();
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        progressDialog.dismiss();
//
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            String success = jsonObject.getString("success");
//
//                            if (success.equals("1")){
//                                Toast.makeText(HomeActivity.this, "Success!", Toast.LENGTH_SHORT).show();
//                                sessionManager.createSession(name, email, id);
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            progressDialog.dismiss();
//                            Toast.makeText(HomeActivity.this, "Error "+ e.toString(), Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        progressDialog.dismiss();
//                        Toast.makeText(HomeActivity.this, "Error "+ error.toString(), Toast.LENGTH_SHORT).show();
//                    }
//                })
//        {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("name", name);
//                params.put("email", email);
//                params.put("id", id);
//                return params;
//            }
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(stringRequest);
//
//    }

    private void chooseFile(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            Uri filePath = data.getData();
//            try {
//
//                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//                profile_image.setImageBitmap(bitmap);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            UploadPicture(getId, getStringImage(bitmap));
//
//        }
//    }

//    private void UploadPicture(final String id, final String photo) {
//
//        final ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Uploading...");
//        progressDialog.show();
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPLOAD,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        progressDialog.dismiss();
//                        Log.i(TAG, response.toString());
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            String success = jsonObject.getString("success");
//
//                            if (success.equals("1")){
//                                Toast.makeText(HomeActivity.this, "Success!", Toast.LENGTH_SHORT).show();
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            progressDialog.dismiss();
//                            Toast.makeText(HomeActivity.this, "Try Again!"+e.toString(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        progressDialog.dismiss();
//                        Toast.makeText(HomeActivity.this, "Try Again!" + error.toString(), Toast.LENGTH_SHORT).show();
//                    }
//                })
//        {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("id", id);
//                params.put("photo", photo);
//
//                return params;
//            }
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(stringRequest);
//
//
//    }

    public String getStringImage(Bitmap bitmap){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageByteArray, Base64.DEFAULT);

        return encodedImage;
    }

    private void loadTrips() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_TRIP,
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

                                Log.d("fby", product.toString(15));

                                //adding the product to product list
                                productList.add(new TripAdmin(
                                        product.getInt("id"),
                                        product.getInt("id_bantek"),
                                        product.getString("name"),
                                        product.getString("division"),
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
                                if (product.getString("sppd_image")==null){

                                }
                            }

                            //creating adapter object and setting it to recyclerview
                            TripAdminAdapter adapter = new TripAdminAdapter(TripAdminActivity.this, productList);
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
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String > params = new HashMap<>();
                params.put("id", getId);
                return params;
            }
        };
        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest);
    }
}
