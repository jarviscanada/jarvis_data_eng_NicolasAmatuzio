package ca.jrvs.apps.grep;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JavaGrepLambdaImp extends JavaGrepImp implements JavaGrepLambda {

    private static final Logger logger = LoggerFactory.getLogger(JavaGrepLambdaImp.class);

    private String regex;
    private String outFile;
    private String rootPath;

    @Override
    public void process() throws IOException {
        // Used to store matches
        List<String> lines = listFilesLazy(getRootPath())
                // create a stream of files, flatMap will create a flattened list of strings
                .flatMap(this::readLinesLazy)
                //filter will pattern match and collect will store filtered lines into a list
                .filter(this::containsPattern).collect(Collectors.toList());
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
    public Stream<File> listFilesLazy(String rootDir) {
        logger.debug("Listing all files in root dir: " + rootDir);
        File[] dir = new File(rootDir).listFiles();

        if(dir == null){
            return Stream.empty();
        }

        return Stream.of(dir).flatMap(f -> {
            if(f.isDirectory()){
                return listFilesLazy(f.getAbsolutePath());
            } else {
                return Stream.of(f);
            }
        });
    }

    @Override
    public Stream<String> readLinesLazy(File inputFile) {
        try {
            logger.debug("Reading file: " + inputFile.getAbsolutePath());
            return  Files.lines(inputFile.toPath());
        } catch (IOException e) {
            logger.debug(e.getMessage());
            return Stream.empty();
        }
    }

    public static void main(String[] args) {
       if (args.length != 3) {
            throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
        }

        //Use default logger config
        BasicConfigurator.configure();

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