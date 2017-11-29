package edu.uw.wuyiz.chromatic;

import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import java.util.List;

public class GalleryScreenActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PICK_IMAGE = 0;
    private static final String KEY_IMAGE_URI = "image_uri";

    private Uri imageUri;
    private String uri;

    private ArrayList<String> mUrlList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_screen);

        Intent intent = getIntent();
        uri = intent.getStringExtra("uri");

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
//                                Intent galleryIntent = new Intent(getApplicationContext(), GalleryScreenActivity.class);
//                                startActivity(galleryIntent);
                                return true;
                            case R.id.action_creations:
                                Intent creationsIntent = new Intent(getApplicationContext(), CreationsActivity.class);
                                startActivity(creationsIntent);

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


        final MyRecyclerAdapter2 adapter = new MyRecyclerAdapter2(mUrlList);
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(manager);

        SpacesItemDecoration decoration = new SpacesItemDecoration(8);
        recyclerView.addItemDecoration(decoration);
        final PictureLoader pictureLoader = new PictureLoader() {
            @Override
            public void showPicture(PictureFragment fragment, PhotoView photoView, String pictureUrl) {

                Glide.with(fragment)
                        .load(pictureUrl)
                        .into(photoView);
            }
        };
        adapter.setOnItemClickListener(new OnItemClickListener() {
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

        //Uri uri = savedInstanceState.getParcelable(KEY_IMAGE_URI);
//        if (uri != null) {
//            Intent intent = new Intent(this, MoodPreviewActivity.class);
//            intent.putExtra("uri", uri.toString());
//            startActivity(intent);
//        }
        mUrlList = savedInstanceState.getStringArrayList(KEY_IMAGE_URI);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_import_image:
                onPickImage();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICK_IMAGE) {
            if (resultCode == RESULT_OK) {
                //onImagePicked(data.getData());
                Intent intent = new Intent(this, MoodPreviewActivity.class);
                intent.putExtra("uri", data.getData().toString());
                startActivity(intent);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void onPickImage() {

        Intent pickImageIntent = new Intent(Intent.ACTION_GET_CONTENT);
        pickImageIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(pickImageIntent,
                getString(R.string.action_import_image));
        startActivityForResult(chooserIntent, REQUEST_CODE_PICK_IMAGE);
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
    class MyRecyclerAdapter2 extends RecyclerView.Adapter<MyViewHolder> implements View.OnClickListener {

        private ArrayList<String> mUrlList;

        public MyRecyclerAdapter2(ArrayList<String> urlList) {
            mUrlList = urlList;
        }
        public ArrayList<String> getData()
        {

            return mUrlList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.gallery_list, parent, false);
            //将创建的View注册点击事件
            view.setOnClickListener(this);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            Glide.with(GalleryScreenActivity.this)
                    .load(mUrlList.get(position))
                    .into(holder.mTvPicture);
            //将position保存在itemView的Tag中，以便点击时进行获取
            holder.itemView.setTag(position);
        }

        @Override
        public int getItemCount() {
            return mUrlList.size();
        }
        private OnItemClickListener mOnItemClickListener = null;


        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v,(int)v.getTag());
            }
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
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

