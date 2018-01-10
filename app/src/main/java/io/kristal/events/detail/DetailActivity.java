package io.kristal.events.detail;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import io.kristal.events.R;

/**
 * Created by sebastien on 10/01/2018.
 */

public abstract class DetailActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, PlaceTextListener {

    protected static final String TAG = DetailActivity.class.getSimpleName();

    public static String EXTRA_EVENT = "io.kristal.events.detail.DetailActivity.EXTRA_EVENT";

    protected DetailFragment mDetail;
    private GoogleMap mMap;
    private Marker mMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {
            mDetail = ((DetailFragment) getSupportFragmentManager().findFragmentById(R.id.detail));
            mDetail.setPlaceTextListener(this);
            ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detail, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_check) {
            onCheckOptionsItemSelected();
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }

    protected abstract void onCheckOptionsItemSelected();

    private void onPlaceChanged(String place) {
        mDetail.onPlaceChanged(place);
    }

    @Override
    public void onPlaceTextChanged(String place) {
        setPlace(place);
    }

    private void setPlace(String place) {
        try {
            List<Address> addresses = new Geocoder(this).getFromLocationName(place, 1);
            if (! addresses.isEmpty()) {
                Address address = addresses.get(0);

                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                if (mMarker == null) {
                    mMarker = mMap.addMarker(new MarkerOptions().position(latLng).title(place));
                }
                else {
                    mMarker.setPosition(latLng);
                    mMarker.setTitle(place);
                }
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }
            else {
                Toast.makeText(this, "No address found", Toast.LENGTH_LONG);
            }
        }
        catch (IOException exception) {
            Toast.makeText(this, "Network unavailable", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "setPlace: network unavailable");
            exception.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapLongClickListener(this);

        LatLng latLng = new LatLng(48.732041, -3.459063);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 7));
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        String place = "Unknown";
        try {
            List<Address> addresses = new Geocoder(this).getFromLocation(latLng.latitude, latLng.longitude, 1);
            String locality = (addresses.isEmpty() ? null : addresses.get(0).getLocality());
            if (locality != null) {
                place = locality;
            }
        }
        catch (IOException exception) {
            Log.i(TAG, "onMapLongClick: network unavailable");
            exception.printStackTrace();
        }

        if (mMarker == null) {
            mMarker = mMap.addMarker(new MarkerOptions().position(latLng).title(place));
        }
        else {
            mMarker.setPosition(latLng);
            mMarker.setTitle(place);
        }

        onPlaceChanged(place);
    }
}
