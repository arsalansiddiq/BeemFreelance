package com.example.arsalansiddiq.beem.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.example.arsalansiddiq.beem.R;
//import com.example.arsalansiddiq.beem.activities.OrderActivity;

public class CustomAlertDialog {


    private Context context;
//    private boolean isWarning;

    public CustomAlertDialog(Context context) {
        this.context = context;
//        this.isWarning = isWarning;
    }

    public void showDialog(boolean isWarning) {
        AlertDialog alertDialog = new AlertDialog.Builder(
                context).create();

        // Setting Dialog Title
        alertDialog.setTitle("Warning!");

        if (isWarning) {
            // Setting Dialog Message
            alertDialog.setMessage("Please Enter Some Quantity.");
        } else{
                // Setting Dialog Message
                alertDialog.setMessage("You can Select only Loose or Carton Quantity.");
        }

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.ic_warning_black_24dp);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
//                    Toast.makeText(getApplicationContext(), "Please Some Quantity", Toast.LENGTH_SHORT).show();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

}
