package common.zhang.customer;

/**
 * Created by zzw on 2017/3/15.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;

import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.HorizontalScrollView;
import android.widget.Scroller;

import java.lang.reflect.Field;


public class MHorizontalScrollView
        extends HorizontalScrollView {
    private static final String TAG = "CustomScrollView";
    private int Scroll_height = 0;
    private GestureDetector mGestureDetector;// = new GestureDetector(paramContext, new YScrollDetector());
    protected Field scrollView_mScroller;
    private int view_height = 0;

    public MHorizontalScrollView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);

        mGestureDetector= new GestureDetector(paramContext, new YScrollDetector());
        setFadingEdgeLength(0);
    }

    public static Field getDeclaredField(Object paramObject, String paramString) {
        paramObject = paramObject.getClass();
        for (; ; ) {
            if (paramObject == Object.class) {
                return null;
            }
            try {
                Field localField = ((Class) paramObject).getDeclaredField(paramString);
                localField.setAccessible(true);
                return localField;
            } catch (Exception localException) {
                paramObject = ((Class) paramObject).getSuperclass();
            }
        }
    }

    private void stopAnim() {
        try {
            if (this.scrollView_mScroller == null) {
                this.scrollView_mScroller = getDeclaredField(this, "mScroller");
            }
            Object localObject = this.scrollView_mScroller.get(this);
            if (localObject == null) {
                return;
            }
            localObject.getClass().getMethod("abortAnimation", new Class[0]).invoke(localObject, new Object[0]);
            return;
        } catch (Exception localException) {
        }
    }

    protected int computeVerticalScrollRange() {
        this.Scroll_height = super.computeVerticalScrollRange();
        return this.Scroll_height;
    }

    public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
        if (paramMotionEvent.getAction() == 0) {
            stopAnim();
        }
        boolean bool1 = super.onInterceptTouchEvent(paramMotionEvent);
        boolean bool2 = this.mGestureDetector.onTouchEvent(paramMotionEvent);
        return (bool1) && (bool2);
    }

    protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
        if (paramBoolean) {
            this.view_height = (paramInt4 - paramInt2);
        }
    }

    protected void onScrollChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        int i = 0;
        if (this.Scroll_height - this.view_height == paramInt2) {
            i = 1;
        }
        if ((paramInt2 == 0) || (i != 0)) {
        }
        try {
            if (this.scrollView_mScroller == null) {
                this.scrollView_mScroller = getDeclaredField(this, "mScroller");
            }
            Object localObject = this.scrollView_mScroller.get(this);
            if (localObject == null) {
                return;
            }
            if (!(localObject instanceof Scroller)) {
                return;
            }
            ((Scroller) localObject).abortAnimation();
        } catch (Exception localException) {
            for (; ; ) {
                Log.e("CustomScrollView", localException.getMessage());
            }
        }
        super.onScrollChanged(paramInt1, paramInt2, paramInt3, paramInt4);
        return;
    }

    public void requestChildFocus(View paramView1, View paramView2) {
        if ((paramView2 != null) && ((paramView2 instanceof WebView))) {
            return;
        }
        super.requestChildFocus(paramView1, paramView2);
    }

    class YScrollDetector
            extends GestureDetector.SimpleOnGestureListener {
        YScrollDetector() {
        }

        public boolean onScroll(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2) {
            return false;
        }
    }
}

