package depth.mvp.thinkerbell.domain.notice.service;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScheduleParser {

    private static final Pattern DATE_PATTERN = Pattern.compile("(\\d{4})\\.(\\d{2})\\s\\.(\\d{2})\\s~\\s\\.(\\d{2})\\s\\.(\\d{2})");

    public static LocalDate[] parseDate(String dateRange) {
        Matcher matcher = DATE_PATTERN.matcher(dateRange);

        if (matcher.matches()) {
            int year = Integer.parseInt(matcher.group(1));
            int startMonth = Integer.parseInt(matcher.group(2));
            int startDay = Integer.parseInt(matcher.group(3));
            int endMonth = Integer.parseInt(matcher.group(4));
            int endDay = Integer.parseInt(matcher.group(5));

            LocalDate startDate = LocalDate.of(year, startMonth, startDay);
            LocalDate endDate;

            if (startMonth > endMonth){
                endDate = LocalDate.of(year + 1, endMonth, endDay);
            } else {
                endDate = LocalDate.of(year, endMonth, endDay);
            }

            return new LocalDate[]{startDate, endDate};
        } else {
            throw new IllegalArgumentException("Invalid date range: " + dateRange);
        }
    }
}
