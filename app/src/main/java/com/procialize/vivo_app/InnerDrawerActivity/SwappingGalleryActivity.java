package com.procialize.vivo_app.InnerDrawerActivity;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;

import com.procialize.vivo_app.Adapter.SwipeImageAdapter;
import com.procialize.vivo_app.Adapter.SwipepagerAdapter;
import com.procialize.vivo_app.GetterSetter.FirstLevelFilter;
import com.procialize.vivo_app.R;
import com.procialize.vivo_app.Utility.Util;

import java.util.List;

import cn.jzvd.JZVideoPlayer;

public class SwappingGalleryActivity extends AppCompatActivity implements SwipeImageAdapter.SwipeImageAdapterListner{

    String name;
    List<FirstLevelFilter> firstLevelFilters;
    SwipeImageAdapter swipeImageAdapter;
    SwipepagerAdapter swipepagerAdapter;
    RecyclerView recyclerView;
    ViewPager pager;
    ImageView right,left,backIv;
    ImageView headerlogoIv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swapping_gallery);

        name = getIntent().getExtras().getString("url");
        firstLevelFilters = (List<FirstLevelFilter>) getIntent().getExtras().getSerializable("gallerylist");

        recyclerView = findViewById(R.id.listrecycler);
        pager = findViewById(R.id.pager);
        right = findViewById(R.id.right);
        left = findViewById(R.id.left);
        backIv = findViewById(R.id.backIv);

        headerlogoIv = findViewById(R.id.headerlogoIv);
        Util.logomethod(this,headerlogoIv);


        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        int resId = R.anim.layout_animation_slide_right;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, resId);
        recyclerView.setLayoutAnimation(animation);

        swipeImageAdapter = new SwipeImageAdapter(this,firstLevelFilters,this);
        recyclerView.setAdapter(swipeImageAdapter);
        swipeImageAdapter.notifyDataSetChanged();



        swipepagerAdapter = new SwipepagerAdapter(this,firstLevelFilters);
        pager.setAdapter(swipepagerAdapter);
        swipepagerAdapter.notifyDataSetChanged();

        indexset(name);

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{

                    int position = pager.getCurrentItem() +1;

                    if (position<=firstLevelFilters.size())
                    {
                        pager.setCurrentItem(position);
                    }

                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        });

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{

                    int position = pager.getCurrentItem() -1;

                    if (position>=0)
                    {
                        pager.setCurrentItem(position);
                    }

                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void onContactSelected(FirstLevelFilter filtergallerylists) {

        indexset(filtergallerylists.getFileName());
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void indexset(String name)
    {
        for (int j=0;j<firstLevelFilters.size();j++)
        {
            if (firstLevelFilters.get(j).getFileName().equalsIgnoreCase(name))
            {
                pager.setCurrentItem(j);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        JZVideoPlayer.releaseAllVideos();

    }
}
