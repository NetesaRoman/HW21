package api;

/*
 *
 * @author Roman Netesa
 *
 */
public class UnknownLineFormatException extends Exception{
    public UnknownLineFormatException(String line) {
        super("Unknown line format, line is too long or too short to map it into the Product obj. Line:" + line);
    }
}
