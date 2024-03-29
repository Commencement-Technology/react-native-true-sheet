package com.lodev09.truesheet

import android.util.Log
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior

class TrueSheetBottomSheetBehavior<T : ViewGroup>() : BottomSheetBehavior<T>() {

  private fun isInsideSheet(scrollView: ScrollView, event: MotionEvent): Boolean {
    val x = event.x
    val y = event.y

    val position = IntArray(2)
    scrollView.getLocationOnScreen(position)

    val nestedX = position[0]
    val nestedY = position[1]

    val boundRight = nestedX + scrollView.width
    val boundBottom = nestedY + scrollView.height

    return (x > nestedX && x < boundRight && y > nestedY && y < boundBottom) ||
      event.action == MotionEvent.ACTION_CANCEL
  }

  // TODO: Pass scrollview explicitly here
  override fun onInterceptTouchEvent(parent: CoordinatorLayout, child: T, event: MotionEvent): Boolean {
    val isDownEvent = (event.actionMasked == MotionEvent.ACTION_DOWN)
    val expanded = state == STATE_EXPANDED

    if (isDownEvent && expanded){
      val container = child.getChildAt(0) as ViewGroup
      val content = (container.getChildAt(0) as ViewGroup).getChildAt(0) as ViewGroup

      for(i in 0 until content.childCount){
        val contentChild = content.getChildAt(i)
        val scrolled = (contentChild is ScrollView && contentChild.scrollY > 0)

        if(!scrolled) continue

        if (isInsideSheet(contentChild as ScrollView, event)) {
          return false
        }
      }
    }

    return super.onInterceptTouchEvent(parent, child, event)
  }

  companion object {
    const val TAG = "TrueSheetView"
  }
}