package Util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Data {
    public static int data() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String dateString = dateFormat.format(new Date());
        int date = Integer.parseInt(dateString);
        return date;
    }

    public static int dataPrint() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String dateString = dateFormat.format(new Date());
        int date = Integer.parseInt(dateString);
        return date;
    }
}


