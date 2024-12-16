package com.example.perpustakaan;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import androidx.fragment.app.DialogFragment;
import android.view.KeyEvent;

public class DialogChoice extends DialogFragment {

    int position;
    SharedPreferences settings;

    public interface DialogChoiceListener{
        void onPositiveButtonClicked(String[] list, int position);
        void onNegativeButtonClicked();
    }

    DialogChoiceListener mListener;

    @Override
    public void onAttach(Context context) {
        settings = context.getSharedPreferences("currentPosition", 0);
        position = settings.getInt("currentPosition", 0);

        super.onAttach(context);

        try {
            mListener = (DialogChoiceListener) context;
        }catch (Exception e){
            throw new ClassCastException(getActivity().toString()+"viewData");
        }
    }



    @Override
    public void onResume() {
        super.onResume();

        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_BACK))
                {
                    dismiss();
                    return true;
                }
                else return false;
            }
        });
    }
}
