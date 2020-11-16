package com.sendbird.calls.quickstart.main;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.GroupChannelListQuery;
import com.sendbird.android.SendBirdException;
import com.sendbird.calls.quickstart.R;
import com.sendbird.calls.quickstart.call.CallService;
import com.sendbird.calls.quickstart.utils.PrefUtils;
import com.sendbird.uikit.SendBirdUIKit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DialFragment extends Fragment {

    private InputMethodManager mInputMethodManager;

    private TextInputEditText mTextInputEditTextUserId;
    private ImageView mImageViewVideoCall;
    private ImageView mImageViewVoiceCall;
    private ImageView mImageViewChat;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getContext() != null) {
            mInputMethodManager = (InputMethodManager) (getContext().getSystemService(Context.INPUT_METHOD_SERVICE));
        }
        return inflater.inflate(R.layout.fragment_dial, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mTextInputEditTextUserId = view.findViewById(R.id.text_input_edit_text_user_id);
        mImageViewVideoCall = view.findViewById(R.id.image_view_video_call);
        mImageViewVoiceCall = view.findViewById(R.id.image_view_voice_call);
        mImageViewChat = view.findViewById(R.id.image_view_chat);

        mImageViewVideoCall.setEnabled(false);
        mImageViewVoiceCall.setEnabled(false);
        mImageViewChat.setEnabled(false);

        String savedCalleeId = PrefUtils.getCalleeId(getContext());
        if (!TextUtils.isEmpty(savedCalleeId)) {
            mTextInputEditTextUserId.setText(savedCalleeId);
            mTextInputEditTextUserId.setSelection(savedCalleeId.length());
            mImageViewVideoCall.setEnabled(true);
            mImageViewVoiceCall.setEnabled(true);
        }

        mTextInputEditTextUserId.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                mTextInputEditTextUserId.clearFocus();
                if (mInputMethodManager != null) {
                    mInputMethodManager.hideSoftInputFromWindow(mTextInputEditTextUserId.getWindowToken(), 0);
                }
                return true;
            }
            return false;
        });
        mTextInputEditTextUserId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mImageViewVideoCall.setEnabled(editable != null && editable.length() > 0);
                mImageViewVoiceCall.setEnabled(editable != null && editable.length() > 0);
                mImageViewChat.setEnabled(editable != null && editable.length() > 0);
            }
        });

        mImageViewVideoCall.setOnClickListener(view1 -> {
            String calleeId = (mTextInputEditTextUserId.getText() != null ? mTextInputEditTextUserId.getText().toString() : "");
            if (!TextUtils.isEmpty(calleeId)) {
                CallService.dial(getContext(), calleeId, true);
                PrefUtils.setCalleeId(getContext(), calleeId);
            }
        });

        mImageViewVoiceCall.setOnClickListener(view1 -> {
            String calleeId = (mTextInputEditTextUserId.getText() != null ? mTextInputEditTextUserId.getText().toString() : "");
            if (!TextUtils.isEmpty(calleeId)) {
                CallService.dial(getContext(), calleeId, false);
                PrefUtils.setCalleeId(getContext(), calleeId);
            }
        });

        /**
         * Adding listener for start chat icon
         */
        mImageViewChat.setOnClickListener(view1 -> {
            String calleeId = (mTextInputEditTextUserId.getText() != null ? mTextInputEditTextUserId.getText().toString() : "");
            Log.v("sendbird", "DialFragment: ImageViewChat: callee Id: " + calleeId);
            if (!TextUtils.isEmpty(calleeId)) {
                getChannelURL(calleeId, (channelURL -> {
                    Log.v("sendbird", "DialFragment: ImageViewChat: channelURL: " + channelURL);
                    displayChat(channelURL);
                }));
            }
        });
    }

    /**
     * Trigger UIKit to display the specified group chat
     */
    void displayChat(String channelURL){
        ((MainActivity) getActivity()).displayChat(channelURL);
    }

    /**
     * Callback interface
     */
    interface channelCallback {
        void onComplete(String channelURL);
    }

    /**
     * Use Sendbird Chat SDK to get the channel URL
     * @param callback
     */
    void getChannelURL(String calleeId, channelCallback callback ){
        Log.v("sendbird", "DialFragment: getChannelUrl" );
        SendBirdUIKit.connect((user, e) -> {
            if (e != null) {
                Log.e("sendbird", "DialFragment: getChannelUrl: ERROR: " + e);
                return;
            }

            getExistingChannel(calleeId, user.getUserId(), (channelUrl)->{
                if (channelUrl == null){
                    // No pre-existing - let's create a new channel
                    List<String> users = Arrays.asList(user.getUserId(), calleeId);
                    GroupChannel.createChannelWithUserIds(users, true, new GroupChannel.GroupChannelCreateHandler() {
                        @Override
                        public void onResult(GroupChannel groupChannel, SendBirdException e) {
                            if (e != null) {    // Error.
                                Log.e("sendbird", "DialFragment: getChannelUrl: ERROR: " + e );
                                return;
                            }
                            callback.onComplete(groupChannel.getUrl());
                        }
                    });
                    return;
                }
                // Prior channel found - let's display that one
                callback.onComplete(channelUrl);
            });
        });
    }

    void getExistingChannel(String calleeId, String callerId, channelCallback callback){
        Log.v("sendbird", "DialFragment: getExistingChannel: callee: " + calleeId + " caller: " + callerId );
        GroupChannelListQuery filteredQuery = GroupChannel.createMyGroupChannelListQuery();
        List<String> userIds = new ArrayList<>();
        userIds.add(calleeId);
        userIds.add(callerId);

        filteredQuery.setUserIdsIncludeFilter(userIds, GroupChannelListQuery.QueryType.AND);
        filteredQuery.next(new GroupChannelListQuery.GroupChannelListQueryResultHandler() {
            @Override
            public void onResult(List<GroupChannel> list, SendBirdException e) {
                Log.v("sendbird", "DialFragment: getExistingChannel: list: " + list.toString() + " ERROR: " + e );

                String result = null;
                if (e == null && !list.isEmpty()){
                    GroupChannel channel = list.get(0);
                    result = channel.getUrl();
                }
                callback.onComplete(result);
            }
        });
    }

}
