package com.garethevans.church.opensongtablet;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

public class PopUpLongSongPressFragment extends DialogFragment {

    static PopUpLongSongPressFragment newInstance() {
        PopUpLongSongPressFragment frag;
        frag = new PopUpLongSongPressFragment();
        return frag;
    }

    public interface MyInterface {
        void openFragment();
        void shareSong();
        void songLongClick();
    }

    private MyInterface mListener;

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

    private Preferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            this.dismiss();
        }
    }

    static void addtoSet(Context c, Preferences preferences) {

        // If the song is in .pro, .onsong, .txt format, tell the user to convert it first
        // This is done by viewing it (avoids issues with file extension renames)
        // Just in case users running older than lollipop, we don't want to open the file
        // In this case, store the current song as a string so we can go back to it
        if (StaticVariables.songfilename.toLowerCase(StaticVariables.locale).endsWith(".pro") ||
                StaticVariables.songfilename.toLowerCase(StaticVariables.locale).endsWith(".chopro") ||
                StaticVariables.songfilename.toLowerCase(StaticVariables.locale).endsWith(".cho") ||
                StaticVariables.songfilename.toLowerCase(StaticVariables.locale).endsWith(".chordpro") ||
                StaticVariables.songfilename.toLowerCase(StaticVariables.locale).endsWith(".onsong") ||
                StaticVariables.songfilename.toLowerCase(StaticVariables.locale).endsWith(".txt")) {

            // Don't add song yet, but tell the user
            StaticVariables.myToastMessage = c.getResources().getString(R.string.convert_song);
            ShowToast.showToast(c);

        } else if (StaticVariables.songfilename.toLowerCase(StaticVariables.locale).endsWith(".doc") ||
                StaticVariables.songfilename.toLowerCase(StaticVariables.locale).endsWith(".docx")) {
            // Don't add song yet, but tell the user it is unsupported
            StaticVariables.myToastMessage = c.getResources().getString(R.string.file_type_unknown);
            ShowToast.showToast(c);

        } else {
            if (preferences.getMyPreferenceBoolean(c,"ccliAutomaticLogging",false)) {
                // Now we need to get the song info quickly to log it correctly
                // as this might not be the song loaded
                String[] vals = LoadXML.getCCLILogInfo(c, preferences, StaticVariables.whichSongFolder, StaticVariables.songfilename);
                if (vals.length == 4 && vals[0] != null && vals[1] != null && vals[2] != null && vals[3] != null) {
                    PopUpCCLIFragment.addUsageEntryToLog(c, preferences, StaticVariables.whichSongFolder + "/" + StaticVariables.songfilename,
                            vals[0], vals[1], vals[2], vals[3], "6"); // Printed
                }
            }

            // Set the appropriate song filename
            if (StaticVariables.whichSongFolder.equals(c.getString(R.string.mainfoldername))) {
                StaticVariables.whatsongforsetwork = "$**_" + StaticVariables.songfilename + "_**$";
            } else {
                StaticVariables.whatsongforsetwork = "$**_" + StaticVariables.whichSongFolder + "/" + StaticVariables.songfilename + "_**$";
            }

            // Allow the song to be added, even if it is already there
            String val = preferences.getMyPreferenceString(c,"setCurrent","") + StaticVariables.whatsongforsetwork;
            preferences.setMyPreferenceString(c,"setCurrent",val);

            // Tell the user that the song has been added.
            StaticVariables.myToastMessage = "\"" + StaticVariables.songfilename + "\" " +
                    c.getResources().getString(R.string.addedtoset);
            ShowToast.showToast(c);
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        this.dismiss();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(true);

        View V = inflater.inflate(R.layout.popup_longsongpress, container, false);

        TextView title = V.findViewById(R.id.dialogtitle);
        title.setText(StaticVariables.songfilename);
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

        preferences = new Preferences();

        // Vibrate to let the user know something happened
        DoVibrate.vibrate(Objects.requireNonNull(getActivity()),50);

        // Initialise the views
        Button addSongToSet_Button = V.findViewById(R.id.addSongToSet_Button);
        Button deleteSong_Button = V.findViewById(R.id.deleteSong_Button);
        Button renameSong_Button = V.findViewById(R.id.renameSong_Button);
        Button duplicateSong_Button = V.findViewById(R.id.duplicateSong_Button);
        Button shareSong_Button = V.findViewById(R.id.shareSong_Button);
        Button editSong_Button = V.findViewById(R.id.editSong_Button);

        // Set up listeners for the buttons
        addSongToSet_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addtoSet(getActivity(), preferences);
                if (mListener!=null) {
                    mListener.songLongClick();
                }
                dismiss();
            }
        });
        deleteSong_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullscreenActivity.whattodo = "deletesong";
                if (mListener!=null) {
                    mListener.openFragment();
                }
                dismiss();
            }
        });
        shareSong_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullscreenActivity.whattodo = "exportsong";
                if (mListener!=null) {
                    mListener.shareSong();
                }
                dismiss();
            }
        });
        renameSong_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullscreenActivity.whattodo = "renamesong";
                if (mListener!=null) {
                    mListener.openFragment();
                }
                dismiss();
            }
        });
        duplicateSong_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullscreenActivity.whattodo = "duplicate";
                if (mListener!=null) {
                    mListener.openFragment();
                }
                dismiss();
            }
        });
        editSong_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullscreenActivity.whattodo = "editsong";
                if (mListener!=null) {
                    mListener.openFragment();
                }
                dismiss();
            }
        });

        PopUpSizeAndAlpha.decoratePopUp(getActivity(),getDialog(), preferences);

        return V;
    }
}