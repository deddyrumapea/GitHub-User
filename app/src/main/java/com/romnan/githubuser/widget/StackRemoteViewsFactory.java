package com.romnan.githubuser.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Binder;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.romnan.githubuser.R;
import com.romnan.githubuser.database.DatabaseContract;
import com.romnan.githubuser.helper.MappingHelper;
import com.romnan.githubuser.model.User;

import java.util.ArrayList;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private ArrayList<User> mWidgetItems = new ArrayList<>();
    private final Context mContext;

    public StackRemoteViewsFactory(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onDataSetChanged() {
        final long identityToken = Binder.clearCallingIdentity();
        mWidgetItems = new ArrayList<>();

        Cursor dataCursor = mContext.getContentResolver().query(DatabaseContract.FavUserColumns.CONTENT_URI,
                null, null, null, null);
        if (dataCursor != null && dataCursor.getCount() > 0) {
            mWidgetItems.addAll(MappingHelper.mapCursorToArrayList(dataCursor));
            dataCursor.close();
        }
        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (getCount() > 0) {
            final RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
            remoteViews.setTextViewText(R.id.tvName, mWidgetItems.get(position).getName());
            remoteViews.setTextViewText(R.id.tvUsername, mWidgetItems.get(position).getUsername());

            Glide
                    .with(mContext)
                    .asBitmap()
                    .load(mWidgetItems.get(position).getAvatar())
                    .circleCrop()
                    .into(new CustomTarget<Bitmap>(100, 100) {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource,
                                                    @Nullable Transition<? super Bitmap> transition) {
                            remoteViews.setImageViewBitmap(R.id.user_profile_image, resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });

            Bundle extras = new Bundle();
            extras.putString(FavUserStackWidget.EXTRA_ITEM, mWidgetItems.get(position).getName());
            Intent fillInIntent = new Intent();
            fillInIntent.putExtras(extras);

            remoteViews.setOnClickFillInIntent(R.id.widget_item, fillInIntent);
            return remoteViews;
        } else return null;
    }

    @Override
    public int getCount() {
        return mWidgetItems.size();
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDestroy() {
    }
}
