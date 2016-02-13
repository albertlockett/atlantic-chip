package ca.albertlockett.atlanticchip.util;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * date time utils - adapted from
 * http://www.mkyong.com/java/java-time-elapsed-in-days-hours-minutes-seconds/
 * 
 * @author albertlockett
 *
 */
public class DateTimeUtils {
	
	private static final Logger logger = LoggerFactory
			.getLogger(DateTimeUtils.class);
	
	public static void printDifference(Date startDate, Date endDate) {
		printDifference(startDate, endDate, logger);
	}
	
	//1 minute = 60 seconds
	//1 hour = 60 x 60 = 3600
	//1 day = 3600 x 24 = 86400
	public static void printDifference(Date startDate, Date endDate, Logger logger){
	
		//milliseconds
		long different = endDate.getTime() - startDate.getTime();
		
		logger.info("startDate : " + startDate);
		logger.info("endDate : "+ endDate);
		logger.info("different : " + different);
		
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
		
		StringBuilder dateDiff = new StringBuilder();
		dateDiff.append(elapsedDays).append(" days,")
				.append(elapsedHours).append(" hours,")
				.append(elapsedMinutes).append(" minutes,")
				.append(elapsedSeconds).append(" seconds");
		logger.info(dateDiff.toString());
	}		
}
