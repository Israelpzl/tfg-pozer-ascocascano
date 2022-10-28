package com.example.leeconmonclick;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.ArrayList;

import es.leerconmonclick.util.Note;
import io.realm.Realm;
import io.realm.RealmResults;

public class AdapterNotes extends RecyclerView.Adapter<AdapterNotes.MyViewHolder>{

    Context context;
    ArrayList<Note> listNotes;
    DatabaseReference databaseReference;
    FirebaseAuth db = FirebaseAuth.getInstance();

    public AdapterNotes(Context context, ArrayList<Note> listNotes) {
        this.context = context;
        this.listNotes = listNotes;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.note_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Note note = listNotes.get(position);
        holder.titleOutput.setText(note.getTitle());
        holder.descriptionOutput.setText(note.getDescription());
        String formatedTime = DateFormat.getDateTimeInstance().format(note.getTime());
        holder.dateOutput.setText(formatedTime);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                PopupMenu menu = new PopupMenu(context, view);
                menu.getMenu().add("BORRAR");
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if(menuItem.getTitle().equals("BORRAR")){
                            databaseReference = FirebaseDatabase.getInstance().getReference();
                            FirebaseUser user = db.getCurrentUser();
                            String userCollection = user.getEmail();
                            String[] parts = userCollection.split("@");
                            userCollection = parts[0];
                            databaseReference.child("Users").child(userCollection).child("notas").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                @Override
                                public void onSuccess(DataSnapshot dataSnapshot) {
                                    for(DataSnapshot objDataSnapshot : dataSnapshot.getChildren()){
                                        Long time = (Long) objDataSnapshot.child("time").getValue();
                                        if(time == note.getTime()){
                                            objDataSnapshot.getRef().removeValue();
                                        }
                                    }
                                }
                            });
                        }
                        return true;
                    }
                });
                menu.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return listNotes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView titleOutput;
        TextView descriptionOutput;
        TextView dateOutput;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            titleOutput = itemView.findViewById(R.id.titleNote);
            descriptionOutput = itemView.findViewById(R.id.descriptionNote);
            dateOutput = itemView.findViewById(R.id.dateNote);
        }
    }
}
