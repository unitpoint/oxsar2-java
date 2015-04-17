/**
 * NetAssault Oxsar http://netassault.ru, http://oxsar.ru Copyright (c)
 * 2009-2010 UnitPoint <support@unitpoint.ru>
 */
package assault;

public enum EUnit {

    METALMINE(1),
    SILICON_LAB(2),
    HYDROGEN_LAB(3),
    SOLAR_PLANT(4),
    HYDROGEN_PLANT(5),
    ROBOTIC_FACTORY(6),
    NANO_FACTORY(7),
    SHIPYARD(8),
    METAL_STORAGE(9),
    SILICON_STORAGE(10),
    HYDROGEN_STORAGE(11),
    RESEARCH_LAB(12),
    SPYWARE(13),
    COMPUTER_TECH(14),
    GUN_TECH(15),
    SHIELD_TECH(16),
    SHELL_TECH(17),
    ENERGY_TECH(18),
    HYPERSPACE_TECH(19),
    COMBUSTION_ENGINE(20),
    IMPULSE_ENGINE(21),
    HYPERSPACE_ENGINE(22),
    LASER_TECH(23),
    ION_TECH(24),
    PLASMA_TECH(25),
    IGN(26),
    EXPO_TECH(27),
    GRAVI(28),
    SMALL_TRANSPORTER(29),
    LARGE_TRANSPORTER(30),
    LIGHT_FIGHTER(31),
    STRONG_FIGHTER(32),
    CRUISER(33),
    BATTLE_SHIP(34),
    FRIGATE(35),
    COLONY_SHIP(36),
    RECYCLER(37),
    ESPIONAGE_SENSOR(38),
    SOLAR_SATELLITE(39),
    BOMBER(40),
    STAR_DESTROYER(41),
    DEATH_STAR(42),
    ROCKET_LAUNCHER(43),
    LIGHT_LASER(44),
    STRONG_LASER(45),
    ION_GUN(46),
    GAUSS_GUN(47),
    PLASMA_GUN(48),
    SMALL_SHIELD(49),
    LARGE_SHIELD(50),
    INTERCEPTOR_ROCKET(51),
    INTERPLANETARY_ROCKET(52),
    ROCKET_STATION(53),
    MOON_BASE(54),
    STAR_SURVEILLANCE(55),
    STAR_GATE(56),
    MOON_ROBOTIC_FACTORY(57),
    TERRA_FORMER(58),
    REPAIR_FACTORY(100),
    DEFENSE_FACTORY(101);

    int value;

    EUnit(int value) {
        this.value = value;
    }
}
