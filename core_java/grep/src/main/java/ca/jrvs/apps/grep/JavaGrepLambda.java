package ca.jrvs.apps.grep;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

public interface JavaGrepLambda {
    /**
     * Traverse a given directory and return all files
     * @param rootDir input directory
     * @return files under the rootDir
     */
    Stream<File> listFilesLazy(String rootDir);

    /**
     * Read a file and return all the lines
     *
     * Explain FileReader, BufferedReader, and character encoding
     *
     * @param inputFile file to be read
     * @return lines
     * @throws IllegalArgumentException if a given inputFile is not a file
     */
    Stream<String> readLinesLazy(File inputFile);

}
