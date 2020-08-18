package com.example.coctailmixer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;

public class ResizeAnimation extends Animation {
    int targetHeight;
    View view;
    int startHeight;

    public ResizeAnimation(View viewC, int targetHeightC, int startHeightC) {
        view = viewC;
        targetHeight = targetHeightC;
        startHeight = startHeightC;
        System.out.println(viewC.getId());
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        int newHeight;
        if (targetHeight > startHeight) {
            newHeight = (int) (startHeight + targetHeight * interpolatedTime);
        }
        else
        {
            newHeight = (int) (targetHeight + startHeight * (1 - interpolatedTime));
        }
        //to support decent animation, change new heigt as Nico S. recommended in comments
        //int newHeight = (int) (startHeight+(targetHeight - startHeight) * interpolatedTime);
        view.getLayoutParams().height = newHeight;
        view.requestLayout();
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}