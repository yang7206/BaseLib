package com.yxy.lib.base.location.citypick;

import android.content.Context;
import android.util.SparseArray;

import com.yxy.lib.base.utils.FirstLetterUtils;
import com.yxy.lib.base.utils.JsonUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Administrator on 2015/11/13.
 */
public class CityPickerUtils {
    private static List<City> cityList = new ArrayList<>();

    public static void loadCity(final Context context, final OnCityLoadCompletedListener l) {
        final WeakReference<OnCityLoadCompletedListener> refListener = new WeakReference<>(l);
        new Thread() {
            @Override
            public void run() {
                if (cityList.size() == 0) {
                    try {
                        BufferedInputStream bis = new BufferedInputStream(context.getResources().getAssets().open("city.json"));
                        ByteArrayOutputStream bas = new ByteArrayOutputStream();
                        BufferedOutputStream bos = new BufferedOutputStream(bas);
                        byte b[] = new byte[8194];
                        int index;
                        while ((index = bis.read(b)) != -1) {
                            bos.write(b, 0, index);
                        }
                        bos.flush();
                        byte[] data = bas.toByteArray();
                        bos.close();
                        bis.close();
                        JSONObject jsonObject = new JSONObject(new String(data, "UTF-8"));
                        String version = jsonObject.optString("version");
                        JSONArray provinceArray = jsonObject.getJSONArray("province");
                        SparseArray<Province> provinceSparseArray = new SparseArray<>(provinceArray.length());
                        for (int i = 0; i < provinceArray.length(); i++) {
                            Province province = JsonUtil.toObject(provinceArray.getJSONObject(i).toString(), Province.class);
                            provinceSparseArray.put(province.getProID(), province);
                        }
                        JSONArray cityArray = jsonObject.getJSONArray("city");
                        for (int i = 0; i < cityArray.length(); i++) {
                            City city = JsonUtil.toObject(cityArray.getJSONObject(i).toString(), City.class);
                            city.setFirstLetters(FirstLetterUtils.getFirstLetter(city.getName()).substring(0, 1));
                            provinceSparseArray.get(city.getProID()).addCity(city);
                        }
                        for (int i = 0; i < provinceSparseArray.size(); i++) {
                            Province province = provinceSparseArray.valueAt(i);
                            cityList.addAll(province.getCityList());
                            province.getCityList().clear();
                        }


                        City[] cities = new City[cityList.size()];
                        cityList.toArray(cities);

                        Arrays.sort(cities, new Comparator<City>() {

                            private String allLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

                            @Override
                            public int compare(City lhs, City rhs) {
                                if (allLetters.indexOf(lhs.getFirstLetters().toUpperCase()) < allLetters.indexOf(rhs.getFirstLetters().toUpperCase()))
                                    return -1;
                                if (allLetters.indexOf(lhs.getFirstLetters().toUpperCase()) > allLetters.indexOf(rhs.getFirstLetters().toUpperCase()))
                                    return 1;
                                return 0;
                            }
                        });

                        cityList.clear();
                        cityList.addAll(Arrays.asList(cities));

                        provinceSparseArray.clear();
                        provinceSparseArray = null;
                        cityArray = null;
                        provinceArray = null;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (refListener != null) {
                    OnCityLoadCompletedListener listener = refListener.get();
                    if (listener != null) {
                        listener.onCompleted(cityList);
                    }
                }
            }
        }.start();
    }

    public interface OnCityLoadCompletedListener {
        void onCompleted(List<City> cityList);
    }

}
