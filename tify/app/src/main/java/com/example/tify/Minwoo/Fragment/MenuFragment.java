package com.example.tify.Minwoo.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.tify.Minwoo.Activity.OrderSummaryActivity;
import com.example.tify.Minwoo.Adapter.MenuAdapter;
import com.example.tify.Minwoo.Bean.Menu;
import com.example.tify.Minwoo.NetworkTask.LMW_MenuNetworkTask;
import com.example.tify.R;

import java.util.ArrayList;


public class MenuFragment extends Fragment {

    String TAG = "MenuFragment";

    private ArrayList<Menu> menuList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MenuAdapter mAdapter;

    private ArrayList<Menu> list;

    String macIP;
    String urlAddr = null;
    String where = null;
    String sName = null;
    int user_uSeqNo = 0;
    int store_sSeqNo = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            // Inflate the layout for this fragment
            View v =  inflater.inflate(R.layout.lmw_fragment_menu, container, false);



            // StoreInfoActivity로 부터 값을 받는다.
            Bundle bundle = getArguments();
            macIP = bundle.getString("macIP");
            user_uSeqNo = bundle.getInt("user_uSeqNo");
            store_sSeqNo = bundle.getInt("store_sSeqNo");
            sName = bundle.getString("sName");

            // 초기 설정 값
            where = "select";
            urlAddr = "http://" + macIP + ":8080/tify/lmw_menulist.jsp?store_sSeqNo=" + store_sSeqNo;
            Log.v(TAG, "urlAddr : " + urlAddr);

            Log.v(TAG, "macIP : " + macIP);
            Log.v(TAG, "user_uSeqNo : " + user_uSeqNo);
            Log.v(TAG, "store_sSeqNo : " + store_sSeqNo);
            Log.v(TAG, "sName : " + sName);
            //----------------------



            //recyclerview
            recyclerView = v.findViewById(R.id.recycler_view);
            recyclerView.setHasFixedSize(true);
            mAdapter = new MenuAdapter(MenuFragment.this, R.layout.lmw_fragment_menu , menuList, macIP);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);

            list = new ArrayList<Menu>();
            list = connectGetData(); // db를 통해 받은 데이터를 담는다.

            mAdapter.setOnItemClickListener(new MenuAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View v, int position) {
                    Intent intent = new Intent(getActivity(), OrderSummaryActivity.class);

                    intent.putExtra("mNo", menuList.get(position).getmNo());
                    intent.putExtra("mName", menuList.get(position).getmName());
                    intent.putExtra("mPrice", menuList.get(position).getmPrice());
                    intent.putExtra("mSizeUp", menuList.get(position).getmSizeUp());
                    intent.putExtra("mShot", menuList.get(position).getmShot());
                    intent.putExtra("mImage", menuList.get(position).getmImage());
                    intent.putExtra("mType", menuList.get(position).getmType());

                    intent.putExtra("macIP", macIP);
                    intent.putExtra("user_uSeqNo", user_uSeqNo);
                    intent.putExtra("store_sSeqNo", store_sSeqNo);
                    intent.putExtra("sName", sName);

                    startActivity(intent);
                }
            });

        return v;
    }

    private ArrayList<Menu> connectGetData(){
        ArrayList<Menu> beanList = new ArrayList<Menu>();
        try {
            ///////////////////////////////////////////////////////////////////////////////////////
            // Date : 2020.12.25
            //
            // Description:
            //  - NetworkTask의 생성자 추가 : where <- "select"
            //
            ///////////////////////////////////////////////////////////////////////////////////////
            LMW_MenuNetworkTask networkTask = new LMW_MenuNetworkTask(getActivity(), urlAddr, where);
            ///////////////////////////////////////////////////////////////////////////////////////

            Object obj = networkTask.execute().get();
            menuList = (ArrayList<Menu>) obj;
            Log.v(TAG, "menuList.size() : " + menuList.size());

            mAdapter = new MenuAdapter(MenuFragment.this, R.layout.lmw_fragment_menu_recycler_item, menuList,macIP);
            recyclerView.setAdapter(mAdapter);

            beanList = menuList;

        }catch (Exception e){
            e.printStackTrace();
        }
        return beanList;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

//    private void prepareData() {
//        menuList.add(new Menu("아메리카노(HOT)",3500));
//        menuList.add(new Menu("아메리카노(COLD)",4000));
//        menuList.add(new Menu("라떼(HOT)",4000));
//        menuList.add(new Menu("라떼(COLD)",4500));
//        menuList.add(new Menu("치즈케이크",5000));
//    }

    @Override
    public void onResume() {
        super.onResume();


    }
}