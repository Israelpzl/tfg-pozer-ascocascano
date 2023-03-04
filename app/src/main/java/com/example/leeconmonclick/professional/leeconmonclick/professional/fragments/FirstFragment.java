package com.example.leeconmonclick.professional.leeconmonclick.professional.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.leeconmonclick.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FirstFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirstFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private TextView namePatient,agePatient,descriptionPatient,emailPatient,title1,title2,title3,title4;
    private CircleImageView iconPatient;
    private DatabaseReference databaseReference;
    private Context context;
    private String userCollection;




    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FirstFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FirstFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FirstFragment newInstance(String param1, String param2) {
        FirstFragment fragment = new FirstFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_first, container, false);

        namePatient = view.findViewById(R.id.namePatient);
        agePatient = view.findViewById(R.id.agePatient);
        descriptionPatient = view.findViewById(R.id.descriptionPatient);
        emailPatient = view.findViewById(R.id.emailPatient);
        iconPatient = view.findViewById(R.id.iconPatientId);
        title1 = view.findViewById(R.id.nameId);
        title2 = view.findViewById(R.id.textView16);
        title3 = view.findViewById(R.id.textView17);
        title4 = view.findViewById(R.id.description);
        context = inflater.getContext();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        userCollection = user.getEmail();
        String[] parts = userCollection.split("@");
        userCollection = parts[0];
        userCollection = userCollection.toLowerCase();


        databaseReference.child("Users").child(userCollection).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String size = snapshot.child("sett").child("0").getValue().toString();
                switch (size) {
                    case "grande":
                        namePatient.setTextSize(30);
                        agePatient.setTextSize(30);
                        descriptionPatient.setTextSize(30);
                        emailPatient.setTextSize(30);
                        title1.setTextSize(30);
                        title2.setTextSize(30);
                        title3.setTextSize(30);
                        title4.setTextSize(30);
                        break;
                    case "normal":
                        namePatient.setTextSize(20);
                        agePatient.setTextSize(20);
                        descriptionPatient.setTextSize(20);
                        emailPatient.setTextSize(20);
                        title1.setTextSize(20);
                        title2.setTextSize(20);
                        title3.setTextSize(20);
                        title4.setTextSize(20);
                        break;
                    case "peque":
                        namePatient.setTextSize(10);
                        agePatient.setTextSize(10);
                        descriptionPatient.setTextSize(10);
                        emailPatient.setTextSize(10);
                        title1.setTextSize(10);
                        title2.setTextSize(10);
                        title3.setTextSize(10);
                        title4.setTextSize(10);
                        break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        databaseReference = FirebaseDatabase.getInstance().getReference();


        if (getArguments() != null){

            String name = getArguments().getString("namePatient").toLowerCase(Locale.ROOT);
            databaseReference.child("userPatient").child(name).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    namePatient.setText(name);
                    agePatient.setText(Objects.requireNonNull(snapshot.child("agePatient").getValue()).toString());
                    descriptionPatient.setText(Objects.requireNonNull(snapshot.child("descriptionPatient").getValue()).toString());
                    emailPatient.setText(Objects.requireNonNull(snapshot.child("emailPatient").getValue()).toString());
                    String icon = Objects.requireNonNull(snapshot.child("icon").getValue()).toString();
                    databaseReference.child("iconPatient").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Glide.with(context).load(Objects.requireNonNull(snapshot.child(icon).getValue()).toString()).into(iconPatient);
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

        }




    }
}