package ch.hes_so.eventapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.ArrayList;
import java.util.Random;

import ch.hes_so.eventapp.models.Calendar;

public class CalendarWebViewFragment extends Fragment {

    private String url = "https://calendar.google.com/calendar/embed?title=LAN%20Festigeek&showNav=0&showDate=0&showPrint=0&showTabs=0&showCalendars=0&showTz=0&mode=WEEK&wkst=2&hl=fr&bgcolor=%23FFFFFF&src=lan.festigeek%40gmail.com&color=%231B887A";
    private String[] calendar_urls;

    private class CalendarWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    public CalendarWebViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Random random = new Random(1000);
            calendar_urls = getArguments().getStringArray("calendar_urls");
            for(String calendar_url : calendar_urls) {
                // create a big random number - maximum is ffffff (hex) = 16777215 (dez)
                //int nextInt = random.nextInt(256*256*256);
                url += "&src="
                        + calendar_url;
                //System.out.println(url);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calendar_web_view, container, false);

        WebView calendar = (WebView) view.findViewById(R.id.web_view);
        calendar.loadUrl(url);
        calendar.setWebViewClient(new CalendarWebViewClient());
        WebSettings webViewSettings = calendar.getSettings();
        webViewSettings.setJavaScriptEnabled(true);

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
