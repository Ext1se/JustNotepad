package com.ext1se.dialog.color_dialog;

import android.content.Context;
import androidx.annotation.ArrayRes;

public class ColorHelper {

    public static ColorItem getColor(Context context, @ArrayRes int idColorTheme){
        int[] colors = context.getResources().getIntArray(idColorTheme);
        return new ColorItem(idColorTheme, colors[0], colors[1], colors[2]);
    }
}
