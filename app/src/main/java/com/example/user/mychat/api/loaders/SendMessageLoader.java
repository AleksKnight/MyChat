package com.example.user.mychat.api.loaders;

import android.content.Context;

import com.example.user.mychat.api.ApiFactory;
import com.example.user.mychat.api.ChatService;
import com.example.user.mychat.api.RetrofitCallback;
import com.example.user.mychat.datachat.GetSession;
import com.example.user.mychat.datachat.Session;
import com.squareup.okhttp.ResponseBody;

import retrofit.Call;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by user on 21.01.2016.
 */
public class SendMessageLoader extends MyBaseLoader {
  private ChatService mChatService;
  private GetSession session;
  private String textMassage;
  private String imgUrl;
  private ResponseBody mResponse;


  public SendMessageLoader(Context context, GetSession _session, String _textMessage, String
      _imgUrl) {
    super(context);
    session = _session;
    textMassage = _textMessage;
    imgUrl = _imgUrl;
    mChatService = ApiFactory.getChatService();

  }

  @Override
  protected void onForceLoad() {
  Session sess = new Session();
    sess.setSession(session.session);
    sess.message.text=textMassage;



    Call<ResponseBody> call = mChatService.message_send(sess);
    call.enqueue(new RetrofitCallback<ResponseBody>() {
      @Override
      public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
        if (response.isSuccess()) {
          mResponse = response.body();
          deliverResult(mResponse);
        } else {
          deliverResult(null);
        }

      }

      @Override
      public void onFailure(Throwable t) {

        System.out.println("нету " + t);
      }

    });

  }


}
