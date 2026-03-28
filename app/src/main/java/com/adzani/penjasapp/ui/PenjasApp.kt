package com.adzani.penjasapp.ui

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.adzani.penjasapp.data.PenjasViewModel
import com.adzani.penjasapp.data.SchoolClass
import com.adzani.penjasapp.data.Student

private object Routes {
    const val Home = "home"
    const val ClassDetail = "class/{classId}/{className}"
    const val StudentDetail = "student/{studentId}"

    fun classDetail(classId: Long, className: String): String =
        "class/$classId/${Uri.encode(className)}"

    fun studentDetail(studentId: Long): String = "student/$studentId"
}

@Composable
fun PenjasApp(viewModel: PenjasViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.Home) {
        composable(Routes.Home) {
            HomeScreen(
                viewModel = viewModel,
                onOpenClass = { classInfo: SchoolClass ->
                    navController.navigate(Routes.classDetail(classInfo.id, classInfo.name))
                },
            )
        }
        composable(
            route = Routes.ClassDetail,
            arguments = listOf(
                navArgument("classId") { type = NavType.LongType },
                navArgument("className") { type = NavType.StringType },
            ),
        ) { backStackEntry ->
            val classId = backStackEntry.arguments?.getLong("classId") ?: 0L
            val className = backStackEntry.arguments?.getString("className").orEmpty()
            ClassDetailScreen(
                classId = classId,
                className = className,
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onOpenStudent = { student: Student ->
                    navController.navigate(Routes.studentDetail(student.id))
                },
            )
        }
        composable(
            route = Routes.StudentDetail,
            arguments = listOf(navArgument("studentId") { type = NavType.LongType }),
        ) { backStackEntry ->
            val studentId = backStackEntry.arguments?.getLong("studentId") ?: 0L
            StudentDetailScreen(
                studentId = studentId,
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
            )
        }
    }
}
