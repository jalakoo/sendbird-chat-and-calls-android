package com.example.sendbird_chat_and_calls

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sendbird.android.SendBirdException
import com.sendbird.android.User
import com.sendbird.uikit.SendBirdUIKit
import com.sendbird.uikit.activities.ChannelListActivity
import com.sendbird.uikit.adapter.SendBirdUIKitAdapter
import com.sendbird.uikit.interfaces.UserInfo
import com.sendbird.uikit.log.Logger
import com.sendbird.uikit.widgets.WaitingDialog

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        SendBirdUIKit.init(object : SendBirdUIKitAdapter {
            override fun getAppId(): String {
                return "6F8C8FA1-3BFE-4C8D-BC2D-992A35101DDA";
            }

            override fun getAccessToken(): String {
                return ""
            }

            override fun getUserInfo(): UserInfo {
                return object : UserInfo {
                    override fun getUserId(): String {
                        return "batman"
                    }

                    override fun getNickname(): String {
                        return "Batman"
                    }

                    override fun getProfileUrl(): String {
                        return ""
                    }
                }
            }
        }, this)


        SendBirdUIKit.connect { user: User?, e: SendBirdException? ->
                println("MainActivity: sendbird connect")
                if (e != null) {
                    Logger.e(e)
                    println("Error: " + e)
                    WaitingDialog.dismiss()
                    return@connect
                }
                WaitingDialog.dismiss()
                // 3. Kick off Group Channels list UI
                startActivity(ChannelListActivity.newIntent(this))
                finish()

            }
        }

}