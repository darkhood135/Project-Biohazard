# Project Biohazard

> A *Resident Evil*–inspired survival-horror overhaul for Minecraft, built from scratch on **NeoForge 26.1** and **GeckoLib 5.5**.

![Minecraft](https://img.shields.io/badge/Minecraft-26.1-62B47A)
![Loader](https://img.shields.io/badge/NeoForge-26.1-orange)
![GeckoLib](https://img.shields.io/badge/GeckoLib-5.5-red)
![Java](https://img.shields.io/badge/Java-25-blue)
![License](https://img.shields.io/badge/License-MIT-green)
![Status](https://img.shields.io/badge/status-in%20development-yellow)

Project Biohazard reworks Minecraft's combat and survival loop around the tension of classic survival horror: scarce healing, dedicated save rooms, item-based counterplay, and enemies you can never quite be sure are actually dead. It's a ground-up content mod centered on a deeply simulated infected enemy, an item-driven medical system, and *RE*-style progression mechanics.

This is an active personal project used as a testbed for advanced Minecraft modding techniques — custom animation systems, server-authoritative entity state machines, event-driven mechanics, and persistent world state.

---

## Featured System — The T-Virus Zombie

The flagship of the mod is a fully custom entity that replaces the standard "walk at the player, swing" mob with a layered behavioral simulation inspired by the *RE1–3* zombies.

- **Uncertain death — corpse & revival.** Instead of vanishing on death, a downed zombie collapses into a persistent **corpse** and may — with no telegraph — twitch back to life. Revival is chance-based (up to twice per zombie) with weighted delay tiers ranging from seconds to minutes, so you're never certain a body is finished. Setting a corpse on fire denies the revival permanently, making fire a deliberate, resource-costing answer to the RNG.
- **Reaction-driven combat.** Damage produces layered, weighted reactions — an additive head *flinch* overlaid on whatever the zombie is currently doing, and a full-body *stagger* (with knockback and a stun window) once accumulated damage crosses a randomized threshold. The zombies are otherwise knockback-immune, so positioning and timing matter.
- **Timed melee.** A custom attack goal drives pursuit and a committed, animation-timed strike — damage lands on a specific frame, with a recovery window afterward, and randomized swing variations.
- **Perfect-parry counterplay.** Integrates with a hatchet parry system: a frame-perfect parry guarantees a stagger and lands knockback that bypasses the zombie's normal immunity, rewarding precise defense.
- **Atmospheric behavior.** Bone-level head-tracking, "notice" reactions on target acquisition, escalating groans during pursuit, and additive arm-reach overlays as it shambles toward you.
- **Randomized variants.** Each zombie rolls a weighted appearance variant on spawn (synced and saved), built on an extensible framework designed to later drive per-variant sounds and animations.

---

## Survival Systems

- **Medical & herbs** — a First Aid Spray with heal-over-time, durability, and separate cone-spray / self-heal modes, plus an herb-and-vial healing line (herbs, syringes, potion vials) inspired by the *RE* herb system.
- **Typewriter save rooms** — *RE*-style save points that protect a snapshot of your inventory against death, tied to a physical, destructible save location.
- **Permanent progression** — consumable upgrades that raise maximum health, alongside custom status effects (Adrenaline, Constitution).
- **Loot barrels** — breakable containers with scattered, physics-driven drops.
- **The Hatchet** — a melee weapon anchoring the parry system, with distinct perfect / normal parry outcomes.

---

## Technical Highlights

The interesting engineering, for anyone reading the code:

- **Advanced GeckoLib 5.5 animation.** Layered *additive* animation controllers for overlays (head flinches, mouth/groans, independent arm reaches) composited over a base locomotion controller; triggered one-shot animations; bone-level pose manipulation via a custom renderer hook; and per-entity texture variants driven through GeckoLib's render-state data-ticket system.
- **Server-authoritative design with client sync.** Gameplay logic runs on the server and networks to clients via `SynchedEntityData`, NBT-persisted entity state, and networked animation triggers, with careful client/server separation to avoid desync.
- **Custom entity state machines.** The corpse → revival → resurrection lifecycle is a persistent state machine that survives world saves and reloads, with dynamically swapped hitboxes, collision/push behavior, and animation states per phase.
- **Event-driven mechanics.** Death is intercepted via `LivingDeathEvent` (canceling vanilla death to enter the corpse state) and the parry system hooks the incoming-damage event — decoupling behavior from the vanilla combat pipeline.
- **Custom AI.** A hand-written pathfinding/attack goal implementing pursuit and a frame-accurate melee state machine with commitment and recovery windows.
- **Full content pipeline.** Custom particles, sounds, data components, entity attachments, enchantments, status effects, keybindings, network packets, and datagen.

---

## Tech Stack

**Java 25** · **NeoForge 26.1** · **GeckoLib 5.5** · models & animations authored in **Blockbench**

---

## Project Status

Early development (`v0.0.1`). The combat, damage-reaction, corpse, and revival systems are complete and functional. On the roadmap: grab / lunge / bite attacks with item-based counterplay, a sensory layer (sound investigation), obstruction handling (wall-breaking), corpse feasting, and a world-scaling difficulty framework.

---

## Building

Requires **JDK 25**.

```bash
./gradlew build          # build the mod jar (see build/libs)
./gradlew runClient      # launch a dev client
```

---

## License & Credits

Released under the **MIT License**.

Created by **DarkHood135**. Built with [NeoForge](https://neoforged.net/) and [GeckoLib](https://github.com/bernie-g/geckolib). Not affiliated with or endorsed by Capcom or the *Resident Evil* franchise; this is a non-commercial fan project.
