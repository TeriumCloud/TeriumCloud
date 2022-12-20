package cloud.terium.cloudsystem.console;

import cloud.terium.cloudsystem.TeriumCloud;
import cloud.terium.teriumapi.console.command.Command;
import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ConsoleCompleter implements Completer {

    @Override
    public void complete(LineReader lineReader, ParsedLine parsedLine, List<Candidate> list) {
        String input = parsedLine.line();

        List<String> suggestions = null;
        String[] arguments = input.split(" ");
        if (input.indexOf(' ') == -1) {
            Collection<String> registeredCommands = TeriumCloud.getTerium().getCommandManager().getBuildedCommands().keySet();
            String tests = arguments[arguments.length - 1];
            List<String> result = new LinkedList<>();
            for (final String s : registeredCommands) {
                if (s != null && (tests.trim().isEmpty() || s.toLowerCase().contains(tests.toLowerCase()))) {
                    result.add(s);
                }
            }

            if (result.isEmpty() && !registeredCommands.isEmpty()) {
                result.addAll(registeredCommands);
            }

            suggestions = result;
        } else {
            Command command = TeriumCloud.getTerium().getCommandManager().getBuildedCommands().get(arguments[0]);
            if (arguments.length > 1) {
                if (input.endsWith(" ")) {
                    arguments = Arrays.copyOfRange(arguments, 1, arguments.length + 1);
                    arguments[arguments.length - 1] = "";
                } else {
                    arguments = Arrays.copyOfRange(arguments, 1, arguments.length);
                }
            }
            if (command != null) {
                suggestions = command.tabComplete(arguments);
            }
        }

        if (suggestions == null || suggestions.isEmpty()) return;

        suggestions.stream().map(Candidate::new).forEach(list::add);
    }
}