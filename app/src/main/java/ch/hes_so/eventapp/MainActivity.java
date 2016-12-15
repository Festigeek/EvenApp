package ch.hes_so.eventapp;

/**
 * Created by Mysteriosis on 12.11.16.
 */

import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import ch.hes_so.eventapp.models.Person;

public class MainActivity extends AppCompatActivity {
    private String[] mListsTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private FrameLayout fragContainer;
    private ActionBarDrawerToggle mDrawerToggle;

    private void initialize() {
        // Init Peoples & Calendars
        Integer nb_person = (int) Person.count(Person.class);
        if(nb_person == -1 || nb_person == 0) {
            List<Person> peoples = new ArrayList<>();

            peoples.add(new Person("Némar", "Jean", 2, "o120cokuf4u6dg54ptcssr0k0g"));
            peoples.add(new Person("Tremblais", "Jean", 2, "f2jbpa6gsigck7nf0015imk674"));
            peoples.add(new Person("Moret", "Jérôme", 0, "f93vo2livmhsfib9lgo8cdtoh8"));
            peoples.add(new Person("Curty", "P-Alain", 0, "lmpprsl9ui07n410e8en3t5jdg"));
            peoples.add(new Person("Cussonais", "Simon", 1, "jmldhmjj0f8rba73ifja32drp8"));

            SugarRecord.saveInTx(peoples);
        }
    }

    private void changePage(android.app.Fragment frag) {
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
                        changePage(new CalendarWebViewFragment());
                        break;
                    case 2:
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

        PersonListFragment frag = new PersonListFragment();
        getFragmentManager().beginTransaction().add(R.id.content_main, frag).commit();
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
