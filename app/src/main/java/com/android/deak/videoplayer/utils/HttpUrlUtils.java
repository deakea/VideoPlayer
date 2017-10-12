package com.android.deak.videoplayer.utils;

import com.alibaba.fastjson.JSON;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by deak on 2016/7/12.
 */
public class HttpUrlUtils {
    private static final String baseUrl = "http://api.rrmj.tv/";
    public static final String seasonIndex = baseUrl + "season/index";
    public static final String seasonList = baseUrl + "season/query";
    public static final String hotWord = baseUrl + "video/hotWord";
    public static final String searchResult = baseUrl + "season/search";
    public static final String details = baseUrl + "season/detail";
    public static final String videoAddress = baseUrl + "video/findM3u8ByEpisodeSid";

    public static void post(RequestParams params, Callback callBack) {
        params.getHeaders().clear();
        params.addHeader("Cache-Control", "");
        x.http().post(params, (Callback.CommonCallback<Object>) callBack);
    }
    public static <T> T parseJason(String json,Class<T> cls){//封装解析
        T t = null;
        try {
            t = JSON.parseObject(json, cls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }
}
