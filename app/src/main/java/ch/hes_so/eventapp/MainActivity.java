package ch.hes_so.eventapp;

/**
 * Created by Mysteriosis on 12.11.16.
 */

import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ch.hes_so.eventapp.models.Person;
import ch.hes_so.eventapp.services.LocationService;

public class MainActivity extends AppCompatActivity {
    private String[] mListsTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private FrameLayout fragContainer;
    private ActionBarDrawerToggle mDrawerToggle;
    private Person me;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(me != null) {
                me.setLatitude(intent.getDoubleExtra("Latitude", 0));
                me.setLongitude(intent.getDoubleExtra("Longitude", 0));
                me.save();
            }
        }
    };

    private void initialize() {

        // Init Peoples & Calendars
        Integer nb_person = (int) Person.count(Person.class);
        if(nb_person == -1 || nb_person == 0) {
            this.me = new Person("Curty", "P-Alain", 0, "lmpprsl9ui07n410e8en3t5jdg");
            this.me.save();

            List<Person> peoples = new ArrayList<>();

            peoples.add(new Person("Némar", "Jean", 2, "o120cokuf4u6dg54ptcssr0k0g", 46.5196535, 6.632273400000031));
            peoples.add(new Person("Tremblais", "Jean", 2, "f2jbpa6gsigck7nf0015imk674"));
            peoples.add(new Person("Moret", "Jérôme", 0, "f93vo2livmhsfib9lgo8cdtoh8", 46.2312995, 6.919441600000027));
            peoples.add(new Person("Cussonais", "Simon", 1, "jmldhmjj0f8rba73ifja32drp8"));

            SugarRecord.saveInTx(peoples);
        }
        else {
            this.me = Person.find(Person.class, "firstname = ? AND lastname = ?", "P-Alain", "Curty").get(0);
        }
    }

    public void changePage(android.app.Fragment frag) {
        fragContainer.removeAllViews();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_main, frag);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        this.initialize();

        Intent intent = new Intent(this, LocationService.class);
        startService(intent);

        fragContainer = (FrameLayout) findViewById(R.id.content_main);
        mListsTitles = getResources().getStringArray(R.array.list_menu);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(view.getContext(), "Element selectionne : " + mListsTitles[position], Toast.LENGTH_SHORT).show();
                switch(position) {
                    case 0:
                        changePage(new PersonListFragment());
                        break;
                    case 1:
                        CalendarWebViewFragment fragment = new CalendarWebViewFragment();
                        Bundle bundle = new Bundle();
                        String[] calendar_urls = new String[(int)Person.count(Person.class)];
                        List<Person> persons = Person.listAll(Person.class);
                        for(int i = 0; i < persons.size(); ++i) {
                            calendar_urls[i] = persons.get(i).getCalendar().getCompleteUrl();
                        }
                        bundle.putStringArray("calendar_urls", calendar_urls);
                        fragment.setArguments(bundle);
                        changePage(fragment);
                        break;
                    case 2:
                        changePage(new MapsFragment());
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    default:
                        Toast.makeText(view.getContext(), "Commande inconnue", Toast.LENGTH_SHORT).show();
                        break;
                }

                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
        mDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, mListsTitles));

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name, R.string.app_description);
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                manageItem("");
//            }
//        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(this.receiver, new IntentFilter(LocationService.MESSAGE));

        PersonListFragment frag = new PersonListFragment();

        getFragmentManager().beginTransaction().add(R.id.content_main, frag).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(this.receiver, new IntentFilter(LocationService.MESSAGE));
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(this.receiver);
        super.onStop();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            Toast.makeText(getApplicationContext(), "Paysage", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getApplicationContext(), "Portrait", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //TODO: Load list
                } else {
                    new AlertDialog.Builder(this).setTitle("Permission Error").setMessage("Missing permission(s)").setNeutralButton("Close", null).show();
                    this.finish();
                }

                return;
            }
        }
    }
}
