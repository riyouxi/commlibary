package com.commlibary.utils;

import android.util.Log;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;


public abstract class Gsons {

    private final static String TAG = Gsons.class.getSimpleName();

    private Gsons() {}

    public static GsonBuilder gsonBuilder() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Long.class, LONG);
        gsonBuilder.registerTypeAdapter(BigDecimal.class, BIG_DECIMAL);
        return gsonBuilder;
    }

    private static TypeAdapter<BigDecimal> BIG_DECIMAL = new TypeAdapter<BigDecimal>() {

        @Override
        public void write(JsonWriter out, BigDecimal value) throws IOException {
            out.value(value);
        }

        @Override
        public BigDecimal read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            try {
                String value = in.nextString();
                if (value.trim().isEmpty()) {
                    return null;
                }
                return new BigDecimal(value);
            } catch (NumberFormatException e) {
                throw new JsonSyntaxException(e);
            }
        }

    };

    private static TypeAdapter<Long> LONG = new TypeAdapter<Long>() {

        @Override
        public void write(JsonWriter out, Long value) throws IOException {
            out.value(value);
        }

        @Override
        public Long read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            String value = in.nextString();
            if (value.trim().isEmpty()) {
                return null;
            }
            try {
                if (value.indexOf("-") > 0) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    java.util.Date date = sdf.parse(value);
                    return date.getTime();
                } else {
                    return Long.valueOf(value);
                }
            } catch (NumberFormatException nfe) {
                // throw new JsonSyntaxException(e);
                Log.e(TAG, nfe.getMessage(), nfe);
            } catch (ParseException pe) {
                Log.e(TAG, pe.getMessage(), pe);
            }

            return null;
        }

    };
}