package com.easybill.ui.addbill;


import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

class BillRecognizer {

    public static void startCameraSource(Context context, Frame frame) {
        //Create the TextRecognizer
        final TextRecognizer textRecognizer = new TextRecognizer.Builder(context).build();
        if (!textRecognizer.isOperational()) {
            Log.w("BillRecognizer", "Detector dependencies not loaded yet");
        } else {
            SparseArray<TextBlock> text = textRecognizer.detect(frame);
        }
    }
}
