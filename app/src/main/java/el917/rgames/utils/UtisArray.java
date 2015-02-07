package el917.rgames.utils;

import android.database.Cursor;

import java.util.ArrayList;

public class UtisArray {


    public static double[] toPrimitive(Double[] arrayDouble) {
        if (arrayDouble == null) {
            return null;
        } else if (arrayDouble.length == 0) {
            throw new NullPointerException("arrayDouble.length == 0");
        }
        final double[] result = new double[arrayDouble.length];
        for (int i = 0; i < arrayDouble.length; i++) {
            result[i] = arrayDouble[i];
        }
        return result;
    }

    public static double[] arrayDoubleFromCursor(Cursor cursor, String nameColumn) {
        int i = -1;
        ArrayList<Double> arrayList = new ArrayList<Double>();
        if (cursor.moveToFirst()) {
            do {

                if (cursor.getDouble(cursor.getColumnIndex(nameColumn)) != 0) {
                    arrayList.add(cursor.getDouble(cursor.getColumnIndex(nameColumn)));
                }
            } while (cursor.moveToNext());
        }

        return toPrimitive(arrayList.toArray((new Double[arrayList.size()])));
    }
    public static double[] toPrimitive(ArrayList<Double> doubleArrayList) {
        if (doubleArrayList == null) {
            return null;
        } else if (doubleArrayList.size() == 0) {
            throw new NullPointerException("doubleArrayList.size == 0");
        }
        return toPrimitive(doubleArrayList.toArray(new Double[doubleArrayList.size()]));
    }
}
