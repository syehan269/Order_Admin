package com.example.orderadm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class search extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private Query userQuery;
    private ProgressBar progressBar;
    private String searchInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //searchView = findViewById(R.id.searchView_search);
        recyclerView = findViewById(R.id.RV_search_adm);
        toolbar = findViewById(R.id.toolbar);
        progressBar = findViewById(R.id.progressBar_adm_search);
        searchInput = getIntent().getExtras().getString("id_search");

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Result");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        databaseReference = FirebaseDatabase.getInstance().getReference("user");

        progressBar.setVisibility(View.VISIBLE);
        Toast.makeText(this, "Result: "+searchInput, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayResult();

    }

    private void displayResult() {
        userQuery = databaseReference.orderByChild("userName").startAt(searchInput).endAt(searchInput+"\uf8ff");

        FirebaseRecyclerAdapter<userList, viewHolder> firebaseRecyclerAdapter =
            new FirebaseRecyclerAdapter<userList, viewHolder>(
                    userList.class,
                    R.layout.list_adm_user,
                    search.viewHolder.class,
                    userQuery
            ) {
                @Override
                protected void populateViewHolder(viewHolder viewHolder, userList userList, int i) {

                    final String userNAme = userList.getUserName();
                    final String id_menu = getRef(i).getKey();

                    progressBar.setVisibility(View.GONE);
                    viewHolder.setEmail(userList.getEmail());
                    viewHolder.setType(userList.getLevel());
                    viewHolder.setUserName(userList.getUserName());

                    viewHolder.mview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final MaterialAlertDialogBuilder dialogBuilder =
                                    new MaterialAlertDialogBuilder(search.this, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert);

                            LayoutInflater layoutInflater = LayoutInflater.from(search.this);
                            final View aView = layoutInflater.inflate(R.layout.dialog_mng_adm, null);

                            dialogBuilder.setView(aView);

                            MaterialButton menu_delete = aView.findViewById(R.id.delete_adm);
                            MaterialButton menu_view = aView.findViewById(R.id.view_adm);
                            MaterialButton menu_update = aView.findViewById(R.id.update_adm);

                            menu_delete.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    MaterialAlertDialogBuilder aleBuilder = new MaterialAlertDialogBuilder(search.this);
                                    aleBuilder.setTitle("Delete User");
                                    aleBuilder.setMessage("Delete "+ userNAme+" ?");

                                    aleBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            databaseReference.child(id_menu).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    databaseReference.child(id_menu).child("topic").removeValue();
                                                    Toast.makeText(search.this, "Deleted", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    });
                                    aleBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });

                                    aleBuilder.show();
                                }
                            });
                            menu_update.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(search.this, update_user.class);
                                    intent.putExtra("id_USR_update", id_menu);
                                    startActivity(intent);
                                }
                            });
                            menu_view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(search.this, view_user.class);
                                    intent.putExtra("id_USR_view", id_menu);
                                    startActivity(intent);
                                }
                            });
                            dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                            dialogBuilder.show();
                        }
                    });

                }
            };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class viewHolder extends RecyclerView.ViewHolder{

        View mview;

        public viewHolder(View itemView){
            super(itemView);
            mview = itemView;
        }

        public void setType(String type){
            TextView tYpe = mview.findViewById(R.id.Tv_type_adm_user);
            tYpe.setText(type);
        }
        public void setUserName(String userName){
            TextView uSerName = mview.findViewById(R.id.Tv_name_adm_user);
            uSerName.setText(userName);
        }
        public void setEmail(String email){
            TextView uSerName = mview.findViewById(R.id.Tv_email_adm_user);
            uSerName.setText(email);
        }

        public void setImageList(String URL){
            ImageView photo_img = mview.findViewById(R.id.IV_poto_User);
            Glide.with(photo_img.getContext())
                    .load(URL)
                    .into(photo_img);
        }

    }
}
