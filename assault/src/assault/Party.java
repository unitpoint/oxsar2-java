/**
 * NetAssault Oxsar http://netassault.ru, http://oxsar.ru Copyright (c)
 * 2009-2010 UnitPoint <support@unitpoint.ru>
 */
package assault;

import java.util.*;

public class Party {
    
    public class SortUnitsByWeight implements Comparator<Units> {
        @Override
        public int compare(Units a, Units b) {
            double w1 = a.getStartTurnWeight();
            double w2 = b.getStartTurnWeight();
            if(w1 < w2){
                return 1;
            }else if(w1 > w2){
                return -1;
            }
            return 0;
        }        
    }

    public class SortUnitsByAttack implements Comparator<Units> {
        @Override
        public int compare(Units a, Units b) {
            int a1 = a.getAttack();
            int a2 = b.getAttack();
            return a2 - a1;
        }        
    }

    public class UnitsList extends ArrayList<Units> {

        // private static final long serialVersionUID = -2075289362305531763L;

        public long weight = 0;

        public void clear() {
            weight = 0;
            super.clear();
        }

        public boolean add(Units units) {
            weight += units.getStartTurnWeight();
            return super.add(units);
        }
        
        public void sortByWeight() {
            Collections.sort(this, new SortUnitsByWeight());
        }
        
        public void sortByAttack() {
            Collections.sort(this, new SortUnitsByAttack());
        }
    }

    public class PartySide {

        public List<Participant> participants = new ArrayList<Participant>();

        public Map<Integer, UnitsList> activeMapUnits = new HashMap<Integer, UnitsList>();
        public UnitsList activeUnits = new UnitsList();
        // public UnitsList activeFleetUnits = new UnitsList();
        public UnitsList activeDefenseUnits = new UnitsList();
        public UnitsList turnAtterUnits = new UnitsList();

        public void addMapUnits(Units units) {
            UnitsList list = activeMapUnits.get(units.getUnitid());
            if (list == null) {
                list = new UnitsList();
                activeMapUnits.put(units.getUnitid(), list);
            }
            list.add(units);
        }

        /* private Units getRandomAtterUnits() {
            UnitsList unitsList = turnAtterUnits;
            if (unitsList.size() > 0) {
                int i = 0;
                long num = Assault.randExclude(unitsList.weight);
                for (; i < unitsList.size() - 1; i++) {
                    Units units = unitsList.get(i);
                    long unitsWeight = units.getTurnAtterWeight();
                    if (num < unitsWeight) {
                        return units;
                    }
                    num -= unitsWeight;
                }
                return unitsList.get(i);
            }
            return null;
        } */

