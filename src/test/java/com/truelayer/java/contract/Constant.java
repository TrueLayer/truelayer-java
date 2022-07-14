package com.truelayer.java.contract;

public final class Constant {

    public static final String CONSUMER_NAME = "JavaSDK";
    public static final String PROVIDER_NAME = "PaymentsV3";

    // Matchers
    public static final String UUID_REGEX = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";
    public static final String JWT_TOKEN_REGEX = "[a-zA-Z0-9_-]*.[a-zA-Z0-9_-]*.[a-zA-Z0-9_-]*";
    public static final String PAYMENT_STATUS_REGEX =
            "^(authorization_required|authorizing|authorized|executed|settled|failed)$";

    // Samples
    public static final String A_JWT_TOKEN =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
    public static final String A_PAYMENT_ID = "48c890dc-8c03-428c-9a8b-2f383fd0ba38";
    public static final String MERCHANT_ACCOUNT_ID = "B7DAEA71-592E-486B-9C00-7132D1FD7AD1";
    public static final String RETURN_URI = "http://localhost:8080/callback";
}
