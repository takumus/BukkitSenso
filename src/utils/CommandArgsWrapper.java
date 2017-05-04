package utils;

/**
 * Created by takumus on 2017/05/04.
 */
public class CommandArgsWrapper {
    private String[] args;
    public CommandArgsWrapper(String[] args) {
        this.setArgs(args);
    }
    public void setArgs(String[] args) {
        this.args = args;
    }
    public String at(int id) {
        if (id < args.length) return args[id];
        return "";
    }
}
