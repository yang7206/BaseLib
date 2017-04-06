package com.yxy.lib.base.location.citypick;

/**
 * Created by Administrator on 2015/11/13.
 */
public class City {
    private Province province;
    private Integer CityID;
    private String name;
    private Integer ProID;
    private Integer CitySort;
    public Province getProvince() {
        return province;
    }

    private String firstLetters;

    public String getFirstLetters() {
        return firstLetters;
    }

    public void setFirstLetters(String firstLetters) {
        this.firstLetters = firstLetters;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public Integer getCityID() {
        return CityID;
    }

    public void setCityID(Integer cityID) {
        CityID = cityID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getProID() {
        return ProID;
    }

    public void setProID(Integer proID) {
        ProID = proID;
    }

    public Integer getCitySort() {
        return CitySort;
    }

    public void setCitySort(Integer citySort) {
        CitySort = citySort;
    }

    public  boolean isDuchyCity(){
        return province.getName().equals(name);
    }
}
