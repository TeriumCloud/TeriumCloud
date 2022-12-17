package cloud.terium.cloudsystem;

import cloud.terium.cloudsystem.event.EventProvider;
import lombok.Getter;

@Getter
public class Terium {

    private static Terium terium;
    private final EventProvider eventProvider;

    public static void main(String[] args) {
        new Terium();
    }

    public Terium() {
        System.setProperty("org.jline.terminal.dumb", "true");

        terium = this;
        this.eventProvider = new EventProvider();
    }

    public static Terium getTerium() {
        return terium;
    }
}