        /* private Units getRandomTargetUnits(UnitsList unitsList) {
            int count = unitsList.size();
            if (count == 1) {
                return unitsList.get(0);
            }
            if (count > 1) {
                int i = 0;
                long num = Assault.randExclude(unitsList.weight);
                for (; i < count - 1; i++) {
                    Units units = unitsList.get(i);
                    double unitsWeight = units.getStartTurnWeight();
                    if (num < unitsWeight) {
                        return units;
                    }
                    num -= unitsWeight;
                }
                return unitsList.get(i);
            }
            return null;
        }

        public Units getRandomUnits() {
            return getRandomTargetUnits(activeUnits);
        }

        public Units getRandomDefenseUnits() {
            return getRandomTargetUnits(activeDefenseUnits);
        }

        public Units getPrimaryTargetUnits(int primaryTargetUnitid) {
            if (primaryTargetUnitid > 0) {
                UnitsList list = activeUnitsMap.get(primaryTargetUnitid);
                if (list != null) {
                    Units units = getRandomTargetUnits(list);
                    if (units.getEndTurnQuantity()> 0) {
                        return units;
                    }
                    for (Units iterUnits : list) {
                        if (iterUnits.getEndTurnQuantity() > 0) {
                            return iterUnits;
                        }
                    }
                }
            }
            return null;
        }

        public Units getPrimaryTargetUnitsFor(int attackerUnitid, int primaryTargetUnitid, boolean allowInterceptor) {
            Units units = null;
            if (attackerUnitid == Assault.UNIT_INTERPLANETARY_ROCKET && allowInterceptor) {
                units = getPrimaryTargetUnits(Assault.UNIT_INTERCEPTOR_ROCKET);
                if (units != null) {
                    return units;
                }
            } else if (attackerUnitid == Assault.UNIT_LARGE_PLANET_SHIELD) {
                units = getPrimaryTargetUnits(Assault.UNIT_DEATH_STAR);
                if (units != null) {
                    return units;
                }
            }
            if (attackerUnitid == Assault.UNIT_INTERPLANETARY_ROCKET) {
                units = getRandomDefenseUnits();
            } else {
                units = getRandomUnits();
            }
            if (units != null) {
                int unitid = units.getUnitid();
                if (unitid == primaryTargetUnitid) {
                    if (units.getEndTurnQuantity()> 0) {
                        return units;
                    }
                } else if ((unitid == Assault.UNIT_SMALL_SHIELD
                        || unitid == Assault.UNIT_LARGE_SHIELD
                        || unitid == Assault.UNIT_SMALL_PLANET_SHIELD
                        || unitid == Assault.UNIT_LARGE_PLANET_SHIELD)
                        && units.getEndTurnQuantity() > 0) {
                    return units;
                }
            }
            Units primaryUnits = getPrimaryTargetUnits(primaryTargetUnitid);
            return primaryUnits != null ? primaryUnits : units;
        } */

        public void removeAllRockets() {
            for (Participant p : participants) {
                p.removeAllRockets();
            }
        }

        public void removeFleet() {
            for (Participant p : participants) {
                p.removeFleet();
            }
        }

        public void setupArtefactBonus() {
            for (Participant p : participants) {
                p.setupArtefactBonus();
            }
        }

        public void updateActiveUnits() {
            activeMapUnits.clear();
            activeUnits.clear();
            // activeFleetUnits.clear();
            activeDefenseUnits.clear();
            turnAtterUnits.clear();

            for (Participant p : participants) {
                for (Units units : p.getUnits()) {
                    if (units.getStartTurnQuantity() > 0) {
                        addMapUnits(units);
                        int unitid = units.getUnitid();
                        if (unitid != Assault.UNIT_INTERCEPTOR_ROCKET
                                && unitid != Assault.UNIT_INTERPLANETARY_ROCKET) {
                            activeUnits.add(units);
                            if (units.getType() == Assault.UNIT_TYPE_FLEET) {
                                // activeFleetUnits.add(units);
                            } else {
                                activeDefenseUnits.add(units);
                            }
                            int attack = units.getAttack();
                            if (attack > 0) {
                                turnAtterUnits.add(units);
                            }
                        }
                    }
                }
            }
            activeUnits.sortByWeight();
            activeDefenseUnits.sortByWeight();
            turnAtterUnits.sortByAttack();
        }

        public void finishTurn() {
            for (Participant participant : participants) {
                for (Units units : participant.getUnits()) {
                    units.finishTurn();

                    if (participant.isAttacker()) {
                        Assault.attackerShipsDestroyed += -units.getStartTurnQuantityDiff();
                    } else {
                        Assault.defenderShipsDestroyed += -units.getStartTurnQuantityDiff();
                    }
                }
            }
            updateActiveUnits();
        }

        public void calculateLost() {
            for (Participant participant : participants) {
                participant.calculateLost();

                Assault.debrisMetal += participant.getDebrisMetal();
                Assault.debrisSilicon += participant.getDebrisSilicon();

                if (participant.isAttacker()) {
                    Assault.atterLostMetal += participant.getLostMetal();
                    Assault.atterLostSilicon += participant.getLostSilicon();
                    Assault.atterLostHydrogen += participant.getLostHydrogen();
                } else {
                    Assault.defenderLostMetal += participant.getLostMetal();
                    Assault.defenderLostSilicon += participant.getLostSilicon();
                    Assault.defenderLostHydrogen += participant.getLostHydrogen();
                }
            }
        }

