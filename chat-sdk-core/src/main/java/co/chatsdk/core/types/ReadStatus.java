package co.chatsdk.core.types;

/**
 * Created by ben on 10/5/17.
 */

public class ReadStatus {

    public static int Hide = -2;
    public static int None = 0;
    public static int Delivered = 1;
    public static int Read = 2;

    private int status;

    public static ReadStatus hide() {
        return new ReadStatus(Hide);
    }

    public static ReadStatus none() {
        return new ReadStatus(None);
    }

    public static ReadStatus delivered() {
        return new ReadStatus(Delivered);
    }

    public static ReadStatus read () {
        return new ReadStatus(Read);
    }

    public ReadStatus (int status) {
        this.status = status;
    }

    public int getValue() {
        return status;
    }

    public boolean is (ReadStatus status) {
        return status.getValue() == getValue();
    }

    public boolean is (ReadStatus... statuses) {
        for (ReadStatus status: statuses) {
            if (is(status)) {
                return true;
            }
        }
        return false;
    }
}
