package com.example.TicketReservationApp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ThankActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank);
        setTitle("HS Concert Ticket Reservation App");

        Intent intent = getIntent();
        String consumerName = intent.getStringExtra("consumerName");

        // 예매 감사 메시지
        TextView thankMessageTextView = findViewById(R.id.thank_message);
        thankMessageTextView.setText(consumerName + " 고객님,\n"+"예매해주셔서 감사합니다!");

        // StartActivity로 이동
        Button startButton = findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent startIntent = new Intent(ThankActivity.this, StartActivity.class);
                startActivity(startIntent);
            }
        });
    }
}
