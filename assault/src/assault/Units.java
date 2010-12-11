/**
 * NetAssault Oxsar http://netassault.ru, http://oxsar.ru
 * Copyright (c) 2009-2010 UnitPoint <support@unitpoint.ru>
 */

package assault;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;

public class Units
{
	private Participant participant = null;
	private int unitid = 0;
	private int type = 0; // UNIT_TYPE_FLEET, UNIT_TYPE_DEFENSE
	private String name = "";
	private int baseAttack = 0;
	private int attack0 = 0;
	private int attack1 = 0;
	private int attack2 = 0;
	private int baseShield = 0;
	private int shield0 = 0;
	private int shield1 = 0;
	private int shield2 = 0;
	private int shell = 0;
	private int userid = 0;
	private int capacity = 0;
	// private int debrisMetal = 0;
	// private int debrisSilicon = 0;
	private int basicMetal = 0;
	private int basicSilicon = 0;
	private int basicHydrogen = 0;
	private int front = 0;
	private int weight = 0;
	private int ballisticsLevel = 0;
	private int maskingLevel = 0;
	// private List<Unit> units = new ArrayList<Unit>();
	private int startBattleQuantity = 0;
	private int startBattleDamaged = 0;
	private int startBattleDamagedShellPercent = 100;
	
	private int curQuantity = 0;
	// private int destroyed = 0;
	private List<Unit> curDamagedUnits = new ArrayList<Unit>();
	
	private int quantity = 0;
	private int turnAtterQuantity = 0;
	private int quantityDiff = 0;
	private int firedDiff = 0;
	private int damaged = 0;
	private double damagedShellPercent = 100;

	// private int totalLoss = 0;
	private int totalGrasped = 0;
	private int totalDestroyed = 0;
	private int totalFired = 0;
	private int prevTotalFired = 0;
	
	public int getStartBattleQuantity()
	{
		return startBattleQuantity;
	}	
	public int getStartBattleDamaged()
	{
		return startBattleDamaged;
	}
	public int getStartBattleDamagedShellPercent()
	{
		return startBattleDamagedShellPercent;
	}	
	
	public int getCurQuantity()
	{
		return curQuantity; // units.size();
	}
	
	public int getQuantity()
	{
		return quantity;
	}

	public int getTurnAtterQuantity()
	{
		return turnAtterQuantity;
	}
	public void decTurnAtterQuantity(int value)
	{
		turnAtterQuantity = Math.max(0, turnAtterQuantity - value);
	}
	
	public int getDamaged()
	{
		// return endTurnDamagedShellPercent <= 99 ? damagedUnits.size() : 0;
		return damaged;
	}
	public int getDamagedShellPercent()
	{
		return (int) damagedShellPercent;
	}
	
	public int getFront()
	{
		return front;
	}
	
	public int getWeight()
	{
		return weight;
	}
	
	public long getFullWeight()
	{
		return (long)weight * quantity;
	}
	
	public long getTurnAtterWeight()
	{
		return (long)weight * turnAtterQuantity;
	}
	
	public int getBallisticsLevel()
	{
		return ballisticsLevel;
	}

	public int getMaskingLevel()
	{
		return maskingLevel;
	}
	
	/*
	public int getDamagedShell()
	{
		try
		{
			return Math.round(endTurnDamagedShell * 100 / shell);
		}
		catch(Exception e)
		{
			return 0;
		}
	}
	*/

	/*
	public Unit getRandomUnit()
	{
		return units.size() > 0 ? units.get(Assault.rand(units.size())) : null;
	}
	*/

	public int getFullCapacity()
	{
		return capacity * getQuantity();
	}
	public int getCapacity()
	{
		return capacity;
	}
	public void setCapacity(int capacity)
	{
		this.capacity = capacity;
	}

	/* public int getDebrisMetal()
	{
		double bulkIntoDebris = Assault.getBulkIntoDebris(type);
		return (int) Math.floor(basicMetal * totalDestroyed * bulkIntoDebris);
	}

	public int getDebrisSilicon()
	{
		double bulkIntoDebris = Assault.getBulkIntoDebris(type);
		return (int) Math.floor(basicSilicon * totalDestroyed * bulkIntoDebris);
	} */

	public double getPoints()
	{
		return ((basicMetal + basicSilicon + basicHydrogen) / 1000.0) * 2.0;
	}

