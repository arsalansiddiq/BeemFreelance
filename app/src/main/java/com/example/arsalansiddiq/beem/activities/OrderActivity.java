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
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.example.arsalansiddiq.beem.models.requestmodels.LoginRequest;
import com.example.arsalansiddiq.beem.models.responsemodels.LoginResponse;
import com.example.arsalansiddiq.beem.models.responsemodels.salesresponsemodels.SalesObjectResponse;
import com.example.arsalansiddiq.beem.models.responsemodels.salesresponsemodels.SalesSKUArrayResponse;
import com.example.arsalansiddiq.beem.utils.Constants;
import com.example.arsalansiddiq.beem.utils.NetworkUtils;

import java.util.List;

import retrofit2.Response;

public class OrderActivity extends AppCompatActivity implements LocationListener {

    private Spinner spinner_saleStatus;
    private Button btn_submit;
    private NetworkUtils networkUtils;
    String cusName; Long contact; String email; String gender; String age; String cBrand;
    String pBrand; Integer saleStatus; Integer empId; String empName; String designation; String city;
    Integer location;
    private LocationManager locationManager;
    private float latitude, longitude;
    private BeemDatabase beemDatabase;

    private ListView listView_order;

    private Intent intent;

    private FrameLayout frameLayout_noProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        beemDatabase = new BeemDatabase(this);
        beemDatabase.getReadableDatabase();

        spinner_saleStatus = findViewById(R.id.spinner_saleStatus);
        btn_submit = findViewById(R.id.btn_submit);
        listView_order = findViewById(R.id.listView_order);

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
        networkUtils = new NetworkUtils(OrderActivity.this);

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

                                        final BeemPreferences beemPreferences = new BeemPreferences(OrderActivity.this);
                                        beemPreferences.initialize_and_createPreferences_forStatus(loginResponse.body().getStatus());
                                        intent = new Intent(OrderActivity.this, SalesActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        Log.i("sale Statuss", String.valueOf(loginResponse.body().getStatus()));
                                    }

                                    @Override
                                    public void failed(String error) {
                                        Toast.makeText(OrderActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                    }
    }


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

                       List<SalesSKUArrayResponse> salesSKUArrayResponseArrayList = response.body().getSku();
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

}
