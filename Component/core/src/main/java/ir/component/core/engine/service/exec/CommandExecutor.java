package ir.component.core.engine.service.exec;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Mohammad Yasin Kaji
 */
public class CommandExecutor {
    Logger logger = LoggerFactory.getLogger(CommandExecutor.class);

    List<String> commandList;

    Map<String, String> context = new HashMap<String, String>();;

    /**
     * Current working directory
     */
    File cwd;

    /**
     * Whether to continue another command if a command failed.
     */
    boolean failover = false;

    int successCode = 0;

    @SuppressWarnings("rawtypes")
    private Map env;

    public CommandExecutionResult run() {
        CommandExecutionResult res = null;
        if (commandList == null) {
            return res;
        }

        for (String cmd : commandList) {
            cmd = substitute(cmd, context);
            CommandLine cmdLine = CommandLine.parse(cmd, context);

            logger.info("External command:\n" + cmdLine);
            res = singleExecute(cmdLine);
            logger.info(String.format("Process %s. Exit code: %s, stdout: [%s], stderr: [%s]", res.succeeded ? "execution succeeded"
                    : "execution failed", res.exitCode, res.stdout, res.stderr));

            if (!isFailover() && !res.succeeded) {
                throw new RuntimeException("Command execution failed");
            }
        }
        return res;
    }

    private String substitute(String cmd, Map<String, String> ctx) {
        for (Entry<String, String> e : ctx.entrySet()) {
            try {
                if (e.getValue() != null) {
                    cmd = cmd.replace("%{" + e.getKey() + "}", e.getValue());
                }
            } catch (Exception e1) {
                logger.error(e.toString());
            }
            // String.format("%\\{%s\\}", e.getKey());
        }
        return cmd;
    }

    private CommandExecutionResult singleExecute(CommandLine cmdLine) throws RuntimeException {
        DefaultExecutor executor = new DefaultExecutor();
        ByteArrayOutputStream stderr = new ByteArrayOutputStream(256);
        ByteArrayOutputStream stdout = new ByteArrayOutputStream(256);
        PumpStreamHandler defaultStreamHandler = new PumpStreamHandler(stdout, stderr);
        executor.setStreamHandler(defaultStreamHandler);
        executor.setExitValue(successCode);
        if (cwd != null) {
            executor.setWorkingDirectory(cwd);
        }
        CommandExecutionResult result = new CommandExecutionResult();
        int resultCode = 1;
        try {
            resultCode = executor.execute(cmdLine, env);
            result.exitCode = resultCode;
            result.succeeded = !executor.isFailure(resultCode);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            result.succeeded = false;
            if (isFailover()) {
                logger.warn("Failed executing command line.", e);
            } else {
                throw new RuntimeException(String.format("exitCode: %s, stdout: %s\nstderr: %s", resultCode, stdout.toString(),
                        stderr.toString()), e);
            }
        } finally {
            result.exitCode = resultCode;
            result.stdout = stdout.toString();
            result.stderr = stderr.toString();
        }
        return result;
    }

    public List<String> getCommandList() {
        return commandList;
    }

    public void setCommandList(List<String> commandList) {
        this.commandList = commandList;
    }

    /**
     * Whether to continue another command if a command failed.
     * 
     * @return
     */
    public boolean isFailover() {
        return failover;
    }

    public void setFailover(boolean failover) {
        this.failover = failover;
    }

    public int getSuccessCode() {
        return successCode;
    }

    public void setSuccessCode(int successCode) {
        this.successCode = successCode;
    }

    public Map<String, String> getContext() {
        return context;
    }

    public void setContext(Map<String, String> context) {
        this.context = context;
    }

    public File getCwd() {
        return cwd;
    }

    public void setCwd(File cwd) {
        this.cwd = cwd;
    }

    @SuppressWarnings("rawtypes")
    public Map getEnv() {
        return env;
    }

    @SuppressWarnings("rawtypes")
    public void setEnv(Map env) {
        this.env = env;
    }

}
