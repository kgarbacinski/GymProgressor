package android.example.gymprogressor;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class MenuPageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mToggle;
    private NavigationView mNavigationView;
    private SessionManager sessionManager;
    private TextView mName, mEmail, mTrainingNo, mToday;

    private String no_trainings, name, email;
    private HashMap<String, String> userProfile;

    /* Prevent from duble clicking same activity*/
    private int pressedItem;

    /* is app launched first time*/
    private boolean vStart = false;

    private boolean isUpdated = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menupage);


        //Dealing with Session Manager
        sessionManager = new SessionManager(this);

        /* Info app's version */
        showSnack();

        /* close app when outdated */
        if(!isUpdated){
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    // code called after 5 seconds...
                    sessionManager.logout(MenuPageActivity.this);
                }
            }, 10000);
        }
        else{
            sessionManager.checkLogin();
            userProfile = sessionManager.getProfile();
            name = userProfile.get(Constants.NAME);
            email = userProfile.get(Constants.EMAIL);


            /* Add toggle listener */
            mDrawer = findViewById(R.id.drawer);
            mToggle = new ActionBarDrawerToggle(this, mDrawer, R.string.open, R.string.close);
            mNavigationView = findViewById(R.id.nav_view);

            mDrawer.addDrawerListener(mToggle);
            mToggle.syncState();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            /* Add item listener */
            mNavigationView.setNavigationItemSelectedListener(this);


            /* Set name and email */
            View mNavHeader = mNavigationView.getHeaderView(0);

            mName = mNavHeader.findViewById(R.id.userName);
            mEmail = mNavHeader.findViewById(R.id.userEmail);
            mTrainingNo = mNavHeader.findViewById(R.id.trainingNo);
            mToday = mNavHeader.findViewById(R.id.dateToday);

            if(!vStart) {
                vStart = true;
                mName.setText("Hi, " + name);
                mEmail.setText(email);

                mToday.setText("Today's: " + new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()));
                mTrainingNo.setText("No. trainings: " + no_trainings);

                //Go to user profile
                onNavigationItemSelected(mNavigationView.getMenu().getItem(0));
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_profile:
                if (pressedItem != menuItem.getItemId()) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new ProfileActivity()).commit();
                    mNavigationView.setCheckedItem(R.id.nav_profile);
                    pressedItem = menuItem.getItemId();
                }
                break;
            case R.id.nav_shedule:
                if (pressedItem != menuItem.getItemId()) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new SheduleActivity()).commit();
                    mNavigationView.setCheckedItem(R.id.nav_shedule);
                    pressedItem = menuItem.getItemId();
                }
                break;

            case R.id.nav_measurements:
                if (pressedItem != menuItem.getItemId()) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new MeasurementsActivity()).commit();
                    mNavigationView.setCheckedItem(R.id.nav_shedule);
                    pressedItem = menuItem.getItemId();
                }
                break;

            case R.id.nav_review:
                if (pressedItem != menuItem.getItemId()) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new ReviewActivity()).commit();
                    mNavigationView.setCheckedItem(R.id.nav_review);
                    pressedItem = menuItem.getItemId();
                }
                break;

            case R.id.nav_logout:
                SessionManager sessionManager = new SessionManager(getApplicationContext());
                sessionManager.logout(this);
                break;
        }
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            userProfile = sessionManager.getProfile();
            no_trainings = userProfile.get(Constants.NO_TRAININGS);
            if (no_trainings == null) no_trainings = "0";
            mTrainingNo.setText("No. trainings: " + no_trainings);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Showing the update
    private void showSnack() {
        String message = "";

        int color = Color.WHITE;
        String appVersion = getString(R.string.app_version);
        if (appVersion.equals(sessionManager.getUpdatedAppVersion())) {
            message = "Your Gym Progressor v. " + appVersion + " is updated!";
        } else {
            message = "Your Gym Progressor v. " + appVersion + " is outdated! Get your update!";
            isUpdated = false;
        }

        Snackbar snackbar;

        if(isUpdated == false) {
             snackbar = Snackbar
                    .make(findViewById(android.R.id.content), message, Snackbar.LENGTH_INDEFINITE);
        }
        else{
            snackbar = Snackbar
                    .make(findViewById(android.R.id.content), message, 5000)
                    .setActionTextColor(getResources().getColor(R.color.colorText));
        }

        View sbView = snackbar.getView();
        sbView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        TextView textView = sbView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextColor(color);

        if(isUpdated) {
            snackbar.setAction("Ok", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Call your action method here
                    snackbar.dismiss();
                }
            });
        }
        snackbar.show();
    }
}
