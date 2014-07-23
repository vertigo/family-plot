/*
 * Copyright 2014 Vertigo Software, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vertigo.familyplot.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.View;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.primitives.Floats;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.vertigo.familyplot.library.utils.LogUtils.LOGW;
import static com.vertigo.familyplot.library.utils.LogUtils.makeLogTag;

// Inspired by https://github.com/andersericsson/ViewTutorialPart2

public class HorizontalBarGraph extends View {
    private static final String TAG = makeLogTag(HorizontalBarGraph.class);

    private DataRange dataRange = DataRange.NULL;
    private List<Entry> datapoints;
    private Paint barPaint = new Paint();
    private Paint gridPaint = new Paint();
    private Paint axisPaint = new Paint();
    private Paint dataPaint = new Paint();
    private Paint labelPaint = new Paint();

    private boolean fromBottom = false;

    private boolean hideAxis;
    private float axisTextSize;
    private int axisColor;
    private int axisTextColor;

    private boolean hideLabels;
    private int labelTextColor;
    private float labelTextSize;

    private int barColor;
    private int barTextColor;
    private float barTextSize;
    private int barHeight;
    private int minRows;
    private boolean hideValues;
    private float maxLabelWidth;
    private int barPadding;
    private int axisPadding;
    private Rect drawRect = new Rect(0, 0, 300, 600);
    private boolean shrinkRange;
    private int sections;


    public HorizontalBarGraph(Context context) {
        this(context, null);
    };

    public HorizontalBarGraph(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    };

    public HorizontalBarGraph(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
        requestFocus();
        formatGraph();
        configureEditMode();
    }

    private void setDrawingBounds() {
        int height = getChartHeight() + getAxisHeight();
        drawRect = new Rect();
        drawRect.set(0, 0, getWidth(), height);
    }


    private void init(Context context, AttributeSet attrs, int defStyle) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HorizontalBarGraph, 0, defStyle);
        if (a != null) {
            axisColor = a.getColor(R.styleable.HorizontalBarGraph_axisColor, Color.LTGRAY);
            axisTextColor = a.getColor(R.styleable.HorizontalBarGraph_axisTextColor, Color.DKGRAY);
            barColor = a.getColor(R.styleable.HorizontalBarGraph_barColor, Color.DKGRAY);
            barTextColor = a.getColor(R.styleable.HorizontalBarGraph_barTextColor, Color.BLACK);
            labelTextColor = a.getColor(R.styleable.HorizontalBarGraph_labelTextColor, Color.BLACK);
            barHeight = a.getDimensionPixelSize(R.styleable.HorizontalBarGraph_barHeight, getResources().getDimensionPixelSize(R.dimen.bar_stroke_width));
            barPadding = a.getDimensionPixelSize(R.styleable.HorizontalBarGraph_barPadding, getResources().getDimensionPixelSize(R.dimen.gutter_sm));
            axisPadding = a.getDimensionPixelSize(R.styleable.HorizontalBarGraph_axisPadding, getResources().getDimensionPixelSize(R.dimen.gutter_sm));
            hideValues = a.getBoolean(R.styleable.HorizontalBarGraph_hideValue, false);
            hideLabels = a.getBoolean(R.styleable.HorizontalBarGraph_hideLabels, false);
            hideAxis = a.getBoolean(R.styleable.HorizontalBarGraph_hideAxis, false);
            shrinkRange = a.getBoolean(R.styleable.HorizontalBarGraph_shrinkRange, false);
            fromBottom = a.getBoolean(R.styleable.HorizontalBarGraph_stackBarsFromBottom, false);
            minRows = a.getInt(R.styleable.HorizontalBarGraph_minRows, 5);
            barTextSize = a.getDimension(R.styleable.HorizontalBarGraph_barTextSize, getResources().getDimension(R.dimen.bar_text_size));
            axisTextSize = a.getDimension(R.styleable.HorizontalBarGraph_axisTextSize, getResources().getDimension(R.dimen.bar_text_size));
            labelTextSize = a.getDimension(R.styleable.HorizontalBarGraph_labelTextSize, getResources().getDimension(R.dimen.bar_text_size));
            sections = a.getInt(R.styleable.HorizontalBarGraph_maxSectionCount, 4);
            if (sections < 1 || sections > 10){
                sections = 4;
                LOGW(TAG, "Max sections must be between 1 and 10");
            }
            a.recycle();
        }

    }

    private void formatGraph() {
        gridPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        gridPaint.setStrokeWidth(2);
        gridPaint.setColor(axisColor);
        gridPaint.setAntiAlias(true);

        axisPaint.setStyle(Paint.Style.FILL);
        axisPaint.setStrokeWidth(1);
        axisPaint.setColor(axisTextColor);
        axisPaint.setTextSize(axisTextSize);
        axisPaint.setTextAlign(Paint.Align.CENTER);
        axisPaint.setAntiAlias(true);

        labelPaint.setStyle(Paint.Style.FILL);
        labelPaint.setStrokeWidth(1);
        labelPaint.setColor(labelTextColor);
        labelPaint.setTextSize(labelTextSize);
        labelPaint.setTextAlign(Paint.Align.RIGHT);
        labelPaint.setAntiAlias(true);

        barPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        barPaint.setColor(barColor);
        barPaint.setStrokeWidth(barHeight);
        barPaint.setAntiAlias(true);
        barPaint.setShadowLayer(4, 2, 2, 0x80000000);


        dataPaint.setStyle(Paint.Style.FILL);
        dataPaint.setStrokeWidth(1);
        dataPaint.setColor(barTextColor);
        dataPaint.setTextSize(barTextSize);
        dataPaint.setTextAlign(Paint.Align.LEFT);
        dataPaint.setAntiAlias(true);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackground(canvas);
        drawBarChart(canvas);
//        requestLayout();
    }

    public void update(List<Entry> datapoints) {
        this.datapoints = datapoints;
        float[] floats = getFloats();
        dataRange = new DataRange(floats, sections, shrinkRange);
        setMaxLabelWidth(datapoints);
        setDrawingBounds();
        invalidate();
        requestLayout();
    }

    private void setMaxLabelWidth(List<Entry> datapoints) {
        Collection<String> labels = Collections2.transform(datapoints, new Function<Entry, String>() {
            @Override
            public String apply(Entry input) {
                return input.getLabel();
            }
        });

        for (String label : labels) {
            Rect bounds = getTextBounds(label, labelPaint);
            if (bounds.width() > maxLabelWidth){
                maxLabelWidth = bounds.width();
            }
        }
    }

    private float[] getFloats() {
        return Floats.toArray(Collections2.transform(datapoints, new Function<Entry, Float>() {
            @Override
            public Float apply(Entry input) {
                return input.getValue();
            }
        }));
    }

    private void drawBackground(Canvas canvas) {

        for (float x = dataRange.getMin(); x <= dataRange.getMax(); x += dataRange.getInterval()) {
            final float xPos = getXPos(x);
            String text;
            if (dataRange.getInterval() == FloatMath.floor(dataRange.getInterval())){
                text = String.format("%s", Integer.toString((int)x));
            } else {
                text = String.format("%.1f", x);
            }

            canvas.drawLine(xPos, getPaddingTop(), xPos, getChartHeight() - getAxisHeight(), gridPaint);
            drawAxisText(canvas, text, xPos);
        }
    }

    private void drawBarChart(Canvas canvas) {
        for (int i = 0; i < datapoints.size(); i++) {
            Entry datapoint = datapoints.get(i);
            float value = (float) (Math.round(10.0 * datapoint.getValue()) / 10.0);
            float yPos = getYPos(i);
            float xPosEnd = getXPos(value);
            float xPosStart = getXPos(dataRange.getMin());

            drawLabelText(canvas, datapoint.getLabel(), yPos, xPosStart);
            drawValueText(canvas, value, yPos, xPosEnd);
            drawBar(canvas, yPos, xPosEnd, xPosStart);
        }
    }

    private void drawBar(Canvas canvas, float yPos, float xPosEnd, float xPosStart) {
        Path path = new Path();
        path.moveTo(xPosStart, yPos);
        path.lineTo(xPosEnd, yPos);
        canvas.drawPath(path, barPaint);
    }


    private void drawAxisText(Canvas canvas, String text, float xPos) {
        if (hideAxis){
            return;
        }
        canvas.drawText(text, xPos, getChartHeight() - getAxisHeight() / 2 + axisPadding, axisPaint);
    }
    private void drawLabelText(Canvas canvas, String label, float yPos, float xPosStart) {
        if (hideLabels){
            return;
        }
        Rect bounds = getTextBounds(label, labelPaint);
        canvas.drawText(label, xPosStart - axisPadding, yPos + bounds.height() / 2, labelPaint);
    }

    private void drawValueText(Canvas canvas, float value, float yPos, float xPosEnd) {
        if (hideValues){
            return;
        }
        String text = String.format("%.1f", value);
        Rect bounds = getTextBounds(text, dataPaint);
        canvas.drawText(text, xPosEnd + bounds.width() / 2, yPos + bounds.height() / 2, dataPaint);
    }

    private Rect getTextBounds(String text, Paint paint) {
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return bounds;
    }

    private float getYPos(int position) {
        float height = getChartHeight();

        int yPos = position * getLineHeight() + barPadding / 2;
        if (fromBottom) {
            yPos = (int) (height - yPos); // invert it so that higher values have lower y
        }
        yPos += getPaddingTop() + getChartPadding() / 2; // offset it to adjust for padding

        return yPos;
    }

    private int getChartPadding() {
        return (int) getResources().getDimension(R.dimen.gutter_sm);
    }

    private float getXPos(float value) {
        float width = getChartWidth();
        float maxValue = dataRange.getMax();
        float minValue = dataRange.getMin();

        value -= minValue; // reduce value by min
        value = (value / (maxValue - minValue)) * width; // scale it to the view size
        value += (getPaddingLeft() + getPaddingRight()) / 2  + maxLabelWidth + getChartPadding() / 2;  // offset it to adjust for padding

        return value;
    }

    private int getAxisHeight() {
        if (hideAxis){
            return 0;
        }
        Rect textBounds = getTextBounds("Axis Text", axisPaint);
        return textBounds.height() + axisPadding;
    }

    private int getChartHeight() {
        int lineHeight = getLineHeight();
        int baseHeight = lineHeight * Math.max(datapoints.size(), minRows);
        return baseHeight + getPaddingTop() + getPaddingBottom();
    }

    private int getLineHeight() {
        return Math.max(barHeight, getTextBounds("JAN", labelPaint).height()) + barPadding;
    }

    private int getChartWidth() {
        int offset = 100;
        return (int) (getWidth() - getPaddingLeft() - getPaddingRight() - offset - Math.ceil(maxLabelWidth));
    }

    private void configureEditMode() {
        if (isInEditMode()) {
            List<Entry> datapoints = Arrays.asList(
                    new Entry("P1", 4.1f),
                    new Entry("P2", 4.5f),
                    new Entry("P3", 5.0f),
                    new Entry("P4", 4.3f),
                    new Entry("P5", 3.4f));
            update(datapoints);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //Get size requested and size mode
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);


        int width = determineWidth(widthMode, widthSize);
        int height = determineHeight(heightMode, heightSize);

        setMeasuredDimension(width, height);
    }

    private int determineHeight(int heightMode, int heightSize) {
        switch(heightMode){
            case MeasureSpec.EXACTLY:
                return heightSize;
            case MeasureSpec.AT_MOST:
                return Math.min(drawRect.height(), heightSize);
            case MeasureSpec.UNSPECIFIED:
            default:
                return drawRect.height();
        }
    }

    private int determineWidth(int widthMode, int widthSize) {
        switch(widthMode){
            case MeasureSpec.EXACTLY:
                return widthSize;
            case MeasureSpec.AT_MOST:
                return Math.min(drawRect.width() + getPaddingTop() + getPaddingBottom(), widthSize);
            case MeasureSpec.UNSPECIFIED:
            default:
                return drawRect.width() + getPaddingTop() + getPaddingBottom();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        drawRect.set(0, 0, w, h);
    }

}
