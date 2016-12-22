package ch.hes_so.eventapp;

import android.app.Fragment;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

    private LocationManager locationManager;
    private GoogleMap mMap;
    private Person person;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null && getArguments().containsKey("pid"))
            this.person = Person.findById(Person.class, getArguments().getLong("pid"));
        else
            this.person = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        ArrayList<Person> people;
        LatLng posCam = new LatLng(46.77891815318736, 6.659035412594676); // HEIG-VD
        //TODO: Prendre la position GPS actuelle du terminal

        if(this.person != null) {
            if (this.person.getLatitude() == null || this.person.getLongitude() == null) {
                Log.e("Person", this.person.toString());
                Toast.makeText(getContext(), "Emplacement de " + this.person.getFirstname() + " " + this.person.getLastname() + " inconnu...", Toast.LENGTH_SHORT).show();
            } else {
                posCam = this.person.getCoords();
                mMap.addMarker(new MarkerOptions().position(posCam).title(this.person.getFirstname() + " " + this.person.getLastname()));
            }
        }
        else {
            people = (ArrayList<Person>) Person.listAll(Person.class);
            for(Person p : people) {
                if (p.getLatitude() != null && p.getLongitude() != null)
                    mMap.addMarker(new MarkerOptions().position(p.getCoords()).title(p.getFirstname() + " " + p.getLastname()));
            }
        }

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(posCam, 18.0f));
    }
}
