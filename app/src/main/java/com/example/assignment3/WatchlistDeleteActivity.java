package com.example.assignment3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

public class WatchlistDeleteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DeleteDialogFragment deleteDialogFragment = new DeleteDialogFragment();
        deleteDialogFragment.show(getSupportFragmentManager(), "Confirm delete");
    }

    // delete confirm dialog
    public  class DeleteDialogFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(final Bundle savedInstanceState) {



            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomDialog);
            builder.setTitle("Attention")
                    .setMessage("Are you sure you want to delete this entry?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(WatchlistDeleteActivity.this, WatchlistFragment.class);
                            Bundle bundle = new Bundle();
                            bundle.putBoolean("confirmDelete", true);
                            intent.putExtras (bundle);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getActivity(), WatchlistFragment.class);
                            Bundle bundle = new Bundle();
                            bundle.putBoolean("confirmDelete", true);
                            intent.putExtras (bundle);
                            startActivity(intent);
                        }
                    })
                    .setCancelable(false);
            //builder.show(); // show() cannot be used here
            return builder.create();
        }


    }
}



