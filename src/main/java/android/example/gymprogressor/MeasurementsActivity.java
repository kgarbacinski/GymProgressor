package android.example.gymprogressor;

import android.animation.ArgbEvaluator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

public class MeasurementsActivity extends Fragment {
    private ViewPager viewPager;
    private Adapter adapter;
    private List<Model> models;
    private View view;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        viewPager.setAdapter(adapter);
        viewPager.setPadding(130, 60, 130, 0);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_measurements, container, false);

        models = new ArrayList<>();
        models.add(new Model(R.drawable.measurement, "Body", "Measure your body!", null));
        models.add(new Model(R.drawable.strength, "Strength", "Set your strength!", null));

        adapter = new Adapter(models, MeasurementsActivity.this.getContext(), "MeasurementsFragment");

        viewPager = view.findViewById(R.id.profilePager);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(130, 60, 130, 0);


        return view;
    }
}
