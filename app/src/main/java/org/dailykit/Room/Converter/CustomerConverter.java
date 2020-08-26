package org.dailykit.room.converter;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.dailykit.room.entity.CustomerEntity;
import org.dailykit.room.entity.ItemEntity;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class CustomerConverter {

    @TypeConverter
    public static ArrayList<ItemEntity> stringToCustomer(String value) {
        Type listType = new TypeToken<CustomerEntity>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String customerToString(CustomerEntity list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

}
