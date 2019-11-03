package com.ext1se.notepad

import android.app.Application
import android.content.res.Resources
import com.ext1se.notepad.di.DI
import com.ext1se.notepad.di.models.AppModule
import io.realm.Realm
import toothpick.Scope
import toothpick.Toothpick
import toothpick.configuration.Configuration
import toothpick.registries.FactoryRegistry
import toothpick.registries.FactoryRegistryLocator
import toothpick.registries.MemberInjectorRegistry
import toothpick.registries.MemberInjectorRegistryLocator
import toothpick.smoothie.module.SmoothieApplicationModule

class App : Application() {

    companion object {
        lateinit var appScope: Scope
    }

    override fun onCreate() {
        super.onCreate()
        initToothpick()
    }

    private fun initToothpick() {
        val configuration: Configuration
        if (BuildConfig.DEBUG) {
            configuration = Configuration.forDevelopment().preventMultipleRootScopes()
        } else {
            configuration = Configuration.forProduction()
        }
        Toothpick.setConfiguration(configuration)

        appScope = Toothpick.openScope(DI.APP_SCOPE)
        appScope.installModules(SmoothieApplicationModule(this), AppModule(this))
    }
}