package com.ecommerce.ecommerce.UTI;

public final class Consts {

    // JWT signing secret — provided via the JWT_SECRET environment variable.
    public static final String ALGHORITM_SECRET = System.getenv().getOrDefault("JWT_SECRET", "change-me-in-env");
    public static final Integer TOKEN_EXPIRES=10*60*1000;
    public static final Integer REFRESHTOKEN_EXPIRES=300*60*1000;
    public static final int PAGE_DIMENSION=20;
    public static final String FIELD_SORT="price";

}
