package com.qinqi.debugtoolbox.dbinspector.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.qinqi.debugtoolbox.R;

import java.io.File;

import static com.qinqi.debugtoolbox.dbinspector.services.ClearTableIntentService.deleteTable;

/**
 * Shows a confirmation dialog before deleting the content of a table.
 */
public class ClearTableAlertDialogFragment extends DialogFragment {

    private static final String KEY_DATABASE = "database";
    private static final String KEY_TABLE = "table";

    public static ClearTableAlertDialogFragment newInstance(final File databaseFile, final String table) {
        ClearTableAlertDialogFragment frag = new ClearTableAlertDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_DATABASE, databaseFile);
        args.putString(KEY_TABLE, table);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final File databaseFile = (File) getArguments().getSerializable(KEY_DATABASE);
        final String tableName = getArguments().getString(KEY_TABLE);
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.dbinspector_clear_table_confirm)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                deleteTable(getActivity(), databaseFile, tableName);
                            }
                        }
                )
                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dismiss();
                            }
                        }
                )
                .create();
    }
}
