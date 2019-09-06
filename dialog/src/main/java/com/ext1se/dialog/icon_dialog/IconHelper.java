/*
 * Copyright (c) 2018 Nicolas Maltais
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.ext1se.dialog.icon_dialog;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.XmlRes;

import com.ext1se.dialog.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IconHelper {

    private static final String TAG = IconHelper.class.getSimpleName();

    private static final String XML_TAG_LIST = "list";
    private static final String XML_TAG_ICON = "icon";
    private static final String XML_ATTR_NAME = "name";
    private static final String XML_ATTR_ID = "id";
    private static final String XML_ATTR_LABELS = "labels";
    private static final String XML_ATTR_PATH = "path";

    private final Context mContext;

    private SparseArray<IconItem> mIcons;
    private boolean mDataLoaded;
    private TaskExecutor mDataLoader;
    private TaskExecutor mDrawablesLoader;
    private List<LoadCallback> mLoadCallbacks;

    public IconHelper(Context context, boolean isAsync) {
        this.mContext = context.getApplicationContext();
        mDataLoaded = false;
        if (isAsync) {
            mDataLoader = new TaskExecutor();
            mDataLoader.execute(() -> {
                loadIcons(R.xml.icons, false);
                mDataLoaded = true;
            }, () -> callLoadCallbacks());

            mDrawablesLoader = new TaskExecutor();
        } else {
            loadIcons(R.xml.icons, false);
            mDataLoaded = true;
        }
    }

    /**
     * @return true if data is loaded, false if not loaded, i.e currently loading mIcons or labels
     */
    public boolean isDataLoaded() {
        return mDataLoaded;
    }

    public SparseArray<IconItem> getIcons() {
        return mIcons;
    }

    /**
     * Load mIcons from XML file
     */
    private synchronized void loadIcons(@XmlRes int xmlFile, boolean append) {
        if (mIcons == null || !append) {
            mIcons = new SparseArray<>();
        }

        XmlPullParser parser = mContext.getResources().getXml(xmlFile);
        try {
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName();
                if (eventType == XmlPullParser.START_TAG) {
                    if (tagName.equalsIgnoreCase(XML_TAG_ICON)) {
                        // <icon id="0" labels="label1,label2,label3"/>
                        int id = Integer.valueOf(parser.getAttributeValue(null, XML_ATTR_ID));
                        String allLabelsStr = parser.getAttributeValue(null, XML_ATTR_LABELS);
                        String pathStr = parser.getAttributeValue(null, XML_ATTR_PATH);

                        byte[] pathData = null;
                        if (pathStr != null) {
                            pathData = pathStr.getBytes();
                        }
                        //assert pathData != null;
                        IconItem icon = new IconItem(id, pathData);
                        //mIcons.append(id, icon);
                        mIcons.put(id, icon);
                    }
                }

                eventType = parser.next();
            }

        } catch (XmlPullParserException | IOException e) {
            Log.e(TAG, "Could not parse mIcons from XML.", e);
        }
    }

    /**
     * Get an icon
     *
     * @param id id of the icon
     * @return the icon, null if it doesn't exist or if data isn't loaded
     */
    @Nullable
    public IconItem getIcon(int id) {
        if (!mDataLoaded) {
            return null;
        }
        return mIcons.get(id);
    }

    public static IconItem getIcon(Context context, int iconId) {
        XmlPullParser parser = context.getResources().getXml(R.xml.icons);
        try {
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName();
                if (eventType == XmlPullParser.START_TAG) {
                    if (tagName.equalsIgnoreCase(XML_TAG_ICON)) {
                        // <icon id="0" labels="label1,label2,label3"/>
                        int id = Integer.valueOf(parser.getAttributeValue(null, XML_ATTR_ID));
                        if (id != iconId) {
                            eventType = parser.next();
                            continue;
                        }
                        String allLabelsStr = parser.getAttributeValue(null, XML_ATTR_LABELS);
                        String pathStr = parser.getAttributeValue(null, XML_ATTR_PATH);

                        byte[] pathData = null;
                        if (pathStr != null) {
                            pathData = pathStr.getBytes();
                        }
                        //assert pathData != null;
                        return new IconItem(id, pathData);
                    }

                }
                eventType = parser.next();
            }

        } catch (XmlPullParserException | IOException e) {
            Log.e(TAG, "Could not parse mIcons from XML.", e);
        }
        return null;
    }

    /**
     * Start loading all mIcons drawable asynchronously
     * This is useful to prevent lag when scrolling the icon dialog's list
     */
    public void loadIconDrawables() {
        if (!mDataLoaded) {
            throw new IllegalStateException("Cannot load drawables before icon data is loaded.");
        }

        mDrawablesLoader.execute(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < mIcons.size(); i++) {
                    if (mIcons.valueAt(i).getDrawable() == null) {
                        mIcons.valueAt(i).getDrawable(mContext);
                    }
                }
            }
        }, null);
    }

    public void stopLoadingDrawables() {
        mDrawablesLoader.interrupt();
    }

    /**
     * Set to null references to all of the mIcons drawable so that they can be garbage collected
     */
    public void freeIconDrawables() {
        mDrawablesLoader.interrupt();
        for (int i = 0; i < mIcons.size(); i++) {
            mIcons.valueAt(i).setDrawable(null);
        }
    }

    public void addLoadCallback(@NonNull LoadCallback cb) {
        if (mDataLoaded) {
            // Data is already loaded: call callback without adding it
            cb.onDataLoaded(this);
            return;
        }

        if (mLoadCallbacks == null) {
            mLoadCallbacks = new ArrayList<>();
        }
        mLoadCallbacks.add(cb);
    }

    public void removeLoadCallback(@NonNull LoadCallback cb) {
        mLoadCallbacks.remove(cb);
        if (mLoadCallbacks.size() == 0) {
            mLoadCallbacks = null;
        }
    }

    private void callLoadCallbacks() {
        if (mLoadCallbacks != null) {
            for (LoadCallback cb : mLoadCallbacks) {
                cb.onDataLoaded(this);
            }
            mLoadCallbacks.clear();
        }
    }

    public interface LoadCallback {
        /**
         * Called when icon data is done loading.
         * All calls to get mIcons, labels and categories will return null before this is called.
         *
         * @param helper The icon helper instance.
         */
        void onDataLoaded(IconHelper helper);
    }

}
