package com.tanzil.sportspal.Utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.tanzil.sportspal.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by arun on 16/12/15.
 */
public class Utils {

    private static Date now;
    private static ProgressDialog progressDialog;


    public static void showLoading(Activity act, String msg) {
        progressDialog = ProgressDialog
                .show(act, "", msg, true);
    }

    public static void dismissLoading() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public static String commentTime(String dateString) {
        Date dat = null;
        String value = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dat = dateFormat.parse(dateString);
            SimpleDateFormat format = new SimpleDateFormat("d MMMM");
            value = format.format(dat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return value;
    }

    public static String capSentence(String string, boolean capitalize) {
        if (!Utils.isEmptyString(string)) {
            if (string.length() == 0) {
                return "";
            }
            String c = string.substring(0, 1);

            if (capitalize) {
                return c.toUpperCase() + capSentence(string.substring(1), c.equals(" "));
            } else {
                return c.toLowerCase() + capSentence(string.substring(1), c.equals(" "));
            }
        }else{
            return "";
        }
    }



    public static boolean isConnectingToInternet(Activity act) {
        ConnectivityManager connMgr = (ConnectivityManager) act.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        // fetch data
// display error
        return networkInfo != null && networkInfo.isConnected();
    }


    public static boolean isEmptyString(String str) {
        return str == null || str.equalsIgnoreCase("null")
                || str.equalsIgnoreCase("") || str.length() < 1;
    }

    public static void closeKeyboard(Context c, IBinder windowToken) {
        InputMethodManager mgr = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(windowToken, 0);
    }



    public static void showAlert(Activity activity, String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder
                .setMessage(msg)
                .setCancelable(false)
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // show it
        alertDialog.show();
    }

    public static void showMessage(Activity activity, String msg) {
        Toast.makeText(activity, ""+msg, Toast.LENGTH_SHORT).show();
    }



    public static void defaultLoader(Activity act) {
        progressDialog = ProgressDialog
                .show(act,
                        "",
                        act.getString(R.string.please_wait),
                        true);
    }

    public static void dismissDefaultLoader() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

}
