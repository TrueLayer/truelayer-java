package com.truelayer.java.mandates;

import com.truelayer.java.http.entities.ApiResponse;
import com.truelayer.java.mandates.entities.CreateMandateRequest;
import com.truelayer.java.mandates.entities.CreateMandateResponse;
import com.truelayer.java.mandates.entities.ListMandatesResponse;
import com.truelayer.java.mandates.entities.mandatedetail.MandateDetail;
import com.truelayer.java.payments.entities.AuthorizationFlowResponse;
import com.truelayer.java.payments.entities.StartAuthorizationFlowRequest;
import com.truelayer.java.payments.entities.SubmitProviderSelectionRequest;
import java.util.concurrent.CompletableFuture;
import retrofit2.http.*;

/**
 * Exposes all the mandates related capabilities of the library.
 *
 * @see <a href="https://docs.truelayer.com/reference/create-mandate"><i>Mandates</i> API reference</a>
 */
public interface IMandatesApi {

    /**
     * Create a new mandate
     * @param request the create mandate request
     * @return the created mandate
     * @see <a href="https://docs.truelayer.com/reference/create-mandate"><i>Create mandate</i> API reference</a>
     */
    @POST("/mandates")
    CompletableFuture<ApiResponse<CreateMandateResponse>> createMandate(@Body CreateMandateRequest request);

    /**
     * Start the authorization flow for a mandate.
     * @param mandateId the id of the mandate
     * @param request the start authorization flow request
     * @return the mandate authorization flow created
     * @see <a href="https://docs.truelayer.com/reference/start-mandate-authorization-flow"><i>Start authorization flow</i> API reference</a>
     */
    @POST("/mandates/{id}/authorization-flow")
    CompletableFuture<ApiResponse<AuthorizationFlowResponse>> startAuthorizationFlow(
            @Path("id") String mandateId, @Body StartAuthorizationFlowRequest request);

    /**
     * Submit the provider details selected by the PSU
     * @param mandateId the id of the mandate
     * @param request the provider selection request
     * @return the next action to take care of
     * @see <a href="https://docs.truelayer.com/reference/submit-mandate-provider-selection"><i>Submit provider selection</i> API reference</a>
     */
    @POST("/mandates/{id}/authorization-flow/actions/provider-selection")
    CompletableFuture<ApiResponse<AuthorizationFlowResponse>> submitProviderSelection(
            @Path("id") String mandateId, @Body SubmitProviderSelectionRequest request);

    /**
     * List all the mandates associated to the client used
     * @param userId optional query parameters to only fetch mandates belonging to a particular user
     * @param cursor cursor used for pagination purposes that represents the first item of the page
     * @param limit maximum number of items included in a returned page
     * @return the list of mandates matching the given criteria
     * @see <a href="https://docs.truelayer.com/reference/list-mandate"><i>List mandates</i> API reference</a>
     */
    @GET("/mandates")
    CompletableFuture<ApiResponse<ListMandatesResponse>> listMandates(
            @Query("user_id") String userId, @Query("cursor") String cursor, @Query("limit") Integer limit);

    /**
     * Get mandate
     * @param mandateId the id of the mandate
     * @return the mandate matching the given id
     * @see <a href="https://docs.truelayer.com/reference/get-mandate"><i>Get mandate</i> API reference</a>
     */
    @GET("/mandates/{id}")
    CompletableFuture<ApiResponse<MandateDetail>> getMandate(@Path("id") String mandateId);

    /**
     * Revoke mandate
     * @param mandateId the id of the mandate
     * @return an empty response in case of success
     * @see <a href="https://docs.truelayer.com/reference/revoke-mandate"><i>Revoke mandate</i> API reference</a>
     */
    @POST("/mandates/{id}/revoke")
    CompletableFuture<ApiResponse<Void>> revokeMandate(@Path("id") String mandateId);
}
