package com.example.tify.Minwoo.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tify.Minwoo.Adapter.CartAdapter;
import com.example.tify.Minwoo.Bean.Cart;
import com.example.tify.Minwoo.Bean.Order;
import com.example.tify.Minwoo.NetworkTask.LMW_CartNetworkTask;
import com.example.tify.Minwoo.NetworkTask.LMW_OrderNetworkTask;
import com.example.tify.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {

    String TAG = "CartActivity";

    private ArrayList<Cart> data = null;
    private CartAdapter adapter = null;
    private ListView listView = null;
    private RecyclerView recyclerView = null;
    private RecyclerView.LayoutManager layoutManager = null;

    private ArrayList<Cart> list;
    private ArrayList<Order> orders;

    // layout
    Button payBtn;
    TextView tv_sName;
    ImageView iv_Init;
    TextView tv_totalPrice;

    // OrderSummaryActivity로 부터 받을 값
    String sName;

    // cartlist RUD
    String macIP;
    String urlAddr = null;
    String where = null;
    int user_uSeqNo = 0;
    int store_sSeqNo = 0;

    String strTotal;
    int oNo;
    int total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lmw_activity_cart);
        Log.v(TAG, "CartActivity onCreate");

        // 받은 값 저장
        Intent intent = getIntent();
        sName = intent.getStringExtra("sName");
        macIP = intent.getStringExtra("macIP");
        user_uSeqNo = intent.getIntExtra("user_uSeqNo", 0);
        store_sSeqNo = intent.getIntExtra("store_sSeqNo", 0);

        // layout 세팅
        payBtn = findViewById(R.id.cart_Btn_Pay);
        tv_sName = findViewById(R.id.cart_TV_sName);
        tv_totalPrice = findViewById(R.id.cart_TV_TotalPrice);
        iv_Init = findViewById(R.id.cart_IV_Cancel);

        tv_sName.setText(sName);
        tv_totalPrice.setText(strTotal + "원");

        orders = OrderConnectGetData(); // order Select onCreate할 때 미리 마지막 번호 찾아와서 +1하기
        if (orders.size() != 0) {
            oNo = orders.get(0).getoNo() + 1;
            Log.v(TAG, "마지막 oNo + 1: " + oNo);
        }


        // NetworkTask 세팅

//        data = new ArrayList<Cart>();
//
//        data.add(new Cart("아메리카노(COLD)","+사이즈업",null, "얼음 조금만 넣어주세요", 5,4000));
//        data.add(new Cart("아메리카노(HOT)","+사이즈업","+샷추가", "미지근하게 해주세요", 1,5000));
//        data.add(new Cart("라떼(COLD)","+사이즈업",null, null, 3,4000));
//        data.add(new Cart("라떼(HOT)","+사이즈업","+샷추가", null, 1,4000));
//        data.add(new Cart("치즈케이크","+사이즈업",null, "포크 2개 넣어주세요", 1,5500));
//        data.add(new Cart("밀크티(COLD)","+사이즈업",null, null, 1,5000));

        //리사이클러뷰에 있는 아이디를 찾기
        recyclerView = findViewById(R.id.cart_recycler_view);

        //리사이클러뷰 규격 만들기
        recyclerView.setHasFixedSize(true);

        //레이아웃 매니저 만들기
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //리사이클러뷰 어댑터를 넣기
//        adapter = new CartAdapter(CartActivity.this, R.layout.lmw_activity_cart_recycler_item, data);

        //어댑터에게 보내기
