package info.doula.utils;

import info.doula.constants.MonthNumbers;

import java.util.Date;

/**
 * Created by Faruque on 12/22/2016.
 */
public class Billing {

    public static Integer[] calculateBillingMonthYear(Integer billingMonth, Integer billingYear) throws Exception {
        Date currentDate = new Date();
        Integer currentMonth = DateUtils.getMonthFromDate(currentDate);
        Integer previousMonth = currentMonth - 1;
        Integer currentDay = DateUtils.getDayFromDate(currentDate);
        Integer currentYear = DateUtils.getYearFromCurrentDate(currentDate);
        Integer previousYear = currentYear - 1;
        Integer lastDayOfCurrentMonth = DateUtils.getLastDayFromCurrentMonth(currentDate);

        if (billingYear == null) throw new Exception("Billing year can not be empty.");
        if (billingMonth == null) throw new Exception("Billing month can not be empty.");

            billingYear = currentYear;
            if (lastDayOfCurrentMonth.equals(currentDay) && billingMonth.equals(currentMonth)) {
                billingMonth = currentMonth;
            } else {
                if (currentMonth.equals(MonthNumbers.JANUARY.getValue())) {
                    billingMonth = MonthNumbers.DECEMBER.getValue();
                    billingYear = previousYear;
                } else {
                    billingMonth = previousMonth;
                }
            }

        Integer[] monthYear = {billingMonth, billingYear};
        return monthYear;
    }

}
