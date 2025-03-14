package com.beingyi.app.AE.utils;

import com.beingyi.app.AE.application.MyApplication;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.KeyGenerator;

import Decoder.BASE64Encoder;

public class FileEntry
{

    public static void encrypt(String fileUrl, String destpath, String key) throws Exception
    {
        File file = new File(fileUrl);
        String path = file.getPath();
        if (!file.exists())
        {
            return;
        }

        File dest = new File(destpath);
        InputStream in = new FileInputStream(fileUrl);
        OutputStream out = new FileOutputStream(dest);
        byte[] buffer = new byte[1024];
        int len=0;;
        byte[] outputBuffer = new byte[1024];
        String regEx="[^0-9]";  
        Pattern p = Pattern.compile(regEx);  
        Matcher m = p.matcher(getMd5(key));
        String result=m.replaceAll("").trim();

        while ((len = in.read(buffer)) > 0)
        {
            for (int i=0;i < len;i++)
            {
                byte b = buffer[i];
                b =(byte)(b^ Integer.parseInt(result));
                outputBuffer[i] = b;
            }

            out.write(outputBuffer, 0, len);
            out.flush();
        }
        in.close();
        out.close();
        System.out.println(MyApplication.getInstance().getString("4a2b3c2e5ec90ef5aae1fcba81e759e0"));
    }





    public static String decrypt(String fileUrl, String destpath, String key) throws Exception
    {
        File file = new File(fileUrl);
        if (!file.exists())
        {
            return null;
        }
        File dest = new File(destpath);
        if (!dest.getParentFile().exists())
        {
            dest.getParentFile().mkdirs();
        }
        InputStream is = new FileInputStream(fileUrl);
        OutputStream out = new FileOutputStream(dest);
        byte[] buffer = new byte[1024];
        int len=0;;
        byte[] outputBuffer = new byte[1024];
        String regEx="[^0-9]";  
        Pattern p = Pattern.compile(regEx);  
        Matcher m = p.matcher(getMd5(key));
        String result=m.replaceAll("").trim();
        while ((len = is.read(buffer)) > 0)
        {
            for (int i=0;i < len;i++)
            {
                byte b = buffer[i];
                b =(byte)(b^ Integer.parseInt(result));
                outputBuffer[i] = b;
            }
            out.write(outputBuffer, 0, len);
            out.flush();
        }
        out.close();
        is.close();
        System.out.println(MyApplication.getInstance().getString("3c0893483f9089840f24f04d9abbbc2a"));
        return destpath;
    }




    public static InputStream encrypt(InputStream is, String key) throws Exception
    {

        InputStream resultStream=null;
        byte[] buffer = new byte[1024];
        int len=0;;
        byte[] outputBuffer = new byte[1024];
        ByteArrayOutputStream outputByte = new ByteArrayOutputStream();
        String regEx="[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(getMd5(key));
        String result=m.replaceAll("").trim();

        while ((len = is.read(buffer)) > 0)
        {
            for (int i=0;i < len;i++)
            {
                byte b = buffer[i];
                b =(byte)(b^ Integer.parseInt(result));
                outputBuffer[i] = b;
            }

            outputByte.write(outputBuffer, 0, len);
            outputByte.flush();
        }
        is.close();

        resultStream=new ByteArrayInputStream(outputByte.toByteArray());

        return resultStream;
    }





    public static InputStream decrypt(InputStream is, String key) throws Exception
    {
        InputStream resultStream=null;
        byte[] buffer = new byte[1024];
        int len=0;;
        byte[] outputBuffer = new byte[1024];
        ByteArrayOutputStream outputByte = new ByteArrayOutputStream();
        String regEx="[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(getMd5(key));
        String result=m.replaceAll("").trim();
        while ((len = is.read(buffer)) > 0)
        {
            for (int i=0;i < len;i++)
            {
                byte b = buffer[i];
                b =(byte)(b^ Integer.parseInt(result));
                outputBuffer[i] = b;
            }
            outputByte.write(outputBuffer, 0, len);
            outputByte.flush();
        }

        resultStream=new ByteArrayInputStream(outputByte.toByteArray());
        is.close();


        return resultStream;
    }





    public static String getMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException
    {
        //确定计算方法
        MessageDigest md5=MessageDigest.getInstance("MD5");
        BASE64Encoder base64en = new BASE64Encoder();
        //加密后的字符串
        String newstr=base64en.encode(md5.digest(str.getBytes("utf-8")));
        return newstr;
    }






    Key key;   
    public FileEntry(String str) {   
        getKey(str);//生成密匙   
    }   
    /**  
     * 根据参数生成KEY  
     */   
    public void getKey(String strKey) {   
        try {   
            KeyGenerator _generator = KeyGenerator.getInstance("DES");   
            _generator.init(new SecureRandom(strKey.getBytes()));   
            this.key = _generator.generateKey();   
            _generator = null;   
        } catch (Exception e) {   
            throw new RuntimeException("Error initializing SqlMap class. Cause: " + e);   
        }   
    }








}

