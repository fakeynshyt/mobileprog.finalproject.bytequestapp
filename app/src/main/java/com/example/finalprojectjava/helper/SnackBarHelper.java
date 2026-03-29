package com.example.finalprojectjava.helper;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.finalprojectjava.R;
import com.google.android.material.snackbar.Snackbar;

public class SnackBarHelper {
    public static void showErrorSnackBar(View parent, String message) {
        Snackbar snackbar = Snackbar.make(parent, message, Snackbar.LENGTH_SHORT)
                .setBackgroundTint(Color.parseColor("#C0FFDDDD"))
                .setTextColor(Color.parseColor("#C11515"));


        View view = snackbar.getView();

        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(15f);

        TextView textView = view.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.sb_snackbar_error,
                0,
                0,
                0
        );

        textView.setCompoundDrawablePadding(8);

        view.setBackground(drawable);

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        params.setMargins(24, 0, 24, 24);
        view.setLayoutParams(params);

        snackbar.show();
    }

    public static void showSuccessSnackBar(View parent, String message) {
        Snackbar snackbar = Snackbar.make(parent, message, Snackbar.LENGTH_SHORT)
                .setBackgroundTint(Color.parseColor("#C0C9F0C8"))
                .setTextColor(Color.parseColor("#187A2C"));


        View view = snackbar.getView();

        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(15f);

        TextView textView = view.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.sb_snackbar_success,
                0,
                0,
                0
        );

        textView.setCompoundDrawablePadding(8);

        view.setBackground(drawable);

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        params.setMargins(24, 0, 24, 24);
        view.setLayoutParams(params);

        snackbar.show();
    }

    public static void showInfoSnackBar(View parent, String message) {
        Snackbar snackbar = Snackbar.make(parent, message, Snackbar.LENGTH_SHORT)
                .setBackgroundTint(Color.parseColor("#C0BFCFF0"))
                .setTextColor(Color.parseColor("#133B7A"));


        View view = snackbar.getView();

        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(15f);

        TextView textView = view.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.sb_snackbar_info,
                0,
                0,
                0
        );

        textView.setCompoundDrawablePadding(8);

        view.setBackground(drawable);

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        params.setMargins(24, 0, 24, 24);
        view.setLayoutParams(params);

        snackbar.show();
    }

    public static void showWarningSnackBar(View parent, String message) {
        Snackbar snackbar = Snackbar.make(parent, message, Snackbar.LENGTH_SHORT)
                .setBackgroundTint(Color.parseColor("#C0FDF3DE"))
                .setTextColor(Color.parseColor("#CE8616"));


        View view = snackbar.getView();

        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(15f);

        TextView textView = view.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.sb_snackbar_warning,
                0,
                0,
                0
        );

        textView.setCompoundDrawablePadding(8);

        view.setBackground(drawable);

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        params.setMargins(24, 0, 24, 24);
        view.setLayoutParams(params);

        snackbar.show();
    }
}
