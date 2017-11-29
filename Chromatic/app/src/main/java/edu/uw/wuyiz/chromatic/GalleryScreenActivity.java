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

public class GalleryScreenActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PICK_IMAGE = 0;
    private static final String KEY_IMAGE_URI = "image_uri";

    private Uri imageUri;
    private String uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_screen);

        Intent intent = getIntent();
        uri = intent.getStringExtra("uri");
        //imageUri = Uri.parse(uri);

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

        final ArrayList<String> mUrlList = new ArrayList<>();

        mUrlList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510081122985&di=8bfc65adda0e868cb7700eadf8dcac71&imgtype=0&src=http%3A%2F%2Fpic.zhutou.com%2Fhtml%2FUploadPic%2F2010-6%2F2010664458474.jpg");
        if (uri != null) {
            mUrlList.add(uri);
        }

//        mUrlList.add("http://img1.imgtn.bdimg.com/it/u=963551012,3660149984&fm=214&gp=0.jpg");
//        mUrlList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510081122984&di=280211f10d59e3edf8e1221b8ccad564&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimage%2Fc0%253Dshijue1%252C0%252C0%252C294%252C40%2Fsign%3Df9871d34de0735fa85fd46faf63865c6%2Fe7cd7b899e510fb35dbbc8b8d333c895d1430c7a.jpg");
//        mUrlList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510081221368&di=48b177e870ba6161b4c055fe41fce56b&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimage%2Fc0%253Dshijue1%252C0%252C0%252C294%252C40%2Fsign%3D24a713ecb8119313d34ef7f30d5166a2%2Fb17eca8065380cd7e6c77fc3ab44ad34588281c7.jpg");
//        mUrlList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510081254731&di=9211817b5c8aa01e93fedd38e06311d7&imgtype=0&src=http%3A%2F%2Fimg1.3lian.com%2F2015%2Fa2%2F228%2Fd%2F134.jpg");
//        mUrlList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510081307059&di=b3168456428fcaf75f5209f8643aea8a&imgtype=0&src=http%3A%2F%2Fqiniu.usitrip.com%2Fimages%2Fckfinder%2Fimages%2Fchongwu_20150507.jpg");
//        mUrlList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510081351061&di=97662ce6fb6f44267766610c94489e35&imgtype=0&src=http%3A%2F%2Fimg.taopic.com%2Fuploads%2Fallimg%2F110914%2F8879-110914234S561.jpg");

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

        outState.putParcelable(KEY_IMAGE_URI, imageUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        Uri uri = savedInstanceState.getParcelable(KEY_IMAGE_URI);
        if (uri != null) {
            //onImagePicked(uri);
            Intent intent = new Intent(this, MoodPreviewActivity.class);
            intent.putExtra("uri", uri.toString());
            startActivity(intent);
        }
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

