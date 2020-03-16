package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.activities.activity_check
import com.example.myapplication.adapters.ChatAdapter
import com.example.myapplication.model.ChatImageMessage
import com.example.myapplication.model.ChatImageUrlMessage
import com.example.myapplication.model.ChatMessage
import com.example.myapplication.model.ChatTextMessage
import com.snapchat.kit.sdk.SnapLogin
import com.snapchat.kit.sdk.bitmoji.OnBitmojiSearchFocusChangeListener
import com.snapchat.kit.sdk.bitmoji.OnBitmojiSelectedListener
import com.snapchat.kit.sdk.bitmoji.ui.BitmojiFragment
import com.snapchat.kit.sdk.bitmoji.ui.BitmojiFragmentSearchMode
import com.snapchat.kit.sdk.bitmoji.ui.BitmojiIconFragment
import com.snapchat.kit.sdk.core.controller.LoginStateController.OnLoginStateChangedListener
import com.snapchat.kit.sdk.login.models.UserDataResponse
import com.snapchat.kit.sdk.login.networking.FetchUserDataCallback


class TestAppActivity : AppCompatActivity(), OnBitmojiSelectedListener,
    OnBitmojiSearchFocusChangeListener, OnEditorActionListener, OnGlobalLayoutListener,
    OnLoginStateChangedListener {
    private var btn: Button? =  null
    private val mAdapter = ChatAdapter()
    private var mContentView: View? = null
    private var mBitmojiContainer: View? = null
    private var mFriendmojiToggle: View? = null
    private var mTextField: EditText? =  null
  //  private var mChatView: RecyclerView? = null
    private var mBitmojiFragment: BitmojiFragment? = null
    private var mBitmojiContainerHeight = 0
    private var mBaseRootViewHeightDiff = 0
    private var mBitmojisSent = 0
    private var mIsBitmojiVisible = true
    private var mShowingFriendmoji = false
    private var mMyExternalId: String? = null
    private   var mChatView:RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("Main Activity","OnCreate start")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_app)
        val sendButton =
            findViewById<Button>(R.id.send_button)
        mContentView = findViewById(R.id.content_view)
        mBitmojiContainer = findViewById(R.id.sdk_container)
        mTextField =findViewById(R.id.input_field)
        mFriendmojiToggle = findViewById(R.id.friendmoji_toggle)
        mChatView =findViewById(R.id.chat)
        btn = findViewById(R.id.save)
        val mChatViewLocal: RecyclerView? = mChatView
        val mTextFieldLocal: EditText = mTextField as EditText

        val mContentViewLocal: View = mContentView as View




        mBitmojiContainerHeight =
            resources.getDimensionPixelSize(R.dimen.bitmoji_container_height)

        mAdapter.setHasStableIds(true)
        if (mChatViewLocal != null) {
            Log.i("Main Activity","OnCreate myadapter start")
            mChatViewLocal.layoutManager = LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, true /* reverseLayout*/
            )
            Log.i("Main Activity","OnCreate myadapter end")
        }
        if (mChatViewLocal != null) {
            Log.i("Main Activity","OnCreate mchatviewlocal start")
            mChatViewLocal.adapter = mAdapter
            Log.i("Main Activity","OnCreate mchatviewlocal end")
        }
        mTextFieldLocal.setOnEditorActionListener(this)
        btn!!.setOnClickListener(View.OnClickListener {
            Log.i("Main Activity","OnCreate btn start")
            val intent = Intent(this@TestAppActivity, activity_check::class.java)
            startActivity(intent)
            Log.i("Main Activity","OnCreate btn end")
        })
        mTextFieldLocal.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                Log.i("Main Activity","OnCreate hasfocus start")
                setBitmojiVisible(true)
                Log.i("Main Activity","OnCreate hasfocus end")
            }
        }
        mTextFieldLocal.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
                Log.i("Main Activity","OnCreate beforeTextchange start")// no-op
            }


            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) { // no-op
                Log.i("Main Activity","onTextchange start")
            }

            override fun afterTextChanged(s: Editable) {
                Log.i("Main Activity","OnCreate afterTextChaged start")
                sendButton.isEnabled = s.length > 0
                if (mBitmojiFragment != null) {
                    mBitmojiFragment!!.setSearchText(
                        s.toString(),
                        BitmojiFragmentSearchMode.SEARCH_RESULT_ONLY
                    )
                }
                Log.i("Main Activity","OnCreate  afterTextChanged end")
            }
        })
        mFriendmojiToggle!!.setOnClickListener(View.OnClickListener {
            Log.i("Main Activity", "OnCreate mFriendmojiToggl start")
            val fragment =
                supportFragmentManager.findFragmentById(R.id.sdk_container)
            if (fragment is BitmojiFragment) {
                fragment.setFriend(if (mShowingFriendmoji) null else mMyExternalId)
            }
            mShowingFriendmoji = !mShowingFriendmoji
            Log.i("Main Activity","OnCreate  mFriendmojiToggl end")
        })
        sendButton.setOnClickListener { sendText() }
        findViewById<View>(R.id.unlink_button).setOnClickListener {
            SnapLogin.getAuthTokenManager(
                this@TestAppActivity
            ).clearToken()
        }
        findViewById<View>(R.id.bitmoji_button).setOnClickListener {
            Log.i("Main Activity","OnCreate bitmoji_button start")
            if (currentFocus !== mTextField) {
                setBitmojiVisible(!mIsBitmojiVisible)

            }
            Log.i("Main Activity","OnCreate bitmoji_button end")
            defocusInput()
        }
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        SnapLogin.getLoginStateController(this).addOnLoginStateChangedListener(this)
        mContentViewLocal.getViewTreeObserver().addOnGlobalLayoutListener(this)
        mTextFieldLocal.requestFocus()
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.sdk_container, BitmojiFragment.builder()
                    .withShowSearchBar(true)
                    .withShowSearchPills(true)
                    .withTheme(R.style.MyBitmojiTheme)
                    .build()
            )
            .commit()
        supportFragmentManager.beginTransaction()
            .replace(R.id.bitmoji_button, BitmojiIconFragment())
            .commit()
        if (SnapLogin.isUserLoggedIn(this)) {
            loadExternalId()
        }
        Log.i("Main Activity","OnCreate end")
    }

    override fun onAttachFragment(fragment: Fragment) {
        Log.i("Main Activity","onAttachFragment start")
        super.onAttachFragment(fragment)
        if (fragment is BitmojiFragment) { // Set the reference to the sticker picker fragment
// This should be done here in order to ensure that the reference is valid
// even when the containing activity or fragment is recreated
            mBitmojiFragment = fragment
        } else if (fragment is BitmojiIconFragment) { // Attach the icon to the sticker picker to enable displaying a preview of
// results from BitmojiFragment#setSearchText()
// Note: This assumes that the icon is attached after the sticker picker
//       Ensure that the order is correct when adding these
            if (mBitmojiFragment != null) {
                mBitmojiFragment!!.attachBitmojiIcon(fragment)
            }
        }
        Log.i("Main Activity","onAttachFragment end")
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        Log.i("Main Activity","dispatchTouchEvent start")
        val hitRect = Rect()
        mChatView!!.getHitRect(hitRect)
        if (hitRect.contains(event.x.toInt(), event.y.toInt())) {
            defocusInput()
            setBitmojiVisible(false)
        }
        Log.i("Main Activity","dispatchTouchEvent end")
        return super.dispatchTouchEvent(event)

    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onGlobalLayout() {
        Log.i("Main Activity","onGlobalLayout start")
        val heightDiff = getRootViewHeightDiff(mContentView)
        if (heightDiff > resources.getDimensionPixelSize(R.dimen.min_keyboard_height)) {
            val params =
                mBitmojiContainer!!.layoutParams as LinearLayout.LayoutParams
            mContentView!!.viewTreeObserver.removeOnGlobalLayoutListener(this)
            mBitmojiContainerHeight = heightDiff - mBaseRootViewHeightDiff
            params.height = mBitmojiContainerHeight
            mBitmojiContainer!!.layoutParams = params
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        } else {
            mBaseRootViewHeightDiff = heightDiff
        }
        Log.i("Main Activity","onGlobalLayout end")
    }

    override fun onBitmojiSelected(
        imageUrl: String,
        previewDrawable: Drawable
    ) {
        Log.i("Main Activity","onBitmojiSelected start")
        handleBitmojiSend(imageUrl, previewDrawable)
        Log.i("Main Activity","onBitmojiSelected end")
    }

    override fun onBitmojiSearchFocusChange(hasFocus: Boolean) {
        Log.i("Main Activity","onBitmojiSearchFocusChange start")
        window.setSoftInputMode(if (hasFocus) WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE else WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        val params =
            mBitmojiContainer!!.layoutParams as LinearLayout.LayoutParams
        // Set container height to 90% of available space when focused
        params.weight =
            if (hasFocus) BITMOJI_CONTAINER_FOCUS_WEIGHT else 0F
        params.height = if (hasFocus) 0 else mBitmojiContainerHeight
        mBitmojiContainer!!.layoutParams = params
        Log.i("Main Activity","onBitmojiSearchFocusChange end")
    }

    override fun onLoginSucceeded() {
        Log.i("Main Activity","onLoginSucceeded start")
        loadExternalId()
        Log.i("Main Activity","onLoginSucceeded end")
    }

    override fun onLoginFailed() { // no-op
        Log.i("Main Activity","onLoginFailed start")
    }

    override fun onLogout() { // no-op
        Log.i("Main Activity","onLogout start")
    }

    override fun onEditorAction(
        textView: TextView,
        actionId: Int,
        event: KeyEvent
    ): Boolean {
        Log.i("Main Activity","onEditorAction start")
        if (actionId == EditorInfo.IME_ACTION_SEND) {
            sendText()
            textView.requestFocus()
            return true
        }
        Log.i("Main Activity","onEditorAction end")
        return false
    }

    private fun loadExternalId() {
        Log.i("Main Activity","loadExternalId start")
        SnapLogin.fetchUserData(
            this,
            EXTERNAL_ID_QUERY,
            null,
            object : FetchUserDataCallback {
                override fun onSuccess(userDataResponse: UserDataResponse?) {
                    if (userDataResponse == null || userDataResponse.hasError()) {
                        return
                    }
                    mMyExternalId = userDataResponse.data.me.externalId
                    mFriendmojiToggle!!.visibility = View.VISIBLE
                }

                override fun onFailure(
                    isNetworkError: Boolean,
                    statusCode: Int
                ) { // handle error
                }
            })
        Log.i("Main Activity","loadExternalId end")
    }

    private fun setBitmojiVisible(isBitmojiVisible: Boolean) {
        Log.i("Main Activity","setBitmojiVisible start")
        mIsBitmojiVisible = isBitmojiVisible
        mBitmojiContainer!!.visibility = if (isBitmojiVisible) View.VISIBLE else View.GONE
        Log.i("Main Activity","setBitmojiVisible end")
    }

    private fun defocusInput() {
        Log.i("Main Activity","defocusInput start")
        val currentFocus = currentFocus ?: return
        val imm =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        currentFocus.clearFocus()
        Log.i("Main Activity","defocusInput end")
    }

    private fun handleBitmojiSend(imageUrl: String, previewDrawable: Drawable) {
        Log.i("Main Activity","handleBitmojiSend start")
        sendMessage(ChatImageUrlMessage(true /*isFromMe*/, imageUrl, previewDrawable))
        if (mBitmojisSent == 0) {
            sendDelayedMessage(ChatTextMessage(false /*isFromMe*/, "Woah, nice Bitmoji!"), 500)
            sendDelayedMessage(ChatImageMessage(false /*isFromMe*/, R.drawable.looking_good), 1000)
        } else if (mBitmojisSent == 1) {
            sendDelayedMessage(ChatImageMessage(false /*isFromMe*/, R.drawable.party_time), 1000)
        } else if (mBitmojisSent == 2) {
            sendDelayedMessage(ChatTextMessage(false /*isFromMe*/, "lol"), 500)
        } else if (mBitmojisSent == 14) {
            sendDelayedMessage(ChatImageMessage(false /*isFromMe*/, R.drawable.chill), 1000)
        }
        mBitmojisSent++
        Log.i("Main Activity","handleBitmojiSend end")
    }

    private fun sendText() {
        Log.i("Main Activity","sendText start")
        val text = mTextField!!.text.toString()
        if (TextUtils.isEmpty(text)) {
            return
        }
        sendMessage(ChatTextMessage(true /*isFromMe*/, text))
        mTextField!!.setText("")
        Log.i("Main Activity","sendText end")
    }

    private fun sendMessage(message: ChatMessage) {
        Log.i("Main Activity","sendMessage start")
        mAdapter.add(message)
        mChatView!!.scrollToPosition(0)
        Log.i("Main Activity","sendMessage end")
    }

    private fun sendDelayedMessage(message: ChatMessage, delayMs: Long) {
        Log.i("Main Activity","sendDelayedMessage start")
        mContentView!!.postDelayed({ sendMessage(message) }, delayMs)
        Log.i("Main Activity","sendDelayedMessage end")
    }

    companion object {

        private const val BITMOJI_CONTAINER_FOCUS_WEIGHT = 9.0f
        private const val EXTERNAL_ID_QUERY = "{me{externalId}}"
        private const val REQUEST_STORAGE_PERMISSION = 101
        private fun getRootViewHeightDiff(view: View?): Int {
            Log.i("Main Activity","getRootViewHeightDiff start")
            return view!!.rootView.height - view.height
        }
    }
}
