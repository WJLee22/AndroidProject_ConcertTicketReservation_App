package com.example.TicketReservationApp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class CartActivity extends AppCompatActivity {

    String[] name;
    int[] price;
    int[] quantity;
    String[] date;
    int fee = 2000; // 수수료를 2000원으로 설정.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        TableLayout cartTable = (TableLayout) findViewById(R.id.cart_table);
        TextView cartInfo = (TextView) findViewById(R.id.cartInfo);
        Button backButton = (Button) findViewById(R.id.backButton);
        Button consumeButton = (Button) findViewById(R.id.consumeButton);
        Button clearCartButton = (Button) findViewById(R.id.clearCartButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ConcertTicketActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("price", price);
                intent.putExtra("quantity", quantity);
                intent.putExtra("date", date);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        consumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCartEmpty()) {
                    cartIsEmptyDialog();
                }
                else {
                    consumeConfirmDialog();
                }
            }
        });

        clearCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearCart();
            }
        });

        Intent intent = getIntent();
        name = intent.getStringArrayExtra("name");
        price = intent.getIntArrayExtra("price");
        quantity = intent.getIntArrayExtra("quantity");
        date = intent.getStringArrayExtra("date");

        setTitle("고객님의 장바구니 입니다");

        if (!isCartEmpty() && name != null && name.length > 0) {
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
                if (quantity[i] > 0) { // quantity 값이 0이 아닌 경우에만 처리
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

            cartInfo.setText("총 가격: " + totalPrice + "원");
        } else {
            cartInfo.setText("장바구니가 비어 있습니다.");
        }
    }

    private TextView createTextView(String text) {
        TextView textView = new TextView(this);
        textView.setPadding(16, 16, 16, 16);
        textView.setText(text);
        textView.setTypeface(null, Typeface.BOLD);
        return textView;
    }

    //장바구니 비우기.
    private void clearCart() {
        quantity = new int[name.length];
        date = new String[name.length];

        TableLayout cartTable = (TableLayout) findViewById(R.id.cart_table);
        cartTable.removeAllViews(); //장바구니 목록 전체 삭제.

        TextView cartInfo = (TextView) findViewById(R.id.cartInfo);
        cartInfo.setText("장바구니가 비어 있습니다.");

        cartCleanedDialog();
    }

    private boolean isCartEmpty() {
        if (quantity == null)
            return true;
        for (int i = 0; i < quantity.length; i++) {
            if (quantity[i] > 0)
                return false;
        }
        return true;
    }

    private void cartIsEmptyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("장바구니가 비어 있습니다");
        builder.setMessage("장바구니에 상품을 담아주세요.");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void cartCleanedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("장바구니를 비웠습니다");
        builder.setMessage("장바구니에 담아둔 상품들을 깔끔히 비웠습니다.");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i)
            {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void consumeConfirmDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("예매진행 확인");
        builder.setMessage("예매를 진행하시겠습니까?");

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                Intent intent = new Intent(getApplicationContext(), DeliveryMapActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("price", price);
                intent.putExtra("quantity", quantity);
                intent.putExtra("date", date);
                intent.putExtra("fee", fee);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
