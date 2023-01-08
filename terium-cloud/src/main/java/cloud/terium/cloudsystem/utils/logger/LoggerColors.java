package cloud.terium.cloudsystem.utils.logger;

import org.fusesource.jansi.Ansi;

public enum LoggerColors {

    RESET(Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.DEFAULT).boldOff().toString(), 'f'),
    WHITE(Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.WHITE).bold().toString(), 'f'),
    BLACK(Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLACK).bold().toString(), '0'),
    RED(Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.RED).bold().toString(), 'c'),
    YELLOW(Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.YELLOW).bold().toString(), 'e'),
    BLUE(Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLUE).bold().toString(), '9'),
    GREEN(Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.GREEN).bold().toString(), 'a'),
    PURPLE(Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.MAGENTA).boldOff().toString(), '5'),
    ORANGE(Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.YELLOW).boldOff().toString(), '6'),
    GRAY(Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.WHITE).boldOff().toString(), '7'),
    DARK_RED(Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.RED).boldOff().toString(), '4'),
    DARK_GRAY(Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLACK).boldOff().toString(), '8'),
    DARK_BLUE(Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLUE).boldOff().toString(), '1'),
    DARK_GREEN(Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.GREEN).boldOff().toString(), '2'),
    LIGHT_BLUE(Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.CYAN).bold().toString(), '3'),
    CYAN(Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.CYAN).boldOff().toString(), 'b');

    private final String ansi;
    private final char code;

    LoggerColors(String ansi, char code) {
        this.ansi = ansi;
        this.code = code;
    }

    public static String replaceColorCodes(String s) {
        for (LoggerColors value : LoggerColors.values()) {
            s = s.replace("§" + value.getCode(), value.ansi);
        }
        return s;
    }

    public char getCode() {
        return code;
    }
}