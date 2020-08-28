package org.dailykit.adapter;

import android.app.Activity;
import androidx.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.dailykit.R;
import org.dailykit.room.database.GroctaurantDatabase;
import org.dailykit.room.entity.ItemEntity;
import org.dailykit.room.entity.OrderEntity;
import org.dailykit.util.AppUtil;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.SingleItemRowHolder> {

    public static final String TAG = "OrderAdapter";
    Context context;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    int currentPosition;
    OrderInternalAdapter adapter;
    private List<OrderEntity> allDetailModelArrayList;
    private Activity activity;
    GroctaurantDatabase groctaurantDatabase;
    private List<ItemEntity> itemEntityList;

    public OrderAdapter(Activity activity, List<OrderEntity> allDetailModelArrayList) {
        this.allDetailModelArrayList = allDetailModelArrayList;
        this.activity = activity;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent,false);
        SingleItemRowHolder rowHolder = new SingleItemRowHolder(v);
        sharedpreferences = AppUtil.getAppPreferences(activity);
        editor = sharedpreferences.edit();
        groctaurantDatabase = Room.databaseBuilder(activity, GroctaurantDatabase.class, "Development")
                .allowMainThreadQueries()
                .build();
        return rowHolder;
    }

    @Override
    public void onBindViewHolder(final SingleItemRowHolder holder, final int position) {
        final OrderEntity singleItem = allDetailModelArrayList.get(position);
        itemEntityList = groctaurantDatabase.itemDao().loadItemsByOrderId(singleItem.getOrderId());
        adapter = new OrderInternalAdapter(activity, itemEntityList, position, singleItem.getOrderId());
        holder.orderList.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        holder.orderList.setAdapter(adapter);
        holder.orderName.setText(singleItem.getOrderId());
        adapter.notifyDataSetChanged();

    }

    public void updateList() {
        this.notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return (null != allDetailModelArrayList ? allDetailModelArrayList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        protected RecyclerView orderList;
        protected TextView orderName;

        public SingleItemRowHolder(View itemView) {
            super(itemView);
            this.orderList = (RecyclerView) itemView.findViewById(R.id.order_internal_list);
            this.orderName = (TextView) itemView.findViewById(R.id.order_name);
        }
    }
}

