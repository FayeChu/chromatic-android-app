package edu.uw.wuyiz.chromatic;

import android.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import android.Manifest;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lwj.widget.picturebrowser.PictureBrowser;
import com.lwj.widget.picturebrowser.PictureFragment;
import com.lwj.widget.picturebrowser.PictureLoader;

import java.util.ArrayList;
import java.util.List;

public class PaletteGalleryScreenActivity extends AppCompatActivity {

    private static final String KEY_IMAGE_URI = "image_uri";

    private static final int REQUEST_CODE_PICK_IMAGE = 0;

    private PaletteGalleryRecyclerAdapter adapter;
    private ArrayList<String> mUrlList;
    private List<Palette> mPalettes;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palette_gallery_screen);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_new_mood_board:
                                Intent selectPhotoIntent = new Intent(getApplicationContext(), SelectPhotoActivity.class);
                                startActivity(selectPhotoIntent);
//                                return true;
                            case R.id.action_palette_gallery:
//                                Intent galleryIntent = new Intent(getApplicationContext(), PaletteGalleryScreenActivity.class);
//                                startActivity(galleryIntent);
                                return true;
                            case R.id.action_mood_board_gallery:
                                Intent creationsIntent = new Intent(getApplicationContext(), MoodBoardGalleryScreenActivity.class);
                                startActivity(creationsIntent);
//                                return true;
                        }
                        return true;
                    }
                });

        if (savedInstanceState == null) {
            final String PALETTE_COLLECTION_STORAGE_KEY = getString(R.string.palette_collection_storage_key);
            mUrlList = new ArrayList<>();
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

        } else {
            mUrlList = savedInstanceState.getStringArrayList(KEY_IMAGE_URI);
        }

        final String PALETTE_COLLECTION_STORAGE_KEY = getString(R.string.palette_collection_storage_key);

        mUrlList = new ArrayList<>();
        mPalettes = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(PALETTE_COLLECTION_STORAGE_KEY);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
//                mUrlList2.clear();
//                mPalettes.clear();
//                Toast.makeText(MoodBoardGalleryScreenActivity.this, "a" + String.valueOf(snapshot.getChildrenCount()), Toast.LENGTH_SHORT).show();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
//                    MoodBoard mb = (HashMap) postSnapshot.getValue();
//                    Toast.makeText(MoodBoardGalleryScreenActivity.this, "a" + mb.moodBoardName, Toast.LENGTH_SHORT).show();

                    Palette palette = postSnapshot.getValue(Palette.class);
//                    Toast.makeText(SelectPhotoActivity.this, palette.name, Toast.LENGTH_SHORT).show();
                    mUrlList.add(palette.imageUri);
                    mPalettes.add(palette);

                }

//                Toast.makeText(PaletteGalleryScreenActivity.this, "a" + String.valueOf(mUrlList2.size()), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        adapter = new PaletteGalleryRecyclerAdapter(mUrlList);
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(manager);

//        SpacesItemDecoration decoration = new SpacesItemDecoration(8);
//        recyclerView.addItemDecoration(decoration);
//        final PictureLoader pictureLoader = new PictureLoader() {
//            @Override
//            public void showPicture(PictureFragment fragment, PhotoView photoView, String pictureUrl) {
//
//                Glide.with(fragment)
//                        .load(pictureUrl)
//                        .into(photoView);
//            }
//        };
//
//        adapter.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//
//                List<String> data = adapter.getData();
//
//                Log.e("onItemClick","onItemClick"+position+data.size());
//
//                PictureBrowser.Builder builder = new PictureBrowser.Builder();
//                builder.setFragmentManager(getSupportFragmentManager())
//                        .setUrlList((ArrayList<String>) data)
//                        .setStartIndex(position)
//                        .initPictureLoader(pictureLoader)
//                        .setShowDeleteIcon(true)
//                        .setShowIndexHint(true)
//                        .setCancelOutside(true)
//                        .create()
//                        .show();
//            }
//        });

        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
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
//        if (requestCode == REQUEST_CODE_PICK_IMAGE) {
//            if (resultCode == RESULT_OK) {
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

    class PaletteGallerySpacesItemDecoration extends RecyclerView.ItemDecoration {

        private int space;

        public PaletteGallerySpacesItemDecoration(int space) {
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

    private void checkPermission () {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.MANAGE_DOCUMENTS) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "No Permission" , Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.MANAGE_DOCUMENTS, Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        } else {
            Toast.makeText(this, "Has Permission" , Toast.LENGTH_SHORT).show();
        }
    }

    class PaletteGalleryRecyclerAdapter extends RecyclerView.Adapter<PaletteGalleryScreenActivity.MyViewHolder> implements View.OnClickListener {

        private List<String> mUrlList;

        public PaletteGalleryRecyclerAdapter(List<String> urlList) {
            mUrlList = urlList;
        }

        public List<String> getData() {
            return mUrlList;
        }

        @Override
        public PaletteGalleryScreenActivity.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.gallery_list, parent, false);
            view.setOnClickListener(this);
            return new PaletteGalleryScreenActivity.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final PaletteGalleryScreenActivity.MyViewHolder holder, final int position) {
            checkPermission();

            Glide.with(PaletteGalleryScreenActivity.this)
                    .load(Uri.parse(mUrlList.get(position)))
                    .into(holder.mTvPicture);
            holder.itemView.setTag(position);
        }

        @Override
        public int getItemCount() {
            return mUrlList.size();
        }

        private PaletteGalleryScreenActivity.OnItemClickListener mOnItemClickListener = null;

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, (int)v.getTag());
            }
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.mOnItemClickListener = listener;
        }
    }

    // define interface
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public final ImageView mTvPicture;

        public MyViewHolder(View itemView) {
            super(itemView);
            mTvPicture = (ImageView) itemView.findViewById(R.id.iv_picture_item);
        }
    }
}