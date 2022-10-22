package cloud.terium.teriumapi;

import cloud.terium.teriumapi.api.ICloudFactory;
import cloud.terium.teriumapi.api.ICloudProvider;
import lombok.Getter;

public abstract class TeriumAPI {

    @Getter
    protected static TeriumAPI teriumAPI;

    protected TeriumAPI() {
        teriumAPI = this;
    }

    public abstract ICloudProvider getProvider();

    public abstract ICloudFactory getFactory();
}