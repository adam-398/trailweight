package dev.auroralaboratories.trailweight.otherutils

/**
 * Checks the validity of a password.
 * @param password The password to check.
 * @return A string containing an error message if the password is invalid, or null if it is valid.
 */
fun passwordChecker(password: String): String? {
    return when {
        password.length < 8 -> "Password must be at least 8 characters"
        !password.any { it.isUpperCase() } -> "Password must contain at least one uppercase letter"
        !password.any { it.isLowerCase() } -> "Password must contain at least one lowercase letter"
        !password.any { it.isDigit() } -> "Password must contain at least one number"
        else -> null
    }
}