package com.agh.pum.listazakupow;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

class Product {
    int product_id;
    int category_id;
    String product_name;

    public Product(int product_id, String product_name, int category_id) {
        product_name = product_name;
        product_id = product_id;
        category_id = category_id;
    }

    public String toString () {
        String str = "Product o id " + product_id + "(kategoria " + category_id + "): " + product_name;
        return str;
    }

    static void insertProduct(SQLiteDatabase database) {

        ContentValues values = new ContentValues();
        values.put("product_id", 2);
        values.put("product_name", "maslo");
        values.put("category_id", 1);
        ContentValues values2 = new ContentValues();
        values2.put("product_id", 3);
        values2.put("product_name", "mleko");
        values2.put("category_id", 1);

        ContentValues values3 = new ContentValues();
        values3.put("product_id", 4);
        values3.put("product_name", "krzeslo");
        values3.put("category_id", 2);

        database.insert("products", null, values);
        database.insert("products", null, values2);
        database.insert("products", null, values3);
    }

    static void getProducts (SQLiteDatabase database, ArrayList<String> list){

        Cursor listCursor = database.query("products", new String[] {"product_id", "product_name", "category_id"}, null, null, null, null, null);
        listCursor.moveToFirst();
        Product p;
        if (!listCursor.isAfterLast()) {
            do {
                int product_id = listCursor.getInt(0);
                String product_name = listCursor.getString(1);
                list.add(product_name);
                int category_id = listCursor.getInt(2);

                p = new Product(product_id, product_name, category_id);

                System.out.println(p.toString());
            } while (listCursor.moveToNext());

        } else {
            System.out.println("No products in database");
        }

        listCursor.close();

    }
}