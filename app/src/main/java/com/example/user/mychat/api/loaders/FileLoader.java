package com.example.user.mychat.api.loaders;

import android.content.Context;

import com.example.user.mychat.api.ApiFactory;
import com.example.user.mychat.api.ChatService;
import com.example.user.mychat.api.RetrofitCallback;
import com.example.user.mychat.datachat.GetImageUrl;
import com.example.user.mychat.datachat.GetSession;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import java.io.File;

import retrofit.Call;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by user on 22.01.2016.
 */
public class FileLoader extends MyBaseLoader {
  private GetSession session;
  private String pathFile;
  private ChatService mChatService;
  private GetImageUrl getImageUrl;

  public FileLoader(Context context, GetSession _session, String _pathFile) {
    super(context);
    session = _session;
    pathFile = _pathFile;
    mChatService = ApiFactory.getChatService();

  }

  @Override
  protected void onForceLoad() {
    File file = new File(pathFile);

    RequestBody requestBody =
        RequestBody.create(MediaType.parse("multipart/form-data"), file);

    Call<GetImageUrl> call = mChatService.upload_file(session, requestBody);
    call.enqueue(new RetrofitCallback<GetImageUrl>() {
      @Override
      public void onResponse(Response<GetImageUrl> response, Retrofit retrofit) {
        if (response.isSuccess()) {
          getImageUrl = response.body();

          deliverResult(getImageUrl);
        } else {
          deliverResult(null);
        }

      }

      @Override
      public void onFailure(Throwable t) {

      }

    });

  }
}
