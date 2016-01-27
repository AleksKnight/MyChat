package com.example.user.mychat.adapters;

/**
 * Created by user on 19.01.2016.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.user.mychat.R;
import com.example.user.mychat.datachat.MessageBody;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ChatArrayAdapter extends BaseAdapter {

  private int marginTextTop;
  private Context ctx;
  private LayoutInflater lInflater;
  private List<MessageBody> messages;
  private RoundedImageView userAvatar;
  private ImageView imageInMessage;
  private TextView userName;
  private TextView dataTime;
  private TextView textMessage;
  private RelativeLayout.LayoutParams textMessageRight;
  private RelativeLayout.LayoutParams textMessageLeft;
  private RelativeLayout.LayoutParams textImageMessageRight;
  private RelativeLayout.LayoutParams textImageMessageLeft;
  private int id;
  private static final String DATE_PATTERN = "hh:mm aa";
  private static final String DATE_PATTERN_IN = "yyyy-MM-dd HH:mm:ss";
  private int paddingImg;
  private int padding;
  private int marginTextRight;
  private int marginTextLeft;
  private int width;
  private int height;
  private int mathParent = RelativeLayout.LayoutParams.MATCH_PARENT;
  private int wrapContent = RelativeLayout.LayoutParams.WRAP_CONTENT;

  public ChatArrayAdapter(Context context, List<MessageBody> _messages, int _id) {
    ctx = context;
    messages = _messages;
    lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    id = _id;
    generateParams();
  }

  @Override
  public int getCount() {
    return messages.size();
  }

  @Override
  public Object getItem(int position) {
    return messages.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View view = convertView;
    if (view == null) {
      view = lInflater.inflate(R.layout.in_sms, parent, false);
    }

    MessageBody msg = (MessageBody) getItem(position);

    userAvatar = (RoundedImageView) view.findViewById(R.id.userAvatar);
    userName = (TextView) view.findViewById(R.id.userName);
    dataTime = (TextView) view.findViewById(R.id.dataTime);
    textMessage = (TextView) view.findViewById(R.id.textMessage);
    imageInMessage = (ImageView) view.findViewById(R.id.imageInMessage);
    imageInMessage.setVisibility(View.GONE);

    if (msg.User != null)
      if (msg.User.getId() != id) {
        userAvatar.setVisibility(View.VISIBLE);
        if (msg.User.avatar_url != null) {
          Picasso.with(ctx)
              .load(msg.User.avatar_url)
              .resizeDimen(R.dimen.text_msg_img_size_height, R.dimen.text_msg_img_size_height)
              .centerCrop()
              .into(userAvatar);
        }

        userName.setText(msg.User.nickname);
        userName.setVisibility(View.VISIBLE);

        dataTime.setText(getDate(msg.Message.updated_at));
        dataTime.setPadding(0, 0, 0, 0);

        textMessage.setText(msg.Message.text);
        textMessage.setBackgroundResource(R.drawable.inn_sms);
        textMessage.setPadding(padding, padding, padding, padding);
        textMessage.setLayoutParams(textMessageLeft);

        if (msg.Message != null)
          if (msg.Message.image_url != null) {
            imageInMessage.setVisibility(View.VISIBLE);
            imageInMessage.setScaleType(ImageView.ScaleType.FIT_START);
            int paddingreal;
            if (msg.Message.text != null) {
              paddingreal = paddingImg + padding;
            } else {
              paddingreal = padding;
              textMessage.setLayoutParams(textImageMessageLeft);
            }

            textMessage.setPadding(padding, paddingreal, padding, padding);
            Picasso.with(ctx)
                .load(msg.Message.image_url)
                .resizeDimen(R.dimen.text_msg_img_size_height, R.dimen.text_msg_img_size_height)
                .centerCrop()
                .into(imageInMessage);
          }

      } else {
        userAvatar.setVisibility(View.GONE);
        userName.setVisibility(View.GONE);

        dataTime.setText(getDate(msg.Message.updated_at));
        dataTime.setPadding(0, 0, marginTextRight, 0);

        textMessage.setText(msg.Message.text);
        textMessage.setBackgroundResource(R.drawable.out_sms);
        textMessage.setPadding(padding, padding, padding, padding);
        textMessage.setLayoutParams(textMessageRight);

        if (msg.Message != null)
          if (msg.Message.image_url != null) {
            imageInMessage.setVisibility(View.VISIBLE);
            imageInMessage.setScaleType(ImageView.ScaleType.FIT_END);
            int paddingreal;
            if (msg.Message.text != null) {
              paddingreal = paddingImg + padding;
            } else {
              paddingreal = padding;
              textMessage.setLayoutParams(textImageMessageRight);
            }
            textMessage.setPadding(padding, paddingreal, padding, padding);
            Picasso.with(ctx)
                .load(msg.Message.image_url)
                .resizeDimen(R.dimen.text_msg_img_size_height, R.dimen.text_msg_img_size_height)
                .centerCrop()
                .into(imageInMessage);
          }


      }

    return view;
  }

  private String getDate(String datein) {
    SimpleDateFormat inDateFormat = new SimpleDateFormat(DATE_PATTERN_IN);
    SimpleDateFormat outDateFormat = new SimpleDateFormat(DATE_PATTERN);
    Date date = null;
    String dateout = "";
    try {
      date = inDateFormat.parse(datein);
      dateout = outDateFormat.format(date);
    } catch (ParseException pe) {
      pe.printStackTrace();
    }
    return dateout;
  }

  private void generateParams() {
    paddingImg = ctx.getResources().getDimensionPixelSize(R.dimen.image_text_padding);
    padding = ctx.getResources().getDimensionPixelSize(R.dimen.msg_text_padding);

    marginTextRight = ctx.getResources().getDimensionPixelSize(R.dimen
        .margin_text_right_msg);
    marginTextLeft = ctx.getResources().getDimensionPixelSize(R.dimen.margin_text_left_msg);

    marginTextTop = ctx.getResources().getDimensionPixelSize(R.dimen.margin_text_top);

    width = ctx.getResources().getDimensionPixelSize(R.dimen.text_msg_size_width);
    height = ctx.getResources().getDimensionPixelSize(R.dimen.text_msg_size_height);

    textImageMessageRight = new RelativeLayout.LayoutParams(width, height);
    textImageMessageRight.rightMargin = marginTextLeft;
    textImageMessageRight.leftMargin = marginTextRight;
    textImageMessageRight.topMargin = marginTextTop;
    textImageMessageRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

    textImageMessageLeft = new RelativeLayout.LayoutParams(width, height);
    textImageMessageLeft.rightMargin = marginTextRight;
    textImageMessageLeft.leftMargin = marginTextLeft;
    textImageMessageLeft.topMargin = marginTextTop;
    textImageMessageLeft.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

    textMessageRight = new RelativeLayout.LayoutParams(mathParent, wrapContent);
    textMessageRight.rightMargin = marginTextLeft;
    textMessageRight.leftMargin = marginTextRight;
    textMessageRight.topMargin = marginTextTop;
    textMessageRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

    textMessageLeft = new RelativeLayout.LayoutParams(mathParent, wrapContent);
    textMessageLeft.rightMargin = marginTextRight;
    textMessageLeft.leftMargin = marginTextLeft;
    textMessageLeft.topMargin = marginTextTop;
    textMessageLeft.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
  }
}

