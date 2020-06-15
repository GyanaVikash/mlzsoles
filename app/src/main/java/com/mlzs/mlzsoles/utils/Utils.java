package com.mlzs.mlzsoles.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.mlzs.mlzsoles.R;

import java.lang.reflect.Field;

public class Utils {

    public static final String SHARED_PREF = "menorahoes_firebase";
    public static String MyPREFERENCES = "my_preferences";
    public static final String PAYPAL_CLIENT_ID = "AZCDAWtn4n81rGk2UU57j0GCVYbsHhrPJ3J4oNfivlv1VwhLRGDjoHFnknfe9V5YeOKyBcpu7ITF-Hu-"; // Pay pal client ID



    public static String SERVER_URL = "https://mlzs.gvhssb.in.net" ; // SERVER BASE URL

    public static String URL = SERVER_URL+"/api/v1/";

    public static String IMAGE_BASE_URL = SERVER_URL+ "/public/uploads/";

    public static String FILE_BASE_URL = SERVER_URL+"/public/uploads/lms/content/";

    public static String USER_PIC_BASE_URL = SERVER_URL+"/public/uploads/users/";

    public static String EXAM_SERIES_BASE_URL = IMAGE_BASE_URL+"exams/series/";

    public static String LMS_SERIES_BASE_URL = IMAGE_BASE_URL+"lms/series/";

    public static String EXAM_TYPE_BASE_URL = IMAGE_BASE_URL+"exams/";

    public static String LOGIN = URL+"login";
    public static String REGISTER = URL + "newregister";
    public static String CHECK_EMAIL = URL + "users/reset-password";
    public static String POPULAR_RECORDS = URL+"dashboard-top-records";
    public static String EXAMS_CATEGORIES_LIST = URL + "exam-categories?user_id=";
    public static String LMS_CATEGORIES_LIST = URL + "lms-categories?user_id=";
    public static String GET_CATEGORY_EXAM_LIST = URL + "exams/";
    public static String GET_CATEGORY_LMS_LIST = URL + "lms/";
    public static String GET_LMS_SERIES_LIST = URL + "lms/series/";
    public static String LMS_SERIES = URL+"lms-series?user_id=";
    public static String GET_MY_PROFILE = URL + "user/profile/";
    public static String GET_SETTINGS = URL + "user/settings/";
    public static String EXAMS_SERIES = URL + "exam-series?user_id=";
    public static String GET_EXAM_SERIES_LIST= URL + "exams/student-exam-series/";
    public static String BOOKMARKS = URL + "user/bookmarks/";
    public static String ADD_FEED_BACK = URL + "feedback/send";
    public static String SUBSCRIPTIONS = URL + "user/subscriptions/";
    public static String NOTIFICATIONS = URL + "notifications";
    public static String START_EXAM = URL + "get-exam-questions/";
    public static String DELETE_BOOK_MARK = URL + "bookmarks/delete/";
    public static String CHANGE_PASSWORD = URL+"user/update-password";
    public static String UPDATE_PROFILE = URL + "users/edit/";
    public static String UPLOAD_PROFILE_PIC = URL + "profile-image";
    public static String PRIVACY_POLICY = URL + "pages/privacy-policy";
    public static String TERMS_CONDITIONS = URL + "pages/terms-conditions";
    public static String UPDATE_SETTINGS = URL + "update/user-sttings/";
    public static String EXAM_INSTUCTIONS = URL + "instructions/";
    public static String ANALYSIS_BY_SUBJECT= URL + "analysis/subject/";
    public static String ANALYSIS_BY_EXAM = URL + "analysis/exam/";
    public static String BOOK_MARK_SAVE_REMOVE = URL + "bookmarks/save";
    public static String APPLY_COUPON_CODE = URL + "validate/coupon";
    public static String SOCIAL_LOGINS = URL + "users/social-login";
    public static String SAVE_TRANSACTION_STATUS = URL + "save-transaction";
    public static String GET_RESULT = URL + "finish-exam/";
    public static String GET_EXAMS_HISTORY = URL + "analysis/history/";
    public static String GET_EXAM_KEY = URL + "get-exam-key/";
    public static String OFFLINE_PAYMENT = URL + "update-offline-payment";
    public static String GET_CURRENCY_CODE = URL + "get-currency-code";

    public static String GET_PAYMET_GATEWAYS = URL + "get-payment-gateways";


    public static void overrideFont(Context context, String defaultFontNameToOverride, String customFontFileNameInAssets) {
        try {
            final Typeface customFontTypeface = Typeface.createFromAsset(context.getAssets(), customFontFileNameInAssets);
            final Field defaultFontTypefaceField = Typeface.class.getDeclaredField(defaultFontNameToOverride);
            defaultFontTypefaceField.setAccessible(true);
            defaultFontTypefaceField.set(null, customFontTypeface);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static ProgressBar m_Dialog ;
    public static ProgressBar showProgressDialog(Context  context, String message){
        m_Dialog   = new ProgressBar(context);
        m_Dialog.setProgress(R.id.progressBar);
        m_Dialog.setVisibility(View.VISIBLE);

        ViewGroup layout = (ViewGroup) ((Activity) context).findViewById(android.R.id.content).getRootView();

        m_Dialog = new ProgressBar(context, null, android.R.attr.progressBarStyle);
        m_Dialog.setIndeterminate(true);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

        RelativeLayout rl = new RelativeLayout(context);

        rl.setGravity(Gravity.CENTER);
        rl.addView(m_Dialog);

        layout.addView(rl, params);

        return m_Dialog;
    }

    public static void dissmisProgress() {
        if(m_Dialog!=null){
            m_Dialog.setVisibility(View.GONE);
        }
    }

    public static void showProgress() {
        if(m_Dialog!=null){
            m_Dialog.setVisibility(View.VISIBLE);
        }
    }

}
