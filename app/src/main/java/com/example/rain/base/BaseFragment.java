package com.example.rain.base;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;


import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public abstract class BaseFragment<T extends ViewBinding,V extends BaseViewModel> extends Fragment
        implements EasyPermissions.PermissionCallbacks{

    protected T layout;
    protected V model;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return initLayout(inflater,container);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view,savedInstanceState);
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


    /**
     * 在视图构建完成后，立即初始化控件相关参数
     * @param root
     * @param savedInstanceState
     */
    protected abstract void initView(View root,Bundle savedInstanceState);
    private View initLayout(LayoutInflater inflater, ViewGroup container){
        View root = null;
        Type types = getClass().getGenericSuperclass();
        if (types instanceof ParameterizedType) {
            Type[] genericType = ((ParameterizedType) types).getActualTypeArguments();
            // 处理布局文件资源文件
            if (genericType[0] instanceof Class){
                String clazz = ((Class<?>) genericType[0]).getName();
                Class<?> layoutClazz = null;
                try {
                    layoutClazz = Class.forName(clazz);
                    Method method = layoutClazz.getMethod("inflate", LayoutInflater.class,ViewGroup.class,boolean.class);
                    Object obj3 = method.invoke(null,inflater,container,false);
                    layout = ((T) obj3); // 给布局应用赋值
                    root = layout.getRoot();
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
                } catch (java.lang.InstantiationException e) {
                    e.printStackTrace();
                }
            }
        }

        return root;
    }
}
