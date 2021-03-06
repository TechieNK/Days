package project.days.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;

import project.days.R;

public class PersonalDetailsActivity extends AppCompatActivity {

    private EditText NameET, NickNameET;
    private TextView MaleT, FemaleT, OtherT, DateT;
    private Button NextButton;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    boolean maleb,femaleb, otherb;
    String name,nickname,gender,date;
    int year,month,day;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_details);
        mAuth=FirebaseAuth.getInstance();
        MaleT = findViewById(R.id.male_select);
        FemaleT = findViewById(R.id.female_select);
        OtherT = findViewById(R.id.other_select);
        DateT = findViewById(R.id.pd_dob);


        Toast.makeText(PersonalDetailsActivity.this, "Great you're one among us now!", Toast.LENGTH_SHORT).show();
        DateT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                year = cal.get(Calendar.YEAR);
                month = cal.get(Calendar.MONTH);
                day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        PersonalDetailsActivity.this,
                        android.R.style.Theme_Material_Light_Dialog,
                        mDateSetListener,
                        day,month,year
                );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                year = i;
                month = i1+1;
                day = i2;
                date = day + "/" + month + "/" + year;
                DateT.setText(date);
            }
        };


        MaleT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaleT.setBackgroundColor(getResources().getColor(R.color.colorPurple));
                MaleT.setTextColor(getResources().getColor(R.color.colorWhite));
                OtherT.setTextColor(getResources().getColor(R.color.grey));
                FemaleT.setTextColor(getResources().getColor(R.color.grey));
                FemaleT.setBackgroundColor(getResources().getColor(R.color.colorWhite));

                OtherT.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                maleb = true;
                femaleb = false;
                otherb = false;
            }
        });
        FemaleT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaleT.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                FemaleT.setBackgroundColor(getResources().getColor(R.color.colorPurple));
                FemaleT.setTextColor(getResources().getColor(R.color.colorWhite));
                MaleT.setTextColor(getResources().getColor(R.color.grey));
                OtherT.setTextColor(getResources().getColor(R.color.grey));
                OtherT.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                maleb = false;
                femaleb = true;
                otherb = false;

            }
        });
        OtherT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaleT.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                FemaleT.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                OtherT.setTextColor(getResources().getColor(R.color.colorWhite));
                MaleT.setTextColor(getResources().getColor(R.color.grey));
                FemaleT.setTextColor(getResources().getColor(R.color.grey));
                OtherT.setBackgroundColor(getResources().getColor(R.color.colorPurple));
                maleb = false;
                femaleb = false;
                otherb = true;

            }
        });

        NameET = findViewById(R.id.pd_name);
        NickNameET = findViewById(R.id.pd_nickname);
        NextButton = findViewById(R.id.nextBtn);

        NextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(maleb || femaleb || otherb)
                {
                    if (maleb)
                        gender = "Male";
                    if(femaleb)
                        gender = "Female";
                    if(otherb)
                        gender = "Other";
                }
                name=NameET.getText().toString();
                nickname=NickNameET.getText().toString();
                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(date) || TextUtils.isEmpty(gender))
                {
                    Toast.makeText(PersonalDetailsActivity.this,  "Please fill the details. ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!(TextUtils.isEmpty(name)&&TextUtils.isEmpty(date)&&TextUtils.isEmpty(gender)))
                {

                    usersRef = FirebaseDatabase.getInstance().getReference("Users");
                    uid = mAuth.getCurrentUser().getUid();
                    HashMap<String,Object> result=new HashMap<>();
                    result.put("Name",name);
                    result.put("DOB",date);
                    result.put("Gender",gender);
                    if(!(TextUtils.isEmpty(nickname)))
                    {
                        result.put("Nickname",nickname);
                    }
                    usersRef.child(uid).updateChildren(result).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(PersonalDetailsActivity.this, "Wow! We are so close now", Toast.LENGTH_SHORT).show();
                                Intent mainIntent = new Intent(PersonalDetailsActivity.this, MainActivity.class);
                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(mainIntent);
                            }
                            else
                            {
                                String message = task.getException().getMessage();
                                Toast.makeText(PersonalDetailsActivity.this, "Error occurred. "+message, Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    });
                }
                /*
                else
                {
                    Toast.makeText(PersonalDetailsActivity.this,  "Please fill the mandatory fields. Only Nickname is optional", Toast.LENGTH_SHORT).show();
                }
                 */
            }


        });

    }
}

