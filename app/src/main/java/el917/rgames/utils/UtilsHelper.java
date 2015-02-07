package el917.rgames.utils;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class UtilsHelper extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        UtilsHelper.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return UtilsHelper.context;
    }


    public static int[] getRandomColor() {
        //0--》background
        //1--》text color

        Random random = new Random();
        int RGB = 0xff + 1;
        int[] colors = new int[2];
        int a = 256;
        int r1 = (int) Math.floor(Math.random() * RGB);
        int r2 = (int) Math.floor(Math.random() * RGB);
        int r3 = (int) Math.floor(Math.random() * RGB);
        colors[0] = Color.rgb(r1, r2, r3);
        if ((r1 + r2 + r3) > 450) {
            colors[1] = Color.parseColor("#222222");
        } else {
            colors[1] = Color.parseColor("#ffffff");
        }
        return colors;
    }


    /*Arraylist in ArrayString*/
    public static String[] arrayListToArrayString(ArrayList<String> arrayList) {
        return arrayList.toArray(new String[arrayList.size()]);
    }

    /*delete duplicat*/
    public static ArrayList<String> arrayListDeleteDuplicat(ArrayList<String> arrayList) {
        return new ArrayList<String>(new HashSet<String>(arrayList));
    }


    /*получить все значения из курсора*/
    public static String[] arrayStringFromCursor(Cursor cursor, String nameColumn, String isNull) {
        int i = -1;
        String[] values = new String[cursor.getCount()];
        if (cursor.moveToFirst()) {
            do {
                i++;
                if (cursor.getString(cursor.getColumnIndex(nameColumn)) != null) {
                    values[i] = (cursor.getString(cursor.getColumnIndex(nameColumn)));
                } else {
                    values[i] = ("-");
                }
            } while (cursor.moveToNext());
        }

        return values;
    }



    public static String[] arrayStringFromCursor(Cursor cursor, int columnIndex, String isNull) {
        int i = -1;
        String[] values = new String[cursor.getCount()];
        if (cursor.moveToFirst()) {
            do {
                i++;
                if (cursor.getString(columnIndex) != null) {
                    values[i] = (cursor.getString(columnIndex));
                } else {
                    values[i] = (isNull);
                }
            } while (cursor.moveToNext());
        }

        return values;
    }

    public static ArrayList<String> arrayListFromCursorWithNameColumn(Cursor cursor, String isNull) {

        ArrayList<String> arrayList = new ArrayList<String>();
        if (cursor.moveToFirst()) {
            do {
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    //добавляем имя столбца
                    arrayList.add(cursor.getColumnName(i));
                    if (cursor.getString(i) != null) {
                        arrayList.add(cursor.getString(i));
                    } else {
                        arrayList.add(isNull);
                    }
                }


            } while (cursor.moveToNext());
        }

        return arrayList;
    }


    /*Остановка потока*/
    public static void stopThread(int sec) {
        try {
            TimeUnit.SECONDS.sleep(sec);
        } catch (InterruptedException e) {
            Log.d(getTag(), String.valueOf(e));
        }
    }


    /*Сохранить Assests как строку*/
    static String loadAssetTextAsString(Context context, String name) {
        BufferedReader bufferedReader = null;
        try {
            StringBuilder stringBuilder = new StringBuilder();
            InputStream inputStream;
            inputStream = context.getAssets().open(name);
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String str;
            boolean isFirst = true;
            while ((str = bufferedReader.readLine()) != null) {
                if (isFirst) {
                    isFirst = false;

                } else {
                    stringBuilder.append('\n');
                }
                stringBuilder.append(str);
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            Log.e(getTag(), "Error opening asset " + name);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    Log.e(getTag(), "Error closing asset " + name);
                }

            }
        }
        return null;
    }

    /*Универсальный TAG*/
    public static String getTag() {
        String tag = "";
        final StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        for (int i = 0; i < ste.length; i++) {
            if (ste[i].getMethodName().equals("getTag")) {
                tag = ste[i + 1].getClassName() + " | LINE = " + ste[i + 1].getLineNumber() + " | TEXT = ";
            }
        }
        return tag;
    }

    /*Закрывает потоки, курсоры, каналы, cокеты и многие другие. Спокойно примет null в качестве параметра*/
    public static boolean close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
                return true;
            } catch (IOException e) {
                Log.e(UtilsHelper.getTag(), String.valueOf(e));
                return false;
            }
        } else {
            return false;
        }
    }


    /*Rонвектирует InputStream в String*/
    public static String convertStreamToString(InputStream is) {
        try {
            return new java.util.Scanner(is).useDelimiter("\\A").next();
        } catch (java.util.NoSuchElementException e) {
            return "";
        }
    }

    /*Проверяем интернет и не забываем <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />*/
    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }


    /*Высылает тост если ON internet  и не забываем     <uses-permission android:name="android.permission.INTERNET" />*/
