package utility;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class DateUtils {
	public static int findDayDiff(Calendar d1, Calendar d2) {
		return (int) (d2.getTimeInMillis() - d1.getTimeInMillis()) / (24 * 60 * 60 * 1000);
	}
	
	
	public static int findDayDiff(Date d1, Date d2) {
		return (int) (d2.getTime() - d1.getTime()) / (24 * 60 * 60 * 1000);
	}
	
	
	public static String reprHourMinutes(Calendar cal) {
		Locale locale = Locale.getDefault();

		String hr = Integer.toString(cal.get(Calendar.HOUR));
		String ampm = cal.getDisplayName(Calendar.AM_PM, Calendar.LONG, locale);
		String mins = (cal.get(Calendar.MINUTE) < 10 ? "0" : "") + Integer.toString(cal.get(Calendar.MINUTE));
		
		return hr + ":" + mins + ampm;
	}
}
