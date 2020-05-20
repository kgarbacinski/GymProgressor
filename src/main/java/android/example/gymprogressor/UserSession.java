package android.example.gymprogressor;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class UserSession {
    private int PRIVATE_MODE = 0;
    private String PREF_NAME = "USER_SESSION";

    private Context activityContext;
    private SharedPreferences shrdPreferences;
    private SharedPreferences.Editor editor;

    UserSession(Context context){
        this.activityContext = context;
        this.shrdPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        this.editor = shrdPreferences.edit();
    }

    void setName(String name){
        editor.putString(Constants.NAME, name);
        editor.apply();
    }

    void setID(int id){
        editor.putInt(Constants.ID, id);
        editor.apply();
    }

    void setEmail(String email){
        editor.putString(Constants.EMAIL, email);
        editor.apply();

    }

    void updateProfileDetails(Map<String, String> changedProfile){
        if (!"null".equals(changedProfile.get(Constants.AGE)))
            editor.putString(Constants.AGE, changedProfile.get(Constants.AGE));

        if (!"null".equals(changedProfile.get(Constants.GENDER)))
            editor.putString(Constants.GENDER, changedProfile.get(Constants.GENDER));

        if (!"null".equals(changedProfile.get(Constants.WEIGHT)))
            editor.putString(Constants.WEIGHT, changedProfile.get(Constants.WEIGHT));

        if (!"null".equals(changedProfile.get(Constants.UNIT)))
            editor.putString(Constants.UNIT, changedProfile.get(Constants.UNIT));

        if (!"0".equals(changedProfile.get(Constants.NO_TRAININGS)))
            Optional.ofNullable(changedProfile.get(Constants.NO_TRAININGS))
                    .map(c -> editor.putString(Constants.NO_TRAININGS, changedProfile.get(Constants.NO_TRAININGS)));

        editor.apply();
    }

    HashMap<String, String> getUserDetails(){
        HashMap<String, String> userDetails = new HashMap<>();

        userDetails.put(Constants.ID, Integer.toString(shrdPreferences.getInt(Constants.ID, 0)));
        userDetails.put(Constants.NAME, shrdPreferences.getString(Constants.NAME, null));
        userDetails.put(Constants.EMAIL, shrdPreferences.getString(Constants.EMAIL, null));
        userDetails.put(Constants.GENDER, shrdPreferences.getString(Constants.GENDER, null));
        userDetails.put(Constants.AGE, shrdPreferences.getString(Constants.AGE, null));
        userDetails.put(Constants.WEIGHT, shrdPreferences.getString(Constants.WEIGHT, null));
        userDetails.put(Constants.UNIT, shrdPreferences.getString(Constants.UNIT, "kg"));
        userDetails.put(Constants.SAVE, Boolean.toString(shrdPreferences.getBoolean(Constants.SAVE, false)));
        userDetails.put(Constants.NO_TRAININGS, shrdPreferences.getString(Constants.NO_TRAININGS, null));

        return userDetails;
    }

}
