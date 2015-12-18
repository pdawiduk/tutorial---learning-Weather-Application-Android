package com.example.dawiduk.podejscie2.data;

/**
 * Created by dawiduk on 18-12-15.
 */
import android.provider.BaseColumns;
import android.text.format.Time;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Defines table and column names for the weather database.
 */
public class WeatherContract {

    private static final int HOUR_IN_MILISEC=60*60*1000;

    public static long normalizeDate(long startDate) {

        GregorianCalendar time = new GregorianCalendar();

        return time.get(Calendar.DAY_OF_MONTH);

    }

    /*
        Inner class that defines the contents of the location table
     */
    public static final class LocationEntry implements BaseColumns {

        public static final String TABLE_NAME = "location";


        public static final String COLUMN_LOCATION_SETTING = "location_setting";


        public static final String COLUMN_CITY_NAME = "city_name";


        public static final String COLUMN_COORD_LAT = "coord_lat";
        public static final String COLUMN_COORD_LONG = "coord_long";
    }


    public static final class WeatherEntry implements BaseColumns {

        public static final String TABLE_NAME = "weather";


        public static final String COLUMN_LOC_KEY = "location_id";

        public static final String COLUMN_DATE = "date";

        public static final String COLUMN_WEATHER_ID = "weather_id";


        public static final String COLUMN_SHORT_DESC = "short_desc";


        public static final String COLUMN_MIN_TEMP = "min";
        public static final String COLUMN_MAX_TEMP = "max";


        public static final String COLUMN_HUMIDITY = "humidity";


        public static final String COLUMN_PRESSURE = "pressure";


        public static final String COLUMN_WIND_SPEED = "wind";


        public static final String COLUMN_DEGREES = "degrees";
    }
}