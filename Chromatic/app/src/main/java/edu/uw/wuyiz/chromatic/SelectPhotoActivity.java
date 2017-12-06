package edu.uw.wuyiz.chromatic;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.Manifest;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class SelectPhotoActivity extends AppCompatActivity {

    private static final String KEY_IMAGE_URI = "image_uri";

    private static final int REQUEST_CODE_PICK_IMAGE = 1;

    private SelectPhotoRecyclerAdapter adapter;
    private ArrayList<String> mUrlList;
    private ArrayList<String> mColorOne;
    private ArrayList<String> mColorTwo;
    private ArrayList<String> mColorThree;
    private ArrayList<String> mColorFour;
    private ArrayList<String> mColorFive;

    private ArrayList<String> checkedList;
    private List<Palette> mPalettes;
    private DatabaseReference mDatabase;
    private Boolean isChecked = false;
    private int width;
    private int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_photo);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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

        final String PALETTE_COLLECTION_STORAGE_KEY = getString(R.string.palette_collection_storage_key);

        width = 190;
        height = 190;

        mUrlList = new ArrayList<>();
        mColorOne = new ArrayList<>();
        mColorTwo = new ArrayList<>();
        mColorThree = new ArrayList<>();
        mColorFour = new ArrayList<>();
        mColorFive = new ArrayList<>();
        checkedList = new ArrayList<>();
        mPalettes = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance().getReference().child(PALETTE_COLLECTION_STORAGE_KEY);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                mUrlList.clear();
                mColorOne.clear();
                mColorTwo.clear();
                mColorThree.clear();
                mColorFour.clear();
                mColorFive.clear();
                checkedList.clear();
                mPalettes.clear();
//                Toast.makeText(MoodBoardGalleryScreenActivity.this, "a" + String.valueOf(snapshot.getChildrenCount()), Toast.LENGTH_SHORT).show();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
//                    MoodBoard mb = (HashMap) postSnapshot.getValue();
//                    Toast.makeText(MoodBoardGalleryScreenActivity.this, "a" + mb.moodBoardName, Toast.LENGTH_SHORT).show();

                    Palette palette = postSnapshot.getValue(Palette.class);
//                    Toast.makeText(SelectPhotoActivity.this, palette.name, Toast.LENGTH_SHORT).show();
                    mUrlList.add(palette.imageUri);
                    mPalettes.add(palette);
//                    mColorOne.add(String.format("#%06X", (0xFFFFFF & palette.colorOne)));

                    checkPermission();
                    Bitmap colorOne = createBitmap(width, height, palette.colorOne);
                    Bitmap colorTwo = createBitmap(width, height, palette.colorTwo);
                    Bitmap colorThree = createBitmap(width, height, palette.colorThree);
                    Bitmap colorFour = createBitmap(width, height, palette.colorFour);
                    Bitmap colorFive = createBitmap(width, height, palette.colorFive);

                    Uri colorOneUri = getImageUri(getApplicationContext(), colorOne);
                    Uri colorTwoUri = getImageUri(getApplicationContext(), colorTwo);
                    Uri colorThreeUri = getImageUri(getApplicationContext(), colorThree);
                    Uri colorFourUri = getImageUri(getApplicationContext(), colorFour);
                    Uri colorFiveUri = getImageUri(getApplicationContext(), colorFive);


                    mColorOne.add(colorOneUri.toString());
                    mColorTwo.add(colorTwoUri.toString());
                    mColorThree.add(colorThreeUri.toString());
                    mColorFour.add(colorFourUri.toString());
                    mColorFive.add(colorFiveUri.toString());
                }

