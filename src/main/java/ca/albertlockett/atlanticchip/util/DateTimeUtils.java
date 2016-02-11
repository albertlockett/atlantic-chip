package ca.albertlockett.atlanticchip.util;

import java.util.Date;

/**
 * date time utils - adapted from
 * http://www.mkyong.com/java/java-time-elapsed-in-days-hours-minutes-seconds/
 * 
 * @author albertlockett
 *
 */
public class DateTimeUtils {
	//1 minute = 60 seconds
	//1 hour = 60 x 60 = 3600
	//1 day = 3600 x 24 = 86400
	public static void printDifference(Date startDate, Date endDate){
	
		//milliseconds
		long different = endDate.getTime() - startDate.getTime();
		
		System.out.println("startDate : " + startDate);
		System.out.println("endDate : "+ endDate);
		System.out.println("different : " + different);
		
		long secondsInMilli = 1000;
		long minutesInMilli = secondsInMilli * 60;
		long hoursInMilli = minutesInMilli * 60;
		long daysInMilli = hoursInMilli * 24;
	
		long elapsedDays = different / daysInMilli;
		different = different % daysInMilli;
		
		long elapsedHours = different / hoursInMilli;
		different = different % hoursInMilli;
		
		long elapsedMinutes = different / minutesInMilli;
		different = different % minutesInMilli;
		
		long elapsedSeconds = different / secondsInMilli;
		
		System.out.printf(
		    "%d days, %d hours, %d minutes, %d seconds%n", 
		    elapsedDays,
		    elapsedHours, elapsedMinutes, elapsedSeconds);
	}		
}
