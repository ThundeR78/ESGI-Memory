package fr.esgi.android.project.esgi_memory.util;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.text.format.DateFormat;

public class FormatDate {

	//DATE FORMAT
	public static SimpleDateFormat dateWebServiceFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	public static SimpleDateFormat datetimeShortFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
	public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	public static SimpleDateFormat datetimeLabelFormat = new SimpleDateFormat("HH:mm 'le' dd/MM/yyyy");
	public static SimpleDateFormat timedateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
	public static NumberFormat numberFormat = NumberFormat.getInstance();
	
	public static String millisecondFormat(long time) {
	    if (time > 60000)	//1min
	    	return (String) DateFormat.format("m'min' ss'sec'", new Date(time));
	    else 
	    	return (String) DateFormat.format("s'sec'", new Date(time));
	}
}
