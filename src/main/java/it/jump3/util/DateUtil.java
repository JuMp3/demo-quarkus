package it.jump3.util;

import io.smallrye.common.constraint.Assert;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    private DateTimeFormatter dateTimeFormatter;
    private SimpleDateFormat simpleDateFormat;
    private DateFormat dateFormat;
    private static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
    private static final DateTimeFormatter dateTimeFormatterDefault = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static final DateTimeFormatter dateTimeFormatterFe = DateTimeFormatter.ofPattern(DATE_PATTERN);
    private static final DateTimeFormatter dateTimeFormatTimestampDefault = DateTimeFormatter.ofPattern(DATE_PATTERN);
    private static final DateTimeFormatter attachmentFolderFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
    private static final DateTimeFormatter dateFormatterDefault = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter dateFormatterFe = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final String ZONE_ID_ITALY = "CET";

    public void init(String formatData) {
        this.dateTimeFormatter = DateTimeFormatter.ofPattern(formatData);
        this.simpleDateFormat = new SimpleDateFormat(formatData);
        this.dateFormat = new SimpleDateFormat(formatData);
    }

    public LocalDateTime getFromString(String date) {
        Assert.assertNotNull(this.dateTimeFormatter);
        return LocalDateTime.parse(date, this.dateTimeFormatter);
    }

    public String getFromLocalDateTime(LocalDateTime date) {
        Assert.assertNotNull(this.dateTimeFormatter);
        return date.format(this.dateTimeFormatter);
    }

    public String getFromTimestamp(Timestamp timestamp) {
        Assert.assertNotNull(this.simpleDateFormat);
        return this.simpleDateFormat.format(timestamp);
    }

    public String getDateDefault(String date) {
        Assert.assertNotNull(this.dateFormat);
        return this.dateFormat.format(date);
    }

    public static LocalDateTime getDateTimeFromStringDefault(String date) {
        if (date == null) return null;
        return LocalDateTime.parse(date, dateTimeFormatterDefault);
    }

    public static LocalDateTime getDateTimeFromStringFe(String date) throws ParseException {
        if (date == null) return null;
        Timestamp timestamp = getTimestampFromStringDefault(date);
        return getLocalDateTime(timestamp);
    }

    public static Timestamp getTimestampFromStringDefault(String date) throws ParseException {
        if (date == null) return null;
        Date parsedDate = new SimpleDateFormat(DATE_PATTERN).parse(date);
        return new Timestamp(parsedDate.getTime());
        //ZonedDateTime zdt = ZonedDateTime.parse(date, dateTimeFormatTimestampDefault);
        //return Timestamp.valueOf(zdt.toLocalDateTime());
    }

    /*public static void main(String[] args) throws ParseException {

        Timestamp timestamp = getTimestampFromStringDefault("2020-11-25T12:06:29.375+02:00");
        System.out.println(timestamp);

        String ts = getDateTimeFromTimestampDefault(timestamp);
        System.out.println(ts);
    }*/

    public static LocalDate getLocalDateFromStringDefault(String date) {
        if (date == null) return null;
        return LocalDate.parse(date, dateFormatterDefault);
    }

    public static LocalDate getDateFromStringFe(String date) {
        if (date == null) return null;
        return LocalDate.parse(date, dateFormatterFe);
    }

    public static String getFromLocalDateTimeDefault(LocalDateTime date) {
        if (date == null) return null;
        return date.format(dateTimeFormatterDefault);
    }

    public static String getFromLocalDateTimeFe(LocalDateTime date) {
        if (date == null) return null;
        return date.format(dateTimeFormatterFe);
    }

    public static String getFromLocalDateDefault(LocalDate date) {
        if (date == null) return null;
        return date.format(dateFormatterDefault);
    }

    public static String getFromLocalDateFe(LocalDate date) {
        if (date == null) return null;
        return date.format(dateFormatterFe);
    }

    public static String getDateTimeFromTimestampDefault(Timestamp timestamp) {
        if (timestamp == null) return null;
        return timestamp.toLocalDateTime().format(dateTimeFormatTimestampDefault);
    }

    public static String getDateStringDefault(Date date) {
        if (date == null) return null;
        LocalDateTime ldt = getLocalDateTimeFromDate(date);
        return ldt.format(dateFormatterDefault);
    }

    public static LocalDateTime getLocalDateTimeFromDate(Date date) {
        if (date == null) return null;
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static String getDateTimeFromXMLGregorianCalendarDefault(XMLGregorianCalendar timestamp) {
        if (timestamp == null) return null;
        Calendar calendar = timestamp.toGregorianCalendar();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        formatter.setTimeZone(calendar.getTimeZone());
        return formatter.format(calendar.getTime());
    }

    public static LocalDateTime getLocalDateTimeFromXMLGregorianCalendar(XMLGregorianCalendar timestamp) {
        return timestamp.toGregorianCalendar().toZonedDateTime().toLocalDateTime();
    }

    public static XMLGregorianCalendar toXMLGregorianCalendar(Date parse) throws DatatypeConfigurationException {
        if (parse == null) return null;
        Instant i = parse.toInstant();
        String dateTimeString = i.toString();
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(dateTimeString);
    }

    public static XMLGregorianCalendar toXMLGregorianCalendar(String dateString) throws DatatypeConfigurationException {
        Date date = getDateFromStringDefault(dateString);
        return toXMLGregorianCalendar(date);
    }

    public static Date getDateFromStringDefault(String stringDate) {
        if (stringDate == null) return null;
        LocalDateTime ldt = getDateTimeFromStringDefault(stringDate);
        return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDateTime getFromString(String formatter, String date) {
        Assert.assertNotNull(formatter);
        DateTimeFormatter dateTimeFormatterDefault = DateTimeFormatter.ofPattern(formatter);
        return LocalDateTime.parse(date, dateTimeFormatterDefault);
    }

    public static String getFromLocalDateTime(String formatter, LocalDateTime date) {
        Assert.assertNotNull(formatter);
        DateTimeFormatter dateTimeFormatterDefault = DateTimeFormatter.ofPattern(formatter);
        return date.format(dateTimeFormatterDefault);
    }

    public static int getCurrentYear() {
        return LocalDate.now().getYear();
    }

    public static LocalDateTime getFirstDayOfYear() {
        return LocalDate.of(getCurrentYear(), Month.JANUARY, 1).atStartOfDay();
    }

    public static LocalDateTime getLastDayOfYear() {
        return LocalDate.of(getCurrentYear(), Month.DECEMBER, 31).atStartOfDay().plusDays(1).minusSeconds(1);
    }

    public static LocalDateTime getLocalDateTime(Timestamp timestamp, Locale locale) {
        ZoneId zoneId = Calendar.getInstance(locale).getTimeZone().toZoneId();
        return timestamp.toInstant().atZone(zoneId).toLocalDateTime();
    }

    public static LocalDateTime getLocalDateTime(Timestamp timestamp) {
        return timestamp == null ? null : timestamp.toLocalDateTime();
    }

    public static Timestamp getTimestamp(LocalDateTime localDateTime) {
        return localDateTime == null ? null : Timestamp.valueOf(localDateTime);
    }

    public static String getAttachmentFolderName() {
        return nowLocalDateTimeItaly().format(attachmentFolderFormatter);
    }

    public static LocalDateTime nowLocalDateTimeUtc() {
        return LocalDateTime.now(ZoneOffset.UTC);
    }

    public static LocalDate nowLocalDateUtc() {
        return LocalDate.now(ZoneOffset.UTC);
    }

    public static LocalDateTime nowLocalDateTime(String zoneId) {
        return LocalDateTime.now(ZoneId.of(zoneId));
    }

    public static LocalDateTime nowLocalDateTimeItaly() {
        return LocalDateTime.now(ZoneId.of(ZONE_ID_ITALY));
    }

    public static LocalDate nowLocalDate(String zoneId) {
        return LocalDate.now(ZoneId.of(zoneId));
    }

    public static LocalDateTime nowLocalDateTime(Locale locale) {
        return LocalDateTime.now(getZoneIdFromLocale(locale));
    }

    public static LocalDate nowLocalDate(Locale locale) {
        return LocalDate.now(getZoneIdFromLocale(locale));
    }

    public static LocalDateTime nowSystemLocalDateTime(Locale locale) {
        return convertInSystemZoneFromUTC(nowLocalDateTimeUtc(), locale);
    }

    public static LocalDate nowSystemLocalDate(Locale locale) {
        return nowSystemLocalDateTime(locale).toLocalDate();
    }

    public static Timestamp nowTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static ZoneId getZoneIdFromLocale(Locale locale) {
        return Calendar.getInstance(locale).getTimeZone().toZoneId();
    }

    public static LocalDateTime convertInSystemZoneFromUTC(LocalDateTime localDateTime, Locale locale) {
        return localDateTime.atZone(ZoneOffset.UTC).withZoneSameInstant(getZoneIdFromLocale(locale)).toLocalDateTime();
    }

    public static LocalDateTime convertInZoneFromUTC(LocalDateTime localDateTime, String zoneId) {
        return localDateTime.atZone(ZoneOffset.UTC).withZoneSameInstant(ZoneId.of(zoneId)).toLocalDateTime();
    }

    public static LocalDateTime convertInSystemZoneFromZoneId(LocalDateTime localDateTime, Locale locale, String zoneId) {
        return localDateTime.atZone(ZoneId.of(zoneId)).withZoneSameInstant(getZoneIdFromLocale(locale)).toLocalDateTime();
    }

    public static LocalDateTime convertInUTC(LocalDateTime localDateTime, String zoneIdFrom) {
        return localDateTime.atZone(ZoneId.of(zoneIdFrom)).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
    }

    public static LocalDateTime convertInUTC(LocalDateTime localDateTime, Locale locale) {
        return localDateTime.atZone(getZoneIdFromLocale(locale)).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
    }
}
