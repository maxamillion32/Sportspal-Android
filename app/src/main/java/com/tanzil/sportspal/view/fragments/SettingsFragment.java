package com.tanzil.sportspal.view.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tanzil.sportspal.R;
import com.tanzil.sportspal.Utility.SPLog;
import com.tanzil.sportspal.Utility.Utils;
import com.tanzil.sportspal.customUi.MyButton;
import com.tanzil.sportspal.customUi.MyEditText;
import com.tanzil.sportspal.customUi.MyTextView;
import com.tanzil.sportspal.model.ModelManager;
import com.tanzil.sportspal.model.bean.Users;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import de.greenrobot.event.EventBus;

/**
 * Created by arun.sharma on 5/10/2016.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener {
    private String TAG = SettingsFragment.class.getSimpleName();
    private Activity activity;
    private MyEditText et_email, et_firstName, et_lastName, et_dob;
    private MyTextView et_description;
    private MyButton btn_preferences, btn_location;
    private Calendar myCalendar;
    private ImageView /*img_update*/img_right, img_user_pic;
    private ArrayList<Users> usersArrayList;
    private String base64ImageData = "", gender = "";
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.activity = super.getActivity();

        Utils.setHeader(activity, "0-" + activity.getString(R.string.title_settings));

        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        img_right = (ImageView) activity.findViewById(R.id.img_right);
        img_right.setImageResource(R.drawable.check);
        img_right.setVisibility(View.VISIBLE);

        myCalendar = Calendar.getInstance();

        et_email = (MyEditText) rootView.findViewById(R.id.et_email);
        et_firstName = (MyEditText) rootView.findViewById(R.id.et_first_name);
        et_lastName = (MyEditText) rootView.findViewById(R.id.et_last_name);
        et_dob = (MyEditText) rootView.findViewById(R.id.et_dob);
        et_description = (MyTextView) rootView.findViewById(R.id.et_description);

        btn_preferences = (MyButton) rootView.findViewById(R.id.btn_preferences);
        btn_location = (MyButton) rootView.findViewById(R.id.btn_location);

//        img_update = (ImageView) rootView.findViewById(R.id.img_add);
        img_user_pic = (ImageView) rootView.findViewById(R.id.img_user_pic);

        btn_location.setTransformationMethod(null);
        btn_preferences.setTransformationMethod(null);

        btn_preferences.setOnClickListener(this);
        btn_location.setOnClickListener(this);
