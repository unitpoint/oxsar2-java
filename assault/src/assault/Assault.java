/**
 * NetAssault Oxsar http://netassault.ru, http://oxsar.ru
 * Copyright (c) 2009-2010 UnitPoint <support@unitpoint.ru>
 */

package assault;

import java.sql.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;
import java.io.FileOutputStream;

public class Assault
{
	static final int UNIT_NOTHING = 0;
	static final int UNIT_METALMINE = 1;
	static final int UNIT_SILICON_LAB = 2;
	static final int UNIT_HYDROGEN_LAB = 3;
	static final int UNIT_SOLAR_PLANT = 4;
	static final int UNIT_HYDROGEN_PLANT = 5;
	static final int UNIT_ROBOTIC_FACTORY = 6;
	static final int UNIT_NANO_FACTORY = 7;
	static final int UNIT_SHIPYARD = 8;
	static final int UNIT_METAL_STORAGE = 9;
	static final int UNIT_SILICON_STORAGE = 10;
	static final int UNIT_HYDROGEN_STORAGE = 11;
	static final int UNIT_RESEARCH_LAB = 12;
	static final int UNIT_SPYWARE = 13;
	static final int UNIT_COMPUTER_TECH = 14;
	static final int UNIT_GUN_TECH = 15;
	static final int UNIT_SHIELD_TECH = 16;
	static final int UNIT_SHELL_TECH = 17;
	static final int UNIT_ENERGY_TECH = 18;
	static final int UNIT_HYPERSPACE_TECH = 19;
	static final int UNIT_COMBUSTION_ENGINE = 20;
	static final int UNIT_IMPULSE_ENGINE = 21;
	static final int UNIT_HYPERSPACE_ENGINE = 22;
	static final int UNIT_LASER_TECH = 23;
	static final int UNIT_ION_TECH = 24;
	static final int UNIT_PLASMA_TECH = 25;
	static final int UNIT_IGN = 26;
	static final int UNIT_EXPO_TECH = 27;
	static final int UNIT_GRAVI = 28;
	static final int UNIT_SMALL_TRANSPORTER = 29;
	static final int UNIT_LARGE_TRANSPORTER = 30;
	static final int UNIT_LIGHT_FIGHTER = 31;
	static final int UNIT_STRONG_FIGHTER = 32;
	static final int UNIT_CRUISER = 33;
	static final int UNIT_BATTLE_SHIP = 34;
	static final int UNIT_FRIGATE = 35;
	static final int UNIT_COLONY_SHIP = 36;
	static final int UNIT_RECYCLER = 37;
	static final int UNIT_ESPIONAGE_SENSOR = 38;
	static final int UNIT_SOLAR_SATELLITE = 39;
	static final int UNIT_BOMBER = 40;
	static final int UNIT_STAR_DESTROYER = 41;
	static final int UNIT_DEATH_STAR = 42;
	static final int UNIT_ROCKET_LAUNCHER = 43;
	static final int UNIT_LIGHT_LASER = 44;
	static final int UNIT_STRONG_LASER = 45;
	static final int UNIT_ION_GUN = 46;
	static final int UNIT_GAUSS_GUN = 47;
	static final int UNIT_PLASMA_GUN = 48;
	static final int UNIT_SMALL_SHIELD = 49;
	static final int UNIT_LARGE_SHIELD = 50;
	static final int UNIT_INTERCEPTOR_ROCKET = 51;
	static final int UNIT_INTERPLANETARY_ROCKET = 52;
	static final int UNIT_ROCKET_STATION = 53;
	static final int UNIT_MOON_BASE = 54;
	static final int UNIT_STAR_SURVEILLANCE = 55;
	static final int UNIT_STAR_GATE = 56;
	static final int UNIT_MOON_ROBOTIC_FACTORY = 57;
	static final int UNIT_TERRA_FORMER = 58;
	static final int UNIT_REPAIR_FACTORY = 100;
	static final int UNIT_DEFENSE_FACTORY = 101;
	static final int UNIT_LANCER_SHIP = 102; // mode - 3
	static final int UNIT_BALLISTICS_TECH = 103; // mode - 2
	static final int UNIT_MASKING_TECH = 104; // mode - 2
	
	static final int UNIT_BATTLE_SHELL_POWER = 316; // mode - 6
	static final int UNIT_BATTLE_SHIELD_POWER = 317; // mode - 6
	static final int UNIT_BATTLE_ATTACK_POWER = 318; // mode - 6

	static final int UNIT_SUMMARY = 1000;

	static final int UNIT_TYPE_CONSTRUCTION = 1;
	static final int UNIT_TYPE_MOON_CONSTRUCTION = 5;
	static final int UNIT_TYPE_RESEARCH = 2;
	static final int UNIT_TYPE_FLEET = 3;
	static final int UNIT_TYPE_DEFENSE = 4;
	static final int UNIT_TYPE_ARTEFACT = 6;
	
	static final int RES_UPDATE_ATTACKER = 24;
	
	static final boolean USE_OGAME_RAPIDFIRE_STYLE = false;
	
	static final int BATTLE_MAX_TURNS = 6;
	
	static final double [] ADV_TECH_FACTOR = { 1, 1.1, 1.2 };
	static final double [][] ADV_TECH_MATRIX =
								{
									{ 0.75, 2.00, 0.00 },
									{ 0.00, 1.00, 2.00 },
									{ 2.00, 0.00, 1.00 },
								};
	
	public static final int MIN_FREE_CAPACITY = 0;

	public static long startTime;
	public static boolean debugmode;
	public static String dbhost = "localhost";
	public static String dbdatabase = "oxsar";
	public static String username = "root";
	public static String dbpasswd = "";
	public static String tablePrefix = "na_";
	public static int assaultid = 0;
	public static int moonAllowType = 0;
	public static boolean isBattleAdvanced = false;
	public static boolean isBattleSimulation = false;
	public static boolean isRocketAttack = false;
	// public static boolean isRocketAttack = false;
	// public static int rocketAttackPrimaryTarget = 0;
	public static int planetid = 0;
	// public static int[][] rapidfire = new int[100][100];
	public static HashMap<Integer, Integer> rapidFireMap = new HashMap<Integer, Integer>();
	public static HashMap<Integer, FireStat> fireStatMap = new HashMap<Integer, FireStat>();
	public static List<Unit> freeUnits = new ArrayList<Unit>();
	public static List<StatUnit> statUnits = new ArrayList<StatUnit>();
	public static Party party;
	public static int assaultResult;
	
	public static int atterShellPowerArtefacts = 0;
	public static int defenderShellPowerArtefacts = 0;

	public static int atterShieldPowerArtefacts = 0;
	public static int defenderShieldPowerArtefacts = 0;
	
	public static int atterAttackPowerArtefacts = 0;
	public static int defenderAttackPowerArtefacts = 0;
	
	public static double planetMetal = 0;
	public static double planetSilicon = 0;
	public static double planetHydrogen = 0;
	public static int planetDiameter = 0;
	
	public static double startBattleAtterPower = 0;
	public static double startBattleDefenderPower = 0;
	
	public static int atterBattleExperience = 0;
	public static int defenderBattleExperience = 0;
	
	public static boolean targetMoon = false;
	public static int targetBuildingid = 0;
	public static int targetBuildingLevel = 0;
	public static double targetBuildingMetal = 0;
	public static double targetBuildingSilicon = 0;
	public static double targetBuildingHydrogen = 0;
	public static boolean targetDestroyed = false;
	public static boolean attackerFleetsExplode = false;
	public static String targetBuildingName = "";
	
	public static int battleTurnsNumber = 0;
	
	public static int attackerShots;
	public static int defenderShots;
	
	public static double attackerPower;
	public static double defenderPower;
	
	public static double attackerShield;
	public static double defenderShield;
	
	public static double attackerShellDestroyed;
	public static double defenderShellDestroyed;
	
	public static int attackerShipsDestroyed;
	public static int defenderShipsDestroyed;
	
	public static StringBuffer assaultReportBuf = new StringBuffer();
	public static StringBuffer quantityBuf = new StringBuffer();
	public static StringBuffer assaultQuantityBuf = new StringBuffer();
	public static StringBuffer gunsBuf = new StringBuffer();
	public static StringBuffer shieldsBuf = new StringBuffer();
	public static StringBuffer shellsBuf = new StringBuffer();
	public static StringBuffer frontBuf = new StringBuffer();
	public static StringBuffer ballisticsBuf = new StringBuffer();
	public static StringBuffer maskingBuf = new StringBuffer();
	public static StringBuffer fireStatBuf = new StringBuffer();
	public static StringBuffer fireStatRowBuf = new StringBuffer();
	
	public static double atterLostMetal = 0;
	public static double atterLostSilicon = 0;
	public static double atterLostHydrogen = 0;
	
	public static double defenderLostMetal = 0;
	public static double defenderLostSilicon = 0;
	public static double defenderLostHydrogen = 0;
	
	public static double debrisMetal = 0;
	public static double debrisSilicon = 0;
	
	// public final static long USE_MAX_INT_VALUE = 2000000000;
	public final static int MOON_PERCENT_PER_RES = 200000;
	public final static int MOON_GUARANTEED_PERCENT_PER_RES = 10000000;
	public final static int MOON_EXP_START_CHANCE = 5;
	public final static int MOON_START_CHANCE = 5;
	public final static int MOON_MAX_CHANCE = 20;
	public static int moonChance = 0;
	public static boolean moon = false;
	public static boolean ismoon = false;
	public static String key;
	public static String key2;
	public static int battleTime;
	// public static final QuickRandom random = new QuickRandom();
	public static final Random random = new Random();
	public static DecimalFormat decFormatter; // = new DecimalFormat(",###");
	public static DecimalFormat oneDigitFormatter;
	public static Map<String, Integer> defenseRepaired = new HashMap<String, Integer>();
	public static Map<String, Integer> graspedUnits = new HashMap<String, Integer>();
	public static List<Integer> useridReported = new ArrayList<Integer>();
	// public static boolean defenseIntoDebris = true;
	// public static double[] bulkIntoDebris = new double[10];
	public static double defenseRepairMin = 0.6;
	public static double defenseRepairMax = 0.8;
	public static double haulMetal = 0;
	public static double haulSilicon = 0;
	public static double haulHydrogen = 0;
	public static boolean defenderZero = false;

