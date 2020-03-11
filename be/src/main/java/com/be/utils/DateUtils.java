package com.be.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class DateUtils {

	public static void main(String[] args) {
		System.out.println(isDateInWeeks(parseDate("2019-12-14","yyyy-MM-dd"),"6"));
	}
	
	/**
	 * week格式类似："1" 或者 "2,4,6"  1是星期一，2是星期二，以此类推
	 * @param date
	 * @param week
	 * @return
	 */
	public static boolean isDateInWeeks(Date date,String week) {
		if(date == null || StringUtils.isEmpty(week)) {
			return false;
		}
		Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dateweek = cal.get(Calendar.DAY_OF_WEEK);//周日是1，周六是7
        switch (dateweek) {
		case 1:
			dateweek = 7;
			break;
		default:
			dateweek = dateweek - 1;
			break;
		}
        
        return week.indexOf(String.valueOf(dateweek)) >= 0;
	}
	
	public static String parseDateToString(Date date, String format) {
		if(date == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	
	public static Date parseDate(Date date, String format) {
		if(date == null) {
			return null;
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.parse(sdf.format(date));
		} catch (ParseException e) {

		}
		return null;
	}

	public static Date parseDate(String date, String format) {
		if(StringUtils.isEmpty(date)) {
			return null;
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.parse(date);
		} catch (ParseException e) {

		}
		return null;
	}

	public static Date parseDate(String date, String format, int day) {
		if(StringUtils.isEmpty(date)) {
			return null;
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);

			Calendar rightNow = Calendar.getInstance();
			rightNow.setTime(sdf.parse(date));
			// rightNow.add(Calendar.YEAR,-1);//日期减1年
			// rightNow.add(Calendar.MONTH,3);//日期加3个月
			rightNow.add(Calendar.DAY_OF_YEAR, day);// 日期加10天
			// rightNow.add(Calendar.SECOND,60);//日期加60秒天

			return rightNow.getTime();
		} catch (ParseException e) {

		}
		return null;
	}
}
