package com.example.TicketReservationApp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PayActivity extends AppCompatActivity {

    String[] name;
    int[] price;
    int[] quantity;
    String []date;
    int fee;
    int shippingFee=3000;
    String consumerName,telNum,address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        setTitle("최종 구매 확정");

        TableLayout cartTable = (TableLayout) findViewById(R.id.cart_table);
        TextView cartInfo = (TextView) findViewById(R.id.cartInfo);
        Button consumeButton= (Button) findViewById(R.id.consumeButton);
        TextView consumerNametv=(TextView)findViewById(R.id.consumerNametv);
        TextView telNumtv=(TextView)findViewById(R.id.telNumtv);
        TextView addresstv=(TextView)findViewById(R.id.addresstv);
        TextView shippingFeetv=(TextView)findViewById(R.id.shippingFeetv);

        Intent intent = getIntent();
        name = intent.getStringArrayExtra("name");
        price = intent.getIntArrayExtra("price");
        quantity = intent.getIntArrayExtra("quantity");
        date = intent.getStringArrayExtra("date");
        fee=intent.getIntExtra("fee",0);
        consumerName=intent.getStringExtra("consumerName");
        telNum=intent.getStringExtra("telNum");
        address=intent.getStringExtra("address");

        consumerNametv.setText(consumerNametv.getText().toString()+" "+consumerName);
        telNumtv.setText(telNumtv.getText().toString()+" "+telNum);
        addresstv.setText(addresstv.getText().toString()+" "+address);
        shippingFeetv.setText(shippingFeetv.getText().toString()+" "+shippingFee+"원");

        // 테이블 헤더 추가
        TableRow headerRow = new TableRow(this);
        headerRow.addView(createTextView("콘서트명"));
        headerRow.addView(createTextView("관람일"));
        headerRow.addView(createTextView("가격"));
        headerRow.addView(createTextView("예매수량"));
        headerRow.addView(createTextView("수수료"));
        headerRow.addView(createTextView("소계"));
        cartTable.addView(headerRow);

        int totalPrice = 0;

        for (int i = 0; i < name.length; i++) {
            if (quantity[i] > 0) {
                TableRow row = new TableRow(this);

                int itemTotalPrice = price[i] * quantity[i] + fee * quantity[i];

                row.addView(createTextView(name[i]));
                row.addView(createTextView(date[i]));
                row.addView(createTextView(price[i] + "원"));
                row.addView(createTextView(quantity[i] + "매"));
                row.addView(createTextView(fee * quantity[i] + "원"));
                row.addView(createTextView(itemTotalPrice + "원"));

                cartTable.addView(row);

                totalPrice += itemTotalPrice;
            }
        }

        cartInfo.setText("총 가격: " + (totalPrice+shippingFee) + "원");

        consumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PayLoading().execute();
            }
        });
    }

    private TextView createTextView(String text) {
        TextView textView = new TextView(this);
        textView.setPadding(16, 16, 16, 16);
        textView.setText(text);
        textView.setTypeface(null, Typeface.BOLD);
        return textView;
    }

    // 구매 진행시 구매가 처리되는 로딩상태를 구현하기 위한 클래스. AsyncTask를 상속받아서,백그라운드에서 4초간 대기하며 결제 진행이 수행되도록.
    private class PayLoading extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(PayActivity.this);
            progressDialog.setMessage("결제 진행 중...");
            progressDialog.show();
        }


        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Intent intent = new Intent(getApplicationContext(), ThankActivity.class);
            intent.putExtra("consumerName", consumerName);
            startActivity(intent);
            finish();
        }
    }
}
