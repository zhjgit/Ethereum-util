package com.sumingk.core;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * create by sumingk 2018/11/10
 * weixin_public_number : blockchain_do/刻意链习
 * weibo: 刻意链习
 */
public class FileOperate {

    /**
     * 1.truffle compile后生成 build文件目录
     */
    public static String CONTRACT_BUILD_PATH = "\\build\\contracts";
    /**
     * 2.solc 生成abi文件
     */
    public static String SOLCJS_ABIS = "\\abis";

    /**
     * 获取合约build 文件路径
     *
     * @return
     */
    public static String getContractBuildPath(int way) {
        String filepath = System.getProperty("user.dir") + (way == 1 ? CONTRACT_BUILD_PATH : SOLCJS_ABIS);
        filepath = filepath.replaceAll("\\\\", "/");
        System.out.println("way:" + way + "---contract build path--- : " + filepath);
        return filepath;
    }

    /**
     * 遍历合约json文件 转换成JSONObject对象
     *
     * @param path
     * @return
     */
    public static JSONObject getDatafromFile(String path) {
        return JSONObject.parseObject(getDataFileString(path));
    }

    /**
     * 遍历abi文件 转换成JSONArray对象
     *
     * @param path
     * @return
     */
    public static JSONArray getDatafromFileForArray(String path) {
        return JSONArray.parseArray(getDataFileString(path));
    }

    /**
     * 获取文件字符串
     *
     * @param path
     * @return
     */
    public static String getDataFileString(String path) {
        BufferedReader reader = null;
        StringBuffer laststr = new StringBuffer("");
        try {
            FileInputStream fileInputStream = new FileInputStream(path);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            reader = new BufferedReader(inputStreamReader);
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                laststr.append(tempString);
            }
            reader.close();
        } catch (IOException e) {

        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return laststr.toString();
    }


    /**
     * 文件内容写入
     *
     * @param filePath
     * @param content
     */
    public static void writeMethodSign(String filePath, String content) {
        File file = new File(filePath);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file, true);
            BufferedOutputStream Buff = new BufferedOutputStream(fos);
            Buff.write(content.getBytes("UTF-8"));
            Buff.flush();
            Buff.close();
            if (fos != null) {
                fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 遍历目录下的所有文件
     *
     * @param fileDir
     * @return
     */
    public static List<File> getFile(String fileDir, int way) {
        List<File> fileList = new ArrayList<File>();
        File file = new File(fileDir);

        File[] files = file.listFiles();//获取目录下的所有文件或文件夹
        if (files == null) {//如果目录为空，直接退出
            return null;
        }
        // 遍历，目录下的所有文件
        if (files.length > 0) {
            for (File f : files) {
                if (f.isFile()) {
                    //如果是以json结尾的文件
                    if (way == 1 && f.getName().endsWith(".json")) {
                        fileList.add(f);
                    } else if (way == 2 && f.getName().endsWith(".abi")) {
                        fileList.add(f);
                    }
                } else if (f.isDirectory()) {
                    getFile(f.getAbsolutePath(), way);
                }
            }
        }
        /*for (int k = 0; k < fileList.size(); k++) {
            System.out.println(k + " :" + fileList.get(k).getName());
        }*/
        return fileList;
    }

}
