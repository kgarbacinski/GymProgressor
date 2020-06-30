package android.example.gymprogressor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.android.volley.VolleyLog.TAG;

public class SessionManager {
    private static final String PREF_NAME = "SESSION_MANAGER";
    private int PRIVATE_MODE = 0;

    private SharedPreferences.Editor editor;
    public Context context;


    private SharedPreferences sharedPreferences;
    private UserSession userSession;

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();

        userSession = new UserSession(context);

    }

    void createSession(int id, String name, String email) {
        editor.putBoolean(Constants.IS_LOGGED, true);
        editor.apply();

        userSession.setEmail(email);
        userSession.setID(id);
        userSession.setName(name);

    }

    void rememberUser() {
        editor.putBoolean(Constants.IS_REMEMBERED, true);
        editor.apply();
    }

    void updateCalendarList(List<Long> timeList, List<List<List<String>>> noteList) {
        if (timeList != null && noteList != null) {
            editor.putString(Constants.TIME_LIST, new Gson().toJson(timeList));
            editor.putString(Constants.NOTE_LIST, new Gson().toJson(noteList));
        } else {
            editor.putString(Constants.TIME_LIST, null);
            editor.putString(Constants.NOTE_LIST, null);
        }
        editor.apply();
    }

    void setAppVersion(String v) {
        editor.putString(Constants.APP_VERSION, v);
        editor.apply();
    }

    String getUpdatedAppVersion() {
        return sharedPreferences.getString(Constants.APP_VERSION, null);
    }

    void updateProfile(Map<String, String> changedProfile) {
        userSession.updateProfileDetails(changedProfile);
    }

    void updateNoTrainings(String number) {
        editor.putString(Constants.NO_TRAININGS, number);
        editor.apply();
    }

    void updateBodyMeasurement(List<HashMap<Integer, String>> bodyList) {
        if (bodyList != null) {
            if (bodyList.size() > 0) {
                Gson gson = new Gson();
                String listJSON = gson.toJson(bodyList, Constants.LIST_TYPE_BODYMEASUREMENT);

                editor.putString(Constants.BODY_LIST, listJSON);
                editor.apply();
            }
        }
    }

    void updateStrengthMeasurement(Map<Integer, String> strengthMap) {
        if (strengthMap != null) {
            if (strengthMap.size() > 0) {
                Gson gson = new Gson();
                String strengthMapJSON = gson.toJson(strengthMap, Constants.MAP_TYPE_STRENGTH_MEASUREMNT);

                editor.putString(Constants.STRENGTH_MAP, strengthMapJSON);
                editor.apply();
            }
        }
    }

    List<HashMap<Integer, String>> getBodyMeasurement() {
        Gson gson = new Gson();
        String listJSON = sharedPreferences.getString(Constants.BODY_LIST, null);

        return gson.fromJson(listJSON, Constants.LIST_TYPE_BODYMEASUREMENT);

    }

    HashMap<Integer, String> getStrengthMeasurement() {
        Gson gson = new Gson();
        String strengthMapJSON = sharedPreferences.getString(Constants.STRENGTH_MAP, null);

        return gson.fromJson(strengthMapJSON, Constants.MAP_TYPE_STRENGTH_MEASUREMNT);

    }


    String getUserUnit() {
        return sharedPreferences.getString(Constants.UNIT, "kg");
    }

    /* For header  */
    HashMap<String, String> getProfile() {
        return userSession.getUserDetails();
    }

    HashMap<String, List<Long>> getTimeDetail() {
        String data = sharedPreferences.getString(Constants.TIME_LIST, null);
        Optional.ofNullable(data)
                .map(f -> {
                    HashMap<String, List<Long>> time = new HashMap<>();
                    time.put(Constants.TIME_LIST, new Gson().fromJson(data, Constants.LIST_TYPE_LONG));

                    return time;
                });

        return null;
    }

    HashMap<String, List<List<List<String>>>> getNoteDetail() {
        String data = sharedPreferences.getString(Constants.NOTE_LIST, null);

        if (data != null) {
            HashMap<String, List<List<List<String>>>> note = new HashMap<>();
            note.put(Constants.NOTE_LIST, new Gson().fromJson(sharedPreferences.getString(Constants.NOTE_LIST, null), Constants.LIST_TYPE_NOTES));

            return note;
        } else return null;
    }

    HashMap<String, HashMap<String, Double>> getStandardsMap() {
        String standardMapJSON = sharedPreferences.getString(Constants.STANDARDS_MAP, null);

        return new Gson().fromJson(standardMapJSON, Constants.MAP_TYPE_STANDARDS);
    }

    void addStandardsMap(String standardsMapJSON) {
        editor.putString(Constants.STANDARDS_MAP, standardsMapJSON);
        editor.apply();
    }

    StringRequest getBodyMeasurementRequest(Context context) {
        String bodyJSON = sharedPreferences.getString(Constants.BODY_LIST, null);

        if (bodyJSON != null) {
            StringRequest request = new StringRequest(Request.Method.POST, Constants.URL_UPDATE_BODY,
                    response -> {
                        Toast.makeText(context, "Saved!", Toast.LENGTH_SHORT).show();
                        ((BodyMeasurementActivity) context).finish();
                    },
                    error -> {
                        Toast.makeText(context, "No Internet connection!", Toast.LENGTH_LONG).show();
                        ((BodyMeasurementActivity) context).finish();
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();

                    int id = sharedPreferences.getInt(Constants.ID, 0);

                    String idJSON = new Gson().toJson(id);

                    params.put("user_id", idJSON);
                    params.put("body_list", bodyJSON);

                    return params;

                }
            };

            return request;
        }
        return null;
    }

    StringRequest getStrengthMeasurementRequest(Context context) {
        String strengthJSON = sharedPreferences.getString(Constants.STRENGTH_MAP, null);

        if (strengthJSON != null) {
            StringRequest request = new StringRequest(Request.Method.POST, Constants.URL_UPDATE_STRENGTH,
                    response -> {
                        Toast.makeText(context, "Saved!", Toast.LENGTH_SHORT).show();
                        ((StrengthMeasurementActivity) context).finish();
                    },
                    error -> {
                        Toast.makeText(context, "No Internet connection!", Toast.LENGTH_LONG).show();
                        ((StrengthMeasurementActivity) context).finish();
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();

                    int id = sharedPreferences.getInt(Constants.ID, 0);

                    String idJSON = new Gson().toJson(id);

                    params.put("user_id", idJSON);
                    params.put("strength_map", strengthJSON);

                    return params;

                }
            };

            return request;
        }
        return null;
    }


    StringRequest getCalendarRequest() {
        String noteJSON = sharedPreferences.getString(Constants.NOTE_LIST, null);
        String timeJSON = sharedPreferences.getString(Constants.TIME_LIST, null);
        String no_trainings = sharedPreferences.getString(Constants.NO_TRAININGS, "0");
        int id = sharedPreferences.getInt(Constants.ID, 0);

        String idJSON = new Gson().toJson(id);

        StringRequest request = new StringRequest(Request.Method.POST, Constants.URL_UPDATE_CALENDAR,
                response -> {
                    Log.d("Success!", response);
                },
                error -> {
                    Log.e(TAG, "ERROR");
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("user_id", idJSON);
                params.put("note_list", noteJSON);
                params.put("time_list", timeJSON);
                params.put("no_trainings", no_trainings);

                return checkParams(params);
            }

            //Convert when deleting last note
            private Map<String, String> checkParams(Map<String, String> map) {
                for (Map.Entry<String, String> pairs : map.entrySet()) {
                    if (pairs.getValue() == null) {
                        map.put(pairs.getKey(), "");
                    }
                }
                return map;
            }
        };
        return request;
    }


    private boolean isLogin() {
        return sharedPreferences.getBoolean(Constants.IS_LOGGED, false);
    }

    void checkLogin() {
        if (!this.isLogin()) {
            Intent i = new Intent(context, LoginActivity.class);
            context.startActivity(i);
            ((MenuPageActivity) context).finish();
        }
    }

    void logout(Context context) {
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, LoginActivity.class);
        ((MenuPageActivity) context).finish();
        context.startActivity(i);
    }
}
