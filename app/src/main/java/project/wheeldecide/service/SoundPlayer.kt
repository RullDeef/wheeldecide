package project.wheeldecide.service

interface SoundPlayer {
    suspend fun playWheelSpinSound()
    suspend fun playWheelStopSound()
}