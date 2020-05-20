package android.example.gymprogressor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText mEmail;
    private EditText mPassword;
    private Button mRegButton;
    private Button mBtnLog;
    private ProgressBar mLoading;
    private CheckBox mCheckBox ;
    private SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /* Init view */
        mEmail = findViewById(R.id.email_field);
        mPassword = findViewById(R.id.password_field);
        mRegButton = findViewById(R.id.reg_button);
        mBtnLog = findViewById(R.id.login_button);
        mLoading = findViewById(R.id.loading_bar);
        mCheckBox = findViewById(R.id.remember_box);

        sessionManager = new SessionManager(this);

        /* Check if user is logged in*/
        @NonNull HashMap<String, String> user = sessionManager.getProfile();
        if(!"false".equals(user.get(Constants.IS_REMEMBERED))) {
            if (!"0".equals(user.get(Constants.ID))) {
                Intent intent = new Intent(this, MenuPageActivity.class);
                startActivity(intent);
                finish();
            }
        }

        mBtnLog.setOnClickListener(v -> {
            final String vEmail = mEmail.getText().toString().trim();
            final String vPassword = mPassword.getText().toString().trim();

            if (!TextUtils.isEmpty(vEmail) && !TextUtils.isEmpty(vPassword)) {
                login(vEmail, vPassword);
            } else if (!TextUtils.isEmpty(vEmail) && TextUtils.isEmpty(vPassword)) {
                mPassword.setError("Please insert password");
            } else if (TextUtils.isEmpty(vEmail) && !TextUtils.isEmpty(vPassword)) {
                mEmail.setError("Please insert email");
            } else if (TextUtils.isEmpty(vEmail) && TextUtils.isEmpty(vPassword)) {
                mEmail.setError("Please insert email");
                mPassword.setError("Please insert password");
            }
        });

        mRegButton.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }

    private void login(final String vEmail, final String vPassword) {
        showLoadingIndicator(); // show loading bar

        StringRequest request = new StringRequest(Request.Method.POST, Constants.URL_LOGIN,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");

                        if (success.equals("1")) {
                            String version = jsonObject.getString("version");
                            JSONArray jsonArrayLogin = jsonObject.getJSONArray("login");
                            JSONArray jsonArrayCalendar = jsonObject.getJSONArray("calendar");
                            JSONArray jsonArrayProfile = jsonObject.getJSONArray("profile");
                            JSONArray jsonArrayMeasurements = jsonObject.getJSONArray("measurements");

                            String name = null;
                            String email = null;
                            int id = -1;

                            /* Calendar Datas */
                            List<Long> timeList = new ArrayList<>();
                            List<List<List<String>>> noteList = new ArrayList<>();

                            /* Profile Data */
                            HashMap<String, String> userProfile = new HashMap<>();

                            /* Measurement Data */
                            String bodyListJSON = "";
                            String strengthListJSON = "";

                            List<HashMap<Integer, String>> bodyList = new ArrayList<>();
                            @SuppressLint("UseSparseArrays") Map<Integer, String> strengthMap = new HashMap<>();

                            //Get UserSession Data; called only once
                            for (int i = 0; i < jsonArrayLogin.length(); i++) {
                                JSONObject object = jsonArrayLogin.getJSONObject(i);

                                id = object.getInt("id");
                                name = object.getString("name");
                                email = object.getString("email");
                            }

                            //Get Calendar Notes and Time
                            for (int i = 0; i < jsonArrayCalendar.length(); i++) {
                                JSONObject object = jsonArrayCalendar.getJSONObject(i);

                                String noteJSON = object.getString("NoteJson");
                                String timeJSON = object.getString("TimeJson");

                                noteList = new Gson().fromJson(noteJSON, Constants.LIST_TYPE_NOTES);
                                timeList = new Gson().fromJson(timeJSON, Constants.LIST_TYPE_LONG);
                                //noteList.add(object.getString("NoteJson"));
                            }

                            /* Get UserSession Profile */
                            for (int i = 0; i < jsonArrayProfile.length(); i++) {
                                JSONObject object = jsonArrayProfile.getJSONObject(i);

                                userProfile.put(Constants.UNIT, object.getString("unit"));
                                userProfile.put(Constants.GENDER, object.getString("gender"));
                                userProfile.put(Constants.AGE, object.getString("age"));
                                userProfile.put(Constants.WEIGHT, object.getString("weight"));
                                userProfile.put(Constants.NO_TRAININGS, object.getString("no_trainings"));

                            }

                            /* Get Measurements called only once*/
                            for (int i = 0; i < jsonArrayMeasurements.length(); i++) {
                                JSONObject object = jsonArrayMeasurements.getJSONObject(i);

                                bodyListJSON = object.getString("bodyJSON");
                                strengthListJSON = object.getString("strengthJSON");
                            }


                            if (!"null".equals(bodyListJSON)) {
                                Gson gson = new Gson();
                                bodyList = gson.fromJson(bodyListJSON, Constants.LIST_TYPE_BODYMEASUREMENT);
                            }

                            if (!"null".equals(strengthListJSON)) {
                                Gson gson = new Gson();
                                strengthMap = gson.fromJson(strengthListJSON, Constants.MAP_TYPE_STRENGTH_MEASUREMNT);
                            }


                            if(mCheckBox.isChecked()) sessionManager.rememberUser(); // If user checked the "Remeber me" field

                            /* Set all the user params */
                            sessionManager.setAppVersion(version);
                            sessionManager.createSession(id, name, email);
                            sessionManager.updateCalendarList(timeList, noteList);
                            sessionManager.updateProfile(userProfile);
                            sessionManager.updateBodyMeasurement(bodyList);
                            sessionManager.updateStrengthMeasurement(strengthMap);

                            /* If succeed, move to MenuActivity */
                            Intent intent = new Intent(LoginActivity.this, MenuPageActivity.class);
                            startActivity(intent);
                            finish();

                        }
                        else {
                            Toast.makeText(LoginActivity.this, "Login Failed.\n" +
                                    "Incorrect username or password!", Toast.LENGTH_SHORT)
                                    .show();

                            hideLoadingIndicator();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_LONG)
                            .show();
                    hideLoadingIndicator();
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();

                        params.put("email", vEmail);
                        params.put("password", vPassword);

                        return params;
                    }
                   };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    /* Change elements' visibility */
    private void showLoadingIndicator() {
        mLoading.setVisibility(View.VISIBLE);
        mBtnLog.setVisibility(View.INVISIBLE);
        mRegButton.setVisibility(View.INVISIBLE);
    }

    private void hideLoadingIndicator() {
        mLoading.setVisibility(View.GONE);
        mBtnLog.setVisibility(View.VISIBLE);
        mRegButton.setVisibility(View.VISIBLE);
    }
}