/*    public static class NetworkReceiver extends BroadcastReceiver {
        static boolean isInternet;

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {

                Toast.makeText(context, "wifi_  connected", Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(context, "lost_  connection", Toast.LENGTH_SHORT).show();

        }
        }*/


    /*Кидаем адрес берем поток*/
    public static InputStream downloadUrl(String urlAdress) throws IOException {
        URL url = new URL(urlAdress);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();
        return conn.getInputStream();
    }

    /*Инкапсулирует рутинную работы по копированию данных из InputStream в OutputStream*/
    private static int writeFromInputToOutput(InputStream source, OutputStream dest) {
        final int EOF_MARK = -1;
        final int BUFFER_SIZE = 2048;

        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead = EOF_MARK;
        int count = 0;
        try {
            while ((bytesRead = source.read(buffer)) != EOF_MARK) {
                dest.write(buffer, 0, bytesRead);
                count += bytesRead;
            }
        } catch (IOException e) {
            Log.e(UtilsHelper.getTag(), String.valueOf(e));
        }
        return count;
    }


    /*Проверяет доступность внешней памяти — SD-карты*/
    private static boolean isExternalAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }


    /*Запись во внутренюю память*/

    /*  Пример:
    final String fileName = "menudomenu";
    final String testText = "here you are here i am fuck you all";
    InputStream is = new ByteArrayInputStream(testTex.getBytes());
    FileUtils.writeInternal(this, is, fileName, MODE_PRIVATE);
    close(is);*/
    /*MODE_APPEND = 0×00008000 : сообщает о том, что если файл существует, то не нужно затирать содежимое, а дописывать в конец;
    MODE_PRIVATE = 0×00000000 : говорит о том, что доступ к файлу будет только у приложения, которое его создало;
    MODE_WORLD_READABLE = 0×00000001 : данной файл смогут читать другие приложения;
    MODE_WORLD_WRITEABLE = 0×00000002 : данный файл будет доступен для записи другим приложениям;*/
    public static boolean writeInternal(Context context, InputStream source, String fileName, int mode) {
        BufferedOutputStream dest = null;
        try {
            dest = new BufferedOutputStream(context.openFileOutput(fileName, mode));
            writeFromInputToOutput(source, dest);
            return true;
        } catch (FileNotFoundException e) {
            Log.e(UtilsHelper.getTag(), String.valueOf(e));
            return false;
        } finally {
            close(dest);
        }
    }

    /*Чтение из внутреней памяти*/

        /* Пример
        InputStream is;
        is = readFromInternalile(this, fileName);
        String readResult = convertStreamToString(is);
        close(is); */

    public static InputStream readFromInternalile(Context context, String fileName) {
        BufferedInputStream source = null;
        try {
            source = new BufferedInputStream(context.openFileInput(fileName));
        } catch (FileNotFoundException e) {
            Log.e(UtilsHelper.getTag(), String.valueOf(e));
        }
        return source;
    }



    /*Запись во внешнюю память*/

    /*Пример:
        String sdText = "this text written to sd card";
        String filePath = "/files/filename.txt";
      InputStream is = new ByteArrayInputStream(sdText.getBytes());
        writeExternal(is, filePath);
        close(is);*/
    public static boolean writeExternal(InputStream source, String filePath) {
        File EXTERNAL_DIR = Environment.getExternalStorageDirectory();
        if (!isExternalAvailable())
            return false;

        File dest = new File(EXTERNAL_DIR + File.separator + filePath);
        FileOutputStream writer = null;

        try {
            if (!dest.exists()) {
                dest.getParentFile().mkdirs();
                dest.createNewFile();
            }
            writer = new FileOutputStream(dest);
            writeFromInputToOutput(source, writer);
            return true;
        } catch (IOException e) {
            Log.e(UtilsHelper.getTag(), String.valueOf(e));
            return false;
        } finally {
            close(writer);
        }
    }
    /*Чтение из внешней памяти*/

    /* Пример:
    InputStream is
    is = FileUtils.readExternal(filePath);
    readResult = CommonIO.convertStreamToString(is);
    close(is);*/
    public static InputStream readExternal(String filePath) {
        File EXTERNAL_DIR = Environment.getExternalStorageDirectory();
        if (!isExternalAvailable()) {
            return null;
        }
        File source = new File(EXTERNAL_DIR + File.separator + filePath);
        InputStream out = null;
        if (source.exists()) {
            try {
                out = new FileInputStream(source);
            } catch (FileNotFoundException e) {
                Log.e(UtilsHelper.getTag(), String.valueOf(e));
            }
        }
        return out;
    }
}



