package com.aarontseng.myviewpagertab;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyFragment extends Fragment{
    private String mTitle;
    private static final String BUNDLE_TITLE="title";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle=getArguments();

        if (bundle!=null) {
            mTitle=(String)bundle.get(BUNDLE_TITLE);
        }

        TextView textView=new TextView(getActivity());
        textView.setText(mTitle);
        textView.setGravity(Gravity.CENTER);

        return textView;
    }

    public static MyFragment newInstance(String title){
        Bundle bundle=new Bundle();
        bundle.putString(BUNDLE_TITLE, title);

        MyFragment MyFragment =new MyFragment();
        MyFragment.setArguments(bundle);

        return MyFragment;
    }
}
