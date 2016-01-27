package com.example.user.mychat.api;

import com.example.user.mychat.datachat.GetImageUrl;
import com.example.user.mychat.datachat.GetSession;
import com.example.user.mychat.datachat.GetMessages;
import com.example.user.mychat.datachat.Session;
import com.example.user.mychat.datachat.GetUser;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.ResponseBody;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Query;

/**
 * Created by user on 19.01.2016.
 */


public interface ChatService {

  @POST("/signup")
  Call<GetSession> session(@Body String post);

  @Headers("content-type: application/json")
  @POST("/session")
  Call<GetUser> update_session(@Body GetSession session);

  @Headers("content-type: application/json")
  @GET("/messages")
  Call<GetMessages> messages(@Query("session") String session, @Query("paging_size") int
      paging_size, @Query("newest_message_id") int newest_message_id, @Query("oldest_message_id") int oldest_message_id);

  @Headers("content-type: application/json")
  @POST("/messages/message")
  Call<ResponseBody> message_send(@Body Session session);

  @Multipart
  @Headers("content-type: multipart/form-data")
  @POST("/image")
  Call<GetImageUrl> upload_file(@Part("session")GetSession session,@Part("file") RequestBody file);
}
