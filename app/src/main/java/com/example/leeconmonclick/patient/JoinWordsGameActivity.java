package com.example.leeconmonclick.patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.example.leeconmonclick.ErrorActivity;
import com.example.leeconmonclick.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import es.leerconmonclick.util.AudioPlay;
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
    private float cardImageX;
    private float cardImageY;
    private final Context context = this;
    private boolean cardWordFinish1, cardWordFinish2, cardWordFinish3 = false;
    private boolean cardImageFinish1, cardImageFinish2, cardImageFinish3 = false;
    private int numSelect;
    private List<String> listImg,listWord;
    private DatabaseReference databaseReference;
    private CircleImageView iconPatient;
    private String category;
    private String difficultySelect;
    private String namePatient;
    private int countSucces,countFailed = 0;
    private ImageButton refresh;

    private TextToSpeech tts;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_words_game);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        Bundle data = getIntent().getExtras();
        category = data.getString("category");
        difficultySelect = data.getString("difficulty");
        findElement();
        getSettings();
        music();


         tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
               if(i != TextToSpeech.ERROR){
                   tts.setLanguage(Locale.getDefault());
               }

            }
        });

        if (!difficultySelect.equals("PRÁCTICA")){
            refresh.setVisibility(View.INVISIBLE);
        }


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

        initBBDD();
        listenerClickSelect();

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                Intent intent = new Intent(JoinWordsGameActivity.this, ErrorActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                System.exit(1);
            }
        });
    }

    private void music(){
        boolean valor = getIntent().getExtras().getBoolean("music");
        if(valor){
            AudioPlay.restart();
        }
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
                    tts.speak(img, TextToSpeech.QUEUE_FLUSH, null);
                    select = true;
                    numSelect =1;


                    cardImageX = cardViewImage1.getLeft() + cardViewImage1.getWidth() / 2;
                    cardImageY = cardViewImage1.getTop() + cardViewImage1.getHeight() / 2;
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
                    tts.speak(img, TextToSpeech.QUEUE_FLUSH, null);
                    select = true;
                    numSelect =2;


                    cardImageX = cardViewImage2.getLeft() + cardViewImage2.getWidth() / 2;
                    cardImageY = cardViewImage2.getTop() + cardViewImage2.getHeight() / 2;
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
                    tts.speak(img, TextToSpeech.QUEUE_FLUSH, null);
                    select = true;
                    numSelect =3;


                    cardImageX = cardViewImage3.getLeft() + cardViewImage3.getWidth() / 2;
                    cardImageY = cardViewImage3.getTop() + cardViewImage3.getHeight() / 2;
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
                    tts.speak(word, TextToSpeech.QUEUE_FLUSH, null);

                    if (img.equals(word)){
                        check(1);
                    }else{
                        tts.speak("fallado, inténtelo de nuevo", TextToSpeech.QUEUE_FLUSH, null);
                        countFailed++;
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
                    tts.speak(word, TextToSpeech.QUEUE_FLUSH, null);
                    if (img.equals(word)) {
                        check(2);
                    }else{
                        tts.speak("fallado, inténtelo de nuevo", TextToSpeech.QUEUE_FLUSH, null);
                        countFailed++;
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
                    tts.speak(word, TextToSpeech.QUEUE_FLUSH, null);
                    if (img.equals(word)){
                        check(3);
                    }else{
                        tts.speak("fallado, inténtelo de nuevo", TextToSpeech.QUEUE_FLUSH, null);
                        countFailed++;
                        word = "";
                        wordSelect3.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
    }


    private void check(int num){

        points++;

        float OPACITY = (float) 0.2;
        switch (num){
            case 1:{
                cardWordFinish1 = true;
                wordSelect1.setVisibility(View.INVISIBLE);
                cardViewWord1.setAlpha(OPACITY);
                break;
            } case 2:{
                cardWordFinish2 = true;
                wordSelect2.setVisibility(View.INVISIBLE);
                cardViewWord2.setAlpha(OPACITY);
                break;
            } case 3:{
                cardWordFinish3 = true;
                wordSelect3.setVisibility(View.INVISIBLE);
                cardViewWord3.setAlpha(OPACITY);
                break;
            }
        }




        switch (numSelect){
            case 1:{
                cardViewImage1.setAlpha(OPACITY);
                cardImageFinish1 = true;
                imageSelect1.setVisibility(View.INVISIBLE);

                if(num==1){
                    drawLineBetweenCardViews(cardViewImage1,cardViewWord1);
                }else if(num==2){
                    drawLineBetweenCardViews(cardViewImage1,cardViewWord2);
                }else if(num==3){
                    drawLineBetweenCardViews(cardViewImage1,cardViewWord3);
                }

                break;
            }
            case 2:{
                cardViewImage2.setAlpha(OPACITY);
                cardImageFinish2 = true;
                imageSelect2.setVisibility(View.INVISIBLE);

                if(num==1){
                    drawLineBetweenCardViews(cardViewImage2,cardViewWord1);
                }else if(num==2){
                    drawLineBetweenCardViews(cardViewImage2,cardViewWord2);
                }else if(num==3){
                    drawLineBetweenCardViews(cardViewImage2,cardViewWord3);
                }

                break;
            } case 3: {
                cardViewImage3.setAlpha(OPACITY);
                cardImageFinish3 = true;
                imageSelect3.setVisibility(View.INVISIBLE);


                if(num==1){
                    drawLineBetweenCardViews(cardViewImage3,cardViewWord1);
                }else if(num==2){
                    drawLineBetweenCardViews(cardViewImage3,cardViewWord2);
                }else if(num==3){
                    drawLineBetweenCardViews(cardViewImage3,cardViewWord3);
                }

                break;
            }
        }

        if (points == 3){
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

        refresh = findViewById(R.id.refresh);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        namePatient = preferences.getString("userPatient","null").toLowerCase(Locale.ROOT);

    }

    private void drawLineBetweenCardViews(CardView cardView1, CardView cardView2) {
        int[] cardView1Location = new int[2];
        cardView1.getLocationOnScreen(cardView1Location);
        int cardView1CenterX = cardView1Location[0] + cardView1.getWidth() / 2;
        int cardView1CenterY = cardView1Location[1] + cardView1.getHeight() / 2;

        int[] cardView2Location = new int[2];
        cardView2.getLocationOnScreen(cardView2Location);
        int cardView2CenterX = cardView2Location[0] + cardView2.getWidth() / 2;
        int cardView2CenterY = cardView2Location[1] + cardView2.getHeight() / 2;

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(10);

        View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        Bitmap bitmap = Bitmap.createBitmap(rootView.getWidth(), rootView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        canvas.drawLine(cardView1CenterX, cardView1CenterY, cardView2CenterX, cardView2CenterY, paint);

        ImageView imageView = new ImageView(this);
        imageView.setImageBitmap(bitmap);

        addContentView(imageView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }


    private void alertFinishGame(){

        databaseReference.child("userPatient").child(namePatient).child("stadistic").child("joinWords").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                countSucces = snapshot.child("difficulties").child(difficultySelect).child("succes").getValue(Integer.class) + 3;

                countFailed = snapshot.child("difficulties").child(difficultySelect).child("failed").getValue(Integer.class) + countFailed;

                int t = snapshot.child("timesPlayed").getValue(Integer.class) + 1;

                int z = snapshot.child("difficulties").child(difficultySelect).child("timesPlayed").getValue(Integer.class) + 1;

                int c = snapshot.child("categories").child(category).child("timesPlayed").getValue(Integer.class) + 1;


                if (!difficultySelect.equals("PRÁCTICA")){
                    databaseReference.child("userPatient").child(namePatient).child("stadistic").child("joinWords").child("timesPlayed").setValue(t);
                    databaseReference.child("userPatient").child(namePatient).child("stadistic").child("joinWords").child("categories").child(category).child("timesPlayed").setValue(c);
                }

                databaseReference.child("userPatient").child(namePatient).child("stadistic").child("joinWords").child("difficulties").child(difficultySelect).child("succes").setValue(countSucces);
                databaseReference.child("userPatient").child(namePatient).child("stadistic").child("joinWords").child("difficulties").child(difficultySelect).child("timesPlayed").setValue(z);
                databaseReference.child("userPatient").child(namePatient).child("stadistic").child("joinWords").child("difficulties").child(difficultySelect).child("failed").setValue(countFailed);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        final View finishGamePopUp = getLayoutInflater().inflate(R.layout.finish_game,null);
        Button btn = (Button) finishGamePopUp.findViewById(R.id.btn);

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

            }
        });

    }

    private void getSettings(){
        final ConstraintLayout constraintLayout;
        constraintLayout =  findViewById(R.id.wordsGame);

        databaseReference.child("userPatient").child(namePatient).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String size = snapshot.child("sett").child("0").getValue().toString();
                switch (size) {
                    case "grande":
                        word1.setTextSize(30);
                        word2.setTextSize(30);
                        word3.setTextSize(30);
                        break;
                    case "normal":
                        word1.setTextSize(20);
                        word2.setTextSize(20);
                        word3.setTextSize(20);
                        break;
                    case "peque":
                        word1.setTextSize(10);
                        word2.setTextSize(10);
                        word3.setTextSize(10);
                        break;
                }
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

    public void getInfo(View view){
        tts.speak("Úna las imágenes con sus palabras", TextToSpeech.QUEUE_FLUSH, null);
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
}