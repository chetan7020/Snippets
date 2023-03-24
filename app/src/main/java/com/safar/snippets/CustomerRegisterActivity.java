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
import com.safar.snippets.model.Customer;
import com.safar.snippets.model.Owner;

public class CustomerRegisterActivity extends AppCompatActivity {

    private static final String TAG = "CustomerRegisterActivity";
    private EditText etName, etEmail, etPassword;
    private String name, email, password;
    private Button btnSubmit;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private void init(){
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
                                            .set(new Customer(name, email, "Customer"))
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(CustomerRegisterActivity.this, "Registration Completed", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(CustomerRegisterActivity.this, MainActivity.class));
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(CustomerRegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(CustomerRegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(CustomerRegisterActivity.this, "Empty Fields are not allowed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initialize(){
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        btnSubmit = findViewById(R.id.btnSubmit);

        name = "";
        email = "";
        password = "";

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void getText() {
        name = etName.getText().toString().trim();
        email = etEmail.getText().toString().trim();
        password = etPassword.getText().toString().trim();
    }

    private boolean checkEmpty() {

        if (name.equals("") || email.equals("") || password.equals("")) {
            return false;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_register);

        init();
    }
}