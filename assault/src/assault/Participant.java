/**
 * NetAssault Oxsar http://netassault.ru, http://oxsar.ru
 * Copyright (c) 2009-2010 UnitPoint <support@unitpoint.ru>
 */

package assault;

import java.sql.*;
import java.util.*;

public class Participant {
	public static final int ATTACKER = 1;
	public static final int DEFENDER = 0;

	private int type;
	private int userid;
	private int participantid = 0;
	private int primaryTargetUnitid = 0;
	private boolean isAliens = false;
	private String username;
	private int attackLevel = 0;
	private int shieldLevel = 0;
	private int shellLevel = 0;
	private int ballisticsLevel = 0;
	private int maskingLevel = 0;
	private int laserLevel = 0;
	private int ionLevel = 0;
	private int plasmaLevel = 0;
	
	private double [] attackFactors = { 1, 0, 0 };
	private double [] shieldFactors = { 1, 0, 0 };
	
	// private int attackAccuracyLevel = 0;
	// private double attackMissFactor = 1;
	private Map<Integer, Units> unitsMap = new HashMap<Integer, Units>();
	private List<Units> unitsList = new ArrayList<Units>();
	private List<Units> artefactList = new ArrayList<Units>();
	private Units atterRockets = null;
	// private Units defenseRockets = null;
	private boolean ismoon = false;
	private int galaxy = 0;
	private int system = 0;
	private int position = 0;

	private double lostPoints = 0;
	private double lostMetal = 0;
	private double lostSilicon = 0;
	private double lostHydrogen = 0;
	private double debrisMetal = 0;
	private double debrisSilicon = 0;
	// private long capacity = 0;
	// private int fleetQuantity;
	private int lostUnits;
	private double capacity = 0;
	private double haulMetal = 0;
	private double haulSilicon = 0;
	private double haulHydrogen = 0;

	// This is important to calculate the right haul
	private double consumption = 0;
	private double preloaded = 0; // prestored resources for this fleet.
	
	private int addAttackLevel = 0;
	private int addShieldLevel = 0;
	private int addShellLevel = 0;
	private int addBallisticsLevel = 0;
	private int addMaskingLevel = 0;
	private int addLaserLevel = 0;
	private int addIonLevel = 0;
	private int addPlasmaLevel = 0;

	public void removeAllRockets() {
		Units units = unitsMap.get(Assault.UNIT_INTERPLANETARY_ROCKET);
		if (units != null) {
			unitsMap.remove(units);
			unitsList.remove(units);
		}
		units = unitsMap.get(Assault.UNIT_INTERCEPTOR_ROCKET);
		if (units != null) {
			unitsMap.remove(units);
			unitsList.remove(units);
		}
		atterRockets = null;
		// defenseRockets = null;
	}

	public void removeFleet() {
		List<Units> newUnitsList = null;
		for (Units units : unitsList) {
			if (units.getType() == Assault.UNIT_TYPE_FLEET) {
				unitsMap.remove(units);
			} else {
				if (newUnitsList == null) {
					newUnitsList = new ArrayList<Units>();
				}
				newUnitsList.add(units);
			}
		}
		if (newUnitsList != null) {
			unitsList.clear();
			unitsList = newUnitsList;
		}
	}

	public List<Units> getUnits() {
		return unitsList;
	}
	
	public Units getUnits(int unitid)
	{
		return unitsMap.get(unitid);
	}

	public List<Units> getArtefacts()
	{
		return artefactList;
	}

	public boolean isAliens() {
		return isAliens;
	}

	public boolean getIsMoon() {
		return ismoon;
	}

	public void setIsMoon(boolean value) {
		this.ismoon = value;
	}

	public int getGalaxy() {
		return galaxy;
	}

	public void setGalaxy(int galaxy) {
		this.galaxy = galaxy;
	}

	public int getSystem() {
		return system;
	}

	public void setSystem(int system) {
		this.system = system;
	}
	
