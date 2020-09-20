package org.dailykit.viewmodel;

import android.app.Application;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.dailykit.OrderListSubscription;
import org.dailykit.constants.Constants;
import org.dailykit.listener.OrderListener;
import org.dailykit.retrofit.APIInterface;
import org.dailykit.retrofit.RetrofitClient;
import org.dailykit.util.AppUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DashboardViewModel extends AndroidViewModel {

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    private APIInterface apiInterface;
    Type type;

    public DashboardViewModel(@NonNull Application application) {
        super(application);
        sharedpreferences = AppUtil.getAppPreferences(application);
        editor = sharedpreferences.edit();
        apiInterface = RetrofitClient.getClient().getApi();
    }


    public void showScanToast() {
        if (!sharedpreferences.getString(Constants.INGREDIENT_FOUND, "").equalsIgnoreCase("")) {
            Toast.makeText(getApplication(), sharedpreferences.getString(Constants.INGREDIENT_FOUND, "").trim(), Toast.LENGTH_LONG).show();
            editor.putString(Constants.INGREDIENT_FOUND, "");
            editor.commit();
        }
    }

    public void setOrderList(List<OrderListSubscription.Order> orderEntityList) {
        editor.putString(Constants.ORDER_LIST, new Gson().toJson(orderEntityList));
        editor.commit();
    }

    public List<OrderListSubscription.Order> getOrderList() {
        type = new TypeToken<List<OrderListSubscription.Order>>() {
        }.getType();
        List<OrderListSubscription.Order> orderList = new Gson().fromJson(sharedpreferences.getString(Constants.ORDER_LIST, ""), type);
        return orderList;
    }

    public void removeSelectedOrder() {
        editor.putString(Constants.SELECTED_ORDER,null);
        editor.commit();
    }

    public void setSelectedOrder(OrderListSubscription.Order order) {
        editor.putString(Constants.SELECTED_ORDER, new Gson().toJson(order));
        editor.commit();
    }

    public OrderListSubscription.Order getSelectedOrder() {
        type = new TypeToken<OrderListSubscription.Order>() {
        }.getType();
        OrderListSubscription.Order order = new Gson().fromJson(sharedpreferences.getString(Constants.SELECTED_ORDER, ""), type);
        return order;
    }

    public ArrayList<String> getTabList(){
        type = new TypeToken<ArrayList<String>>() {}.getType();
        ArrayList<String> tabList = new Gson().fromJson(sharedpreferences.getString(Constants.TAB_LIST, null), type);
        if(null==tabList){
            tabList=new ArrayList<>();
        }
        return tabList;
    }

    public void addToTabList(String orderId){
        ArrayList<String> tabList = getTabList();
        for(String tab:tabList){
            if(orderId.equals(tab)){
                return;
            }
        }
        tabList.add(orderId);
        editor.putString(Constants.TAB_LIST, new Gson().toJson(tabList));
        editor.commit();
    }

    public void removeFromTabList(OrderListener orderListener,String orderId){
        ArrayList<String> tabList = getTabList();
        tabList.remove(orderId);
        editor.putString(Constants.TAB_LIST, new Gson().toJson(tabList));
        editor.commit();
        orderListener.updateTabList(getTabList());
    }

    public void setSelectedOrder(String orderId){
        List<OrderListSubscription.Order> orderEntityList = getOrderList();
        for(OrderListSubscription.Order order:orderEntityList){
            if(orderId.equals(order.id())){
                setSelectedOrder(order);
                break;
            }
        }
    }

}