//        img_update.setOnClickListener(this);
        img_right.setOnClickListener(this);
        img_user_pic.setOnClickListener(this);

        usersArrayList = ModelManager.getInstance().getUsersManager().getMyDetails(false);
        if (usersArrayList == null) {
            Utils.showLoading(activity, activity.getString(R.string.please_wait));
            ModelManager.getInstance().getUsersManager().getMyDetails(true);
        } else {
            setData();
        }

        return rootView;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Utils.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                    SPLog.e("Permission Denied : ", "Permission not granted");
                }
                break;
        }
    }

    private void setData() {
        usersArrayList = ModelManager.getInstance().getUsersManager().getMyDetails(false);
        if (usersArrayList != null) {
            if (usersArrayList.size() > 0) {
//                for (int i = 0; i < usersArrayList.size(); i++) {
//                    if (i == 0) {
                int i = 0;
                et_email.setText(usersArrayList.get(i).getEmail());
                et_firstName.setText(usersArrayList.get(i).getFirst_name());
                et_lastName.setText(usersArrayList.get(i).getLast_name());
                et_dob.setText(usersArrayList.get(i).getDob());
                btn_location.setText(usersArrayList.get(i).getAddress());

                if (!Utils.isEmptyString(usersArrayList.get(i).getBio())) {
                    et_description.setText(usersArrayList.get(i).getBio());
                    et_description.setVisibility(View.VISIBLE);
                } else
                    et_description.setVisibility(View.GONE);


                gender = usersArrayList.get(i).getGender();

                int img = R.drawable.default_pic;
//                if (!Utils.isEmptyString(gender))
//                    if (gender.equalsIgnoreCase("Female"))
//                        img = R.drawable.default_female;
//                    else
//                        img = R.drawable.default_male;

                if (!Utils.isEmptyString(usersArrayList.get(i).getImage()))
                    Picasso.with(activity)
                            .load(usersArrayList.get(i).getImage())
                            .placeholder(img)
                            .error(img)
                            .into(img_user_pic);
                else {
                    img_user_pic.setImageResource(img);
                    img_user_pic.setScaleType(ImageView.ScaleType.FIT_CENTER);
                }
//                    }
//                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_right:
                if (et_firstName.getText().toString().trim().length() == 0) {
                    et_firstName.requestFocus();
                    Toast.makeText(activity, "Please enter first name field", Toast.LENGTH_SHORT).show();
                } else if (et_lastName.getText().toString().trim().length() == 0) {
                    et_lastName.requestFocus();
                    Toast.makeText(activity, "Please enter last name field", Toast.LENGTH_SHORT).show();
//                } else if (et_description.getText().toString().trim().length() == 0) {
//                    et_description.requestFocus();
//                    Toast.makeText(activity, "Please enter description", Toast.LENGTH_SHORT).show();
                } else {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("first_name", et_firstName.getText().toString().trim());
                        jsonObject.put("last_name", et_lastName.getText().toString().trim());
                        jsonObject.put("dob", et_dob.getText().toString().trim());
                        jsonObject.put("gender", gender);
                        jsonObject.put("bio", et_description.getText().toString().trim());
                        jsonObject.put("image", base64ImageData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Utils.showLoading(activity, activity.getString(R.string.please_wait));
                    ModelManager.getInstance().getUsersManager().updateProfile(jsonObject);
                }
                break;

            case R.id.btn_location:
                break;

            case R.id.btn_preferences:
                Fragment fragment = new PreferencesFragment();
                Bundle bundle = new Bundle();
                bundle.putString("type", "detail");
                FragmentManager fragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment, "PreferencesFragment");
                fragmentTransaction.addToBackStack("PreferencesFragment");
                fragmentTransaction.commit();
                break;

            case R.id.img_user_pic:
                selectImage();
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utils.checkPermission(activity);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        img_right.setVisibility(View.VISIBLE);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        if (thumbnail != null) {
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        }

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        base64ImageData = null;
        base64ImageData = Utils.encodeTobase64(Utils.scaleBitmap(thumbnail, 300, 400));
        img_user_pic.setImageBitmap(thumbnail);

        SPLog.e("Base 64 String", ""+base64ImageData);
    }

    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), data.getData());
                base64ImageData = null;
                base64ImageData = Utils.encodeTobase64(Utils.scaleBitmap(bm, 300, 400));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        SPLog.e("Base 64 String", ""+base64ImageData);
        img_user_pic.setImageBitmap(bm);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        //gpDatabase.setConversation(chatManager.getConversations());
        img_right.setVisibility(View.INVISIBLE);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void onEventMainThread(String message) {
        Log.e(TAG, "-- " + message);
        if (message.equalsIgnoreCase("GetMyDetails True")) {
            Utils.dismissLoading();
            setData();
        } else if (message.equalsIgnoreCase("GetMyDetails False")) {
            Utils.dismissLoading();
        } else if (message.equalsIgnoreCase("GetMyDetails Network Error")) {
            Utils.dismissLoading();
        } else if (message.equalsIgnoreCase("UpdateProfile True")) {
            Utils.dismissLoading();
            Utils.showLoading(activity, activity.getString(R.string.please_wait));
            ModelManager.getInstance().getUsersManager().getMyDetails(true);
        } else if (message.equalsIgnoreCase("UpdateProfile False")) {
            Utils.dismissLoading();
        } else if (message.equalsIgnoreCase("UpdateProfile Network Error")) {
            Utils.dismissLoading();
        }
    }
}
