package com.intuit.urlshortner.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ConstantUtils {
    public static final String RANGE_OPERATION_LOCK_KEY = "range-operation-lock";
    public static final String COUNTER_LOCK_KEY = "cache_counter";
    public static final String CURRENT_RANGE_START = "current_range_start";
    private static final String NEXT_RANGE_START = "next_range_start";
    public static final String COUNTER_KEY = "next_counter_id";
    public static final Long COUNTER_LIMIT = 10000L;
    public static final Long INITIAL_RANGE_START = 57000000000L;
    public static final String TINY_URLS_CACHE = "tinyUrls";

    public static final int CACHE_SIZE = 10000; // Adjust as needed

}
