package com.example.leeconmonclick.patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.example.leeconmonclick.AudioPlay;
import com.example.leeconmonclick.ErrorActivity;
import com.example.leeconmonclick.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import es.leerconmonclick.util.Content;
import es.leerconmonclick.util.Syllable;

public class SyllablesGameActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private CircleImageView iconPatient;
    private Context context = this;
    private String namePatient;
    private ImageView puzzle1, puzzle2, puzzle3, puzzle4,puzzle5,yellow,yellow2;
    private boolean isIntersectPuzzle1, isIntersectPuzzle2, isIntersectPuzzle3, isIntersectPuzzle4,isIntersectPuzzle5,intersectPuzzle = false;
    private List<Syllable> listSylable;
    private  List<Content> l;
    private boolean first,second;
    private AlertDialog alertDialog;
    private AlertDialog.Builder alertDialogBuilder;
    private int countFailed,countSucces=  0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syllables_game);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        findElements();

        Boolean valor = getIntent().getExtras().getBoolean("music");
        if(valor){
            AudioPlay.restart();
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        namePatient = preferences.getString("userPatient","null").toLowerCase(Locale.ROOT);


        listSylable = new ArrayList<>();
        l = new ArrayList<>();





        databaseReference.child("userPatient").child(namePatient).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String icon = snapshot.child("icon").getValue().toString();
                databaseReference.child("iconPatient").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Glide.with(context.getApplicationContext()).load(snapshot.child(icon).getValue().toString()).into(iconPatient);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        final ConstraintLayout constraintLayout;
        constraintLayout =  findViewById(R.id.syllableGame);

        databaseReference.child("userPatient").child(namePatient).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String dalto = snapshot.child("sett").child("1").getValue().toString();
                if(dalto.equals("tritanopia")){
                    constraintLayout.setBackgroundResource(R.color.background_tritano);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        puzzle1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                checkPuzzle(puzzle1,event);
                return true;
            }
        });

        puzzle2.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                checkPuzzle(puzzle2,event);
                return true;
            }
        });

        puzzle3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                checkPuzzle(puzzle3,event);
                return true;
            }
        });

        puzzle4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                checkPuzzle(puzzle4,event);
                return true;
            }
        });

        puzzle5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                checkPuzzle(puzzle5,event);
                return true;
            }
        });


        initBBDD();



    }

    private void checkPuzzle (ImageView puzzle, MotionEvent event){

        Rect rect1 = new Rect();
        Rect rect2 = new Rect();
        Rect rect3 = new Rect();

        puzzle.getHitRect(rect1);
        yellow.getHitRect(rect2);
        yellow2.getHitRect(rect3);


        if (event.getAction() == MotionEvent.ACTION_MOVE) {

            puzzle.setX(event.getRawX() - puzzle.getWidth() / 2);
            puzzle.setY(event.getRawY() - puzzle.getHeight() / 2);

            boolean failedInCurrentPosition = false;


            String[] sy = l.get(0).getSyllables().split("-");

            if (Rect.intersects(rect1, rect2)) {
                intersectPuzzle = true;
                if (puzzle.getTag().toString().equals(sy[0].toLowerCase(Locale.ROOT))) {
                    Toast.makeText(getApplicationContext(), "Pieza Correcta", Toast.LENGTH_LONG).show();
                    first = true;
                }
            } else if (Rect.intersects(rect1, rect3)) {
                intersectPuzzle = true;
                if (puzzle.getTag().toString().equals(sy[1].toLowerCase(Locale.ROOT))) {
                    Toast.makeText(getApplicationContext(), "Pieza Correcta", Toast.LENGTH_LONG).show();
                    second = true;
                }
            }else{
                intersectPuzzle = false;
            }

        }


        if (event.getAction() == MotionEvent.ACTION_UP){

            if (!Rect.intersects(rect1, rect2) && !Rect.intersects(rect1, rect3)) {
                intersectPuzzle = false;
                first = false;
                second = false;
            }

            if (first && second) {
                alertFinishGame();
            }

            if (intersectPuzzle && !first && !second){
                countFailed++;
                Toast.makeText(getApplicationContext(), "Pieza Incorrecta", Toast.LENGTH_LONG).show();
            }

        }

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                Intent intent = new Intent(SyllablesGameActivity.this, ErrorActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                System.exit(1);
            }
        });

    }



    public void refreshBBDD(View v){   Toast.makeText(getApplicationContext(), "Cargando nuevo puzzle...", Toast.LENGTH_LONG).show();
        recreate();
    }

    private void initBBDD (){

        String[] listCategory = {"Hogar","Animales","Comidas"};

        List<Content> listContent = new ArrayList<>();


            databaseReference.child("content").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {



                    for (String categoty : listCategory){
                        for (DataSnapshot objSnapshot : snapshot.child(categoty).getChildren()) {

                            boolean isSyllable =  (boolean) objSnapshot.child("isSyllable").getValue();
                            if (isSyllable) {
                                String syllable = objSnapshot.child("syllables").getValue().toString();
                                String img = objSnapshot.child("img").getValue().toString();
                                String word = objSnapshot.child("word").getValue().toString();
                                Content content = new Content(word, img, syllable, null);
                                listContent.add(content);
                            }
                        }
                    }

                    Collections.shuffle(listContent);
                    l.add(listContent.get(0));
                    l.add(listContent.get(1));

                    Collections.shuffle(l);
                    getImgPuzzle(l.get(0));

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

    }

    private void randomPuzzle(){

        databaseReference.child("content").child("Puzzle").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String[] sy = l.get(0).getSyllables().split("-");
                List<String> sl = new ArrayList<>();

                for (DataSnapshot objSnapshot : snapshot.getChildren()){

                    if(!sy[0].toLowerCase(Locale.ROOT).equals(objSnapshot.getKey().toLowerCase(Locale.ROOT)) &&
                            !sy[1].toLowerCase(Locale.ROOT).equals(objSnapshot.getKey().toLowerCase(Locale.ROOT))){
                        sl.add(objSnapshot.getKey().toLowerCase(Locale.ROOT));
                    }
                }
                Collections.shuffle(sl);
                Syllable s1 = new Syllable(sl.get(0).toLowerCase(Locale.ROOT),snapshot.child(sl.get(0)).getValue().toString());
                Syllable s2 = new Syllable(sl.get(1).toLowerCase(Locale.ROOT),snapshot.child(sl.get(1)).getValue().toString());
                Syllable s3 = new Syllable(sl.get(2).toLowerCase(Locale.ROOT),snapshot.child(sl.get(2)).getValue().toString());
                listSylable.add(s1);
                listSylable.add(s2);
                listSylable.add(s3);


                if (listSylable.size() == 5) {

                    Collections.shuffle(listSylable);

                    Glide.with(context).load(listSylable.get(0).getImg()).into(puzzle1);
                    Glide.with(context).load(listSylable.get(1).getImg()).into(puzzle2);
                    Glide.with(context).load(listSylable.get(2).getImg()).into(puzzle3);
                    Glide.with(context).load(listSylable.get(3).getImg()).into(puzzle4);
                    Glide.with(context).load(listSylable.get(4).getImg()).into(puzzle5);


                    puzzle1.setTag(listSylable.get(0).getSyllable().toLowerCase(Locale.ROOT));
                    puzzle2.setTag(listSylable.get(1).getSyllable().toLowerCase(Locale.ROOT));
                    puzzle3.setTag(listSylable.get(2).getSyllable().toLowerCase(Locale.ROOT));
                    puzzle4.setTag(listSylable.get(3).getSyllable().toLowerCase(Locale.ROOT));
                    puzzle5.setTag(listSylable.get(4).getSyllable().toLowerCase(Locale.ROOT));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getImgPuzzle(Content content){

        String[] sy = content.getSyllables().split("-");


        databaseReference.child("content").child("Puzzle").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot objSnapshot : snapshot.getChildren()){
                    String z =objSnapshot.getKey().toLowerCase(Locale.ROOT);
                    if (z.equals(sy[0].toLowerCase(Locale.ROOT))){
                        Syllable s = new Syllable(sy[0],objSnapshot.getValue().toString());
                        listSylable.add(s);
                        break;
                    }
                }

                for (DataSnapshot objSnapshot : snapshot.getChildren()){
                    String z =objSnapshot.getKey().toLowerCase(Locale.ROOT);
                    if (z.equals(sy[1].toLowerCase(Locale.ROOT))){
                        Syllable s = new Syllable(sy[1],objSnapshot.getValue().toString());
                        listSylable.add(s);
                        break;
                    }
                }
                randomPuzzle();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void alertFinishGame(){


        databaseReference.child("userPatient").child(namePatient).child("stadistic").child("syllables").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                countSucces = snapshot.child("succes").getValue(Integer.class) + 1;

                countFailed = snapshot.child("failed").getValue(Integer.class) + countFailed;

                int t = snapshot.child("timesPlayed").getValue(Integer.class);
                t++;

                databaseReference.child("userPatient").child(namePatient).child("stadistic").child("syllables").child("succes").setValue(countSucces);
                databaseReference.child("userPatient").child(namePatient).child("stadistic").child("syllables").child("timesPlayed").setValue(t);
                databaseReference.child("userPatient").child(namePatient).child("stadistic").child("syllables").child("failed").setValue(countFailed);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        alertDialogBuilder = new AlertDialog.Builder(this);
        final View finishGamePopUp = getLayoutInflater().inflate(R.layout.finish_game,null);

        ImageView img = (ImageView) finishGamePopUp.findViewById(R.id.img);
        Button btn = (Button) finishGamePopUp.findViewById(R.id.btn);



        Glide.with(context).load(l.get(0).getImg()).into(img);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                finish();
            }
        });

        alertDialogBuilder.setView(finishGamePopUp);
        alertDialog =  alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

                alertDialog.dismiss();
                finish();


            }
        });


    }


    private void findElements(){
        iconPatient = findViewById(R.id.iconPatientId);

        puzzle1 =  findViewById(R.id.puzzle1);
        puzzle2 = findViewById(R.id.puzzle2);
        puzzle3 = findViewById(R.id.puzzle3);
        puzzle4 = findViewById(R.id.puzzle4);
        puzzle5 = findViewById(R.id.puzzle5);
        yellow =findViewById(R.id.yellow);
        yellow2 =findViewById(R.id.yellow2);



    }

    public void goBack(View v){
        finish();
    }

    @Override
    protected void onPause() {
        AudioPlay.stopAudio();
        super.onPause();
    }


    @Override
    protected void onRestart() {
        Boolean valor = getIntent().getExtras().getBoolean("music");
        if(valor){
            AudioPlay.restart();
        }
        super.onRestart();
    }



}