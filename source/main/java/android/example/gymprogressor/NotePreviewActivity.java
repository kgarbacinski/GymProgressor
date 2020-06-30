package android.example.gymprogressor;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.applandeo.materialcalendarview.EventDay;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotePreviewActivity extends AppCompatActivity {
    private Object event;
    private Button confirmNoteButton;
    FloatingActionButton editButton, deleteButton;

    private TextView exerciseName, setsNumber, repsNumber, breakValue, weightValue;
    private LinearLayout parentLinearLayout;

    private String unit;

    public static String getFormattedDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        return simpleDateFormat.format(date);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SessionManager sessionManager = new SessionManager(this);
        unit = sessionManager.getUserUnit();

        /* For alert */
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);

        builder.setTitle("Confirm");
        builder.setMessage("Are you sure you want to delete?");

        builder.setPositiveButton("NO", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("YES", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing but close the dialog
                deleteNote(event);
            }
        });

        setContentView(R.layout.activity_note_preview);

        parentLinearLayout = findViewById(R.id.parent_linear_layout);

        Intent intent = getIntent();
        editButton = findViewById(R.id.editButton);
        deleteButton = findViewById(R.id.deleteButton);
        confirmNoteButton = findViewById(R.id.confirmNoteButton);

        if (intent != null) {
            event = intent.getParcelableExtra(SheduleActivity.EVENT);

            if (event instanceof MyEventDay) {
                MyEventDay myEventDay = (MyEventDay) event;
                //getSupportActionBar().setTitle(getFormattedDate(myEventDay.getCalendar().getTime()));

                String noteJSON = myEventDay.getNote();
                if (noteJSON != null) {
                    initTable(noteJSON);
                }
                else {
                    editNote(event);
                }

                editButton.show();
                deleteButton.show();

            } else if (event instanceof EventDay) {
                EventDay eventDay = (EventDay) event;
                getSupportActionBar().setTitle(getFormattedDate(eventDay.getCalendar().getTime()));
                //floatingActionButton.hide();
            }
        }

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editNote(event);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        confirmNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (event instanceof MyEventDay) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra(SheduleActivity.RESULT, (MyEventDay) event);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            }
        });

    }

    public void initTable(String noteJSON) {
        parentLinearLayout.removeAllViewsInLayout();

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Gson gson = new Gson();
        List<List<String>> noteList = gson.fromJson(noteJSON, MyEventDay.LIST_TYPE_CALENDAR);

        /* No. (exercise, sets, reps, break) */
        for (int i = 0; i < noteList.size(); i++) {
            View rowView = inflater.inflate(R.layout.previewfield_skeleton, null);

            exerciseName = rowView.findViewById(R.id.exercise_label);
            setsNumber = rowView.findViewById(R.id.sets_label);
            repsNumber = rowView.findViewById(R.id.reps_label);
            breakValue = rowView.findViewById(R.id.break_label);
            weightValue = rowView.findViewById(R.id.weight_label);

            if (noteList.get(i).get(0).isEmpty()) exerciseName.setText("Ns.");
            else exerciseName.setText(noteList.get(i).get(0));

            if (noteList.get(i).get(1).isEmpty()) setsNumber.setText("Ns.");
            else setsNumber.setText(noteList.get(i).get(1));

            if (noteList.get(i).get(2).isEmpty()) repsNumber.setText("Ns.");
            else repsNumber.setText(noteList.get(i).get(2));

            if (noteList.get(i).get(3).isEmpty()) breakValue.setText("Ns.");
            else breakValue.setText(noteList.get(i).get(3) + " sec");

            if(noteList.get(i).get(4).isEmpty()) weightValue.setText("Ns.");
            else weightValue.setText(noteList.get(i).get(4) + " " + unit);

            parentLinearLayout.addView(rowView, i);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 45 && resultCode == RESULT_OK) {
            event = data.getParcelableExtra(SheduleActivity.RESULT);

            if (event instanceof MyEventDay) {
                deleteButton.hide();

                //Show Confirm Button
                confirmNoteButton.setVisibility(View.VISIBLE);

                MyEventDay myEventDay = (MyEventDay) event;

                String noteJSON;
                noteJSON = myEventDay.getNote();

                if (noteJSON != null) {
                    initTable(noteJSON);
                } else {
                }
            }
        }
    }

    private void editNote(Object event) {
        Intent newIntent = new Intent(this, EditNoteActivity.class);
        if (event instanceof MyEventDay) {
            newIntent.putExtra(SheduleActivity.EVENT, (MyEventDay) event);
        }

        //Call EditNoteActivity
        startActivityForResult(newIntent, SheduleActivity.EDIT_NOTE);
    }

    private void deleteNote(Object event) {
        if (event instanceof MyEventDay) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra(SheduleActivity.RESULT, (MyEventDay) event);
            setResult(Activity.RESULT_FIRST_USER, returnIntent);
            finish();
        }
    }
}