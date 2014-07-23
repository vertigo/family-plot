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

import android.util.FloatMath;

class DataRange {

    private final int sections;
    private final boolean shrinkRange;
    private float interval;
    private float min;
    private float max;
    private static final float[] intervals = new float[]{ .1f, .2f, .5f };

    public static final DataRange NULL = new DataRange(new float[0], 5, false);

    DataRange(float[] points, int sections, boolean shrinkRange) {
        this.sections = sections;
        this.shrinkRange = shrinkRange;
        setMax(points);
        setMin(points);
        setInterval();
    }


    public float getInterval() {

        return interval;
    }

    public float getMin() {
        return getAdjustedMin(interval);
    }

    public float getMax() {
        return getMin() + (sections * interval);
    }

    private void setInterval() {
        int index = 0;
        int multiplier = 1;

        do{
            float adjustedMax = getAdjustedMax(intervals[index] * multiplier);
            float adjustedMin = getAdjustedMin(intervals[index] * multiplier);
            float diff = adjustedMax - adjustedMin;

            float range = intervals[index] * multiplier * sections;

            if (range >= diff){
                this.interval = intervals[index] * multiplier;
                return;
            } else {
                index++;
                if (index + 1 > intervals.length) {
                    index = 0;
                    multiplier *= 10;
                }
            }
            if (index==Integer.MAX_VALUE){
                throw new IllegalArgumentException("Calculated interval is too large");
            }

        } while(true);
    }

    private float getAdjustedMax(float interval) {
        float adjustedMax;
        if (interval < 1){
            adjustedMax = (float) (Math.ceil(10.0 * max) / 10.0);
        } else {
            adjustedMax = FloatMath.ceil(max);
        }
        return adjustedMax;
    }

    private float getAdjustedMin(float interval) {
        float value = FloatMath.floor(min / interval) * interval;
        return min > value || shrinkRange == false ? value : value - interval;
    }

    private void setMax(float[] array) {
        if (array == null || array.length == 0){
            return;
        }
        float max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        this.max = (float) (Math.ceil(10.0 * max) / 10.0);
    }

    private void setMin(float[] array) {
        if (array == null || array.length == 0){
            return;
        }
        float min = shrinkRange ? array[0] : 0;
        for (int i = 1; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
            }
        }
        this.min = (float) (Math.floor(10.0 * min) / 10.0);
    }

}
