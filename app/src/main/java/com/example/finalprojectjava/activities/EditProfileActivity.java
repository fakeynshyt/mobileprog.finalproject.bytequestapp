package com.example.finalprojectjava.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.finalprojectjava.R;
import com.example.finalprojectjava.dao.UserDAO;
import com.example.finalprojectjava.helpers.PrefsHelper;
import com.example.finalprojectjava.helpers.SnackBarHelperActivity;
import com.example.finalprojectjava.managers.SessionManager;
import com.example.finalprojectjava.managers.UserManager;
import com.example.finalprojectjava.models.User;

import java.time.LocalDate;
import java.util.Random;

public class EditProfileActivity extends AppCompatActivity {

    EditText inputFullName, inputUsername, inputAddress;
    CheckBox male, female;
    LinearLayout dateBirthPicker;
    TextView displayDate, skipClick;
    Button saveProfile;
    ImageView goBackClick;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);

        // Assign widgets from XML
        inputFullName = findViewById(R.id.inputFullName);
        inputUsername = findViewById(R.id.inputUsername);
        inputAddress = findViewById(R.id.inputAddress);


        male = findViewById(R.id.cbMale);
        female = findViewById(R.id.cbFemale);;

        dateBirthPicker = findViewById(R.id.dateOfBirthPicker);

        displayDate = findViewById(R.id.inputDate);
        skipClick = findViewById(R.id.skipClick);

        goBackClick = findViewById(R.id.goBackClick);

        male.setChecked(true);
        inputFullName.setKeyListener(null);

        inputFullName.setText(UserManager.getInstance().getCurrentUser().getFull_name());

        String address = UserManager.getInstance().getCurrentUser().getAddress();
        String username = UserManager.getInstance().getCurrentUser().getUsername();
        String email = UserManager.getInstance().getCurrentUser().getUser_email();

        LocalDate today = LocalDate.now();
        int year = today.getYear();
        int month = today.getMonthValue();
        int day = today.getDayOfMonth();

        displayDate.setText(month + "/" + day + "/" + year);

        SessionManager session = new SessionManager(this, email);

        if(username != null) {
            inputUsername.setText(UserManager.getInstance().getCurrentUser().getUsername());
        }

        if(address != null) {
            inputAddress.setText(UserManager.getInstance().getCurrentUser().getAddress());
        }

        male.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                female.setChecked(false);
            }
        });

        female.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                male.setChecked(false);
            }
        });

        saveProfile = findViewById(R.id.saveProfileBtn);
        saveProfile.setOnClickListener(v -> {
            saveEditedProfile();

        });

        goBackClick.setOnClickListener(v -> {
            new Handler().postDelayed(() -> {
                startActivity(new Intent(this, DashboardActivity.class));
                session.setKeyNewUser(false);
                finish();
            }, 500);
        });

        skipClick.setOnClickListener(v -> {
            new Handler().postDelayed(() -> {
                startActivity(new Intent(this, DashboardActivity.class));
                session.setKeyNewUser(false);
                finish();
            }, 500);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void showDatePicker() {
        LocalDate today = LocalDate.now();
        int year = today.getYear();
        int month = today.getMonthValue();
        int day = today.getDayOfMonth();

        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(15f);
        drawable.setColor(Color.WHITE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,R.style.DatePickerTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                displayDate.setText(String.valueOf(month + 1) + "/" + String.valueOf(dayOfMonth) + "/" + String.valueOf(year));
            }
        },year, month-1, day);
        if (datePickerDialog.getWindow() != null) datePickerDialog.getWindow().setBackgroundDrawable(drawable);
        datePickerDialog.show();
        datePickerDialog.getButton(DialogInterface.BUTTON_POSITIVE)
                .setTextColor(getResources().getColor(R.color.purple));
        datePickerDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
                .setTextColor(getResources().getColor(R.color.purple));

    }

    private void saveEditedProfile() {
        String username = inputUsername.getText().toString();
        String gender = male.isChecked() ? "Male" : (female.isChecked() ? "Female" : "Undefined");
        String birthDate = displayDate.getText().toString();
        String address = inputAddress.getText().toString();

        if(username.isEmpty()) {
            int randomInt = new Random().nextInt(6) + 5;
            username = generateRandomString(randomInt);

        }

        String userEmail = new PrefsHelper(this).getString("user_email_key", null);

        UserDAO userDAO = new UserDAO(this);
        User currentUser = userDAO.getUserByEmail(userEmail);


        if (currentUser != null) {
            currentUser.setUsername(username);
            currentUser.setGender(gender);
            currentUser.setBirth_date(birthDate);
            currentUser.setAddress(address);
        }

        userDAO.updateUserProfile(currentUser);
        UserManager.getInstance().setCurrentUser(currentUser);

        SnackBarHelperActivity.showSuccessSnackBar(findViewById(R.id.main), "Profile successfully updated!");

        // ***** Set Key Signed in false because user already change its profile
        SessionManager session = new SessionManager(this, currentUser.getUser_email());
        session.setKeyNewUser(false);

        session.setBonusClaimed(false);

        new Handler().postDelayed(() -> {
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        }, 1500);
    }

    public void openDatePickerDialog(View view) {
        showDatePicker();
    }

    private String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder randomString = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = (int) (Math.random() * characters.length());
            randomString.append(characters.charAt(randomIndex));
        }
        return randomString.toString();
    }
}