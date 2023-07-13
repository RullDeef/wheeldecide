package project.wheeldecide.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import project.wheeldecide.service.SoundPlayer

class WheelFactory(private val soundPlayer: SoundPlayer): ViewModelProvider.Factory {

    override fun <T: ViewModel> create(modelClass: Class<T>): T = WheelViewModel(soundPlayer) as T
}