package com.example.leeconmonclick.patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.example.leeconmonclick.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class LetterGameActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private CircleImageView iconPatient;
    private Context context =this;
    private ImageView image1, image2,image3, imgLetter;
    private CardView cardViewImg1, cardViewImg2,cardViewImg3;
    private List<String> listImg;

    private String correctWord;
    private AlertDialog alertDialog;
    private AlertDialog.Builder alertDialogBuilder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_letter_game);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        findElement();
        listImg = new ArrayList<>();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String namePatient = preferences.getString("userName","null");

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

        initBBDD ();
        listenerOnclick ();

    }

    private void listenerOnclick (){

        cardViewImg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = listImg.get(0);

                if (correctWord.equals(word)){
                    alertFinishGame();
                    Toast.makeText(getApplicationContext(), "Juego Terminado", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Has fallado, intentalo de nuevo", Toast.LENGTH_LONG).show();
                }
            }
        });

        cardViewImg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = listImg.get(1);

                if (correctWord.equals(word)){
                    alertFinishGame();
                    Toast.makeText(getApplicationContext(), "Juego Terminado", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Has fallado, intentalo de nuevo", Toast.LENGTH_LONG).show();
                }
            }
        });

        cardViewImg3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = listImg.get(2);

                if (correctWord.equals(word)){
                    alertFinishGame();
                    Toast.makeText(getApplicationContext(), "Juego Terminado", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Has fallado, intentalo de nuevo", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void  initBBDD () {

        List<String> contentList = new ArrayList<>();

        databaseReference.child("content").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot objDataSnapshot : snapshot.getChildren()){
                    String w = (String) objDataSnapshot.child("word").getValue();
                    contentList.add(w);
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

    private void findElement(){

        iconPatient = findViewById(R.id.iconPatientId);
        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        image3 = findViewById(R.id.image3);
        imgLetter = findViewById(R.id.letter);
        cardViewImg1 = findViewById(R.id.cardViewImage1);
        cardViewImg2 = findViewById(R.id.cardViewImage2);
        cardViewImg3 = findViewById(R.id.cardViewImage3);
    }


    public void goBack(View v){
        finish();
    }
}