package com.romnan.githubuser.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.romnan.githubuser.R;
import com.romnan.githubuser.model.User;

import java.util.ArrayList;
import java.util.List;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private final List<User> mWidgetItems = new ArrayList<>();
    private final Context mContext;

    public StackRemoteViewsFactory(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        mWidgetItems.add(new User(201920, "dijfdjhf", "Deddy Romnan Rumapea",
                "deddyromnan", "Jambi", "romnan", 11, 12,
                13));
        mWidgetItems.add(new User(201920, "dijfdjhf", "ccccccccccc",
                "deddyromnan", "Jambi", "romnan", 11, 12,
                13));
        mWidgetItems.add(new User(201920, "dijfdjhf", "ddddddddddd",
                "deddyromnan", "Jambi", "romnan", 11, 12,
                13));
        mWidgetItems.add(new User(201920, "dijfdjhf", "eeeeeeeeee",
                "deddyromnan", "Jambi", "romnan", 11, 12,
                13));
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mWidgetItems.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        rv.setTextViewText(R.id.tvName, mWidgetItems.get(position).getName());
        rv.setTextViewText(R.id.tvUsername, mWidgetItems.get(position).getUsername());

        Bundle extras = new Bundle();
        extras.putInt(FavUserStackWidget.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);

        rv.setOnClickFillInIntent(R.id.tvName, fillInIntent);
        return rv;
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
}
