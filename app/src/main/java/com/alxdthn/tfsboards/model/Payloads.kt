package com.alxdthn.tfsboards.model

import android.text.Spanned

class ImagePayload(val url: String?, val color: Int)
class NamePayload(val name: String)
class UrlPayload(val url: String)
class InfoPayload(val text: String)
class AvatarPayload(val hash: String?, val color: Int)
class DescriptionPayload(val text: Spanned?)
class TimePayload(val text: String)
class ImagePreviewPayload(val url: String?)