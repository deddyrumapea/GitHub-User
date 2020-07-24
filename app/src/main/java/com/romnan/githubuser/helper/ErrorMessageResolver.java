package com.romnan.githubuser.helper;

import android.content.Context;

import com.romnan.githubuser.R;

public class ErrorMessageResolver {

    public static String getErrorMessageString(Context context, int errorCode) {
        String errorMessage;
        switch (errorCode) {
            case 0:
                errorMessage = context.getString(R.string.error_0);
                break;
            case 69:
                errorMessage = context.getString(R.string.error_69);
                break;
            case 70:
                errorMessage = context.getString(R.string.error_70);
                break;
            case 71:
                errorMessage = context.getString(R.string.error_71);
                break;
            case 401:
                errorMessage = context.getString(R.string.error_401);
                break;
            case 403:
                errorMessage = context.getString(R.string.error_403);
                break;
            case 404:
                errorMessage = context.getString(R.string.error_404);
                break;
            default:
                errorMessage = String.format(context.getString(R.string.error_default), errorCode);
        }
        return errorMessage;
    }
}
