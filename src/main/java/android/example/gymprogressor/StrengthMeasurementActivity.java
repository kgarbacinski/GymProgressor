package android.example.gymprogressor;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class StrengthMeasurementActivity extends AppCompatActivity {
    private List<Model> models;
    private Adapter adapter;
    private ViewPager viewPager;

    private ProgressBar mLoadingBar;
    private CardView saveCard;
    private Button saveBtn;
    private String unit;

    private TextView unitField;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strength_measurement);

        mLoadingBar = findViewById(R.id.loadingBar);
        saveBtn = findViewById(R.id.save_btn);
        saveCard = findViewById(R.id.save_card);
        unitField = findViewById(R.id.unit_field);

        SessionManager sessionManager = new SessionManager(this);

        HashMap<Integer, String> strengthMap = sessionManager.getStrengthMeasurement();
        if (strengthMap == null) {
            for (int i = 0; i < 6; i++) {
                strengthMap = new HashMap<>();
                strengthMap.put(i, null);
            }
        }
        else {}


        /* Get an array during cards rotation */
        models = new ArrayList<>();

        models.add(new Model(R.drawable.bench_press, "Bench Press", "Type your BP max.", strengthMap.get(3)));
        models.add(new Model(R.drawable.deadlift, "Deadlift", "Type your DL max.", strengthMap.get(2)));
        models.add(new Model(R.drawable.overhead_press, "Overhead Press", "Type your OHP max.", strengthMap.get(1)));
        models.add(new Model(R.drawable.squat, "Squat", "Type your SQT size.", strengthMap.get(0)));
        //models.add(new Model(R.drawable.thigh, "Thigh", "Type your thigh size.", modelsPosList.get(1).get(1)));
        //models.add(new Model(R.drawable.calf, "Calf", "Type your calf size.", modelsPosList.get(0).get(0)));

        adapter = new Adapter(models, this, "StrengthMeasurement");

        viewPager = findViewById(R.id.measurementPager);
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(130, 60, 130, 0);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            Boolean first = true;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isValueOk = true;
                HashMap<Integer, String> inputsStrengthMap = new HashMap<>();
                for (int i = 0; i < models.size(); i++) {
                    EditText input = viewPager.getChildAt(i).findViewById(R.id.user_input);

                    String inputValue = input.getText().toString();
                    inputValue = inputValue.replaceFirst("^0+(?!$)", "");

                    if (!inputValue.isEmpty()) {
                        if (Double.parseDouble(inputValue) > 999.0) {
                            input.setError("Invalid value!");
                            isValueOk = false;
                        } else {
                        }
                    }

                    inputsStrengthMap.put(i, inputValue);
                }

                if (isValueOk) {
                    /* Save the result into sessionManager */
                    sessionManager.updateStrengthMeasurement(inputsStrengthMap);

                    mLoadingBar.setVisibility(View.VISIBLE);
                    viewPager.setVisibility(View.INVISIBLE);
                    saveCard.setVisibility(View.INVISIBLE);

                    StringRequest request = sessionManager.getStrengthMeasurementRequest(StrengthMeasurementActivity.this);
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    if (request != null) requestQueue.add(request);
                }
            }
        });
    }


}
