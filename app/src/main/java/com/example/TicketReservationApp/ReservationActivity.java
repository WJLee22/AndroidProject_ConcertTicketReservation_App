package com.example.TicketReservationApp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import java.util.Calendar;

public class ReservationActivity extends AppCompatActivity {
    String date;
    int quantity;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        Intent conIntent = getIntent();
        name = conIntent.getStringExtra("concertName");
        setTitle("<" + name + ">" + " 예매창");

        CalendarView calendarView = findViewById(R.id.calendarView);
        EditText ticketQuantity = findViewById(R.id.ticketQuantity);
        Button addToCartButton = findViewById(R.id.addToCartButton);

        // 오늘 날짜
        Calendar today = Calendar.getInstance();

        //콘서트 관람 가능일을 이 주로 세팅.

        // 최소 날짜 설정 (오늘로부터 1일 후)
        Calendar minDate = (Calendar) today.clone();
        minDate.add(Calendar.DAY_OF_YEAR, 1);
        calendarView.setMinDate(minDate.getTimeInMillis());

        // 최대 날짜 설정 (오늘로부터 7일 후)
        Calendar maxDate = (Calendar) today.clone();
        maxDate.add(Calendar.DAY_OF_YEAR, 7);
        calendarView.setMaxDate(maxDate.getTimeInMillis());

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                date = year + "/" + (month + 1) + "/" + dayOfMonth;
                Toast.makeText(ReservationActivity.this, "선택된 날짜: " + date, Toast.LENGTH_SHORT).show();
            }
        });

        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (date == null) {
                    Toast.makeText(ReservationActivity.this, "날짜를 선택하세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    quantity = Integer.parseInt(ticketQuantity.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(ReservationActivity.this, "유효한 예매 수량을 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (quantity <= 0) {
                    Toast.makeText(ReservationActivity.this, "예매 수량을 입력하세요", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(ReservationActivity.this, ConcertTicketActivity.class);
                    intent.putExtra("date", date);
                    intent.putExtra("quantity", quantity);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }
}
