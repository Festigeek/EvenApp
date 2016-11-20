package ch.hes_so.eventapp;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

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
        this.people = listPeoples;
        this.activity = activity;
    }


    @Override
    public PersonRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.person_list_item, parent, false);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Person selectedItem = people.get((Integer)v.getTag());
                Toast toast = Toast.makeText(v.getContext(), "Element selectionne : " + selectedItem.getNom(), Toast.LENGTH_SHORT);
                toast.show();

                //TODO: Instancier le Fragment correspondant
//                Fragment frag = new PersonListFragment();
//                FragmentTransaction transaction = activity.getFragmentManager().beginTransaction();
//                transaction.replace(android.R.id.content, frag);
//                transaction.addToBackStack(null);
//                transaction.commit();
            }
        });
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder h, int position) {
        PersonRecyclerAdapter.ViewHolder holder = (PersonRecyclerAdapter.ViewHolder) h;
        holder.name.setText(this.people.get(position).getNom() + " " + this.people.get(position).getPrenom());
    }

    @Override
    public int getItemCount() {
        return this.people.size();
    }
}
