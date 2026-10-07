package com.example

import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Timeline
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import com.example.data.local.AppDatabase
import com.example.data.model.Grade
import com.example.data.model.Question
import com.example.data.repository.StudyRepository
import com.example.service.LockScreenService
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.KnowledgeBaseScreen
import com.example.ui.screens.LearningPathScreen
import com.example.ui.screens.UnlockQuizScreen
import com.example.ui.screens.UnlockResultScreen
import com.example.ui.screens.WrongBookScreen
import com.example.ui.theme.PrimaryStudyTheme

enum class MainTab(val title: String) {
    HOME("解锁关卡"),
    LEARNING_PATH("学情路径"),
    WRONG_BOOK("错题精析"),
    KNOWLEDGE("知识题库")
}

sealed class ScreenState {
    object MainTabScreen : ScreenState()
    data class QuizSession(val customQuestions: List<Question>? = null) : ScreenState()
    data class QuizResult(val isSuccess: Boolean, val correct: Int, val total: Int) : ScreenState()
}

class MainActivity : ComponentActivity() {

    private val forceQuizTrigger = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        configureLockScreenWindowFlags()

        val database = AppDatabase.getDatabase(this)
        val repository = StudyRepository(
            wrongQuestionDao = database.wrongQuestionDao(),
            unlockRecordDao = database.unlockRecordDao(),
            learningPathDao = database.learningPathDao()
        )

        // Check if opened from lock screen trigger
        checkIntentForForceQuiz(intent)

        // Auto start lock screen service if not running
        startLockScreenService()

