package com.example.arsalansiddiq.beem.utils;

import com.example.arsalansiddiq.beem.models.requestmodels.LoginRequest;
import com.example.arsalansiddiq.beem.models.responsemodels.AttandanceResponse;
import com.example.arsalansiddiq.beem.models.responsemodels.LoginResponse;
import com.example.arsalansiddiq.beem.models.responsemodels.salesresponsemodels.SalesObjectResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by arsalansiddiq on 7/16/18.
 */

public interface NetworkRequestInterfaces {

//    @Headers("Content-Type: application/json")
//    @POST("insert.php")
//    Call<SendLcoationResponseModel> insertCoordinates(@Body SendLocationRequestModel sendLocationRequestModel);

    @Headers("Content-Type: application/json")
    @POST("login")
    Call<LoginResponse> userLogin(@Body LoginRequest loginRequest);

//    @Multipart
//    @POST("attendance")
//    Call<AttandanceResponse> attandanceBA();

    @Multipart
    @POST("attendance")
    Call<AttandanceResponse> attandanceBA(@Part("date") String date,
                                                   @Part("empid") int userId,
                                                   @Part("name") String name,
                                                   @Part("startTime") String startTime,
                                                   @Part("latitude") double latitude,
                                                   @Part("longitude") double longitude,
                                                   @Part("status") int status,
                                                           @Part MultipartBody.Part file);

    @Multipart
    @POST("endattendance")
    Call<AttandanceResponse> endAttandanceBA(@Part("id") int meetingId,
                                          @Part("endTime") String endTime,
                                          @Part("elat") float eLatitude,
                                          @Part("elng") float eLongitude,
                                          @Part MultipartBody.Part endImage);

    @GET("sku/{SKUCaategory}")
    Call<SalesObjectResponse> getBrands(@Path("SKUCaategory") String SKUCaategory);

    @Multipart
    @POST("sales")
    Call<LoginResponse> sendSalesDetails(@Part("cusName") String cusName, @Part("Contact") Integer contact,
                                         @Part("email") String email, @Part("gender") String gender,
                                         @Part("age") Integer age, @Part("cBrand") String cBrand,
                                         @Part("pBrand") String pBrand, @Part("saleStatus") Integer saleStatus,
                                         @Part("empId") Integer empId, @Part("empName") String empName,
                                         @Part("designation") String designation, @Part("City") String city,
                                         @Part("Location") Integer location);

//    @Multipart
//    @POST("sales")
//    Call<LoginResponse> sendSalesDetails(@Field("cusName") String cusName, @Field("Contact") Integer contact,
//                                         @Field("email") String email, @Field("gender") String gender,
//                                         @Field("age") Integer age, @Field("cBrand") String cBrand,
//                                         @Field("pBrand") String pBrand, @Field("saleStatus") Integer saleStatus,
//                                         @Field("empId") Integer empId, @Field("empName") String empName,
//                                         @Field("designation") String designation, @Field("City") String city,
//                                         @Field("Location") Integer location);


}
