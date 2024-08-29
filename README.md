# EpicParagliders
A compat mod to add the Paraglider's stamina system to Epic Fight. Obviously, a lot of this code is derived from
both [Epic Fight](https://www.curseforge.com/minecraft/mc-mods/epic-fight-mod) and 
[Paragliders](https://www.curseforge.com/minecraft/mc-mods/paragliders), therefore a lot of credit is owed to both of those teams. I've spent several
sleepless nights trying to make these two mods play nice together because I think the marriage of the two would
be a fantastic concept for myself and hopefully a few others.

## Install Instructions
Simply download the mod and the dependencies in the above section along with this mod, and you're golden!

## Notes
This mod creates a config file here `saves/<YOUR_WORLD_NAME>/serverconfig/epicparaglider-server.toml`.
Currently, this config file doesn't have anything you can change, but is a placeholder for a future update soon
that will have several config options for the amount of stamina you want different actions to drain.

## Support & Affiliations
Looking for a hosting service? Check out Kinetic Hosting!
[![CravenCraft.png](src%2Fmain%2Fresources%2Fassets.hostlink%2FCravenCraft.png)](https://billing.kinetichosting.net/aff.php?aff=559)
Use code **"CravenCraft"** to get your first month **_15%_** off!

## V0.7.0 - Latest Paraglider & Epic Fight Version support
- Updated the entire package system of the mod.
- Updated the properties of the mod to be a lot more organized.
- Updated Paragliders to V20.1.1

## V0.6.0 - Fixed Stamina Cost Support
- Added server config option for a fixed stamina cost for weapon types
- Fixed small bug that increases stamina cost when the attack_damage attribute is increased
- Rework now will allow for addon support for other types of weapon attacks (not skills yet)
- More efficient stamina cost calculation


## V0.5.1 - Bug Fixes & Attribute Support
- Fixed a bug with the Stamina Pillager skill consuming a massive amount of stamina
- Fixed a similar bug with the Technician skill
- Fixed a bug in the Emergency Escape skill that sometimes caused stamina to not be drained
- Fixed a small bug with basic attacks
- Fixed some bad math with basic attacks
- Fixed issue with Meteor slam drain more stamina from basic attacks when learned
- Added attribute support for various weapon types & skills

## V0.5.0 - The New Epic Fight Version Update
- Updated to the latest version of the [Epic Fight Mod](https://www.curseforge.com/minecraft/mc-mods/epic-fight-mod/files/4861783)
- All new skills added
- MASSIVE update to the server config
- Client side config now available as well
- Option to swap between the Paraglider stamina wheel and the Epic Fight stamina bar in the client config
- Huge restructure to all the underlying code of the mod. May even be more efficient now
- Added an option for an Elden Ring-like stamina system in the server config
- Fixed shields. They now drain an appropriate amount of stamina

## V0.4.0
- Added a config option to add a list of custom status effects with their respective strengths to the serverconfig file.
    The default is now Weakness and Mining Fatigue. Both of which can be changed to whatever the player wants in the file.
- Added light compatibility for **all** weapons. By default, they will drain their attack damage in stamina, which is
    still fairly low compared to others, but not bad out of the box.
- Updated the **epicparagliders-server.toml** file in the serverconfig. Players and server owners will need to delete the
    existing one for the new defaults to override.
- This mod now integrates with datapacks that add compatibility for **Epic Fight**. When creating/adding datapacks that 
    add weapon capabilities to **Epic Fight** from other mods, simply add an extra field **stamina_cost** to the item's
    json file. Will looks something like this:
```
{
    "type": "greatsword",
    "attributes": {
        "common": {
            "armor_negation": 10.0,
            "impact": 15.1,
            "max_strikes": 1,
            "damage_bonus": 30,
            "stamina_cost": 18
        }
    }
}
```
- Additionally, this mod now reads the **type** key (like the one listed above) and uses that to determine which
    weapon type stamina drain (defined in **epicparagliders-server.toml**) this item should drain from.

## V0.3.0
- Added exhaustion animations for whenever the player's stamina is fully depleted. Big thanks to bstylia14 for providing
    the animations!
- Added all new configs for each weapon type! Can be found in the serverconfig folder of your world save. The amount in
    the config will be directly multiplied to the amount of stamina that is drained from a weapon or skill. So, if the 
    config is set to 0.0 then no stamina is drained, if it's set to 0.5 then only half is drained, if it's 2.0 then
    double the stamina is drained, etc. 
- Modified the Active Guard skill a bit. Now perfect guards give you back a bit of stamina while missed guards drain
    30% more stamina than regular guards. Great for boss fights.
- Jump attacks now consume stamina.

## V0.2.0
- MASSIVE change in the mod's code. Using mixins (I finally learned how to use them) now to do almost everything.
- This mod should be a lot more optimized because of these changes.
- Code rework will make porting a LOT easier in the future. Especially with the BIG update EFM will have soon.
- Removed unnecessary logs that would constantly spam the console.
- Fixed a bug in multiplayer that would cause players to sometimes share stamina with other's.
- Attribute and config support coming in the next update soon!

## V0.1.1
- Using reflection we can directly modify the ModCfg file of Paragliders. This means no more restarting for my mod
    to override it! This mod is now plug and play!

## V0.1.0
- First official minor release!
- ~~Added a file check that will check for the paraglider-server.toml file in the 'severconfig' directory, and change
    the paragliding and running stamina drain attributes to false so that Epic Paragliders can automatically override
    it. This should eliminate the need for manually changing the config file upon first installing this mod, _hopefully_.~~

## V0.0.4
- Changed logic for disabling EF stamina bar. Should now only display for half a second upon respawning.
- Added support for the passive skills Technician, Energized Guard, Stamina Pillager, and Active Guard.
- Introduced a new system for multiple moves draining stamina at the same time (such as rolling right after an attack)
    that will drain almost exactly the proper amount of stamina needed regardless of when the actions take place. Uses
    the Triangular Number algorithm to do this.
- Using the above new system/algorithm introduced a new blue stamina "drain" bar that displays whenever the user performs
    an action that replenishes stamina (so far that is only the passive skill "Stamina Pillager").
- Added weight benefits to blocking (similar to how poise would work). The heavier the player is, the less stamina is 
    used to block an attack. Inversely, the heavier the player, the more stamina is used to dodge.
- Refactored how different guard drain stamina. Guarding now takes a larger initial stamina consumption amount while 
    draining only a small amount more with each consecutive block. Feels a bit more balanced, and skills like active
    guard and energizing guard still give good benefits with this new rework. Also, poise is factored into the algorithm
    as well.

## V0.0.3
- Fixed the rendering issue with the Epic Fight rendering engine conflicting with Paraglider's gliding animation.
  Huge shoutout to [Thunder](https://github.com/Thundertheidiot) for implementing a similar fix as to the one done in 
  the [DawnCraft-Tweaks](https://github.com/SmileycorpMC/DawnCraft-Tweaks/blob/master/src/main/java/com/afunproject/dawncraft/integration/epicfight/client/EpicFightParagliderEvents.java) mod.
- [Fixed a small bug](https://github.com/CravenCraft/EpicParagliders/commit/58aef081e8344c28da4568d77dbaf004301bd4ec#diff-228c4b34c9b6bb9d3dd5f8ac49b7521d6254e0f6042287053022bd6126bd3e12R124-R132)
  that allowed the player to attack while gliding if they were in **Battle** mode

## V0.0.2
- All Epic Fight skills, except for some Passive skills like Technician and (maybe) Knockdown Wakeup (still WIP),
  should now be compatible with Paraglider's stamina system.
- Do **NOT** use the **Technician** skill until support is added. Doing so will disable the dodge ability.
  If you do accidentally learn it, just open the Skills menu and assign it to something else
  (or use commands to unlearn it).
- Fixed various bugs causing stamina issues such as player death, dimension transport, skill learning/swapping.
- Stamina system is a bit more balanced with all weapons now. Takes in both attack delay time and weapon strength.
  So, higher tiered weapons such as diamond and netherite will drain a little more stamina, which should be balanced
  out by the player having a larger stamina wheel late game.
- Epic Fight's stamina bar has now been fully removed from the GUI. The only time it should show up is possibly
  sometimes upon respawning, but should disappear once it fills up if so.

## V0.0.1
- Both mods now use the Paraglider's stamina system
- Basic attacks now consume a set amount of stamina based on the attack speed of a weapon (will modify later to be more
  balanced with all weapons. Currently, weapons like axes consume WAY too much stamina).
