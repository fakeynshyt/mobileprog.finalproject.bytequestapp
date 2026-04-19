package com.example.finalprojectjava.helpers;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.finalprojectjava.R;
import com.google.android.material.snackbar.Snackbar;

public class SnackBarHelperFragment {
    private static void applyStyle(
            View view,
            int bgColor,
            int textColor,
            int iconRes,
            String message
    ) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
                .setBackgroundTint(bgColor)
                .setTextColor(textColor);

        View snackView = snackbar.getView();

        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(15f);

        TextView textView = snackView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setCompoundDrawablesWithIntrinsicBounds(iconRes, 0, 0, 0);
        textView.setCompoundDrawablePadding(8);

        snackView.setBackground(drawable);

        int bottomMargin = dpToPx(snackView, 69.9f);

        ViewGroup.MarginLayoutParams params =
                (ViewGroup.MarginLayoutParams) snackView.getLayoutParams();

        params.setMargins(24, 0, 24, bottomMargin);
        snackView.setLayoutParams(params);

        snackbar.show();
    }

    public static void showErrorSnackBar(View parent, String message) {
        applyStyle(
                parent,
                Color.parseColor("#C0FFDDDD"),
                Color.parseColor("#C11515"),
                R.drawable.sb_snackbar_error,
                message
        );
    }

    public static void showSuccessSnackBar(View parent, String message) {
        applyStyle(
                parent,
                Color.parseColor("#C0C9F0C8"),
                Color.parseColor("#187A2C"),
                R.drawable.sb_snackbar_success,
                message
        );
    }

    public static void showInfoSnackBar(View parent, String message) {
        applyStyle(
                parent,
                Color.parseColor("#C0BFCFF0"),
                Color.parseColor("#133B7A"),
                R.drawable.sb_snackbar_info,
                message
        );
    }

    public static void showWarningSnackBar(View parent, String message) {
        applyStyle(
                parent,
                Color.parseColor("#C0FDF3DE"),
                Color.parseColor("#CE8616"),
                R.drawable.sb_snackbar_warning,
                message
        );
    }

    private static int dpToPx(View view, float dp) {
        float density = view.getResources().getDisplayMetrics().density;
        return (int) (dp * density);
    }
}