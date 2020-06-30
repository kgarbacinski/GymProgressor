package android.example.gymprogressor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddNoteActivity extends AppCompatActivity {
   /* private static final Type LIST_EVENTS_TYPE = new TypeToken<List<EventDay>>() {
    }.getType();*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*Intent intent = getIntent();
        String eventDaysJSON = intent.getStringExtra(SheduleActivity.EVENTS_JSON);
        ArrayList<EventDay> mEventDaysToDisable = new Gson().fromJson(eventDaysJSON, LIST_EVENTS_TYPE);*/


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        final CalendarView datePicker =  findViewById(R.id.datePicker);
       // datePicker.setEvents(mEventDaysToDisable);

        Button button = (Button) findViewById(R.id.addNoteButton);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();

                MyEventDay myEventDay = new MyEventDay(datePicker.getSelectedDate(),
                        R.drawable.ic_message_black, null);

                returnIntent.putExtra(SheduleActivity.RESULT, myEventDay);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
    }

}
