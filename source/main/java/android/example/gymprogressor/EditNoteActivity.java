package android.example.gymprogressor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class EditNoteActivity extends AppCompatActivity {
    private LinearLayout parentLinearLayout;
    private EditText exerciseName, setsNumber, repsNumber, breakValue, weightValue;

    private MyEventDay myEventDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        parentLinearLayout = findViewById(R.id.parent_linear_layout);

        Intent intent = getIntent();

        if (intent != null) {
            Object event = intent.getParcelableExtra(SheduleActivity.EVENT);
            if (event instanceof MyEventDay) {
                myEventDay = (MyEventDay) event;
                String noteJSON = myEventDay.getNote();

                if (noteJSON != null) {
                    initTable(noteJSON);
                } else {
                }
            }
        }
    }

    public void onAddField(View v) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.editfield_skeleton, null);
        parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount());
    }


    public void onRemoveField(View v) {
        parentLinearLayout.removeView((View) v.getParent().getParent());
    }

    private void initTable(String noteJSON) {
        parentLinearLayout.removeAllViewsInLayout();

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Gson gson = new Gson();
        List<List<String>> noteList = gson.fromJson(noteJSON, MyEventDay.LIST_TYPE_CALENDAR);
        /* No. (exercise, sets, reps, break) */
        for (int i = 0; i < noteList.size(); i++) {
            final View rowView = inflater.inflate(R.layout.editfield_skeleton, null);

            exerciseName = rowView.findViewById(R.id.exercise_input);
            setsNumber = rowView.findViewById(R.id.sets_input);
            repsNumber = rowView.findViewById(R.id.reps_input);
            breakValue = rowView.findViewById(R.id.break_input);
            weightValue = rowView.findViewById(R.id.weight_input);

            exerciseName.setText(noteList.get(i).get(0));
            setsNumber.setText(noteList.get(i).get(1));
            repsNumber.setText(noteList.get(i).get(2));
            breakValue.setText(noteList.get(i).get(3));
            weightValue.setText(noteList.get(i).get(4));

            parentLinearLayout.addView(rowView, i);
        }
    }

    public void onSaveField(View v) {
        List<List<String>> noteList = new ArrayList<>();
        for (int i = 0; i < parentLinearLayout.getChildCount(); i++) {
            View view = parentLinearLayout.getChildAt(i);

            exerciseName = view.findViewById(R.id.exercise_input);
            setsNumber = view.findViewById(R.id.sets_input);
            repsNumber = view.findViewById(R.id.reps_input);
            breakValue = view.findViewById(R.id.break_input);
            weightValue = view.findViewById(R.id.weight_input);

            List<String> buf = new ArrayList<>();
            buf.add(exerciseName.getText().toString());
            buf.add(setsNumber.getText().toString());
            buf.add(repsNumber.getText().toString());
            buf.add(breakValue.getText().toString());
            buf.add(weightValue.getText().toString());

            noteList.add(buf);
        }


        Gson gson = new Gson();
        String noteJSON = gson.toJson(noteList, MyEventDay.LIST_TYPE_CALENDAR);

        myEventDay.setNote(noteJSON);
        Intent returnIntent = new Intent();
        returnIntent.putExtra(SheduleActivity.RESULT, myEventDay);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();

    }

}