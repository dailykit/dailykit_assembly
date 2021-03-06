package org.dailykit.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.dailykit.OrderListSubscription;
import org.dailykit.R;
import org.dailykit.listener.OrderListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InventoryProductAdapter extends RecyclerView.Adapter<InventoryProductAdapter.ViewHolder> {

    private List<OrderListSubscription.OrderInventoryProduct> orderInventoryProductList;
    private Activity activity;
    private OrderListener orderListener;

    public InventoryProductAdapter(Activity activity, OrderListener orderListener, List<OrderListSubscription.OrderInventoryProduct> orderInventoryProductList) {
        this.orderInventoryProductList = orderInventoryProductList;
        this.orderListener = orderListener;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inventory_product, parent, false);
        ViewHolder rowHolder = new ViewHolder(v);
        return rowHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final OrderListSubscription.OrderInventoryProduct singleItem = orderInventoryProductList.get(position);
        String comboName = "";
        if (null != singleItem.comboProductId() && null != singleItem.comboProduct()) {
            comboName = " - " + singleItem.comboProduct().name();
            if (null != singleItem.comboProductComponent()) {
                comboName = comboName + " (" + singleItem.comboProductComponent().label() + ")";
            }
        }
        holder.name.setText(singleItem.inventoryProduct().name() + comboName);
        holder.quantity.setText(singleItem.quantity() + " nos");

        if ("PENDING".equals(singleItem.assemblyStatus())) {
            holder.layout.setBackgroundColor(activity.getResources().getColor(R.color.list_yellow));
            holder.status.setText("0/0/1");
        } else if ("COMPLETED".equals(singleItem.assemblyStatus()) && !singleItem.isAssembled()) {
            holder.layout.setBackgroundColor(activity.getResources().getColor(R.color.list_blue));
            holder.status.setText("0/1/1");
        } else if ("COMPLETED".equals(singleItem.assemblyStatus()) && singleItem.isAssembled()) {
            holder.layout.setBackgroundColor(activity.getResources().getColor(R.color.list_green));
            holder.status.setText("0/1/1");
        }
        holder.serving.setText(singleItem.inventoryProductOption().quantity()+" - "+singleItem.inventoryProductOption().label());

    }

    public void updateList() {
        this.notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return (null != orderInventoryProductList ? orderInventoryProductList.size() : 0);
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.serving)
        TextView serving;
        @BindView(R.id.quantity)
        TextView quantity;
        @BindView(R.id.status)
        TextView status;
        @BindView(R.id.layout)
        LinearLayout layout;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

