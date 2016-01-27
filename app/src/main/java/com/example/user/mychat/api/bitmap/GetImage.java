package com.example.user.mychat.api.bitmap;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

/**
 * Created by user on 22.01.2016.
 */
public class GetImage extends AsyncTask<Void, Void, Bitmap> {

  public static final int FILE_TRUE = 0;
  public static final int FILE_FALSE = 1;
  public static final int FILE_BEGIN_DATA = 2;
  private final int mWidth;
  private final int mHeight;
  private Uri uri;
  private String path;
  private Context mContext;

  public GetImage(Context context, Uri _uri, int _mWidth, int _mHeight) {
    mWidth = _mWidth;
    mHeight = _mHeight;
    uri = _uri;
    mContext = context;
  }


  public interface Listener {
    void onStateChange(int state, String _path);
  }

  private Listener mListener = null;

  public void setStateChangeListener(Listener listener) {
    mListener = listener;
  }

  @Override
  protected void onPreExecute() {
    if (mListener != null)
      mListener.onStateChange(FILE_BEGIN_DATA, path);
  }

  @Override
  protected Bitmap doInBackground(Void... params) {
    path = getRealPathFromURI(mContext, uri);
    return decodeBitmap(path, mWidth, mHeight);
  }

  @Override
  protected void onPostExecute(Bitmap bitmap) {
    if (bitmap != null) {
      if (mListener != null)
        mListener.onStateChange(FILE_TRUE, path);
    } else {
      if (mListener != null)
        mListener.onStateChange(FILE_FALSE, path);
    }
  }

  private static Bitmap decodeBitmap(String path,
                                     int mWidth, int mHeight) {
    final BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    BitmapFactory.decodeFile(path, options);
    options.inSampleSize = calculateSize(options, mWidth,
        mHeight);
    options.inJustDecodeBounds = false;
    return BitmapFactory.decodeFile(path, options);
  }

  private static int calculateSize(BitmapFactory.Options options,
                                   int mWidth, int mHeight) {
    final int height = options.outHeight;
    final int width = options.outWidth;
    int inSampleSize = 1;

    if (height > mHeight || width > mWidth) {
      final int halfHeight = height / 2;
      final int halfWidth = width / 2;

      while ((halfHeight / inSampleSize) > mHeight
          && (halfWidth / inSampleSize) > mWidth) {
        inSampleSize *= 2;
      }
    }
    return inSampleSize;
  }

  public String getRealPathFromURI(Context context, Uri contentUri) {
    Cursor cursor = null;
    try {
      String[] proj = {MediaStore.Images.Media.DATA};
      cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
      int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
      cursor.moveToFirst();
      return cursor.getString(column_index);
    } finally {
      if (cursor != null) {
        cursor.close();
      }
    }
  }
}
