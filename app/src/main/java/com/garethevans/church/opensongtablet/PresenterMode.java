package com.garethevans.church.opensongtablet;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.mediarouter.app.MediaRouteActionProvider;
import androidx.mediarouter.media.MediaControlIntent;
import androidx.mediarouter.media.MediaRouteSelector;
import androidx.mediarouter.media.MediaRouter;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.cast.CastDevice;
import com.google.android.gms.cast.CastMediaControlIntent;
import com.google.android.gms.cast.CastRemoteDisplayLocalService;
import com.google.android.gms.common.api.Status;
import com.peak.salut.Callbacks.SalutCallback;
import com.peak.salut.Callbacks.SalutDataCallback;
import com.peak.salut.Salut;
import com.peak.salut.SalutDataReceiver;
import com.peak.salut.SalutServiceData;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import lib.folderpicker.FolderPicker;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)

public class PresenterMode extends AppCompatActivity implements MenuHandlers.MyInterface,
        SongMenuListeners.MyInterface, PopUpChooseFolderFragment.MyInterface,
        PopUpSongDetailsFragment.MyInterface, PopUpEditSongFragment.MyInterface,
        SetActions.MyInterface, PopUpPresentationOrderFragment.MyInterface,
        PopUpSetViewNew.MyInterface, IndexSongs.MyInterface, SearchView.OnQueryTextListener,
        OptionMenuListeners.MyInterface, PopUpFullSearchFragment.MyInterface,
        PopUpListSetsFragment.MyInterface,
        PopUpLongSongPressFragment.MyInterface,
        PopUpFileChooseFragment.MyInterface, PopUpBackupPromptFragment.MyInterface,
        PopUpSongFolderRenameFragment.MyInterface, PopUpSongCreateFragment.MyInterface,
        PopUpSongRenameFragment.MyInterface, PopUpImportExportOSBFragment.MyInterface,
        PopUpImportExternalFile.MyInterface, PopUpCustomSlideFragment.MyInterface,
        PopUpFindNewSongsFragment.MyInterface,
        PopUpThemeChooserFragment.MyInterface, PopUpGroupedPageButtonsFragment.MyInterface,
        PopUpQuickLaunchSetup.MyInterface, PopUpPagesFragment.MyInterface,
        PopUpExtraInfoFragment.MyInterface, PopUpPageButtonsFragment.MyInterface,
        PopUpScalingFragment.MyInterface, PopUpFontsFragment.MyInterface,
        PopUpChordsFragment.MyInterface, PopUpCustomChordsFragment.MyInterface,
        PopUpTransposeFragment.MyInterface, PopUpEditStickyFragment.MyInterface,
        PopUpPadFragment.MyInterface, PopUpAutoscrollFragment.MyInterface,
        PopUpMetronomeFragment.MyInterface, PopUpStickyFragment.MyInterface,
        PopUpLinks.MyInterface, PopUpAreYouSureFragment.MyInterface,
        SongMenuAdapter.MyInterface, BatteryMonitor.MyInterface, SalutDataCallback,
        PopUpMenuSettingsFragment.MyInterface, PopUpAlertFragment.MyInterface,
        PopUpLayoutFragment.MyInterface, DownloadTask.MyInterface,
        PopUpExportFragment.MyInterface, PopUpActionBarInfoFragment.MyInterface,
        PopUpCreateDrawingFragment.MyInterface,
        PopUpPDFToTextFragment.MyInterface, PopUpRandomSongFragment.MyInterface,
        PopUpCCLIFragment.MyInterface,
        PopUpBibleXMLFragment.MyInterface, PopUpShowMidiMessageFragment.MyInterface {

    private DialogFragment newFragment;

    // Helper classes
    private SetActions setActions;
    private ExportPreparer exportPreparer;
    private StorageAccess storageAccess;
    private IndexSongs indexSongs;
    private Preferences preferences;
    private SongXML songXML;
    private ChordProConvert chordProConvert;
    private OnSongConvert onSongConvert;
    private UsrConvert usrConvert;
    private TextSongConvert textSongConvert;
    private SQLiteHelper sqLiteHelper;
    private SQLite sqLite;  // The song details from SQLite.  Used for menu and searches.
    private ProcessSong processSong;
    private ProfileActions profileActions;

    // MIDI
    private Midi midi;

    // Casting
    private MediaRouter mMediaRouter;
    private MediaRouteSelector mMediaRouteSelector;
    private final MyMediaRouterCallback mMediaRouterCallback = new MyMediaRouterCallback();
    private CastDevice mSelectedDevice;
    private PresentationServiceHDMI hdmi;

    // The toolbar and menu
    private Toolbar ab_toolbar;
    private static ActionBar ab;
    private RelativeLayout songandauthor;
    private TextView digitalclock;
    private TextView songtitle_ab;
    private TextView songkey_ab;
    private TextView songcapo_ab;
    private TextView songauthor_ab;
    private TextView batterycharge;
    private ImageView batteryimage;
    private RelativeLayout batteryholder;

    // AsyncTasks
    private AsyncTask<Object, Void, String> preparesongmenu_async;
    private AsyncTask<Object, Void, String> prepareoptionmenu_async;
    private AsyncTask<Object, Void, String> autoslideshowtask;
    private AsyncTask<Object, Void, String> sharesong_async;
    private AsyncTask<Object, Void, String> shareset_async;
    private AsyncTask<Object, Void, String> load_customreusable;
    private AsyncTask<Object, Void, String> add_slidetoset;
    private AsyncTask<Object, Void, String> open_drawers;
    private AsyncTask<Object, Void, String> close_drawers;
    private AsyncTask<Object, Void, String> resize_drawers;
    private AsyncTask<Object, Void, String> do_moveinset;
    private AsyncTask<Object, Void, String> shareactivitylog_async;
    private AsyncTask<Object, Void, String> check_storage;
    private AsyncTask<String, Integer, String> do_download;
    private LoadSong loadsong_async;

    // The views
    private LinearLayout mLayout;
    private LinearLayout pres_details;
    private LinearLayout presenter_song_buttonsListView;
    private LinearLayout presenter_set_buttonsListView;
    private LinearLayout loopandtimeLinearLayout;
    private TextView presenter_songtitle;
    private TextView presenter_author;
    private TextView presenter_copyright;
    private CheckBox presenter_order_text;
    private CheckBox loopCheckBox;
    private Button presenter_order_button;
    private FloatingActionButton set_view_fab;
    private FloatingActionButton startstopSlideShow;
    private EditText presenter_lyrics;
    private EditText timeEditText;
    private ImageView presenter_lyrics_image;
    private ScrollView presenter_songbuttons;
    private ScrollView preso_action_buttons_scroll;
    private ScrollView presenter_setbuttons;

    // Quick nav buttons
    private FloatingActionButton nav_prevsong;
    private FloatingActionButton nav_nextsong;
    private FloatingActionButton nav_prevsection;
    private FloatingActionButton nav_nextsection;
    private boolean autoproject = false;
    private boolean pedalsenabled = true;

    // The buttons
    private TextView presenter_project_group;
    private TextView presenter_logo_group;
    private TextView presenter_blank_group;
    private TextView presenter_alert_group;
    private TextView presenter_audio_group;
    private TextView presenter_dB_group;
    private TextView presenter_slide_group;
    private TextView presenter_scripture_group;
    private TextView presenter_display_group;

    // The song and option menu stuff
    private DrawerLayout mDrawerLayout;
    private LinearLayout songmenu;
    private LinearLayout optionmenu;
    private TextView menuFolder_TextView;
    private TextView menuCount_TextView;
    private ListView song_list_view;
    private boolean firstrun_option = true;
    private boolean firstrun_song = true;
    private boolean newsongloaded = false;

    // The media player
    public static MediaPlayer mp;
    public static String mpTitle = "";

    // Variables used by the popups
    static String whatBackgroundLoaded;

    // General variables
    private String[] imagelocs;

    // Which Actions buttons are selected
    private boolean projectButton_isSelected = false;
    private boolean blankButton_isSelected = false;
    static boolean logoButton_isSelected = false;
    static String alert_on = "N";

    // Auto slideshow
    private boolean isplayingautoslideshow = false;
    private boolean autoslideloop = false;
    private int autoslidetime = 0;

    // Network discovery / connections
    private static final String TAG = "StageMode";
    private SalutMessage myMessage;
    private SalutMessage mySongMessage;
    private SalutMessage mySectionMessage;

    // Battery
    private BroadcastReceiver br;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("d", "Welcome to Presentation Mode");
        StaticVariables.activity = PresenterMode.this;
        FullscreenActivity.mContext = PresenterMode.this;
        FullscreenActivity.appRunning = true;

        // Initialise the helpers
        setActions = new SetActions();
        exportPreparer = new ExportPreparer();
        storageAccess = new StorageAccess();
        indexSongs = new IndexSongs();
        preferences = new Preferences();
        songXML = new SongXML();
        chordProConvert = new ChordProConvert();
        onSongConvert = new OnSongConvert();
        usrConvert = new UsrConvert();
        textSongConvert = new TextSongConvert();
        sqLiteHelper = new SQLiteHelper(PresenterMode.this);
        processSong = new ProcessSong();
        profileActions = new ProfileActions();

        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        StaticVariables.myToastMessage = getString(R.string.search_index_start);
                        ShowToast.showToast(PresenterMode.this);
                    }
                });
                indexSongs.fullIndex(PresenterMode.this,preferences,storageAccess,sqLiteHelper,songXML,
                        chordProConvert,onSongConvert,textSongConvert,usrConvert);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        StaticVariables.myToastMessage = getString(R.string.search_index_end);
                        ShowToast.showToast(PresenterMode.this);

                        // Now instruct the song menu to be built again.
                        prepareSongMenu();
                    }
                });
            }
        }).start();

        // Get the language
        FixLocale.fixLocale(PresenterMode.this,preferences);

        checkStorage();

        mp = new MediaPlayer();

        // Load the layout and set the title
        setContentView(R.layout.presenter_mode);

        // In order to quickly start, load the minimum variables we need
        loadStartUpVariables();

        // Set the fullscreen window flags
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                setWindowFlags();
                setWindowFlagsAdvanced();
            }
        });

        // Battery monitor
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        try {
            br = new BatteryMonitor();
            PresenterMode.this.registerReceiver(br, filter);
        } catch (Exception e) {
            Log.d("PresenterMode", "Didn't register battery");
        }


        // Setup the CastContext
        mMediaRouter = MediaRouter.getInstance(getApplicationContext());
        mMediaRouteSelector = new MediaRouteSelector.Builder()
                .addControlCategory(CastMediaControlIntent.categoryForCast("4E2B0891"))
                .addControlCategory(MediaControlIntent.CATEGORY_LIVE_VIDEO)
                .build();

        // Since this mode has just been opened, force an update to the cast screen
        StaticVariables.forcecastupdate = true;

        // Set up the toolbar and views
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ab_toolbar = findViewById(R.id.mytoolbar); // Attaching the layout to the toolbar object
                setSupportActionBar(ab_toolbar);                     // Setting toolbar as the ActionBar with setSupportActionBar() call
                ab = getSupportActionBar();
                if (ab != null) {
                    ab.setDisplayShowHomeEnabled(false); // show or hide the default home button
                    ab.setDisplayHomeAsUpEnabled(false);
                    ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
                    ab.setDisplayShowTitleEnabled(false);
                }

                // Identify the views
                initialiseTheViews();
                screenClickListeners();

                // Make the drawers match half the width of the screen
                resizeDrawers();

                // Set up the page buttons
                setupSetButtons();

                // Set up the menus
                //prepareSongMenu();
                prepareOptionMenu();

                // Set up the song buttons
                setupSongButtons();

                // Set up the Salut service
                getBluetoothName();
                startRegistration();

                // Initialise the ab info
                adjustABInfo();

                // Click on the first item in the set
                if (presenter_set_buttonsListView.getChildCount() > 0) {
                    presenter_set_buttonsListView.getChildAt(0).performClick();

                } else {
                    // Load the song
                    loadSong();
                }

                // Deal with any intents from external files/intents
                dealWithIntent();
            }
        });

        // Check if we need to remind the user to backup their songs
        checkBackupState();
    }

    // Handlers for main page on/off/etc. and window flags
    @Override
    public void onStart() {
        super.onStart();
        FullscreenActivity.appRunning = true;
        StaticVariables.activity = PresenterMode.this;
        mMediaRouter.addCallback(mMediaRouteSelector, mMediaRouterCallback,
                MediaRouter.CALLBACK_FLAG_REQUEST_DISCOVERY);
        // Fix the page flags
        windowFlags();
    }
    @Override
    protected void onStop() {
        super.onStop();
        try {
            FullscreenActivity.appRunning = false;
            mMediaRouter.removeCallback(mMediaRouterCallback);
        } catch (Exception e) {
            Log.d("d", "Problem removing mediaroutercallback");
        }

        if (br!=null) {
            try {
                PresenterMode.this.unregisterReceiver(br);
            } catch (Exception e2) {
                Log.d("PresenterMode", "Battery receiver not registerd, so no need to unregister");
            }
        }
    }
    @Override
    protected void onResume() {
        FullscreenActivity.appRunning = true;
        StaticVariables.activity = PresenterMode.this;
        resizeDrawers();
        // Fix the page flags
        windowFlags();
        // Be sure to call the super class.
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Battery monitor
        if (br!=null) {
            try {
                PresenterMode.this.unregisterReceiver(br);
            } catch (Exception e) {
                Log.d("d","Error closing battery monitor");
            }
        }
        tryCancelAsyncTasks();

        if (FullscreenActivity.network!=null && FullscreenActivity.network.isRunningAsHost) {
            try {
                FullscreenActivity.network.stopNetworkService(false);
            } catch (Exception e) {
                Log.d("d","Error closing network service");
            }
        } else if (FullscreenActivity.network!=null) {
            try {
                FullscreenActivity.network.unregisterClient(false);
            } catch (Exception e) {
                Log.d("d","Error closing network service");
            }
        }

        //Second screen
        try {
            CastRemoteDisplayLocalService.stopService();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (hdmi!=null) {
                hdmi.dismiss();
            }
        } catch (Exception e) {
            // Ooops
            e.printStackTrace();
        }
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Get the language
        FixLocale.fixLocale(PresenterMode.this,preferences);

        FullscreenActivity.orientationchanged = FullscreenActivity.mScreenOrientation != newConfig.orientation;
        if (FullscreenActivity.orientationchanged) {
            if (newFragment != null && newFragment.getDialog() != null) {
                PopUpSizeAndAlpha.decoratePopUp(PresenterMode.this, newFragment.getDialog(), preferences);
            }
            // Now, reset the orientation.
            FullscreenActivity.orientationchanged = false;

            // Get the current orientation
            FullscreenActivity.mScreenOrientation = getResources().getConfiguration().orientation;

            //invalidateOptionsMenu();
            closeMyDrawers("both");
            resizeDrawers();
        }
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            windowFlags();
        }
    }
    public void windowFlags() {
        setWindowFlags();
        setWindowFlagsAdvanced();
    }
    private void setWindowFlags() {
        try {
            View v = getWindow().getDecorView();
            v.setOnSystemUiVisibilityChangeListener(null);
            v.setOnFocusChangeListener(null);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void setWindowFlagsAdvanced() {
        try {
            View v = getWindow().getDecorView();
            v.setOnSystemUiVisibilityChangeListener(null);
            v.setOnFocusChangeListener(null);

            v.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LOW_PROFILE);

            v.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void tryCancelAsyncTasks() {
        doCancelAsyncTask(loadsong_async);
        doCancelAsyncTask(loadsong_async);
        doCancelAsyncTask(preparesongmenu_async);
        doCancelAsyncTask(prepareoptionmenu_async);
        doCancelAsyncTask(sharesong_async);
        doCancelAsyncTask(shareset_async);
        doCancelAsyncTask(shareactivitylog_async);
        doCancelAsyncTask(load_customreusable);
        doCancelAsyncTask(open_drawers);
        doCancelAsyncTask(close_drawers);
        doCancelAsyncTask(resize_drawers);
        doCancelAsyncTask(do_moveinset);
        doCancelAsyncTask(add_slidetoset);
        doCancelAsyncTask(autoslideshowtask);
        doCancelAsyncTask(do_download);
        doCancelAsyncTask(check_storage);

    }
    private void doCancelAsyncTask(AsyncTask ast) {
        if (ast != null) {
            try {
                ast.cancel(true);
            } catch (Exception e) {
                // OOps
            }
        }
    }
    @Override
    protected void onNewIntent (Intent intent) {
        super.onNewIntent(intent);
        dealWithIntent();
    }
    private void dealWithIntent() {
        try {
            switch (FullscreenActivity.whattodo) {
                case "importfile_customreusable_scripture":
                    // Receiving scripture text
                    FullscreenActivity.whattodo = "customreusable_scripture";
                    openFragment();
                    break;
                case "importfile_processimportosb":
                    // Receiving an OpenSongApp backup file
                    FullscreenActivity.whattodo = "processimportosb";
                    openFragment();
                    break;
                case "importfile_doimport":
                    // Receiving another file
                    FullscreenActivity.whattodo = "doimport";
                    openFragment();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Load the variables we need
    private void loadStartUpVariables() {

        StaticVariables.mDisplayTheme = preferences.getMyPreferenceString(PresenterMode.this,"appTheme","dark");

        // Locale
        try {
            StaticVariables.locale = new Locale(preferences.getMyPreferenceString(PresenterMode.this, "locale", "en"));
            Locale.setDefault(StaticVariables.locale);
            Configuration config = new Configuration();
            config.setLocale(StaticVariables.locale);
            this.getResources().updateConfiguration(config, this.getResources().getDisplayMetrics());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Song location
        loadFileLocation();
    }

    @Override
    public void adjustABInfo() {
        // Change the visibilities
        if (preferences.getMyPreferenceBoolean(PresenterMode.this,"batteryDialOn",true)) {
            batteryimage.setVisibility(View.VISIBLE);
        } else {
            batteryimage.setVisibility(View.INVISIBLE);
        }
        if (preferences.getMyPreferenceBoolean(PresenterMode.this,"batteryTextOn",true)) {
            batterycharge.setVisibility(View.VISIBLE);
        } else {
            batterycharge.setVisibility(View.GONE);
        }
        if (preferences.getMyPreferenceBoolean(PresenterMode.this,"clockOn",true)) {
            digitalclock.setVisibility(View.VISIBLE);
        } else {
            digitalclock.setVisibility(View.GONE);
        }

        // Set the text sizes
        batterycharge.setTextSize(preferences.getMyPreferenceFloat(PresenterMode.this, "batteryTextSize",9.0f));
        digitalclock.setTextSize(preferences.getMyPreferenceFloat(PresenterMode.this,"clockTextSize",9.0f));
        songtitle_ab.setTextSize(preferences.getMyPreferenceFloat(PresenterMode.this,"songTitleSize",13.0f));
        songcapo_ab.setTextSize(preferences.getMyPreferenceFloat(PresenterMode.this,"songTitleSize",13.0f));
        songauthor_ab.setTextSize(preferences.getMyPreferenceFloat(PresenterMode.this,"songAuthorSize",11.0f));
        songkey_ab.setTextSize(preferences.getMyPreferenceFloat(PresenterMode.this,"songTitleSize",13.0f));

        // Set the time format
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df;
        if (preferences.getMyPreferenceBoolean(PresenterMode.this,"clock24hFormat",true)) {
            df = new SimpleDateFormat("HH:mm", StaticVariables.locale);
        } else {
            df = new SimpleDateFormat("h:mm", StaticVariables.locale);
        }
        String formattedTime = df.format(c.getTime());
        digitalclock.setText(formattedTime);
    }

    @Override
    // The navigation drawers
    public void prepareSongMenu() {
        try {
            if (song_list_view==null) {
                song_list_view = findViewById(R.id.song_list_view);
            }
            doCancelAsyncTask(preparesongmenu_async);
            song_list_view.setFastScrollEnabled(false);
            song_list_view.setScrollingCacheEnabled(false);
            preparesongmenu_async = new PrepareSongMenu();
            preparesongmenu_async.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkBackupState() {
        // Check for the number of times the app has run without the user backing up his songs
        // If this is 10 (or more) show the backup prompt window.
        int runssincebackup = preferences.getMyPreferenceInt(PresenterMode.this, "runssincebackup", 0) + 1;

        // Save the new value
        preferences.setMyPreferenceInt(PresenterMode.this, "runssincebackup", runssincebackup);
        if (runssincebackup >= 10) {
            FullscreenActivity.whattodo = "promptbackup";
            openFragment();
        }
    }

    public void loadSong() {
        if (!FullscreenActivity.alreadyloading) {
            FullscreenActivity.alreadyloading = true;
            // Get the song indexes
            //listSongFiles.getCurrentSongIndex();

            // Don't do this for a blacklisted filetype (application, video, audio)
            Uri uri = storageAccess.getUriForItem(PresenterMode.this, preferences, "Songs", StaticVariables.whichSongFolder,
                    StaticVariables.songfilename);
            if (!storageAccess.checkFileExtensionValid(uri) && !storageAccess.determineFileTypeByExtension()) {
                StaticVariables.myToastMessage = getResources().getString(R.string.file_type_unknown);
                ShowToast.showToast(PresenterMode.this);
            } else {
                // Declare we have loaded a new song (for the ccli log).
                // This stops us reporting projecting every section
                newsongloaded = true;

                // Send WiFiP2P intent
                if (FullscreenActivity.network != null && FullscreenActivity.network.isRunningAsHost) {
                    try {
                        sendSongLocationToConnected();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                doCancelAsyncTask(loadsong_async);
                loadsong_async = new LoadSong();
                try {
                    loadsong_async.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } catch (Exception e) {
                    // Error loading the song
                }
            }
        }
    }
    private void findSongInFolders() {
        //scroll to the song in the song menu
        try {
            song_list_view.setSelection(FullscreenActivity.currentSongIndex);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void songShortClick(String clickedfile, String clickedfolder, int i) {
        // Close both drawers
        closeMyDrawers("both");

        // Save our preferences
        saveFileLocation(clickedfile,clickedfolder);

        // Load the song
        loadSong();

        FullscreenActivity.currentSongIndex = i;

        // Scroll to this song in the song menu
        song_list_view.smoothScrollToPosition(i);

        // Initialise the previous and next songs
        findSongInFolders();
    }

    @Override
    public void openSongLongClickAction(String clickedfile, String clickedfolder,int i) {
        // Set the values
        FullscreenActivity.whattodo = "songlongpress";
        StaticVariables.songfilename = clickedfile;
        StaticVariables.whichSongFolder = clickedfolder;
        // Short click the song as well!
        songShortClick(clickedfile,clickedfolder,i);
        openFragment();
    }

    private void loadFileLocation() {
        StaticVariables.songfilename = preferences.getMyPreferenceString(PresenterMode.this,"songfilename","Welcome to OpenSongApp");
        StaticVariables.whichSongFolder = preferences.getMyPreferenceString(PresenterMode.this, "whichSongFolder", getString(R.string.mainfoldername));
    }
    private void saveFileLocation(String loc_name, String loc_folder) {
        StaticVariables.songfilename = loc_name;
        StaticVariables.whichSongFolder = loc_folder;
        preferences.setMyPreferenceString(PresenterMode.this, "songfilename", loc_name);
        preferences.setMyPreferenceString(PresenterMode.this, "whichSongFolder", loc_folder);


    }

    @Override
    public void songLongClick() {
        // Rebuild the set list as we've just added a song
        setActions.prepareSetList(PresenterMode.this,preferences);
        prepareOptionMenu();
        fixSet();
        closeMyDrawers("song");
    }
    @Override
    public void prepareOptionMenu() {
        doCancelAsyncTask(prepareoptionmenu_async);
        prepareoptionmenu_async = new PrepareOptionMenu();
        try {
            prepareoptionmenu_async.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadPDFPagePreview() {
        Bitmap bmp = processSong.createPDFPage(PresenterMode.this, preferences, storageAccess, 800, 800, "Y");

        presenter_lyrics_image.setVisibility(View.VISIBLE);
        presenter_lyrics.setVisibility(View.GONE);

        if (bmp != null) {
            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(bmp.getWidth(), bmp.getHeight());
            presenter_lyrics_image.setLayoutParams(llp);
            // Set the image to the view
            presenter_lyrics_image.setBackgroundColor(0xffffffff);
            presenter_lyrics_image.setImageBitmap(bmp);

        } else {
            // Set the image to the unhappy android
            Drawable myDrawable = getResources().getDrawable(R.drawable.unhappy_android);
            presenter_lyrics_image.setImageDrawable(myDrawable);

            // Set an intent to try and open the pdf with an appropriate application
            Intent target = new Intent(Intent.ACTION_VIEW);
            // Run an intent to try to show the pdf externally
            target.setDataAndType(StaticVariables.uriToLoad, "application/pdf");
            target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            callIntent("openpdf", target);
        }
        if (autoproject || preferences.getMyPreferenceBoolean(PresenterMode.this,"presoAutoUpdateProjector",true)) {
            autoproject = false;
            presenter_project_group.performClick();
        }
    }
    public void resizeDrawers() {
        doCancelAsyncTask(resize_drawers);
        resize_drawers = new ResizeDrawers();
        try {
            resize_drawers.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @SuppressLint("StaticFieldLeak")
    private class ResizeDrawers extends AsyncTask<Object, Void, String> {
        int width;

        @Override
        protected String doInBackground(Object... o) {
            try {
                width = preferences.getMyPreferenceInt(PresenterMode.this,"menuSize",250);
                float density = getResources().getDisplayMetrics().density;
                width = Math.round((float) width * density);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        boolean cancelled = false;

        @Override
        protected void onCancelled() {
            cancelled = true;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                if (!cancelled) {
                    songmenu.setLayoutParams(DrawerTweaks.resizeMenu(songmenu, width));
                    optionmenu.setLayoutParams(DrawerTweaks.resizeMenu(optionmenu, width));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void openMyDrawers(String which) {
        doCancelAsyncTask(open_drawers);
        open_drawers = new OpenMyDrawers(which);
        try {
            open_drawers.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @SuppressLint("StaticFieldLeak")
    private class OpenMyDrawers extends AsyncTask<Object, Void, String> {

        final String which;

        OpenMyDrawers(String w) {
            which = w;
        }

        @Override
        protected String doInBackground(Object... obj) {
            return null;
        }

        boolean cancelled = false;

        @Override
        protected void onCancelled() {
            cancelled = true;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                if (!cancelled) {
                    DrawerTweaks.openMyDrawers(mDrawerLayout, songmenu, optionmenu, which);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void closeMyDrawers(String which) {
        doCancelAsyncTask(close_drawers);
        close_drawers = new CloseMyDrawers(which);
        try {
            close_drawers.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @SuppressLint("StaticFieldLeak")
    private class CloseMyDrawers extends AsyncTask<Object, Void, String> {

        final String which;

        CloseMyDrawers(String w) {
            which = w;
        }

        @Override
        protected String doInBackground(Object... obj) {
            return null;
        }

        boolean cancelled = false;

        @Override
        protected void onCancelled() {
            cancelled = true;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                if (!cancelled) {
                    DrawerTweaks.closeMyDrawers(mDrawerLayout, songmenu, optionmenu, which);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // The overflow menu and actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.presenter_actions, menu);

        // Setup the menu item for connecting to cast devices
        // Setup the menu item for connecting to cast devices
        MenuItem mediaRouteMenuItem = menu.findItem(R.id.media_route_menu_item);
        View mr = menu.findItem(R.id.media_route_menu_item).getActionView();
        if (mr!=null) {
            mr.setFocusable(false);
            mr.setFocusableInTouchMode(false);
        }
        MediaRouteActionProvider mediaRouteActionProvider =
                (MediaRouteActionProvider) MenuItemCompat.getActionProvider(mediaRouteMenuItem);
        if (mMediaRouteSelector != null) {
            mediaRouteActionProvider.setRouteSelector(mMediaRouteSelector);
        }

        // Force overflow icon to show, even if hardware key is present
        MenuHandlers.forceOverFlow(PresenterMode.this, ab, menu);

        // Set up battery monitor
        setUpBatteryMonitor();

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        MenuHandlers.actOnClicks(PresenterMode.this, preferences,item.getItemId());
        return super.onOptionsItemSelected(item);
    }
    public void setUpBatteryMonitor() {
        // Get clock
        try {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df;
            if (preferences.getMyPreferenceBoolean(PresenterMode.this,"clock24hFormat",true)) {
                df = new SimpleDateFormat("HH:mm", StaticVariables.locale);
            } else {
                df = new SimpleDateFormat("h:mm", StaticVariables.locale);
            }
            String formattedTime = df.format(c.getTime());
            if (preferences.getMyPreferenceBoolean(PresenterMode.this,"clockOn",true)) {
                digitalclock.setVisibility(View.VISIBLE);
            } else {
                digitalclock.setVisibility(View.GONE);
            }
            digitalclock.setTextSize(preferences.getMyPreferenceFloat(PresenterMode.this,"clockTextSize",9.0f));
            digitalclock.setText(formattedTime);

            // Get battery
            int i = (int) (BatteryMonitor.getBatteryStatus(PresenterMode.this) * 100.0f);
            String charge = i + "%";
            if (preferences.getMyPreferenceBoolean(PresenterMode.this,"batteryTextOn",true)) {
                batterycharge.setVisibility(View.VISIBLE);
            } else {
                batterycharge.setVisibility(View.GONE);
            }
            batterycharge.setTextSize(preferences.getMyPreferenceFloat(PresenterMode.this, "batteryTextSize",9.0f));
            batterycharge.setText(charge);
            int abh = ab.getHeight();
            StaticVariables.ab_height = abh;
            if (preferences.getMyPreferenceBoolean(PresenterMode.this,"batteryDialOn",true)) {
                batteryimage.setVisibility(View.VISIBLE);
            } else {
                batteryimage.setVisibility(View.INVISIBLE);
            }
            if (ab != null && abh > 0) {
                BitmapDrawable bmp = BatteryMonitor.batteryImage(PresenterMode.this, preferences, i, abh);
                batteryimage.setImageDrawable(bmp);
            }

            // Ask the app to check again in 60s
            Handler batterycheck = new Handler();
            batterycheck.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setUpBatteryMonitor();
                }
            }, 60000);
        } catch (Exception e) {
            // Ooops
        }
    }
    @Override
    public void refreshActionBar() {
        invalidateOptionsMenu();
    }
    @Override
    public void showActionBar() {
        // Do nothing as we don't allow this in Presentation Mode
    }
    @Override
    public void hideActionBar() {
        // Do nothing as we don't allow this in Presentation Mode
    }

    private void checkStorage() {
        if (check_storage!=null) {
            check_storage.cancel(true);
        }
        check_storage = new CheckStorage();
        try {
            check_storage.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @SuppressLint("StaticFieldLeak")
    private class CheckStorage extends AsyncTask<Object, Void, String> {

        @Override
        protected String doInBackground(Object... objects) {
            try {
                if (ActivityCompat.checkSelfPermission(PresenterMode.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED) {
                    finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    // Prepare the stuff we need
    private void initialiseTheViews() {

        // The main views
        mLayout = findViewById(R.id.pagepresentermode);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        pres_details = findViewById(R.id.pres_details);
        presenter_songtitle = findViewById(R.id.presenter_songtitle);
        presenter_author = findViewById(R.id.presenter_author);
        presenter_copyright = findViewById(R.id.presenter_copyright);
        presenter_order_text = findViewById(R.id.presenter_order_text);
        presenter_order_button = findViewById(R.id.presenter_order_button);
        set_view_fab = findViewById(R.id.set_view_fab);
        TextView presenter_set = findViewById(R.id.presenter_set);
        presenter_set_buttonsListView = findViewById(R.id.presenter_set_buttonsListView);
        presenter_lyrics = findViewById(R.id.presenter_lyrics);
        presenter_lyrics_image = findViewById(R.id.presenter_lyrics_image);
        loopandtimeLinearLayout = findViewById(R.id.loopandtimeLinearLayout);
        loopCheckBox = findViewById(R.id.loopCheckBox);
        timeEditText = findViewById(R.id.timeEditText);
        startstopSlideShow = findViewById(R.id.startstopSlideShow);
        presenter_songbuttons = findViewById(R.id.presenter_songbuttons);
        preso_action_buttons_scroll = findViewById(R.id.preso_action_buttons_scroll);
        presenter_setbuttons = findViewById(R.id.presenter_setbuttons);
        presenter_song_buttonsListView = findViewById(R.id.presenter_song_buttonsListView);
        SwitchCompat autoProject = findViewById(R.id.autoProject);

        // Quick nav buttons
        nav_prevsong = findViewById(R.id.nav_prevsong);
        nav_nextsong = findViewById(R.id.nav_nextsong);
        nav_prevsection = findViewById(R.id.nav_prevsection);
        nav_nextsection = findViewById(R.id.nav_nextsection);
        enabledisableButton(nav_prevsong, false);
        enabledisableButton(nav_nextsong, false);
        enabledisableButton(nav_prevsection, false);
        enabledisableButton(nav_nextsection, false);

        // The buttons
        presenter_project_group = findViewById(R.id.presenter_project_group);
        presenter_logo_group = findViewById(R.id.presenter_logo_group);
        presenter_blank_group = findViewById(R.id.presenter_blank_group);
        presenter_alert_group = findViewById(R.id.presenter_alert_group);
        presenter_audio_group = findViewById(R.id.presenter_audio_group);
        presenter_dB_group = findViewById(R.id.presenter_dB_group);
        presenter_slide_group = findViewById(R.id.presenter_slide_group);
        presenter_scripture_group = findViewById(R.id.presenter_scripture_group);
        presenter_display_group = findViewById(R.id.presenter_display_group);

        // The toolbar
        songandauthor = findViewById(R.id.songandauthor);
        digitalclock = findViewById(R.id.digitalclock);
        songtitle_ab = findViewById(R.id.songtitle_ab);
        songkey_ab = findViewById(R.id.songkey_ab);
        songcapo_ab = findViewById(R.id.songcapo_ab);
        songauthor_ab = findViewById(R.id.songauthor_ab);
        songtitle_ab.setText(getResources().getString(R.string.presentermode));
        songkey_ab.setText("");
        songcapo_ab.setText("");
        songauthor_ab.setText("");
        batterycharge = findViewById(R.id.batterycharge);
        batteryimage = findViewById(R.id.batteryimage);
        batteryholder = findViewById(R.id.batteryholder);

        // The song menu
        songmenu = findViewById(R.id.songmenu);
        menuFolder_TextView = findViewById(R.id.menuFolder_TextView);
        menuCount_TextView = findViewById(R.id.menuCount_TextView);
        song_list_view = findViewById(R.id.song_list_view);
        FloatingActionButton closeSongsFAB = findViewById(R.id.closeSongsFAB);
        closeSongsFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeMyDrawers("song");
            }
        });


        // The option menu
        optionmenu = findViewById(R.id.optionmenu);
        menuFolder_TextView = findViewById(R.id.menuFolder_TextView);
        menuFolder_TextView.setText(getString(R.string.wait));
        LinearLayout changefolder_LinearLayout = findViewById(R.id.changefolder_LinearLayout);
        changefolder_LinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullscreenActivity.whattodo = "choosefolder";
                openFragment();
            }
        });

        // Make views focusable
        presenter_songtitle.isFocusable();
        presenter_songtitle.requestFocus();

        autoProject.setChecked(preferences.getMyPreferenceBoolean(PresenterMode.this,"presoAutoUpdateProjector",true));

        // Set the button listeners
        presenter_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Edit current set
                //mDrawerLayout.closeDrawer(expListViewOption);
                FullscreenActivity.whattodo = "editset";
                openFragment();
            }
        });
        set_view_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Edit current set
                //mDrawerLayout.closeDrawer(expListViewOption);
                CustomAnimations.animateFAB(set_view_fab,PresenterMode.this);
                FullscreenActivity.whattodo = "editset";
                openFragment();
            }
        });
        startstopSlideShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomAnimations.animateFAB(startstopSlideShow, PresenterMode.this);
                if (isplayingautoslideshow) {
                    prepareStopAutoSlideShow();
                } else {
                    prepareStartAutoSlideShow();
                }
            }
        });
        autoProject.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                preferences.setMyPreferenceBoolean(PresenterMode.this,"presoAutoUpdateProjector",b);
            }
        });

        // Scrollbars
        presenter_set_buttonsListView.setScrollbarFadingEnabled(false);
        presenter_songbuttons.setScrollbarFadingEnabled(false);
        preso_action_buttons_scroll.setScrollbarFadingEnabled(false);

        // Hide some stuff
        presenter_lyrics.setVisibility(View.VISIBLE);
        presenter_lyrics_image.setVisibility(View.GONE);

        // Disable the views until a screen is connected
        noSecondScreen();
    }
    private void screenClickListeners() {
        songandauthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullscreenActivity.whattodo = "songdetails";
                openFragment();
            }
        });
        batteryholder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullscreenActivity.whattodo = "actionbarinfo";
                openFragment();
            }
        });
        pres_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullscreenActivity.whattodo = "songdetails";
                openFragment();
            }
        });
        presenter_order_text.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                preferences.setMyPreferenceBoolean(PresenterMode.this,"usePresentationOrder",isChecked);
                refreshAll();
            }
        });
        presenter_order_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FullscreenActivity.isPDF) {
                    FullscreenActivity.whattodo = "extractPDF";
                    openFragment();
                } else if (FullscreenActivity.isSong) {
                    FullscreenActivity.whattodo = "editsong";
                    openFragment();
                }
            }
        });
        nav_prevsong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomAnimations.animateFAB(nav_prevsong, PresenterMode.this);
                tryClickPreviousSongInSet();
            }
        });
        nav_nextsong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomAnimations.animateFAB(nav_nextsong, PresenterMode.this);
                tryClickNextSongInSet();
            }
        });
        nav_prevsection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomAnimations.animateFAB(nav_prevsection, PresenterMode.this);
                tryClickPreviousSection();
            }
        });
        nav_nextsection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomAnimations.animateFAB(nav_nextsection, PresenterMode.this);
                tryClickNextSection();
            }
        });
        presenter_project_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                projectButtonClick();
            }
        });
        presenter_logo_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoButtonClick();
            }
        });
        presenter_blank_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                blankButtonClick();
            }
        });
        presenter_alert_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertButtonClick();
            }
        });
        presenter_audio_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                audioButtonClick();
            }
        });
        presenter_dB_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dBButtonClick();
            }
        });
        presenter_slide_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullscreenActivity.whattodo = "customreusable_slide";
                openFragment();
            }
        });
        presenter_scripture_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullscreenActivity.whattodo = "customreusable_scripture";
                openFragment();
            }
        });
        presenter_display_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullscreenActivity.whattodo = "connecteddisplay";
                openFragment();
            }
        });
    }
    private void showCorrectViews() {
        if (FullscreenActivity.isImage || FullscreenActivity.isPDF) {
            // Image and PDF files replace the slide text with an image preview
            presenter_lyrics_image.setVisibility(View.VISIBLE);
            presenter_lyrics.setVisibility(View.GONE);
            loopandtimeLinearLayout.setVisibility(View.GONE);
        } else if (FullscreenActivity.isSong) {
            presenter_lyrics_image.setVisibility(View.GONE);
            presenter_lyrics.setVisibility(View.VISIBLE);
            loopandtimeLinearLayout.setVisibility(View.GONE);
        }
        if (FullscreenActivity.isImageSlide) {
            presenter_lyrics_image.setVisibility(View.VISIBLE);
            presenter_lyrics.setVisibility(View.GONE);
            loopandtimeLinearLayout.setVisibility(View.VISIBLE);
        }
        if (FullscreenActivity.isSlide) {
            presenter_lyrics_image.setVisibility(View.GONE);
            presenter_lyrics.setVisibility(View.VISIBLE);
            loopandtimeLinearLayout.setVisibility(View.VISIBLE);
        }
    }
    private void setupSetButtons() {
        // Create a new button for each song in the Set
        setActions.prepareSetList(PresenterMode.this,preferences);
        presenter_set_buttonsListView.removeAllViews();
        try {
            if (StaticVariables.mSetList != null && StaticVariables.mSetList.length > 0) {
                for (int x = 0; x < StaticVariables.mSet.length; x++) {
                    // Button for the song and set
                    Button newSetButton = processSong.makePresenterSetButton(x, PresenterMode.this);
                    newSetButton.setOnClickListener(new SetButtonClickListener(x));
                    presenter_set_buttonsListView.addView(newSetButton);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private class SetButtonClickListener implements View.OnClickListener {
        int which = 0;

        SetButtonClickListener(int i) {
            if (i > 0) {
                which = i;
            }
        }

        @Override
        public void onClick(View view) {
            // Make sure we are now in set view
            StaticVariables.setView = true;
            StaticVariables.indexSongInSet = which;

            // Scroll this song to the top of the list
            presenter_setbuttons.smoothScrollTo(0, view.getTop());

            // We will use the first section in the new song
            StaticVariables.currentSection = 0;

            // Unhightlight all of the items in the set button list except this one
            for (int v = 0; v < presenter_set_buttonsListView.getChildCount(); v++) {
                if (v != which) {
                    // Change the background colour of this button to show it is active
                    processSong.unhighlightPresenterSetButton((Button) presenter_set_buttonsListView.getChildAt(v));
                } else {
                    // Change the background colour of this button to show it is active
                    processSong.highlightPresenterSetButton((Button) presenter_set_buttonsListView.getChildAt(v));
                }
            }

            // Identify our new position in the set
            StaticVariables.indexSongInSet = which;
            if (StaticVariables.mSetList != null && StaticVariables.mSetList.length > which) {
                StaticVariables.whatsongforsetwork = StaticVariables.mSetList[which];
                FullscreenActivity.linkclicked = StaticVariables.mSetList[which];
                if (which < 1) {
                    StaticVariables.previousSongInSet = "";
                } else {
                    StaticVariables.previousSongInSet = StaticVariables.mSetList[which - 1];
                }
                if (which == (StaticVariables.setSize - 1)) {
                    StaticVariables.nextSongInSet = "";
                } else {
                    StaticVariables.previousSongInSet = StaticVariables.mSetList[which + 1];
                }

                // Call the script to get the song location.
                setActions.getSongFileAndFolder(PresenterMode.this);
                findSongInFolders();
                prepareSongMenu();

                // Close the drawers in case they are open
                closeMyDrawers("both");

                // Load the song
                loadSong();
            }
        }
    }
    private void setupSongButtons() {
        // Create a new button for each songSection
        // If the 'song' is custom images, set them as the background
        presenter_song_buttonsListView.removeAllViews();
        presenter_songtitle.setText(StaticVariables.mTitle);
        presenter_author.setText(StaticVariables.mAuthor);
        presenter_copyright.setText(StaticVariables.mCopyright);
        if (StaticVariables.mPresentation.isEmpty()) {
            presenter_order_text.setText(getResources().getString(R.string.notset));
        } else {
            presenter_order_text.setText(StaticVariables.mPresentation);
        }
        // Need to decide if checkbox is on or off
        presenter_order_text.setChecked(preferences.getMyPreferenceBoolean(PresenterMode.this,"usePresentationOrder",false));

        imagelocs = null;

        // Song and set button variables
        LinearLayout newSongSectionGroup;
        Button newSongButton;
        TextView newSongSectionText;
        if (FullscreenActivity.isPDF) {
            int pages = FullscreenActivity.pdfPageCount;
            if (pages > 0) {
                for (int p = 0; p < pages; p++) {
                    String sectionText = (p + 1) + "";
                    String buttonText = getResources().getString(R.string.pdf_selectpage) + " " + (p + 1);
                    newSongSectionGroup = processSong.makePresenterSongButtonLayout(PresenterMode.this);
                    newSongSectionText = processSong.makePresenterSongButtonSection(PresenterMode.this, sectionText);
                    newSongButton = processSong.makePresenterSongButtonContent(PresenterMode.this, buttonText);
                    newSongButton.setOnClickListener(new SectionButtonClickListener(p));
                    newSongSectionGroup.addView(newSongSectionText);
                    newSongSectionGroup.addView(newSongButton);
                    presenter_song_buttonsListView.addView(newSongSectionGroup);
                }
            }


        } else if (FullscreenActivity.isImage) {
            String sectionText = getResources().getString(R.string.image);
            String buttonText = StaticVariables.songfilename;
            newSongSectionGroup = processSong.makePresenterSongButtonLayout(PresenterMode.this);
            newSongSectionText = processSong.makePresenterSongButtonSection(PresenterMode.this, sectionText);
            newSongButton = processSong.makePresenterSongButtonContent(PresenterMode.this, buttonText);
            newSongButton.setOnClickListener(new SectionButtonClickListener(0));
            newSongSectionGroup.addView(newSongSectionText);
            newSongSectionGroup.addView(newSongButton);
            presenter_song_buttonsListView.addView(newSongSectionGroup);

        } else {
            if (StaticVariables.whichSongFolder.contains("../Images")) {
                // Custom images so split the mUser3 field by newline.  Each value is image location
                imagelocs = StaticVariables.mUser3.split("\n");
            }

            if (StaticVariables.songSections != null && StaticVariables.songSections.length > 0) {
                int numsectionbuttons = StaticVariables.songSections.length;
                for (int x = 0; x < numsectionbuttons; x++) {

                    // Get the image locations if they exist
                    String thisloc = null;
                    if (imagelocs != null && imagelocs[x] != null) {
                        thisloc = imagelocs[x];
                    }

                    StringBuilder buttonText;
                    if (StaticVariables.songSections!=null && StaticVariables.songSections.length>x) {
                        buttonText = new StringBuilder(StaticVariables.songSections[x]);
                    } else {
                        buttonText = new StringBuilder();
                    }
                    // Get the text for the button
                    if (FullscreenActivity.isImageSlide) {
                        if (thisloc == null) {
                            thisloc = "";
                        }
                        buttonText = new StringBuilder(thisloc);
                        // Try to remove everything except the name
                        if (buttonText.toString().contains("/") && buttonText.lastIndexOf("/") < buttonText.length() - 1) {
                            buttonText = new StringBuilder(buttonText.substring(buttonText.lastIndexOf("/") + 1));
                        }
                    }

                    // If we aren't showing the chords, strip them out
                    if (!preferences.getMyPreferenceBoolean(PresenterMode.this,"presoShowChords",false)) {
                        String[] l;
                        l = buttonText.toString().split("\n");

                        buttonText = new StringBuilder();
                        // Add the lines back in, but removing the ones starting with .
                        for (String eachline : l) {
                            if (!eachline.startsWith(".")) {
                                buttonText.append(eachline.trim()).append("\n");
                            }
                        }
                        buttonText = new StringBuilder(buttonText.toString().trim());
                    }

                    // Get the button information (type of section)
                    String sectionText;
                    try {
                        sectionText = StaticVariables.songSectionsLabels[x];
                    } catch (Exception e) {
                        sectionText = "";
                    }

                    newSongSectionGroup = processSong.makePresenterSongButtonLayout(PresenterMode.this);
                    newSongSectionText = processSong.makePresenterSongButtonSection(PresenterMode.this, sectionText.replace("_", " "));
                    newSongButton = processSong.makePresenterSongButtonContent(PresenterMode.this, buttonText.toString());

                    if (FullscreenActivity.isImageSlide || FullscreenActivity.isSlide) {
                        // Make sure the time, loop and autoslideshow buttons are visible
                        loopandtimeLinearLayout.setVisibility(View.VISIBLE);
                        enabledisableButton(startstopSlideShow, true);
                        // Just in case we were playing a slide show, stop it
                        prepareStopAutoSlideShow();
                        // Set the appropiate values
                        if (StaticVariables.mUser1 != null) {
                            timeEditText.setText(StaticVariables.mUser1);
                        }
                        if (StaticVariables.mUser2 != null && StaticVariables.mUser2.equals("true")) {
                            loopCheckBox.setChecked(true);
                        } else {
                            loopCheckBox.setChecked(false);
                        }

                    } else {
                        // Otherwise, hide them
                        loopandtimeLinearLayout.setVisibility(View.GONE);
                    }
                    newSongButton.setOnClickListener(new SectionButtonClickListener(x));
                    newSongSectionGroup.addView(newSongSectionText);
                    newSongSectionGroup.addView(newSongButton);
                    presenter_song_buttonsListView.addView(newSongSectionGroup);
                }
            }
        }
        // Select the first button if we can
        StaticVariables.currentSection = 0;
        selectSectionButtonInSong(StaticVariables.currentSection);
    }

    private void selectSectionButtonInSong(int which) {

        StaticVariables.currentSection = which;
        if (StaticVariables.songSections != null && StaticVariables.songSections.length > 0) {
            // if which=-1 then we want to pick the first section of the previous song in set
            // Otherwise, move to the next one.
            // If we are at the end, move to the nextsonginset

            if (StaticVariables.currentSection < 0 && !isplayingautoslideshow) {
                StaticVariables.currentSection = 0;
                tryClickPreviousSongInSet();
            } else if (StaticVariables.currentSection >= StaticVariables.songSections.length && !isplayingautoslideshow) {
                StaticVariables.currentSection = 0;
                tryClickNextSongInSet();
            } else if (StaticVariables.currentSection < 0 || StaticVariables.currentSection >= StaticVariables.songSections.length) {
                StaticVariables.currentSection = 0;
            }

            // enable or disable the quick nav buttons
            fixNavButtons();

            LinearLayout row = (LinearLayout) presenter_song_buttonsListView.getChildAt(StaticVariables.currentSection);
            Button thisbutton = (Button) row.getChildAt(1);
            thisbutton.performClick();
        }
    }
    private void unhighlightAllSetButtons() {
        // Unhighlighting all buttons
        int numbuttons = presenter_set_buttonsListView.getChildCount();
        for (int z = 0; z < numbuttons; z++) {
            processSong.unhighlightPresenterSetButton((Button) presenter_set_buttonsListView.getChildAt(z));
        }
    }
    private void fixNavButtons() {
        // By default disable them all!
        //enabledisableButton(nav_prevsection,false);
        //enabledisableButton(nav_nextsection,false);
        enabledisableButton(nav_prevsong, false);
        enabledisableButton(nav_nextsong, false);

        // Show the previous section button if we currently showing a section higher than 0
        if (StaticVariables.currentSection > 0) {
            enabledisableButton(nav_prevsection, true);
        } else {
            enabledisableButton(nav_prevsection, false);
        }

        // Show the next section button if we are currently in a section lower than the count by 1
        int sectionsavailable = presenter_song_buttonsListView.getChildCount();
        if (StaticVariables.currentSection < sectionsavailable - 1) {
            enabledisableButton(nav_nextsection, true);
        } else {
            enabledisableButton(nav_nextsection, false);
        }

        // Enable the previous set button if we are in set view and indexSongInSet is >0 (but less than set size)
        int numsongsinset = 0;
        if (StaticVariables.mSetList != null) {
            numsongsinset = StaticVariables.mSetList.length;
        }
        if (StaticVariables.setView && StaticVariables.indexSongInSet > 0 && StaticVariables.indexSongInSet < numsongsinset) {
            enabledisableButton(nav_prevsong, true);
        } else {
            enabledisableButton(nav_prevsong, false);
        }

        // Enable the next set button if we are in set view and index SongInSet is < set size -1
        if (StaticVariables.setView && StaticVariables.indexSongInSet > -1 && StaticVariables.indexSongInSet < numsongsinset - 1) {
            enabledisableButton(nav_nextsong, true);
        } else {
            enabledisableButton(nav_nextsong, false);
        }

        /*if (FullscreenActivity.songSections!=null && FullscreenActivity.currentSection>=FullscreenActivity.songSections.length) {
            enabledisableButton(nav_nextsection,false);
        } else if (FullscreenActivity.songSections!=null){
            enabledisableButton(nav_nextsection,true);
        } else {
            enabledisableButton(nav_nextsection,false);
        }
        // Initially disable the set buttons
        enabledisableButton(nav_prevsong,false);
        enabledisableButton(nav_nextsong,false);

        if (FullscreenActivity.setView && FullscreenActivity.indexSongInSet>0) {
            enabledisableButton(nav_prevsong,true);
        }
        if (FullscreenActivity.setView && FullscreenActivity.indexSongInSet>=0 &&
                FullscreenActivity.indexSongInSet<FullscreenActivity.mSetList.length-1) {
            enabledisableButton(nav_nextsong, true);
        }*/
    }
    private void tryClickNextSection() {
        if (StaticVariables.currentSection < StaticVariables.songSections.length - 1) {
            StaticVariables.currentSection += 1;
            autoproject = true;
            preso_action_buttons_scroll.smoothScrollTo(0, presenter_project_group.getTop());
            selectSectionButtonInSong(StaticVariables.currentSection);
        }
    }
    private void tryClickPreviousSection() {
        // Enable or disable the previous section button
        if (StaticVariables.currentSection > 0) {
            StaticVariables.currentSection -= 1;
            autoproject = true;
            preso_action_buttons_scroll.smoothScrollTo(0, presenter_project_group.getTop());
            selectSectionButtonInSong(StaticVariables.currentSection);
        }
    }
    private void tryClickNextSongInSet() {
        if (StaticVariables.mSetList != null && StaticVariables.indexSongInSet>-1 && StaticVariables.indexSongInSet < StaticVariables.mSetList.length - 1) {
            StaticVariables.indexSongInSet += 1;
            StaticVariables.currentSection = 0;
            autoproject = true;
            doMoveInSet();
        }
    }
    private void tryClickPreviousSongInSet() {
        if (StaticVariables.mSetList != null && StaticVariables.mSetList.length > StaticVariables.indexSongInSet &&
                StaticVariables.indexSongInSet > 0) {
            StaticVariables.indexSongInSet -= 1;
            StaticVariables.currentSection = 0;
            autoproject = true;
            doMoveInSet();
        }
    }
    private void enabledisableButton(FloatingActionButton fab, boolean enable) {
        if (fab!=null) {
            fab.setEnabled(enable);
            if (enable) {
                fab.setAlpha(1.0f);
            } else {
                fab.setAlpha(0.5f);
            }
        }
    }
    @Override
    public void doMoveSection() {
        switch (StaticVariables.setMoveDirection) {
            case "forward":
                StaticVariables.currentSection += 1;
                selectSectionButtonInSong(StaticVariables.currentSection);
                break;
            case "back":
                StaticVariables.currentSection -= 1;
                selectSectionButtonInSong(StaticVariables.currentSection);
                break;
        }
    }

    // Salut stuff
    @Override
    public void onDataReceived(Object data) {
        // Attempt to extract the song details
        if (data != null && (data.toString().contains("_____") || data.toString().contains("<lyrics>") ||
                data.toString().contains("___section___"))) {
            String action = processSong.getSalutReceivedLocation(data.toString(), PresenterMode.this, preferences, storageAccess);
            switch (action) {
                case "Location":
                    holdBeforeLoading();
                    break;
                case "HostFile":
                    holdBeforeLoadingXML();
                    break;
                case "SongSection":
                    holdBeforeLoadingSection(processSong.getSalutReceivedSection(data.toString()));
                    break;
            }
        }
    }

    public void doMoveInSet() {
        doCancelAsyncTask(do_moveinset);
        do_moveinset = new DoMoveInSet();
        try {
            do_moveinset.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void confirmedAction() {
        switch (FullscreenActivity.whattodo) {
            case "exit":
                this.finish();
                break;

            case "saveset":
                // Save the set
                setActions.saveSetMessage(PresenterMode.this, preferences, storageAccess, processSong);
                refreshAll();
                break;

            case "clearset":
                // Clear the set
                setActions.clearSet(PresenterMode.this,preferences);
                refreshAll();
                break;

            case "deletesong":
                // Delete current song
                Uri uri = storageAccess.getUriForItem(PresenterMode.this, preferences, "Songs", StaticVariables.whichSongFolder,
                        StaticVariables.songfilename);
                storageAccess.deleteFile(PresenterMode.this, uri);
                // If we are autologging CCLI information
                if (preferences.getMyPreferenceBoolean(PresenterMode.this,"ccliAutomaticLogging",false)) {
                    PopUpCCLIFragment.addUsageEntryToLog(PresenterMode.this, preferences, StaticVariables.whichSongFolder + "/" + StaticVariables.songfilename,
                            "", "",
                            "", "", "2"); // Deleted
                }

                // Remove the item from the SQL database
                // Get the SQLite stuff
                sqLiteHelper.deleteSong(PresenterMode.this, sqLite.getSongid());
                prepareSongMenu();
                break;

            case "deleteset":
                // Delete set
                setActions.deleteSet(PresenterMode.this, preferences, storageAccess);
                refreshAll();
                break;

            case "wipeallsongs":
                // Wipe all songs - Getting rid of this!!!!!
                Log.d("PresenterMode", "Trying wipe songs folder - ignoring");
                /*storageAccess.wipeFolder(PresenterMode.this, preferences, "Songs", "");
                // Rebuild the song list
                storageAccess.listSongs(PresenterMode.this, preferences);
                listSongFiles.songUrisInFolder(PresenterMode.this, preferences);
                refreshAll();*/
                break;

            /*case "resetcolours":
                // Reset the theme colours
                PopUpThemeChooserFragment.getDefaultColours();
                Preferences.savePreferences();
                refreshAll();
                FullscreenActivity.whattodo = "changetheme";
                openFragment();
                break;*/
        }
    }

    private void holdBeforeLoading() {
        // When a song is sent via Salut, it occassionally gets set multiple times (poor network)
        // As soon as we receive if, check this is the first time
        if (FullscreenActivity.firstReceivingOfSalut) {
            // Now turn it off
            FullscreenActivity.firstReceivingOfSalut = false;
            // Decide if the file exists on this device first
            Uri uri = storageAccess.getUriForItem(PresenterMode.this, preferences, "Songs",
                    StaticVariables.whichSongFolder, StaticVariables.songfilename);

            if (storageAccess.uriExists(PresenterMode.this, uri)) {
                loadSong();
            }

            // After a delay of 2 seconds, reset the firstReceivingOfSalut;
            Handler h = new Handler();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    FullscreenActivity.firstReceivingOfSalut = true;
                }
            }, 500);
        }
    }
    private void holdBeforeSending() {
        // When a song is sent via Salut, it occassionally gets set multiple times (poor network)
        // As soon as we send it, check this is the first time
        if (FullscreenActivity.firstSendingOfSalut) {
            // Now turn it off
            FullscreenActivity.firstSendingOfSalut = false;
            if (FullscreenActivity.network != null) {
                if (FullscreenActivity.network.isRunningAsHost) {
                    try {
                        FullscreenActivity.network.sendToAllDevices(myMessage, new SalutCallback() {
                            @Override
                            public void call() {
                                Log.e(TAG, "Oh no! The data failed to send.");
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                // After a delay of 2 seconds, reset the firstSendingOfSalut;
                Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        FullscreenActivity.firstSendingOfSalut = true;
                    }
                }, 500);
            }
        }
    }
    private void holdBeforeSendingXML() {
        // When a song is sent via Salut, it occassionally gets set multiple times (poor network)
        // As soon as we send it, check this is the first time
        if (FullscreenActivity.firstSendingOfSalutXML) {
            // Now turn it off
            FullscreenActivity.firstSendingOfSalutXML = false;
            if (FullscreenActivity.network != null) {
                if (FullscreenActivity.network.isRunningAsHost) {
                    try {
                        FullscreenActivity.network.sendToAllDevices(mySongMessage, new SalutCallback() {
                            @Override
                            public void call() {
                                Log.e(TAG, "Oh no! The data failed to send.");
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        Log.d("d", "mySongMessage being sent=" + mySongMessage.description);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                // After a delay of 2 seconds, reset the firstSendingOfSalut;
                Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        FullscreenActivity.firstSendingOfSalutXML = true;
                    }
                }, 500);
            }
        }
    }
    private void holdBeforeSendingSection() {
        // When a song is sent via Salut, it occassionally gets set multiple times (poor network)
        // As soon as we send it, check this is the first time
        if (FullscreenActivity.firstSendingOfSalutSection) {
            // Now turn it off
            FullscreenActivity.firstSendingOfSalutSection = false;
            if (FullscreenActivity.network != null) {
                if (FullscreenActivity.network.isRunningAsHost) {
                    try {
                        FullscreenActivity.network.sendToAllDevices(mySectionMessage, new SalutCallback() {
                            @Override
                            public void call() {
                                Log.e(TAG, "Oh no! The data failed to send.");
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                // After a delay of 2 seconds, reset the firstSendingOfSalutSection;
                Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        FullscreenActivity.firstSendingOfSalutSection = true;
                    }
                }, 500);
            }
        }
    }

    private void holdBeforeLoadingXML() {
        // When a song is sent via Salut, it occassionally gets set multiple times (poor network)
        // As soon as we receive if, check this is the first time
        if (FullscreenActivity.firstReceivingOfSalutXML) {
            // Now turn it off
            FullscreenActivity.firstReceivingOfSalutXML = false;

            Uri uri = storageAccess.getUriForItem(PresenterMode.this, preferences, "Songs",
                    StaticVariables.whichSongFolder, StaticVariables.songfilename);
            if (!storageAccess.uriExists(PresenterMode.this, uri) || FullscreenActivity.receiveHostFiles) {
                FullscreenActivity.mySalutXML = FullscreenActivity.mySalutXML.replace("\\n", "$$__$$");
                FullscreenActivity.mySalutXML = FullscreenActivity.mySalutXML.replace("\\", "");
                FullscreenActivity.mySalutXML = FullscreenActivity.mySalutXML.replace("$$__$$", "\n");

                // Create the temp song file
                Uri receivedUri = storageAccess.getUriForItem(PresenterMode.this, preferences,
                        "Received", "", "ReceivedSong");
                storageAccess.lollipopCreateFileForOutputStream(PresenterMode.this, preferences, receivedUri,
                        null, "Received", "", "ReceivedSong");
                OutputStream outputStream = storageAccess.getOutputStream(PresenterMode.this, receivedUri);
                storageAccess.writeFileFromString(FullscreenActivity.mySalutXML, outputStream);

                StaticVariables.songfilename = "ReceivedSong";
                StaticVariables.whichSongFolder = "../Received";

                loadSong();

            } else if (storageAccess.uriExists(PresenterMode.this,uri)) {
                // Just load the song
                loadSong();
            } else {
                StaticVariables.myToastMessage = getResources().getString(R.string.songdoesntexist);
                ShowToast.showToast(PresenterMode.this);
            }

            // After a delay of 2 seconds, reset the firstReceivingOfSalut;
            Handler h = new Handler();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    FullscreenActivity.firstReceivingOfSalutXML = true;
                }
            },500);
        }
    }

    private void loadImagePreview() {
        // Make the appropriate bits visible
        presenter_lyrics_image.setVisibility(View.VISIBLE);
        presenter_lyrics.setVisibility(View.GONE);

        // Process the image location into an URI, then get the sizes
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        //Returns null, sizes are in the options variable
        InputStream inputStream = storageAccess.getInputStream(PresenterMode.this, StaticVariables.uriToLoad);
        if (inputStream != null) {
            BitmapFactory.decodeStream(inputStream, null, options);
            int imgwidth = options.outWidth;
            int imgheight = options.outHeight;
            int widthavail = 800;
            int heightavail = 800;
            float xscale = (float) widthavail / (float) imgwidth;
            float yscale = (float) heightavail / (float) imgheight;
            // Now decide on the scaling required....
            if (xscale > yscale) {
                xscale = yscale;
            }
            int glidewidth = (int) ((float) imgwidth * xscale);
            int glideheight = (int) ((float) imgheight * xscale);

            // Draw the image to the preview window
            presenter_lyrics_image.setBackgroundColor(0x00000000);
            RequestOptions myOptions = new RequestOptions()
                    .override(glidewidth, glideheight);
            Glide.with(PresenterMode.this).load(StaticVariables.uriToLoad).apply(myOptions).into(presenter_lyrics_image);

            if (autoproject || preferences.getMyPreferenceBoolean(PresenterMode.this,"presoAutoUpdateProjector",true)) {
                autoproject = false;
                presenter_project_group.performClick();
            }
        }
    }
    private void holdBeforeLoadingSection(int s) {
        if (FullscreenActivity.firstReceivingOfSalutSection) {
            // Now turn it off
            FullscreenActivity.firstReceivingOfSalutSection = false;
            StaticVariables.currentSection = s;
            selectSectionButtonInSong(StaticVariables.currentSection);

            // After a delay of 2 seconds, reset the firstReceivingOfSalutSection;
            Handler h = new Handler();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    FullscreenActivity.firstReceivingOfSalutSection = true;
                }
            }, 500);
        }
    }

    private void sendSongLocationToConnected() {
        Log.d("d","Sending song");
        String messageString = StaticVariables.whichSongFolder + "_____" +
                StaticVariables.songfilename + "_____" +
                FullscreenActivity.whichDirection;

        myMessage = new SalutMessage();
        myMessage.description = messageString;
        holdBeforeSending();
    }
    private void sendSongXMLToConnected() {
        String myXML;
        if (FullscreenActivity.isSong && FullscreenActivity.myXML != null) {
            myXML = FullscreenActivity.presenterSendSong;
        } else {
            myXML = "";
        }
        mySongMessage = new SalutMessage();
        mySongMessage.description = myXML;
        holdBeforeSendingXML();
    }
    private void sendSongSectionToConnected() {
        int sectionval;
        if (StaticVariables.whichMode.equals("Stage") || StaticVariables.whichMode.equals("Presentation")) {
            sectionval = StaticVariables.currentSection;
            mySectionMessage = new SalutMessage();
            mySectionMessage.description = "___section___" + sectionval;
            holdBeforeSendingSection();
        }
    }
    private void getBluetoothName() {
        try {
            if (FullscreenActivity.mBluetoothAdapter == null) {
                FullscreenActivity.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            }
            FullscreenActivity.mBluetoothName = FullscreenActivity.mBluetoothAdapter.getName();
            if (FullscreenActivity.mBluetoothName == null) {
                FullscreenActivity.mBluetoothName = "Unknown";
            }
        } catch (Exception e) {
            FullscreenActivity.mBluetoothName = "Unknown";
        }
    }
    private void startRegistration() {
        try {
            FullscreenActivity.dataReceiver = new SalutDataReceiver(PresenterMode.this, PresenterMode.this);
            FullscreenActivity.serviceData = new SalutServiceData("OpenSongApp", 60606,
                    FullscreenActivity.mBluetoothName);

            FullscreenActivity.network = new Salut(FullscreenActivity.dataReceiver, FullscreenActivity.serviceData, new SalutCallback() {
                @Override
                public void call() {
                    FullscreenActivity.salutLog += "\n" + getResources().getString(R.string.nowifidirect);
                }
            });

        } catch (Exception e) {
            FullscreenActivity.salutLog += "\n" + getResources().getString(R.string.nowifidirect);
            e.printStackTrace();
        }
    }

    // Loading the song
    @Override
    public void loadSongFromSet() {
        // Redraw the set buttons as the user may have changed the order
        refreshAll();

        closePopUps();

        StaticVariables.setView = true;
        // Specify which songinset button
        StaticVariables.currentSection = 0;

        // Select it
        if (presenter_set_buttonsListView.getChildCount() > StaticVariables.indexSongInSet) {
            Button which_song_to_click = (Button) presenter_set_buttonsListView.getChildAt(StaticVariables.indexSongInSet);
            which_song_to_click.performClick();
        }
    }

    // The right hand column buttons
    private void projectButtonClick() {
        try {
            projectButton_isSelected = !projectButton_isSelected;

            if (!FullscreenActivity.isPDF && !FullscreenActivity.isImage && !FullscreenActivity.isImageSlide) {
                StaticVariables.projectedContents[StaticVariables.currentSection] = presenter_lyrics.getText().toString().split("\n");
                int linesnow = StaticVariables.projectedContents[StaticVariables.currentSection].length;
                StaticVariables.projectedLineTypes[StaticVariables.currentSection] = new String[linesnow];
                for (int i = 0; i < linesnow; i++) {
                    StaticVariables.projectedLineTypes[StaticVariables.currentSection][i] =
                            processSong.determineLineTypes(StaticVariables.projectedContents[StaticVariables.currentSection][i], PresenterMode.this);
                }
            }

            // Turn off the other actions buttons as we are now projecting!
            if (logoButton_isSelected) {
                presenter_logo_group.performClick();  // This turns off the logo
            }
            if (blankButton_isSelected) {
                presenter_blank_group.performClick();
            }


            // Turn on the project button for now
            highlightButtonClicked(presenter_project_group);


            // Update the projector
            if (mSelectedDevice != null) {
                try {
                    PresentationService.ExternalDisplay.doUpdate();

                    // If we are autologging CCLI information
                    if (newsongloaded && preferences.getMyPreferenceBoolean(PresenterMode.this,"ccliAutomaticLogging",false)) {
                        PopUpCCLIFragment.addUsageEntryToLog(PresenterMode.this, preferences, StaticVariables.whichSongFolder + "/" + StaticVariables.songfilename,
                                StaticVariables.mTitle, StaticVariables.mAuthor,
                                StaticVariables.mCopyright, StaticVariables.mCCLI, "5"); // Presented
                        newsongloaded = false;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (FullscreenActivity.isHDMIConnected) {
                try {
                    PresentationServiceHDMI.doUpdate();
                    // If we are autologging CCLI information
                    if (newsongloaded && preferences.getMyPreferenceBoolean(PresenterMode.this,"ccliAutomaticLogging",false)) {
                        PopUpCCLIFragment.addUsageEntryToLog(PresenterMode.this, preferences, StaticVariables.whichSongFolder + "/" + StaticVariables.songfilename,
                                StaticVariables.mTitle, StaticVariables.mAuthor,
                                StaticVariables.mCopyright, StaticVariables.mCCLI, "5"); // Presented
                        newsongloaded = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            Handler unhighlight = new Handler();
            unhighlight.postDelayed(new Runnable() {
                @Override
                public void run() {
                    projectButton_isSelected = false;
                    unhighlightButtonClicked(presenter_project_group);
                }
            }, 500);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void rebuildSearchIndex() {
        showToastMessage(getString(R.string.search_rebuild));
        RebuildSearchIndex doRebuildSearchIndex = new RebuildSearchIndex();
        doRebuildSearchIndex.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
    @SuppressLint("StaticFieldLeak")
    private class RebuildSearchIndex extends AsyncTask<Object, Void, String> {

        @Override
        protected String doInBackground(Object... objects) {
            // Write a crude text file (line separated) with the song Ids (folder/file)
            ArrayList<String> songIds = storageAccess.listSongs(PresenterMode.this, preferences);
            storageAccess.writeSongIDFile(PresenterMode.this, preferences, songIds);

            // Try to create the basic database
            sqLiteHelper.resetDatabase(PresenterMode.this);
            sqLiteHelper.insertFast(PresenterMode.this,storageAccess);

            // Build the full index
            indexSongs.fullIndex(PresenterMode.this,preferences,storageAccess, sqLiteHelper, songXML,
                    chordProConvert, onSongConvert, textSongConvert, usrConvert);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            showToastMessage(getString(R.string.search_index_end));
            // Update the song menu
            prepareSongMenu();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == StaticVariables.LINK_AUDIO || requestCode == StaticVariables.LINK_OTHER) {
            // This has been called from the popuplinks fragment
            try {
                newFragment.onActivityResult(requestCode, resultCode, data);
            } catch (Exception e) {
                Log.d("StageMode","Error sending activity result to fragment");
            }

        } else if (requestCode==StaticVariables.REQUEST_IMAGE_CODE) {
            // This has been called from the custom slides fragment
            try {
                newFragment.onActivityResult(requestCode, resultCode, data);
            } catch (Exception e) {
                Log.d("StageMode", "Error sending activity result to fragment");
            }

        } else if (requestCode==StaticVariables.REQUEST_BACKGROUND_IMAGE1 ||
                requestCode==StaticVariables.REQUEST_BACKGROUND_IMAGE2 ||
                requestCode==StaticVariables.REQUEST_BACKGROUND_VIDEO1 ||
                requestCode==StaticVariables.REQUEST_BACKGROUND_VIDEO2 ||
                requestCode==StaticVariables.REQUEST_CUSTOM_LOGO) {
            // This has been called from the layout dialog.  Send the info back there
            try {
                newFragment.onActivityResult(requestCode, resultCode, data);
            } catch (Exception e) {
                Log.d("StageMode", "Error sending activity result to fragment");
            }

        } else if (requestCode == StaticVariables.REQUEST_CAMERA_CODE && resultCode == Activity.RESULT_OK) {
            FullscreenActivity.whattodo = "savecameraimage";
            openFragment();

        } else if (requestCode == StaticVariables.REQUEST_PDF_CODE) {
            // PDF sent back, so reload it
            loadSong();

        } else if (requestCode == StaticVariables.REQUEST_FILE_CHOOSER && data != null && data.getExtras() != null) {
            try {
                // This is for the File Chooser returning a file uri
                String filelocation = data.getExtras().getString("data");
                if (filelocation != null) {
                    boolean validfiletype = (FullscreenActivity.whattodo.equals("processimportosb") && filelocation.endsWith(".osb")) ||
                            (FullscreenActivity.whattodo.equals("importos") && filelocation.endsWith(".backup")) ||
                            FullscreenActivity.whattodo.equals("doimport") ||
                            FullscreenActivity.whattodo.equals("doimportset");

                    if (validfiletype) {
                        File f = new File(filelocation);
                        FullscreenActivity.file_uri = FileProvider.getUriForFile(PresenterMode.this,
                                "OpenSongAppFiles", f);
                        openFragment();
                    } else {
                        StaticVariables.myToastMessage = getString(R.string.file_type_unknown);
                        ShowToast.showToast(PresenterMode.this);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (requestCode == StaticVariables.REQUEST_PROFILE_LOAD && data!=null && data.getData()!=null) {
            // Loading in a profile
            new Thread(new Runnable() {
                @Override
                public void run() {
                    boolean success = profileActions.doLoadProfile(PresenterMode.this,preferences,storageAccess,data.getData());
                    if (success) {
                        StaticVariables.myToastMessage = getString(R.string.success);
                    } else {
                        StaticVariables.myToastMessage = getString(R.string.error);
                    }
                    // Once done, reload everything
                    runOnUiThread(new Runnable() {
                                      @Override
                                      public void run() {
                                          ShowToast.showToast(PresenterMode.this);
                                          loadStartUpVariables();
                                          refreshAll();
                                      }
                                  }
                    );
                }
            }).start();

        } else if (requestCode == StaticVariables.REQUEST_PROFILE_SAVE && data!=null && data.getData()!=null) {
            // Saving a profile
            new Thread(new Runnable() {
                @Override
                public void run() {
                    boolean success = profileActions.doSaveProfile(PresenterMode.this,preferences,storageAccess,data.getData());
                    if (success) {
                        StaticVariables.myToastMessage = getString(R.string.success);
                    } else {
                        StaticVariables.myToastMessage = getString(R.string.error);
                    }
                    // Once done, say so
                    runOnUiThread(new Runnable() {
                                      @Override
                                      public void run() {
                                          ShowToast.showToast(PresenterMode.this);
                                      }
                                  }
                    );
                }
            }).start();
        }
    }

    private class SectionButtonClickListener implements View.OnClickListener {
        int which = 0;

        SectionButtonClickListener(int i) {
            if (i > 0) {
                which = i;
            }
        }

        @Override
        public void onClick(View view) {

            // We will use this section for the song
            StaticVariables.currentSection = which;

            // Fix the nav buttons
            fixNavButtons();

            // Send section to other devices (checks we are in stage or presentation mode in called method
            if (FullscreenActivity.network != null && FullscreenActivity.network.isRunningAsHost) {
                try {
                    sendSongSectionToConnected();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // Scroll this section to the top of the list
            // Have to do this manually - add the height of the buttons before the one wanted + margin
            int totalheight = 0;
            for (int d = 0; d < which; d++) {
                totalheight += presenter_song_buttonsListView.getChildAt(d).getHeight();
                totalheight += 10;
            }
            presenter_songbuttons.smoothScrollTo(0, totalheight);


            // Unhightlight all of the items in the song button list except this one
            for (int v = 0; v < presenter_song_buttonsListView.getChildCount(); v++) {
                LinearLayout row = (LinearLayout) presenter_song_buttonsListView.getChildAt(v);
                if (v != which) {
                    processSong.unhighlightPresenterSongButton((Button) row.getChildAt(1));
                } else {
                    // Change the background colour of this button to show it is active
                    processSong.highlightPresenterSongButton((Button) row.getChildAt(1));
                }
            }


            // If this is an image, hide the text, show the image, otherwise show the text in the slide window
            if (FullscreenActivity.isPDF) {
                FullscreenActivity.pdfPageCurrent = which;
                loadPDFPagePreview();
            } else if (FullscreenActivity.isImage) {
                StaticVariables.uriToLoad = storageAccess.getUriForItem(PresenterMode.this, preferences, "Songs", StaticVariables.whichSongFolder, StaticVariables.songfilename);
                loadImagePreview();
            } else if (FullscreenActivity.isImageSlide) {
                // Get the image location from the projectedSongSection
                if (imagelocs[StaticVariables.currentSection] != null) {
                    String loc = imagelocs[StaticVariables.currentSection];
                    Log.d("PresenterMode","image uri="+loc);
                    //StaticVariables.uriToLoad = Uri.parse(imagelocs[StaticVariables.currentSection]);
                    StaticVariables.uriToLoad = storageAccess.fixLocalisedUri(PresenterMode.this,preferences,loc);
                    loadImagePreview();
                }
            } else {
                loadSongPreview();
            }

            // Since the slide has been armed, but not projected, turn off the project button
            // This encourages the user to click it again to update the projector screen
            unhighlightButtonClicked(presenter_project_group);
            projectButton_isSelected = false;
        }
    }
    private void loadSongPreview() {
        // Set the appropriate views to visible
        presenter_lyrics_image.setVisibility(View.GONE);
        presenter_lyrics.setVisibility(View.VISIBLE);

        // Prepare the text to go in the view
        StringBuilder s = new StringBuilder();
        try {
            if (StaticVariables.projectedContents!=null && StaticVariables.projectedContents[StaticVariables.currentSection]!=null) {
                for (int w = 0; w < StaticVariables.projectedContents[StaticVariables.currentSection].length; w++) {
                    if (preferences.getMyPreferenceBoolean(PresenterMode.this,"presoShowChords",false)) {
                        s.append(StaticVariables.projectedContents[StaticVariables.currentSection][w]).append("\n");
                    } else {
                        s.append(StaticVariables.projectedContents[StaticVariables.currentSection][w].trim()).append("\n");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        s = new StringBuilder(s.toString().trim());

        // And write it
        presenter_lyrics.setText(s.toString());

        if (autoproject || preferences.getMyPreferenceBoolean(PresenterMode.this,"presoAutoUpdateProjector",true)) {
            autoproject = false;
            presenter_project_group.performClick();
        }
    }

    // Interface listeners for PopUpPages
    @Override
    public void backupInstall() {
        // Songs have been imported, so update the song menu and rebuild the search index
        rebuildSearchIndex();
    }

    @Override
    public void fixSet() {
        closeMyDrawers("song");
        setupSetButtons();
    }
    @Override
    public void callIntent(String what, Intent i) {
        switch (what) {
            case "web":
                startActivity(i);
                break;
            case "twitter":
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=opensongapp")));
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/opensongapp")));
                }
                break;
            case "forum":
                String mailto = "mailto:opensongapp@googlegroups.com";
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse(mailto));
                try {
                    startActivity(emailIntent);
                } catch (ActivityNotFoundException e) {
                    StaticVariables.myToastMessage = getString(R.string.error);
                    ShowToast.showToast(PresenterMode.this);
                }
                break;

            case "activity":
                startActivity(i);
                finish();
                break;

            case "openpdf":
                try {
                    startActivity(i);
                } catch (ActivityNotFoundException e) {
                    // Instruct the user to install a PDF reader here, or something
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.apps.pdfviewer")));
                    } catch (ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.pdfviewer")));
                    }
                }
                break;
        }
    }

    @Override
    public void profileWork(String s) {
        switch (s) {
            case "load":
                try {
                    Intent i = profileActions.openProfile(PresenterMode.this,preferences,storageAccess);
                    this.startActivityForResult(i, StaticVariables.REQUEST_PROFILE_LOAD);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case "save":
                try {
                    Intent i = profileActions.saveProfile(PresenterMode.this,preferences,storageAccess);
                    this.startActivityForResult(i, StaticVariables.REQUEST_PROFILE_SAVE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class PrepareOptionMenu extends AsyncTask<Object, Void, String> {

        public void onPreExecute() {
            try {
                optionmenu = findViewById(R.id.optionmenu);
                optionmenu.removeAllViews();
                optionmenu.addView(OptionMenuListeners.prepareOptionMenu(PresenterMode.this, getSupportFragmentManager()));
                if (optionmenu != null) {
                    OptionMenuListeners.optionListeners(optionmenu, PresenterMode.this, preferences, storageAccess);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Object... objects) {
            // Get the current set list
            try {
                setActions.prepareSetList(PresenterMode.this,preferences);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        boolean cancelled = false;

        @Override
        protected void onCancelled() {
            cancelled = true;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                if (!cancelled) {
                    if (firstrun_option) {
                        openMyDrawers("option");
                        closeMyDrawers("option_delayed");
                        firstrun_option = false;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            OptionMenuListeners.updateMenuVersionNumber(PresenterMode.this, (TextView) findViewById(R.id.menu_version_bottom));
        }

    }
    @Override
    public void doEdit() {
        if (FullscreenActivity.isPDF) {
            FullscreenActivity.whattodo = "extractPDF";
            openFragment();
        } else if (FullscreenActivity.isSong) {
            FullscreenActivity.whattodo = "editsong";
            openFragment();
        }
    }
    private boolean justSong(Context c) {
        boolean isallowed = true;
        if (FullscreenActivity.isImage || FullscreenActivity.isPDF || !FullscreenActivity.isSong) {
            showToastMessage(c.getResources().getString(R.string.not_allowed));
            isallowed = false;
        }
        return isallowed;
    }
    @Override
    public void openFragment() {
        // Load the whichSongFolder in case we were browsing elsewhere
        StaticVariables.whichSongFolder = preferences.getMyPreferenceString(PresenterMode.this,
                "whichSongFolder",getString(R.string.mainfoldername));

        // Initialise the newFragment
        newFragment = OpenFragment.openFragment(PresenterMode.this);
        String message = OpenFragment.getMessage(PresenterMode.this);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(newFragment,message);


        if (newFragment != null && !this.isFinishing()) {
            try {
                ft.commitAllowingStateLoss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onSongImportDone() {
        rebuildSearchIndex();
    }
    @Override
    public void shareSong() {
        if (justSong(PresenterMode.this)) {
            // Export - Take a screenshot as a bitmap
            doCancelAsyncTask(sharesong_async);
            sharesong_async = new ShareSong();
            try {
                sharesong_async.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void loadCustomReusable() {
        doCancelAsyncTask(load_customreusable);
        load_customreusable = new LoadCustomReusable();
        try {
            load_customreusable.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void shareActivityLog() {
        doCancelAsyncTask(shareactivitylog_async);
        shareactivitylog_async = new ShareActivityLog();
        try {
            shareactivitylog_async.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class ShareSong extends AsyncTask<Object, Void, String> {
        @Override
        protected String doInBackground(Object... objects) {
            // Send this off to be processed and sent via an intent
            try {
                String title = getString(R.string.exportcurrentsong);
                Intent emailIntent = exportPreparer.exportSong(PresenterMode.this, preferences,
                        FullscreenActivity.bmScreen, storageAccess, processSong);
                Intent chooser = Intent.createChooser(emailIntent, title);
                startActivity(chooser);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }
    private void shareSet() {
        doCancelAsyncTask(shareset_async);
        shareset_async = new ShareSet();
        try {
            shareset_async.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class ShareActivityLog extends AsyncTask<Object, Void, String> {
        @Override
        protected String doInBackground(Object... objects) {
            // Send this off to be processed and sent via an intent
            try {
                Intent emailIntent = exportPreparer.exportActivityLog(PresenterMode.this, preferences, storageAccess);
                startActivityForResult(Intent.createChooser(emailIntent, "ActivityLog.xml"), 2222);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    @Override
    public void doExport() {
        // This is called after the user has specified what should be exported.
        switch (FullscreenActivity.whattodo) {
            case "customise_exportsong":
                shareSong();
                break;
            case "ccli_export":
                shareActivityLog();
                break;
            default:
                shareSet();
                break;
        }
    }
    @Override
    public void showToastMessage(String message) {
        if (message != null && !message.isEmpty()) {
            StaticVariables.myToastMessage = message;
            ShowToast.showToast(PresenterMode.this);
        }
    }
    @Override
    public void shuffleSongsInSet() {
        setActions.indexSongInSet();
        fixSet();
        newFragment = PopUpSetViewNew.newInstance();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(newFragment,"dialog");

        if (newFragment != null && !PresenterMode.this.isFinishing()) {
            try {
                ft.commitAllowingStateLoss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //newFragment = PopUpSetViewNew.newInstance();
        //newFragment.show(getSupportFragmentManager(), "dialog");
    }
    @Override
    public void splashScreen() {
        Intent intent = new Intent();
        intent.putExtra("showsplash",true);
        intent.setClass(PresenterMode.this, BootUpCheck.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void toggleDrawerSwipe() {
        if (preferences.getMyPreferenceBoolean(PresenterMode.this,"swipeForMenus",true)) {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } else {
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
        closeMyDrawers("both");
    }

    @Override
    public boolean onQueryTextSubmit(String newText) {
        // TODO Not sure if this does anything as FullscreenActivity.sva is never assigned anything!
        SearchViewItems item = (SearchViewItems) FullscreenActivity.sva.getItem(0);
        StaticVariables.songfilename = item.getFilename();
        StaticVariables.whichSongFolder = item.getFolder();
        StaticVariables.setView = false;
        StaticVariables.myToastMessage = StaticVariables.songfilename;
        loadSong();
        return true;
    }

    @SuppressLint("StaticFieldLeak")
    private class ShareSet extends AsyncTask<Object, Void, String> {
        @Override
        protected String doInBackground(Object... objects) {
            // Send this off to be processed and sent via an intent
            try {
                String title = getString(R.string.exportsavedset);
                ExportPreparer exportPreparer = new ExportPreparer();
                Intent emailIntent = exportPreparer.exportSet(PresenterMode.this, preferences, storageAccess);
                Intent chooser = Intent.createChooser(emailIntent, title);
                startActivity(chooser);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    @Override
    public void addSlideToSet() {
        doCancelAsyncTask(add_slidetoset);
        add_slidetoset = new AddSlideToSet();
        try {
            add_slidetoset.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class LoadCustomReusable extends AsyncTask<Object, Void, String> {

        @Override
        protected String doInBackground(Object... obj) {
            try {
                LoadXML.prepareLoadCustomReusable(PresenterMode.this, preferences, storageAccess,
                        processSong, FullscreenActivity.customreusabletoload);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        boolean cancelled = false;

        @Override
        protected void onCancelled() {
            cancelled = true;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                if (!cancelled) {
                    // This reopens the choose backgrounds popupFragment
                    // Initialise the newFragment
                    newFragment = PopUpCustomSlideFragment.newInstance();
                    String message = "dialog";
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.add(newFragment,message);

                    if (newFragment != null && !PresenterMode.this.isFinishing()) {
                        try {
                            ft.commitAllowingStateLoss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    // This reopens the choose backgrounds popupFragment
                    //newFragment = PopUpCustomSlideFragment.newInstance();
                    //newFragment.show(getSupportFragmentManager(), "dialog");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void removeSongFromSet(int val) {
        // Vibrate to let the user know something happened
        DoVibrate.vibrate(PresenterMode.this, 50);

        // Take away the menu item
        String tempSong = StaticVariables.mSetList[val];
        StaticVariables.mSetList[val] = "";

        StringBuilder sb = new StringBuilder();
        for (String aMSetList : StaticVariables.mSetList) {
            if (!aMSetList.isEmpty()) {
                sb.append("$**_").append(aMSetList).append("_**$");
            }
        }

        preferences.setMyPreferenceString(PresenterMode.this,"setCurrent",sb.toString());

        // Tell the user that the song has been removed.
        showToastMessage("\"" + tempSong + "\" "
                + getResources().getString(R.string.removedfromset));

        //Check to see if our set list is still valid
        setActions.prepareSetList(PresenterMode.this,preferences);
        prepareOptionMenu();
        fixSet();

        closeMyDrawers("option");
    }
    @Override
    public void changePDFPage(int page, String direction) {
        FullscreenActivity.whichDirection = direction;
        FullscreenActivity.pdfPageCurrent = page;
        if (presenter_song_buttonsListView.getChildCount()>page) {
            LinearLayout row = (LinearLayout) presenter_song_buttonsListView.getChildAt(page);
            Button thisbutton = (Button) row.getChildAt(1);
            thisbutton.performClick();
        }
    }
    @Override
    public void doDownload(String filename) {
        if (do_download!=null) {
            doCancelAsyncTask(do_download);
        }
        do_download = new DownloadTask(PresenterMode.this,filename);
        try {
            do_download.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMidi() {
        if ((Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M &&
                getPackageManager().hasSystemFeature(PackageManager.FEATURE_MIDI) &&
                preferences.getMyPreferenceBoolean(PresenterMode.this,"midiSendAuto",false) &&
                StaticVariables.midiDevice!=null &&
                StaticVariables.midiInputPort!=null && StaticVariables.mMidi!=null &&
                !StaticVariables.mMidi.isEmpty()) && !StaticVariables.mMidi.trim().equals("")) {
            // Declare the midi code
            Handler mh = new Handler();
            mh.post(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void run() {
                    try {
                        if (midi==null) {
                            midi = new Midi();
                        }
                        // Split the midi messages by line, after changing , into new line
                        StaticVariables.mMidi = StaticVariables.mMidi.replace(",", "\n");
                        StaticVariables.mMidi = StaticVariables.mMidi.replace("\n\n", "\n");
                        String[] midilines = StaticVariables.mMidi.trim().split("\n");
                        for (String ml : midilines) {
                            Log.d("d","Sending "+ml);
                            if (midi!=null) {
                                midi.sendMidi(midi.returnBytesFromHexText(ml));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    // The song index
    private void displayIndex(ArrayList<SongMenuViewItems> songMenuViewItems,
                              SongMenuAdapter songMenuAdapter) {
        LinearLayout indexLayout = findViewById(R.id.side_index);
        if (preferences.getMyPreferenceBoolean(PresenterMode.this,"songMenuAlphaIndexShow",true)) {
            indexLayout.setVisibility(View.VISIBLE);
        } else {
            indexLayout.setVisibility(View.GONE);
        }
        indexLayout.removeAllViews();
        TextView textView;
        final Map<String,Integer> map = songMenuAdapter.getAlphaIndex(PresenterMode.this,songMenuViewItems);
        Set<String> setString = map.keySet();
        List<String> indexList = new ArrayList<>(setString);
        for (String index : indexList) {
            textView = (TextView) View.inflate(PresenterMode.this,
                    R.layout.leftmenu, null);

            textView.setTextSize(preferences.getMyPreferenceFloat(PresenterMode.this,"songMenuAlphaIndexSize",14.0f));
            int i = (int) preferences.getMyPreferenceFloat(PresenterMode.this,"songMenuAlphaIndexSize",14.0f) *2;
            textView.setPadding(i,i,i,i);
            textView.setText(index);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView selectedIndex = (TextView) view;
                    try {
                        if (selectedIndex.getText() != null) {
                            String myval = selectedIndex.getText().toString();
                            Object obj = map.get(myval);
                            if (obj!=null) {
                                song_list_view.setSelection((int)obj);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            indexLayout.addView(textView);
        }
    }

    private void startCamera() {
        closeMyDrawers("option");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            Uri photoUri = getImageUri();
            Log.d("PresenterMode", "photoUri=" + photoUri);
            // Continue only if the File was successfully created
            if (photoUri != null) {
                try {
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(takePictureIntent, StaticVariables.REQUEST_CAMERA_CODE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Uri getImageUri() {
        try {
            // Create an image file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", StaticVariables.locale).format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );

            Uri imageUri = FileProvider.getUriForFile(PresenterMode.this, "OpenSongAppFiles", image);
            // Save a file: path for use with ACTION_VIEW intents
            FullscreenActivity.mCurrentPhotoPath = imageUri.toString();
            return imageUri;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public boolean onQueryTextChange(String newText) {
        // Replace unwanted symbols
        newText = processSong.removeUnwantedSymbolsAndSpaces(PresenterMode.this, preferences, newText);
        // TODO Not sure if this does anything as FullscreenActivity.sva is never assigned anything!
        if (FullscreenActivity.sva != null) {
            FullscreenActivity.sva.getFilter().filter(newText);
        }
        return false;
    }

    // The stuff to deal with the overall views
    @Override
    public void refreshAll() {
        // Clear the set and song section buttons
        presenter_set_buttonsListView.removeAllViews();
        presenter_song_buttonsListView.removeAllViews();
        presenter_lyrics.setText("");
        setupSetButtons();

        prepareSongMenu();
        prepareOptionMenu();

        // Load the song
        loadSong();
    }
    @Override
    public void closePopUps() {
        try {
            if (newFragment != null) {
                newFragment.dismiss();
            }
        } catch (Exception e) {
            // Oops
        }
    }
    @Override
    public void updatePresentationOrder() {
        doEdit();
    }

    // The stuff to deal with the slideshow
    private void prepareStopAutoSlideShow() {
        if (autoslideshowtask != null) {
            try {
                autoslideshowtask.cancel(true);
                autoslideshowtask = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        isplayingautoslideshow = false;
        startstopSlideShow.setImageResource(R.drawable.ic_play_white_36dp);
        enabledisableButton(startstopSlideShow, true);
    }
    private void prepareStartAutoSlideShow() {
        // Stop the slideshow if it already happening
        prepareStopAutoSlideShow();

        try {
            autoslidetime = Integer.parseInt(timeEditText.getText().toString());
        } catch (Exception e) {
            autoslidetime = 0;
        }
        autoslideloop = loopCheckBox.isChecked();

        if (autoslidetime > 0) {
            // Start asynctask that recalls every autoslidetime
            // Once we have reached the end of the slide group we either
            // Start again (if autoslideloop)
            // Or we exit autoslideshow
            projectButtonClick();
            isplayingautoslideshow = true;
            startstopSlideShow.setImageResource(R.drawable.ic_stop_white_36dp);
            doCancelAsyncTask(autoslideshowtask);
            autoslideshowtask = new AutoSlideShow();
            autoslideshowtask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            showToastMessage(getResources().getString(R.string.bad_time));
        }
        enabledisableButton(startstopSlideShow, true);

    }
    @SuppressLint("StaticFieldLeak")
    private class AutoSlideShow extends AsyncTask<Object, Void, String> {

        boolean cancelled = false;

        @Override
        protected void onCancelled() {
            cancelled = true;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if (!cancelled) {
                    // Check if we can move to the next section in the song
                    if (StaticVariables.currentSection < StaticVariables.songSections.length - 1 && isplayingautoslideshow) {
                        // Move to next song section
                        StaticVariables.currentSection++;
                        selectSectionButtonInSong(StaticVariables.currentSection);
                        prepareStopAutoSlideShow();
                        prepareStartAutoSlideShow();
                    } else if (autoslideloop && StaticVariables.currentSection >= (StaticVariables.songSections.length - 1) && isplayingautoslideshow) {
                        // Go back to first song section
                        StaticVariables.currentSection = 0;
                        selectSectionButtonInSong(StaticVariables.currentSection);
                        prepareStopAutoSlideShow();
                        prepareStartAutoSlideShow();
                    } else {
                        // Stop autoplay
                        prepareStopAutoSlideShow();
                    }
                }
            } catch (Exception e) {
                //  Oops
            }
        }

        @Override
        protected String doInBackground(Object... objects) {
            try {
                // Get clock time
                long start = System.currentTimeMillis();
                long end = start;
                while (end < (start + (autoslidetime * 1000)) && isplayingautoslideshow) {
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    end = System.currentTimeMillis();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class AddSlideToSet extends AsyncTask<Object, Void, String> {
        CustomSlide customSlide;

        @Override
        protected void onPreExecute() {
            customSlide = new CustomSlide();
        }

        @Override
        protected String doInBackground(Object... objects) {
            // Add the slide
            try {
                customSlide.addCustomSlide(PresenterMode.this, preferences);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        boolean cancelled = false;

        @Override
        protected void onCancelled() {
            cancelled = true;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                if (!cancelled) {
                    // Tell the user that the song has been added.
                    showToastMessage("\"" + FullscreenActivity.customslide_title + "\" " + getResources().getString(R.string.addedtoset));

                    // Vibrate to let the user know something happened
                    DoVibrate.vibrate(PresenterMode.this, 50);

                    //invalidateOptionsMenu();
                    prepareOptionMenu();
                    fixSet();
                    closeMyDrawers("option_delayed");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void logoButtonClick() {
        if (projectButton_isSelected) {
            projectButton_isSelected = false;
            unhighlightButtonClicked(presenter_project_group);
        }
        if (blankButton_isSelected) {
            blankButton_isSelected = false;
            unhighlightButtonClicked(presenter_blank_group);
        }

        logoButton_isSelected = !logoButton_isSelected;

        if (logoButton_isSelected) {
            // Fade in the logo after highlighting the button and disabling
            presenter_logo_group.setEnabled(false);
            highlightButtonClicked(presenter_logo_group);
            if (mSelectedDevice != null) {
                try {
                    PresentationService.ExternalDisplay.showLogo();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (FullscreenActivity.isHDMIConnected) {
                try {
                    PresentationServiceHDMI.showLogo();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            Handler h = new Handler();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    presenter_logo_group.setEnabled(true);
                }
            }, 800);
        } else {
            // Fade out the logo after unhighlighting the button and disabling
            presenter_logo_group.setEnabled(false);
            unhighlightButtonClicked(presenter_logo_group);
            if (mSelectedDevice != null) {
                try {
                    PresentationService.ExternalDisplay.hideLogo();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (FullscreenActivity.isHDMIConnected) {
                try {
                    PresentationServiceHDMI.hideLogo();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Handler h = new Handler();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    presenter_logo_group.setEnabled(true);
                }
            }, 800);
        }
    }
    private void blankButtonClick() {
        if (projectButton_isSelected) {
            projectButton_isSelected = false;
            unhighlightButtonClicked(presenter_project_group);
        }
        if (logoButton_isSelected) {
            logoButton_isSelected = false;
            unhighlightButtonClicked(presenter_logo_group);
        }

        blankButton_isSelected = !blankButton_isSelected;

        if (blankButton_isSelected) {
            // Fade out everything after highlighting the button and disabling
            presenter_blank_group.setEnabled(false);
            highlightButtonClicked(presenter_blank_group);
            if (mSelectedDevice != null) {
                try {
                    PresentationService.ExternalDisplay.blankUnblankDisplay(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (FullscreenActivity.isHDMIConnected) {
                try {
                    PresentationServiceHDMI.blankUnblankDisplay(false);
                } catch (Exception e) {/**/
                    e.printStackTrace();
                }

            }
            Handler h = new Handler();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    presenter_blank_group.setEnabled(true);
                }
            }, 800);
        } else {
            // Fade back everything after unhighlighting the button and disabling
            presenter_blank_group.setEnabled(false);
            unhighlightButtonClicked(presenter_blank_group);
            if (mSelectedDevice != null) {
                try {
                    PresentationService.ExternalDisplay.blankUnblankDisplay(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (FullscreenActivity.isHDMIConnected) {
                try {
                    PresentationServiceHDMI.blankUnblankDisplay(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            Handler h = new Handler();
            h.postDelayed(new Runnable() {
                @Override
                public void run() {
                    presenter_blank_group.setEnabled(true);
                }
            }, 800);
        }
    }
    private void alertButtonClick() {
        highlightButtonClicked(presenter_alert_group);
        FullscreenActivity.whattodo = "alert";
        openFragment();

        // After a short time, turn off the button
        Handler delay = new Handler();
        delay.postDelayed(new Runnable() {
            @Override
            public void run() {
                unhighlightButtonClicked(presenter_alert_group);
            }
        }, 500);
    }
    private void audioButtonClick() {
        highlightButtonClicked(presenter_audio_group);
        FullscreenActivity.whattodo = "presenter_audio";
        openFragment();

        // After a short time, turn off the button
        Handler delay = new Handler();
        delay.postDelayed(new Runnable() {
            @Override
            public void run() {
                unhighlightButtonClicked(presenter_audio_group);
            }
        }, 500);
    }
    private void dBButtonClick() {
        // Check audio record is allowed
        if (ActivityCompat.checkSelfPermission(PresenterMode.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
                Snackbar.make(mLayout, R.string.microphone_rationale, Snackbar.LENGTH_INDEFINITE).setAction(R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ActivityCompat.requestPermissions(PresenterMode.this, new String[]{Manifest.permission.RECORD_AUDIO}, StaticVariables.REQUEST_MICROPHONE_CODE);
                    }
                }).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},
                        StaticVariables.REQUEST_MICROPHONE_CODE);
            }

        } else {
            highlightButtonClicked(presenter_dB_group);
            FullscreenActivity.whattodo = "presenter_db";
            openFragment();

            // After a short time, turn off the button
            Handler delay = new Handler();
            delay.postDelayed(new Runnable() {
                @Override
                public void run() {
                    unhighlightButtonClicked(presenter_dB_group);
                }
            }, 500);
        }
    }

    // Highlight or unhighlight the presenter (col 3) buttons
    private void highlightButtonClicked(View v) {
        v.setBackground(ContextCompat.getDrawable(PresenterMode.this, R.drawable.presenter_box_blue_active));
    }
    private void unhighlightButtonClicked(View v) {
        v.setBackground(null);
    }

    // Enable or disable the buttons in the final column
    private void noSecondScreen() {
        unhighlightButtonClicked(presenter_project_group);
        unhighlightButtonClicked(presenter_logo_group);
        unhighlightButtonClicked(presenter_blank_group);
        unhighlightButtonClicked(presenter_alert_group);
        unhighlightButtonClicked(presenter_audio_group);
        unhighlightButtonClicked(presenter_dB_group);
        unhighlightButtonClicked(presenter_slide_group);
        unhighlightButtonClicked(presenter_scripture_group);
        unhighlightButtonClicked(presenter_display_group);
        presenter_project_group.setEnabled(true);
        presenter_logo_group.setEnabled(true);
        presenter_blank_group.setEnabled(true);
        presenter_alert_group.setEnabled(true);
        presenter_audio_group.setEnabled(true);
        presenter_dB_group.setEnabled(true);
        presenter_slide_group.setEnabled(true);
        presenter_scripture_group.setEnabled(true);
        presenter_display_group.setEnabled((true));
        projectButton_isSelected = false;
        logoButton_isSelected = false;
        blankButton_isSelected = false;
    }
    private void isSecondScreen() {
        presenter_project_group.setEnabled(true);
        presenter_logo_group.setEnabled(true);
        presenter_blank_group.setEnabled(true);
        presenter_alert_group.setEnabled(true);
        presenter_audio_group.setEnabled(true);
        presenter_dB_group.setEnabled(true);
        presenter_slide_group.setEnabled(true);
        presenter_scripture_group.setEnabled(true);
        presenter_display_group.setEnabled(true);
        projectButton_isSelected = false;
        logoButton_isSelected = false;
        blankButton_isSelected = false;
    }

    @Override
    public void allowPDFEditViaExternal() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("application/pdf");
        // Always use string resources for UI text.
        String title = getResources().getString(R.string.export_pdf);
        // Create intent to show chooser
        Intent chooser = Intent.createChooser(intent, title);

        // Verify the intent will resolve to at least one activity
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(chooser, StaticVariables.REQUEST_PDF_CODE);
        }
    }

    // The camera permissions and stuff
    @Override
    public void useCamera() {
        if (ContextCompat.checkSelfPermission(PresenterMode.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(PresenterMode.this, new String[]{Manifest.permission.CAMERA},
                    StaticVariables.REQUEST_CAMERA_CODE);
        } else {
            startCamera();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        // If request is cancelled, the result arrays are empty.
        if (requestCode == StaticVariables.REQUEST_CAMERA_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // Success, go for it
                startCamera();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class LoadSong extends AsyncTask<Object, Void, String> {

        @Override
        protected String doInBackground(Object... objects) {
            try {
                Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
                // Load up the song
                try {
                    LoadXML.loadXML(PresenterMode.this, preferences, storageAccess, processSong);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Clear the old headings (presention order looks for these)
                FullscreenActivity.foundSongSections_heading = new ArrayList<>();

                // Don't process images or image slide details here.  No need.  Only do this for songs
                if (FullscreenActivity.isPDF) {
                    LoadXML.getPDFPageCount(PresenterMode.this, preferences, storageAccess);
                } else if (FullscreenActivity.isSong || FullscreenActivity.isSlide || FullscreenActivity.isScripture) {

                    // 1. Sort multiline verse/chord formats
                    FullscreenActivity.myLyrics = processSong.fixMultiLineFormat(PresenterMode.this, preferences,FullscreenActivity.myLyrics);
                    // 2. Split the song into sections
                    StaticVariables.songSections = processSong.splitSongIntoSections(PresenterMode.this, preferences, FullscreenActivity.myLyrics);

                    // 3. Put the song into presentation order if required
                    if (preferences.getMyPreferenceBoolean(PresenterMode.this,"usePresentationOrder",false) &&
                            !StaticVariables.mPresentation.isEmpty()) {
                        StaticVariables.songSections = processSong.matchPresentationOrder(PresenterMode.this, preferences, StaticVariables.songSections);
                    }

                    // 3b Add extra sections for double linebreaks and || code
                    StaticVariables.songSections = processSong.splitLaterSplits(PresenterMode.this,preferences,StaticVariables.songSections);

                    // 4. Get the section headings/types (may have changed after presentationorder
                    StaticVariables.songSectionsLabels = new String[StaticVariables.songSections.length];
                    StaticVariables.songSectionsTypes = new String[StaticVariables.songSections.length];
                    for (int sl = 0; sl < StaticVariables.songSections.length; sl++) {
                        StaticVariables.songSectionsLabels[sl] = processSong.getSectionHeadings(StaticVariables.songSections[sl]);
                        if (!FullscreenActivity.foundSongSections_heading.contains(StaticVariables.songSectionsLabels[sl])) {
                            FullscreenActivity.foundSongSections_heading.add(StaticVariables.songSectionsLabels[sl]);
                        }
                    }

                    // 5. Get rid of the tag/heading lines
                    StaticVariables.songSections = processSong.removeTagLines(StaticVariables.songSections);

                    // If we are a host then rebuild
                    if (FullscreenActivity.network != null && FullscreenActivity.network.isRunningAsHost) {
                        PopUpEditSongFragment.prepareSongXML();
                        // Replace the lyrics
                        FullscreenActivity.presenterSendSong = FullscreenActivity.myXML.replace(StaticVariables.mLyrics,
                                processSong.rebuildParsedLyrics(StaticVariables.songSections.length));
                    } else {
                        FullscreenActivity.presenterSendSong = FullscreenActivity.myXML;
                    }

                    // Now that we have generated the song to send to a guest device, decide if we should remove chords, comments, etc.
                    for (int i = 0; i < StaticVariables.songSections.length; i++) {
                        StaticVariables.songSections[i] = processSong.removeUnderScores(PresenterMode.this,
                                preferences, StaticVariables.songSections[i]);
                        if (!preferences.getMyPreferenceBoolean(PresenterMode.this,"presoShowChords",false)) {
                            StaticVariables.songSections[i] = processSong.removeChordLines(StaticVariables.songSections[i]);
                        }
                        if (FullscreenActivity.network == null || !FullscreenActivity.network.isRunningAsHost) {
                            StaticVariables.songSections[i] = processSong.removeCommentLines(StaticVariables.songSections[i]);
                        }
                        StaticVariables.songSections[i] = processSong.removeUnderScores(PresenterMode.this,
                                preferences, StaticVariables.songSections[i]);
                    }

                    // We need to split each section into string arrays by line
                    StaticVariables.sectionContents = new String[StaticVariables.songSections.length][];
                    StaticVariables.projectedContents = new String[StaticVariables.songSections.length][];
                    for (int x = 0; x < StaticVariables.songSections.length; x++) {

                        StaticVariables.sectionContents[x] = StaticVariables.songSections[x].split("\n");
                        StaticVariables.projectedContents[x] = StaticVariables.songSections[x].split("\n");
                    }

                    // Determine what each line type is
                    // Copy the array of sectionContents into sectionLineTypes
                    // Then we'll replace the content with the line type
                    // This keeps the array sizes the same simply
                    StaticVariables.sectionLineTypes = new String[StaticVariables.songSections.length][];
                    StaticVariables.projectedLineTypes = new String[StaticVariables.songSections.length][];


                    for (int x = 0; x < StaticVariables.sectionLineTypes.length; x++) {
                        StaticVariables.sectionLineTypes[x] = new String[StaticVariables.sectionContents[x].length];
                        for (int y = 0; y < StaticVariables.sectionLineTypes[x].length; y++) {
                            StaticVariables.sectionLineTypes[x][y] = processSong.determineLineTypes(StaticVariables.sectionContents[x][y], PresenterMode.this);
                            if (StaticVariables.sectionContents[x][y] != null &&
                                    StaticVariables.sectionContents[x][y].length() > 0 && (StaticVariables.sectionContents[x][y].indexOf(" ") == 0 ||
                                    StaticVariables.sectionContents[x][y].indexOf(".") == 0 || StaticVariables.sectionContents[x][y].indexOf(";") == 0)) {
                                StaticVariables.sectionContents[x][y] = StaticVariables.sectionContents[x][y].substring(1);
                            }
                        }
                    }

                    for (int x = 0; x < StaticVariables.projectedLineTypes.length; x++) {
                        StaticVariables.projectedLineTypes[x] = new String[StaticVariables.projectedContents[x].length];
                        for (int y = 0; y < StaticVariables.projectedLineTypes[x].length; y++) {
                            StaticVariables.projectedLineTypes[x][y] = processSong.determineLineTypes(StaticVariables.projectedContents[x][y], PresenterMode.this);

                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "done";
        }

        boolean cancelled = false;

        @Override
        protected void onCancelled() {
            FullscreenActivity.alreadyloading = false;
            cancelled = true;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                if (!cancelled) {
                    // Now, reset the orientation.
                    FullscreenActivity.orientationchanged = false;

                    // Get the current orientation
                    FullscreenActivity.mScreenOrientation = getResources().getConfiguration().orientation;

                    setActions.indexSongInSet();
                    if (!StaticVariables.setView) {
                        // Unhighlight the set buttons
                        unhighlightAllSetButtons();
                    }
                    showCorrectViews();
                    setupSongButtons();
                    findSongInFolders();


                    // Send WiFiP2P intent
                    if (FullscreenActivity.network != null && FullscreenActivity.network.isRunningAsHost) {
                        try {
                            sendSongXMLToConnected();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    // Send the midi data if we can
                    sendMidi();

                    // If the user has shown the 'Welcome to OpenSongApp' file, and their song lists are empty,
                    // open the find new songs menu
                    if (StaticVariables.mTitle.equals("Welcome to OpenSongApp") &&
                            sqLiteHelper.getSongsCount(PresenterMode.this)<1) {
                        StaticVariables.whichOptionMenu = "FIND";
                        prepareOptionMenu();
                        Handler find = new Handler();
                        find.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                openMyDrawers("option");
                            }
                        }, 2000);
                    }
                    // If we have created, or converted a song format (e.g from OnSong or ChordPro), rebuild the database
                    // or pull up the edit screen
                    if (FullscreenActivity.needtoeditsong) {
                        FullscreenActivity.whattodo = "editsong";
                        FullscreenActivity.alreadyloading = false;
                        FullscreenActivity.needtorefreshsongmenu = true;
                    } else if (FullscreenActivity.needtorefreshsongmenu) {
                        FullscreenActivity.needtorefreshsongmenu = false;
                        rebuildSearchIndex();
                    }

                    // Get the SQLite stuff if the song exists.  Otherwise throws an exception (which is ok)
                    if (!StaticVariables.whichSongFolder.startsWith("..")) {
                        String songId = StaticVariables.whichSongFolder + "/" + StaticVariables.songfilename;
                        sqLite = sqLiteHelper.getSong(PresenterMode.this, songId);

                        // If this song isn't indexed, set its details
                        if (sqLite.getLyrics()==null || sqLite.getLyrics().equals("")) {
                            sqLite = sqLiteHelper.setSong(sqLite);
                            sqLiteHelper.updateSong(PresenterMode.this,sqLite);
                        }
                    } else {
                        // Not a song in the database (likley a variation, slide, etc.)
                        sqLite.setSongid("");
                        sqLite.setId(0);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            FullscreenActivity.alreadyloading = false;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class PrepareSongMenu extends AsyncTask<Object, Void, String> {

        ArrayList<SQLite> songsInFolder;

        @Override
        protected void onPreExecute() {
            menuCount_TextView.setText("");
            menuCount_TextView.setVisibility(View.GONE);
            menuFolder_TextView.setText(getString(R.string.wait));
            song_list_view.setAdapter(null);
            LinearLayout indexLayout = findViewById(R.id.side_index);
            indexLayout.removeAllViews();
        }

        @Override
        protected String doInBackground(Object... params) {
            try {
                // Get a list of the songs in the current folder
                // TODO
                songsInFolder = sqLiteHelper.getSongsInFolder(PresenterMode.this, StaticVariables.whichSongFolder);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        boolean cancelled = false;

        @Override
        protected void onCancelled() {
            cancelled = true;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                if (!cancelled) {
                    // Set the name of the current folder
                    menuFolder_TextView.setText(StaticVariables.whichSongFolder);

                    // Go through the found songs in folder and prepare the menu
                    ArrayList<SongMenuViewItems> songmenulist = new ArrayList<>();

                    for (int i=0; i<songsInFolder.size(); i++) {
                        String foundsongfilename = songsInFolder.get(i).getFilename();
                        String foundsongauthor = songsInFolder.get(i).getAuthor();
                        String foundsongkey = songsInFolder.get(i).getKey();

                        if (foundsongfilename == null) {
                            foundsongfilename = getString(R.string.error);
                        }
                        if (foundsongauthor == null) {
                            foundsongauthor = "";
                        }
                        if (foundsongkey == null) {
                            foundsongkey = "";
                        }

                        String whattolookfor; // not going to find this by accident...
                        whattolookfor = setActions.whatToLookFor(PresenterMode.this, StaticVariables.whichSongFolder, foundsongfilename);

                        boolean isinset = preferences.getMyPreferenceString(PresenterMode.this,"setCurrent","").contains(whattolookfor);

                        SongMenuViewItems song = new SongMenuViewItems(foundsongfilename,
                                foundsongfilename, foundsongauthor, foundsongkey, isinset);
                        songmenulist.add(song);
                    }

                    SongMenuAdapter lva = new SongMenuAdapter(PresenterMode.this, preferences, songmenulist);
                    song_list_view.setAdapter(lva);
                    song_list_view.setFastScrollEnabled(true);
                    song_list_view.setScrollingCacheEnabled(true);
                    lva.notifyDataSetChanged();

                    // Set the secondary alphabetical side bar
                    displayIndex(songmenulist, lva);

                    // Flick the song drawer open on8ce it is ready
                    findSongInFolders();
                    if (firstrun_song) {
                        openMyDrawers("song");
                        closeMyDrawers("song_delayed");
                        firstrun_song = false;
                    }

                    String menusize = "" + (songmenulist.size());
                    if (menuCount_TextView != null) {
                        menuCount_TextView.setText(menusize);
                        menuCount_TextView.setVisibility(View.VISIBLE);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class DoMoveInSet extends AsyncTask<Object, Void, String> {

        @Override
        protected String doInBackground(Object... objects) {
            // Get the appropriate song
            try {
                FullscreenActivity.linkclicked = StaticVariables.mSetList[StaticVariables.indexSongInSet];
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        boolean cancelled = false;

        @Override
        protected void onCancelled() {
            cancelled = true;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                if (!cancelled) {
                    // Get the next set positions and song
                    FullscreenActivity.linkclicked = StaticVariables.mSetList[StaticVariables.indexSongInSet];
                    StaticVariables.whatsongforsetwork = FullscreenActivity.linkclicked;
                    StaticVariables.setMoveDirection = ""; // Expects back or forward for Stage/Performance, but not here
                    setActions.doMoveInSet(PresenterMode.this);

                    // Set indexSongInSet position has moved
                    //invalidateOptionsMenu();

                    // Click the item in the set list
                    if (presenter_set_buttonsListView.getChildAt(StaticVariables.indexSongInSet) != null) {
                        presenter_set_buttonsListView.getChildAt(StaticVariables.indexSongInSet).performClick();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // The stuff to deal with the second screen
    @Override
    public void connectHDMI() {
        mMediaRouter.addCallback(mMediaRouteSelector, mMediaRouterCallback,
                    MediaRouter.CALLBACK_FLAG_REQUEST_DISCOVERY);
        updateDisplays();
    }

    @SuppressLint("NewApi")
    private class MyMediaRouterCallback extends MediaRouter.Callback {

        @Override
        public void onRouteSelected(MediaRouter router, MediaRouter.RouteInfo info) {
            mSelectedDevice = CastDevice.getFromBundle(info.getExtras());
            isSecondScreen();
            logoButton_isSelected = true;
            highlightButtonClicked(presenter_logo_group);
            updateDisplays();
        }

        @Override
        public void onRouteUnselected(MediaRouter router, MediaRouter.RouteInfo info) {
            teardown();
            mSelectedDevice = null;
            FullscreenActivity.isPresenting = false;
            FullscreenActivity.isHDMIConnected = false;
        }

        void teardown() {
            try {
                CastRemoteDisplayLocalService.stopService();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (hdmi!=null) {
                    hdmi.dismiss();
                }
            } catch (Exception e) {
                // Ooops
                e.printStackTrace();
            }
            logoButton_isSelected = false;
            noSecondScreen();
        }

        @Override
        public void onRouteAdded(MediaRouter mediaRouter, MediaRouter.RouteInfo routeInfo) {
        }

        @Override
        public void onRouteRemoved(MediaRouter mediaRouter, MediaRouter.RouteInfo routeInfo) {
        }

        @Override
        public void onRouteChanged(MediaRouter mediaRouter, MediaRouter.RouteInfo routeInfo) {
        }

        @Override
        public void onRouteVolumeChanged(MediaRouter mediaRouter, MediaRouter.RouteInfo routeInfo) {
        }
    }
    private void updateDisplays() {
        // This is called when display devices are changed (connected, disconnected, etc.)
        StaticVariables.activity = PresenterMode.this;

        Intent intent = new Intent(PresenterMode.this,
                PresenterMode.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(
                PresenterMode.this, 0, intent, 0);

        CastRemoteDisplayLocalService.NotificationSettings settings =
                new CastRemoteDisplayLocalService.NotificationSettings.Builder()
                        .setNotificationPendingIntent(notificationPendingIntent).build();

        if (mSelectedDevice!=null) {
            CastRemoteDisplayLocalService.startService(
                    getApplicationContext(),
                    PresentationService.class, getString(R.string.app_id),
                    mSelectedDevice, settings,
                    new CastRemoteDisplayLocalService.Callbacks() {
                        @Override
                        public void onServiceCreated(
                                CastRemoteDisplayLocalService service) {
                        }

                        @Override
                        public void onRemoteDisplaySessionStarted(
                                CastRemoteDisplayLocalService service) {
                        }

                        @Override
                        public void onRemoteDisplaySessionError(Status status) {
                            Log.d("d","onRemoteDisplaySessionError status="+status);
                        }

                        @Override
                        public void onRemoteDisplaySessionEnded(CastRemoteDisplayLocalService castRemoteDisplayLocalService) {
                            Log.d("d","onRemoteDisplaySessionEnded");
                        }

                    });
        } else {
            // Might be a hdmi connection
            try {
                Display mDisplay = mMediaRouter.getSelectedRoute().getPresentationDisplay();
                if (mDisplay != null) {
                    hdmi = new PresentationServiceHDMI(PresenterMode.this, mDisplay, processSong, this);
                    hdmi.show();
                    isSecondScreen();
                    logoButton_isSelected = true;
                    highlightButtonClicked(presenter_logo_group);
                    FullscreenActivity.isHDMIConnected = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
               // Ooops
            }
        }
    }
    @Override
    public void refreshSecondaryDisplay(String which) {
        try {
            switch (which) {
                case "logo":
                    if (FullscreenActivity.isPresenting && !FullscreenActivity.isHDMIConnected) {
                        try {
                            PresentationService.ExternalDisplay.setUpLogo();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (FullscreenActivity.isHDMIConnected) {
                        try {
                            PresentationServiceHDMI.setUpLogo();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;

                case "all":
                case "chords":
                case "autoscale":
                case "maxfontsize":
                case "manualfontsize":
                default:
                    if (FullscreenActivity.isPresenting && !FullscreenActivity.isHDMIConnected) {
                        try {
                            PresentationService.ExternalDisplay.doUpdate();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (FullscreenActivity.isHDMIConnected) {
                        try {
                            PresentationServiceHDMI.doUpdate();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;

                case "info":
                    if (FullscreenActivity.isPresenting && !FullscreenActivity.isHDMIConnected) {
                        try {
                            PresentationService.ExternalDisplay.updateFonts();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (FullscreenActivity.isHDMIConnected) {
                        try {
                            PresentationServiceHDMI.updateFonts();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;

                case "backgrounds":
                    if (FullscreenActivity.isPresenting && !FullscreenActivity.isHDMIConnected) {
                        try {
                            PresentationService.ExternalDisplay.fixBackground();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (FullscreenActivity.isHDMIConnected) {
                        try {
                            PresentationServiceHDMI.fixBackground();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;

                case "margins":
                    if (FullscreenActivity.isPresenting && !FullscreenActivity.isHDMIConnected) {
                        try {
                            PresentationService.ExternalDisplay.changeMargins();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (FullscreenActivity.isHDMIConnected) {
                        try {
                            PresentationServiceHDMI.changeMargins();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void updateAlert(boolean ison) {
        if (FullscreenActivity.isPresenting && !FullscreenActivity.isHDMIConnected) {
            try {
                PresentationService.ExternalDisplay.updateAlert(ison);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (FullscreenActivity.isHDMIConnected) {
            try {
                PresentationServiceHDMI.updateAlert(ison);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Listeners for key or pedal presses
    @Override
    public void onBackPressed() {
        if (mp.isPlaying()) {
            // Stop the media player
            mp.stop();
            mp.reset();
            mpTitle = "";
        }

        if (mDrawerLayout.isDrawerOpen(songmenu)) {
            mDrawerLayout.closeDrawer(songmenu);
            return;
        }
        if (mDrawerLayout.isDrawerOpen(optionmenu)) {
            mDrawerLayout.closeDrawer(optionmenu);
            return;
        }

        String message = getResources().getString(R.string.exit);
        FullscreenActivity.whattodo = "exit";

        newFragment = PopUpAreYouSureFragment.newInstance(message);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(newFragment,message);

        if (newFragment != null && !PresenterMode.this.isFinishing()) {
            try {
                ft.commitAllowingStateLoss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //newFragment = PopUpAreYouSureFragment.newInstance(message);
        //newFragment.show(getSupportFragmentManager(), "dialog");
    }
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        // To stop repeated pressing too quickly, set a handler to wait for 500ms before reenabling
        if (event.getAction() == KeyEvent.ACTION_UP && pedalsenabled) {
            if (keyCode == preferences.getMyPreferenceInt(PresenterMode.this,"pedal1Code",21)) {
                doPedalAction(preferences.getMyPreferenceString(PresenterMode.this,"pedal1ShortPressAction","prev"));
            } else if (keyCode == preferences.getMyPreferenceInt(PresenterMode.this,"pedal2Code",22)) {
                doPedalAction(preferences.getMyPreferenceString(PresenterMode.this,"pedal2ShortPressAction","next"));
            } else if (keyCode == preferences.getMyPreferenceInt(PresenterMode.this,"pedal3Code",19)) {
                doPedalAction(preferences.getMyPreferenceString(PresenterMode.this,"pedal3ShortPressAction","prev"));
            } else if (keyCode == preferences.getMyPreferenceInt(PresenterMode.this,"pedal4Code",20)) {
                doPedalAction(preferences.getMyPreferenceString(PresenterMode.this,"pedal4ShortPressAction","next"));
            } else if (keyCode == preferences.getMyPreferenceInt(PresenterMode.this,"pedal5Code",92)) {
                doPedalAction(preferences.getMyPreferenceString(PresenterMode.this,"pedal15hortPressAction","prev"));
            } else if (keyCode == preferences.getMyPreferenceInt(PresenterMode.this,"pedal6Code",93)) {
                doPedalAction(preferences.getMyPreferenceString(PresenterMode.this,"pedal6ShortPressAction","next"));
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    private void doPedalAction(String action) {
        switch (action) {
            case "prev":
            case "down":
                pausePedalUse();
                tryClickPreviousSection();
                break;

            case "next":
            case "up":
                pausePedalUse();
                tryClickNextSection();
                break;
        }
    }
    private void pausePedalUse() {
        pedalsenabled = false;
        // Close both drawers
        closeMyDrawers("both");

        Handler reenablepedal = new Handler();
        reenablepedal.postDelayed(new Runnable() {
            @Override
            public void run() {
                pedalsenabled = true;
            }
        }, 500);
    }
    @Override
    public void gesture5() {
        // Stop or start autoscroll - Does nothing in presentation mode
    }
    @Override
    public void gesture6() {
        // Stop or start pad - Does nothing in presentation mode
    }
    @Override
    public void gesture7() {
        // Start or stop the metronome - Does nothing in presentation mode
    }


    // Page buttons not officially used in PresenterMode, although some features are
    @Override
    public void setUpPageButtonsColors() {
        // Not using page buttons as FABs on the screen, so do nothing
    }
    @Override
    public void pageButtonAlpha(String s) {
        // Do nothing as this override is for StageMode
    }
    @Override
    public void setupQuickLaunchButtons() {
        // Do nothing as this override is for StageMode
    }
    @Override
    public void groupPageButtons() {}

    // The pad - Not used in PresenterMode
    @Override
    public void preparePad() {}
    @Override
    public void killPad() {}
    @Override
    public void fadeoutPad() {}

    // Autoscroll - Not used in PresenterMode
    @Override
    public void stopAutoScroll() {}
    @Override
    public void startAutoScroll() {}
    @Override
    public void prepareLearnAutoScroll() {}

    // Metronome - Not used in PresenterMode
    @Override
    public void stopMetronome() {}

    @Override
    public void updateExtraInfoColorsAndSizes(String what) {}

    @Override
    public void takeScreenShot() {
        // Do nothing - this is only for Performance mode
    }
    @Override
    public void displayHighlight(boolean fromautoshow) {
        // Do nothing - this is only for Performance mode
    }

    @Override
    public void selectAFileUri(String s) {
        Intent intent = new Intent(this, FolderPicker.class);
        intent.putExtra("title", s);
        intent.putExtra("pickFiles", true);
        if (StaticVariables.uriTree!=null) {
            intent.putExtra("location", StaticVariables.uriTree.getPath());
        }
        startActivityForResult(intent, StaticVariables.REQUEST_FILE_CHOOSER);
    }
}