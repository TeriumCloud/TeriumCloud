package cloud.terium.teriumapi.module;

public interface IModule {

    /**
     * Called when the module will be enabled.
     */
    void onEnable();

    /**
     * Called when the module will be disabled.
     */
    void onDisable();

    /**
     * Returns if the module is reloadable.
     *
     * @return Boolean
     */
    Boolean isReloadable();
}