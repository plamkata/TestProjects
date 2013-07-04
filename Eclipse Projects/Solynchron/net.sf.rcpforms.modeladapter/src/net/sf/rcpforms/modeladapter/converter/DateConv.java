/*******************************************************************************
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *      - initial API and implementation
 *
 ******************************************************************************
 */

package net.sf.rcpforms.modeladapter.converter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import net.sf.rcpforms.modeladapter.Messages;
import net.sf.rcpforms.modeladapter.util.DateUtils;

/**
 * Helper class to provide conversion methods for ISO date that is used by Gatekeepres Auf IP4
 * sollen an den PKV-Gtk-Schnittstellen sämtliche Felder mit Datum vereinheitlicht werden. Die
 * Datum- Felder werden weiterhin als xs:string übergeben, als String-Pattern wird die Struktur nach
 * ISO 8601 gewählt, da so die Darstellung in der XML-Message derjenigen von xs:date entspricht
 * (siehe http://www.w3.org/TR/xmlschema-2/#dateTime) There is a unit test for the class
 * DateConvTest. Please use test first approach to modify the class. FIXME remove dependency to
 * DateUtils!
 */
public class DateConv
{
    public static String LOCALIZED_DATE_FORMAT = 
    	Messages.getString("DateConv.LOCALIZED_DATE_FORMAT"); //$NON-NLS-1$

    /** needed to parse shortcut-dates */
    public static final String LOCALIZED_TWODIGIT_DATE_FORMAT = 
    	Messages.getString("DateConv.LOCALIZED_TWODIGIT_DATE_FORMAT");  //$NON-NLS-1$

    public static final String LOCALIZED_DATETIME_FORMAT = 
    	Messages.getString("DateConv.LOCALIZED_DATETIME_FORMAT"); //$NON-NLS-1$

    public static final String LOCALIZED_DATETIMEMS_FORMAT = 
    	Messages.getString("DateConv.LOCALIZED_DATETIMEMS_FORMAT"); //$NON-NLS-1$

    public static final String GK_DATE_FORMAT = 
    	Messages.getString("DateConv.GK_DATE_FORMAT"); //$NON-NLS-1$

    public static final String GK_DATETIME_FORMAT = 
    	Messages.getString("DateConv.GK_DATETIME_FORMAT"); //$NON-NLS-1$

    private static final DateFormat s_localizedDateFormat = 
    	getDateFormat(LOCALIZED_DATE_FORMAT);
    
    private static final DateFormat s_localizedTwoDigitDateFormat = 
        getDateFormat(LOCALIZED_TWODIGIT_DATE_FORMAT);

    private static final DateFormat s_localizedDateTimeFormat = 
    	getDateTimeFormat(LOCALIZED_DATETIME_FORMAT);

    private static final DateFormat s_localizedDateTimeMsFormat = 
    	getDateTimeFormat(LOCALIZED_DATETIMEMS_FORMAT);

    private static final DateFormat s_gkDateFormat = 
    	getDateFormat(GK_DATE_FORMAT);

    private static final DateFormat s_gkDateTimeFormat = 
    	getDateTimeFormat(GK_DATETIME_FORMAT);

    public static final int LOCALIZED_DATE_LENGTH = 
    	getDateFormatLength(LOCALIZED_DATE_FORMAT);
    
    public static final int LOCALIZED_TWODIGIT_DATE_LENGTH = 
        getDateFormatLength(LOCALIZED_TWODIGIT_DATE_FORMAT);
    
    /**
     * Initialize the date format by the pattern from resources.
     * If no pattern exists in message resources the default locale will be used.
     *  
     * @param datePattern the date pattern from local resources, or !resourceKey!
     * @return a DateFormat that corresponds to the specified date pattern
     */
    public static DateFormat getDateFormat(String datePattern) {
    	DateFormat format = null;
    	if (!datePattern.contains("!")) {
    		try {
				format = new SimpleDateFormat(datePattern);
				((SimpleDateFormat) format).set2DigitYearStart(getStartDate());
			} catch (IllegalArgumentException e) {
				format = DateFormat.getDateInstance();
			}
    	} else {
    		format = DateFormat.getDateInstance();
    	}
    	return format;
    }
    
    /**
     * Initialize the date time format by the pattern from resources.
     * If no pattern exists in message resources the default locale will be used.
     *  
     * @param datePattern the date time pattern from message resources, or !resourceKey!
     * @return a DateFormat that corresponds to the specified date time pattern
     */
    public static DateFormat getDateTimeFormat(String datePattern) {
    	DateFormat format = null;
    	if (!datePattern.contains("!")) {
    		try {
				format = new SimpleDateFormat(datePattern);
				((SimpleDateFormat) format).set2DigitYearStart(getStartDate());
			} catch (IllegalArgumentException e) {
	    		format = DateFormat.getDateTimeInstance();
			}
    	} else {
    		format = DateFormat.getDateTimeInstance();
    	}
    	return format;
    }
    
