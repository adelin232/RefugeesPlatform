package com.project;

public interface DateValidator {
    boolean isValid(String dateStr);
    boolean isStartBeforeEnd(String startDate, String endDate);
}
