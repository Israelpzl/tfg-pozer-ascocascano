package com.example.leeconmonclick.patient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.leeconmonclick.R;

import java.util.Collections;

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
    private int select;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_words_game);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        arrayImg = new String[]{"perro", "gato", "oso"};
        arrayWord = new String[]{"oso", "gato", "perro"};

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
            }
        });

        cardViewImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageSelect1.setVisibility(View.INVISIBLE);
                imageSelect2.setVisibility(View.VISIBLE);
                imageSelect3.setVisibility(View.INVISIBLE);
                img = arrayImg[1];
            }
        });

        cardViewImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageSelect1.setVisibility(View.INVISIBLE);
                imageSelect2.setVisibility(View.INVISIBLE);
                imageSelect3.setVisibility(View.VISIBLE);
                img = arrayImg[2];
            }
        });

        cardViewWord1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wordSelect1.setVisibility(View.VISIBLE);
                wordSelect2.setVisibility(View.INVISIBLE);
                wordSelect3.setVisibility(View.INVISIBLE);
                word = arrayWord [0];
            }
        });

        cardViewWord2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wordSelect1.setVisibility(View.INVISIBLE);
                wordSelect2.setVisibility(View.VISIBLE);
                wordSelect3.setVisibility(View.INVISIBLE);
                word = arrayWord [1];
            }
        });

        cardViewWord3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wordSelect1.setVisibility(View.INVISIBLE);
                wordSelect2.setVisibility(View.INVISIBLE);
                wordSelect3.setVisibility(View.VISIBLE);
                word = arrayWord [2];
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

    public void goBack(View v){
        finish();
    }
}