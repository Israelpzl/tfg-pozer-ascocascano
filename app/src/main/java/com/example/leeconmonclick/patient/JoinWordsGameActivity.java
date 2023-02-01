package com.example.leeconmonclick.patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.leeconmonclick.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.leerconmonclick.util.LineView;

public class JoinWordsGameActivity extends AppCompatActivity {

    private CardView cardViewImage1,cardViewImage2,cardViewImage3;
    private CardView cardViewWord1,cardViewWord2,cardViewWord3;
    private ImageView imageView1,imageView2,imageView3;
    private ImageView imageSelect1,imageSelect2,imageSelect3;
    private ImageView wordSelect1,wordSelect2,wordSelect3;
    private TextView word1,word2,word3;
    private String img,word;
    private boolean select;
    private int points;
    private AlertDialog alertDialog;
    private AlertDialog.Builder alertDialogBuilder;
    private float cardImageX,cardImageY,cardWordX,cardWordY;
    private Context context = this;
    private boolean cardWordFinish1, cardWordFinish2, cardWordFinish3 = false;
    private boolean cardImageFinish1, cardImageFinish2, cardImageFinish3 = false;
    private int numSelect;
    private List<String> listImg,listWord;
    private final float OPACITY = (float) 0.2;
    private DatabaseReference databaseReference;
    private CircleImageView iconPatient;
    private Bundle data;
    private String category;
    private String difficultySelect;
    private String namePatient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_words_game);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        data = getIntent().getExtras();
        category = data.getString("category");
        difficultySelect = data.getString("difficulty");
        findElement();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        namePatient = preferences.getString("userPatient","null");

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
                        setContentView(R.layout.activity_error2);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                setContentView(R.layout.activity_error2);
            }
        });

        initBBDD();
        listenerClickSelect();

    }


    private void listenerClickSelect(){

        listImg = new ArrayList<>();
        listWord = new ArrayList<>();
        points = 0;

        cardViewImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cardImageFinish1){
                    imageSelect1.setVisibility(View.VISIBLE);
                    imageSelect2.setVisibility(View.INVISIBLE);
                    imageSelect3.setVisibility(View.INVISIBLE);
                    img = listImg.get(0);
                    select = true;
                    numSelect =1;

                    int[] loc = new int[2];
                    cardViewImage1.getLocationOnScreen(loc);
                    cardImageX = loc[0];
                    cardImageY = loc[1];
                }

            }
        });

        cardViewImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!cardImageFinish2){
                    imageSelect1.setVisibility(View.INVISIBLE);
                    imageSelect2.setVisibility(View.VISIBLE);
                    imageSelect3.setVisibility(View.INVISIBLE);
                    img = listImg.get(1);
                    select = true;
                    numSelect =2;

                    int[] loc = new int[2];
                    cardViewImage2.getLocationOnScreen(loc);
                    cardImageX = loc[0];
                    cardImageY = loc[1];
                }

            }
        });

        cardViewImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cardImageFinish3){
                    imageSelect1.setVisibility(View.INVISIBLE);
                    imageSelect2.setVisibility(View.INVISIBLE);
                    imageSelect3.setVisibility(View.VISIBLE);
                    img = listImg.get(2);
                    select = true;
                    numSelect =3;

                    int[] loc = new int[2];
                    cardViewImage3.getLocationOnScreen(loc);
                    cardImageX = loc[0];
                    cardImageY = loc[1];
                }

            }
        });


        cardViewWord1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (select && !cardWordFinish1) {
                    wordSelect1.setVisibility(View.VISIBLE);
                    wordSelect2.setVisibility(View.INVISIBLE);
                    wordSelect3.setVisibility(View.INVISIBLE);
                    word = listWord.get(0);

                    if (img.equals(word)){
                        check(1);
                    }else{
                        Toast.makeText(getApplicationContext(), "Fallado", Toast.LENGTH_LONG).show();
                        word = "";
                        wordSelect1.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

        cardViewWord2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (select && !cardWordFinish2) {
                    wordSelect1.setVisibility(View.INVISIBLE);
                    wordSelect2.setVisibility(View.VISIBLE);
                    wordSelect3.setVisibility(View.INVISIBLE);
                    word = listWord.get(1);
                    if (img.equals(word)) {
                        check(2);
                    }else{
                        Toast.makeText(getApplicationContext(), "Fallado", Toast.LENGTH_LONG).show();
                        word = "";
                        wordSelect2.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

        cardViewWord3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                if (select && !cardWordFinish3) {
                    wordSelect1.setVisibility(View.INVISIBLE);
                    wordSelect2.setVisibility(View.INVISIBLE);
                    wordSelect3.setVisibility(View.VISIBLE);
                    word = listWord.get(2);
                    if (img.equals(word)){
                        check(3);
                    }else{
                        Toast.makeText(getApplicationContext(), "Fallado", Toast.LENGTH_LONG).show();
                        word = "";
                        wordSelect3.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
    }


    private void check(int num){

        points++;

        int[] loc = new int[2];
        switch (num){
            case 1:{
                cardViewWord1.getLocationOnScreen(loc);
                cardWordFinish1 = true;
                wordSelect1.setVisibility(View.INVISIBLE);
                cardViewWord1.setAlpha(OPACITY);
                break;
            } case 2:{
                cardViewWord2.getLocationOnScreen(loc);
                cardWordFinish2 = true;
                wordSelect2.setVisibility(View.INVISIBLE);
                cardViewWord2.setAlpha(OPACITY);
                break;
            } case 3:{
                cardViewWord3.getLocationOnScreen(loc);
                cardWordFinish3 = true;
                wordSelect3.setVisibility(View.INVISIBLE);
                cardViewWord3.setAlpha(OPACITY);
                break;
            }
        }


        cardWordX = loc[0];
        cardWordY = loc[1];
        LineView lineView = new LineView(context,cardImageX+350,cardImageY+75,cardWordX-385,cardWordY+150);
        addContentView(lineView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));

        switch (numSelect){
            case 1:{
                cardViewImage1.setAlpha(OPACITY);
                cardImageFinish1 = true;
                imageSelect1.setVisibility(View.INVISIBLE);
                break;
            }
            case 2:{
                cardViewImage2.setAlpha(OPACITY);
                cardImageFinish2 = true;
                imageSelect2.setVisibility(View.INVISIBLE);
                break;
            } case 3: {
                cardViewImage3.setAlpha(OPACITY);
                cardImageFinish3 = true;
                imageSelect3.setVisibility(View.INVISIBLE);
                break;
            }
        }

        if (points == 3){
            Toast.makeText(getApplicationContext(), "Juego Terminado", Toast.LENGTH_LONG).show();
            alertFinishGame();

        }

    }

    public void refreshBBDD(View v){   Toast.makeText(getApplicationContext(), "Cargando nuevo contenido...", Toast.LENGTH_LONG).show();
        recreate();
    }


    private void findElement(){

        cardViewImage1 = findViewById(R.id.cardViewImage1);
        cardViewImage2 = findViewById(R.id.cardViewImage2);
        cardViewImage3 = findViewById(R.id.cardViewImage3);

        imageView1 = findViewById(R.id.image1);
        imageView2 = findViewById(R.id.image2);
        imageView3 = findViewById(R.id.image3);

        imageSelect1 = findViewById(R.id.imageSelect1);
        imageSelect2 = findViewById(R.id.imageSelect2);
        imageSelect3 = findViewById(R.id.imageSelect3);

        cardViewWord1 = findViewById(R.id.cardViewWord1);
        cardViewWord2 = findViewById(R.id.cardViewWord2);
        cardViewWord3 = findViewById(R.id.cardViewWord3);

        word1 = findViewById(R.id.word1);
        word2 = findViewById(R.id.word2);
        word3 = findViewById(R.id.word3);

        wordSelect1 = findViewById(R.id.wordSelect1);
        wordSelect2 = findViewById(R.id.wordSelect2);
        wordSelect3 = findViewById(R.id.wordSelect3);

        iconPatient = findViewById(R.id.iconPatientId);

    }


    private void alertFinishGame(){
        alertDialogBuilder = new AlertDialog.Builder(this);
        final View finishGamePopUp = getLayoutInflater().inflate(R.layout.finish_game,null);
        Button btn = (Button) finishGamePopUp.findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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

    private void  initBBDD () {

        List<String> contentList = new ArrayList<>();

        databaseReference.child("content").child(category).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot objDataSnapshot : snapshot.getChildren()){
                    String w = (String) objDataSnapshot.child("word").getValue();
                    String difficulty = objDataSnapshot.child("difficulty").getValue().toString();

                    if (difficulty.equals(difficultySelect)){
                        contentList.add(w);
                    }else if (difficultySelect.equals("PRÁCTICA")){
                        contentList.add(w);
                    }

                }

                for (int i = 0; i<3;i++){
                    int index = (int)Math.floor(Math.random()*(contentList.size()-1));
                    listImg.add(contentList.get(index));
                    listWord.add(contentList.get(index));
                    contentList.remove(index);
                }

                Collections.shuffle(listImg);
                Collections.shuffle(listWord);

                word1.setText(listWord.get(0));
                word2.setText(listWord.get(1));
                word3.setText(listWord.get(2));

                Glide.with(context).load(snapshot.child(listImg.get(0)).child("img").getValue().toString()).into(imageView1);
                Glide.with(context).load(snapshot.child(listImg.get(1)).child("img").getValue().toString()).into(imageView2);
                Glide.with(context).load(snapshot.child(listImg.get(2)).child("img").getValue().toString()).into(imageView3);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                setContentView(R.layout.activity_error2);
            }
        });

    }


    public void goBack(View v){
        finish();
    }
}