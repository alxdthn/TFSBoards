package com.alxdthn.tfsboards.ui.teams.fragment.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.alxdthn.tfsboards.model.*
import com.alxdthn.tfsboards.ui.teams.fragment.adapter.touch_helper.TouchHelperViewHolder
import com.alxdthn.tfsboards.utilities.AppCommon
import kotlinx.android.synthetic.main.layout_teams_list_header.view.*
import kotlinx.android.synthetic.main.layout_teams_list_item.view.*

sealed class TeamsViewHolder {
	class BoardViewHolder(v: View) : RecyclerView.ViewHolder(v), TouchHelperViewHolder {
		fun bindTo(boardItem: BoardItem) {
			bindName(boardItem.name)
			bindImage(boardItem.url, boardItem.color)
			itemView.translationX = 0f
		}

		fun bindTo(payloads: List<Any?>) {
			payloads.forEach { payload ->
				when (payload) {
					is NamePayload -> bindName(payload.name)
					is ImagePayload -> bindImage(payload.url, payload.color)
				}
			}
		}

		private fun bindImage(url: String?, color: Int) {
			if (url == null) {
				itemView.ivBoardItemImage.setImageDrawable(null)
				itemView.ivBoardItemImage.setBackgroundColor(color)
			} else {
				AppCommon.setImageBackground(itemView.ivBoardItemImage, url, color)
			}
		}

		private fun bindName(name: String) {
			itemView.txvBoardItemName.text = name
		}

		override fun onItemSelected() {
			itemView.translationZ = 10f
		}

		override fun onItemClear() {
			itemView.translationZ = 0f
		}
	}

	class HeaderViewHolder(v: View) : RecyclerView.ViewHolder(v) {
		fun bindTo(headerItem: HeaderItem) {
			bindName(headerItem.name)
			bindInfo(headerItem.countOfBoards.toString())
		}

		fun bindTo(payloads: List<Any?>) {
			payloads.forEach { payload ->
				when (payload) {
					is NamePayload -> bindName(payload.name)
					is InfoPayload -> bindInfo(payload.text)
				}
			}
		}

		private fun bindName(name: String) {
			itemView.txvGroupHeaderName.text = name
		}

		private fun bindInfo(info: String) {
			itemView.txvGroupHeaderItemCount.text = info
		}
	}


}