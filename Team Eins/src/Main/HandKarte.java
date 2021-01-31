package Main;

import java.io.Serializable;


public class HandKarte extends Karte  implements Serializable {


    /**
     * @param value
     * @param covered
     */
    public HandKarte(int value, boolean covered) {
        super(value, covered);
    }

    public String toString() {
        return String.valueOf(this.getValue());
    }


}