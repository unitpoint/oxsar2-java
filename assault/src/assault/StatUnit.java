/**
 * NetAssault Oxsar http://netassault.ru, http://oxsar.ru Copyright (c)
 * 2009-2010 UnitPoint <support@unitpoint.ru>
 */
package assault;

public class StatUnit {

    public int unitid;
    public String name;
    public int horizUnitsNumber = 0;
    public int vertUnitsNumber = 0;
    public boolean horizExists = false;
    public boolean vertExists = false;

    public StatUnit(int unitid, String name) {
        this.unitid = unitid;
        this.name = name;
    }

    public String idName() {
        if (unitid == Assault.UNIT_SUMMARY) {
            return "sum";
        }
        return "" + unitid;
    }

    public String langName() {
        return "{lang}" + name + "{/lang}";
    }
}
