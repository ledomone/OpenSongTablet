package com.garethevans.church.opensongtablet;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ScrollView;
import android.widget.TextView;

class AutoScrollFunctions {

    static void getAutoScrollTimes(Context c, Preferences preferences) {
        // Set the autoscroll values
        try {
            StaticVariables.autoScrollDuration = Integer.parseInt(StaticVariables.mDuration.replaceAll("[\\D]", ""));
        } catch (Exception e) {
            StaticVariables.autoScrollDuration = -1;
        }

        try {
            StaticVariables.autoScrollDelay = Integer.parseInt(StaticVariables.mPreDelay.replaceAll("[\\D]", ""));
        } catch (Exception e) {
            StaticVariables.autoScrollDelay = 0;
        }
        StaticVariables.usingdefaults = false;
        if (StaticVariables.mDuration.isEmpty() &&
                preferences.getMyPreferenceBoolean(c,"autoscrollUseDefaultTime",false)) {
            StaticVariables.autoScrollDuration = preferences.getMyPreferenceInt(c,"autoscrollDefaultSongLength",180);
            StaticVariables.usingdefaults = true;
        }

        if (StaticVariables.mPreDelay.isEmpty() &&
                preferences.getMyPreferenceBoolean(c,"autoscrollUseDefaultTime",false)) {
            StaticVariables.autoScrollDelay = preferences.getMyPreferenceInt(c,"autoscrollDefaultSongPreDelay",10);
            StaticVariables.usingdefaults = true;
        }
    }

    static void getAutoScrollValues(Context c, Preferences preferences, ScrollView scrollpage, View main_page, View toolbar) {
        // Get the autoScrollDuration;
        if (StaticVariables.mDuration.isEmpty() &&
                preferences.getMyPreferenceBoolean(c,"autoscrollUseDefaultTime",false)) {
            StaticVariables.autoScrollDuration = preferences.getMyPreferenceInt(c,"autoscrollDefaultSongLength",180);
        } else if (StaticVariables.mDuration.isEmpty() &&
                !preferences.getMyPreferenceBoolean(c,"autoscrollUseDefaultTime",false)) {
            StaticVariables.autoScrollDuration = -1;
        } else {
            try {
                StaticVariables.autoScrollDuration = Integer.parseInt(StaticVariables.mDuration.replaceAll("[\\D]", ""));
            } catch (Exception e) {
                StaticVariables.autoScrollDuration = 0;
            }
        }

        // Get the autoScrollDelay;
        if (StaticVariables.mPreDelay.isEmpty() &&
                preferences.getMyPreferenceBoolean(c,"autoscrollUseDefaultTime",false)) {
            StaticVariables.autoScrollDelay = preferences.getMyPreferenceInt(c,"autoscrollDefaultSongPreDelay",10);
        } else if (StaticVariables.mDuration.isEmpty() &&
                !preferences.getMyPreferenceBoolean(c,"autoscrollUseDefaultTime",false)) {
            StaticVariables.autoScrollDelay = -1;
        } else {
            try {
                StaticVariables.autoScrollDelay = Integer.parseInt(StaticVariables.mPreDelay.replaceAll("[\\D]", ""));
            } catch (Exception e) {
                StaticVariables.autoScrollDelay = 0;
            }
        }

        if (StaticVariables.autoScrollDuration > -1 && StaticVariables.autoScrollDelay > -1) {
            // If it duration is less than the predelay, stop!
            if (StaticVariables.autoScrollDuration < StaticVariables.autoScrollDelay) {
                StaticVariables.isautoscrolling = false;
                return;
            } else {
                // Remove the autoScrollDelay
                StaticVariables.autoScrollDuration = StaticVariables.autoScrollDuration - StaticVariables.autoScrollDelay;
            }

            // Ok figure out the size of amount of scrolling needed
            int height;
            try {
                height = (scrollpage.getChildAt(0).getMeasuredHeight() - (main_page.getHeight() - toolbar.getHeight()));
            } catch (Exception e) {
                height = 0;
            }
            if (height >= scrollpage.getScrollY()) {
                StaticVariables.total_pixels_to_scroll = height;
            } else {
                StaticVariables.total_pixels_to_scroll = 0;
            }

            // Ok how many pixels per 500ms - autoscroll_pause_time
            FullscreenActivity.autoscroll_pixels = ((float) StaticVariables.total_pixels_to_scroll /
                    ((float) StaticVariables.autoScrollDuration * 1000 / (float) StaticVariables.autoscroll_pause_time));

            FullscreenActivity.newPosFloat=0.0f;
            StaticVariables.autoScrollDuration = StaticVariables.autoScrollDuration + StaticVariables.autoScrollDelay;
        }
    }

