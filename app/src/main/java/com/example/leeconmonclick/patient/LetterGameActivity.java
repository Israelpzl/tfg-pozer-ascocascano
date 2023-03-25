package com.example.leeconmonclick.patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.view.View;
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
import es.leerconmonclick.util.AudioPlay;


public class LetterGameActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private CircleImageView iconPatient;
    private final Context context =this;
    private ImageView image1, image2,image3, imgLetter;
    private ImageView imageSelect1,imageSelect2,imageSelect3;
    private CardView cardViewImg1, cardViewImg2,cardViewImg3;
    private List<String> listImg;
    private String correctWord;
    private AlertDialog alertDialog;
    private String category;
    private String difficultySelect;
    private String imgFinish;
    private String namePatient;
    private Bundle data;
    private ImageButton nextPicture;
    private int countSucces,countFailed = 0;
    private TextToSpeech tts;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_letter_game);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        findElement();
        getSettings();
        music();

        nextPicture.setVisibility(View.GONE);
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

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i != TextToSpeech.ERROR){
                    tts.setLanguage(Locale.getDefault());
                }

            }
        });



        initBBDD ();
        countSucces = data.getInt("succes");
        countFailed = data.getInt("failed");
        listenerOnclick();

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                Intent intent = new Intent(LetterGameActivity.this, ErrorActivity.class);
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

    private void listenerOnclick (){

        cardViewImg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = listImg.get(0);
                tts.speak(word, TextToSpeech.QUEUE_FLUSH, null);
                imageSelect1.setVisibility(View.VISIBLE);
                imageSelect2.setVisibility(View.INVISIBLE);
                imageSelect3.setVisibility(View.INVISIBLE);

                nextPicture.setVisibility(View.VISIBLE);
                nextPicture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkWord(word);
                    }
                });
            }
        });

        cardViewImg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = listImg.get(1);
                tts.speak(word, TextToSpeech.QUEUE_FLUSH, null);
                imageSelect1.setVisibility(View.INVISIBLE);
                imageSelect2.setVisibility(View.VISIBLE);
                imageSelect3.setVisibility(View.INVISIBLE);

                nextPicture.setVisibility(View.VISIBLE);
                nextPicture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkWord(word);
                    }
                });
            }
        });

        cardViewImg3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = listImg.get(2);
                tts.speak(word, TextToSpeech.QUEUE_FLUSH, null);
                imageSelect1.setVisibility(View.INVISIBLE);
                imageSelect2.setVisibility(View.INVISIBLE);
                imageSelect3.setVisibility(View.VISIBLE);

                nextPicture.setVisibility(View.VISIBLE);
                nextPicture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkWord(word);
                    }
                });
            }
        });
    }

    public void refreshBBDD(View v){
        Toast.makeText(getApplicationContext(), "Cargando nuevo contenido...", Toast.LENGTH_LONG).show();
        recreate();
    }

    private void goLetterGame(boolean c){
        Intent lettersIntent = new Intent(context, LetterGameActivity.class);
        lettersIntent.putExtra("category", category);
        lettersIntent.putExtra("difficulty", difficultySelect);
        lettersIntent.putExtra("numberGame",  data.getInt("numberGame")+ 1);
        lettersIntent.putExtra("music", AudioPlay.isIsplayingAudio());
        lettersIntent.putExtra("succes",  countSucces);
        lettersIntent.putExtra("failed",  countFailed);

        if(data.getInt("numberGame") == 0){
            lettersIntent.putExtra("img1b",c);
            lettersIntent.putExtra("img1",  imgFinish);
        }else if(data.getInt("numberGame")== 1){
            lettersIntent.putExtra("img1",  data.getString("img1"));
            lettersIntent.putExtra("img1b",data.getBoolean("img1b"));
            lettersIntent.putExtra("img2b",c);
            lettersIntent.putExtra("img2",  imgFinish);
        }

        startActivity(lettersIntent);
        finish();
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
                Collections.shuffle(contentList);
                int i = 0;
                for (String content :contentList){
                    if(i == 0){
                        listImg.add(content);
                        i++;
                    }else if (i<3){
                        if (!(listImg.get(0).charAt(0) == content.charAt(0))){
                            i++;
                            listImg.add(content);
                        }
                    }else{
                        break;
                    }
                }
                selectLetter(listImg.get(0).charAt(0));

                correctWord = listImg.get(0);
                imgFinish =snapshot.child(listImg.get(0)).child("img").getValue().toString();

                Collections.shuffle(listImg);

                Glide.with(context).load(snapshot.child(listImg.get(0)).child("img").getValue().toString()).into(image1);
                Glide.with(context).load(snapshot.child(listImg.get(1)).child("img").getValue().toString()).into(image2);
                Glide.with(context).load(snapshot.child(listImg.get(2)).child("img").getValue().toString()).into(image3);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkWord(String word){
        if (correctWord.equals(word)){
            countSucces++;
            if (data.getInt("numberGame")>=2){
                alertFinishGame(true);
            }else{
                goLetterGame(true);
            }

        }else{
            countFailed++;
            if (data.getInt("numberGame")>=2){
                alertFinishGame(false);
            }else{
                goLetterGame(false);
            }
        }
    }

    private void alertFinishGame(boolean b){

        databaseReference.child("userPatient").child(namePatient).child("stadistic").child("letters").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                countSucces = snapshot.child("difficulties").child(difficultySelect).child("succes").getValue(Integer.class) +  countSucces;

                countFailed = snapshot.child("difficulties").child(difficultySelect).child("failed").getValue(Integer.class) + countFailed;

                int t = snapshot.child("timesPlayed").getValue(Integer.class);
                t++;

                int z = snapshot.child("difficulties").child(difficultySelect).child("timesPlayed").getValue(Integer.class);
                z++;

                int c = snapshot.child("categories").child(category).child("timesPlayed").getValue(Integer.class);
                c++;


                if (!difficultySelect.equals("PRÁCTICA")){
                    databaseReference.child("userPatient").child(namePatient).child("stadistic").child("letters").child("timesPlayed").setValue(t);
                    databaseReference.child("userPatient").child(namePatient).child("stadistic").child("letters").child("categories").child(category).child("timesPlayed").setValue(c);
                }

               databaseReference.child("userPatient").child(namePatient).child("stadistic").child("letters").child("difficulties").child(difficultySelect).child("succes").setValue(countSucces);
               databaseReference.child("userPatient").child(namePatient).child("stadistic").child("letters").child("difficulties").child(difficultySelect).child("timesPlayed").setValue(z);
               databaseReference.child("userPatient").child(namePatient).child("stadistic").child("letters").child("difficulties").child(difficultySelect).child("failed").setValue(countFailed);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        final View finishGamePopUp = getLayoutInflater().inflate(R.layout.finish_game_letters,null);

        ImageView img1 = (ImageView) finishGamePopUp.findViewById(R.id.img);
        ImageView img2 = (ImageView) finishGamePopUp.findViewById(R.id.img2);
        ImageView img3 = (ImageView) finishGamePopUp.findViewById(R.id.img3);

        ImageView succes1 = (ImageView) finishGamePopUp.findViewById(R.id.succes1);
        ImageView succes2 = (ImageView) finishGamePopUp.findViewById(R.id.succes2);
        ImageView succes3 = (ImageView) finishGamePopUp.findViewById(R.id.succes3);

        Button btn = (Button) finishGamePopUp.findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                finish();
            }
        });

        Glide.with(getApplicationContext()).load(data.getString("img1")).into(img1);
        Glide.with(getApplicationContext()).load(data.getString("img2")).into(img2);
        Glide.with(getApplicationContext()).load(imgFinish).into(img3);

        if(!data.getBoolean("img1b")){
            succes1.setImageResource(R.drawable.failed_letters);
        }
        if(!data.getBoolean("img2b")){
            succes2.setImageResource(R.drawable.failed_letters);
        }

        if(!b){
            succes3.setImageResource(R.drawable.failed_letters);
        }


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

    private void getSettings(){
        final ConstraintLayout constraintLayout;
        constraintLayout =  findViewById(R.id.letterGame);

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

    private void findElement(){

        iconPatient = findViewById(R.id.iconPatientId);
        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        image3 = findViewById(R.id.image3);
        imgLetter = findViewById(R.id.letter);
        cardViewImg1 = findViewById(R.id.cardViewImage1);
        cardViewImg2 = findViewById(R.id.cardViewImage2);
        cardViewImg3 = findViewById(R.id.cardViewImage3);
        nextPicture = findViewById(R.id.button10);
        imageSelect1 = findViewById(R.id.imageSelectd1);
        imageSelect2 = findViewById(R.id.imageSelectd2);
        imageSelect3 = findViewById(R.id.imageSelectd3);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        data = getIntent().getExtras();
        category = data.getString("category");
        difficultySelect = data.getString("difficulty");
        listImg = new ArrayList<>();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        namePatient = preferences.getString("userPatient","null").toLowerCase(Locale.ROOT);
    }

    private void selectLetter (char c){

        switch (c){
            case 'A': {
                imgLetter.setImageResource(R.drawable.letra_a);
                break;
            }
            case 'B': {
                imgLetter.setImageResource(R.drawable.letra_b);
                break;
            }
            case 'C': {
                imgLetter.setImageResource(R.drawable.letra_c);
                break;
            }
            case 'D': {
                imgLetter.setImageResource(R.drawable.letra_d);
                break;
            }
            case 'E': {
                imgLetter.setImageResource(R.drawable.letra_e);
                break;
            }
            case 'F': {
                imgLetter.setImageResource(R.drawable.letra_f);
                break;
            }
            case 'G': {
                imgLetter.setImageResource(R.drawable.letra_g);
                break;
            }
            case 'H': {
                imgLetter.setImageResource(R.drawable.letra_h);
                break;
            }
            case 'I': {
                imgLetter.setImageResource(R.drawable.letra_i);
                break;
            }
            case 'J': {
                imgLetter.setImageResource(R.drawable.letra_j);
                break;
            }
            case 'K': {
                imgLetter.setImageResource(R.drawable.letra_k);
                break;
            }
            case 'L': {
                imgLetter.setImageResource(R.drawable.letra_l);
                break;
            }
            case 'M': {
                imgLetter.setImageResource(R.drawable.letra_m);
                break;
            }
            case 'N': {
                imgLetter.setImageResource(R.drawable.letra_n);
                break;
            }
            case 'O': {
                imgLetter.setImageResource(R.drawable.letra_o);
                break;
            }
            case 'P': {
                imgLetter.setImageResource(R.drawable.letra_p);
                break;
            } case 'Q': {
                imgLetter.setImageResource(R.drawable.letra_q);
                break;
            } case 'R': {
                imgLetter.setImageResource(R.drawable.letra_r);
                break;
            }
            case 'S': {
                imgLetter.setImageResource(R.drawable.letra_s);
                break;
            } case 'T': {
                imgLetter.setImageResource(R.drawable.letra_t);
                break;
            } case 'U': {
                imgLetter.setImageResource(R.drawable.letra_u);
                break;
            }
            case 'V': {
                imgLetter.setImageResource(R.drawable.letra_v);
                break;
            }
            case 'W': {
                imgLetter.setImageResource(R.drawable.letra_w);
                break;
            }
            case 'X': {
                imgLetter.setImageResource(R.drawable.letra_x);
                break;
            }
            case 'Z': {
                imgLetter.setImageResource(R.drawable.letra_z);
                break;
            }
        }
    }

    public void getInfo(View view){
        tts.speak("Escoge la imágen que empiece por la letra mostrada en pantalla", TextToSpeech.QUEUE_FLUSH, null);
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


    public void goBack(View v){
        finish();
    }
}