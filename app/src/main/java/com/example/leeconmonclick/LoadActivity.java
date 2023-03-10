package com.example.leeconmonclick;

import static java.lang.Thread.sleep;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.leeconmonclick.professional.leeconmonclick.professional.PatientListActivity;

import es.leerconmonclick.util.PBAnimation;

public class LoadActivity extends AppCompatActivity {

    ProgressBar pb;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        pb=(ProgressBar)findViewById(R.id.progress_bar);
        tv=(TextView) findViewById(R.id.text_view);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        pb.setMax(100);
        pb.setScaleY(3f);

        try {
            progressAnimation();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        startActivity(new Intent(getApplicationContext(), PatientListActivity.class));
        finish();
    }

    public void progressAnimation() throws InterruptedException {
        PBAnimation anim= new PBAnimation(this, pb, tv, 0f, 100f);
        anim.setDuration(8000);
        sleep(500);
        pb.setAnimation(anim);
    }
}