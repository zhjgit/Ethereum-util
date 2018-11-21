package com.sumingk.core;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.bouncycastle.jcajce.provider.digest.Keccak;
import org.bouncycastle.util.encoders.Hex;

import java.io.File;
import java.util.List;

/**
 * create by sumingk 2018/11/10
 * weixin_public_number : blockchain_do/刻意链习
 * weibo: 刻意链习
 */
public class MethodSign {
    /**
     * 生成的方法签名文件
     */
    public static final String OUTPUT_FILENAME = "/contract_method_signature.md";


    public static void main(String[] args) {
        File file = new File(System.getProperty("user.dir") + OUTPUT_FILENAME);
        if (file.exists()) file.delete();
        anlyContractABI();
    }

    /**
     * abi 解析
     */
    public static void anlyContractABI() {
        //truffle 生成json
        List<File> fileList = FileOperate.getFile(FileOperate.getContractBuildPath(1), 1);
        String message = "";
        boolean flag = true;//默认使用truffle
        if (fileList == null || fileList.size() == 0) {
            message = "\r\n" + "'" + FileOperate.CONTRACT_BUILD_PATH.replaceAll("\\\\", "/").substring(1) + "' File directory does not exist!";
            //solc 生成abi
            fileList = FileOperate.getFile(FileOperate.getContractBuildPath(2), 2);
            flag = false;//solc
            if (fileList == null || fileList.size() == 0) {
                message += " && " + "'" + FileOperate.SOLCJS_ABIS.replaceAll("\\\\", "/").substring(1) + "' File directory does not exist!";
                message += "\r\n" + " please use truffle or solc compile contract first!";
                System.err.println(message);
                return;
            }
        }
        Keccak.Digest256 digest256 = new Keccak.Digest256();
        if (fileList != null && fileList.size() > 0) {
            for (File f1 : fileList) {
                System.out.println("");
                if (flag) {//truffle
                    truffleGenerate(f1, digest256);
                } else {//solc
                    solcGenerate(f1, digest256);
                }
            }
        }
    }

    /**
     * 根据truffle 生成build/contracts 生成方法签名
     *
     * @param f1
     * @param digest256
     */
    public static void truffleGenerate(File f1, Keccak.Digest256 digest256) {
        String fileName = f1.getName();
        String printContractName = "--------------------------------" + fileName.substring(0, fileName.lastIndexOf("json")) + "sol" + "------------------------------------";
        System.out.println(printContractName);
        FileOperate.writeMethodSign(System.getProperty("user.dir") + OUTPUT_FILENAME, "\r\n" + printContractName + "\r\n");
        if (f1.canRead()) {
            JSONObject jsonObject = FileOperate.getDatafromFile(f1.getPath());
            JSONArray jsonArray = jsonObject.getJSONArray("abi");
            if (jsonArray != null && jsonArray.size() > 0) {
                digestMethodSign(jsonArray, digest256);
            }
        }
    }

    /**
     * 根据solc 生成abis 目录 得到的abi生产方法签名
     *
     * @param f1
     * @param digest256
     */
    public static void solcGenerate(File f1, Keccak.Digest256 digest256) {
        String fileName = f1.getName();
        if (fileName.endsWith(".abi")) {//以.abi结尾的文件
            String printContractName = "--------------------------------" + fileName.substring(0, fileName.lastIndexOf("abi")) + "sol" + "------------------------------------";
            System.out.println(printContractName);
            FileOperate.writeMethodSign(System.getProperty("user.dir") + OUTPUT_FILENAME, "\r\n" + printContractName + "\r\n");
            if (f1.canRead()) {
                JSONArray jsonArray = FileOperate.getDatafromFileForArray(f1.getPath());//abi文件
                if (jsonArray != null && jsonArray.size() > 0) {
                    digestMethodSign(jsonArray, digest256);
                }
            }
        }
    }

    /**
     * 方法签名解析
     *
     * @param jsonArray
     * @param digest256
     */
    public static void digestMethodSign(JSONArray jsonArray, Keccak.Digest256 digest256) {
        for (int k = 0; k < jsonArray.size(); k++) {
            JSONObject object = jsonArray.getJSONObject(k);
            if ("function".equalsIgnoreCase(object.getString("type"))) {
                String funcName = object.getString("name");
                String functionSign = "";
                JSONArray inputsArray = object.getJSONArray("inputs");
                String writeContent = "";
                if (inputsArray != null && inputsArray.size() > 0) {
                    StringBuffer sbf = new StringBuffer("(");
                    for (int i = 0; i < inputsArray.size(); i++) {
                        sbf.append(inputsArray.getJSONObject(i).getString("type"));
                        if (i != inputsArray.size() - 1) sbf.append(",");
                    }
                    sbf.append(")");
                    String finalFunc = funcName + sbf.toString();
                    functionSign = Hex.toHexString(digest256.digest(finalFunc.getBytes())).substring(0, 8);
                    writeContent = functionSign + " : " + finalFunc;
                    System.out.println(writeContent);
                } else {
                    functionSign = Hex.toHexString(digest256.digest((funcName + "()").getBytes())).substring(0, 8);
                    writeContent = functionSign + " : " + funcName + "()";
                    System.out.println(writeContent);
                }
                FileOperate.writeMethodSign(System.getProperty("user.dir") + OUTPUT_FILENAME, writeContent + "\r\n");
            }
        }
    }

}
