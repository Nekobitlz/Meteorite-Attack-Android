package com.nekobitlz.meteorite_attack.views.custom;// ClickShrinkEffectKt.java

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.MotionEvent;
import android.view.View;

import java.lang.ref.WeakReference;

public final class ClickShrinkEffect {

    private static final float SHRINK_VALUE = 0.93F;

    private final WeakReference weakRefView;

    public static final View applyClickShrink(View applyClickShrink) {
        new ClickShrinkEffect(applyClickShrink);
        return applyClickShrink;
    }

    private final Animator buildShrinkAnimator() {
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, new float[]{1.0F, 0.93F});
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, new float[]{1.0F, 0.93F});
        View var10000 = (View)this.weakRefView.get();
        if (var10000 != null) {
            View var3 = var10000;
            boolean var4 = false;
            boolean var5 = false;
            boolean var7 = false;
            ObjectAnimator var9 = ObjectAnimator.ofPropertyValuesHolder(var3, new PropertyValuesHolder[]{scaleX, scaleY});
            ObjectAnimator animator = var9;
            animator.setDuration(100L);
            return (Animator)animator;
        } else {
            return null;
        }
    }

    private final Animator buildGrowAnimator() {
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, new float[]{0.93F, 1.0F});
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, new float[]{0.93F, 1.0F});
        View var10000 = (View)this.weakRefView.get();
        if (var10000 != null) {
            View var3 = var10000;
            boolean var4 = false;
            boolean var5 = false;
            boolean var7 = false;
            ObjectAnimator var9 = ObjectAnimator.ofPropertyValuesHolder(var3, new PropertyValuesHolder[]{scaleX, scaleY});
            ObjectAnimator animator = var9;
            animator.setDuration(100L);
            return (Animator)animator;
        } else {
            return null;
        }
    }

    public ClickShrinkEffect(View view) {
        super();
        this.weakRefView = new WeakReference(view);
        if (!view.hasOnClickListeners()) {
            view.setOnClickListener((View.OnClickListener)null);
        }

        view.setOnTouchListener((View.OnTouchListener)(new View.OnTouchListener() {
            public final boolean onTouch(View $noName_0, MotionEvent event) {
                Animator var10000;
                switch(event.getAction()) {
                    case 0:
                        var10000 = ClickShrinkEffect.this.buildShrinkAnimator();
                        if (var10000 != null) {
                            var10000.start();
                        }
                        break;
                    case 1:
                        var10000 = ClickShrinkEffect.this.buildGrowAnimator();
                        if (var10000 != null) {
                            var10000.start();
                        }
                }

                return false;
            }
        }));
    }
}