        public void finish() {
            for (Participant participant : participants) {
                participant.finish();
            }
        }

        public int getLostUnits() {
            int lostUnits = 0;
            for (Participant participant : participants) {
                lostUnits += participant.getLostUnits();
            }
            return lostUnits;
        }
    }

    private PartySide attacker = new PartySide();
    private PartySide defender = new PartySide();

    public List<Participant> getAttackers() {
        return attacker.participants;
    }

    public List<Participant> getDefenders() {
        return defender.participants;
    }

    public void addParticipant(Participant participant) {
        if (participant.isAttacker()) {
            attacker.participants.add(participant);
        } else {
            defender.participants.add(participant);
        }
    }

    public int getAttersNumber() {
        return attacker.participants.size();
    }

    public int getDefendersNumber() {
        return defender.participants.size();
    }

    public int getAtterLostUnits() {
        return attacker.getLostUnits();
    }

    public int getDefenderLostUnits() {
        return defender.getLostUnits();
    }

    public Participant getRandomAtter() {
        return attacker.participants.get(Assault.randExclude(attacker.participants.size()));
    }

    public Participant getRandomDefender() {
        return defender.participants.get(Assault.randExclude(defender.participants.size()));
    }

    public boolean atterHasUnits() {
        // return attacker.activeFleetUnits.size() > 0 || attacker.activeDefenseUnits.size() > 0 || atterHasRockets();
        return attacker.activeUnits.size() > 0 || atterHasRockets();
    }

    public boolean atterHasRockets() {
        return attacker.activeMapUnits.get(Assault.UNIT_INTERPLANETARY_ROCKET) != null;
    }

    public boolean atterHasRocketsOnly() {
        return attacker.activeMapUnits.size() == 1 && atterHasRockets();
    }

    public boolean atterHasTargets() {
        // return defender.activeFleetUnits.size() > 0 || defender.activeDefenseUnits.size() > 0;
        return defender.activeUnits.size() > 0;
    }

    public boolean defenderHasUnits() {
        // return defender.activeFleetUnits.size() > 0 || defender.activeDefenseUnits.size() > 0 || (atterHasRockets() && defenderHasRockets());
        return defender.activeUnits.size() > 0 || (atterHasRockets() && defenderHasRockets());
    }

    public boolean defenderHasRockets() {
        return defender.activeMapUnits.get(Assault.UNIT_INTERCEPTOR_ROCKET) != null;
    }

    public boolean defenderHasTargets() {
        // return attacker.activeFleetUnits.size() > 0 || attacker.activeDefenseUnits.size() > 0;
        return attacker.activeUnits.size() > 0;
    }

