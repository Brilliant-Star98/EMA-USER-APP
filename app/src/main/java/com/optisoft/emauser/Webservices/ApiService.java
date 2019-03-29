package com.optisoft.emauser.Webservices;


import com.optisoft.emauser.Model.ResponseModel;

import java.util.HashMap;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

import static com.optisoft.emauser.HelperClasses.ApiConstant.API_AGENT_DATA;
import static com.optisoft.emauser.HelperClasses.ApiConstant.API_CANCEL_RESERVATION;
import static com.optisoft.emauser.HelperClasses.ApiConstant.API_CAR_HISTORY;
import static com.optisoft.emauser.HelperClasses.ApiConstant.API_CONTACT_REQ;
import static com.optisoft.emauser.HelperClasses.ApiConstant.API_CONTACT_US;
import static com.optisoft.emauser.HelperClasses.ApiConstant.API_CURRENT_BILL;
import static com.optisoft.emauser.HelperClasses.ApiConstant.API_DISTRESS_MESSAGE;
import static com.optisoft.emauser.HelperClasses.ApiConstant.API_DISTRESS_MESSAGES;
import static com.optisoft.emauser.HelperClasses.ApiConstant.API_DOENLOAD_BILL;
import static com.optisoft.emauser.HelperClasses.ApiConstant.API_DOENLOAD_INVOICE;
import static com.optisoft.emauser.HelperClasses.ApiConstant.API_FORGOT_PASSWORD;
import static com.optisoft.emauser.HelperClasses.ApiConstant.API_GET_CARS;
import static com.optisoft.emauser.HelperClasses.ApiConstant.API_GRAPH_DATA;
import static com.optisoft.emauser.HelperClasses.ApiConstant.API_LOAD_GUARD_DATA;
import static com.optisoft.emauser.HelperClasses.ApiConstant.API_LOAD_GUARD_USERS;
import static com.optisoft.emauser.HelperClasses.ApiConstant.API_LOAD_RESERVED_VISITORS;
import static com.optisoft.emauser.HelperClasses.ApiConstant.API_LOAD_VISITORS;
import static com.optisoft.emauser.HelperClasses.ApiConstant.API_LOGIN;
import static com.optisoft.emauser.HelperClasses.ApiConstant.API_LOGIN_AGENT;
import static com.optisoft.emauser.HelperClasses.ApiConstant.API_LOGIN_GUARD;
import static com.optisoft.emauser.HelperClasses.ApiConstant.API_NOTIFICATION;
import static com.optisoft.emauser.HelperClasses.ApiConstant.API_PAYMENT_HISTORY;
import static com.optisoft.emauser.HelperClasses.ApiConstant.API_PREVIOUS_BILL;
import static com.optisoft.emauser.HelperClasses.ApiConstant.API_REPLY_VISITER;
import static com.optisoft.emauser.HelperClasses.ApiConstant.API_RESET_PASSWORD;
import static com.optisoft.emauser.HelperClasses.ApiConstant.API_SEND_DIS_ALL;
import static com.optisoft.emauser.HelperClasses.ApiConstant.API_SIGN_UP_USER;
import static com.optisoft.emauser.HelperClasses.ApiConstant.API_TRANSACTION;
import static com.optisoft.emauser.HelperClasses.ApiConstant.API_UPDATE_PROFILE;
import static com.optisoft.emauser.HelperClasses.ApiConstant.API_VERIFY_AGENT;
import static com.optisoft.emauser.HelperClasses.ApiConstant.API_VISITOR_ENTRY;
import static com.optisoft.emauser.HelperClasses.ApiConstant.API_VISITOR_RESERVE;
import static com.optisoft.emauser.HelperClasses.ApiConstant.API_VISITOR_RESERVE_UPDATE;
import static com.optisoft.emauser.HelperClasses.ApiConstant.API_VISITOR_UPDATE;
import static com.optisoft.emauser.HelperClasses.ApiConstant.API__RESERVED_VISITOR_LIVE;


