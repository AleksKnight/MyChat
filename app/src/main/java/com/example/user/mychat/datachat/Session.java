package com.example.user.mychat.datachat;

/**
 * Created by user on 19.01.2016.
 */
public class Session {
  private String session;
  public Message message = new Message();

  public String getSession() {
    return session;
  }

  public void setSession(String _session) {
    session = _session;
  }
}

