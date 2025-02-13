package com.garethevans.church.opensongtablet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.peak.salut.Callbacks.SalutCallback;
import com.peak.salut.Callbacks.SalutDeviceCallback;
import com.peak.salut.SalutDevice;

public class OptionMenuListeners extends AppCompatActivity {

    public interface MyInterface {
        void openFragment();
        void openMyDrawers(String which);
        void closeMyDrawers(String which);
        void refreshActionBar();
        void loadSong();
        void prepareSongMenu();
        void rebuildSearchIndex();
        void callIntent(String what, Intent intent);
        void prepareOptionMenu();
        void removeSongFromSet(int val);
        void splashScreen();
        void showActionBar();
        void hideActionBar();
        void useCamera();
        void doDownload(String file);
        void connectHDMI();
        void takeScreenShot();
        void prepareLearnAutoScroll();
        void stopAutoScroll();
        void killPad();
        void stopMetronome();
        void doExport();
        void updateExtraInfoColorsAndSizes(String s);
        void selectAFileUri(String s);
        void profileWork(String s);
    }

    private static MyInterface mListener;

    private static FragmentManager fm;

    static LinearLayout prepareOptionMenu(Context c, FragmentManager fragman) {
        mListener = (MyInterface) c;
        fm = fragman;
        LinearLayout menu;
        switch (StaticVariables.whichOptionMenu) {
            case "MAIN":
            default:
                menu = createMainMenu(c);
                break;

            case "SET":
                menu = createSetMenu(c);
                break;

            case "SONG":
                menu = createSongMenu(c);
                break;

            case "PROFILE":
                menu = createProfileMenu(c);
                break;

            case "FIND":
                menu = createFindSongsMenu(c);
                break;

            case "CHORDS":
                menu = createChordsMenu(c);
                break;

            case "DISPLAY":
                menu = createDisplayMenu(c);
                break;

            case "CONNECT":
                menu = createConnectMenu(c);
                break;

            case "MIDI":
                menu = createMidiMenu(c);
                break;

            case "MODE":
                menu = createModeMenu(c);
                break;

            case "STORAGE":
                menu = createStorageMenu(c);
                break;

            case "GESTURES":
                menu = createGesturesMenu(c);
                break;

            case "AUTOSCROLL":
                menu = createAutoscrollMenu(c);
                break;

            case "PAD":
                menu = createPadMenu(c);
                break;

            case "METRONOME":
                menu = createMetronomeMenu(c);
                break;

            case "CCLI":
                menu = createCCLIMenu(c);
                break;

            case "OTHER":
                menu = createOtherMenu(c);
                break;

        }
        if (mListener!=null) {
            mListener.refreshActionBar();
        }
        return menu;
    }

    @SuppressLint("InflateParams")
    private static LinearLayout createMainMenu(Context c) {
        LayoutInflater inflater;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (inflater != null) {
            return (LinearLayout) inflater.inflate(R.layout.popup_option, null);
        } else {
            return null;
        }
    }

