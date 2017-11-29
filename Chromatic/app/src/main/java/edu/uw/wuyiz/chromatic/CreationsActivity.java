package edu.uw.wuyiz.chromatic;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.lwj.widget.picturebrowser.PictureBrowser;
import com.lwj.widget.picturebrowser.PictureFragment;
import com.lwj.widget.picturebrowser.PictureLoader;

import java.util.ArrayList;

public class CreationsActivity extends AppCompatActivity {

    private static final String KEY_IMAGE_URI = "image_uri";
    private static final int REQUEST_CODE_PICK_IMAGE = 0;

    private ArrayList<String> mUrlList;

    private String uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creations);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_moodboards:
                                Intent moodBoardsIntent = new Intent(getApplicationContext(), CreateMoodBoardActivity.class);
                                startActivity(moodBoardsIntent);
                            case R.id.action_gallery:
                                Intent galleryIntent = new Intent(getApplicationContext(), GalleryScreenActivity.class);
                                startActivity(galleryIntent);
                            case R.id.action_creations:
//                                Intent creationsIntent = new Intent(getApplicationContext(), CreationsActivity.class);
//                                startActivity(creationsIntent);
                                return true;

                        }
                        return true;
                    }
                });

        if (savedInstanceState == null) {
            mUrlList = new ArrayList<>();

            mUrlList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510081122985&di=8bfc65adda0e868cb7700eadf8dcac71&imgtype=0&src=http%3A%2F%2Fpic.zhutou.com%2Fhtml%2FUploadPic%2F2010-6%2F2010664458474.jpg");
            if (uri != null) {
                mUrlList.add(uri);
            }
        } else {
            mUrlList = savedInstanceState.getStringArrayList(KEY_IMAGE_URI);
        }



        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);


        final CreationsActivity.MyRecyclerAdapter2 adapter = new CreationsActivity.MyRecyclerAdapter2(mUrlList);
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(manager);

        CreationsActivity.SpacesItemDecoration decoration = new CreationsActivity.SpacesItemDecoration(8);
        recyclerView.addItemDecoration(decoration);
        final PictureLoader pictureLoader = new PictureLoader() {
            @Override
            public void showPicture(PictureFragment fragment, PhotoView photoView, String pictureUrl) {

                Glide.with(fragment)
                        .load(pictureUrl)
                        .into(photoView);
            }
        };
        adapter.setOnItemClickListener(new GalleryScreenActivity.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                ArrayList<String> data = adapter.getData();

                Log.e("onItemClick","onItemClick"+position+data.size());

                PictureBrowser.Builder builder = new PictureBrowser.Builder();
                builder.setFragmentManager(getSupportFragmentManager())
                        .setUrlList(data)
                        .setStartIndex(position)
                        .initPictureLoader(pictureLoader)
                        .setShowDeleteIcon(true)
                        .setShowIndexHint(true)
                        .setCancelOutside(true)
                        .create()
                        .show();



            }
        });

        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //outState.putParcelable(KEY_IMAGE_URI, imageUri);
        outState.putStringArrayList(KEY_IMAGE_URI, mUrlList);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mUrlList = savedInstanceState.getStringArrayList(KEY_IMAGE_URI);
    }

    class SpacesItemDecoration extends RecyclerView.ItemDecoration {

        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;
//        if(parent.getChildAdapterPosition(view)==0){
            outRect.top = space;
//        }
        }
    }
    class MyRecyclerAdapter2 extends RecyclerView.Adapter<CreationsActivity.MyViewHolder> implements View.OnClickListener {

        private ArrayList<String> mUrlList;

        public MyRecyclerAdapter2(ArrayList<String> urlList) {
            mUrlList = urlList;
        }
        public ArrayList<String> getData()
        {

            return mUrlList;
        }

        @Override
        public CreationsActivity.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.creations_list, parent, false);
            view.setOnClickListener(this);
            return new CreationsActivity.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(CreationsActivity.MyViewHolder holder, final int position) {
            Glide.with(CreationsActivity.this)
                    .load(mUrlList.get(position))
                    .into(holder.mTvPicture);
            //将position保存在itemView的Tag中，以便点击时进行获取
            holder.itemView.setTag(position);
        }

        @Override
        public int getItemCount() {
            return mUrlList.size();
        }
        private GalleryScreenActivity.OnItemClickListener mOnItemClickListener = null;


        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v,(int)v.getTag());
            }
        }

        public void setOnItemClickListener(GalleryScreenActivity.OnItemClickListener listener) {
            this.mOnItemClickListener = listener;
        }
    }

    //define interface
    public  interface OnItemClickListener {
        void onItemClick(View view , int position);
    }
    class MyViewHolder extends RecyclerView.ViewHolder {


        public final ImageView mTvPicture;

        public MyViewHolder(View itemView) {
            super(itemView);
            mTvPicture = (ImageView) itemView.findViewById(R.id.iv_picture_item);
        }
    }

}
