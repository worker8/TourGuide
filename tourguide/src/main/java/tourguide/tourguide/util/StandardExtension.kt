package tourguide.tourguide.util

/**
 * example usage:
 * ```
 *
 *   var recipe: String? = null
 *   var uri: Boolean? = null
 *
 *   recipe.let(uri) { recipe, uri ->
 *     // safe usage here
 *   }
 * }
 * ```
 */
inline fun <T, T2, R> T?.letWith(secondArg: T2?, block: (T, T2) -> R): R? {
    return this?.let {
        secondArg?.let {
            block(this, secondArg)
        }
    }
}