    public void sideAttack(boolean isAtterAttack) {
        PartySide atterSide = isAtterAttack ? attacker : defender;
        PartySide defenderSide = !isAtterAttack ? attacker : defender;
        for (Participant p : atterSide.participants) {
            Units atterRockets = p.getAtterRockets();
            if (atterRockets != null && atterRockets.getStartTurnQuantity() > 0) {
                int atterRocketsNumber = atterRockets.getStartTurnQuantity();
                UnitsList antiRocketsList = defenderSide.activeMapUnits.get(Assault.UNIT_INTERCEPTOR_ROCKET);
                if(antiRocketsList != null){
                    for(Units antiRockets : antiRocketsList){
                        int antiRocketsNumber = antiRockets.getStartTurnQuantity();
                        if(antiRocketsNumber > atterRocketsNumber){
                            antiRocketsNumber = atterRocketsNumber;
                        }
                        antiRockets.fireTurnUnits(antiRocketsNumber);
                        double killAtterRocketsNumber = antiRocketsNumber * 0.98;
                        int saveAtterRocketsNumber = atterRocketsNumber;
                        atterRocketsNumber -= (int)killAtterRocketsNumber;
                        double killAtterRocketsNumberFrac = killAtterRocketsNumber - (int)killAtterRocketsNumber;
                        if(atterRocketsNumber > 0 && Assault.randDouble(0, 1) <= killAtterRocketsNumberFrac){
                           atterRocketsNumber--; 
                        }
                        int firedAtterRockets = saveAtterRocketsNumber - atterRocketsNumber;
                        double rocketsShellDestroyed = atterRockets.getShell()* antiRocketsNumber;
                        // atterRocketsNumber -= antiRocketsNumber;
                        if (!isAtterAttack) {
                            Assault.attackerShots += antiRocketsNumber;
                            Assault.attackerPower += rocketsShellDestroyed;
                            Assault.defenderShellDestroyed += rocketsShellDestroyed;
                            
                            Assault.defenderShots += firedAtterRockets;
                            Assault.defenderPower += firedAtterRockets * atterRockets.getAttack();
                        } else {
                            Assault.defenderShots += antiRocketsNumber;
                            Assault.defenderPower += rocketsShellDestroyed;
                            Assault.attackerShellDestroyed += rocketsShellDestroyed;
                            
                            Assault.attackerShots += firedAtterRockets;
                            Assault.attackerPower += firedAtterRockets * atterRockets.getAttack();
                        }                        
                        // atterRocketsNumber -= antiRocketsNumber;
                    }
                }
                // int killAtterRocketsNumber = p.getAtterRockets().getTurnQuantity() - atterRocketsNumber;
                // p.getAtterRockets().fireTurnUnits(killAtterRocketsNumber);
                
                if(atterRocketsNumber > 0){
                    UnitsList primaryTargetList = defenderSide.activeMapUnits.get(p.getPrimaryTargetUnitid());
                    if(primaryTargetList != null){
                        for(Units primaryTarget : primaryTargetList){
                            
                        }
                    }
                }
                
                if(atterRocketsNumber > 0){
                    unitsAttack(atterRockets, atterRocketsNumber);
                    /* for(Units units: defenderSide.activeDefenseUnits){
                        unitsAttack(units, units.getStartBattleQuantity());
                    } */
                }

                atterRockets.fireTurnUnits(atterRockets.getStartTurnQuantity());
                p.setAtterRockets(null);
            }
        }
        for(Units units: atterSide.turnAtterUnits){
            unitsAttack(units, units.getStartTurnQuantity());
        }
        /* while (atterSide.turnAtterUnits.weight > 0) {
            Units units = atterSide.getRandomAtterUnits();
            int turnAtterQuantity = units.getTurnAtterQuantity();
            int attackQuantity = Math.max(1, turnAtterQuantity / 1000);
            unitsAttack(units, attackQuantity);
            units.decTurnAtterQuantity(attackQuantity);
            atterSide.turnAtterUnits.weight -= units.getWeight() * attackQuantity;
        } */
    }

