package com.agh.pum.listazakupow;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by baratowl on 5/26/2016.
 */
public class SqlOpenHelper extends SQLiteOpenHelper {


        public static final String DBNAME = "shoppingdb.sqlite";
        public static final int VERSION =1;

        /**
         * class constructor
         */
        public SqlOpenHelper(Context context) {
            super(context, DBNAME, null, VERSION);
        }

        /**
         *
         */
        public void onCreate(SQLiteDatabase db) {
            createDatabase(db);
        }

        /**
         *
         */
        public void onUpgrade(SQLiteDatabase db, int a, int b) {

        }

        /**
         *
         */
        private void createDatabase(SQLiteDatabase db) {
            db.execSQL(createDbSql);
        }

        private String createDbSql = "CREATE TABLE products (\n" +
                "\tproduct_id integer primary key autoincrement not null,\n" +
                "\tproduct_name varchar,\n" +
                "\tcategory_id integer,\n" +
                "\tpopularity integer,\n" +
                "        foreign key (category_id) references categories(category_id)\n" +
                ");\n" +
                "\n" +
                "CREATE TABLE shops (\n" +
                "\tshop_id integer primary key autoincrement not null,\n" +
                "\tshop_name varchar,\n" +
                "\tlat float,\n" +
                "\tlon float,\n" +
                "\tpopularity integer,\n" +
                ");\n" +
                "\n" +
                "CREATE TABLE categories (\n" +
                "\tcategory_id integer primary key autoincrement not null,\n" +
                "\tcategory_name varchar,\n" +
                "\tpopularity integer\n" +
                ");\n" +
                "\n" +
                "CREATE TABLE shopping_list (\n" +
                "\tposition_id integer primary key autoincrement not null,\n" +
                "\tproduct_id integer,\n" +
                "\tquantity integer\n" +
                "         foreign key (product_id) references products(product_id)\n" +
                ");\n" +
                "\n" +
                "CREATE TABLE shops_categories (\n" +
                "\tshop_category_id integer primary key autoincrement not null,\n" +
                "\tshop_id integer,\n" +
                "\tcategory_id integer,\n" +
                "         foreign key (shop_id) references shops(shop_id),\n" +
                "         foreign key (category_id) references categories(category_id)\n" +
                ");";

}
