package com.offgrid.coupler.controller.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.offgrid.coupler.R;
import com.offgrid.coupler.controller.map.configurator.ContactLocationConfigurator;
import com.offgrid.coupler.controller.map.configurator.DeviceLocationConfigurator;
import com.offgrid.coupler.controller.map.configurator.PlaceLocationConfigurator;
import com.offgrid.coupler.core.callback.OnClickContactLocationListener;
import com.offgrid.coupler.core.callback.OnClickPlaceLocationListener;
import com.offgrid.coupler.core.holder.ContactDetailsViewHolder;


public class MapFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener {

    private View rootView;
    private MapView mapView;
    private MapboxMap mapboxMap;

    private BottomSheetBehavior contactDetailsSheet;
    private BottomSheetBehavior placeDetailsSheet;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Mapbox.getInstance(getActivity(), getString(R.string.map_access_token));
        rootView = inflater.inflate(R.layout.fragment_map, container, false);

        placeDetailsSheet = BottomSheetBehavior.from(rootView.findViewById(R.id.bottom_sheet_place_details));
        placeDetailsSheet.setState(BottomSheetBehavior.STATE_HIDDEN);

        contactDetailsSheet = BottomSheetBehavior.from(rootView.findViewById(R.id.bottom_sheet_contact_details));
        contactDetailsSheet.setState(BottomSheetBehavior.STATE_HIDDEN);

        mapView = rootView.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_map, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                new DeviceLocationConfigurator()
                        .withContext(getActivity())
                        .withMapbox(MapFragment.this.mapboxMap)
                        .configure();

                new ContactLocationConfigurator()
                        .withContext(getActivity())
                        .withMapbox(MapFragment.this.mapboxMap)
                        .configure();

                new PlaceLocationConfigurator()
                        .withContext(getActivity())
                        .withMapbox(MapFragment.this.mapboxMap)
                        .configure();
            }
        });


        OnClickContactLocationListener contactLocationListener = new OnClickContactLocationListener(getActivity())
                .withMapbox(mapboxMap)
                .withBottomSheet(contactDetailsSheet)
                .withViewHolder(new ContactDetailsViewHolder(rootView));


        OnClickPlaceLocationListener placeLocationListener = new OnClickPlaceLocationListener(this)
                .withMapbox(mapboxMap)
                .withBottomSheet(placeDetailsSheet)
                .withRootView(rootView)
                .withViewModel()
                .withDialog();

        mapboxMap.addOnMapClickListener(contactLocationListener);
        mapboxMap.addOnMapLongClickListener(placeLocationListener);

        rootView.findViewById(R.id.btn_contact_info).setOnClickListener(contactLocationListener);
        rootView.findViewById(R.id.btn_contact_chat).setOnClickListener(contactLocationListener);
        rootView.findViewById(R.id.close_contact_details).setOnClickListener(contactLocationListener);

        rootView.findViewById(R.id.back_to_camera_tracking_mode).setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.back_to_camera_tracking_mode) {
            LocationComponent locationComponent = mapboxMap.getLocationComponent();
            locationComponent.setCameraMode(CameraMode.TRACKING_COMPASS);
            locationComponent.zoomWhileTracking(16f);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

}