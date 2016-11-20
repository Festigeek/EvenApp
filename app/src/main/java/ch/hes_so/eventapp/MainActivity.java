package ch.hes_so.eventapp;

/**
 * Created by Mysteriosis on 12.11.16.
 */

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
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class MainActivity extends AppCompatActivity {
    private String[] mListsTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        /*
            Bouton flottant
         */

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                manageItem("");
//            }
//        });

        /*
            DRAWER
         */

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mListsTitles = getResources().getStringArray(R.array.list_menu);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name, R.string.app_description);

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.addDrawerListener(mDrawerToggle);

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

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, mListsTitles));
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
