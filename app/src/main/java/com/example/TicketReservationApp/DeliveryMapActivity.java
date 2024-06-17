package com.example.TicketReservationApp;

import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.app.AlertDialog;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class DeliveryMapActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap deliveryMap;
    Button addressBtn, confirmBtn;
    EditText addressEdt, nameEdt, telEdt;
    LinearLayout linear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliverymap);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("배송정보 입력");
        builder.setMessage("예매하실 티켓을 수령받기위한 배송정보를 작성해주세요");

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();


        setTitle("배송정보 입력");
        addressBtn = (Button) findViewById(R.id.addressBtn);
        addressEdt = (EditText) findViewById(R.id.addressEdt);
        linear = (LinearLayout) findViewById(R.id.linear);
        confirmBtn = (Button) findViewById(R.id.confirmBtn);
        nameEdt = (EditText) findViewById(R.id.nameEdt);
        telEdt = (EditText) findViewById(R.id.telEdt);

        Intent prevIntent = getIntent();

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmationDialog(prevIntent);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        addressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = addressEdt.getText().toString();
                if (!address.isEmpty()) {
                    // 주소값을 위도 경도로 변환하기위한 Geocoder 클래스
                    Geocoder geocoder = new Geocoder(DeliveryMapActivity.this, Locale.getDefault());
                    try {
                        //사용자가 입력한 주소값을 가지고, 이를 위도와 경도 값으로 변환. 이를 통해 입력값이 지도상에 위도 경도로 나타나도록 구현.
                        List<Address> addresses = geocoder.getFromLocationName(address, 1);
                        if (addresses != null && !addresses.isEmpty()) {
                            Address location = addresses.get(0);
                            double lat = location.getLatitude();
                            double lng = location.getLongitude();
                            moveCameraToLocation(lat, lng); // 카메라 위치 이동
                            linear.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(DeliveryMapActivity.this, "유효한 주소를 입력해주세요.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(DeliveryMapActivity.this, "주소 변환에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(DeliveryMapActivity.this, "주소를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        deliveryMap = googleMap;
    }

    private void moveCameraToLocation(double lat, double lng) {
        if (deliveryMap != null) {
            LatLng location = new LatLng(lat, lng);
            deliveryMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 17));
            deliveryMap.addMarker(new MarkerOptions().position(location).title("배송지"));
        }
    }

    private void showConfirmationDialog(Intent prevIntent) {
        AlertDialog.Builder dlgBuilder = new AlertDialog.Builder(this);
        //dlgBuilder.setTitle("확인");
        dlgBuilder.setMessage("입력한 배송정보로 결제를 진행하시겠습니까?");
        dlgBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
                new deliveryLoading(prevIntent).execute(); // deliveryLoading 실행
            }
        });
        dlgBuilder.setNegativeButton("닫기", null);
        dlgBuilder.show();
    }

    //배송정보 확정 버튼 클릭후, 배송정보 처리가 진행됨을 구현하기위한 로딩 클래스.
    private class deliveryLoading extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progressDialog;
        private Intent prevIntent;

        public deliveryLoading(Intent intent) {
            this.prevIntent = intent;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(DeliveryMapActivity.this);
            progressDialog.setMessage("배송정보 처리중...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                Thread.sleep(3000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();

            Intent intent = new Intent(getApplicationContext(), PayActivity.class);
            intent.putExtra("name", prevIntent.getStringArrayExtra("name"));
            intent.putExtra("price", prevIntent.getIntArrayExtra("price"));
            intent.putExtra("quantity", prevIntent.getIntArrayExtra("quantity"));
            intent.putExtra("date", prevIntent.getStringArrayExtra("date"));
            intent.putExtra("fee", prevIntent.getIntExtra("fee", 0));
            intent.putExtra("consumerName", nameEdt.getText().toString());
            intent.putExtra("telNum", telEdt.getText().toString());
            intent.putExtra("address", addressEdt.getText().toString());

            startActivity(intent);
            finish();
        }
    }
}

