# Introduction
This is a simple grep application implemented in Java using Maven. 
It searches for specific patterns within text files and outputs the matching lines efficiently. 
The application uses core Java and Java Lambdas and streams for processing text data. 
The entire application has been containerized using Docker to ensure consistent environments across different deployment scenarios.

# Quick Start
```bash
# USAGE: regex rootPath outFile
# - regex: a special text string for describing a search pattern
# - rootPath: root directory path
# - outFile: output file name

# Using the java shell command
java -jar target/grep-1.0-SNAPSHOT.jar ${regex} ${rootPath} /out/${outFile}

# Using the docker image
docker run --rm \
-v `pwd`/data:/data -v `pwd`/out:/out namatuzio/grep \
${regex} ${rootPath} /out/${outFile}
```

# Implemenation
## Pseudocode

```java
public void process() throws IOException{
    create a stream of files 
    flatten stream to use one dimensional list
    filter lines, store them in a list
    write to outfile
}
```

`outFile` does not have to exist, the creation of the required directory is handled seamlessly.

## Performance Issue

A problem with implementing grep is working with 1. large files and 2. low memory environments. 
The most effective solution to this problem was implementing lazy loading with streams to process data in easily processable chunks.

# Test

Testing was done through IntelliJ IDEA by setting default commands to run the Java file with the following parameters:
```bash
java -jar target/grep-app.jar "pattern" /path/to/testfile.txt
```

Log4j was also used to throw errors and enable debug information to print when the program was executed. To configure Log4j, you can add a `log4j2.xml` file in the `src/main/resources` directory with the appropriate configuration.
# Deployment
Packaged in a jar file with maven, Deployed to a docker image on docker hub with code being hosted on GitHub.

# Improvement
Various improvements could be made to the grep app that would improve code consistency and functionality.
1. Count matching - Creating functionality to find the total count of matches found in a file system.
2. Filtering file extensions - Adding a filter specific to file extensions would improve usability a lot.
3. General debug improvements - log more debug information and improve error handling codes to provide more details.

