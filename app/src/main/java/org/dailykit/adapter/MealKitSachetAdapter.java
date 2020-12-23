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
import org.dailykit.listener.MealKitProductListener;
import org.dailykit.listener.OrderListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MealKitSachetAdapter extends RecyclerView.Adapter<MealKitSachetAdapter.ViewHolder> {

    private List<OrderListSubscription.OrderSachet> orderSachetList;
    private Activity activity;
    private MealKitProductListener mealKitProductListener;

    public MealKitSachetAdapter(Activity activity, MealKitProductListener mealKitProductListener, List<OrderListSubscription.OrderSachet> orderSachetList) {
        this.orderSachetList = orderSachetList;
        this.mealKitProductListener = mealKitProductListener;
        this.activity = activity;
    }

    @Override
    public MealKitSachetAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal_kit_sachet, parent, false);
        MealKitSachetAdapter.ViewHolder rowHolder = new MealKitSachetAdapter.ViewHolder(v);
        return rowHolder;
    }

    @Override
    public void onBindViewHolder(final MealKitSachetAdapter.ViewHolder holder, final int position) {
        final OrderListSubscription.OrderSachet singleItem = orderSachetList.get(position);
        holder.name.setText(singleItem.ingredientName());
        double unitSize = (null == singleItem.quantity())?0L:(double)singleItem.quantity();
        String unit = (null == singleItem.unit())?"":singleItem.unit();
        holder.quantity.setText(unitSize+" "+unit);
    }

    public void updateList() {
        this.notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return (null != orderSachetList ? orderSachetList.size() : 0);
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
