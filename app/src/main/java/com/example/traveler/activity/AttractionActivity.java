package com.example.traveler.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.traveler.R;
import com.example.traveler.pojo.Attraction;
import com.example.traveler.pojo.Comment;
import com.example.traveler.pojo.User;

import java.util.ArrayList;
import java.util.List;

import static com.jaeger.library.StatusBarUtil.setTranslucentForCoordinatorLayout;

public class AttractionActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView imageView;
    private ImageView imageStar;
    private ImageView map_btn;
    private ImageView comment_btn;
    private BaseQuickAdapter<Comment, BaseViewHolder> adapter;
    private RecyclerView recyclerView;
    private Attraction a;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attraction);
        initView();
        initCommentRecycler();
    }

    private void initView(){
        imageView = findViewById(R.id.attraction_img);
        //StatusBarUtil.setTranslucentForImageView(AttractionActivity.this, 0, imageView);
        //StatusBarUtil.setTransparent(AttractionActivity.this);
        setTranslucentForCoordinatorLayout(AttractionActivity.this, 0);
        imageStar = findViewById(R.id.attraction_star);
        imageStar.setOnClickListener(this);
        map_btn = findViewById(R.id.attraction_map);
        map_btn.setOnClickListener(this);
        comment_btn = findViewById(R.id.attraction_comment);
        comment_btn.setOnClickListener(this);

        //接收数据
        a = (Attraction) getIntent().getSerializableExtra("attraction");
        initAttractionData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.attraction_star:
                break;

            case R.id.attraction_comment:
                ReplyDialogFragment replyDialogFragment = new ReplyDialogFragment();
                replyDialogFragment.show(getFragmentManager(),"comment");
                break;
            case R.id.attraction_map:
                break;
        }
    }

    private void initAttractionData() {
        TextView txt_name = findViewById(R.id.attraction_name);
        TextView txt_about = findViewById(R.id.attraction_about);
        TextView txt_address = findViewById(R.id.attraction_address);
        ImageView img = findViewById(R.id.attraction_img);
        txt_name.setText(a.getAttr_name());
        txt_about.setText(a.getIntroduction());
        txt_address.setText(a.getStatus());
        Glide.with(this).load(a.getImageurl()).into(img);
    }


    private void initCommentRecycler() {
        List<Comment> list = new ArrayList<>();
        list.add(new Comment((long)1,new User((long) 1,"aaa"),"很好玩，很好看"));
        list.add(new Comment((long)2,new User((long) 2,"bbb"),"很好玩，很好看啊"));
        list.add(new Comment((long)3,new User((long) 3,"ccc"),"很好玩，很好看"));
        list.add(new Comment((long)3,new User((long) 3,"ccc"),"很好玩，很好看"));
        list.add(new Comment((long)3,new User((long) 3,"ccc"),"很好玩，很好看"));
        list.add(new Comment((long)3,new User((long) 3,"ccc"),"很好玩，很好看"));
        list.add(new Comment((long)3,new User((long) 3,"ccc"),"很好玩，很好看"));
        list.add(new Comment((long)3,new User((long) 3,"ccc"),"很好玩，很好看"));
        recyclerView = findViewById(R.id.attraction_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new BaseQuickAdapter<Comment, BaseViewHolder>(R.layout.recycler_comment, new ArrayList<Comment>()) {
            @Override
            protected void convert(BaseViewHolder helper, Comment item) {
                helper.setText(R.id.item_comment_tv_userName,item.getPublisher().getName())
                        .setText(R.id.item_comment_tv_content,item.getContent());

            }
        };
        adapter.setNewData(list);
        recyclerView.setAdapter(adapter);

    }
}
