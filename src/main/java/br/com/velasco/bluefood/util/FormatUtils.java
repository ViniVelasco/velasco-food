package br.com.velasco.bluefood.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class FormatUtils {
	
	private static final Locale LOCALE_BRAZIl = new Locale("pt", "br");
	
	public static NumberFormat newCurrencyFormat() {
		NumberFormat nf = NumberFormat.getNumberInstance(LOCALE_BRAZIl);
	
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		nf.setGroupingUsed(false);
		
		return nf;
	}
	
	public static String formatCurrency(BigDecimal number) {
		if(number == null) {
			return null;
		}
		
		return newCurrencyFormat().format(number);
	}

}
