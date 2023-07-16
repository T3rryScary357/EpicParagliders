# EpicParagliders
A compat mod to add the Paraglider's stamina system to Epic Fight. Obviously, a lot of this code is derived from
both [Epic Fight](https://www.curseforge.com/minecraft/mc-mods/epic-fight-mod) and 
[Paragliders](https://www.curseforge.com/minecraft/mc-mods/paragliders), therefore a lot of credit is owed to both of those teams. I've spent several
sleepless nights trying to make these two mods play nice together because I think the marriage of the two would
be a fantastic concept for myself and hopefully a few others.

## Required Mod Versions
- [Epic Fight](https://www.curseforge.com/minecraft/mc-mods/epic-fight-mod/files/4018756) (1.18.2)
- [Paragliders](https://www.curseforge.com/minecraft/mc-mods/paragliders/files/4478246) (1.18.2)

## How Do You Make It Work?
More than likely this mod won't work straight out of the gate. 
1. Paraglider's default stamina system must be disabled in the serverconfig folder.
   - That can be found here: **'saves/<YOUR_WORLD_NAME>/serverconfig/paraglider-server.toml'**. 
   - Simply set the **paraGlidingConsumesStamina** and **runningAndSwimmingConsumesStamina** both to **false**.
   - **NOTE**: Everything else in this config file can be whatever you want. Those are the only two settings
     that need to be overridden for this mod to work. Everything else will be read as normal.

2. EpicParaglider's stamina system must be enabled. By default it will be, but if for whatever reason it is not,
   then check in the same directory for this **'saves/<YOUR_WORLD_NAME>/serverconfig/epicparaglider-server.toml'**.
   - Set whatever you want to consume stamina to be **true**.

3. Restart your game and the new settings should take effect!

## V0.0.4
- Changed logic for disabling EF stamina bar. Should now only display for half a second upon respawning.
- Added support for the passive skills Technician, Energized Guard, and Active Guard (need to finish stamina pillager).
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