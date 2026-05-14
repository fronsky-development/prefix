package nl.fronsky.prefix.logic.commands.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method as a subcommand handler in a {@link nl.fronsky.prefix.logic.commands.CommandHandler}.
 * <p>
 * Annotated methods must have the signature:
 * {@code void methodName(CommandSender sender, String label, String[] args)}
 * <p>
 * The method name becomes the subcommand name (matched case-insensitively).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SubCommandMethod {

}
