package com.professionalandroid.apps.miniproject_platfarm.emoticon_custom_view.modles

data class EmoticonData(
    val tabImage: List<Int>,    // tab에 표시될 이모티콘 tabImage[0] = color image, tabImage[1] = monochrome image
    val emoticon: List<Int>     // emoticon images
)