	public int getBaseAttack()
	{
		return baseAttack;
	}
	public void setBaseAttack(int value)
	{
		baseAttack = value;
	}
	
	public int getAttack0()
	{
		return attack0;
	}
	public int getAttack1()
	{
		return attack1;
	}
	public int getAttack2()
	{
		return attack2;
	}

	public int getBaseShield()
	{
		return baseShield;
	}
	public void setBaseShield(int value)
	{
		baseShield = value;
	}

	public int getShield0()
	{
		return shield0;
	}
	public int getShield1()
	{
		return shield1;
	}
	public int getShield2()
	{
		return shield2;
	}

	/*
	public void setShield(double shield)
	{
		this.shield = shield;
	}
	*/

	/*
	public double getStructure()
	{
		return structure;
	}

	public void setStructure(double structure)
	{
		this.structure = structure;
	}
	*/

	public int getShell()
	{
		return shell;
	}

	/*
	public void setShell(double shell)
	{
		this.shell = shell;
	}
	*/

	public int getUnitid()
	{
		return unitid;
	}

	public String getName()
	{
		return name;
	}

	public int getQuantityDiff()
	{
		return quantityDiff;
	}
	
	public int getFiredDiff()
	{
		return firedDiff;
	}

	public int getTotalDestroyed()
	{
		return totalDestroyed;
	}

	public int getTotalFiredDestroyed()
	{
		return totalFired;
	}

	/*
	public int getTotalDamaged()
	{
		return totalDamaged;
	}
	*/

	public int getTotalGrasped()
	{
		return totalGrasped;
	}

	public int getBasicMetal()
	{
		return basicMetal;
	}
	public void setBasicMetal(int value)
	{
		basicMetal = value;
	}

	public int getBasicSilicon()
	{
		return basicSilicon;
	}
	public void setBasicSilicon(int value)
	{
		basicSilicon = value;
	}

