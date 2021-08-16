package com.example.rain.utils

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri

object PermissionSettingHelper {

    fun gotoPermission(activity : Activity){
        gotoMiuiPermission(activity);
    }


    /**
     * 跳转到miui的权限管理页面
     */
    private fun gotoMiuiPermission(activity : Activity) {
        val i = Intent("miui.intent.action.APP_PERM_EDITOR")
        val componentName = ComponentName(
            "com.miui.securitycenter",
            "com.miui.permcenter.permissions.PermissionsEditorActivity"
        )
        i.component = componentName
        i.putExtra("extra_pkgname", activity?.packageName)
        try {
            activity.startActivity(i)
        } catch (e: Exception) {
            e.printStackTrace()
            gotoMeizuPermission(activity)
        }
    }

    /**
     * 跳转到魅族的权限管理系统
     */
    private fun gotoMeizuPermission(activity : Activity) {
        val intent = Intent("com.meizu.safe.security.SHOW_APPSEC")
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.putExtra("packageName", activity?.packageName)
        try {
            activity.startActivity(intent)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            gotoHuaweiPermission(activity)
        }
    }

    /**
     * 华为的权限管理页面
     */
    private fun gotoHuaweiPermission(activity : Activity) {
        try {
            val intent = Intent()
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            val comp = ComponentName(
                "com.huawei.systemmanager",
                "com.huawei.permissionmanager.ui.MainActivity"
            ) //华为权限管理
            intent.component = comp
            activity.startActivity(intent)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            goOppoManager(activity)
        }
    }

    /**
     * OPPO的权限管理页面
     */
    private fun goOppoManager(activity : Activity) {
        try {
            val intent = Intent("android.settings.APPLICATION_DETAILS_SETTINGS")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("packageName", activity?.packageName)
            val comp = ComponentName("com.coloros.securitypermission", "com.coloros.securitypermission.permission.PermissionAppAllPermissionActivity")//R11t 7.1.1 os-v3.2
            intent.component = comp
            activity.startActivity(intent)
        }catch (e: Exception) {
            goSonyManager(activity)
        }
    }

    /**
     * 索尼的权限管理页面
     */
    private fun goSonyManager(activity : Activity) {
        try {
            var intent: Intent = Intent(activity?.packageName)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            var comp: ComponentName = ComponentName("com.sonymobile.cta", "com.sonymobile.cta.SomcCTAMainActivity")
            intent.setComponent(comp)
            activity.startActivity(intent)
        } catch (e: Exception) {
            goLGManager(activity)
        }
    }
    /**
     * LG的权限管理页面
     */
    private fun goLGManager(activity : Activity) {
        try {
            val intent = Intent("android.intent.action.MAIN")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("packageName", activity?.packageName)
            val comp = ComponentName("com.android.settings", "com.android.settings.Settings\$AccessLockSummaryActivity")
            intent.component = comp
            activity.startActivity(intent)
        }catch (e: Exception) {
            goLetvManager(activity)
        }
    }

    /**
     * 乐视的权限管理页面
     */
    private fun goLetvManager(activity : Activity) {
        try {
            val intent = Intent()
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("packageName", activity?.packageName)
            val comp = ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.PermissionAndApps")
            intent.component = comp
            activity.startActivity(intent)
        }catch (e: Exception) {
            getAppDetailSettingIntent(activity)
        }
    }



    /**
     * 跳转到权限设置界面
     */
    private fun getAppDetailSettingIntent(activity : Activity) {
        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
        intent.data = Uri.fromParts("package", activity?.packageName, null)
        activity.startActivity(intent)
    }
}