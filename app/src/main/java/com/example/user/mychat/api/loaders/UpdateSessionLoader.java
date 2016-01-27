package com.example.user.mychat.api.loaders;

import android.content.Context;

import com.example.user.mychat.api.ApiFactory;
import com.example.user.mychat.api.ChatService;
import com.example.user.mychat.api.RetrofitCallback;
import com.example.user.mychat.datachat.GetSession;
import com.example.user.mychat.datachat.GetUser;

import retrofit.Call;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by user on 19.01.2016.
 */
public class UpdateSessionLoader extends MyBaseLoader {

  private GetSession user_session;
  private ChatService mChatService;
  private GetUser user;

  public UpdateSessionLoader(Context context, GetSession _session) {
    super(context);
    user_session=_session;
    mChatService = ApiFactory.getChatService();
  }

  @Override
  protected void onForceLoad() {
    Call<GetUser> call = mChatService.update_session(user_session);
    call.enqueue(new RetrofitCallback<GetUser>() {
      @Override
      public void onResponse(Response<GetUser> response, Retrofit retrofit) {
        if (response.isSuccess()) {
          user = response.body();
          System.out.println("body " + response.body());
          //.setSession(user_session);
          deliverResult(user);
        } else {
          deliverResult(null);
        }

      }


    });

  }


}
