package ch.hes_so.eventapp;

/**
 * Created by Mysteriosis on 12.11.16.
 */

import android.accounts.AccountManager;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.ExponentialBackOff;
import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.hes_so.eventapp.models.Person;
import ch.hes_so.eventapp.services.CalendarTask;
import ch.hes_so.eventapp.services.LocationService;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    public static final int REQUEST_ACCOUNT_PICKER = 1000;
    public static final int REQUEST_AUTHORIZATION = 1001;
    public static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    public static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;
    public static final String PREF_ACCOUNT_NAME = "accountName";

    public static final int REQUEST_LOCALISATION = 500;
    private String[] mListsTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private FrameLayout fragContainer;
    private ActionBarDrawerToggle mDrawerToggle;
//    private ProgressDialog mProgress;

    private Person me;
    public GoogleAccountCredential mCredential;

    /**
     * Initialize SQLite at first startup (DB seed).
     */
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

                        // Add calendars of all persons
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

//        mProgress = new ProgressDialog(this);
//        mProgress.setMessage("Communication avec Google Calendar API ...");

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

        // Initialize credentials.
        mCredential = GoogleAccountCredential.usingOAuth2(
            this, Arrays.asList(CalendarTask.SCOPES))
            .setBackOff(new ExponentialBackOff());

        // Check Permissions
        checkPermissions();
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(this.locationReceiver, new IntentFilter(LocationService.MESSAGE));
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(this.locationReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(this.locationReceiver, new IntentFilter(LocationService.MESSAGE));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(this.locationReceiver);
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
    }

    /**
     * Listener for Location service broadcast
     */
    private BroadcastReceiver locationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(me != null) {
                me.setLatitude(intent.getDoubleExtra("Latitude", 0));
                me.setLongitude(intent.getDoubleExtra("Longitude", 0));
                me.save();
            }
        }
    };


    /**
     * All verifications to use CalendarAPI
     */

    /**
     * Attempt to call the API, after verifying that all the preconditions are
     * satisfied. The preconditions are: Google Play Services installed, an
     * account was selected and the device currently has online access. If any
     * of the preconditions are not satisfied, the app will prompt the user as
     * appropriate.
     */
    private void checkPermissions() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        if (apiAvailability.isGooglePlayServicesAvailable(this) != ConnectionResult.SUCCESS) {
            final int connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(this);
            if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
                Dialog dialog = apiAvailability.getErrorDialog(
                    MainActivity.this,
                    connectionStatusCode,
                    REQUEST_GOOGLE_PLAY_SERVICES);
                dialog.show();
            }
        }
        else if (mCredential == null || mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        }
        else if (!isDeviceOnline()) {
            Log.e("Connexion", "No network connection available.");
        }

        if(!EasyPermissions.hasPermissions(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Request the ACCESS_FINE_LOCATION permission via a user dialog
            EasyPermissions.requestPermissions(
                this,
                "This app needs to access your localisation informations.",
                REQUEST_LOCALISATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        }

        // Load PersonListFragment if all permission are granted.
        PersonListFragment frag = new PersonListFragment();
        getFragmentManager().beginTransaction().add(R.id.content_main, frag).commit();
    }

    /**
     * Attempts to set the account used with the API credentials. If an account
     * name was previously saved it will use that one; otherwise an account
     * picker dialog will be shown to the user. Note that the setting the
     * account to use with the credentials object requires the app to have the
     * GET_ACCOUNTS permission, which is requested here if it is not already
     * present. The AfterPermissionGranted annotation indicates that this
     * function will be rerun automatically whenever the GET_ACCOUNTS permission
     * is granted.
     */
    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(this, android.Manifest.permission.GET_ACCOUNTS)) {
            String accountName = getPreferences(Context.MODE_PRIVATE).getString(PREF_ACCOUNT_NAME, null);

            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
            }
            else {
                startActivityForResult(
                    mCredential.newChooseAccountIntent(),
                    REQUEST_ACCOUNT_PICKER
                );
            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(
                this,
                "This app needs to access your Google account (via Contacts).",
                REQUEST_PERMISSION_GET_ACCOUNTS,
                android.Manifest.permission.GET_ACCOUNTS);
        }
    }

    /**
     * Called when an activity launched here (specifically, AccountPicker
     * and authorization) exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     * @param requestCode code indicating which activity result is incoming.
     * @param resultCode code indicating the result of the incoming
     *     activity result.
     * @param data Intent (containing result data) returned by incoming
     *     activity result.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    Toast.makeText(getApplicationContext(), "This app requires Google Play Services. Please install " +
                            "Google Play Services on your device and relaunch this app.", Toast.LENGTH_LONG).show();
                }
                else {
                    checkPermissions();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null && data.getExtras() != null) {
                    String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);

                    if (accountName != null) {
                        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        mCredential.setSelectedAccountName(accountName);
                        checkPermissions();
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    checkPermissions();
                }
                break;
        }
    }

    /**
     * Respond to requests for permissions at runtime for API 23 and above.
     * @param requestCode The request code passed in
     *     requestPermissions(android.app.Activity, String, int, String[])
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *     which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /**
     * Checks whether the device currently has a network connection.
     * @return true if the device has a network connection, false otherwise.
     */
    private boolean isDeviceOnline() {
        NetworkInfo networkInfo = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        // Do nothing.
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        // Do nothing.
    }
}
