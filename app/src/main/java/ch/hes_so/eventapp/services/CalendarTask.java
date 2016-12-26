package ch.hes_so.eventapp.services;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ch.hes_so.eventapp.MainActivity;
import ch.hes_so.eventapp.models.Person;

/**
 * Created by Mysteriosis on 22.12.16.
 */

public class CalendarTask extends AsyncTask<Void, Void, List<String>> {

    public static final String[] SCOPES = { CalendarScopes.CALENDAR_READONLY };

    private com.google.api.services.calendar.Calendar mService = null;
    private Exception mLastError = null;
    private Person person;
    private String calId = "primary";
    private MainActivity main;
    private RecyclerView.Adapter adapter = null;

    public CalendarTask(Activity activity) {
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        this.main = (MainActivity) activity;

        this.mService = new com.google.api.services.calendar.Calendar.Builder(
                transport, jsonFactory, this.main.mCredential)
                .setApplicationName("Google Calendar API Android Quickstart")
                .build();
    }

    public CalendarTask(Activity activity, Person person) {
        this(activity);
        this.person = person;
        this.calId = person.getCalendar().getCompleteUrl();
    }

    public CalendarTask(Activity activity, Person person, RecyclerView.Adapter adapter) {
        this(activity, person);
        this.adapter = adapter;
    }

    /**
     * Background task to call Google Calendar API.
     * @param params no parameters needed for this task.
     */
    @Override
    protected List<String> doInBackground(Void... params) {
        try {
            return getCurrentEvent();
        } catch (Exception e) {
            mLastError = e;
            cancel(true);
            return null;
        }
    }

    /**
     * Fetch a list of the next 10 events from the primary calendar.
     * @return List of Strings describing returned events.
     * @throws IOException
     */
    private List<String> getCurrentEvent() throws IOException {
        // List the next 10 events from the primary calendar.
        DateTime now = new DateTime(System.currentTimeMillis());
        List<String> eventStrings = new ArrayList<>();

        Events events = mService.events().list(calId)
                .setMaxResults(1)
                .setTimeMin(now)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
        List<Event> items = events.getItems();

        for (Event event : items) {
            DateTime start = event.getStart().getDateTime();
            DateTime end = event.getEnd().getDateTime();
            if (start == null) start = event.getStart().getDate();
            if (end == null) end = new DateTime(System.currentTimeMillis()+ 24*60*60);

            if(start.getValue() < now.getValue() && end.getValue() > now.getValue()) {
                String s = String.format("%s [%s(%d) - %s(%d)]", event.getSummary(), start, start.getValue(), end, end.getValue());
                eventStrings.add(s);
            }
        }

        return eventStrings;
    }


    @Override
    protected void onPreExecute() {
        Log.v("AsyncTask", "Start Calendar Task");
    }

    @Override
    protected void onPostExecute(List<String> output) {
        //mProgress.hide();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }

        if(output != null && output.size() > 0) {
            this.person.getCalendar().setBusy(true);
            this.person.getCalendar().save();
        }
        else {
            this.person.getCalendar().setBusy(false);
            this.person.getCalendar().save();
        }

        Log.v("TASKS", "CalendarTask finished");
    }

    @Override
    protected void onCancelled() {
        if (mLastError != null) {
            if(mLastError instanceof UserRecoverableAuthIOException) {
                this.main.startActivityForResult(
                    ((UserRecoverableAuthIOException) mLastError).getIntent(),
                    this.main.REQUEST_AUTHORIZATION
                );
            }
            else if(mLastError instanceof Exception) {
                mLastError.printStackTrace();
            }
            else {
                Log.e("Google Calendar", mLastError.getClass().getSimpleName());
            }
        } else {
            Log.e("Google Calendar", "Request cancelled.");
        }
    }
}
