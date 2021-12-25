package com.jchip.album.photo.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * lazy load
 */
public abstract class BaseLazyFragment extends Fragment {
    protected boolean isVisiable = false;
    public boolean isPrepared = false;
    public boolean isLoadData = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return initView(inflater, container, savedInstanceState);
    }

    public abstract View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisiable = true;
            onVisiable();
        } else {
            isVisiable = false;
            onInVisiable();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getUserVisibleHint() && isPrepared) {
            loadData();
        }
    }

    private void onInVisiable() { // 不可見時做甚麼事
        unLoadData();
    }

    private void onVisiable() {
        loadData();
    }

    protected abstract void loadData();

    protected abstract void unLoadData();
}
