/**
 * NetAssault Oxsar http://netassault.ru, http://oxsar.ru
 * Copyright (c) 2009-2010 UnitPoint <support@unitpoint.ru>
 */

package assault;
import java.util.*;

public class Party
{
	public class UnitsList extends ArrayList<Units>
	{
		private static final long serialVersionUID = -2075289362305531763L;
		
		public long weight = 0;
		
		public void clear()
		{
			weight = 0;
			super.clear();
		}
		
		public boolean add(Units units)
		{
			weight += units.getFullWeight();
			return super.add(units);
		}
	}
	
	public class PartySide
	{
		public List<Participant> participants = new ArrayList<Participant>();
		
		public Map<Integer, UnitsList> activeUnitsMap = new HashMap<Integer, UnitsList>();
		public UnitsList activeFleetUnits = new UnitsList();
		public UnitsList activeDefenseUnits = new UnitsList();
		public UnitsList turnAtterUnits = new UnitsList();
		
		public void addMapUnits(Units units)
		{
			UnitsList list = activeUnitsMap.get(units.getUnitid());
			if(list == null)
			{
				list = new UnitsList();
				activeUnitsMap.put(units.getUnitid(), list);
			}	
			list.add(units);
		}
		
		private Units getRandomAtterUnits()
		{
			UnitsList unitsList = turnAtterUnits;
			if(unitsList.size() > 0)
			{
				int i = 0;
				long num = Assault.randExclude(unitsList.weight);
				for(; i < unitsList.size()-1; i++)
				{
					Units units = unitsList.get(i);
					long unitsWeight = units.getTurnAtterWeight();
					if(num < unitsWeight)
					{
						return units;
					}
					num -= unitsWeight;
				}
				return unitsList.get(i);
			}
			return null;
		}
		
		private Units getRandomTargetUnits(UnitsList unitsList)
		{
			int count = unitsList.size();
			if(count == 1)
			{
				return unitsList.get(0);
			}
			if(count > 1)
			{
				int i = 0;
				long num = Assault.randExclude(unitsList.weight);
				for(; i < count-1; i++)
				{
					Units units = unitsList.get(i);
					long unitsWeight = units.getFullWeight();
					if(num < unitsWeight)
					{
						return units;
					}
					num -= unitsWeight;
				}
				return unitsList.get(i);
			}
			return null;
		}
		
		public Units getRandomFleetUnits()
		{
			return getRandomTargetUnits(activeFleetUnits);
		}
		
		public Units getRandomDefenseUnits()
		{
			return getRandomTargetUnits(activeDefenseUnits);
		}

		public Units getPrimaryTargetUnits(int primaryTargetUnitid)
		{
			if(primaryTargetUnitid > 0)
			{
				UnitsList list = activeUnitsMap.get(primaryTargetUnitid);
				if(list != null)
				{
					Units units = getRandomTargetUnits(list);
					if(units.getCurQuantity() > 0)
					{
						return units;
					}
					for(Units iterUnits : list)
					{
						if(iterUnits.getCurQuantity() > 0)
						{
							return iterUnits;
						}
					}
				}
			}
			return null;
		}

		public Units getPrimaryTargetUnitsFor(int attackerUnitid, int primaryTargetUnitid, boolean allowInterceptor)
		{
			Units units = null;
			if(attackerUnitid == Assault.UNIT_INTERPLANETARY_ROCKET && allowInterceptor)
			{
				units = getPrimaryTargetUnits(Assault.UNIT_INTERCEPTOR_ROCKET);
				if(units != null)
				{
					return units;
				}
			}
			if(attackerUnitid == Assault.UNIT_INTERPLANETARY_ROCKET 
					|| activeFleetUnits.size() == 0)
			{
				units = getRandomDefenseUnits();
			}
			else if(activeDefenseUnits.size() == 0)
			{
				units = getRandomFleetUnits();
			}
			else if(Assault.randInt(0, 1) == 0)
			{
				units = getRandomDefenseUnits();
			}
			else
			{
				units = getRandomFleetUnits();
			}
			if(units != null)
			{
				if(units.getUnitid() == primaryTargetUnitid)
				{
					if(units.getCurQuantity() > 0)
					{
						return units;
					}
				}
				else if((units.getUnitid() == Assault.UNIT_SMALL_SHIELD
						|| units.getUnitid() == Assault.UNIT_LARGE_SHIELD)
						&& units.getCurQuantity() > 0)
				{
					return units;
				}
			}
			Units primaryUnits = getPrimaryTargetUnits(primaryTargetUnitid);
			return primaryUnits != null ? primaryUnits : units;
		}
		
