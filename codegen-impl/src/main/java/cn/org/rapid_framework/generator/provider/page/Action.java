package cn.org.rapid_framework.generator.provider.page;

/**
 * TODO 一句话描述该类用途
 * <p/>
 * 创建时间: 14-10-2 下午8:58<br/>
 *
 * @author qyang
 * @since v0.0.1
 */
public class Action {
    private String action;
    private String name;

    public Action(String action, String name) {
        this.action = action;
        this.name = name;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
