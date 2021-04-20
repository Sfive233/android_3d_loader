package com.example.android_3d_loader.view.widget.notice;

public class Notice {
    public enum NoticeType{
        INFO,
        WARNING,
        ERROR,
    }
    protected NoticeType mNoticeType;
    protected String mNoticeMsg;
    protected static OnNoticeOccurListener mOnNoticeOccurListener;

    public static void setListener(OnNoticeOccurListener l){
        mOnNoticeOccurListener = l;
    }

    public Notice(String msg, NoticeType noticeType){
        this.mNoticeMsg = msg;
        this.mNoticeType = noticeType;
    }

    public static void makeInfo(String msg){
        Notice notice = new Notice(msg, NoticeType.INFO);
        mOnNoticeOccurListener.noticeOccur(notice);
    }

    public static void makeWarning(String msg){
        Notice notice = new Notice(msg, NoticeType.WARNING);
        mOnNoticeOccurListener.noticeOccur(notice);
    }

    public static void makeError(String msg){
        Notice notice = new Notice(msg, NoticeType.ERROR);
        mOnNoticeOccurListener.noticeOccur(notice);
    }

    public NoticeType getNoticeType() {
        return mNoticeType;
    }

    public String getNoticeMsg() {
        return mNoticeMsg;
    }
}
