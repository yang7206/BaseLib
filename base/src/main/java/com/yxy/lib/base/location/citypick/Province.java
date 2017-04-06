package com.yxy.lib.base.location.citypick;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/13.
 */
public class Province {
    private Integer ProID;
    private String name;
    private Integer ProSort;
    private String ProRemark;
    private String First;

    private List<City> cityList = new ArrayList<>();

    public Integer getProID() {
        return ProID;
    }

    public void setProID(Integer proID) {
        ProID = proID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getProSort() {
        return ProSort;
    }

    public void setProSort(Integer proSort) {
        ProSort = proSort;
    }

    public String getProRemark() {
        return ProRemark;
    }

    public void setProRemark(String proRemark) {
        ProRemark = proRemark;
    }

    public String getFirst() {
        return First;
    }

    public void setFirst(String first) {
        First = first;
    }

    public void addCity(City city) {
        city.setProvince(this);
        cityList.add(city);
    }

    public List<City> getCityList(){
        return  cityList;
    }

}
