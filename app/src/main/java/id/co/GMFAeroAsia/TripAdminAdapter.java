package id.co.GMFAeroAsia;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.util.List;
import java.util.Map;

import static java.lang.String.valueOf;


public class TripAdminAdapter extends RecyclerView.Adapter<TripAdminAdapter.ProductViewHolder> {


    private Context mCtx;
    private List<TripAdmin> productList;

    private static String URL_STATUS = "http://kodec.id/android_bantek_online/submit_status.php";

    public TripAdminAdapter(Context mCtx, List<TripAdmin> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.trip_admin_list, null);
        return new ProductViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, int position) {
        final TripAdmin product = productList.get(position);
        Log.d("asd", String.valueOf(product.getSppd_image()));
        Log.d("as1", String.valueOf(product.getId_admin()));

        holder.textViewName.setText(product.getName_admin());
        holder.textViewIdEmp.setText(String.valueOf(product.getId_admin()));
        holder.textViewDivision.setText(product.getDivision_admin());

        holder.textViewTripDepartureCode.setText(product.getDeparture_station());
        holder.textViewTripArrivalCode.setText(product.getArrival_station());
        holder.textViewTripDepartureCity.setText(product.getDeparture_city());
        holder.textViewTripArrivalCity.setText(product.getArrival_city());
        holder.textViewTripDepartureDate.setText(product.getDeparture_date());
        holder.textViewTripReturnDate.setText(product.getReturn_date());

        holder.checkBoxSppd.setChecked(!product.getSppd_image().equals("0"));
        holder.checkBoxTiket.setChecked(!product.getTiket_image().equals("0"));
        holder.checkBoxInvoice.setChecked(!product.getInvoice_image().equals("0"));
        holder.checkBoxVoucher.setChecked(!product.getVoucher_image().equals("0"));
        holder.checkBoxAML.setChecked(!product.getAml_image().equals("0"));

        if (product.getStatus().equals("Y")){
            holder.cvMain.setCardBackgroundColor(Color.parseColor("#edfead"));
            holder.buttonReject.setVisibility(View.GONE);
            holder.buttonApprove.setVisibility(View.GONE);
            holder.radioGroupRemaks.setVisibility(View.GONE);
            holder.linearLayoutCheckBox.setVisibility(View.VISIBLE);
        }

        if (product.getStatus().equals("N")){
            holder.cvMain.setVisibility(View.GONE);
        }

        holder.buttonReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String id_bantek = String.valueOf(product.getId_bantek());
                final String status = "N";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_STATUS,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String success = jsonObject.getString("success");

                                    if (success.equals("1")) {
                                        Toast.makeText(mCtx, "Reject Success!", Toast.LENGTH_SHORT).show();
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(mCtx, "Reject Error! " + e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(mCtx, "Reject Error! " + error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("id_bantek", id_bantek);
                        params.put("status", status);
                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(mCtx);
                requestQueue.add(stringRequest);
            }
        });


        holder.buttonApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String id_bantek = String.valueOf(product.getId_bantek());
                final String status = "Y";

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_STATUS,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        String success = jsonObject.getString("success");

                                        if (success.equals("1")) {
                                            Toast.makeText(mCtx, "Approve Success!", Toast.LENGTH_SHORT).show();

                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(mCtx, "Approve Error! : " + e.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(mCtx, "Approve Error! : " + error.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("id_bantek", id_bantek);
                            params.put("status", status);
                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(mCtx);
                    requestQueue.add(stringRequest);
            }
        });


    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName, textViewDivision, textViewIdEmp, textViewTripDepartureCode, textViewTripArrivalCode, textViewTripDepartureCity, textViewTripArrivalCity, textViewTripDepartureDate, textViewTripReturnDate;
        CheckBox checkBoxSppd, checkBoxTiket, checkBoxInvoice, checkBoxVoucher, checkBoxAML;
        CardView cvMain;
        View viewUpload;
        Button buttonReject, buttonApprove;
        RadioButton radioButtonPBTH, radioButtonTMB;
        RadioGroup radioGroupRemaks;
        LinearLayout linearLayoutCheckBox;


        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewDivision = itemView.findViewById(R.id.tvDivisionReportAdmin);
            textViewIdEmp = itemView.findViewById(R.id.tvIdEmpReportAdmin);
            textViewName = itemView.findViewById(R.id.tvNameReportAdmin);

            buttonApprove = itemView.findViewById(R.id.btnApproveAdmin);
            buttonReject = itemView.findViewById(R.id.btnRejectAdmin);


            textViewTripDepartureCode = itemView.findViewById(R.id.tvTripReportDepartureCodeAdmin);
            textViewTripArrivalCode = itemView.findViewById(R.id.tvTripReportArrivalCodeAdmin);
            textViewTripDepartureCity = itemView.findViewById(R.id.tvTripDepartureCityAdmin);
            textViewTripArrivalCity = itemView.findViewById(R.id.tvTripArrivalCityAdmin);
            textViewTripDepartureDate = itemView.findViewById(R.id.tvTripReportDepartureDateAdmin);
            textViewTripReturnDate = itemView.findViewById(R.id.tvTripReportReturnDateAdmin);
            checkBoxSppd = itemView.findViewById(R.id.cbTripReportSppdAdmin);
            checkBoxTiket = itemView.findViewById(R.id.cbTripReportTiketAdmin);
            checkBoxInvoice = itemView.findViewById(R.id.cbTripReportInvoiceAdmin);
            checkBoxVoucher = itemView.findViewById(R.id.cbTripReportVoucherAdmin);
            checkBoxAML = itemView.findViewById(R.id.cbTripReportAMLAdmin);
            cvMain = itemView.findViewById(R.id.cvMainAdmin);
            viewUpload = itemView.findViewById(R.id.viewUploadAdmin);

            radioButtonPBTH = itemView.findViewById(R.id.rbPBTHAdmin);
            radioButtonTMB = itemView.findViewById(R.id.rbTMBAdmin);
            radioGroupRemaks = itemView.findViewById(R.id.rgRemaksAdmin);
            linearLayoutCheckBox = itemView.findViewById(R.id.llCheckBoxAdmin);
        }
    }


}
