package com.yxy.lib.base.ui.base;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/9.
 */
public class FragmentLoaderManager {

    private static Map<String, Class<? extends BaseFragment>> fragmentMap = new HashMap<>();

    public static void putFragment(String type,Class<? extends BaseFragment> fragment) {
        fragmentMap.put(type, fragment);
    }

    protected static Class<? extends BaseFragment> getFragment(String type) {
        return fragmentMap.get(type);
    }

}
