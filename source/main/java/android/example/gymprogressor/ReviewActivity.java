package android.example.gymprogressor;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class ReviewActivity extends Fragment {
    private TextView genderField, weightField, ageField;

    private int numberLabels = 4;

    private final String URL_STANDARDS = "https://gymprogressor.000webhostapp.com/app/getStandards.php";

    private SessionManager sessionManager;
    private HashMap<String, String> userProfile;
    private LinearLayout parentLinearLayout;

    private ProgressBar mLoadingBar;

    private String genderValue, weightValue, ageValue;
    private String unit;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* Create a session */
        sessionManager = new SessionManager(ReviewActivity.this.getContext());
        userProfile = sessionManager.getProfile();
        unit = userProfile.get(Constants.UNIT);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_review, container, false);

        /* View */
        mLoadingBar = view.findViewById(R.id.loadingBar);
        genderField = view.findViewById(R.id.gender_field);
        weightField = view.findViewById(R.id.weight_field);
        ageField = view.findViewById(R.id.age_field);

        parentLinearLayout = view.findViewById(R.id.parent_linear_layout);

        /*Set header labels */
        genderValue = userProfile.get(Constants.GENDER);
        weightValue = userProfile.get(Constants.WEIGHT);
        ageValue = userProfile.get(Constants.AGE);

        if (genderValue != null) {
            if (genderValue.equals("Male")) {
                genderField.setText("Male");
            } else if (genderValue.equals("Female")) genderField.setText("Female");
            else {
                genderField.setText("Not Set");
            }
        } else genderField.setText("Not Set");

        if (weightValue != null) {
            if (!weightValue.trim().isEmpty()) {
                weightField.setText(weightValue + " " + unit);
            } else weightField.setText("Not Set");
        } else weightField.setText("Not Set");

        if (ageValue != null) {
            if (!ageValue.trim().isEmpty()) {
                ageField.setText(ageValue);
            } else ageField.setText("Not Set");
        } else ageField.setText("Not Set");

        getTable();

        return view;
    }

    private void getTable() {
        mLoadingBar.setVisibility(View.VISIBLE);

        if (genderValue != null && weightValue != null) {
            HashMap<String, HashMap<String, Double>> standardsMap = new HashMap<>();

            /* Get standards besides db standards */
            if (Double.parseDouble(weightValue) < 55.0 && genderValue.equals("Male")) {
                HashMap<String, Double> standardsBuf = new HashMap<>();

                standardsBuf.put("beginner", 0.48);
                standardsBuf.put("novice", 0.77);
                standardsBuf.put("intermediate", 1.13);
                standardsBuf.put("advanced", 1.57);
                standardsBuf.put("elite", 2.06);

                standardsMap.put("bp", standardsBuf);

                standardsBuf.put("beginner", 0.67);
                standardsBuf.put("novice", 1.04);
                standardsBuf.put("intermediate", 1.52);
                standardsBuf.put("advanced", 2.09);
                standardsBuf.put("elite", 2.71);

                standardsMap.put("squat", standardsBuf);

                standardsBuf.put("beginner", 0.87);
                standardsBuf.put("novice", 1.31);
                standardsBuf.put("intermediate", 1.85);
                standardsBuf.put("advanced", 2.5);
                standardsBuf.put("elite", 3.2);

                standardsMap.put("deadlift", standardsBuf);

                String standardsMapJSON = new Gson().toJson(standardsMap, Constants.MAP_TYPE_STANDARDS);
                sessionManager.addStandardsMap(standardsMapJSON);

                setLabels();
            }
            else if (Double.parseDouble(weightValue) >= 140.0 && genderValue.equals("Male")) {
                HashMap<String, Double> standardsBuf = new HashMap<>();

                standardsBuf.put("beginner", 0.73);
                standardsBuf.put("novice", 0.93);
                standardsBuf.put("intermediate", 1.16);
                standardsBuf.put("advanced", 1.42);
                standardsBuf.put("elite", 1.69);

                standardsMap.put("bp", standardsBuf);

                standardsBuf.put("beginner", 0.97);
                standardsBuf.put("novice", 1.22);
                standardsBuf.put("intermediate", 1.52);
                standardsBuf.put("advanced", 1.85);
                standardsBuf.put("elite", 2.19);

                standardsMap.put("squat", standardsBuf);

                standardsBuf.put("beginner", 1.11);
                standardsBuf.put("novice", 1.39);
                standardsBuf.put("intermediate", 1.71);
                standardsBuf.put("advanced", 2.07);
                standardsBuf.put("elite", 2.44);

                standardsMap.put("deadlift", standardsBuf);
                String standardsMapJSON = new Gson().toJson(standardsMap, Constants.MAP_TYPE_STANDARDS);
                sessionManager.addStandardsMap(standardsMapJSON);

                setLabels();
            }
            else if (Double.parseDouble(weightValue) < 45.0 && genderValue.equals("Female")) {
                HashMap<String, Double> standardsBuf = new HashMap<>();

                standardsBuf.put("beginner", 0.21);
                standardsBuf.put("novice", 0.45);
                standardsBuf.put("intermediate", 0.79);
                standardsBuf.put("advanced", 1.24);
                standardsBuf.put("elite", 1.76);

                standardsMap.put("bp", standardsBuf);

                standardsBuf.put("beginner", 0.43);
                standardsBuf.put("novice", 0.79);
                standardsBuf.put("intermediate", 1.27);
                standardsBuf.put("advanced", 1.86);
                standardsBuf.put("elite", 2.54);

                standardsMap.put("squat", standardsBuf);

                standardsBuf.put("beginner", 0.59);
                standardsBuf.put("novice", 1.0);
                standardsBuf.put("intermediate", 1.55);
                standardsBuf.put("advanced", 2.22);
                standardsBuf.put("elite", 2.96);

                standardsMap.put("deadlift", standardsBuf);

                String standardsMapJSON = new Gson().toJson(standardsMap, Constants.MAP_TYPE_STANDARDS);
                sessionManager.addStandardsMap(standardsMapJSON);

                setLabels();
            }
            else if (Double.parseDouble(weightValue) >= 120.0 && genderValue.equals("Female")) {
                HashMap<String, Double> standardsBuf = new HashMap<>();

                standardsBuf.put("beginner", 0.31);
                standardsBuf.put("novice", 0.46);
                standardsBuf.put("intermediate", 0.65);
                standardsBuf.put("advanced", 0.88);
                standardsBuf.put("elite", 1.12);

                standardsMap.put("bp", standardsBuf);

                standardsBuf.put("beginner", 0.47);
                standardsBuf.put("novice", 0.66);
                standardsBuf.put("intermediate", 0.91);
                standardsBuf.put("advanced", 1.19);
                standardsBuf.put("elite", 1.49);

                standardsMap.put("squat", standardsBuf);

                standardsBuf.put("beginner", 0.55);
                standardsBuf.put("novice", 0.77);
                standardsBuf.put("intermediate", 1.04);
                standardsBuf.put("advanced", 1.34);
                standardsBuf.put("elite", 1.67);

                standardsMap.put("deadlift", standardsBuf);

                String standardsMapJSON = new Gson().toJson(standardsMap, Constants.MAP_TYPE_STANDARDS);
                sessionManager.addStandardsMap(standardsMapJSON);

                setLabels();
            }
            else {
                /* Set standards */
                StringRequest request = new StringRequest(Request.Method.POST, URL_STANDARDS,
                        response -> {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");

                                if (success.equals("1")) {
                                    /* Bench press */
                                    JSONArray bp = jsonObject.getJSONArray("bp");

                                    if (bp != null) {
                                        for (int i = 0; i < bp.length(); i++) {
                                            JSONObject object = bp.getJSONObject(i);

                                            HashMap<String, Double> standardsBp = new HashMap<>();

                                            standardsBp.put("beginner", (double) object.getDouble("beginner"));
                                            standardsBp.put("novice", (double) object.getDouble("novice"));
                                            standardsBp.put("intermediate", (double) object.getDouble("intermediate"));
                                            standardsBp.put("advanced", (double) object.getDouble("advanced"));
                                            standardsBp.put("elite", (double) object.getDouble("elite"));

                                            standardsMap.put("bp", standardsBp);
                                        }
                                    }

                                    /* Squat */
                                    JSONArray squat = jsonObject.getJSONArray("squat");

                                    if (squat != null) {
                                        for (int i = 0; i < squat.length(); i++) {
                                            JSONObject object = squat.getJSONObject(i);

                                            HashMap<String, Double> standardsSquat = new HashMap<>();

                                            standardsSquat.put("beginner", (double) object.getDouble("beginner"));
                                            standardsSquat.put("novice", (double) object.getDouble("novice"));
                                            standardsSquat.put("intermediate", (double) object.getDouble("intermediate"));
                                            standardsSquat.put("advanced", (double) object.getDouble("advanced"));
                                            standardsSquat.put("elite", (double) object.getDouble("elite"));

                                            standardsMap.put("squat", standardsSquat);
                                        }
                                    }

                                    /* Dead Lift */
                                    JSONArray dl = jsonObject.getJSONArray("deadlift");

                                    if (dl != null) {
                                        for (int i = 0; i < dl.length(); i++) {
                                            JSONObject object = dl.getJSONObject(i);

                                            HashMap<String, Double> standardsDeadlift = new HashMap<>();

                                            standardsDeadlift.put("beginner", (double) object.getDouble("beginner"));
                                            standardsDeadlift.put("novice", (double) object.getDouble("novice"));
                                            standardsDeadlift.put("intermediate", (double) object.getDouble("intermediate"));
                                            standardsDeadlift.put("advanced", (double) object.getDouble("advanced"));
                                            standardsDeadlift.put("elite", (double) object.getDouble("elite"));

                                            standardsMap.put("deadlift", standardsDeadlift);
                                        }
                                    }

                                    String standardsMapJSON = new Gson().toJson(standardsMap, Constants.MAP_TYPE_STANDARDS);
                                    sessionManager.addStandardsMap(standardsMapJSON);

                                    /* Create layout */
                                    mLoadingBar.setVisibility(View.GONE);
                                    setLabels();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }, error -> { mLoadingBar.setVisibility(View.GONE);
                                    Toast.makeText(this.getContext(), "No Internet connection!", Toast.LENGTH_LONG).show();}) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();

                        params.put("gender", genderValue);
                        params.put("weight", weightValue);

                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(this.getContext());
                requestQueue.add(request);
            }
        }
    }

    private void setLabels() {
        parentLinearLayout.removeAllViews();

        /* Get user's strength*/
        Map<Integer, String> strengthMap = new HashMap<>();
        strengthMap = sessionManager.getStrengthMeasurement();


        HashMap<String, HashMap<String, Double>> standardsMap = sessionManager.getStandardsMap();
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(strengthMap == null){
            for(int i = 0; i < 3; i++){
                final View rowView = inflater.inflate(R.layout.review_label, null);

                TextView exerciseName = rowView.findViewById(R.id.exercise_name);
                TextView maxField = rowView.findViewById(R.id.max_field);

                if(i==0) exerciseName.setText("Official " + genderValue + " Squat Standard");
                if(i==1) exerciseName.setText("Official " + genderValue + " Deadlift Standard");
                if(i==2) exerciseName.setText("Official " + genderValue + " Bench Press Standard");

                maxField.setText("Not Set");
                parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount());
            }
        }

        else if (standardsMap != null && strengthMap != null) {
            for (int i = 0; i < numberLabels; i++) {
                final View rowView = inflater.inflate(R.layout.review_label, null);

                TextView exerciseName = rowView.findViewById(R.id.exercise_name);
                TextView levelField = rowView.findViewById(R.id.level_field);
                TextView promotionField = rowView.findViewById(R.id.promotion_field);
                TextView maxField = rowView.findViewById(R.id.max_field);

                /*Squat*/
                if(i==0){
                    exerciseName.setText("Official " + genderValue + " Squat Standard");

                    if (strengthMap.get(0) != null) {
                        if (!strengthMap.get(0).isEmpty()) {
                            maxField.setText(strengthMap.get(0) + " " + unit);

                            double weightValueD = Double.parseDouble(weightValue);
                            double squatValue = Double.parseDouble(strengthMap.get(0));

                            //* Beginner *//
                            if ((squatValue / weightValueD < standardsMap.get("squat").get("novice"))) {

                                levelField.setText("Beginner");
                                promotionField.setText("+" + ((int) (standardsMap.get("squat").get("novice") * weightValueD - squatValue)) + " " + unit);
                            }
                            //* Novice *//
                            if ((squatValue / weightValueD >= standardsMap.get("squat").get("novice") &&
                                    squatValue / weightValueD < standardsMap.get("squat").get("intermediate"))) {

                                levelField.setText("Novice");
                                promotionField.setText("+" + (int) (standardsMap.get("squat").get("intermediate") * weightValueD - squatValue) + " " + unit);
                            }
                            //* Intermediate *//
                            if ((squatValue / weightValueD >= standardsMap.get("squat").get("intermediate") &&
                                    squatValue / weightValueD < standardsMap.get("squat").get("advanced"))) {

                                levelField.setText("Intermediate");
                                promotionField.setText("+" + (int) ((standardsMap.get("squat").get("advanced") * weightValueD) - squatValue) + " " + unit);
                            }
                            //*Advanced*//
                            if ((squatValue / weightValueD >= standardsMap.get("squat").get("advanced") &&
                                    squatValue / weightValueD < standardsMap.get("squat").get("elite"))) {

                                levelField.setText("Advanced");
                                promotionField.setText("+" + (int) (standardsMap.get("squat").get("elite") * weightValueD - squatValue) + " " + unit);
                            }
                            //* Elite *//
                            if ((squatValue / weightValueD >= standardsMap.get("squat").get("elite"))) {

                                levelField.setText("Elite");
                                promotionField.setText("Max");
                            }

                        } else maxField.setText("Not set");
                    } else maxField.setText("Not set");
                }

                /*Dead Lift*/
                if (i == 2) {
                    exerciseName.setText("Official " + genderValue + " Deadlift Standard");

                    if (strengthMap.get(2) != null) {
                        if (!strengthMap.get(2).isEmpty()) {
                            maxField.setText(strengthMap.get(2) + " " + unit);

                            double weightValueD = Double.parseDouble(weightValue);
                            double bpValue = Double.parseDouble(strengthMap.get(2));

                            //* Beginner *//
                            if ((bpValue / weightValueD < standardsMap.get("deadlift").get("novice"))) {

                                levelField.setText("Beginner");
                                promotionField.setText("+" + ((int) (standardsMap.get("deadlift").get("novice") * weightValueD - bpValue)) + " " + unit);
                            }
                            //* Novice *//
                            if ((bpValue / weightValueD >= standardsMap.get("deadlift").get("novice") &&
                                    bpValue / weightValueD < standardsMap.get("deadlift").get("intermediate"))) {

                                levelField.setText("Novice");
                                promotionField.setText("+" + (int) (standardsMap.get("deadlift").get("intermediate") * weightValueD - bpValue) + " " + unit);
                            }
                            //* Intermediate *//
                            if ((bpValue / weightValueD >= standardsMap.get("deadlift").get("intermediate") &&
                                    (bpValue / weightValueD) < standardsMap.get("deadlift").get("advanced"))) {

                                levelField.setText("Intermediate");
                                promotionField.setText("+" + (int) ((standardsMap.get("deadlift").get("advanced") * weightValueD) - bpValue) + " " + unit);
                            }
                            //*Advanced*//
                            if ((bpValue / weightValueD >= standardsMap.get("deadlift").get("advanced") &&
                                    bpValue / weightValueD < standardsMap.get("deadlift").get("elite"))) {

                                levelField.setText("Advanced");
                                promotionField.setText("+" + (int) (standardsMap.get("deadlift").get("elite") * weightValueD - bpValue) + " " + unit);
                            }
                            //* Elite *//
                            if ((bpValue / weightValueD >= standardsMap.get("deadlift").get("elite"))) {

                                levelField.setText("Elite");
                                promotionField.setText("Max");
                            }

                        } else maxField.setText("Not set");
                    } else maxField.setText("Not set");

                }

                /*Benchpress*/
                if (i == 3) {
                    exerciseName.setText("Official " + genderValue + " Bench Press Standard");

                    if (strengthMap.get(3) != null) {
                        if (!strengthMap.get(3).isEmpty()) {
                            maxField.setText(strengthMap.get(3) + " " + unit);

                            double weightValueD = Double.parseDouble(weightValue);
                            double bpValue = Double.parseDouble(strengthMap.get(3));

                            //* Beginner *//
                            if ((bpValue / weightValueD < standardsMap.get("bp").get("novice"))) {

                                levelField.setText("Beginner");
                                promotionField.setText("+" + ((int) (standardsMap.get("bp").get("novice") * weightValueD - bpValue)) + " " + unit);
                            }
                            //* Novice *//
                            if ((bpValue / weightValueD >= standardsMap.get("bp").get("novice") &&
                                    bpValue / weightValueD < standardsMap.get("bp").get("intermediate"))) {

                                levelField.setText("Novice");
                                promotionField.setText("+" + (int) (standardsMap.get("bp").get("intermediate") * weightValueD - bpValue) + " " + unit);
                            }
                            //* Intermediate *//
                            if ((bpValue / weightValueD >= standardsMap.get("bp").get("intermediate") &&
                                    (bpValue / weightValueD) < standardsMap.get("bp").get("advanced"))) {

                                levelField.setText("Intermediate");
                                promotionField.setText("+" + (int) ((standardsMap.get("bp").get("advanced") * weightValueD) - bpValue) + " " + unit);
                            }
                            //*Advanced*//
                            if ((bpValue / weightValueD >= standardsMap.get("bp").get("advanced") &&
                                    bpValue / weightValueD < standardsMap.get("bp").get("elite"))) {

                                levelField.setText("Advanced");
                                promotionField.setText("+" + (int) (standardsMap.get("bp").get("elite") * weightValueD - bpValue) + " " + unit);
                            }
                            //* Elite *//
                            if ((bpValue / weightValueD >= standardsMap.get("bp").get("elite"))) {

                                levelField.setText("Elite");
                                promotionField.setText("Max");
                            }

                        } else maxField.setText("Not set");
                    } else maxField.setText("Not set");

                }

                if (i==0 || i==3 || i == 2)
                    parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount());
            }
        }

    }
}