package com.tanzil.sportspal.Utility;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.tanzil.sportspal.R;
import com.tanzil.sportspal.model.ModelManager;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by arun on 16/12/15.
 */
public class Utils {

    private static Date now;
    private static ProgressDialog progressDialog;
    private static GoogleCloudMessaging gcm;
    private static String regid;
    public static final String[] INITIAL_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CONTACTS
    };
    public static final int INITIAL_REQUEST=1337;
    public static final int LOCATION_REQUEST=INITIAL_REQUEST+3;


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


    public static void startAnimationBG(Activity activity, ImageView mainView) {
        AnimationDrawable animationDrawable = (AnimationDrawable) mainView
                .getDrawable();
        animationDrawable.setOneShot(false);
        animationDrawable.start();
        Animation animation = AnimationUtils.loadAnimation(activity,
                android.R.anim.fade_in);
        mainView.setAnimation(animation);
        animation.start();
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Animation fadeOut = AnimationUtils.loadAnimation(
                // LoginActivity.this, android.R.anim.fade_out);
                // mainView.startAnimation(fadeOut);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
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
        } else {
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
        Toast.makeText(activity, "" + msg, Toast.LENGTH_SHORT).show();
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

    public static String getRegId(final Activity contex) {
        new AsyncTask<Void, Void, String>() {
            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog
                        .show(contex,
                                "",
                                contex.getString(R.string.please_wait),
                                true);
            }

            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(contex);
                    }
                    regid = gcm.register(ServiceApi.GCM_PROJECT_NUMBER);
                    ModelManager.getInstance().getAuthManager().setDeviceToken(regid);
                    // Utils.deviceId = regid;
                    msg = "Device registered, registration ID=" + regid;

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();

                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                Log.e("UTILS", "DEVICE_Token---> " + regid);
            }

        }.execute(null, null, null);
        return regid;
    }

    public static boolean checkGPSEnabled(Activity activity) {
        final LocationManager manager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static void showGPSDisabledAlertToUser(final Activity activity) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                activity.startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

}
