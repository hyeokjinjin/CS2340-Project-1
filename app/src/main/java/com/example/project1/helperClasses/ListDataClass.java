package com.example.project1.helperClasses;

import java.io.Serializable;

public class ListDataClass implements Serializable {
    private String heading, subhead1, subhead2, subhead3;
    private Integer month, day, year;

    public ListDataClass(String heading, String subhead1, String subhead2) {
        this(heading, subhead1, subhead2, "");
    }

    public ListDataClass(String heading, String subhead1, String subhead2, int month, int day, int year) {
        this.heading = heading;
        this.subhead1 = subhead1;
        this.subhead2 = subhead2;
        this.month = month;
        this.day = day;
        this.year = year;
    }

    public ListDataClass(String heading, String subhead1, String subhead2, String subhead3) {
        this.heading = heading;
        this.subhead1 = subhead1;
        this.subhead2 = subhead2;
        this.subhead3 = subhead3;
    }

    public String getHeading() {
        return heading;
    }

    public String getSubhead1() {
        return subhead1;
    }

    public String getSubhead2() {
        return subhead2;
    }

    public String getSubhead3() {
        return subhead3;
    }

    public Integer getMonth() {
        return month;
    }

    public Integer getDay() {
        return day;
    }

    public Integer getYear() {
        return year;
    }
}