    public void unitsAttack(Units units, int quantity) {
        /*
         Assault.updateAssault("[unitsAttack] unitid: "+units.getUnitid()
         + ", quant: "+quantity);
         */
        double attack = units.getAttack();
        if (attack < 1 || quantity < 1) {
            return;
        }

        Participant participant = units.getParticipant();
        // double attackMissFactor = participant.getAttackMissFactor();

        boolean isAttacker = participant.isAttacker();
        PartySide defenderSide = isAttacker ? defender : attacker;

        int unitid = units.getUnitid();
        
        UnitsList activeUnitsList = defenderSide.activeUnits;
        if(unitid == Assault.UNIT_INTERPLANETARY_ROCKET){
            activeUnitsList = defenderSide.activeDefenseUnits;
        }

        UnitsList primaryActiveUnitsList = null;
        int primaryTargetUnitid = participant.getPrimaryTargetUnitid();
        if(primaryTargetUnitid > 0){
            UnitsList primaryTargetUnitsList = defenderSide.activeMapUnits.get(primaryTargetUnitid);
            if(primaryTargetUnitsList != null && primaryTargetUnitsList.size() > 0 && primaryTargetUnitsList.get(0).getUnitid() != primaryTargetUnitid){
                primaryActiveUnitsList = new UnitsList();
                for(Units primaryTargetUnits: primaryTargetUnitsList){
                    primaryActiveUnitsList.add(primaryTargetUnits);
                }
                for(Units activeUnits: activeUnitsList){
                    primaryActiveUnitsList.add(activeUnits);
                }
                activeUnitsList = primaryActiveUnitsList;
            }
        }
        
        int remainQuantity = quantity;
        double defenderActiveUnitsWeight = activeUnitsList.weight;
        for(Units targetUnits: activeUnitsList){
            if(remainQuantity < 1){
                break;
            }
            int attackQuantity = (int)Math.round(quantity * targetUnits.getStartTurnWeight() / defenderActiveUnitsWeight);
            attackQuantity = Assault.clampVal(attackQuantity, 1, remainQuantity);
            remainQuantity -= attackQuantity;
            remainQuantity += units.processAttack(targetUnits, attackQuantity);
        }
        
        if(primaryActiveUnitsList != null){
            primaryActiveUnitsList.clear();
        }
    }

