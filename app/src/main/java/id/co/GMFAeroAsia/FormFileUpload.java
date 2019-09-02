package id.co.GMFAeroAsia;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.valueOf;

public class FormFileUpload extends AppCompatActivity {

    private static final String TAG = FormFileUpload.class.getSimpleName(); //getting the info
    private Button buttonGallerySppd, buttonSubmitFile, buttonGalleryTiket, buttonGalleryVoucher, buttonGalleryInvoice, buttonGalleryAML, buttonCameraSppd, buttonCameraTiket, buttonCameraVoucher, buttonCameraInvoice, buttonCameraAML;
    SessionManager sessionManager;
    String getId;
    private static String URL_READ = "http://192.168.100.15/register_login/read_detail.php";
    private static String URL_EDIT = "http://192.168.100.15/register_login/edit_detail.php";
    private static String URL_UPLOAD_FILE = "http://kodec.id/android_bantek_online/upload_file.php";
    private static String URL_UPLOAD_TIKET = "http://kodec.id/android_bantek_online/upload_tiket.php";
    private static String URL_UPLOAD_INVOICE = "http://kodec.id/android_bantek_online/upload_invoice.php";
    private static String URL_UPLOAD_VOUCHER = "http://kodec.id/android_bantek_online/upload_voucher.php";
    private static String URL_UPLOAD_AML = "http://kodec.id/android_bantek_online/upload_aml.php";
    private Menu action;
    private Bitmap bitmap, bitmapTiket, bitmapInvoice, bitmapVoucher, bitmapAML;
    ImageView sppd_image, tiket_image, invoice_image, voucher_image, aml_image;

    Uri image_uriSppd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_file_upload);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        buttonGallerySppd = findViewById(R.id.btnGallerySppd);
        buttonGalleryTiket = findViewById(R.id.btnGalleryTiket);
        buttonGalleryVoucher = findViewById(R.id.btnGalleryVoucher);
        buttonGalleryInvoice = findViewById(R.id.btnGalleryInvoice);
        buttonGalleryAML = findViewById(R.id.btnGalleryAML);
        buttonCameraSppd = findViewById(R.id.btnCameraSppd);
