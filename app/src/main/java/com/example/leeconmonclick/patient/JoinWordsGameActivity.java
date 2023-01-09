package com.example.leeconmonclick.patient;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leeconmonclick.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JoinWordsGameActivity extends AppCompatActivity {

    private CardView cardViewImage1,cardViewImage2,cardViewImage3;
    private CardView cardViewWord1,cardViewWord2,cardViewWord3;
    private ImageView imageView1,imageView2,imageView3;
    private ImageView imageSelect1,imageSelect2,imageSelect3;
    private ImageView wordSelect1,wordSelect2,wordSelect3;
    private TextView word1,word2,word3;
    private CardView[] cardViewsImage = new CardView[2];
    private CardView[] cardViewsWord = new CardView[2];
    private String[] arrayImg, arrayWord;
    private String img,word;
    private boolean select;
    private int points;
    private AlertDialog alertDialog;
    private AlertDialog.Builder alertDialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_words_game);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        arrayImg = new String[]{"perro", "gato", "oso"};
        arrayWord = new String[]{"oso", "gato", "perro"};
        points = 0;

        findElement();
        listenerClickSelect();


    }



    private void checkResult(){

        if (img.equals(word)){
            System.out.println("*****************Has ACERTADO*************");
        }else{
            System.out.println("*****************Has FALALDO*************");
        }

    }

    private void listenerClickSelect(){

        cardViewImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageSelect1.setVisibility(View.VISIBLE);
                imageSelect2.setVisibility(View.INVISIBLE);
                imageSelect3.setVisibility(View.INVISIBLE);
                img = arrayImg[0];
                select = true;
            }
        });

        cardViewImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageSelect1.setVisibility(View.INVISIBLE);
                imageSelect2.setVisibility(View.VISIBLE);
                imageSelect3.setVisibility(View.INVISIBLE);
                img = arrayImg[1];
                select = true;
            }
        });

        cardViewImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageSelect1.setVisibility(View.INVISIBLE);
                imageSelect2.setVisibility(View.INVISIBLE);
                imageSelect3.setVisibility(View.VISIBLE);
                img = arrayImg[2];
                select = true;
            }
        });


            cardViewWord1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (select) {
                        wordSelect1.setVisibility(View.VISIBLE);
                        wordSelect2.setVisibility(View.INVISIBLE);
                        wordSelect3.setVisibility(View.INVISIBLE);
                        word = arrayWord [0];
                        if (img.equals(word)){
                            Toast.makeText(getApplicationContext(), "Has Acertado", Toast.LENGTH_LONG).show();
                            points++;
                            cardViewWord1.setVisibility(View.INVISIBLE);
                            if (points == 3){
                                Toast.makeText(getApplicationContext(), "Juego Terminado", Toast.LENGTH_LONG).show();
                                alertFinishGame();
                            }
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
                    if (select) {
                        wordSelect1.setVisibility(View.INVISIBLE);
                        wordSelect2.setVisibility(View.VISIBLE);
                        wordSelect3.setVisibility(View.INVISIBLE);
                        word = arrayWord [1];
                        if (img.equals(word)){
                            Toast.makeText(getApplicationContext(), "Has Acertado", Toast.LENGTH_LONG).show();
                            points++;
                            cardViewWord2.setVisibility(View.INVISIBLE);
                            if (points == 3){
                                Toast.makeText(getApplicationContext(), "Juego Terminado", Toast.LENGTH_LONG).show();
                                alertFinishGame();
                            }
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
                public void onClick(View v) {
                    if(select) {
                        wordSelect1.setVisibility(View.INVISIBLE);
                        wordSelect2.setVisibility(View.INVISIBLE);
                        wordSelect3.setVisibility(View.VISIBLE);
                        word = arrayWord [2];
                        if (img.equals(word)){
                            Toast.makeText(getApplicationContext(), "Has Acertado", Toast.LENGTH_LONG).show();
                            points++;
                            cardViewWord3.setVisibility(View.INVISIBLE);

                            if (points == 3){
                                Toast.makeText(getApplicationContext(), "Juego Terminado", Toast.LENGTH_LONG).show();
                                alertFinishGame();

                            }
                        }else{
                            Toast.makeText(getApplicationContext(), "Fallado", Toast.LENGTH_LONG).show();
                            word = "";
                            wordSelect3.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            });
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

    }

    public void goGameSelecction(){
        Intent gameSelecctionIntent = new Intent(this, GameSelecctionActivity.class);
        startActivity(gameSelecctionIntent);
        finish();
    }

    private void alertFinishGame(){
        alertDialogBuilder = new AlertDialog.Builder(this);
        final View finishGamePopUp = getLayoutInflater().inflate(R.layout.finish_game,null);
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


    public void goBack(View v){
        finish();
    }
}