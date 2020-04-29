package com.offgrid.coupler.core.holder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.offgrid.coupler.R;
import com.offgrid.coupler.data.entity.Region;

public class RegionViewHolder extends RecyclerView.ViewHolder {

    private final TextView regionName;
    private final TextView tilesCount;

    public RegionViewHolder(View itemView) {
        super(itemView);
        regionName = itemView.findViewById(R.id.region_name);
        tilesCount = itemView.findViewById(R.id.tiles_count);
    }

    public void update(Region region) {
        regionName.setText(region.getName());
        tilesCount.setText("3.2K");
    }

}
