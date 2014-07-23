package com.vertigo.familyplot.library.utils;

import android.util.Log;

import com.vertigo.familyplot.library.BuildConfig;


public final class LogUtils {

    private static final String LOG_PREFIX = "familyplot:";
    private static final int LOG_PREFIX_LENGTH = LOG_PREFIX.length();
    private static final int MAX_LOG_TAG_LENGTH = 23;

    public static String makeLogTag(String str) {
        if (str.length() > MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH) {
            return LOG_PREFIX + str.substring(0, MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH - 1);
        }

        return LOG_PREFIX + str;
    }

    public static String makeLogTag(Class cls) {
        return makeLogTag(cls.getSimpleName());
    }

    public static void LOGD(final String tag, String message) {
        if (BuildConfig.DEBUG || Log.isLoggable(tag, Log.DEBUG)) {
            Log.d(tag, message);
        }
    }

    public static void LOGD(final String tag, String message, Throwable cause) {
        if (BuildConfig.DEBUG || Log.isLoggable(tag, Log.DEBUG)) {
            Log.d(tag, message, cause);
        }
    }

    public static void LOGD(final String tag, String msg, Object... args) {
        if (BuildConfig.DEBUG || Log.isLoggable(tag, Log.DEBUG)) {
            Log.d(tag, String.format(msg, args));
        }
    }

    public static void LOGD(final String tag, String msg, Throwable tr, Object... args) {
        if (BuildConfig.DEBUG || Log.isLoggable(tag, Log.DEBUG)) {
            Log.d(tag, String.format(msg, args), tr);
        }
    }

    public static void LOGV(final String tag, String message) {
        if (BuildConfig.DEBUG && Log.isLoggable(tag, Log.VERBOSE)) {
            Log.v(tag, message);
        }
    }

    public static void LOGV(final String tag, String message, Throwable cause) {
        if (BuildConfig.DEBUG && Log.isLoggable(tag, Log.VERBOSE)) {
            Log.v(tag, message, cause);
        }
    }

    public static void LOGV(final String tag, String msg, Object... args) {
        if (BuildConfig.DEBUG && Log.isLoggable(tag, Log.VERBOSE)) {
            Log.v(tag, String.format(msg, args));
        }
    }

    public static void LOGV(final String tag, String msg, Throwable tr, Object... args) {
        if (BuildConfig.DEBUG && Log.isLoggable(tag, Log.VERBOSE)) {
            Log.v(tag, String.format(msg, args), tr);
        }
    }

    public static void LOGI(final String tag, String message) {
        Log.i(tag, message);
    }

    public static void LOGI(final String tag, String message, Throwable cause) {
        Log.i(tag, message, cause);
    }

    public static void LOGI(final String tag, String msg, Object... args) {
        if (Log.isLoggable(tag, Log.INFO)) {
            Log.i(tag, String.format(msg, args));
        }
    }

    public static void LOGI(final String tag, String msg, Throwable tr, Object... args) {
        if (Log.isLoggable(tag, Log.INFO)) {
            Log.i(tag, String.format(msg, args), tr);
        }
    }

    public static void LOGW(final String tag, String message) {
        Log.w(tag, message);
    }

    public static void LOGW(final String tag, String message, Throwable cause) {
        Log.w(tag, message, cause);
    }

    public static void LOGW(final String tag, String msg, Object... args) {
        if (Log.isLoggable(tag, Log.WARN)) {
            Log.w(tag, String.format(msg, args));
        }
    }

    public static void LOGW(final String tag, String msg, Throwable tr, Object... args) {
        if (Log.isLoggable(tag, Log.WARN)) {
            Log.w(tag, String.format(msg, args), tr);
        }
    }

    public static void LOGE(final String tag, String message) {
        Log.e(tag, message);
    }

    public static void LOGE(final String tag, String message, Throwable cause) {
        Log.e(tag, message, cause);
    }

    public static void LOGE(final String tag, String msg, Object... args) {
        if (Log.isLoggable(tag, Log.ERROR)) {
            Log.e(tag, String.format(msg, args));
        }
    }

    public static void LOGE(final String tag, String msg, Throwable tr, Object... args) {
        if (Log.isLoggable(tag, Log.ERROR)) {
            Log.e(tag, String.format(msg, args), tr);
        }
    }

    private LogUtils() {
    }
}