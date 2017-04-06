package com.yxy.lib.base.location.citypick;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.yxy.lib.base.R;
import com.yxy.lib.base.location.AMapLocationManager;
import com.yxy.lib.base.ui.base.BaseActivity;
import com.yxy.lib.base.utils.FirstLetterUtils;
import com.yxy.lib.base.widget.IndexView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2015/11/13.
 */
public class CityPickActivity extends BaseActivity implements CityPickerUtils.OnCityLoadCompletedListener, AdapterView.OnItemClickListener, IndexView.OnTouchLetterChangeListenner {
    private ListView cityListView;
    private IndexView indexview;
    private Handler mHander;
    private List<City> cityList = new ArrayList<>();
    private String allLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private CityAdapter mAdapter;

    @Override
    protected int activityResId() {
        return R.layout.activity_citypicker;
    }

    @Override
    protected int TopBarId() {
        return R.id.topbar;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        cityListView = (ListView) findViewById(R.id.cityListView);
        indexview = (IndexView) findViewById(R.id.indexview);
        cityListView.setSelector(R.color.tran);
        indexview.setVisibility(View.GONE);
        mHander = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        if (mAdapter != null)
                            mAdapter.notifyDataSetChanged();
                        break;
                    case 1:
                        cityListView.setSelection(msg.arg1);
                        break;
                    case 2:
                        AMapLocationManager.getInstance().stopLocation();
                        break;
                    case 3:
                        indexview.setVisibility(View.VISIBLE);
                        break;
                }
            }
        };
        mAdapter = new CityAdapter();
        cityListView.setAdapter(mAdapter);
        cityListView.setOnItemClickListener(this);
        getSelectCityIfExist();
        CityPickerUtils.loadCity(CityPickActivity.this, this);
        AMapLocationManager.getInstance().setAMapLocationListener(mAMapLocationListener);
        startLocation();
        indexview.setOnTouchLetterChangeListenner(this);
    }

    private void sendLocationMsg(int res) {
        City locCity = null;
        if (cityList.size() == 0) {
            locCity = new City();
            cityList.add(0, locCity);
        } else {
            locCity = cityList.get(0);
        }
        locCity.setProvince(null);
        locCity.setName(getString(res));
        mHander.sendEmptyMessage(0);
    }

    private void startLocation() {
        sendLocationMsg(R.string.hx_city_locationing);
        AMapLocationManager.getInstance().startLocation();
    }

    private void getSelectCityIfExist() {
        String province = getIntent().getStringExtra(KEY_PROVINCE);
        String city = getIntent().getStringExtra(KEY_CITY);
        if (!TextUtils.isEmpty(province) && !TextUtils.isEmpty(city)) {
            selectCity = new SelectCityInfo(province, city);
        }
    }

    public static void show(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, CityPickActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }


    public static final String KEY_PROVINCE = "province";
    public static final String KEY_CITY = "city";

    public static void show(Activity activity, String province, String city, int requestCode) {
        Intent intent = new Intent(activity, CityPickActivity.class);
        intent.putExtra(KEY_PROVINCE, province);
        intent.putExtra(KEY_CITY, city);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    public void onCompleted(List<City> citys) {
        cityList.addAll(citys);
        mHander.sendEmptyMessage(0);
        mHander.sendEmptyMessage(3);
        changeSelectIfExist();
    }

    private void changeSelectIfExist() {
        if (selectCity != null) {
            new Thread() {
                @Override
                public void run() {
                    int select = 0;
                    for (int i = 0; i < cityList.size(); i++) {
                        if (selectCity.isEqualsCity(cityList.get(i))) {
                            select = i;
                            break;
                        }
                    }
                    mHander.obtainMessage(1, select, 0).sendToTarget();
                }
            }.start();
        }
    }

    @Override
    public void onRightClick() {
        if (selectCity == null) {
            Toast.makeText(this, R.string.hx_tips_select_city, Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent();
        String province = Pattern.compile(getString(R.string.hx_text_province)).matcher(selectCity.provinceName).replaceAll("");
        String city = Pattern.compile(getString(R.string.hx_text_city)).matcher(selectCity.cityName).replaceAll("");
        intent.putExtra(KEY_PROVINCE, province);
        intent.putExtra(KEY_CITY, city);
        setResult(RESULT_OK, intent);
        finish();
    }

    private int locationRetry = 0;
    private AMapLocationListener mAMapLocationListener = new AMapLocationListener() {

        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            locationRetry++;
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //定位成功回调信息，设置相关消息
                    amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                    amapLocation.getLatitude();//获取经度
                    amapLocation.getLongitude();//获取纬度
                    amapLocation.getAccuracy();//获取精度信息
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date(amapLocation.getTime());
                    df.format(date);//定位时间
                    amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果
                    amapLocation.getCountry();//国家信息
                    amapLocation.getProvince();//省信息
                    amapLocation.getCity();//城市信息
                    amapLocation.getDistrict();//城区信息
                    amapLocation.getRoad();//街道信息
                    amapLocation.getCityCode();//城市编码
                    amapLocation.getAdCode();//地区编码
                    Log.i("CityPickActivity", "province :" + amapLocation.getProvince() + ",city :" + amapLocation.getCity());

                    sendLocationedMsg(amapLocation);
                    if (locationRetry >= 3) {
                        mHander.sendEmptyMessage(2);
                    }
                } else {
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    Log.i("CityPickActivity", "AmapError location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                    if (locationRetry >= 10) {
                        sendLocationMsg(R.string.hx_city_location_fail);
                        mHander.sendEmptyMessage(2);
                    }
                }
                return;
            }

            if (locationRetry >= 10) {
                sendLocationMsg(R.string.hx_city_location_fail);
                mHander.sendEmptyMessage(2);
            }
        }

        private void sendLocationedMsg(AMapLocation amapLocation) {
            City city = cityList.get(0);
            city.setName(amapLocation.getCity());
            city.setFirstLetters(FirstLetterUtils.getFirstLetter(amapLocation.getCity()).substring(0, 1));
            Province p = new Province();
            p.setName(amapLocation.getProvince());
            city.setProvince(p);
            mHander.sendEmptyMessage(0);
        }
    };

    @Override
    public void onTouchLetterChange(boolean isTouched, String s) {
        System.out.println("s :" + s);
        if ("#".equals(s)) {
            mHander.obtainMessage(1, 0, 0).sendToTarget();
            return;
        }
        String nextChar = "";
        if (allLetters.indexOf(s.toUpperCase()) + 1 > allLetters.length()) {
            nextChar = String.valueOf(allLetters.charAt(allLetters.indexOf(s.toUpperCase()) + 1));
        }
        for (int i = 1; i < cityList.size(); i++) {
            City city = cityList.get(i);
            if (city.getFirstLetters().toUpperCase().equals(s.toUpperCase())) {
                mHander.obtainMessage(1, i, i).sendToTarget();
                break;
            } else if (!TextUtils.isEmpty(nextChar) && city.getFirstLetters().toUpperCase().equals(nextChar)) {
                mHander.obtainMessage(1, i, i).sendToTarget();
                break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    private class SelectCityInfo {
        String provinceName;
        String cityName;

        private SelectCityInfo(String province, String city) {
            this.provinceName = province;
            this.cityName = city;
        }

        boolean isEqualsCity(City city) {
            return city.getName().equals(cityName) && city.getProvince().getName().equals(provinceName);
        }
    }

    private SelectCityInfo selectCity = null;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        City city = cityList.get(position);
        if (position == 0) {
            if (city.getProvince() != null) {
                selectCity = new SelectCityInfo(city.getProvince().getName(), city.getName());
                onRightClick();
            }
            return;
        }
        selectCity = new SelectCityInfo(city.getProvince().getName(), city.getName());
        mAdapter.notifyDataSetChanged();
    }

    private class CityAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return cityList.size();
        }

        @Override
        public Object getItem(int position) {
            return cityList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(CityPickActivity.this).inflate(R.layout.layout_city_item, null);
                holder.init(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.bind(position);
            return convertView;
        }
    }

    private class ViewHolder {
        TextView item_province;
        TextView item_city;
        ImageView iv_loc;
        LinearLayout parent_item_city;

        void init(View root) {
            parent_item_city = (LinearLayout) root.findViewById(R.id.parent_item_city);
            iv_loc = (ImageView) root.findViewById(R.id.iv_loc);
            item_city = (TextView) root.findViewById(R.id.item_city);
            item_province = (TextView) root.findViewById(R.id.item_province);
        }

        void bind(int position) {
            City city = cityList.get(position);
            if (position == 0) {
                iv_loc.setVisibility(View.VISIBLE);
                item_province.setVisibility(View.GONE);
                parent_item_city.setGravity(Gravity.CENTER);
                if (city.getProvince() == null) {
                    item_city.setText(city.getName());
                } else {
                    item_city.setText(getString(R.string.hx_city_locationed_format, city.getProvince().getName(), city.getName()));
                }
                return;
            }
            iv_loc.setVisibility(View.GONE);
            boolean isFirst = true;
            if (position != 1) {
                City previousCity = cityList.get(position - 1);
                isFirst = !previousCity.getFirstLetters().equals(city.getFirstLetters());
            }
            item_province.setVisibility(!isFirst ? View.GONE : View.VISIBLE);
            item_province.setText(city.getFirstLetters().toUpperCase());
            item_city.setText(city.getName());

            boolean isSelect = selectCity != null && selectCity.isEqualsCity(city);
            parent_item_city.setGravity(Gravity.LEFT);
            parent_item_city.setBackgroundResource(isSelect ? R.color.red_half : R.color.white);

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AMapLocationManager.getInstance().destory();
    }
}
