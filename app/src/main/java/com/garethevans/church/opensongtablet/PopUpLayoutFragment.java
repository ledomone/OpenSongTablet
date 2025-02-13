package com.garethevans.church.opensongtablet;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.widget.SwitchCompat;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;

import java.util.Objects;

public class PopUpLayoutFragment extends DialogFragment {

    static PopUpLayoutFragment newInstance() {
        PopUpLayoutFragment frag;
        frag = new PopUpLayoutFragment();
        return frag;
    }

    public interface MyInterface {
        void refreshSecondaryDisplay(String what);
    }

    private static MyInterface mListener;

    @Override
    @SuppressWarnings("deprecation")
    public void onAttach(Activity activity) {
        mListener = (MyInterface) activity;
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    private boolean firsttime = true;

    private SwitchCompat toggleChordsButton, toggleAutoScaleButton;
    private LinearLayout group_maxfontsize;
    private LinearLayout group_manualfontsize;
    private SeekBar setMaxFontSizeProgressBar, setFontSizeProgressBar, presoAlphaProgressBar,
            setXMarginProgressBar, setYMarginProgressBar, presoTitleSizeSeekBar,
            presoAuthorSizeSeekBar, presoCopyrightSizeSeekBar,
            presoAlertSizeSeekBar, presoTransitionTimeSeekBar;
    private TextView maxfontSizePreview;
    private TextView fontSizePreview;
    private TextView presoAlphaText;
    private TextView lyrics_title_align;
    private TextView presoTransitionTimeTextView;
    private FloatingActionButton lyrics_left_align, lyrics_center_align, lyrics_right_align,
            info_left_align, info_center_align, info_right_align;
    private ImageView chooseLogoButton, chooseImage1Button, chooseImage2Button, chooseVideo1Button,
            chooseVideo2Button;
    private CheckBox image1CheckBox, image2CheckBox, video1CheckBox, video2CheckBox;
    private StorageAccess storageAccess;
    private Preferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            this.dismiss();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(true);

        final View V = inflater.inflate(R.layout.popup_layout, container, false);

        TextView title = V.findViewById(R.id.dialogtitle);
        title.setText(Objects.requireNonNull(getActivity()).getResources().getString(R.string.connected_display));
        final FloatingActionButton closeMe = V.findViewById(R.id.closeMe);
        closeMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomAnimations.animateFAB(closeMe,getActivity());
                closeMe.setEnabled(false);
                dismiss();
            }
        });
        FloatingActionButton saveMe = V.findViewById(R.id.saveMe);
        saveMe.hide();

        storageAccess = new StorageAccess();
        preferences = new Preferences();
        SetTypeFace setTypeFace = new SetTypeFace();

        identifyViews(V);

        // Initialise the font handlers
        // Handlers for fonts
        Handler lyrichandler = new Handler();
        Handler chordhandler = new Handler();
        Handler presohandler = new Handler();
        Handler presoinfohandler = new Handler();
        Handler customhandler = new Handler();

        setTypeFace.setUpAppFonts(getActivity(), preferences, lyrichandler, chordhandler,
                presohandler, presoinfohandler, customhandler);

        prepareViews();

        setupListeners();

        // Make sure the logo we have is what is displayed (if we have set a new one)
        sendUpdateToScreen("logo");

        PopUpSizeAndAlpha.decoratePopUp(getActivity(),getDialog(), preferences);

        return V;
    }

    private void identifyViews(View V) {
        toggleChordsButton = V.findViewById(R.id.toggleChordsButton);
        toggleAutoScaleButton = V.findViewById(R.id.toggleAutoScaleButton);
        group_maxfontsize = V.findViewById(R.id.group_maxfontsize);
        setMaxFontSizeProgressBar = V.findViewById(R.id.setMaxFontSizeProgressBar);
        maxfontSizePreview = V.findViewById(R.id.maxfontSizePreview);
        group_manualfontsize = V.findViewById(R.id.group_manualfontsize);
        setFontSizeProgressBar = V.findViewById(R.id.setFontSizeProgressBar);
        fontSizePreview = V.findViewById(R.id.fontSizePreview);
        lyrics_title_align = V.findViewById(R.id.lyrics_title_align);
        lyrics_left_align = V.findViewById(R.id.lyrics_left_align);
        lyrics_center_align = V.findViewById(R.id.lyrics_center_align);
        lyrics_right_align = V.findViewById(R.id.lyrics_right_align);
        info_left_align = V.findViewById(R.id.info_left_align);
        info_center_align = V.findViewById(R.id.info_center_align);
        info_right_align = V.findViewById(R.id.info_right_align);
        presoTitleSizeSeekBar = V.findViewById(R.id.presoTitleSizeSeekBar);
        presoAuthorSizeSeekBar = V.findViewById(R.id.presoAuthorSizeSeekBar);
        presoCopyrightSizeSeekBar = V.findViewById(R.id.presoCopyrightSizeSeekBar);
        presoAlertSizeSeekBar = V.findViewById(R.id.presoAlertSizeSeekBar);
        presoTransitionTimeSeekBar = V.findViewById(R.id.presoTransitionTimeSeekBar);
        presoTransitionTimeTextView = V.findViewById(R.id.presoTransitionTimeTextView);
        presoAlphaProgressBar = V.findViewById(R.id.presoAlphaProgressBar);
        presoAlphaText = V.findViewById(R.id.presoAlphaText);
        chooseLogoButton = V.findViewById(R.id.chooseLogoButton);
        chooseImage1Button = V.findViewById(R.id.chooseImage1Button);
        chooseImage2Button = V.findViewById(R.id.chooseImage2Button);
        chooseVideo1Button = V.findViewById(R.id.chooseVideo1Button);
        chooseVideo2Button = V.findViewById(R.id.chooseVideo2Button);
        image1CheckBox = V.findViewById(R.id.image1CheckBox);
        image2CheckBox = V.findViewById(R.id.image2CheckBox);
        video1CheckBox = V.findViewById(R.id.video1CheckBox);
        video2CheckBox = V.findViewById(R.id.video2CheckBox);
        setXMarginProgressBar = V.findViewById(R.id.setXMarginProgressBar);
        setYMarginProgressBar = V.findViewById(R.id.setYMarginProgressBar);
        TextView modes_TextView = V.findViewById(R.id.modes_TextView);
        String s = getString(R.string.performancemode) + " " + getString(R.string.separator) + " " +
                getString(R.string.stagemode) +" " + getString(R.string.separator) + " " +
                getString (R.string.presentermode);
        modes_TextView.setText(s);
    }

    private void prepareViews() {
        // Set the stuff up to what it should be from preferences
        toggleChordsButton.setChecked(preferences.getMyPreferenceBoolean(getActivity(),"presoShowChords",false));
        toggleAutoScaleButton.setChecked(preferences.getMyPreferenceBoolean(getActivity(),"presoAutoscale",true));
        setMaxFontSizeProgressBar.setMax(70);
        setMaxFontSizeProgressBar.setProgress((int)preferences.getMyPreferenceFloat(getActivity(),"fontSizePresoMax",40.0f) - 4);
        maxfontSizePreview.setTypeface(StaticVariables.typefacePreso);
        setFontSizeProgressBar.setMax(70);
        setFontSizeProgressBar.setProgress((int)preferences.getMyPreferenceFloat(getActivity(),"fontSizePreso",14.0f) - 4);
        fontSizePreview.setTypeface(StaticVariables.typefacePreso);
        setUpAlignmentButtons();
        presoTitleSizeSeekBar.setMax(32);
        presoAuthorSizeSeekBar.setMax(32);
        presoCopyrightSizeSeekBar.setMax(32);
        presoAlertSizeSeekBar.setMax(32);
        presoTitleSizeSeekBar.setProgress((int)preferences.getMyPreferenceFloat(getActivity(),"presoTitleTextSize", 14.0f));
        presoAuthorSizeSeekBar.setProgress((int)preferences.getMyPreferenceFloat(getActivity(),"presoAuthorTextSize", 12.0f));
        presoCopyrightSizeSeekBar.setProgress((int)preferences.getMyPreferenceFloat(getActivity(),"presoCopyrightTextSize", 12.0f));
        presoAlertSizeSeekBar.setProgress((int)preferences.getMyPreferenceFloat(getActivity(),"presoAlertTextSize", 12.0f));
        float alphaval = preferences.getMyPreferenceFloat(getActivity(),"presoBackgroundAlpha",0.8f);
        presoAlphaProgressBar.setProgress((int)(alphaval*100.0f));
        String newtext = (int) (alphaval * 100.0f) + " %";
        presoAlphaText.setText(newtext);
        presoTransitionTimeSeekBar.setMax(23);
        presoTransitionTimeSeekBar.setProgress(timeToSeekBarProgress());
        presoTransitionTimeTextView.setText(SeekBarProgressToText());
        setupPreviews();
        setCheckBoxes();
        setXMarginProgressBar.setMax(50);
        setYMarginProgressBar.setMax(50);
        setXMarginProgressBar.setProgress(preferences.getMyPreferenceInt(getActivity(),"presoXMargin",20));
        setYMarginProgressBar.setProgress(preferences.getMyPreferenceInt(getActivity(),"presoXMargin",10));
    }

    private void setupListeners() {
        // Set listeners
        toggleChordsButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                preferences.setMyPreferenceBoolean(getActivity(),"presoShowChords",b);
                sendUpdateToScreen("chords");
                setUpAlignmentButtons();
            }
        });
        toggleAutoScaleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                preferences.setMyPreferenceBoolean(getActivity(),"presoAutoScale",b);
                showorhideView(group_maxfontsize,b);
                showorhideView(group_manualfontsize,!b);
                sendUpdateToScreen("autoscale");
            }
        });
        setMaxFontSizeProgressBar.setOnSeekBarChangeListener(new setMaxFontSizeListener());
        setFontSizeProgressBar.setOnSeekBarChangeListener(new setFontSizeListener());
        lyrics_left_align.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomAnimations.animateFAB(lyrics_left_align, getActivity());
                preferences.setMyPreferenceInt(getActivity(),"presoLyricsAlign",Gravity.START);
                setUpAlignmentButtons();
                sendUpdateToScreen("all");
            }
        });
        lyrics_center_align.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomAnimations.animateFAB(lyrics_center_align, getActivity());
                preferences.setMyPreferenceInt(getActivity(),"presoLyricsAlign",Gravity.CENTER);
                setUpAlignmentButtons();
                sendUpdateToScreen("all");
            }
        });
        lyrics_right_align.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomAnimations.animateFAB(lyrics_right_align, getActivity());
                preferences.setMyPreferenceInt(getActivity(),"presoLyricsAlign",Gravity.END);
                setUpAlignmentButtons();
                sendUpdateToScreen("all");
            }
        });
        info_left_align.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomAnimations.animateFAB(info_left_align, getActivity());
                preferences.setMyPreferenceInt(getActivity(),"presoInfoAlign",Gravity.START);
                setUpAlignmentButtons();
                sendUpdateToScreen("info");
            }
        });
        info_center_align.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomAnimations.animateFAB(info_center_align, getActivity());
                preferences.setMyPreferenceInt(getActivity(),"presoInfoAlign",Gravity.CENTER);
                setUpAlignmentButtons();
                sendUpdateToScreen("info");
            }
        });
        info_right_align.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomAnimations.animateFAB(info_right_align, getActivity());
                preferences.setMyPreferenceInt(getActivity(),"presoInfoAlign",Gravity.END);
                setUpAlignmentButtons();
                sendUpdateToScreen("info");
            }
        });
        presoTransitionTimeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                presoTransitionTimeTextView.setText(SeekBarProgressToText());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                preferences.setMyPreferenceInt(getActivity(),"presoTransitionTime",seekBarProgressToTime());
            }
        });
        presoTitleSizeSeekBar.setOnSeekBarChangeListener(new presoSectionSizeListener());
        presoAuthorSizeSeekBar.setOnSeekBarChangeListener(new presoSectionSizeListener());
        presoCopyrightSizeSeekBar.setOnSeekBarChangeListener(new presoSectionSizeListener());
        presoAlertSizeSeekBar.setOnSeekBarChangeListener(new presoSectionSizeListener());
        presoAlphaProgressBar.setOnSeekBarChangeListener(new presoAlphaListener());
        setXMarginProgressBar.setOnSeekBarChangeListener(new setMargin_Listener());
        setYMarginProgressBar.setOnSeekBarChangeListener(new setMargin_Listener());
        chooseLogoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open another popup listing the files to choose from
                PresenterMode.whatBackgroundLoaded = "logo";
                chooseFile("image/*",StaticVariables.REQUEST_CUSTOM_LOGO);
            }
        });
        chooseImage1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open another popup listing the files to choose from
                PresenterMode.whatBackgroundLoaded = "image1";
                chooseFile("image/*",StaticVariables.REQUEST_BACKGROUND_IMAGE1);
            }
        });
        chooseImage2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open another popup listing the files to choose from
                PresenterMode.whatBackgroundLoaded = "image2";
                chooseFile("image/*",StaticVariables.REQUEST_BACKGROUND_IMAGE2);
            }
        });
        chooseVideo1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open another popup listing the files to choose from
                PresenterMode.whatBackgroundLoaded = "video1";
                chooseFile("video/*",StaticVariables.REQUEST_BACKGROUND_VIDEO1);
            }
        });
        chooseVideo2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open another popup listing the files to choose from
                PresenterMode.whatBackgroundLoaded = "video2";
                chooseFile("video/*",StaticVariables.REQUEST_BACKGROUND_VIDEO2);
            }
        });
        image1CheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    image2CheckBox.setChecked(false);
                    video1CheckBox.setChecked(false);
                    video2CheckBox.setChecked(false);
                    PresenterMode.whatBackgroundLoaded = "image1";
                    preferences.setMyPreferenceString(getActivity(),"backgroundTypeToUse","image");
                    preferences.setMyPreferenceString(getActivity(),"backgroundToUse","img1");
                    sendUpdateToScreen("backgrounds");
                }
            }
        });
        image2CheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    image1CheckBox.setChecked(false);
                    video1CheckBox.setChecked(false);
                    video2CheckBox.setChecked(false);
                    PresenterMode.whatBackgroundLoaded = "image2";
                    preferences.setMyPreferenceString(getActivity(),"backgroundTypeToUse","image");
                    preferences.setMyPreferenceString(getActivity(),"backgroundToUse","img2");
                    sendUpdateToScreen("backgrounds");
                }
            }
        });
        video1CheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    image1CheckBox.setChecked(false);
                    image2CheckBox.setChecked(false);
                    video2CheckBox.setChecked(false);
                    PresenterMode.whatBackgroundLoaded = "video1";
                    preferences.setMyPreferenceString(getActivity(),"backgroundTypeToUse","video");
                    preferences.setMyPreferenceString(getActivity(),"backgroundToUse","vid1");
                    sendUpdateToScreen("backgrounds");
                }
            }
        });
        video2CheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    image1CheckBox.setChecked(false);
                    image2CheckBox.setChecked(false);
                    video1CheckBox.setChecked(false);
                    PresenterMode.whatBackgroundLoaded = "video2";
                    preferences.setMyPreferenceString(getActivity(),"backgroundTypeToUse","video");
                    preferences.setMyPreferenceString(getActivity(),"backgroundToUse","vid2");
                    sendUpdateToScreen("backgrounds");
                }
            }
        });
    }

    private int timeToSeekBarProgress() {
        // Min time is 200ms, max is 2000ms
        return (preferences.getMyPreferenceInt(getActivity(),"presoTransitionTime",800)/100) - 2;
    }

    private int seekBarProgressToTime() {
        return (presoTransitionTimeSeekBar.getProgress() + 2) * 100;
    }

    private String SeekBarProgressToText() {
        return (((float)presoTransitionTimeSeekBar.getProgress() + 2.0f)/10.0f) + " s";
    }

    private void setUpAlignmentButtons() {
        int lyralign = preferences.getMyPreferenceInt(getActivity(),"presoLyricsAlign",Gravity.CENTER);
        int infalign = preferences.getMyPreferenceInt(getActivity(),"presoInfoAlign",Gravity.END);
        if (lyralign == Gravity.START) {
            lyrics_left_align.setBackgroundTintList(ColorStateList.valueOf(0xffff0000));
            lyrics_center_align.setBackgroundTintList(ColorStateList.valueOf(0xff555555));
            lyrics_right_align.setBackgroundTintList(ColorStateList.valueOf(0xff555555));
        } else if (lyralign == Gravity.CENTER) {
            lyrics_left_align.setBackgroundTintList(ColorStateList.valueOf(0xff555555));
            lyrics_center_align.setBackgroundTintList(ColorStateList.valueOf(0xffff0000));
            lyrics_right_align.setBackgroundTintList(ColorStateList.valueOf(0xff555555));
        } else {
            lyrics_left_align.setBackgroundTintList(ColorStateList.valueOf(0xff555555));
            lyrics_center_align.setBackgroundTintList(ColorStateList.valueOf(0xff555555));
            lyrics_right_align.setBackgroundTintList(ColorStateList.valueOf(0xffff0000));
        }
        if (infalign == Gravity.START) {
            info_left_align.setBackgroundTintList(ColorStateList.valueOf(0xffff0000));
            info_center_align.setBackgroundTintList(ColorStateList.valueOf(0xff555555));
            info_right_align.setBackgroundTintList(ColorStateList.valueOf(0xff555555));
        } else if (infalign == Gravity.END) {
            info_left_align.setBackgroundTintList(ColorStateList.valueOf(0xff555555));
            info_center_align.setBackgroundTintList(ColorStateList.valueOf(0xff555555));
            info_right_align.setBackgroundTintList(ColorStateList.valueOf(0xffff0000));
        } else {
            info_left_align.setBackgroundTintList(ColorStateList.valueOf(0xff555555));
            info_center_align.setBackgroundTintList(ColorStateList.valueOf(0xffff0000));
            info_right_align.setBackgroundTintList(ColorStateList.valueOf(0xff555555));
        }

        // If chords are being show, hide the lyrics align, otherwise it is ok to show
        boolean vis = true;
        if (preferences.getMyPreferenceBoolean(getActivity(),"presoShowChords",false)) {
            vis = false;
        }
        setFABVis(lyrics_left_align,vis);
        setFABVis(lyrics_center_align,vis);
        setFABVis(lyrics_right_align,vis);
        setViewVis(lyrics_title_align,vis);
    }

    private void setFABVis(FloatingActionButton fav, boolean vis) {
        if (vis) {
            fav.show();
        } else {
            fav.hide();
        }
    }
    private void setViewVis(View v, boolean vis) {
        if (vis) {
            v.setVisibility(View.VISIBLE);
        } else {
            v.setVisibility(View.GONE);
        }
    }

    private void setCheckBoxes() {

        switch (preferences.getMyPreferenceString(getActivity(),"backgroundToUse","img1")) {
            case "img1":
                image1CheckBox.setChecked(true);
                image2CheckBox.setChecked(false);
                video1CheckBox.setChecked(false);
                video2CheckBox.setChecked(false);
                break;
            case "img2":
                image1CheckBox.setChecked(false);
                image2CheckBox.setChecked(true);
                video1CheckBox.setChecked(false);
                video2CheckBox.setChecked(false);
                break;
            case "vid1":
                image1CheckBox.setChecked(false);
                image2CheckBox.setChecked(false);
                video1CheckBox.setChecked(true);
                video2CheckBox.setChecked(false);
                break;
            case "vid2":
                image1CheckBox.setChecked(false);
                image2CheckBox.setChecked(false);
                video1CheckBox.setChecked(false);
                video2CheckBox.setChecked(true);
                break;
        }
    }

    private void chooseFile(String mimeType, int requestCode) {
        // This calls an intent to choose a file (image or video)
        Uri uri = storageAccess.getUriForItem(getActivity(),preferences,"Backgrounds","","");
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT,uri);
        if (mimeType.equals("image/*")) {
            intent.setDataAndType(uri,"image/*");
        } else {
            intent.setDataAndType(uri,"video/*");
        }
        startActivityForResult(intent, requestCode);
    }

    private void showorhideView(View v, boolean show) {
        if (show) {
            v.setVisibility(View.VISIBLE);
        } else {
            v.setVisibility(View.GONE);
        }
    }

    private void sendUpdateToScreen(String what) {
        if (!firsttime) {
            if (mListener != null) {
                mListener.refreshSecondaryDisplay(what);
            }
        } else {
            // We have just initialised the variables
            firsttime = false;
            if (mListener != null) {
                mListener.refreshSecondaryDisplay("logo");
            }
        }
    }

    private class setMargin_Listener implements SeekBar.OnSeekBarChangeListener {

        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) { }

        public void onStartTrackingTouch(SeekBar seekBar) {}

        public void onStopTrackingTouch(SeekBar seekBar) {
            preferences.setMyPreferenceInt(getActivity(),"presoXMargin",setXMarginProgressBar.getProgress());
            preferences.setMyPreferenceInt(getActivity(),"presoYMargin",setYMarginProgressBar.getProgress());
            sendUpdateToScreen("margins");
        }
    }
    private class setFontSizeListener implements SeekBar.OnSeekBarChangeListener {

        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            String newtext = (progress + 4) + " sp";
            fontSizePreview.setText(newtext);
            fontSizePreview.setTextSize(progress + 4);
        }

        public void onStartTrackingTouch(SeekBar seekBar) {}

        public void onStopTrackingTouch(SeekBar seekBar) {
            float val = seekBar.getProgress()+4.0f;
            preferences.setMyPreferenceFloat(getActivity(),"fontSizePreso",val);
            sendUpdateToScreen("manualfontsize");
        }
    }
    private class setMaxFontSizeListener implements SeekBar.OnSeekBarChangeListener {

        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            String newtext = (progress + 4) + " sp";
            maxfontSizePreview.setText(newtext);
            maxfontSizePreview.setTextSize(progress + 4);
        }

        public void onStartTrackingTouch(SeekBar seekBar) {}

        public void onStopTrackingTouch(SeekBar seekBar) {
            float val = seekBar.getProgress() + 4.0f;
            preferences.setMyPreferenceFloat(getActivity(),"fontSizePresoMax",val);
            sendUpdateToScreen("maxfontsize");
        }

    }
    private class presoSectionSizeListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) { }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            preferences.setMyPreferenceFloat(getActivity(),"presoTitleTextSize", (float)presoTitleSizeSeekBar.getProgress());
            preferences.setMyPreferenceFloat(getActivity(),"presoAuthorTextSize", (float)presoAuthorSizeSeekBar.getProgress());
            preferences.setMyPreferenceFloat(getActivity(),"presoCopyrightTextSize", (float)presoCopyrightSizeSeekBar.getProgress());
            preferences.setMyPreferenceFloat(getActivity(),"presoAlertTextSize", (float)presoAlertSizeSeekBar.getProgress());
            sendUpdateToScreen("info");
        }
    }
    private class presoAlphaListener implements SeekBar.OnSeekBarChangeListener {

        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            String update = progress + " %";
            presoAlphaText.setText(update);
        }

        public void onStartTrackingTouch(SeekBar seekBar) {}

        public void onStopTrackingTouch(SeekBar seekBar) {

            preferences.setMyPreferenceFloat(getActivity(),"presoBackgroundAlpha",((float)seekBar.getProgress() / 100f));
            sendUpdateToScreen("backgrounds");
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        this.dismiss();
    }

    private void setupPreviews() {
        // This sets the background thumbnails for the images/videos/logo
        Uri img1Uri = storageAccess.fixLocalisedUri(getActivity(),preferences,preferences.getMyPreferenceString(getActivity(),"backgroundImage1","ost_bg.png"));
        updatePreview(getActivity(),chooseImage1Button, img1Uri);
        Uri img2Uri = storageAccess.fixLocalisedUri(getActivity(),preferences,preferences.getMyPreferenceString(getActivity(),"backgroundImage2","ost_bg.png"));
        updatePreview(getActivity(),chooseImage2Button, img2Uri);
        Uri vid1Uri = storageAccess.fixLocalisedUri(getActivity(),preferences,preferences.getMyPreferenceString(getActivity(),"backgroundVideo1",""));
        updatePreview(getActivity(),chooseVideo1Button, vid1Uri);
        Uri vid2Uri = storageAccess.fixLocalisedUri(getActivity(),preferences,preferences.getMyPreferenceString(getActivity(),"backgroundVideo2",""));
        updatePreview(getActivity(),chooseVideo2Button, vid2Uri);
        Uri logoUri = storageAccess.fixLocalisedUri(getActivity(),preferences,preferences.getMyPreferenceString(getActivity(),"customLogo","ost_logo.png"));
        updatePreview(getActivity(),chooseLogoButton, logoUri);
    }

    private void updatePreview (Context c, ImageView view, Uri uri) {
        Log.d("PopUpLayout","updatePreview, uri="+uri);
        if (uri==null || !storageAccess.uriExists(getActivity(),uri)) {
            view.setBackgroundColor(0xff000000);
        } else {
            view.setBackgroundColor(0x00000000);
            RequestOptions myOptions = new RequestOptions().fitCenter().override(120, 90);
            GlideApp.with(c).load(uri).apply(myOptions).into(view);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        // This gets sent back here from the parent activity
        Log.d("PopUpLayout","onActivityResult triggered");
        Log.d("PopUpLayout","requestCode="+requestCode);
        if (resultData!=null && resultData.getData()!=null) {
            Uri uri = resultData.getData();
            if (getActivity()!=null && uri!=null) {
                Log.d("PopUpLayout","uri="+uri);
                String uriString = storageAccess.fixUriToLocal(uri);
                Log.d("PopUpLayout","uriString="+uriString);

                if (requestCode == StaticVariables.REQUEST_BACKGROUND_IMAGE1) {
                    preferences.setMyPreferenceString(getActivity(), "backgroundImage1", uriString);
                    updatePreview(getActivity(),chooseImage1Button,uri);
                    sendUpdateToScreen("backgrounds");

                } else if (requestCode == StaticVariables.REQUEST_BACKGROUND_IMAGE2) {
                    preferences.setMyPreferenceString(getActivity(), "backgroundImage2", uriString);
                    updatePreview(getActivity(),chooseImage2Button,uri);
                    sendUpdateToScreen("backgrounds");

                } else if (requestCode == StaticVariables.REQUEST_BACKGROUND_VIDEO1) {
                    preferences.setMyPreferenceString(getActivity(), "backgroundVideo1", uriString);
                    updatePreview(getActivity(),chooseVideo1Button,uri);
                    sendUpdateToScreen("backgrounds");

                } else if (requestCode == StaticVariables.REQUEST_BACKGROUND_VIDEO2) {
                    preferences.setMyPreferenceString(getActivity(), "backgroundVideo2", uriString);
                    updatePreview(getActivity(),chooseVideo2Button,uri);
                    sendUpdateToScreen("backgrounds");

                } else if (requestCode == StaticVariables.REQUEST_CUSTOM_LOGO) {
                    preferences.setMyPreferenceString(getActivity(), "customLogo", uriString);
                    updatePreview(getActivity(),chooseLogoButton,uri);
                    sendUpdateToScreen("logo");
                }
            }
        }
    }

}