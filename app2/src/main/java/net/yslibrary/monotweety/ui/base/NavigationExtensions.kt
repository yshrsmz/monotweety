package net.yslibrary.monotweety.ui.base

import androidx.navigation.NavController
import androidx.navigation.NavDirections

/**
 * Check if the current destination can handle the provided [direction].
 *
 * https://stackoverflow.com/questions/51060762/java-lang-illegalargumentexception-navigation-destination-xxx-is-unknown-to-thi
 */
fun NavController.navigateSafe(direction: NavDirections) {
    val action =
        currentDestination?.getAction(direction.actionId) ?: graph.getAction(direction.actionId)
    if (action != null && currentDestination?.id != action.destinationId) {
        navigate(direction)
    }
}
