package com.pirhotech.hammingchat.activities;

import static com.pirhotech.hammingchat.utilities.UtilityMethods.showToast;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pirhotech.hammingchat.R;
import com.pirhotech.hammingchat.databinding.ActivityResetPasswordBinding;
import com.pirhotech.hammingchat.databinding.ActivitySignUpBinding;
import com.pirhotech.hammingchat.utilities.Constants;
import com.pirhotech.hammingchat.utilities.PreferenceManager;
import com.pirhotech.hammingchat.utilities.UtilityMethods;

import java.util.HashMap;

public class ResetPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityResetPasswordBinding  binding;
    private PreferenceManager preferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResetPasswordBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());


        preferenceManager=new PreferenceManager(this);


    }

    private void setClickListeners() {
        binding.buttonReset.setOnClickListener(this);

    }
    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.buttonReset) {
            if (isValidResetDetails()) {


                FirebaseFirestore database = FirebaseFirestore.getInstance();
                database.collection(Constants.KEY_COLLECTION_USERS)
                        .whereEqualTo(Constants.KEY_PHONE, preferenceManager.getString(Constants.KEY_PHONE))
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() == 0) {
                                // User is not registered
                                showToast(this,"This number is not registered.");
                            } else {
                                // User is not already registered
                                resetPassword();
                            }
                        });

            }

        } else {
            Toast.makeText(this, "Invalid Click", Toast.LENGTH_SHORT).show();
        }
    }

    private void resetPassword(){
        loading(true);

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_PHONE, preferenceManager.getString(Constants.KEY_PHONE))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {
                        DocumentSnapshot snapshot = task.getResult().getDocuments().get(0);

                        HashMap<String, Object> userData = new HashMap<>();

                        userData.put(Constants.KEY_PASSWORD, binding.inputPassword.getText().toString());
                        userData.put(Constants.KEY_USER_ID, snapshot.getId());
                        userData.put(Constants.KEY_NAME, snapshot.get(Constants.KEY_NAME));
                        userData.put(Constants.KEY_PHONE, preferenceManager.getString(Constants.KEY_PHONE));
                        userData.put(Constants.KEY_IMAGE, snapshot.get(Constants.KEY_IMAGE));


                        preferenceManager.putString(Constants.KEY_USER_ID, (String) userData.get(Constants.KEY_USER_ID));
                        preferenceManager.putString(Constants.KEY_NAME, (String) userData.get(Constants.KEY_NAME));
                        preferenceManager.putString(Constants.KEY_PHONE, (String) userData.get(Constants.KEY_PHONE));
                        preferenceManager.putString(Constants.KEY_IMAGE, (String) userData.get(Constants.KEY_IMAGE));

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra(Constants.USER_DATA, userData);
                        intent.putExtra(Constants.KEY_PHONE, preferenceManager.getString(Constants.KEY_PHONE));
                        intent.putExtra(Constants.STARTER_ACTIVITY, "forgetPassword");
                        startActivity(intent);
                        loading(false);
                    } else {
                        Toast.makeText(this, "Unable to sign in.", Toast.LENGTH_SHORT).show();
                        loading(false);
                    }
                });
    }

    private Boolean isValidResetDetails() {
        // TODO: Complete Validation
       if (binding.inputPassword.getText().toString().trim().isEmpty()) {
            showToast(this,"Enter new Password");
            return false;
        }  else if (!binding.inputPassword.getText().toString().equals(binding.inputConfirmPassword.getText().toString())) {
            showToast(this,"Password & Confirm Password must be same");
            return false;
        } else {
            return true;
        }
    }

    private void loading(boolean isLoading) {
        if (isLoading) {
            binding.buttonReset.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.buttonReset.setVisibility(View.VISIBLE);
        }
    }
}