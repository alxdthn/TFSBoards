package com.alxdthn.tfsboards.ui.card.fragment.card_attachments_adapter

import android.view.View
import com.alxdthn.tfsboards.base.BaseItemsHandler
import com.alxdthn.tfsboards.model.AttachmentFileItem
import com.alxdthn.tfsboards.model.AttachmentImageItem
import com.alxdthn.tfsboards.model.Item
import com.alxdthn.tfsboards.model.local.Attachment
import com.alxdthn.tfsboards.ui.card.fragment.CardFragment
import com.alxdthn.tfsboards.utilities.ItemToUi
import io.reactivex.subjects.PublishSubject

@Suppress("UNCHECKED_CAST")
class CardAttachmentsItemsHandler(
	main: CardFragment
) : BaseItemsHandler(
	main.getCompositeDisposable(),
	CardAttachmentsDiffCallback(),
	CardAttachmentsAdapter()
) {

	val onAttachmentClick = PublishSubject.create<ItemToUi>()

	init {
		(adapter as CardAttachmentsAdapter).itemsHandler = this
	}

	override fun renderer(data: Iterable<Any>): List<Item> {
		val items = mutableListOf<Item>()
		val attachments = data as List<Attachment>

		attachments.forEach { attachment ->
			val item = if (attachment.previews.isNotEmpty()) {
				AttachmentImageItem().render(attachment)
			} else {
				AttachmentFileItem().render(attachment)
			}
			items.add(item)
		}
		return items
	}

	override fun onResult(result: List<Item>) = Unit

	override fun onItemClick(item: Item, view: View) {
		onAttachmentClick.onNext(ItemToUi(item, view))
	}
}