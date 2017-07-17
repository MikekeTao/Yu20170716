package com.baway.yuwentao;

/**
 * 类用途 :bean的实体类
 * 作者 : 郁文涛
 * 时间 : 2017/7/17 9:49
 */

public class BeanCart {
    private String desc;
    private String count;
    private String pv;
    private String price;
    private boolean checked;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getPv() {
        return pv;
    }

    public void setPv(String pv) {
        this.pv = pv;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
