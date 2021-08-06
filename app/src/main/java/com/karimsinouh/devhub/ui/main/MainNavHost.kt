package com.karimsinouh.devhub.ui.main

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.pager.ExperimentalPagerApi
import com.karimsinouh.devhub.ui.createNew.CreateNew
import com.karimsinouh.devhub.ui.editPost.EditPost
import com.karimsinouh.devhub.ui.editProfile.EditProfile
import com.karimsinouh.devhub.ui.home.Home
import com.karimsinouh.devhub.ui.profile.Profile
import com.karimsinouh.devhub.ui.search.Search
import com.karimsinouh.devhub.ui.notifications.Notifications
import com.karimsinouh.devhub.ui.viewHashtags.ViewHashtags
import com.karimsinouh.devhub.ui.viewPost.ViewPost
import com.karimsinouh.devhub.ui.viewPost.ViewPostViewModel
import com.karimsinouh.devhub.ui.viewProfile.ViewProfile
import com.karimsinouh.devhub.ui.viewUsersList.ViewUsersList
import com.karimsinouh.devhub.utils.Screen
import com.karimsinouh.devhub.utils.Screen.EditProfile.route
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalPagerApi
@ExperimentalFoundationApi
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainNavHost(
    controller:NavHostController,
    vm:MainViewModel
){
    AnimatedNavHost(
        navController = controller,
        startDestination = Screen.Home.route
    ){

        composable(
            route=Screen.Home.route,
            enterTransition = {_,_->
                slideInHorizontally(initialOffsetX = {1000})
            },
            exitTransition = {_,_->
                slideOutHorizontally(targetOffsetX = {-1000})
            },
            popEnterTransition = {_,_->
                slideInHorizontally(initialOffsetX = {-1000})
            },
            popExitTransition = {_,_->
                slideOutHorizontally(targetOffsetX = {1000})
            },

            ){
            Home(nav = controller, vm = vm)
        }

        composable(route=Screen.Search.route,
            enterTransition = {_,_->
                slideInHorizontally(initialOffsetX = {1000})
            },
            exitTransition = {_,_->
                slideOutHorizontally(targetOffsetX = {-1000})
            },
            popEnterTransition = {_,_->
                slideInHorizontally(initialOffsetX = {-1000})
            },
            popExitTransition = {_,_->
                slideOutHorizontally(targetOffsetX = {1000})
            },
        ){
            Search(nav = controller)
        }

        composable(route=Screen.Profile.route,
            enterTransition = {_,_->
                slideInHorizontally(initialOffsetX = {1000})
            },
            exitTransition = {_,_->
                slideOutHorizontally(targetOffsetX = {-1000})
            },
            popEnterTransition = {_,_->
                slideInHorizontally(initialOffsetX = {-1000})
            },
            popExitTransition = {_,_->
                slideOutHorizontally(targetOffsetX = {1000})
            },
        ){
            Profile(nav = controller, vm = vm)
        }

        composable(route=Screen.Notifications.route,
            enterTransition = {_,_->
                slideInHorizontally(initialOffsetX = {1000})
            },
            exitTransition = {_,_->
                slideOutHorizontally(targetOffsetX = {-1000})
            },
            popEnterTransition = {_,_->
                slideInHorizontally(initialOffsetX = {-1000})
            },
            popExitTransition = {_,_->
                slideOutHorizontally(targetOffsetX = {1000})
            },
        ){
            Notifications(nav = controller, vm = vm)
        }

        composable(
            route=Screen.EditProfile.route,
            enterTransition = {_,_->
                slideInHorizontally(initialOffsetX = {1000})
            },
            exitTransition = {_,_->
                slideOutHorizontally(targetOffsetX = {-1000})
            },
            popEnterTransition = {_,_->
                slideInHorizontally(initialOffsetX = {-1000})
            },
            popExitTransition = {_,_->
                slideOutHorizontally(targetOffsetX = {1000})
            },

            ){
            EditProfile(controller)
        }

        composable(
            route=Screen.CreateNew.route,
            enterTransition = {_,_->
                slideInHorizontally(initialOffsetX = {1000})
            },
            exitTransition = {_,_->
                slideOutHorizontally(targetOffsetX = {-1000})
            },
            popEnterTransition = {_,_->
                slideInHorizontally(initialOffsetX = {-1000})
            },
            popExitTransition = {_,_->
                slideOutHorizontally(targetOffsetX = {1000})
            },

            ){
            CreateNew(nav = controller)
        }

        composable(
            route = Screen.ViewPost.route,
            arguments = listOf(navArgument("postId"){
                type= NavType.StringType
            }),
            enterTransition = {_,_->
                slideInHorizontally(initialOffsetX = {1000})
            },
            exitTransition = {_,_->
                slideOutHorizontally(targetOffsetX = {-1000})
            },
            popEnterTransition = {_,_->
                slideInHorizontally(initialOffsetX = {-1000})
            },
            popExitTransition = {_,_->
                slideOutHorizontally(targetOffsetX = {1000})
            },

            ){
            val postId=it.arguments?.getString("postId")
            //val viewPostViewModel= hiltViewModel<ViewPostViewModel>()
            ViewPost(nav = controller, postId = postId?:"",)
        }

        composable(
            route=Screen.ViewProfile.route,
            arguments = listOf(navArgument("uid"){type= NavType.StringType}),
            enterTransition = {_,_->
                slideInHorizontally(initialOffsetX = {1000})
            },
            exitTransition = {_,_->
                slideOutHorizontally(targetOffsetX = {-1000})
            },
            popEnterTransition = {_,_->
                slideInHorizontally(initialOffsetX = {-1000})
            },
            popExitTransition = {_,_->
                slideOutHorizontally(targetOffsetX = {1000})
            },

            ){
            val uid=it.arguments?.getString("uid")!!
            ViewProfile(nav = controller, uid = uid)
        }

        composable(
            route = Screen.ViewUsersList.route,
            arguments = listOf(
                navArgument("uid"){type= NavType.StringType},
                navArgument("action"){type=NavType.IntType}
            ),
            enterTransition = {_,_->
                slideInHorizontally(initialOffsetX = {1000})
            },
            exitTransition = {_,_->
                slideOutHorizontally(targetOffsetX = {-1000})
            },
            popEnterTransition = {_,_->
                slideInHorizontally(initialOffsetX = {-1000})
            },
            popExitTransition = {_,_->
                slideOutHorizontally(targetOffsetX = {1000})
            },

            ){
            val uid=it.arguments?.getString("uid")!!
            val action=it.arguments?.getInt("action")!!
            ViewUsersList(nav = controller, uid = uid, action =action)
        }

        composable(
            route = Screen.EditPost.route,
            arguments = listOf(navArgument("postId"){type=NavType.StringType}),
            enterTransition = {_,_->
                slideInHorizontally(initialOffsetX = {1000})
            },
            exitTransition = {_,_->
                slideOutHorizontally(targetOffsetX = {-1000})
            },
            popEnterTransition = {_,_->
                slideInHorizontally(initialOffsetX = {-1000})
            },
            popExitTransition = {_,_->
                slideOutHorizontally(targetOffsetX = {1000})
            },

            ){
            val postId=it.arguments?.getString("postId")!!
            EditPost(postId = postId, nav =controller)
        }


        composable(
            route = Screen.ViewHashtags.route,
            arguments= listOf(
                navArgument("hashtag"){NavType.StringType}
            ),
            enterTransition = {_,_->
                slideInHorizontally(initialOffsetX = {1000})
            },
            exitTransition = {_,_->
                slideOutHorizontally(targetOffsetX = {-1000})
            },
            popEnterTransition = {_,_->
                slideInHorizontally(initialOffsetX = {-1000})
            },
            popExitTransition = {_,_->
                slideOutHorizontally(targetOffsetX = {1000})
            },
        ){
            val hashtag=it.arguments?.getString("hashtag")!!
            ViewHashtags(nav = controller, _hashtag = hashtag)
        }

    }
}