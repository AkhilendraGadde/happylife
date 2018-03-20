package com.tssquad.apps.happylife;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    int bColors[];
    ViewAdapter mAdapter;
    RecyclerView recyclerView;
    List<String> quoteString;
    private BottomSheetBehavior mBottomSheetBehavior;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View postReplySheet = findViewById(R.id.sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(postReplySheet);
        mBottomSheetBehavior.setHideable(true);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
        recyclerView = findViewById(R.id.quotesView);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        fab =  findViewById(R.id.add_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quotePost();
            }
        });



        mAdapter = new ViewAdapter();
        try {
            getQuotes();
        } catch (Exception e) {
            e.printStackTrace();
        }


        bColors = new int[]     {
                R.color.blue_200,
                R.color.pink_200,
                R.color.purple_200,
                R.color.deep_purple_200,
                R.color.indigo_200,
                R.color.blue_grey_200,
                R.color.light_green_200,
                R.color.brown_200
        };


    }

    private void quotePost() {

        final EditText editText = findViewById(R.id.post);

        final DatabaseHelper helper = new DatabaseHelper(getApplicationContext());


        findViewById(R.id.btn_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!TextUtils.isEmpty(editText.getText().toString()))  {

                    helper.insertTo(helper,editText.getText().toString());


                    try {
                        quoteString.addAll(0,helper.readAll(helper));
                    } catch (Exception E)   {
                        E.getMessage();
                    }
                    mAdapter.removeAll();
                    mAdapter.addAll(quoteString);

                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    fab.show();

                    editText.setText("");

                }
            }
        });



        if(mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            fab.hide();
        }
        else {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            fab.show();
        }

    }

    private void getQuotes() throws IOException, JSONException {


        quoteString = new ArrayList<>();

        StringBuilder buf = new StringBuilder();
        InputStream json = getAssets().open("quotes.json");
        BufferedReader in=
                new BufferedReader(new InputStreamReader(json, "UTF-8"));
        String str;

        while ((str=in.readLine()) != null) {
            buf.append(str);
        }

        in.close();

        JSONArray array = new JSONArray(new String(buf));

        for (int i = 0; i < array.length(); i++) {
            quoteString.add(String.valueOf(array.get(i)));
        }

        Collections.shuffle(quoteString);

        final DatabaseHelper helper = new DatabaseHelper(getApplicationContext());

        try {
            quoteString.addAll(0,helper.readAll(helper));
        } catch (Exception E)   {
            E.getMessage();
        }


        mAdapter.addAll(quoteString);
        recyclerView.setAdapter(mAdapter);

    }


    public class ViewAdapter extends RecyclerView.Adapter<Quotes> {

        private List<String> userModels;

        ViewAdapter() {
            this.userModels = new ArrayList<>();
            setHasStableIds(true);
        }

        @NonNull
        @Override
        public Quotes onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new Quotes(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_list, parent, false));
        }
        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public void onBindViewHolder(@NonNull Quotes holder, int position) {


            holder.setData(userModels.get(position));

            holder.mCard = holder.mView.findViewById(R.id.back);

            Drawable drawable[] = new Drawable[2];
            Resources res = getResources();
            drawable[0] = res.getDrawable(bColors[(int)(Math.random() * bColors.length)]);
            drawable[1] = res.getDrawable(bColors[(int)(Math.random() * bColors.length)]);
            TransitionDrawable transitionDrawable = new TransitionDrawable(drawable);
            holder.mCard.setBackground(transitionDrawable);
            transitionDrawable.startTransition(250);

        }

        @Override
        public int getItemCount() {
            return userModels.size();
        }

        void addAll(List<String> newUsers) {
            userModels.addAll(newUsers);
            notifyDataSetChanged();
        }

        void removeAll()    {
            userModels.clear();
            notifyDataSetChanged();
        }
    }

    class Quotes extends RecyclerView.ViewHolder    {

        View mView;
        CardView mCard;


        public Quotes(View itemView) {
            super(itemView);
            this.mView = itemView;
        }


        public void setData(String s) {

            TextView textView = mView.findViewById(R.id.quote);
            //textView.setText(" \" "+  s + " \"");
            textView.setText(s);
        }
    }


    @Override
    public void onBackPressed() {

        if(mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            fab.show();
        } else super.onBackPressed();
    }
}
