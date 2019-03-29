package com.optisoft.emauser.Webservices;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.optisoft.emauser.Activity.AddVisitorActivity;
import com.optisoft.emauser.Activity.BillActivity;
import com.optisoft.emauser.Activity.BillSummaryActivity;
import com.optisoft.emauser.Activity.ContactUsActivity;
import com.optisoft.emauser.Activity.DistressMessageActivity;
import com.optisoft.emauser.Activity.ForgotPasswordActivity;
import com.optisoft.emauser.Activity.ListVisitorActivity;
import com.optisoft.emauser.Activity.LoginActivity;
import com.optisoft.emauser.Activity.MpesaActivity;
import com.optisoft.emauser.Activity.NotificationActivity;
import com.optisoft.emauser.Activity.PaymentActivity;
import com.optisoft.emauser.Activity.ProfileActivity;
import com.optisoft.emauser.Activity.ReserveVisitorActivity;
import com.optisoft.emauser.Activity.SignUpActivity;
import com.optisoft.emauser.Activity.VehicleHistoryActivity;
import com.optisoft.emauser.Activity.VehicleListActivity;
import com.optisoft.emauser.Activity.VisitorHistoryActivity;
import com.optisoft.emauser.Adapter.ContactsAdapter;
import com.optisoft.emauser.Fragments.AgentDashboardFragment;
import com.optisoft.emauser.Fragments.DashboardFragment;
import com.optisoft.emauser.Fragments.GuardDashboardFragment;
import com.optisoft.emauser.Fragments.HomeMenuFragment;
import com.optisoft.emauser.Model.ResponseModel;
import com.optisoft.emauser.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;
import static com.optisoft.emauser.HelperClasses.ApiConstant.API_BASE_URL;
import static com.optisoft.emauser.HelperClasses.ApiConstant.CURRENT_TAG;
import static com.optisoft.emauser.HelperClasses.ApiConstant.ERROR_TAG;
import static com.optisoft.emauser.HelperClasses.ApiConstant.SUCCESS_TAG;

public class CallApi {

    public ProgressDialog progressDialog;
    Gson gson = new GsonBuilder()
            .excludeFieldsWithModifiers(Modifier.TRANSIENT)
            .disableHtmlEscaping()
            .setLenient()
            .create();

    OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS).build();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();


    ApiService service = retrofit.create(ApiService.class);

    public void dialogShow(Context context, String msg) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(msg);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }

    public void dialogHide() {
        if (progressDialog != null) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            progressDialog = null;
        }
    }

    public void requestVerifyAgentCode(final SignUpActivity context, String agent_codeSt) {
        dialogShow(context, "processing...");
        Call<ResponseModel> call = service.requestVerifyAgentCode(agent_codeSt);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel>call, Response<ResponseModel> response) {
                dialogHide();
                Log.e(SUCCESS_TAG, response.message());
                if (response.body() != null){
                    context.responseVerifyAgentCode(response.body());
                    Log.e(SUCCESS_TAG, response.body().toString());
                }else {
                    context.customToast(response.message());
                }
            }
            @Override
            public void onFailure(Call<ResponseModel>call, Throwable t) {
                dialogHide();
                Log.e(ERROR_TAG, t.toString());
                context.customToast(context.getString(R.string.server_not_responding));
            }
        });
    }

    public void requestSignUpUser(final SignUpActivity context, HashMap<String, String> map) {
        dialogShow(context, "processing...");
        Call<ResponseModel> call = service.requestSignUpUser(map);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel>call, Response<ResponseModel> response) {
                dialogHide();
                Log.e(SUCCESS_TAG, response.message());
                if (response.body() != null){
                    context.responseSignupUser(response.body());
                    Log.e(SUCCESS_TAG, response.body().toString());
                }else {
                    context.customToast(response.message());
                }
            }
            @Override
            public void onFailure(Call<ResponseModel>call, Throwable t) {
                dialogHide();
                Log.e(ERROR_TAG, t.toString());
                context.customToast(context.getString(R.string.server_not_responding));
            }
        });
    }

    public void requestLogin(final LoginActivity context, HashMap<String, String> map) {
        dialogShow(context, "processing...");
        Call<ResponseModel> call = service.requestLogin(map);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel>call, Response<ResponseModel> response) {
                dialogHide();
                if (response.body() != null){
                    context.responseLogin(response.body());
                    Log.e(SUCCESS_TAG, response.body().toString());
                }else {
                    context.customToast(response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseModel>call, Throwable t) {
                Log.e("OTH_RES_Error", t.toString());
                dialogHide();
                context.customToast(context.getString(R.string.server_not_responding));
            }
        });
    }

    public void requestAgentLogin(final LoginActivity context, HashMap<String, String> map) {
        dialogShow(context, "processing...");
        Call<ResponseModel> call = service.requestAgentLogin(map);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel>call, Response<ResponseModel> response) {
                dialogHide();
                if (response.body() != null){
                    context.responseLogin(response.body());
                    Log.e(SUCCESS_TAG, response.body().toString());
                }else {
                    context.customToast(response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseModel>call, Throwable t) {
                Log.e("OTH_RES_Error", t.toString());
                dialogHide();
                context.customToast(context.getString(R.string.server_not_responding));
            }
        });
    }

    public void requestGuardLogin(final LoginActivity context, HashMap<String, String> map) {
        dialogShow(context, "processing...");
        Call<ResponseModel> call = service.requestGuardLogin(map);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel>call, Response<ResponseModel> response) {
                dialogHide();
                if (response.body() != null){
                    context.responseLogin(response.body());
                    Log.e(SUCCESS_TAG, response.body().toString());
                }else {
                    context.customToast(response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseModel>call, Throwable t) {
                Log.e("OTH_RES_Error", t.toString());
                dialogHide();
                context.customToast(context.getString(R.string.server_not_responding));
            }
        });
    }

    public void requestUpdateProfile(final ProfileActivity context, HashMap<String, RequestBody> map) {
        dialogShow(context, "processing...");
        Call<ResponseModel> call = service.requestUpdateProfile( map);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel>call, Response<ResponseModel> response) {
                dialogHide();
                if (response.body() != null){
                    Log.e(SUCCESS_TAG, response.body().getMessage());
                    context.responseUpdateProfile(response.body());
                }else {
                    context.customToast(response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseModel>call, Throwable t) {
                Log.e("OTH_RES_Error", t.toString());
                dialogHide();
                context.customToast(context.getString(R.string.server_not_responding));
            }
        });
    }

    public void requestCurrentBillList(final BillActivity context, String user_id, String tag ) {
        dialogShow(context, "processing...");
        Call<ResponseModel> call = null;
        if (tag.equalsIgnoreCase(CURRENT_TAG)){
            call = service.requestCurrentBillList(user_id);
        }else {
            call = service.requestPreviousBillList(user_id);
        }

        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel>call, Response<ResponseModel> response) {
                dialogHide();
                if (response.body() != null){
                    context.responseCurrentBillList(response.body());
                    Log.e(SUCCESS_TAG, response.body().toString());
                }else {
                    context.customToast(response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseModel>call, Throwable t) {
                Log.e("OTH_RES_Error", t.toString());
                dialogHide();
                context.customToast(context.getString(R.string.server_not_responding));
            }
        });
    }

    public void paymentHistory(final PaymentActivity context, String user_id ) {
        dialogShow(context, "processing...");
        Call<ResponseModel> call = service.paymentHistory(user_id);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel>call, Response<ResponseModel> response) {
                dialogHide();
                if (response.body() != null){
                    context.paymentHistory(response.body());
                    Log.e(SUCCESS_TAG, response.body().toString());
                }else {
                    context.customToast(response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseModel>call, Throwable t) {
                Log.e("OTH_RES_Error", t.toString());
                dialogHide();
                context.customToast(context.getString(R.string.server_not_responding));
            }
        });
    }

    public void requestGraphData(final DashboardFragment context, String user_id ) {
        dialogShow(context.getActivity(), "processing...");
        Call<ResponseModel> call = service.requestGraphData(user_id);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel>call, Response<ResponseModel> response) {
                dialogHide();
                if (response.body() != null){
                    context.responseGraphData(response.body());
                    Log.e(SUCCESS_TAG, response.body().toString());
                }else {
                    context.customToast(response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseModel>call, Throwable t) {
                Log.e("OTH_RES_Error", t.toString());
                dialogHide();
                context.customToast(context.getString(R.string.server_not_responding));
            }
        });
    }

    public void notificationHistory(final NotificationActivity context, String user_id ) {
        dialogShow(context, "processing...");
        Call<ResponseModel> call = service.notificationHistory(user_id);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel>call, Response<ResponseModel> response) {
                dialogHide();
                if (response.body() != null){
                    context.notificationHistory(response.body());
                    Log.e(SUCCESS_TAG, response.body().toString());
                }else {
                    context.customToast(response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseModel>call, Throwable t) {
                Log.e("OTH_RES_Error", t.toString());
                dialogHide();
                context.customToast(context.getString(R.string.server_not_responding));
            }
        });
    }

    public void contactRequestList(final NotificationActivity context, String user_id ) {
        dialogShow(context, "processing...");
        Call<ResponseModel> call = service.contactRequestList(user_id);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel>call, Response<ResponseModel> response) {
                dialogHide();
                if (response.body() != null){
                    context.notificationHistory(response.body());
                    Log.e(SUCCESS_TAG, response.body().toString());
                }else {
                    context.customToast(response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseModel>call, Throwable t) {
                Log.e("OTH_RES_Error", t.toString());
                dialogHide();
                context.customToast(context.getString(R.string.server_not_responding));
            }
        });
    }

    public void mPesaTransaction(final MpesaActivity context, HashMap<String, String> map) {
        dialogShow(context, "processing...");
        Call<ResponseModel> call = service.mPesaTransaction(map);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel>call, Response<ResponseModel> response) {
                dialogHide();
                if (response.body() != null){
                    context.mPesaTransaction(response.body());
                    Log.e(SUCCESS_TAG, response.body().toString());
                }else {
                    context.customToast(response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseModel>call, Throwable t) {
                Log.e("OTH_RES_Error", t.toString());
                dialogHide();
                context.customToast(context.getString(R.string.server_not_responding));
            }
        });
    }

    public void requestresetPassword(final ProfileActivity context, HashMap<String, String> map) {
        dialogShow(context, "processing...");
        Call<ResponseModel> call = service.requestresetPassword(map);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel>call, Response<ResponseModel> response) {
                dialogHide();
                if (response.body() != null){
                    context.requestresetPassword(response.body());
                    Log.e(SUCCESS_TAG, response.body().toString());
                }else {
                    context.customToast(response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseModel>call, Throwable t) {
                Log.e("OTH_RES_Error", t.toString());
                dialogHide();
                context.customToast(context.getString(R.string.server_not_responding));
            }
        });
    }

    public void requestForgotPassword(final ForgotPasswordActivity context, HashMap<String, String> map) {
        dialogShow(context, "processing...");
        Call<ResponseModel> call = service.requestForgotPassword(map);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel>call, Response<ResponseModel> response) {
                dialogHide();
                if (response.body() != null){
                    context.requestForgotPassword(response.body());
                    Log.e(SUCCESS_TAG, response.body().toString());
                }else {
                    context.customToast(response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseModel>call, Throwable t) {
                Log.e("OTH_RES_Error", t.toString());
                dialogHide();
                context.customToast(context.getString(R.string.server_not_responding));
            }
        });
    }

    public void requestContactUs(final ContactUsActivity context, HashMap<String, String> map, String flagActivity) {
        dialogShow(context, "processing...");
        Call<ResponseModel> call = null;
        if (flagActivity.equals("contact")){
            call = service.requestContactUs(map);
        }else {
            call = service.requestDistressMessage(map);
        }

        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel>call, Response<ResponseModel> response) {
                dialogHide();
                if (response.body() != null){
                    context.requestContactUs(response.body());
                    Log.e(SUCCESS_TAG, response.body().toString());
                }else {
                    context.customToast(response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseModel>call, Throwable t) {
                Log.e("OTH_RES_Error", t.toString());
                dialogHide();
                context.customToast(context.getString(R.string.server_not_responding));
            }
        });
    }

    public void requestReplyVisiter(final com.optisoft.emauser.Firebase.NotificationActivity context, HashMap<String, String> map) {
        dialogShow(context, "processing...");
        Call<ResponseModel> call = service.requestReplyVisiter(map);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel>call, Response<ResponseModel> response) {
                dialogHide();
                if (response.body() != null){
                    context.requestReplyVisiter(response.body());
                    Log.e(SUCCESS_TAG, response.body().toString());
                }else {
                    context.customToast(response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseModel>call, Throwable t) {
                Log.e("OTH_RES_Error", t.toString());
                dialogHide();
                context.customToast(context.getString(R.string.server_not_responding));
            }
        });
    }

    public void requestDownloadBill(final BillSummaryActivity context, String billId, String userId) {
        dialogShow(context, "processing...");
        Call<ResponseBody> call = service.requestDownloadBill(billId, userId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    dialogHide();
                    Log.d(SUCCESS_TAG, "server contacted and has file");

                    boolean writtenToDisk = writeResponseBodyToDisk(context, response.body(), "bill");
                    Log.d(TAG, "file download was a success? " + writtenToDisk);
                }else {
                    dialogHide();
                    context.customToast("server contact failed");
                    Log.d(ERROR_TAG, "server contact failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("OTH_RES_Error", t.toString());
                dialogHide();
                context.customToast(context.getString(R.string.server_not_responding));
            }
        });
    }

    public void requestDownloadInvoice(final BillSummaryActivity context, String txnId, String billId, String userId) {
        dialogShow(context, "processing...");
        Call<ResponseBody> call = service.requestDownloadInvoice(txnId, billId, userId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    dialogHide();
                    Log.d(SUCCESS_TAG, "server contacted and has file");

                    boolean writtenToDisk = writeResponseBodyToDisk(context, response.body(), "invoice");
                    Log.d(TAG, "file download was a success? " + writtenToDisk);
                }else {
                    dialogHide();
                    context.customToast("server contact failed");
                    Log.d(ERROR_TAG, "server contact failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("OTH_RES_Error", t.toString());
                dialogHide();
                context.customToast(context.getString(R.string.server_not_responding));
            }
        });
    }

    private boolean writeResponseBodyToDisk(BillSummaryActivity context, ResponseBody body, String temp) {
        try {
            // todo change the file location/name according to your needs
            String tempName = String.valueOf(System.currentTimeMillis());
            File folder = new File(Environment.getExternalStorageDirectory() + File.separator + "EmaDoc");
            boolean success = true;
            if (!folder.exists()) {
                folder.mkdirs();
            }
            File futureStudioIconFile = new File(Environment.getExternalStorageDirectory() + File.separator + "EmaDoc" + File.separator + "ema-"+temp+"-"+tempName+".pdf");

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                Log.d("FILE_NAME", futureStudioIconFile.getPath());
                context.customToast("File saved at path : "+futureStudioIconFile.getPath());

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    public void requestAgentData(final AgentDashboardFragment context, String user_id ) {
        dialogShow(context.getActivity(), "processing...");
        Call<ResponseModel> call = service.requestAgentData(user_id);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel>call, Response<ResponseModel> response) {
                dialogHide();
                if (response.body() != null){
                    context.responseAgentData(response.body());
                    Log.e(SUCCESS_TAG, response.body().toString());
                }else {
                    context.customToast(response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseModel>call, Throwable t) {
                Log.e("OTH_RES_Error", t.toString());
                dialogHide();
                context.customToast(context.getString(R.string.server_not_responding));
            }
        });
    }

    public void requestDistressMessages(final HomeMenuFragment context, String user_id ) {
        dialogShow(context.getActivity(), "processing...");
        Call<ResponseModel> call = service.requestDistressMessages(user_id);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel>call, Response<ResponseModel> response) {
                dialogHide();
                if (response.body() != null){
                    context.responseDistressMessages(response.body());
                    Log.e(SUCCESS_TAG, response.body().toString());
                }else {
                    context.customToast(response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseModel>call, Throwable t) {
                Log.e("OTH_RES_Error", t.toString());
                dialogHide();
                context.customToast(context.getString(R.string.server_not_responding));
            }
        });
    }

    public void requestSendDistressToAll(final DistressMessageActivity context, HashMap<String, String> map, final int pos) {
        dialogShow(context, "processing...");
        Call<ResponseModel> call = service.requestSendDistressToAll(map);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel>call, Response<ResponseModel> response) {
                dialogHide();
                if (response.body() != null){
                    context.responseSendDistressToAll(response.body(), pos);
                    Log.e(SUCCESS_TAG, response.body().toString());
                }else {
                    context.customToast(response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseModel>call, Throwable t) {
                Log.e("OTH_RES_Error", t.toString());
                dialogHide();
                context.customToast(context.getString(R.string.server_not_responding));
            }
        });
    }

    public void requestGuardUsers(final AddVisitorActivity context, String agentId ) {
        dialogShow(context, "processing...");
        Call<ResponseModel> call = service.requestGuardUsers(agentId);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel>call, Response<ResponseModel> response) {
                dialogHide();
                if (response.body() != null){
                    context.responseGuardUsers(response.body());
                    Log.e(SUCCESS_TAG, response.body().toString());
                }else {
                    context.customToast(response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseModel>call, Throwable t) {
                Log.e("OTH_RES_Error", t.toString());
                dialogHide();
                context.customToast(context.getString(R.string.server_not_responding));
            }
        });
    }

    public void requestEntryVisitor(final AddVisitorActivity context, HashMap<String, RequestBody> map) {
        dialogShow(context, "processing...");
        Call<ResponseModel> call = service.requestEntryVisitor( map);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel>call, Response<ResponseModel> response) {
                dialogHide();
                if (response.body() != null){
                    Log.e(SUCCESS_TAG, response.body().getMessage());
                    context.responseEntryVisitor(response.body());
                }else {
                    context.customToast(response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseModel>call, Throwable t) {
                Log.e("OTH_RES_Error", t.toString());
                dialogHide();
                context.customToast(context.getString(R.string.server_not_responding));
            }
        });
    }

    public void requestUpdateVisitor(final AddVisitorActivity context, HashMap<String, RequestBody> map) {
        dialogShow(context, "processing...");
        Call<ResponseModel> call = service.requestUpdateVisitor( map);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel>call, Response<ResponseModel> response) {
                dialogHide();
                if (response.body() != null){
                    Log.e(SUCCESS_TAG, response.body().getMessage());
                    context.responseUpdateVisitor(response.body());
                }else {
                    context.customToast(response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseModel>call, Throwable t) {
                Log.e("OTH_RES_Error", t.toString());
                dialogHide();
                context.customToast(context.getString(R.string.server_not_responding));
            }
        });
    }

    public void requestLoadVisitor(final ListVisitorActivity context, String guard_id, String date) {
        dialogShow(context, "processing...");
        Call<ResponseModel> call = service.requestLoadVisitor( guard_id, date);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel>call, Response<ResponseModel> response) {
                dialogHide();
                if (response.body() != null){
                    Log.e(SUCCESS_TAG, response.body().getMessage());
                    context.responseLoadVisitor(response.body());
                }else {
                    context.customToast(response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseModel>call, Throwable t) {
                Log.e("OTH_RES_Error", t.toString());
                dialogHide();
                context.customToast(context.getString(R.string.server_not_responding));
            }
        });
    }
    public void requestLoadGuardData(final GuardDashboardFragment context, String guard_id, String date) {
        dialogShow(context.getActivity(), "processing...");
        Call<ResponseModel> call = service.requestLoadGuardData( guard_id, date);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel>call, Response<ResponseModel> response) {
                dialogHide();
                if (response.body() != null){
                    Log.e(SUCCESS_TAG, response.body().getMessage());
                    context.responseLoadVisitor(response.body());
                }else {
                    context.customToast(response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseModel>call, Throwable t) {
                Log.e("OTH_RES_Error", t.toString());
                dialogHide();
                context.customToast(context.getString(R.string.server_not_responding));
            }
        });
    }

    public void getCarListForUser(final VehicleListActivity context, String user_id ) {
        dialogShow(context, "processing...");
        Call<ResponseModel> call = service.getCarListForUser(user_id);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel>call, Response<ResponseModel> response) {
                dialogHide();
                if (response.body() != null){
                    context.showVehicleList(response.body());
                    Log.e(SUCCESS_TAG, response.body().toString());
                }else {
                    context.customToast(response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseModel>call, Throwable t) {
                Log.e("OTH_RES_Error", t.toString());
                dialogHide();
                context.customToast(context.getString(R.string.server_not_responding));
            }
        });
    }

    public void getCarHistory(final VehicleHistoryActivity context, HashMap<String, String> map) {
        dialogShow(context, "processing...");
        Call<ResponseModel> call = service.getCarHistory(map);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel>call, Response<ResponseModel> response) {
                dialogHide();
                if (response.body() != null){
                    context.showHistoryList(response.body());
                    Log.e(SUCCESS_TAG, response.body().toString());
                }else {
                    //context.customToast(response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseModel>call, Throwable t) {
                Log.e("OTH_RES_Error", t.toString());
                dialogHide();
                //context.customToast(context.getString(R.string.server_not_responding));
            }
        });
    }

    public void requestReserveVisitor(final ReserveVisitorActivity context, HashMap<String, RequestBody> map) {
        dialogShow(context, "processing...");
        Call<ResponseModel> call = service.requestReserveVisitor( map);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel>call, Response<ResponseModel> response) {
                dialogHide();
                if (response.body() != null){
                    Log.e(SUCCESS_TAG, response.body().getMessage());
                    context.responseReserveVisitor(response.body());
                }else {
                    context.customToast(response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseModel>call, Throwable t) {
                Log.e("OTH_RES_Error", t.toString());
                dialogHide();
                context.customToast(context.getString(R.string.server_not_responding));
            }
        });
    }

    public void requestUpdateReserveVisitor(final ReserveVisitorActivity context, HashMap<String, RequestBody> map) {
        dialogShow(context, "processing...");
        Call<ResponseModel> call = service.requestUpdateReserveVisitor( map);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel>call, Response<ResponseModel> response) {
                dialogHide();
                if (response.body() != null){
                    Log.e(SUCCESS_TAG, response.body().getMessage());
                    context.responseUpdateReserveVisitor(response.body());
                }else {
                    context.customToast(response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseModel>call, Throwable t) {
                Log.e("OTH_RES_Error", t.toString());
                dialogHide();
                context.customToast(context.getString(R.string.server_not_responding));
            }
        });
    }

    public void requestCancelReservation(final VisitorHistoryActivity context, String visitorId) {
        dialogShow(context, "processing...");
        Call<ResponseModel> call = service.requestCancelReservation(visitorId);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel>call, Response<ResponseModel> response) {
                dialogHide();
                if (response.body() != null){
                    Log.e(SUCCESS_TAG, response.body().getMessage());
                    context.responseCancelReservation(response.body());
                }else {
                    context.customToast(response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseModel>call, Throwable t) {
                Log.e("OTH_RES_Error", t.toString());
                dialogHide();
                context.customToast(context.getString(R.string.server_not_responding));
            }
        });
    }

    public void isReservedVisitor(final ReserveVisitorActivity context, String vId) {
        dialogShow(context, "processing...");
        Call<ResponseModel> call = service.isReservedVisitor(vId);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel>call, Response<ResponseModel> response) {
                dialogHide();
                if (response.body() != null){
                    Log.e(SUCCESS_TAG, response.body().getMessage());
                    context.responseIsReservedVisitor(response.body());
                }else {
                    context.customToast(response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseModel>call, Throwable t) {
                Log.e("OTH_RES_Error", t.toString());
                dialogHide();
                //context.customToast(context.getString(R.string.server_not_responding));
            }
        });
    }
    public void requestLoadReservedVisitor(final VisitorHistoryActivity context, String userId, String date) {
        dialogShow(context, "processing...");
        Call<ResponseModel> call = service.requestLoadReservedVisitor( userId, date);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel>call, Response<ResponseModel> response) {
                dialogHide();
                if (response.body() != null){
                    Log.e(SUCCESS_TAG, response.body().getMessage());
                    context.responseLoadReservedVisitor(response.body());
                }else {
                    context.customToast(response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseModel>call, Throwable t) {
                Log.e("OTH_RES_Error", t.toString());
                dialogHide();
                //context.customToast(context.getString(R.string.server_not_responding));
            }
        });
    }
}
