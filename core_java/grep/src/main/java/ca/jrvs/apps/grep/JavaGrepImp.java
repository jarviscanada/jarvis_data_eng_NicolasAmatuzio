package ca.jrvs.apps.grep;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import jdk.jfr.internal.tool.Main;

import java.io.*;
import java.util.*;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaGrepImp implements JavaGrep {

    final Logger logger = LoggerFactory.getLogger(JavaGrep.class);

    private String regex;
    private String outFile;
    private String rootPath;

    public static void Main (String [] args) {

    }

    @Override
    public void process() throws IOException {

    }

    @Override
    public List<File> listFiles(String rootDir) {
        logger.debug("Listing all files in root dir: " + rootDir);
        List<File> files = new ArrayList<>();
        File[] dir = new File(rootDir).listFiles();

        for (File f : dir) {
            logger.debug(f.getName());
            if (f.isDirectory()) {
                files.addAll(listFiles(f.getAbsolutePath()));
            } else {
                files.add(f);
            }
        }
        return files;
    }

    @Override
    public List<String> readLines(File inputFile) {
        List<String> str = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while((line = reader.readLine()) != null) {
                str.add(line);
            }
        } catch (IOException e) {
            logger.debug(e.getMessage());
            return null;
        }
        return str;
    }

    @Override
    public boolean containsPattern(String line) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);

        return matcher.find();
    }

    @Override
    public void WriteToFile(List<String> lines) {

    }

    @Override
    public String getRootPath() {
        return this.rootPath;
    }

    @Override
    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    @Override
    public String getRegex() {
        return this.regex;
    }

    @Override
    public void setRegex(String regex) {
        this.regex = regex;
    }

    @Override
    public String getOutFile() {
        return this.outFile;
    }

    @Override
    public void setOutFile(String outFile) {

    }
}
