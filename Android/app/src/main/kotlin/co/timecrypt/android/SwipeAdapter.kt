package co.timecrypt.android

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * The pager adapter being used in the [MessageActivity].
 */
class SwipeAdapter(val manager: FragmentManager) : FragmentPagerAdapter(manager) {

    private fun ensureFragment() {
        // TODO
    }

    override fun getItem(position: Int): Fragment {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCount(): Int {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}