package com.example.tictactoe;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class ContactDialogUtils {

    private static final int[] COLORS = {
            Color.parseColor("#7B1FA2"),  // Purple
            Color.parseColor("#E65100"),  // Deep Orange
            Color.parseColor("#C2185B"),  // Pink
            Color.parseColor("#1565C0"),  // Blue
            Color.parseColor("#00796B"),  // Teal
            Color.parseColor("#F57F17"),  // Amber
            Color.parseColor("#5E35B1"),  // Deep Purple
            Color.parseColor("#00838F"),  // Cyan
            Color.parseColor("#D32F2F"),  // Red
            Color.parseColor("#689F38")   // Light Green
    };

    private static ValueAnimator colorAnimator;
    private static AnimatorSet scaleAnimatorSet;
    private static int currentColorIndex = 0;

    public static void setUpContactDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.contact_dev_dialog, null);

        LottieAnimationView callAnim = dialogView.findViewById(R.id.animCall);
        LottieAnimationView emailAnim = dialogView.findViewById(R.id.animEmail);

        builder.setView(dialogView);

        TextView tvContactMe = dialogView.findViewById(R.id.tvContactMe);

        AlertDialog dialog = builder.create();
        callAnim.playAnimation();
        emailAnim.playAnimation();

        dialog.show();
        callAnim.setOnClickListener(V->{
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:03360654125"));
            context.startActivity(callIntent);
        });
        emailAnim.setOnClickListener(V->{
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:mehranhaider1786@gmail.com"));
            context.startActivity(emailIntent);
        });
        startColorAndSizeAnimation(tvContactMe);
    }

    private static void startColorAndSizeAnimation(TextView textView) {
        currentColorIndex = 0;
        animateNextCycle(textView);
    }

    private static void animateNextCycle(TextView textView) {
        int currentColor = COLORS[currentColorIndex];
        int nextColor = COLORS[(currentColorIndex + 1) % COLORS.length];

        colorAnimator = ObjectAnimator.ofInt(textView, "textColor", currentColor, nextColor);
        colorAnimator.setDuration(1500);
        colorAnimator.setEvaluator(new android.animation.ArgbEvaluator());

        ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(textView, "scaleX", 1f, 1.3f);
        ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(textView, "scaleY", 1f, 1.3f);
        scaleUpX.setDuration(750);
        scaleUpY.setDuration(750);

        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(textView, "scaleX", 1.3f, 1f);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(textView, "scaleY", 1.3f, 1f);
        scaleDownX.setDuration(750);
        scaleDownY.setDuration(750);

        scaleAnimatorSet = new AnimatorSet();
        scaleAnimatorSet.play(scaleUpX).with(scaleUpY);
        scaleAnimatorSet.play(scaleDownX).with(scaleDownY).after(scaleUpX);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(colorAnimator).with(scaleAnimatorSet);

        animatorSet.addListener(new android.animation.AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                currentColorIndex = (currentColorIndex + 1) % COLORS.length;
                animateNextCycle(textView);
            }
        });

        animatorSet.start();
    }

    public static void stopAnimations() {
        if (colorAnimator != null && colorAnimator.isRunning()) {
            colorAnimator.cancel();
        }
        if (scaleAnimatorSet != null && scaleAnimatorSet.isRunning()) {
            scaleAnimatorSet.cancel();
        }
    }
}