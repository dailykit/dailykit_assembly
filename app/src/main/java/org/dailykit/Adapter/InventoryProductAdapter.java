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
        holder.name.setText(singleItem.inventoryProduct().name());
        if (null == singleItem.inventoryProduct().supplierItem()) {
            holder.quantity.setText("");
        } else {
            int unitSize = (null == singleItem.inventoryProduct().supplierItem().unitSize()) ? 0 : singleItem.inventoryProduct().supplierItem().unitSize();
            String unit = (null == singleItem.inventoryProduct().supplierItem().unit()) ? "" : singleItem.inventoryProduct().supplierItem().unit();
            holder.quantity.setText(unitSize + " " + unit);
        }
    }

    public void updateList() {
        this.notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return (null != orderInventoryProductList ? orderInventoryProductList.size() : 0);
    }


    static
    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.quantity)
        TextView quantity;
        @BindView(R.id.layout)
        LinearLayout layout;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}

