package com.example.user.mychat.api.loaders;

import android.content.Context;
import android.content.Loader;

/**
 * Created by user on 19.01.2016.
 */


public class MyBaseLoader extends Loader<Object> {

  private Object mObject;

  public MyBaseLoader(Context context) {

    super(context);

  }

  @Override
  public void deliverResult(Object object) {
    if (isReset()) {
      if (object != null) {

      }
      return;
    }
    mObject = object;

    if (isStarted()) {
      super.deliverResult(object);
    }

  }

  @Override
  protected void onStartLoading() {
    if (mObject != null) {
      deliverResult(mObject);
    } else {
      forceLoad();
    }
  }

  @Override
  protected void onReset() {

    mObject = null;
  }

}