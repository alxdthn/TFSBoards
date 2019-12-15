package com.alxdthn.tfsboards.ui.teams.view_model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alxdthn.tfsboards.base.LocalState
import com.alxdthn.tfsboards.base.BaseViewModel
import com.alxdthn.tfsboards.model.local.Board
import com.alxdthn.tfsboards.model.local.Team
import com.alxdthn.tfsboards.model.local.User
import com.alxdthn.tfsboards.model.responses.ItemResponse
import com.alxdthn.tfsboards.repositories.BoardRepository
import com.alxdthn.tfsboards.repositories.TeamRepository
import com.alxdthn.tfsboards.repositories.UserRepository
import com.alxdthn.tfsboards.ui.board.view_model.BoardViewModel
import com.alxdthn.tfsboards.utilities.AppColors
import com.alxdthn.tfsboards.utilities.AppConstants.AVATAR_SIZE
import com.alxdthn.tfsboards.utilities.AppConstants.AVATAR_URL
import com.alxdthn.tfsboards.utilities.extensions.removeFirstIf
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.net.URL
import javax.inject.Inject

class TeamsViewModel @Inject constructor(
	private val userRepository: UserRepository,
	private val teamRepository: TeamRepository,
	private val boardRepository: BoardRepository
) : BaseViewModel() {

	private val _teamsData = MutableLiveData<List<Team>>()
	val teamsData: LiveData<List<Team>> = _teamsData

	private val _avatarBitmap = MutableLiveData<Bitmap>()
	val avatarBitmap: LiveData<Bitmap> = _avatarBitmap

	private val _userData = MutableLiveData<User>()
	val userData: LiveData<User> = _userData

	val downloadingTeams = LocalState().withLog("downloading")
	val updatingData = LocalState().withLog("updating")

	val removingBoard = PublishSubject.create<Board>()
	val boardRemoved = PublishSubject.create<List<Team>>()
	val boardInserted = PublishSubject.create<List<Team>>()

	private var selectedBoard: Board? = null

	override fun initializer() {
		if (_userData.value == null) {
			downloadUser()
		}
		if (_teamsData.value == null) {
			downloadingTeams.start()
			downloadTeams()
		}
	}

	fun updateTeams() {
		updatingData.start()
		downloadTeams()
	}

	/**
	 * 		Network Part
	 */
	private fun downloadTeams() {
		teamRepository.getTeams()
			.subscribe({ preparedTeams ->
				_teamsData.value = preparedTeams
				if (downloadingTeams.isActive()) downloadingTeams.cancel()
				if (updatingData.isActive()) updatingData.cancel()
			}, { error ->
				handleError(error)
			}).addTo(getCompositeDisposable())
	}

	fun uploadBoard(name: String, idTeam: String?) {
		val newBoard = Board(
			name = name,
			idTeam = idTeam,
			color = AppColors.getRandColor()
		)

		boardRepository.addBoard(newBoard)
			.subscribe({ itemResponse ->
				addLocalBoard(newBoard, itemResponse)
				boardInserted.onNext(getTeams())
				showBoard(itemResponse.id)
			}, { error ->
				handleError(error)
			})
			.addTo(getCompositeDisposable())
	}

	fun removeBoard(idBoard: String) {
		boardRepository.removeBoard(idBoard)
			.subscribe({
				removeLocalBoard(idBoard)
				boardRemoved.onNext(getTeams())
			}, { error ->
				handleError(error)
			})
			.addTo(getCompositeDisposable())
	}

	private fun downloadAvatar(avatarHash: String) {
		Single.fromCallable { readImage(avatarHash) }
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.cache()
			.subscribe { bitmap ->
				_avatarBitmap.value = bitmap
			}
			.addTo(getCompositeDisposable())
	}

	private fun downloadUser() {
		userRepository.getUser()
			.subscribe({ userResponse ->
				_userData.value = User().fromResponse(userResponse)
				if (userResponse.avatarHash != null) {
					downloadAvatar(userResponse.avatarHash)
				}
			}, { error ->
				handleError(error)
			})
			.addTo(getCompositeDisposable())
	}

	/**
	 * 		Tools
	 */

	private fun findBoard(idBoard: String): Board {
		val teams = getTeams()

		teams.forEach { team ->
			team.boards.forEach { board ->
				if (board.id == idBoard) {
					return board
				}
			}
		}
		throw IllegalStateException("Missing board in teams")
	}

	private fun readImage(avatarHash: String): Bitmap? {
		var image: Bitmap? = null

		try {
			val input = URL("$AVATAR_URL/$avatarHash/$AVATAR_SIZE.png").openStream()

			image = BitmapFactory.decodeStream(input)
		} catch (e: Throwable) {
			e.printStackTrace()
		}
		return image
	}

	private fun removeLocalBoard(idBoard: String) {
		val teams = getTeams()
		teams.find { inTeams ->
			inTeams.boards.removeFirstIf { it.id == idBoard }
		}
	}

	private fun addLocalBoard(newBoard: Board, itemResponse: ItemResponse) {
		val teams = getTeams()
		val team = teams.find { it.id == newBoard.idTeam }
			?: throw IllegalStateException("Missing team id")

		newBoard.id = itemResponse.id
		team.boards.add(newBoard)
		_teamsData.value = teams
	}

	fun showBoard(idBoard: String) {
		BoardViewModel.start(selectBoard(idBoard))
	}

	fun startRemoveDialog(idBoard: String) {
		removingBoard.onNext(selectBoard(idBoard))
	}

	fun selectBoard(id: String): Board {
		val board = findBoard(id)
		selectedBoard = board
		return board
	}

	fun getTeams(): List<Team> {
		return teamsData.value ?: throw IllegalStateException("Missing items data")
	}
}
