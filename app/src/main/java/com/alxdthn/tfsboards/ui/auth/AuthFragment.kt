package com.alxdthn.tfsboards.ui.auth

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.alxdthn.tfsboards.R
import com.alxdthn.tfsboards.base.BaseFragment
import com.alxdthn.tfsboards.di.component.AppComponent
import com.alxdthn.tfsboards.utilities.AppAnimator
import com.alxdthn.tfsboards.utilities.AppConstants.PREPARED_AUTH_URL
import com.alxdthn.tfsboards.utilities.extensions.*
import kotlinx.android.synthetic.main.fragment_auth.*
import kotlinx.android.synthetic.main.layout_start.*

class AuthFragment : BaseFragment(R.layout.fragment_auth) {

	private val viewModel by viewModels<AuthViewModel> { viewModelFactory }

	override fun initViewModel() = Unit

	override fun initializeUi() {
		initializeWebView()
		btnLogin.setOnClickListener {
			viewModel.onLogin.start()
		}
	}

	override fun initializeObservers() {
		viewModel.onLogin.observe(viewLifecycleOwner, Observer { onLogin ->
			if (onLogin) {
				AppAnimator show wvAuthWebView
				AppAnimator hide startLayout
			} else {
				wvAuthWebView.invisible()
				startLayout.visible()
			}
		})
		viewModel.onLogin.observe(viewLifecycleOwner, Observer { accessGranted ->
			wvAuthWebView goneIf accessGranted
		})
	}

	@SuppressLint("SetJavaScriptEnabled")
	private fun initializeWebView() {
		wvAuthWebView.apply {
			settings.javaScriptEnabled = true
			webViewClient = AuthWebClient(viewModel)
			loadUrl(PREPARED_AUTH_URL)
		}
	}

	class AuthWebClient(private val viewModel: AuthViewModel) : WebViewClient() {
		override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
			viewModel.handleUrl(url)
		}
	}

	override fun inject(injector: AppComponent) = injector.inject(this)
}