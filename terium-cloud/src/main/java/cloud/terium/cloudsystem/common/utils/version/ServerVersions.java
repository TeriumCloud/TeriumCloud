package cloud.terium.cloudsystem.common.utils.version;


import lombok.Getter;

@Getter
public enum ServerVersions {
    VELOCITY("velocity", "https://api.papermc.io/v2/projects/velocity/versions/1.1.9/builds/447/downloads/velocity-1.1.9-447.jar"),
    VELOCITY_3("velocity-3", "https://api.papermc.io/v2/projects/velocity/versions/3.2.0-SNAPSHOT/builds/225/downloads/velocity-3.2.0-SNAPSHOT-225.jar"),
    WATERFALL("waterfall", "https://api.papermc.io/v2/projects/waterfall/versions/1.19/builds/510/downloads/waterfall-1.19-510.jar"),
    BUNGEECORD("bungeecord", "https://ci.md-5.net/job/BungeeCord/lastSuccessfulBuild/artifact/bootstrap/target/BungeeCord.jar"),
    PAPER_1_19_4("paper-1.19.4", "https://api.papermc.io/v2/projects/paper/versions/1.19.4/builds/477/downloads/paper-1.19.4-477.jar"),
    PAPER_1_19_3("paper-1.19.3", "https://api.papermc.io/v2/projects/paper/versions/1.19.3/builds/431/downloads/paper-1.19.3-431.jar"),
    PAPER_1_19_2("paper-1.19.2", "https://api.papermc.io/v2/projects/paper/versions/1.19.2/builds/307/downloads/paper-1.19.2-307.jar"),
    PAPER_1_18_2("paper-1.18.2", "https://api.papermc.io/v2/projects/paper/versions/1.18.2/builds/388/downloads/paper-1.18.2-388.jar"),
    PAPER_1_17_1("paper-1.17.1", "https://api.papermc.io/v2/projects/paper/versions/1.17.1/builds/411/downloads/paper-1.17.1-411.jar"),
    PAPER_1_16_5("paper-1.16.5", "https://api.papermc.io/v2/projects/paper/versions/1.16.5/builds/794/downloads/paper-1.16.5-794.jar"),
    PAPER_1_15_2("paper-1.15.2", "https://api.papermc.io/v2/projects/paper/versions/1.15.2/builds/393/downloads/paper-1.15.2-393.jar"),
    PAPER_1_14_4("paper-1.14.4", "https://api.papermc.io/v2/projects/paper/versions/1.14.4/builds/245/downloads/paper-1.14.4-245.jar"),
    PAPER_1_13_2("paper-1.13.2", "https://api.papermc.io/v2/projects/paper/versions/1.13.2/builds/657/downloads/paper-1.13.2-657.jar"),
    PAPER_1_12_2("paper-1.12.2", "https://api.papermc.io/v2/projects/paper/versions/1.12.2/builds/1620/downloads/paper-1.12.2-1620.jar");

    private final String name;
    private final String url;

    ServerVersions(String name, String url) {
        this.name = name;
        this.url = url;
    }
}
