package com.example.rain.dlg;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.rain.R;
import com.example.rain.fragment.FaceFragment;
import com.example.rain.utils.KeyBoardHelper;

import org.jetbrains.annotations.NotNull;


public class InputDialog extends DialogFragment {

    private FrameLayout frame_tools;
    private boolean mShowFace;
    private boolean mShowKeyBoard;
    private int keyboard_height = 0;

    private EditText input_edit;
    private FaceFragment faceFragment;
    private InputMethodManager inputMethodManager;
    private int input_flag = -1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, R.style.MyDialog);
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        KeyBoardHelper.addOnSoftKeyBoardVisibleListener(getActivity(), new KeyBoardHelper.OnKeyBoarOpenListener() {
            @Override
            public void onOpen(boolean isVisible, int keyboardHeight) {
                Log.i("dddd", " isVisible : " + isVisible);
                Log.i("dddd", " keyboardHeight : " + keyboardHeight);
                if (isVisible && !mShowKeyBoard) {
                    keyboard_height = keyboardHeight;
                    mShowKeyBoard = true;
                    if (!mShowFace) { // 在没有显示表情的时候，才执行动画
                        // 这个地方必须减去14,不然会出现诡异的bug
                        startAnimator(ValueAnimator.ofInt(0, keyboard_height));
                    } else {
                        mShowFace = false;
                    }
                }

                if (!isVisible && mShowKeyBoard) {
                    mShowKeyBoard = false;
                    if (!mShowFace) {
                        startAnimator(ValueAnimator.ofInt(keyboard_height, 0));
                    }
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_input, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        frame_tools = view.findViewById(R.id.frame_tools);
        input_edit = view.findViewById(R.id.input_edit);
        input_edit.setFocusable(true);
        input_edit.setFocusableInTouchMode(true);
        input_edit.requestFocus();
        if (input_flag != -1) input_edit.setInputType(input_flag);
        view.findViewById(R.id.button_face).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 表情未展示，有显示软盘的时候
                if (!mShowFace && mShowKeyBoard) {
                    mShowFace = true;
                    inputMethodManager.hideSoftInputFromWindow(input_edit.getWindowToken(), 0);
                    return;
                }
                // 表情未展示，无软盘显示的时候
                if (!mShowFace && !mShowKeyBoard) {
                    mShowFace = true; // 执行打开动画
                    startAnimator(ValueAnimator.ofInt(0, keyboard_height));
                    return;
                }

                if (mShowFace) {
                    mShowFace = false; // 再次点击的时候，就关闭
                    startAnimator(ValueAnimator.ofInt(keyboard_height, 0));
                }

            }
        });

        view.findViewById(R.id.button_input).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 设置使用键盘输入
                inputMethodManager.showSoftInput(input_edit, 0);
            }
        });

        view.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("dddd"," l :" + frame_tools.getLeft());
                Log.i("dddd"," r :" + frame_tools.getRight());
                Log.i("dddd"," t :" + frame_tools.getTop());
                Log.i("dddd"," b :" + frame_tools.getBottom());
            }
        });
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        Window dialog_window = dialog.getWindow();
        dialog_window.setGravity(Gravity.BOTTOM);//设置显示的位置
        dialog_window.setAttributes(params);//设置显示的大小
        dialog_window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog_window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            dialog_window.setStatusBarColor(Color.BLUE);
        }
        return dialog;
    }

    private void startAnimator(ValueAnimator valueAnimator) {
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int h = (int) animation.getAnimatedValue();
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) frame_tools.getLayoutParams();
                lp.height = h;  // 这里每次更新都会刷新布局，监听就会回调一次
                frame_tools.setLayoutParams(lp);
            }
        });
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.setDuration(120);
        valueAnimator.start();
    }

}
