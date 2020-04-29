package com.example.orderadm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class view_user extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView TV_email, TV_pass, TV_name, TV_depart, TV_level;
    private String updateKey, fbKey, getName, getPass, getEmail, getLevel, getdepart;
    private DatabaseReference userReference;
    private Query userQuery;
    private FloatingActionButton FAB_detail_USR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user);

        toolbar = findViewById(R.id.toolbar);
        TV_depart = findViewById(R.id.TV_detail_depart);
        TV_email = findViewById(R.id.TV_detail_email);
        TV_level = findViewById(R.id.TV_detail_level);
        TV_name = findViewById(R.id.TV_detail_name);
        TV_pass = findViewById(R.id.TV_detail_password);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("View user");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fbKey = getIntent().getExtras().getString("id_USR_view");
        userReference = FirebaseDatabase.getInstance().getReference("user");
        userQuery = userReference.child(fbKey);

        showView();

    }

    private void showView() {
        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getdepart = (String) dataSnapshot.child("depart").getValue();
                getEmail = (String) dataSnapshot.child("email").getValue();
                getLevel = (String) dataSnapshot.child("level").getValue();
                getName = (String) dataSnapshot.child("userName").getValue();
                getPass = (String) dataSnapshot.child("pass").getValue();

                TV_depart.setText(getdepart);
                TV_email.setText(getEmail);
                TV_level.setText(getLevel);
                TV_name.setText(getName);
                TV_pass.setText(getPass);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(view_user.this, "ERROR "+ databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

}
