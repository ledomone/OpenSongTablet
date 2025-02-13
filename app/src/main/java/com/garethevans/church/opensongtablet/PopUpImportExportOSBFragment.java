package com.garethevans.church.opensongtablet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.DocumentsContract;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class PopUpImportExportOSBFragment extends DialogFragment {

    static PopUpImportExportOSBFragment newInstance() {
        PopUpImportExportOSBFragment frag;
        frag = new PopUpImportExportOSBFragment();
        return frag;
    }

    public interface MyInterface {
        void openFragment();
        void backupInstall();
        void selectAFileUri(String s);
    }

    private PopUpImportExportOSBFragment.MyInterface mListener;

    @Override
    @SuppressWarnings("deprecation")
    public void onAttach(Activity activity) {
        mListener = (PopUpImportExportOSBFragment.MyInterface) activity;
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    private LinearLayout zipcontents;
    private TextView currentFileWork;
    private ListView folderlist;
    private String selectednote;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> foldersfoundinzip;
    private LinearLayout progressUpdate;
    private String[] foldersselectedtoimport;
    private String message = "";
    private ProgressBar waiting;
    private FloatingActionButton saveMe;
    private SwitchCompat overwrite;
    private StorageAccess storageAccess;
    private ExportPreparer exportPreparer;
    private Preferences preferences;
    private ProgressBar progressBar;
    private SongFolders songFolders;
    private String error;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            this.dismiss();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Decide if we are importing or exporting
        String mTitle;
        if (FullscreenActivity.whattodo.equals("processimportosb")) {
            mTitle = Objects.requireNonNull(getActivity()).getResources().getString(R.string.backup_import);
        } else {
            mTitle = Objects.requireNonNull(getActivity()).getResources().getString(R.string.backup_export);
        }
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(false);

        View V = inflater.inflate(R.layout.popup_importexportosb, container, false);

        songFolders = new SongFolders();
        storageAccess = new StorageAccess();
        preferences = new Preferences();
        error = getActivity().getResources().getString(R.string.backup_error);

        TextView title = V.findViewById(R.id.dialogtitle);
        title.setText(mTitle);
        final FloatingActionButton closeMe = V.findViewById(R.id.closeMe);
        closeMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopUpImportExportOSBFragment.this.dismiss();
            }
        });
        saveMe = V.findViewById(R.id.saveMe);
        if (FullscreenActivity.whattodo.equals("processimportosb")) {
            saveMe.hide();
            saveMe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CustomAnimations.animateFAB(saveMe, PopUpImportExportOSBFragment.this.getActivity());
                    saveMe.setEnabled(false);
                    PopUpImportExportOSBFragment.this.doTheImporting();
                }
            });
        } else {
            saveMe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CustomAnimations.animateFAB(saveMe, PopUpImportExportOSBFragment.this.getActivity());
                    saveMe.setEnabled(false);
                    PopUpImportExportOSBFragment.this.doTheExporting();
                }
            });
        }

        // Initialise the views
        LinearLayout importfilechooser = V.findViewById(R.id.importfilechooser);
        zipcontents = V.findViewById(R.id.zipcontents);
        TextView chooseosbfile = V.findViewById(R.id.chooseosbfile);
        folderlist = V.findViewById(R.id.folderlist);
        waiting = V.findViewById(R.id.waiting);
        overwrite = V.findViewById(R.id.overwrite);
        progressBar = V.findViewById(R.id.progressBar);
        currentFileWork = V.findViewById(R.id.currentFileWork);
        progressUpdate = V.findViewById(R.id.progressUpdate);
        progressUpdate.setVisibility(View.GONE);

        // Listener for choose osb file
        chooseosbfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.selectAFileUri(Objects.requireNonNull(getActivity()).getString(R.string.backup_import));
                    mListener.openFragment();
                }
                PopUpImportExportOSBFragment.this.dismiss();
            }
        });


        // Set up the folder list and hide the required layouts
        if (FullscreenActivity.whattodo.equals("processimportosb")) {
            importfilechooser.setVisibility(View.VISIBLE);
            overwrite.setVisibility(View.GONE);
            zipcontents.setVisibility(View.GONE);
        } else {
            importfilechooser.setVisibility(View.GONE);
            overwrite.setVisibility(View.GONE);
            zipcontents.setVisibility(View.VISIBLE);
            prepareFolderListExport();
        }

        if (FullscreenActivity.file_uri!=null && FullscreenActivity.whattodo.equals("processimportosb")) {
            // We must be importing and have selected an appropriate .osb file
            importfilechooser.setVisibility(View.VISIBLE);
            String nameoffile = FullscreenActivity.file_uri.getLastPathSegment();
            if (nameoffile != null && nameoffile.contains("/")) {
                nameoffile = nameoffile.substring(nameoffile.lastIndexOf("/") + 1);
            }
            chooseosbfile.setText(nameoffile);
            waiting.setVisibility(View.VISIBLE);
            prepareFolderListImport();
        }
        // Reset the folders selected text note
        selectednote = "";

        final ArrayList<String> songfolders = songFolders.prepareSongFolders(getActivity(),preferences);

        folderlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (folderlist.isItemChecked(i)) {
                    // Add the folder if it isn't there already
                    if (!selectednote.contains("%__" + songfolders.get(i) + "__%")) {
                        selectednote = selectednote + "%__" + songfolders.get(i) + "__%";
                    }

                } else {
                    // Remove the folder if it is there already
                    if (selectednote.contains("%__" + songfolders.get(i) + "__%")) {
                        selectednote = selectednote.replace("%__" + songfolders.get(i) + "__%", "");
                    }
                }
            }
        });
        PopUpSizeAndAlpha.decoratePopUp(getActivity(),getDialog(), preferences);

        return V;
    }

    private void prepareFolderListExport() {

        ArrayList<String> songfolders = songFolders.prepareSongFolders(getActivity(),preferences);
        adapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), android.R.layout.simple_list_item_multiple_choice, songfolders);
        folderlist.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        folderlist.setAdapter(adapter);
    }

    private void doTheExporting() {
        // Get a note of the folders chosen and add them to a string
        progressBar.setVisibility(View.VISIBLE);
        exportPreparer = new ExportPreparer();
        new Thread(new Runnable() {
            @Override
            public void run() {
                exportPreparer.createSelectedOSB(getActivity(), preferences, selectednote, storageAccess);
                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                    }
                });
                try {
                    dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void prepareFolderListImport() {
        // We need to parse the .osb (zip) file to extract a list of folders it contains as AsyncTask
        PrepareFolderListImport prepare_folder_list_import = new PrepareFolderListImport();
        waiting.setVisibility(View.VISIBLE);
        prepare_folder_list_import.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
    @SuppressLint("StaticFieldLeak")
    private class PrepareFolderListImport extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            ZipInputStream zis = null;
            foldersfoundinzip = new ArrayList<>();
            foldersfoundinzip.clear();
            try {
                InputStream inputStream = storageAccess.getInputStream(getActivity(),FullscreenActivity.file_uri);
                zis = new ZipInputStream(new BufferedInputStream(inputStream));
                ZipEntry ze;
                while ((ze = zis.getNextEntry()) != null) {
                    if (ze.toString().contains("/")) {
                        int i = ze.toString().lastIndexOf("/");
                        String tf = ze.toString().substring(0,i);
                        if (!foldersfoundinzip.contains(tf)) {
                            foldersfoundinzip.add(tf);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                message = Objects.requireNonNull(getActivity()).getResources().getString(R.string.backup_error);
            } finally {
                if (zis!=null) {
                    try {
                        zis.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        message = Objects.requireNonNull(getActivity()).getString(R.string.backup_error);
                    }
                }
            }
            return message;
        }

        @Override
        protected void onPostExecute(String s) {

            if (s!=null && getActivity()!=null && !s.equals(getActivity().getResources().getString(R.string.backup_error))) {
                // Sort the folders
                Collator coll = Collator.getInstance(StaticVariables.locale);
                coll.setStrength(Collator.SECONDARY);
                Collections.sort(foldersfoundinzip, coll);

                // Add the main folder
                foldersfoundinzip.add(0, getActivity().getString(R.string.mainfoldername));
                waiting.setVisibility(View.GONE);

                zipcontents.setVisibility(View.VISIBLE);
                overwrite.setVisibility(View.VISIBLE);
                saveMe.hide();  // Will be available when a folder is selected
                adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_multiple_choice, foldersfoundinzip);
                folderlist.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                folderlist.setAdapter(adapter);
                folderlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        // Check to see if we have selected any folders.  If not, hide the save button
                        boolean oktoproceed = false;
                        for (int y = 0; y < foldersfoundinzip.size(); y++) {
                            if (folderlist.isItemChecked(y)) {
                                oktoproceed = true;
                            }
                        }
                        if (oktoproceed) {
                            saveMe.show();
                        } else {
                            saveMe.hide();
                        }
                    }
                });
            } else {
                // There was an error, so show the message and hide the progressbar
                waiting.setVisibility(View.GONE);
                StaticVariables.myToastMessage = message;
                ShowToast.showToast(getActivity());
            }
        }
    }
    private void doTheImporting() {
        // Send an alert to the screen
        StaticVariables.myToastMessage = Objects.requireNonNull(getActivity()).getString(R.string.wait);
        ShowToast.showToast(getActivity());
        // Check the selected folders
        selectednote = "";
        StringBuilder sb = new StringBuilder();

        for (int i=0; i<foldersfoundinzip.size(); i++) {
            if (folderlist.isItemChecked(i)) {
                sb.append("%__").append(foldersfoundinzip.get(i)).append("/__%");
            }
        }

        selectednote = sb.toString();

        // Split
        foldersselectedtoimport = selectednote.split("__%%__");
        for (int i=0; i<foldersselectedtoimport.length; i++) {
            foldersselectedtoimport[i] = foldersselectedtoimport[i].replace("%__","");
            foldersselectedtoimport[i] = foldersselectedtoimport[i].replace("__%","");
        }

        // Now import the stuff via an AsyncTask
        SelectedFolderImport selected_folder_import = new SelectedFolderImport();
        selected_folder_import.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @SuppressLint("StaticFieldLeak")
    private class SelectedFolderImport extends AsyncTask<String, String, String> {

        boolean canoverwrite;
        InputStream inputStream;
        ZipInputStream zis = null;
        int numzips;
        int numfile = 0;

        @Override
        protected void onPreExecute() {
            canoverwrite = overwrite.isChecked();

            // Initialise the zip file
            inputStream = storageAccess.getInputStream(getActivity(),FullscreenActivity.file_uri);
            zis = new ZipInputStream(new BufferedInputStream(inputStream));
            try {
                while (zis.getNextEntry()!=null) {
                    numzips ++;
                }
                zis.close();
                inputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
                numzips = 0;
            }

            inputStream = storageAccess.getInputStream(getActivity(),FullscreenActivity.file_uri);
            zis = new ZipInputStream(new BufferedInputStream(inputStream));

            // Show the progressbar
            progressBar.setMax(numzips);
            progressUpdate.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            ArrayList<String> createdfolders = new ArrayList<>();
            createdfolders.clear();

            try {
                ZipEntry ze;

                // Create a reference to the main songs folder
                createdfolders.add("MAIN");

                byte[] buffer = new byte[8192];
                while ((ze = zis.getNextEntry()) != null) {
                    int count;
                    numfile ++;
                    // Look to see if ze is in one of the folders we are wanting to import
                    boolean oktoimportthisone = false;

                    for (String aFoldersselectedtoimport : foldersselectedtoimport) {
                        // Is it in the main folder
                        if (aFoldersselectedtoimport.equals(getString(R.string.mainfoldername) + "/") && !ze.getName().contains("/")) {
                            oktoimportthisone = true;
                            // Or another folder
                        } else if (ze.getName().contains(aFoldersselectedtoimport)) {
                            oktoimportthisone = true;
                        }
                    }

                    if (oktoimportthisone) {

                        // If the ze.getName() is a directory, then check to see if the directory is created locally
                        // If not, add it
                        if (ze.isDirectory()) {
                            if (!createdfolders.contains(ze.getName())) {
                                storageAccess.createFile(getActivity(), preferences, DocumentsContract.Document.MIME_TYPE_DIR,
                                        "Songs", ze.getName(), "");
                                createdfolders.add(ze.getName());
                                //documentFolders.add(DocumentFile.fromSingleUri(getActivity(),folder_uri));
                                publishProgress(numfile + "&&_" + ze.getName());
                            }
                        }

                        // If this is a file, check if it exists, if not, create it
                        if (!ze.isDirectory()) {
                            // Get a uri for the song
                            Uri file_uri = storageAccess.getUriForItem(getActivity(), preferences, "Songs", "", ze.getName());
                            publishProgress(numfile + "&&_" + ze.getName());

                            // If we are lollipop or later, we need to create a file for the output stream to work
                            boolean exists = storageAccess.uriExists(getActivity(), file_uri);
                            if (!exists) {
                                storageAccess.lollipopCreateFileForOutputStream(getActivity(), preferences,
                                        file_uri, null, "Songs", "", ze.getName());
                            }


                            // If we have allowed overwriting, get an output stream and write it
                            if (!exists || canoverwrite) {
                                OutputStream outputStream = null;
                                if (file_uri!=null) {
                                    Log.d("ImportExportOSB","file_uri="+file_uri);
                                    outputStream = storageAccess.getOutputStream(getActivity(), file_uri);
                                }

                                // Write the contents
                                try {
                                    if (outputStream!=null) {
                                        while ((count = zis.read(buffer)) != -1) {
                                            outputStream.write(buffer, 0, count);
                                        }
                                    } else {
                                        Log.d("d","outputStream=null");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                } finally {
                                    try {
                                        if (outputStream!=null) {
                                            outputStream.close();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                    zis.closeEntry();
                }
            } catch (Exception e) {
                e.printStackTrace();
                message = error;

            } finally {
                if (zis!=null) {
                    try {
                        zis.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        message = error;
                    }
                }
            }
            return message;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            // Split by &&_
            String[] bits = values[0].split("&&_");
            int progress = Integer.parseInt(bits[0]);
            progressBar.setProgress(progress);
            currentFileWork.setText(bits[1]);
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                if (mListener != null) {
                    mListener.backupInstall();
                }
                dismiss();
            } catch (Exception e) {
                e.printStackTrace();
                dismiss();
            }
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        try {
            this.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}