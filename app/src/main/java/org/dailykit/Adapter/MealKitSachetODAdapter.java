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
        holder.weight.setText(unitSize + " " + unit);
        holder.processing.setText(singleItem.processingName());
        holder.layout.setOnClickListener(v -> {
            Intent intent=new Intent(activity, ContinuousScanActivity.class);
            activity.startActivity(intent);
        });
    }

    public void updateList() {
        this.notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return (null != orderSachetList ? orderSachetList.size() : 0);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.enable_image)
        ImageView enableImage;
        @BindView(R.id.disable_image)
        ImageView disableImage;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.processing)
        TextView processing;
        @BindView(R.id.serving)
        TextView serving;
        @BindView(R.id.weight)
        TextView weight;
        @BindView(R.id.inner_layout)
        LinearLayout innerLayout;
        @BindView(R.id.layout)
        LinearLayout layout;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
