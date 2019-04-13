package com.example.traveler;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Fragment2 extends Fragment {
    private AttractionsDataManager mAttractionsDataManager;
    private Button button_search;
    private Button button_createdb;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_fragment2,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAttractionsDataManager=new AttractionsDataManager(getActivity());

//        button_createdb=(Button)getActivity().findViewById(R.id.button_attr);
        button_search=(Button)getActivity().findViewById(R.id.button_2);
        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                SQLiteDatabase db=mAttractionsDataManager.getWritableDatabase();
//                ContentValues values=new ContentValues();
//                values.put("city_name","上海");
//                values.put("attraction_name","迪士尼");
//                db.insert("attractions",null,values);
//                mAttractionsDataManager.getWritableDatabase();
                Intent intent=new Intent(getActivity(),SearchDemo.class);
                startActivity(intent);
            }
        });


//        button_createdb.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
    }
}