		public void removeAllRockets()
		{
			for(Participant p : participants)
			{
				p.removeAllRockets();
			}
		}
		
		public void removeFleet()
		{
			for(Participant p : participants)
			{
				p.removeFleet();
			}
		}
		
		public void setupArtefactBonus()
		{
			for(Participant p : participants)
			{
				p.setupArtefactBonus();
			}
		}
		
		public void updateActiveUnits()
		{
			activeUnitsMap.clear();
			activeFleetUnits.clear();
			activeDefenseUnits.clear();
			turnAtterUnits.clear();

			for(Participant p : participants)
			{
				for(Units units : p.getUnits())
				{
					if(units.getQuantity() > 0)
					{
						addMapUnits(units);
						if(units.getUnitid() != Assault.UNIT_INTERCEPTOR_ROCKET 
								&& units.getUnitid() != Assault.UNIT_INTERPLANETARY_ROCKET)
						{
							if(units.getType() == Assault.UNIT_TYPE_FLEET)
							{
								activeFleetUnits.add(units);
							}
							else
							{
								activeDefenseUnits.add(units);							
							}
							turnAtterUnits.add(units);
						}
					}
				}
			}
		}
		
		public void finishTurn()
		{
			for(Participant participant : participants)
			{
				for(Units units : participant.getUnits())
				{
					units.finishTurn(); // Remove exploded ships
					
					if(participant.isAttacker())
					{
						Assault.attackerShipsDestroyed += units.getFiredDiff() - units.getQuantityDiff();
					}
					else
					{
						Assault.defenderShipsDestroyed += units.getFiredDiff() - units.getQuantityDiff();
					}
				}
			}
			updateActiveUnits();
		}
		
		public void calculateLost()
		{
			for(Participant participant : participants)
			{
				participant.calculateLost();

				Assault.debrisMetal += participant.getDebrisMetal();
				Assault.debrisSilicon += participant.getDebrisSilicon();
				
				if(participant.isAttacker())
				{
					Assault.atterLostMetal += participant.getLostMetal();
					Assault.atterLostSilicon += participant.getLostSilicon();
					Assault.atterLostHydrogen += participant.getLostHydrogen();
				}
				else
				{
					Assault.defenderLostMetal += participant.getLostMetal();
					Assault.defenderLostSilicon += participant.getLostSilicon();
					Assault.defenderLostHydrogen += participant.getLostHydrogen();
				}
			}
		}
		
		public void finish()
		{
			for(Participant participant : participants)
			{
				participant.finish();
			}
		}
		
		public int getLostUnits()
		{
			int lostUnits = 0;
			for(Participant participant : participants)
			{
				lostUnits += participant.getLostUnits();
			}
			return lostUnits;
		}
	}
	
	private PartySide attacker = new PartySide();
	private PartySide defender = new PartySide();
	
	public List<Participant> getAttackers()
	{
		return attacker.participants;
	}
	
	public List<Participant> getDefenders()
	{
		return defender.participants;
	}
	
	public void addParticipant(Participant participant)
	{
		if(participant.isAttacker())
		{
			attacker.participants.add(participant);
		}
		else
		{
			defender.participants.add(participant);
		}
	}
	
	public int getAttersNumber()
	{
		return attacker.participants.size();
	}
	
	public int getDefendersNumber()
	{
		return defender.participants.size();
	}
	
	public int getAtterLostUnits()
	{
		return attacker.getLostUnits();
	}
	
