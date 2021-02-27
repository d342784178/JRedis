package command;

import command.template.ICommand;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-01-24
 * Time: 12:17
 */
public class CommandManager {

    private static Map<String, ICommand> container = new ConcurrentHashMap<>();

    static {
        Predicate<ICommand> predicate = command -> {
            container.put(command.name(), command);
            return true;
        };
        predicate.test(new LPushCommand());
        predicate.test(new LPopCommand());
        predicate.test(new LRemCommand());
        predicate.test(new LLenCommand());
        predicate.test(new DelCommand());
        predicate.test(new KeysCommand());
        predicate.test(new ObjectCommand());
    }

    public static ICommand obtainCommand(String commandConstants) {
        return container.get(commandConstants);

    }

}
