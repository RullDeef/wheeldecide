package project.wheeldecide

import android.content.Context
import android.media.AudioManager
import android.media.SoundPool
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstrainScope
import androidx.constraintlayout.compose.ConstraintLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import project.wheeldecide.service.SoundPlayer
import project.wheeldecide.ui.components.AddOptionDialog
import project.wheeldecide.ui.components.DecisionView
import project.wheeldecide.ui.components.OptionsList
import project.wheeldecide.ui.components.Wheel
import project.wheeldecide.ui.theme.WheelDecideTheme
import project.wheeldecide.viewmodel.WheelFactory
import project.wheeldecide.viewmodel.WheelViewModel

class MainActivity : ComponentActivity(), SoundPlayer {

    private val wheelViewModel by viewModels<WheelViewModel> { WheelFactory(this) }

    private lateinit var audioManager: AudioManager
    private lateinit var soundPool: SoundPool
    private var spinSoundID: Int = 0
    private var stopSoundID: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        soundPool = SoundPool.Builder().setMaxStreams(10).build()
        spinSoundID = soundPool.load(applicationContext, R.raw.water_bubble, 1)
        stopSoundID = soundPool.load(applicationContext, R.raw.success, 1)

        setContent {
            WheelDecideTheme {
                MainView(wheelViewModel)
            }
        }
    }

    override suspend fun playWheelSpinSound() {
        withContext(Dispatchers.Main) {
            val volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC).toFloat()
            soundPool.play(spinSoundID, volume, volume, 1, 0, 1f)
        }
    }

    override suspend fun playWheelStopSound() {
        withContext(Dispatchers.Main) {
            val volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC).toFloat()
            soundPool.play(stopSoundID, volume, volume, 1, 0, 1f)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainView(wheelViewModel: WheelViewModel) {
    val addOptionDialogShown = remember { mutableStateOf(false) }

    val optionsList by wheelViewModel.optionList.collectAsState(listOf())
    val pies by wheelViewModel.pies.collectAsState(listOf())
    val offset by wheelViewModel.wheelOffset.collectAsState(0f)

    val state by wheelViewModel.state.collectAsState()
    val addOptionEnabled by remember { derivedStateOf { state == WheelViewModel.State.IDLE } }
    val decideEnabled by remember { derivedStateOf { state == WheelViewModel.State.IDLE } }
    val decisionVisible by remember { derivedStateOf { state == WheelViewModel.State.DECIDED } }

    val coroutineScope = rememberCoroutineScope()

    val side = 300.dp

    Scaffold(topBar = { TopAppBar(title = { TopAppbarTitle() }) }) { padding ->
        Column(
            Modifier
                .fillMaxWidth()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ConstraintLayout(
                Modifier
                    .height(side)
                    .fillMaxWidth()
            ) {
                val (wheelRef, decisionRef) = createRefs()
                Wheel(pies,
                    offset,
                    side,
                    Modifier
                        .size(side, side)
                        .constrainAs(wheelRef) { fillMax() })
                if (decisionVisible) {
                    DecisionView(wheelViewModel.lastDecision,
                        Modifier
                            .clickable { wheelViewModel.confirmDecisionViewed() }
                            .constrainAs(decisionRef) { fillMax() })
                }
            }
            Row(Modifier.padding(horizontal = 20.dp)) {
                PrettyButton(addOptionEnabled, Modifier.weight(1f), "add option") {
                    addOptionDialogShown.value = true
                }
                Spacer(Modifier.width(20.dp))
                PrettyButton(decideEnabled, Modifier.weight(1f), "decide!") {
                    coroutineScope.launch { wheelViewModel.decide() }
                }
            }
            OptionsList(Modifier, optionsList, addOptionEnabled) {
                wheelViewModel.removeOption(it)
            }
        }
    }

    AddOptionDialog(
        addOptionDialogShown.value,
        onCancel = { addOptionDialogShown.value = false },
        onOptionAdded = {
            wheelViewModel.addOption(it)
            addOptionDialogShown.value = false
        },
    )
}

@Composable
fun TopAppbarTitle() {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.titleLarge,
        )
    }
}

@Composable
fun PrettyButton(enabled: Boolean, modifier: Modifier, text: String, onClick: () -> Unit) {
    ElevatedButton(
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),
        modifier = modifier,
        enabled = enabled,
        onClick = onClick,
    ) {
        Text(text)
    }
}

fun ConstrainScope.fillMax() {
    top.linkTo(parent.top)
    bottom.linkTo(parent.bottom)
    start.linkTo(parent.start)
    end.linkTo(parent.end)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val wheelViewModel = WheelViewModel(object : SoundPlayer {
        override suspend fun playWheelSpinSound() {}
        override suspend fun playWheelStopSound() {}
    })
    wheelViewModel.addOption("pepegula")
    wheelViewModel.addOption("omega")
    wheelViewModel.addOption("wufallo")

    WheelDecideTheme {
        MainView(wheelViewModel)
    }
}