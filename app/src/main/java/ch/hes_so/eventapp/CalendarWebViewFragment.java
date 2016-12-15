package ch.hes_so.eventapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class CalendarWebViewFragment extends Fragment {

    private String url = "https://calendar.google.com/calendar/embed?title=LAN%20Festigeek&showNav=0&showDate=0&showPrint=0&showTabs=0&showCalendars=0&showTz=0&mode=WEEK&height=600&wkst=2&hl=fr&bgcolor=%23FFFFFF&src=lan.festigeek%40gmail.com&color=%231B887A&src=o120cokuf4u6dg54ptcssr0k0g%40group.calendar.google.com&color=%232F6309&src=f2jbpa6gsigck7nf0015imk674%40group.calendar.google.com&color=%235C1158&src=f93vo2livmhsfib9lgo8cdtoh8%40group.calendar.google.com&color=%2329527A&src=lmpprsl9ui07n410e8en3t5jdg%40group.calendar.google.com&color=%23853104&src=jmldhmjj0f8rba73ifja32drp8%40group.calendar.google.com&color=%23AB8B00&ctz=Europe%2FZurich";

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
