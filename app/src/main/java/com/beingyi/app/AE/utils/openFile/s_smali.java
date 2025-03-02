package com.beingyi.app.AE.utils.openFile;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;

import com.beingyi.app.AE.R;
import com.beingyi.app.AE.activity.TextEditor;
import com.beingyi.app.AE.dialog.getSavePath;
import com.beingyi.app.AE.interfaces.GetSavePathCallBack;
import com.beingyi.app.AE.ui.AlertProgress;
import com.beingyi.app.AE.utils.FileUtils;
import com.beingyi.app.AE.utils.Smali2Java;

import java.io.File;
import java.util.Arrays;

import org.jf.dexlib2.Opcodes;
import org.jf.dexlib2.iface.ClassDef;
import org.jf.dexlib2.writer.builder.DexBuilder;
import org.jf.dexlib2.writer.io.MemoryDataStore;
import org.jf.smali.Smali;
import org.jf.smali.SmaliOptions;

public class s_smali extends s_base {

    String Path;
    File file;

    public s_smali(Context mContext, int mWindow, String mPath, View view, boolean mIsInZip) {
        super(mContext, mWindow, view, mIsInZip);
        this.Path = mPath;
        this.file = new File(Path);


        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.getMenuInflater().inflate(R.menu.file_smali, popupMenu.getMenu());
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.action_smali_edit:
                        Intent intent = new Intent(context, TextEditor.class);
                        intent.putExtra("isFile", true);
                        intent.putExtra("Path", file.getAbsolutePath());
                        context.startActivity(intent);

                        break;


                    case R.id.action_smali_toDex: {
                        final File outFile = new File(new File(Path).getParent(), FileUtils.getPrefix(Path) + ".dex");

                        new getSavePath(context, window, outFile.getAbsolutePath(), new GetSavePathCallBack() {

                            @Override
                            public void onSuccess(String filePath) {

                                smali2Dex(outFile.getAbsolutePath());
                            }

                            @Override
                            public void onCancel() {

                            }

                        });
                    }
                    break;

                    case R.id.action_smali_toJava: {
                        final File outFile = new File(new File(Path).getParent(), FileUtils.getPrefix(Path) + ".java");

                        new getSavePath(context, window, outFile.getAbsolutePath(), new GetSavePathCallBack() {

                            @Override
                            public void onSuccess(String filePath) {

                                smali2Java(outFile.getAbsolutePath());
                            }

                            @Override
                            public void onCancel() {

                            }

                        });
                    }
                    break;


                }

                return true;
            }
        });


    }


    public void smali2Dex(final String outPath) {


        final AlertProgress progres = new AlertProgress(context);
        new Thread() {
            @Override
            public void run() {
                progres.setLabel("加载中");
                progres.setNoProgress();
                progres.show();
                try {


                    DexBuilder dexBuilder = new DexBuilder(Opcodes.getDefault());
                    dexBuilder.setIgnoreMethodAndFieldError(true);

                    ClassDef classDef = Smali.assembleSmaliFile(new File(Path), dexBuilder, new SmaliOptions());
                    MemoryDataStore memoryDataStore = new MemoryDataStore();
                    dexBuilder.writeTo(memoryDataStore);
                    byte[] result = Arrays.copyOf(memoryDataStore.getBufferData(), memoryDataStore.getSize());
                    FileUtils.saveFile(result, outPath);


                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            activity.showToast("转换完成");
                            adapter.refresh();
                            adapter.setItemHighLight(outPath);
                        }
                    });

                } catch (Exception e) {
                    activity.showMessage(context, "错误：", e.toString());
                }

                progres.dismiss();
            }
        }.start();


    }





    public void smali2Java(final String outPath) {


        final AlertProgress progres = new AlertProgress(context);
        new Thread() {
            @Override
            public void run() {
                progres.setLabel("加载中");
                progres.setNoProgress();
                progres.show();
                try {

                    Smali2Java.translate(new File(Path),new File(outPath));

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            activity.showToast("转换完成");
                            adapter.refresh();
                            adapter.setItemHighLight(outPath);
                        }
                    });

                } catch (Exception e) {
                    activity.showMessage(context, "错误：", e.toString());
                }

                progres.dismiss();
            }
        }.start();


    }







}
