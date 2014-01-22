package fr.esgi.android.project.esgi_memory.util;

import java.text.NumberFormat;
import java.util.Locale;

public class FormatNumber {

	public static String formatPoint(int number) {
		return NumberFormat.getNumberInstance(Locale.getDefault()).format(number);
	}
}
