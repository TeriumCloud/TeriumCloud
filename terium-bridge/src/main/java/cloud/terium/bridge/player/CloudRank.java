package cloud.terium.bridge.player;

import lombok.Getter;

@Getter
public enum CloudRank {
    Admin("Admin", "<gradient:#bc01bc:#1d1dfd>ADMIN</gradient>", "", 500, 10),
    Mod("Moderator", "<gradient:#b0fcfc:#3dfafa>MOD</gradient>", "", 400, 11),
    Content_Creator("Content Creator", "<gradient:#9e27b3:#f23ddd:#802f9c>CONTENT CREATOR</gradient>", "", 300, 12),
    VIP("VIP", "<gradient:#fefe40:#f1c56c>VIP</gradient>", "", 200, 13),
    Default("Default", "<gradient:#9c999c:#ffffff>DEFAULT</gradient>", "", 100, 14);

    private final String name;
    private final String prefix;
    private final String suffix;
    private final int power;
    private final int sort;

    CloudRank(String name, String prefix, String suffix, int power, int sort) {
        this.name = name;
        this.prefix = prefix;
        this.suffix = suffix;
        this.power = power;
        this.sort = sort;
    }
}