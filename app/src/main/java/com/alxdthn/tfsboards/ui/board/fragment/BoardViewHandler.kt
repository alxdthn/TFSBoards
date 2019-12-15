package com.alxdthn.tfsboards.ui.board.fragment

import com.woxthebox.draglistview.BoardView

interface BoardViewHandler : BoardView.BoardCallback, BoardView.BoardListener {
	override fun onItemDragEnded(fromColumn: Int, fromRow: Int, toColumn: Int, toRow: Int)
	override fun canDropItemAtPosition(p0: Int, p1: Int, newColumn: Int, newRow: Int): Boolean
	override fun canDragItemAtPosition(column: Int, dragPosition: Int): Boolean
	override fun onColumnDragStarted(position: Int) = Unit
	override fun onColumnDragEnded(position: Int) = Unit
	override fun onColumnDragChangedPosition(oldPosition: Int, newPosition: Int) = Unit
	override fun onItemDragStarted(column: Int, row: Int)
	override fun onItemChangedColumn(oldColumn: Int, newColumn: Int) = Unit
	override fun onItemChangedPosition(oldColumn: Int, oldRow: Int, newColumn: Int, newRow: Int) = Unit
	override fun onFocusedColumnChanged(oldColumn: Int, newColumn: Int) = Unit
}