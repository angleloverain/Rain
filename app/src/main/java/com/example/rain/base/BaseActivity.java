package com.example.rain.base;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewbinding.ViewBinding;


import com.example.rain.model.MyViewModel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.logging.Logger;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

// 1。第一个泛型布局文件     组件管理
// 2。第二个泛型ViewModel   数据管理
// 3. UI交互逻辑该处进行，相关的处理
public class BaseActivity<T extends ViewBinding,V extends BaseViewModel> extends AppCompatActivity
        implements EasyPermissions.PermissionCallbacks{

    protected T layout;
    protected V model;

    private void initLayout(){
        Type types = getClass().getGenericSuperclass();
        if (types instanceof ParameterizedType) {
            Type[] genericType = ((ParameterizedType) types).getActualTypeArguments();
            // 处理布局文件资源文件
            if (genericType[0] instanceof Class){
                String clazz = ((Class<?>) genericType[0]).getName();
                Class<?> layoutClazz = null;
                try {
                    layoutClazz = Class.forName(clazz);
                    Method method = layoutClazz.getMethod("inflate", LayoutInflater.class);
                    Object obj3 = method.invoke(null,getLayoutInflater());
                    layout = ((T) obj3); // 给布局应用赋值
                    setContentView(layout.getRoot());
                } catch (ClassNotFoundException | NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }

            // 处理ViewModel文件
            if (genericType[1] instanceof Class){
                String clazz = ((Class<?>) genericType[1]).getName();
                Class<?> modelClazz = null;
                try {
                    modelClazz = Class.forName(clazz);
                    model = (V) modelClazz.newInstance();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);initLayout();
    }

    /* 权限处理 */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Some permissions have been granted
    }
    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Some permissions have been denied

    }
    /* 权限处理 */

}
