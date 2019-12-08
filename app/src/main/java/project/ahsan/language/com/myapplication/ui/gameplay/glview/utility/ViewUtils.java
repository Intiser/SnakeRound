package project.ahsan.language.com.myapplication.ui.gameplay.glview.utility;

import android.content.Context;

public class ViewUtils {

    public static double getPixelsFromDP(Context context, int x) {
        double y = x * 1.0;
        double pixel = context.getResources().getDisplayMetrics().density * y;
        return pixel;
    }

}
