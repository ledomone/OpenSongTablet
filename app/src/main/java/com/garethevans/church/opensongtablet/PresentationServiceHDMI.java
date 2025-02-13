package com.garethevans.church.opensongtablet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Presentation;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

// All of the classes after initialisation are the same for the PresentationService and PresentationServiceHDMI files
// The both call functions in the PresentationCommon file
// Both files are needed as they initialise and communicate with the display differently, but after that the stuff is identical.

class PresentationServiceHDMI extends Presentation
        implements MediaPlayer.OnVideoSizeChangedListener,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener, SurfaceHolder.Callback {


    PresentationServiceHDMI(Context context, Display display, ProcessSong pS, Activity act) {
        super(context, display);
        c = context;
        myscreen = display;
        processSong = pS;
        activity = act;
    }

    // Define the variables and views
    private static Display myscreen;
    @SuppressLint("StaticFieldLeak")
    private static RelativeLayout pageHolder, projectedPage_RelativeLayout;
    @SuppressLint("StaticFieldLeak")
    private static LinearLayout projected_LinearLayout, presentermode_bottombit;
    @SuppressLint("StaticFieldLeak")
    private static ImageView projected_ImageView, projected_Logo, projected_BackgroundImage;
    @SuppressLint("StaticFieldLeak")
    private static TextView songinfo_TextView, presentermode_title, presentermode_author, presentermode_copyright, presentermode_alert;
    @SuppressLint("StaticFieldLeak")
    private static LinearLayout bottom_infobar, col1_1, col1_2, col2_2, col1_3, col2_3, col3_3;
    @SuppressLint("StaticFieldLeak")
    private static SurfaceView projected_SurfaceView;
    private static SurfaceHolder projected_SurfaceHolder;
    private static StorageAccess storageAccess;
    private static Preferences preferences;
    private static ProcessSong processSong;
    private static PresentationCommon presentationCommon;
    @SuppressLint("StaticFieldLeak")
    private static Context c;
    @SuppressLint("StaticFieldLeak")
    private static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.cast_screen);

            storageAccess = new StorageAccess();
            preferences = new Preferences();
            presentationCommon = new PresentationCommon();

            getDefaultColors();

            // Identify the views
            identifyViews();

            c = projectedPage_RelativeLayout.getContext();

            // Set the default background image
            setDefaultBackgroundImage();

            // Based on the mode we are in, hide the appropriate stuff at the bottom of the page
            matchPresentationToMode();

            // Change margins
            changeMargins();

            // Decide on screen sizes
            getScreenSizes();

            // Set up the logo
            setUpLogo();

            // Prepare the display after 2 secs (a chance for stuff to be measured and show the logo
            Handler h = new Handler();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!StaticVariables.whichMode.equals("Presentation")) {
                        normalStartUp();
                    } else {
                        // Switch to the user background and logo
                        presenterStartUp();
                    }
                }
            }, 2000);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // The screen and layout defaults
    private void identifyViews() {
        pageHolder = findViewById(R.id.pageHolder);
        projectedPage_RelativeLayout = findViewById(R.id.projectedPage_RelativeLayout);
        projected_LinearLayout = findViewById(R.id.projected_LinearLayout);
        projected_ImageView = findViewById(R.id.projected_ImageView);
        projected_BackgroundImage = findViewById(R.id.projected_BackgroundImage);
        projected_SurfaceView = findViewById(R.id.projected_SurfaceView);
        projected_SurfaceHolder = projected_SurfaceView.getHolder();
        projected_SurfaceHolder.addCallback(PresentationServiceHDMI.this);
        projected_Logo = findViewById(R.id.projected_Logo);
        songinfo_TextView = findViewById(R.id.songinfo_TextView);
        presentermode_bottombit = findViewById(R.id.presentermode_bottombit);
        presentermode_title = findViewById(R.id.presentermode_title);
        presentermode_author = findViewById(R.id.presentermode_author);
        presentermode_copyright = findViewById(R.id.presentermode_copyright);
        presentermode_alert = findViewById(R.id.presentermode_alert);
        bottom_infobar = findViewById(R.id.bottom_infobar);
        col1_1 = findViewById(R.id.col1_1);
        col1_2 = findViewById(R.id.col1_2);
        col2_2 = findViewById(R.id.col2_2);
        col1_3 = findViewById(R.id.col1_3);
        col2_3 = findViewById(R.id.col2_3);
        col3_3 = findViewById(R.id.col3_3);
    }
    private static void getScreenSizes() {
        presentationCommon.getScreenSizes(myscreen,bottom_infobar,projectedPage_RelativeLayout);
    }
    private void setDefaultBackgroundImage() {
        presentationCommon.setDefaultBackgroundImage(c);
    }
    private static void matchPresentationToMode() {
        if (presentationCommon.matchPresentationToMode(songinfo_TextView,presentermode_bottombit,projected_SurfaceView,projected_BackgroundImage,projected_ImageView)) {
            fixBackground();
        }
    }
    static void changeMargins() {
        presentationCommon.changeMargins(c,preferences,songinfo_TextView,projectedPage_RelativeLayout,StaticVariables.cast_presoInfoColor);
    }
    static void fixBackground() {
        presentationCommon.fixBackground(c,preferences,storageAccess,projected_BackgroundImage,projected_SurfaceHolder,projected_SurfaceView);
        updateAlpha();
    }
    private static void getDefaultColors() {
        presentationCommon.getDefaultColors(c,preferences);
    }
    private static void updateAlpha() {
        presentationCommon.updateAlpha(c,preferences,projected_BackgroundImage,projected_SurfaceView);
    }
    private void normalStartUp() {
        // Animate out the default logo
        getDefaultColors();
        presentationCommon.normalStartUp(c,preferences,projected_Logo);
        doUpdate();
    }
    private void presenterStartUp() {
        getDefaultColors();
        // Set up the text styles and fonts for the bottom info bar
        presenterThemeSetUp();
        presentationCommon.presenterStartUp(c,preferences,storageAccess,projected_BackgroundImage,projected_SurfaceHolder,projected_SurfaceView);
    }
    private static void presenterThemeSetUp() {
        getDefaultColors();
        // Set the text at the bottom of the page to match the presentation text colour
        presentationCommon.presenterThemeSetUp(c,preferences,presentermode_bottombit, presentermode_title, presentermode_author,
                presentermode_copyright,presentermode_alert);
    }
    static void updateFonts() {
        getDefaultColors();
        presenterThemeSetUp(); // Sets the bottom info bar for presentation
        doUpdate(); // Updates the page
    }


    // Video
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        getScreenSizes();
        presentationCommon.prepareMediaPlayer(c, preferences, projected_SurfaceHolder, myscreen, bottom_infobar, projectedPage_RelativeLayout);
        StaticVariables.cast_mediaPlayer.setOnPreparedListener(this);
        StaticVariables.cast_mediaPlayer.setOnCompletionListener(this);
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
    }
    private static void reloadVideo() {
        presentationCommon.reloadVideo(c,preferences,projected_SurfaceHolder,projected_SurfaceView);
    }
    @Override
    public void onPrepared(MediaPlayer mp) {
        presentationCommon.mediaPlayerIsPrepared(projected_SurfaceView);
    }
    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mp != null) {
            if (mp.isPlaying()) {
                mp.stop();
            }
            mp.reset();
        }
        try {
            reloadVideo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Update the screen content
    static void doUpdate() {
        presentationCommon.doUpdate(activity, c,preferences,storageAccess,processSong,myscreen,songinfo_TextView,presentermode_bottombit,projected_SurfaceView,
                projected_BackgroundImage, pageHolder,projected_Logo,projected_ImageView,projected_LinearLayout,bottom_infobar,projectedPage_RelativeLayout,
                presentermode_title, presentermode_author, presentermode_copyright, col1_1, col1_2, col2_2, col1_3, col2_3, col3_3);
    }
    static void updateAlert(boolean show) {
        presentationCommon.updateAlert(c, preferences, myscreen, bottom_infobar,projectedPage_RelativeLayout,show, presentermode_alert);
    }
    static void setUpLogo() {
        presentationCommon.setUpLogo(c,preferences,storageAccess,projected_Logo,StaticVariables.cast_availableWidth_1col,StaticVariables.cast_availableScreenHeight);
    }
    static void showLogo() {
        presentationCommon.showLogo(c,preferences,projected_ImageView,projected_LinearLayout,pageHolder,bottom_infobar,projected_Logo);
    }
    static void hideLogo() {
        presentationCommon.hideLogo(c,preferences,projected_ImageView,projected_LinearLayout,projected_Logo,bottom_infobar);
    }
    static void blankUnblankDisplay(boolean unblank) {
        presentationCommon.blankUnblankDisplay(c,preferences,pageHolder,unblank);
    }

}
