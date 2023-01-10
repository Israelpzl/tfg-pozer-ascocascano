package com.example.leeconmonclick.patient;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leeconmonclick.R;

import es.leerconmonclick.util.LineView;

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
    private float cardImageX,cardImageY,cardWordX,cardWordY;
    private Context context = this;
    private boolean cardWordFinish1, cardWordFinish2, cardWordFinish3 = false;
    private boolean cardImageFinish1, cardImageFinish2, cardImageFinish3 = false;
    private int numSelect;

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


    private void listenerClickSelect(){

        cardViewImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cardImageFinish1){
                    imageSelect1.setVisibility(View.VISIBLE);
                    imageSelect2.setVisibility(View.INVISIBLE);
                    imageSelect3.setVisibility(View.INVISIBLE);
                    img = arrayImg[0];
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
                    img = arrayImg[1];
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
                    img = arrayImg[2];
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
                        word = arrayWord [0];
                        if (img.equals(word)){
                            points++;
                            int[] loc = new int[2];
                            cardViewWord1.getLocationOnScreen(loc);
                            cardWordX = loc[0];
                            cardWordY = loc[1];
                            LineView lineView = new LineView(context,cardImageX+350,cardImageY+75,cardWordX-385,cardWordY+150);
                            addContentView(lineView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
                            cardWordFinish1 = false;
                            wordSelect1.setVisibility(View.INVISIBLE);


                            float z = (float) 0.2;
                            cardViewWord1.setAlpha(z);

                            switch (numSelect){
                                case 1:{
                                    float a = (float) 0.2;
                                    cardViewImage1.setAlpha(a);
                                    cardImageFinish1 = true;
                                    imageSelect1.setVisibility(View.INVISIBLE);
                                    break;
                                }
                                case 2:{
                                    float a = (float) 0.2;
                                    cardViewImage2.setAlpha(a);
                                    cardImageFinish2 = true;
                                    imageSelect2.setVisibility(View.INVISIBLE);
                                    break;
                                } case 3: {
                                    float a = (float) 0.2;
                                    cardViewImage3.setAlpha(a);
                                    cardImageFinish3 = true;
                                    imageSelect3.setVisibility(View.INVISIBLE);
                                    break;
                                }
                            }

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
                @SuppressLint("Range")
                @Override
                public void onClick(View v) {
                    if (select && !cardWordFinish2) {
                        wordSelect1.setVisibility(View.INVISIBLE);
                        wordSelect2.setVisibility(View.VISIBLE);
                        wordSelect3.setVisibility(View.INVISIBLE);
                        word = arrayWord [1];
                        if (img.equals(word)){
                            points++;

                            int[] loc = new int[2];
                            cardViewWord2.getLocationOnScreen(loc);
                            cardWordX = loc[0];
                            cardWordY = loc[1];

                            LineView lineView = new LineView(context,cardImageX+350,cardImageY+75,cardWordX-385,cardWordY+150);
                            addContentView(lineView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));


                            cardWordFinish2 = true;
                            wordSelect2.setVisibility(View.INVISIBLE);

                            float z = (float) 0.2;
                            cardViewWord2.setAlpha(z);

                            switch (numSelect){
                                case 1:{
                                    float a = (float) 0.2;
                                    cardViewImage1.setAlpha(a);
                                    cardImageFinish1 = true;
                                    imageSelect1.setVisibility(View.INVISIBLE);
                                    break;
                                }
                                case 2:{
                                    float a = (float) 0.2;
                                    cardViewImage2.setAlpha(a);
                                    cardImageFinish2 = true;
                                    imageSelect2.setVisibility(View.INVISIBLE);
                                    break;
                                } case 3: {
                                    float a = (float) 0.2;
                                    cardViewImage3.setAlpha(a);
                                    cardImageFinish3 = true;
                                    imageSelect3.setVisibility(View.INVISIBLE);
                                    break;
                                }
                            }

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
                    if(select && !cardWordFinish3) {
                        wordSelect1.setVisibility(View.INVISIBLE);
                        wordSelect2.setVisibility(View.INVISIBLE);
                        wordSelect3.setVisibility(View.VISIBLE);
                        word = arrayWord [2];
                        if (img.equals(word)){
                            points++;
                            int[] loc = new int[2];
                            cardViewWord3.getLocationOnScreen(loc);
                            cardWordX = loc[0];
                            cardWordY = loc[1];
                            LineView lineView = new LineView(context,cardImageX+350,cardImageY+75,cardWordX-385,cardWordY+150);
                            addContentView(lineView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
                            cardWordFinish3 =true;
                            wordSelect3.setVisibility(View.INVISIBLE);

                            float z = (float) 0.2;
                            cardViewWord3.setAlpha(z);
                            switch (numSelect){
                                case 1:{
                                    float a = (float) 0.2;
                                    cardViewImage1.setAlpha(a);
                                    cardImageFinish1 = true;
                                    imageSelect1.setVisibility(View.INVISIBLE);
                                    break;
                                }
                                case 2:{
                                    float a = (float) 0.2;
                                    cardViewImage2.setAlpha(a);
                                    cardImageFinish2 = true;
                                    imageSelect2.setVisibility(View.INVISIBLE);
                                    break;
                                } case 3: {
                                    float a = (float) 0.2;
                                    cardViewImage3.setAlpha(a);
                                    cardImageFinish3 = true;
                                    imageSelect3.setVisibility(View.INVISIBLE);
                                    break;
                                }
                            }

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