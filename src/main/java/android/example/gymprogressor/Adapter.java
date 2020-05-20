package android.example.gymprogressor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class Adapter extends PagerAdapter {
    private boolean isClicked;

    private List<Model> models;
    private String activityName;
    private LayoutInflater layoutInflater;
    private Context context;

    Adapter(List<Model> models, Context context, String name) {
        this.models = models;
        this.context = context;
        this.activityName = name;
    }


    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        isClicked = false;

        layoutInflater = LayoutInflater.from(context);
        SessionManager sessionManager = new SessionManager(context);
        String unit = sessionManager.getUserUnit();

        View view;
        switch (activityName) {
            case "BodyMeasurement":
                view = layoutInflater.inflate(R.layout.item_body_measurement, container, false);
                break;
            case "MeasurementsFragment":
                view = layoutInflater.inflate(R.layout.item_menu_measurements, container, false);
                break;
            case "StrengthMeasurement":
                view = layoutInflater.inflate(R.layout.item_strength_measurement, container, false);
                break;
            default:
                view = null; // exception
                break;
        }

        ImageView imageView;
        TextView title, desc;
        EditText input;

        imageView = view.findViewById(R.id.image);
        title = view.findViewById(R.id.title);
        desc = view.findViewById(R.id.desc);


        if (activityName.equals("BodyMeasurement")) {
            input = view.findViewById(R.id.user_input);
            input.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(4, 2)});
            input.setText(models.get(position).getInput());
        }

        if (activityName.equals("StrengthMeasurement")) {
            TextView unitField = view.findViewById(R.id.unit_field);
            unitField.setText(unit);

            input = view.findViewById(R.id.user_input);
            input.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(4, 2)});
            input.setText(models.get(position).getInput());
        }

        imageView.setImageResource(models.get(position).getImage());
        title.setText(models.get(position).getTitle());
        desc.setText(models.get(position).getDescription());

        container.addView(view, 0);


        if (activityName.equals("MeasurementsFragment")) {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isClicked) {
                        isClicked = true;
                        if (models.get(position).getTitle().equals("Body")) {
                            Intent intent = new Intent(context, android.example.gymprogressor.BodyMeasurementActivity.class);
                            context.startActivity(intent);
                        } else if (models.get(position).getTitle().equals("Strength")) {
                            Intent intent = new Intent(context, android.example.gymprogressor.StrengthMeasurementActivity.class);
                            context.startActivity(intent);
                        }
                    }
                }
            });
        }
        return view;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
