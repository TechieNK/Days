package project.days;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;


public class DiaryContentActivity extends AppCompatActivity {
    private EditText mDiaryContent;
    private Button mContentViewBack;
    private Button mContentEdit;
    private Button mContentDelete;
    private Button mContentAddPhotos;
    private Button mContentAddVideos;
    private FirebaseAuth fAuth;
    private Uri filePath;
    private FirebaseStorage storage;
    private StorageReference storageReference;


    public DiaryContentActivity() {
    }



    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_content);

        mContentViewBack = findViewById(R.id.content_view_back_icon);
        mContentEdit     = findViewById(R.id.content_edit_icon);
        mContentDelete   = findViewById(R.id.content_delete_icon);
        mDiaryContent    = findViewById(R.id.diary_content_text);
        mContentAddPhotos= findViewById(R.id.content_add_photos);
        mContentAddVideos= findViewById(R.id.content_add_videos);


        fAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        mContentEdit.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v){
                                                String diarycontent = mDiaryContent.getText().toString().trim();

                                                if(TextUtils.isEmpty(diarycontent)){
                                                    mDiaryContent.setError("Enter the content.");
                                                    return;
                                                }
                                                if(diarycontent.length()<1000000000){
                                                    mDiaryContent.setError("Content is exceded");
                                                }
                                            }
                                        }
        );
        mContentAddPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void OnClick(View v)
            {
                chooseImage();
                UploadImage();
            }
        });
        mContentAddVideos.setOnClickListener(new View.OnClickListener){
            @Override
            public void OnClick(View v)
            {
                chooseVideo();
                UploadVideo();
            }
        });
    }

    protected void chooseImage()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select image"),1);
    }

    protected void chooseVideo()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select videos"),1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data!= null && data.getData()!=null)
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                /*imageView.setImageBitmap(bitmap);*/
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void UploadImage()
    {
        if(filePath!=null)
        { final ProgressDialog progressDialog = new ProgressDialog(DiaryContentActivity.this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();
            StorageReference reference = storageReference.child("video/" + UUID.randomUUID().toString());
            reference.putFile((filePath)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    progressDialog.dismiss();
                    Toast.makeText(DiaryContentActivity.this, "Video uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded"+(int)progress+"%" );

                }
            });
        }
    }

    private void UploadVideo()
    {
        if(filePath!=null)
        { final ProgressDialog progressDialog = new ProgressDialog(DiaryContentActivity.this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();
            StorageReference reference = storageReference.child("video/" + UUID.randomUUID().toString());
            reference.putFile((filePath)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    progressDialog.dismiss();
                    Toast.makeText(DiaryContentActivity.this, "Video uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded"+(int)progress+"%" );
                }
            });
        }
    }
}