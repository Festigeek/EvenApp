package ch.hes_so.eventapp;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import ch.hes_so.eventapp.models.Person;

/**
 * Created by Mysteriosis on 20.10.16.
 */

public class PersonRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Person> people;
    private Activity activity;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout layout;
        public TextView name;
        public ImageView favorite;
        public ImageView presence;

        public ViewHolder(View v) {
            super(v);
            this.layout = (RelativeLayout) v.findViewById(R.id.list_item);
            this.name = (TextView) v.findViewById(R.id.name);
            this.favorite = (ImageView) v.findViewById(R.id.favorite_image);
            this.presence = (ImageView) v.findViewById(R.id.presence_image);
        }
    }

    public PersonRecyclerAdapter(Activity activity)
    {
        this.people = Person.listAll(Person.class);
        this.activity = activity;
    }

    @Override
    public PersonRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.person_list_item, parent, false);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Person p = Person.findById(Person.class, (Long)v.getTag());

                CalendarWebViewFragment fragment = new CalendarWebViewFragment();
                Bundle bundle = new Bundle();
                String[] calendar_urls = new String[]{p.getCalendar().getCompleteUrl()};
                bundle.putStringArray("calendar_urls", calendar_urls);
                fragment.setArguments(bundle);

                FloatingActionButton fab = (FloatingActionButton) activity.findViewById(R.id.fab);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        changePage(MapsFragment.newInstance(p.getId()));
                    }
                });

                changePage(fragment);
            }
        });
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder h, int position) {
        this.people = Person.listAll(Person.class);
        Person p = this.people.get(position);
        ViewHolder holder = (ViewHolder) h;

        holder.layout.setTag(p.getId());
        holder.name.setText(p.getLastname() + " " + p.getFirstname());

        if(p.getFavorite())
            holder.presence.setImageResource(android.R.drawable.btn_star_big_on);
        else
            holder.presence.setImageResource(android.R.drawable.btn_star_big_off);

        if(p.getCalendar() == null || !p.getCalendar().isBusy())
            holder.presence.setImageResource(android.R.drawable.presence_online);
        else
            holder.presence.setImageResource(android.R.drawable.presence_busy);
    }

    @Override
    public int getItemCount() {
        return this.people.size();
    }

    private void changePage(android.app.Fragment frag) {
        FragmentTransaction transaction = activity.getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_main, frag);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
