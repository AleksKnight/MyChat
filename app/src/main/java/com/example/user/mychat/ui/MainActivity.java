package com.example.user.mychat.ui;

import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.mychat.R;
import com.example.user.mychat.adapters.ChatArrayAdapter;
import com.example.user.mychat.api.bitmap.GetImage;
import com.example.user.mychat.api.loaders.FileLoader;
import com.example.user.mychat.api.loaders.MessagesLoader;
import com.example.user.mychat.api.loaders.SendMessageLoader;
import com.example.user.mychat.api.loaders.SessionLoader;
import com.example.user.mychat.api.loaders.UpdateSessionLoader;
import com.example.user.mychat.datachat.GetImageUrl;
import com.example.user.mychat.datachat.GetSession;
import com.example.user.mychat.datachat.GetMessages;
import com.example.user.mychat.datachat.GetUser;
import com.squareup.okhttp.ResponseBody;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks, View.OnClickListener, PopupMenu.OnMenuItemClickListener, GetImage.Listener {


  private static final int RESULT_LOAD_IMAGE = 111;
  private GetMessages textMassages;
  private GetSession session;
  private GetUser user;
  private ImageView pop_menu_view;
  private ImageView send_message;
  private TextView send_message_text;
  private ListView listView;
  private ProgressDialog progressDialog;
  private String pathFile;
  private ChatArrayAdapter chatArrayAdapter;
  private Context context;
  private boolean getOldMassage = false;
  private Timer myTimer;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    context = this;

    Toolbar toolbar = (Toolbar) findViewById(R.id.app_toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayShowTitleEnabled(false);

    pop_menu_view = (ImageView) findViewById(R.id.pop_menu_view);
    pop_menu_view.setOnClickListener(this);

    send_message = (ImageView) findViewById(R.id.send_message);
    send_message.setOnClickListener(this);
    send_message.setClickable(true);
    send_message_text = (TextView) findViewById(R.id.send_message_text);

    listView = (android.widget.ListView) findViewById(R.id.listid);
    listView.setOnScrollListener(MyOnScrollListner);

  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }


  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    if (id == R.id.action_plus) {
      showPopupMenuPlus();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private void showPopupMenuPlus() {
    PopupMenu popupMenu = new PopupMenu(this, findViewById(R.id.action_plus));
    popupMenu.inflate(R.menu.menu_plus);
    popupMenu.setOnMenuItemClickListener(this);
    popupMenu.show();
  }

  private void showPopupMenu() {
    PopupMenu popupMenu = new PopupMenu(this, findViewById(R.id.pop_menu_view));
    popupMenu.inflate(R.menu.menu_settings);
    popupMenu.setOnMenuItemClickListener(this);
    popupMenu.show();
  }

  private Timer runTimer() {
    Timer timer = new Timer();
    timer.schedule(getTimerTask(), 0, 5000);
    return timer;
  }


  private TimerTask getTimerTask() {
    TimerTask timer = new TimerTask() {
      @Override
      public void run() {
        getLoaderManager().initLoader(R.id.messagesLoader, Bundle.EMPTY, (LoaderManager.LoaderCallbacks<Object>) context);
      }
    };
    return timer;
  }

  @Override
  public Loader onCreateLoader(int id, Bundle args) {
    switch (id) {
      case R.id.sessionLoader:
        return new SessionLoader(this);
      case R.id.updateSession:
        return new UpdateSessionLoader(this, session);
      case R.id.messagesLoader:
        if (textMassages == null) {
          return new MessagesLoader(this, session, 20, 0, 0);
        } else if (getOldMassage) {
          return new MessagesLoader(this, session, 20, 0, textMassages.messages.get(0).Message.id);
        } else {
          return new MessagesLoader(this, session, 0, textMassages.messages.get(textMassages
              .messages.size() - 1)
              .Message.id, 0);
        }
      case R.id.sendMessage:
        return new SendMessageLoader(this, session, send_message_text.getText().toString(), "NULL");
      case R.id.fileLoader:
        return new FileLoader(this, session, pathFile);
      default:
        return null;
    }
  }

  @Override
  public void onLoadFinished(Loader loader, Object data) {
    int id = loader.getId();
    switch (id) {
      case R.id.sessionLoader:
        if (data != null) {
          session = (GetSession) data;
          if (session.message == null) {
            getLoaderManager().initLoader(R.id.updateSession, Bundle.EMPTY, this);
          } else {
            Toast.makeText(this, session.message, Toast.LENGTH_LONG).show();
          }
        }
        break;
      case R.id.updateSession:
        if (data != null) {
          user = (GetUser) data;
          if (user.message == null) {
            pop_menu_view.setOnClickListener(null);
            send_message.setClickable(true);
            getLoaderManager().initLoader(R.id.messagesLoader, Bundle.EMPTY, this);
          } else {
            Toast.makeText(this, user.message, Toast.LENGTH_LONG).show();
          }
        }
        break;
      case R.id.messagesLoader:
        if (data != null) {
          if (chatArrayAdapter == null) {
            textMassages = (GetMessages) data;
            if (textMassages.message == null) {
              if (textMassages.messages.size() != 0) {
                textMassages.reverse();
                chatArrayAdapter = new ChatArrayAdapter(this, textMassages.messages,
                    user.User.getId());
                listView.setAdapter(chatArrayAdapter);
              }
            } else {
              Toast.makeText(this, textMassages.message, Toast.LENGTH_LONG).show();
            }
            myTimer = runTimer();
          } else if (getOldMassage) {
            GetMessages message = (GetMessages) data;
            if (message.message == null) {
              if (message.messages.size() != 0) {
                textMassages.addListMessagesToPostionIn(message.messages, chatArrayAdapter, listView);
                textMassages.setTotalItemsCount(message.getTotalItemsCount());
                getOldMassage = false;
              }
            } else {
              Toast.makeText(this, message.message, Toast.LENGTH_LONG).show();
            }
          } else {
            GetMessages message = (GetMessages) data;
            if (message.message == null) {
              if (message.messages.size() != 0) {
                textMassages.addListMessagesNew(message.messages, chatArrayAdapter, listView);
                textMassages.setTotalItemsCount(message.getTotalItemsCount());
              }
            } else {
              Toast.makeText(this, message.message, Toast.LENGTH_LONG).show();
            }
          }
        }
        break;
      case R.id.sendMessage:
        if (data != null) {
          ResponseBody e = (ResponseBody) data;
          send_message_text.setText("");
        }
        myTimer = runTimer();
        break;
      case R.id.fileLoader:
        if (data != null) {
          GetImageUrl get = (GetImageUrl) data;
          if (get.message == null) {
            Toast.makeText(this, get.message, Toast.LENGTH_LONG).show();
          }
          h.sendEmptyMessageDelayed(0, 2000);
        } else {
          h.sendEmptyMessageDelayed(0, 2000);
        }
    }
    getLoaderManager().destroyLoader(id);
  }

  @Override
  public void onLoaderReset(Loader loader) {

  }

  @Override
  public void onClick(View v) {
    if (v.getId() == R.id.pop_menu_view) {
      showPopupMenu();
    }
    if (v.getId() == R.id.send_message) {
      if (send_message_text.getText().toString().equals("") != true) {
        getLoaderManager().initLoader(R.id.sendMessage, Bundle.EMPTY, this);
        myTimer.cancel();
      } else {
        Toast.makeText(this, R.string.message_emty,
            Toast.LENGTH_LONG).show();
      }
    }
  }

  @Override
  public boolean onMenuItemClick(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.sigin:
        getLoaderManager().initLoader(R.id.sessionLoader, Bundle.EMPTY, this);
        return true;
      case R.id.add_image:
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
      default:
        return false;
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    switch (requestCode) {
      case RESULT_LOAD_IMAGE:
        if (null != data) {
          GetImage getImage = new GetImage(this, data.getData(), 600, 600);
          getImage.setStateChangeListener(this);
          getImage.execute();
        }
        break;
      default:
        break;
    }
  }

  @Override
  public void onStateChange(int state, String _path) {
    switch (state) {
      case GetImage.FILE_BEGIN_DATA:
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.file_upload));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        break;
      case GetImage.FILE_FALSE:
        progressDialog.setMessage(getResources().getString(R.string.file_not_valid));
        h.sendEmptyMessageDelayed(0, 2000);
        break;
      case GetImage.FILE_TRUE:
        pathFile = _path;
        getLoaderManager().initLoader(R.id.fileLoader, Bundle.EMPTY, this);
        break;
      default:
        break;
    }
  }

  Handler h = new Handler() {
    public void handleMessage(Message msg) {
      progressDialog.dismiss();
    }
  };

  AbsListView.OnScrollListener MyOnScrollListner = new AbsListView.OnScrollListener() {

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
      int count = listView.getCount();
      int countAll = textMassages.getTotalItemsCount();
      if (scrollState == SCROLL_STATE_IDLE) {
        if (listView.getFirstVisiblePosition() == 0 && count < countAll) {
          getOldMassage = true;
          getLoaderManager().initLoader(R.id.messagesLoader, Bundle.EMPTY, (LoaderManager.LoaderCallbacks<Object>) context);
        }
      }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
  };
}
