package es.leerconmonclick.util;

import android.content.Context;
import android.content.Intent;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.leeconmonclick.MainActivity;

public class PBAnimation extends Animation {

    private Context context;
    private ProgressBar pb;
    private TextView tv;
    private float from;
    private float to;

    public PBAnimation(Context context, ProgressBar pb, TextView tv, float from, float to) {
        this.context = context;
        this.pb = pb;
        this.tv = tv;
        this.from = from;
        this.to = to;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        float value = from + (to - from) * interpolatedTime;
        pb.setProgress((int) value);
        tv.setText((int) value + " %");

        if (value == to) {
            context.startActivity(new Intent(context, PersonalNotesActivity.class));
        }
    }
}