	public void setAddLevels(int addAttackLevel, int addShieldLevel, int addShellLevel, 
			int addBallisticsLevel, int addMaskingLevel, 
			int addLaserLevel, int addIonLevel, int addPlasmaLevel)
	{
		if(this.isAliens)
		{
			this.attackLevel += addAttackLevel;
			this.shieldLevel += addShieldLevel;
			this.shellLevel += addShellLevel;
			this.ballisticsLevel += addBallisticsLevel;
			this.maskingLevel += addMaskingLevel;
			this.laserLevel += addLaserLevel;
			this.ionLevel += addIonLevel;
			this.plasmaLevel += addPlasmaLevel;
		}
		else
		{
			this.addAttackLevel = addAttackLevel;
			this.addShieldLevel = addShieldLevel;
			this.addShellLevel = addShellLevel;
			this.addBallisticsLevel = addBallisticsLevel;
			this.addMaskingLevel = addMaskingLevel;
			this.addLaserLevel = addLaserLevel;
			this.addIonLevel = addIonLevel;
			this.addPlasmaLevel = addPlasmaLevel;
		}
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getUsername() {
		return username;
	}

	public boolean isAttacker() {
		return type == ATTACKER;
	}

	public boolean isDefender() {
		return type == DEFENDER;
	}

	public double getLostMetal() {
		return lostMetal;
	}

	public double getLostSilicon() {
		return lostSilicon;
	}

	public double getLostHydrogen() {
		return lostHydrogen;
	}

	public double getDebrisMetal() {
		return debrisMetal;
	}

	public double getDebrisSilicon() {
		return debrisSilicon;
	}

	public int getParticipantId() {
		return participantid;
	}

	public void setParticipantId(int id) {
		participantid = id;
	}

	public int getPrimaryTargetUnitid() {
		return primaryTargetUnitid;
	}

	public void setPrimaryTargetUnitid(int unitid) {
		primaryTargetUnitid = unitid;
	}

	public int getUserid() {
		return this.userid;
	}

	public int getAttackLevel() {
		return Math.max(0, attackLevel + addAttackLevel);
	}

	public int getAttackAddLevel() {
		return addAttackLevel;
	}

	public int getShieldLevel() {
		return Math.max(0, shieldLevel + addShieldLevel);
	}

	public int getShieldAddLevel() {
		return addShieldLevel;
	}

	public int getShellLevel() {
		return Math.max(0, shellLevel + addShellLevel);
	}

	public int getShellAddLevel() {
		return addShellLevel;
	}

	public int getBallisticsLevel() {
		return Math.max(0, ballisticsLevel + addBallisticsLevel);
	}

	public int getBallisticsAddLevel() {
		return addBallisticsLevel;
	}

	public int getMaskingLevel() {
		return Math.max(0, maskingLevel + addMaskingLevel);
	}

	public int getMaskingAddLevel() {
		return addMaskingLevel;
	}

	public int getLaserLevel() {
		return Math.max(0, laserLevel + addLaserLevel);
	}

	public int getLaserAddLevel() {
		return addLaserLevel;
	}

	public int getIonLevel() {
		return Math.max(0, ionLevel + addIonLevel);
	}

	public int getIonAddLevel() {
		return addIonLevel;
	}

	public int getPlasmaLevel() {
		return Math.max(0, plasmaLevel + addPlasmaLevel);
	}

	public int getPlasmaAddLevel() {
		return addPlasmaLevel;
	}
	
	public double getAttackFactor(int i)
	{
		return attackFactors[i];
	}

	public double getShieldFactor(int i)
	{
		return shieldFactors[i];
	}

	public int getType() {
		return type;
	}

	public double getLostPoints() {
		return lostPoints;
	}

	public Participant(int userid, int type, String username) {
		this.userid = userid;
		this.type = type;
		this.username = username;
		if (userid == 0 || username == "") {
			if (userid == 0) {
				isAliens = true;
				this.username = "{lang}ALIENS{/lang}";
			} else {
				this.username = "{lang}UNKNOWN_USER{/lang}";
			}
		}
		attackLevel = 0;
		shieldLevel = 0;
		shellLevel = 0;
		// attackAccuracyLevel = 0;
		// attackMissFactor = 1;
		ballisticsLevel = 0;
		maskingLevel = 0;
		laserLevel = 0;
		ionLevel = 0;
		plasmaLevel = 0;
		// fleetQuantity = 0;
		lostUnits = 0;

		/**
		 * Get techs.
		 */
		ResultSet rs = null;
		try {
			Statement stmt = Database.createStatement();
			rs = stmt.executeQuery("SELECT r2u.level, r2u.buildingid "
					+ " FROM " + Assault.getTablePrefix()
					+ "research2user r2u " + " WHERE r2u.buildingid in ("
					+ Assault.UNIT_GUN_TECH + ", " + Assault.UNIT_SHIELD_TECH + ", " + Assault.UNIT_SHELL_TECH + ", "					
					+ Assault.UNIT_BALLISTICS_TECH + ", " + Assault.UNIT_MASKING_TECH + ", " 
					+ Assault.UNIT_LASER_TECH + ", " + Assault.UNIT_ION_TECH + ", " + Assault.UNIT_PLASMA_TECH
					+ ") AND r2u.userid = '"
					+ userid + "'");
			while (rs.next()) {
				switch (rs.getInt("buildingid")) {
				case Assault.UNIT_GUN_TECH:
					attackLevel = rs.getInt("level");
					break;
				case Assault.UNIT_SHIELD_TECH:
					shieldLevel = rs.getInt("level");
					break;
				case Assault.UNIT_SHELL_TECH:
					shellLevel = rs.getInt("level");
					break;
				case Assault.UNIT_BALLISTICS_TECH:
					ballisticsLevel = rs.getInt("level");
					break;
				case Assault.UNIT_MASKING_TECH:
					maskingLevel = rs.getInt("level");
					break;
				case Assault.UNIT_LASER_TECH:
					laserLevel = rs.getInt("level");
					break;
				case Assault.UNIT_ION_TECH:
					ionLevel = rs.getInt("level");
					break;
				case Assault.UNIT_PLASMA_TECH:
					plasmaLevel = rs.getInt("level");
					break;
				}
			}
			// attackMissFactor = Math.pow(0.9, attackAccuracyLevel);
			Assault.updateAssault("[new Participant] type: " + type
					+ ", userid: " + userid + ", attack lvl: " + attackLevel
					+ ", shield lvl: " + shieldLevel + ", shell lvl: "
					+ shellLevel + ", ballistics lvl: " + ballisticsLevel
					+ ", masking lvl: " + maskingLevel);
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	public void loadShips() {
		if(Assault.isBattleAdvanced)
		{
			int [] techList = { 
					(int) Math.round(getLaserLevel() * Assault.ADV_TECH_FACTOR[0]), 
					(int) Math.round(getIonLevel() * Assault.ADV_TECH_FACTOR[1]), 
					(int) Math.round(getPlasmaLevel() * Assault.ADV_TECH_FACTOR[2]) };
			int techMax = Math.max(techList[0], Math.max(techList[1], techList[2]));
			int [] techPick = { 0, 0, 0 };
			for(int i = 2; i >= 0; i--)
			{
				if(techList[i] == techMax)
				{
					techPick[i] = techList[i];
					break;
				}
			}
			double techSum = techPick[0] + techPick[1] + techPick[2];
			if(techSum > 0)
			{
				attackFactors[0] = techPick[0] / techSum + techList[0] / 100.0; 
				attackFactors[1] = techPick[1] / techSum + techList[1] / 100.0; 
				attackFactors[2] = techPick[2] / techSum + techList[2] / 100.0; 
			}
			else
			{
				attackFactors[0] = 1.0 / 3 + techList[0] / 100.0; 
				attackFactors[1] = 1.0 / 3 + techList[1] / 100.0; 
				attackFactors[2] = 1.0 / 3 + techList[2] / 100.0; 
			}
			
			for(int i = 0; i < 3; i++)
			{
				shieldFactors[i] = attackFactors[0] * Assault.ADV_TECH_MATRIX[i][0] 
                                     + attackFactors[1] * Assault.ADV_TECH_MATRIX[i][1]
                                     + attackFactors[2] * Assault.ADV_TECH_MATRIX[i][2];					                                                                 	
			}
		}
		ResultSet rs = null;
		try {
			String prefix = Assault.getTablePrefix();
			Statement stmt = Database.createStatement();
			String sql = "SELECT f2a.unitid, f2a.quantity, f2a.damaged, f2a.shell_percent, "
					+ "sd.attack, sd.shield, sd.capicity, "
					+ "b.mode, b.front, b.ballistics, b.masking, b.name, b.basic_metal, b.basic_silicon, b.basic_hydrogen"
					+ " FROM "
					+ prefix
					+ "fleet2assault f2a"
					+ " LEFT JOIN "
					+ prefix
					+ "ship_datasheet sd ON sd.unitid = f2a.unitid"
					+ " LEFT JOIN "
					+ prefix
					+ "construction b ON b.buildingid = f2a.unitid"
					+ " WHERE f2a.participantid = '"
					+ participantid
					+ "' AND f2a.assaultid = '"
					+ Assault.getAssaultid()
					+ "'"
					+ " ORDER BY b.display_order ASC, f2a.unitid ASC, b.buildingid ASC";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				int quantity = rs.getInt("quantity");
				if (quantity <= 0) {
					continue;
				}
				int unitid = rs.getInt("unitid");
				if (unitid == Assault.UNIT_INTERPLANETARY_ROCKET
						&& isDefender()) {
					continue;
				}
				int structure = rs.getInt("basic_metal") + rs.getInt("basic_silicon");
				int unitAttack = (int) Math.round(rs.getInt("attack") * (1 + getAttackLevel() / 10.0));
				int unitShield = (int) Math.round(rs.getInt("shield") * (1 + getShieldLevel() / 10.0));
				int unitShell = (int) Math.round(structure * (1 + getShellLevel() / 10.0) / 10.0);
				int unitFront = rs.getInt("front");
				int unitBallistics = rs.getInt("ballistics") + getBallisticsLevel();
				int unitMasking = rs.getInt("masking") + getMaskingLevel();
				
				int attack0 = (int) Math.ceil(unitAttack * attackFactors[0]); 
				int attack1 = (int) Math.ceil(unitAttack * attackFactors[1]); 
				int attack2 = (int) Math.ceil(unitAttack * attackFactors[2]); 

				int shield0 = (int) Math.ceil(unitShield * shieldFactors[0]); 
				int shield1 = (int) Math.ceil(unitShield * shieldFactors[1]); 
				int shield2 = (int) Math.ceil(unitShield * shieldFactors[2]); 
				
				Units units = new Units(this, userid, unitid, 
						rs.getString("name"), 
						attack0, attack1, attack2, 
						shield0, shield1, shield2,
						unitShell,
						unitFront, unitBallistics, unitMasking, quantity, 
						rs.getInt("damaged"), rs.getInt("shell_percent"));

				units.setType(rs.getInt("mode"));
				units.setCapacity(rs.getInt("capicity"));
				units.setBasicMetal(rs.getInt("basic_metal"));
				units.setBasicSilicon(rs.getInt("basic_silicon"));
				units.setBasicHydrogen(rs.getInt("basic_hydrogen"));
				units.setBaseAttack(unitAttack);
				units.setBaseShield(unitShield);

				unitsMap.put(units.getUnitid(), units);
				if(units.getType() != Assault.UNIT_TYPE_ARTEFACT)
				{
					unitsList.add(units);
	
					switch (units.getUnitid()) {
					case Assault.UNIT_INTERCEPTOR_ROCKET:
						// defenseRockets = units;
						break;
	
					case Assault.UNIT_INTERPLANETARY_ROCKET:
						atterRockets = units;
						break;
					}
	
					Assault.addStatUnit(units.getUnitid(), units.getName());
				}
				else
				{
					boolean isBattleArtefact = false;
					switch (units.getUnitid()) {
					case Assault.UNIT_BATTLE_SHELL_POWER:
						isBattleArtefact = true;
						if(isAttacker())
						{
							Assault.atterShellPowerArtefacts += quantity;
						}
						else
						{
							Assault.defenderShellPowerArtefacts += quantity;
						}
						break;
						
					case Assault.UNIT_BATTLE_SHIELD_POWER:
						isBattleArtefact = true;
						if(isAttacker())
						{
							Assault.atterShieldPowerArtefacts += quantity;
						}
						else
						{
							Assault.defenderShieldPowerArtefacts += quantity;
						}
						break;
						
					case Assault.UNIT_BATTLE_ATTACK_POWER:
						isBattleArtefact = true;
						if(isAttacker())
						{
							Assault.atterAttackPowerArtefacts += quantity;
						}
						else
						{
							Assault.defenderAttackPowerArtefacts += quantity;
						}
						break;
					}
					if(isBattleArtefact)
					{
						artefactList.add(units);
					}
				}

				/*
				 * if(units.getType() == Assault.UNIT_TYPE_FLEET) {
				 * fleetQuantity += units.getCurQuantity(); }
				 */

				Assault.updateAssault(String.format("[loadShips] unitid: %d, attack: %d %d %d, shield: %d %d %d, shell: %d, quant: %d",
						units.getUnitid(), 
						units.getAttack0(), units.getAttack1(), units.getAttack2(),
						units.getShield0(), units.getShield1(), units.getShield2(),
						units.getShell(), units.getCurQuantity()));
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}
	
	public void setupArtefactBonus()
	{		
		for (Units units : unitsList) {
			units.setupArtefactBonus();			
		}
	}
	
	public void addGraspedUnits(Units srcUnits, int quantity) {
		Units units = unitsMap.get(srcUnits.getUnitid());
		if (units == null) {
			units = new Units(this, userid, srcUnits.getUnitid(), srcUnits.getName(), 
					srcUnits.getAttack0(), srcUnits.getAttack1(), srcUnits.getAttack2(),
					srcUnits.getShield0(), srcUnits.getShield1(), srcUnits.getShield2(),
					srcUnits.getShell(), srcUnits.getFront(), 
					srcUnits.getBallisticsLevel(), srcUnits.getMaskingLevel(),
					0, 0, 0);

			units.setType(srcUnits.getType());
			units.setCapacity(srcUnits.getCapacity());
			units.setBasicMetal(srcUnits.getBasicMetal());
			units.setBasicSilicon(srcUnits.getBasicSilicon());
			units.setBasicHydrogen(srcUnits.getBasicHydrogen());

			unitsMap.put(units.getUnitid(), units);
			unitsList.add(units);

			/*
			 * if(newUnit.getMode() == 3) { fleetQuantity +=
			 * newUnit.getQuantity(); }
			 */
		}
		units.addGraspedQuantity(quantity);
	}

	public boolean hasVisUnits() {
		if(artefactList.size() > 0)
		{
			return true;
		}
		for (Units units : unitsList) {
			if (units.getQuantity() > 0 || units.getQuantityDiff() < 0) {
				return true;
			}
		}
		return false;
	}

	public void setConsumption(double consumption) {
		this.consumption = consumption;
	}

	public void setPreloaded(double preloaded) {
		this.preloaded = preloaded;
	}

	/*
	 * public void processAttack() { if(getAtterRockets() != null &&
	 * getAtterRockets().getQuantity() > 0) {
	 * Assault.party.unitsAttack(getAtterRockets()); } for (Units units :
	 * unitsList) { if (units.getQuantity() > 0 && units.getUnitid() !=
	 * Assault.UNIT_INTERCEPTOR_ROCKET && units.getUnitid() !=
	 * Assault.UNIT_INTERPLANETARY_ROCKET) { Assault.party.unitsAttack(units); }
	 * } }
	 */

	public void calculateLost() {
		debrisMetal = 0;
		debrisSilicon = 0;
		lostPoints = 0;
		lostMetal = 0;
		lostSilicon = 0;
		lostHydrogen = 0;
		lostUnits = 0;
		capacity = 0;
		haulMetal = 0;
		haulSilicon = 0;
		haulHydrogen = 0;

		for (Units units : unitsList) {
			/*
			 * Assault.updateAssault("[Participant::finish] BEGIN units" +
			 * ", partid: " + this.getParticipantId() + ", units id: " +
			 * units.getUnitid(), true);
			 */

			if (!Assault.isRocketAttack && units.getTotalDestroyed() > 0
					&& isDefender()
					&& units.getType() == Assault.UNIT_TYPE_DEFENSE
					&& units.getUnitid() != Assault.UNIT_INTERCEPTOR_ROCKET
					&& units.getUnitid() != Assault.UNIT_INTERPLANETARY_ROCKET) {
				int repair = 0;
				double defenseRepairMin = Assault.defenseRepairMin;
				double defenseRepairMax = Assault.defenseRepairMax;
				if (units.getStartBattleQuantity() <= 10) {
					if (units.getStartBattleQuantity() == 1
							&& units.getTotalDestroyed() == 1) {
						repair = Assault.randExclude(3) == 0 ? 1 : 0;
					} else if (units.getStartBattleQuantity() <= 5) {
						repair = Assault.randInt(0, units
								.getTotalDestroyed() - 1);
					} else {
						defenseRepairMin = 0;
						// defenseRepairMax = 0.49;
						repair = (int) Math.floor(units.getTotalDestroyed()
								* Assault.randDouble(defenseRepairMin,
										defenseRepairMax));
					}
				} else {
					repair = (int) Math.round(units.getTotalDestroyed()
							* Assault.randDouble(defenseRepairMin,
									defenseRepairMax));
				}
				/*
				 * if(repair == units.getTotalDestroyed() &&
				 * units.getStartBattleQuantity() <= 10) //
				 * Assault.randExclude(5) != 0) { repair--; }
				 */
				if (repair > 0) {
					units.addRepairedQuantity(repair);
					// lostPoints -= repair * units.getPoints();
					Assault.defenseRepaired.put(units.getName(), repair);
				}
			}
			int curLostUnits = units.getStartBattleQuantity()
					- units.getQuantity();
			if (curLostUnits != 0) {
				// if(units.getType() == Assault.UNIT_TYPE_FLEET)
				{
					this.lostUnits += curLostUnits;
				}
				lostPoints += units.getPoints() * curLostUnits;
				lostMetal += (double) units.getBasicMetal() * curLostUnits;
				lostSilicon += (double) units.getBasicSilicon() * curLostUnits;
				lostHydrogen += (double) units.getBasicHydrogen()
						* curLostUnits;
			}
			if (curLostUnits > 0) {
				double bulkIntoDebris = Assault.getBulkIntoDebris(units
						.getType());
				debrisMetal += (double) units.getBasicMetal()
						* (double) curLostUnits * bulkIntoDebris;
				debrisSilicon += (double) units.getBasicSilicon()
						* (double) curLostUnits * bulkIntoDebris;
				/*
				 * System.out.println("unit: "+units.getName()+", unitid: "+units
				 * .getUnitid()+", lost: "+lostUnits+", dp: "+bulkIntoDebris);
				 * System
				 * .out.println("debrisMetal: "+debrisMetal+", basic: "+units
				 * .getBasicMetal());
				 * System.out.println("debrisSilicon: "+debrisSilicon
				 * +", basic: "+units.getBasicSilicon());
				 */
			}
			capacity += units.getFullCapacity();
		}
	}

	public double getFreeCapacity() {
		return capacity - consumption - preloaded - haulMetal - haulSilicon
				- haulHydrogen;
	}

	public void setAtterRockets(Units atterRockets) {
		this.atterRockets = atterRockets;
	}

	public Units getAtterRockets() {
		return atterRockets;
	}

	public boolean addHaul(int partsNumber) {
		double capacity = getFreeCapacity();
		if (capacity > Assault.MIN_FREE_CAPACITY) {
			double availMetal = Math.ceil(Assault.planetMetal / partsNumber);
			double availSilicon = Math
					.ceil(Assault.planetSilicon / partsNumber);
			double availHydrogen = Math.ceil(Assault.planetHydrogen
					/ partsNumber);

			/*
			 * Assault.updateAssault("[Participant::finish] BEGIN " +
			 * ", partid: " + this.getParticipantId() +
			 * ", availMetal: "+availMetal + ", availSilicon: "+availSilicon +
			 * ", availHydrogen: "+availHydrogen + ", capacity: "+capacity,
			 * true);
			 */

			if ((availMetal + availSilicon + availHydrogen) > capacity) {
				while (capacity > Assault.MIN_FREE_CAPACITY) {
					double res, s, h, cnt;
					if (availMetal > 0) {
						s = availSilicon > 0 ? 1 : 0;
						h = availHydrogen > 0 ? 1 : 0;
						cnt = 1 + s + h;
						res = Math.min(availMetal, Math.ceil(capacity / cnt));
						capacity -= res;
						haulMetal += res;
						availMetal -= res;
					}
					if (availSilicon > 0) {
						h = availHydrogen > 0 ? 1 : 0;
						cnt = 1 + h;
						res = Math.min(availSilicon, Math.ceil(capacity / cnt));
						capacity -= res;
						haulSilicon += res;
						availSilicon -= res;
					}
					if (availHydrogen > 0) {
						res = Math.min(availHydrogen, capacity);
						capacity -= res;
						haulHydrogen += res;
						availHydrogen -= res;
					}

					/*
					 * Assault.updateAssault("[Participant::finish] end step " +
					 * ", partid: " + this.getParticipantId() +
					 * ", availMetal: "+availMetal +
					 * ", availSilicon: "+availSilicon +
					 * ", availHydrogen: "+availHydrogen +
					 * ", capacity: "+capacity, true);
					 */
				}
			} else {
				capacity = 0;
				haulMetal += availMetal;
				haulSilicon += availSilicon;
				haulHydrogen += availHydrogen;
			}
		}
		return capacity > Assault.MIN_FREE_CAPACITY;
	}

	public void finish() {
		for (Units units : unitsList) {
			units.finish();
		}

		/*
		 * Assault.updateAssault("[Participant::finish] END units" +
		 * ", partid: " + this.getParticipantId() , true);
		 */

		Statement stmt = Database.createStatement();
		String prefix = Assault.getTablePrefix();
		String sql = "";

		// Get haul
		if (haulMetal > 0 || haulSilicon > 0 || haulHydrogen > 0) // Assault.assaultResult
																	// == 1 &&
																	// isAtter())
		{
			// haulMetal = Math.floor(haulMetal); haulSilicon =
			// Math.floor(haulSilicon); haulHydrogen = Math.floor(haulHydrogen);
			Assault.haulMetal += haulMetal;
			Assault.haulSilicon += haulSilicon;
			Assault.haulHydrogen += haulHydrogen;

			sql = "UPDATE " + prefix + "assaultparticipant SET "
					+ " haul_metal = '" + Assault.clampDbVal(haulMetal) + "', "
					+ " haul_silicon = '" + Assault.clampDbVal(haulSilicon)
					+ "', " + " haul_hydrogen = '"
					+ Assault.clampDbVal(haulHydrogen) + "' "
					+ " WHERE participantid = '" + participantid
					+ "' AND assaultid = '" + Assault.assaultid
					+ "' AND userid = '" + userid + "'";

			if (!Assault.debugmode) {
				try {
					stmt.executeUpdate(sql);
				} catch (SQLException e) {
					System.err.println(sql);
					e.printStackTrace();
				}
			}
		}

		// Update data
		if (!Assault.debugmode && userid > 0) {
			if (!Assault.useridReported.contains(userid)) {
				try {
					String battleKey = ""
							+ Assault.assaultid
							+ (Assault.isRocketAttack ? "-rocket"
									: (isDefender() ? "" : "-atter"));
					sql = String
							.format(
									"INSERT INTO "
											+ prefix
											+ "message "
											+ " (mode, time, sender, receiver, message, subject, readed) "
											+ " VALUES ('5', '%d', NULL, '%d', '%s', 'ASSAULT_REPORT', '0')",
									Assault.battleTime, userid, battleKey);
					stmt.executeUpdate(sql);

					int userExperience = isAttacker() ? Assault.atterBattleExperience
							: Assault.defenderBattleExperience;
					if (userExperience != 0) {
						sql = String
								.format(
										"INSERT INTO "
												+ prefix
												+ "user_experience "
												+ " (time, isatter, userid, experience, assaultid) "
												+ " VALUES ('%d', '%d', '%d', '%d', '%d')",
										Assault.battleTime, isAttacker() ? 1 : 0,
										userid, userExperience,
										Assault.assaultid);
						stmt.executeUpdate(sql);
					}

					int experiencePoints = isAttacker() ? Assault.atterBattleExperience
							: Assault.defenderBattleExperience;
					sql = "UPDATE "
							+ prefix
							+ "user set "
							+ " e_points = e_points + '" + experiencePoints + "'"
							+ ", be_points = be_points + '" + experiencePoints + "'"
							+ ", battles = battles + 1" + " where userid = '"
							+ userid + "'";
					stmt.executeUpdate(sql);

					Assault.useridReported.add(userid);
				} catch (SQLException e) {
					System.err.println(sql);
					e.printStackTrace();
				}
			}

			try {
				sql = "UPDATE " + prefix + "user SET " 
						+ " points = GREATEST(0, points - '" + lostPoints + "') " 
						+ ", u_points = GREATEST(0, u_points - '" + lostPoints + "') " 
						+ ", u_count = GREATEST(0, u_count - '" + lostUnits + "') " 
						+ " WHERE userid = '" + userid + "'";
				stmt.executeUpdate(sql);
			} catch (SQLException e) {
				System.err.println(sql);
				e.printStackTrace();
			}
		}
	}
}
