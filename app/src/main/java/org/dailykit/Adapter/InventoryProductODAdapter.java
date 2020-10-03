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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InventoryProductODAdapter extends RecyclerView.Adapter<InventoryProductODAdapter.ViewHolder> {

    private List<OrderListSubscription.OrderInventoryProduct> orderInventoryProductList;
    private Activity activity;
    private InventoryProductListener inventoryProductListener;

    public InventoryProductODAdapter(Activity activity, InventoryProductListener inventoryProductListener, List<OrderListSubscription.OrderInventoryProduct> orderInventoryProductList) {
        this.orderInventoryProductList = orderInventoryProductList;
        this.inventoryProductListener = inventoryProductListener;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inventory_product_od, parent, false);
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
        if (null != singleItem.inventoryProduct().supplierItem().bulkItemAsShipped()) {
            holder.processing.setText(singleItem.inventoryProduct().supplierItem().bulkItemAsShipped().processingName());
            holder.processing.setVisibility(View.VISIBLE);
        } else {
            holder.processing.setVisibility(View.INVISIBLE);
        }

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
        holder.serving.setText(singleItem.inventoryProductOption().quantity() + " - " + singleItem.inventoryProductOption().label());

        holder.toggle.setOnClickListener(v -> {
            if (holder.optionsLayout.getVisibility() == View.GONE) {
                holder.toggle.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_arrow_up_grey));
                holder.optionsLayout.setVisibility(View.VISIBLE);
            } else {
                holder.toggle.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_arrow_down_grey));
                holder.optionsLayout.setVisibility(View.GONE);
            }
        });

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
        @BindView(R.id.processing)
        TextView processing;
        @BindView(R.id.status)
        TextView status;
        @BindView(R.id.toggle)
        ImageView toggle;
        @BindView(R.id.mark_as_assembled)
        LinearLayout markAsAssembled;
        @BindView(R.id.options_layout)
        LinearLayout optionsLayout;
        @BindView(R.id.layout)
        LinearLayout layout;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

