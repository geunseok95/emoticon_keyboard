package com.professionalandroid.apps.miniproject_platfarm.emoticon_custom_view.modles

data class GiphyResponse (
    val data: List<Datum>,
    val pagination: Pagination,
    val meta: Meta
)

data class Datum (
    val type: String,
    val id: String,
    val url: String,
    val slug: String,
    val bitlyGIFURL: String,
    val bitlyURL: String,
    val embedURL: String,
    val username: String,
    val source: String,
    val title: String,
    val rating: String,
    val contentURL: String,
    val sourceTLD: String,
    val sourcePostURL: String,
    val isSticker: Long,
    val importDatetime: String,
    val trendingDatetime: String,
    val images: Images,
    val analyticsResponsePayload: String,
    val analytics: Analytics,
    val user: User
)

data class Analytics (
    val onload: Onclick,
    val onclick: Onclick,
    val onsent: Onclick
)

data class Onclick (
    val url: String
)

data class Images (
    val original: FixedHeight,
    val downsized: The480_WStill,
    val downsized_large: The480_WStill,
    val downsized_medium: The480_WStill,
    val downsized_small: The4_K,
    val downsized_still: The480_WStill,
    val fixed_height: FixedHeight,
    val fixed_height_downsampled: FixedHeight,
    val fixed_height_small: FixedHeight,
    val fixed_height_small_still: The480_WStill,
    val fixed_height_still: The480_WStill,
    val fixed_width: FixedHeight,
    val fixed_width_downsampled: FixedHeight,
    val fixed_width_small: FixedHeight,
    val fixed_width_small_still: The480_WStill,
    val fixed_width_still: The480_WStill,
    val looping: Looping,
    val original_still: The480_WStill,
    val original_mp4: The4_K,
    val preview: The4_K,
    val preview_gif: The480_WStill,
    val preview_webp: The480_WStill,
    val the480WStill: The480_WStill,
    val hd: The4_K? = null,
    val the4K: The4_K? = null
)

data class The480_WStill (
    val height: String,
    val width: String,
    val size: String,
    val url: String
)

data class The4_K (
    val height: String,
    val width: String,
    val mp4Size: String,
    val mp4: String
)

data class FixedHeight (
    val height: String,
    val width: String,
    val size: String,
    val url: String,
    val mp4Size: String? = null,
    val mp4: String? = null,
    val webpSize: String,
    val webp: String,
    val frames: String? = null,
    val hash: String? = null
)

data class Looping (
    val mp4Size: String,
    val mp4: String
)

data class User (
    val avatarURL: String,
    val bannerImage: String,
    val bannerURL: String,
    val profileURL: String,
    val username: String,
    val displayName: String,
    val description: String,
    val instagramURL: String,
    val websiteURL: String,
    val isVerified: Boolean
)

data class Meta (
    val status: Long,
    val msg: String,
    val responseID: String
)

data class Pagination (
    val totalCount: Long,
    val count: Long,
    val offset: Long
)
