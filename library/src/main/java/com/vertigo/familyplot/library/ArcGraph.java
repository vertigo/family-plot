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

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.NumberFormat;

public class ArcGraph extends RelativeLayout {

    private ImageView mInnerImage;
    private FrameLayout mGraphFrame;
    private TextView mPercentageText;
    private ArcGraphDrawable mArcGraph;
    private int mArcColor;
    private int mTrackColor;
    private float mPercentage;
    private int mStrokeWidth;

    public ArcGraph(Context context) {
        super(context);
        init(context, null, 0);
    }

    public ArcGraph(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public ArcGraph(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    public int getArcColor() {
        return mArcColor;
    }

    public void setArcColor(int arcColor) {
        mArcColor = arcColor;

        if (mArcGraph != null) {
            mArcGraph.setColor(arcColor);
        }
    }

    public int getTrackColor() {
        return mTrackColor;
    }

    public void setTrackColor(int trackColor) {
        mTrackColor = trackColor;

        if (mArcGraph != null) {
            mArcGraph.setTrackColor(mTrackColor);
        }
    }

    public float getPercentage() {
        return mPercentage;
    }

    public void setPercentage(float percentage) {
        mPercentage = percentage;

        if (mArcGraph != null) {
            mArcGraph.setSweepLength(mPercentage);
        }

        if (mPercentageText != null) {
            mPercentageText.setText(NumberFormat.getPercentInstance().format(mPercentage));
        }
    }

    public int getStrokeWidth() {
        return mStrokeWidth;
    }

    public void setStrokeWidth(int strokeWidth) {
        mStrokeWidth = strokeWidth;

        if (mArcGraph != null) {
            mArcGraph.setStrokeWidth(mStrokeWidth);
        }
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        View root = inflate(context, R.layout.arc_graph, this);

        mInnerImage = (ImageView) root.findViewById(R.id.inner_image);
        mGraphFrame = (FrameLayout) root.findViewById(R.id.graph_frame);
        mPercentageText = (TextView) root.findViewById(R.id.percentage);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ArcGraph, 0, defStyle);
        if (!isInEditMode() && a != null) {
            mArcColor = a.getColor(R.styleable.ArcGraph_arcColor, Color.DKGRAY);
            mTrackColor = a.getColor(R.styleable.ArcGraph_trackColor, Color.LTGRAY);
            mStrokeWidth = a.getDimensionPixelSize(R.styleable.ArcGraph_strokeWidth, getResources().getDimensionPixelSize(R.dimen.arc_graph_stroke_width));
            mInnerImage.setImageDrawable(a.getDrawable(R.styleable.ArcGraph_innerImage));
            mPercentageText.setTextColor(a.getColor(R.styleable.ArcGraph_android_textColor, Color.BLACK));
            a.recycle();
        }

        mPercentageText.setText(NumberFormat.getPercentInstance().format(mPercentage));
        mArcGraph = new ArcGraphDrawable(mArcColor, mPercentage);
        mArcGraph.setStrokeWidth(mStrokeWidth);
        mArcGraph.setTrackColor(mTrackColor);
        mGraphFrame.setBackground(mArcGraph);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }
}
