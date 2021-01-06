package com.example.tify.Minwoo.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tify.Minwoo.Activity.OrderDetailActivity;
import com.example.tify.Minwoo.Bean.OrderList;
import com.example.tify.R;

import java.util.ArrayList;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.MyViewHolder> {

    String TAG = "OrderListAdapter 확인용";

    private ArrayList<OrderList> mDataset;
    private OrderDetailAdapter.OnItemClickListener mListener = null;

    //인터페이스 선언
    public interface OnItemClickListener{
        void onItemClick(View v, int position);

    }

    //메인에서 사용할 클릭메서드 선언
    public void setOnItemClickListener(OrderDetailAdapter.OnItemClickListener listener){
        this.mListener = listener;
        Log.v(TAG, "setOnItemClickListener");
    }

    // 메인 액티비티에서 받은 myDataset을 가져오
    public OrderDetailAdapter(OrderDetailActivity orderDetailActivity, int layout, ArrayList<OrderList> myDataset) {
        mDataset = myDataset;
        Log.v(TAG, "OrderDetailAdapter Constructor");

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mName;
        public TextView addOrder1;
        public TextView addOrder2;
        public TextView request;
        public TextView sName;
        public TextView oDate;
        public TextView oSeqno;
        public TextView subTotalPrice;

        MyViewHolder(View v) {
            super(v);
            Log.v(TAG, "MyViewHolder");
            oSeqno = v.findViewById(R.id.activity_OrderDetail_CV_OSeqno);
            oDate = v.findViewById(R.id.activity_OrderDetail_CV_ODate);
            sName = v.findViewById(R.id.activity_OrderDetail_CV_sName);
            mName = v.findViewById(R.id.activity_OrderDetail_CV_TV_mName);
            addOrder1 = v.findViewById(R.id.activity_OrderDetail_CV_TV_AddOrder1);
            addOrder2 = v.findViewById(R.id.activity_OrderDetail_CV_TV_AddOrder2);
            request = v.findViewById(R.id.activity_OrderDetail_CV_TV_Request);
            subTotalPrice = v.findViewById(R.id.activity_OrderDetail_CV_SubTotalPrice);

            // 뷰홀더에서만 리스트 포지션값을 불러올 수 있음.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.v(TAG, "MyViewHolder onClick");

                    int position=getAdapterPosition();//어뎁터 포지션값
                    // 뷰홀더에서 사라지면 NO_POSITION 을 리턴
                    if(position!=RecyclerView.NO_POSITION){
                        if(mListener !=null){
                            mListener.onItemClick(view,position);
                        }
                    }
                }
            });

        }
    }

    @NonNull
    @Override
    public OrderDetailAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.v(TAG, "onCreateViewHolder");
        // create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lmw_activity_orderdetail_recycler_item, parent, false);
        //     반복할 xml 파일
        OrderDetailAdapter.MyViewHolder vh = new OrderDetailAdapter.MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailAdapter.MyViewHolder holder, int position) {
        Log.v(TAG, "onBindViewHolder");

       holder.oSeqno.setText("주문번호 : " + mDataset.get(position).getoSeqno());
       holder.oDate.setText(mDataset.get(position).getoDate());
//       holder.sName.setText("가게이름");
        holder.mName.setText(mDataset.get(position).getmName());

        if(mDataset.get(position).getAddOrder1() != null){
            holder.addOrder1.setVisibility(View.VISIBLE);
            holder.addOrder1.setText(mDataset.get(position).getAddOrder1());
        }
        if(mDataset.get(position).getAddOrder2() != null){
            holder.addOrder2.setVisibility(View.VISIBLE);
            holder.addOrder2.setText(mDataset.get(position).getAddOrder2());
        }
        if(mDataset.get(position).getRequest() != null){
            holder.request.setVisibility(View.VISIBLE);
            holder.request.setText(mDataset.get(position).getRequest());
        }

        holder.subTotalPrice.setText(mDataset.get(position).getSubTotalPrice() + "원");


    }

    @Override
    public int getItemCount() {
        Log.v(TAG, "getItemCount");
        return mDataset.size();
    }
}
