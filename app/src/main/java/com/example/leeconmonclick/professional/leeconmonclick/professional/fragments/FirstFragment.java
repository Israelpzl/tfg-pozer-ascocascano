package com.example.leeconmonclick.professional.leeconmonclick.professional.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.leeconmonclick.R;
import com.example.leeconmonclick.professional.leeconmonclick.professional.PatientListActivity;
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


    private TextView namePatient,agePatient,descriptionPatient,emailPatient;
    private CircleImageView iconPatient;
    private DatabaseReference databaseReference;
    private Context context;
    private String name;
    private ImageButton deleteBtn;




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
        context = inflater.getContext();


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        databaseReference = FirebaseDatabase.getInstance().getReference();


        if (getArguments() != null){

            name = getArguments().getString("namePatient").toLowerCase(Locale.ROOT);
            databaseReference.child("userPatient").child(name).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.exists()){
                        namePatient.setText(name);
                        agePatient.setText(snapshot.child("agePatient").getValue().toString());
                        descriptionPatient.setText(snapshot.child("descriptionPatient").getValue().toString());
                        emailPatient.setText(snapshot.child("emailPatient").getValue().toString());
                        String icon = snapshot.child("icon").getValue().toString();
                        databaseReference.child("iconPatient").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Glide.with(context.getApplicationContext()).load((snapshot.child(icon).getValue()).toString()).into(iconPatient);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    builder.setMessage("¿Quieres borrar el paciente?");
                    builder.setTitle("Borrado");
                    builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            databaseReference.child("userPatient").child(name).removeValue();
                            Toast.makeText(context, "Usuario Paciente borrado con éxito", Toast.LENGTH_LONG).show();
                            Intent patientListIntent = new Intent(context, PatientListActivity.class);
                            patientListIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            context.startActivity(patientListIntent);

                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {}
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });

        }




    }





}