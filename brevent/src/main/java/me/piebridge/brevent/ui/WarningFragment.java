package me.piebridge.brevent.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;

import me.piebridge.brevent.BuildConfig;
import me.piebridge.brevent.R;

/**
 * Created by thom on 2018/1/4.
 */
public class WarningFragment extends AbstractDialogFragment
        implements DialogInterface.OnClickListener {

    private static final String MESSAGE = "message";

    private static final String EXTRA = "extra";

    public WarningFragment() {
        setArguments(new Bundle());
        setCancelable(false);
        setStyle(STYLE_NO_TITLE, 0);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(BuildConfig.ICON);
        builder.setTitle(getString(R.string.brevent) + " " + BuildConfig.VERSION_NAME);
        int message = getMessage();
        if (message == R.string.unsupported_no_event2) {
            CharSequence extra = getArguments().getCharSequence(EXTRA);
            Resources resources = builder.getContext().getResources();
            builder.setMessage(resources.getString(R.string.unsupported_no_event2, extra));
        } else {
            builder.setMessage(message);
        }
        builder.setPositiveButton(android.R.string.ok, this);
        if (shouldShowCancel()) {
            builder.setNegativeButton(android.R.string.cancel, this);
        }
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        AbstractActivity activity = (AbstractActivity) getActivity();
        if (activity == null || activity.isStopped()) {
            return;
        }
        if (which == DialogInterface.BUTTON_POSITIVE) {
            onClickOk();
        } else if (which == DialogInterface.BUTTON_NEGATIVE) {
            onClickCancel();
        }
    }

    private boolean shouldShowCancel() {
        int message = getMessage();
        switch (message) {
            case R.string.unsupported_granted:
            case R.string.unsupported_granted_miui:
            case R.string.unsupported_checking:
            case R.string.cmd_warning:
            case R.string.unsupported_brevented:
                return true;
            default:
                return false;
        }
    }

    private void onClickOk() {
        int message = getMessage();
        switch (message) {
            case R.string.unsupported_granted:
            case R.string.unsupported_granted_miui:
                ((BreventApplication) getActivity().getApplication()).launchDevelopmentSettings();
                break;
            case R.string.unsupported_checking:
                ((BreventActivity) getActivity()).onUnsupportedChecking();
                break;
            case R.string.cmd_warning:
                break;
            case R.string.unsupported_brevented:
                ((BreventActivity) getActivity()).unbreventImportant(true);
                break;
            default:
                break;
        }
    }

    private void onClickCancel() {
        int message = getMessage();
        switch (message) {
            case R.string.unsupported_granted:
            case R.string.unsupported_granted_miui:
                ((BreventApplication) getActivity().getApplication()).setGrantedWarned(true);
                break;
            case R.string.cmd_warning:
                getActivity().finish();
                break;
            case R.string.unsupported_brevented:
                ((BreventActivity) getActivity()).unsetBreventImportant();
            default:
                break;
        }
    }

    public void setMessage(int resId) {
        getArguments().putInt(MESSAGE, resId);
    }

    public int getMessage() {
        return getArguments().getInt(MESSAGE);
    }

    public void setExtra(CharSequence extra) {
        getArguments().putCharSequence(EXTRA, extra);
    }

}