/**
 * Created by OptiSoft_A on 8/30/2017.
 */

 public interface ApiService {

    @GET(API_VERIFY_AGENT)
    Call<ResponseModel> requestVerifyAgentCode(@Path("agent_code") String agent_codeSt);

    @FormUrlEncoded
    @POST(API_SIGN_UP_USER)
    Call<ResponseModel> requestSignUpUser(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(API_LOGIN)
    Call<ResponseModel> requestLogin(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(API_LOGIN_AGENT)
    Call<ResponseModel> requestAgentLogin(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(API_LOGIN_GUARD)
    Call<ResponseModel> requestGuardLogin(@FieldMap HashMap<String, String> params);

    @Multipart
    @POST(API_UPDATE_PROFILE)
    Call<ResponseModel> requestUpdateProfile(@PartMap HashMap<String, RequestBody> param);

    @Multipart
    @POST(API_VISITOR_ENTRY)
    Call<ResponseModel> requestEntryVisitor(@PartMap HashMap<String, RequestBody> param);

    @Multipart
    @POST(API_VISITOR_UPDATE)
    Call<ResponseModel> requestUpdateVisitor(@PartMap HashMap<String, RequestBody> param);

    @GET(API_CURRENT_BILL)
    Call<ResponseModel> requestCurrentBillList(@Path("user_id") String agent_codeSt);

    @GET(API_LOAD_GUARD_DATA)
    Call<ResponseModel> requestLoadGuardData(@Path("guard_id") String guard_id, @Path("date") String date);

    @GET(API_LOAD_VISITORS)
    Call<ResponseModel> requestLoadVisitor(@Path("guard_id") String guard_id, @Path("date") String date);

    @GET(API_PREVIOUS_BILL)
    Call<ResponseModel> requestPreviousBillList(@Path("user_id") String agent_codeSt);

    @GET(API_PAYMENT_HISTORY)
    Call<ResponseModel> paymentHistory(@Path("user_id") String agent_codeSt);

    @GET(API_GRAPH_DATA)
    Call<ResponseModel> requestGraphData(@Path("user_id") String agent_codeSt);

    @GET(API_DISTRESS_MESSAGES)
    Call<ResponseModel> requestDistressMessages(@Path("user_id") String agent_codeSt);

    @GET(API_LOAD_GUARD_USERS)
    Call<ResponseModel> requestGuardUsers(@Path("agent_id") String agentId);

    @GET(API_AGENT_DATA)
    Call<ResponseModel> requestAgentData(@Path("user_id") String agent_codeSt);

    @GET(API_NOTIFICATION)
    Call<ResponseModel> notificationHistory(@Path("user_id") String agent_codeSt);

    @GET(API_CONTACT_REQ)
    Call<ResponseModel> contactRequestList(@Path("user_id") String agent_codeSt);

   @FormUrlEncoded
   @POST(API_TRANSACTION)
   Call<ResponseModel> mPesaTransaction(@FieldMap HashMap<String, String> params);

   @FormUrlEncoded
   @POST(API_RESET_PASSWORD)
   Call<ResponseModel> requestresetPassword(@FieldMap HashMap<String, String> params);

   @FormUrlEncoded
   @POST(API_FORGOT_PASSWORD)
   Call<ResponseModel> requestForgotPassword(@FieldMap HashMap<String, String> params);

   @FormUrlEncoded
   @POST(API_CONTACT_US)
   Call<ResponseModel> requestContactUs(@FieldMap HashMap<String, String> params);

   @FormUrlEncoded
   @POST(API_REPLY_VISITER)
   Call<ResponseModel> requestReplyVisiter(@FieldMap HashMap<String, String> params);

   @FormUrlEncoded
   @POST(API_SEND_DIS_ALL)
   Call<ResponseModel> requestSendDistressToAll(@FieldMap HashMap<String, String> params);


   @FormUrlEncoded
   @POST(API_DISTRESS_MESSAGE)
   Call<ResponseModel> requestDistressMessage(@FieldMap HashMap<String, String> params);

    @GET(API_DOENLOAD_BILL)
    Call<ResponseBody> requestDownloadBill(@Path("bill_id") String bill_id, @Path("user_id") String user_id);

    @GET(API_DOENLOAD_INVOICE)
    Call<ResponseBody> requestDownloadInvoice(@Path("txn_id") String txnid, @Path("bill_id") String bill_id, @Path("user_id") String user_id);

    @GET(API_GET_CARS)
    Call<ResponseModel> getCarListForUser(@Path("user_id") String agent_codeSt);

    @FormUrlEncoded
    @POST(API_CAR_HISTORY)
    Call<ResponseModel> getCarHistory(@FieldMap HashMap<String, String> param);

    @Multipart
    @POST(API_VISITOR_RESERVE)
    Call<ResponseModel> requestReserveVisitor(@PartMap HashMap<String, RequestBody> param);

    @Multipart
    @POST(API_VISITOR_RESERVE_UPDATE)
    Call<ResponseModel> requestUpdateReserveVisitor(@PartMap HashMap<String, RequestBody> param);

    @GET(API_CANCEL_RESERVATION)
    Call<ResponseModel> requestCancelReservation(@Path("visitor_id") String visitor_id);

    @GET(API_LOAD_RESERVED_VISITORS)
    Call<ResponseModel> requestLoadReservedVisitor(@Path("user_id") String guard_id, @Path("date") String date);

    @GET(API__RESERVED_VISITOR_LIVE)
    Call<ResponseModel> isReservedVisitor(@Path("visitor_id") String vId);


/*    @FormUrlEncoded
    @POST(API_SUBMIT_TRANSACTION)
    Call<ResponseModel> submitTransactionbData(@FieldMap Map<String, String> param);

    @FormUrlEncoded
    @POST(API_UPDATE_THEME)
    Call<ResponseModel> updateThemeColor(@FieldMap Map<String, String> param);

    @FormUrlEncoded
    @POST(API_HELP)
    Call<ResponseModel> requestHelp(@FieldMap Map<String, String> param);

    @FormUrlEncoded
    @POST(API_RESET_PASSWORD)
    Call<ResponseModel> requestResetPassword(@FieldMap Map<String, String> param);

    @GET(API_PLAN_LIST)
    Call<ResponseModel> requestLoadPlanList();

    @GET(API_FORGOT_PASSWORD)
    Call<ResponseModel> requestForgotPassword(@Query("email") String email);

    @GET(USER_DATA)
    Call<ResponseModel> Get_User_Data(@Path("mobile") String mobile, @Path("password") String password);

    @POST(USER_DATA)
    Call<ResponseModel> VerifyUserNumber(@Path("mobile_num") String mobile_num);

    @POST(USER_DATA)
    Call<Object> SendOtp(@QueryMap HashMap<String, String> params);

    @POST(USER_DATA)
    Call<Object> ReSendOtp(@QueryMap HashMap<String, String> params);

    @POST(USER_DATA)
    Call<Object> VerifyOtp(@QueryMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(USER_DATA)
    Call<ResponseModel> SignInUser(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST(USER_DATA)
    Call<ResponseModel> sendReport(@FieldMap HashMap<String, String> params);*/

}