//                Toast.makeText(MoodBoardGalleryScreenActivity.this, "a" + String.valueOf(mMoodBoards.size()), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        adapter = new SelectPhotoRecyclerAdapter(mUrlList, mColorOne, mColorTwo, mColorThree, mColorFour, mColorFive);
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(manager);

        recyclerView.setAdapter(adapter);

        //Toast.makeText(this, String.valueOf(checkedList.size()) + "a", Toast.LENGTH_SHORT).show();
    }

    private Bitmap createBitmap(int w, int h, int color) {

        Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
        Bitmap bmp = Bitmap.createBitmap(w, h, conf); // this creates a MUTABLE bitmap
        Canvas canvas = new Canvas(bmp);

        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);

        Paint rect_paint = new Paint();
        rect_paint.setStyle(Paint.Style.FILL);
        rect_paint.setColor(Color.rgb(r, g, b));
        canvas.drawRect(0, 0, w, h, rect_paint);

        return bmp;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        checkPermission();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
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
                Intent intent = new Intent(SelectPhotoActivity.this, CreateMoodBoardActivity.class);
                intent.putExtra("checkedList", checkedList);
                startActivity(intent);
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

    class SelectPhotoSpacesItemDecoration extends RecyclerView.ItemDecoration {

        private int space;

        public SelectPhotoSpacesItemDecoration(int space) {
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

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.MANAGE_DOCUMENTS) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(this, "No Permission" , Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.MANAGE_DOCUMENTS,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        } else {
//            Toast.makeText(this, "Has Permission" , Toast.LENGTH_SHORT).show();
        }
    }

    class SelectPhotoRecyclerAdapter extends RecyclerView.Adapter<SelectPhotoActivity.MyViewHolder> implements View.OnClickListener {

        private List<String> mUrlList;
        private List<String> mColorOne;
        private List<String> mColorTwo;
        private List<String> mColorThree;
        private List<String> mColorFour;
        private List<String> mColorFive;




        public SelectPhotoRecyclerAdapter(List<String> urlList, List<String> colorOne, List<String> colorTwo,
                                  List<String> colorThree, List<String> colorFour, List<String> colorFive) {
            mUrlList = urlList;
            mColorOne = colorOne;
            mColorTwo = colorTwo;
            mColorThree = colorThree;
            mColorFour = colorFour;
            mColorFive = colorFive;
        }

        public List<String> getData() {
            return mUrlList;
        }

        @Override
        public SelectPhotoActivity.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.palette_selection_list, parent, false);
            view.setOnClickListener(this);
            return new SelectPhotoActivity.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final SelectPhotoActivity.MyViewHolder holder, final int position) {
            checkPermission();

            Glide.with(SelectPhotoActivity.this)
                    .load(Uri.parse(mUrlList.get(position)))
                    .into(holder.mTvPicture);

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

            Glide.with(SelectPhotoActivity.this)
                    .load(Uri.parse(mColorOne.get(position)))
                    .into(holder.mColorOne);

            holder.itemView.setTag(position);

            holder.mColorOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!isChecked) {
                        checkedList.add(mColorOne.get(position));
                        holder.mIconOne.setImageResource(R.mipmap.imageselector_select_checked);
                        isChecked = true;
                    } else {
                        holder.mIconOne.setImageResource(R.mipmap.imageselector_select_uncheck);
                        isChecked = false;
                        checkedList.remove(mColorOne.get(position));
                    }

                }
            });

            Glide.with(SelectPhotoActivity.this)
                    .load(Uri.parse(mColorTwo.get(position)))
                    .into(holder.mColorTwo);

            holder.itemView.setTag(position);

            holder.mColorTwo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!isChecked) {
                        checkedList.add(mColorTwo.get(position));
                        holder.mIconTwo.setImageResource(R.mipmap.imageselector_select_checked);
                        isChecked = true;
                    } else {
                        holder.mIconTwo.setImageResource(R.mipmap.imageselector_select_uncheck);
                        isChecked = false;
                        checkedList.remove(mColorTwo.get(position));
                    }

                }
            });

            Glide.with(SelectPhotoActivity.this)
                    .load(Uri.parse(mColorThree.get(position)))
                    .into(holder.mColorThree);

            holder.itemView.setTag(position);

            holder.mColorThree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!isChecked) {
                        checkedList.add(mColorThree.get(position));
                        holder.mIconThree.setImageResource(R.mipmap.imageselector_select_checked);
                        isChecked = true;
                    } else {
                        holder.mIconThree.setImageResource(R.mipmap.imageselector_select_uncheck);
                        isChecked = false;
                        checkedList.remove(mColorThree.get(position));
                    }

                }
            });

            Glide.with(SelectPhotoActivity.this)
                    .load(Uri.parse(mColorFour.get(position)))
                    .into(holder.mColorFour);

            holder.itemView.setTag(position);

            holder.mColorFour.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!isChecked) {
                        checkedList.add(mColorFour.get(position));
                        holder.mIconFour.setImageResource(R.mipmap.imageselector_select_checked);
                        isChecked = true;
                    } else {
                        holder.mIconFour.setImageResource(R.mipmap.imageselector_select_uncheck);
                        isChecked = false;
                        checkedList.remove(mColorFour.get(position));
                    }

                }
            });

            Glide.with(SelectPhotoActivity.this)
                    .load(Uri.parse(mColorFive.get(position)))
                    .into(holder.mColorFive);

            holder.itemView.setTag(position);

            holder.mColorFive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!isChecked) {
                        checkedList.add(mColorFive.get(position));
                        holder.mIconFive.setImageResource(R.mipmap.imageselector_select_checked);
                        isChecked = true;
                    } else {
                        holder.mIconFive.setImageResource(R.mipmap.imageselector_select_uncheck);
                        isChecked = false;
                        checkedList.remove(mColorFive.get(position));
                    }

                }
            });

        }


        @Override
        public int getItemCount() {
            return mUrlList.size();
        }

        private SelectPhotoActivity.OnItemClickListener mOnItemClickListener = null;

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, (int)v.getTag());
            }
        }

        public void setOnItemClickListener(SelectPhotoActivity.OnItemClickListener listener) {
            this.mOnItemClickListener = listener;
        }
    }

    // define interface
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public final ImageView mTvPicture;
        public final ImageView mIcon;
        public final ImageView mIconOne;
        public final ImageView mIconTwo;
        public final ImageView mIconThree;
        public final ImageView mIconFour;
        public final ImageView mIconFive;

        public final ImageView mColorOne;
        public final ImageView mColorTwo;
        public final ImageView mColorThree;
        public final ImageView mColorFour;
        public final ImageView mColorFive;

        public MyViewHolder(View itemView) {
            super(itemView);
            mTvPicture = (ImageView) itemView.findViewById(R.id.iv_picture_item);
            mIcon = (ImageView) itemView.findViewById(R.id.photo_check);
            mIconOne = (ImageView) itemView.findViewById(R.id.photo_check_one);
            mIconTwo = (ImageView) itemView.findViewById(R.id.photo_check_two);
            mIconThree = (ImageView) itemView.findViewById(R.id.photo_check_three);
            mIconFour = (ImageView) itemView.findViewById(R.id.photo_check_four);
            mIconFive = (ImageView) itemView.findViewById(R.id.photo_check_five);

            mColorOne = (ImageView) itemView.findViewById(R.id.palette_color_one);
            mColorTwo = (ImageView) itemView.findViewById(R.id.palette_color_two);
            mColorThree = (ImageView) itemView.findViewById(R.id.palette_color_three);
            mColorFour = (ImageView) itemView.findViewById(R.id.palette_color_four);
            mColorFive = (ImageView) itemView.findViewById(R.id.palette_color_five);

        }
    }
}