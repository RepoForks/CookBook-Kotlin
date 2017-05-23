package com.simoncherry.cookbookkotlin.ui.activity

import android.R
import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import com.simoncherry.cookbookkotlin.mvp.presenter.BasePresenter
import com.simoncherry.cookbookkotlin.ui.BaseView
import com.simoncherry.cookbookkotlin.util.ToastUtils

/**
 * <pre>
 *     author : Donald
 *     e-mail : xxx@xx
 *     time   : 2017/05/22
 *     desc   :
 *     version: 1.0
 * </pre>
 */
abstract class BaseActivity<in V : BaseView, T : BasePresenter<V>> : AppCompatActivity(), BaseView {
    var mPresenter: T? = null
    lateinit var mContext: Activity
    var mProgressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayout())
        mContext = this
        initComponent()
        mPresenter?.attachView(this as V)
    }

    protected abstract fun getLayout(): Int
    protected abstract fun initComponent()

    override fun onDestroy() {
        super.onDestroy()
        mPresenter?.detachView()
    }

    private fun initProgressBar() {
        mProgressBar = ProgressBar(this, null, R.attr.progressBarStyleLarge)
        val layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.gravity = Gravity.CENTER
        addContentView(mProgressBar, layoutParams)
    }

    override fun onShowProgressBar() {
        if (mProgressBar == null) {
            initProgressBar()
        }
        mProgressBar?.isIndeterminate = true
        mProgressBar?.visibility = View.VISIBLE
    }

    override fun onHideProgressBar() {
        mProgressBar?.visibility = View.GONE
    }

    override fun onShowToast(msg: String) {
        ToastUtils.show(mContext, msg)
    }
}