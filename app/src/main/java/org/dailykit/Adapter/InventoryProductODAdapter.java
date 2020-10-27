package org.dailykit.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import org.dailykit.OrderListDetailSubscription;
import org.dailykit.R;
import org.dailykit.UpdateOrderInventoryProductMutation;
import org.dailykit.listener.InventoryProductListener;
import org.dailykit.network.Network;
import org.dailykit.type.Order_orderInventoryProduct_pk_columns_input;
import org.dailykit.type.Order_orderInventoryProduct_set_input;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class InventoryProductODAdapter extends RecyclerView.Adapter<InventoryProductODAdapter.ViewHolder> {

    private List<OrderListDetailSubscription.OrderInventoryProduct> orderInventoryProductList;
    private Activity activity;
    private InventoryProductListener inventoryProductListener;

    public InventoryProductODAdapter(Activity activity, InventoryProductListener inventoryProductListener, List<OrderListDetailSubscription.OrderInventoryProduct> orderInventoryProductList) {
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
        final OrderListDetailSubscription.OrderInventoryProduct singleItem = orderInventoryProductList.get(position);
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
            holder.markAsAssembled.setBackground(activity.getResources().getDrawable(R.drawable.round_button_light_grey));
            holder.repack.setBackground(activity.getResources().getDrawable(R.drawable.round_button_light_grey));
            holder.reassemble.setBackground(activity.getResources().getDrawable(R.drawable.round_button_light_grey));
        } else if ("COMPLETED".equals(singleItem.assemblyStatus()) && !singleItem.isAssembled()) {
            holder.layout.setBackgroundColor(activity.getResources().getColor(R.color.list_blue));
            holder.status.setText("0/1/1");
            holder.markAsAssembled.setBackground(activity.getResources().getDrawable(R.drawable.round_button_light_blue));
            holder.repack.setBackground(activity.getResources().getDrawable(R.drawable.round_button_light_blue));
            holder.reassemble.setBackground(activity.getResources().getDrawable(R.drawable.round_button_light_grey));
        } else if ("COMPLETED".equals(singleItem.assemblyStatus()) && singleItem.isAssembled()) {
            holder.layout.setBackgroundColor(activity.getResources().getColor(R.color.list_green));
            holder.status.setText("1/1/1");
            holder.markAsAssembled.setBackground(activity.getResources().getDrawable(R.drawable.round_button_light_grey));
            holder.repack.setBackground(activity.getResources().getDrawable(R.drawable.round_button_light_blue));
            holder.reassemble.setBackground(activity.getResources().getDrawable(R.drawable.round_button_light_blue));
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

        holder.markAsAssembled.setOnClickListener(v -> {
            if ("PENDING".equals(singleItem.assemblyStatus())) {
                Toast.makeText(activity, "Please pack this item before assembling", Toast.LENGTH_SHORT).show();
            } else {
                UpdateOrderInventoryProductMutation updateOrderInventoryProductMutation = new UpdateOrderInventoryProductMutation(
                        Order_orderInventoryProduct_pk_columns_input
                                .builder()
                                .id(singleItem.id())
                                .build(),
                        Order_orderInventoryProduct_set_input
                                .builder()
                                .isAssembled(true)
                                .build());

                Network.apolloClient.mutate(updateOrderInventoryProductMutation).enqueue(new ApolloCall.Callback<UpdateOrderInventoryProductMutation.Data>() {
                    @Override
                    public void onResponse(@NotNull Response<UpdateOrderInventoryProductMutation.Data> response) {
                        Timber.e("onResponse : " + response.toString());
                        inventoryProductListener.onResponse(singleItem,"Marked Assembled Successfully");
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        Timber.e("onFailure : " + e.getMessage());
                    }
                });
            }

        });

        holder.repack.setOnClickListener(v -> {
            if ("PENDING".equals(singleItem.assemblyStatus())) {
                Toast.makeText(activity, "Please pack this item before repacking", Toast.LENGTH_SHORT).show();
            } else {
                UpdateOrderInventoryProductMutation updateOrderInventoryProductMutation = new UpdateOrderInventoryProductMutation(
                        Order_orderInventoryProduct_pk_columns_input
                                .builder()
                                .id(singleItem.id())
                                .build(),
                        Order_orderInventoryProduct_set_input
                                .builder()
                                .assemblyStatus("PENDING")
                                .build());

                Network.apolloClient.mutate(updateOrderInventoryProductMutation).enqueue(new ApolloCall.Callback<UpdateOrderInventoryProductMutation.Data>() {
                    @Override
                    public void onResponse(@NotNull Response<UpdateOrderInventoryProductMutation.Data> response) {
                        Timber.e("onResponse : " + response.toString());
                        inventoryProductListener.onResponse(singleItem,"Repacked Successfully");
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        Timber.e("onFailure : " + e.getMessage());
                    }
                });
            }

        });


        holder.reassemble.setOnClickListener(v -> {
            if (!("COMPLETED".equals(singleItem.assemblyStatus()) && singleItem.isAssembled())) {
                Toast.makeText(activity, "Please assemble this item before reassembling", Toast.LENGTH_SHORT).show();
            } else {
                UpdateOrderInventoryProductMutation updateOrderInventoryProductMutation = new UpdateOrderInventoryProductMutation(
                        Order_orderInventoryProduct_pk_columns_input
                                .builder()
                                .id(singleItem.id())
                                .build(),
                        Order_orderInventoryProduct_set_input
                                .builder()
                                .isAssembled(false)
                                .build());

                Network.apolloClient.mutate(updateOrderInventoryProductMutation).enqueue(new ApolloCall.Callback<UpdateOrderInventoryProductMutation.Data>() {
                    @Override
                    public void onResponse(@NotNull Response<UpdateOrderInventoryProductMutation.Data> response) {
                        Timber.e("onResponse : " + response.toString());
                        inventoryProductListener.onResponse(singleItem,"Reassembled Successfully");
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {
                        Timber.e("onFailure : " + e.getMessage());
                    }
                });
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
        @BindView(R.id.repack)
        LinearLayout repack;
        @BindView(R.id.reassemble)
        LinearLayout reassemble;
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

