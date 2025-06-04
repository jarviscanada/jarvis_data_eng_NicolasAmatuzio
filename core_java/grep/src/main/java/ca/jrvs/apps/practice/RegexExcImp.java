package ca.jrvs.apps.practice;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexExcImp implements RegexExc {

    Pattern pattern;

    @Override
    public boolean matchJpeg(String filename) {
        pattern = Pattern.compile(".jpg$|.jpeg$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(filename);
        return matcher.find();
    }

    @Override
    public boolean matchIp(String ip) {
        pattern = Pattern.compile("^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$");
        Matcher matcher = pattern.matcher(ip);
        return matcher.find();
    }

    @Override
    public boolean isEmptyLine(String line) {
        pattern = Pattern.compile("^\\s|^$|\\t");
        Matcher matcher = pattern.matcher(line);
        return matcher.find();
    }
}
