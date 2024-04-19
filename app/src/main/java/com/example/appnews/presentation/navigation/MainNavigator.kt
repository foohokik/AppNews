package com.example.appnews.presentation.navigation

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import com.example.appnews.presentation.MainActivity
import com.github.terrakok.cicerone.BackTo
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.github.terrakok.cicerone.androidx.FragmentScreen
import kotlin.random.Random

class MainNavigator(
    private val mainActivity: MainActivity,
    mainActivityIdContainer: Int
) : AppNavigator(mainActivity, mainActivityIdContainer) {

    //стек фрагментов для вкладок навигейшн бара, 1 вкладка может содержать в себе множество фрагментов
    private var tabFragmentStack = hashMapOf<PagesType, MutableList<String>>()
    private var currentPagesType: PagesType? = null
    private var lastPagesType: PagesType? = null

    //общий стек фрагментов
    private var stackTab = arrayListOf<PagesType>()

    override fun commitNewFragmentScreen(screen: FragmentScreen, addToBackStack: Boolean) {
        val transaction = fragmentManager.beginTransaction()
        transaction.setReorderingAllowed(true)
        if (screen is MainFragmentScreen && stackTab.contains(screen.pagesType)) {
            if (currentPagesType != screen.pagesType) {
                stackTab.remove(screen.pagesType)
                stackTab.add(screen.pagesType)
                lastPagesType = currentPagesType
                currentPagesType = screen.pagesType
                val fLast =
                    fragmentManager.findFragmentByTag(tabFragmentStack[lastPagesType]?.lastOrNull())
                hide(fLast, transaction)
                val fCurrent =
                    fragmentManager.findFragmentByTag(
                        tabFragmentStack[currentPagesType]?.lastOrNull()
                    )
                show(fCurrent, transaction)
                transaction.commit()
                mainActivity.setSelectedBottomNavigationTab(currentPagesType)
            } else {
                return
            }
            return
        }

        val fragment = screen.createFragment(fragmentFactory)

        if (checkoutBottomNavigationTabFragmentTag(screen.screenKey) && screen is MainFragmentScreen) {
            transaction.add(containerId, fragment, screen.screenKey)
            hideFragment(transaction, currentPagesType)
            lastPagesType = currentPagesType
            currentPagesType = screen.pagesType
            stackTab.add(screen.pagesType)
            tabFragmentStack[screen.pagesType] = arrayListOf(screen.screenKey)
            mainActivity.setSelectedBottomNavigationTab(currentPagesType)
        } else {
            val tag = "${screen.screenKey}_${currentPagesType?.name}_${Random(Int.MAX_VALUE)}"
            transaction.add(containerId, fragment, tag)
            hideFragment(transaction, currentPagesType)
            tabFragmentStack[currentPagesType]?.add(tag)
        }
        transaction.commit()
    }

    override fun back() {
        val lastIsTab = tabFragmentStack[currentPagesType]?.size?.let { s -> s <= 1 } ?: false
        val transaction = fragmentManager.beginTransaction()

        when {
            lastIsTab && stackTab.size <= 1 -> {
                mainActivity.finishAffinity()
            }

            lastIsTab -> {
                removeAndDetachBackFragmentAndShowCurrent(
                    transaction,
                    getTagFromPage(currentPagesType),
                    tabFragmentStack[lastPagesType]?.lastOrNull(),
                )
                stackTab.remove(currentPagesType)
                tabFragmentStack.remove(currentPagesType)
                currentPagesType = lastPagesType
                lastPagesType = stackTab.getOrNull(stackTab.lastIndex - 1)
                mainActivity.setSelectedBottomNavigationTab(currentPagesType)
            }

            else -> {
                val popTag = tabFragmentStack[currentPagesType]?.removeLast()
                removeAndDetachBackFragmentAndShowCurrent(
                    transaction,
                    popTag,
                    tabFragmentStack[currentPagesType]?.lastOrNull(),
                )
            }
        }
        transaction.commit()
    }

    override fun backTo(command: BackTo) {
        val screen = command.screen
        when {
            screen is MainFragmentScreen && screen.pagesType == PagesType.HEADLINE -> {
                replaceFragmentByPagesTypeAndTag(
                    PagesType.HEADLINE,
                    NavigationKeys.HEADLINES_TAB_FRAGMENT
                )
            }

            screen is MainFragmentScreen && screen.pagesType == PagesType.SAVE -> {
                replaceFragmentByPagesTypeAndTag(PagesType.SAVE, NavigationKeys.SAVE_TAB_FRAGMENT)
            }

            screen is MainFragmentScreen && screen.pagesType == PagesType.SOURCE -> {
                replaceFragmentByPagesTypeAndTag(
                    PagesType.SOURCE,
                    NavigationKeys.SOURCE_TAB_FRAGMENT
                )
            }
        }
    }

    private fun hide(
        fLast: Fragment?,
        transaction: FragmentTransaction,
    ) {
        fLast?.let { f ->
            transaction.hide(f)
            transaction.setMaxLifecycle(f, Lifecycle.State.STARTED)
        }
    }

    private fun hideFragment(transaction: FragmentTransaction, pagesType: PagesType?) {
        val hideFragmentTag = tabFragmentStack[pagesType]?.lastOrNull()
        val hideFragment = fragmentManager.findFragmentByTag(hideFragmentTag)
        hide(hideFragment, transaction)
    }

    private fun removeAndDetachBackFragmentAndShowCurrent(
        transaction: FragmentTransaction,
        backTag: String?,
        currentTag: String?,
    ) {
        val backFragment = fragmentManager.findFragmentByTag(backTag)
        fragmentManager.fragments.remove(backFragment)
        backFragment?.let(transaction::remove)
        val newCurrentFragment = fragmentManager.findFragmentByTag(currentTag)
        show(newCurrentFragment, transaction)
    }

    private fun replaceFragmentByPagesTypeAndTag(type: PagesType, tag: String) {
        if (!tabFragmentStack.keys.contains(type)) {
            return
        }
        val transaction = fragmentManager.beginTransaction()
        tabFragmentStack[type]?.forEach { tagFragment ->
            if (!tagFragment.contains(tag)) {
                val fragmentPop = fragmentManager.findFragmentByTag(tagFragment)
                fragmentPop?.let(transaction::detach)
                fragmentManager.fragments.remove(fragmentPop)
            }
        }
        val fragmentCatalog = fragmentManager.findFragmentByTag(tag)
        show(fragmentCatalog, transaction)
        if (currentPagesType != type) {
            val lastF = tabFragmentStack[currentPagesType]?.lastOrNull()
            val fragmentHide = fragmentManager.findFragmentByTag(lastF)
            hide(fragmentHide, transaction)
        }
        transaction.commit()
        lastPagesType =
            if (currentPagesType == type) {
                PagesType.HEADLINE
            } else {
                currentPagesType
            }
        currentPagesType = type
        stackTab.remove(type)
        stackTab.add(type)
        tabFragmentStack[type]?.clear()
        tabFragmentStack[type]?.add(tag)
    }

    private fun show(
        fCurrent: Fragment?,
        transaction: FragmentTransaction,
    ) {
        fCurrent?.let { f ->
            transaction.show(f)
            transaction.setMaxLifecycle(f, Lifecycle.State.RESUMED)
        }
    }

    private fun checkoutBottomNavigationTabFragmentTag(tag: String) =
        tag == NavigationKeys.HEADLINES_TAB_FRAGMENT ||
                tag == NavigationKeys.SAVE_TAB_FRAGMENT ||
                tag == NavigationKeys.SOURCE_TAB_FRAGMENT

    private fun getTagFromPage(pageType: PagesType?): String =
        when (pageType) {
            PagesType.HEADLINE -> NavigationKeys.HEADLINES_TAB_FRAGMENT
            PagesType.SAVE -> NavigationKeys.SAVE_TAB_FRAGMENT
            PagesType.SOURCE -> NavigationKeys.SOURCE_TAB_FRAGMENT
            else -> ""
        }
}