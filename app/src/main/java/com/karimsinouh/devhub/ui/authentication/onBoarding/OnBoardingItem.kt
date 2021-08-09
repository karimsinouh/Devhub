package com.karimsinouh.devhub.ui.authentication.onBoarding

import com.karimsinouh.devhub.R

class OnBoardingItem(
    val title:Int,
    val text:Int,
    val image:Int,
) {

    companion object{

        fun get():List<OnBoardingItem>{
            return listOf(
                OnBoardingItem(R.string.onBoardingTitle1,R.string.onBoardingText1,R.drawable.ic_onboarding1),
                OnBoardingItem(R.string.onBoardingTitle2,R.string.onBoardingText2,R.drawable.ic_onboarding2),
                OnBoardingItem(R.string.onBoardingTitle3,R.string.onBoardingText3,R.drawable.ic_onboarding3),
            )
        }

    }

}