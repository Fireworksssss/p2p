/*
 * Copyright (C) 2016-2018 Yaroslav Pronin <proninyaroslav@mail.ru>
 *
 * This file is part of LibreTorrent.
 *
 * LibreTorrent is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LibreTorrent is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LibreTorrent.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.example.qiangge.torrtest.core.utils;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.qiangge.torrtest.R;
import com.example.qiangge.torrtest.core.BencodeFileItem;
import com.frostwire.jlibtorrent.FileStorage;

import org.acra.ACRA;
import org.acra.ReportField;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * General utils.
 */

public class Utils
{
    public static final String INFINITY_SYMBOL = "\u221e";
    public static final String MAGNET_PREFIX = "magnet";
    public static final String HTTP_PREFIX = "http";
    public static final String HTTPS_PREFIX = "https";
    public static final String UDP_PREFIX = "udp";
    public static final String INFOHASH_PREFIX = "magnet:?xt=urn:btih:";
    public static final String FILE_PREFIX = "file";
    public static final String CONTENT_PREFIX = "content";
    public static final String TRACKER_URL_PATTERN =
            "^(https?|udp)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
    public static final String HASH_PATTERN = "\\b[0-9a-fA-F]{5,40}\\b";
    public static final int MAX_HTTP_REDIRECTION = 10;
    public static final String MIME_TORRENT = "application/x-bittorrent";

    /*
     * Colored status bar in KitKat.
     */


    /*
     * Colorize the progress bar in the accent color (for pre-Lollipop).
     */


    /*
     * Returns the list of BencodeFileItem objects, extracted from FileStorage.
     * The order of addition in the list corresponds to the order of indexes in jlibtorrent.FileStorage
     */

    public static ArrayList<BencodeFileItem> getFileList(FileStorage storage)
    {
        ArrayList<BencodeFileItem> files = new ArrayList<>();
        for (int i = 0; i < storage.numFiles(); i++) {
            BencodeFileItem file = new BencodeFileItem(storage.filePath(i), i, storage.fileSize(i));
            files.add(file);
        }

        return files;
    }

    public static void setBackground(View v, Drawable d)
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
            v.setBackgroundDrawable(d);
        else
            v.setBackground(d);
    }

    /*
     * Returns the checking result or throws an exception.
     */


    /*
     * Returns the link as "(http[s]|udp)://[www.]name.domain/...".
     */

    public static String normalizeURL(String url)
    {
        if (!url.startsWith(HTTP_PREFIX) && !url.startsWith(HTTPS_PREFIX) && !url.startsWith(UDP_PREFIX))
            return HTTP_PREFIX + "://" + url;
        else
            return url;
    }

    /*
     * Returns the link as "magnet:?xt=urn:btih:hash".
     */

    public static String normalizeMagnetHash(String hash)
    {
        return INFOHASH_PREFIX + hash;
    }


    /*
     * Tablets (from 7"), notebooks, TVs
     */

    /*
     * Returns true if link has the form "http[s][udp]://[www.]name.domain/...".
     *
     * Returns false if the link is not valid.
     */

    public static boolean isValidTrackerUrl(String url)
    {
        if (url == null || TextUtils.isEmpty(url))
            return false;

        Pattern pattern = Pattern.compile(TRACKER_URL_PATTERN);
        Matcher matcher = pattern.matcher(url.trim());

        return matcher.matches();
    }

    public static boolean isHash(String hash) {
        if (hash == null || TextUtils.isEmpty(hash))
            return false;

        Pattern pattern = Pattern.compile(HASH_PATTERN);
        Matcher matcher = pattern.matcher(hash.trim());

        return matcher.matches();
    }

    /*
     * Return system text line separator (in android it '\n').
     */

    public static String getLineSeparator()
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
            return System.lineSeparator();
        else
            return System.getProperty("line.separator");
    }

    /*
     * Returns the first item from clipboard.
     */

    @Nullable
    public static String getClipboard(Context context)
    {
        ClipboardManager clipboard = (ClipboardManager)context.getSystemService(Activity.CLIPBOARD_SERVICE);
        if (clipboard == null)
            return null;

        if (!clipboard.hasPrimaryClip())
            return null;

        ClipData clip = clipboard.getPrimaryClip();
        if (clip == null || clip.getItemCount() == 0)
            return null;

        CharSequence text = clip.getItemAt(0).getText();
        if (text == null)
            return null;

        return text.toString();
    }

    public static void reportError(Throwable error, String comment)
    {
        if (error == null)
            return;

        if (comment != null)
            ACRA.getErrorReporter().putCustomData(ReportField.USER_COMMENT.toString(), comment);

        ACRA.getErrorReporter().handleSilentException(error);
    }

    public static int dpToPx(Context context, float dp)
    {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp, context.getResources().getDisplayMetrics());
    }

    public static int getDefaultBatteryLowLevel()
    {
        return Resources.getSystem().getInteger(
                Resources.getSystem().getIdentifier("config_lowBatteryWarningLevel", "integer", "android"));
    }

    public static float getBatteryLevel(Context context)
    {
        Intent batteryIntent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        /* Error checking that probably isn't needed but I added just in case */
        if (level == -1 || scale == -1)
            return 50.0f;

        return ((float) level / (float) scale) * 100.0f;
    }

    public static boolean isBatteryCharging(Context context)
    {
        Intent batteryIntent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int status = batteryIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);

        return status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;
    }

    public static boolean isBatteryLow(Context context)
    {
        return Utils.getBatteryLevel(context) <= Utils.getDefaultBatteryLowLevel();
    }

    public static boolean isBatteryBelowThreshold(Context context, int threshold)
    {
        return Utils.getBatteryLevel(context) <= threshold;
    }





    public static boolean checkStoragePermission(Context context)
    {
        return ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isWifiEnabled(Context context)
    {
        WifiManager manager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        return manager != null && manager.isWifiEnabled();
    }


    /*
     * Workaround for start service in Android 8+ if app no started.
     * We have a window of time to get around to calling startForeground() before we get ANR,
     * if work is longer than a millisecond but less than a few seconds.
     */


    public static void setTextViewStyle(Context context, TextView textView, int resId)
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            textView.setTextAppearance(context, resId);
        else
            textView.setTextAppearance(resId);
    }

    public static String getAppVersionName(Context context)
    {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);

            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            /* Ignore */
        }

        return null;
    }

    /*
     * Without additional information (e.g -DEBUG)
     */

    public static String getAppVersionNumber(String versionName)
    {
        if (versionName == null)
            return null;

        int index = versionName.indexOf("-");
        if (index >= 0)
            versionName = versionName.substring(0, index);

        return versionName;
    }

    /*
     * Return version components in these format: [major, minor, revision]
     */

    public static int[] getVersionComponents(String versionName)
    {
        int[] version = new int[3];
        if (versionName == null)
            return version;

        /* Discard additional information */
        versionName = getAppVersionNumber(versionName);

        String[] components = versionName.split("\\.");
        if (components.length < 2)
            return version;

        try {
            version[0] = Integer.parseInt(components[0]);
            version[1] = Integer.parseInt(components[1]);
            if (components.length >= 3)
                version[2] = Integer.parseInt(components[2]);

        } catch (NumberFormatException e) {
            /* Ignore */
        }

        return version;
    }
}
