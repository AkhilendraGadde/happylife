package com.tssquad.apps.happylife;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    int bColors[];
    ViewAdapter mAdapter;
    RecyclerView recyclerView;
    List<String> quoteString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = findViewById(R.id.quotesView);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

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

    private void getQuotes() throws IOException, JSONException {


        quoteString = new ArrayList<>();


        //quoteString.add("People try to say suicide is the most cowardly act a man could ever commit. I don’t think that’s true at all. What’s cowardly is treating a man so badly that he wants to commit suicide.");


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
}
