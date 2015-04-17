/**
 * NetAssault Oxsar http://netassault.ru, http://oxsar.ru Copyright (c)
 * 2009-2010 UnitPoint <support@unitpoint.ru>
 */
package assault;

public class Unit {

    public int shield0;
    public int shield1;
    public int shield2;
    public int shell;

    public Unit() {
        shield0 = 0;
        shield1 = 0;
        shield2 = 0;
        shell = 0;
    }

    public Unit(int shield0, int shield1, int shield2, int shell) {
        Init(shield0, shield1, shield2, shell);
    }

    public void Init(int shield0, int shield1, int shield2, int shell) {
        this.shield0 = shield0;
        this.shield1 = shield1;
        this.shield2 = shield2;
        this.shell = shell;
    }

    public void SetShield(int shield0, int shield1, int shield2) {
        this.shield0 = shield0;
        this.shield1 = shield1;
        this.shield2 = shield2;
    }

    int getShield(int i) {
        switch (i) {
            case 1:
                return shield1;
            case 2:
                return shield2;
        }
        return shield0;
    }

    void setShield(int i, int value) {
        switch (i) {
            case 0:
                shield0 = value;
                break;
            case 1:
                shield1 = value;
                break;
            case 2:
                shield2 = value;
                break;
        }
    }
}
