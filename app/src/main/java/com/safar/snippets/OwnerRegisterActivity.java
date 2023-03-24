package com.safar.snippets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.safar.snippets.model.Owner;

public class OwnerRegisterActivity extends AppCompatActivity {

    private static final String TAG = "OwnerRegisterActivity";
    private EditText etName, etEmail, etRollNumber, etStar, etPassword;
    private Button btnSubmit;
    private String name, email, password;
    private int rollNumber;
    private double star;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private void init() {

        initialize();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getText();

                if (checkEmpty()) {
                    firebaseAuth
                            .createUserWithEmailAndPassword(email, password)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    firebaseFirestore
                                            .collection("User")
                                            .document(email)
                                            .set(new Owner(name, email, "Owner", rollNumber, star))
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(OwnerRegisterActivity.this, "Registration Completed", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(OwnerRegisterActivity.this, MainActivity.class));
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(OwnerRegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(OwnerRegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(OwnerRegisterActivity.this, "Empty Fields are not allowed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean checkEmpty() {

        if (name.equals("") || email.equals("") || password.equals("")) {
            Log.d(TAG, "checkEmpty: false");
            return false;
        }

        if (rollNumber == -1){
            Log.d(TAG, "checkEmpty: rollNumber");
            return false;
        }

        if (star == -1.0){
            Log.d(TAG, "checkEmpty: star");
            return false;
        }

        Log.d(TAG, "checkEmpty: true");
        return true;
    }

    private void initialize() {

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etRollNumber = findViewById(R.id.etRollNumber);
        etStar = findViewById(R.id.etStar);
        etPassword = findViewById(R.id.etPassword);

        btnSubmit = findViewById(R.id.btnSubmit);

        name = "";
        email = "";
        password = "";
        rollNumber = -1;
        star = -1.0;

        firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void getText() {
        name = etName.getText().toString().trim();
        email = etEmail.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        if (!(etRollNumber.getText().toString().trim().equals("")) && !(etStar.getText().toString().trim().equals(""))) {
            rollNumber = Integer.parseInt(etRollNumber.getText().toString().trim());
            star = Double.parseDouble(etStar.getText().toString().trim());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_register);

        init();
    }
}