package com.bakingstory;

import android.content.Context;

import com.bakingstory.entities.Recepie;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by emil.ivanov on 3/28/18.
 */

public class HelperJsonDataParser {


    public static List<Recepie> getAllRecepies(Context context) throws IOException {
        List<Recepie> recepieList ;
        Type fooType = new TypeToken<ArrayList<Recepie>>() {}.getType();
        recepieList = new Gson().fromJson(extractJsonRecepiesFromFile(context),fooType);

        return recepieList;

    }


    private static String extractJsonRecepiesFromFile(Context context) throws IOException {


        InputStream inputStream = context.getAssets().open("data.json");
        int size = inputStream.available();
        byte[] buffer = new byte[size];
        inputStream.read(buffer);
        inputStream.close();
        return new String(buffer, "UTF-8");


    }
}
