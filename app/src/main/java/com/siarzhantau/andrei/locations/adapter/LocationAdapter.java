package com.siarzhantau.andrei.locations.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.siarzhantau.andrei.locations.R;
import com.siarzhantau.andrei.locations.model.Location;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class LocationAdapter extends RealmRecyclerViewAdapter<Location, LocationAdapter.LocationViewHolder> {

    private LocationClickListener mLocationClickListener;

    public interface LocationClickListener {
        void onLocationClick(String locationId);
    }

    public LocationAdapter(Context context, OrderedRealmCollection<Location> realmResults, LocationClickListener locationClickListener) {
        super(context, realmResults, true);
        mLocationClickListener = locationClickListener;
    }

    @Override
    public LocationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new LocationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LocationViewHolder holder, int position) {
        final Location location = getData().get(position);

        holder.mTitle.setText(location.name);
        if (TextUtils.isEmpty(location.description)) {
            holder.mDescription.setText("Location: " + location.lng + " " + location.lat);
        } else {
            holder.mDescription.setText(location.description);
        }

        holder.itemView.setOnClickListener(v -> {
            if (mLocationClickListener != null) {
                mLocationClickListener.onLocationClick(location.id);
            }
        });
    }

    static class LocationViewHolder extends RecyclerView.ViewHolder {
        TextView mTitle;
        TextView mDescription;

        LocationViewHolder(View view) {
            super(view);
            mTitle = (TextView) view.findViewById(R.id.title);
            mDescription = (TextView) view.findViewById(R.id.description);
        }
    }
}