	public static double getBulkIntoDebris(int mode) {
		switch (mode) {
		case UNIT_TYPE_FLEET: // fleet
			return 0.5f;

		case UNIT_TYPE_DEFENSE: // defense
			return 0.01;
		}
		return 0;
	}

	public static void putRapidFire(int src_unit, int target_unit, int value) {
		int key = src_unit * 10000 + target_unit;
		rapidFireMap.put(key, value);
	}

	public static int getRapidFire(int src_unit, int target_unit) {
		int key = src_unit * 10000 + target_unit;
		if (rapidFireMap.containsKey(key)) {
			return rapidFireMap.get(key);
		}
		return 0;
	}
	
	public static void addStatUnit(int unitid, String name)
	{
		for(StatUnit unit : statUnits)
		{
			if(unit.unitid == unitid)
			{
				return;
			}
		}
		statUnits.add(new StatUnit(unitid, name));
	}
	
	public static void addFireStat(boolean isAttacker, int src_unit, int target_unit, int damage, boolean isDestroyed)
	{
		int key = src_unit * UNIT_SUMMARY * 100 + target_unit * 10 + (isAttacker ? 1 : 0);
		FireStat stat = fireStatMap.get(key);
		if(stat == null)
		{
			stat = new FireStat();
			fireStatMap.put(key, stat);
		}
		stat.hit++;
		stat.damage += damage;
		if(isDestroyed)
		{
			stat.destroyed++;
		}
		if(src_unit != UNIT_SUMMARY && target_unit != UNIT_SUMMARY)
		{
			addFireStat(isAttacker, src_unit, UNIT_SUMMARY, damage, isDestroyed);
			addFireStat(isAttacker, UNIT_SUMMARY, UNIT_SUMMARY, damage, isDestroyed);
			addFireStat(isAttacker, UNIT_SUMMARY, target_unit, damage, isDestroyed);
		}
	}
	
	public static FireStat getFireStat(boolean isAtter, int src_unit, int target_unit)
	{
		int key = src_unit * UNIT_SUMMARY * 100 + target_unit * 10 + (isAtter ? 1 : 0);
		return fireStatMap.get(key);
	}
	
	public static void resetFireStats()
	{
		fireStatMap.clear();
	}
	
	private static String parseArg(String s)
	{
		if(s.length() >= 2 && (s.charAt(0) == '\'' || s.charAt(0) == '"')
				&& s.charAt(0) == s.charAt(s.length()-1))
		{
			return s.substring(1, s.length()-1);
		}
		return s;
	}

	/**
	 * @param args
	 * @throws SQLException 
	 * @throws SQLException
	 */
	public static void main(String[] args) throws SQLException
	{
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setGroupingSeparator('.');
		dfs.setDecimalSeparator(',');
		decFormatter = new DecimalFormat(",###", dfs);
		oneDigitFormatter = new DecimalFormat(",###.#", dfs);
		
		startTime = System.currentTimeMillis();

		debugmode = false; // On: Will proceed database updates. Off: Will
		// output report in console and create report file.
		
		if (args.length >= 6) {
			dbhost = parseArg(args[0]);
			username = parseArg(args[1]);
			dbpasswd = parseArg(args[2]);
			dbdatabase = parseArg(args[3]);
			tablePrefix = parseArg(args[4]);
			assaultid = Integer.valueOf(parseArg(args[5]));
			// isBattleAdvanced = true; // args.length >= 7 ? Integer.valueOf(args[6]) != 0 : false;
			debugmode = args.length >= 7 ? parseArg(args[6]) == "debug" : false;
			isBattleSimulation = tablePrefix.indexOf("sim_") >= 0;
		}

		// Assault configuration
		// defenseIntoDebris = false;
		// bulkIntoDebris[3] = 0.3; // Fleet
		// bulkIntoDebris[4] = 0.01; // Defense
		// defenseRepairMin = 0.6;
		// defenseRepairMax = 0.8;

		// Random key to protect the access
		key = generateKey(4);
		key2 = generateKey(4);
		
		addStatUnit(UNIT_SUMMARY, "UNIT_SUMMARY");
		addStatUnit(UNIT_NOTHING, "UNIT_NOTHING");

		party = new Party(); // Initialize party

		/**
		 * Load planet data
		 */
		int userid = 0;
		ResultSet rs = null;
		try {
			updateAssault("[read planets res] before");

			String sql ="SELECT a.time, a.accomplished " 
				+ ", a.target_moon, a.target_buildingid, a.building_level "
				+ ", a.building_metal, a.building_silicon, a.building_hydrogen " 
				+ ", a.advanced_system, a.moon_allow_type "
				+ ", b.name as building_name, p.planetid "
				+ ", p.metal, p.silicon, p.hydrogen, p.ismoon, p.diameter"
				// "g.moonid"
				+ " FROM " + tablePrefix + "assault a"
				+ " LEFT JOIN " + tablePrefix + "planet p ON p.planetid = a.planetid"
				+ " LEFT JOIN " + tablePrefix + "construction b ON b.buildingid = a.target_buildingid"
				// + " LEFT JOIN " + tablePrefix + "galaxy g ON a.planetid = g.planetid"
				// + " LEFT JOIN " + tablePrefix + "galaxy gm ON a.planetid = g.moonid"
				+ " WHERE a.assaultid = '" + assaultid + "' LIMIT 1";
			
			Statement stmt = Database.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next()) {
				if (rs.getInt("accomplished") == 1) {
					System.err.println("Combat is already accomplished.");
					System.exit(1);
				}
				// rocketAttackPrimaryTarget = rs.getInt("rocket_target");
				// isRocketAttack = rocketAttackPrimaryTarget > 0;
				battleTime = rs.getInt("time"); // Assault time
				planetid = rs.getInt("planetid");
				if(!isBattleSimulation){
					planetMetal = rs.getDouble("metal") / 2; // available haul
					planetSilicon = rs.getDouble("silicon") / 2; // available haul
					planetHydrogen = rs.getDouble("hydrogen") / 2; // available haul
				}else{
					planetMetal = 0;
					planetSilicon = 0;
					planetHydrogen = 0;
				}
				planetDiameter = rs.getInt("diameter");
				ismoon = rs.getInt("ismoon") == 1; // || rs.getInt("moonid") > 0) {
				isBattleAdvanced = rs.getInt("advanced_system") == 1;
				moonAllowType = rs.getInt("moon_allow_type");

				targetMoon = ismoon && rs.getInt("target_moon") == 1;
				if(!targetMoon)
				{
					targetBuildingid = rs.getInt("target_buildingid");
					targetBuildingLevel = rs.getInt("building_level");
					targetBuildingMetal = rs.getDouble("building_metal");
					targetBuildingSilicon = rs.getDouble("building_silicon");
					targetBuildingHydrogen = rs.getDouble("building_hydrogen");
					targetBuildingName = rs.getString("building_name");
				}
			}

			updateAssault("[read planets res] after"
					+", target moon: "+(targetMoon ? 1 : 0)
					+", metal: "+planetMetal
					+", silicon: "+planetSilicon
					+", hydrogen: "+planetHydrogen
					+", building name: "+targetBuildingName
					+", building level: "+targetBuildingLevel
					);
		} catch (SQLException e) {
			// updateAssault(e.getMessage(), true);
			System.err.println(e.getMessage());
		}

		/**
		 * Read in users for this assault.
		 */
		try {
			updateAssault("[read participants] before");

			String sql = "SELECT u.userid, u.username, pp.mode, pp.participantid, pp.planetid, " +
				"pp.planetid, pp.preloaded, pp.consumption, pp.target_unitid, " +
				"pp.add_gun_tech, pp.add_shield_tech, pp.add_shell_tech, " +
				"pp.add_ballistics_tech, pp.add_masking_tech, " +
				"pp.add_laser_tech, pp.add_ion_tech, pp.add_plasma_tech, " + 
				"IFNULL(g.galaxy, gm.galaxy) as galaxy, " +
				"IFNULL(g.system, gm.system) as system, " +
				"IFNULL(g.position, gm.position) as position, "
				+ "case when g.galaxy is null then 1 else 0 end as ismoon"
				+ " FROM " + tablePrefix + "assaultparticipant pp"
				+ " LEFT JOIN " + tablePrefix + "user u ON u.userid = pp.userid"
				+ " LEFT JOIN " + tablePrefix + "galaxy g ON g.planetid = pp.planetid"
				+ " LEFT JOIN " + tablePrefix + "galaxy gm ON gm.moonid = pp.planetid"
				+ " WHERE pp.assaultid = '" + assaultid + "'"
				+ " ORDER BY pp.participantid ASC";
			Statement stmt = Database.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				userid = rs.getInt("userid");
				if (userid >= 0)
				{
					int type = rs.getInt("mode");
					if(type == Participant.ATTACKER || type == Participant.DEFENDER)
					{
						int planetid = rs.getInt("pp.planetid");
						Participant participant = new Participant(userid, type, planetid, rs.getString("username"));
						participant.setAddLevels(
								rs.getInt("add_gun_tech"),
								rs.getInt("add_shield_tech"),
								rs.getInt("add_shell_tech"),
								rs.getInt("add_ballistics_tech"),
								rs.getInt("add_masking_tech"),
								rs.getInt("add_laser_tech"),
								rs.getInt("add_ion_tech"),
								rs.getInt("add_plasma_tech")
								);
						participant.setIsMoon(rs.getInt("ismoon") != 0);
						participant.setGalaxy(rs.getInt("galaxy"));
						participant.setSystem(rs.getInt("system"));
						participant.setPosition(rs.getInt("position"));
						participant.setParticipantId(rs.getInt("participantid"));
						participant.setConsumption(rs.getDouble("consumption"));
						participant.setPreloaded(rs.getDouble("preloaded"));
						participant.setPrimaryTargetUnitid(rs.getInt("target_unitid"));
						participant.loadShips();
						
						if(participant.getUnits().size() > 0 
								|| participant.getArtefacts().size() > 0 
								|| (type == Participant.DEFENDER && party.getDefendersNumber() == 0))
						{
							party.addParticipant(participant);
						}
					}
				}
			}
			party.setupArtefactBonus();
			party.updateActiveUnits();
			
