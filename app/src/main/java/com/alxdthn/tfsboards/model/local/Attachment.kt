package com.alxdthn.tfsboards.model.local

import com.alxdthn.tfsboards.model.responses.AttachmentResponse
import com.alxdthn.tfsboards.model.responses.PreviewResponse
import java.io.Serializable

data class Attachment(
	var id: String = "?",
	var bytes: Long = 0,
	var date: String = "",
	var edgeColor: String = "",
	var idMember: String = "",
	var isUploaded: Boolean = false,
	var mimeType: String = "",
	var name: String = "",
	var url: String = "",
	var pos: Int = 0,
	var previews: List<Preview> = emptyList()
) {
	fun fromResponse(attachmentResponse: AttachmentResponse): Attachment {
		return this.apply {
			id = attachmentResponse.id
			bytes = attachmentResponse.bytes
			date = attachmentResponse.date
			edgeColor = attachmentResponse.edgeColor
			idMember = attachmentResponse.idMember
			isUploaded = attachmentResponse.isUploaded
			mimeType = attachmentResponse.mimeType
			name = attachmentResponse.name
			url = attachmentResponse.url
			pos = attachmentResponse.pos
			previews = attachmentResponse.previews.map { Preview().fromResponse(it) }
		}
	}

	data class Preview(
		var id: String = "?",
		var scaled: Boolean = false,
		var url: String = "",
		var bytes: Long = 0,
		var height: Int = 0,
		var width: Int = 0
	) {
		fun fromResponse(previewResponse: PreviewResponse): Preview {
			return this.apply {
				id = previewResponse.id
				scaled = previewResponse.scaled
				url = previewResponse.url
				bytes = previewResponse.bytes
				height = previewResponse.height
				width = previewResponse.width
			}
		}
	}
}