package com.example.orderadm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class form extends AppCompatActivity {

    private TextView kategori_SP;
    private EditText deskripsi_Et, Name_Et, request_Et;
    private ArrayList<String[]> kategoriString = new ArrayList<String[]>();
    private Toolbar toolbar;
    private DatabaseReference listReference, formReference;
    private String fbAuth, getRequest, getName, getUid, getDesc, getDepart;
    private String[] DepartAdapter = new String[] {"Administration","Finance-Accounting","HR-GA","IT","Marketing","Primary","Secondary"};
    private FirebaseUser getUser;
    private MaterialAlertDialogBuilder dialogBuilder;

    private ProgressDialog progressDialog;

    private static final String channel_id = "NOTIFY_ID";
    private static final String channel_title = "Notification Title";
    private static final String channel_desc = "Notification Description";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        toolbar = findViewById(R.id.toolbar);
        kategori_SP = findViewById(R.id.SP_form_O);
        deskripsi_Et = findViewById(R.id.ET_desc_form);
        Name_Et = findViewById(R.id.ET_from_form);
        request_Et = findViewById(R.id.ET_subject_form);

        fbAuth = FirebaseAuth.getInstance().getCurrentUser().getUid();
        getUser = FirebaseAuth.getInstance().getCurrentUser();
        formReference = FirebaseDatabase.getInstance().getReference("Form");

        progressDialog = new ProgressDialog(this, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert);

        notifChannel();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Form");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        kategori_SP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayDialog();
                dialogBuilder.show();
            }
        });
        displayDialog();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_request, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.menu_request:
                // save
                simpanArtikel();

                if (getUser.getUid().equals(getUid)){
                    //notifyRequest();
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void simpanArtikel(){

        getName = Name_Et.getText().toString();
        getDesc = deskripsi_Et.getText().toString();
        getDepart = kategori_SP.getText().toString().trim();
        getRequest = request_Et.getText().toString();

         getUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference databaseReference =
                FirebaseDatabase.getInstance().getReference("user").child(getUid);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd ");
        final String currentTime = dateFormat.format(new Date());
        final String status = "NO";
        final String approval = "Disapproved";

        final String query_handle = getDepart + "_" + status;

        final String getTopic = "T_"+ getDepart;
        final String supervised = "not approved";
        final String complete = "not completed";

        if (getName.isEmpty() && getDesc.isEmpty() && getRequest.isEmpty()){
            Toast.makeText(form.this, "Data must be filled", Toast.LENGTH_SHORT).show();
        }

        else {

            progressDialog();
            queueNotif();

            FirebaseDatabase.getInstance().getReference().child("Form").push()
                    .setValue(new profileForm(getRequest, getName, getDesc, getDepart, status, currentTime, getUid, approval, query_handle, supervised, complete ))
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        formReference.push().child(getUid);

                        Intent intent = new Intent(form.this, form.class);
                        startActivity(intent);
                        finish();

                    }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(form.this, "ERROR",Toast.LENGTH_LONG).show();
                        }
                    });

        }

    }

    private void progressDialog(){
        progressDialog.setTitle("Uploading");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
    }

    private void notifChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(channel_id, channel_title, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(channel_desc);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    private void queueNotif(){

        getName = Name_Et.getText().toString();
        getDesc = deskripsi_Et.getText().toString();
        getDepart = kategori_SP.getText().toString().trim();
        getRequest = request_Et.getText().toString();

        final String getTopic = "T_"+ getDepart;

        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("queue");

        database.push().setValue(new queueNotif(getRequest, getDesc, getName, getDepart, getTopic))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(form.this, "queue created", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(form.this, "queue failed", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void displayDialog() {
        dialogBuilder = new MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert);
        final String data = "dialog_depart";

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        final View lView = layoutInflater.inflate(R.layout.dialog_depart_pro, null);
        final RecyclerView rv_depart = lView.findViewById(R.id.RV_depart_pro);

        rv_depart.setLayoutManager(new LinearLayoutManager(form.this));

        dialogBuilder.setView(lView);
        Log.d(data, "setting layout success");

        DatabaseReference queryDepart = FirebaseDatabase.getInstance().getReference().child("Type");
        Log.d(data,"setting data path");

        FirebaseRecyclerAdapter<category, form.ViewHolderCategory> firebaseAdapter =
                new FirebaseRecyclerAdapter<category, form.ViewHolderCategory>(
                        category.class,
                        R.layout.list_depart_pro,
                        form.ViewHolderCategory.class,
                        queryDepart
                ) {
                    @Override
                    protected void populateViewHolder(form.ViewHolderCategory viewHolder, category model, int i) {
                        Log.d(data, "adapter created");
                        viewHolder.setCategory(model.getCategory());
                        Log.d(data, "set the data");

                        final String key = getRef(i).getKey();
                        final String value = model.getCategory();

                        viewHolder.view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(form.this, "Sent to "+ value, Toast.LENGTH_SHORT).show();
                                kategori_SP.setText(value);

                            }
                        });
                    }
                };

        rv_depart.setAdapter(firebaseAdapter);

        dialogBuilder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

    }
    
    public static class ViewHolderCategory extends RecyclerView.ViewHolder{

        View view;

        public ViewHolderCategory(View itemView){
            super(itemView);
            view = itemView;
        }

        public void setCategory(String Category){
            TextView kategori = view.findViewById(R.id.CB_depart_pro);
            kategori.setText(Category);

        }

    }
    
}