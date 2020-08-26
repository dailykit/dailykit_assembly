package org.dailykit.room.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "customer")
public class CustomerEntity {

    @ColumnInfo(name = "email")
    public String email;

    public CustomerEntity(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "CustomerEntity{" +
                "email='" + email + '\'' +
                '}';
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
