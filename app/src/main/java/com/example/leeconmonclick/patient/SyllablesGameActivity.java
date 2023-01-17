package com.example.leeconmonclick.patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.leeconmonclick.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.content.ClipData;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;

public class SyllablesGameActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private CircleImageView iconPatient;
    private Context context = this;
    private String namePatient;
    private ImageView blue,green,gray,red,yellow;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syllables_game);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        findElements();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        namePatient = preferences.getString("userName","null");

        databaseReference.child("userPatient").child(namePatient).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String icon = snapshot.child("icon").getValue().toString();
                Glide.with(context).load(icon).into(iconPatient);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        blue.setOnLongClickListener(longClickListener);
        blue.setOnTouchListener(touchListener);
        red.setOnLongClickListener(longClickListener);
        green.setOnLongClickListener(longClickListener);
        gray.setOnLongClickListener(longClickListener);
        yellow.setOnDragListener(dragListener);
        textView.setOnLongClickListener(longClickListener);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.calendar_style);




    }

    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            ClipData clipData = ClipData.newPlainText("","");
            View.DragShadowBuilder myShadowBuilder = new View.DragShadowBuilder(v);
            v.startDrag(clipData,myShadowBuilder,null,0);
            return true;
        }
    };


    View.OnLongClickListener longClickListener = (v) -> {
        ClipData clipData = ClipData.newPlainText("","");

        View.DragShadowBuilder myShadowBuilder = new View.DragShadowBuilder(v);
        v.startDrag(clipData,myShadowBuilder,v,0);
        return true;
    };



    View.OnDragListener dragListener = new View.OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {

            int dragEvent = event.getAction();
            final View view = (View) event.getLocalState();
            switch (dragEvent) {
                case DragEvent.ACTION_DRAG_ENTERED:{

                        textView.setText("ENTRE");
                    break;
                }
                case DragEvent.ACTION_DRAG_EXITED:{

                        textView.setText("SALI");
                    break;

                }
                case DragEvent.ACTION_DROP:{
                        textView.setText("DROP");
                    break;
                }
                case DragEvent.ACTION_DRAG_ENDED:{

                        textView.setText("END");

                    break;
                }
            }
            return true;
        }
    };



    private void findElements(){
        iconPatient = findViewById(R.id.iconPatientId);

        blue =  findViewById(R.id.blue);
        red = findViewById(R.id.reed);
        green = findViewById(R.id.green);
        gray = findViewById(R.id.gray);
        yellow =findViewById(R.id.yellow);

        textView = findViewById(R.id.textView10);

    }

    public void goBack(View v){
        finish();
    }
}