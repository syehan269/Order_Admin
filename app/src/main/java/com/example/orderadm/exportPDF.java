package com.example.orderadm;

import android.os.Environment;
import android.util.Log;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class exportPDF {
    //public boolean write(String dName, String dContent){
    public void write() {
        String dName = "test";
        Log.d("method_pdf","ACCESS");

        try{
            Log.d("method_pdf","create path");
            String path = "/sdcard"+"Test"+".pdf";
            String dpath  = Environment.getExternalStorageDirectory()+"/Test.pdf";
            File file = new File(dpath);

            Log.d("method_pdf","create file");
            if (!file.exists()){
                file.createNewFile();
            }

            Log.d("method_pdf","create doc");
            Document document = new Document();
            FileOutputStream outputStream = new FileOutputStream(file.getAbsoluteFile());
            PdfWriter.getInstance(document, outputStream);

            Log.d("method_pdf","add lines");
            document.add(new Paragraph("KEK"));
            document.add(new Paragraph("LOLOLOLOLOLOLLOLOLOLOLOLLOLO"));
            Log.d("method_pdf","close doc");
            document.close();

        } catch (IOException e) {
            Log.d("method_pdf",e.getMessage());
            e.printStackTrace();
        } catch (DocumentException e) {
            Log.d("method_pdf",e.getMessage());
        }

    }
}
