package id.co.GMFAeroAsia;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import static java.lang.String.valueOf;


public class TripAdapter extends RecyclerView.Adapter<TripAdapter.ProductViewHolder> {


    private Context mCtx;
    private List<Trip> productList;

    public TripAdapter(Context mCtx, List<Trip> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.trip_list, null);
        return new ProductViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        final Trip product = productList.get(position);
        Log.d("asd", String.valueOf(product.getSppd_image()));
        Log.d("asd1", String.valueOf(product.getTiket_image()));

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

        if(product.getStatus_user().equals("Y")){
            holder.textViewStatusApprove.setVisibility(View.GONE);
            holder.linearLayoutCheckBoxUser.setVisibility(View.VISIBLE);
        } else if(product.getStatus_user().equals("N")){
            holder.textViewStatusApprove.setVisibility(View.VISIBLE);
            holder.textViewStatusApprove.setText("Rejected!");
            holder.cvMain.setCardBackgroundColor(Color.parseColor("#eaebe6"));
            holder.linearLayoutCheckBoxUser.setVisibility(View.GONE);
            holder.viewUpload.setVisibility(View.GONE);
        }else {
            holder.textViewStatusApprove.setVisibility(View.VISIBLE);
            holder.linearLayoutCheckBoxUser.setVisibility(View.GONE);
            holder.viewUpload.setVisibility(View.GONE);
            holder.cvMain.setCardBackgroundColor(Color.parseColor("#f2f3ee"));
        }

        holder.viewUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mCtx, "Selected: " + product.getId_bantek(), Toast.LENGTH_LONG).show();

                Integer id = product.getId();
                String id_bantek = String.valueOf(product.getId_bantek());
                String departureDate = product.getDeparture_date();
                String returnDate = product.getReturn_date();
                String departureStation = product.getDeparture_station();
                String departureCity = product.getDeparture_city();
                String arrivalCity = product.getArrival_city();
                String arrivalStation = product.getArrival_station();
                String sppdImage = product.getSppd_image();
                String tiketImage = product.getTiket_image();
                String invoiceImage = product.getInvoice_image();
                String voucherImage = product.getVoucher_image();
                String amlImage = product.getAml_image();

                Intent intent = new Intent(mCtx, FileUpload.class);
                intent.putExtra("ID", id);
                intent.putExtra("ID_BANTEK", id_bantek);
                intent.putExtra("DEPARTURE_DATE", departureDate);
                intent.putExtra("RETURN_DATE", returnDate);
                intent.putExtra("DEPARTURE_STATION", departureStation);
                intent.putExtra("DEPARTURE_CITY", departureCity);
                intent.putExtra("ARRIVAL_CITY", arrivalCity);
                intent.putExtra("ARRIVAL_STATION", arrivalStation);
                intent.putExtra("SPPD_IMAGE", sppdImage);
                intent.putExtra("TIKET_IMAGE", tiketImage);
                intent.putExtra("INVOICE_IMAGE", invoiceImage);
                intent.putExtra("VOUCHER_IMAGE", voucherImage);
                intent.putExtra("AML_IMAGE", amlImage);
                mCtx.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewStatusApprove, textViewTripDepartureCode, textViewTripArrivalCode, textViewTripDepartureCity, textViewTripArrivalCity, textViewTripDepartureDate, textViewTripReturnDate;
        CheckBox checkBoxSppd, checkBoxTiket, checkBoxInvoice, checkBoxVoucher, checkBoxAML;
        CardView cvMain;
        View viewUpload;
        LinearLayout linearLayoutCheckBoxUser;


        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewTripDepartureCode = itemView.findViewById(R.id.tvTripDepartureCode);
            textViewTripArrivalCode = itemView.findViewById(R.id.tvTripArrivalCode);
            textViewTripDepartureCity = itemView.findViewById(R.id.tvTripDepartureCity);
            textViewTripArrivalCity = itemView.findViewById(R.id.tvTripArrivalCity);
            textViewTripDepartureDate = itemView.findViewById(R.id.tvTripDepartureDate);
            textViewTripReturnDate = itemView.findViewById(R.id.tvTripReturnDate);
            checkBoxSppd = itemView.findViewById(R.id.cbTripSppd);
            checkBoxTiket = itemView.findViewById(R.id.cbTripTiket);
            checkBoxInvoice = itemView.findViewById(R.id.cbTripInvoice);
            checkBoxVoucher = itemView.findViewById(R.id.cbTripVoucher);
            checkBoxAML = itemView.findViewById(R.id.cbTripAML);
            cvMain = itemView.findViewById(R.id.cvMain);
            viewUpload = itemView.findViewById(R.id.viewUpload);
            textViewStatusApprove = itemView.findViewById(R.id.tvStatusApprove);
            linearLayoutCheckBoxUser = itemView.findViewById(R.id.llCheckBoxUser);
        }
    }
}
