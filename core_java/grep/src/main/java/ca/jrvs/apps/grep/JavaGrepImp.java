package ca.jrvs.apps.grep;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class JavaGrepImp implements JavaGrep {

    @Override
    public void process() throws IOException {

    }

    @Override
    public List<File> listFiles(String rootDir) {
        return Collections.emptyList();
    }

    @Override
    public List<String> readLines(File inputFile) {
        return Collections.emptyList();
    }

    @Override
    public boolean containsPattern(String line) {
        return false;
    }

    @Override
    public void WriteToFile(List<String> lines) {

    }

    @Override
    public String getRootPath() {
        return "";
    }

    @Override
    public void setRootPath(String rootPath) {

    }

    @Override
    public String getRegex() {
        return "";
    }

    @Override
    public void setRegex(String regex) {

    }

    @Override
    public String getOutFile() {
        return "";
    }

    @Override
    public void setOutFile(String outFile) {

    }
}
