package com.alxdthn.tfsboards.ui.filter.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.alxdthn.tfsboards.R
import com.alxdthn.tfsboards.base.BaseAdapter
import com.alxdthn.tfsboards.common.CardViewHolderBinder
import com.alxdthn.tfsboards.model.CardItem

@Suppress("UNCHECKED_CAST")
class FilterCardAdapter : BaseAdapter() {

	lateinit var itemsHandler: FilterItemsHandler

	override fun getItems() = itemsHandler.items as List<CardItem>

	private val layout = R.layout.layout_board_card_item

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
		val view = LayoutInflater.from(parent.context).inflate(layout, parent, false) as CardView

		view.radius = 10f
		return FilterCardViewHolder(view)
	}

	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		if (holder is FilterCardViewHolder) {
			val item = getItems()[position]

			holder.bindTo(getItems()[position])
			holder.itemView.setOnClickListener { view ->
				itemsHandler.onItemClick(item, view)
			}
		}
	}

	override fun onBindViewHolderWithPayloads(
		holder: RecyclerView.ViewHolder,
		payloads: MutableList<Any?>
	) {
		if (holder is FilterCardViewHolder) {
			holder.bindTo(payloads)
		}
	}

	class FilterCardViewHolder(v: View) : RecyclerView.ViewHolder(v) {

		private val binder = CardViewHolderBinder()

		fun bindTo(cardItem: CardItem) {
			binder(itemView, cardItem)
		}

		fun bindTo(payloads: List<Any?>) {
			binder(itemView, payloads)
		}
	}
}