        setContent {
            PrimaryStudyTheme {
                MainAppContainer(
                    repository = repository,
                    forceQuizRequested = forceQuizTrigger.value,
                    onForceQuizConsumed = { forceQuizTrigger.value = false }
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        checkIntentForForceQuiz(intent)
    }

    private fun checkIntentForForceQuiz(intent: Intent?) {
        if (intent?.getBooleanExtra("FORCE_UNLOCK_QUIZ", false) == true) {
            forceQuizTrigger.value = true
        }
    }

    private fun configureLockScreenWindowFlags() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
            val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as? KeyguardManager
            keyguardManager?.requestDismissKeyguard(this, null)
        } else {
            @Suppress("DEPRECATION")
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
            )
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private fun startLockScreenService() {
        val serviceIntent = Intent(this, LockScreenService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }
    }

    private fun stopLockScreenService() {
        val serviceIntent = Intent(this, LockScreenService::class.java)
        stopService(serviceIntent)
    }

    fun toggleLockScreenService(enabled: Boolean) {
        if (enabled) {
            startLockScreenService()
        } else {
            stopLockScreenService()
        }
    }
}

@Composable
fun MainAppContainer(
    repository: StudyRepository,
    forceQuizRequested: Boolean,
    onForceQuizConsumed: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedGrade by remember { mutableStateOf(Grade.GRADE_4) }
    var currentTab by remember { mutableStateOf(MainTab.HOME) }
    var screenState by remember { mutableStateOf<ScreenState>(ScreenState.MainTabScreen) }
    var isDeviceUnlocked by remember { mutableStateOf(false) }
    var isAutoPopupEnabled by remember { mutableStateOf(true) }

    // When phone unlocked broadcast triggers, launch force quiz immediately
    LaunchedEffect(forceQuizRequested) {
        if (forceQuizRequested) {
            isDeviceUnlocked = false
            screenState = ScreenState.QuizSession(null)
            onForceQuizConsumed()
        }
    }

    // Android back handler handling
    BackHandler(enabled = screenState !is ScreenState.MainTabScreen || currentTab != MainTab.HOME) {
        if (screenState !is ScreenState.MainTabScreen) {
            // If device is not yet unlocked, do not dismiss quiz casually
            if (isDeviceUnlocked) {
                screenState = ScreenState.MainTabScreen
            }
        } else if (currentTab != MainTab.HOME) {
            currentTab = MainTab.HOME
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing,
        bottomBar = {
            if (screenState is ScreenState.MainTabScreen) {
                NavigationBar(
                    modifier = Modifier.testTag("main_bottom_nav")
                ) {
                    NavigationBarItem(
                        selected = currentTab == MainTab.HOME,
                        onClick = { currentTab = MainTab.HOME },
                        icon = {
                            Icon(
                                imageVector = if (currentTab == MainTab.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                                contentDescription = "解锁关卡"
                            )
                        },
                        label = { Text("解锁关卡") },
                        modifier = Modifier.testTag("nav_item_home")
                    )

                    NavigationBarItem(
                        selected = currentTab == MainTab.LEARNING_PATH,
                        onClick = { currentTab = MainTab.LEARNING_PATH },
                        icon = {
                            Icon(
                                imageVector = if (currentTab == MainTab.LEARNING_PATH) Icons.Filled.Timeline else Icons.Outlined.Timeline,
                                contentDescription = "学情路径"
                            )
                        },
                        label = { Text("学情路径") },
                        modifier = Modifier.testTag("nav_item_learning_path")
                    )

                    NavigationBarItem(
                        selected = currentTab == MainTab.WRONG_BOOK,
                        onClick = { currentTab = MainTab.WRONG_BOOK },
                        icon = {
                            Icon(
                                imageVector = if (currentTab == MainTab.WRONG_BOOK) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                                contentDescription = "错题回顾"
                            )
                        },
                        label = { Text("错题回顾") },
                        modifier = Modifier.testTag("nav_item_wrong_book")
                    )

                    NavigationBarItem(
                        selected = currentTab == MainTab.KNOWLEDGE,
                        onClick = { currentTab = MainTab.KNOWLEDGE },
                        icon = {
                            Icon(
                                imageVector = if (currentTab == MainTab.KNOWLEDGE) Icons.Filled.MenuBook else Icons.Outlined.MenuBook,
                                contentDescription = "知识题库"
                            )
                        },
                        label = { Text("知识题库") },
                        modifier = Modifier.testTag("nav_item_knowledge")
                    )
                }
            }
        }
    ) { innerPadding ->
        when (val state = screenState) {
            is ScreenState.MainTabScreen -> {
                when (currentTab) {
                    MainTab.HOME -> {
                        HomeScreen(
                            selectedGrade = selectedGrade,
                            onGradeChange = { selectedGrade = it },
                            repository = repository,
                            isDeviceUnlocked = isDeviceUnlocked,
                            isAutoPopupEnabled = isAutoPopupEnabled,
                            onToggleAutoPopup = { enabled ->
                                isAutoPopupEnabled = enabled
                                (context as? MainActivity)?.toggleLockScreenService(enabled)
                            },
                            onStartUnlockQuiz = {
                                screenState = ScreenState.QuizSession(null)
                            },
                            onLockDevice = {
                                isDeviceUnlocked = false
                            },
                            onNavigateToWrongBook = {
                                currentTab = MainTab.WRONG_BOOK
                            },
                            onNavigateToKnowledgeBase = {
                                currentTab = MainTab.KNOWLEDGE
                            },
                            onNavigateToLearningPath = {
                                currentTab = MainTab.LEARNING_PATH
                            },
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                    MainTab.LEARNING_PATH -> {
                        LearningPathScreen(
                            currentGrade = selectedGrade,
                            repository = repository,
                            onStartPractice = { customQuestions ->
                                screenState = ScreenState.QuizSession(customQuestions)
                            },
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                    MainTab.WRONG_BOOK -> {
                        WrongBookScreen(
                            repository = repository,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                    MainTab.KNOWLEDGE -> {
                        KnowledgeBaseScreen(
                            currentGrade = selectedGrade,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }

            is ScreenState.QuizSession -> {
                UnlockQuizScreen(
                    grade = selectedGrade,
                    repository = repository,
                    customQuestions = state.customQuestions,
                    onUnlockFinished = { isSuccess, correct, total ->
                        if (isSuccess) {
                            isDeviceUnlocked = true
                        }
                        screenState = ScreenState.QuizResult(isSuccess, correct, total)
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }

            is ScreenState.QuizResult -> {
                UnlockResultScreen(
                    isSuccess = state.isSuccess,
                    correctCount = state.correct,
                    totalCount = state.total,
                    grade = selectedGrade,
                    onContinue = {
                        screenState = ScreenState.MainTabScreen
                        currentTab = MainTab.HOME
                    },
                    onRetry = {
                        screenState = ScreenState.QuizSession(null)
                    },
                    onGoToWrongBook = {
                        screenState = ScreenState.MainTabScreen
                        currentTab = MainTab.WRONG_BOOK
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}
