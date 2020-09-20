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
import org.dailykit.listener.ReadyToEatProductListener;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class ReadyToEatProductODAdapter extends RecyclerView.Adapter<ReadyToEatProductODAdapter.ViewHolder> {

    private List<OrderListSubscription.OrderReadyToEatProduct> orderReadyToEatProducts;
    private Activity activity;
    private ReadyToEatProductListener readyToEatProductListener;

    public ReadyToEatProductODAdapter(Activity activity, ReadyToEatProductListener readyToEatProductListener, List<OrderListSubscription.OrderReadyToEatProduct> orderReadyToEatProducts) {
        this.orderReadyToEatProducts = orderReadyToEatProducts;
        this.readyToEatProductListener = readyToEatProductListener;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ready_to_eat_product_od, parent, false);
        ViewHolder rowHolder = new ViewHolder(v);
        return rowHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final OrderListSubscription.OrderReadyToEatProduct singleItem = orderReadyToEatProducts.get(position);
        String comboName = "";
        if(null != singleItem.comboProductId() && null != singleItem.comboProduct()){
            comboName = " - "+singleItem.comboProduct().name();
            if(null != singleItem.comboProductComponent()){
                comboName = comboName+" ("+singleItem.comboProductComponent().label()+")";
            }
        }
        try {
            JSONObject yield = new JSONObject(singleItem.simpleRecipeProductOption().simpleRecipeYield().yield().toString());
            holder.serving.setText(yield.get("serving")+"");
        } catch (Throwable t) {
            Timber.e(t.getMessage());
        }
        holder.name.setText(singleItem.simpleRecipeProduct().name()+comboName);
        holder.weight.setText(singleItem.quantity()+" nos");
        if (singleItem.isAssembled()) {
            holder.enableImage.setVisibility(View.VISIBLE);
            holder.disableImage.setVisibility(View.GONE);
        } else {
            holder.enableImage.setVisibility(View.GONE);
            holder.disableImage.setVisibility(View.VISIBLE);
        }
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
        return (null != orderReadyToEatProducts ? orderReadyToEatProducts.size() : 0);
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

