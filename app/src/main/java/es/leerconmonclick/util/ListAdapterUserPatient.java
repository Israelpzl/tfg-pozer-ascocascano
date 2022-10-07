package es.leerconmonclick.util;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.leeconmonclick.AddPatientsActivity;
import com.example.leeconmonclick.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ListAdapterUserPatient extends RecyclerView.Adapter<ListAdapterUserPatient.ViewHolder> {

    private List<UserPatient> mDataPatient;
    private LayoutInflater mInflater;
    private Context context;


    private DatabaseReference databaseReference;
    private FirebaseAuth db = FirebaseAuth.getInstance();


    public ListAdapterUserPatient(List<UserPatient> itemList, Context context){
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mDataPatient = itemList;
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

        ViewHolder(View itemView) {
            super(itemView);

            namePatientView =  itemView.findViewById(R.id.namePatientItemId);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
            editBtn = itemView.findViewById(R.id.editBtn);

        }

        void bindData(final UserPatient userPatient){

            namePatientView.setText(userPatient.getNamePatient());

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
        }
    }

    private void deleteUserPatient(UserPatient userPatient) {
        databaseReference.child("userPatient").child(userPatient.getNamePatient()).removeValue();
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