    static void getAudioLength(Context c, Preferences preferences) {
        MediaPlayer mediafile = new MediaPlayer();

        // Ultimately these should be stored as strings that get parsed as Uris
        StorageAccess storageAccess = new StorageAccess();
        String audiofile = StaticVariables.mLinkAudio;
        Uri uri = storageAccess.fixLocalisedUri(c, preferences, audiofile);

        if (uri!=null && storageAccess.uriExists(c,uri)) {
            try {
                mediafile.setDataSource(c,uri);
                mediafile.prepare();
                StaticVariables.audiolength = (int) (mediafile.getDuration() / 1000.0f);
                mediafile.reset();
                mediafile.release();
            } catch (Exception e) {
                StaticVariables.audiolength = -1;
                mediafile.reset();
                mediafile.release();
            }
        } else {
            StaticVariables.audiolength=-1;
            mediafile.reset();
            mediafile.release();
        }
    }

    static final Handler doautoScroll = new Handler();
    static class AutoScrollRunnable implements Runnable {
        final ScrollView sv;
        AutoScrollRunnable(ScrollView s) {
            sv = s;
        }

        @Override
        public void run() {
            final ObjectAnimator animator;
            animator = ObjectAnimator.ofInt(sv, "scrollY", (int) FullscreenActivity.newPosFloat);
            Interpolator linearInterpolator = new LinearInterpolator();
            animator.setInterpolator(linearInterpolator);
            animator.setDuration(StaticVariables.autoscroll_pause_time);
            if (!FullscreenActivity.isManualDragging) {
                sv.post(new Runnable() {
                    @Override
                    public void run() {
                        animator.start();
                    }
                });
            }
        }
    }

    static final Handler doProgressTime = new Handler();
    static class ProgressTimeRunnable implements Runnable {
        final TextView tv;
        final TextView tvt;
        final TextView tvs;
        final Context c;
        final Preferences preferences;
        ProgressTimeRunnable(Context ctx, Preferences pref, TextView t, TextView tt, TextView ts) {
            tv = t;
            tvt = tt;
            tvs = ts;
            c = ctx;
            preferences = pref;
        }

        @Override
        public void run() {
            if (StaticVariables.isautoscrolling) {
                FullscreenActivity.time_passed = System.currentTimeMillis();
                int currtimesecs = (int) ((FullscreenActivity.time_passed - FullscreenActivity.time_start) / 1000);
                String text;
                if (preferences.getMyPreferenceBoolean(c,"autoscrollLargeFontInfoBar",true)) {
                    tv.setTextSize(StaticVariables.infoBarLargeTextSize);
                    tvt.setTextSize(StaticVariables.infoBarLargeTextSize);
                    tvs.setTextSize(StaticVariables.infoBarLargeTextSize);
                } else {
                    tv.setTextSize(StaticVariables.infoBarSmallTextSize);
                    tvt.setTextSize(StaticVariables.infoBarSmallTextSize);
                    tvs.setTextSize(StaticVariables.infoBarSmallTextSize);
                }

                if (currtimesecs < StaticVariables.autoScrollDelay) {
                    // Set the time as a backwards count down
                    currtimesecs = StaticVariables.autoScrollDelay - currtimesecs;
                    text = TimeTools.timeFormatFixer(currtimesecs);
                    tv.setTextColor(0xffff0000);
                    tv.setText(text);
                } else {
                    text = TimeTools.timeFormatFixer(currtimesecs);
                    tv.setTextColor(0xffffffff);
                    tv.setText(text);
                }
            }
        }
    }
    static final Handler doautoScrollLearn = new Handler();
    static class LearnTimeRunnable implements Runnable {
        final TextView tv;
        LearnTimeRunnable(TextView t) {
            tv = t;
        }

        @Override
        public void run() {
            if (StaticVariables.learnPreDelay || StaticVariables.learnSongLength) {
                FullscreenActivity.time_passed = System.currentTimeMillis();
                int currtimesecs = (int) ((FullscreenActivity.time_passed - FullscreenActivity.time_start) / 1000);
                String text;
                text = TimeTools.timeFormatFixer(currtimesecs);
                tv.setTextColor(0xffffffff);
                tv.setText(text);
            }
        }
    }
}
