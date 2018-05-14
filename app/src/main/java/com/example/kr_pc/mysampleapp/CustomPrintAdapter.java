package com.example.kr_pc.mysampleapp;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by KR-PC on 13-03-2018.
 */
@RequiresApi(api = Build.VERSION_CODES.KITKAT)
class CustomPrintAdapter extends PrintDocumentAdapter {

    Context mContext;
    public CustomPrintAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public void onFinish() {
        ((FormActivity)mContext).finish();
        Log.e("onFinish","onFinish");
    }

    @Override
    public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, WriteResultCallback callback) {
        InputStream input = null;
        OutputStream output = null;

        Log.e("onWrite","onWrite");


        try {

            input = new FileInputStream("/sdcard/my_file.pdf");
            output = new FileOutputStream(destination.getFileDescriptor());

            byte[] buf = new byte[1024];
            int bytesRead;

            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }

            callback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});

        } catch (FileNotFoundException ee) {
            //Catch exception
        } catch (Exception e) {
            //Catch exception
        } finally {
            try {
                input.close();
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle extras) {
        Log.e("onLayout","onLayout");
        if (cancellationSignal.isCanceled()) {
            callback.onLayoutCancelled();
            return;
        }


        PrintDocumentInfo pdi = new PrintDocumentInfo.Builder("Name of file").setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT).build();

        callback.onLayoutFinished(pdi, true);
    }
}
