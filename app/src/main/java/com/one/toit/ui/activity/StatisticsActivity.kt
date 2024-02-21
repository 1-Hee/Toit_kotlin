package com.one.toit.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleObserver
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.one.toit.base.fatory.ApplicationFactory
import com.one.toit.base.ui.BaseComposeActivity
import com.one.toit.ui.compose.nav.StatisticsRoute
import com.one.toit.ui.compose.style.mono500
import com.one.toit.ui.compose.style.white
import com.one.toit.ui.compose.ui.page.WeeklyPage
import com.one.toit.data.viewmodel.TaskViewModel
import timber.log.Timber

class StatisticsActivity :  BaseComposeActivity(), LifecycleObserver{

    private lateinit var taskViewModel: TaskViewModel
    private lateinit var launcher: ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 액티비티 호출용 콜백 함수 런처
        launcher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { // 액티비티 종료시 결과릴 리턴받기 위한 콜백 함수
                result -> Timber.d("onActivityResult.......")
            if (result.resultCode == Activity.RESULT_OK) {
                Timber.i("OK......")
            }else if(result.resultCode == Activity.RESULT_CANCELED){
                Timber.i("CANCEL......")
            }
        }
        setContent {
            StatisticsScreenView(taskViewModel, launcher = launcher)
        }
    }
    override fun initViewModel() {
        super.initViewModel()
        val factory = ApplicationFactory(this.application)
        taskViewModel = getApplicationScopeViewModel(TaskViewModel::class.java, factory)
    }
}

@Composable
fun StatisticsScreenView(
    taskViewModel: TaskViewModel,
    launcher: ActivityResultLauncher<Intent>? = null
){
    val navController = rememberNavController()
    Scaffold(
        topBar = { StatisticsTopBarComponent() }
    ) {
        Box(Modifier.padding(it)){
            StatisticsNavGraph(navController, taskViewModel, launcher)
        }
    }
}

@Composable
fun StatisticsTopBarComponent()
{
    val activity = (LocalContext.current as? Activity)
    val context = LocalContext.current
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(48.dp)
        .background(white)
        .padding(horizontal = 16.dp, vertical = 12.dp)
    ){
        Icon(
            imageVector = Icons.Rounded.ArrowBack,
            contentDescription = "뒤로 가기",
            modifier = Modifier
                .width(24.dp)
                .height(24.dp)
                .align(Alignment.CenterStart)
                .clickable {
                    activity?.finish()
                },
            tint = mono500
        )
        // 타이틀
        Text(
            text = "상세 통계",
            style = MaterialTheme.typography.subtitle1
                .copy(
                    fontSize = 16.sp
                ),
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}

@Composable
fun StatisticsNavGraph(
    navController: NavHostController,
    taskViewModel: TaskViewModel,
    launcher: ActivityResultLauncher<Intent>? = null
) {
    NavHost(
        navController,
        startDestination = StatisticsRoute.Weekly.route
    )  {
        composable(route = StatisticsRoute.Weekly.route) {
            WeeklyPage(navController, taskViewModel, launcher)
        }
    }
}