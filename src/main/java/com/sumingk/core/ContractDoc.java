package com.sumingk.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;

/**
 * create by sumingk 2018/11/20
 * weixin_public_number : blockchain_do/刻意链习
 * weibo: 刻意链习
 */
public class ContractDoc {
    /**
     * 生成合约文档文件
     */
    public static final String CONTRACT_FILENAME = "/contract_DOC.html";

    public static void main(String[] args) {
        File file = new File(System.getProperty("user.dir") + MethodSign.OUTPUT_FILENAME);
        if (file.exists()) file.delete();
        MethodSign.anlyContractABI();

        if (!file.exists()) {
            System.err.println("File " + MethodSign.OUTPUT_FILENAME.substring(1) + " does not exist!");
            return;
        }
        printDocHtml(file);
    }

    /**
     * 生成contract_doc.html 文档
     *
     * @param file
     */
    public static void printDocHtml(File file) {
        StringBuilder sb = new StringBuilder();
        PrintStream printStream = null;

        File apiFile = new File(System.getProperty("user.dir") + CONTRACT_FILENAME);
        if (apiFile.exists()) apiFile.delete();

        try {
            printStream = new PrintStream(new FileOutputStream(apiFile));
            sb.append("<html>");
            sb.append("<head>");
            sb.append("<title>Contract Method API</title>");
            sb.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
            sb.append("<style type=\"text/css\">");
            sb.append("TABLE{border-collapse:collapse;border-left:solid 1 #000000; border-top:solid 1 #000000;padding:5px;}");
            sb.append("TH{border-right:solid 1 #000000;border-bottom:solid 1 #000000;}");
            sb.append("ol{list-style-type:none;counter-reset:sectioncounter;}");
            sb.append("TD{font:normal;border-right:solid 1 #000000;border-bottom:solid 1 #000000;}");
            sb.append("</style></head>");
            sb.append("<body>");
            sb.append("<div align=\"left\">");
            sb.append("<ol>");

            List<String> lists = FileOperate.readFileIntoStringArrList(file);
            if (lists.size() > 0) {
                for (int k = 0; k < lists.size(); k++) {
                    String str = lists.get(k).replaceAll("\\s*", "");
                    //排除 空格 空行
                    if (str == null || "".equals(str)) continue;
                    if (str.indexOf(".sol") > -1) {
                        if (k < lists.size() - 1) {
                            String nextStr = lists.get(k + 1);
                            nextStr = nextStr.replaceAll("\\s*", "");
                            if (nextStr == null || "".equals(nextStr)) {
                                continue;
                            }
                            sb.append("<li>" + str + "</li>").append("<br/>");
                        }
                    } else {
                        if (str.indexOf(":") > -1) {
                            sb.append("<li>" + str.split(":")[1] + "</li>").append("<br/>");
                        }
                    }
                }
            }
            sb.append("</ol>");
            sb.append("</div></body></html>");
            printStream.println(sb.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
