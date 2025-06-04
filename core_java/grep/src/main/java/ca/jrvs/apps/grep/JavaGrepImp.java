package ca.jrvs.apps.grep;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.log4j.BasicConfigurator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaGrepImp implements JavaGrep {

    private static final Logger logger = LoggerFactory.getLogger(JavaGrepImp.class);

    private String regex;
    private String outFile;
    private String rootPath;

    @Override
    public void process() throws IOException {
        // Used to store matches
        ArrayList<String> lines = new ArrayList<>();

        // Resurisve search for files
        for(File f: this.listFiles(rootPath)) {
            // Search files for matches and add them
            for (String s: readLines(f)) {
                if(this.containsPattern(s)) {
                    lines.add(s);
                }
            }
        }
        // Write to outfile
        this.WriteToFile(lines);
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
        File f = new File(this.outFile);
        if(f.getParentFile() != null) {
            if(!f.getParentFile().exists()) {
                f.getParentFile().mkdirs();
            }
        }

        try {
            FileWriter fw = new FileWriter(this.outFile);
            for (String s : lines) {
                fw.write(s + System.lineSeparator());
            }
            fw.close();
            logger.debug("Completed write to file {}", this.outFile);
        } catch (IOException e) {
            logger.debug(e.getMessage());
        }
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
        this.outFile = outFile;
    }

    public static void main(String [] args) {
        if(args.length != 3){
            throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
        }

        //Use default logger config
        BasicConfigurator.configure();

        JavaGrepImp jGI = new JavaGrepImp();
        jGI.setRegex(args[0]);
        jGI.setRootPath(args[1]);
        jGI.setOutFile(args[2]);

        try {
            jGI.process();
        } catch (Exception e) {
            jGI.logger.error(e.getMessage());
        }
    }
}
