package android.example.gymprogressor;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

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
import java.util.Optional;


public class BodyMeasurementActivity extends AppCompatActivity {
    private List<Model> models;
    private Adapter adapter;
    private ViewPager mViewPager;
    private ProgressBar mLoadingBar;

    private Button saveBtn;
    private CardView saveCard;

    static final int bodyParts = 7; // body parts + 1



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_measurement);

        saveBtn = findViewById(R.id.save_btn);
        saveCard = findViewById(R.id.save_card);
        mLoadingBar = findViewById(R.id.loadingBar);

        SessionManager sessionManager = new SessionManager(getApplicationContext());
        List<HashMap<Integer, String>> modelsPosList = new ArrayList<>();

        Optional<List<HashMap<Integer, String>>> listBuf = Optional.ofNullable(sessionManager.getBodyMeasurement());
        if (!listBuf.isPresent()) {
            for (int i = 0; i < bodyParts; i++) {
                HashMap<Integer, String> buf = new HashMap<>();
                buf.put(i, null);

                modelsPosList.add(buf);
            }
        }
        else {
            modelsPosList = sessionManager.getBodyMeasurement();
        }


        /* Get an array during cards rotation */
        models = new ArrayList<>();

        models.add(new Model(R.drawable.biceps, "Biceps", "Your bicep's size.", modelsPosList.get(6).get(6)));
        models.add(new Model(R.drawable.neck, "Neck", "Your neck's size.", modelsPosList.get(5).get(5)));
        models.add(new Model(R.drawable.chest, "Chest", "Your chest's size.", modelsPosList.get(4).get(4)));
        models.add(new Model(R.drawable.stomach, "Stomach", "Your stomach's size.", modelsPosList.get(3).get(3)));
        models.add(new Model(R.drawable.bottom, "Bottom", "Your bottom's size.", modelsPosList.get(2).get(2)));
        models.add(new Model(R.drawable.thigh, "Thigh", "Your thigh's size.", modelsPosList.get(1).get(1)));
        models.add(new Model(R.drawable.calf, "Calf", "Your calf's size.", modelsPosList.get(0).get(0)));

        adapter = new Adapter(models, this, "BodyMeasurement");

        mViewPager = findViewById(R.id.measurementPager);
        mViewPager.setOffscreenPageLimit(6);
        mViewPager.setAdapter(adapter);
        mViewPager.setPadding(130, 60, 130, 0);


        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
                List<HashMap<Integer, String>> inputsList = new ArrayList<>();
                for (int i = 0; i < models.size(); i++) {
                    EditText input = mViewPager.getChildAt(i).findViewById(R.id.user_input);

                    HashMap<Integer, String> map = new HashMap<>();

                    String inputValue = input.getText().toString();

                    inputValue = inputValue.replaceFirst("^0+(?!$)", "");
                    if(!inputValue.isEmpty()) {
                        if (Double.parseDouble(inputValue) > 999.0) {
                            input.setError("Invalid value!");
                            isValueOk = false;
                        } else {
                        }
                    }

                    map.put(i, inputValue);
                    inputsList.add(map);
                }

                if (isValueOk) {
                    /* Save the result into sessionManager */
                    sessionManager.updateBodyMeasurement(inputsList);

                    mLoadingBar.setVisibility(View.VISIBLE);
                    mViewPager.setVisibility(View.INVISIBLE);
                    saveCard.setVisibility(View.INVISIBLE);

                    StringRequest request = sessionManager.getBodyMeasurementRequest(BodyMeasurementActivity.this);
                    RequestQueue requestQueue = Volley.newRequestQueue(BodyMeasurementActivity.this);
                    if (request != null) {
                        requestQueue.add(request);
                    }
                }
            }
        });
    }

}
