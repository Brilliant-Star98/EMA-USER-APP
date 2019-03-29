package com.optisoft.emauser.HelperClasses;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by OptiSoft_A on 2/3/2018.
 */

public class ApiConstant {

    public static final String DRAWER_HOME     = "drawer_home";
    public static final String FRAG_HOME       = "frag_home";
    public static final String FRAG_CURRENT    = "frag_current";

    public static final String CURRENT_TAG     = "current";
    public static final String PREVIOUS_TAG    = "previous";
    public static final String PAYMENT_TAG     = "payment";
    public static final String NOTIFICATION_TAG  = "notification";
    public static final String INTENT_TAG      =  "intent_data";
    public static final String ALARM_TAG       =  "alarm_tag";
    public static final String ALARM_APPEAR    =  "alarm_appear";


    public static final String ERROR_TAG        = "RESPONSE_ERROR";
    public static final String SUCCESS_TAG      = "RESPONSE_SUCCESS";
    public static final String EXCEPTION_TAG    = "EXCEPTION";
    public static final String EMAIL_PATTERN    = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    /**************************************** API CONSTANTS START ***************************************************/

//    public static final String API_BASE_URL               = "http://etha.in/ema/admin/";
    public static final String API_BASE_URL               = "http://10.0.2.2/ema/admin/";
//    public static final String API_BASE_URL               = "http://192.168.1.100/ema/admin/";
    public static final String API_VERIFY_AGENT           = "Api/verifyAgentCode/{agent_code}";
    public static final String API_SIGN_UP_USER           = "Api/registerUser";
    public static final String API_SIGN_UP_HELPER         = "Api/registerHelper";
    public static final String API_LOGIN                  = "Api/loginUser";
    public static final String API_LOGIN_AGENT            = "Api/loginAgent";
    public static final String API_LOGIN_GUARD            = "Api/loginGuard";
    public static final String API_UPDATE_PROFILE         = "Api/updateProfile";
    public static final String API_CURRENT_BILL           = "Api/currentBills/{user_id}";
    public static final String API_PREVIOUS_BILL          = "Api/previousBills/{user_id}";
    public static final String API_PAYMENT_HISTORY        = "Api/paymentHistory/{user_id}";
    public static final String API_GRAPH_DATA             = "Api/graphData/{user_id}";
    public static final String API_AGENT_DATA             = "Api/agentData/{user_id}";
    public static final String API_NOTIFICATION           = "Api/notificationList/{user_id}";
    public static final String API_CONTACT_REQ            = "Api/getContactList/{user_id}";
    public static final String API_TRANSACTION            = "Api/addTransaction";
    public static final String API_RESET_PASSWORD         = "Api/passwordReset";
    public static final String API_FORGOT_PASSWORD        = "Api/forgotPassword";
    public static final String API_CONTACT_US             = "Api/contactUsData";
    public static final String API_REPLY_VISITER          = "Api/replyVisiter";
    public static final String API_DISTRESS_MESSAGE       = "Api/sendDistressMessage";
    public static final String API_DISTRESS_MESSAGES      = "Api/getDistressMessage/{user_id}";
    public static final String API_SEND_DIS_ALL           = "Api/sendDistressMessageAll";
    public static final String API_DOENLOAD_BILL          = "Api/downloadBill/{bill_id}/{user_id}";
    public static final String API_DOENLOAD_INVOICE       = "Api/downloadInvoice/{txn_id}/{bill_id}/{user_id}";


    public static final String API_LOAD_GUARD_USERS       = "Api/getAllUsersByAgentId/{agent_id}";
    public static final String API_LOAD_GUARD_DATA        = "Api/getGuardData/{guard_id}/{date}";
    public static final String API_LOAD_VISITORS          = "Api/getAllVisitors/{guard_id}/{date}";
    public static final String API_VISITOR_ENTRY          = "Api/setVisitorEntry";
    public static final String API_VISITOR_UPDATE         = "Api/setVisitorUpdate";
    public static final String API_GET_CARS               = "Api/getCarListForUser/{user_id}";
    public static final String API_CAR_HISTORY            = "Api/getCarHistory";
    public static final String API_VISITOR_RESERVE        = "Api/reserveVisitor";
    public static final String API_VISITOR_RESERVE_UPDATE = "Api/updateReserveVisitor";
    public static final String API_CANCEL_RESERVATION     = "Api/cancelReservation/{visitor_id}";
    public static final String API_LOAD_RESERVED_VISITORS = "Api/getAllReservedVisitors/{user_id}/{date}";
    public static final String API__RESERVED_VISITOR_LIVE = "Api/isReservedVisitorLive/{visitor_id}";


    public static final String IMAGE_URL               = "http://etha.in/ema/admin/assets/uploads/";

    public static final int PLACE_PICKER_REQUEST = 12;

    public static String convert_time(int n){
        return n < 10 ? "0" + n : "" + n;
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }





    /***************************************** API CONSTANTS END ***************************************************/
}
