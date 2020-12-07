package com.professionalandroid.apps.miniproject_platfarm.emoticon_custom_view.modles

data class EmoticonData(
    val tabImage: MutableList<String>,    // tab에 표시될 이모티콘 tabImage[0] = color image, tabImage[1] = monochrome image
    val emoticon: MutableList<String>     // emoticon images
)