package ch.hes_so.eventapp;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.plus.People;

import java.util.ArrayList;
import java.util.List;

import ch.hes_so.eventapp.models.Person;

/**
 * Created by Mysteriosis on 20.10.16.
 */

public class PersonRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Person> people;
    private Activity activity;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public LinearLayout layout;

        public ViewHolder(View v) {
            super(v);
            this.name = (TextView) v.findViewById(R.id.name);
            this.layout = (LinearLayout) v.findViewById(R.id.list_item);
        }
    }

    public PersonRecyclerAdapter(Activity activity)
    {
        this.people = Person.listAll(Person.class);
        this.activity = activity;

        Log.i("SIZE: ", String.valueOf(this.people.size()));
    }


    @Override
    public PersonRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.person_list_item, parent, false);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Person p = Person.findById(Person.class, (Long)v.getTag());
                Log.i("INFO_PERSON", p.toString());
                Toast.makeText(v.getContext(), "Element selectionne : " + p.getLastname(), Toast.LENGTH_SHORT).show();
                
                // TODO : Régler le problème des couleurs
                CalendarWebViewFragment fragment = new CalendarWebViewFragment();
                Bundle bundle = new Bundle();
                String[] calendar_urls = new String[]{p.getCalendar().getCompleteUrl()};
                bundle.putStringArray("calendar_urls", calendar_urls);
                fragment.setArguments(bundle);

                FragmentTransaction transaction = activity.getFragmentManager().beginTransaction();
                transaction.replace(R.id.content_main, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder h, int position) {
        //TODO: Recupérer des infos via l'API Calendar pour savoir si les gens sont occupés ou non (dans une tâche)
        Person p = this.people.get(position);
        PersonRecyclerAdapter.ViewHolder holder = (PersonRecyclerAdapter.ViewHolder) h;
        holder.name.setText(p.getLastname() + " " + p.getFirstname());
        holder.layout.setTag(p.getId());
    }

    @Override
    public int getItemCount() {
        return this.people.size();
    }
}
