package com.truelayer.java.signupplus;

import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.signupplus.entities.UserData;
import java.util.concurrent.CompletableFuture;

public interface ISignupPlusHandler {

    CompletableFuture<ApiResponse<UserData>> getUserDataByPayment(String paymentId);
}