			isRocketAttack = party.atterHasRocketsOnly();
			if(isRocketAttack)
			{
				party.removeFleet();
				party.updateActiveUnits();
			}
			else if(!party.atterHasRockets())
			{
				party.removeAllRockets();
				party.updateActiveUnits();
				// key2 = key;
			}

			updateAssault("[read participants] after, is rocket attack: "+ (isRocketAttack ? "1" : "0"));
		} catch (SQLException e) {
			// updateAssault(e.getMessage(), true);
			System.err.println(e.getMessage());
		}

		// addStatUnit(UNIT_SUMMARY, "UNIT_SUMMARY");
		
		assaultReportBuf.setLength(0);
		assaultReportBuf.append("<div class=\"center\">");
		assaultReportBuf
				.append(String
						.format(
								"{embedded[%s]}%ta %td. %tb %tY, %tT{/embedded}<br />\n<br />\n",
								isRocketAttack ? "ROCKET_ATTACK_TIME" : "ASSAULT_TIME",
								Calendar.getInstance(), Calendar
										.getInstance(), Calendar
										.getInstance(), Calendar
										.getInstance(), Calendar
										.getInstance()));
		
		if(targetMoon)
		{
			assaultReportBuf.append("{lang}START_REPORT_TARGET_MOON{/lang}<br />\n<br />\n");
		}
		else if(targetBuildingid != 0)
		{
			assaultReportBuf.append("{lang}START_REPORT_TARGET_BUILDING{/lang}");
			assaultReportBuf.append(" {lang}" + targetBuildingName + "{/lang}");
			if(targetBuildingLevel > 0)
			{
				assaultReportBuf.append(" " + targetBuildingLevel);
			}
			assaultReportBuf.append("<br />\n<br />\n");
		}

		if (!party.defenderHasUnits()) {
			defenderZero = true;
			assaultResult = 1;
		} else {
			/**
			 * Load rapid fire.
			 */
			rs = null;
			try {
				updateAssault("[read rapidfire]");

				Statement stmt = Database.createStatement();
				rs = stmt.executeQuery("SELECT unitid, target, value FROM "
						+ tablePrefix
						+ "rapidfire ORDER BY unitid ASC, target ASC");
				while (rs.next()) {
					// rapidfire[rs.getInt("unitid")][rs.getInt("target")] =
					// rs.getInt("value");
					putRapidFire(rs.getInt("unitid"), rs.getInt("target"),
							rs.getInt("value"));
				}
			} catch (SQLException e) {
				// updateAssault(e.getMessage(), true);
				System.err.println(e.getMessage());
			}
		}

		/**
		 * Here begins the assault calculations.
		 */
		for (int turn = 1; turn <= BATTLE_MAX_TURNS; turn++) {
			updateAssault("[begin turn] " + turn);

			// Flush turn variables
			attackerShots = 0;
			defenderShots = 0;
			attackerPower = 0;
			defenderPower = 0;
			attackerShield = 0;
			defenderShield = 0;
			attackerShellDestroyed = 0;
			defenderShellDestroyed = 0;
			attackerShipsDestroyed = 0;
			defenderShipsDestroyed = 0;

			if (defenderZero)
			{
				if(isRocketAttack && turn == 1)
				{
					// battleTurnsNumber++;
					
					party.sideAttack(true);
					party.finishTurn();
					
					// startBattleAtterPower = atterPower;
					// startBattleDefenderPower = defenderPower;
				}
				break;
			}
			
			battleTurnsNumber++;

			if(!isRocketAttack)
			{
				assaultReportBuf.append("<p><b>{lang}TURN{/lang}: " + turn + "</b></p>\n");
			}

			// Attackers shoot
			for (Participant participant : party.getAttackers())
			{
				if(!participant.hasVisUnits())
				{
					continue;
				}
				updateAssault("[attacker turn] "
						+ participant.getUsername());

				String coords = "";
				if (!participant.isAliens()) {
					coords = "[" + participant.getGalaxy() + ":"
							+ participant.getSystem() + ":"
							+ participant.getPosition() + "]"
							+ (participant.getIsMoon() ? "{lang}LOON_POST{/lang}" : "");
				}
				assaultReportBuf.append("{lang}ATTACKER{/lang} "
						+ "{user["+participant.getUserid()+"]}"+participant.getUsername() + "{/user} " + coords
						+ "<br />\n");
				
				printTechLevels(participant);
				printArtefacts(participant);
				/*
				assaultReportBuf.append(String.format("{lang}GUN_POWER{/lang}: %.0f&#037; ",
						participant.getAttackLevel() * 10.0));						
				assaultReportBuf.append(String.format("{lang}SHIELD_POWER{/lang}: %.0f&#037; ",
						participant.getShieldLevel() * 10.0));				
				assaultReportBuf.append(String.format("{lang}ARMORING{/lang}: %.0f&#037; ",
						participant.getShellLevel() * 10.0));
				assaultReportBuf.append(String.format("{lang}BALLISTICS_POWER{/lang}: %d ",
						participant.getBallisticsLevel()));						
				assaultReportBuf.append(String.format("{lang}MASKING_POWER{/lang}: %d ",
						participant.getMaskingLevel()));						
				assaultReportBuf.append("<br />\n");
				*/
				
				assaultReportBuf.append("<table class=\"atable\"><tr>");
				assaultReportBuf.append("<th>&nbsp;</th>"); // {lang}TYPE{/lang}</th>");

				printParticipant(participant, turn);

				assaultReportBuf.append(quantityBuf);
				assaultReportBuf.append(gunsBuf);
				assaultReportBuf.append(shieldsBuf);
				assaultReportBuf.append(shellsBuf);
				assaultReportBuf.append(frontBuf);
				assaultReportBuf.append(ballisticsBuf);
				assaultReportBuf.append(maskingBuf);
				assaultReportBuf.append(assaultQuantityBuf);

				assaultReportBuf.append("</tr></table><br />\n");

				/* if(party.atterHasTargets())
				{
					participant.processAttack();
				} */
			}

			// Defenders shoot
			for (Participant participant : party.getDefenders())
			{
				if(!participant.hasVisUnits())
				{
					continue;
				}
				updateAssault("[defender turn] "
						+ participant.getUsername());

				String coords = "";
				if (!participant.isAliens()) {
					coords = "[" + participant.getGalaxy() + ":"
							+ participant.getSystem() + ":"
							+ participant.getPosition() + "]"
							+ (participant.getIsMoon() ? "{lang}LOON_POST{/lang}" : "");
				}
				assaultReportBuf.append("{lang}DEFENDER{/lang} "
						+ "{user["+participant.getUserid()+"]}"+participant.getUsername() + "{/user} " + coords
						+ "<br />\n");
				
				printArtefacts(participant);
				printTechLevels(participant);
				/*
				assaultReportBuf.append(String.format("{lang}GUN_POWER{/lang}: %.0f&#037; ",
						participant.getAttackLevel() * 10.0));						
				assaultReportBuf.append(String.format("{lang}SHIELD_POWER{/lang}: %.0f&#037; ",
						participant.getShieldLevel() * 10.0));				
				assaultReportBuf.append(String.format("{lang}ARMORING{/lang}: %.0f&#037; ",
						participant.getShellLevel() * 10.0));
				assaultReportBuf.append(String.format("{lang}BALLISTICS_POWER{/lang}: %d ",
						participant.getBallisticsLevel()));						
				assaultReportBuf.append(String.format("{lang}MASKING_POWER{/lang}: %d ",
						participant.getMaskingLevel()));						
				assaultReportBuf.append("<br />\n");
				*/
				
				// if(turn == 1)
				{
					assaultReportBuf.append("{hide_text}");
				}
				
				assaultReportBuf.append("<table class=\"atable\"><tr>");
				assaultReportBuf.append("<th>&nbsp;</th>"); // {lang}TYPE{/lang}</th>");

				printParticipant(participant, turn);

				assaultReportBuf.append(quantityBuf);
				assaultReportBuf.append(gunsBuf);
				assaultReportBuf.append(shieldsBuf);
				assaultReportBuf.append(shellsBuf);
				assaultReportBuf.append(frontBuf);
				assaultReportBuf.append(ballisticsBuf);
				assaultReportBuf.append(maskingBuf);
				assaultReportBuf.append(assaultQuantityBuf);

				assaultReportBuf.append("</tr></table><br />\n");

				// if(turn == 1)
				{
					assaultReportBuf.append("{/hide_text}");
				}
				
				/* if(party.defenderHasTargets())
				{
					participant.processAttack();
				} */
			}
			
			if(party.atterHasTargets() || party.defenderHasTargets())
			{
				party.sideAttack(true);
				party.sideAttack(false);
			}

			updateAssault("[turn] finishing");

			// Get values of this turn
			
			party.finishTurn(); // Renew the party: Reload shields and
								// remove ships
			// with explosion flag

			assaultReportBuf.append("<p><b>{lang}FIGHT{/lang}</b></p>\n");

			assaultReportBuf.append("<table class=\"atable\">");
			
			assaultReportBuf.append("<tr>");
			assaultReportBuf.append("<th>&nbsp;</th>");
			assaultReportBuf.append("<th>{img}FIGHT_SHOTS_NUMBER{/img}</th>");
			assaultReportBuf.append("<th>{img}FIGHT_SHOTS_POWER{/img}</th>");
			assaultReportBuf.append("<th>{img}FIGHT_SHOTS_MISS{/img}</th>");
			assaultReportBuf.append("<th>{img}FIGHT_SHIELD_ABSORB{/img}</th>");
			assaultReportBuf.append("<th>{img}FIGHT_SHELL_DESTROYED{/img}</th>");
			assaultReportBuf.append("<th>{img}FIGHT_UNITS_DESTROYED{/img}</th>");
			assaultReportBuf.append("</tr>");

			assaultReportBuf.append("<tr>");
			assaultReportBuf.append("<th>{lang}FIGHT_ATTACKER{/lang}</th>");
			assaultReportBuf.append("<td title='{lang}FIGHT_SHOTS_NUMBER{/lang}'>"+decFormatter.format(attackerShots)+"</td>");
			assaultReportBuf.append("<td title='{lang}FIGHT_SHOTS_POWER{/lang}'>"+decFormatter.format(attackerPower)+"</td>");
			assaultReportBuf.append("<td title='{lang}FIGHT_SHOTS_MISS{/lang}'>"+formatPower(attackerPower - defenderShield - defenderShellDestroyed, attackerPower)+"</td>");
			assaultReportBuf.append("<td title='{lang}FIGHT_SHIELD_ABSORB{/lang}'>"+formatPower(defenderShield, attackerPower)+"</td>");
			assaultReportBuf.append("<td title='{lang}FIGHT_SHELL_DESTROYED{/lang}'>"+formatPower(defenderShellDestroyed, attackerPower)+"</td>");
			assaultReportBuf.append("<td title='{lang}FIGHT_UNITS_DESTROYED{/lang}'>"+decFormatter.format(defenderShipsDestroyed)+"</th>");
			assaultReportBuf.append("</tr>");

			assaultReportBuf.append("<tr>");
			assaultReportBuf.append("<th>{lang}FIGHT_DEFENDER{/lang}</th>");
			assaultReportBuf.append("<td title='{lang}FIGHT_SHOTS_NUMBER{/lang}'>"+decFormatter.format(defenderShots)+"</td>");
			assaultReportBuf.append("<td title='{lang}FIGHT_SHOTS_POWER{/lang}'>"+decFormatter.format(defenderPower)+"</td>");
			assaultReportBuf.append("<td title='{lang}FIGHT_SHOTS_MISS{/lang}'>"+formatPower(defenderPower - attackerShield - attackerShellDestroyed, defenderPower)+"</td>");
			assaultReportBuf.append("<td title='{lang}FIGHT_SHIELD_ABSORB{/lang}'>"+formatPower(attackerShield, defenderPower)+"</td>");
			assaultReportBuf.append("<td title='{lang}FIGHT_SHELL_DESTROYED{/lang}'>"+formatPower(attackerShellDestroyed, defenderPower)+"</td>");
			assaultReportBuf.append("<td title='{lang}FIGHT_UNITS_DESTROYED{/lang}'>"+decFormatter.format(attackerShipsDestroyed)+"</th>");
			assaultReportBuf.append("</tr>");
			
			assaultReportBuf.append("</table>");
			
			assaultReportBuf.append("<br />\n");			
			assaultReportBuf
					.append(String
							.format(
									"{embedded[ATTACKER_SHOTS]}%s{/embedded} {embedded[ATTACKER_POWER]}%s{/embedded} {embedded[DEFENDER_SHIELD]}%s{/embedded}<br />\n",
									decFormatter.format(attackerShots),
									decFormatter.format(attackerPower),
									decFormatter.format(defenderShield)));
			assaultReportBuf
					.append(String
							.format(
									"{embedded[DEFENDER_SHOTS]}%s{/embedded} {embedded[DEFENDER_POWER]}%s{/embedded} {embedded[ATTACKER_SHIELD]}%s{/embedded}<br />\n<br />\n",
									decFormatter.format(defenderShots),
									decFormatter.format(defenderPower),
									decFormatter.format(attackerShield)));
			
			if(turn == 1)
			{
				startBattleAtterPower = attackerPower;
				startBattleDefenderPower = defenderPower;
			}
			assaultReportBuf.append("{battle_matrix_turn_"+turn+"}");
			assaultReportBuf.append(fireStatBuf);
			assaultReportBuf.append("{/battle_matrix_turn_"+turn+"}");
			
			// Check if attacker or defender has still fleet to battle
			boolean atterNoUnits = !party.atterHasUnits();
			boolean defenderNoUnits = !party.defenderHasUnits();
			if (atterNoUnits || defenderNoUnits) {
				if (atterNoUnits && defenderNoUnits) {
					assaultResult = 0; // Draw
				} else if (defenderNoUnits) {
					assaultResult = 1; // Attacker won
				} else {
					assaultResult = 2; // Defender won
				}
				break;
			} else if (turn == BATTLE_MAX_TURNS) {
				assaultResult = 0; // Draw
			}

			updateAssault("[turn] finished");
		}

		if(!isRocketAttack && startBattleAtterPower > 0 && startBattleDefenderPower > 0 && battleTurnsNumber > 0)
		{
			// double atterExperience = Math.min(20, Math.max(0.1, startBattleDefenderPower / startBattleAtterPower)) * battleTurnsNumber;
			// double defenderExperience = Math.min(20, Math.max(0.1, startBattleAtterPower / startBattleDefenderPower)) * battleTurnsNumber;

			// startBattleAtterPower - мощность огн€ атакующего в первом раунде
			// startBattleDefenderPower - мощность огн€ оборон€ющегос€ в первом раунде
			
			double battleTurnsCoefficient = Math.pow(battleTurnsNumber, 1.1) / BATTLE_MAX_TURNS;
			
			double atterExperience = (Math.atan(startBattleDefenderPower / startBattleAtterPower * 1.5 - 1.5)+1)*0.4*3*battleTurnsCoefficient + 1;
			double defenderExperience = (Math.atan(startBattleAtterPower / startBattleDefenderPower * 1.5 - 1.5)+1)*0.4*3*battleTurnsCoefficient + 1;
			
			if(assaultResult == 1) // победил атакующий
			{
				atterExperience *= 3;
			}
			else if(assaultResult == 2) // победил оборон€ющийс€
			{
				defenderExperience *= 3;
			}
			else // ничь€

			{
				atterExperience *= 1.5;
				defenderExperience *= 1.7;
			}
			
			// масштаб битвы
			double battlePower = Math.sqrt(startBattleAtterPower * startBattleDefenderPower) / 1000000;
			double battlePowerCoefficient = (Math.atan(battlePower*10*0.2-1.6)+1)*0.4*19+1;
			
			if(planetid == 0)
			{
				battlePowerCoefficient *= 0.5;
			}
			
			atterExperience *= battlePowerCoefficient;
			defenderExperience *= battlePowerCoefficient;
			
			// итоговый опыт
			atterBattleExperience = (int) Math.round(atterExperience);
			defenderBattleExperience = (int) Math.round(defenderExperience);
		}
		
		// try to destroy building
		StringBuffer targetDestroyBuf = null;
		if (assaultResult == 1 && targetMoon)
		{
			updateAssault("[MOON DESTROY] try to destroy moon");
			targetDestroyBuf = new StringBuffer();
			int deathStarsNumber = 0;
			for (Participant participant : party.getAttackers()) {
				Units units = participant.getUnits(UNIT_DEATH_STAR);
				if(units != null)
				{
					deathStarsNumber += units.getQuantity();
				}
			}
			double chance = clampVal((100 - Math.sqrt(planetDiameter)) * Math.sqrt(deathStarsNumber), 0, 90); 
			if(randDouble(0.1, 100) <= chance)
			{
				for (Participant participant : party.getDefenders())
				{
					for(Units units : participant.getUnits())
					{
						units.destroyAfterTurnFinished(units.getQuantity());
					}
				}
				targetDestroyed = true;
				planetMetal = 0;
				planetSilicon = 0;
				targetDestroyBuf.append("{lang}TARGET_MOON_DESTROYED{/lang}<br />\n");
			}
			else
			{
				chance = clampVal(Math.sqrt(planetDiameter), 0, 90);
				if(randDouble(0.1, 100) <= chance)
				{
					for (Participant participant : party.getAttackers())
					{
						for(Units units : participant.getUnits())
						{
							units.destroyAfterTurnFinished(units.getQuantity());
						}
					}
					assaultResult = 2;
					attackerFleetsExplode = true;
					targetDestroyBuf.append("{lang}ATTACKER_FLLETS_EXPLODE{/lang}<br />\n");
				}
				else
				{
					targetDestroyBuf.append("{lang}TARGET_MOON_NOT_DESTROYED{/lang}<br />\n");
				}
			}
		}
		else if ((assaultResult == 1 || assaultResult == 0) && targetBuildingid != 0) {
			updateAssault("[DESTROY] try to destroy "+targetBuildingName+" "+targetBuildingLevel);
			targetDestroyBuf = new StringBuffer();
			
			int deathStarsDestroyedNumber = 0;
			boolean isBuildingExist = party.getDefendersNumber() > 0;
			if(isBuildingExist && !isBattleSimulation)
			{
				Participant defender = party.getDefenders().get(0);
				if(!defender.isAliens() && planetid != 0)
				{
					String sql = "SELECT level FROM " + tablePrefix + "building2planet "
						+ " WHERE planetid = " + planetid
						+ "   AND buildingid = " + targetBuildingid
						+ "   AND level = " + targetBuildingLevel;
					Statement stmt = Database.createStatement();
					rs = stmt.executeQuery(sql);
					if (!rs.next()) {
						isBuildingExist = false;
					}
				}
			}
			if(isBuildingExist)
			{
				int deathStarsNumber = 0;
				List<Units> unitsList = new ArrayList<Units>();
				for (Participant participant : party.getAttackers()) {
					Units units = participant.getUnits(UNIT_DEATH_STAR);
					if(units != null && units.getQuantity() > 0)
					{
						deathStarsNumber += units.getQuantity();
						unitsList.add(units);
					}
				}
				double targetDestroyChance = clampVal(10 * Math.pow(deathStarsNumber, 0.8), 0, 90);
				if(randDouble(0.1, 100) <= targetDestroyChance)
				{
					targetDestroyed = true;
					
					targetDestroyBuf.append("{lang}TARGET_BUILDING_DESTROYED{/lang}");
					targetDestroyBuf.append(" {lang}" + targetBuildingName + "{/lang}");
					if(targetBuildingLevel > 0)
					{
						targetDestroyBuf.append(" " + targetBuildingLevel);
					}
					targetDestroyBuf.append("<br />\n");
					
					targetDestroyBuf.append("{embedded2[DESTROYED_BUILDING_PLANET_METAL_AND_SILICON]}");
					targetDestroyBuf.append("("+decFormatter.format(targetBuildingMetal)+")");
					targetDestroyBuf.append("("+decFormatter.format(targetBuildingSilicon)+")");
					targetDestroyBuf.append("{/embedded2}");
					targetDestroyBuf.append("<br />\n");
					
					planetMetal += targetBuildingMetal;
					planetSilicon += targetBuildingSilicon;
				}
				for(Units units : unitsList)
				{
					int destroyed = 0;
					for(int i = 0; i < units.getQuantity(); i++)
					{
						if(randDouble(0.1, 100) > targetDestroyChance)
						{
							destroyed++;
						}
					}
					if(destroyed > 0)
					{
						units.destroyAfterTurnFinished(destroyed);
						deathStarsDestroyedNumber += destroyed;						
					}
				}
			}
			if(!targetDestroyed)
			{
				targetDestroyBuf.append("{lang}TARGET_BUILDING_NOT_DESTROYED{/lang}");
				targetDestroyBuf.append(" {lang}" + targetBuildingName + "{/lang}");
				if(targetBuildingLevel > 0)
				{
					targetDestroyBuf.append(" " + targetBuildingLevel);
				}
				targetDestroyBuf.append("<br />\n");
			}
			if(deathStarsDestroyedNumber > 0)
			{
				targetDestroyBuf.append("{embedded[DESTROY_MISSION_DEATH_STARS_DESTROYED]}");
				targetDestroyBuf.append(decFormatter.format(deathStarsDestroyedNumber));
				targetDestroyBuf.append("{/embedded}<br />\n");
			}
		}
		
		// try to grasp loser ships
		updateAssault("[GRASP] try to grasp loser ships");
		if ((assaultResult == 1 || assaultResult == 2) 
				&& !isRocketAttack 
				&& !attackerFleetsExplode
				&& !(targetMoon && targetDestroyed))
		{
			List<Participant> loser = assaultResult == 2 ? party.getAttackers()
					: party.getDefenders();

			for (Participant participant : loser) {
				for (Units units : participant.getUnits()) {
					if (units.getType() == Assault.UNIT_TYPE_FLEET 
							&& units.getTotalDestroyed() > 0)
					{
						int unitsNumber = Math.min(units.getTotalDestroyed(), units.getStartBattleQuantity() / 2);
						double minPercent = 100 * 0.5 / unitsNumber;
						double maxPercent = Math.min(100, Math.max(minPercent + 5, 100 * 2.5 / unitsNumber));
						double graspPercent = randDouble(minPercent, maxPercent);

						unitsNumber = Math.max(1, unitsNumber);
						
						int grasped = (int) (unitsNumber * graspPercent / 100);
						if(grasped > 0)
						{
							for (int i = 0; i < grasped; i++) {
								Participant target = assaultResult == 1 
									? party.getRandomAtter()
									: party.getRandomDefender();
								target.addGraspedUnits(units, 1);
							}
							if(graspedUnits.containsKey(units.getName()))
							{
								graspedUnits.put(units.getName(), graspedUnits.get(units.getName())+grasped);
							}
							else
							{
								graspedUnits.put(units.getName(), grasped);
							}
						}
					}
				}
			}
		}

		if(targetDestroyBuf != null)
		{
			// assaultReportBuf.append("<br />\n");
			assaultReportBuf.append("<p><b>{lang}DESTROY_MISSION_TITLE{/lang}</b></p>\n");
			assaultReportBuf.append(targetDestroyBuf);
			assaultReportBuf.append("<br />\n");
		}
		// Final result of remaining ships
		// Attackers
		updateAssault("[Attackers] Final result of remaining ships");

		if(!isRocketAttack || battleTurnsNumber > 0)
		{
			assaultReportBuf.append("<p><b>{lang}SUMMARY{/lang}</b></p>\n");
		}
		
		for (Participant participant : party.getAttackers()) {
			String coords = "";
			if (!participant.isAliens()) {
				coords = "[" + participant.getGalaxy() + ":"
						+ participant.getSystem() + ":"
						+ participant.getPosition() + "]"
						+ (participant.getIsMoon() ? "{lang}LOON_POST{/lang}" : "");
			}
			assaultReportBuf.append("{lang}ATTACKER{/lang} "
					+ "{user["+participant.getUserid()+"]}"+participant.getUsername() + "{/user} " + coords
					+ "<br />\n");

			// if (assaultResult != 2) {
			if (assaultResult == 2)
			{
				assaultReportBuf
						.append("<strong>{lang}DESTROYED{/lang}</strong><br />\n");
			}
			if(participant.hasVisUnits())
			{
				assaultReportBuf
						.append("<table class='atable'><tr><th>&nbsp;</th>");

				printParticipant(participant, -1);

				assaultReportBuf.append(quantityBuf);
				assaultReportBuf.append(gunsBuf);
				assaultReportBuf.append(shieldsBuf);
				assaultReportBuf.append(shellsBuf);
				assaultReportBuf.append(assaultQuantityBuf);

				assaultReportBuf.append("</tr></table><br />\n");
			}
		}

		// Defenders
		updateAssault("[Defenders] Final result of remaining ships");
		assaultReportBuf.append("<br />\n");
		for (Participant participant : party.getDefenders()) {
			String coords = "";
			if (!participant.isAliens()) {
				coords = "[" + participant.getGalaxy() + ":"
						+ participant.getSystem() + ":"
						+ participant.getPosition() + "]"
						+ (participant.getIsMoon() ? "{lang}LOON_POST{/lang}" : "");
			}
			assaultReportBuf.append("{lang}DEFENDER{/lang} "
					+ "{user["+participant.getUserid()+"]}"+participant.getUsername() + "{/user} " + coords
					+ "<br />\n");

			// if (assaultResult != 1) {
			if (assaultResult == 1)
			{
				assaultReportBuf
						.append("<strong>{lang}DESTROYED{/lang}</strong><br />\n");
			}
			if(participant.hasVisUnits())
			{
				// if(battleTurnsNumber == 1)
				{
					assaultReportBuf.append("{hide_text}");
				}
				
				assaultReportBuf
						.append("<table class='atable'><tr><th>&nbsp;</th>");

				printParticipant(participant, -1);

				assaultReportBuf.append(quantityBuf);
				assaultReportBuf.append(gunsBuf);
				assaultReportBuf.append(shieldsBuf);
				assaultReportBuf.append(shellsBuf);
				assaultReportBuf.append(assaultQuantityBuf);

				assaultReportBuf.append("</tr></table><br />\n");

				// if(battleTurnsNumber == 1)
				{
					assaultReportBuf.append("{/hide_text}");
				}
			}
		}
		
		party.finish();

		if(attackerFleetsExplode || (targetMoon && targetDestroyed))
		{
			debrisMetal = 0;
			debrisSilicon = 0;
			// planetMetal += haulMetal;
			// planetSilicon += haulSilicon;
			// planetHydrogen += haulHydrogen; 
			haulMetal = 0;
			haulSilicon = 0;
			haulHydrogen = 0;
		}
		
		// Assault result out steam
		updateAssault("[assault] Assault result out steam");

		assaultReportBuf.append("<br />\n");
		
		/* if(targetDestroyBuf != null)
		{
			// assaultReportBuf.append("<br />\n");
			assaultReportBuf.append(targetDestroyBuf);
			assaultReportBuf.append("<br />\n<br />\n");
		} */

		if(!isRocketAttack)
		{
			switch (assaultResult) {
			case 0:
				assaultReportBuf.append("<b>{lang}BATTLE_DRAW{/lang}</b><br />\n<br />\n");
				break;
			case 1:
				assaultReportBuf.append("<b>{lang}ATTACKER_WON{/lang}</b><br />\n");
				assaultReportBuf.append("{lang}ATTACKER_HAUL{/lang}<br />\n");
				assaultReportBuf.append(decFormatter.format(haulMetal));
				assaultReportBuf.append(" {lang}METAL{/lang}, ");
				assaultReportBuf.append(decFormatter.format(haulSilicon));
				assaultReportBuf.append(" {lang}SILICON{/lang} {lang}AND{/lang} ");
				assaultReportBuf.append(decFormatter.format(haulHydrogen));
				assaultReportBuf.append(" {lang}HYDROGEN{/lang}<br />\n<br />\n");
				break;
			case 2:
				assaultReportBuf.append("<b>{lang}DEFENDER_WON{/lang}</b><br />\n<br />\n");
				break;
			}
		}
		
		/*
		System.out.println("PRE atterLostMetal: "+atterLostMetal);
		System.out.println("PRE atterLostSilicon: "+atterLostSilicon);
		System.out.println("PRE atterLostHydrogen: "+atterLostHydrogen);
		System.out.println("PRE defenderLostMetal: "+defenderLostMetal);
		System.out.println("PRE defenderLostSilicon: "+defenderLostSilicon);
		System.out.println("PRE defenderLostHydrogen: "+defenderLostHydrogen);
		System.out.println("PRE debrisMetal: "+debrisMetal);
		System.out.println("PRE debrisSilicon: "+debrisSilicon);
		
		atterLostMetal = clampVal(atterLostMetal, -maxIntValue, maxIntValue);
		atterLostSilicon = clampVal(atterLostSilicon, -maxIntValue, maxIntValue);
		atterLostHydrogen = clampVal(atterLostHydrogen, -maxIntValue, maxIntValue);
		defenderLostMetal = clampVal(defenderLostMetal, -maxIntValue, maxIntValue);
		defenderLostSilicon = clampVal(defenderLostSilicon, -maxIntValue, maxIntValue);
		defenderLostHydrogen = clampVal(defenderLostHydrogen, -maxIntValue, maxIntValue);
		debrisMetal = clampVal(debrisMetal, -maxIntValue, maxIntValue);
		debrisSilicon = clampVal(debrisSilicon, -maxIntValue, maxIntValue);
		*/
		
		defenderLostMetal += targetBuildingMetal;
		defenderLostSilicon += targetBuildingSilicon;
		defenderLostHydrogen += targetBuildingHydrogen;
		
		System.out.println("atterLostMetal: "+decFormatter.format(atterLostMetal));
		System.out.println("atterLostSilicon: "+decFormatter.format(atterLostSilicon));
		System.out.println("atterLostHydrogen: "+decFormatter.format(atterLostHydrogen));
		System.out.println("defenderLostMetal: "+decFormatter.format(defenderLostMetal));
		System.out.println("defenderLostSilicon: "+decFormatter.format(defenderLostSilicon));
		System.out.println("defenderLostHydrogen: "+decFormatter.format(defenderLostHydrogen));
		System.out.println("debrisMetal: "+decFormatter.format(debrisMetal));
		System.out.println("debrisSilicon: "+decFormatter.format(debrisSilicon));
		
		// Lost units and debris out stream
		assaultReportBuf.append("{embedded4[ATTACKER_LOST_RES_4]}");
		assaultReportBuf.append("("+decFormatter.format(atterLostMetal+atterLostSilicon+atterLostHydrogen)+")");
		assaultReportBuf.append("("+decFormatter.format(atterLostMetal)+")");
		assaultReportBuf.append("("+decFormatter.format(atterLostSilicon)+")");
		assaultReportBuf.append("("+decFormatter.format(atterLostHydrogen)+")");
		assaultReportBuf.append("{/embedded4}<br />\n");

		assaultReportBuf.append("{embedded4[DEFENDER_LOST_RES_4]}");
		assaultReportBuf.append("("+decFormatter.format(defenderLostMetal+defenderLostSilicon+defenderLostHydrogen)+")");
		assaultReportBuf.append("("+decFormatter.format(defenderLostMetal)+")");
		assaultReportBuf.append("("+decFormatter.format(defenderLostSilicon)+")");
		assaultReportBuf.append("("+decFormatter.format(defenderLostHydrogen)+")");
		assaultReportBuf.append("{/embedded4}<br />\n<br />\n");

		// Grasped units
		if (graspedUnits.size() > 0)
		{
			assaultReportBuf.append("<b>{lang}GRASPED_UNITS{/lang}:</b> ");

			int i = 0;
			for (String unitname : graspedUnits.keySet()) {
				if (i++ > 0) {
					assaultReportBuf.append(", ");
				}
				assaultReportBuf.append("{lang}" + unitname + "{/lang}: ");
				assaultReportBuf.append(decFormatter.format(graspedUnits.get(unitname)));
			}
			assaultReportBuf.append("<br /><br />\n");
		}
		
		if (debrisMetal > 0 || debrisSilicon > 0) {
			assaultReportBuf.append("{embedded2[DEBRIS_METAL_AND_SILICON]}");
			assaultReportBuf.append("("+decFormatter.format(debrisMetal)+")");
			assaultReportBuf.append("("+decFormatter.format(debrisSilicon)+")");
			assaultReportBuf.append("{/embedded2}<br />\n");
			/*
			assaultReportBuf
					.append(String
							.format(
									"{lang}DEBRIS{/lang} %s {lang}METAL{/lang} {lang}AND{/lang} %s {lang}SILICON{/lang}.<br />\n",
									decFormatter.format(debrisMetal),
									decFormatter.format(debrisSilicon)));
			*/
		}

		// Get chance of moon appearance
		moonChance = 0;
		boolean USE_MOON_ARTEFACT_STYLE = true;
		if((!ismoon || USE_MOON_ARTEFACT_STYLE) 
			&& !isRocketAttack
			&& planetid != 0
			&& !attackerFleetsExplode
			&& !(targetMoon && targetDestroyed)
			)
		{
			if(USE_MOON_ARTEFACT_STYLE)
			{
				moonChance = 0;

				int effectExperience = Math.min(atterBattleExperience, defenderBattleExperience); 
				if(effectExperience >= MOON_EXP_START_CHANCE)
				{
					int experienceMoonChance = (int) Math.round(Math.pow(effectExperience, 0.8));
					int debrisMoonChance = (int) ((debrisMetal + debrisSilicon) / MOON_PERCENT_PER_RES);
					moonChance = Math.min(experienceMoonChance, debrisMoonChance);
					
					int attackerLostUnits = party.getAtterLostUnits();
					int defenderLostUnits = party.getDefenderLostUnits();
					int lostUnits = Math.min(attackerLostUnits, defenderLostUnits);
					moonChance = Math.min(moonChance, lostUnits); 
				}				

				int moonStartChance = MOON_START_CHANCE;
				int guaranteedDebrisMoonChance = (int) ((debrisMetal + debrisSilicon) / MOON_GUARANTEED_PERCENT_PER_RES);					
				moonChance = Math.max(moonChance, guaranteedDebrisMoonChance);
				moonChance = clampVal(moonChance, 0, MOON_MAX_CHANCE);
				if(guaranteedDebrisMoonChance > 0 && moonStartChance > guaranteedDebrisMoonChance)
				{
					moonStartChance = guaranteedDebrisMoonChance;
				}
				
				if (moonChance >= moonStartChance)
				{
					if(moonAllowType == 2)
					{
						moonChance = 0;
						assaultReportBuf.append("<br />\n{lang}MOON_ARTEFACT_CHANCE_NONE{/lang}<br />\n");
					}
					else
					{
						String langStrId = "MOON_ARTEFACT_CHANCE";
						if(moonAllowType == 1)
						{
							moonChance = Math.max(moonChance/3, 1);
							langStrId = "MOON_ARTEFACT_CHANCE_LOW";
						}
						assaultReportBuf.append("<br />\n{embedded["+langStrId+"]}"
								+ decFormatter.format(moonChance)
								+ "{/embedded}<br />\n");
						if (randDouble(0.1, 100) <= moonChance) {
							moon = true;
							assaultReportBuf.append("<b>{lang}MOON_ARTEFACT_APPEARED{/lang}</b><br />\n");
						}
						else
						{
							assaultReportBuf.append("{lang}MOON_ARTEFACT_NOT_APPEARED{/lang}<br />\n");
						}
					}
				}
				else
				{
					moonChance = 0;
				}
			}
			else if(atterBattleExperience >= MOON_EXP_START_CHANCE && defenderBattleExperience >= MOON_EXP_START_CHANCE)
			{
				moonChance = (int)clampVal((debrisMetal + debrisSilicon) / MOON_PERCENT_PER_RES 
						+ Assault.randDouble(-2, 2), 0, MOON_MAX_CHANCE);
				if (moonChance > 0)
				{
					assaultReportBuf.append("{embedded[MOON_CHANCE]}"
							+ decFormatter.format(moonChance)
							+ "{/embedded}<br />\n");
					if (randInt(1, 100) <= moonChance) {
						moon = true;
						assaultReportBuf.append("<strong>{lang}MOON{/lang}</strong><br />\n");
	
						double capacity = MOON_PERCENT_PER_RES * MOON_MAX_CHANCE;
						if ((debrisMetal + debrisSilicon) > capacity) {
							while (capacity > MIN_FREE_CAPACITY) {
								double res, cnt;
								if (debrisMetal > 0) {
									cnt = 1 + (debrisSilicon > 0 ? 1 : 0);
									res = Math.min(debrisMetal, Math.ceil(capacity / cnt));
									capacity -= res;
									debrisMetal -= res;
								}
								if (debrisSilicon > 0) {
									res = Math.min(debrisSilicon, capacity);
									capacity -= res;
									debrisSilicon -= res;
								}
							}
						} else {
							capacity = 0;
							debrisMetal = 0;
							debrisSilicon = 0;
						}
						assaultReportBuf.append("{embedded2[DEBRIS_AFTER_MOON_CREATED]}");
						assaultReportBuf.append("("+decFormatter.format(debrisMetal)+")");
						assaultReportBuf.append("("+decFormatter.format(debrisSilicon)+")");
						assaultReportBuf.append("{/embedded2}<br />\n");
					}
				}
			}
		}

		// Repaired defense out stream
		if (defenseRepaired.size() > 0)
		{
			assaultReportBuf.append("<br />\n");
			assaultReportBuf.append("<b>{lang}REPAIRED_UNITS{/lang}:</b> ");

			int i = 0;
			for (String unitname : defenseRepaired.keySet()) {
				if (i++ > 0) {
					assaultReportBuf.append(", ");
				}
				assaultReportBuf.append("{lang}" + unitname + "{/lang}: ");
				assaultReportBuf.append(decFormatter.format(defenseRepaired
						.get(unitname)));
			}
			assaultReportBuf.append("<br />\n");
		}
		
		System.out.println("startBattleAtterPower: " + decFormatter.format(startBattleAtterPower));
		System.out.println("startBattleDefenderPower: " + decFormatter.format(startBattleDefenderPower));
		
		if(atterBattleExperience > 0 || defenderBattleExperience > 0)
		{
			System.out.println("atterBattleExperience: " + atterBattleExperience);
			System.out.println("defenderBattleExperience: " + defenderBattleExperience);
			
			assaultReportBuf.append("<br />\n");
			
			assaultReportBuf.append("{embedded[ATTACKER_EXPERIENCE]}");
			assaultReportBuf.append(decFormatter.format(atterBattleExperience));
			assaultReportBuf.append("{/embedded}<br />\n");

			assaultReportBuf.append("{embedded[DEFENDER_EXPERIENCE]}");
			assaultReportBuf.append(decFormatter.format(defenderBattleExperience));
			assaultReportBuf.append("{/embedded}");
		}
		
		assaultReportBuf.append("</div>");

		updateAssault("[assault] finishing, rep len: " + assaultReportBuf.length());
		System.out.println("rep len: " + assaultReportBuf.length());

		if (debugmode)
		{
			String assaultReport = assaultReportBuf.toString();
			try {
				FileOutputStream output = new FileOutputStream("oxsar-assaultres.html");
				for (int i = 0; i < assaultReport.length(); i++) {
					output.write((byte) assaultReport.charAt(i));
				}
				output.close();
				output = null;
			} catch (Exception e) {
				// updateAssault(e.getMessage(), true);
			}
			if(debugmode)
			{
				System.out.println(assaultReport);
			}
			else
			{
				assaultReport = null;
				finish();
			}
		} else {
			finish();
		}

		System.out.println("Finished");
	}
	
	public static void printArtefacts(Participant participant)
	{
		if(participant.getArtefacts().size() > 0)
		{
			int i = 0;
			for(Units units : participant.getArtefacts())
			{
				if(i++ > 0)
				{
					assaultReportBuf.append(", ");
				}
				assaultReportBuf.append("{lang}" + units.getName() + "{/lang}: "
						+ decFormatter.format(units.getQuantity()));
			}
			assaultReportBuf.append("<br />\n");
		}
	}
	
	public static String getStringAutoValue(double level, boolean forceInt)
	{
		if(forceInt || Math.abs((int)level - level) < 0.1){
			return String.format("%.0f", level);
		}
		
		return oneDigitFormatter.format(level);
	}

	public static String getStringAutoValue(double level)
	{
		return getStringAutoValue(level, false);
	}
	
	public static void printAddTech(double level, boolean isPercent)
	{
		if(level != 0)
		{
			assaultReportBuf.append(String.format("<span class='%s'><nowrap>( %s%s%s )</nowrap></span> ", 
					level > 0 ? "rep_quantity_grasped" : "rep_quantity_diff",
					level > 0 ? "+" : "",
					getStringAutoValue(level, isPercent),
					isPercent ? "&#037" : ""));						
		}
	}
	
	public static void printTechLevels(Participant participant)
	{
		assaultReportBuf.append(String.format("{lang}GUN_POWER{/lang}: %.0f&#037; ",
				participant.getAttackLevel() * 10.0));	
		printAddTech(participant.getAttackAddLevel() * 10, true);
		
		assaultReportBuf.append(String.format("&nbsp; {lang}SHIELD_POWER{/lang}: %.0f&#037; ",
				participant.getShieldLevel() * 10.0));				
		printAddTech(participant.getShieldAddLevel() * 10, true);
		
		assaultReportBuf.append(String.format("&nbsp; {lang}SHELL_POWER{/lang}: %.0f&#037; ",
				participant.getShellLevel() * 10.0));
		printAddTech(participant.getShellAddLevel() * 10, true);
		
		assaultReportBuf.append(String.format("&nbsp; {lang}BALLISTICS_TECH{/lang}: %s ",
				getStringAutoValue(participant.getBallisticsLevel())));
		printAddTech(participant.getBallisticsAddLevel(), false);
		
		assaultReportBuf.append(String.format("&nbsp; {lang}MASKING_TECH{/lang}: %s ",
				getStringAutoValue(participant.getMaskingLevel())));						
		printAddTech(participant.getMaskingAddLevel(), false);
		
		if(isBattleAdvanced)
		{
			assaultReportBuf.append(String.format("<br />\n{lang}LASER_TECH{/lang}: %d ",
					participant.getLaserLevel()));						
			printAddTech(participant.getLaserAddLevel(), false);			

			assaultReportBuf.append(String.format("&nbsp; {lang}ION_TECH{/lang}: %d ",
					participant.getIonLevel()));						
			printAddTech(participant.getIonAddLevel(), false);			

			assaultReportBuf.append(String.format("&nbsp; {lang}PLASMA_TECH{/lang}: %d ",
					participant.getPlasmaLevel()));						
			printAddTech(participant.getPlasmaAddLevel(), false);			

			assaultReportBuf.append("<br />\n");			
			assaultReportBuf.append(String.format("{lang}ADV_GUN_POWER{/lang}: &nbsp; %.0f&#037; &nbsp; %.0f&#037; &nbsp; %.0f&#037;",
					participant.getAttackFactor(0) * 100.0,
					participant.getAttackFactor(1) * 100.0,
					participant.getAttackFactor(2) * 100.0
					));
			
			assaultReportBuf.append("<br />\n");			
			assaultReportBuf.append(String.format("{lang}ADV_SHIELD_POWER{/lang}: &nbsp; %.0f&#037; &nbsp; %.0f&#037; &nbsp; %.0f&#037;",
					participant.getShieldFactor(0) * 100.0,
					participant.getShieldFactor(1) * 100.0,
					participant.getShieldFactor(2) * 100.0
					));
		}
		
		assaultReportBuf.append("<br />\n");
	}
	
	public static void printParticipant(Participant participant, int turn)
	{
		resetTurnBuffers();
		
		boolean frontSet = false;
		int minFront = 0, maxFront = 0;
		
		boolean ballisticsSet = false;
		double minBallistics = 0, maxBallistics = 0;
		
		boolean maskingSet = false;
		double minMasking = 0, maxMasking = 0;
		
		for (Units units : participant.getUnits())
		{
			updateAssault("[makeParticipantTurn] unitid: "+units.getUnitid());
			if (units.getQuantity() > 0 || units.getQuantityDiff() < 0)
			{
				assaultReportBuf.append("<th>{lang}" + units.getName()
						+ "{/lang}</th>");

				int grasped = units.getTotalGrasped();
				int quantity = units.getQuantity();
				int diff = units.getQuantityDiff() - grasped;

				boolean quantityShown = false;
				// boolean diffShown = false;

				quantityBuf.append("<td nowrap='nowrap'>");
				if (diff < 0 || (grasped > 0 && grasped != quantity) || (grasped == 0 && quantity > 0)) {
					if(quantity > 0)
					{
						quantityBuf.append(decFormatter.format(quantity));
					}
					else
					{
						quantityBuf.append("<span class='rep_quantity_diff'>");
						quantityBuf.append(decFormatter.format(quantity));
						quantityBuf.append("</span>");
					}
					// quantityBuf.append(" *"+decFormatter.format(units.getStartAssaultQuantity()));
					quantityShown = true;
				}
				if (diff < 0) {
					quantityBuf.append(" <span class='rep_quantity_diff'>( ");
					quantityBuf.append(decFormatter.format(diff));
					quantityBuf.append(" )</span>");
					// diffShown = true;
				}
				if (grasped > 0) {
					quantityBuf.append(" <span class='rep_quantity_grasped'>");
					if (quantityShown)
					{
						quantityBuf.append("( ");
					}
					quantityBuf.append("+" + decFormatter.format(grasped));
					if (quantityShown)
					{
						quantityBuf.append(" )");
					}
					quantityBuf.append("</span>");
				}

				if (units.getDamaged() > 0) {
					String spanClass = units.getDamagedShellPercent() <= 70 ? "rep_quantity_damage"
							: "rep_quantity_damage_low";
					quantityBuf
							.append("<br /><span class='" + spanClass + "'>");
					if (units.getDamaged() != quantity) {
						quantityBuf.append(decFormatter.format(units
								.getDamaged())
								+ " - ");
					}
					quantityBuf.append(units.getDamagedShellPercent());
					quantityBuf.append("%</span>");
				}
				quantityBuf.append("</td>");

				if(units.getUnitid() != UNIT_INTERCEPTOR_ROCKET)
				{
					if(!isBattleAdvanced)
					{
						gunsBuf.append("<td>" + decFormatter.format(units.getBaseAttack()));
					}
					else
					{
						gunsBuf.append("<td>");
						if(turn == 1) // || turn < 0)
						{
							gunsBuf.append(String.format("%s<br />%s<br />%s<br />\n",
									decFormatter.format(units.getAttack0()),
									decFormatter.format(units.getAttack1()),
									decFormatter.format(units.getAttack2())
								));
						}
						gunsBuf.append("<span class='rep_quantity_damage_low'>(" + decFormatter.format(units.getBaseAttack()) + ")</span>");
					}
					gunsBuf.append("</td>");
				}
				else
				{
					gunsBuf.append("<td>-</td>");
				}
				
				if(units.getUnitid() != UNIT_INTERCEPTOR_ROCKET && units.getUnitid() != UNIT_INTERPLANETARY_ROCKET)
				{
					if(!isBattleAdvanced)
					{
						shieldsBuf.append("<td>" + decFormatter.format(units.getBaseShield())); 
					}
					else
					{
						shieldsBuf.append("<td>");
						if(turn == 1) // || turn < 0)
						{
							shieldsBuf.append(String.format("%s<br />%s<br />%s<br />\n",
									decFormatter.format(units.getShield0()),	
									decFormatter.format(units.getShield1()),	
									decFormatter.format(units.getShield2())	
								));
						}
						shieldsBuf.append("<span class='rep_quantity_damage_low'>(" + decFormatter.format(units.getBaseShield()) + ")</span>"); 
					}
					shieldsBuf.append("</td>");
					
					shellsBuf.append("<td>" + decFormatter.format(units.getShell()) + "</td>");
					
					frontBuf.append("<td>" + decFormatter.format(units.getFront()) + "</td>");
					if(!frontSet)
					{
						frontSet = true;
						minFront = maxFront = units.getFront();
					}
					else
					{
						minFront = Math.min(minFront, units.getFront());
						maxFront = Math.max(maxFront, units.getFront());
					}
				}
				else
				{
					shieldsBuf.append("<td>-</td>");
					shellsBuf.append("<td>-</td>");
					frontBuf.append("<td>-</td>");
				}
				
				ballisticsBuf.append("<td>" + getStringAutoValue(units.getBallisticsLevel()) + "</td>");
				if(!ballisticsSet)
				{
					ballisticsSet = true;
					minBallistics = maxBallistics = units.getBallisticsLevel();
				}
				else
				{
					minBallistics = Math.min(minBallistics, units.getBallisticsLevel());
					maxBallistics = Math.max(maxBallistics, units.getBallisticsLevel());
				}
				
				maskingBuf.append("<td>" + getStringAutoValue(units.getMaskingLevel()) + "</td>");
				if(!maskingSet)
				{
					maskingSet = true;
					minMasking = maxMasking = units.getMaskingLevel();
				}
				else
				{
					minMasking = Math.min(minMasking, units.getMaskingLevel());
					maxMasking = Math.max(maxMasking, units.getMaskingLevel());
				}
				
				if(units.getStartBattleQuantity() > 0)
				{
					int alivePercent = Math.min(100, (int)(units.getQuantity() * 100.0 / units.getStartBattleQuantity()));
					assaultQuantityBuf.append("<td>"
							+ "<div class='rep_destroyed_back_div'>"
							+ "<div class='rep_alive_over_div' style='width: "+alivePercent+"%' />"
							+ "</div></td>");
				}
				else
				{
					assaultQuantityBuf.append("<td></td>");
				}
			}
		}
		if(minFront == maxFront)
		{
			frontBuf.setLength(0);
		}
		if(maxBallistics == 0) // minBallistics == maxBallistics)
		{
			ballisticsBuf.setLength(0);
		}
		if(maxMasking == 0) // minMasking == maxMasking)
		{
			maskingBuf.setLength(0);
		}
	}

	public static Unit newUnit(int shield1, int shield2, int shield3, int shell) {
		if (freeUnits.size() > 0) {
			Unit unit = freeUnits.remove(freeUnits.size() - 1);
			// unit.shield = shield;
			// unit.shell = shell;
			unit.Init(shield1, shield2, shield3, shell);
			return unit;
		}
		return new Unit(shield1, shield2, shield3, shell);
	}

	public static void freeUnit(Unit unit) {
		freeUnits.add(unit);
	}

	public static boolean canShootAgain(Units units, Units target)
	{
		// Get rapidfire
		int rf = getRapidFire(units.getUnitid(), target.getUnitid()); // rapidfire[unit.unitid][target.unitid];
		if (rf > 0) {
			// Random chance of shot again
			double chance = 100.0 * (rf - 1) / rf;
			return randDouble(0, 100) <= chance;
		}
		return false;
	}

	public static String getDBHost() {
		return "jdbc:mysql://" + dbhost + "/" + dbdatabase;
	}

	public static String getUsername() {
		return username;
	}

	public static String getPassword() {
		return dbpasswd;
	}

	public static String getTablePrefix() {
		return tablePrefix;
	}

	public static String getAssaultid() {
		return String.valueOf(assaultid);
	}

	public static String getPlanetid() {
		return String.valueOf(planetid);
	}

	public static void resetTurnBuffers() {
		quantityBuf.setLength(0);
		quantityBuf.append("</tr><tr><th>{lang}QUANTITY{/lang}</th>");

		gunsBuf.setLength(0);
		gunsBuf.append("</tr><tr><th>{lang}GUNS{/lang}</th>");

		shieldsBuf.setLength(0);
		shieldsBuf.append("</tr><tr><th>{lang}SHIELDS{/lang}</th>");

		shellsBuf.setLength(0);
		shellsBuf.append("</tr><tr><th>{lang}ARMOR{/lang}</th>");
		
		frontBuf.setLength(0);
		frontBuf.append("</tr><tr><th>{lang}FRONT{/lang}</th>");
		
		ballisticsBuf.setLength(0);
		ballisticsBuf.append("</tr><tr><th>{lang}BALLISTICS_POWER{/lang}</th>");
		
		maskingBuf.setLength(0);
		maskingBuf.append("</tr><tr><th>{lang}MASKING_POWER{/lang}</th>");
		
		assaultQuantityBuf.setLength(0);
		assaultQuantityBuf.append("</tr><tr><th></th>");
	}

	public static void finish()
	{
		int moonCreated = moon ? 1 : 0;

		Statement stmt = Database.createStatement();

		// Set debris
		String sql = "UPDATE " + tablePrefix + "galaxy SET metal = metal + '"
			+ debrisMetal + "', silicon = silicon + '" + debrisSilicon
			+ "' WHERE planetid = '" + planetid + "' OR moonid = '"
			+ planetid + "'";
		updateAssault("[finish] Set debris, sql: " + sql);
		try {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			System.err.println(sql);
			e.printStackTrace();
		}

		// Subtract haul from planet
		if (assaultResult == 1) {
			sql = "UPDATE " + tablePrefix
					+ "planet SET metal = GREATEST(0, metal + '" + (targetBuildingMetal - haulMetal)
					+ "'), silicon = GREATEST(0, silicon + '" + (targetBuildingSilicon - haulSilicon)
					+ "'), hydrogen = GREATEST(0, hydrogen + '" + (targetBuildingHydrogen - haulHydrogen)
					+ "'), last = UNIX_TIMESTAMP(NOW())"
					+ " WHERE planetid = '" + planetid + "'";
			updateAssault("[finish] Subtract haul from planet, sql: " + sql);
			try {
				stmt.executeUpdate(sql);
				
				sql = "INSERT INTO " + tablePrefix +"res_log " +
						" (type, planetid, userid, metal, silicon, hydrogen, result_metal, result_silicon, result_hydrogen, ownerid)" +
						" SELECT "+RES_UPDATE_ATTACKER+", planetid, userid, " +
								"'"+(targetBuildingMetal - haulMetal)+"', " +
								"'"+(targetBuildingSilicon - haulSilicon)+"', " +
								"'"+(targetBuildingHydrogen - haulHydrogen)+"', " +
								"metal, silicon, hydrogen, '"+assaultid+"' " +
						" FROM " + tablePrefix +"planet " +
						" WHERE planetid = '" + planetid + "'";
				stmt.executeUpdate(sql);		
				
			} catch (SQLException e) {
				System.err.println(sql);
				e.printStackTrace();
			}
		}

		// Set final data for this assault
		long time = System.currentTimeMillis() - startTime;
		sql = "UPDATE " + tablePrefix + "assault SET report = '"
				+ assaultReportBuf.toString().replaceAll("'", "''")
				+ "', `key` = '" + key
				+ "', `key2` = '" + key2
				+ "', `result` = '" + assaultResult + "', moonchance = '" + moonChance 
				+ "', moon = '" + moonCreated
				+ "', target_destroyed = '" + (targetDestroyed ? 1 : 0)
				+ "', attacker_explode = '" + (attackerFleetsExplode ? 1 : 0)
				+ "', attacker_lost_res = '" + clampDbVal(atterLostMetal + atterLostSilicon + atterLostHydrogen)
				+ "', attacker_lost_metal = '" + clampDbVal(atterLostMetal)
				+ "', attacker_lost_silicon = '" + clampDbVal(atterLostSilicon)
				+ "', attacker_lost_hydrogen = '" + clampDbVal(atterLostHydrogen)
				+ "', defender_lost_res = '" + clampDbVal(defenderLostMetal + defenderLostSilicon + defenderLostHydrogen)
				+ "', defender_lost_metal = '" + clampDbVal(defenderLostMetal)
				+ "', defender_lost_silicon = '" + clampDbVal(defenderLostSilicon)
				+ "', defender_lost_hydrogen = '" + clampDbVal(defenderLostHydrogen)
				+ "', debris_metal = '" + clampDbVal(debrisMetal)
				+ "', debris_silicon = '" + clampDbVal(debrisSilicon)
				+ "', planet_metal = '" + clampDbVal(planetMetal)
				+ "', planet_silicon = '" + clampDbVal(planetSilicon)
				+ "', planet_hydrogen = '" + clampDbVal(planetHydrogen)
				+ "', haul_metal = '" + clampDbVal(haulMetal)
				+ "', haul_silicon = '" + clampDbVal(haulSilicon)
				+ "', haul_hydrogen = '" + clampDbVal(haulHydrogen)
				+ "', attacker_exp = '" + atterBattleExperience
				+ "', defender_exp = '" + defenderBattleExperience
				+ "', turns = '" + battleTurnsNumber
				+ "', gentime = '" + time + "', accomplished = '1'"
				+ " WHERE assaultid = '" + assaultid + "'";
		// updateAssault("[finish] Set final data for this assault, sql: " + sql);
		try {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			System.err.println(sql);
			e.printStackTrace();
		}
	}

	public static void updateAssault(String s, boolean addMessage)
	{
		addMessage = false;
		if (addMessage) {
			Statement stmt = Database.createStatement();
			long time = System.currentTimeMillis() - startTime;
			try {
				stmt.executeUpdate("UPDATE "
								+ tablePrefix
								+ "assault SET "
								+ "gentime = '"
								+ time
								+ "', "
								+ "message = CONCAT("
								+ (addMessage ? "case when message is null then '' else CONCAT(message,' | ') end,"
										: "") + "'" + s.replaceAll("'", "''")
								+ "') " + "WHERE assaultid = '" + assaultid + "'");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void updateAssault(String s)
	{
		updateAssault(s, false);
	}
	
	public static String formatPower(double i, double sum)
	{
		return decFormatter.format(i) + (sum > 0 ? " <sup><span class='rep_quantity_damage_low'>" + Math.round(i * 100 / sum) +"%</span></sup>" : "");
	}

	public static String generateKey(int length) {
		String sKey = "";
		long r1 = random.nextLong();
		long r2 = random.nextLong();
		String hash1 = Long.toHexString(r1);
		String hash2 = Long.toHexString(r2);
		sKey = hash1 + hash2;
		if (sKey.length() > length) {
			sKey = sKey.substring(0, length);
		}
		return sKey.toLowerCase();
	}
	
	public static int clampVal(int i, int a, int b)
	{
		if(i < a) return a;
		if(i > b) return b;
		return i;
	}

	public static long clampVal(long i, long a, long b)
	{
		if(i < a) return a;
		if(i > b) return b;
		return i;
	}

	public static double clampVal(double i, double a, double b)
	{
		if(i < a) return a;
		if(i > b) return b;
		return i;
	}
	
	public static double clampDbVal(double  i)
	{
		return Math.floor(i);
		// return clampVal(i, -useMaxIntValue, useMaxIntValue);
		// return i;
	}

	public static int randInt(int min, int max)
	{
		int size = max - min;
		return size > 0 ? min + random.nextInt(size+1) : min;
	}
	
	public static int randExclude(int maxExclude)
	{
		return maxExclude > 0 ? random.nextInt(maxExclude) : maxExclude;
	}

	public static long randExclude(long maxExclude)
	{
		return maxExclude > 0 ? Math.abs(random.nextLong()) % maxExclude : maxExclude;
	}

	public static double randDouble(double min, double max) {
		return random.nextDouble() * (max - min) + min;
	}
}
