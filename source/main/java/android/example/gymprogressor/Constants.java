package android.example.gymprogressor;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

class Constants {
    static String URL_LOGIN = "https://gymprogressor.000webhostapp.com/app/login.php";

    static final String URL_UPDATE_CALENDAR = "https://gymprogressor.000webhostapp.com/app/updateCalendar.php";;
    static final String URL_UPDATE_BODY = "https://gymprogressor.000webhostapp.com/app/updateBody.php";
    static final String URL_UPDATE_STRENGTH = "https://gymprogressor.000webhostapp.com/app/updateStrength.php";

    static final String APP_VERSION = "APP_V";

    /* Constants names for identifying variables of session managers */
    static final String IS_LOGGED = "IS_LOGGED";
    static final String IS_REMEMBERED = "IS_REMBEMBERED";
    static final String SAVE = "SAVE";
    static final String ID = "ID";
    static final String NAME = "NAME";
    static final String EMAIL = "EMAIL";

    /* Data UserInput */
    public static final String GENDER = "GENDER";
    public static final String AGE = "AGE";
    public static final String WEIGHT = "WEIGHT";
    public static final String UNIT = "UNIT";
    public static final String NO_TRAININGS = "NO_TRAININGS";

    /* Data From Database (Calendar) */
    static final String NOTE_LIST = "NOTE_LIST";
    static final String TIME_LIST = "TIME_LIST";

    /* Data From Body and Strength Measurement */
    public static final String BODY_LIST = "BODY_LIST";
    public static final String STRENGTH_MAP = "STRENGTH_MAP";

    public static final String STANDARDS_MAP = "STANDARDS_MAP";

    /* Token Types for parsing JSON */
    public static final Type LIST_TYPE_BODYMEASUREMENT = new TypeToken<List<HashMap<Integer, String>>>() {
    }.getType();
    public static final Type MAP_TYPE_STRENGTH_MEASUREMNT = new TypeToken<HashMap<Integer, String>>() {
    }.getType();

    public static final Type MAP_TYPE_STANDARDS = new TypeToken<HashMap<String, HashMap<String, Double>>>() {
    }.getType();
    /* Tokens to dejson */
    public static final Type LIST_TYPE_LONG = new TypeToken<List<Long>>() {
    }.getType();
    public static final Type LIST_TYPE_NOTES = new TypeToken<List<List<List<String>>>>() {
    }.getType();
}
