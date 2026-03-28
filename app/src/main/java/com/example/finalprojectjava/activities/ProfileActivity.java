package com.example.finalprojectjava.activities;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.finalprojectjava.database.DatabaseHelper;
import com.example.finalprojectjava.manager.SessionManager;
import com.example.finalprojectjava.manager.UserManager;
import com.example.finalprojectjava.models.User;

public class ProfileActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_profile);

        // ***** EditText variables assigned
        inputFullName = findViewById(R.id.inputFullName);
        inputAddress = findViewById(R.id.inputAddress);
        inputUsername = findViewById(R.id.inputUsername);

        // ***** CheckBox variables assigned
        male = findViewById(R.id.cbMale);
        female = findViewById(R.id.cbFemale);

        male.setChecked(true);

        // ***** LinearLayout variable assigned
        dateBirthPicker = findViewById(R.id.dateOfBirthPicker);

        // ***** TextView variables assigned
        displayDate = findViewById(R.id.inputDate);
        skipClick = findViewById(R.id.skipClick);

        // ***** ImageView variable assigned
        goBackClick = findViewById(R.id.goBackClick);

        inputFullName.setText(UserManager.getInstance().getCurrentUser().getFull_name());

        String address = UserManager.getInstance().getCurrentUser().getAddress();
        String username = UserManager.getInstance().getCurrentUser().getUsername();

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

            Log.e(TAG, "Profile saved!");

        });

        goBackClick.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, LoggingInActivity.class);

            SessionManager session = new SessionManager(this, UserManager.getInstance().getCurrentUser().getUser_email());
            startActivity(intent);
            finish();
        });

        skipClick.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, LessonActivity.class);

            SessionManager session = new SessionManager(this, UserManager.getInstance().getCurrentUser().getUser_email());
            session.setKeyRememberMe(false);
            startActivity(intent);
            finish();
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,R.style.DatePickerTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                displayDate.setText(String.valueOf(month + 1) + "/" + String.valueOf(dayOfMonth) + "/" + String.valueOf(year));
            }
        },2026, 3-1, 22);
        datePickerDialog.show();
        datePickerDialog.getButton(DialogInterface.BUTTON_POSITIVE)
                .setTextColor(getResources().getColor(R.color.purple));
        datePickerDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
                .setTextColor(getResources().getColor(R.color.purple));

    }

    private void saveEditedProfile() {
        String name = inputFullName.getText().toString();
        String username = inputUsername.getText().toString();
        String gender = male.isChecked() ? "Male" : (female.isChecked() ? "Female" : "Undefined");
        String birthDate = displayDate.getText().toString();
        String address = inputAddress.getText().toString();

        User currentUser = UserManager.getInstance().getCurrentUser();
        if (currentUser != null) {

            currentUser.setUsername(username);
            currentUser.setGender(gender);
            currentUser.setBirth_date(birthDate);
            currentUser.setAddress(address);
        }

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        int user_id = currentUser.getUser_id();
        dbHelper.updateUserProfile(user_id, username, gender, birthDate, address);


        // ***** Set Key Signed in false because user already change its profile
        SessionManager session = new SessionManager(this, currentUser.getUser_email());
        session.setKeyNewUser(false);

        Intent intent = new Intent(ProfileActivity.this, LessonActivity.class);
        startActivity(intent);
        finish();
    }

    public void openDatePickerDialog(View view) {
        showDatePicker();
    }
}