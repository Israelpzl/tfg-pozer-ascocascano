package es.leerconmonclick.util.adapters;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.leeconmonclick.professional.leeconmonclick.professional.AddPatientsActivity;
import com.example.leeconmonclick.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import es.leerconmonclick.util.utils.UserPatient;

public class ListAdapterUserPatient extends RecyclerView.Adapter<ListAdapterUserPatient.ViewHolder> {

    private List<UserPatient> mDataPatient;
    private LayoutInflater mInflater;
    private Context context;


    private DatabaseReference databaseReference;
    private FirebaseAuth db = FirebaseAuth.getInstance();
    private String userCollection;
    final ListAdapterUserPatient.OnItemClickListener listener;


    public interface OnItemClickListener{
        void onItemClick(UserPatient userPatient);
    }


    public ListAdapterUserPatient(List<UserPatient> itemList, Context context,ListAdapterUserPatient.OnItemClickListener listener){
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mDataPatient = itemList;
        this.listener = listener;
    }

    @Override
    public int getItemCount() {return mDataPatient.size();}


    @Override
    public ListAdapterUserPatient.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_user_patient, null);
        return new ListAdapterUserPatient.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder( ListAdapterUserPatient.ViewHolder holder, int position) {
        holder.bindData(mDataPatient.get(position));
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView namePatientView;
        private ImageButton deleteBtn,editBtn;
        private CircleImageView circleImageView;

        ViewHolder(View itemView) {
            super(itemView);

            namePatientView =  itemView.findViewById(R.id.namePatientItemId);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
            editBtn = itemView.findViewById(R.id.editBtn);
            circleImageView = itemView.findViewById(R.id.iconPatientId);

        }

        void bindData(final UserPatient userPatient){

            namePatientView.setText(userPatient.getNamePatient());
            databaseReference = FirebaseDatabase.getInstance().getReference();



            databaseReference.child("userPatient").child(userPatient.getNamePatient().toLowerCase(Locale.ROOT)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.child("icon").exists()) {
                        String icon = snapshot.child("icon").getValue().toString();
                        databaseReference.child("iconPatient").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Glide.with(context).load(snapshot.child(icon).getValue().toString()).into(circleImageView);
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

            FirebaseUser user = db.getCurrentUser();
            userCollection = user.getEmail();
            String[] parts = userCollection.split("@");
            userCollection = parts[0];
            userCollection = userCollection.toLowerCase();

            databaseReference.child("Users").child(userCollection).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    String size = snapshot.child("sett").child("0").getValue().toString();
                    if(size.equals("grande")){
                        namePatientView.setTextSize(30);
                    }else if(size.equals("normal")){
                        namePatientView.setTextSize(20);
                    }else if(size.equals("peque")){
                        namePatientView.setTextSize(10);
                    }
                    String dalto = snapshot.child("sett").child("1").getValue().toString();
                    if(dalto.equals("tritanopia")){
                        editBtn.setBackgroundResource(R.color.button_edit_tritano);
                        deleteBtn.setBackgroundResource(R.color.butto_red_tritano);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editUserPatient(userPatient);
                }
            });

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteUserPatient(userPatient);
                }
            });

            itemView.setOnClickListener( new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    listener.onItemClick(userPatient);
                }
            });


        }
    }

    private void deleteUserPatient(UserPatient userPatient) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage("¿Quieres borrar el paciente?");
        builder.setTitle("Borrado");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                databaseReference.child("userPatient").child(userPatient.getNamePatient()).removeValue();
                Toast.makeText(context, "Usuario Paciente borrado con éxito", Toast.LENGTH_LONG).show();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {}
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void editUserPatient(UserPatient userPatient) {

        Intent intent = new Intent(context,AddPatientsActivity.class);
        intent.putExtra("namePatient",userPatient.getNamePatient());
        intent.putExtra("agePatient",userPatient.getAgePatient());
        intent.putExtra("emailPatient",userPatient.getEmailPatient());
        intent.putExtra("descriptionPatient",userPatient.getDescriptionPatient());
        intent.putExtra("passPatient",userPatient.getPassword());
        intent.putExtra("modeEdit", true);
        context.startActivity(intent);
    }


}
