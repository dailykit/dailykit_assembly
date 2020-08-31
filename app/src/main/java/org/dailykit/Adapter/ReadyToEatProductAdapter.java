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

public class ReadyToEatProductAdapter extends RecyclerView.Adapter<ReadyToEatProductAdapter.ViewHolder> {

    private List<OrderListSubscription.OrderReadyToEatProduct> orderReadyToEatProductList;
    private Activity activity;
    private OrderListener orderListener;

    public ReadyToEatProductAdapter(Activity activity, OrderListener orderListener, List<OrderListSubscription.OrderReadyToEatProduct> orderReadyToEatProductList) {
        this.orderReadyToEatProductList = orderReadyToEatProductList;
        this.orderListener = orderListener;
        this.activity = activity;
    }

    @Override
    public ReadyToEatProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ready_to_eat_product, parent, false);
        ReadyToEatProductAdapter.ViewHolder rowHolder = new ReadyToEatProductAdapter.ViewHolder(v);
        return rowHolder;
    }

    @Override
    public void onBindViewHolder(final ReadyToEatProductAdapter.ViewHolder holder, final int position) {
        final OrderListSubscription.OrderReadyToEatProduct singleItem = orderReadyToEatProductList.get(position);
        holder.name.setText(singleItem.simpleRecipeProduct().name());
        holder.quantity.setText(" ");
    }

    public void updateList() {
        this.notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return (null != orderReadyToEatProductList ? orderReadyToEatProductList.size() : 0);
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

