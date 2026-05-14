package nl.fronsky.prefix.logic.commands.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a class as a command handler, providing the command name and base permission.
 * <p>
 * Classes annotated with this must extend {@link nl.fronsky.prefix.logic.commands.CommandHandler}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CommandClass {
    /** The command name as registered in plugin.yml. */
    String name();
    /** The base permission required to use this command. */
    String permission();
}
