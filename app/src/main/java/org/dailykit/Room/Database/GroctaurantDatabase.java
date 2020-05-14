package org.dailykit.room.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import android.content.Context;
import android.util.Log;


import org.dailykit.room.converter.IngredientConverter;
import org.dailykit.room.converter.IngredientDetailConverter;
import org.dailykit.room.converter.ItemConverter;
import org.dailykit.room.dao.IngredientDao;
import org.dailykit.room.dao.IngredientDetailDao;
import org.dailykit.room.dao.ItemDao;
import org.dailykit.room.dao.OrderDao;
import org.dailykit.room.dao.TabDao;
import org.dailykit.room.entity.IngredientDetailEntity;
import org.dailykit.room.entity.IngredientEntity;
import org.dailykit.room.entity.ItemEntity;
import org.dailykit.room.entity.OrderEntity;
import org.dailykit.room.entity.TabEntity;


/**
 * Created by Danish Rafique on 24-08-2018.
 */

@Database(entities = {OrderEntity.class, ItemEntity.class, IngredientEntity.class, IngredientDetailEntity.class, TabEntity.class}, version = 1, exportSchema = false)
@TypeConverters({ItemConverter.class, IngredientConverter.class, IngredientDetailConverter.class})
public abstract class GroctaurantDatabase extends RoomDatabase {

    private static final String LOG_TAG = GroctaurantDatabase.class.getSimpleName();

    public abstract OrderDao orderDao();

    public abstract ItemDao itemDao();

    public abstract IngredientDao ingredientDao();

    public abstract IngredientDetailDao ingredientDetailDao();

    public abstract TabDao tabDao();

    private static GroctaurantDatabase INSTANCE;


    static GroctaurantDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (GroctaurantDatabase.class) {
                Log.d(LOG_TAG, "Creating new database instance");
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            GroctaurantDatabase.class, "groctaurant_database")
                            .build();

                }
            }
        }
        Log.d(LOG_TAG, "Getting the old database instance");
        return INSTANCE;
    }

}
