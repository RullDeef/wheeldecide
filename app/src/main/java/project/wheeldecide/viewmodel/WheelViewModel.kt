package project.wheeldecide.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import project.wheeldecide.model.WheelConfig
import project.wheeldecide.service.SoundPlayer
import kotlin.random.Random

class WheelViewModel(private val soundPlayer: SoundPlayer) : ViewModel() {

    enum class State { IDLE, DECIDING, DECIDED }

    private val _wheelConfig = MutableStateFlow(WheelConfig())

    val optionList = _wheelConfig.asStateFlow().map { it.optionsList }
    private var currentOptionsLen: Int = 0

    val pies = _wheelConfig.asStateFlow().map { it.pies }

    private var _wheelOffset = MutableStateFlow(0f)
    val wheelOffset = _wheelOffset.asStateFlow()

    private val _state = MutableStateFlow(State.IDLE)
    val state = _state.asStateFlow()

    private var _lastDecision: String? = null
    val lastDecision: String
        get() = _lastDecision ?: throw Exception("no decision was made")

    fun addOption(option: String) {
        _wheelConfig.update { conf ->
            conf.addOption(option).also { currentOptionsLen = it.optionsList.size }
        }
    }

    fun removeOption(index: Int) {
        _wheelConfig.update { conf ->
            conf.removeOption(index).also { currentOptionsLen = it.optionsList.size }
        }
    }

    suspend fun decide() {
        if (currentOptionsLen == 0) {
            throw Exception("no options selected!")
        }

        val maxSpeed = 360f + 90f * Random.nextFloat()
        val maxTime = 5f + 1f * Random.nextFloat() // seconds
        val acceleration = 2f * maxSpeed / maxTime
        var speed = 0f
        val deltaTime = 0.1f // seconds

        var soundCounter: Float = 180f / currentOptionsLen

        // start wheel moving
        _state.update { State.DECIDING }

        // increase speed
        while (speed < maxSpeed) {
            _wheelOffset.update { it + speed * deltaTime }
            speed += acceleration * deltaTime

            soundCounter += speed * deltaTime
            if (soundCounter > 360f / currentOptionsLen) {
                soundCounter -= 360f / currentOptionsLen
                soundPlayer.playWheelSpinSound()
            }

            delay((deltaTime * 1000f).toLong())
        }

        // decrease speed
        while (speed > 0) {
            _wheelOffset.update { it + speed * deltaTime }
            speed -= acceleration * deltaTime

            soundCounter += speed * deltaTime
            if (soundCounter > 360f / currentOptionsLen) {
                soundCounter -= 360f / currentOptionsLen
                soundPlayer.playWheelSpinSound()
            }

            delay((deltaTime * 1000f).toLong())
        }

        // produce decide value
        soundPlayer.playWheelStopSound()
        _lastDecision = _wheelConfig.value.chooseAtAngle(_wheelOffset.value)
        _state.update { State.DECIDED }
    }

    fun confirmDecisionViewed() {
        _wheelOffset.update { 0f }
        _state.update { State.IDLE }
    }
}