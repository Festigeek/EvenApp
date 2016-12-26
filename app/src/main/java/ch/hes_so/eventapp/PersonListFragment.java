package ch.hes_so.eventapp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ch.hes_so.eventapp.models.Person;
import ch.hes_so.eventapp.services.CalendarTask;

/**
 * Created by Mysteriosis on 17.11.16.
 */

public class PersonListFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private MenuItem mRefreshButton;

    private void updatePersonsEvents(){
        ArrayList<Person> people = (ArrayList<Person>) Person.listAll(Person.class);
        for(Person p : people) {
            new CalendarTask(getActivity(), p, mRecyclerView.getAdapter()).execute();
        }
    }

    // Overrides

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_person_list, container, false);
        setHasOptionsMenu(true);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.frag_person_list_recycler);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        mRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        PersonRecyclerAdapter adapter = new PersonRecyclerAdapter(getActivity());
        mRecyclerView.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);

        updatePersonsEvents();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        mRefreshButton = menu.getItem(0);
        mRefreshButton.setVisible(true);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        switch(itemId) {
            case R.id.action_refresh:
                updatePersonsEvents();
            default:
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop() {
        mRefreshButton.setVisible(false);
        super.onStop();
    }
}
