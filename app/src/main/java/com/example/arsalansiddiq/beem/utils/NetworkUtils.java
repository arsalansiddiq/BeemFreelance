package com.example.arsalansiddiq.beem.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.arsalansiddiq.beem.interfaces.AttandanceInterface;
import com.example.arsalansiddiq.beem.interfaces.EndAttendanceInterface;
import com.example.arsalansiddiq.beem.interfaces.LoginInterface;
import com.example.arsalansiddiq.beem.interfaces.SKUCategoryInterface;
import com.example.arsalansiddiq.beem.models.requestmodels.LoginRequest;
import com.example.arsalansiddiq.beem.models.responsemodels.AttandanceResponse;
import com.example.arsalansiddiq.beem.models.responsemodels.LoginResponse;
import com.example.arsalansiddiq.beem.models.responsemodels.salesresponsemodels.SalesObjectResponse;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by arsalansiddiq on 1/18/18.
 */

public class NetworkUtils {

    private static final String LOG_TAG = "NetworkUtils";

    private Context mcontext;

    private static NetworkRequestInterfaces networkRequestInterfaces = ApiUtils.getConnection();

    //ProgressDialog for Instance
    private ProgressDialog progressDialog;

    public NetworkUtils(Context context) {
        this.mcontext = context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setTitle("Loading");
        progressDialog.setCancelable(false);
    }

    public boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                mcontext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public void userLogin(LoginRequest loginRequest, final LoginInterface loginInterface) {
        progressDialog.show();
        networkRequestInterfaces.userLogin(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                progressDialog.cancel();
                if (response.isSuccessful()) {
                    Log.i(LOG_TAG, String.valueOf(response.body().getStatus()));
                    loginInterface.success(response);
                } else {
                    loginInterface.failed("invalid credentials");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.i(LOG_TAG, t.getLocalizedMessage().toString());
                loginInterface.failed("Something went wrong!");
                progressDialog.cancel();
            }
        });
    }

    public void attandanceBA(String date, int userId, String name, File file, String startTime,
                             float latitude, float longitude, int status, final AttandanceInterface attandanceInterface) {

        MultipartBody.Part filePart = MultipartBody.Part.createFormData("StartImage", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));

        progressDialog.show();
        networkRequestInterfaces.attandanceBA(date, userId, name, startTime, latitude,
                longitude, status, filePart).enqueue(new Callback<AttandanceResponse>() {
            @Override
            public void onResponse(Call<AttandanceResponse> call, Response<AttandanceResponse> response) {
                progressDialog.cancel();
                if (response.isSuccessful()) {
                    attandanceInterface.success(response);
                } else {
                    attandanceInterface.failed("invalid credentials");
                }
            }

            @Override
            public void onFailure(Call<AttandanceResponse> call, Throwable t) {
                Log.i(LOG_TAG, t.getLocalizedMessage().toString());
                attandanceInterface.failed(t.getLocalizedMessage().toString());
                progressDialog.cancel();
            }
        });
    }

    public void endAttandenceBA(int meetingId, String endTime, float eLatitude, float eLongitude,
                                File userImage, final EndAttendanceInterface endAttendanceInterface) {

        MultipartBody.Part endImage = MultipartBody.Part.createFormData("EndImage", userImage.getName(), RequestBody.create(MediaType.parse("image/*"), userImage));

        progressDialog.show();
        networkRequestInterfaces.endAttandanceBA(meetingId, endTime, eLatitude, eLongitude, endImage).enqueue(new Callback<AttandanceResponse>() {
            @Override
            public void onResponse(Call<AttandanceResponse> call, Response<AttandanceResponse> response) {
                progressDialog.cancel();
                if (response.isSuccessful()) {
                    endAttendanceInterface.success(response);
                } else {
                    endAttendanceInterface.failed("invalid credentials");
                }
            }

            @Override
            public void onFailure(Call<AttandanceResponse> call, Throwable t) {
                Log.i(LOG_TAG, t.getLocalizedMessage().toString());
                endAttendanceInterface.failed(t.getLocalizedMessage().toString());
                progressDialog.cancel();
            }
        });
    }

    public void getBrandsofUser(String brandName, final SKUCategoryInterface skuCategoryInterface) {

        progressDialog.show();

        networkRequestInterfaces.getBrands("Brite").enqueue(new Callback<SalesObjectResponse>() {
            @Override
            public void onResponse(Call<SalesObjectResponse> call, Response<SalesObjectResponse> response) {
                progressDialog.cancel();
                if (response.isSuccessful()) {
                    skuCategoryInterface.success(response);
                } else {
                    skuCategoryInterface.failed("No Products Available");
                }
            }

            @Override
            public void onFailure(Call<SalesObjectResponse> call, Throwable t) {
                Log.i(LOG_TAG, t.getLocalizedMessage().toString());
                skuCategoryInterface.failed(t.getLocalizedMessage().toString());
                progressDialog.cancel();
            }
        });
    }

    public void sendSaleDetail(String cusName, Integer contact, String email, String gender, Integer age, String cBrand,
                              String pBrand, Integer saleStatus, Integer empId, String empName, String designation, String city,
                              Integer location, final LoginInterface loginInterface) {

        progressDialog.show();

        networkRequestInterfaces.sendSalesDetails(cusName, contact, email, gender, age, cBrand, pBrand, saleStatus,
                empId, empName, designation, city, location).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                progressDialog.cancel();
                if (response.isSuccessful()) {
                    loginInterface.success(response);
                    Log.i("Sale Status", String.valueOf(response.body().getStatus()));
                } else {
                    loginInterface.failed("Something Went Worng");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.i(LOG_TAG, t.getLocalizedMessage().toString());
                loginInterface.failed(t.getLocalizedMessage().toString());
                progressDialog.cancel();
            }
        });
    }

}
