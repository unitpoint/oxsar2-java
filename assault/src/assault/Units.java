/**
 * NetAssault Oxsar http://netassault.ru, http://oxsar.ru Copyright (c)
 * 2009-2010 UnitPoint <support@unitpoint.ru>
 */
package assault;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;

public class Units {

    private Participant participant = null;
    private int unitid = 0;
    private int type = 0; // UNIT_TYPE_FLEET, UNIT_TYPE_DEFENSE
    private String name = "";
    private int attack = 0;
    private int shield = 0;
    private int shell = 0;
    private int userid = 0;
    private int capacity = 0;
    private int basicMetal = 0;
    private int basicSilicon = 0;
    private int basicHydrogen = 0;
    private int front = 0;
    private double weight = 0;
    private double ballisticsLevel = 0;
    private double maskingLevel = 0;
    
    // private List<Unit> units = new ArrayList<Unit>();
    
    private int startBattleQuantity = 0;
    private int startBattleDamaged = 0;
    private int startBattleDamagedShellPercent = 100;

    private double startTurnWeight = 0;
    private int startTurnQuantity = 0;
    private int startTurnDamaged = 0;
    private double startTurnDamagedShellPercent = 100;

    // private double startTurnShield = 0;
    private double startTurnShell = 0;
    private double turnShield = 0;
    private double turnShell = 0;
    private int turnFiredQuantity = 0;
    
    private int startTurnQuantityDiff = 0;
    // private int endTurnFiredDiff = 0;

    private int totalGrasped = 0;
    private int totalDestroyed = 0;
    
    // private int totalFired = 0;
    // private int prevTotalFired = 0;

    public int getStartBattleQuantity() {
        return startBattleQuantity;
    }

    public int getStartBattleDamaged() {
        return startBattleDamaged;
    }

    public int getStartBattleDamagedShellPercent() {
        return startBattleDamagedShellPercent;
    }

    public int getStartTurnQuantity() {
        return startTurnQuantity;
    }

    public int getStartTurnDamaged() {
        return startTurnDamaged;
    }

    public int getStartTurnDamagedShellPercent() {
        return (int)startTurnDamagedShellPercent;
    }

    public int getFront() {
        return front;
    }

    public double getWeight() {
        return weight;
    }

    public double getStartTurnWeight() {
        return startTurnWeight; // fullWeight = (long)weight * quantity;
    }

    public double getBallisticsLevel() {
        return ballisticsLevel;
    }

    public double getMaskingLevel() {
        return maskingLevel;
    }

