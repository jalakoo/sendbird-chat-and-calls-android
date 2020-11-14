[![platform](https://img.shields.io/badge/platform-Android-yellow.svg)](https://www.android.com)
[![Languages](https://img.shields.io/badge/kotlin-%230095D5.svg)](https://kotlinlang.org/)
[![API](https://img.shields.io/badge/API-26%2B-brightgreen.svg?style=plastic)](https://android-arsenal.com/api?level=26)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://github.com/sendbird/quickstart-calls-ios/blob/develop/LICENSE.md)

## Introduction
This repo contains sample code for implementing chat and voice calls with Sendbird's UIKit. It is broken up into the following sub-folders:
- **1_start**/ contains a slightly modified `Login Activity` starter app
- **2_chat**/ contains the above app with Sendbird's UIKit w/ chat support
- **3_calls**/ contains the Sendbird's Calls SDK coupled with the UIKit code from folder 2

### Requirements
The minimum requirements for running the included samples apps are:

- An active [Sendbird](https://dashboard.sendbird.com/auth/signup) account
- An Android development IDE like [Android Studio 4.0+](https://developer.android.com/studio/)

### Getting Started
1. Create or select an application from your Sendbird Dashboard
2. Add users to the selected application*

** If you have a .csv of users, they can be uploaded using a python script from this [repo](https://github.com/jalakoo/sendbird_bulk_user_uploader). Alternatively, you can add any number of random test users with the python script[here](https://github.com/jalakoo/sendbird_random_users) 


## Reference
For further information see the following docs:
- [Sendbird Chat SDK](https://sendbird.com/docs/chat/v3/android/getting-started/about-chat-sdk)
- [Sendbird UIKit SDK](https://sendbird.com/docs/uikit/v1/android/getting-started/about-uikit)
- [Sendbird Calls SDK](https://sendbird.com/docs/calls/v1/android/getting-started/about-calls-sdk)

## License
See the LICENSE.md file
