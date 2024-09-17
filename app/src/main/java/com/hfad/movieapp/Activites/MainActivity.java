package com.hfad.movieapp.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hfad.movieapp.Adapters.CategoryListAdapter;
import com.hfad.movieapp.Adapters.FilmListAdapter;
import com.hfad.movieapp.Adapters.SliderAdapter;
import com.hfad.movieapp.Domain.GenresItem;
import com.hfad.movieapp.Domain.ListFilm;
import com.hfad.movieapp.Domain.SliderItems;
import com.hfad.movieapp.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapterBestMovies,adapterUpComing,adapterCategory;
    private RecyclerView recyclerViewBestMovies,recyclerViewUpcoming,recyclerViewCategory;
 private RequestQueue nRequestQueue;
 private StringRequest nStringRequest,nStringRequest2,nStringRequest3;
 private ProgressBar loading1, loading2, loading3;
    private ViewPager2 viewPager2;
    private Handler slideHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initView();
        banners();
        sendRequestBestMovies();
        sendRequestUpComing();
        sendRequestUpCategory();
    }

    private void sendRequestBestMovies() {
        nRequestQueue= Volley.newRequestQueue(this);
        loading1.setVisibility(View.VISIBLE);
        nStringRequest=new StringRequest(Request.Method.GET, "https://moviesapi.ir/api/v1/movies?page=1", response -> {

            Gson gson=new Gson();
            loading1.setVisibility(View.GONE);
            ListFilm items=gson.fromJson(response,ListFilm.class);
            adapterBestMovies=new FilmListAdapter(items);
            recyclerViewBestMovies.setAdapter(adapterBestMovies);
        }, error -> {

            loading1.setVisibility(View.GONE);
            Log.i("UiLover","onErrorResponse:"+error.toString());
        });
        nRequestQueue.add(nStringRequest);
    }

    private void sendRequestUpComing() {
        nRequestQueue= Volley.newRequestQueue(this);
        loading3.setVisibility(View.VISIBLE);
        nStringRequest3=new StringRequest(Request.Method.GET, "https://moviesapi.ir/api/v1/movies?page=2", response -> {

            Gson gson=new Gson();
            loading3.setVisibility(View.GONE);
            ListFilm items=gson.fromJson(response,ListFilm.class);
            adapterUpComing=new FilmListAdapter(items);
            recyclerViewUpcoming.setAdapter(adapterUpComing);
        }, error -> {

            loading3.setVisibility(View.GONE);
            Log.i("UiLover","onErrorResponse:"+error.toString());
        });
        nRequestQueue.add(nStringRequest3);
    }

    private void sendRequestUpCategory() {
        nRequestQueue= Volley.newRequestQueue(this);
        loading2.setVisibility(View.VISIBLE);
        nStringRequest2=new StringRequest(Request.Method.GET, "https://moviesapi.ir/api/v1/genres", response -> {

            Gson gson=new Gson();
            loading2.setVisibility(View.GONE);
           ArrayList<GenresItem> catList=gson.fromJson(response,new TypeToken<ArrayList<GenresItem>>(){
           }.getType());
            adapterCategory=new CategoryListAdapter(catList);
            recyclerViewCategory.setAdapter(adapterCategory);
        }, error -> {

            loading2.setVisibility(View.GONE);
            Log.i("UiLover","onErrorResponse:"+error.toString());
        });
        nRequestQueue.add(nStringRequest2);
    }

    private void banners() {
        List<SliderItems> sliderItems=new ArrayList<>();
        sliderItems.add(new SliderItems(R.drawable.wide1));
        sliderItems.add(new SliderItems(R.drawable.net));
        sliderItems.add(new SliderItems(R.drawable.img));

        viewPager2.setAdapter(new SliderAdapter(sliderItems,viewPager2));
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer=new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer((page, position) -> {
            float r=1-Math.abs(position);
            page.setScaleY(0.85f+r+0.15f);
        });
        viewPager2.setPageTransformer(compositePageTransformer);
        viewPager2.setCurrentItem(1);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                slideHandler.removeCallbacks(slideRunner);
            }
        });

    }
    private final Runnable slideRunner=new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem()+1);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        slideHandler.postDelayed(slideRunner,2000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        slideHandler.removeCallbacks(slideRunner);
    }

    private void initView() {
        viewPager2 = findViewById(R.id.viewPagerSlider);

        recyclerViewBestMovies = findViewById(R.id.view1);
        recyclerViewBestMovies.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewUpcoming.findViewById(R.id.View3);
        recyclerViewUpcoming.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewCategory = findViewById(R.id.view2);
        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        loading1 = findViewById(R.id.progressBar1);
        loading2 = findViewById(R.id.progressBar2);
        loading3 = findViewById(R.id.progressBar3);


    }
}