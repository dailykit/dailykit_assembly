package org.dailykit.room.converter;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.dailykit.room.entity.IngredientDetailEntity;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Danish Rafique on 09-10-2018.
 */
public class IngredientDetailConverter {

    @TypeConverter
    public static ArrayList<IngredientDetailEntity> stringToIngredientEntityList(String value) {
        Type listType = new TypeToken<ArrayList<IngredientDetailEntity>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String ingredientEntityListToString(ArrayList<IngredientDetailEntity> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}
