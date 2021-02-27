package command.template;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-02-27
 * Time: 12:04
 */
public interface IKeyCommand extends ICommand {
    Class<?>[] support();

    /**
     * 是否有key
     * @return
     */
    boolean key();


}
