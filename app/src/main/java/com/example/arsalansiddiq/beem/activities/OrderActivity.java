package com.example.arsalansiddiq.beem.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arsalansiddiq.beem.R;
import com.example.arsalansiddiq.beem.adapters.CustomListAdapter;
import com.example.arsalansiddiq.beem.databases.BeemDatabase;
import com.example.arsalansiddiq.beem.databases.BeemPreferences;
import com.example.arsalansiddiq.beem.interfaces.LoginInterface;
import com.example.arsalansiddiq.beem.interfaces.SKUCategoryInterface;
import com.example.arsalansiddiq.beem.interfaces.SampleInterface;
import com.example.arsalansiddiq.beem.models.responsemodels.LoginResponse;
import com.example.arsalansiddiq.beem.models.responsemodels.salesresponsemodels.SalesObjectResponse;
import com.example.arsalansiddiq.beem.models.responsemodels.salesresponsemodels.SalesSKUArrayResponse;
import com.example.arsalansiddiq.beem.utils.AppUtils;
import com.example.arsalansiddiq.beem.utils.Constants;
import com.example.arsalansiddiq.beem.utils.CustomAlertDialog;
import com.example.arsalansiddiq.beem.utils.NetworkUtils;

import java.util.List;
import retrofit2.Response;

public class OrderActivity extends AppCompatActivity implements LocationListener {

    private Spinner spinner_saleStatus = null;
    private NetworkUtils networkUtils = null;
    String cusName; Long contact; String email; String gender; String age; String cBrand = null;
    String pBrand = null;
    Integer saleStatus = null;
    private LocationManager locationManager = null;
    private float latitude, longitude;
    private BeemDatabase beemDatabase = null;

    private ListView listView_order = null;
    private Intent intent = null;
    private FrameLayout frameLayout_noProducts = null;
    private View view = null;
    private EditText edtText_loose = null;
    private EditText edtText_carton = null;
    private TextView txtView_name = null;

    private float totalAmount = 0;

    private List<SalesSKUArrayResponse> salesSKUArrayResponseArrayList = null;

    CustomAlertDialog customAlertDialog;

    int doubleQuantity = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        beemDatabase = new BeemDatabase(this);
        beemDatabase.getReadableDatabase();

        customAlertDialog = new CustomAlertDialog(OrderActivity.this);
        networkUtils = new NetworkUtils(OrderActivity.this);

        spinner_saleStatus = findViewById(R.id.spinner_saleStatus);
        listView_order = findViewById(R.id.listView_order);
        listView_order.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        listView_order.setItemsCanFocus(true);

        frameLayout_noProducts = findViewById(R.id.frameLayout_noProducts);

        if (getIntent().getExtras() != null) intent = getIntent();
        cusName = intent.getStringExtra("name");
        contact = intent.getExtras().getLong("contact");
        email = intent.getStringExtra("email");
        gender = intent.getStringExtra("gender");
        age = intent.getStringExtra("age");
        pBrand = intent.getStringExtra("pBrand");
        cBrand = intent.getStringExtra("cBrand");

        ArrayAdapter adapterGender = ArrayAdapter.createFromResource(this, R.array.saleStatus_array, android.R.layout.simple_spinner_item);
        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_saleStatus.setAdapter(adapterGender);