    public void finishTurn() {
        /*
        PartySide horizSide = defender;
        PartySide vertSide = attacker;

        boolean isHorizAttacker = horizSide == attacker;
        boolean isVertAttacker = !isHorizAttacker;

        int horizUnitsNumber = 0;
        // int vertUnitsNumber = 0;
        boolean summaryFound = false;
        boolean nothingFound = false;
        for (StatUnit u : Assault.statUnits) {
            u.horizUnitsNumber = 0;
            u.vertUnitsNumber = 0;
            if (u.unitid == Assault.UNIT_NOTHING) {
                // nothingFound = true;
                u.horizExists = true;
                u.vertExists = true;
            } else if (u.unitid == Assault.UNIT_SUMMARY) {
                // summaryFound = true;
                u.horizExists = true;
                u.vertExists = true;
            } else {
                UnitsList list = horizSide.activeUnitsMap.get(u.unitid);
                if (list != null) {
                    for (Units units : list) {
                        u.horizUnitsNumber += units.getQuantity();
                    }
                }
                u.horizExists = u.horizUnitsNumber > 0;

                list = vertSide.activeUnitsMap.get(u.unitid);
                if (list != null) {
                    for (Units units : list) {
                        u.vertUnitsNumber += units.getQuantity();
                    }
                }
                u.vertExists = u.vertUnitsNumber > 0;
            }

            horizUnitsNumber += u.horizExists ? 1 : 0;
            // vertUnitsNumber += u.vertExists ? 1 : 0;
        }

        Assault.fireStatBuf.setLength(0);

        if (true) {
            double attackerDamage = 0;
            double defenderDamage = 0;

            Assault.fireStatBuf.append("<br />\n");
            Assault.fireStatBuf.append("<table class='atable'>");

            Assault.fireStatBuf.append("<tr>");
            Assault.fireStatBuf.append("<th>&nbsp;</th>");
            for (StatUnit u : Assault.statUnits) {
                if ((u.horizExists || u.vertExists) && u.unitid != Assault.UNIT_NOTHING) {
                    if (u.unitid != Assault.UNIT_SUMMARY) {
                        Assault.fireStatBuf.append("<th colspan='3'>{lang}" + u.name + "{/lang}</th>");

                        FireStat stat = Assault.getFireStat(true, u.unitid, Assault.UNIT_SUMMARY);
                        attackerDamage += stat != null ? stat.damage : 0;

                        stat = Assault.getFireStat(false, u.unitid, Assault.UNIT_SUMMARY);
                        defenderDamage += stat != null ? stat.damage : 0;
                    } else {
                        Assault.fireStatBuf.append("<th colspan='2'>{lang}" + u.name + "{/lang}</th>");
                    }
                }
            }
            Assault.fireStatBuf.append("</tr>");

            Assault.fireStatBuf.append("<tr>"); // class='bmat_row bmat_row_atter'>");
            Assault.fireStatBuf.append("<th>{lang}ATTACKER{/lang}</th>");

            Assault.fireStatRowBuf.setLength(0);
            Assault.fireStatRowBuf.append("<tr>"); // class='odd bmat_row bmat_row_defender'>");
            Assault.fireStatRowBuf.append("<th>{lang}DEFENDER{/lang}</th>");
            for (StatUnit u : Assault.statUnits) {
                if ((u.horizExists || u.vertExists) && u.unitid != Assault.UNIT_NOTHING) {
                    String tip = "";
                    for (int i = 0; i < 2; i++) {
                        double sumDamage = i > 0 ? defenderDamage : attackerDamage;
                        String td_class = "bmat_col bmat_col_unit_" + u.unitid;
                        StringBuffer buf = i > 0 ? Assault.fireStatRowBuf : Assault.fireStatBuf;
                        FireStat stat = Assault.getFireStat(i == 0, u.unitid, Assault.UNIT_SUMMARY);
                        if (stat != null && sumDamage > 0) {
                            if (u.unitid != Assault.UNIT_SUMMARY) {
                                tip = i > 0 ? "BMAT_DEFENDER_UNIT_SHOTS" : "BMAT_ATTER_UNIT_SHOTS";
                            } else {
                                tip = i > 0 ? "BMAT_DEFENDER_ALL_SHOTS" : "BMAT_ATTER_ALL_SHOTS";
                            }
                            buf.append("<td class='" + td_class + "' style='text-align:right' title='{lang}" + tip + "{/lang}'>");
                            buf.append(Assault.decFormatter.format(stat.hit));
                            buf.append("</td>");

                            if (u.unitid != Assault.UNIT_SUMMARY) {
                                tip = i > 0 ? "BMAT_DEFENDER_UNIT_DAMAGE" : "BMAT_ATTER_UNIT_DAMAGE";
                            } else {
                                tip = i > 0 ? "BMAT_DEFENDER_ALL_DAMAGE" : "BMAT_ATTER_ALL_DAMAGE";
                            }
                            buf.append("<td class='" + td_class + " rep_quantity_diff' style='text-align:center' title='{lang}" + tip + "{/lang}'>");
                            buf.append(Assault.decFormatter.format(stat.damage));
                            buf.append("</td>");

                            if (u.unitid != Assault.UNIT_SUMMARY) {
                                tip = i > 0 ? "BMAT_DEFENDER_UNIT_DAMAGE_PERCENT" : "BMAT_ATTER_UNIT_DAMAGE_PERCENT";
                                buf.append("<td class='" + td_class + "' style='text-align:left' title='{lang}" + tip + "{/lang}'>");
                                buf.append(Math.round(stat.damage * 100.0 / sumDamage));
                                buf.append("%");
                                buf.append("</td>");
                            }
                        } else {
                            buf.append("<td colspan='" + (u.unitid != Assault.UNIT_SUMMARY ? "3" : "2") + "' class='" + td_class + "' style='text-align:center'>-</td>");
                        }
                    }
                }
            }
            Assault.fireStatRowBuf.append("</tr>");
            Assault.fireStatBuf.append("</tr>");
            Assault.fireStatBuf.append(Assault.fireStatRowBuf);
            Assault.fireStatBuf.append("</table>");
        }

        Assault.fireStatBuf.append("<br />\n");
        Assault.fireStatBuf.append("<table class='atable battle_matrix battle_matrix_turn_" + Assault.battleTurnsNumber + "'>");

        Assault.fireStatBuf.append("<tr>");
        Assault.fireStatBuf.append("<th>{lang}" + (vertSide == attacker ? "ATTACKER" : "DEFENDER") + "{/lang}</th>");
        Assault.fireStatBuf.append("<td colspan='" + (horizUnitsNumber * 3 - (summaryFound ? 1 : 0) - (nothingFound ? 1 : 0)) + "'>&nbsp</td>");
        Assault.fireStatBuf.append("</tr>");

        for (StatUnit vertUnit : Assault.statUnits) {
            if (!vertUnit.vertExists) {
                continue;
            }

            Assault.fireStatBuf.append("<tr class='bmat_row bmat_row_" + (isVertAttacker ? "atter" : "defender") + " bmat_row_unit_" + vertUnit.idName() + "'>");
            Assault.fireStatBuf.append("<th rowspan='2'" + (vertUnit.unitid == Assault.UNIT_NOTHING ? " title='{lang}UNIT_NOTHING_INFO{/lang}'" : "") + ">");
            Assault.fireStatBuf.append("{lang}" + vertUnit.name + "{/lang}");
            Assault.fireStatBuf.append(vertUnit.vertUnitsNumber > 0 ? " (" + vertUnit.vertUnitsNumber + ")" : "");
            Assault.fireStatBuf.append("</th>");

            // Assault.fireStatBuf.append("<th>&nbsp</th>");
            Assault.fireStatRowBuf.setLength(0);
            for (StatUnit horizUnit : Assault.statUnits) {
                if (!horizUnit.horizExists) {
                    continue;
                }
                FireStat stat1 = Assault.getFireStat(isVertAttacker, vertUnit.unitid, horizUnit.unitid);
                FireStat stat2 = Assault.getFireStat(isHorizAttacker, horizUnit.unitid, vertUnit.unitid);

                for (int i = 0; i < 2; i++) {
                    String td_class = "bmat_col bmat_col_unit_" + horizUnit.unitid;
                    StringBuffer buf = i > 0 ? Assault.fireStatRowBuf : Assault.fireStatBuf;
                    FireStat stat = i > 0 ? stat2 : stat1;
                    String tip = "";
                    String tip_prefix = (i == 0) == isVertAttacker ? "BMAT_ATTER" : "BMAT_DEFENDER";
                    // String tip_aim = i > 0 ? horizUnit.idName() + "_" + vertUnit.idName() : vertUnit.idName() + "_" + horizUnit.idName();
                    String tip_aim = i > 0 ? "(" + horizUnit.langName() + ")(" + vertUnit.langName() + ")" : "(" + vertUnit.langName() + ")(" + horizUnit.langName() + ")";

                    if (stat != null && stat.hit > 0) {
                        tip = "{embedded2[" + tip_prefix + "_HIT]}" + tip_aim + "{/embedded2}";
                        buf.append("<td class='" + td_class + "' style='text-align:right' title='" + tip + "'>");
                        buf.append(Assault.decFormatter.format(stat.hit));
                        buf.append("</td>");

                        if (horizUnit.unitid != Assault.UNIT_SUMMARY || !summaryFound) {
                            tip = "{embedded2[" + tip_prefix + "_PERCENT]}" + tip_aim + "{/embedded2}";
                            buf.append("<td class='" + td_class + "' style='text-align:center' title='" + tip + "'>");
                            FireStat sumStat = i > 0
                                    ? Assault.getFireStat(isHorizAttacker, horizUnit.unitid, Assault.UNIT_SUMMARY)
                                    : Assault.getFireStat(isVertAttacker, vertUnit.unitid, Assault.UNIT_SUMMARY);
                            if (sumStat != null && sumStat.hit > 0) {
                                buf.append(Math.round(stat.hit * 100.0f / sumStat.hit));
                                buf.append("%");
                            } else {
                                buf.append("0%");
                            }
                            buf.append("</td>");
                        }

                        if (horizUnit.unitid != Assault.UNIT_NOTHING || !nothingFound) {
                            tip = "{embedded2[" + tip_prefix + "_DAMAGE]}" + tip_aim + "{/embedded2}";
                            buf.append("<td class='" + td_class + " rep_quantity_diff' style='text-align:" + (i == 0 ? "left" : "right") + "' title='" + tip + "'>");
                            // buf.append("<span class='rep_quantity_diff'>");
                            buf.append(Assault.decFormatter.format(stat.damage));
                            // buf.append("</span>");
                            buf.append("</td>");
                        }
                    } else {
                        int cols = (horizUnit.unitid != Assault.UNIT_SUMMARY || !summaryFound)
                                && (horizUnit.unitid != Assault.UNIT_NOTHING || !nothingFound) ? 3 : 2;
                        buf.append("<td class='" + td_class + "' colspan='" + cols + "'>-</td>");
                    }
                }
            }
            Assault.fireStatBuf.append("</tr>");

            Assault.fireStatBuf.append("<tr class='odd bmat_row bmat_row_" + (isVertAttacker ? "defender" : "atter") + " bmat_row_unit_" + vertUnit.idName() + "'>");
            Assault.fireStatBuf.append(Assault.fireStatRowBuf);
            Assault.fireStatBuf.append("</tr>");
        }

        Assault.fireStatBuf.append("<tr>");
        // Assault.fireStatBuf.append("<th>&nbsp</th>");
        Assault.fireStatBuf.append("<th align='right'>{lang}" + (horizSide == attacker ? "ATTACKER" : "DEFENDER") + "{/lang}</th>");
        for (StatUnit horizUnit : Assault.statUnits) {
            if (horizUnit.horizExists) {
                int cols = (horizUnit.unitid != Assault.UNIT_SUMMARY || !summaryFound) && (horizUnit.unitid != Assault.UNIT_NOTHING || !nothingFound) ? 3 : 2;
                Assault.fireStatBuf.append("<th colspan='" + cols + "'" + (horizUnit.unitid == Assault.UNIT_NOTHING ? " title='{lang}UNIT_NOTHING_INFO{/lang}'" : "") + ">");
                Assault.fireStatBuf.append("{lang}" + horizUnit.name + "{/lang}");
                Assault.fireStatBuf.append(horizUnit.horizUnitsNumber > 0 ? " (" + horizUnit.horizUnitsNumber + ")" : "");
                Assault.fireStatBuf.append("</th>");
            }
        }
        Assault.fireStatBuf.append("</tr>");

        Assault.fireStatBuf.append("</table>");

        Assault.resetFireStats();
        */

        attacker.finishTurn();
        defender.finishTurn();
    }

