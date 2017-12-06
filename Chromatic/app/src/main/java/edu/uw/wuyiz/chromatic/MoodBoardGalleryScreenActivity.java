package edu.uw.wuyiz.chromatic;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MoodBoardGalleryScreenActivity extends AppCompatActivity {

    private static final String KEY_IMAGE_URI = "image_uri";

    private List<MoodBoard> mMoodBoards;
    private List<Bitmap> mBitmaps;
    private DatabaseReference mDatabase;

    private String uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_board_gallery_screen);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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
                                Intent galleryIntent = new Intent(getApplicationContext(), PaletteGalleryScreenActivity.class);
                                startActivity(galleryIntent);
                            case R.id.action_creations:
//                                Intent creationsIntent = new Intent(getApplicationContext(), MoodBoardGalleryScreenActivity.class);
//                                startActivity(creationsIntent);
                                return true;

                        }
                        return true;
                    }
                });

        final String MOOD_BOARD_COLLECTION_STORAGE_KEY = getString(R.string.mood_board_collection_storage_key);

        mMoodBoards = new ArrayList<>();
        mBitmaps = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(MOOD_BOARD_COLLECTION_STORAGE_KEY);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                mMoodBoards.clear();
                mBitmaps.clear();
//                Toast.makeText(MoodBoardGalleryScreenActivity.this, "a" + String.valueOf(snapshot.getChildrenCount()), Toast.LENGTH_SHORT).show();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
//                    MoodBoard mb = (HashMap) postSnapshot.getValue();
//                    Toast.makeText(MoodBoardGalleryScreenActivity.this, "a" + mb.moodBoardName, Toast.LENGTH_SHORT).show();
//                    Toast.makeText(MoodBoardGalleryScreenActivity.this, "a" + moodBoard.moodBoardName, Toast.LENGTH_SHORT).show();

                    MoodBoard moodBoard = postSnapshot.getValue(MoodBoard.class);
                    mMoodBoards.add(moodBoard);
                    mBitmaps.add(stringToBitmap(moodBoard.getMoodBoardBitmapStr()));
                }

//                Toast.makeText(MoodBoardGalleryScreenActivity.this, "a" + String.valueOf(mMoodBoards.size()), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

//        mDatabase.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
//
//                MoodBoard moodBoard = dataSnapshot.getValue(MoodBoard.class);
//                moodboardList.add(moodBoard);
////                mBitmaps.add(moodBoard.getMoodBoardBitmap());
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//                MoodBoard moodBoard = dataSnapshot.getValue(MoodBoard.class);
//                moodboardList.remove(moodBoard);
////                mBitmaps.remove(moodBoard.getMoodBoardBitmap());
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
//                MoodBoard moodBoard = dataSnapshot.getValue(MoodBoard.class);
//                moodboardList.add(moodBoard);
////                mBitmaps.add(moodBoard.getMoodBoardBitmap());
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        });



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


        final MoodBoardGalleryScreenActivity.MyRecyclerAdapter2 adapter = new MoodBoardGalleryScreenActivity.MyRecyclerAdapter2(mBitmaps);
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(manager);

//        MoodBoardGalleryScreenActivity.SpacesItemDecoration decoration = new MoodBoardGalleryScreenActivity.SpacesItemDecoration(8);
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
//        adapter.setOnItemClickListener(new PaletteGalleryScreenActivity.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//
//                ArrayList<String> data = adapter.getData();
//
//                Log.e("onItemClick","onItemClick"+position+data.size());
//
//                PictureBrowser.Builder builder = new PictureBrowser.Builder();
//                builder.setFragmentManager(getSupportFragmentManager())
//                        .setUrlList(data)
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

    /**
     * @param encodedString
     * @return bitmap (from given string)
     */
    private Bitmap stringToBitmap(String encodedString) {
        try {
            byte[] encodedByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodedByte, 0, encodedByte.length);

            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//        //outState.putParcelable(KEY_IMAGE_URI, imageUri);
//        //outState.putStringArrayList(KEY_IMAGE_URI, mUrlList);
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//
//        //mUrlList = savedInstanceState.getStringArrayList(KEY_IMAGE_URI);
//    }
//

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

    class MyRecyclerAdapter2 extends RecyclerView.Adapter<MoodBoardGalleryScreenActivity.MyViewHolder> implements View.OnClickListener {

        private List<Bitmap> mBitmaps;

        public MyRecyclerAdapter2(List<Bitmap> bitmaps) {
            mBitmaps = bitmaps;
        }

        public List<Bitmap> getData() {

            return mBitmaps;
        }

        @Override
        public MoodBoardGalleryScreenActivity.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.creations_list, parent, false);
            view.setOnClickListener(this);
            return new MoodBoardGalleryScreenActivity.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MoodBoardGalleryScreenActivity.MyViewHolder holder, final int position) {
            Glide.with(MoodBoardGalleryScreenActivity.this)
                    .load(mBitmaps.get(position))
                    .into(holder.mTvPicture);
            holder.itemView.setTag(position);
        }

        @Override
        public int getItemCount() {
            return mBitmaps.size();
        }

        private PaletteGalleryScreenActivity.OnItemClickListener mOnItemClickListener = null;


        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, (int) v.getTag());
            }
        }

        public void setOnItemClickListener(PaletteGalleryScreenActivity.OnItemClickListener listener) {
            this.mOnItemClickListener = listener;
        }
    }

    //
//    //define interface
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
