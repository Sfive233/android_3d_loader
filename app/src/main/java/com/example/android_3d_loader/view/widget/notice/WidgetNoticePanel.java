package com.example.android_3d_loader.view.widget.notice;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.android_3d_loader.R;

public class WidgetNoticePanel extends ScrollView implements OnNoticeOccurListener{

    private NoticeAddHandler mHandler;
    private LinearLayout mNoticePanelRoot;

    public WidgetNoticePanel(Context context) {
        super(context);
        init();
    }

    public WidgetNoticePanel(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.widget_notice_panel, this);
        mNoticePanelRoot = findViewById(R.id.notice_panel_root);
        mHandler = new NoticeAddHandler(this);
        Notice.setListener(this);
    }

    @Override
    public void noticeOccur(final Notice notice) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = Message.obtain();
                message.obj = notice;
                mHandler.sendMessage(message);
            }
        }).start();
    }

    public LinearLayout getNoticePanelRoot() {
        return mNoticePanelRoot;
    }

    private static class NoticeAddHandler extends Handler{
        private final WidgetNoticePanel mWidgetNoticePanel;
        private final LinearLayout mNoticePanelRoot;

        public NoticeAddHandler(WidgetNoticePanel mWidgetNoticePanel) {
            this.mWidgetNoticePanel = mWidgetNoticePanel;
            this.mNoticePanelRoot = mWidgetNoticePanel.getNoticePanelRoot();
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            Notice notice = (Notice) msg.obj;
            WidgetNotice newNotice;
            switch (notice.mNoticeType){
                case WARNING:
                    newNotice = new WarningNotice(mWidgetNoticePanel.getContext());
                    break;
                case ERROR:
                    newNotice = new ErrorNotice(mWidgetNoticePanel.getContext());
                    break;
                case INFO:
                default:
                    newNotice = new InfoNotice(mWidgetNoticePanel.getContext());
                    break;
            }
            newNotice.setMsg(notice.getNoticeMsg());
            mNoticePanelRoot.addView(newNotice, 0);
        }
    }
}
