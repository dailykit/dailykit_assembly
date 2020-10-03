package org.dailykit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.dailykit.OrderListSubscription;
import org.dailykit.R;
import org.dailykit.adapter.OrderDetailViewPager;
import org.dailykit.adapter.TabAdapter;
import org.dailykit.listener.OrderDetailListener;
import org.dailykit.listener.OrderListener;
import org.dailykit.util.AppUtil;
import org.dailykit.util.SoftwareConfig;
import org.dailykit.viewmodel.DashboardViewModel;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class OrderDetailActivity extends CustomAppCompatActivity implements OrderListener, OrderDetailListener {

    @BindView(R.id.all)
    LinearLayout all;
    @BindView(R.id.tab_list)
    RecyclerView tabList;
    TabAdapter tabAdapter;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.ordered_on)
    TextView orderedOn;
    @BindView(R.id.ready_by)
    TextView readyBy;
    @BindView(R.id.pick_up)
    TextView pickUp;
    @BindView(R.id.hidden_layout)
    LinearLayout hiddenLayout;
    @BindView(R.id.start_assembling)
    LinearLayout startAssembling;
    @BindView(R.id.order_name)
    TextView orderName;
    @BindView(R.id.item_count)
    TextView itemCount;
    @BindView(R.id.customer_name)
    TextView customerName;
    @BindView(R.id.toggle)
    ImageView toggle;
    @BindView(R.id.amount)
    TextView amount;
    @BindView(R.id.customer_number)
    TextView customerNumber;
    @BindView(R.id.customer_email)
    TextView customerEmail;
    @BindView(R.id.customer_detail)
    LinearLayout customerDetail;
    private OrderDetailActivity orderDetailActivity;
    DashboardViewModel dashboardViewModel;
    private OrderListSubscription.Order order;
    private OrderDetailViewPager orderDetailViewPager;
    private SoftwareConfig softwareConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        orderDetailActivity = this;
        softwareConfig = new SoftwareConfig(orderDetailActivity);
        ButterKnife.bind(this);
        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        all.setOnClickListener(v -> {
            finish();
        });
        setView();
    }

    public void setView() {
        order = dashboardViewModel.getSelectedOrder();
        orderDetailViewPager = new OrderDetailViewPager(getSupportFragmentManager(), orderDetailActivity);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(orderDetailViewPager);
        viewPager.setOffscreenPageLimit(3);
        orderedOn.setText(AppUtil.getISTTime("yyyy-MM-dd'T'hh:mm:ss", "dd MMM hh:mm aa", ((String) order.created_at()).substring(0, 18)));
        readyBy.setText(AppUtil.getISTTime("yyyy-MM-dd'T'hh:mm:ss", "dd MMM hh:mm aa", (String) order.readyByTimestamp()));
        pickUp.setText(AppUtil.getISTTime("yyyy-MM-dd'T'hh:mm:ss", "dd MMM hh:mm aa", (String) order.fulfillmentTimestamp()));
        try {
            Timber.e(order.customer().toString());
            JSONObject customer = new JSONObject(String.valueOf(order.customer()).trim());

            if (null != customer.getString("customerFirstName") && null != customer.getString("customerLastName") && !customer.getString("customerFirstName").isEmpty() && !customer.getString("customerLastName").isEmpty()) {
                customerName.setVisibility(View.VISIBLE);
                customerName.setText(customer.getString("customerFirstName") + " " + customer.getString("customerLastName"));
            } else {
                customerName.setVisibility(View.GONE);
            }
            if (null != customer.getString("customerPhone") && !customer.getString("customerPhone").isEmpty()) {
                customerNumber.setVisibility(View.VISIBLE);
                customerNumber.setText(customer.getString("customerPhone"));
            } else {
                customerNumber.setVisibility(View.GONE);
            }
            if (null != customer.getString("customerEmail") && !customer.getString("customerEmail").isEmpty()) {
                customerEmail.setVisibility(View.VISIBLE);
                customerEmail.setText(customer.getString("customerEmail"));
            } else {
                customerEmail.setVisibility(View.GONE);
            }

            if ((null == customer.getString("customerPhone") || customer.getString("customerPhone").isEmpty()) && (null == customer.getString("customerEmail") || customer.getString("customerEmail").isEmpty())) {
                toggle.setVisibility(View.GONE);
            } else {
                toggle.setVisibility(View.VISIBLE);
            }

        } catch (Throwable t) {
            Timber.e(t.getMessage());
        }
        amount.setText(orderDetailActivity.getResources().getString(R.string.dollar) + " 0.00");
        toggle.setOnClickListener(v -> {
            if (customerDetail.getVisibility() == View.GONE) {
                toggle.setImageDrawable(orderDetailActivity.getResources().getDrawable(R.drawable.ic_arrow_up_grey));
                customerDetail.setVisibility(View.VISIBLE);
            } else {
                toggle.setImageDrawable(orderDetailActivity.getResources().getDrawable(R.drawable.ic_arrow_down_grey));
                customerDetail.setVisibility(View.GONE);
            }
        });

        int count = order.orderInventoryProducts().size()+order.orderMealKitProducts().size()+order.orderReadyToEatProducts().size();
        itemCount.setText("0/"+count);

        if(!softwareConfig.isPartialPackingEnabled() && !"READY_TO_ASSEMBLE".equals(order.orderStatus())){
            startAssembling.setOnClickListener(v -> {
                Toast.makeText(orderDetailActivity, "Please pack all the items before assembling", Toast.LENGTH_SHORT).show();
            });
            startAssembling.setBackground(orderDetailActivity.getResources().getDrawable(R.drawable.round_circle_grey));
        }
        else{
            startAssembling.setBackground(orderDetailActivity.getResources().getDrawable(R.drawable.round_circle_blue));
            startAssembling.setOnClickListener(v -> {
                moveToContinuousScanActivity(order);
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateTabList(dashboardViewModel.getTabList());
    }

    @Override
    public void moveToContinuousScanActivity(OrderListSubscription.Order order) {
        dashboardViewModel.setSelectedOrder(order);
        dashboardViewModel.addToTabList((String) order.id());
        startActivity(new Intent(orderDetailActivity, ContinuousScanActivity.class));
    }

    @Override
    public void removeOrderFromList(String orderId) {
        dashboardViewModel.removeFromTabList(orderDetailActivity, orderId);
    }

    @Override
    public void updateTabList(List<String> tabModelArrayList) {
        tabList.setHasFixedSize(true);
        tabAdapter = new TabAdapter(orderDetailActivity, orderDetailActivity, tabModelArrayList);
        tabList.setLayoutManager(new LinearLayoutManager(orderDetailActivity, LinearLayoutManager.HORIZONTAL, false));
        tabList.setAdapter(tabAdapter);
        tabAdapter.notifyDataSetChanged();
    }

    @Override
    public void getOrder(String orderId) {
        dashboardViewModel.setSelectedOrder(orderId);
        dashboardViewModel.addToTabList(orderId);
        startActivity(new Intent(orderDetailActivity, OrderDetailActivity.class));
        finish();
    }

    @Override
    public OrderListSubscription.Order getSelectedOrder() {
        return dashboardViewModel.getSelectedOrder();
    }

    @Override
    public int getPageCount() {
        int count = 0;
        if (order.orderMealKitProducts().size() > 0)
            count++;
        if (order.orderReadyToEatProducts().size() > 0)
            count++;
        if (order.orderInventoryProducts().size() > 0)
            count++;
        return count;
    }

    @Override
    public OrderListSubscription.Order getOrder() {
        return order;
    }

    @OnClick(R.id.start_assembling)
    public void onViewClicked() {
        moveToContinuousScanActivity(order);
    }
}