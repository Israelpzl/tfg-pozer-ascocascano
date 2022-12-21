package es.leerconmonclick.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.leeconmonclick.R;

public class DialogSettingPatient extends AppCompatDialogFragment {

    private EditText EditNumber;
    private DialogListener listener;
    private ImageButton checkBtn, cancelBtn;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_setting_patient,null);

        checkBtn = view.findViewById(R.id.checkBtnId);
        cancelBtn = view.findViewById(R.id.cancelBtnId);
        EditNumber = view.findViewById(R.id.number);



        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = EditNumber.getText().toString();
                listener.applyTexts(number);
                dismiss();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        builder.setView(view);



        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener =(DialogListener) context;
        }catch (ClassCastException e){
           throw new ClassCastException(context.toString() + "must implement DialogListener");
        }

    }

    public interface  DialogListener{
        void applyTexts(String number);
    }
}
