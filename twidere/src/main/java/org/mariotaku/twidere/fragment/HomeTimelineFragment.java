/*
 * Twidere - Twitter client for Android
 *
 *  Copyright (C) 2012-2014 Mariotaku Lee <mariotaku.lee@gmail.com>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mariotaku.twidere.fragment;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import org.mariotaku.twidere.annotation.ReadPositionTag;
import org.mariotaku.twidere.model.RefreshTaskParam;
import org.mariotaku.twidere.model.UserKey;
import org.mariotaku.twidere.provider.TwidereDataStore.Statuses;
import org.mariotaku.twidere.util.AsyncTwitterWrapper;
import org.mariotaku.twidere.util.ErrorInfoStore;

import edu.tsinghua.hotmobi.model.TimelineType;

/**
 * Created by mariotaku on 14/12/3.
 */
public class HomeTimelineFragment extends CursorStatusesFragment {

    @NonNull
    @Override
    protected String getErrorInfoKey() {
        return ErrorInfoStore.KEY_HOME_TIMELINE;
    }

    @Override
    public Uri getContentUri() {
        return Statuses.CONTENT_URI;
    }

    @Override
    protected int getNotificationType() {
        return NOTIFICATION_ID_HOME_TIMELINE;
    }

    @Override
    protected boolean isFilterEnabled() {
        return true;
    }

    @Override
    protected void updateRefreshState() {
        final AsyncTwitterWrapper twitter = mTwitterWrapper;
        if (twitter == null) return;
        setRefreshing(twitter.isHomeTimelineRefreshing());
    }

    @Override
    public boolean isRefreshing() {
        final AsyncTwitterWrapper twitter = mTwitterWrapper;
        return twitter != null && twitter.isHomeTimelineRefreshing();
    }

    @Override
    public boolean getStatuses(RefreshTaskParam param) {
        final AsyncTwitterWrapper twitter = mTwitterWrapper;
        if (twitter == null) return false;
        if (!param.hasMaxIds()) return twitter.refreshAll(param.getAccountKeys());
        return twitter.getHomeTimelineAsync(param);
    }

    @Override
    public void setUserVisibleHint(final boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        final FragmentActivity activity = getActivity();
        if (isVisibleToUser && activity != null) {
            for (UserKey accountId : getAccountKeys()) {
                final String tag = "home_" + accountId;
                mNotificationManager.cancel(tag, NOTIFICATION_ID_HOME_TIMELINE);
            }
        }
    }

    @NonNull
    @Override
    @TimelineType
    protected String getTimelineType() {
        return TimelineType.HOME;
    }

    @Override
    @ReadPositionTag
    protected String getReadPositionTag() {
        return ReadPositionTag.HOME_TIMELINE;
    }

}
