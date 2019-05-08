package tourguide.tourguide

import android.view.View

/**
 * Created by HieuPT on 5/18/2019.
 */
data class ViewHole(val view: View, val config: Config) {

    override fun equals(other: Any?): Boolean {
        return if (other is ViewHole) {
            view === other.view
        } else
            false
    }

    override fun hashCode(): Int {
        var result = view.hashCode()
        result = 31 * result + config.hashCode()
        return result
    }
}