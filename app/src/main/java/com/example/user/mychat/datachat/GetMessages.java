package com.example.user.mychat.datachat;

import android.widget.ListView;

import com.example.user.mychat.adapters.ChatArrayAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by user on 20.01.2016.
 */
public class GetMessages {
  public List<MessageBody> messages = new ArrayList<MessageBody>();
  public int total_items_count;
  public String message;

  public List<MessageBody> getListMessages() {
    return messages;
  }

  public int getTotalItemsCount() {
    return total_items_count;
  }

  public void setTotalItemsCount(int _total_items_count) {
    total_items_count = _total_items_count;
  }

  public void addListMessagesToPostionIn(List<MessageBody> _messages, ChatArrayAdapter chatArrayAdapter, ListView listView) {
    Collections.reverse(_messages);
    messages.addAll(0, _messages);
    chatArrayAdapter.notifyDataSetChanged();
    listView.setSelection(_messages.size() - 1);
  }

  public void addListMessagesNew(List<MessageBody> _messages, ChatArrayAdapter chatArrayAdapter, ListView listView) {
    int position = messages.size() - 1;
    Collections.reverse(_messages);
    messages.addAll(_messages);

    chatArrayAdapter.notifyDataSetChanged();

    if (listView.getLastVisiblePosition() == position) {
      listView.setSelection(messages.size() - 1);
    }
  }

  public void reverse() {
    Collections.reverse(messages);
  }
}
