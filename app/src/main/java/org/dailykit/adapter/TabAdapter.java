package org.dailykit.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.dailykit.OrderListSubscription;
import org.dailykit.R;
import org.dailykit.listener.OrderListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TabAdapter extends RecyclerView.Adapter<TabAdapter.SingleItemRowHolder> {


    private List<String> tabModelArrayList;
    private Activity activity;
    private OrderListener orderListener;

    public TabAdapter(Activity activity, OrderListener orderListener, List<String> tabModelArrayList) {
        this.tabModelArrayList = tabModelArrayList;
        this.orderListener = orderListener;
        this.activity = activity;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tab, parent, false);
        SingleItemRowHolder rowHolder = new SingleItemRowHolder(v);
        return rowHolder;
    }

    @Override
    public void onBindViewHolder(final SingleItemRowHolder holder, final int position) {
        final String singleItem = tabModelArrayList.get(position);
        holder.text.setText(singleItem);
        holder.close.setOnClickListener(v -> {
            orderListener.removeOrderFromList(singleItem);
        });
        holder.layout.setOnClickListener(v -> {
            orderListener.getOrder(singleItem);
        });

        OrderListSubscription.Order order = orderListener.getSelectedOrderIndividual();

        if(null != order && singleItem.equals((String)order.id())){
            holder.text.setTextColor(activity.getResources().getColor(R.color.white));
            holder.layout.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.round_button_light_blue));
            holder.close.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_close));
        }
        else {
            holder.text.setTextColor(activity.getResources().getColor(R.color.primary_blue));
            holder.layout.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.round_border_grey));
            holder.close.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_close_grey));
        }

    }

    public void updateList() {
        this.notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return (null != tabModelArrayList ? tabModelArrayList.size() : 0);
    }

    static
    class SingleItemRowHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text)
        TextView text;
        @BindView(R.id.close)
        ImageView close;
        @BindView(R.id.layout)
        FrameLayout layout;

        SingleItemRowHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}


