package com.example.orderadm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
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

import org.spongycastle.jcajce.provider.symmetric.ARC4;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class preview_request extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private Toolbar toolbar;
    private String fbKey, getName, getDesc, getDepart, getDate, getTitle, getApprove, getSatus ;
    private TextView tv_name, tv_depart, tv_date, tv_title, tv_desc, tv_approval, tv_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_request);

        toolbar = findViewById(R.id.toolbar);
        tv_date = findViewById(R.id.TV_date_req);
        tv_depart = findViewById(R.id.TV_depart_req);
        tv_desc = findViewById(R.id.TV_desc_req);
        tv_name = findViewById(R.id.TV_name_req);
        tv_title = findViewById(R.id.TV_subject_req);
        tv_status = findViewById(R.id.status_read_req);
        tv_approval = findViewById(R.id.approve_read_req);

        fbKey = getIntent().getExtras().getString("id_request");

        databaseReference = FirebaseDatabase.getInstance().getReference("Form").child(fbKey);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Request");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setPreview();

    }

    private void updateRequest() {

        try {
            databaseReference.child(fbKey).child("status").setValue("YES");
            databaseReference.child(fbKey).child("queryHandle").setValue(getDepart + "_YES");
            Intent intent = new Intent(preview_request.this, MainActivity.class);
            startActivity(intent);

        } catch (Exception e){
            e.printStackTrace();
        }
        Toast.makeText(preview_request.this, "Updated", Toast.LENGTH_SHORT).show();

    }

    private void setPreview() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getName = (String) dataSnapshot.child("name").getValue();
                getDepart = (String) dataSnapshot.child("category").getValue();
                getApprove = (String) dataSnapshot.child("approval").getValue();
                getDate = (String) dataSnapshot.child("date").getValue();
                getDesc = (String) dataSnapshot.child("description").getValue();
                getSatus = (String) dataSnapshot.child("status").getValue();
                getTitle = (String) dataSnapshot.child("request").getValue();

                //tv_approval.setText(getApprove);
                tv_date.setText(getDate);
                tv_depart.setText(getDepart);
                tv_desc.setText(getDesc);
                tv_title.setText(getTitle);
                //tv_status.setText(getSatus);
                tv_name.setText(getName);

                tv_approval.setText(getApprove);

                if (getSatus.equals("NO")){
                    tv_status.setText(R.string.status_NO);
                }
                else if (getSatus.equals("YES")){
                    tv_status.setText(R.string.status_YES);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_export, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.export_PDF:
                Log.d("method_pdf","clicked");
                getData();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getData(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("method_pdf","getting data");
                
                String dName = dataSnapshot.child("name").getValue().toString();
                String dDate = dataSnapshot.child("date").getValue().toString();
                String dDepart = dataSnapshot.child("category").getValue().toString();
                String dStatus =dataSnapshot.child("status").getValue().toString();
                String dDescript = dataSnapshot.child("description").getValue().toString();
                String dRequest = dataSnapshot.child("request").getValue().toString();
                String dApprove = dataSnapshot.child("approval").getValue().toString();
                String dSupervised = dataSnapshot.child("supervised").getValue().toString();
                String dComplete = dataSnapshot.child("complete").getValue().toString();

                createPdf(dName,dDate,dDepart,dStatus,dDescript,dRequest,dApprove,dComplete,dSupervised);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("method_pdf", databaseError.getMessage());
            }
        });
    }

    private void createPdf(String name, String date, String depart, String status, String descript, String request, String approve, String complete, String supervised){
        try {

            //File dFolder = new File(String.valueOf(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)));
            File aFolder = new File("/sdcard/Order PDF Export/");

            if (!aFolder.exists()){
                aFolder.mkdirs();
                Log.d("method_pdf","create directory TOPKEK");
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy ");
            final String currentTime = dateFormat.format(new Date());
            //Long second = System.currentTimeMillis();

            File file = new File(aFolder,currentTime+"_"+name+"_"+depart+".pdf");
            file.createNewFile();
            OutputStream outputStream = new FileOutputStream(file);

            Document document = new Document(PageSize.A5);
            PdfWriter.getInstance(document, outputStream);
            document.open();

            BaseColor black = BaseColor.BLACK;

            BaseFont fontTitle = BaseFont.createFont("res/font/lato_regular.ttf","UTF-8", BaseFont.EMBEDDED);
            BaseFont fontSubText = BaseFont.createFont("res/font/open_sans_semi_bold.ttf","UTF-8", BaseFont.EMBEDDED);
            BaseFont fontSub = BaseFont.createFont("res/font/roboto_regular.ttf","UTF-8",BaseFont.EMBEDDED);

            Font textFont = new Font(fontSubText,13,Font.BOLD, black);
            Font fontDetail = new Font(fontSub, 13,Font.NORMAL, black);
            Font textSubFont = new Font(fontSubText,13,Font.NORMAL, black);
            Font textTitle = new Font(fontTitle,16,Font.BOLD, black);

            document.addCreator(name);
            document.addAuthor(name);
            document.addCreationDate();
            document.addTitle(request);

            addItem(document, "Work Order Request Form",Element.ALIGN_CENTER,textTitle);
            addSeparator(document);
            addSpace(document);
            addBlank(document);

            addTextData2(document,"From: "+name,"To Departement: "+depart,textFont, fontDetail);
            Log.d("method_pdf","name");

            //addTextData(document,"To departement: ",depart,textFont, fontDetail);
            Log.d("method_pdf","to");

            addTextData(document,"Date: ",date,textFont, fontDetail);
            Log.d("method_pdf","Date");

            addTextData(document,"Request: ",request,textFont, fontDetail);
            Log.d("method_pdf","request");
            addSeparator(document);

            addItem(document,"Description: ",Element.ALIGN_LEFT,textFont);
            addItem(document,descript,Element.ALIGN_LEFT,fontDetail);
            Log.d("method_pdf","descript");
            //addSpace(document);
            addBlank(document);
            addSeparator(document);

            addTextData2(document,"Acknowledged by:","Completed by:",textFont, textTitle);
            addTextData2(document,supervised,complete,textFont, textSubFont);
            Log.d("method_pdf","approve");

            addSpace(document);

            Log.d("method_pdf","completed");

            document.close();
            Log.d("method_pdf","document closed");
            Toast.makeText(this, "Exported to Storage/Order export PDF", Toast.LENGTH_SHORT).show();

        } catch (FileNotFoundException e) {
            Log.d("method_pdf_File",e.getMessage());
        } catch (DocumentException e) {
            Log.d("method_pdf_Doc",e.getMessage());
        } catch (IOException e) {
            Log.d("method_pdf_i/o",e.getMessage());
        }
    }

    private void addItem(Document document, String string, int align, Font font) throws DocumentException {
        Paragraph para = new Paragraph(string,font);
        para.setAlignment(align);
        document.add(new Paragraph(para));
    }

    private void addSeparator(Document document) throws DocumentException {
        LineSeparator lineSeparator = new LineSeparator();
        lineSeparator.setLineColor(BaseColor.BLACK);
        addSpace(document);
        document.add(new Chunk(lineSeparator));
        addSpace(document);
    }

    private void addBlank(Document document) throws DocumentException {
        LineSeparator lineSeparator = new LineSeparator();
        lineSeparator.setLineColor(BaseColor.WHITE);
        addSpace(document);
        document.add(new Chunk(lineSeparator));
        addSpace(document);
    }

    private void addSpace(Document document) throws DocumentException {
        document.add(new Paragraph(""));
    }

    private void addTextData(Document document,String title, String subtitle, Font fontTitleText, Font fontKek) throws DocumentException {
        Chunk chunkTitle = new Chunk(title, fontKek);
        Chunk chunkSub = new Chunk(subtitle,fontKek);

        Paragraph paragraph = new Paragraph(chunkTitle);
        paragraph.add(chunkSub);
        document.add(paragraph);
    }

    private void addTextData2(Document document,String title, String subtitle, Font fontTitleText, Font fontSub) throws DocumentException {
        Chunk chunkTitle = new Chunk(title, fontSub);
        Chunk chunkSub = new Chunk(subtitle,fontSub);

        Paragraph paragraph = new Paragraph(chunkTitle);
        paragraph.add(new Chunk(new VerticalPositionMark()));
        paragraph.add(chunkSub);
        document.add(paragraph);
    }

    private  void getPermis(){
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(preview_request.this, "Access granted", Toast.LENGTH_SHORT).show();
                            Log.d("method_pdf_ACCESS","Write storage access granted");
                        }
                        else if (report.isAnyPermissionPermanentlyDenied()) {
                            Toast.makeText(preview_request.this, "Access denied", Toast.LENGTH_SHORT).show();
                            Log.d("method_pdf_ACCESS","Write storage access denied");
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        Log.d("method_pdf_ACCESS","You must agree");
                    }
                })
                .withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Log.d("method_pdf_ACCESS ",error.toString());
                    }
                })
                .onSameThread()
                .check();
    }

}
