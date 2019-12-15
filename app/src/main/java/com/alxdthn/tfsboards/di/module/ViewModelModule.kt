package com.alxdthn.tfsboards.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alxdthn.tfsboards.di.ViewModelFactory
import com.alxdthn.tfsboards.di.key.ViewModelKey
import com.alxdthn.tfsboards.main.MainViewModel
import com.alxdthn.tfsboards.ui.auth.AuthViewModel
import com.alxdthn.tfsboards.ui.board.view_model.BoardViewModel
import com.alxdthn.tfsboards.ui.card.view_model.CardViewModel
import com.alxdthn.tfsboards.ui.filter.FilterViewModel
import com.alxdthn.tfsboards.ui.members.MembersViewModel
import com.alxdthn.tfsboards.ui.teams.view_model.TeamsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {

	@Binds
	internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

	@Binds
	@IntoMap
	@ViewModelKey(MainViewModel::class)
	internal abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel

	@Binds
	@IntoMap
	@ViewModelKey(AuthViewModel::class)
	internal abstract fun bindAuthViewModel(viewModel: AuthViewModel): ViewModel

	@Binds
	@IntoMap
	@ViewModelKey(TeamsViewModel::class)
	internal abstract fun bindTeamsViewModel(viewModel: TeamsViewModel): ViewModel

	@Binds
	@IntoMap
	@ViewModelKey(BoardViewModel::class)
	internal abstract fun bindBoardViewModel(viewModel: BoardViewModel): ViewModel

	@Binds
	@IntoMap
	@ViewModelKey(CardViewModel::class)
	internal abstract fun bindCardViewModel(viewModel: CardViewModel): ViewModel

	@Binds
	@IntoMap
	@ViewModelKey(MembersViewModel::class)
	internal abstract fun bindBoardMembersViewModel(viewModel: MembersViewModel): ViewModel

	@Binds
	@IntoMap
	@ViewModelKey(FilterViewModel::class)
	internal abstract fun bindFilterViewModel(viewModel: FilterViewModel): ViewModel
}