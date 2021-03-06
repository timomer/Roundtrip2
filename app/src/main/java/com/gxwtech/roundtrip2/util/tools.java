package com.gxwtech.roundtrip2.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.ClipboardManager;
import android.widget.TextView;
import android.widget.Toast;

import com.gxwtech.roundtrip2.MainActivity;
import com.gxwtech.roundtrip2.MainApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Tim on 15/06/2016.
 */
public class tools {
    final static String TAG = "Tools";

    public static void showLogging(){
        String logCat = "no logs";
        final String processId = Integer.toString(android.os.Process.myPid());
        try {
            Process process = Runtime.getRuntime().exec("logcat -d");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder log = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                if(line.contains(processId)) log.append(line + "\n");
            }
            logCat = log.toString();

        } catch (IOException e) {
            logCat = e.getLocalizedMessage();
        } finally {
            showAlertText(logCat, MainApp.instance());
        }
    }

    public static void showAlertText(final String msg, final Context context){
        try {
            AlertDialog alertDialog = new AlertDialog.Builder(context)
                    .setMessage(msg)
                    .setPositiveButton("Copy to Clipboard", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                            clipboard.setText(msg);
                            Toast.makeText(MainApp.instance(), "Copied to clipboard", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .show();

            if (msg.length() > 100) {
                TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
                textView.setTextSize(10);
            }
        } catch (Exception e){
            //Crashlytics.logException(e);
        }
    }
}
