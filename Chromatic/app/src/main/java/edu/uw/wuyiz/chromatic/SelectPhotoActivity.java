package edu.uw.wuyiz.chromatic;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lwj.widget.picturebrowser.PictureBrowser;

import java.util.ArrayList;
import java.util.List;

public class SelectPhotoActivity extends AppCompatActivity {

    private static final String KEY_IMAGE_URI = "image_uri";

    private static final int REQUEST_CODE_PICK_IMAGE = 1;

    private MyRecyclerAdapter2 adapter;

    private ArrayList<String> mUrlList;
    private ArrayList<String> checkedList;

    private List<Palette> mPalettes;
    private DatabaseReference mDatabase;

    private Boolean isChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_photo);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final String PALETTE_COLLECTION_STORAGE_KEY = getString(R.string.palette_collection_storage_key);

        mUrlList = new ArrayList<>();
        checkedList = new ArrayList<>();
        mPalettes = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(PALETTE_COLLECTION_STORAGE_KEY);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                mPalettes.clear();
//                Toast.makeText(MoodBoardGalleryScreenActivity.this, "a" + String.valueOf(snapshot.getChildrenCount()), Toast.LENGTH_SHORT).show();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
//                    MoodBoard mb = (HashMap) postSnapshot.getValue();
//                    Toast.makeText(MoodBoardGalleryScreenActivity.this, "a" + mb.moodBoardName, Toast.LENGTH_SHORT).show();


                    Palette palette = postSnapshot.getValue(Palette.class);
//                    Toast.makeText(SelectPhotoActivity.this, palette.name, Toast.LENGTH_SHORT).show();
                    mPalettes.add(palette);
                    mUrlList.add(palette.imageUri);
                }

//                Toast.makeText(MoodBoardGalleryScreenActivity.this, "a" + String.valueOf(mMoodBoards.size()), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

//        if (savedInstanceState == null) {
//            mUrlList = new ArrayList<>();
//
//            mUrlList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510081122985&di=8bfc65adda0e868cb7700eadf8dcac71&imgtype=0&src=http%3A%2F%2Fpic.zhutou.com%2Fhtml%2FUploadPic%2F2010-6%2F2010664458474.jpg");
//            mUrlList.add("http://img1.imgtn.bdimg.com/it/u=963551012,3660149984&fm=214&gp=0.jpg");
//            mUrlList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510081122984&di=280211f10d59e3edf8e1221b8ccad564&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimage%2Fc0%253Dshijue1%252C0%252C0%252C294%252C40%2Fsign%3Df9871d34de0735fa85fd46faf63865c6%2Fe7cd7b899e510fb35dbbc8b8d333c895d1430c7a.jpg");
//            mUrlList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510081221368&di=48b177e870ba6161b4c055fe41fce56b&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimage%2Fc0%253Dshijue1%252C0%252C0%252C294%252C40%2Fsign%3D24a713ecb8119313d34ef7f30d5166a2%2Fb17eca8065380cd7e6c77fc3ab44ad34588281c7.jpg");
//            mUrlList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510081254731&di=9211817b5c8aa01e93fedd38e06311d7&imgtype=0&src=http%3A%2F%2Fimg1.3lian.com%2F2015%2Fa2%2F228%2Fd%2F134.jpg");
//            mUrlList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510081307059&di=b3168456428fcaf75f5209f8643aea8a&imgtype=0&src=http%3A%2F%2Fqiniu.usitrip.com%2Fimages%2Fckfinder%2Fimages%2Fchongwu_20150507.jpg");
//            mUrlList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510081351061&di=97662ce6fb6f44267766610c94489e35&imgtype=0&src=http%3A%2F%2Fimg.taopic.com%2Fuploads%2Fallimg%2F110914%2F8879-110914234S561.jpg");
//            if (uri != null) {
//                mUrlList.add(uri);
//            }
//        } else {
//            mUrlList = savedInstanceState.getStringArrayList(KEY_IMAGE_URI);
//        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        adapter = new MyRecyclerAdapter2(mUrlList);
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(manager);

        recyclerView.setAdapter(adapter);

        //Toast.makeText(this, String.valueOf(checkedList.size()) + "a", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_select_photo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_select_done:
                //send image to create mood boards activity
                Intent imageIntent = new Intent(this, CreateMoodBoardActivity.class);
                imageIntent.putExtra("checkedList", checkedList);
                startActivity(imageIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_import_image:
//                onPickImage();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK) {
//            if () {
//                //onImagePicked(data.getData());
//                Intent intent = new Intent(this, MoodPreviewActivity.class);
//                intent.putExtra("uri", data.getData().toString());
//                startActivity(intent);
//            }
//        } else {
//            super.onActivityResult(requestCode, resultCode, data);
//        }
//    }

//    private void onPickImage() {
//
//        Intent pickImageIntent = new Intent(Intent.ACTION_GET_CONTENT);
//        pickImageIntent.setType("image/*");
//
//        Intent chooserIntent = Intent.createChooser(pickImageIntent,
//                getString(R.string.action_import_image));
//        startActivityForResult(chooserIntent, REQUEST_CODE_PICK_IMAGE);
//    }

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

    private void checkDiskPermission ()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDER) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "No Permissions" , Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
        }
        else
        {
            Toast.makeText(this, "Has Permissions" , Toast.LENGTH_LONG).show();
        }
    }

    class MyRecyclerAdapter2 extends RecyclerView.Adapter<SelectPhotoActivity.MyViewHolder> implements View.OnClickListener {

        private List<String> mUrlList;

        public MyRecyclerAdapter2(List<String> urlList) {
            mUrlList = urlList;
        }

        public List<String> getData() {
            return mUrlList;
        }

        @Override
        public SelectPhotoActivity.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.select_list, parent, false);
            view.setOnClickListener(this);
            return new SelectPhotoActivity.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final SelectPhotoActivity.MyViewHolder holder, final int position) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.MANAGE_DOCUMENTS}, 1);

            Glide.with(SelectPhotoActivity.this)
                    .load(Uri.parse(mUrlList.get(position)))
                    .into(holder.mTvPicture);
            //将position保存在itemView的Tag中，以便点击时进行获取
            holder.itemView.setTag(position);

            holder.mTvPicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!isChecked) {
                        checkedList.add(mUrlList.get(position));
                        holder.mIcon.setImageResource(R.mipmap.imageselector_select_checked);
                        isChecked = true;
                    } else {
                        holder.mIcon.setImageResource(R.mipmap.imageselector_select_uncheck);
                        isChecked = false;
                        checkedList.remove(mUrlList.get(position));
                    }

                }
            });


        }

        @Override
        public int getItemCount() {
            return mUrlList.size();
//            return 2;
        }
        private SelectPhotoActivity.OnItemClickListener mOnItemClickListener = null;


        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v,(int)v.getTag());
            }
        }

        public void setOnItemClickListener(SelectPhotoActivity.OnItemClickListener listener) {
            this.mOnItemClickListener = listener;
        }
    }

    //define interface
    public  interface OnItemClickListener {
        void onItemClick(View view , int position);
    }
    class MyViewHolder extends RecyclerView.ViewHolder {


        public final ImageView mTvPicture;
        public final ImageView mIcon;

        public MyViewHolder(View itemView) {
            super(itemView);
            mTvPicture = (ImageView) itemView.findViewById(R.id.iv_picture_item);
            mIcon = (ImageView) itemView.findViewById(R.id.photo_check);
        }
    }

}