	public int getDefenderLostUnits()
	{
		return defender.getLostUnits();
	}
	
	public Participant getRandomAtter()
	{
		return attacker.participants.get(Assault.randExclude(attacker.participants.size()));
	}
	
	public Participant getRandomDefender()
	{
		return defender.participants.get(Assault.randExclude(defender.participants.size()));
	}
	
	public boolean atterHasUnits()
	{
		return attacker.activeFleetUnits.size() > 0 || attacker.activeDefenseUnits.size() > 0
				|| atterHasRockets();
	}
	
	public boolean atterHasRockets()
	{
		return attacker.activeUnitsMap.get(Assault.UNIT_INTERPLANETARY_ROCKET) != null;
	}
	
	public boolean atterHasRocketsOnly()
	{
		return attacker.activeUnitsMap.size() == 1 && atterHasRockets();
	}
	
	public boolean atterHasTargets()
	{
		return defender.activeFleetUnits.size() > 0 || defender.activeDefenseUnits.size() > 0;
	}
	
	public boolean defenderHasUnits()
	{
		return defender.activeFleetUnits.size() > 0 || defender.activeDefenseUnits.size() > 0 
				|| (atterHasRockets() && defenderHasRockets());
	}
	
	public boolean defenderHasRockets()
	{
		return defender.activeUnitsMap.get(Assault.UNIT_INTERCEPTOR_ROCKET) != null;
	}
	
	public boolean defenderHasTargets()
	{
		return attacker.activeFleetUnits.size() > 0 || attacker.activeDefenseUnits.size() > 0;
	}

