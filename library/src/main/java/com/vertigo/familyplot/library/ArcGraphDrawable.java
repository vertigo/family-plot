/*
 * Copyright 2014 Vertigo Software, Inc.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.vertigo.familyplot.library;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

public class ArcGraphDrawable extends Drawable {

    private final Paint mPaint;
    private final Paint mTrackPaint;
    private final RectF mRect;
    private int mColor;
    private int mTrackColor;
    private float mSweepLength;
    private int mStrokeWidth;

    public ArcGraphDrawable() {
        this(Color.RED, 0f);
    }

    public ArcGraphDrawable(int color, float sweepLength) {
        setSweepLength(sweepLength);
        mColor = color;
        mRect = new RectF();

        mPaint = new Paint();
        mPaint.setColor(color);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.SQUARE);
        mPaint.setStrokeWidth(mStrokeWidth);

        mTrackPaint = new Paint();
        mTrackColor = Color.DKGRAY;
        mTrackPaint.setColor(mTrackColor);
        mTrackPaint.setAntiAlias(true);
        mTrackPaint.setStyle(Paint.Style.STROKE);
        mTrackPaint.setStrokeCap(Paint.Cap.SQUARE);
        mTrackPaint.setStrokeWidth(mStrokeWidth);
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        if (mColor != color) {
            mColor = color;
            mPaint.setColor(color);
            invalidateSelf();
        }
    }

    public int getTrackColor() {
        return mTrackColor;
    }

    public void setTrackColor(int trackColor) {
        if (mTrackColor != trackColor) {
            mTrackColor = trackColor;
            mTrackPaint.setColor(trackColor);
            invalidateSelf();
        }
    }

    public float getSweepLength() {
        return mSweepLength;
    }

    public void setSweepLength(float sweepLength) {
        if (mStrokeWidth != sweepLength) {
            if (sweepLength > 1f) {
                mSweepLength = 1;
            } else if (sweepLength < 0) {
                mSweepLength = 0;
            } else {
                mSweepLength = sweepLength;
            }

            invalidateSelf();
        }
    }

    public int getStrokeWidth() {
        return mStrokeWidth;
    }

    public void setStrokeWidth(int strokeWidth) {
        if (mStrokeWidth != strokeWidth) {
            mStrokeWidth = strokeWidth;
            mPaint.setStrokeWidth(mStrokeWidth);
            mTrackPaint.setStrokeWidth(mStrokeWidth);
            invalidateSelf();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (mSweepLength > 0f && mStrokeWidth > 0) {
            int count = canvas.save();
            Rect bounds = getBounds();
            Rect innerBounds = new Rect(
                    mStrokeWidth - 1,
                    mStrokeWidth - 1,
                    bounds.width() - mStrokeWidth + 1,
                    bounds.height() - mStrokeWidth + 1);
            mRect.set(innerBounds);

            if (mSweepLength != 1f) {
                canvas.drawArc(mRect, 0, 360, false, mTrackPaint);
            }

            canvas.drawArc(mRect, -90, mSweepLength * 360, false, mPaint);
            canvas.restoreToCount(count);
        }
    }

    @Override
    public void setAlpha(int alpha) {
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
    }

    @Override
    public int getOpacity() {
        return 0;
    }
}
