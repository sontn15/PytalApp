package com.sh.pytalapp.utils;

import android.annotation.SuppressLint;

import com.sh.pytalapp.model.dto.NumberDTO;

import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

public class StringFormatUtils {

    public static String getCurrentDateStr() {
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") String date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(calendar.getTime());
        return date;
    }

    public static String getCurrentDateNotTimeStr() {
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") String date = new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime());
        return date;
    }

    public static String getCurrentDateNotTimeKeyStr() {
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") String date = new SimpleDateFormat("ddMMyyyy").format(calendar.getTime());
        return date;
    }

    public static String getAlphaNumericString(int n) {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            int index = (int) (AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }
        return sb.toString();
    }

    public static boolean isNullOrEmpty(String s) {
        return s == null || "".equals(s.trim());
    }

    public static String convertListNumberToString(List<NumberDTO> listNumberChooses) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < listNumberChooses.size(); i++) {
            result.append(listNumberChooses.get(i).getValue()).append(",");
        }
        return result.substring(0, result.length() - 1);
    }

    public static void convertStringToListNumber(List<NumberDTO> listNumber,
                                                 List<NumberDTO> listNumberChooses,
                                                 String input) {
        listNumberChooses.clear();
        List<String> arrNumbers = Arrays.asList(input.split(","));
        for (int i = 0; i < listNumber.size(); i++) {
            if (arrNumbers.contains(listNumber.get(i).getValue())) {
                listNumber.get(i).setSelected(true);
                listNumberChooses.add(listNumber.get(i));
            } else {
                listNumber.get(i).setSelected(false);

            }
        }
    }

    public static String getTextFromSoiCauMbStringToView(String input) {
        StringBuilder response = new StringBuilder();
        List<String> listData = Arrays.asList(input.split(","));
        for (int i = 1; i <= listData.size(); i++) {
            response.append(listData.get(i - 1)).append(",");
            if (i % 9 == 0 && i != listData.size()) {
                response.append("\n");
            }
        }
        return response.substring(0, response.length() - 1);
    }

    public static String convertUTF8ToString(String value) {
        try {
            String temp = Normalizer.normalize(value, Normalizer.Form.NFD);
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            return pattern.matcher(temp).replaceAll("").replace("Đ", "D")
                    .replace("đ", "d").toLowerCase();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
