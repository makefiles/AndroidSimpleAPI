package com.amolla.sdk;

interface ITube {
    int doAction(String key, Bundle val);
    int setValue(String key, Bundle val);
    Bundle getValue(String key, Bundle val);
}
