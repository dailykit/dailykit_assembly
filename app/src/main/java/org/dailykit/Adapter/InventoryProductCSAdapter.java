package org.dailykit.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.dailykit.OrderListSubscription;
import org.dailykit.R;
import org.dailykit.listener.InventoryProductListener;
import org.dailykit.listener.OrderListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InventoryProductCSAdapter extends RecyclerView.Adapter<InventoryProductCSAdapter.ViewHolder> {

    private List<OrderListSubscription.OrderInventoryProduct> orderInventoryProductList;
    private Activity activity;
    private InventoryProductListener inventoryProductListener;

    public InventoryProductCSAdapter(Activity activity, InventoryProductListener inventoryProductListener, List<OrderListSubscription.OrderInventoryProduct> orderInventoryProductList) {
        this.orderInventoryProductList = orderInventoryProductList;
        this.inventoryProductListener = inventoryProductListener;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inventory_cs_product, parent, false);
        ViewHolder rowHolder = new ViewHolder(v);
        return rowHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final OrderListSubscription.OrderInventoryProduct singleItem = orderInventoryProductList.get(position);
        String comboName = "";
        if(null != singleItem.comboProductId() && null != singleItem.comboProduct()){
            comboName = " - "+singleItem.comboProduct().name();
            if(null != singleItem.comboProductComponent()){
                comboName = comboName+" ("+singleItem.comboProductComponent().label()+")";
            }
        }
        holder.name.setText(singleItem.inventoryProduct().name()+comboName);
        holder.quantity.setText(singleItem.quantity()+" nos");
        if(null != singleItem.inventoryProduct().supplierItem().bulkItemAsShipped()){
            holder.processing.setText(singleItem.inventoryProduct().supplierItem().bulkItemAsShipped().processingName());
            holder.processing.setVisibility(View.VISIBLE);
        }
        else{
            holder.processing.setVisibility(View.GONE);
        }
        if(singleItem.isAssembled()){
            holder.available.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_check_green));
        }
        else{
            holder.available.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_check_grey));
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
        @BindView(R.id.available)
        ImageView available;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.processing)
        TextView processing;
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