    @SuppressLint("InflateParams")
    private static LinearLayout createSetMenu(Context c) {
        LayoutInflater inflater;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            return (LinearLayout) inflater.inflate(R.layout.popup_option_set,null);
        } else {
            return null;
        }
    }

    @SuppressLint("InflateParams")
    private static LinearLayout createSongMenu(Context c) {
        LayoutInflater inflater;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            return (LinearLayout) inflater.inflate(R.layout.popup_option_song,null);
        } else {
            return null;
        }
    }

    @SuppressWarnings("InflateParams")
    private static LinearLayout createProfileMenu(Context c) {
        LayoutInflater inflater;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            return (LinearLayout) inflater.inflate(R.layout.popup_option_profile,null);
        } else {
            return null;
        }
    }

    @SuppressLint("InflateParams")
    private static LinearLayout createChordsMenu(Context c) {
        LayoutInflater inflater;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            return (LinearLayout) inflater.inflate(R.layout.popup_option_chords,null);
        } else {
            return null;
        }
    }

    @SuppressLint("InflateParams")
    private static LinearLayout createDisplayMenu(Context c) {
        LayoutInflater inflater;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            return (LinearLayout) inflater.inflate(R.layout.popup_option_display,null);
        } else {
            return null;
        }
    }

    @SuppressLint("InflateParams")
    private static LinearLayout createStorageMenu(Context c) {
        LayoutInflater inflater;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            return (LinearLayout) inflater.inflate(R.layout.popup_option_storage,null);
        } else {
            return null;
        }
    }

    @SuppressLint("InflateParams")
    private static LinearLayout createMidiMenu(Context c) {
        LayoutInflater inflater;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            return (LinearLayout) inflater.inflate(R.layout.popup_option_midi,null);
        } else {
            return null;
        }
    }

    @SuppressLint("InflateParams")
    private static LinearLayout createFindSongsMenu(Context c) {
        LayoutInflater inflater;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            return (LinearLayout) inflater.inflate(R.layout.popup_option_findsongs,null);
        } else {
            return null;
        }
    }

    @SuppressLint("InflateParams")
    private static LinearLayout createConnectMenu(Context c) {
        LayoutInflater inflater;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            return (LinearLayout) inflater.inflate(R.layout.popup_option_connections,null);
        } else {
            return null;
        }
    }

    @SuppressLint("InflateParams")
    private static LinearLayout createModeMenu(Context c) {
        LayoutInflater inflater;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            return (LinearLayout) inflater.inflate(R.layout.popup_option_modes,null);
        } else {
            return null;
        }
    }

    @SuppressLint("InflateParams")
    private static LinearLayout createGesturesMenu(Context c) {
        LayoutInflater inflater;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            return (LinearLayout) inflater.inflate(R.layout.popup_option_gestures,null);
        } else {
            return null;
        }
    }

    @SuppressLint("InflateParams")
    private static LinearLayout createAutoscrollMenu(Context c) {
        LayoutInflater inflater;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            return (LinearLayout) inflater.inflate(R.layout.popup_option_autoscroll,null);
        } else {
            return null;
        }
    }

    @SuppressLint("InflateParams")
    private static LinearLayout createPadMenu(Context c) {
        LayoutInflater inflater;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            return (LinearLayout) inflater.inflate(R.layout.popup_option_pad,null);
        } else {
            return null;
        }
    }

    @SuppressLint("InflateParams")
    private static LinearLayout createMetronomeMenu(Context c) {
        LayoutInflater inflater;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            return (LinearLayout) inflater.inflate(R.layout.popup_option_metronome,null);
        } else {
            return null;
        }
    }

    @SuppressLint("InflateParams")
    private static LinearLayout createCCLIMenu(Context c) {
        LayoutInflater inflater;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            return (LinearLayout) inflater.inflate(R.layout.popup_option_ccli,null);
        } else {
            return null;
        }
    }

    @SuppressLint("InflateParams")
    private static LinearLayout createOtherMenu(Context c) {
        LayoutInflater inflater;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            return (LinearLayout) inflater.inflate(R.layout.popup_option_other,null);
        } else {
            return null;
        }
    }

    static void optionListeners(View v, Context c, Preferences preferences, StorageAccess storageAccess) {

        // Decide which listeners we need based on the menu
        switch (StaticVariables.whichOptionMenu) {
            case "MAIN":
            default:
                mainOptionListener(v,c);
                break;

            case "SET":
                setOptionListener(v, c, preferences, storageAccess);
                break;

            case "SONG":
                songOptionListener(v,c,preferences);
                break;

            case "PROFILE":
                profileOptionListener(v,c);
                break;

            case "CHORDS":
                chordOptionListener(v, c, preferences);
                break;

            case "DISPLAY":
                displayOptionListener(v,c);
                break;

            case "FIND":
                findSongsOptionListener(v,c);
                break;

            case "STORAGE":
                storageOptionListener(v, c, preferences);
                break;

            case "CONNECT":
                connectOptionListener(v,c);
                break;

            case "MIDI":
                midiOptionListener(v,c,preferences);
                break;

            case "MODE":
                modeOptionListener(v,c,preferences);
                break;

            case "GESTURES":
                gestureOptionListener(v,c,preferences);
                break;

            case "AUTOSCROLL":
                autoscrollOptionListener(v,c,preferences);
                break;

            case "PAD":
                padOptionListener(v,c,preferences);
                break;

            case "METRONOME":
                metronomeOptionListener(v,c, preferences);
                break;

            case "CCLI":
                ccliOptionListener(v,c,preferences);
                break;

            case "OTHER":
                otherOptionListener(v, c, preferences);
                break;
        }
    }

    private static void mainOptionListener(View v, final Context c) {
        mListener = (MyInterface) c;
        // Identify the buttons
        Button menuSetButton = v.findViewById(R.id.menuSetButton);
        Button menuSongButton = v.findViewById(R.id.menuSongButton);
        Button menuProfileButton = v.findViewById(R.id.menuProfileButton);
        Button menuChordsButton = v.findViewById(R.id.menuChordsButton);
        Button menuDisplayButton = v.findViewById(R.id.menuDisplayButton);
        Button menuGesturesButton = v.findViewById(R.id.menuGesturesButton);
        Button menuConnectButton = v.findViewById(R.id.menuConnectButton);
        Button menuModeButton = v.findViewById(R.id.menuModeButton);
        Button menuMidiButton = v.findViewById(R.id.menuMidiButton);
        Button menuFindSongsButton = v.findViewById(R.id.menuFindSongsButton);
        Button menuStorageButton = v.findViewById(R.id.menuStorageButton);
        Button menuPadButton = v.findViewById(R.id.menuPadButton);
        Button menuAutoScrollButton = v.findViewById(R.id.menuAutoScrollButton);
        Button menuMetronomeButton = v.findViewById(R.id.menuMetronomeButton);
        Button menuCCLIButton = v.findViewById(R.id.menuCCLIButton);
        Button menuOtherButton = v.findViewById(R.id.menuOtherButton);
        FloatingActionButton closeOptionsFAB = v.findViewById(R.id.closeOptionsFAB);

        // Capitalise all the text by locale
        menuSetButton.setText(c.getString(R.string.set).toUpperCase(StaticVariables.locale));
        menuSongButton.setText(c.getString(R.string.song).toUpperCase(StaticVariables.locale));
        menuProfileButton.setText(c.getString(R.string.profile).toUpperCase(StaticVariables.locale));
        menuChordsButton.setText(c.getString(R.string.chords).toUpperCase(StaticVariables.locale));
        menuDisplayButton.setText(c.getString(R.string.display).toUpperCase(StaticVariables.locale));
        menuGesturesButton.setText(c.getString(R.string.gesturesandmenus).toUpperCase(StaticVariables.locale));
        menuConnectButton.setText(c.getString(R.string.connections_connect).toUpperCase(StaticVariables.locale));
        menuMidiButton.setText(c.getString(R.string.midi).toUpperCase(StaticVariables.locale));
        menuModeButton.setText(c.getString(R.string.choose_app_mode).toUpperCase(StaticVariables.locale));
        menuFindSongsButton.setText(c.getString(R.string.findnewsongs).toUpperCase(StaticVariables.locale));
        menuStorageButton.setText(c.getString(R.string.storage).toUpperCase(StaticVariables.locale));
        menuPadButton.setText(c.getString(R.string.pad).toUpperCase(StaticVariables.locale));
        menuAutoScrollButton.setText(c.getString(R.string.autoscroll).toUpperCase(StaticVariables.locale));
        menuMetronomeButton.setText(c.getString(R.string.metronome).toUpperCase(StaticVariables.locale));
        menuCCLIButton.setText(c.getString(R.string.edit_song_ccli).toUpperCase(StaticVariables.locale));
        menuOtherButton.setText(c.getString(R.string.other).toUpperCase(StaticVariables.locale));

        // Only allow connection menu for JellyBean+
        menuConnectButton.setVisibility(View.VISIBLE);

        // Only allow MIDI menu for Marshmallow+ and if it is available
        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
        /*if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M &&
                c.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MIDI)) {*/
            menuMidiButton.setVisibility(View.VISIBLE);
        } else {
            menuMidiButton.setVisibility(View.GONE);
        }

        // Set the listeners
        menuSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StaticVariables.whichOptionMenu = "SET";
                if (mListener!=null) {
                    mListener.prepareOptionMenu();
                }
            }
        });
        menuSongButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StaticVariables.whichOptionMenu = "SONG";
                if (mListener!=null) {
                    mListener.prepareOptionMenu();
                }
            }
        });
        menuProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StaticVariables.whichOptionMenu = "PROFILE";
                if (mListener!=null) {
                    mListener.prepareOptionMenu();
                }
            }
        });
        menuChordsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StaticVariables.whichOptionMenu = "CHORDS";
                if (mListener!=null) {
                    mListener.prepareOptionMenu();
                }
            }
        });
        menuDisplayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StaticVariables.whichOptionMenu = "DISPLAY";
                if (mListener!=null) {
                    mListener.prepareOptionMenu();
                }
            }
        });
        menuGesturesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StaticVariables.whichOptionMenu = "GESTURES";
                if (mListener!=null) {
                    mListener.prepareOptionMenu();
                }
            }
        });
        menuFindSongsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StaticVariables.whichOptionMenu = "FIND";
                if (mListener!=null) {
                    mListener.prepareOptionMenu();
                }
            }
        });
        menuStorageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StaticVariables.whichOptionMenu = "STORAGE";
                if (mListener!=null) {
                    mListener.prepareOptionMenu();
                }
            }
        });
        if (c.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MIDI)) {
            menuMidiButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    StaticVariables.whichOptionMenu = "MIDI";
                    if (mListener != null) {
                        mListener.prepareOptionMenu();
                    }
                }
            });
        } else {
            menuMidiButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    StaticVariables.myToastMessage = "MIDI - " + c.getString(R.string.nothighenoughapi);
                    ShowToast.showToast(c);
                }
            });
        }

        menuConnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StaticVariables.whichOptionMenu = "CONNECT";
                if (mListener!=null) {
                    mListener.prepareOptionMenu();
                }
            }
        });
        menuModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StaticVariables.whichOptionMenu = "MODE";
                if (mListener!=null) {
                    mListener.prepareOptionMenu();
                }
            }
        });
        menuAutoScrollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StaticVariables.whichOptionMenu = "AUTOSCROLL";
                if (mListener!=null) {
                    mListener.prepareOptionMenu();
                }
            }
        });
        menuPadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StaticVariables.whichOptionMenu = "PAD";
                if (mListener!=null) {
                    mListener.prepareOptionMenu();
                }
            }
        });
        menuMetronomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StaticVariables.whichOptionMenu = "METRONOME";
                if (mListener!=null) {
                    mListener.prepareOptionMenu();
                }
            }
        });
        menuCCLIButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StaticVariables.whichOptionMenu = "CCLI";
                if (mListener!=null) {
                    mListener.prepareOptionMenu();
                }
            }
        });
        menuOtherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StaticVariables.whichOptionMenu = "OTHER";
                if (mListener!=null) {
                    mListener.prepareOptionMenu();
                }
            }
        });

        closeOptionsFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener!=null) {
                    mListener.closeMyDrawers("option");
                }
            }
        });

    }

    private static void setOptionListener(View v, final Context c, final Preferences preferences, final StorageAccess storageAccess) {
        mListener = (MyInterface) c;

        // Identify the buttons
        TextView menuup = v.findViewById(R.id.setMenuTitle);
        Button setLoadButton = v.findViewById(R.id.setLoadButton);
        Button setSaveButton = v.findViewById(R.id.setSaveButton);
        Button setNewButton = v.findViewById(R.id.setNewButton);
        Button setDeleteButton = v.findViewById(R.id.setDeleteButton);
        Button setOrganiseButton = v.findViewById(R.id.setOrganiseButton);
        Button setImportButton = v.findViewById(R.id.setImportButton);
        Button setExportButton = v.findViewById(R.id.setExportButton);
        Button setCustomButton = v.findViewById(R.id.setCustomButton);
        Button setVariationButton = v.findViewById(R.id.setVariationButton);
        Button setEditButton = v.findViewById(R.id.setEditButton);
        SwitchCompat showSetTickBoxInSongMenu = v.findViewById(R.id.showSetTickBoxInSongMenu);
        LinearLayout setLinearLayout = v.findViewById(R.id.setLinearLayout);
        FloatingActionButton closeOptionsFAB = v.findViewById(R.id.closeOptionsFAB);

        // Capitalise all the text by locale
        menuup.setText(c.getString(R.string.set).toUpperCase(StaticVariables.locale));
        setLoadButton.setText(c.getString(R.string.load).toUpperCase(StaticVariables.locale));
        setSaveButton.setText(c.getString(R.string.save).toUpperCase(StaticVariables.locale));
        setNewButton.setText(c.getString(R.string.set_new).toUpperCase(StaticVariables.locale));
        setDeleteButton.setText(c.getString(R.string.delete).toUpperCase(StaticVariables.locale));
        setOrganiseButton.setText(c.getString(R.string.managesets).toUpperCase(StaticVariables.locale));
        setImportButton.setText(c.getString(R.string.importnewset).toUpperCase(StaticVariables.locale));
        setExportButton.setText(c.getString(R.string.export).toUpperCase(StaticVariables.locale));
        setCustomButton.setText(c.getString(R.string.add_custom_slide).toUpperCase(StaticVariables.locale));
        setVariationButton.setText(c.getString(R.string.customise_set_item).toUpperCase(StaticVariables.locale));
        setEditButton.setText(c.getString(R.string.edit).toUpperCase(StaticVariables.locale));
        showSetTickBoxInSongMenu.setText(c.getString(R.string.setquickcheck).toUpperCase(StaticVariables.locale));

        showSetTickBoxInSongMenu.setChecked(preferences.getMyPreferenceBoolean(c,"songMenuSetTicksShow",true));

        // Set the button listeners
        menuup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StaticVariables.whichOptionMenu = "MAIN";
                if (mListener!=null) {
                    mListener.prepareOptionMenu();
                }
            }
        });

        setLoadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullscreenActivity.whattodo = "loadset";
                if (mListener!=null) {
                    mListener.closeMyDrawers("option");
                    mListener.openFragment();
                }
            }
        });

        setSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String lastSetName = preferences.getMyPreferenceString(c,"setCurrentLastName","");
                Uri settosave = storageAccess.getUriForItem(c, preferences, "Sets", "", lastSetName);
                if (lastSetName==null || lastSetName.equals("")) {
                    FullscreenActivity.whattodo = "saveset";
                    if (mListener != null) {
                        mListener.closeMyDrawers("option");
                        mListener.openFragment();
                    }
                } else if (storageAccess.uriExists(c, settosave)) {
                    // Load the are you sure prompt
                    FullscreenActivity.whattodo = "saveset";
                    String setnamenice = lastSetName.replace("__"," / ");
                    String message = c.getResources().getString(R.string.save) + " \'" + setnamenice + "\"?";
                    StaticVariables.myToastMessage = message;
                    try {
                        DialogFragment newFragment = PopUpAreYouSureFragment.newInstance(message);
                        newFragment.show(fm,message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    FullscreenActivity.whattodo = "saveset";
                    if (mListener != null) {
                        mListener.openFragment();
                    }
                }
            }
        });

        setNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullscreenActivity.whattodo = "clearset";
                if (mListener!=null) {
                    mListener.closeMyDrawers("option");
                    mListener.openFragment();
                }
            }
        });

        setOrganiseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullscreenActivity.whattodo = "managesets";
                if (mListener!=null) {
                    mListener.closeMyDrawers("option");
                    mListener.openFragment();
                }
            }
        });

        setDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullscreenActivity.whattodo = "deleteset";
                if (mListener!=null) {
                    mListener.closeMyDrawers("option");
                    mListener.openFragment();
                }
            }
        });

        setImportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullscreenActivity.whattodo = "doimportset";
                if (mListener!=null) {
                    mListener.closeMyDrawers("option");
                    mListener.selectAFileUri(c.getString(R.string.importnewset));
                }
            }
        });

        setExportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullscreenActivity.whattodo = "exportset";
                if (mListener!=null) {
                    mListener.closeMyDrawers("option");
                    mListener.openFragment();
                }
            }
        });

        setCustomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullscreenActivity.whattodo = "customcreate";
                if (mListener!=null) {
                    mListener.closeMyDrawers("option");
                    mListener.openFragment();
                }
            }
        });

        setVariationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullscreenActivity.whattodo = "setitemvariation";
                if (mListener!=null) {
                    mListener.closeMyDrawers("option");
                    mListener.openFragment();
                }
            }
        });

        setEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullscreenActivity.whattodo = "editset";
                if (mListener!=null) {
                    mListener.closeMyDrawers("option");
                    mListener.openFragment();
                }
            }
        });

        showSetTickBoxInSongMenu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                preferences.setMyPreferenceBoolean(c,"songMenuSetTicksShow",b);
                if (mListener!=null) {
                    mListener.prepareSongMenu();
                }
            }
        });

        closeOptionsFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener!=null) {
                    mListener.closeMyDrawers("option");
                }
            }
        });

        // Add the set list to the menu
        if (StaticVariables.mSetList!=null) {
            for (int x = 0; x< StaticVariables.mSetList.length; x++) {
                TextView tv = new TextView(c);
                tv.setText(StaticVariables.mSetList[x]);
                tv.setTextColor(0xffffffff);
                tv.setTextSize(16.0f);
                tv.setPadding(16,16,16,16);
                LinearLayout.LayoutParams tvp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                tvp.setMargins(40,40,40,40);
                tv.setLayoutParams(tvp);
                final int val = x;
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            StaticVariables.setView = true;
                            FullscreenActivity.pdfPageCurrent = 0;
                            FullscreenActivity.linkclicked = StaticVariables.mSetList[val];
                            StaticVariables.indexSongInSet = val;
                            SetActions setActions = new SetActions();
                            setActions.songIndexClickInSet();
                            setActions.getSongFileAndFolder(c);
                            if (mListener != null) {
                                mListener.closeMyDrawers("option");
                                mListener.loadSong();
                            }
                        } catch (Exception e) {
                            Log.d("OptionMenuListeners", "Something went wrong with the set item");
                        }
                    }
                });
                tv.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        try {
                            FullscreenActivity.linkclicked = StaticVariables.mSetList[val];
                            if (mListener != null) {
                                mListener.removeSongFromSet(val);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return false;
                    }
                });
                setLinearLayout.addView(tv);
            }
        }

    }

    private static void songOptionListener(View v, final Context c, final Preferences preferences) {
        mListener = (MyInterface) c;

        // Identify the buttons
        TextView menuup = v.findViewById(R.id.songMenuTitle);
        Button songEditButton = v.findViewById(R.id.songEditButton);
        Button songDuplicateButton = v. findViewById(R.id.songDuplicateButton);
        Button songPadButton = v.findViewById(R.id.songPadButton);
        Button songAutoScrollButton = v.findViewById(R.id.songAutoScrollButton);
        Button songMetronomeButton = v.findViewById(R.id.songMetronomeButton);
        Button songStickyButton = v.findViewById(R.id.songStickyButton);
        Button songDrawingButton = v.findViewById(R.id.songDrawingButton);
        Button songChordsButton = v.findViewById(R.id.songChordsButton);
        Button songScoreButton = v.findViewById(R.id.songScoreButton);
        Button songOnYouTubeButton = v.findViewById(R.id.songOnYouTubeButton);
        Button songOnWebButton = v.findViewById(R.id.songOnWebButton);
        Button songLinksButton = v.findViewById(R.id.songLinksButton);
        Button songRenameButton = v.findViewById(R.id.songRenameButton);
        Button songNewButton = v.findViewById(R.id.songNewButton);
        Button songDeleteButton = v.findViewById(R.id.songDeleteButton);
        Button songExportButton = v.findViewById(R.id.songExportButton);
        Button songImportButton = v.findViewById(R.id.songImportButton);
        final SwitchCompat songPresentationOrderButton = v.findViewById(R.id.songPresentationOrderButton);
        SwitchCompat songKeepMultiLineCompactButton = v.findViewById(R.id.songKeepMultiLineCompactButton);
        FloatingActionButton closeOptionsFAB = v.findViewById(R.id.closeOptionsFAB);

        // Capitalise all the text by locale
        menuup.setText(c.getString(R.string.song).toUpperCase(StaticVariables.locale));
        songPadButton.setText(c.getString(R.string.pad).toUpperCase(StaticVariables.locale));
        songAutoScrollButton.setText(c.getString(R.string.autoscroll).toUpperCase(StaticVariables.locale));
        songMetronomeButton.setText(c.getString(R.string.metronome).toUpperCase(StaticVariables.locale));
        songChordsButton.setText(c.getString(R.string.chords).toUpperCase(StaticVariables.locale));
        songLinksButton.setText(c.getString(R.string.link).toUpperCase(StaticVariables.locale));
        songDuplicateButton.setText(c.getString(R.string.duplicate).toUpperCase(StaticVariables.locale));
        songEditButton.setText(c.getString(R.string.edit).toUpperCase(StaticVariables.locale));
        songStickyButton.setText(c.getString(R.string.stickynotes_edit).toUpperCase(StaticVariables.locale));
        songDrawingButton.setText(c.getString(R.string.highlight).toUpperCase(StaticVariables.locale));
        songScoreButton.setText(c.getString(R.string.music_score).toUpperCase(StaticVariables.locale));
        songOnYouTubeButton.setText(c.getString(R.string.youtube).toUpperCase(StaticVariables.locale));
        songOnWebButton.setText(c.getString(R.string.websearch).toUpperCase(StaticVariables.locale));
        songRenameButton.setText(c.getString(R.string.rename).toUpperCase(StaticVariables.locale));
        songNewButton.setText(c.getString(R.string.new_something).toUpperCase(StaticVariables.locale));
        songDeleteButton.setText(c.getString(R.string.delete).toUpperCase(StaticVariables.locale));
        songImportButton.setText(c.getString(R.string.importnewsong).toUpperCase(StaticVariables.locale));
        songExportButton.setText(c.getString(R.string.export).toUpperCase(StaticVariables.locale));
        songPresentationOrderButton.setText(c.getString(R.string.edit_song_presentation).toUpperCase(StaticVariables.locale));
        songKeepMultiLineCompactButton.setText(c.getString(R.string.keepmultiline).toUpperCase(StaticVariables.locale));

        // Hide the drawing option unless we are in performance mode
        if (StaticVariables.whichMode.equals("Performance")) {
            songDrawingButton.setVisibility(View.VISIBLE);
        } else {
            songDrawingButton.setVisibility(View.GONE);
        }

        // Set the switches up based on preferences
        songPresentationOrderButton.setChecked(preferences.getMyPreferenceBoolean(c,"usePresentationOrder",false));
        songKeepMultiLineCompactButton.setChecked(preferences.getMyPreferenceBoolean(c,"multiLineVerseKeepCompact",false));

        // Set the button listeners
        menuup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StaticVariables.whichOptionMenu = "MAIN";
                if (mListener!=null) {
                    mListener.prepareOptionMenu();
                }
            }
        });

        songDuplicateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullscreenActivity.whattodo = "duplicate";
                if (mListener!=null) {
                    mListener.closeMyDrawers("option");
                    mListener.openFragment();
                }
            }
        });

        songPadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FullscreenActivity.isSong) {
                    FullscreenActivity.whattodo = "page_pad";
                    if (mListener != null) {
                        mListener.closeMyDrawers("option");
                        mListener.openFragment();
                    }
                }
            }
        });

        songAutoScrollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FullscreenActivity.isSong) {
                    FullscreenActivity.whattodo = "page_autoscroll";
                    if (mListener != null) {
                        mListener.closeMyDrawers("option");
                        mListener.openFragment();
                    }
                }
            }
        });

        songMetronomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FullscreenActivity.isSong) {
                    FullscreenActivity.whattodo = "page_metronome";
                    if (mListener != null) {
                        mListener.closeMyDrawers("option");
                        mListener.openFragment();
                    }
                }
            }
        });

        songChordsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FullscreenActivity.isSong) {
                    FullscreenActivity.whattodo = "page_chords";
                    if (mListener != null) {
                        mListener.closeMyDrawers("option");
                        mListener.openFragment();
                    }
                }
            }
        });

        songLinksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FullscreenActivity.isSong) {
                    FullscreenActivity.whattodo = "page_links";
                    if (mListener != null) {
                        mListener.closeMyDrawers("option");
                        mListener.openFragment();
                    }
                }
            }
        });

        songEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FullscreenActivity.isSong) {
                    FullscreenActivity.whattodo = "editsong";
                    if (mListener != null) {
                        mListener.closeMyDrawers("option");
                        mListener.openFragment();
                    }
                } else {
                    StaticVariables.myToastMessage = c.getString(R.string.not_allowed);
                    ShowToast.showToast(c);
                }
            }
        });

        songStickyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FullscreenActivity.isSong) {
                    FullscreenActivity.whattodo = "editnotes";
                    if (mListener != null) {
                        mListener.closeMyDrawers("option");
                        mListener.openFragment();
                    }
                } else {
                    StaticVariables.myToastMessage = c.getString(R.string.not_allowed);
                    ShowToast.showToast(c);
                }
            }
        });

        songDrawingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullscreenActivity.whattodo = "drawnotes";
                if (mListener!=null) {
                    mListener.closeMyDrawers("option");
                    // Take a snapshot of the songwindow
                    mListener.takeScreenShot();
                    if (FullscreenActivity.bmScreen!=null) {
                        mListener.openFragment();
                    } else {
                        Log.d("OptionMenuListeners", "screenshot is null");
                    }
                }
            }
        });

        songScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullscreenActivity.whattodo = "abcnotation_edit";
                if (mListener!=null) {
                    mListener.closeMyDrawers("option");
                    mListener.openFragment();
                }
            }
        });

        songOnYouTubeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullscreenActivity.whattodo = "youtube";
                if (mListener != null) {
                    Intent youtube = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.youtube.com/results?search_query=" + StaticVariables.mTitle + "+" + StaticVariables.mAuthor));
                    mListener.callIntent("web", youtube);
                    mListener.closeMyDrawers("option");
                }
            }
        });

        songOnWebButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullscreenActivity.whattodo = "websearch";
                if (mListener!=null) {
                    Intent web = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.google.com/search?q=" + StaticVariables.mTitle + "+" + StaticVariables.mAuthor));
                    mListener.callIntent("web", web);
                    mListener.closeMyDrawers("option");
                }
            }
        });

        songRenameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullscreenActivity.whattodo = "renamesong";
                if (mListener!=null) {
                    mListener.closeMyDrawers("option");
                    mListener.openFragment();
                }
            }
        });

        songNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullscreenActivity.whattodo = "createsong";
                if (mListener!=null) {
                    mListener.closeMyDrawers("option");
                    mListener.openFragment();
                }
            }
        });

        songDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullscreenActivity.whattodo = "deletesong";
                if (mListener!=null) {
                    mListener.closeMyDrawers("option");
                    mListener.openFragment();
                }
            }
        });

        songImportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullscreenActivity.whattodo = "doimport";
                if (mListener!=null) {
                    mListener.closeMyDrawers("option");
                    mListener.selectAFileUri(c.getString(R.string.importnewsong));
                }
            }
        });

        songExportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FullscreenActivity.isSong) {
                    FullscreenActivity.whattodo = "customise_exportsong";
                    if (mListener != null) {
                        mListener.closeMyDrawers("option");
                        mListener.openFragment();
                    }
                } else {
                    StaticVariables.myToastMessage = c.getString(R.string.not_allowed);
                    ShowToast.showToast(c);
                }
            }
        });

        songPresentationOrderButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                preferences.setMyPreferenceBoolean(c,"usePresentationOrder",b);
                if (FullscreenActivity.isSong) {
                    if (mListener != null) {
                        mListener.loadSong();
                    }
                }
            }
        });

        songKeepMultiLineCompactButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                preferences.setMyPreferenceBoolean(c,"multiLineVerseKeepCompact",b);
                if (FullscreenActivity.isSong) {
                    if (mListener != null) {
                        mListener.loadSong();
                    }
                }
            }
        });

        closeOptionsFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener!=null) {
                    mListener.closeMyDrawers("option");
                }
            }
        });

    }

    private static void chordOptionListener(View v, final Context c, final Preferences preferences) {
        mListener = (MyInterface) c;

        // Identify the buttons
        TextView menuup = v.findViewById(R.id.chordsMenuTitle);
        Button chordsButton = v.findViewById(R.id.chordsButton);
        Button chordsTransposeButton = v.findViewById(R.id.chordsTransposeButton);
        Button chordsSharpButton = v.findViewById(R.id.chordsSharpButton);
        Button chordsFlatButton = v.findViewById(R.id.chordsFlatButton);
        SwitchCompat chordsToggleSwitch = v.findViewById(R.id.chordsToggleSwitch);
        SwitchCompat chordsLyricsToggleSwitch = v.findViewById(R.id.chordsLyricsToggleSwitch);
        final SwitchCompat chordsCapoToggleSwitch = v.findViewById(R.id.chordsCapoToggleSwitch);
        final SwitchCompat chordsNativeAndCapoToggleSwitch = v.findViewById(R.id.chordsNativeAndCapoToggleSwitch);
        final SwitchCompat capoAsNumeralsToggleSwitch = v.findViewById(R.id.capoAsNumeralsToggleSwitch);
        SwitchCompat switchCapoTextSize = v.findViewById(R.id.switchCapoTextSize);
        Button chordsFormatButton = v.findViewById(R.id.chordsFormatButton);
        Button chordsConvertButton = v.findViewById(R.id.chordsConvertButton);
        FloatingActionButton closeOptionsFAB = v.findViewById(R.id.closeOptionsFAB);

        // Capitalise all the text by locale
        menuup.setText(c.getString(R.string.chords).toUpperCase(StaticVariables.locale));
        chordsButton.setText(c.getString(R.string.chords).toUpperCase(StaticVariables.locale));
        chordsTransposeButton.setText(c.getString(R.string.transpose).toUpperCase(StaticVariables.locale));
        chordsSharpButton.setText(c.getString(R.string.chords_sharp).toUpperCase(StaticVariables.locale));
        String temp = c.getString(R.string.chords_flat).replace("b","#");
        temp = temp.toUpperCase(StaticVariables.locale);
        temp = temp.replace("#","\u266d");
        chordsFlatButton.setTransformationMethod(null);
        chordsFlatButton.setText(temp);
        chordsToggleSwitch.setText(c.getString(R.string.showchords).toUpperCase(StaticVariables.locale));
        chordsLyricsToggleSwitch.setText(c.getString(R.string.showlyrics).toUpperCase(StaticVariables.locale));
        chordsCapoToggleSwitch.setText(c.getString(R.string.showcapo).toUpperCase(StaticVariables.locale));
        chordsNativeAndCapoToggleSwitch.setText(c.getString(R.string.capo_toggle_bothcapo).toUpperCase(StaticVariables.locale));
        capoAsNumeralsToggleSwitch.setText(c.getString(R.string.capo_style).toUpperCase(StaticVariables.locale));
        switchCapoTextSize.setText(c.getString(R.string.size).toUpperCase(StaticVariables.locale));
        chordsFormatButton.setText(c.getString(R.string.choose_chordformat).toUpperCase(StaticVariables.locale));
        chordsConvertButton.setText(c.getString(R.string.chord_convert).toUpperCase(StaticVariables.locale));

        // Set the switches up based on preferences
        if (preferences.getMyPreferenceBoolean(c,"displayChords",true)) {
            chordsToggleSwitch.setChecked(true);
        } else {
            chordsToggleSwitch.setChecked(false);
            chordsCapoToggleSwitch.setEnabled(false);
            chordsNativeAndCapoToggleSwitch.setEnabled(false);
        }

        switchCapoTextSize.setChecked(preferences.getMyPreferenceBoolean(c,"capoLargeFontInfoBar",true));

        chordsLyricsToggleSwitch.setChecked(preferences.getMyPreferenceBoolean(c,"displayLyrics",true));
        boolean capochordsbuttonenabled = preferences.getMyPreferenceBoolean(c,"displayChords",true);
        chordsCapoToggleSwitch.setChecked(preferences.getMyPreferenceBoolean(c,"displayCapoChords",true));
        chordsCapoToggleSwitch.setEnabled(capochordsbuttonenabled);
        if (!capochordsbuttonenabled) {
            chordsCapoToggleSwitch.setAlpha(0.4f);
        }

        boolean nativeandcapobuttonenabled = preferences.getMyPreferenceBoolean(c,"displayChords",true) &&
                capochordsbuttonenabled;
        chordsNativeAndCapoToggleSwitch.setChecked(preferences.getMyPreferenceBoolean(c,"displayCapoAndNativeChords",false));
        chordsNativeAndCapoToggleSwitch.setEnabled(nativeandcapobuttonenabled);
        if (!nativeandcapobuttonenabled) {
            chordsNativeAndCapoToggleSwitch.setAlpha(0.4f);
        }
        capoAsNumeralsToggleSwitch.setChecked(preferences.getMyPreferenceBoolean(c,"capoInfoAsNumerals",false));

        // Set the button listeners
        menuup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StaticVariables.whichOptionMenu = "MAIN";
                if (mListener!=null) {
                    mListener.prepareOptionMenu();
                }
            }
        });

        chordsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FullscreenActivity.isSong) {
                    FullscreenActivity.whattodo = "page_chords";
                    if (mListener != null) {
                        mListener.closeMyDrawers("option");
                        mListener.openFragment();
                    }
                }
            }
        });

        chordsTransposeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FullscreenActivity.isSong) {
                    FullscreenActivity.whattodo = "transpose";
                    if (mListener != null) {
                        mListener.closeMyDrawers("option");
                        mListener.openFragment();
                    }
                } else {
                    StaticVariables.myToastMessage = c.getString(R.string.not_allowed);
                    ShowToast.showToast(c);
                }
            }
        });

        chordsSharpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Transpose transpose = new Transpose();
                FullscreenActivity.whattodo = "transpose";
                if (FullscreenActivity.isPDF) {
                    // Can't do this action on a pdf!
                    StaticVariables.myToastMessage = c.getResources().getString(R.string.pdf_functionnotavailable);
                    ShowToast.showToast(c);
                } else if (!FullscreenActivity.isSong) {
                    // Editing a slide / note / scripture / image
                    StaticVariables.myToastMessage = c.getResources().getString(R.string.not_allowed);
                    ShowToast.showToast(c);
                } else {
                    StaticVariables.transposeDirection = "0";
                    transpose.checkChordFormat(c,preferences);
                    try {
                        transpose.doTranspose(c, preferences, true, false, false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (mListener!=null) {
                        mListener.loadSong();
                    }
                }
            }
        });

        chordsFlatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Transpose transpose = new Transpose();
                FullscreenActivity.whattodo = "transpose";
                if (FullscreenActivity.isPDF) {
                    // Can't do this action on a pdf!
                    StaticVariables.myToastMessage = c.getResources().getString(R.string.pdf_functionnotavailable);
                    ShowToast.showToast(c);
                } else if (!FullscreenActivity.isSong) {
                    // Editing a slide / note / scripture / image
                    StaticVariables.myToastMessage = c.getResources().getString(R.string.not_allowed);
                    ShowToast.showToast(c);
                } else {
                    StaticVariables.transposeDirection = "0";
                    transpose.checkChordFormat(c,preferences);
                    try {
                        transpose.doTranspose(c, preferences, false, true, false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (mListener!=null) {
                        mListener.loadSong();
                    }
                }
            }
        });

        chordsToggleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                preferences.setMyPreferenceBoolean(c,"displayChords",b);
                chordsCapoToggleSwitch.setEnabled(b);
                if (!b) {
                    chordsCapoToggleSwitch.setAlpha(0.4f);
                } else {
                    chordsCapoToggleSwitch.setAlpha(1.0f);
                }

                boolean nativeandcapobuttonenabled = preferences.getMyPreferenceBoolean(c,"displayCapoChords",true) && b;
                chordsNativeAndCapoToggleSwitch.setEnabled(nativeandcapobuttonenabled);
                if (!nativeandcapobuttonenabled) {
                    chordsNativeAndCapoToggleSwitch.setAlpha(0.4f);
                } else {
                    chordsNativeAndCapoToggleSwitch.setAlpha(1.0f);
                }

                if (mListener!=null) {
                    mListener.loadSong();
                }
            }
        });

        chordsLyricsToggleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                preferences.setMyPreferenceBoolean(c,"displayLyrics",b);
                if (mListener!=null) {
                    mListener.loadSong();
                }
            }
        });

        chordsCapoToggleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                preferences.setMyPreferenceBoolean(c,"displayCapoChords",b);
                boolean nativeandcapobuttonenabled = preferences.getMyPreferenceBoolean(c,"displayChords",true) && b;
                chordsNativeAndCapoToggleSwitch.setEnabled(nativeandcapobuttonenabled);
                if (!nativeandcapobuttonenabled) {
                    chordsNativeAndCapoToggleSwitch.setAlpha(0.4f);
                } else {
                    chordsNativeAndCapoToggleSwitch.setAlpha(1.0f);
                }
                if (mListener!=null) {
                    mListener.loadSong();
                }
            }
        });

        chordsNativeAndCapoToggleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                preferences.setMyPreferenceBoolean(c,"displayCapoAndNativeChords",b);
                if (mListener!=null) {
                    mListener.loadSong();
                }
            }
        });

        capoAsNumeralsToggleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                preferences.setMyPreferenceBoolean(c,"capoInfoAsNumerals",b);
                if (mListener!=null) {
                    mListener.loadSong();
                }
            }
        });
        chordsFormatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullscreenActivity.whattodo = "choosechordformat";
                if (mListener!=null) {
                    mListener.closeMyDrawers("option");
                    mListener.openFragment();
                }
            }
        });
        switchCapoTextSize.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                preferences.setMyPreferenceBoolean(c,"capoLargeFontInfoBar",b);
                if (mListener!=null) {
                    mListener.updateExtraInfoColorsAndSizes("capo");
                }
            }
        });
        chordsConvertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Transpose transpose = new Transpose();
                if (FullscreenActivity.isPDF) {
                    // Can't do this action on a pdf!
                    StaticVariables.myToastMessage = c.getResources().getString(R.string.pdf_functionnotavailable);
                    ShowToast.showToast(c);
                } else if (!FullscreenActivity.isSong) {
                    // Editing a slide / note / scripture / image
                    StaticVariables.myToastMessage = c.getResources().getString(R.string.not_allowed);
                    ShowToast.showToast(c);
                } else {
                    StaticVariables.transposeDirection = "0";
                    transpose.checkChordFormat(c,preferences);
                    try {
                        transpose.doTranspose(c, preferences,false,false, true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (mListener!=null) {
                    mListener.closeMyDrawers("option");
                    mListener.loadSong();
                }
            }
        });

        closeOptionsFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener!=null) {
                    mListener.closeMyDrawers("option");
                }
            }
        });
    }

    private static void profileOptionListener(View v, final Context c) {
        mListener = (MyInterface) c;

        // Identify the buttons
        TextView menuup = v.findViewById(R.id.optionProfileTitle);
        Button profileLoadButton = v.findViewById(R.id.profileLoadButton);
        Button profileSaveButton = v.findViewById(R.id.profileSaveButton);
        FloatingActionButton closeOptionsFAB = v.findViewById(R.id.closeOptionsFAB);

        // Capitalise all the text by locale
        menuup.setText(c.getString(R.string.profile).toUpperCase(StaticVariables.locale));
        profileLoadButton.setText(c.getString(R.string.load).toUpperCase(StaticVariables.locale));
        profileSaveButton.setText(c.getString(R.string.save).toUpperCase(StaticVariables.locale));

        // Set the button listeners
        menuup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StaticVariables.whichOptionMenu = "MAIN";
                if (mListener!=null) {
                    mListener.prepareOptionMenu();
                }
            }
        });
        profileLoadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener!=null) {
                    mListener.profileWork("load");
                }
            }
        });
        profileSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mListener!=null) {
                    mListener.profileWork("save");
                }
            }
        });

        closeOptionsFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener!=null) {
                    mListener.closeMyDrawers("option");
                }
            }
        });
    }

    private static void displayOptionListener(View v, final Context c) {
        mListener = (MyInterface) c;

        // Identify the buttons
        TextView menuup = v.findViewById(R.id.optionDisplayTitle);
        Button displayThemeButton = v.findViewById(R.id.displayThemeButton);
        Button displayAutoScaleButton = v.findViewById(R.id.displayAutoScaleButton);
        Button displayFontButton = v.findViewById(R.id.displayFontButton);
        Button displayButtonsButton = v.findViewById(R.id.displayButtonsButton);
        Button displayPopUpsButton = v.findViewById(R.id.displayPopUpsButton);
        Button displayInfoButton = v.findViewById(R.id.displayInfoButton);
        Button displayActionBarButton = v.findViewById(R.id.displayActionBarButton);
        Button displayConnectedDisplayButton = v.findViewById(R.id.displayConnectedDisplayButton);
        Button displayHDMIButton = v.findViewById(R.id.displayHDMIButton);
        FloatingActionButton closeOptionsFAB = v.findViewById(R.id.closeOptionsFAB);

        // Capitalise all the text by locale
        menuup.setText(c.getString(R.string.display).toUpperCase(StaticVariables.locale));
        displayThemeButton.setText(c.getString(R.string.choose_theme).toUpperCase(StaticVariables.locale));
        displayAutoScaleButton.setText(c.getString(R.string.autoscale_toggle).toUpperCase(StaticVariables.locale));
        displayFontButton.setText(c.getString(R.string.choose_fonts).toUpperCase(StaticVariables.locale));
        displayButtonsButton.setText(c.getString(R.string.pagebuttons).toUpperCase(StaticVariables.locale));
        displayPopUpsButton.setText(c.getString(R.string.display_popups).toUpperCase(StaticVariables.locale));
        displayInfoButton.setText(c.getString(R.string.extra).toUpperCase(StaticVariables.locale));
        displayActionBarButton.setText(c.getString(R.string.actionbar).toUpperCase(StaticVariables.locale));
        displayConnectedDisplayButton.setText(c.getString(R.string.connected_display).toUpperCase(StaticVariables.locale));
        displayHDMIButton.setText(c.getString(R.string.hdmi).toUpperCase(StaticVariables.locale));

        // Set the button listeners
        menuup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StaticVariables.whichOptionMenu = "MAIN";
                if (mListener!=null) {
                    mListener.prepareOptionMenu();
                }
            }
        });

        displayThemeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener!=null) {
                    FullscreenActivity.whattodo = "changetheme";
                    mListener.closeMyDrawers("option");
                    mListener.openFragment();
                }
            }
        });

        displayAutoScaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener!=null) {
                    FullscreenActivity.whattodo = "autoscale";
                    mListener.closeMyDrawers("option");
                    mListener.openFragment();
                }
            }
        });

        displayFontButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener!=null) {
                    FullscreenActivity.whattodo = "changefonts";
                    mListener.closeMyDrawers("option");
                    mListener.openFragment();
                }
            }
        });

        displayButtonsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener!=null) {
                    FullscreenActivity.whattodo = "pagebuttons";
                    mListener.closeMyDrawers("option");
                    mListener.openFragment();
                }
            }
        });

        displayPopUpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener!=null) {
                    FullscreenActivity.whattodo = "popupsettings";
                    mListener.closeMyDrawers("option");
                    mListener.openFragment();
                }
            }
        });

        displayInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener!=null) {
                    FullscreenActivity.whattodo = "extra";
                    mListener.closeMyDrawers("option");
                    mListener.openFragment();
                }
            }
        });

        displayActionBarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener!=null) {
                    FullscreenActivity.whattodo = "actionbarinfo";
                    mListener.closeMyDrawers("option");
                    mListener.openFragment();
                }
            }
        });

        displayConnectedDisplayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener!=null && (FullscreenActivity.isPresenting || FullscreenActivity.isHDMIConnected)) {
                    FullscreenActivity.whattodo = "connecteddisplay";
                    mListener.closeMyDrawers("option");
                    mListener.openFragment();
                } else {
                    StaticVariables.myToastMessage = view.getContext().getString(R.string.nodisplays);
                    ShowToast.showToast(view.getContext());
                }
            }
        });

        displayHDMIButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener!=null) {
                    StaticVariables.myToastMessage = view.getContext().getString(R.string.connections_searching);
                    ShowToast.showToast(view.getContext());
                    FullscreenActivity.whattodo = "hdmi";
                    mListener.connectHDMI();
                }
            }
        });

        closeOptionsFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener!=null) {
                    mListener.closeMyDrawers("option");
                }
            }
        });

    }

    private static void findSongsOptionListener(View v, final Context c) {
        mListener = (MyInterface) c;

        // Identify the buttons
        TextView menuup = v.findViewById(R.id.findSongMenuTitle);
        Button ugSearchButton = v.findViewById(R.id.ugSearchButton);
        Button chordieSearchButton = v.findViewById(R.id.chordieSearchButton);
        Button songselectSearchButton = v.findViewById(R.id.songselectSearchButton);
        Button worshiptogetherSearchButton = v.findViewById(R.id.worshiptogetherSearchButton);
        Button worshipreadySearchButton = v.findViewById(R.id.worshipreadySearchButton);
        Button ukutabsSearchButton = v.findViewById(R.id.ukutabsSearchButton);
        Button holychordsSearchButton = v.findViewById(R.id.holychordsSearchButton);
        Button bandDownloadButton = v.findViewById(R.id.bandDownloadButton);
        Button churchDownloadButton = v.findViewById(R.id.churchDownloadButton);
        Button songImportButton = v.findViewById(R.id.songImportButton);
        Button cameraButton = v.findViewById(R.id.cameraButton);
        FloatingActionButton closeOptionsFAB = v.findViewById(R.id.closeOptionsFAB);

        worshipreadySearchButton.setVisibility(View.GONE);

        // Capitalise all the text by locale
        menuup.setText(c.getString(R.string.findnewsongs).toUpperCase(StaticVariables.locale));
        ugSearchButton.setText(c.getString(R.string.ultimateguitarsearch).toUpperCase(StaticVariables.locale));
        chordieSearchButton.setText(c.getString(R.string.chordiesearch).toUpperCase(StaticVariables.locale));
        String ss = c.getString(R.string.songselect) + " " + c.getString(R.string.subscription);
        songselectSearchButton.setText(ss.toUpperCase(StaticVariables.locale));
        String wt = c.getString(R.string.worshiptogether) + " " + c.getString(R.string.subscription);
        worshiptogetherSearchButton.setText(wt.toUpperCase(StaticVariables.locale));
        String wr = c.getString(R.string.worshipready) + " " + c.getString(R.string.subscription);
        worshipreadySearchButton.setText(wr.toUpperCase(StaticVariables.locale));
        ukutabsSearchButton.setText(c.getString(R.string.ukutabs).toUpperCase(StaticVariables.locale));
        holychordsSearchButton.setText(c.getString(R.string.holychords).toUpperCase(StaticVariables.locale));
        bandDownloadButton.setText(c.getString(R.string.my_band).toUpperCase(StaticVariables.locale));
        churchDownloadButton.setText(c.getString(R.string.my_church).toUpperCase(StaticVariables.locale));
        songImportButton.setText(c.getString(R.string.importnewsong).toUpperCase(StaticVariables.locale));
        cameraButton.setText(c.getString(R.string.camera).toUpperCase(StaticVariables.locale));

        // Set the button listeners
        menuup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StaticVariables.whichOptionMenu = "MAIN";
                if (mListener!=null) {
                    mListener.prepareOptionMenu();
                }
            }
        });

        ugSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullscreenActivity.whattodo = "ultimate-guitar";
                if (mListener!=null) {
                    mListener.closeMyDrawers("option");
                    mListener.openFragment();
                }
            }
        });
        chordieSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullscreenActivity.whattodo = "chordie";
                if (mListener!=null) {
                    mListener.closeMyDrawers("option");
                    mListener.openFragment();
                }
            }
        });
        worshiptogetherSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullscreenActivity.whattodo = "worshiptogether";
                if (mListener != null) {
                    mListener.closeMyDrawers("option");
                    mListener.openFragment();
                }
            }
        });
        worshipreadySearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullscreenActivity.whattodo = "worshipready";
                if (mListener!=null) {
                    mListener.closeMyDrawers("option");
                    mListener.openFragment();
                }
            }
        });
        songselectSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullscreenActivity.whattodo = "songselect";
                if (mListener!=null) {
                    mListener.closeMyDrawers("option");
                    mListener.openFragment();
                }
            }
        });
        ukutabsSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullscreenActivity.whattodo = "ukutabs";
                if (mListener!=null) {
                    mListener.closeMyDrawers("option");
                    mListener.openFragment();
                }
            }
        });
        holychordsSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullscreenActivity.whattodo = "holychords";
                if (mListener!=null) {
                    mListener.closeMyDrawers("option");
                    mListener.openFragment();
                }
            }
        });
        bandDownloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullscreenActivity.whattodo = "download_band";
                StaticVariables.myToastMessage = c.getString(R.string.wait);
                ShowToast.showToast(c);
                if (mListener!=null) {
                    mListener.doDownload("https://drive.google.com/uc?export=download&id=0B-GbNhnY_O_leDR5bFFjRVVxVjA");
                    mListener.closeMyDrawers("option");
                }
            }
        });
        churchDownloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullscreenActivity.whattodo = "download_church";
                StaticVariables.myToastMessage = c.getString(R.string.wait);
                ShowToast.showToast(c);
                if (mListener!=null) {
                    mListener.doDownload("https://drive.google.com/uc?export=download&id=0B-GbNhnY_O_lbVY3VVVOMkc5OGM");
                    mListener.closeMyDrawers("option");
                }
            }
        });
        songImportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullscreenActivity.whattodo = "doimport";
                if (mListener!=null) {
                    mListener.closeMyDrawers("option");
                    mListener.selectAFileUri(c.getString(R.string.importnewsong));
                }
            }
        });
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mListener!=null) {
                    mListener.useCamera();
                }
           }
        });

        closeOptionsFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener!=null) {
                    mListener.closeMyDrawers("option");
                }
            }
        });

    }

    private static void storageOptionListener(View v, final Context c, final Preferences preferences) {
        mListener = (MyInterface) c;

        // Identify the buttons
        TextView menuup = v.findViewById(R.id.optionStorageTitle);
        Button storageNewFolderButton = v.findViewById(R.id.storageNewFolderButton);
        Button storageEditButton = v.findViewById(R.id.storageEditButton);
        Button storageManageButton = v.findViewById(R.id.storageManageButton);
        Button exportSongListButton = v.findViewById(R.id.exportSongListButton);
        Button storageImportOSBButton = v.findViewById(R.id.storageImportOSBButton);
        Button storageExportOSBButton = v.findViewById(R.id.storageExportOSBButton);
        Button storageImportOnSongButton = v.findViewById(R.id.storageImportOnSongButton);
        Button storageSongMenuButton = v.findViewById(R.id.storageSongMenuButton);
        Button storageDatabaseButton = v.findViewById(R.id.storageDatabaseButton);
        Button storageLogButton = v.findViewById(R.id.storageLogButton);
        FloatingActionButton closeOptionsFAB = v.findViewById(R.id.closeOptionsFAB);

        // Capitalise all the text by locale
        menuup.setText(c.getString(R.string.storage).toUpperCase(StaticVariables.locale));
        storageNewFolderButton.setText(c.getString(R.string.folder_new).toUpperCase(StaticVariables.locale));
        storageEditButton.setText(c.getString(R.string.folder_rename).toUpperCase(StaticVariables.locale));
        storageManageButton.setText(c.getString(R.string.storage_choose).toUpperCase(StaticVariables.locale));
        exportSongListButton.setText(c.getString(R.string.exportsongdirectory).toUpperCase(StaticVariables.locale));
        storageImportOSBButton.setText(c.getString(R.string.backup_import).toUpperCase(StaticVariables.locale));
        storageExportOSBButton.setText(c.getString(R.string.backup_export).toUpperCase(StaticVariables.locale));
        storageImportOnSongButton.setText(c.getString(R.string.import_onsong_choose).toUpperCase(StaticVariables.locale));
        storageSongMenuButton.setText(c.getString(R.string.refreshsongs).toUpperCase(StaticVariables.locale));
        storageDatabaseButton.setText(c.getString(R.string.search_rebuild).toUpperCase(StaticVariables.locale));
        storageLogButton.setText(c.getString(R.string.search_log).toUpperCase(StaticVariables.locale));

        // Set the button listeners
        menuup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StaticVariables.whichOptionMenu = "MAIN";
                if (mListener!=null) {
                    mListener.prepareOptionMenu();
                }
            }
        });

        storageNewFolderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener!=null) {
                    FullscreenActivity.whattodo = "newfolder";
                    mListener.closeMyDrawers("option");
                    mListener.openFragment();
                }
            }
        });

        storageEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener!=null) {
                    FullscreenActivity.whattodo = "editfoldername";
                    mListener.closeMyDrawers("option");
                    mListener.openFragment();
                }
            }
        });

        storageManageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener!=null) {
                    preferences.setMyPreferenceInt(c, "lastUsedVersion", 0);
                    mListener.closeMyDrawers("option");
                    mListener.splashScreen();
                }
            }
        });

        exportSongListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener!=null) {
                    FullscreenActivity.whattodo = "exportsonglist";
                    mListener.closeMyDrawers("option");
                    mListener.openFragment();
                }
            }
        });

        storageImportOSBButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener!=null) {
                    FullscreenActivity.whattodo = "processimportosb";
                    mListener.selectAFileUri(c.getString(R.string.backup_import));
                    mListener.closeMyDrawers("option");
                }
            }
        });

        storageExportOSBButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener!=null) {
                    FullscreenActivity.whattodo = "exportosb";
                    mListener.closeMyDrawers("option");
                    mListener.openFragment();
                }
            }
        });

        storageImportOnSongButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener!=null) {
                    FullscreenActivity.whattodo = "importos";
                    mListener.closeMyDrawers("option");
                    mListener.selectAFileUri(c.getString(R.string.import_onsong_choose));
                }
            }
        });

        storageSongMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener!=null) {
                    mListener.prepareSongMenu();
                    mListener.closeMyDrawers("option");
                    mListener.openMyDrawers("song");
                    mListener.closeMyDrawers("song_delayed");
                }
            }
        });

        storageDatabaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener!=null) {
                    mListener.closeMyDrawers("option");
                    mListener.rebuildSearchIndex();
                }
            }
        });

        storageLogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener!=null) {
                    FullscreenActivity.whattodo = "errorlog";
                    mListener.closeMyDrawers("option");
                    mListener.openFragment();
                }
            }
        });

        closeOptionsFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener!=null) {
                    mListener.closeMyDrawers("option");
                }
            }
        });
    }

    private static void connectOptionListener(View v, final Context c) {
        mListener = (MyInterface) c;

        // Identify the buttons
        TextView menuUp = v.findViewById(R.id.connectionsMenuTitle);

        // We keep a static reference to these in the FullscreenActivity
        FullscreenActivity.hostButton = v.findViewById(R.id.connectionsHostButton);
        FullscreenActivity.clientButton = v.findViewById(R.id.connectionsGuestButton);
        SwitchCompat connectionsReceiveHostFile = v.findViewById(R.id.connectionsReceiveHostFile);
        FullscreenActivity.connectionsLog = v.findViewById(R.id.options_connections_log);

        if (FullscreenActivity.salutLog==null || FullscreenActivity.salutLog.equals("")) {
            FullscreenActivity.salutLog = c.getResources().getString(R.string.connections_log) + "\n\n";
        }
        FullscreenActivity.connectionsLog.setText(FullscreenActivity.salutLog);

        FloatingActionButton closeOptionsFAB = v.findViewById(R.id.closeOptionsFAB);

        // Capitalise all the text by locale
        connectionsReceiveHostFile.setText(c.getResources().getString(R.string.connections_receive_host).toUpperCase(StaticVariables.locale));
        menuUp.setText(c.getString(R.string.connections_connect).toUpperCase(StaticVariables.locale));
        if (FullscreenActivity.hostButtonText==null || FullscreenActivity.hostButtonText.equals("")) {
            FullscreenActivity.hostButtonText = c.getResources().getString(R.string.connections_service_start).toUpperCase(StaticVariables.locale);
        }
        FullscreenActivity.hostButton.setText(FullscreenActivity.hostButtonText);
        if (FullscreenActivity.clientButtonText==null || FullscreenActivity.clientButtonText.equals("")) {
            FullscreenActivity.clientButtonText = c.getResources().getString(R.string.connections_discover).toUpperCase(StaticVariables.locale);
        }
        FullscreenActivity.clientButton.setText(FullscreenActivity.clientButtonText);

        connectionsReceiveHostFile.setChecked(FullscreenActivity.receiveHostFiles);

        // Set the button listeners
        menuUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StaticVariables.whichOptionMenu = "MAIN";
                if (mListener!=null) {
                    mListener.prepareOptionMenu();
                }
            }
        });
        connectionsReceiveHostFile.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                FullscreenActivity.receiveHostFiles = b;
            }
        });
        FullscreenActivity.connectionsLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullscreenActivity.salutLog = c.getResources().getString(R.string.connections_log) + "\n\n";
                FullscreenActivity.connectionsLog.setText(FullscreenActivity.salutLog);
            }
        });
        FullscreenActivity.hostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setupNetwork(c);
            }
        });
        FullscreenActivity.clientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                discoverServices(c);
            }
        });
        closeOptionsFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener!=null) {
                    mListener.closeMyDrawers("option");
                }
            }
        });

    }

    private static void midiOptionListener(View v, final Context c, final Preferences preferences) {
        mListener = (MyInterface) c;

        // Identify the buttons
        TextView menuUp = v.findViewById(R.id.midiMenuTitle);

        Button midiBluetooth = v.findViewById(R.id.midiBluetooth);
        Button midiUSB = v.findViewById(R.id.midiUSB);
        Button midiCommands = v.findViewById(R.id.midiCommands);
        Button midiSend = v.findViewById(R.id.midiSend);
        SwitchCompat midiAuto = v.findViewById(R.id.midiAuto);
        FloatingActionButton closeOptionsFAB = v.findViewById(R.id.closeOptionsFAB);

        // Capitalise all the text by locale
        midiBluetooth.setText(c.getResources().getString(R.string.midi_bluetooth).toUpperCase(StaticVariables.locale));
        midiUSB.setText(c.getResources().getString(R.string.midi_usb).toUpperCase(StaticVariables.locale));
        midiCommands.setText(c.getString(R.string.midi_commands).toUpperCase(StaticVariables.locale));
        midiSend.setText(c.getString(R.string.midi_send).toUpperCase(StaticVariables.locale));
        midiAuto.setText(c.getString(R.string.midi_auto).toUpperCase(StaticVariables.locale));
        menuUp.setText(c.getString(R.string.midi).toUpperCase(StaticVariables.locale));

        // Set the default
        midiAuto.setChecked(preferences.getMyPreferenceBoolean(c,"midiSendAuto",false));

        // Set the button listeners
        menuUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StaticVariables.whichOptionMenu = "MAIN";
                if (mListener!=null) {
                    mListener.prepareOptionMenu();
                }
            }
        });
        midiBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullscreenActivity.whattodo = "bluetoothmidi";
                if (mListener!=null) {
                    mListener.closeMyDrawers("option");
                    mListener.openFragment();
                }
            }
        });
        midiUSB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullscreenActivity.whattodo = "usbmidi";
                if (mListener!=null) {
                    mListener.closeMyDrawers("option");
                    mListener.openFragment();
                }
            }
        });
        midiCommands.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullscreenActivity.whattodo = "midicommands";
                if (mListener!=null) {
                    mListener.closeMyDrawers("option");
                    mListener.openFragment();
                }
            }
        });
        midiSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullscreenActivity.whattodo = "showmidicommands";
                if (mListener!=null) {
                    mListener.closeMyDrawers("option");
                    mListener.openFragment();
                }
            }
        });
        midiAuto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                preferences.setMyPreferenceBoolean(c,"midiSendAuto",b);
            }
        });
        closeOptionsFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener!=null) {
                    mListener.closeMyDrawers("option");
                }
            }
        });
    }

    private static void setupNetwork(final Context c) {
        Log.d("OptionMenuListener","FullscreenActivity.network="+FullscreenActivity.network);
        if (FullscreenActivity.network!=null) {
            Log.d("OptionMenuListener", "FullscreenActivity.network.isRunningAsHost=" + FullscreenActivity.network.isRunningAsHost);
        }

        if (FullscreenActivity.network!=null && !FullscreenActivity.network.isRunningAsHost) {
            try {
                FullscreenActivity.network.startNetworkService(new SalutDeviceCallback() {
                    @Override
                    public void call(SalutDevice salutDevice) {
                        StaticVariables.myToastMessage = salutDevice.readableName + " - " +
                                c.getResources().getString(R.string.connections_success);
                        FullscreenActivity.salutLog += "\n" + StaticVariables.myToastMessage;
                        FullscreenActivity.connectionsLog.setText(FullscreenActivity.salutLog);
                        ShowToast.showToast(c);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

            FullscreenActivity.hostButtonText = c.getResources().getString(R.string.connections_service_stop).toUpperCase(StaticVariables.locale);
            FullscreenActivity.hostButton.setText(FullscreenActivity.hostButtonText);
            FullscreenActivity.clientButton.setAlpha(0.5f);
            FullscreenActivity.clientButton.setClickable(false);
            StaticVariables.myToastMessage = c.getResources().getString(R.string.connections_broadcast) +
                    " " + FullscreenActivity.mBluetoothName;
            FullscreenActivity.salutLog += "\n" + StaticVariables.myToastMessage;
            FullscreenActivity.connectionsLog.setText(FullscreenActivity.salutLog);
            ShowToast.showToast(c);
        } else {
            try {
                if (FullscreenActivity.network!=null) {
                    FullscreenActivity.network.stopNetworkService(false);
                }
                FullscreenActivity.hostButtonText = c.getResources().getString(R.string.connections_service_start).toUpperCase(StaticVariables.locale);
                FullscreenActivity.hostButton.setText(FullscreenActivity.hostButtonText);
                FullscreenActivity.salutLog += "\n" + c.getResources().getString(R.string.connections_service_stop);
                FullscreenActivity.connectionsLog.setText(FullscreenActivity.salutLog);
                FullscreenActivity.clientButton.setAlpha(1f);
                FullscreenActivity.clientButton.setClickable(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void discoverServices(final Context c) {
        if(FullscreenActivity.network!=null && !FullscreenActivity.network.isRunningAsHost && !FullscreenActivity.network.isDiscovering) {
            try {
                FullscreenActivity.network.discoverNetworkServices(new SalutCallback() {
                    @Override
                    public void call() {
                        SalutDevice hostname = FullscreenActivity.network.foundDevices.get(0);
                        StaticVariables.myToastMessage = c.getResources().getString(R.string.connections_host) +
                                " " + hostname.readableName;
                        FullscreenActivity.salutLog += "\n" + StaticVariables.myToastMessage;
                        FullscreenActivity.connectionsLog.setText(FullscreenActivity.salutLog);
                        ShowToast.showToast(c);
                        registerWithHost(c,hostname);
                    }
                }, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            FullscreenActivity.salutLog += "\n" + c.getResources().getString(R.string.connections_searching);
            FullscreenActivity.connectionsLog.setText(FullscreenActivity.salutLog);
            FullscreenActivity.clientButtonText = c.getResources().getString(R.string.connections_discover_stop).toUpperCase(StaticVariables.locale);
            FullscreenActivity.clientButton.setText(FullscreenActivity.clientButtonText);
            FullscreenActivity.hostButton.setAlpha(0.5f);
            FullscreenActivity.hostButton.setClickable(false);
        } else {
            FullscreenActivity.salutLog += "\n" +c.getResources().getString(R.string.connections_discover_stop);
            FullscreenActivity.connectionsLog.setText(FullscreenActivity.salutLog);
            try {
                FullscreenActivity.network.stopServiceDiscovery(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            FullscreenActivity.clientButtonText = c.getResources().getString(R.string.connections_discover).toUpperCase(StaticVariables.locale);
            FullscreenActivity.clientButton.setText(FullscreenActivity.clientButtonText);
            FullscreenActivity.hostButton.setAlpha(1f);
            FullscreenActivity.hostButton.setClickable(true);
        }

    }

    private static void registerWithHost(final Context c, final SalutDevice possibleHost) {
        try {
            Log.d("OptionMenu","possibleHost="+possibleHost);
            FullscreenActivity.network.registerWithHost(possibleHost, new SalutCallback() {
                @Override
                public void call() {
                    StaticVariables.myToastMessage = c.getResources().getString(R.string.connections_connected) +
                            " " + possibleHost.readableName;
                    FullscreenActivity.salutLog += "\n" + StaticVariables.myToastMessage;
                    FullscreenActivity.connectionsLog.setText(FullscreenActivity.salutLog);
                    ShowToast.showToast(c);
                    FullscreenActivity.clientButtonText = (c.getResources().getString(R.string.connections_disconnect) +
                            " " + possibleHost.readableName).toUpperCase(StaticVariables.locale);
                    FullscreenActivity.clientButton.setText(FullscreenActivity.clientButtonText);

                }
            }, new SalutCallback() {
                @Override
                public void call() {
                    StaticVariables.myToastMessage = possibleHost.readableName + ": " +
                            c.getResources().getString(R.string.connections_failure);
                    FullscreenActivity.salutLog += "\n" + StaticVariables.myToastMessage;
                    FullscreenActivity.connectionsLog.setText(FullscreenActivity.salutLog);
                    ShowToast.showToast(c);
                    try {
                        FullscreenActivity.network.stopServiceDiscovery(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    FullscreenActivity.clientButtonText = c.getResources().getString(R.string.connections_discover).toUpperCase(StaticVariables.locale);
                    FullscreenActivity.clientButton.setText(FullscreenActivity.clientButtonText);
                    FullscreenActivity.hostButton.setAlpha(1f);
                    FullscreenActivity.hostButton.setClickable(true);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void modeOptionListener(View v, final Context c, final Preferences preferences) {
        mListener = (MyInterface) c;

        // Identify the buttons
        TextView menuUp = v.findViewById(R.id.modeMenuTitle);
        Button modePerformanceButton = v.findViewById(R.id.modePerformanceButton);
        Button modeStageButton = v.findViewById(R.id.modeStageButton);
        Button modePresentationButton = v.findViewById(R.id.modePresentationButton);
        FloatingActionButton closeOptionsFAB = v.findViewById(R.id.closeOptionsFAB);
        // ImageView connectionsStatusImage = (ImageView) v.findViewById(R.id.connectionsStatusImage);

        // Capitalise all the text by locale
        menuUp.setText(c.getString(R.string.choose_app_mode).toUpperCase(StaticVariables.locale));
        modePerformanceButton.setText(c.getString(R.string.performancemode).toUpperCase(StaticVariables.locale));
        modeStageButton.setText(c.getString(R.string.stagemode).toUpperCase(StaticVariables.locale));
        modePresentationButton.setText(c.getString(R.string.presentermode).toUpperCase(StaticVariables.locale));

        // Set a tick next to the current mode
        switch (StaticVariables.whichMode) {
            case "Performance":
                modePerformanceButton.setEnabled(false);
                modeStageButton.setEnabled(true);
                modePresentationButton.setEnabled(true);
                break;

            case "Stage":
                modePerformanceButton.setEnabled(true);
                modeStageButton.setEnabled(false);
                modePresentationButton.setEnabled(true);
                break;

            case "Presentation":
                modePerformanceButton.setEnabled(true);
                modeStageButton.setEnabled(true);
                modePresentationButton.setEnabled(false);
                break;

        }
        // Set the button listeners
        menuUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StaticVariables.whichOptionMenu = "MAIN";
                if (mListener!=null) {
                    mListener.prepareOptionMenu();
                }
            }
        });
        modePerformanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!StaticVariables.whichMode.equals("Performance")) {
                    // Switch to performance mode
                    StaticVariables.whichMode = "Performance";
                    preferences.setMyPreferenceString(c,"whichMode","Performance");
                    Intent performmode = new Intent();
                    performmode.setClass(c, StageMode.class);
                    if (mListener!=null) {
                        mListener.closeMyDrawers("option");
                        mListener.callIntent("activity", performmode);
                    }
                }
            }
        });
        modeStageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!StaticVariables.whichMode.equals("Stage")) {
                    // Switch to stage mode
                    StaticVariables.whichMode = "Stage";
                    preferences.setMyPreferenceString(c,"whichMode","Stage");
                    Intent stagemode = new Intent();
                    stagemode.setClass(c, StageMode.class);
                    if (mListener!=null) {
                        mListener.closeMyDrawers("option");
                        mListener.callIntent("activity", stagemode);
                    }
                }
            }
        });
        modePresentationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!StaticVariables.whichMode.equals("Presentation")) {
                    // Switch to presentation mode
                    StaticVariables.whichMode = "Presentation";
                    preferences.setMyPreferenceString(c,"whichMode","Presentation");
                    Intent presentmode = new Intent();
                    presentmode.setClass(c, PresenterMode.class);
                    if (mListener!=null) {
                        mListener.closeMyDrawers("option");
                        mListener.callIntent("activity", presentmode);
                    }
                }
            }
        });

        closeOptionsFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener!=null) {
                    mListener.closeMyDrawers("option");
                }
            }
        });

    }

    private static void gestureOptionListener(View v, final Context c, final Preferences preferences) {
        mListener = (MyInterface) c;

        // Identify the buttons
        TextView menuup = v.findViewById(R.id.optionGestureTitle);
        Button gesturesPedalButton = v.findViewById(R.id.gesturesPedalButton);
        Button gesturesPageButton = v.findViewById(R.id.gesturesPageButton);
        Button gesturesCustomButton = v.findViewById(R.id.gesturesCustomButton);
        Button gesturesMenuOptions = v.findViewById(R.id.gesturesMenuOptions);
        Button gesturesScrollButton = v.findViewById(R.id.gesturesScrollButton);
        SwitchCompat displayMenuToggleSwitch = v.findViewById(R.id.displayMenuToggleSwitch);
        Button gesturesSongSwipeButton = v.findViewById(R.id.gesturesSongSwipeButton);
        FloatingActionButton closeOptionsFAB = v.findViewById(R.id.closeOptionsFAB);

        // Capitalise all the text by locale
        menuup.setText(c.getString(R.string.gesturesandmenus).toUpperCase(StaticVariables.locale));
        gesturesPedalButton.setText(c.getString(R.string.footpedal).toUpperCase(StaticVariables.locale));
        gesturesPageButton.setText(c.getString(R.string.quicklaunch_title).toUpperCase(StaticVariables.locale));
        gesturesCustomButton.setText(c.getString(R.string.custom_gestures).toUpperCase(StaticVariables.locale));
        gesturesMenuOptions.setText(c.getString(R.string.menu_settings).toUpperCase(StaticVariables.locale));
        gesturesScrollButton.setText(c.getString(R.string.scrollbuttons).toUpperCase(StaticVariables.locale));
        displayMenuToggleSwitch.setText(c.getString(R.string.hide_actionbar).toUpperCase(StaticVariables.locale));
        gesturesSongSwipeButton.setText(c.getString(R.string.swipe).toUpperCase(StaticVariables.locale));

        // Set the switches up based on preferences
        displayMenuToggleSwitch.setChecked(preferences.getMyPreferenceBoolean(c,"hideActionBar",false));

        // Set the button listeners
        menuup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StaticVariables.whichOptionMenu = "MAIN";
                if (mListener!=null) {
                    mListener.prepareOptionMenu();
                }
            }
        });

        gesturesPedalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener!=null) {
                    FullscreenActivity.whattodo = "footpedal";
                    mListener.closeMyDrawers("option");
                    mListener.openFragment();
                }
            }
        });

        gesturesPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener!=null) {
                    FullscreenActivity.whattodo = "quicklaunch";
                    mListener.closeMyDrawers("option");
                    mListener.openFragment();
                }
            }
        });

        gesturesCustomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener!=null) {
                    FullscreenActivity.whattodo = "gestures";
                    mListener.closeMyDrawers("option");
                    mListener.openFragment();
                }
            }
        });

        gesturesMenuOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener!=null) {
                    FullscreenActivity.whattodo = "menuoptions";
                    mListener.closeMyDrawers("option");
                    mListener.openFragment();
                }
            }
        });

        gesturesScrollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener!=null) {
                    FullscreenActivity.whattodo = "scrollsettings";
                    mListener.closeMyDrawers("option");
                    mListener.openFragment();
                }
            }
        });

        gesturesSongSwipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener!=null) {
                    FullscreenActivity.whattodo = "swipesettings";
                    mListener.closeMyDrawers("option");
                    mListener.openFragment();
                }
            }
        });
        displayMenuToggleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                preferences.setMyPreferenceBoolean(c,"hideActionBar",b);
                if (mListener!=null) {
                    if (b) {
                        mListener.hideActionBar();
                    } else {
                        mListener.showActionBar();
                    }
                    mListener.loadSong();
                }
            }
        });

        closeOptionsFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener!=null) {
                    mListener.closeMyDrawers("option");
                }
            }
        });

    }

    private static void autoscrollOptionListener(View v, final Context c, final Preferences preferences) {
        mListener = (MyInterface) c;

        // Identify the buttons
        TextView menuup = v.findViewById(R.id.optionAutoScrollTitle);
        Button autoScrollButton = v.findViewById(R.id.autoScrollButton);
        Button autoScrollTimeDefaultsButton = v.findViewById(R.id.autoScrollTimeDefaultsButton);
        Button autoScrollLearnButton = v.findViewById(R.id.autoScrollLearnButton);
        SwitchCompat switchTimerSize = v.findViewById(R.id.switchTimerSize);
        SwitchCompat autoScrollStartButton = v.findViewById(R.id.autoScrollStartButton);
        SwitchCompat autoscrollActivatedSwitch = v.findViewById(R.id.autoscrollActivatedSwitch);
        FloatingActionButton closeOptionsFAB = v.findViewById(R.id.closeOptionsFAB);

        // Capitalise all the text by locale
        menuup.setText(c.getString(R.string.autoscroll).toUpperCase(StaticVariables.locale));
        autoScrollButton.setText(c.getString(R.string.autoscroll).toUpperCase(StaticVariables.locale));
        autoScrollTimeDefaultsButton.setText(c.getString(R.string.default_autoscroll).toUpperCase(StaticVariables.locale));
        autoScrollStartButton.setText(c.getString(R.string.autostart_autoscroll).toUpperCase(StaticVariables.locale));
        autoscrollActivatedSwitch.setText(c.getString(R.string.activated).toUpperCase(StaticVariables.locale));
        switchTimerSize.setText(c.getString(R.string.timer_size).toUpperCase(StaticVariables.locale));
        autoScrollLearnButton.setText(c.getString(R.string.timer_learn).toUpperCase(StaticVariables.locale));

        // Set the switches up based on preferences
        autoScrollStartButton.setChecked(preferences.getMyPreferenceBoolean(c,"autoscrollAutoStart",false));
        autoscrollActivatedSwitch.setChecked(StaticVariables.clickedOnAutoScrollStart);

        switchTimerSize.setChecked(preferences.getMyPreferenceBoolean(c,"autoscrollLargeFontInfoBar",true));

        // Set the button listeners
        menuup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StaticVariables.whichOptionMenu = "MAIN";
                if (mListener!=null) {
                    mListener.prepareOptionMenu();
                }
            }
        });

        autoScrollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FullscreenActivity.isSong) {
                    FullscreenActivity.whattodo = "page_autoscroll";
                    if (mListener != null) {
                        mListener.closeMyDrawers("option");
                        mListener.openFragment();
                    }
                }
            }
        });
        autoscrollActivatedSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                StaticVariables.clickedOnAutoScrollStart = b;
                if (!b && mListener!=null) {
                    mListener.stopAutoScroll();
                }
            }
        });
        autoScrollTimeDefaultsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener!=null) {
                    FullscreenActivity.whattodo = "autoscrolldefaults";
                    mListener.closeMyDrawers("option");
                    mListener.openFragment();
                }
            }
        });

        autoScrollStartButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                preferences.setMyPreferenceBoolean(c,"autoscrollAutoStart",b);
            }
        });

        autoScrollLearnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener!=null) {
                    mListener.closeMyDrawers("option");
                    mListener.prepareLearnAutoScroll();
                }
            }
        });
        switchTimerSize.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                preferences.setMyPreferenceBoolean(c,"autoscrollLargeFontInfoBar",b);
                if (mListener!=null) {
                    mListener.updateExtraInfoColorsAndSizes("autoscroll");
                }
            }
        });

        closeOptionsFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener!=null) {
                    mListener.closeMyDrawers("option");
                }
            }
        });
    }

    private static void padOptionListener(View v, final Context c, final Preferences preferences) {
        mListener = (MyInterface) c;

        // Identify the buttons
        TextView menuup = v.findViewById(R.id.optionPadTitle);
        Button padButton = v.findViewById(R.id.padButton);
        Button padCrossFadeButton = v.findViewById(R.id.padCrossFadeButton);
        Button padCustomButton = v.findViewById(R.id.padCustomButton);
        FloatingActionButton closeOptionsFAB = v.findViewById(R.id.closeOptionsFAB);
        SwitchCompat switchTimerSize = v.findViewById(R.id.switchTimerSize);
        SwitchCompat padStartButton = v.findViewById(R.id.padStartButton);
        SwitchCompat padActivatedSwitch = v.findViewById(R.id.padActivatedSwitch);


        // Capitalise all the text by locale
        padButton.setText(c.getString(R.string.pad).toUpperCase(StaticVariables.locale));
        menuup.setText(c.getString(R.string.pad).toUpperCase(StaticVariables.locale));
        padStartButton.setText(c.getString(R.string.autostartpad).toUpperCase(StaticVariables.locale));
        padCustomButton.setText(c.getString(R.string.custom).toUpperCase(StaticVariables.locale));
        padCrossFadeButton.setText(c.getString(R.string.crossfade_time).toUpperCase(StaticVariables.locale));
        padActivatedSwitch.setText(c.getString(R.string.activated).toUpperCase(StaticVariables.locale));
        switchTimerSize.setText(c.getString(R.string.timer_size).toUpperCase(StaticVariables.locale));

        // Set the switch
        switchTimerSize.setChecked(preferences.getMyPreferenceBoolean(c,"padLargeFontInfoBar",true));

        padStartButton.setChecked(preferences.getMyPreferenceBoolean(c,"padAutoStart",false));
        padActivatedSwitch.setChecked(StaticVariables.clickedOnPadStart);
        // Set the button listeners
        menuup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StaticVariables.whichOptionMenu = "MAIN";
                if (mListener!=null) {
                    mListener.prepareOptionMenu();
                }
            }
        });

        padButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FullscreenActivity.isSong) {
                    FullscreenActivity.whattodo = "page_pad";
                    if (mListener != null) {
                        mListener.closeMyDrawers("option");
                        mListener.openFragment();
                    }
                }
            }
        });

        padStartButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                preferences.setMyPreferenceBoolean(c,"padAutoStart",b);
            }
        });
        padCustomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener!=null) {
                    mListener.closeMyDrawers("option");
                    FullscreenActivity.whattodo = "custompads";
                    mListener.openFragment();
                }
            }
        });
        padCrossFadeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullscreenActivity.whattodo = "crossfade";
                if (mListener!=null) {
                    mListener.closeMyDrawers("option");
                    mListener.openFragment();
                }
            }
        });

        closeOptionsFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener!=null) {
                    mListener.closeMyDrawers("option");
                }
            }
        });
        switchTimerSize.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                preferences.setMyPreferenceBoolean(c,"autoscrollLargeFontInfoBar",b);
                if (mListener!=null) {
                    mListener.updateExtraInfoColorsAndSizes("pad");
                }
            }
        });
        padActivatedSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                StaticVariables.clickedOnPadStart = b;
                if (!b && mListener!=null) {
                    mListener.killPad();
                }
            }
        });

    }

    private static void metronomeOptionListener(View v, final Context c, final Preferences preferences) {
        mListener = (MyInterface) c;

        // Identify the buttons
        TextView menuup = v.findViewById(R.id.optionMetronomeTitle);
        Button metronomeButton = v.findViewById(R.id.metronomeButton);
        SwitchCompat metronomeStartButton = v.findViewById(R.id.metronomeStartButton);
        SwitchCompat metronomeActivatedSwitch = v.findViewById(R.id.metronomeActivatedSwitch);
        FloatingActionButton closeOptionsFAB = v.findViewById(R.id.closeOptionsFAB);

        // Capitalise all the text by locale
        menuup.setText(c.getString(R.string.metronome).toUpperCase(StaticVariables.locale));
        metronomeButton.setText(c.getString(R.string.metronome).toUpperCase(StaticVariables.locale));
        metronomeActivatedSwitch.setText(c.getString(R.string.activated).toUpperCase(StaticVariables.locale));
        metronomeStartButton.setText(c.getString(R.string.autostartmetronome).toUpperCase(StaticVariables.locale));

        // Set the switches up based on preferences
        metronomeStartButton.setChecked(preferences.getMyPreferenceBoolean(c,"metronomeAutoStart",false));
        metronomeActivatedSwitch.setChecked(StaticVariables.clickedOnMetronomeStart);

        // Set the button listeners
        menuup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StaticVariables.whichOptionMenu = "MAIN";
                if (mListener!=null) {
                    mListener.prepareOptionMenu();
                }
            }
        });

        metronomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FullscreenActivity.isSong) {
                    FullscreenActivity.whattodo = "page_metronome";
                    if (mListener != null) {
                        mListener.closeMyDrawers("option");
                        mListener.openFragment();
                    }
                }
            }
        });
        metronomeStartButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                preferences.setMyPreferenceBoolean(c,"metronomeAutoStart",b);
            }
        });

        metronomeActivatedSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                StaticVariables.clickedOnMetronomeStart = b;
                if (!b && mListener!=null) {
                    if (StaticVariables.metronomeonoff.equals("on")) {
                        mListener.stopMetronome();
                    }
                }
            }
        });

        closeOptionsFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener!=null) {
                    mListener.closeMyDrawers("option");
                }
            }
        });
    }

    private static void ccliOptionListener(View v, final Context c, final Preferences preferences) {
        mListener = (MyInterface) c;

        // Identify the buttons
        TextView menuup = v.findViewById(R.id.optionCCLITitle);
        Button ccliChurchButton = v.findViewById(R.id.ccliChurchButton);
        Button ccliLicenceButton = v.findViewById(R.id.ccliLicenceButton);
        SwitchCompat ccliAutoButton = v.findViewById(R.id.ccliAutoButton);
        Button ccliViewButton = v.findViewById(R.id.ccliViewButton);
        Button ccliExportButton = v.findViewById(R.id.ccliExportButton);
        Button ccliResetButton = v.findViewById(R.id.ccliResetButton);
        FloatingActionButton closeOptionsFAB = v.findViewById(R.id.closeOptionsFAB);

        // Capitalise all the text by locale
        String mcname = preferences.getMyPreferenceString(c,"ccliChurchName","");
        String mcnum  = preferences.getMyPreferenceString(c,"ccliLicence","");
        if (!mcname.isEmpty()) {
            mcname = "\n"+mcname;
        }
        if (!mcnum.isEmpty()) {
            mcnum = "\n"+mcnum;
        }
        String cname = c.getString(R.string.ccli_church).toUpperCase(StaticVariables.locale) + mcname;
        String clice = c.getString(R.string.ccli_licence).toUpperCase(StaticVariables.locale) + mcnum;
        menuup.setText(c.getString(R.string.edit_song_ccli).toUpperCase(StaticVariables.locale));
        ccliChurchButton.setText(cname);
        ccliLicenceButton.setText(clice);
        ccliAutoButton.setText(c.getString(R.string.ccli_automatic).toUpperCase(StaticVariables.locale));
        ccliViewButton.setText(c.getString(R.string.ccli_view).toUpperCase(StaticVariables.locale));
        ccliExportButton.setText(c.getString(R.string.export).toUpperCase(StaticVariables.locale));
        ccliResetButton.setText(c.getString(R.string.ccli_reset).toUpperCase(StaticVariables.locale));

        // Set the switches up based on preferences
        ccliAutoButton.setChecked(preferences.getMyPreferenceBoolean(c,"ccliAutomaticLogging",false));

        // Set the button listeners
        menuup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StaticVariables.whichOptionMenu = "MAIN";
                if (mListener!=null) {
                    mListener.prepareOptionMenu();
                }
            }
        });

        ccliAutoButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                preferences.setMyPreferenceBoolean(c,"ccliAutomaticLogging",b);
            }
        });
        ccliChurchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullscreenActivity.whattodo = "ccli_church";
                if (mListener!=null) {
                    mListener.openFragment();
                }
            }
        });
        ccliLicenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullscreenActivity.whattodo = "ccli_licence";
                if (mListener!=null) {
                    mListener.openFragment();
                }
            }
        });
        ccliViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullscreenActivity.whattodo = "ccli_view";
                if (mListener!=null) {
                    mListener.openFragment();
                }
            }
        });
        ccliExportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullscreenActivity.whattodo = "ccli_export";
                if (mListener!=null) {
                    mListener.doExport();
                }
            }
        });
        ccliResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullscreenActivity.whattodo = "ccli_reset";
                if (mListener!=null) {
                    mListener.openFragment();
                }
            }
        });
        closeOptionsFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener!=null) {
                    mListener.closeMyDrawers("option");
                }
            }
        });
    }

    private static void otherOptionListener(View v, final Context c, final Preferences preferences) {
        mListener = (MyInterface) c;

        // Identify the buttons
        TextView menuup = v.findViewById(R.id.optionOtherTitle);
        Button otherHelpButton = v.findViewById(R.id.otherHelpButton);
        Button otherTweetButton = v.findViewById(R.id.otherTweetButton);
        Button otherLanguageButton = v.findViewById(R.id.otherLanguageButton);
        Button otherStartButton = v.findViewById(R.id.otherStartButton);
        Button otherRateButton = v.findViewById(R.id.otherRateButton);
        Button otherPayPalButton = v.findViewById(R.id.otherPayPalButton);
        Button otherEmailButton = v.findViewById(R.id.otherEmailButton);
        FloatingActionButton closeOptionsFAB = v.findViewById(R.id.closeOptionsFAB);

        // Capitalise all the text by locale
        menuup.setText(c.getString(R.string.other).toUpperCase(StaticVariables.locale));
        otherHelpButton.setText(c.getString(R.string.help).toUpperCase(StaticVariables.locale));
        otherLanguageButton.setText(c.getString(R.string.language).toUpperCase(StaticVariables.locale));
        otherStartButton.setText(c.getString(R.string.start_screen).toUpperCase(StaticVariables.locale));
        otherRateButton.setText(c.getString(R.string.rate).toUpperCase(StaticVariables.locale));
        otherEmailButton.setText(c.getString(R.string.forum).toUpperCase(StaticVariables.locale));
        otherPayPalButton.setText(c.getString(R.string.paypal).toUpperCase(StaticVariables.locale));

        // Set the button listeners
        menuup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StaticVariables.whichOptionMenu = "MAIN";
                if (mListener!=null) {
                    mListener.prepareOptionMenu();
                }
            }
        });

        otherHelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://www.opensongapp.com/user-guide";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                if (mListener!=null) {
                    mListener.closeMyDrawers("option");
                    mListener.callIntent("web",i);
                }
            }
        });

        otherTweetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener!=null) {
                    mListener.closeMyDrawers("option");
                    mListener.callIntent("twitter",null);
                }
            }
        });

        otherEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://www.opensongapp.com/forum";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                if (mListener!=null) {
                    mListener.closeMyDrawers("option");
                    mListener.callIntent("web",i);
                }
            }
        });
        otherLanguageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener!=null) {
                    FullscreenActivity.whattodo = "language";
                    mListener.closeMyDrawers("option");
                    mListener.openFragment();
                }
            }
        });

        otherStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener!=null) {
                    // Set the last used version to 1
                    // Setting to 0 is now only for fresh installs
                    preferences.setMyPreferenceInt(c, "lastUsedVersion", 1);
                    mListener.closeMyDrawers("option");
                    mListener.splashScreen();
                }
            }
        });

        otherRateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener!=null) {
                    // Rate this app
                    String appPackage = c.getPackageName();
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackage));
                    mListener.closeMyDrawers("option");
                    mListener.callIntent("web", i);
                }
            }
        });

        otherPayPalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener!=null) {
                    // PayPal.Me
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.paypal.me/opensongapp"));
                    mListener.closeMyDrawers("option");
                    mListener.callIntent("web", i);
                }
            }
        });

        closeOptionsFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener!=null) {
                    mListener.closeMyDrawers("option");
                }
            }
        });
    }

    public static void updateMenuVersionNumber(Context c, TextView showVersion) {
        // Update the app version in the menu
        PackageInfo pinfo;
        int versionNumber = 0;
        String versionName = "?";
        try {
            pinfo = c.getPackageManager().getPackageInfo(c.getPackageName(), 0);
            versionNumber = pinfo.versionCode;
            versionName = pinfo.versionName;
        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
        }

        if (!versionName.equals("?") && versionNumber > 0) {
            String temptext = "V" + versionName + " (" + versionNumber + ")";
            if (showVersion != null) {
                showVersion.setVisibility(View.VISIBLE);
                showVersion.setText(temptext);
            }
        } else {
            if (showVersion != null) {
                showVersion.setVisibility(View.GONE);
            }
        }
    }
}