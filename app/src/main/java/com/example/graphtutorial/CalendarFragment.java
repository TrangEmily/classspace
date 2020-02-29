package com.example.graphtutorial;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.microsoft.graph.concurrency.ICallback;
import com.microsoft.graph.core.ClientException;
import com.microsoft.graph.models.extensions.DateTimeTimeZone;
import com.microsoft.graph.models.extensions.EmailAddress;
import com.microsoft.graph.models.extensions.Event;
import com.microsoft.graph.models.extensions.Recipient;
import com.microsoft.graph.requests.extensions.IEventCollectionPage;
import com.microsoft.identity.client.AuthenticationCallback;
import com.microsoft.identity.client.IAuthenticationResult;
import com.microsoft.identity.client.exception.MsalException;

import java.util.Arrays;
import java.util.List;

public class CalendarFragment extends Fragment {

    private List<Event> mEventList = null;
    private ProgressBar mProgress = null;

    private List<Event> getManualEvents() {
        // Fake data
        Event e1 = new Event();
        e1.start = new DateTimeTimeZone();
        e1.start.dateTime = "2020-03-02T12:00:00.0000000";
        e1.start.timeZone = "PST";
        e1.end = new DateTimeTimeZone();
        e1.end.dateTime = "2020-03-02T13:00:00.0000000";
        e1.end.timeZone = "PST";
        e1.subject="Math";
        e1.organizer = new Recipient();
        e1.organizer.emailAddress = new EmailAddress();
        e1.organizer.emailAddress.name = "Trang Emily";

        Event e2 = new Event();
        e2.start = new DateTimeTimeZone();
        e2.start.dateTime = "2020-03-02T15:00:00.0000000";
        e2.start.timeZone = "PST";
        e2.end = new DateTimeTimeZone();
        e2.end.dateTime = "2020-03-02T16:00:00.0000000";
        e2.end.timeZone = "PST";
        e2.subject="Social Studies";
        e2.organizer = new Recipient();
        e2.organizer.emailAddress = new EmailAddress();
        e2.organizer.emailAddress.name = "Trang Emily";

        return Arrays.asList(e1,e2);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Get a current access token
        AuthenticationHelper.getInstance()
                .acquireTokenSilently(new AuthenticationCallback() {
                    @Override
                    public void onSuccess(IAuthenticationResult authenticationResult) {
                        final GraphHelper graphHelper = GraphHelper.getInstance();

                        // Get the user's events
                        graphHelper.getEvents(authenticationResult.getAccessToken(),
                                getEventsCallback());
                    }

                    @Override
                    public void onError(MsalException exception) {
                        Log.e("AUTH", "Could not get token silently", exception);
                        // Fake Event
                        mEventList = getManualEvents();
                        addEventsToList();

                        // Temporary for debugging
                        String jsonEvents = GraphHelper.getInstance().serializeObject(mEventList);
                        Log.d("GRAPH", jsonEvents);

                        hideProgressBar();
                    }

                    @Override
                    public void onCancel() {
                        hideProgressBar();
                    }
                });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mProgress = getActivity().findViewById(R.id.progressbar);
        showProgressBar();
    }

    private void showProgressBar() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgress.setVisibility(View.VISIBLE);
            }
        });
    }

    private void hideProgressBar() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgress.setVisibility(View.GONE);
            }
        });
    }

    private ICallback<IEventCollectionPage> getEventsCallback() {
        return new ICallback<IEventCollectionPage>() {
            @Override
            public void success(IEventCollectionPage iEventCollectionPage) {
                mEventList = iEventCollectionPage.getCurrentPage();
                addEventsToList();

                // Temporary for debugging
                String jsonEvents = GraphHelper.getInstance().serializeObject(mEventList);
                Log.d("GRAPH", jsonEvents);

                hideProgressBar();
            }

            @Override
            public void failure(ClientException ex) {
                Log.e("GRAPH", "Error getting events", ex);
                hideProgressBar();
            }
        };
    }

    private void addEventsToList() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ListView eventListView = getView().findViewById(R.id.eventlist);

                EventListAdapter listAdapter = new EventListAdapter(getActivity(),
                        R.layout.event_list_item, mEventList);

                eventListView.setAdapter(listAdapter);
            }
        });
    }
}
