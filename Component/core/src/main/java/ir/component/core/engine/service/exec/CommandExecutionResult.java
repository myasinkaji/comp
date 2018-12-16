package ir.component.core.engine.service.exec;

/**
 * A holder for command execution results. Useful for logging and checking successful execution of a command.
 * 
 * @author Mohammad Yasin Kaji
 */
public class CommandExecutionResult {

    public CommandExecutionResult(int resultCode, String stdout) {
        this(resultCode, stdout, null);
    }

    public CommandExecutionResult(int resultCode, String stdout, String stderr) {
        this.exitCode = resultCode;
        this.stdout = stdout;
        this.stderr = stderr;
    }

    public CommandExecutionResult() {
    }

    public boolean succeeded = false;

    public int exitCode;

    public String stdout;

    public String stderr;
}