//        buttonCameraTiket = findViewById(R.id.btnCameraTiket);
//        buttonCameraVoucher = findViewById(R.id.btnCameraVoucher);
//        buttonCameraInvoice = findViewById(R.id.btnCameraInvoice);
//        buttonCameraAML = findViewById(R.id.btnCameraAML);
        buttonSubmitFile = findViewById(R.id.submitfile);
        sppd_image = findViewById(R.id.ivSppd);
        tiket_image = findViewById(R.id.ivTiket);
        invoice_image = findViewById(R.id.ivInvoice);
        voucher_image = findViewById(R.id.ivVoucher);
        aml_image = findViewById(R.id.ivAML);

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(sessionManager.ID);

        buttonGallerySppd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });

        buttonGalleryTiket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFileTiket();
            }
        });

        buttonGalleryInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFileInvoice();
            }
        });

        buttonGalleryVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFileVoucher();
            }
        });

        buttonGalleryAML.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFileAML();
            }
        });

        buttonCameraSppd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePictureSppd();
            }
        });


        buttonSubmitFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("bitmap", valueOf(bitmap));
                Log.d("bitmaptiket", valueOf(bitmapTiket));
                if (bitmap != null){
                    UploadPicture(getId, getStringImage(bitmap));
                }
                if (bitmapTiket != null){
                    UploadTiket(getId, getStringImageTiket(bitmapTiket));
                }
                if (bitmapVoucher != null){
                    UploadVoucher(getId, getStringImageVoucher(bitmapVoucher));
                }
                if (bitmapInvoice != null){
                    UploadInvoice(getId, getStringImageInvoice(bitmapInvoice));
                }
                if (bitmapAML != null){
                    UploadAML(getId, getStringImageAML(bitmapAML));
                }
                if (bitmap == null && bitmapTiket == null && bitmapInvoice == null && bitmapVoucher == null && bitmapAML == null){
                    Toast.makeText(FormFileUpload.this, "No image selected!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        Toolbar toolbar = findViewById(R.id.toolbar_dua);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void chooseFile(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                sppd_image.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (requestCode == 2 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePathTiket = data.getData();
            try {

                bitmapTiket = MediaStore.Images.Media.getBitmap(getContentResolver(), filePathTiket);
                tiket_image.setImageBitmap(bitmapTiket);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (requestCode == 3 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePathInvoice = data.getData();
            try {

                bitmapInvoice = MediaStore.Images.Media.getBitmap(getContentResolver(), filePathInvoice);
                invoice_image.setImageBitmap(bitmapInvoice);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (requestCode == 4 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePathVoucher = data.getData();
            try {

                bitmapVoucher = MediaStore.Images.Media.getBitmap(getContentResolver(), filePathVoucher);
                voucher_image.setImageBitmap(bitmapVoucher);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (requestCode == 5 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePathAML = data.getData();
            try {

                bitmapAML = MediaStore.Images.Media.getBitmap(getContentResolver(), filePathAML);
                aml_image.setImageBitmap(bitmapAML);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (requestCode == 6 && resultCode == RESULT_OK) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), image_uriSppd);
                sppd_image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void chooseFileTiket(){
        Intent intentTiket = new Intent();
        intentTiket.setType("image/*");
        intentTiket.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intentTiket, "Select Picture"), 2);
    }

    private void chooseFileInvoice(){
        Intent intentInvoice = new Intent();
        intentInvoice.setType("image/*");
        intentInvoice.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intentInvoice, "Select Picture"), 3);
    }

    private void chooseFileVoucher(){
        Intent intentVoucher = new Intent();
        intentVoucher.setType("image/*");
        intentVoucher.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intentVoucher, "Select Picture"), 4);
    }

    private void chooseFileAML(){
        Intent intentAML = new Intent();
        intentAML.setType("image/*");
        intentAML.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intentAML, "Select Picture"), 5);
    }

    private void takePictureSppd(){
        //if system os is >= marshmallow, request runtime permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkSelfPermission(Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_DENIED){
                //permission not enabled, request it
                String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                //show popup to request permissions
                requestPermissions(permission, 6);
            }
            else {
                //permission already granted
                openCamera();
            }
        }
        else {
            //system os < marshmallow
            openCamera();
        }
    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        image_uriSppd = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        //Camera intent
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uriSppd);
        startActivityForResult(cameraIntent, 6);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //this method is called, when user presses Allow or Deny from Permission Request Popup
        switch (requestCode){
            case 6:{
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED){
                    //permission from popup was granted
                    openCamera();
                }
                else {
                    //permission from popup was denied
                    Toast.makeText(this, "Permission denied...", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }



    private void UploadPicture(final String id, final String string_sppd) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");
        progressDialog.show();

//        Intent intent = getIntent();
//        final String id_bantek = intent.getStringExtra("ID_BANTEK");
//        Log.d("tags", String.valueOf(id_bantek));

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPLOAD_FILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.i(TAG, response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
                                Toast.makeText(FormFileUpload.this, "Upload sppd success!", Toast.LENGTH_SHORT).show();
                                bitmap.recycle();
                                bitmap=null;
                                sppd_image.setImageDrawable(getResources().getDrawable(R.drawable.ic_photo_grey_24dp));
                                Log.d("bitmapkosong", valueOf(bitmap));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(FormFileUpload.this, "Try Again!"+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(FormFileUpload.this, "Try Again!" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d("tagss", id);
                params.put("id", id);
//                params.put("id_bantek", id_bantek);
                params.put("sppd_image", string_sppd);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }


    private void UploadTiket(final String id, final String string_tiket) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPLOAD_TIKET,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.i(TAG, response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
                                Toast.makeText(FormFileUpload.this, "Upload tiket success!", Toast.LENGTH_SHORT).show();
                                bitmapTiket.recycle();
                                bitmapTiket=null;
                                tiket_image.setImageDrawable(getResources().getDrawable(R.drawable.ic_photo_grey_24dp));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(FormFileUpload.this, "Try Again!"+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(FormFileUpload.this, "Try Again!" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d("tag", id);
                params.put("id", id);
                params.put("tiket_image", string_tiket);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    private void UploadInvoice(final String id, final String string_invoice) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPLOAD_INVOICE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.i(TAG, response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
                                Toast.makeText(FormFileUpload.this, "Upload invoice success!", Toast.LENGTH_SHORT).show();
                                bitmapInvoice.recycle();
                                bitmapInvoice=null;
                                invoice_image.setImageDrawable(getResources().getDrawable(R.drawable.ic_photo_grey_24dp));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(FormFileUpload.this, "Try Again!"+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(FormFileUpload.this, "Try Again!" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d("tag", id);
                params.put("id", id);
                params.put("invoice_image", string_invoice);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }


    private void UploadVoucher(final String id, final String string_voucher) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPLOAD_VOUCHER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.i(TAG, response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
                                Toast.makeText(FormFileUpload.this, "Upload voucher success!", Toast.LENGTH_SHORT).show();
                                bitmapVoucher.recycle();
                                bitmapVoucher=null;
                                voucher_image.setImageDrawable(getResources().getDrawable(R.drawable.ic_photo_grey_24dp));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(FormFileUpload.this, "Try Again!"+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(FormFileUpload.this, "Try Again!" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d("tag", id);
                params.put("id", id);
                params.put("voucher_image", string_voucher);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    private void UploadAML(final String id, final String string_aml) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPLOAD_AML,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.i(TAG, response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
                                Toast.makeText(FormFileUpload.this, "Upload aml success!", Toast.LENGTH_SHORT).show();
                                bitmapAML.recycle();
                                bitmapAML=null;
                                aml_image.setImageDrawable(getResources().getDrawable(R.drawable.ic_photo_grey_24dp));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(FormFileUpload.this, "Try Again!"+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(FormFileUpload.this, "Try Again!" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d("tag", id);
                params.put("id", id);
                params.put("aml_image", string_aml);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    public String getStringImage(Bitmap bitmap){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageByteArray, Base64.DEFAULT);

        return encodedImage;
    }

    public String getStringImageTiket(Bitmap bitmapTiket){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmapTiket.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        String encodedImageTiket = Base64.encodeToString(imageByteArray, Base64.DEFAULT);

        return encodedImageTiket;
    }

    public String getStringImageInvoice(Bitmap bitmapInvoice){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmapInvoice.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        String encodedImageInvoice = Base64.encodeToString(imageByteArray, Base64.DEFAULT);

        return encodedImageInvoice;
    }

    public String getStringImageVoucher(Bitmap bitmapVoucher){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmapVoucher.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        String encodedImageVoucher = Base64.encodeToString(imageByteArray, Base64.DEFAULT);

        return encodedImageVoucher;
    }

    public String getStringImageAML(Bitmap bitmapAML){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmapAML.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        String encodedImageAML = Base64.encodeToString(imageByteArray, Base64.DEFAULT);

        return encodedImageAML;
    }
}
