package com.suguna.lbsalesperson.Utils

import android.Manifest


object LBConstant {

    @kotlin.jvm.JvmField
    var URL_TYPE: Int = 1

    @kotlin.jvm.JvmField
    val SALES_HEAD: String = "1"
    @kotlin.jvm.JvmField
    val SALES_EXECUTIVE = "0"
    internal var PREF_FILE_NAME = "LBApp_Patient"

    var SERVER_NOT_REACH = "Server Not Reachable"

    // App Screen Titles
    //var SALES_HEAD = "1"




    // Date/Time Formats
    var DD_MM_YY = "dd-MM-yyyy" // 06-12-1993
    var YY_MM_DD = "yyyy-MM-dd" // 1993-12-06
    var YY_MM_DD_SLASH = "yyyy/MM/dd" // 1993/12/06
    var DD_MMM_YY = "dd MMM yyyy" // 06 Dec 1993
    var MMM_YYYY = "MMM yyyy" // Dec 1993
    var DD_MMM_YY_ZONE = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'" // 2019-03-07T16:00:00.000-05:00
    var HH_MM = "HH:mm" // 20:30
    var HH_MM_AA = "hh:mm aa" // 10:30 AM

    var DATE_FORMAT_FROM_SERVER = "yyyy-MM-dd hh:mm:ss"
    var DATE_FORMAT_FROM_SERVER_1 = "MMM dd, yyyy @ hh:mm aa"
    var DATE_FORMAT_FROM_SERVER_CURRENT_DAY = " @ hh:mm a"

    var CAMERA = Manifest.permission.CAMERA
    var WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE
    var READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE
    var RECORD_AUDIO = Manifest.permission.RECORD_AUDIO
    var MODIFY_AUDIO_SETTINGS = Manifest.permission.MODIFY_AUDIO_SETTINGS
//    var CONTACTS = Manifest.permission.READ_CONTACTS

    // 1 - NewsFeed, 2 - Friends feed, 3 - Club feed
    var SourceType: String = "SourceType"
    var Video_Play_State: String = "VideoPlayState"
    var SelectedClubList: String = "selectedclublist"
    var VIDEO_SEEK_POSITION_PUBLIC: String = "video_seek_position_public"
    var VIDEO_SEEK_POSITION_FRIENDS: String = "video_seek_position_friends"
    var VIDEO_SEEK_POSITION_CLUB: String = "video_seek_position_club"
    var VIDEO_SEEK_POSITION_PROFILE: String = "video_seek_position_profile"
    var VIDEO_SEEK_POSITION_CLUB_DETAIL: String = "video_seek_position_club_detail"
    var VIDEO_SEEK_POSITION_SEARCH: String = "video_seek_position_search"

    // Push Notification
    var PUSH_FEED_TYPE = "share"
    var PUSH_CHAT_TYPE = "chat_message"
}
