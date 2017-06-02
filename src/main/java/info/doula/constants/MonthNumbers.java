package info.doula.constants;

/**
 * Created by Faruque on 12/22/2016.
 */
public enum MonthNumbers {

    JANUARY(1),
    DECEMBER(12);

    private int value;

    private MonthNumbers(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}