//        recyclerView.setAdapter(adapter);

        // 기본 구분선
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),new LinearLayoutManager(this).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);


        list = new ArrayList<Cart>();
        list = connectGetData(); // db를 통해 받은 데이터를 담는다.

        Log.v(TAG, "list size : " + list.size());

        if(list.size() == 0){
            finish();

            Intent intent1 = new Intent(CartActivity.this, EmptyCartActivity.class);
            startActivity(intent1);
        }

        // 툴바 생성
        Toolbar toolbar = (Toolbar)findViewById(R.id.cart_toolbar); // 상단 툴바
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김

        // 클릭 리스너
        payBtn.setOnClickListener(mClickListener);
        iv_Init.setOnClickListener(mClickListener);



    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                Intent intent = new Intent(CartActivity.this, StoreInfoActivity.class);
                startActivity(intent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = null;

            switch (v.getId()){
                case R.id.cart_Btn_Pay:
                    intent = new Intent(CartActivity.this, BeforePayActivity2.class);

                    intent.putExtra("sName", sName);
                    intent.putExtra("macIP", macIP);
                    intent.putExtra("user_uSeqNo", user_uSeqNo);
                    intent.putExtra("store_sSeqNo", store_sSeqNo);
                    intent.putExtra("total", total);
                    intent.putExtra("from", "CartActivity");

                    startActivity(intent);
                    break;
                case R.id.cart_IV_Cancel:
                    Log.v(TAG, "클릭 확인");

                    AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                    builder.setTitle("<장바구니 전체삭제>");
                    builder.setMessage("장바구니에 담긴 모든 메뉴를 삭제하시겠습니까?");
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            urlAddr = "http://" + macIP + ":8080/tify/lmw_cartlist_delete_all.jsp?user_uSeqNo=" + user_uSeqNo + "&store_sSeqNo=" + store_sSeqNo;
                            where = "delete";

                            connectDeleteData(); // 해당 메뉴 삭제
                            Intent intent = new Intent(CartActivity.this, CartActivity.class);
                            intent.putExtra("sName", sName);
                            intent.putExtra("macIP", macIP);
                            intent.putExtra("user_uSeqNo", user_uSeqNo);
                            intent.putExtra("store_sSeqNo", store_sSeqNo);
                            startActivity(intent);
                        }
                    });
                   builder.setNegativeButton("취소", null);
                   builder.create().show();
                   break;
            }

        }
    };

    private ArrayList<Cart> connectGetData(){
        ArrayList<Cart> beanList = new ArrayList<Cart>();

        where = "select";
        urlAddr = "http://" + macIP + ":8080/tify/lmw_cartlist_select.jsp?user_uSeqNo=" + user_uSeqNo;

        try {
            ///////////////////////////////////////////////////////////////////////////////////////
            // Date : 2020.12.25
            //
            // Description:
            //  - NetworkTask의 생성자 추가 : where <- "select"
            //
            ///////////////////////////////////////////////////////////////////////////////////////
            LMW_CartNetworkTask networkTask = new LMW_CartNetworkTask(CartActivity.this, urlAddr, where);
            ///////////////////////////////////////////////////////////////////////////////////////

            Object obj = networkTask.execute().get();
            data = (ArrayList<Cart>) obj;
            Log.v(TAG, "data.size() : " + data.size());

            adapter = new CartAdapter(CartActivity.this, R.layout.lmw_activity_cart_recycler_item, data);
            recyclerView.setAdapter(adapter);

            total = 0;

            for(int i = 0; i < data.size(); i++){
                total = total + data.get(i).getcLPrice();
            }

            NumberFormat moneyFormat = null;
            moneyFormat = NumberFormat.getInstance(Locale.KOREA);
            strTotal = moneyFormat.format(total);

            tv_totalPrice.setText(strTotal + "원");

            beanList = data;

        }catch (Exception e){
            e.printStackTrace();
        }
        return beanList;
    }

    private ArrayList<Order> OrderConnectGetData(){
        ArrayList<Order> beanList = new ArrayList<Order>();

        where = "select";
        urlAddr = "http://" + macIP + ":8080/tify/lmw_orderoNo_select.jsp?user_uNo=" + user_uSeqNo;

        try {
            ///////////////////////////////////////////////////////////////////////////////////////
            // Date : 2020.12.25
            //
            // Description:
            //  - NetworkTask의 생성자 추가 : where <- "select"
            //
            ///////////////////////////////////////////////////////////////////////////////////////
            LMW_OrderNetworkTask networkTask = new LMW_OrderNetworkTask(CartActivity.this, urlAddr, where);
            ///////////////////////////////////////////////////////////////////////////////////////

            Object obj = networkTask.execute().get();
            orders = (ArrayList<Order>) obj;
            Log.v(TAG, "data.size() : " + orders.size());

            beanList = orders;

        }catch (Exception e){
            e.printStackTrace();
        }
        return beanList;
    }

    private String connectDeleteData(){ // 선택한 메뉴 삭제 or 전체 삭제
        String result = null;

        try {
            ///////////////////////////////////////////////////////////////////////////////////////
            // Date : 2020.12.25
            //
            // Description:
            //  - NetworkTask를 한곳에서 관리하기 위해 기존 CUDNetworkTask 삭제
            //  - NetworkTask의 생성자 추가 : where <- "insert"
            //
            ///////////////////////////////////////////////////////////////////////////////////////
            LMW_CartNetworkTask networkTask = new LMW_CartNetworkTask(CartActivity.this, urlAddr, where);
            ///////////////////////////////////////////////////////////////////////////////////////

            ///////////////////////////////////////////////////////////////////////////////////////
            // Date : 2020.12.24
            //
            // Description:
            //  - 입력 결과 값을 받기 위해 Object로 return후에 String으로 변환 하여 사용
            //
            ///////////////////////////////////////////////////////////////////////////////////////
            Object obj = networkTask.execute().get();
            result = (String) obj;
            ///////////////////////////////////////////////////////////////////////////////////////

        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }


    @Override
    protected void onResume() {
        super.onResume();

        adapter.setOnItemClickListener(new CartAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

                AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                builder.setTitle("<장바구니 삭제>");
                builder.setMessage("이 메뉴를 장바구니에서 삭제하시겠습니까?");
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() { // 해당 메뉴 장바구니에서 삭제
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        urlAddr = "http://" + macIP + ":8080/tify/lmw_cartlist_delete_choose.jsp?user_uSeqNo=" + user_uSeqNo + "&cLNo=" + list.get(position).getcLNo();
                        where = "CartActivity";

                        connectDeleteData(); // 해당 메뉴 삭제

                        Intent intent = new Intent(CartActivity.this, CartActivity.class);

                        intent.putExtra("sName", sName);
                        intent.putExtra("macIP", macIP);
                        intent.putExtra("user_uSeqNo", user_uSeqNo);
                        intent.putExtra("store_sSeqNo", store_sSeqNo);

                        startActivity(intent);

                        Toast.makeText(CartActivity.this, "선택하신 메뉴가 장바구니에서 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("취소", null);
                builder.create().show();
            }
        });
    }
}