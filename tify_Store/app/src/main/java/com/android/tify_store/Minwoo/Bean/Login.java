package com.android.tify_store.Minwoo.Bean;

public class Login {

    int skSeqNo;
    int cnt;

    public Login(int skSeqNo, int cnt) {
        this.skSeqNo = skSeqNo;
        this.cnt = cnt;
    }

    public int getSkSeqNo() {
        return skSeqNo;
    }

    public void setSkSeqNo(int skSeqNo) {
        this.skSeqNo = skSeqNo;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }
}
