package com.example.admin.takecustomscreenshotandstorage;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl);
        final Button btn = (Button) findViewById(R.id.btn);
        final ImageView iv=findViewById(R.id.iv);
        final Button fetch = (Button) findViewById(R.id.btn_fetch);
        fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //it will crop image
         /*       Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                intent.setType("image*//*");
                intent.putExtra("crop", "true");
                intent.putExtra("scale", true);
                intent.putExtra("outputX", 256);
                intent.putExtra("outputY", 256);
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, 11);*/


                //You can fetch image from storage not gallery
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +"/"+ "test.jpg");
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                iv.setImageBitmap(bitmap);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bitmap bitmap = TakeScreenShot(rl);

                //Use for store image into internal store
                //always save as
                String fileName = "test.jpg";
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
                File ExternalStorageDirectory = Environment.getExternalStorageDirectory();
                File file = new File(ExternalStorageDirectory + File.separator + fileName);
                FileOutputStream fileOutputStream = null;
                try {
                    file.createNewFile();
                    fileOutputStream = new FileOutputStream(file);
                    fileOutputStream.write(bytes.toByteArray());
                    Toast.makeText(MainActivity.this,
                            file.getAbsolutePath(),
                            Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }

            }
        });



        //Use for gallary
       /* btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap screenShot = TakeScreenShot(rl);
                MediaStore.Images.Media.insertImage(
                        getContentResolver(),
                        screenShot,
                        "Image",
                        "Captured ScreenShot"
                );

                Toast.makeText(getApplicationContext(), "Screen Captured.",Toast.LENGTH_SHORT).show();

            }
        });*/

        if (shouldAskPermissions()) {
            askPermissions();
        }

    }

    protected boolean shouldAskPermissions() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(23)
    protected void askPermissions() {
        String[] permissions = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"
        };
        int requestCode = 200;
        requestPermissions(permissions, requestCode);
    }


    private Bitmap TakeScreenShot(RelativeLayout rl) {
        Bitmap bitmap = Bitmap.createBitmap(rl.getWidth(),rl.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        rl.draw(canvas);
        return bitmap;
    }


}
