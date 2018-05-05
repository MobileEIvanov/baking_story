package com.bakingstory.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by emil.ivanov on 3/28/18.
 * <p>
 * Igredient POJO representation
 * <p>
 * "quantity":350,
 * "measure":"G",
 * "ingredient":"Bittersweet chocolate (60-70% cacao)"
 */

public class Ingredient implements Parcelable, MeasurementTypes {
    public static final String INGREDIENT_DATA = "ingredient_data";

    private long id;
    private double quantity;
    private String measure;
    private String ingredient;


    protected Ingredient(Parcel in) {
        id = in.readLong();
        quantity = in.readDouble();
        measure = in.readString();
        ingredient = in.readString();
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getQuantity() {

        return quantity;
    }

    public String getFormattedQuantity() {

        return new java.text.DecimalFormat("#").format(quantity);
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }


    /**
     * Returns the designated measure as plain, user friendly text.
     *
     * @return - String with the measure
     */
    public String getMeasurementAsPlainText() {
        String measurePlainText;

        switch (this.measure) {
            case MeasurementTypes.GRAMS:
                measurePlainText = "grams";
                break;
            case MeasurementTypes.UNIT:
                measurePlainText = "unit";
                break;
            case MeasurementTypes.TBLSP:
                measurePlainText = "table spoon";
                break;
            case MeasurementTypes.TSP:
                measurePlainText = "tea spoon";
                break;
            case MeasurementTypes.KILO:
                measurePlainText = "kilo";
                break;
            case MeasurementTypes.OZ:
                measurePlainText = "ounce";
                break;
            case MeasurementTypes.CUP:
                measurePlainText = "cup";
                break;
            default:
                measurePlainText = "N/A";
        }
        return measurePlainText;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", measure='" + measure + '\'' +
                ", ingredient='" + ingredient + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeDouble(quantity);
        dest.writeString(measure);
        dest.writeString(ingredient);
    }
}