	public void sideAttack(boolean isAtterAttack)
	{
		/*if(isAtterAttack)
		{
			if(!atterHasTargets())
			{
				return;
			}
		}
		else
		{
			if(!defenderHasTargets())
			{
				return;
			}
		} */
		PartySide atterSide = isAtterAttack ? attacker : defender;
		for(Participant p : atterSide.participants)
		{
			if(p.getAtterRockets() != null && p.getAtterRockets().getQuantity() > 0)
			{
				unitsAttack(p.getAtterRockets(), p.getAtterRockets().getQuantity());
				// p.getAtterRockets().decTurnAtterQuantity( p.getAtterRockets().getQuantity() );
				
				p.setAtterRockets(null);
			}
		}
		while(atterSide.turnAtterUnits.weight > 0)
		{
			Units units = atterSide.getRandomAtterUnits();
			unitsAttack(units, 1);
			units.decTurnAtterQuantity(1);
			atterSide.turnAtterUnits.weight -= units.getWeight();			
		}
	}
	
	
	public void unitsAttack(Units units, int quantity)
	{
		/*
		Assault.updateAssault("[unitsAttack] unitid: "+units.getUnitid()
				+ ", quant: "+quantity);
		*/
		
		Participant participant = units.getParticipant();
		// double attackMissFactor = participant.getAttackMissFactor();
		int primaryTargetUnitid = participant.getPrimaryTargetUnitid();
		
		boolean isAttacker = participant.isAttacker();		
		PartySide defenderSide = isAttacker ? defender : attacker;
		
		int unitid = units.getUnitid();
		// int quantity = units.getQuantity();
		for(int i = 0; i < quantity; i++)
		{
			Units targetUnits = defenderSide.getPrimaryTargetUnitsFor(unitid, primaryTargetUnitid, true);
			if(targetUnits == null) // all destroyed
			{					
				// Add turn values
				int damage = units.getAttack0() + units.getAttack1() + units.getAttack2();
				Assault.addFireStat(isAttacker, unitid, Assault.UNIT_NOTHING, 0 /*units.getAttack()*/, false);
				if (isAttacker)
				{
					Assault.attackerShots++;
					Assault.attackerPower += damage;
				}
				else
				{
					Assault.defenderShots++;
					Assault.defenderPower += damage;
				}
				if(unitid == Assault.UNIT_INTERPLANETARY_ROCKET)
				{
					units.firedDestroy(1);
				}
				continue;
			}
			if(targetUnits.getUnitid() == Assault.UNIT_INTERCEPTOR_ROCKET)
			{
				if(unitid != Assault.UNIT_INTERPLANETARY_ROCKET)
				{
					// todo: it's error !
					Assault.addFireStat(isAttacker, unitid, Assault.UNIT_NOTHING, 0 /*units.getAttack()*/, false);
					continue;
				}
				Units newTargetUnits = null;
				if(Assault.randInt(1, 100) <= 2)
				{
					newTargetUnits = defenderSide.getPrimaryTargetUnitsFor(unitid, primaryTargetUnitid, false);
				}
				if(newTargetUnits == null)
				{
					Assault.attackerShots++;
					Assault.attackerPower += units.getAttack0() + units.getAttack1() + units.getAttack2();
					Assault.attackerShellDestroyed += units.getShell();
					Assault.defenderShots++;
					Assault.defenderPower += units.getShell();
					Assault.defenderShellDestroyed += targetUnits.getShell();
					targetUnits.firedDestroy(1);
					units.destroy(1);

					Assault.addFireStat(isAttacker, unitid, targetUnits.getUnitid(), units.getShell() /*getAttack()*/, true);
					Assault.addFireStat(!isAttacker, targetUnits.getUnitid(), unitid, units.getShell(), true);
					
					Assault.updateAssault("[unitsAttack] IP_ROCKET is destroyed by INT_ROCKET");
					
					continue;
				}				
				Assault.defenderShots++;
				Assault.defenderPower += units.getShell();
				targetUnits.firedDestroy(1);
				
				// Assault.addFireStat(unitid, targetUnits.getUnitid(), units.getShell(), true);
				Assault.addFireStat(!isAttacker, targetUnits.getUnitid(), Assault.UNIT_NOTHING, 0 /*units.getShell()*/, false);
				
				targetUnits = newTargetUnits;
			}
			
			// boolean isPrimaryTarget = targetUnits.getUnitid() == primaryTargetUnitid;
			if(unitid == Assault.UNIT_INTERPLANETARY_ROCKET)
			{
				int damage = units.getAttack0() + units.getAttack1() + units.getAttack2();

				// Add turn values
				if (isAttacker)
				{
					Assault.attackerShots++;
					Assault.attackerPower += damage;
				}
				else
				{
					Assault.defenderShots++;
					Assault.defenderPower += damage;
				}
				
				Assault.updateAssault("[unitsAttack] IP_ROCKET attack: "+damage);
				
				for(;;)
				{
					// damage -= targetUnits.processDamage(units); // unitid, damage, 1.0, 0); // attackMissFactor);
					damage -= units.processAttack(targetUnits);
					damage = (int) (damage * 0.99);
					if(damage <= 10)
					{
						break;
					}
					
					Units newTargetUnits = defenderSide.getPrimaryTargetUnitsFor(unitid, primaryTargetUnitid, false);
					if(newTargetUnits != null 
							&& newTargetUnits != targetUnits
							&& newTargetUnits.getCurQuantity() > 0
							&& newTargetUnits.getUnitid() != Assault.UNIT_INTERCEPTOR_ROCKET)
					{
						targetUnits = newTargetUnits;
						continue;
					}
					
					if(targetUnits.getCurQuantity() <= 0)
					{
						break;
					}
				}
				units.firedDestroy(1);
			}
			else if(Assault.USE_OGAME_RAPIDFIRE_STYLE)
			{
				int rapidFire = Assault.getRapidFire(units.getUnitid(), targetUnits.getUnitid());
				double rapidChance = rapidFire > 0 ? 100.0 * (rapidFire - 1) / rapidFire : 0;
				int maxShotsNumber = 1 + rapidFire + rapidFire/10;
				for(int j = 0; j < maxShotsNumber; j++)
				{
					int damage = units.getAttack0() + units.getAttack1() + units.getAttack2();
					// Add turn values
					if (isAttacker)
					{
						Assault.attackerShots++;
						Assault.attackerPower += damage;
					}
					else
					{
						Assault.defenderShots++;
						Assault.defenderPower += damage;
					}
				
					// targetUnits.processDamage(units); // unitid, units.getAttack(), attackMissFactor);
					units.processAttack(targetUnits);
					if(rapidFire <= 0 || Assault.randDouble(1, 100) >= rapidChance) // targetUnits.getCurQuantity() <= 0)
					{
						break;
					}
					
					Units newTargetUnits = defenderSide.getPrimaryTargetUnitsFor(unitid, primaryTargetUnitid, false);
					if(newTargetUnits != null 
							&& newTargetUnits != targetUnits
							&& newTargetUnits.getUnitid() != Assault.UNIT_INTERCEPTOR_ROCKET)
					{
						targetUnits = newTargetUnits;
						rapidFire = Assault.getRapidFire(units.getUnitid(), targetUnits.getUnitid());
						rapidChance = rapidFire > 0 ? 100.0 * (rapidFire - 1) / rapidFire : 0;
						maxShotsNumber = Math.max(maxShotsNumber, 1 + rapidFire + rapidFire/10);
					}
				}
			}
			else
			{
				int rapidFire = Assault.getRapidFire(units.getUnitid(), targetUnits.getUnitid());
				// double rapidChance = rapidFire > 0 ? 100.0 * (rapidFire - 1) / rapidFire : 0;
				// int maxShotsNumber = 1 + rapidFire + rapidFire/10;
				// for(; maxShotsNumber > 0; --maxShotsNumber)
				int shotsNumber = Math.max(1, rapidFire);
				for(; shotsNumber > 0; --shotsNumber)
				{
					int damage = units.getAttack0() + units.getAttack1() + units.getAttack2();
					// Add turn values
					if (isAttacker)
					{
						Assault.attackerShots++;
						Assault.attackerPower += damage;
					}
					else
					{
						Assault.defenderShots++;
						Assault.defenderPower += damage;
					}
				
					// targetUnits.processDamage(units); // unitid, units.getAttack(), attackMissFactor);
					units.processAttack(targetUnits);
					/* if(rapidFire <= 0 || Assault.randDouble(0, 100) > rapidChance 
							|| targetUnits.getCurQuantity() <= 0)
					{
						break;
					} */
				}
			}
		}
	}
	
