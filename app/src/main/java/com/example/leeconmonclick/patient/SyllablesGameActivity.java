package com.example.leeconmonclick.patient;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
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
import android.speech.tts.TextToSpeech;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import es.leerconmonclick.util.AudioPlay;
import es.leerconmonclick.util.utils.Content;
import es.leerconmonclick.util.utils.Syllable;

public class SyllablesGameActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private CircleImageView iconPatient;
    private final Context context = this;
    private String namePatient;
    private ImageView puzzle1, puzzle2, puzzle3, puzzle4,puzzle5,yellow,yellow2;
    private List<Syllable> listSylable;
    private  List<Content> l;
    private AlertDialog alertDialog;
    private int countFailed,countSucces=  0;
    private Toast myToast;
    private TextToSpeech tts;

    private String syllable1;
    private String syllable2;

    private Rect[] rectList = new Rect[5];

    private Rect rect1,rect2,rect3,rect4,rect5,box1,box2;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syllables_game);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        findElements();
        music();
        getSettings();

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                Intent intent = new Intent(SyllablesGameActivity.this, ErrorActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                System.exit(1);
            }
        });

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i != TextToSpeech.ERROR){
                    tts.setLanguage(Locale.getDefault());
                }

            }
        });

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

        puzzle1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                tts.speak(puzzle1.getTag().toString(), TextToSpeech.QUEUE_FLUSH, null);
                checkPuzzle(puzzle1,event);
                return true;
            }
        });

        puzzle2.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                tts.speak(puzzle2.getTag().toString(), TextToSpeech.QUEUE_FLUSH, null);
                checkPuzzle(puzzle2,event);
                return true;
            }
        });

        puzzle3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                tts.speak(puzzle3.getTag().toString(), TextToSpeech.QUEUE_FLUSH, null);
                checkPuzzle(puzzle3,event);
                return true;
            }
        });

        puzzle4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                tts.speak(puzzle4.getTag().toString(), TextToSpeech.QUEUE_FLUSH, null);
                checkPuzzle(puzzle4,event);
                return true;
            }
        });

        puzzle5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                tts.speak(puzzle5.getTag().toString(), TextToSpeech.QUEUE_FLUSH, null);
                checkPuzzle(puzzle5,event);
                return true;
            }
        });

        initBBDDv2();

    }

    private void getRectList(){

        rect1 = new Rect();
        rect2 = new Rect();
        rect3 = new Rect();
        rect4 = new Rect();
        rect5 = new Rect();
        box1 = new Rect();
        box2 = new Rect();

        puzzle1.getHitRect(rect1);
        puzzle2.getHitRect(rect2);
        puzzle3.getHitRect(rect3);
        puzzle4.getHitRect(rect4);
        puzzle5.getHitRect(rect5);

        yellow.getHitRect(box1);
        yellow2.getHitRect(box2);

        rectList[0] = rect1;
        rectList[1] = rect2;
        rectList[2] = rect3;
        rectList[3] = rect4;
        rectList[4] = rect5;

    }



    private void checkPuzzle (ImageView puzzle, MotionEvent event){

        Rect puzzleRect = new Rect();
        Rect box1 = new Rect();
        Rect box2 = new Rect();

        puzzle.getHitRect(puzzleRect);
        yellow.getHitRect(box1);
        yellow2.getHitRect(box2);

        getRectList();

        if (event.getAction() == MotionEvent.ACTION_MOVE) {

            puzzle.setX(event.getRawX() - puzzle.getWidth() / 2);
            puzzle.setY(event.getRawY() - puzzle.getHeight() / 2);

            if (Rect.intersects(puzzleRect, box1)) {
                  syllable1 = puzzle.getTag().toString();
              }else{
                syllable1 = null;
            }

              if (Rect.intersects(puzzleRect, box2)) {
                  syllable2 = puzzle.getTag().toString();
              }else{
                  syllable2 = null;
              }
        }

        if (event.getAction() == MotionEvent.ACTION_UP){

            if(syllable1 != null && syllable2!= null){

                String word = syllable1 + syllable2;

                databaseReference.child("content").child("Sílabas-content").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        for (DataSnapshot objSnapshot : snapshot.getChildren()) {

                            if(objSnapshot.getKey().toLowerCase().equals(word)){

                                alertFinishGame(objSnapshot.child("img").getValue().toString(),word);
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }
    }

    public void refreshBBDD(View v){   myToast = Toast.makeText(getApplicationContext(), "Cargando nuevo puzzle...", Toast.LENGTH_LONG);
        myToast.show();
        recreate();
    }

    private void initBBDDv2 (){

        String[] listCategory = {"Hogar","Animales","Comidas"};
        List<Content> listContent = new ArrayList<>();
        databaseReference.child("content").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (String categoty : listCategory){
                    for (DataSnapshot objSnapshot : snapshot.child(categoty).getChildren()) {
                        boolean isSyllable =  (boolean) objSnapshot.child("isSyllable").getValue();
                        if (isSyllable) {
                            String syllable = objSnapshot.child("syllables").getValue().toString().toLowerCase();
                            String img = objSnapshot.child("img").getValue().toString();
                            String word = objSnapshot.child("word").getValue().toString();
                            Content content = new Content(word, img, syllable, null,false);
                            listContent.add(content);

                        }
                    }
                }

                Collections.shuffle(listContent);
                l.add(listContent.get(0));

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

    private void alertFinishGame(String wordImg,String word){

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
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        final View finishGamePopUp = getLayoutInflater().inflate(R.layout.finish_game,null);

        ImageView img = (ImageView) finishGamePopUp.findViewById(R.id.img);
        Button btn = (Button) finishGamePopUp.findViewById(R.id.btn);

        tts.speak(word, TextToSpeech.QUEUE_FLUSH, null);
        Glide.with(context).load(wordImg).into(img);

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

        databaseReference = FirebaseDatabase.getInstance().getReference();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        namePatient = preferences.getString("userPatient","null").toLowerCase(Locale.ROOT);


        listSylable = new ArrayList<>();
        l = new ArrayList<>();

    }

    public void getInfo(View view){
        tts.speak("Coloca las piezas en los recuadros amarillos para formar una palabra", TextToSpeech.QUEUE_FLUSH, null);
    }

    private void music(){
        boolean valor = getIntent().getExtras().getBoolean("music");
        if(valor){
            AudioPlay.restart();
        }
    }

    private void getSettings(){
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
        music();
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        if(myToast != null){
            myToast.cancel();
        }
        super.onDestroy();
    }
}