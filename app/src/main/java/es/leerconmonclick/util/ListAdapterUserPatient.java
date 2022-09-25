package es.leerconmonclick.util;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.leeconmonclick.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

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

        ViewHolder(View itemView) {
            super(itemView);

        }

        void bindData(final UserPatient userPatient){


        }
    }


}
