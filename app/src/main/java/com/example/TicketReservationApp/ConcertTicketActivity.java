package com.example.TicketReservationApp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class ConcertTicketActivity extends AppCompatActivity {

    String[] name = {"I'M HERO THE STADIUM", "성시경의 축가", "YB 2024 TOUR LIGHTS; INFINITY", "OLIVIA RODRIGO GUTS", "노엘 겔러거 하이 플라잉 버즈", "sg 워너비:우리의 노래", "IU:THE GOLDEN HOUR", "STAYC-TEENFRESH"};
    int[] price = {100000, 80000, 120000, 150000, 110000, 90000, 130000, 70000};
    String[] genre = {"발라드&트로트", "발라드", "락", "팝", "팝&밴드", "발라드", "팝&발라드", "K-POP"};
    int[] quantity = {0, 0, 0, 0, 0, 0, 0, 0};
    String[] date = new String[name.length];
    int[] song = {R.raw.song1,R.raw.song2, R.raw.song3,R.raw.song4,R.raw.song5,R.raw.song6,R.raw.song7,R.raw.song8};
    private static final int RESERVATION_REQUEST_CODE = 1;
    private static final int CART_REQUEST_CODE = 2;

    int poss;
    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concertticket);

        for(int i=0; i<quantity.length; i++)
            quantity[i]=0;

        setTitle("HS Concert Ticket Reservation App");
        final GridView gv = findViewById(R.id.gridView1);
        MyGridAdapter gAdapter = new MyGridAdapter(this);
        gv.setAdapter(gAdapter);

        Button cartButton = findViewById(R.id.cart_button);
        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConcertTicketActivity.this, CartActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("price", price);
                intent.putExtra("genre", genre);
                intent.putExtra("quantity", quantity);
                intent.putExtra("date", date);
                startActivityForResult(intent, CART_REQUEST_CODE);
            }
        });
    }

    public class MyGridAdapter extends BaseAdapter {

        Context context;

        public MyGridAdapter(Context c) {
            context = c;
        }

        public int getCount() {
            return posterID.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        Integer[] posterID = {
                R.drawable.con01, R.drawable.con02, R.drawable.con03, R.drawable.con04,
                R.drawable.con05, R.drawable.con06, R.drawable.con07, R.drawable.con08
        };

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(200, 300));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setPadding(5, 5, 5, 5);

            imageView.setImageResource(posterID[position]);

            final int pos = position;

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    View dialogView = View.inflate(ConcertTicketActivity.this, R.layout.dialog, null);
                    AlertDialog.Builder dlgBuilder = new AlertDialog.Builder(ConcertTicketActivity.this);
                    ImageView ivPoster = dialogView.findViewById(R.id.ivPoster);
                    TextView tvConcertName = dialogView.findViewById(R.id.tvConcertName);
                    TextView tvConcertPrice = dialogView.findViewById(R.id.tvConcertPrice);
                    TextView tvMusicGenre = dialogView.findViewById(R.id.tvMusicGenre);
                    Button btnPlaySong = dialogView.findViewById(R.id.btnPlaySong);

                    ivPoster.setImageResource(posterID[pos]);
                    tvConcertName.setText(name[pos]);
                    tvConcertPrice.setText("가격: " + price[pos] + "원");
                    tvMusicGenre.setText("장르: " + genre[pos]);
                    poss = pos;

                    dlgBuilder.setTitle(name[pos]);
                    dlgBuilder.setIcon(R.drawable.ticket_icon);
                    dlgBuilder.setView(dialogView);
                    dlgBuilder.setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (mediaPlayer != null) {
                                mediaPlayer.release(); // 음악 재생 객체 해제
                                mediaPlayer = null;
                            }
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dlg = dlgBuilder.create();
                    dlg.show();

                    btnPlaySong.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mediaPlayer == null) {
                                mediaPlayer = MediaPlayer.create(getApplicationContext(), song[pos]);
                            }

                            if (mediaPlayer.isPlaying()) {
                                mediaPlayer.pause();
                                btnPlaySong.setText("대표곡 듣기");
                            } else {
                                mediaPlayer.start();
                                btnPlaySong.setText("일시중지");
                            }
                        }
                    });

                    Button btnReserve = dialogView.findViewById(R.id.btnReserve);
                    btnReserve.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // 예매하기 버튼을 눌렀을때 현재 음악이 재생 중인지 확인하고, 재생중이라면 음악 멈추기.
                            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                                mediaPlayer.pause();
                                mediaPlayer.release();
                                mediaPlayer = null;
                                btnPlaySong.setText("대표곡 듣기");
                            }
                            Intent intent = new Intent(getApplicationContext(), ReservationActivity.class);
                            intent.putExtra("concertName", name[pos]);
                            startActivityForResult(intent, RESERVATION_REQUEST_CODE);
                            dlg.dismiss();
                        }
                    });
                }
            });

            return imageView;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //reservationActivity로 부터 intent 되어졌다면
        if (requestCode == RESERVATION_REQUEST_CODE && resultCode == RESULT_OK)
        {
            date[poss] = data.getStringExtra("date");
            quantity[poss] = data.getIntExtra("quantity", 0);

            TextView text = findViewById(R.id.text);
            text.setText(name[poss] + " 공연일: " + date[poss] + ", 수량: " + quantity[poss]);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("장바구니에 담았습니다.");
            builder.setMessage("콘서트명: " + name[poss] + "\n시작일: " + date[poss] + "\n예매 수량: " + quantity[poss]+"매");

            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();

        }
        //cartActivity로 부터 intent 되어졌다면
        else if (requestCode == CART_REQUEST_CODE && resultCode == RESULT_OK) {
            name = data.getStringArrayExtra("name");
            price = data.getIntArrayExtra("price");
            quantity = data.getIntArrayExtra("quantity");
            date = data.getStringArrayExtra("date");

        }
    }
}