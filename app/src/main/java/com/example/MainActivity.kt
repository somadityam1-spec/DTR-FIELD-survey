package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.ReportEntity
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ReportDetailScreen
import com.example.ui.screens.ReportFormScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.ReportViewModel

enum class AppScreen {
    HOME,
    FORM,
    DETAIL
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp(
    viewModel: ReportViewModel = viewModel()
) {
    val context = LocalContext.current
    var currentScreen by remember { mutableStateOf(AppScreen.HOME) }
    var selectedReportForDetail by remember { mutableStateOf<ReportEntity?>(null) }
    val toastMessage by viewModel.toastMessage.collectAsState()

    LaunchedEffect(toastMessage) {
        toastMessage?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }

    BackHandler(enabled = currentScreen != AppScreen.HOME) {
        currentScreen = AppScreen.HOME
    }

    AnimatedContent(
        targetState = currentScreen,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "ScreenTransition"
    ) { screen ->
        when (screen) {
            AppScreen.HOME -> {
                HomeScreen(
                    viewModel = viewModel,
                    onAddNewReport = {
                        viewModel.initNewReport()
                        currentScreen = AppScreen.FORM
                    },
                    onReportClick = { report ->
                        selectedReportForDetail = report
                        currentScreen = AppScreen.DETAIL
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
            AppScreen.FORM -> {
                ReportFormScreen(
                    viewModel = viewModel,
                    onBack = { currentScreen = AppScreen.HOME },
                    onSaved = { currentScreen = AppScreen.HOME },
                    modifier = Modifier.fillMaxSize()
                )
            }
            AppScreen.DETAIL -> {
                val report = selectedReportForDetail
                if (report != null) {
                    ReportDetailScreen(
                        report = report,
                        onBack = { currentScreen = AppScreen.HOME },
                        onEdit = {
                            viewModel.loadReportForEdit(report)
                            currentScreen = AppScreen.FORM
                        },
                        onDelete = {
                            viewModel.deleteReport(report)
                            currentScreen = AppScreen.HOME
                        },
                        onDuplicate = {
                            viewModel.duplicateReport(report)
                            currentScreen = AppScreen.HOME
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    currentScreen = AppScreen.HOME
                }
            }
        }
    }
}
