package com.bayer.bayerreward.CustomTools;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;

import static android.support.v7.widget.RecyclerView.OnItemTouchListener;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;
import static android.support.v7.widget.RecyclerView.State;

class NoScrollHorizontalLayoutManager extends LinearLayoutManager {
    ScrollingTouchInterceptor interceptor = new ScrollingTouchInterceptor();
    protected boolean canScroll;

    public NoScrollHorizontalLayoutManager(Context ctx) {
        super(ctx, LinearLayoutManager.HORIZONTAL, false);
    }

    public OnItemTouchListener getInterceptor() {
        return interceptor;
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, State state, int position) {
        canScroll = true;
        super.smoothScrollToPosition(recyclerView, state, position);
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if(state == SCROLL_STATE_IDLE) {
            canScroll = false;
        }
    }

    @Override
    public boolean canScrollHorizontally() {
        return canScroll;
    }

    public class ScrollingTouchInterceptor implements OnItemTouchListener {
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            return canScrollHorizontally();
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }
}