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
        this.turnShield = (double)this.startTurnQuantity * this.shield;
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

        this.turnShield = (double)this.startTurnQuantity * this.shield;
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

    public int processAttack(Units underFireUnits, int attackQuantity) {
        int rapidFire = Math.max(1, Assault.getRapidFire(unitid, underFireUnits.getUnitid()));
        double shotsNumber = rapidFire * attackQuantity;

        System.out.printf("[Units.processAttack] BEGIN pid: %d, uid: %d (%s) => %d (%s)\nq: %d, rf: %d, shots: %.0f, attack: %d\nunderFireUnits.turnShield: %.0f, turnShell: %.0f\n", 
                participant.getParticipantId(), unitid, name, underFireUnits.unitid, underFireUnits.name,
                attackQuantity, rapidFire, shotsNumber, getAttack(), underFireUnits.turnShield, underFireUnits.turnShell);
        
        boolean isBattleAttackerUnderFire = participant.isDefender();
        if (!isBattleAttackerUnderFire) {
            Assault.attackerShots += shotsNumber;
            Assault.attackerPower += attack * shotsNumber;
        } else {
            Assault.defenderShots += shotsNumber;
            Assault.defenderPower += attack * shotsNumber;
        }

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
                System.out.printf("missedShotsNumber: %.0f\n", missedShotsNumber);
                shotsNumber -= missedShotsNumber;
            }
            
            if(underFireUnits.shield > 0){
                double fullTurnShield = (double)underFireUnits.startTurnQuantity * underFireUnits.shield;
                // double fullShellSum = this.startTurnQuantity * this.shell;

                double shieldDamageFactor = underFireUnits.turnShield / fullTurnShield;
                // double shieldDestroyFactor = Math.min(1.0, (1.1 - shieldDamageFactor) * 1.0);
                double shieldDestroyFactor = Assault.clampVal((1.0 - shieldDamageFactor) * 1.0, 0.01, 1.0);
                // double shieldDestroyFactor = 1.0 - shieldDamageFactor;

                double shieldDestroy = underFireUnits.turnShield * shieldDestroyFactor;
                double shieldDestroyUnits = Math.floor(shieldDestroy / underFireUnits.shield);
                if(shieldDestroyUnits > shotsNumber){
                    shieldDestroyUnits = shotsNumber;
                }
                shieldDestroy = shieldDestroyUnits * underFireUnits.shield;
                shieldDestroyFactor = shieldDestroy / underFireUnits.turnShield;

                double shieldExistFactor = 1 - shieldDestroyFactor;
                double shieldExist = underFireUnits.turnShield * shieldExistFactor;

                double shieldShotsNumber = Math.ceil(shotsNumber * shieldExistFactor);
                if(shieldShotsNumber > 0){
                    double shieldShotsPower = attack * shieldShotsNumber;
                    if(attack > ignoreAttack){
                        double maxShieldShotsPower = shieldShotsNumber * underFireUnits.shield;
                        if(shieldShotsPower > maxShieldShotsPower && unitid != Assault.UNIT_INTERPLANETARY_ROCKET){
                            shieldShotsPower = maxShieldShotsPower;
                        }
                        if(shieldShotsPower > shieldExist){
                            shieldShotsPower = shieldExist;
                        }
                        underFireUnits.turnShield -= shieldShotsPower;
                        shieldShotsNumber = Math.round(shieldShotsPower / attack);
                        /* if(shieldExist >= shieldShotsPower){
                            underFireUnits.turnShield -= shieldShotsPower;
                            shieldShotsNumber = Math.ceil(shieldShotsPower / attack);
                        }else{
                            underFireUnits.turnShield -= shieldExist;
                            shieldShotsNumber = Math.ceil(shieldExist / attack);
                            shieldShotsPower = shieldExist;
                        } */
                    }
                    if (isBattleAttackerUnderFire) {
                        Assault.attackerShield += shieldShotsPower;
                    } else {
                        Assault.defenderShield += shieldShotsPower;
                    }
                    shotsNumber -= shieldShotsNumber;
                    System.out.printf("shieldShots: %.0f, underFireUnits.turnShield: %.0f\n", shieldShotsNumber, underFireUnits.turnShield);
                }
            }
            
            double shellShotsPower = attack * shotsNumber;
            double maxShellShotsPower = underFireUnits.shell * shotsNumber;
            if(shellShotsPower > maxShellShotsPower && unitid != Assault.UNIT_INTERPLANETARY_ROCKET){
                shellShotsPower = maxShellShotsPower;
            }
            double shellShotsNumber = shotsNumber;
            if(underFireUnits.turnShell < shellShotsPower){
                shellShotsNumber = Math.ceil(underFireUnits.turnShell / attack);
                shellShotsPower = underFireUnits.turnShell;
            }
            underFireUnits.turnShell -= shellShotsPower;
            shotsNumber -= shellShotsNumber;
            
            if (isBattleAttackerUnderFire) {
                Assault.attackerShellDestroyed += shellShotsPower;
            } else {
                Assault.defenderShellDestroyed += shellShotsPower;
            }
            System.out.printf("shellShots: %.0f, underFireUnits.turnShell: %.0f\n", shellShotsNumber, underFireUnits.turnShell);
            System.out.printf("[Units.processAttack] END pid: %d, uid: %d (%s) => %d (%s), ships shots remain: %.0f\n\n", 
                    participant.getParticipantId(), unitid, name, underFireUnits.unitid, underFireUnits.name,
                    shotsNumber / rapidFire);
            
            return (int)(shotsNumber / rapidFire);
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
        double turnShellPercent = startTurnDamagedShellPercent;
        
        System.out.printf("[Units.finishTurn] BEGIN pid: %d, uid: %d (%s), tq: %.0f, td: %.0f, tsp: %.0f\n", 
                participant.getParticipantId(), unitid, name, turnQuantity, turnDamaged, turnShellPercent);
        
        /* double startTurnShield = startTurnQuantity * shield;
        if(turnShield < startTurnShield){
            double turnShieldDestroyed = startTurnShield - turnShield;
            double maxShieldDamagedUnits = Math.ceil(turnShieldDestroyed / shield);
        } */
        
        if(turnShell < startTurnShell){
            double turnShellDestroyed = startTurnShell - turnShell;
            System.out.printf("turnShell < startTurnShell (%.0f < %.0f), turnShellDestroyed: %.0f, turnDamaged: %.0f\n",
                    turnShell, startTurnShell, turnShellDestroyed, turnDamaged);
            if(turnDamaged > 0){
                double damagedUnitShell = shell * startTurnDamagedShellPercent / 100;
                double maxDamagedUnitsDestroyed = Math.min(turnDamaged, Math.floor(turnShellDestroyed / damagedUnitShell));
                double damagedUnitsDestroyed = Math.ceil(maxDamagedUnitsDestroyed * 0.85); // Assault.randInt(maxDamagedUnitsDestroyed/2, maxDamagedUnitsDestroyed);
                turnQuantity -= damagedUnitsDestroyed;
                turnDamaged -= damagedUnitsDestroyed;
                turnShellDestroyed -= damagedUnitsDestroyed * damagedUnitShell;
                System.out.printf("turnDamaged > 0, max damaged: %.0f, real damaged: %.0f, tq: %.0f, td: %.0f, tsp: %.0f\n",
                        maxDamagedUnitsDestroyed, damagedUnitsDestroyed, turnQuantity, turnDamaged, turnShellPercent);
            }
            double maxUnitsDestroyed = Math.min(turnQuantity, Math.floor(turnShellDestroyed / shell));
            double unitsDestroyed = Math.ceil(maxUnitsDestroyed * 0.85); // Assault.randInt(maxUnitsDestroyed/2, maxUnitsDestroyed);
            turnQuantity -= unitsDestroyed;
            
            double minDamaged = (turnShellDestroyed - unitsDestroyed * shell) / (shell * 0.99);
            double maxDamaged = (turnShellDestroyed - unitsDestroyed * shell) / (shell * 0.1);
            // double minDamaged = Math.floor((turnShell - turnQuantity * shell) / (shell * 0.1 - shell));
            // double maxDamaged = Math.floor((turnShell - turnQuantity * shell) / (shell * 0.9 - shell));
            minDamaged = Assault.clampVal(minDamaged, turnDamaged, turnQuantity);
            maxDamaged = Assault.clampVal(maxDamaged, minDamaged, turnQuantity);   
            double deltaDamaged = (maxDamaged - minDamaged) * 0.5;
            minDamaged += deltaDamaged * 0.49;
            maxDamaged -= deltaDamaged * 0.49;
            turnDamaged = Math.round(Assault.randDouble(minDamaged, maxDamaged)); // int)((minDamaged + maxDamaged) / 2); // Assault.randInt(minDamaged, maxDamaged);
            if(turnDamaged == 0 && turnShellDestroyed > 0 && turnQuantity > 0){
                turnDamaged = 1;
            }
            turnShellPercent = turnDamaged > 0 ? (turnShell - (turnQuantity - turnDamaged) * shell) * 100 / (turnDamaged * shell) : 0;
            
            double remainTurnShellDestroyed = turnShellDestroyed - (maxUnitsDestroyed - unitsDestroyed) * shell;
            if(remainTurnShellDestroyed < 0){
                remainTurnShellDestroyed = 0;
            }
            boolean accurateExploding = true;
            if(accurateExploding){
                maxUnitsDestroyed = Math.ceil(remainTurnShellDestroyed / (shell * Assault.clampVal(turnShellPercent, 1, 99) / 100));
                if(maxUnitsDestroyed > turnDamaged){
                    maxUnitsDestroyed = turnDamaged;
                }
            }else{
                maxUnitsDestroyed = turnDamaged;
            }
            
            System.out.printf("PRE td: %.0f, tsp: %.0f, rtsd: %.0f, mud: %.0f\n", turnDamaged, turnShellPercent, remainTurnShellDestroyed, maxUnitsDestroyed);
            
            if(turnShellPercent < 20){
                double explodingUnits = maxUnitsDestroyed;
                turnQuantity -= explodingUnits;
                turnDamaged -= explodingUnits;
            }else if(turnShellPercent < 65){
                double explodingChance = 1 - turnShellPercent / 100;
                double explodingUnits = Math.ceil(maxUnitsDestroyed * explodingChance);
                turnQuantity -= explodingUnits;
                turnDamaged -= explodingUnits;
            }else if(turnShellPercent > 99){
                turnDamaged = 0;
            }
        }else if(turnShell < 1){
            turnQuantity = turnDamaged = 0;
            turnShellPercent = 0;
            System.out.printf("turnShell < 1");
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
        this.turnShield = (double)this.startTurnQuantity * this.shield;
        this.startTurnShell = this.turnShell = (double)(this.startTurnQuantity - this.startTurnDamaged) * this.shell 
                + (double)this.startTurnDamaged * this.shell * this.startTurnDamagedShellPercent / 100;
        
        // turnAtterQuantity = curQuantity;
        // turnAtterWeight = weight * turnAtterQuantity;
        
        System.out.printf("[Units.finishTurn] END pid: %d, uid: %d (%s), tq: %.0f, td: %.0f, tsp: %.0f\n\n", 
                participant.getParticipantId(), unitid, name, turnQuantity, turnDamaged, turnShellPercent);
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