    public void removeAllRockets() {
        attacker.removeAllRockets();
        defender.removeAllRockets();
    }

    public void removeFleet() {
        attacker.removeFleet();
        defender.removeFleet();
    }

    public void setupArtefactBonus() {
        attacker.setupArtefactBonus();
        defender.setupArtefactBonus();
    }

    public void updateActiveUnits() {
        attacker.updateActiveUnits();
        defender.updateActiveUnits();
    }

    public void finish() {
        Assault.debrisMetal = 0;
        Assault.debrisSilicon = 0;
        Assault.atterLostMetal = 0;
        Assault.atterLostSilicon = 0;
        Assault.atterLostHydrogen = 0;
        Assault.defenderLostMetal = 0;
        Assault.defenderLostSilicon = 0;
        Assault.defenderLostHydrogen = 0;

        Assault.haulMetal = 0;
        Assault.haulSilicon = 0;
        Assault.haulHydrogen = 0;

        attacker.calculateLost();
        defender.calculateLost();

        if (Assault.assaultResult == 1 && !Assault.isRocketAttack) {
            List<Participant> nextList = new ArrayList<Participant>();
            List<Participant> participants = new ArrayList<Participant>();
            for (Participant p : attacker.participants) {
                if (p.addHaul(attacker.participants.size())) {
                    participants.add(p);
                }
            }
            while (participants.size() > 0) {
                nextList.clear();
                for (Participant p : participants) {
                    if (p.addHaul(participants.size())) {
                        nextList.add(p);
                    }
                }
                List<Participant> temp = participants;
                participants = nextList;
                nextList = temp;
            }
        }

        attacker.finish();
        defender.finish();
    }
}
