package com.example.user.mychat.api.loaders;

import android.content.Context;

import com.example.user.mychat.api.ApiFactory;
import com.example.user.mychat.api.ChatService;
import com.example.user.mychat.api.RetrofitCallback;
import com.example.user.mychat.datachat.GetSession;
import com.example.user.mychat.datachat.GetMessages;

import retrofit.Call;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by user on 20.01.2016.
 */
public class MessagesLoader extends MyBaseLoader {
  private ChatService mChatService;
  private GetSession session;
  private GetMessages messages;
  private int paging_size;
  private int newest_message_id;
  private int oldest_message_id;

  public MessagesLoader(Context context,GetSession _session,int _paging_size,int
      _newest_message_id,int
      _oldest_message_id) {
    super(context);
    session=_session;
    paging_size=_paging_size;
    newest_message_id=_newest_message_id;
    oldest_message_id=_oldest_message_id;
    mChatService = ApiFactory.getChatService();
  }

  @Override
  protected void onForceLoad() {
    System.out.println("есть 1");
    Call<GetMessages> call = mChatService.messages(session.session, paging_size,
        newest_message_id, oldest_message_id);
    System.out.println("есть 2");
    call.enqueue(new RetrofitCallback<GetMessages>() {
      @Override
      public void onResponse(Response<GetMessages> response, Retrofit retrofit) {
        if (response.isSuccess()) {
          messages = response.body();
          System.out.println("есть " +response.code());

          deliverResult(messages);
        } else {
           deliverResult(null);
          System.out.println("нету");
        }

      }
      @Override
      public void onFailure(Throwable t) {

        System.out.println("нету " +t);
      }

    });

  }

}
