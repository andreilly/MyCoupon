package com.illica.mycoupon.Other;

import android.os.Build;
import android.widget.EditText;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Utils {

    public static boolean isEmptyEditText(EditText etText) {
        if (etText.getText().toString().trim().length() > 0)
            return false;

        return true;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String convertInITAFormat(LocalDate localdate){
        DateTimeFormatter italianDateFormatter = DateTimeFormatter.ofPattern("dd/MM/uuuu");
        return localdate.format(italianDateFormatter);
    }
}
