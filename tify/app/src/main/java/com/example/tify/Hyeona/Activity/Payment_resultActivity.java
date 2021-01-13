package com.example.tify.Hyeona.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tify.Hyeona.Adapter.review_adapter;
import com.example.tify.Hyeona.Bean.Bean_payment_select;
import com.example.tify.Hyeona.Bean.Bean_review_review;
import com.example.tify.Hyeona.Bean.Bean_reward_stamphistory;
import com.example.tify.Hyeona.NetworkTask.CUDNetworkTask_payment;
import com.example.tify.Hyeona.NetworkTask.CUDNetworkTask_review;
import com.example.tify.Hyeona.NetworkTask.CUDNetworkTask_stampCount;
import com.example.tify.Jiseok.Activity.JiseokMainActivity;
import com.example.tify.R;
import com.example.tify.ShareVar;

import java.util.ArrayList;


public class Payment_resultActivity extends AppCompatActivity {

    ShareVar shareVar =new ShareVar();
    String MacIP = shareVar.getMacIP();

    int userSeq;
    String userNickName;
    String myLocation;
    Button payment_result_btn;
    int oNo;
    int user_uNo;


    int coffee_count;


    TextView payment_money, payment_day, payment_card,payment_card_number;
    Bean_payment_select bean_payment_select;

    int point = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cha_main_search);

        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        user_uNo = auto.getInt("userSeq",0);

        Intent intent = getIntent();
        oNo = intent.getIntExtra("oNo",0);
        // 인텐트로 포인트 값 꼭 받아야함

        payment_money = findViewById(R.id.payment_money);
        payment_day = findViewById(R.id.payment_day);
        payment_card =findViewById(R.id.payment_card);
        payment_card_number = findViewById(R.id.payment_card_number);

        payment_result_btn = findViewById(R.id.payment_result_btn);
        payment_result_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Payment_resultActivity.this, JiseokMainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        connectGetData();
        connectInsertData(point);
        coffee_count = connectStampData(oNo);

    }

    private void connectGetData(){
        try {
            String urlAddr = "http://" + MacIP + ":8080/tify/cha_payment_select.jsp?";
            String urlAddress = urlAddr + "oNo=" + oNo;
            CUDNetworkTask_payment mCUDNetworkTask_payment = new CUDNetworkTask_payment(urlAddress,"select");
            Object obj = mCUDNetworkTask_payment.execute().get();
            bean_payment_select = (Bean_payment_select) obj;

            payment_money.setText(bean_payment_select.getoSum());
            payment_day.setText(bean_payment_select.getoInsertDate());
            payment_card.setText(bean_payment_select.getoCardName());
            payment_card_number.setText(bean_payment_select.getoCardNo());

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private int connectStampData(int oNo) {
        // 여기에서 몇잔 주문했는지 받아올 것임 받은 숫자는 다시 스탬프에 넣어야한다 (넣을예정)
        int result = 0;
        try {
            String urlAddr = "http://" + MacIP + ":8080/tify/cha_stamp_update1?oNo="+oNo;
            CUDNetworkTask_payment mCUDNetworkTask_payment = new CUDNetworkTask_payment(urlAddr,"stamp_count");
            Object obj = mCUDNetworkTask_payment.execute().get();
            result = (int) obj;
        }catch (Exception e){
            e.printStackTrace();
        }return result;
    }

    private int connectInsertData(int point) {
        //여기 리절트는 성공 실패만 확인
        int result = 0;
        try {
            String urlAddr = "http://" + MacIP + ":8080/tify/cha_payment_update.jsp?point="+point+"&user_uNo="+user_uNo;
            CUDNetworkTask_payment mCUDNetworkTask_payment = new CUDNetworkTask_payment(urlAddr,"update");
            Object obj = mCUDNetworkTask_payment.execute().get();
            result = (int) obj;

        }catch (Exception e){
            e.printStackTrace();
        }return result;
    }


}