package com.example.orderadm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button category_btn, user_btn, request_btn, nuke_btn;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private ProgressDialog progressDialog;
    private MaterialAlertDialogBuilder alertDialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        category_btn = findViewById(R.id.category_adm);
        user_btn = findViewById(R.id.user_adm);
        request_btn = findViewById(R.id.request_adm);
        nuke_btn = findViewById(R.id.nuke_adm);

        alertDialogBuilder = new MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert);
        user = FirebaseAuth.getInstance().getCurrentUser();
        progressDialog = new ProgressDialog(this, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Admin.ver");

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user == null){
                }
            }
        };

        user_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, manage_user.class);
                startActivity(intent);
            }
        });
        category_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, manage_depart.class);
                startActivity(intent);
            }
        });
        request_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, manage_request.class);
                startActivity(intent);
            }
        });
        nuke_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConsole();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(authStateListener);
        getToken();
        subToTopic();
        //getPermis();
        getAccess();
    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().addAuthStateListener(authStateListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_bar, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.about:
                // about
                MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert);
                LayoutInflater inflater =LayoutInflater.from(this);
                View view = inflater.inflate(R.layout.dialog_about, null);
                alertDialogBuilder.setView(view);

                alertDialogBuilder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                alertDialogBuilder.show();
                return true;
            case R.id.logout:
                // logout
                //unsubTopic();

                try {
                    FirebaseInstanceId.getInstance().deleteInstanceId();
                    Toast.makeText(this, "instance deleted", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                FirebaseAuth.getInstance().signOut();
                
                Intent logout_int = new Intent(MainActivity.this, login.class);
                startActivity(logout_int);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK){

            MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(MainActivity.this);
            alertDialogBuilder.setTitle("Exit")
                    .setMessage("Are you sure ?")
                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            moveTaskToBack(true);

                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

            alertDialogBuilder.show();
        }

        return super.onKeyDown(keyCode, event);
    }

    private void deleteData() {
        try {

            FirebaseDatabase.getInstance().getReference("user").child(user.getUid()).removeValue()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            Toast.makeText(MainActivity.this, "Account Deleted", Toast.LENGTH_SHORT).show();
                        }
                    });

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void deleteACC() {

        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Failed Delete", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showDialog(){
        alertDialogBuilder.setTitle("Delete Account");
        alertDialogBuilder.setMessage("Are you sure ?").
                setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        progressDialog.setTitle("Deleting");
                        progressDialog.setMessage("Please wait...");
                        progressDialog.show();
                        deleteACC();
                        deleteData();

                        FirebaseAuth.getInstance().signOut();
                        Intent logout = new Intent(MainActivity.this, login.class);
                        startActivity(logout);
                        finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        alertDialogBuilder.show();
    }

    private void getToken() {

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()){
                            Log.d("FAGGOT", "Failed");
                            return;
                        }

                        String token = task.getResult().getToken();
                        String msg = getString(R.string.msg_token, token);

                        //Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
                        Log.d("Hello Fagg", msg);
                    }
                });
    }

    private void subToTopic(){
        String Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference subRef = FirebaseDatabase.getInstance().getReference("user").child(Uid);

        subRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String setTopic = (String) dataSnapshot.child("depart").getValue();

                FirebaseMessaging.getInstance().subscribeToTopic("T_"+setTopic)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //subRef.child("topic").setValue("T_"+setTopic);

                                //Toast.makeText(MainActivity.this, "Sub To "+ setTopic, Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Toast.makeText(MainActivity.this, "Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void showConsole(){
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        final View view = layoutInflater.inflate(R.layout.dialog_nuke, null);

        alertDialogBuilder.setView(view);

        final MaterialCheckBox CB_request = view.findViewById(R.id.CB_nuke_request);
        final MaterialCheckBox CB_user = view.findViewById(R.id.CB_nuke_user);
        final MaterialCheckBox CB_queue = view.findViewById(R.id.CB_nuke_queue);

        final DatabaseReference nukeRef = FirebaseDatabase.getInstance().getReference();

        alertDialogBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (CB_queue.isChecked()){
                    nukeRef.child("queue").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(MainActivity.this, "Queue node deleted", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("ERROR queue: ", e.getMessage());
                        }
                    });
                }
                else if (CB_request.isChecked()){
                    nukeRef.child("Form").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(MainActivity.this, "Request node deleted", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("ERROR request: ", e.getMessage());
                        }
                    });
                }
                else if (CB_user.isChecked()){
                    nukeRef.child("user").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(MainActivity.this, "User node deleted", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("ERROR user: ", e.getMessage());
                        }
                    });
                }
                else {
                    Toast.makeText(MainActivity.this, "Abort", Toast.LENGTH_SHORT).show();
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialogBuilder.show();
    }

    private void unsubTopic(){
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference getRef = FirebaseDatabase.getInstance().getReference("user").child(uid);

        getRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String getTopic = (String) dataSnapshot.child("topic").getValue();

                FirebaseMessaging.getInstance().unsubscribeFromTopic(getTopic)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this, "Cleared "+getTopic, Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "ERROR logout: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getAccess(){
        Dexter.withActivity(this)
                .withPermission(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                //Toast.makeText(MainActivity.this, "Access granted", Toast.LENGTH_SHORT).show();
                Log.d("method_pdf_ACCESS","Write storage access granted");
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                //Toast.makeText(MainActivity.this, "Access denied", Toast.LENGTH_SHORT).show();
                Log.d("method_pdf_ACCESS","Write storage access denied");
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                Toast.makeText(MainActivity.this, "You must agree", Toast.LENGTH_SHORT).show();
            }
        }).withErrorListener(new PermissionRequestErrorListener() {
            @Override
            public void onError(DexterError error) {
                Log.d("method_pdf_ACCESS ",error.toString());
            }
        })
                .onSameThread()
                .check();
    }

}
