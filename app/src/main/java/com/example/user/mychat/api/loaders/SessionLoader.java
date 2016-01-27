package com.example.user.mychat.api.loaders;

import android.content.Context;

import com.example.user.mychat.api.ApiFactory;
import com.example.user.mychat.api.ChatService;
import com.example.user.mychat.api.RetrofitCallback;
import com.example.user.mychat.datachat.GetSession;

import retrofit.Call;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by user on 19.01.2016.
 */
public class SessionLoader extends MyBaseLoader {

  private ChatService mChatService;
  private GetSession session;

  public SessionLoader(Context context) {

    super(context);
    mChatService = ApiFactory.getChatService();
  }

  @Override
  protected void onForceLoad() {
    Call<GetSession> call = mChatService.session("");
    call.enqueue(new RetrofitCallback<GetSession>() {
      @Override
      public void onResponse(Response<GetSession> response, Retrofit retrofit) {
        if (response.isSuccess()) {
          session = response.body();
          deliverResult(session);
        } else {
          deliverResult(null);
        }

      }


    });

  }
}
