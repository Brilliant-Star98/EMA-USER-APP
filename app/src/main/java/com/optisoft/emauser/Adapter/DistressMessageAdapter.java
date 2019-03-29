package com.optisoft.emauser.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.optisoft.emauser.Activity.DistressMessageActivity;
import com.optisoft.emauser.Model.DistressModel;
import com.optisoft.emauser.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class DistressMessageAdapter extends RecyclerView.Adapter<DistressMessageAdapter.ViewHolder>  {

    private final List<DistressModel> mValues;
    private DistressMessageActivity context;

    public DistressMessageAdapter(DistressMessageActivity context, List<DistressModel> items) {
        this.mValues = items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.distress_layout_item, parent, false);
        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.mItem = mValues.get(position);
        holder.mTitle.setText(holder.mItem.getSubject());
        holder.mMessage.setText(holder.mItem.getMessage());
        if (holder.mItem.getFname() != null){
            holder.name.setText(holder.mItem.getFname()+" "+holder.mItem.getLname());
            holder.name.setVisibility(View.VISIBLE);
        }

        if (context.isAgent && holder.mItem.getStatus().equals("0")){
            holder.send.setVisibility(View.VISIBLE);
        }else {
            holder.send.setVisibility(View.GONE);
        }

        holder.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.sendNotifiocationToAll(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback {
        public final View mView;
        public final TextView mTitle, mMessage, name, send;
        public DistressModel mItem;
        public FrameLayout mFrame;
        public MapView mapView;

        public ViewHolder(View view) {
            super(view);
            mView    = view;
            mTitle   = (TextView) view.findViewById(R.id.title);
            mMessage = (TextView) view.findViewById(R.id.message);
            name     = (TextView) view.findViewById(R.id.name);
            send     = (TextView) view.findViewById(R.id.send);
            mFrame   = (FrameLayout) view.findViewById(R.id.map_layout);
            mapView  = (MapView) view.findViewById(R.id.gmap);

            if (mapView != null)
            {
                mapView.onCreate(null);
                mapView.onResume();
                mapView.getMapAsync(this);
            }
        }

        @Override
        public String toString()
        {
            return super.toString() + " '" + mTitle.getText()+ "'";
        }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            int pos = getPosition();
            DistressModel model = mValues.get(pos);
            LatLng latLng = new LatLng(Double.parseDouble(model.getLatitude()), Double.parseDouble(model.getLongitude()));
            googleMap.addMarker(new MarkerOptions().position(latLng).title(model.getFname()+" "+model.getLname()));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15f));
        }
    }

}