    public double getStartTurnCapacity() {
        return (double) capacity * getStartTurnQuantity();
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public double getPoints() {
        return ((basicMetal + basicSilicon + basicHydrogen) / 1000.0) * 2.0;
    }

    public int getAttack() {
        return attack;
    }

    /* public void setAttack(int value) {
        attack = value;
    } */

    public int getShield() {
        return shield;
    }

    /* public void setShield(int value) {
        shield = value;
    } */

    public int getShell() {
        return shell;
    }

    /*
     public void setShell(double shell)
     {
     this.shell = shell;
     }
     */
    
    public int getUnitid() {
        return unitid;
    }

    public String getName() {
        return name;
    }

    public int getStartTurnQuantityDiff() {
        return startTurnQuantityDiff; // endTurnQuantity - startTurnQuantity;
    }

    public int getTotalDestroyed() {
        return totalDestroyed;
    }

    /*
    public int getEndFiredDiff() {
        return endTurnFiredDiff;
    }

    public int getTotalFiredDestroyed() {
        return totalFired;
    }
    */

    /*
     public int getTotalDamaged()
     {
     return totalDamaged;
     }
     */
    public int getTotalGrasped() {
        return totalGrasped;
    }

    public int getBasicMetal() {
        return basicMetal;
    }

    public void setBasicMetal(int value) {
        basicMetal = value;
    }

    public int getBasicSilicon() {
        return basicSilicon;
    }

    public void setBasicSilicon(int value) {
        basicSilicon = value;
    }

    public int getBasicHydrogen() {
        return basicHydrogen;
    }

    public void setBasicHydrogen(int value) {
        basicHydrogen = value;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Participant getParticipant() {
        return participant;
    }
    /* public void setParticipant(Participant p)
     {
     this.participant = p;
     } */

    public Units(Participant p, int userid, int unitid, String name,
            int attack,
            int shield,
            int shell, int front,
            double ballistics, double masking,
            int quantity, int damaged, int damagedShellPercent)
    {
        if (damagedShellPercent > 99 || damaged <= 0) {
            damagedShellPercent = 100;
            damaged = 0;
        }

        this.participant = p;
        this.unitid = unitid;
        this.name = name;
        this.userid = userid;
        this.attack = attack;
        this.shield = shield;
        this.shell = shell;
        this.front = front;
        this.ballisticsLevel = ballistics;
        this.maskingLevel = masking;

        this.weight = Math.pow(2, front);
        
        this.startBattleQuantity = quantity;
        this.startBattleDamaged = damaged;
        this.startBattleDamagedShellPercent = damagedShellPercent;
        
        this.startTurnWeight = this.weight * quantity;
        this.startTurnQuantity = quantity;
        this.startTurnDamaged = damaged;
        this.startTurnDamagedShellPercent = damagedShellPercent;
        this.turnShield = this.startTurnQuantity * this.shield;
        this.startTurnShell = this.turnShell = (double)(this.startTurnQuantity - this.startTurnDamaged) * this.shell 
                + (double)this.startTurnDamaged * this.shell * this.startTurnDamagedShellPercent / 100;
    }

    public void setupArtefactBonus() {
        int shellPowerArtefacts;
        int shieldPowerArtefacts;
        int attackPowerArtefacts;
        int shellPower10Artefacts;
        int shieldPower10Artefacts;
        int attackPower10Artefacts;
        int neutronAffectorArtefacts;
        if (participant.isAttacker()) {
            shellPowerArtefacts = Assault.atterShellPowerArtefacts;
            shieldPowerArtefacts = Assault.atterShieldPowerArtefacts;
            attackPowerArtefacts = Assault.atterAttackPowerArtefacts;
            shellPower10Artefacts = Assault.atterShellPower10Artefacts;
            shieldPower10Artefacts = Assault.atterShieldPower10Artefacts;
            attackPower10Artefacts = Assault.atterAttackPower10Artefacts;
            neutronAffectorArtefacts = Assault.atterNeutronAffectorArtefacts;
        } else {
            shellPowerArtefacts = Assault.defenderShellPowerArtefacts;
            shieldPowerArtefacts = Assault.defenderShieldPowerArtefacts;
            attackPowerArtefacts = Assault.defenderAttackPowerArtefacts;
            shellPower10Artefacts = Assault.defenderShellPower10Artefacts;
            shieldPower10Artefacts = Assault.defenderShieldPower10Artefacts;
            attackPower10Artefacts = Assault.defenderAttackPower10Artefacts;
            neutronAffectorArtefacts = Assault.defenderNeutronAffectorArtefacts;
        }

        double shellBonusPower = 1;
        double shieldBonusPower = 1;
        if (shellPowerArtefacts != 0 || shellPower10Artefacts != 0 || neutronAffectorArtefacts != 0) {
            shellBonusPower = Math.max(0.1, 1 + shellPower10Artefacts + 0.1 * shellPowerArtefacts + 10 * neutronAffectorArtefacts);
            shell = (int) Math.round(shell * shellBonusPower);
        }
        if (shieldPowerArtefacts != 0 || shieldPower10Artefacts != 0 || neutronAffectorArtefacts != 0) {
            shieldBonusPower = Math.max(0.1, 1 + shieldPower10Artefacts + 0.1 * shieldPowerArtefacts + 10 * neutronAffectorArtefacts);
            shield = (int) Math.round(shield * shieldBonusPower);
        }
        if (attackPowerArtefacts != 0 || attackPower10Artefacts != 0 || neutronAffectorArtefacts != 0) {
            double bonusPower = Math.max(0.1, 1 + attackPower10Artefacts + 0.1 * attackPowerArtefacts + 10 * neutronAffectorArtefacts);
            attack = (int) Math.round(attack * bonusPower);
        }

        this.turnShield = this.startTurnQuantity * this.shield;
        this.startTurnShell = this.turnShell = (double)(this.startTurnQuantity - this.startTurnDamaged) * this.shell 
                + (double)this.startTurnDamaged * this.shell * this.startTurnDamagedShellPercent / 100;
        
        /* if (shellPowerArtefacts != 0 || shieldPowerArtefacts != 0
                || shellPower10Artefacts != 0 || shieldPower10Artefacts != 0
                || neutronAffectorArtefacts != 0) {
            for (Unit unit : curDamagedUnits) {
                unit.shell = (int) Math.round(unit.shell * shellBonusPower);
                unit.shield0 = (int) Math.round(unit.shield0 * shieldBonusPower);
                unit.shield1 = (int) Math.round(unit.shield1 * shieldBonusPower);
                unit.shield2 = (int) Math.round(unit.shield2 * shieldBonusPower);
            }
        } */
    }

    public double processAttack(Units underFireUnits, double shotsNumber) {
        boolean isBattleAttackerUnderFire = participant.isDefender();
        double attack = getAttack();
        if (underFireUnits.startTurnQuantity > 0 && attack > 0 && shotsNumber > 0) {
            double ballistics = getBallisticsLevel();
            double masking = underFireUnits.getMaskingLevel();

            // int useQuantity = (int) (curQuantity + (quantity - curQuantity) * attackMissFactor);
            double maskingEffect = masking - ballistics;
            if (maskingEffect > 0) {
                // maskingEffect *= 0.5;
            }else{
                maskingEffect = 0;
            }
            maskingEffect = 1.0 - 1.0 / (1 + maskingEffect * 2 / 10.0);
            double missedShotsNumber = Assault.clampVal(Math.floor(shotsNumber * maskingEffect), 0, shotsNumber);

            double ignoreAttack = underFireUnits.shield / 100;
            if (underFireUnits.unitid == Assault.UNIT_SMALL_PLANET_SHIELD || underFireUnits.unitid == Assault.UNIT_LARGE_PLANET_SHIELD) {
                ignoreAttack = 0;
            }
            
            if (missedShotsNumber > 0) {
                /* if (damage <= ignoreDamage) {
                    returnDamage += damage * missedShotsNumber;
                    if (isBattleAttackerUnderFire) {
                        Assault.attackerShield += damage * missedShotsNumber;
                    } else {
                        Assault.defenderShield += damage * missedShotsNumber;
                    }
                    // Assault.addFireStat(participant.isAttacker(), attackerUnitid, underFireUnits.unitid, 0, false);
                }else{
                    
                } */
                shotsNumber -= missedShotsNumber;
            }
            
            
            double fullTurnShield = underFireUnits.startTurnQuantity * underFireUnits.shield;
            // double fullShellSum = this.startTurnQuantity * this.shell;

            double shieldDamageFactor = underFireUnits.turnShield / fullTurnShield;
            double shieldDestroyFactor = Math.min(1.0, (1.4 - shieldDamageFactor) * 2.0);

            double shieldDestroy = underFireUnits.turnShield * shieldDestroyFactor;
            double shieldDestroyUnits = Math.floor(shieldDestroy / underFireUnits.shield);
            shieldDestroy = shieldDestroyUnits * underFireUnits.shield;
            shieldDestroyFactor = shieldDestroy / underFireUnits.turnShield;
            
            double shieldExistFactor = 1 - shieldDestroyFactor;
            double shieldExist = underFireUnits.turnShield * shieldExistFactor;
            
            double shieldShotsNumber = Math.ceil(shotsNumber * shieldExistFactor);
            if(shieldShotsNumber > 0){
                double shieldShotsPower = attack * shieldShotsNumber;
                if(attack > ignoreAttack){
                    if(shieldExist >= shieldShotsPower){
                        underFireUnits.turnShield -= shieldShotsPower;
                    }else{
                        underFireUnits.turnShield -= shieldExist;
                        shieldShotsNumber = Math.ceil(shieldExist / attack);
                        shieldShotsPower = shieldExist;
                    }
                }
                if (isBattleAttackerUnderFire) {
                    Assault.attackerShield += shieldShotsPower;
                } else {
                    Assault.defenderShield += shieldShotsPower;
                }
                shotsNumber -= shieldShotsNumber;
            }
            
            double shellShotsPower = attack * shotsNumber;
            if(underFireUnits.turnShell < shellShotsPower){
                double shellShotsNumber = Math.ceil(underFireUnits.turnShell / attack);
                shotsNumber -= shellShotsNumber;
                shellShotsPower = underFireUnits.turnShell;
            }else{
                shotsNumber = 0;                
            }
            underFireUnits.turnShell -= shellShotsPower;
            if (isBattleAttackerUnderFire) {
                Assault.attackerShellDestroyed += shellShotsPower;
            } else {
                Assault.defenderShellDestroyed += shellShotsPower;
            }
            return shotsNumber;
        }
        return 0;
    }

    /* public void subStartTurnUnits(int destroyQuantity) {
        startTurnQuantity = Math.max(0, startTurnQuantity - destroyQuantity);
        startTurnDamaged = Math.max(0, startTurnDamaged - destroyQuantity);
    } */

    public void fireTurnUnits(int destroyQuantity) {
        turnFiredQuantity = Math.min(startTurnQuantity, turnFiredQuantity + destroyQuantity);
    }

    public void addStartTurnQuantity(int addQuantity, int unitShell) {
        if (addQuantity > 0) {
            if (unitShell < shell) { // * 0.99)
                double oldPercentSum = startTurnDamagedShellPercent * startTurnDamaged;
                double addPercentSum = (unitShell * 100.0 / shell) * addQuantity;
                startTurnDamagedShellPercent = (oldPercentSum + addPercentSum) / (startTurnDamaged + addQuantity);
                startTurnDamaged += addQuantity;
            }
            startTurnQuantity += addQuantity;
            startTurnWeight = weight * startTurnQuantity;
            startTurnQuantityDiff += addQuantity;
            // turnAtterQuantity += addQuantity;
            // turnAtterWeight = weight * turnAtterQuantity;
        }
    }

    public void addRepairedQuantity(int quantity) {
        addStartTurnQuantity(quantity, shell);
    }

    public void addGraspedQuantity(int quantity) {
        if (quantity > 0) {
            int percent;
            if (Assault.randInt(0, 10) == 0) {
                if (Assault.randInt(0, 10) == 0) {
                    percent = Assault.randInt(95, 99);
                } else {
                    percent = Assault.randInt(65, 90);
                }
            } else {
                percent = Assault.randInt(15, 45);
            }
            addStartTurnQuantity(quantity, percent * shell / 100);
            totalGrasped += quantity;
        }
    }

    public void destroyAfterTurnFinished(int destroyQuantity) {
        fireTurnUnits(destroyQuantity);
        finishTurn();
    }

    public void finishTurn() {
        // endTurnFiredDiff = prevTotalFired - totalFired;
        // prevTotalFired = totalFired;

        double turnQuantity = startTurnQuantity - turnFiredQuantity;
        double turnDamaged = startTurnDamaged;
        double turnShellPercent = 0;
        
        /* double startTurnShield = startTurnQuantity * shield;
        if(turnShield < startTurnShield){
            double turnShieldDestroyed = startTurnShield - turnShield;
            double maxShieldDamagedUnits = Math.ceil(turnShieldDestroyed / shield);
        } */
        
        if(turnShell < startTurnShell){
            double turnShellDestroyed = startTurnShell - turnShell;
            if(turnDamaged > 0){
                double damagedUnitShell = shell * startTurnDamagedShellPercent / 100;
                double maxDamagedUnitsDestroyed = Math.min(turnDamaged, Math.floor(turnShellDestroyed / damagedUnitShell));
                double damagedUnitsDestroyed = Math.ceil(maxDamagedUnitsDestroyed * 0.85); // Assault.randInt(maxDamagedUnitsDestroyed/2, maxDamagedUnitsDestroyed);
                turnQuantity -= damagedUnitsDestroyed;
                turnDamaged -= damagedUnitsDestroyed;
                turnShellDestroyed -= damagedUnitsDestroyed * damagedUnitShell;
            }
            double maxUnitsDestroyed = Math.floor(turnShellDestroyed / shell);
            double unitsDestroyed = Math.ceil(maxUnitsDestroyed * 0.85); // Assault.randInt(maxUnitsDestroyed/2, maxUnitsDestroyed);
            turnQuantity -= unitsDestroyed;
            double minDamaged = Math.floor((turnShell - turnQuantity * shell) / (shell * 0.1 - shell));
            double maxDamaged = Math.floor((turnShell - turnQuantity * shell) / (shell * 0.9 - shell));
            minDamaged = Assault.clampVal(minDamaged, turnDamaged, turnQuantity);
            maxDamaged = Assault.clampVal(maxDamaged, minDamaged, turnQuantity);   
            double deltaDamaged = (maxDamaged - minDamaged) * 0.5;
            minDamaged += deltaDamaged * 0.4;
            maxDamaged -= deltaDamaged * 0.4;
            turnDamaged = Math.round(Assault.randDouble(minDamaged, maxDamaged)); // int)((minDamaged + maxDamaged) / 2); // Assault.randInt(minDamaged, maxDamaged);
            if(turnDamaged == 0 && turnShellDestroyed > 0){
                turnDamaged = 1;
            }
            turnShellPercent = turnDamaged > 0 ? (turnShell - (turnQuantity - turnDamaged) * shell) * 100 / (turnDamaged * shell) : 0;
            if(turnShellPercent < 1 || turnShellPercent > 99){
                int i = 0;
                turnDamaged = 0;
            }
            if(turnShellPercent == 0 && turnDamaged > 0){
                int i = 0;
            }
        }else if(turnShell < 1){
            turnQuantity = turnDamaged = 0;
            turnShellPercent = 0;
        }
        
        startTurnQuantityDiff = (int)turnQuantity - startTurnQuantity;
        // recalculateDamaged();

        int destroyed = startTurnQuantity - (int)turnQuantity;
        totalDestroyed += destroyed;

		// double bulkIntoDebris = Assault.getBulkIntoDebris(type);
        // debrisMetal += Math.floor(basicMetal * destroyed * bulkIntoDebris);
        // debrisSilicon += Math.floor(basicSilicon * destroyed * bulkIntoDebris);
        startTurnQuantity = (int)turnQuantity;
        startTurnWeight = weight * startTurnQuantity;
        startTurnDamaged = (int)turnDamaged;
        startTurnDamagedShellPercent = turnShellPercent;
        
        turnFiredQuantity = 0;
        this.turnShield = this.startTurnQuantity * this.shield;
        this.startTurnShell = this.turnShell = (double)(this.startTurnQuantity - this.startTurnDamaged) * this.shell 
                + (double)this.startTurnDamaged * this.shell * this.startTurnDamagedShellPercent / 100;
        
        // turnAtterQuantity = curQuantity;
        // turnAtterWeight = weight * turnAtterQuantity;
    }

    public void finish() {
        if (!Assault.debugmode) {
            String sql = "";
            Statement stmt = Database.createStatement();
            try {
                /* if(getTotalGrasped() > 0 && getTotalGrasped() == getCurQuantity())
                 {
                 }
                 else // if(getTotalLoss() > 0 || getTotalDamaged() > 0) */
                {
                    if (getStartBattleQuantity() > 0) {
                        sql = "UPDATE " + Assault.getTablePrefix() + "fleet2assault SET "
                                + " quantity = '" + getStartTurnQuantity() + "' "
                                + ", damaged = '" + getStartTurnDamaged() + "' "
                                + ", grasped = '" + totalGrasped + "' "
                                + ", shell_percent = '" + getStartTurnDamagedShellPercent() + "' "
                                + " WHERE assaultid = '" + Assault.assaultid + "' " // AND userid = '" + userid + "' "
                                + " AND unitid = '" + unitid + "' AND participantid = '" + participant.getParticipantId() + "'";
                        Assault.updateAssault("[sql] " + sql, true);
                        stmt.executeUpdate(sql);
                    } else {
                        sql = "REPLACE INTO " + Assault.getTablePrefix() + "fleet2assault "
                                + " (assaultid, userid, unitid, participantid, mode, quantity, damaged, shell_percent, grasped, org_quantity, org_damaged, org_shell_percent) "
                                + " values("
                                + "'" + Assault.assaultid + "'"
                                + " , " + (userid != 0 ? "'" + userid + "'" : "NULL")
                                + " , '" + unitid + "'"
                                + " , '" + participant.getParticipantId() + "'"
                                + " , '" + participant.getType() + "'"
                                + " , '" + getStartTurnQuantity() + "'"
                                + " , '" + getStartTurnDamaged() + "'"
                                + " , '" + getStartTurnDamagedShellPercent() + "'"
                                + " , '" + totalGrasped + "'"
                                + " , '" + getStartBattleQuantity() + "'"
                                + " , '" + getStartBattleDamaged() + "'"
                                + " , '" + getStartBattleDamagedShellPercent() + "'"
                                + ")";
                        Assault.updateAssault("[sql] " + sql, true);
                        stmt.executeUpdate(sql);
                    }
                }
            } catch (SQLException e) {
                // Assault.updateAssault(e.getMessage(), true);
                System.err.println(sql);
                e.printStackTrace();
            }
        }
    }
}
