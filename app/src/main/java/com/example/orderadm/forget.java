package com.example.orderadm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forget extends AppCompatActivity {

    private EditText et_Edittext;
    private Button forget_btn;
    private FirebaseAuth fbAuth;
    private Toolbar toolbar;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        forget_btn = findViewById(R.id.forget_btn);
        et_Edittext = findViewById(R.id.forget_et);
        fbAuth = FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        forget_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = et_Edittext.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(forget.this, "Email empty", Toast.LENGTH_SHORT).show();
                }

                else {
                    fbAuth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(forget.this, "Request send", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(forget.this, "Request failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }

            }
        });

    }

}

