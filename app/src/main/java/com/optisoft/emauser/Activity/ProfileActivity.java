package com.optisoft.emauser.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.optisoft.emauser.HelperClasses.CommonPrefrence;
import com.optisoft.emauser.Model.ResidentModel;
import com.optisoft.emauser.Model.ResponseModel;
import com.optisoft.emauser.Model.UserModel;
import com.optisoft.emauser.R;
import com.optisoft.emauser.Webservices.CallApi;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hbb20.CountryCodePicker;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.annotation.Inherited;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import in.myinnos.awesomeimagepicker.activities.AlbumSelectActivity;
import in.myinnos.awesomeimagepicker.helpers.ConstantsCustomGallery;
import in.myinnos.awesomeimagepicker.models.Image;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.optisoft.emauser.HelperClasses.ApiConstant.EMAIL_PATTERN;
import static com.optisoft.emauser.HelperClasses.ApiConstant.EXCEPTION_TAG;
import static com.optisoft.emauser.HelperClasses.ApiConstant.IMAGE_URL;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "TAG";
    public static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 11;
    public static final int REQUEST_CAMERA = 0;
    public static final int SELECT_FILE = 1;


    private TextView submit, full_name, phone_number, add_new, vehicle_yes;
    private LinearLayout parentLinearLayout;
    private ArrayList<EditText> resNameArr = new ArrayList<>();
    private ArrayList<AppCompatSpinner> resGenderArr   = new ArrayList<>();
    private ArrayList<EditText> resPContactArr = new ArrayList<>();
    private ArrayList<EditText> resSContactArr = new ArrayList<>();
    private EditText fname, lname, email, phone, full_address, court, street,oldpassword,newpassword;
    private TextInputEditText carplate_front, carplate_back;
    private String fnameSt, lnameSt, emailSt, phoneSt, full_addressSt, courtSt, streetSt,oldSt,newSt, plate_info, plate_front, plate_back;
    private CountryCodePicker country;
    private ImageView img_back;
    private CommonPrefrence commonPrefrence = new CommonPrefrence();
    private UserModel userModel = null;
    private File destination;
    private String userChoosenTask = "";
    private ImageView profile_image;
    private Gson gson = new Gson();
    private Dialog resetpasswordDialog;
    TextView reset;
    private LinearLayout vehicle_layout;
    private boolean newVehicle = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userModel = commonPrefrence.getUserLoginSharedPref(this);

        country = (CountryCodePicker) findViewById(R.id.country);
        parentLinearLayout = (LinearLayout) findViewById(R.id.ly_residents);
        fname = (EditText) findViewById(R.id.fname);
        lname = (EditText) findViewById(R.id.lname);
        email = (EditText) findViewById(R.id.email);
        phone = (EditText) findViewById(R.id.phone);
        full_address = (EditText) findViewById(R.id.fulladdress);
        court = (EditText) findViewById(R.id.court);
        street = (EditText) findViewById(R.id.street);
        vehicle_yes = (TextView) findViewById(R.id.vehicle_yes);

        reset=(TextView)findViewById(R.id.resetpassword) ;
        full_name = (TextView) findViewById(R.id.full_name);
        phone_number = (TextView) findViewById(R.id.phone_number);
        submit = (TextView) findViewById(R.id.submit);
        add_new = (TextView) findViewById(R.id.add_new);
        img_back = (ImageView) findViewById(R.id.drawer);
        profile_image = (ImageView) findViewById(R.id.profile_image);
        img_back.setOnClickListener(this);
        submit.setOnClickListener(this);
        add_new.setOnClickListener(this);
        profile_image.setOnClickListener(this);
        reset.setOnClickListener(this);
        vehicle_yes.setOnClickListener(this);
        setUserView();
    }

    private void setUserView() {
        country.setEnabled(false);
        if (userModel == null) {
            return;
        }
        try {
            full_name.setText(userModel.getFname() + " " + userModel.getLname());
            phone_number.setText("+" + userModel.getCountry_code() + "-" + userModel.getMobile());
            if (userModel.getShort_code().isEmpty()){
                country.setCountryForPhoneCode(Integer.parseInt(userModel.getCountry_code()));
            }else {
                country.setCountryForNameCode(userModel.getShort_code());
            }
            fname.setText(userModel.getFname());
            lname.setText(userModel.getLname());
            email.setText(userModel.getEmail());
            phone.setText(userModel.getMobile());
            full_address.setText(userModel.getFull_address());
            court.setText(userModel.getCourt());
            street.setText(userModel.getStreet());
            Picasso.with(this).load(IMAGE_URL + userModel.getProfile_picture()).placeholder(R.drawable.user).error(R.drawable.user).into(profile_image);

            String residents = userModel.getResidents();
            if (!residents.isEmpty()){
                Type type = new TypeToken<ArrayList<ResidentModel>>(){}.getType();
                ArrayList<ResidentModel> mList = gson.fromJson(residents, type);
                for (ResidentModel object: mList) {
                    onAddField(object);
                }
            }
        } catch (Exception e) {
            Log.e("EXCEPTION", e.getMessage());
        }
    }

    public void customToast(String message) {
        Toast.makeText(getApplication(), message, Toast.LENGTH_LONG).show();
    }

    public void responseUpdateProfile(ResponseModel body) {
        try {
            customToast(body.getMessage());
            if (body.getStatus() == 1) {
                JSONArray jsonArray = new JSONArray(gson.toJson(body.getResponse()));
                Type type = new TypeToken<UserModel>() {
                }.getType();
                userModel = gson.fromJson(jsonArray.get(0).toString(), type);
                if (userModel != null) {
                    CommonPrefrence commonPrefrence = new CommonPrefrence();
                    commonPrefrence.setUserLoginSharedPref(ProfileActivity.this, userModel);
                    Intent intent = new Intent(getApplication(), ProfileActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        } catch (Exception e) {
            customToast(e.getMessage());
            Log.e(EXCEPTION_TAG, e.getMessage());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit:
                callSubmit();
                break;
            case R.id.drawer:
                finish();
                break;
            case R.id.profile_image:
                selectImage();
                break;
            case R.id.resetpassword:
                passwordAlertDialog();
                break;
            case R.id.add_new:
                onAddField(null);
                break;
            case R.id.vehicle_yes:
                if (newVehicle == false){
                    vehicle_yes.setText("No thanks");
                    newVehicle = true;
                    addVehicleLayout();
                }
                else {
                    vehicle_yes.setText("Sure");
                    removeVehicleLayout();
                    newVehicle = false;
                }
                break;
        }
    }

    public void onAddField(ResidentModel model) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.field_resident, null);
        // Add the new row before the add field button.
        parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);
        if (parentLinearLayout.getChildCount() > 10){
            add_new.setVisibility(View.GONE);
        }

        EditText fullname  = (EditText) rowView.findViewById(R.id.res_fullname);
        EditText p_contact = (EditText) rowView.findViewById(R.id.primary_contact);
        EditText s_contact = (EditText) rowView.findViewById(R.id.secondary_contact);
        AppCompatSpinner gender = (AppCompatSpinner) rowView.findViewById(R.id.gender);

        List<String> genderArr = Arrays.asList(getResources().getStringArray(R.array.gender));

        if (model != null){
            try {
                fullname.setText(model.getFullname());
                gender.setSelection(genderArr.indexOf(model.getGender()));
                p_contact.setText(model.getPrimary_contact());
                s_contact.setText(model.getSecondry_contact());
            }catch (Exception e){
                Log.e(EXCEPTION_TAG, e.getMessage());
            }
        }

        resNameArr.add(fullname);
        resGenderArr.add(gender);
        resPContactArr.add(p_contact);
        resSContactArr.add(s_contact);
    }

    public void onDelete(View v) {
        View rowView = (View) v.getParent().getParent().getParent().getParent();
        int count = parentLinearLayout.indexOfChild(rowView);
        parentLinearLayout.removeView(rowView);
        resNameArr.remove(count);
        resGenderArr.remove(count);
        resPContactArr.remove(count);
        resSContactArr.remove(count);
        add_new.setVisibility(View.VISIBLE);
    }

    private void callSubmit() {

        phoneSt = phone.getText().toString();
        fnameSt = fname.getText().toString();
        lnameSt = lname.getText().toString();
        emailSt = email.getText().toString();
        full_addressSt = full_address.getText().toString();
        courtSt = court.getText().toString();
        streetSt = street.getText().toString();
        plate_info = null;
        if (carplate_front != null && carplate_back != null) {
            plate_front = carplate_front.getText().toString();
            plate_back = carplate_back.getText().toString();
        }

        if (fnameSt.isEmpty()) {
            fname.setError("Enter First Name");
            fname.requestFocus();
            return;
        }
        if (lnameSt.isEmpty()) {
            lname.setError("Enter Last Name");
            lname.requestFocus();
            return;
        }
        if (emailSt.isEmpty()) {
            email.setError("Enter Email Id");
            email.requestFocus();
            return;
        }
        if (!emailSt.matches(EMAIL_PATTERN)) {
            email.setError("Invalid Email Id");
            email.requestFocus();
            return;
        }
        if (full_addressSt.isEmpty()) {
            full_address.setError("Please Enter Your Address");
            full_address.requestFocus();
        }
        if (courtSt.isEmpty()) {
            court.setError("Please Enter Your Court");
            court.requestFocus();
        }
        if (streetSt.isEmpty()) {
            street.setError("Please Provide Your Street");
            street.requestFocus();
        }
        if (plate_front == null && carplate_front != null) {
            carplate_front.setError("Enter 1ST REG No*");
            carplate_front.requestFocus();
            return;
        }
        if (plate_front != null && plate_front.isEmpty() && carplate_front != null) {
            carplate_front.setError("Enter 1ST REG No*");
            carplate_front.requestFocus();
            return;
        }
        if (!plate_front.isEmpty() && carplate_front != null && !(plate_front.length() >=2 && plate_front.length() <=3)) {
            carplate_front.setError("Length has to be 2 or 3");
            carplate_front.requestFocus();
            return;
        }

        if (plate_back == null && carplate_back != null) {
            carplate_back.setError("Enter 2ND REG No*");
            carplate_back.requestFocus();
            return;
        }
        if (plate_back != null && plate_back.isEmpty() && carplate_back != null){
            carplate_back.setError("Enter 2ND REG No*");
            carplate_back.requestFocus();
            return;
        }
        if (!plate_back.isEmpty() && carplate_back != null && !isCarPlateCorrect()) {
            carplate_back.setError("First 3 Letters have to be Number and last Letter has to be alphabet");
            carplate_back.requestFocus();
            return;
        }
        if (!plate_front.isEmpty() && !plate_back.isEmpty())
            plate_info = plate_front + ' ' + plate_back;

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("user_id", RequestBody.create(MediaType.parse("text/plain"), userModel.getUserId()));
        map.put("fname", RequestBody.create(MediaType.parse("text/plain"), fnameSt));
        map.put("lname", RequestBody.create(MediaType.parse("text/plain"), lnameSt));
        map.put("email", RequestBody.create(MediaType.parse("text/plain"), emailSt));
        map.put("full_address", RequestBody.create(MediaType.parse("text/plain"), full_addressSt));
        map.put("court", RequestBody.create(MediaType.parse("text/plain"), courtSt));
        map.put("street", RequestBody.create(MediaType.parse("text/plain"), streetSt));
        if (plate_info != null)
            map.put("plate_info", RequestBody.create(MediaType.parse("text/plain"), plate_info));
        else
            map.put("plate_info", RequestBody.create(MediaType.parse("text/plain"), ""));


        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < resNameArr.size(); i++){
            String nameStr   = resNameArr.get(i).getText().toString();
            String genderStr = resGenderArr.get(i).getSelectedItem().toString();
            String pContStr  = resPContactArr.get(i).getText().toString();
            String sContStr  = resSContactArr.get(i).getText().toString();

            if (nameStr.isEmpty()){
                resNameArr.get(i).setError("Enter Full Name");
                resNameArr.get(i).requestFocus();
                return;
            }

            JSONObject object = new JSONObject();
            try {
                object.put("name", nameStr);
                object.put("gender", genderStr);
                object.put("pcontact", pContStr);
                object.put("scontact", sContStr);
                jsonArray.put(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        map.put("residents", RequestBody.create(MediaType.parse("text/plain"), jsonArray.toString()));

        try {
            if (destination != null) {
                File temp = saveBitmapToFile(destination);
                if (temp == null) {
                    customToast("Profile Image not selected correctly");
                    return;
                }
                map.put("image\"; filename=\"" + temp.getName(), RequestBody.create(MediaType.parse("image*//*"), temp));
            }
        } catch (Exception e) {
            Log.d("IMAGE_ERROR", e.getMessage());
            customToast("Profile Image not selected correctly");
            return;
        }

        CallApi callApi = new CallApi();
        callApi.requestUpdateProfile(ProfileActivity.this, map);
    }

    private void selectImage() {
        if (isStoragePermissionGranted()) {
            Intent intent = new Intent(this, AlbumSelectActivity.class);
            intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_LIMIT, 1); // set limit for image selection
            startActivityForResult(intent, ConstantsCustomGallery.REQUEST_CODE);
        }
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
                return false;
            }
        }
        else {
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConstantsCustomGallery.REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            ArrayList<Image> images = data.getParcelableArrayListExtra(ConstantsCustomGallery.INTENT_EXTRA_IMAGES);
            for (int i = 0; i < images.size(); i++) {
                Uri uri = Uri.fromFile(new File(images.get(i).path));
                destination = new File(images.get(i).path);
                profile_image.setImageBitmap(BitmapFactory.decodeFile(images.get(i).path));
            }
            return;
        }
    }

    public File saveBitmapToFile(File file) {
        try {
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            FileInputStream inputStream = new FileInputStream(file);
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();
            final int REQUIRED_SIZE = 75;
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);
            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            return file;
        } catch (Exception e) {
            return null;
        }
    }

    public void requestresetPassword(ResponseModel model){
        try {
            customToast(model.getMessage());
            if(model.getStatus()==1)
            {
                resetpasswordDialog.dismiss();
            }



        }catch (Exception e){
            Log.e(EXCEPTION_TAG, e.getMessage());
            customToast(e.getMessage());
        }

    }

    private void passwordAlertDialog( ) {
        resetpasswordDialog = new Dialog(this);
        resetpasswordDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        resetpasswordDialog.setCancelable(true);
        resetpasswordDialog.setContentView(R.layout.reset_password_dialog);
        resetpasswordDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        resetpasswordDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

         TextView btn = (TextView) resetpasswordDialog.findViewById(R.id.btn);
        final EditText oldpassword=(EditText) resetpasswordDialog.findViewById(R.id.old_password);
       final EditText newpassword=(EditText) resetpasswordDialog.findViewById(R.id.newpassword);
        final EditText cnfnewpassword=(EditText) resetpasswordDialog.findViewById(R.id.cnf_password);



        btn.setOnClickListener(new View.OnClickListener() {






            @Override
            public void onClick(View v) {
                String oldSt=oldpassword.getText().toString();
                String newSt=newpassword.getText().toString();
                String cnfSt=cnfnewpassword.getText().toString();

                if (oldSt.isEmpty()){
                    oldpassword.setError("Enter Your Old Password ");
                    oldpassword.requestFocus();
                    return;
                }
                if (oldSt.length() < 6){
                    oldpassword.setError("Invalid Password");
                    oldpassword.requestFocus();
                    return;
                }

                if (newSt.isEmpty()){
                    newpassword.setError("Enter New Password");
                    newpassword.requestFocus();
                    return;
                }
                if (newSt.length() < 6){
                    newpassword.setError("Invalid New Password");
                    newpassword.requestFocus();
                    return;
                }
                if (!cnfSt.equals(newSt)){
                    cnfnewpassword.setError("Confirm New Password");
                    cnfnewpassword.requestFocus();
                    return;
                }




                CallApi callApi = new CallApi();

                HashMap<String, String> map=new HashMap<>();
                map.put("user id", userModel.getUserId());
                map.put("old_Password", oldSt);
                map.put("new__password",newSt);

                callApi.requestresetPassword(ProfileActivity.this, map);


            }
        });
        resetpasswordDialog.show();
    }

    private void addVehicleLayout(){
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (dend - start > 2)
                    return "";
                for (int i = start; i < end; ++i)
                {
                    if (!Pattern.compile("[ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz]*").matcher(String.valueOf(source.charAt(i))).matches())
                    {
                        return "";
                    }
                }

                return null;
            }
        };

        InputFilter filter1 = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (dend - start > 3 )
                    return "";
                for (int i = start; i < end; ++i)
                {
                    if (!Pattern.compile("[ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890]*").matcher(String.valueOf(source.charAt(i))).matches())
                    {
                        return "";
                    }
                }

                return null;
            }
        };

        vehicle_layout = findViewById(R.id.vehicle_list_layout);
        carplate_front = new TextInputEditText(this);
        carplate_front.setHintTextColor(getResources().getColor(R.color.off_white));
        ColorStateList colorStateList = ColorStateList.valueOf(Color.WHITE);
        carplate_front.setSupportBackgroundTintList(colorStateList);
        carplate_front.setTextColor(Color.WHITE);
        carplate_front.setTextSize(14);
        carplate_front.setHint("1ST REG No*");
        int i = 156;
        carplate_front.setId(i);
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        carplate_front.setLayoutParams(layoutParams1);
        carplate_front.setFilters(new InputFilter[]{filter});


        carplate_back = new TextInputEditText(this);
        carplate_back.setHintTextColor(getResources().getColor(R.color.off_white));
        carplate_back.setSupportBackgroundTintList(colorStateList);
        carplate_back.setTextColor(Color.WHITE);
        carplate_back.setTextSize(14);
        carplate_back.setHint("2ND REG No*");
        carplate_back.setId(i + 1);
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        carplate_back.setLayoutParams(layoutParams2);
        carplate_back.setFilters(new InputFilter[]{filter1});


        vehicle_layout.addView(carplate_front);
        vehicle_layout.addView(carplate_back);

    }

    private void removeVehicleLayout(){
        vehicle_layout.removeViewAt(vehicle_layout.getChildCount() - 2);
        vehicle_layout.removeViewAt(vehicle_layout.getChildCount() - 1);
        carplate_front = null;
        carplate_back = null;
    }

    private boolean isCarPlateCorrect() {
        String vehicle_back = carplate_back.getText().toString();
        if (vehicle_back.length() != 4)
            return false;
        int i;
        for (i = 0 ; i < 4 ; i ++) {
            if ((i >= 0 && i <= 2) && !(vehicle_back.charAt(i) >= '0' && vehicle_back.charAt(i) <= '9'))
                return false;
            if (i == 3 && !((vehicle_back.charAt(i) >= 'A' && vehicle_back.charAt(i) <= 'Z') || (vehicle_back.charAt(i) >= 'a' && vehicle_back.charAt(i) <= 'z')))
                return false;
        }
        return true;
    }
}
