package com.baway.yuwentao;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * 类用途 :适配器类
 * 作者 : 郁文涛
 * 时间 : 2017/7/17 9:48
 */

public class ShopCarAdapter extends BaseAdapter {
    //集合 ，存放ListView的商品实体类数据
    private List<BeanCart> products;
    //上下文
    private Context context;
    //图片加载器
    //第一步，设置接口，这里方便在外面的activity或者fragment进行回调
    private View.OnClickListener onAddNum;
    private View.OnClickListener onSubNum;
    private View.OnClickListener onCheck;

    //第二步，设置接口方法
    public void setOnAddNum(View.OnClickListener onAddNum) {
        this.onAddNum = onAddNum;
    }

    public void setOnCheck(View.OnClickListener onCheck) {
        this.onCheck = onCheck;
    }

    public void setOnSubNum(View.OnClickListener onSubNum) {
        this.onSubNum = onSubNum;
    }

    public ShopCarAdapter(List<BeanCart> products, Context context) {
        this.products = products;
        this.context = context;
    }

    @Override
    public int getCount() {
        int ret = 0;
        if (products != null) {
            ret = products.size();
        }
        return ret;
    }

    @Override
    public Object getItem(int i) {
        return products.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View v, ViewGroup parent) {
        ViewHolder holder;
        if (v == null) {
            holder = new ViewHolder();
            v = View.inflate(context, R.layout.item_listview, null);
            holder.item_product_descp = (TextView) v.findViewById(R.id.shopcar_item_product_name);
            holder.item_product_count = (TextView) v.findViewById(R.id.shopcar_item_product_count);
            holder.item_product_pv = (TextView) v.findViewById(R.id.shopcar_item_product_pv);
            holder.item_product_price = (TextView) v.findViewById(R.id.shopcar_item_product_price);
            holder.item_iv_img = (ImageView) v.findViewById(R.id.shopcar_item_image);
            //第三步,设置接口回调，注意参数不是上下文，它需要ListView所在的Activity或者Fragment处理接口回调方法
            holder.item_iv_add = (ImageView) v.findViewById(R.id.shopcar_item_btn_add);
            holder.item_iv_add.setOnClickListener(onAddNum);
            holder.item_iv_sub = (ImageView) v.findViewById(R.id.shopcar_item_btn_sub);
            holder.item_iv_sub.setOnClickListener(onSubNum);
            holder.item_cb_check = (CheckBox) v.findViewById(R.id.shopcar_item_check);
            holder.item_cb_check.setOnClickListener(onCheck);
        } else {
            holder = (ViewHolder) v.getTag();
        }
        holder.item_product_descp.setText(products.get(i).getDesc());
        holder.item_product_count.setText(products.get(i).getCount());
        holder.item_product_pv.setText(products.get(i).getPv());
        String price = '¥' + products.get(i).getPrice();
        holder.item_product_price.setText(price);
        holder.item_product_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
        holder.item_cb_check.setChecked(products.get(i).isChecked());
        //显示图片
        holder.item_iv_img.setImageResource(R.drawable.goods);
        holder.item_product_count.setText(products.get(i).getCount());
        //第四步，设置Tag，用于判断用户当前点击的哪一个列表项的按钮
        holder.item_iv_add.setTag(i);
        holder.item_iv_sub.setTag(i);
        holder.item_cb_check.setTag(i);
        v.setTag(holder);
        return v;
    }


    private static class ViewHolder {
        //商品名称，数量，总价
        private TextView item_product_descp;
        private TextView item_product_count;
        private TextView item_product_pv;
        private TextView item_product_price;
        //增减商品数量按钮
        private ImageView item_iv_add;
        private ImageView item_iv_sub;
        //商品的图标
        private ImageView item_iv_img;
        //是否选择
        private CheckBox item_cb_check;
    }
}
