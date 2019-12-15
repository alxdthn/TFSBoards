package com.alxdthn.tfsboards.ui.teams.fragment.adapter

import android.view.View
import com.alxdthn.tfsboards.base.BaseItemsHandler
import com.alxdthn.tfsboards.model.BoardItem
import com.alxdthn.tfsboards.model.HeaderItem
import com.alxdthn.tfsboards.model.Item
import com.alxdthn.tfsboards.model.local.Team
import com.alxdthn.tfsboards.ui.teams.fragment.TeamsFragment
import com.alxdthn.tfsboards.ui.teams.fragment.adapter.touch_helper.TouchHelperInterface
import com.alxdthn.tfsboards.utilities.AppDiffUtil
import com.alxdthn.tfsboards.utilities.extensions.getOrientation
import com.alxdthn.tfsboards.utilities.extensions.indexOf
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

@Suppress("UNCHECKED_CAST")
class TeamsItemsHandler(
	main: TeamsFragment
) : BaseItemsHandler(main.getCompositeDisposable(), TeamsDiffCallback(), TeamsAdapter()),
	TouchHelperInterface {

	private val viewModel = main.viewModel
	private val orientation = main.getOrientation()

	val onBoardMove = PublishSubject.create<Boolean>()
	val onBoardClick = PublishSubject.create<String>()
	val onBoardSwipe = PublishSubject.create<String>()

	private lateinit var itemMover: TeamsItemMover

	init {
		(adapter as TeamsAdapter).itemsHandler = this
	}

	override fun renderer(data: Iterable<Any>): List<Item> {
		val newItems = mutableListOf<Item>()
		val teams = data as List<Team>

		teams.forEach { team ->
			if (team.boards.isNotEmpty()) {
				val header = HeaderItem().render(team)

				newItems.add(header)
				team.boards.forEach { board ->
					newItems.add(BoardItem().render(board, orientation))
				}
			}
		}
		return newItems
	}

	override fun onItemMoveStart(position: Int) {
		val item = items[position]

		onBoardMove.onNext(true)
		itemMover = TeamsItemMover(viewModel.getTeams() as MutableList<Team>, item.id)
	}

	override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
		val dir = toPosition - fromPosition

		itemMover.move(dir)
		return true
	}

	override fun onItemMoveEnd() {
		itemMover.acceptMove()
		onBoardMove.onNext(false)
		render(viewModel.getTeams())
	}

	override fun onItemSwipe(position: Int): Boolean {
		val id = items[position].id

		onBoardSwipe.onNext(id)
		return true
	}

	override fun onItemClick(item: Item, view: View) {
		onBoardClick.onNext(item.id)
	}

	fun notifyItemChanged(idItem: String) {
		adapter.notifyItemChanged(items.indexOf { it.id == idItem })
	}

	override fun onResult(result: List<Item>) = Unit
}