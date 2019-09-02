package id.co.GMFAeroAsia;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

public class AirportListAdapter extends RecyclerView.Adapter<AirportListAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<AirportList> airportList;
    private List<AirportList> airportListFiltered;
    private AirportListAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, phone,textViewLocation,textViewCode,textViewName,textViewCity;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
//            name = view.findViewById(R.id.name);
//            phone = view.findViewById(R.id.phone);
//            thumbnail = view.findViewById(R.id.thumbnail);


            textViewLocation = itemView.findViewById(R.id.tvLocation);
            textViewCode = itemView.findViewById(R.id.tvCode);
            textViewName = itemView.findViewById(R.id.tvName);
            textViewCity = itemView.findViewById(R.id.tvCity);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onAirportListSelected(airportListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }


    public AirportListAdapter(Context context, List<AirportList> airportList, AirportListAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.airportList = airportList;
        this.airportListFiltered = airportList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.airport_list_row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final AirportList airportList = airportListFiltered.get(position);
//        holder.name.setText(airportList.getName());

//        Glide.with(context)
//                .load(airportList.getImage())
//                .apply(RequestOptions.circleCropTransform())
//                .into(holder.thumbnail);
//        holder.phone.setText(airportList.getPhone());

        holder.textViewLocation.setText(airportList.getLocation());
        holder.textViewCode.setText(airportList.getCode());
        holder.textViewName.setText(String.valueOf(airportList.getName()));
        holder.textViewCity.setText(String.valueOf(airportList.getCity()));
    }

    @Override
    public int getItemCount() {
        return airportListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    airportListFiltered = airportList;
                } else {
                    List<AirportList> filteredList = new ArrayList<>();
                    for (AirportList row : airportList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if ( row.getLocation().toLowerCase().contains(charString.toLowerCase()) ||row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getCode().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    airportListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = airportListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                airportListFiltered = (ArrayList<AirportList>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface AirportListAdapterListener {
        void onAirportListSelected(AirportList airportList);
    }
}
