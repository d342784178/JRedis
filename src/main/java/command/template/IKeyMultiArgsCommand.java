package command.template;

/**
 * Desc:
 * Author: ljdong2
 * Date: 2021-02-27
 * Time: 12:04
 */
public interface IKeyMultiArgsCommand extends IKeyCommand {


    /**
     * 多参数时步长
     * @return
     */
    int argsStep();

}
