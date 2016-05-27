package com.tanzil.sportspal.view.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.tanzil.sportspal.Utility.Utils;
import com.tanzil.sportspal.customUi.MyButton;
import com.tanzil.sportspal.customUi.MyEditText;
import com.tanzil.sportspal.customUi.MyTextView;
import com.tanzil.sportspal.model.ModelManager;
import com.tanzil.sportspal.model.bean.Users;

import org.json.JSONException;
import org.json.JSONObject;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.activity = super.getActivity();

        Utils.setHeader(activity, "0-" + activity.getString(R.string.title_settings));

        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        ImageView img_right = (ImageView) activity.findViewById(R.id.img_right);
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

                int img = R.drawable.default_male;
                if (!Utils.isEmptyString(gender))
                    if (gender.equalsIgnoreCase("Female"))
                        img = R.drawable.default_female;
                    else
                        img = R.drawable.default_male;

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
        final CharSequence[] items = {"Take Photo", "Choose from gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, Utils.REQUEST_CAMERA);
                } else if (items[item].equals("Choose from gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select File"), Utils.SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Utils.REQUEST_CAMERA) {
                try {
                    Bitmap imagedata = (Bitmap) data.getExtras().get("data");
                    img_user_pic.setImageBitmap(imagedata);
                    base64ImageData = null;
                    base64ImageData = Utils.encodeTobase64(Utils.scaleBitmap(imagedata, 300, 400));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == Utils.SELECT_FILE) {
                try {
                    Uri selectedImageUri = data.getData();
                    Bitmap imagedata = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), selectedImageUri);
                    img_user_pic.setImageBitmap(imagedata);
                    base64ImageData = null;
                    base64ImageData = Utils.encodeTobase64(Utils.scaleBitmap(imagedata, 300, 400));


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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