	public int getBasicHydrogen()
	{
		return basicHydrogen;
	}
	public void setBasicHydrogen(int value)
	{
		basicHydrogen = value;
	}

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}

	public Participant getParticipant()
	{
		return participant;
	}
	/* public void setParticipant(Participant p)
	{
		this.participant = p;
	} */

	public Units(Participant p, int userid, int unitid, String name, 
			int attack1, int attack2, int attack3,
			int shield1, int shield2, int shield3, 
			int shell, int front,
			int ballistics, int masking,
			int quantity, int damaged, int damagedShellPercent)
	{
		if(damagedShellPercent > 99 || damaged <= 0)
		{
			damagedShellPercent = 100;
			damaged = 0;
		}
		
		this.participant = p;
		this.unitid = unitid;
		this.name = name;
		this.userid = userid;
		this.attack0 = attack1;
		this.attack1 = attack2;
		this.attack2 = attack3;
		this.shield0 = shield1;
		this.shield1 = shield2;
		this.shield2 = shield3;
		this.shell = shell;
		this.front = front;
		this.weight = (int) Math.pow(2, front);
		this.ballisticsLevel = ballistics;
		this.maskingLevel = masking;
		this.startBattleQuantity = quantity;
		this.startBattleDamaged = damaged;
		this.startBattleDamagedShellPercent = damagedShellPercent;
		this.curQuantity = quantity;
		this.quantity = quantity;
		this.turnAtterQuantity = quantity;
		this.damaged = damaged;
		this.damagedShellPercent = damagedShellPercent;
		
		/* if(unitid == Assault.UNIT_INTERPLANETARY_ROCKET)
		{
			this.ballisticsLevel += 10;
		} */

		/*
		this.weight = (shell + 250) / 500;
		if(this.weight < 1) this.weight = 1;
		else if(this.weight > 20) this.weight = 20;
		
		switch(unitid)
		{
		case Assault.UNIT_SMALL_SHIELD:
			this.weight = 100;
			break;
			
		case Assault.UNIT_LARGE_SHIELD:
			this.weight = 200;
			break;
		}
		*/
		
		int damagedShell = (int) ((double)shell * damagedShellPercent / 100.0);
		for(int i = 0; i < damaged; i++)
		{
			curDamagedUnits.add(Assault.newUnit(shield1, shield2, shield3, damagedShell));
		}
	}
	
	public void setupArtefactBonus()
	{
		int shellPowerArtefacts;
		int shieldPowerArtefacts;
		int attackPowerArtefacts;
		if(participant.isAttacker())
		{
			shellPowerArtefacts = Assault.atterShellPowerArtefacts;
			shieldPowerArtefacts = Assault.atterShieldPowerArtefacts;
			attackPowerArtefacts = Assault.atterAttackPowerArtefacts;
		}
		else
		{
			shellPowerArtefacts = Assault.defenderShellPowerArtefacts;
			shieldPowerArtefacts = Assault.defenderShieldPowerArtefacts;
			attackPowerArtefacts = Assault.defenderAttackPowerArtefacts;
		}

		if(shellPowerArtefacts != 0)
		{
			shell = (int) Math.round(shell * Math.max(0.1, 1 + 0.1 * shellPowerArtefacts));
		}
		if(shieldPowerArtefacts != 0)
		{
			double bonusPower = Math.max(0.1, 1 + 0.1 * shieldPowerArtefacts);
			baseShield = (int) Math.round(baseShield * bonusPower);
			shield0 = (int) Math.round(shield0 * bonusPower);
			shield1 = (int) Math.round(shield1 * bonusPower);
			shield2 = (int) Math.round(shield2 * bonusPower);
		}
		if(attackPowerArtefacts != 0)
		{
			double bonusPower = Math.max(0.1, 1 + 0.1 * attackPowerArtefacts);
			baseAttack = (int) Math.round(baseAttack * bonusPower);
			attack0 = (int) Math.round(attack0 * bonusPower);
			attack1 = (int) Math.round(attack1 * bonusPower);
			attack2 = (int) Math.round(attack2 * bonusPower);
		}
	}
	
	/*
	public int processDamage(Units attackUnits)
	{
		boolean isAttacker = participant.isAttacker();
		boolean isDefender = !isAttacker;
		
		int returnDamage = 0;
		int attackerUnitid = attackUnits.getUnitid();
		int damage1 = attackUnits.getAttack0();
		int damage2 = attackUnits.getAttack1();
		int damage3 = attackUnits.getAttack2();
		
		int ballistics = attackUnits.getBallisticsLevel();
		int masking = getMaskingLevel();
		if(curQuantity > 0 && (damage1 > 0 || damage2 > 0 || damage3 > 0))
		{
			// int useQuantity = (int) (curQuantity + (quantity - curQuantity) * attackMissFactor);
			int useQuantity = Math.max(curQuantity, (int)Math.ceil(quantity * (1 + (masking - ballistics) * 2 / 10.0)));
			int num = Assault.randExclude(useQuantity); // quantity is turn start number of units
			if(num >= curQuantity)
			{
				Assault.addFireStat(isDefender, attackerUnitid, Assault.UNIT_NOTHING, 0, false);
				return 0;
			}
			
			Unit unit;
			int ignoreDamageShield = shield / 100;
			if(num >= curDamagedUnits.size())
			{
				if(damage <= ignoreDamageShield)
				{
					if (isAttacker)
					{
						Assault.attackerShield += damage;
					}
					else
					{
						Assault.defenderShield += damage;	
					}
					Assault.addFireStat(isDefender, attackerUnitid, unitid, 0, false);
					return damage;
				}
				unit = Assault.newUnit(shield, shield1, shield2, shell);
				curDamagedUnits.add(unit);			
			}
			else
			{
				unit = curDamagedUnits.get(num);
			}
			
			int damageToShell = 0;
			int shieldAbsorb = 0;

			if(damage >= unit.shield) // Shield destroyed?
			{
				shieldAbsorb = unit.shield;
				damageToShell = damage - unit.shield;
				unit.shield = 0;
			}
			else if(damage > ignoreDamageShield) // Shield sustains damage
			{
				shieldAbsorb = damage;
				unit.shield -= damage; // Decrease shield
			}
			else // Damage of lesser than 1% to the shield will be ignored
			{
				shieldAbsorb = damage;
			}

			if (isAttacker)
			{
				Assault.attackerShield += shieldAbsorb;
			}
			else
			{
				Assault.defenderShield += shieldAbsorb;	
			}
			returnDamage += shieldAbsorb;
			
			boolean isDestroyed = false;
			
			// If there's still damage to shell
			if(damageToShell > 0)
			{
				damageToShell = Math.min(damageToShell, unit.shell);
				unit.shell -= damageToShell; // Decrease shell
				
				if (isAttacker)
				{
					Assault.attackerShellDestroyed += damageToShell;	
				}
				else
				{
					Assault.defenderShellDestroyed += damageToShell;
				}
				returnDamage += damageToShell;
				
				// Shell destroyed?
				if(unit.shell <= 0)
				{
					isDestroyed = true;
				}
				else if(unit.shell < shell * 0.7)
				{
					// Explosion chance, if the unit's shell is 30% or
					// higher destroyed

					int explodingChance = (int) Math.round(100 - unit.shell * 100.0 / shell);
					if(explodingChance >= 99 || Assault.randIntRange(1, 100) <= explodingChance)
					{
						// Ships explodes due to perforated shell
						// Ship will be removed at the end of a turn.
						isDestroyed = true;
					}
				}
				if(isDestroyed)
				{
					curDamagedUnits.remove(unit);
					Assault.freeUnit(unit);
					curQuantity--;
					// destroyed++;
				}
			}
			Assault.addFireStat(isDefender, attackerUnitid, unitid, returnDamage, isDestroyed);
		}
		else
		{
			Assault.addFireStat(isDefender, attackerUnitid, Assault.UNIT_NOTHING, 0, false);
		}
		return returnDamage;
	}
	*/
	
	static private int [] processAttackTempDamages = new int[3];
	static private int [] processAttackTempIgnoreDamages = new int[3];
	
	public int processAttack(Units underFireUnits)
	{
		boolean isBattleAttackerUnderFire = participant.isDefender(); 
		// boolean isAttacker = participant.isAttacker();
		// boolean isDefender = !isAttacker;
		
		int returnDamage = 0;
		int attackerUnitid = getUnitid();
		int [] damages = processAttackTempDamages;
		damages[0] = getAttack0();
		damages[1] = getAttack1();
		damages[2] = getAttack2();		
		if(underFireUnits.curQuantity > 0 && (damages[0] > 0 || damages[1] > 0 || damages[2] > 0))
		{
			int ballistics = getBallisticsLevel();
			int masking = getMaskingLevel();
			
			// int useQuantity = (int) (curQuantity + (quantity - curQuantity) * attackMissFactor);
			int virtualQuantity = Math.max(underFireUnits.curQuantity, (int)Math.ceil(underFireUnits.quantity * (1 + (masking - ballistics) * 2 / 10.0)));
			int unitIndex = Assault.randExclude(virtualQuantity); // quantity is turn start number of units
			if(unitIndex >= underFireUnits.curQuantity)
			{
				Assault.addFireStat(participant.isAttacker(), attackerUnitid, Assault.UNIT_NOTHING, 0, false);
				return 0;
			}
			
			int [] ignoreDamages = processAttackTempIgnoreDamages;
			ignoreDamages[0] = underFireUnits.shield0 / 100;
			ignoreDamages[1] = underFireUnits.shield1 / 100;
			ignoreDamages[2] = underFireUnits.shield2 / 100;
			
			Unit underFireUnit;
			if(unitIndex >= underFireUnits.curDamagedUnits.size())
			{
				if(damages[0] <= ignoreDamages[0] && damages[1] <= ignoreDamages[1] && damages[2] <= ignoreDamages[2])
				{
					// returnDamage = 0;
					for(int i = 0; i < 3; i++)
					{
						if(damages[i] <= ignoreDamages[i])
						{
							returnDamage += damages[i];
							if (isBattleAttackerUnderFire)
							{								
								Assault.attackerShield += damages[i];
							}
							else
							{
								Assault.defenderShield += damages[i];	
							}
						}
					}
					Assault.addFireStat(participant.isAttacker(), attackerUnitid, underFireUnits.unitid, 0, false);
					return returnDamage;
				}
				underFireUnit = Assault.newUnit(underFireUnits.shield0, underFireUnits.shield1, underFireUnits.shield2, underFireUnits.shell);
				underFireUnits.curDamagedUnits.add(underFireUnit);			
			}
			else
			{
				underFireUnit = underFireUnits.curDamagedUnits.get(unitIndex);
			}
			
			int shieldAbsorb = 0;
			int shellDamage = 0;

			for(int i = 0; i < 3; i++)
			{
				int damage = damages[i];
				int ignoreDamage = ignoreDamages[i];
				int shield = underFireUnit.getShield(i);
				if(damage >= shield) // Shield destroyed?
				{
					shieldAbsorb += shield;
					shellDamage += damage - shield;
					underFireUnit.setShield(i, 0);
				}
				else if(damage > ignoreDamage) // Shield sustains damage
				{
					shieldAbsorb += damage;
					underFireUnit.setShield(i, shield - damage); // Decrease shield
				}
				else // Damage of lesser than 1% to the shield will be ignored
				{
					shieldAbsorb += damage;
				}
			}

			if (isBattleAttackerUnderFire)
			{
				Assault.attackerShield += shieldAbsorb;
			}
			else
			{
				Assault.defenderShield += shieldAbsorb;	
			}
			returnDamage += shieldAbsorb;
			
			boolean isDestroyed = false;
			
			// If there's still damage to shell
			if(shellDamage > 0)
			{
				shellDamage = Math.min(shellDamage, underFireUnit.shell);
				underFireUnit.shell -= shellDamage; // Decrease shell
				
				if (isBattleAttackerUnderFire)
				{
					Assault.attackerShellDestroyed += shellDamage;	
				}
				else
				{
					Assault.defenderShellDestroyed += shellDamage;
				}
				returnDamage += shellDamage;
				
				// Shell destroyed?
				if(underFireUnit.shell <= 0)
				{
					isDestroyed = true;
				}
				else if(underFireUnit.shell < underFireUnits.shell * 0.7)
				{
					// Explosion chance, if the unit's shell is 30% or
					// higher destroyed

					double explodingChance = Math.round(100 - underFireUnit.shell * 100.0 / underFireUnits.shell);
					if(Assault.randDouble(0.1, 100) <= explodingChance)
					{
						// Ships explodes due to perforated shell
						// Ship will be removed at the end of a turn.
						isDestroyed = true;
					}
				}
				if(isDestroyed)
				{
					underFireUnits.curDamagedUnits.remove(underFireUnit);
					Assault.freeUnit(underFireUnit);
					underFireUnits.curQuantity--;
					// destroyed++;
				}
			}
			Assault.addFireStat(participant.isAttacker(), attackerUnitid, underFireUnits.getUnitid(), returnDamage, isDestroyed);
		}
		else
		{
			Assault.addFireStat(participant.isAttacker(), attackerUnitid, Assault.UNIT_NOTHING, 0 /*damage*/, false);
		}
		return returnDamage;
	}
	
	public void destroy(int destroyQuantity)
	{
		while(curQuantity > 0 && destroyQuantity-- > 0)
		{
			int lastIndex = curDamagedUnits.size()-1;
			if(lastIndex >= 0)
			{
				Unit unit = curDamagedUnits.get(lastIndex);
				curDamagedUnits.remove(lastIndex);
				Assault.freeUnit(unit);
			}
			curQuantity--;
		}
	}
	
	public void firedDestroy(int destroyQuantity)
	{
		int oldQuantity = curQuantity;
		destroy(destroyQuantity);
		totalFired += oldQuantity - curQuantity;
	}
	
	public void addQuantity(int addQuantity, int unitShell)
	{
		if(addQuantity > 0)
		{
			if(unitShell < shell) // * 0.99)
			{
				for(int i = 0; i < addQuantity; i++)
				{
					curDamagedUnits.add(Assault.newUnit(shield0, shield1, shield2, unitShell));
				}
				double oldPercentSum = damagedShellPercent * damaged;
				double addPercentSum = (unitShell * 100.0 / shell) * addQuantity;
				damagedShellPercent = (oldPercentSum + addPercentSum) / (damaged + addQuantity);
				damaged += addQuantity;
			}
			curQuantity += addQuantity;
			quantity += addQuantity;
			quantityDiff += addQuantity;
			turnAtterQuantity += addQuantity;
			// totalLoss = Math.max(0, totalLoss - addQuantity);
		}
	}

	public void addRepairedQuantity(int quantity)
	{
		addQuantity(quantity, shell);		
	}

	public void addGraspedQuantity(int quantity)
	{
		if(quantity > 0)
		{
			int percent;
			if(Assault.randIntRange(0, 10) == 0)
			{
				if(Assault.randIntRange(0, 10) == 0)
				{
					percent = Assault.randIntRange(95, 99);
				}
				else
				{
					percent = Assault.randIntRange(65, 90);
				}
			}
			else
			{
				percent = Assault.randIntRange(15, 45); 
			}
			addQuantity(quantity, percent * shell / 100);		
			totalGrasped += quantity;
		}
	}
	
	public void destroyAfterTurnFinished(int destroyQuantity)
	{
		int oldQuantity = curQuantity;
		destroy(destroyQuantity);
		destroyQuantity = oldQuantity - curQuantity;
		
		quantityDiff -= destroyQuantity;
		recalculateDamaged();
		
		totalDestroyed += destroyQuantity;
		
		quantity = curQuantity;
		turnAtterQuantity = curQuantity;
	}
	
	public void recalculateDamaged()
	{
		damaged = 0;
		damagedShellPercent = 100;
		if(curDamagedUnits.size() > 0)
		{
			// double notDamagedShell = shell * 0.99;
			double damagedShellSum = 0;
			for(Unit unit : curDamagedUnits)
			{
				unit.SetShield(shield0, shield1, shield2);
				if(unit.shell < shell) // notDamagedShell)
				{
					damagedShellSum += unit.shell;
					damaged++;
				}
			}
			if(damaged > 0)
			{
				damagedShellPercent = (damagedShellSum / damaged) * 100.0 / shell;
			}
		}
	}

	public void finishTurn()
	{
		firedDiff = prevTotalFired - totalFired;
		prevTotalFired = totalFired;
		
		quantityDiff = curQuantity - quantity;
		recalculateDamaged();
		
		int destroyed = quantity - curQuantity;		
		totalDestroyed += destroyed;
		
		// double bulkIntoDebris = Assault.getBulkIntoDebris(type);
		// debrisMetal += Math.floor(basicMetal * destroyed * bulkIntoDebris);
		// debrisSilicon += Math.floor(basicSilicon * destroyed * bulkIntoDebris);
		
		quantity = curQuantity;
		turnAtterQuantity = curQuantity;
	}

	public void finish()
	{
		if(!Assault.debugmode)
		{
			String sql = "";
			Statement stmt = Database.createStatement();
			try
			{
				/* if(getTotalGrasped() > 0 && getTotalGrasped() == getCurQuantity())
				{
				}
				else // if(getTotalLoss() > 0 || getTotalDamaged() > 0) */
				{
					if(getStartBattleQuantity() > 0)
					{
						sql = "UPDATE " + Assault.getTablePrefix() + "fleet2assault SET "
							+ " quantity = '" + getQuantity() + "' "
							+ ", damaged = '" + getDamaged() + "' "
							+ ", grasped = '" + totalGrasped + "' "
							+ ", shell_percent = '" + getDamagedShellPercent() + "' "
							+ " WHERE assaultid = '" + Assault.assaultid + "' AND userid = '" + userid + "' "
							+ " AND unitid = '" + unitid + "' AND participantid = '"+participant.getParticipantId()+"'";
						Assault.updateAssault("[sql] "+sql, true);
						stmt.executeUpdate(sql);
					}
					else
					{
						sql = "REPLACE INTO " + Assault.getTablePrefix() + "fleet2assault "
							+ " (assaultid, userid, unitid, participantid, mode, quantity, damaged, shell_percent, grasped, org_quantity, org_damaged, org_shell_percent) "
							+ " values("
							+ "'"+Assault.assaultid+"'"
							+ " , '"+userid+"'"
							+ " , '"+unitid+"'"
							+ " , '"+participant.getParticipantId()+"'"
							+ " , '"+participant.getType()+"'"
							+ " , '"+getQuantity()+"'"
							+ " , '"+getDamaged()+"'"
							+ " , '"+getDamagedShellPercent()+"'"
							+ " , '"+totalGrasped+"'"
							+ " , '"+getStartBattleQuantity()+"'"
							+ " , '"+getStartBattleDamaged()+"'"
							+ " , '"+getStartBattleDamagedShellPercent()+"'"
							+ ")";
						Assault.updateAssault("[sql] "+sql, true);
						stmt.executeUpdate(sql);
					}
				}
			}
			catch(SQLException e)
			{
				// Assault.updateAssault(e.getMessage(), true);
				System.err.println(sql);
				e.printStackTrace();
			}
		}
	}
}