    /**
     * Initialize the length of the DateFormat by the specified date pattern.
     * 
     * @param datePattern the date pattern from message resources
     * @return the number of characters in the date format strings
     */
    public static int getDateFormatLength(String datePattern) {
		DateFormat format = getDateFormat(datePattern);
		String now = format.format(new Date());
    	return now.length();
    }

    /**
     * date formatter used for parsing string dates which might be shortcut as yy; the start year
     * for extending 2-digit years is set to 1940
     * 
     * @return date formatter
     * @throws ParseException
     */
    private static Date getStartDate()
    {
    	Date startDate = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(LOCALIZED_TWODIGIT_DATE_FORMAT);
        try {
			startDate = simpleDateFormat.parse("01.01.1940");
		} catch (ParseException e) {
			startDate = simpleDateFormat.get2DigitYearStart();
		}
		return startDate;
    }

    /**
     * automatically detect and convert date if nescessary with time
     * 
     * @param dataStr date / or date-time represented as iso string (could be <code>null</code>)
     * @return valid date/date time or <code>null</code> if input string is <code>null</code> or
     *         blank
     * @throws IllegalArgumentException (runtime) if string could not be converted to date
     * @see #toDate(String)
     * @see #toDateTime(String)
     */
    public static Date fromString(final String dataStr)
    {
        Date result = null;
        if (isNotBlank(dataStr))
        {
            result = dataStr.length() <= LOCALIZED_DATE_LENGTH ? 
            		toDate(dataStr) : toDateTime(dataStr);
        }
        return result;
    }

    /**
     * Conver data in string format to java Date object
     * 
     * @param dataStr must be in format {@value #LOCALIZED_DATE_FORMAT} (could be <code>null</code>)
     * @return parsed data or <code>null</code> if supplied dataStr is <code>null</code> or blank
     * @exception IllegalArgumentException is thrown if string does not match expected format
     */
    public static Date toDate(final String dataStr)
    {
        Date result;
        if (isNotBlank(dataStr))
        {
            try
            {
                // FastDateFormat does not support parsing yet, but SimpleDateFormat is not thread safe
                if (dataStr.length() <= LOCALIZED_TWODIGIT_DATE_LENGTH) {
                    result = s_localizedTwoDigitDateFormat.parse(dataStr);
                } else {
                    result = s_localizedDateFormat.parse(dataStr);
                }
            }
            catch (ParseException e)
            {
                throw new IllegalArgumentException("dataStr: " + dataStr, e); //$NON-NLS-1$
            }
        }
        else
        {
            result = null;
        }
        return result;
    }

    /**
     * Conver data in string format to java Date object
     * 
     * @param dataStr must be in format {@value #LOCALIZED_DATETIME_FORMAT} (could be
     *            <code>null</code>)
     * @return parsed data or <code>null</code> if supplied dataStr is <code>null</code> or blank
     * @exception IllegalArgumentException is thrown if string does not match expected format
     */
    public static Date toDateTime(final String dataStr)
    {
        Date result;
        if (isNotBlank(dataStr))
        {
            try
            {
                // FastDateFormat does not support parsing yet, but SimpleDateFormat is not threead
                // safe
                result = s_localizedDateTimeFormat.parse(dataStr);
            }
            catch (ParseException e)
            {
                throw new IllegalArgumentException("Parse error in date time string " + dataStr); //$NON-NLS-1$
            }
        }
        else
        {
            result = null;
        }
        return result;
    }

    /**
     * Convert data to string format {@value LOCALIZED_DATE_FORMAT}
     * 
     * @param date to convert
     * @return string or <code>null</code> if supplied date is <code>null</code>
     */
    public static String toDateString(final Date date)
    {
        String res = null;
        if (date != null)
        {
            res = s_localizedDateFormat.format(date);
        }
        return res;
    }

    public static String toDateTimeString(final Date date)
    {
        String res = null;
        if (date != null)
        {
            res = s_localizedDateTimeFormat.format(date);
        }
        return res;
    }

    // /**
    // * Conver date to string in format <tt>\d{16}</tt> <tt>YYYYMMDDhhmmsscc</tt>
    // * @param date to convert
    // * @return string or <code>null</code> if supplied date is <code>null</code>
    // */
    // public static String toDateTimeString16(final Date date) {
    // String res = null;
    // if (date != null) {
    // res = StringUtils.substring(s_localizedDateTimeMsFormat.format(date), 0, 16);
    // }
    // return res;
    // }
    //
    // /**
    // * Conver date to string in format <tt>\d{18}</tt> <tt>YYYYMMDDhhmmsscccc</tt>
    // * @param date to convert
    // * @return string or <code>null</code> if supplied date is <code>null</code>
    // */
    // public static String toDateTimeString18(final Date date) {
    // String res = null;
    // if (date != null) {
    // res = StringUtils.rightPad(s_localizedDateTimeMsFormat.format(date), 18, '0');
    // }
    // return res;
    // }

