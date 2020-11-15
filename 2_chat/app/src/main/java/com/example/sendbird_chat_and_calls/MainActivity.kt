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
                return "D56438AE-B4DB-4DC9-B440-E032D7B35CEB";
            }

            override fun getAccessToken(): String {
                return ""
            }

            override fun getUserInfo(): UserInfo {
                return object : UserInfo {
                    override fun getUserId(): String {
                        return "jason"
                    }

                    override fun getNickname(): String {
                        return "Jason"
                    }

                    override fun getProfileUrl(): String {
                        return "https://media-exp1.licdn.com/dms/image/C5603AQFFExCa2WI0WQ/profile-displayphoto-shrink_400_400/0?e=1611187200&v=beta&t=16CiMdFw1JeC5bj3237MlOyWrLi7cBI90i-Wrv7oB5s"
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