	public void finishTurn()
	{
		PartySide horizSide = defender;
		PartySide vertSide = attacker;
		
		boolean isHorizAttacker = horizSide == attacker;
		boolean isVertAttacker = !isHorizAttacker;
		
		int horizUnitsNumber = 0;
		int vertUnitsNumber = 0;
		boolean summaryFound = false;
		boolean nothingFound = false;
		for(StatUnit u : Assault.statUnits)
		{
			u.horizUnitsNumber = 0;
			u.vertUnitsNumber = 0;
			if(u.unitid == Assault.UNIT_NOTHING)
			{
				// nothingFound = true;
				u.horizExists = true;
				u.vertExists = true;
			}
			else if(u.unitid == Assault.UNIT_SUMMARY)
			{
				// summaryFound = true;
				u.horizExists = true;
				u.vertExists = true;
			}
			else
			{
				UnitsList list = horizSide.activeUnitsMap.get(u.unitid);
				if(list != null)
				{
					for(Units units : list)
					{
						u.horizUnitsNumber += units.getQuantity();
					}
				}
				u.horizExists = u.horizUnitsNumber > 0;

				list = vertSide.activeUnitsMap.get(u.unitid);
				if(list != null)
				{
					for(Units units : list)
					{
						u.vertUnitsNumber += units.getQuantity();
					}
				}
				u.vertExists = u.vertUnitsNumber > 0;
			}
			
			horizUnitsNumber += u.horizExists ? 1 : 0;
			vertUnitsNumber += u.vertExists ? 1 : 0;
		}

		Assault.fireStatBuf.setLength(0);
		
		if(true)
		{
			double attackerDamage = 0;
			double defenderDamage = 0;
			
			Assault.fireStatBuf.append("<br />\n");			
			Assault.fireStatBuf.append("<table class='atable'>");

			Assault.fireStatBuf.append("<tr>");
			Assault.fireStatBuf.append("<th>&nbsp;</th>");
			for(StatUnit u : Assault.statUnits)
			{
				if((u.horizExists || u.vertExists) && u.unitid != Assault.UNIT_NOTHING)
				{
					if(u.unitid != Assault.UNIT_SUMMARY)
					{
						Assault.fireStatBuf.append("<th colspan='3'>{lang}"+u.name+"{/lang}</th>");

						FireStat stat = Assault.getFireStat(true, u.unitid, Assault.UNIT_SUMMARY);
						attackerDamage += stat != null ? stat.damage : 0;
						
						stat = Assault.getFireStat(false, u.unitid, Assault.UNIT_SUMMARY);
						defenderDamage += stat != null ? stat.damage : 0;
					}
					else
					{
						Assault.fireStatBuf.append("<th colspan='2'>{lang}"+u.name+"{/lang}</th>");
					}
				}
			}
			Assault.fireStatBuf.append("</tr>");

			Assault.fireStatBuf.append("<tr>"); // class='bmat_row bmat_row_atter'>");
			Assault.fireStatBuf.append("<th>{lang}ATTACKER{/lang}</th>");

			Assault.fireStatRowBuf.setLength(0);
			Assault.fireStatRowBuf.append("<tr>"); // class='odd bmat_row bmat_row_defender'>");
			Assault.fireStatRowBuf.append("<th>{lang}DEFENDER{/lang}</th>");
			for(StatUnit u : Assault.statUnits)
			{
				if((u.horizExists || u.vertExists) && u.unitid != Assault.UNIT_NOTHING)
				{
					String tip = "";
					for(int i = 0; i < 2; i++)
					{
						double sumDamage = i > 0 ? defenderDamage : attackerDamage;
						String td_class = "bmat_col bmat_col_unit_"+u.unitid;
						StringBuffer buf = i > 0 ? Assault.fireStatRowBuf : Assault.fireStatBuf;
						FireStat stat = Assault.getFireStat(i == 0, u.unitid, Assault.UNIT_SUMMARY);
						if(stat != null && sumDamage > 0)
						{
							if(u.unitid != Assault.UNIT_SUMMARY)
							{
								tip = i > 0 ? "BMAT_DEFENDER_UNIT_SHOTS" : "BMAT_ATTER_UNIT_SHOTS";
							}
							else
							{
								tip = i > 0 ? "BMAT_DEFENDER_ALL_SHOTS" : "BMAT_ATTER_ALL_SHOTS";
							}
							buf.append("<td class='"+td_class+"' style='text-align:right' title='{lang}"+tip+"{/lang}'>");
							buf.append(Assault.decFormatter.format(stat.hit));					
							buf.append("</td>");

							if(u.unitid != Assault.UNIT_SUMMARY)
							{
								tip = i > 0 ? "BMAT_DEFENDER_UNIT_DAMAGE" : "BMAT_ATTER_UNIT_DAMAGE";
							}
							else
							{
								tip = i > 0 ? "BMAT_DEFENDER_ALL_DAMAGE" : "BMAT_ATTER_ALL_DAMAGE";
							}
							buf.append("<td class='"+td_class+" rep_quantity_diff' style='text-align:center' title='{lang}"+tip+"{/lang}'>");
							buf.append(Assault.decFormatter.format(stat.damage));					
							buf.append("</td>");

							if(u.unitid != Assault.UNIT_SUMMARY)
							{
								tip = i > 0 ? "BMAT_DEFENDER_UNIT_DAMAGE_PERCENT" : "BMAT_ATTER_UNIT_DAMAGE_PERCENT";
								buf.append("<td class='"+td_class+"' style='text-align:left' title='{lang}"+tip+"{/lang}'>");
								buf.append(Math.round(stat.damage * 100.0 / sumDamage));
								buf.append("%");
								buf.append("</td>");
							}
						}
						else
						{
							buf.append("<td colspan='"+(u.unitid != Assault.UNIT_SUMMARY ? "3" : "2")+"' class='"+td_class+"' style='text-align:center'>-</td>");
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
		Assault.fireStatBuf.append("<table class='atable battle_matrix battle_matrix_turn_"+Assault.battleTurnsNumber+"'>");
		
		Assault.fireStatBuf.append("<tr>");
		Assault.fireStatBuf.append("<th>{lang}"+(vertSide == attacker ? "ATTACKER" : "DEFENDER") + "{/lang}</th>");
		Assault.fireStatBuf.append("<td colspan='"+(horizUnitsNumber*3 - (summaryFound ? 1 : 0) - (nothingFound ? 1 : 0))+"'>&nbsp</td>");
		Assault.fireStatBuf.append("</tr>");
		
		for(StatUnit vertUnit : Assault.statUnits)
		{
			if(!vertUnit.vertExists)
			{
				continue;
			}
			
			Assault.fireStatBuf.append("<tr class='bmat_row bmat_row_"+(isVertAttacker ? "atter" : "defender")+" bmat_row_unit_"+vertUnit.idName()+"'>");
			Assault.fireStatBuf.append("<th rowspan='2'"+(vertUnit.unitid == Assault.UNIT_NOTHING ? " title='{lang}UNIT_NOTHING_INFO{/lang}'" : "")+">");
			Assault.fireStatBuf.append("{lang}"+vertUnit.name+"{/lang}");
			Assault.fireStatBuf.append(vertUnit.vertUnitsNumber > 0 ? " ("+vertUnit.vertUnitsNumber+")" : "");
			Assault.fireStatBuf.append("</th>");

			// Assault.fireStatBuf.append("<th>&nbsp</th>");
			Assault.fireStatRowBuf.setLength(0);
			for(StatUnit horizUnit : Assault.statUnits)
			{
				if(!horizUnit.horizExists)
				{
					continue;
				}
				FireStat stat1 = Assault.getFireStat(isVertAttacker, vertUnit.unitid, horizUnit.unitid);
				FireStat stat2 = Assault.getFireStat(isHorizAttacker, horizUnit.unitid, vertUnit.unitid);
				
				for(int i = 0; i < 2; i++)
				{
					String td_class = "bmat_col bmat_col_unit_"+horizUnit.unitid;
					StringBuffer buf = i > 0 ? Assault.fireStatRowBuf : Assault.fireStatBuf;
					FireStat stat = i > 0 ? stat2 : stat1;
					String tip = "";
					String tip_prefix = (i == 0) == isVertAttacker ? "BMAT_ATTER" : "BMAT_DEFENDER";
					// String tip_aim = i > 0 ? horizUnit.idName() + "_" + vertUnit.idName() : vertUnit.idName() + "_" + horizUnit.idName();
					String tip_aim = i > 0 ? "("+horizUnit.langName()+")("+vertUnit.langName()+")" : "("+vertUnit.langName()+")("+horizUnit.langName()+")";

					if(stat != null && stat.hit > 0)
					{
						tip = "{embedded2[" + tip_prefix + "_HIT]}" + tip_aim + "{/embedded2}";
						buf.append("<td class='"+td_class+"' style='text-align:right' title='"+tip+"'>");
						buf.append(Assault.decFormatter.format(stat.hit));					
						buf.append("</td>");

						if(horizUnit.unitid != Assault.UNIT_SUMMARY || !summaryFound)
						{
							tip = "{embedded2[" + tip_prefix + "_PERCENT]}" + tip_aim + "{/embedded2}";
							buf.append("<td class='"+td_class+"' style='text-align:center' title='"+tip+"'>");
							FireStat sumStat = i > 0 
								? Assault.getFireStat(isHorizAttacker, horizUnit.unitid, Assault.UNIT_SUMMARY) 
								: Assault.getFireStat(isVertAttacker, vertUnit.unitid, Assault.UNIT_SUMMARY);
							if(sumStat != null && sumStat.hit > 0)
							{
								buf.append(Math.round(stat.hit * 100.0f / sumStat.hit));
								buf.append("%");
							}
							else
							{
								buf.append("0%");
							}
							buf.append("</td>");
						}
						
						if(horizUnit.unitid != Assault.UNIT_NOTHING || !nothingFound)
						{
							tip = "{embedded2[" + tip_prefix + "_DAMAGE]}" + tip_aim + "{/embedded2}";
							buf.append("<td class='"+td_class+" rep_quantity_diff' style='text-align:"+(i == 0 ? "left" : "right")+"' title='"+tip+"'>");
							// buf.append("<span class='rep_quantity_diff'>");
							buf.append(Assault.decFormatter.format(stat.damage));
							// buf.append("</span>");
							buf.append("</td>");
						}
					}
					else
					{
						int cols = (horizUnit.unitid != Assault.UNIT_SUMMARY || !summaryFound) 
							&& (horizUnit.unitid != Assault.UNIT_NOTHING || !nothingFound) ? 3 : 2;
						buf.append("<td class='"+td_class+"' colspan='"+cols+"'>-</td>");
					}
				}
			}
			Assault.fireStatBuf.append("</tr>");

			Assault.fireStatBuf.append("<tr class='odd bmat_row bmat_row_"+(isVertAttacker ? "defender" : "atter")+" bmat_row_unit_"+vertUnit.idName()+"'>");
			Assault.fireStatBuf.append(Assault.fireStatRowBuf);
			Assault.fireStatBuf.append("</tr>");
		}
		
		Assault.fireStatBuf.append("<tr>");
		// Assault.fireStatBuf.append("<th>&nbsp</th>");
		Assault.fireStatBuf.append("<th align='right'>{lang}"+(horizSide == attacker ? "ATTACKER" : "DEFENDER") + "{/lang}</th>");
		for(StatUnit horizUnit : Assault.statUnits)
		{
			if(horizUnit.horizExists)
			{
				int cols = (horizUnit.unitid != Assault.UNIT_SUMMARY || !summaryFound) && (horizUnit.unitid != Assault.UNIT_NOTHING || !nothingFound) ? 3 : 2;
				Assault.fireStatBuf.append("<th colspan='"+cols+"'"+(horizUnit.unitid == Assault.UNIT_NOTHING ? " title='{lang}UNIT_NOTHING_INFO{/lang}'" : "")+">");
				Assault.fireStatBuf.append("{lang}"+horizUnit.name+"{/lang}");
				Assault.fireStatBuf.append(horizUnit.horizUnitsNumber > 0 ? " ("+horizUnit.horizUnitsNumber+")" : "");
				Assault.fireStatBuf.append("</th>");
			}
		}
		Assault.fireStatBuf.append("</tr>");

		Assault.fireStatBuf.append("</table>");
		
		Assault.resetFireStats();

		attacker.finishTurn();
		defender.finishTurn();
	}
	
	public void removeAllRockets()
	{
		attacker.removeAllRockets();
		defender.removeAllRockets();
	}
	
	public void removeFleet()
	{
		attacker.removeFleet();
		defender.removeFleet();
	}
	
	public void setupArtefactBonus()
	{
		attacker.setupArtefactBonus();
		defender.setupArtefactBonus();
	}
	
	public void updateActiveUnits()
	{
		attacker.updateActiveUnits();
		defender.updateActiveUnits();
	}
	
	public void finish()
	{
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
		
		if(Assault.assaultResult == 1 && !Assault.isRocketAttack)
		{
			List<Participant> nextList = new ArrayList<Participant>();
			List<Participant> participants = new ArrayList<Participant>();
			for(Participant p : attacker.participants)
			{
				if(p.addHaul(attacker.participants.size()))
				{
					participants.add(p);
				}
			}
			while(participants.size() > 0)
			{
				nextList.clear();
				for(Participant p : participants)
				{
					if(p.addHaul(participants.size()))
					{
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
