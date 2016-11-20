package ch.hes_so.eventapp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Mysteriosis on 17.11.16.
 */

public class PersonListFragment extends Fragment {
    private ArrayList<Person> peoples = new ArrayList<Person>();

    private RecyclerView mRecyclerView;


    // Private functions

    private void initList(ArrayList<Person> p)
    {
        p.add(new Person("Tremblais", "Jean", 2));
        p.add(new Person("Cussonais", "Simon", 1));
        p.add(new Person("Curty", "P-Alain", 0));
        p.add(new Person("Moret", "Jérôme", 0));
    }

    private void manageItem(String item) {

    }

    // Overrides

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_person_list, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.frag_person_list_recycler);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        initList(peoples);

        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        PersonRecyclerAdapter adapter = new PersonRecyclerAdapter(getActivity(), peoples);
        mRecyclerView.setAdapter(adapter);


        //--------------------------------------
        // check if the permission is granted
        //--------------------------------------

        /*
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            init_phone_music_grid();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
        */
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            manageItem("");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
