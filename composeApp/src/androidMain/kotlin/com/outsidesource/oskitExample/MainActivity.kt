package com.outsidesource.oskitExample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.outsidesource.oskitcompose.lib.koinInjector
import com.outsidesource.oskitcompose.systemui.KmpAppLifecycleObserver
import com.outsidesource.oskitcompose.systemui.KmpAppLifecycleObserverContext
import com.outsidesource.oskitkmp.capability.KmpCapabilities
import com.outsidesource.oskitkmp.capability.KmpCapabilityContext
import com.outsidesource.oskitkmp.filesystem.IKmpFs
import com.outsidesource.oskitkmp.filesystem.KmpFs
import com.outsidesource.oskitkmp.filesystem.KmpFsContext
import com.outsidesource.oskitkmp.notifications.KmpLocalNotifications
import com.outsidesource.oskitkmp.notifications.KmpLocalNotificationsContext
import org.koin.core.component.inject
import ui.app.App

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        KmpAppLifecycleObserver.init(KmpAppLifecycleObserverContext())
        KmpFs.init(KmpFsContext(application, this))

        val capabilities by koinInjector.inject<KmpCapabilities>()
        capabilities.init(KmpCapabilityContext(this))

        val notifications by koinInjector.inject<KmpLocalNotifications>()
        notifications.init(KmpLocalNotificationsContext(this))

        setContent {
            App()
        }
    }
}