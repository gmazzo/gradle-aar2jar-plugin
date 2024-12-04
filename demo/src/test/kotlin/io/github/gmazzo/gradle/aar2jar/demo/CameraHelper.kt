package io.github.gmazzo.gradle.aar2jar.demo

import android.app.Application
import android.content.Context
import androidx.camera.view.PreviewView

object CameraHelper {

    fun createCamera() = PreviewView(Application())

}
