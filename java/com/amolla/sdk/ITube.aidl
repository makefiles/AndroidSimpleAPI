package com.amolla.sdk;

import android.os.Bundle;

/**
 * TODO: Enter the descriptions
 * @since 1.0
 */
interface ITube {
    int doAction(String key, inout Bundle val);
    int setValue(String key, inout Bundle val);
    Bundle getValue(String key, inout Bundle val);
}
