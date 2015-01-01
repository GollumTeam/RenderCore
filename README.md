RenderCore
==========

This is a coremod for MC 1.8 that adds rendering hooks that are no longer available with the 1.7 -> 1.8 update.

### Why not add this to Forge?
Well, we tried to. With [this](https://github.com/MinecraftForge/MinecraftForge/pull/1596), [this](https://github.com/MinecraftForge/MinecraftForge/pull/1597), and [this](https://github.com/MinecraftForge/MinecraftForge/pull/1600). None were accepted *(yet)*.

### Why provide the hooks if Lex doesn't want them available at all?
It's controversial. Some people think they need them. Personally, out of everything here, I think the only thing that I need is the TEISR hook, as without it, all TESR-only rendered blocks are rendered as Vanilla chests.
However, some people think they need all the hooks. So, rather than have 30 mods ASMing in the same hook, this is my attempt to centralize them all.

### What will others think?
For the people that keep complaining about not having the hooks, this should shut them up *(hopefully)*.
For people who were adamant about not having these hooks, they will probably be mad, but I really don't care.

### What if there's a part that I want to disable?
Because I know that incompatibilities exist, or I know that people may just want to turn an aspect of the mod off, I've offered a way to disable parts of the mod.
Because a config isn't made available until after two of three hooks need to be injected, I use system properties (VM Args) to control the turning off of parts of the mod.

* `-DrenderCore.disableWorldRendering` disables the IWorldRenderer (World rendering hooks).
* `-DrenderCore.disableItemRendering` disables the IDynamicItemModel (Item rendering hooks).
* `-DrenderCore.disableTEISRRendering` disables the TileEntity ItemStack rendering hooks.

### I'm a mod dev. How do I use this?
First off, because of the nature of this, this mod is under the public domain. You own it as much as I do. Do literally whatever you want with this. Repackage it. Close source it. Claim it's your original work. Make money off it. I don't care.
However, the purpose this project was to have one centralized place for all these hooks, which will help to reduce the amount of ASM. Therefore, if people shaded/repackaged this, it would lose that point.
Therefore, I *recommend* to mod devs that they have a hard dep on this mod, to help to keep the ASMing down. You are free to depload the mod if you wish. Just know the consequences.

1. Download the mod from this GitHub repo. (I *might* put it on CurseForge later.)
2. Deobfuscate it using [BON2](https://github.com/Parker8283/BON2). *plug* The only reason I'm not offering a deobfed build is because, now with 1.8, mappings can be inconsistent. I know that AbrarSyed has talked about adding auto-deobfing of mods when added to your `build.gradle`, so hopefully that's added soon.
3. Put the mod in the `./libs` directory of your workspace.
4. Setup your workspace and your IDE files.
5. Use the mod. Javadocs are supplied.
6. Build your mod. FG should find the mod in your `./libs` folder without needing to touch your `build.gradle`

*Ta-da*

### I'm a mod dev. Can this be on a maven?
I will look into putting this on a maven once ForgeGradle has the auto-deobfing things that AbrarSyed talked about adding.

### I'm a mod dev. I already have the same hooks you do.
Talk to me about it. I'm in a lot of IRC rooms. Or open an issue here on GitHub.
