package org.dailykit.Room.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.util.Log;


import org.dailykit.Room.Converter.IngredientConverter;
import org.dailykit.Room.Converter.IngredientDetailConverter;
import org.dailykit.Room.Converter.ItemConverter;
import org.dailykit.Room.DAO.IngredientDao;
import org.dailykit.Room.DAO.IngredientDetailDao;
import org.dailykit.Room.DAO.ItemDao;
import org.dailykit.Room.DAO.OrderDao;
import org.dailykit.Room.DAO.TabDao;
import org.dailykit.Room.Entity.IngredientDetailEntity;
import org.dailykit.Room.Entity.IngredientEntity;
import org.dailykit.Room.Entity.ItemEntity;
import org.dailykit.Room.Entity.OrderEntity;
import org.dailykit.Room.Entity.TabEntity;


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
