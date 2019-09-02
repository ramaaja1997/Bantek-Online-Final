package id.co.GMFAeroAsia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUserId, editTextPassword;
    private Button buttonLogin;
    private ProgressBar progressBarLoading;
    private static String URL_LOGIN = "http://kodec.id/android_bantek_online/login.php";
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);

        progressBarLoading = findViewById(R.id.pbLoading);
        editTextUserId = findViewById(R.id.etUserId);
        editTextPassword = findViewById(R.id.etPassword);
        buttonLogin = findViewById(R.id.btnLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mId = editTextUserId.getText().toString().trim();
                String mPass = editTextPassword.getText().toString().trim();

                if (!mId.isEmpty() || !mPass.isEmpty()) {
                    Login(mId, mPass);
                } else {
                    editTextUserId.setError("Please insert email");
                    editTextPassword.setError("Please insert password");
                }
            }
        });

    }

    private void Login(final String id, final String password) {

        progressBarLoading.setVisibility(View.VISIBLE);
        buttonLogin.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("login");

                            if (success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String name = object.getString("name").trim();
                                    String email = object.getString("email").trim();
                                    String id = object.getString("id").trim();

                                    sessionManager.createSession(name, email, id);


                                    if(id.equals("654321")){
                                        Intent intent = new Intent(LoginActivity.this, TripAdminActivity.class);
                                        intent.putExtra("name", name);
                                        intent.putExtra("email", email);
                                        intent.putExtra("id", id);
                                        startActivity(intent);
                                        finish();
                                    }else{
                                        Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
//                                    Intent intent = new Intent(LoginActivity.this, TripActivity.classh);
                                        intent.putExtra("name", name);
                                        intent.putExtra("email", email);
                                        intent.putExtra("id", id);
                                        startActivity(intent);
                                        finish();
                                    }


                                    progressBarLoading.setVisibility(View.GONE);


                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressBarLoading.setVisibility(View.GONE);
                            buttonLogin.setVisibility(View.VISIBLE);
                            Toast.makeText(LoginActivity.this, "Error " +e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBarLoading.setVisibility(View.GONE);
                        buttonLogin.setVisibility(View.VISIBLE);
                        Toast.makeText(LoginActivity.this, "Error " +error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("password", password);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}