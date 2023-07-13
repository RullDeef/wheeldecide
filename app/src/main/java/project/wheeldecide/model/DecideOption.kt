package project.wheeldecide.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DecideOption(
    val title: String,
) : Parcelable
