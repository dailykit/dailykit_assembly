package org.dailykit.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.dailykit.OrderListSubscription;
import org.dailykit.R;
import org.dailykit.activity.ContinuousScanActivity;
import org.dailykit.listener.MealKitProductListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MealKitSachetODAdapter extends RecyclerView.Adapter<MealKitSachetODAdapter.ViewHolder> {

    private List<OrderListSubscription.OrderSachet> orderSachetList;
    private Activity activity;
    private MealKitProductListener mealKitProductListener;

    public MealKitSachetODAdapter(Activity activity, MealKitProductListener mealKitProductListener, List<OrderListSubscription.OrderSachet> orderSachetList) {
        this.orderSachetList = orderSachetList;
        this.mealKitProductListener = mealKitProductListener;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal_kit_product_od, parent, false);
        ViewHolder rowHolder = new ViewHolder(v);
        return rowHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final OrderListSubscription.OrderSachet singleItem = orderSachetList.get(position);
        holder.name.setText(singleItem.ingredientName());
        double unitSize = (null == singleItem.quantity()) ? 0L : (double) singleItem.quantity();
        String unit = (null == singleItem.unit()) ? "" : singleItem.unit();
        holder.serving.setText(unitSize + " " + unit);
        holder.processing.setText(singleItem.processingName());
        if ("PENDING".equals(singleItem.status())) {
            holder.layout.setBackgroundColor(activity.getResources().getColor(R.color.list_yellow));
            holder.status.setText("0/0/1");
        } else if ("PACKED".equals(singleItem.status()) && !singleItem.isAssembled()) {
            holder.layout.setBackgroundColor(activity.getResources().getColor(R.color.list_blue));
            holder.status.setText("0/1/1");
        } else if ("PACKED".equals(singleItem.status()) && singleItem.isAssembled()) {
            holder.layout.setBackgroundColor(activity.getResources().getColor(R.color.list_green));
            holder.status.setText("1/1/1");
        }
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
        return (null != orderSachetList ? orderSachetList.size() : 0);
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
