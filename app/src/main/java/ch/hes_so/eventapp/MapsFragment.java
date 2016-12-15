package ch.hes_so.eventapp;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import ch.hes_so.eventapp.models.Person;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Person person;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.person = Person.findById(Person.class, getArguments().getLong("pid"));
    }

    public static MapsFragment newInstance(Long id) {
        MapsFragment mapFrag = new MapsFragment();
        Bundle bundle = new Bundle();

        bundle.putLong("pid", id);
        mapFrag.setArguments(bundle);

        return mapFrag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        MapFragment mapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(this.person.getLatitude() == null || this.person.getLongitude() == null) {
            Log.e("Person", this.person.toString());
            Toast.makeText(getContext(), "Emplacement de " + this.person.getFirstname() + " " + this.person.getLastname() + " inconnu...", Toast.LENGTH_SHORT).show();
        }
        else {
            LatLng position = this.person.getCoords();
            mMap.addMarker(new MarkerOptions().position(position).title(this.person.getFirstname() + " " + this.person.getLastname()));
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 18.0f));
        }
    }
}
