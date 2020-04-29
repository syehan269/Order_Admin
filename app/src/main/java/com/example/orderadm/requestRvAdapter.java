package com.example.orderadm;

import android.content.Intent;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class requestRvAdapter {

    public DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Form");
    public RecyclerView rvAdapter;
    public FirebaseAuth fbAuth;
    public ProgressBar progressBar;
    public Query query;
    public FirebaseRecyclerAdapter<handleAdapter, manage_request.ViewHolderRequest> firebaseRecyclerAdapter;

    public void setAll(){
        query = databaseReference.orderByChild("status").equalTo("NO");
    }
    public void setPrimary(){
        query = databaseReference.orderByChild("queryHandle").equalTo("Primary_NO");
    }
    public void setSecondary(){
        query = databaseReference.orderByChild("queryHandle").equalTo("Secondary_NO");
    }
    public void setIt(){
        query = databaseReference.orderByChild("queryHandle").equalTo("IT_NO");
    }
    public void setHrga(){
        query = databaseReference.orderByChild("queryHandle").equalTo("HR-GA_NO");
    }
    public void setMarketing(){
        query = databaseReference.orderByChild("queryHandle").equalTo("Marketing_NO");
    }
    public void setAdminist(){
        query = databaseReference.orderByChild("queryHandle").equalTo("Administarition_NO");
    }
    public void setFinance(){
        query = databaseReference.orderByChild("queryHandle").equalTo("Finance-Accounting_NO");
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

    }


}
