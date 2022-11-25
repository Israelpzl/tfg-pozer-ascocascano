package es.leerconmonclick.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.leeconmonclick.PersonalNotesActivity;
import com.example.leeconmonclick.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;

public class ListAdapterNotes extends RecyclerView.Adapter<ListAdapterNotes.MyViewHolder>{

    Context context;
    ArrayList<Note> listNotes;
    DatabaseReference databaseReference;
    FirebaseAuth db = FirebaseAuth.getInstance();
    private FirebaseUser user;
    private String userCollection;

    public ListAdapterNotes(Context context, ArrayList<Note> listNotes) {
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

        databaseReference = FirebaseDatabase.getInstance().getReference();
        db = FirebaseAuth.getInstance();
        user = db.getCurrentUser();
        userCollection = user.getEmail();
        String[] parts = userCollection.split("@");
        userCollection = parts[0];
        userCollection = userCollection.toLowerCase();

        databaseReference.child("Users").child(userCollection).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String size = snapshot.child("sett").child("0").getValue().toString();
                if(size.equals("grande")){
                    holder.titleOutput.setTextSize(30);
                    holder.descriptionOutput.setTextSize(30);
                }else if(size.equals("normal")){
                    holder.titleOutput.setTextSize(20);
                    holder.descriptionOutput.setTextSize(20);
                }else if(size.equals("peque")){
                    holder.titleOutput.setTextSize(10);
                    holder.descriptionOutput.setTextSize(10);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
                                @SuppressLint("NotifyDataSetChanged")
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
