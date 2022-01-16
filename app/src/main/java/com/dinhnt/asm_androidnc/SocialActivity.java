package com.dinhnt.asm_androidnc;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class SocialActivity extends AppCompatActivity {

    ImageView imgShare;
    TextView txtImage;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social);

        EditText edtContent = findViewById(R.id.edtContent);
        Button btnShare = findViewById(R.id.btnShare);
        LinearLayout linearShare = findViewById(R.id.linearShare);
        txtImage = findViewById(R.id.txtImage);
        imgShare = findViewById(R.id.imgShare);

        linearShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);

                chooseImage.launch(i);
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, edtContent.getText().toString());
                intent.putExtra(Intent.EXTRA_SUBJECT, "DinhNT chia sẻ");

                if(bitmap != null){
                    Uri uri = getmageToShare(bitmap);
                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                    intent.setType("image/png");
                }else{
                    intent.setType("text/plain");
                }

                startActivity(Intent.createChooser(intent, "Chia sẻ nội dung thông qua"));
            }
        });
    }

    ActivityResultLauncher<Intent> chooseImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Uri selectedImageUri = data.getData();
                        if (null != selectedImageUri) {
                            imgShare.setImageURI(selectedImageUri);
                            txtImage.setText("Lựa chọn lại hình ảnh");

                            BitmapDrawable bitmapDrawable = (BitmapDrawable) imgShare.getDrawable();
                            bitmap = bitmapDrawable.getBitmap();
                        }
                    }
                }
            });

    private Uri getmageToShare(Bitmap bitmap) {
        File imagefolder = new File(getCacheDir(), "images");
        Uri uri = null;
        try {
            imagefolder.mkdirs();
            File file = new File(imagefolder, "shared_image.png");
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
            outputStream.flush();
            outputStream.close();
            uri = FileProvider.getUriForFile(this, "com.dinhnt.asm_androidnc.fileprovider", file);
        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return uri;
    }
}