    /**
     * Convert data to localized string format (with/without time)
     * 
     * @param date to convert
     * @param withTime <code>true</code> if date should be formatted with time <code>false</code>
     *            otherwise
     * @return date formatted string or <code>null</code> if supplied date is <code>null</code>
     */
    public static String toLocalizedString(final Date date, boolean withTime)
    {
        String res = null;
        if (date != null)
        {
            if (withTime)
            {
                res = s_localizedDateTimeMsFormat.format(date);
            }
            else
            {
                res = s_localizedDateFormat.format(date);
            }
        }
        return res;
    }

    /**
     * Convert data to localized string format with time up to seconds
     * 
     * @param date to convert
     * @return date formatted string or <code>null</code> if supplied date is <code>null</code>
     */
    public static String toLocalizedDateTime(final Date date)
    {
        String res = null;
        if (date != null)
        {
            res = s_localizedDateTimeFormat.format(date);
        }
        return res;
    }

    /**
     * Returns current date without a time
     * 
     * @return date
     */
    public static Date clearTime(final Date date)
    {
        return null != date ? DateUtils.truncate(date, Calendar.DAY_OF_MONTH) : null;
    }

    public static String toGkDateString(final Date date)
    {
        String res = null;
        if (date != null)
        {
            res = s_gkDateFormat.format(date);
        }
        return res;
    }

    public static String toGkDateTimeString(final Date date)
    {
        String res = null;
        if (date != null)
        {
            res = s_gkDateTimeFormat.format(date);
        }
        return res;
    }

    /**
     * parses a date-string in the Gatekeeper format to a valid date object
     * 
     * @param gkDateString the date as String as stored and delivered by the gatekeeper (in GDTs)
     * @return the date instance that corresponds to the date fed in as argument
     */
    public static Date fromGkDateString(final String gkDateString)
    {
        Date result = null;
        if (isNotBlank(gkDateString))
        {
            try
            {
                // FastDateFormat does not support parsing yet, but SimpleDateFormat is not threead
                // safe
                result = new SimpleDateFormat(GK_DATE_FORMAT).parse(gkDateString);
            }
            catch (ParseException e)
            {
                throw new IllegalArgumentException("gkDateString"); //$NON-NLS-1$
            }
        }
        return result;
    }

    public static Date fromGkDateTimeString(final String gkDateTimeString)
    {
        Date result = null;
        if (isNotBlank(gkDateTimeString))
        {
            try
            {
                // FastDateFormat does not support parsing yet, but SimpleDateFormat is not threead
                // safe
                result = new SimpleDateFormat(GK_DATETIME_FORMAT).parse(gkDateTimeString);
            }
            catch (ParseException e)
            {
                throw new IllegalArgumentException("gkDateTimeString"); //$NON-NLS-1$
            }
        }
        return result;
    }

    public static String getTodayGkDateString()
    {
        return toGkDateString(clearTime(new Date()));
    }

    public static Date getToday()
    {
        return clearTime(new Date());
    }

    public static Date getYesterday()
    {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -1);
        return clearTime(cal.getTime());
    }

    /* (non-Javadoc)
     * @see org.apache.commons.beanutils.Converter#convert(java.lang.Class, java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    public Object convert(final Class type, final Object value)
    {
        Object res = null;
        if (value != null)
        {
            if (type.equals(Date.class))
            {
                if (value instanceof String)
                {
                    final String strVal = (String) value;
                    try
                    {
                        res = fromString(strVal);
                    }
                    catch (Exception e)
                    {
                        // ignore
                    }
                    if (res == null)
                    {
                        throw new IllegalArgumentException("wrong format: " + value); //$NON-NLS-1$
                    }
                }
                else
                {
                    throw new IllegalArgumentException(
                            "unsupported conversion from type/value " + value); //$NON-NLS-1$
                }
            }
            else
            {
                throw new IllegalArgumentException(
                        "unsupported conversion to type " + type.getName()); //$NON-NLS-1$
            }
        }
        return res;
    }

    public static Date addTimeToDate(Date date, Date time)
    {

        Date result = null;

        if (date == null)
        {
            result = time;
        }
        else
        {
            Calendar calDate = Calendar.getInstance();
            //GregorianCalendar calDate = new GregorianCalendar();
            calDate.setTime(date);

            if (time != null)
            {
                Calendar calTime = new GregorianCalendar();
                calTime.setTime(time);

                calDate.set(Calendar.HOUR_OF_DAY, calTime.get(Calendar.HOUR_OF_DAY));
                calDate.set(Calendar.MINUTE, calTime.get(Calendar.MINUTE));
                calDate.set(Calendar.SECOND, calTime.get(Calendar.SECOND));
            }

            result = calDate.getTime();
        }

        return result;
    }

    private static boolean isNotBlank(String str)
    {
        int strLen;
        if (str == null || (strLen = str.length()) == 0)
        {
            return false;
        }
        for (int i = 0; i < strLen; i++)
        {
            if (!Character.isWhitespace(str.charAt(i)))
            {
                return true;
            }
        }
        return false;
    }
}
