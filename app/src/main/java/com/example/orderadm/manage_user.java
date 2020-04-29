package com.example.orderadm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class manage_user extends AppCompatActivity {

    private Toolbar toolbar;
    private FloatingActionButton fab_control_adm;
    private RecyclerView recyclerView_user;
    private DatabaseReference dataReference;
    private ProgressBar progressBar_adm_user;
    private Query queryUser;
    private SearchView searchView;
    private MaterialAlertDialogBuilder alertDialogBuilder;
    private FirebaseRecyclerAdapter<userList, ViewHolderUser> firebaseRecyclerAdapter, firebaseRecyclerAdapterSPR,
            firebaseRecyclerAdapterADM, firebaseRecyclerAdapterAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_user);

        toolbar = findViewById(R.id.toolbar);
        recyclerView_user = findViewById(R.id.recycle_admin);
        fab_control_adm = findViewById(R.id.FAB_control);
        progressBar_adm_user = findViewById(R.id.progressBar_adm_user);

        alertDialogBuilder = new MaterialAlertDialogBuilder(manage_user.this, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Manage account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        recyclerView_user.setLayoutManager(linearLayoutManager);

        dataReference = FirebaseDatabase.getInstance().getReference("user");

        progressBar_adm_user.setVisibility(View.VISIBLE);

        fab_control_adm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(manage_user.this , register.class);
                startActivity(intent);
            }
        });

        //FAB animation
        recyclerView_user.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 ){
                    fab_control_adm.hide();
                }

                else if (dy < 0 ){
                    fab_control_adm.show();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        displayAll();

        super.onStart();
    }

    //bruh intent manual, gara gara bug
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK){

            Intent intent = new Intent(manage_user.this, MainActivity.class);
            startActivity(intent);
        }

        return true;
    }

    //macem method gawe filter list account

    private void displayAll(){
        queryUser = dataReference.orderByChild("level").startAt("Admin").endAt("User");

        firebaseRecyclerAdapterAll = new FirebaseRecyclerAdapter<userList, ViewHolderUser>(
                userList.class,
                R.layout.list_adm_user,
                ViewHolderUser.class,
                queryUser
        ) {
            @Override
            protected void populateViewHolder(ViewHolderUser viewHolderUser, userList userList, int i) {
                progressBar_adm_user.setVisibility(View.GONE);

                viewHolderUser.setUserName(userList.getUserName());
                viewHolderUser.setEmail(userList.getEmail());
                viewHolderUser.setType(userList.getLevel());

                final String userNAme = userList.getUserName();
                final String id_menu = getRef(i).getKey();

                viewHolderUser.mview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final MaterialAlertDialogBuilder dialogBuilder =
                                new MaterialAlertDialogBuilder(manage_user.this, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert);

                        LayoutInflater layoutInflater = LayoutInflater.from(manage_user.this);
                        final View aView = layoutInflater.inflate(R.layout.dialog_mng_adm, null);

                        dialogBuilder.setView(aView);

                        MaterialButton menu_delete = aView.findViewById(R.id.delete_adm);
                        MaterialButton menu_view = aView.findViewById(R.id.view_adm);
                        MaterialButton menu_update = aView.findViewById(R.id.update_adm);

                        menu_delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                MaterialAlertDialogBuilder aleBuilder = new MaterialAlertDialogBuilder(manage_user.this);
                                aleBuilder.setTitle("Delete User");
                                aleBuilder.setMessage("Delete "+ userNAme+" ?");

                                aleBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dataReference.child(id_menu).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                dataReference.child(id_menu).child("topic").removeValue();
                                                Toast.makeText(manage_user.this, "Deleted", Toast.LENGTH_SHORT).show();
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

                        menu_view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(manage_user.this, view_user.class);
                                intent.putExtra("id_USR_view", id_menu);
                                startActivity(intent);
                            }
                        });

                        menu_update.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(manage_user.this, update_user.class);
                                intent.putExtra("id_USR_update", id_menu);
                                startActivity(intent);
                            }
                        });

                        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        dialogBuilder.show();
                    }
                });

            }
        };
        recyclerView_user.setAdapter(firebaseRecyclerAdapterAll);
    }

    private void displayUser() {
        fab_control_adm.show();
        queryUser = dataReference.orderByChild("level").equalTo("User");

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<userList, ViewHolderUser>(
                userList.class,
                R.layout.list_adm_user,
                ViewHolderUser.class,
                queryUser
        ) {
            @Override
            protected void populateViewHolder(ViewHolderUser viewHolderUser, userList model, int i) {
                progressBar_adm_user.setVisibility(View.GONE);

                viewHolderUser.setUserName(model.getUserName());
                viewHolderUser.setType(model.getLevel());
                viewHolderUser.setEmail(model.getEmail());

                final String userNAme = model.getUserName();
                final String id_menu = getRef(i).getKey();

                viewHolderUser.mview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final MaterialAlertDialogBuilder dialogBuilder =
                                new MaterialAlertDialogBuilder(manage_user.this, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert);

                        LayoutInflater layoutInflater = LayoutInflater.from(manage_user.this);
                        final View aView = layoutInflater.inflate(R.layout.dialog_mng_adm, null);

                        dialogBuilder.setView(aView);

                        MaterialButton menu_delete = aView.findViewById(R.id.delete_adm);
                        MaterialButton menu_view = aView.findViewById(R.id.view_adm);
                        MaterialButton menu_update = aView.findViewById(R.id.update_adm);

                        menu_delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                MaterialAlertDialogBuilder aleBuilder = new MaterialAlertDialogBuilder(manage_user.this);
                                aleBuilder.setTitle("Delete User");
                                aleBuilder.setMessage("Delete "+ userNAme+" ?");

                                aleBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dataReference.child(id_menu).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                dataReference.child(id_menu).child("topic").removeValue();
                                                Toast.makeText(manage_user.this, "Deleted", Toast.LENGTH_SHORT).show();
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

                        menu_view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(manage_user.this, view_user.class);
                                intent.putExtra("id_USR_view", id_menu);
                                startActivity(intent);
                            }
                        });

                        menu_update.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(manage_user.this, update_user.class);
                                intent.putExtra("id_USR_update", id_menu);
                                startActivity(intent);
                            }
                        });

                        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        dialogBuilder.show();
                    }
                });

            }
        };
        recyclerView_user.swapAdapter(firebaseRecyclerAdapter,true);
    }

    private void displaySuper(){
        queryUser = dataReference.orderByChild("level").equalTo("Super User");

        firebaseRecyclerAdapterSPR = new FirebaseRecyclerAdapter<userList, ViewHolderUser>(
                userList.class,
                R.layout.list_adm_user,
                ViewHolderUser.class,
                queryUser
        ) {
            @Override
            protected void populateViewHolder(ViewHolderUser viewHolderUser, userList userList, int i) {
                progressBar_adm_user.setVisibility(View.GONE);

                viewHolderUser.setUserName(userList.getUserName());
                viewHolderUser.setType(userList.getLevel());
                viewHolderUser.setEmail(userList.getEmail());

                final String userNAme = userList.getUserName();
                final String id_menu = getRef(i).getKey();

                viewHolderUser.mview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final MaterialAlertDialogBuilder dialogBuilder =
                                new MaterialAlertDialogBuilder(manage_user.this, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert);

                        LayoutInflater layoutInflater = LayoutInflater.from(manage_user.this);
                        final View aView = layoutInflater.inflate(R.layout.dialog_mng_adm, null);

                        dialogBuilder.setView(aView);

                        MaterialButton menu_delete = aView.findViewById(R.id.delete_adm);
                        MaterialButton menu_view = aView.findViewById(R.id.view_adm);
                        MaterialButton menu_update = aView.findViewById(R.id.update_adm);

                        menu_delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                MaterialAlertDialogBuilder aleBuilder = new MaterialAlertDialogBuilder(manage_user.this);
                                aleBuilder.setTitle("Delete User");
                                aleBuilder.setMessage("Delete "+ userNAme+" ?");

                                aleBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dataReference.child(id_menu).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(manage_user.this, "Deleted", Toast.LENGTH_SHORT).show();
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

                        menu_view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(manage_user.this, view_user.class);
                                intent.putExtra("id_USR_view", id_menu);
                                startActivity(intent);
                            }
                        });

                        menu_update.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(manage_user.this, update_user.class);
                                intent.putExtra("id_USR_update", id_menu);
                                startActivity(intent);
                            }
                        });

                        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        dialogBuilder.show();
                    }
                });
            }
        };

        recyclerView_user.swapAdapter(firebaseRecyclerAdapterSPR, true);
    }

    private void displayAdmin(){
        fab_control_adm.show();
        queryUser = dataReference.orderByChild("level").equalTo("Admin");

        firebaseRecyclerAdapterADM = new FirebaseRecyclerAdapter<userList, ViewHolderUser>(
                userList.class,
                R.layout.list_adm_user,
                ViewHolderUser.class,
                queryUser
        ) {
            @Override
            protected void populateViewHolder(ViewHolderUser viewHolderUser, userList userList, int i) {
                progressBar_adm_user.setVisibility(View.GONE);

                viewHolderUser.setUserName(userList.getUserName());
                viewHolderUser.setType(userList.getLevel());
                viewHolderUser.setEmail(userList.getEmail());

                final String userNAme = userList.getUserName();
                final String id_menu = getRef(i).getKey();

                viewHolderUser.mview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final MaterialAlertDialogBuilder dialogBuilder =
                                new MaterialAlertDialogBuilder(manage_user.this, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert);

                        LayoutInflater layoutInflater = LayoutInflater.from(manage_user.this);
                        final View aView = layoutInflater.inflate(R.layout.dialog_mng_adm, null);

                        dialogBuilder.setView(aView);

                        MaterialButton menu_delete = aView.findViewById(R.id.delete_adm);
                        MaterialButton menu_view = aView.findViewById(R.id.view_adm);
                        MaterialButton menu_update = aView.findViewById(R.id.update_adm);

                        menu_delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                MaterialAlertDialogBuilder aleBuilder = new MaterialAlertDialogBuilder(manage_user.this);
                                aleBuilder.setTitle("Delete User");
                                aleBuilder.setMessage("Delete "+ userNAme+" ?");

                                aleBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dataReference.child(id_menu).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(manage_user.this, "Deleted", Toast.LENGTH_SHORT).show();
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

                        menu_view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(manage_user.this, view_user.class);
                                intent.putExtra("id_USR_view", id_menu);
                                startActivity(intent);
                            }
                        });

                        menu_update.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(manage_user.this, update_user.class);
                                intent.putExtra("id_USR_update", id_menu);
                                startActivity(intent);
                            }
                        });

                        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        dialogBuilder.show();
                    }
                });
            }
        };
        recyclerView_user.swapAdapter(firebaseRecyclerAdapterADM, true);
    }

    public static class ViewHolderUser extends RecyclerView.ViewHolder{

        View mview;

        public ViewHolderUser(View itemView){
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_control_adm, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.show_admin:
                displayAdmin();

                return true;
            case R.id.show_super_user:
                displaySuper();

                return true;

            case R.id.show_user:
                displayUser();

                return true;

            case R.id.show_all:
                displayAll();

                return true;

            case R.id.menu_search:
                //Intent intent = new Intent(manage_user.this, search.class);
                //startActivity(intent);
                setSearch();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setSearch(){

        LayoutInflater inflater = LayoutInflater.from(manage_user.this);
        final View view = inflater.inflate(R.layout.dialog_search, null);
        alertDialogBuilder.setView(view);

        //alertDialogBuilder.setTitle("Search User");
        alertDialogBuilder.setPositiveButton("Search", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                EditText search_Et = view.findViewById(R.id.search_input);
                final String getInput = search_Et.getText().toString().trim();

                Intent intent = new Intent(manage_user.this, search.class);
                intent.putExtra("id_search", getInput);
                startActivity(intent);

                //Toast.makeText(manage_user.this, getInput, Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialogBuilder.show();
    }

}
