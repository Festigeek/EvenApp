package ch.hes_so.eventapp;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ch.hes_so.eventapp.models.Person;

/**
 * Created by Mysteriosis on 20.10.16.
 */

public class PersonRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Person> people;
    private Activity activity;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;

        public ViewHolder(View v) {
            super(v);
            this.name = (TextView) v.findViewById(R.id.name);
        }
    }

    public PersonRecyclerAdapter(Activity activity, ArrayList<Person> listPeoples)
    {
        Log.i("SIZE: ", String.valueOf(listPeoples.size()));
        this.people = listPeoples;
        this.activity = activity;
    }


    @Override
    public PersonRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.person_list_item, parent, false);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println((Integer)v.getTag());
                //Person selectedItem = Pers.get((Integer)v.getTag());
                /*Toast toast = Toast.makeText(v.getContext(), "Element selectionne : " + selectedItem.getLastname(), Toast.LENGTH_SHORT);
                toast.show();

                CalendarWebViewFragment fragment = new CalendarWebViewFragment();
                Bundle bundle = new Bundle();
                String[] calendar_urls = new String[]{selectedItem.getCalendar().getCompleteUrl()};
                bundle.putStringArray("calendar_urls", calendar_urls);
                fragment.setArguments(bundle);

                FragmentTransaction transaction = activity.getFragmentManager().beginTransaction();
                transaction.replace(android.R.id.content, fragment);
                transaction.addToBackStack(null);
                transaction.commit();*/
            }
        });
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder h, int position) {
        PersonRecyclerAdapter.ViewHolder holder = (PersonRecyclerAdapter.ViewHolder) h;
        holder.name.setText(this.people.get(position).getLastname() + " " + this.people.get(position).getFirstname());
    }

    @Override
    public int getItemCount() {
        return this.people.size();
    }
}
