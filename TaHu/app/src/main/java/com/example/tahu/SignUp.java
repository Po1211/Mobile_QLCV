package com.example.tahu;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class SignUp extends AppCompatActivity {


    TextInputEditText signupUsername, signupFullname, signupEmail, signupPassword, signupConfirmPass;
    TextView loginRedirectText;
    Button signupBtn;
    FirebaseDatabase db;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signupUsername = findViewById(R.id.signup_name);
        signupFullname = findViewById(R.id.signup_fullname);
        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_password);
        signupConfirmPass = findViewById(R.id.signup_confirm_password);
        signupBtn = findViewById(R.id.signup_btn);
        loginRedirectText = findViewById(R.id.loginRedirectText);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSignUp();
            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent);
            }
        });
    }

    private void handleSignUp() {
        db = FirebaseDatabase.getInstance();
        ref = db.getReference("users");

        String username = signupUsername.getText().toString();
        String fullname = signupFullname.getText().toString();
        String email = signupEmail.getText().toString();
        String password = signupPassword.getText().toString();
        String confirmPass = signupConfirmPass.getText().toString();

        if (!password.equals(confirmPass)) {
            Toast.makeText(SignUp.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
            return;
        }

        checkEmailUnique(email, new FirebaseCallback() {
            @Override
            public void onCallback(boolean isUnique) {
                if (!isUnique) {
                    Toast.makeText(SignUp.this, "Email is already registered!", Toast.LENGTH_SHORT).show();
                } else {
                    checkUsernameUnique(username, new FirebaseCallback() {
                        @Override
                        public void onCallback(boolean isUnique) {
                            if (!isUnique) {
                                Toast.makeText(SignUp.this, "Username is already taken!", Toast.LENGTH_SHORT).show();
                            } else {
                                createUser(username, fullname, email, password);
                            }
                        }
                    });
                }
            }
        });
    }

    private void checkEmailUnique(String email, FirebaseCallback callback) {
        ref.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                callback.onCallback(!dataSnapshot.exists());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SignUp.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkUsernameUnique(String username, FirebaseCallback callback) {
        ref.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                callback.onCallback(!dataSnapshot.exists());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SignUp.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createUser(String username, String fullname, String email, String password) {
        // Reference to the users node
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");

        // Check if the username already exists
        ref.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Username already exists
                    Toast.makeText(SignUp.this, "Username already taken. Please choose another one.", Toast.LENGTH_SHORT).show();
                } else {
                    // Username is unique, proceed with creating the user
                    UsersModel usersModel = new UsersModel(username, fullname, email, password);
                    ref.child(username).setValue(usersModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SignUp.this, "You have signed up successfully!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignUp.this, Login.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(SignUp.this, "Signup failed. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors.
                Toast.makeText(SignUp.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private interface FirebaseCallback {
        void onCallback(boolean isUnique);
    }

}