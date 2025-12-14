package com.example.bilingualbuddy.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bilingualbuddy.ui.screen.HomeScreen
import com.example.bilingualbuddy.ui.screen.QuestionScreen
import com.example.bilingualbuddy.ui.screen.ResultScreen
import java.net.URLEncoder

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Question : Screen("question")
    object Result : Screen("result/{vietnameseSummary}/{koreanExplanation}/{pronunciation}") {
        fun createRoute(
            vietnameseSummary: String,
            koreanExplanation: String,
            pronunciation: String
        ): String {
            val encodedVietnamese = URLEncoder.encode(vietnameseSummary, "UTF-8")
            val encodedKorean = URLEncoder.encode(koreanExplanation, "UTF-8")
            val encodedPronunciation = URLEncoder.encode(pronunciation, "UTF-8")
            return "result/$encodedVietnamese/$encodedKorean/$encodedPronunciation"
        }
    }
}

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Home.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        
        composable(Screen.Question.route) {
            QuestionScreen(navController = navController)
        }
        
        composable(
            route = Screen.Result.route,
            arguments = listOf(
                navArgument("vietnameseSummary") { type = NavType.StringType },
                navArgument("koreanExplanation") { type = NavType.StringType },
                navArgument("pronunciation") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val vietnameseSummary = backStackEntry.arguments?.getString("vietnameseSummary") ?: ""
            val koreanExplanation = backStackEntry.arguments?.getString("koreanExplanation") ?: ""
            val pronunciation = backStackEntry.arguments?.getString("pronunciation") ?: ""
            
            ResultScreen(
                navController = navController,
                vietnameseSummary = vietnameseSummary,
                koreanExplanation = koreanExplanation,
                pronunciation = pronunciation
            )
        }
    }
}

