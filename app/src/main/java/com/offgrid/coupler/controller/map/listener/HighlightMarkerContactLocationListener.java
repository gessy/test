package com.offgrid.coupler.controller.map.listener;

import android.animation.ValueAnimator;
import android.graphics.PointF;

import androidx.annotation.NonNull;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.List;

import static com.offgrid.coupler.controller.map.MapConstants.*;

public class HighlightMarkerContactLocationListener implements MapboxMap.OnMapClickListener {

    private MapboxMap mapboxMap;
    private ValueAnimator markerAnimator;

    private boolean markerSelected = false;


    public HighlightMarkerContactLocationListener(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        initAnimator();
    }

    private void initAnimator() {
        markerAnimator = new ValueAnimator();
        markerAnimator.setDuration(300);
        markerAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                SymbolLayer layer = (SymbolLayer) mapboxMap.getStyle().getLayer(SELECTED_USER_LOCATION_LAYER_ID);
                layer.setProperties(PropertyFactory.iconSize((float) animator.getAnimatedValue()));
            }
        });
    }


    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        Style style = mapboxMap.getStyle();
        if (style != null) {
            final PointF pixel = mapboxMap.getProjection().toScreenLocation(point);
            List<Feature> features = mapboxMap.queryRenderedFeatures(pixel, USER_LOCATION_LAYER_ID);
            List<Feature> selectedFeature = mapboxMap.queryRenderedFeatures(pixel, SELECTED_USER_LOCATION_LAYER_ID);

            if (selectedFeature.size() > 0 && markerSelected) {
                return false;
            }

            if (features.isEmpty()) {
                if (markerSelected) deselectMarker();
                return false;
            }

            GeoJsonSource source = style.getSourceAs(SELECTED_USER_LOCATION_GEOJSON_ID);
            if (source != null) {
                source.setGeoJson(FeatureCollection.fromFeatures(
                        new Feature[]{Feature.fromGeometry(features.get(0).geometry())}));
            }

            if (markerSelected) {
                deselectMarker();
            }
            if (features.size() > 0) {
                selectMarker();
            }
        }

        return true;
    }

    private void selectMarker() {
        markerAnimator.setObjectValues(1f, 2f);
        markerAnimator.start();
        markerSelected = true;
    }

    private void deselectMarker() {
        markerAnimator.setObjectValues(2f, 1f);
        markerAnimator.start();
        markerSelected = false;
    }

}
