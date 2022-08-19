package com.illica.mycoupon.Other;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.widget.EditText;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Utils {

    /**
     * Method used to check if EditText is empty
     * @param etText EditText to check
     * @return true if a EditText is empty, false otherwise
     */
    public static boolean isEmptyEditText(EditText etText) {
        if (etText.getText().toString().trim().length() > 0)
            return false;

        return true;
    }

    /**
     * Convert localdate in string with ITA format
     * @param localdate localdate
     * @return a new string
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String convertInITAFormat(LocalDate localdate){
        DateTimeFormatter italianDateFormatter = DateTimeFormatter.ofPattern("dd/MM/uuuu");
        return localdate.format(italianDateFormatter);
    }

    /**
     * Add border to bitmap
     * @param bmp bitmap
     * @param borderSize how many pixels for the border
     * @return a new bitmap
     */
    public static Bitmap addWhiteBorder(Bitmap bmp, int borderSize) {
        Bitmap bmpWithBorder = Bitmap.createBitmap(bmp.getWidth() + borderSize * 2, bmp.getHeight() + borderSize * 2, bmp.getConfig());
        Canvas canvas = new Canvas(bmpWithBorder);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bmp, borderSize, borderSize, null);
        return bmpWithBorder;
    }
}
