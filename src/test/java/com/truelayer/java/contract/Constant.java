package com.truelayer.java.contract;

import com.truelayer.java.entities.CurrencyCode;
import com.truelayer.java.payments.entities.CountryCode;
import com.truelayer.java.payments.entities.paymentdetail.Status;

public final class Constant {

    public static final String CONSUMER_NAME = "JavaSDK";
    public static final String PROVIDER_NAME = "PaymentsV3";

    // Matchers
    public static final String JWT_TOKEN_REGEX = "[a-zA-Z0-9_-]*.[a-zA-Z0-9_-]*.[a-zA-Z0-9_-]*";
    public static final String PAYMENT_STATUS_REGEX =
            "^(" + Status.AUTHORIZATION_REQUIRED +
                    "|" + Status.AUTHORIZING +
                    "|" + Status.AUTHORIZED +
                    "|" + Status.EXECUTED +
                    "|" + Status.SETTLED +
                    "|" + Status.FAILED + ")$";

    public static final String URI_REGEX = "^(https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

    public static final String RGB_REGEX = "^#([a-fA-F0-9]{6})$";

    public static final String COUNTRY_CODE_REGEX = "^("+ CountryCode.GB+"|"+CountryCode.FR+"|"+CountryCode.IE+")$";
    public static final String CURRENCY_REGEX = "^("+ CurrencyCode.GBP+"|"+CurrencyCode.EUR+")$";
    public static final String ISO_8106_REGEX = "^((?:[1-9][0-9]*)?[0-9]{4})-(1[0-2]|0[1-9])-(3[01]|0[1-9]|[12][0-9])T(2[0-3]|[01][0-9]):([0-5][0-9]):([0-5][0-9])(\\.[0-9]+)?(Z|[+-](?:2[0-3]|[01][0-9]):[0-5][0-9])?$";

    // Samples
    public static final String A_JWT_TOKEN =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
    public static final String A_PAYMENT_ID = "48c890dc-8c03-428c-9a8b-2f383fd0ba38";
    public static final String ANOTHER_PAYMENT_ID = "f9b48c9d-176b-46dd-b2da-fe1a2b77350c";
    public static final String MERCHANT_ACCOUNT_ID = "B7DAEA71-592E-486B-9C00-7132D1FD7AD1";
    public static final String RETURN_URI = "http://localhost:8080/callback";
    public static final String PROVIDER_ID_GB = "mock-payments-gb-redirect";

}
