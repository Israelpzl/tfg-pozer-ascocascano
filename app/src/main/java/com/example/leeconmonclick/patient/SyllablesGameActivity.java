package com.example.leeconmonclick.patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.leeconmonclick.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
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
    private boolean isIntersectPuzzle1, isIntersectPuzzle2, isIntersectPuzzle3, isIntersectPuzzle4,isIntersectPuzzle5 = false;
    private List<Syllable> listSylable;
    private  List<Content> l;
    private boolean first,second;
    private AlertDialog alertDialog;
    private AlertDialog.Builder alertDialogBuilder;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syllables_game);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        findElements();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        namePatient = preferences.getString("userPatient","null");

        listSylable = new ArrayList<>();
        l = new ArrayList<>();


        databaseReference.child("userPatient").child(namePatient).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String icon = snapshot.child("icon").getValue().toString();
                databaseReference.child("iconPatient").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Glide.with(context).load(snapshot.child(icon).getValue().toString()).into(iconPatient);
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

                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    puzzle1.setX(event.getRawX() - puzzle1.getWidth() / 2);
                    puzzle1.setY(event.getRawY() - puzzle1.getHeight() / 2);

                    Rect rect1 = new Rect();
                    puzzle1.getHitRect(rect1);
                    Rect rect2 = new Rect();
                    yellow.getHitRect(rect2);
                    Rect rect3 = new Rect();
                    yellow2.getHitRect(rect3);

                    String[] sy = l.get(0).getSyllables().split("-");

                    if (Rect.intersects(rect1, rect2)) {
                        if (!isIntersectPuzzle1) {

                            if(puzzle1.getTag().toString().equals(sy[0].toLowerCase(Locale.ROOT))){
                                first = true;
                            }

                            isIntersectPuzzle1 = true;
                        }
                    } else if (Rect.intersects(rect1,rect3)){
                        if (!isIntersectPuzzle1) {
                            if(puzzle1.getTag().toString().equals(sy[1].toLowerCase(Locale.ROOT))){
                                second = true;
                            }
                            isIntersectPuzzle1 = true;
                        }
                    }else{
                        if (isIntersectPuzzle1) {
                            isIntersectPuzzle1 = false;
                        }
                    }

                    if (first && second){
                        alertFinishGame();
                    }
                }
                return true;
            }
        });

        puzzle2.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    puzzle2.setX(event.getRawX() - puzzle2.getWidth() / 2);
                    puzzle2.setY(event.getRawY() - puzzle2.getHeight() / 2);

                    Rect rect1 = new Rect();
                    puzzle2.getHitRect(rect1);
                    Rect rect2 = new Rect();
                    yellow.getHitRect(rect2);
                    Rect rect3 = new Rect();
                    yellow2.getHitRect(rect3);

                    String[] sy = l.get(0).getSyllables().split("-");

                    if (Rect.intersects(rect1, rect2)) {
                        if (!isIntersectPuzzle2) {
                            if(puzzle2.getTag().toString().equals(sy[0].toLowerCase(Locale.ROOT))){
                                first = true;
                            }
                            isIntersectPuzzle2 = true;
                        }
                    } else if (Rect.intersects(rect1,rect3)){
                        if (!isIntersectPuzzle2) {
                            if(puzzle2.getTag().toString().equals(sy[1].toLowerCase(Locale.ROOT))){
                                second = true;
                            }
                            isIntersectPuzzle2 = true;
                        }
                    }else{
                        if (isIntersectPuzzle2) {
                            isIntersectPuzzle2 = false;
                        }
                    }

                    if (first && second){
                        alertFinishGame();
                    }
                }
                return true;
            }
        });

        puzzle3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    puzzle3.setX(event.getRawX() - puzzle3.getWidth() / 2);
                    puzzle3.setY(event.getRawY() - puzzle3.getHeight() / 2);

                    Rect rect1 = new Rect();
                    puzzle3.getHitRect(rect1);
                    Rect rect2 = new Rect();
                    yellow.getHitRect(rect2);
                    Rect rect3 = new Rect();
                    yellow2.getHitRect(rect3);

                    String[] sy = l.get(0).getSyllables().split("-");

                    if (Rect.intersects(rect1, rect2)) {
                        if (!isIntersectPuzzle3) {
                            if(puzzle3.getTag().toString().equals(sy[0].toLowerCase(Locale.ROOT))){
                                first = true;
                            }
                            isIntersectPuzzle3 = true;
                        }
                    } else if (Rect.intersects(rect1,rect3)){
                        if (!isIntersectPuzzle3) {

                            if(puzzle3.getTag().toString().equals(sy[1].toLowerCase(Locale.ROOT))){
                                second = true;
                            }
                            isIntersectPuzzle3 = true;
                        }
                    }else{
                        if (isIntersectPuzzle3) {
                            isIntersectPuzzle3 = false;
                        }
                    }

                    if (first && second){
                        alertFinishGame();
                    }
                }
                return true;
            }
        });

        puzzle4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    puzzle4.setX(event.getRawX() - puzzle4.getWidth() / 2);
                    puzzle4.setY(event.getRawY() - puzzle4.getHeight() / 2);

                    Rect rect1 = new Rect();
                    puzzle4.getHitRect(rect1);
                    Rect rect2 = new Rect();
                    yellow.getHitRect(rect2);
                    Rect rect3 = new Rect();
                    yellow2.getHitRect(rect3);

                    String[] sy = l.get(0).getSyllables().split("-");

                    if (Rect.intersects(rect1, rect2)) {
                        if (!isIntersectPuzzle4) {

                            if(puzzle4.getTag().toString().equals(sy[0].toLowerCase(Locale.ROOT))){
                                first = true;
                            }
                            isIntersectPuzzle4 = true;
                        }
                    } else if (Rect.intersects(rect1,rect3)){
                        if (!isIntersectPuzzle4) {
                            if(puzzle4.getTag().toString().equals(sy[1].toLowerCase(Locale.ROOT))){
                                second = true;
                            }
                            isIntersectPuzzle4 = true;
                        }
                    }else{
                        if (isIntersectPuzzle4) {
                            isIntersectPuzzle4 = false;
                        }
                    }
                    if (first && second){
                        alertFinishGame();
                    }
                }

                return true;
            }
        });

        puzzle5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    puzzle5.setX(event.getRawX() - puzzle5.getWidth() / 2);
                    puzzle5.setY(event.getRawY() - puzzle5.getHeight() / 2);

                    Rect rect1 = new Rect();
                    puzzle5.getHitRect(rect1);
                    Rect rect2 = new Rect();
                    yellow.getHitRect(rect2);
                    Rect rect3 = new Rect();
                    yellow2.getHitRect(rect3);

                    String[] sy = l.get(0).getSyllables().split("-");

                    if (Rect.intersects(rect1, rect2)) {
                        if (!isIntersectPuzzle5) {
                            if(puzzle5.getTag().toString().equals(sy[0].toLowerCase(Locale.ROOT))){
                                first = true;
                            }
                            isIntersectPuzzle5 = true;
                        }
                    } else if (Rect.intersects(rect1,rect3)){
                        if (!isIntersectPuzzle5) {
                            if(puzzle5.getTag().toString().equals(sy[1].toLowerCase(Locale.ROOT))){
                                second = true;
                            }
                            isIntersectPuzzle5 = true;
                        }
                    }else{
                        if (isIntersectPuzzle5) {
                            isIntersectPuzzle5 = false;
                        }
                    }

                    if (first && second){
                        alertFinishGame();
                    }
                }
                return true;
            }
        });



        initBBDD();



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
        alertDialogBuilder = new AlertDialog.Builder(this);
        final View finishGamePopUp = getLayoutInflater().inflate(R.layout.finish_game,null);

        ImageView img = (ImageView) finishGamePopUp.findViewById(R.id.img);
        Button btn = (Button) finishGamePopUp.findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Glide.with(context).load(l.get(0).getImg()).into(img);

        alertDialogBuilder.setView(finishGamePopUp);
        alertDialog =  alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
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
}