        spinner_saleStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        frameLayout_noProducts.setVisibility(View.GONE);
        getBrandItems();


    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onSubmit(View view) {

        getSelectedItemAndPrice();

        if (doubleQuantity > 0) {

            customAlertDialog.showDialog(false);

        } else {

            if (beemDatabase.checkUserSelectedItem()) {

                getLocation();

                if (spinner_saleStatus.getSelectedItemPosition() == 0) {

                    Toast.makeText(OrderActivity.this, "Please Select Sale Status", Toast.LENGTH_SHORT).show();

                } else {

                    SharedPreferences preferences = this.getSharedPreferences(Constants.BA_ATTENDANCE_ID, MODE_PRIVATE);
                    int id = preferences.getInt(Constants.KEY_BA_ATTENDANCE_ID, 0);

                    LoginResponse loginResponse = beemDatabase.getUserDetail();

                    if (networkUtils.isNetworkConnected()) {

                        String currentString = age;
                        String[] separated = currentString.split("-");

                        int calculatedAge = Integer.parseInt(separated[0]);
                        calculatedAge += Integer.parseInt(separated[1]);

                        calculatedAge = calculatedAge / 2;

                        networkUtils.sendSaleDetail(cusName, Math.toIntExact(contact), email, gender, calculatedAge, cBrand, pBrand, saleStatus,
                                id, loginResponse.getName(), "Manager", "Karachi", (int) latitude, new LoginInterface() {
                                    @Override
                                    public void success(Response<LoginResponse> loginResponse) {
                                        if (loginResponse.body().getStatus() == 1) {

//                                            sendOrder(loginResponse.body().getSales_id());

//                                            AppUtils appUtils = new AppUtils(OrderActivity.this);
//
//                                            networkUtils.sendOrderDetail(loginResponse.body().getSales_id(), appUtils.getDate(),
//                                                    "Brite", "Brite", 0, 0, 0, Float.valueOf(0),
//                                                    beemDatabase.getTotalAmount(), new SampleInterface() {
//
//                                                        @Override
//                                                        public void success(LoginResponse loginResponse) {
//                                                            final BeemPreferences beemPreferences = new BeemPreferences(OrderActivity.this);
//                                                            beemPreferences.initialize_and_createPreferences_forStatus(loginResponse.getStatus());
//                                                            intent = new Intent(OrderActivity.this, SalesActivity.class);
//                                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                                            startActivity(intent);
//                                                            Log.i("order Statuss", String.valueOf(loginResponse.getOrder_id()));
//                                                        }
//
//                                                        @Override
//                                                        public void failed(String error) {
//
//                                                        }
//                                                    });
                                        }
                                    }

                                    @Override
                                    public void failed(String error) {
                                        Toast.makeText(OrderActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
            } else {
                customAlertDialog.showDialog(true);
            }
        }


    }


//    void sendOrder(final int sales_id) {
//
//
//
////        final Handler handler = new Handler();
////        try {
////            handler.postDelayed(new Runnable() {
////                @Override
////                public void run() {
//
////                    Thread thread = new Thread(new Runnable(){
////                        @Override
////                        public void run(){
//
//                            AppUtils appUtils = new AppUtils(OrderActivity.this);
//                            networkUtils.sendOrderDetail(sales_id, "sdfsd",
//                                    "Brite", "Brite", 0, 0, 0, Float.valueOf(0),
//                                    Float.valueOf(0), new SampleInterface() {
//
//                                        @Override
//                                        public void success(LoginResponse loginResponse) {
//                                            final BeemPreferences beemPreferences = new BeemPreferences(OrderActivity.this);
//                                            beemPreferences.initialize_and_createPreferences_forStatus(loginResponse.getStatus());
//                                            intent = new Intent(OrderActivity.this, SalesActivity.class);
//                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                            startActivity(intent);
//                                            Log.i("order Statuss", String.valueOf(loginResponse.getOrder_id()));
//
//                                        }
//
//                                        @Override
//                                        public void failed(String error) {
//
//                                        }
//                                    });
////                        }
////                    });
////                    thread.start();
//
//
////                }
////            }, 2000);
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
//
//    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = (float) location.getLatitude();
        longitude = (float) location.getLongitude();

        Log.i("LocationOrder", latitude + "  " + longitude);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},99);
            return;
        } else {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, OrderActivity.this);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch(keyCode)
        {
            case KeyEvent.KEYCODE_BACK:

                intent = new Intent(OrderActivity.this, SalesActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
//                moveTaskToBack(true);

                return true;
        }
        return false;
    }

    private void getBrandItems() {


        NetworkUtils networkUtils = new NetworkUtils(OrderActivity.this);

        if (networkUtils.isNetworkConnected()) {
            networkUtils.getBrandsofUser(cBrand, new SKUCategoryInterface() {

                @Override
                public void success(Response<SalesObjectResponse> response) {
                    if (response.body().getStatus() == 1) {

                       salesSKUArrayResponseArrayList = response.body().getSku();
                       CustomListAdapter adapter = new CustomListAdapter(OrderActivity.this, 0, salesSKUArrayResponseArrayList);
                       listView_order.setAdapter(adapter);

                    } else {
                        Toast.makeText(OrderActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void failed(String error) {
                    Log.i("SKU", error);
                    Toast.makeText(OrderActivity.this, error, Toast.LENGTH_SHORT).show();
                    listView_order.setVisibility(View.GONE);

                    frameLayout_noProducts.setVisibility(View.VISIBLE);

//                    txtView_noProducts.setVisibility(View.VISIBLE);
                }
            });
        }

    }


    void getSelectedItemAndPrice() {

        beemDatabase.removeSelectedItemTableRaws();

        int listLength = listView_order.getChildCount();

        int looseItem, cartonItem;

        if  (listLength > 0) {

            for (int i = 0; i < listLength; i++) {

                doubleQuantity = 0;

                looseItem = 0; cartonItem = 0;

                view = listView_order.getChildAt(i);

                txtView_name = (TextView) view.findViewById(R.id.txtView_name);

                edtText_loose = (EditText) view.findViewById(R.id.edtText_loose);
                edtText_carton = (EditText) view.findViewById(R.id.edtText_carton);

                String looseText = edtText_loose.getText().toString().trim();
                String cartonText = edtText_carton.getText().toString().trim();

                if (looseText.equals("")  &&
                        cartonText.equals("")) {
                } else if (looseText.length() > 0 &&
                        cartonText.length() > 0){
                    doubleQuantity += 1;
                    return;
                } else if (looseText.length() > 0 &&
                        cartonText.equals("")){
                    looseItem = Integer.parseInt(looseText);
                } else if (looseText.equals("") &&
                        cartonText.length() > 0){
                    cartonItem = Integer.parseInt(cartonText);
                }

                SalesSKUArrayResponse salesSKUArrayResponse = salesSKUArrayResponseArrayList.get(i);

                     if (looseItem != 0 || cartonItem != 0) {

                         float totalAmount = cartonItem * 10 * salesSKUArrayResponse.getPrice();

                        beemDatabase.insertSelectItemDetail(salesSKUArrayResponse.getName(), looseItem, cartonItem, totalAmount);
                    }

            }
        } else {

            listView_order.setVisibility(View.GONE);
            frameLayout_noProducts.setVisibility(View.VISIBLE);

        }

    }

    private void getCalculatedValues(int i, int loose, int carton) {

        SalesSKUArrayResponse salesSKUArrayResponse = salesSKUArrayResponseArrayList.get(i);

        float pricePerItem = (salesSKUArrayResponse.getPrice() / salesSKUArrayResponse.getItemPerCarton());

        int items = 0;
        items = carton * 10;
        items += loose;

        totalAmount += pricePerItem * items;

    }

}
