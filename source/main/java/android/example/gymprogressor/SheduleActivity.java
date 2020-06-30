package android.example.gymprogressor;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_FIRST_USER;
import static android.app.Activity.RESULT_OK;
import static java.util.Calendar.DAY_OF_YEAR;

public class SheduleActivity extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener {
    private boolean isClicked = false;

    public static final String RESULT = "result";
    public static final String EVENT = "event";
    public static final int EDIT_NOTE = 45;
    private static final int ADD_NOTE = 44;
    private static final int DELETE_NOTE = 46;
    public static final String EVENTS_JSON = "event_json";
    private CalendarView mCalendarView;
    private List<EventDay> mEventDays;
    private FloatingActionButton mFloatingButton;

    private ProgressBar mLoadingBar;

    private AppBarLayout mToolBarLayout;

    private SessionManager sessionManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mEventDays = new ArrayList<>();

        /* Create a Session Manager */
        sessionManager = new SessionManager(SheduleActivity.this.getContext());

        HashMap<String, List<Long>> timeMap = sessionManager.getTimeDetail();
        HashMap<String, List<List<List<String>>>> noteMap = sessionManager.getNoteDetail();

        if (timeMap != null && noteMap != null) {
            List<Long> timeList = timeMap.get(Constants.TIME_LIST);
            List<List<List<String>>> noteList = noteMap.get(Constants.NOTE_LIST);

            for (int i = 0; i < timeList.size(); i++) {
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(timeList.get(i));

                if (noteList.get(i) == null) {
                    mEventDays.add(new MyEventDay(cal, R.drawable.ic_message_black, null));
                } else if (noteList.get(i).isEmpty()) {
                    mEventDays.add(new MyEventDay(cal, R.drawable.ic_message_black, null));
                } else {
                    mEventDays.add(new MyEventDay(cal, R.drawable.ic_message_black, new Gson().toJson(noteList.get(i))));
                }
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        isClicked = false;

        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mEventDays != null) {
            if (mEventDays.size() == 0) {
                sessionManager.updateCalendarList(null, null);
            } else {
                //update List
                List<Long> timeList = new ArrayList<>(); // Conatins time of notes
                List<List<List<String>>> noteList = new ArrayList<>(); // Contains single note as json

                List<MyEventDay> convertedList = new ArrayList<>();
                for (int i = 0; i < mEventDays.size(); i++) {
                    convertedList.add((MyEventDay) (mEventDays.get(i))); // convert to MyEventDay
                    timeList.add(convertedList.get(i).getCalendar().getTimeInMillis());

                    String noteJSON = convertedList.get(i).getNote();
                    //new Gson().fromJson(noteJSON, MyEventDay.LIST_TYPE_CALENDAR); // List<List<String>>

                    noteList.add(new Gson().fromJson(noteJSON, MyEventDay.LIST_TYPE_CALENDAR)); // List<List<String>>);
                }
                sessionManager.updateCalendarList(timeList, noteList);

                /* Update no trainings */
                String noTrainings = Integer.toString(mEventDays.size());
                sessionManager.updateNoTrainings(noTrainings);
            }

            StringRequest request = sessionManager.getCalendarRequest();
            RequestQueue requestQueue = Volley.newRequestQueue(SheduleActivity.this.getContext());
            requestQueue.add(request);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_shedule, container, false);

        /* Set Dates in CalendarView */
        mCalendarView = view.findViewById(R.id.calendarView);
        mCalendarView.setEvents(mEventDays);

        mFloatingButton =  view.findViewById(R.id.floatingActionButton);
        mLoadingBar = view.findViewById(R.id.loading_bar);

        mFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkConnection()) addNote();
                else {
                }
            }
        });

        mCalendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                if(!isClicked) { isClicked = true; previewNote(eventDay);}
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //For AddNoteActivity
        if (requestCode == ADD_NOTE && resultCode == RESULT_OK) {
            MyEventDay myEventDay = data.getParcelableExtra(RESULT);
            mCalendarView.setDate(myEventDay.getCalendar());

            addEvent(myEventDay);

            mCalendarView.setEvents(mEventDays);
        }

        //For NotePreviewActivity
        else if (requestCode == EDIT_NOTE && resultCode == RESULT_OK) {
            MyEventDay myEventDay = data.getParcelableExtra(RESULT);
            mCalendarView.setDate(myEventDay.getCalendar());
            addEvent(myEventDay);

            mCalendarView.setEvents(mEventDays);
        }

        //For NotePreviewActivity
        else if (requestCode == EDIT_NOTE && resultCode == RESULT_FIRST_USER) {
            MyEventDay myEventDay = data.getParcelableExtra(RESULT);
            mCalendarView.setDate(myEventDay.getCalendar());
            deleteEvent(myEventDay);

            mCalendarView.setEvents(mEventDays);
        }
    }

    // Method to manually check connection status
    private boolean checkConnection() {
        boolean isConnected = (ConnectivityReceiver.isConnected());
        showSnack(isConnected);

        return isConnected;
    }

    private void addEvent(MyEventDay myEventDay) {
        for (int i = 0; i < mEventDays.size(); i++) {
            if (mEventDays.get(i).getCalendar().get(Calendar.YEAR) == myEventDay.getCalendar().get(Calendar.YEAR) &&
                    mEventDays.get(i).getCalendar().get(DAY_OF_YEAR) == myEventDay.getCalendar().get(DAY_OF_YEAR)) {

                mEventDays.set(i, myEventDay);
                return;
            }
        }
        mEventDays.add(myEventDay);
    }

    private void deleteEvent(MyEventDay myEventDay) {
        for (int i = 0; i < mEventDays.size(); i++) {
            if (mEventDays.get(i).getCalendar().get(Calendar.YEAR) == myEventDay.getCalendar().get(Calendar.YEAR) &&
                    mEventDays.get(i).getCalendar().get(DAY_OF_YEAR) == myEventDay.getCalendar().get(DAY_OF_YEAR)) {

                mEventDays.remove(i);
            }
        }
    }

   /* private String convertEventsToJson(){
        return new Gson().toJson(mEventDays);

    }*/

    private void addNote() {
        Intent intent = new Intent(SheduleActivity.this.getActivity(), AddNoteActivity.class);
        //intent.putExtra(EVENTS_JSON, convertEventsToJson());
        startActivityForResult(intent, ADD_NOTE);
    }

    private void previewNote(EventDay eventDay) {
        Intent intent = new Intent(SheduleActivity.this.getActivity(), NotePreviewActivity.class);
        if (eventDay instanceof MyEventDay) {
            intent.putExtra(EVENT, (MyEventDay) eventDay);
        }
        startActivityForResult(intent, EDIT_NOTE);
    }


    // Showing the status in Snackbar
    private void showSnack(boolean isConnected) {
        if (!isConnected) {
            String message = "";
            int color = Color.RED;
            message = "Not connected to the Internet!";
            Snackbar snackbar = Snackbar
                    .make(requireView(), message, Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(com.google.android.material.R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
        } else {
        }
    }

    /**
     * Callback will be triggered when there is change in
     * network connection
     */
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

}
