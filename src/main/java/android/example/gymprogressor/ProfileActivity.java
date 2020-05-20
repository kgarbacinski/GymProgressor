package android.example.gymprogressor;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ProfileActivity extends Fragment implements AdapterView.OnItemSelectedListener {

    private final String URL_UPDATE_PROFIL = "https://gymprogressor.000webhostapp.com/app/updateProfile.php";

    private HashMap<String, String> userProfile;
    private SessionManager sessionManager;

    private ProgressBar mLoadingBar;
    private RelativeLayout mContainer;

    private TextView unit;
    private EditText nameField, emailField, ageField, weightField;
    private Button saveButton;
    private Spinner genderSpinner, unitSpinner;

    private ArrayAdapter<CharSequence> unitAdapter, genderAdapter;

    private String textGender, textUnit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Create a session */
        sessionManager = new SessionManager(ProfileActivity.this.getContext());
        userProfile = sessionManager.getProfile();

        /* Create an adapter for gender*/
        genderAdapter = ArrayAdapter.createFromResource(ProfileActivity.this.getContext(), R.array.gender, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        /* Create an adapter for unit */
        unitAdapter = ArrayAdapter.createFromResource(ProfileActivity.this.getContext(), R.array.unit, android.R.layout.simple_spinner_item);
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    }

    @Override
    public void onPause() {
        super.onPause();

        if (userProfile.size() > 0) {
            sessionManager.updateProfile(userProfile);
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_userprofile, container, false);

        /* View */
        genderSpinner = view.findViewById(R.id.gender_spinner);
        unitSpinner = view.findViewById(R.id.unit_spinner);
        nameField = view.findViewById(R.id.name_edit);
        emailField = view.findViewById(R.id.email_edit);
        unit = view.findViewById(R.id.unit);
        ageField = view.findViewById(R.id.age_edit);
        weightField = view.findViewById(R.id.weight_edit);
        weightField.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(4, 2)});

        mLoadingBar = view.findViewById(R.id.loadingBar);
        mContainer = view.findViewById(R.id.container);


        /* Set text for fields */
        nameField.setText(userProfile.get(Constants.NAME));
        emailField.setText(userProfile.get(Constants.EMAIL));
        unit.setText(userProfile.get(Constants.UNIT));

        textGender = userProfile.get(Constants.GENDER);
        textUnit = userProfile.get(Constants.UNIT);

        if (userProfile.get(Constants.AGE) != null) {
            if (!userProfile.get(Constants.AGE).isEmpty())
                ageField.setText(userProfile.get(Constants.AGE));
            else ageField.setHint("Not Set");
        } else ageField.setHint("Not Set");

        if (userProfile.get(Constants.WEIGHT) != null) {
            if (!userProfile.get(Constants.WEIGHT).isEmpty())
                weightField.setText(userProfile.get(Constants.WEIGHT));
            else weightField.setHint("Not Set");
        } else weightField.setHint("Not Set");


        /* Gender spinner */
        genderSpinner.setAdapter(genderAdapter);
        genderSpinner.setOnItemSelectedListener(this);
        genderSpinner.setSelection(getIndex(genderSpinner, textGender));

        /*Unit spinner */
        unitSpinner.setAdapter(unitAdapter);
        unitSpinner.setOnItemSelectedListener(this);
        unitSpinner.setSelection(getIndex(unitSpinner, textUnit));

        /* Save Button */
        saveButton = view.findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean ageOk = true;
                boolean weightOk = true;

                String age = (ageField.getText().toString());
                String weight = (weightField.getText().toString());

                age = age.replaceFirst("^0+(?!$)", "");
                weight = weight.replaceFirst("^0+(?!$)", "");

                if (age.isEmpty()) { ageField.setError("Cannot be empty!"); ageOk = false;}
                else if (Integer.parseInt(age) > 130 || Integer.parseInt(age) < 10) { ageField.setError("Invalid age!"); ageOk = false; }

                if (weight.isEmpty()) { weightField.setError("Cannot be empty!"); weightOk = false;}
                else if (Double.parseDouble(weight) > 300.0 || Double.parseDouble(weight) < 20.0){
                    weightField.setError("Invalid weight!");
                    weightOk = false;
                }

                if (ageOk && weightOk) {
                    userProfile.put(Constants.GENDER, textGender);
                    userProfile.put(Constants.UNIT, textUnit);
                    userProfile.put(Constants.AGE, age.trim());
                    userProfile.put(Constants.WEIGHT, weight.trim());

                    ageField.setText(age);
                    weightField.setText(weight);

                    //Change unit
                    unit.setText(userProfile.get(Constants.UNIT));

                    sessionManager.updateProfile(userProfile);
                    sendToDatabase();
                }
            }
        });
        return view;

    }

    public void sendToDatabase() {
        mLoadingBar.setVisibility(View.VISIBLE);
        mContainer.setVisibility(View.INVISIBLE);

        StringRequest request = new StringRequest(Request.Method.POST, URL_UPDATE_PROFIL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(ProfileActivity.this.getContext(), "Saved!", Toast.LENGTH_SHORT).show();

                        mLoadingBar.setVisibility(View.GONE);
                        mContainer.setVisibility(View.VISIBLE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProfileActivity.this.getContext(), "No Internet Connection!", Toast.LENGTH_SHORT).show();

                        mLoadingBar.setVisibility(View.GONE);
                        mContainer.setVisibility(View.VISIBLE);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                String idJSON = new Gson().toJson(userProfile.get(Constants.ID));

                params.put("user_id", idJSON);

                Optional.ofNullable(userProfile.get(Constants.GENDER)).ifPresent(o -> {params.put(("gender"), userProfile.get(Constants.GENDER));});
                Optional.ofNullable(userProfile.get(Constants.AGE)).ifPresent(o -> {params.put(("age"), userProfile.get(Constants.AGE));});
                Optional.ofNullable(userProfile.get(Constants.WEIGHT)).ifPresent(o -> {params.put(("weight"), userProfile.get(Constants.WEIGHT));});
                Optional.ofNullable(userProfile.get(Constants.UNIT)).ifPresent(o -> {params.put(("unit"), userProfile.get(Constants.UNIT));});

                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ProfileActivity.this.getContext());
        requestQueue.add(request);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.gender_spinner) {
            textGender = parent.getItemAtPosition(position).toString();

        }
        if (parent.getId() == R.id.unit_spinner) {
            textUnit = parent.getItemAtPosition(position).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public int getIndex(Spinner spinner, String selection) {
        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(selection)) {
                index = i;
            }
        }

        return index;
    }
}
