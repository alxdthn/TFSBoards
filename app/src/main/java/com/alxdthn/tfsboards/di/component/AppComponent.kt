package com.alxdthn.tfsboards.di.component

import android.content.Context
import com.alxdthn.tfsboards.di.module.NetworkModule
import com.alxdthn.tfsboards.di.module.StorageModule
import com.alxdthn.tfsboards.di.module.TrelloServicesModule
import com.alxdthn.tfsboards.di.module.ViewModelModule
import com.alxdthn.tfsboards.di.scope.AppScope
import com.alxdthn.tfsboards.main.MainActivity
import com.alxdthn.tfsboards.ui.auth.AuthFragment
import com.alxdthn.tfsboards.ui.board.fragment.BoardFragment
import com.alxdthn.tfsboards.ui.card.fragment.CardFragment
import com.alxdthn.tfsboards.ui.filter.FilterFragment
import com.alxdthn.tfsboards.ui.members.MembersFragment
import com.alxdthn.tfsboards.ui.teams.fragment.TeamsFragment
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component(
	modules = [
		StorageModule::class,
		ViewModelModule::class,
		NetworkModule::class,
		TrelloServicesModule::class
	]
)
interface AppComponent {

	fun inject(mainActivity: MainActivity)

	fun inject(authFragment: AuthFragment)

	fun inject(teamsFragment: TeamsFragment)

	fun inject(boardFragment: BoardFragment)

	fun inject(cardFragment: CardFragment)

	fun inject(membersFragment: MembersFragment)

	fun inject(filterFragment: FilterFragment)

	@Component.Builder
	interface Builder {

		@BindsInstance
		fun application(appContext: Context): Builder

		fun build(): AppComponent
	}
}