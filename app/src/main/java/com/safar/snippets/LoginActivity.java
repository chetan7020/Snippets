package com.safar.snippets;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.safar.snippets.model.Customer;
import com.safar.snippets.model.Owner;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private String email, password;

    private TextView tvOwnerRegister, tvCustomerRegister;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private void init() {
        initialize();

        tvOwnerRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, OwnerRegisterActivity.class));
            }
        });

        tvCustomerRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, CustomerRegisterActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getText();

                if (checkEmpty()) {
                    firebaseFirestore
                            .collection("User")
                            .document(email)
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot document) {
                                    if (document.exists()) {
                                        String userType = document.get("userType").toString();
                                        Log.d(TAG, "onSuccess: " + userType);

                                        if (userType.equals("Owner")) {
                                            Owner owner = document.toObject(Owner.class);

                                            Log.d(TAG, "onSuccess: " + owner.getName() + " " + owner.getEmail() + " " + owner.getUserType() + " " + owner.getRollNumber() + " " + owner.getRating());

                                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                            finish();
                                        } else if (userType.equals("Customer")) {
                                            Customer customer = document.toObject(Customer.class);

                                            Log.d(TAG, "onSuccess: " + customer.getName() + " " + customer.getEmail());

                                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                            finish();
                                        }
                                    } else {
                                        Toast.makeText(LoginActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }

    private void initialize() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        btnLogin = findViewById(R.id.btnLogin);

        tvOwnerRegister = findViewById(R.id.tvOwnerRegister);
        tvCustomerRegister = findViewById(R.id.tvCustomerRegister);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void getText() {
        email = etEmail.getText().toString().trim();
        password = etPassword.getText().toString().trim();
    }

    private boolean checkEmpty() {

        if (email.equals("") || password.equals("")) {
            return false;
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }
}