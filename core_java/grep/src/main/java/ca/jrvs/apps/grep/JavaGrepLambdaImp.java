package ca.jrvs.apps.grep;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class JavaGrepLambdaImp extends JavaGrepImp {

    private static final Logger logger = LoggerFactory.getLogger(JavaGrepImp.class);

    private String regex;
    private String outFile;
    private String rootPath;

    @Override
    public void process() throws IOException {
        // Used to store matches
        List<String> lines = listFiles(getRootPath())
                // create a stream of files, flatMap will create a flattened list of strings
                .stream()
                .flatMap(file -> {
                    try{
                        return Files.lines(file.toPath());
                    } catch (IOException e) {
                        logger.error(e.getMessage());
                    }
                    return Stream.empty();
                //filter will pattern match and collect will store filtered lines into a list
                }).filter(this::containsPattern).collect(Collectors.toList());
        // Write to outfile
        this.WriteToFile(lines);
    }

    @Override
    public List<File> listFiles(String rootDir) {
        logger.debug("Listing all files in root dir: " + rootDir);
        List<File> files = new ArrayList<>();
        File[] dir = new File(rootDir).listFiles();

        if(dir != null){
            Arrays.stream(dir).forEach(f -> {
                logger.debug(f.getName());
                if(f.isDirectory()){
                    files.addAll(listFiles(f.getAbsolutePath()));
                } else {
                    files.add(f);
                }
            });
        }
        return files;
    }

    @Override
    public void WriteToFile(List<String> lines) {
        File f = new File(this.outFile);
        if(f.getParentFile() != null) {
            if(!f.getParentFile().exists()) {
                f.getParentFile().mkdirs();
            }
        }

        try {
            FileWriter fw = new FileWriter(this.outFile);
            lines.stream()
                .map(l -> l + System.lineSeparator())
                .forEach(l -> {
                    try{
                        fw.write(l);
                    } catch(IOException e){
                        logger.error(e.getMessage());
                    }
                });
            fw.close();
            logger.debug("Completed write to file {}", this.outFile);
        } catch (IOException e) {
            logger.debug(e.getMessage());
        }
    }


    public static void main(String[] args) {
        if (args.length != 3) {
            throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
        }

        JavaGrepLambdaImp javaGrepLambdaImp = new JavaGrepLambdaImp();
        javaGrepLambdaImp.setRegex(args[0]);
        javaGrepLambdaImp.setRootPath(args[1]);
        javaGrepLambdaImp.setOutFile(args[2]);

        try{
            javaGrepLambdaImp.process();
        } catch(Exception e) {
            logger.error(e.getMessage());
        }
    }

}
