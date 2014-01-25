package fr.esgi.android.project.esgi_memory.util;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.text.format.DateFormat;

public class FormatValue {

	//DATE FORMAT
	public static SimpleDateFormat datetimeSQLiteFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
	public static SimpleDateFormat dateWebServiceFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
	public static SimpleDateFormat datetimeShortFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.getDefault());
	public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
	public static SimpleDateFormat datetimeLabelFormat = new SimpleDateFormat("HH:mm 'le' dd/MM/yyyy", Locale.getDefault());
	public static SimpleDateFormat datetimeLabelFormat2 = new SimpleDateFormat("'le 'dd/MM/yyyy' à 'HH'h'mm", Locale.getDefault());
	public static SimpleDateFormat timedateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy", Locale.getDefault());
	public static NumberFormat numberFormat = NumberFormat.getInstance();
	
	public static String millisecondFormat(long time) {
	    if (time > 60000)	//1min
	    	return (String) DateFormat.format("m'min' ss'sec'", new Date(time));
	    else 
	    	return (String) DateFormat.format("s'sec'", new Date(time));
	}
	
	public static String formatBigNumber(int number) {
		return NumberFormat.getNumberInstance(Locale.getDefault()).format(number);
	}
}
