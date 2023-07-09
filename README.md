# EpicParagliders
A compat mod to add the Paraglider's stamina system to Epic Fight. Obviously, a lot of this code is derived from
both [Epic Fight](https://www.curseforge.com/minecraft/mc-mods/epic-fight-mod) and 
[Paragliders](https://www.curseforge.com/minecraft/mc-mods/paragliders), therefore a lot of credit is owed to both of those teams. I've spent several
sleepless nights trying to make these two mods play nice together because I think the marriage of the two would
be a fantastic concept for myself and hopefully a few others.

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

## V0.0.1
- Both mods now use the Paraglider's stamina system
- Basic attacks now consume a set amount of stamina based on the attack speed of a weapon (will modify later to be more
   balanced with all weapons. Currently, weapons like axes consume WAY too much stamina).