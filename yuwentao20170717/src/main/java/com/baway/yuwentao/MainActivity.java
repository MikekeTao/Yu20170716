package com.baway.yuwentao;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 类用途 :主页面
 * 作者 : 郁文涛
 * 时间 : 2017/7/17 8:56
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //此列表存放出服务器加载的购物车数据
    private List<BeanCart> goods = new ArrayList<>();
    //listview本尊
    private ListView lv;
    // 网络图片加载器
    private ImageView backBtn;
    private TextView tv_sumpv, tv_text01, tv_text02;
    //条目自定义适配器
    private ShopCarAdapter adapter;
    // 主页的checkbox
    private CheckBox allCheck;
    private TextView tv_setitem;
    private boolean setState = false;
    private Button btn_push;
    // 合计积分花费，我们要花多少钱都在这里啦，多少妹子月末总想着这个数字，哇哈哈
    private int totalpv = 0;
    /**
     * 观察者模式，监听listview的数据变换，dfdf 学了这么久的架构，第一次用上观察者模式，幸福哈哈。观察，就是把关，妹子不舒服了就关心下
     */
    private DataSetObserver sumObserver = new DataSetObserver() {
        /**
         * 当Adapter的notifyDataSetChanged方法执行时被调用，一变化就看是编辑模式还是正常模式，正常的就就计算合计，设置结算选中个数
         */
        @Override
        public void onChanged() {
            super.onChanged();
            totalPv();// 计算总价
            tv_sumpv.setText(totalpv + "");
            if (!setState) {
                btn_push.setText("结算" + "(" + totalCheckNumber() + ")");
            }
        }

        /**
         * 当Adapter 调用 notifyDataSetInvalidate方法执行时被调用，目前还不用
         */
        @Override
        public void onInvalidated() {
            super.onInvalidated();
            totalPv();// 计算总价
            tv_sumpv.setText(totalpv + "");
        }
    };

    //加载数据完毕，初始listview
    private void startInitListview() {
        adapter = new ShopCarAdapter(goods, MainActivity.this);
        //设置Adapter的数据变化观察者，只要Adapter的notifyDataSet被调用，观察者自动调用
        adapter.registerDataSetObserver(sumObserver);
        for (int i = 0; i < 10; i++) {
            BeanCart beanCart = new BeanCart();
            beanCart.setDesc("坚果商店");
            beanCart.setCount("0");
            beanCart.setPrice("46.0");
            beanCart.setPv("60.00");
            beanCart.setChecked(false);
            goods.add(beanCart);
        }
        lv.setAdapter(adapter);
        //好巧妙的设置监听，第一次看别人这么用，我也学学，用用更健康
        adapter.setOnAddNum(listener);
        adapter.setOnSubNum(listener);
        adapter.setOnCheck(listener);
    }

    /**
     * 这里处理我们listview item里面的监听事件
     */
    protected View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Object tag = v.getTag();
            int key = v.getId();
            switch (key) {
                case R.id.shopcar_item_btn_add: //点击添加数量按钮，执行相应的处理
                    // 获取 Adapter 中设置的 Tag
                    if (tag != null && tag instanceof Integer) { //解决问题：如何知道你点击的按钮是哪一个列表项中的，通过Tag的position
                        int position = (Integer) tag;
                        //更改集合的数据
                        int num = Double.valueOf(goods.get(position).getCount()).intValue();
                        num++;
                        goods.get(position).setCount(num + ""); //修改集合中商品数量
                        //解决问题：点击某个按钮的时候，如果列表项所需的数据改变了，如何更新UI
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case R.id.shopcar_item_btn_sub: //点击减少数量按钮 ，执行相应的处理
                    // 获取 Adapter 中设置的 Tag
                    if (tag != null && tag instanceof Integer) {
                        int position = (Integer) tag;
                        //更改集合的数据
                        int num = Double.valueOf(goods.get(position).getCount()).intValue();
                        if (num > 0) {
                            num--;
                            goods.get(position).setCount(num + ""); //修改集合中商品数量
                            adapter.notifyDataSetChanged();
                        }
                    }
                    break;
                case R.id.shopcar_item_check: //处理选中时间
                    // 获取 Adapter 中设置的 Tag
                    if (tag != null && tag instanceof Integer) {
                        int position = (Integer) tag;
                        //更改集合的数据
                        boolean ischecked = goods.get(position).isChecked();
                        ischecked = !ischecked;
                        goods.get(position).setChecked(ischecked);
                        if (isTheBoxallCheck()) {
                            allCheck.setChecked(true);
                        } else {
                            allCheck.setChecked(false);
                        }
                        adapter.notifyDataSetChanged();
                    }
                    break;
                default:
                    break;
            }
        }

        /**
         * 判断是否全选
         */
        private boolean isTheBoxallCheck() {
            for (int i = 0; i < goods.size(); i++) {
                if (!goods.get(i).isChecked()) {
                    return false;
                }
            }
            return true;
        }
    };


    /**
     * 计算当前的选中个数
     */
    public int totalCheckNumber() {
        int cnumber = 0;
        for (int i = 0; i < goods.size(); i++) {
            if (goods.get(i).isChecked()) {
                cnumber++;
            }
        }
        return cnumber;
    }

    /**
     * 计算当前状态的pv总数
     */
    public int totalPv() {
        totalpv = 0;
        for (int i = 0; i < goods.size(); i++) {
            int count = Double.valueOf(goods.get(i).getCount()).intValue();
            int itempv = Double.valueOf(goods.get(i).getPv()).intValue();
            boolean check = goods.get(i).isChecked();
            int choose = 0;
            if (check) {
                choose = 1;
            }
            int item = count * choose * itempv;
            totalpv += item;
        }
        return totalpv;
    }

    //处理这个点击事件
    @Override
    public void onClick(View v) {
        int key = v.getId();
        switch (key) {
            case R.id.btn_back_fragment_shopcar:
                break;
            case R.id.total_check_fragment_shopcar:
                //  处理全选的逻辑
                if (allCheck.isChecked()) {
                    checkAllItem();
                } else {
                    uncheckAllItem();
                }
                break;
            case R.id.tv_fragment_shopcar_reset:
                //处理编辑的逻辑
                if (!setState) {
                    setState = true;
                    //出现删除条目
                    beginsetItemGone();
                } else {
                    //恢复源条目

                    reSetItemStay();
                    setState = false;
                }
                break;
            case R.id.btn_fragment_shopcar_pushsum:
                //处理提交的任务分在state在删除状态和提交状态
                if (!setState) {
                    //处理提交的业务

                } else {
                    //处理删除的业务
                    deleteChooseItem();
                    //需要提交修改后的数据

                }
                break;
            default:
                break;
        }
    }
    /**
     * 保存提交修改的购物车列表
     */

    /**
     * 选中删除条目
     */
    private void deleteChooseItem() {
        int j = goods.size();
        for (int i = j - 1; i >= 0; i--) {
            if (goods.get(i).isChecked()) {
                //选中移除
                goods.remove(i);
            }
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 正常状态
     */
    private void reSetItemStay() {
        tv_text01.setVisibility(View.VISIBLE);
        tv_text02.setVisibility(View.VISIBLE);
        tv_sumpv.setVisibility(View.VISIBLE);
        btn_push.setText("结算" + "(" + totalCheckNumber() + ")");
        tv_setitem.setText("编辑");
    }

    /**
     * 编辑状态
     */
    private void beginsetItemGone() {
        tv_text01.setVisibility(View.GONE);
        tv_text02.setVisibility(View.GONE);
        tv_sumpv.setVisibility(View.GONE);
        btn_push.setText("删除");
        tv_setitem.setText("保存");
    }

    //设置列表不全选，并刷新列表
    private void uncheckAllItem() {
        for (int i = 0; i < goods.size(); i++) {
            goods.get(i).setChecked(false);
        }
        adapter.notifyDataSetChanged();
    }

    /*
     * 设置列表全选，并刷新列表
     */
    public void checkAllItem() {

        for (int i = 0; i < goods.size(); i++) {
            goods.get(i).setChecked(true);
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 记得在destroy移除观察者订阅
     */
    @Override
    public void onDestroy() {

        super.onDestroy();
        adapter.unregisterDataSetObserver(sumObserver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        backBtn = (ImageView) findViewById(R.id.btn_back_fragment_shopcar);
        backBtn.setOnClickListener(this);
        //购物车显示列表
        lv = (ListView) findViewById(R.id.lv_fragment_shopcar_cart);
        tv_sumpv = (TextView) findViewById(R.id.tv_fragment_shopcar_total_sum);
        allCheck = (CheckBox) findViewById(R.id.total_check_fragment_shopcar);
        allCheck.setOnClickListener(this);
        tv_setitem = (TextView) findViewById(R.id.tv_fragment_shopcar_reset);
        tv_setitem.setOnClickListener(this);
        tv_text01 = (TextView) findViewById(R.id.tv_fragment_shopcar_text01);
        tv_text02 = (TextView) findViewById(R.id.tv_fragment_shopcar_text02);
        btn_push = (Button) findViewById(R.id.btn_fragment_shopcar_pushsum);
        btn_push.setOnClickListener(this);
        startInitListview();
    }
}
