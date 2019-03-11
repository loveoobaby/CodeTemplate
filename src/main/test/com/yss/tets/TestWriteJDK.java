package com.yss.tets;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.intellij.openapi.util.io.FileUtil;

import java.io.File;
import java.io.IOException;

/**
 * @author yss
 * @date 2019/3/11上午9:24
 * @description: TODO
 */
public class TestWriteJDK {

    public static void main(String[] args) throws IOException {

        JSONArray array = new JSONArray();



        {
            String name = "ScheduleExecutorService";
            String template = "        ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);\n" +
                    "        ScheduledFuture future = executor.schedule(new Runnable() {\n" +
                    "            @Override\n" +
                    "            public void run() {\n" +
                    "                System.out.println(\"60 second later\");\n" +
                    "            }\n" +
                    "        }, 60, TimeUnit.SECONDS);\n" +
                    "        future.get();\n" +
                    "        executor.shutdown();";
            JSONObject o = new JSONObject();
            o.put("name", name);
            o.put("template", template);
            array.add(o);
        }

        writeDataToFile(array.toJSONString());



    }


    private static void writeDataToFile(String jsonFormatData) throws IOException {
        FileUtil.writeToFile(new File("/Users/yss/work/CodeTemplate/src/main/resources/template/jdk.json"), jsonFormatData);

    }

}
