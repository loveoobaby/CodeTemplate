import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * @author yss
 * @date 2019/3/12上午10:02
 * @description: TODO
 */
public class GeneratorJson {

    public static void main(String[] args) throws IOException {
        String targetClassPath = Thread.currentThread().getContextClassLoader().getResource("").getFile();
        String sourcePath = targetClassPath.substring(0, targetClassPath.indexOf("target")) + "src/main/java/com";
        String resourcePath = targetClassPath.substring(0, targetClassPath.indexOf("target")) + "src/main/resources/";

        Path rootPath = FileSystems.getDefault().getPath(sourcePath);

        JSONArray nettDemo = new JSONArray();
        Files.walkFileTree(rootPath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if(file.toString().endsWith(".java")){
                    JSONObject o = new JSONObject();
                    o.put("name", file.getFileName().toString().replace(".java", ""));
                    o.put("template", readFile(file));
                    nettDemo.add(o);
                }
                return FileVisitResult.CONTINUE;
            }
        });

        write2File(resourcePath + "jdk.json", nettDemo.toJSONString());

    }

    public static String readFile(Path file){
        StringBuilder result = new StringBuilder();
        try{
            BufferedReader br = new BufferedReader(new FileReader(file.toFile()));//构造一个BufferedReader类来读取文件
            String s = null;
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                if(s.startsWith("package")){
                    continue;
                }
                result.append(System.lineSeparator()+s);
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return result.toString();
    }

    public static void write2File(String file, String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))){
            writer.write(content);
        }
    }

}
