# Project Biohazard — To-Do & Parked Ideas

A running list of things to build, fix, or revisit. Newest priorities near the top.

## Dream features / long-term roadmap (Tyler's vision — sequenced by what unlocks the most)

All authentically RE and coherent. Ordered by dependency, not desire. **Entities are the big unlock** —
Tyler hasn't built custom entities yet, and doing so unlocks ~half this list.

**Foundational unlocks (build these first — they gate the rest):**
- **Custom entities** (the domain to learn next): registration, model/renderer, AI goals, spawning, sync.
  Learn on a **custom zombie** first, then everything below follows.
- **Infection system** (#13): staged/progressing infection via attachments+effects+events. MECHANIC is
  feasible now; the hallucination VISUALS are client-rendering (defer). Underpins upgrade-station infection
  resistance + the whole tone.
- **Dodge + special melee (#11):** same ability-code family as the parry. Feasible now. Together with
  infection, these unblock the parked **Upgrade Station**.

**Entity features (after the entity pipeline is learned, roughly in order):**
- Custom zombies/skeletons replacing base mobs (#4) — creepier tone, variants.
- RE4 Ganado (#5) — "smart zombies," patient AI that dodges/times strikes.
- Player-like corpses (#8), grab attacks (#12) — entity behavior + player-control lock.
- **Nemesis (#1) — the CAPSTONE and Tyler's whole reason for the mod.** Hyper-intelligent stalker;
  STARS-badge trigger, escalating appearances, defeatable once. Not literal AI — layered designed behaviors
  (predictive pathing, attacks on player-vulnerable frames, escalation states). Build LAST, when fluent.

**Feasible item systems (can slot in anytime):**
- Herb growing via lab equipment (#2) — blocks + growth mechanic (block entities / random tick).
- Chainsaw (#3) — brutal damage is easy now; limb-gib gore is client rendering (polish later).
- Guns (#6) — big but feasible (arrow-like projectiles + ammo + reload). Reload feeds STARS outfit.

**Hard / risky client-rendering tier (set expectations; the churny, least-documented area):**
- Flashlight (#7) — HARDEST ask; a real dynamic beam that isn't client-only is ~shader territory in MC.
  Realistic v1 = a held light that brightens the area; true beam is the stretch dream.
- Blood mechanics (#9) — particles easy, persistent decals/pooling need custom rendering.
- Clothing (#10) — EFFECTS easy (attachments/attributes: Leon jacket = +parry window; STARS = faster reload
  but higher Nemesis spawn rate). VISUAL (non-armor-looking) = advanced player-model rendering. It's a
  downstream INTEGRATION feature: its effects depend on guns (#6) and Nemesis (#1) existing first.

### Dream features — batch 2

**The spine (design throughline, not a discrete task):**
- **Gradual progression (helpless survivor -> apex predator, Leon RE2 -> RE9).** The organizing principle:
  upgrade/unlock state (attachments + gated abilities) + enemy tuning. Counterpart to Nemesis as the core.
  Most everything below hangs off it.

**Extends existing systems (feasible, low-risk, near-term):**
- Knives (#8) — parry-weapon variant; re-tune of the hatchet parry (briefer window, faster recharge).
- Coins/charms (#11, #12) — passive items granting effects while in inventory (one inventory-tick mechanism).
- Breakable barrels (#4) — block + loot table; ammo/pesetas drops wait on those items.
- Final hatchet upgrade (#9) — "sharpness" resource replacing durability + parry-charges; the progression reward.
- Grenades (#10) — thrown-item entity + explosion (snowball/egg pattern).

**Entity-dependent (needs the custom-entity arc first):**
- Enemy stagger-on-parry (#2) — lives ON the entity ("if parried -> stagger"). Head-kick (#2.2) layers on:
  stagger + player kick ability + progression gate. (Entities + abilities + progression converge here.)
- Merchant + Pesetas (#5) — currency item is trivial; the Merchant = custom trader entity + trade UI (first
  custom GUI, OR reuse the vanilla villager-trade screen = lower effort). Replaces the wandering trader.
- Barricade boards (#7) — board block easy; "zombies see through + break glass" = custom AI.
- Survivors replacing villagers (#14) — player-model NPC entities; build with the player-corpse idea.

**Direction decision:** Remove/replace vanilla mobs (#6) turns this from an ADDON into a TOTAL CONVERSION
(config/datapack toggle to keep vanilla). Coherent with the vision; identity-defining. Code = spawn cancel
+ config; the weight is design.

**Client-rendering tier (hard/churny — polish later):**
- Improved parry animation (#2.1) — custom player animation (the area that fought us on the parry swing).
- New third person (#3) — OTS camera; moderate client work, strong existing-mod precedent (Shoulder Surfing)
  — consider integrating vs reinventing.

**Compat:** Curios (#13) — make coins/charms work via inventory with NO dependency; offer Curios as an
optional soft-dep for dedicated accessory slots. Standalone-first, Curios as a bonus.

### Dream features — batch 3 (world & endgame tier; VERY long-term, Tyler's words)

Two big NEW domains here: **worldgen** (biomes + structures) and **boss entities**. This is the summit.

**Reachable sooner (extends existing / item-based):**
- Safe zones (#10) — formalize the light + no-hostiles check (already built for the safe-room music) into
  defined zones with mechanical benefits. Low-risk, near-term.
- STARS communication (#9) — walkie-talkie item found WITH the STARS badge: triggers Nemesis AND opens a
  narrative early-mid-game guide (message/guide delivery + writing). Doubles as onboarding. Feasible before
  the big structures.

**Biomes (#1-4) — natural first, urban is a major task:**
- Natural: woodland/swampy (#3, RE4/RE7), mountainous w/ houses (#4, RE8) — biome JSON: terrain, vegetation,
  fog/colors, spawns. Achievable.
- Urban: city (#1, RE2/3 Raccoon City), town (#2, RE1) — the "atmosphere" is GENERATED BUILDINGS = custom
  structure generation at scale. Much harder than a biome JSON.

**Landmark structures + bosses — the "raid tier" (each = big build + worldgen placement + a phase boss):**
- Infested building + giant spider boss (#6) — needs realistic spiders (#5, entity rework).
- Baker house + Jack/Marguerite + Eveline (#7).
- Raccoon City police station + Mr. X (lesser-Nemesis, biome-specific) -> Super Tyrant phase transition (#8).
- Castle Dimitrescu + Alcina (#11).
- These need entity mastery AND worldgen mastery — built LAST, the payoff of everything else. Bosses share
  tech with Nemesis (phases, escalation).

## Textures — RE4 block set (in progress)

Emulating Resident Evil 4 rural-village architecture (timber-framed stucco cottages). Guide-only
collaboration — analyze & advise, Tyler implements.

**Done so far:**
- Weathered Bricks (chimney + the source texture for exposed-brick-under-plaster). Recipe motif: bricks + gravel.
- Dirty Glass (grime in corners, olive tint, mostly-clear center). Recipe: glass + gravel.
- Stone Tiles roof — scalloped fish-scale via *clustered noise* (NOT banding; banding read as pipes),
  with a mossy variant + value variation. Separate block from the flat-noise floor Stone Tiles. Stair
  geometry provides the course lines, so the texture stays flat/non-directional.
- Plaster system: clean quiet **base** (weighted random variants + uvlock rotation), **Weathered** variant
  (bottom grime, rotation-locked so it can't flip up), **Dripping** variant (vertical streaks from the top
  under roofline/sills, rotation-locked). Base stays calm; variants + placement carry the weathering.

**To do:**
1. **Exposed Plaster** — plaster peeled showing Weathered Bricks. Hand-placed, FIXED orientation so brick
   courses stay horizontal; brick must match the chimney block. Recipe: Plaster + Weathered Brick (shapeless).
2. **Cracked Plaster** — hairline cracks / chipped corner, hand-placed decay. Recipe: Plaster + gravel
   (same "gravel = grime" motif as bricks/glass).
3. **Crafting recipes for plaster (all forms)** — base Plaster from lime-ish mats (e.g. clay + sand + bone
   meal); damage variants via the shapeless recipes above; add stonecutter paths (Plaster → each variant)
   for cheap decorative access.
4. **Different plaster colors** — the planned warm soot-brown INTERIOR tone as a sibling (matches the RE4
   interior fireplace-room reference); optionally other muted village tones (faded ochre/blue/green). Keep
   all of them low-contrast like the base so the system stays coherent.
5. **Custom crafting block for plaster blocks** — a dedicated workstation if the recipe set grows large;
   otherwise vanilla table + stonecutter already cover it. Decide once the recipe count is known.

**Ideas / polish (optional):**
- Plaster slab / stairs / wall shapes for windowsills, trim, and half-height detailing.
- Per-brick color variation on Weathered Bricks (a few darker/spalled bricks) to break the uniform grid.
- Keep the Dripping variant selective — only walls directly under the roofline, or it gets busy.

## Structure — RE4 village house as a generated structure (planned)

Goal: the RE4-style house spawns as a generated structure in a custom **dark / foggy / swampy forest
biome** (wicked gloomy, RE4 village mood), likely populated by **Ganado**. Cracked-plaster craze variant
is done and reading well (slightly too regular/lattice up close — optional future polish to break the grid).
First interior is largely Tyler's own design; the two original reference images were separate houses.

**Fiction to lock first:** lived-in vs abandoned. RE4 village houses were *occupied* (tidy-but-grim, lit) —
that's what the current build is, which is coherent. Best result = a MIX: some occupied, some rotting husks,
so the biome isn't uniform.

**To do:**
- **More house designs** — multiple footprints/layouts so the biome doesn't feel copy-pasted. Interior AND
  exterior variety to differentiate houses from each other.
- **Ruin-state variants** — reuse the existing decay blocks (heavy exposed brick, cracked plaster, missing
  railings, broken windows, collapsed roof corners, interior overgrowth) to make abandoned husks alongside
  the occupied houses. Same shell, different wear.
- **Generation variety** — jigsaw/modular approach (shuffle rooms + damage states) or several NBT variants,
  so repeated spawns don't read as copy-paste and kill the dread.
- **Tie house to biome** — moss/vines creeping up the plaster near the ground, stilts standing in murk, dead
  dark trees crowding it. Environment should bleed onto the structure.
- **Encounter staging for Ganado** — use the horror bones already there (railing/stairwell chokepoints, long
  table breaking sightlines, dark corners). Hear-before-you-see flow; something between player and the door.
  Lit typewriter save point inside = "brief safety in a hostile place" beat.
- **Biome atmosphere** — custom fog color, low light, swampy/dead-forest vegetation, ambient sound to sell it.

## DONE — Typewriter "Save State" (complete)

Directional-placed Blockbench block. Right-click with an Ink Ribbon = save: sets vanilla respawn
(`RespawnConfig` / `RespawnData.of`), consumes a ribbon, plays `typewriter_save` sound, snapshots the
inventory (`SAVE_SNAPSHOT` attachment, serialized, NO copyOnDeath = one-use), and stores the typewriter's
`GlobalPos` (`SAVE_TYPEWRITER_POS`). On death (`LivingDeathEvent`): if the typewriter is confirmed gone
(only when its chunk `isLoaded` — else assume present, no force-load) the save voids; otherwise protected
items (count-capped vs snapshot, matched **ignoring the `DAMAGE` component** so worn gear counts) are pulled
from the inventory before drops and stashed (`RESTORE_STASH`), then re-added on `PlayerEvent.Clone`. Anti-dupe
by moving items, not copying. Recipe: aluminum frame + Totem of Undying + Echo Shard + iron/redstone/paper.
Note: beds & the typewriter share one vanilla respawn point (last-write-wins for *location*); a bed does NOT
clear the item protection (separate systems) — intentional. Reach the server via `level().getServer()`.

**Next natural goal:** the Upgrade Station (the deferred 2nd typewriter idea) — but it's gated on dodge,
infection, special melees, and parry upgrades existing first. Those features ARE the roadmap toward it.

## DONE — Herb Vial system (complete)

Full feature shipped: single `glass_vial` item + `HERB_VIAL_COMBINATION` string component + `Herb` enum;
dynamic name/tooltip; 20 per-mixture textures via `minecraft:select` on `custom_model_data` (Option A);
~46 auto-generated recipes (direct + incremental, `DataComponentIngredient` for the input vial,
`ItemStackTemplate` + `DataComponentPatch` for component-bearing outputs); drinkable-only-when-filled via
`use()` gate; combination effects resolved in `HerbEffects` (red = amplifier), shared by `HerbItem` and
`VialItem` via composition (no inheritance). Reminder: `heal()` is in HEALTH POINTS (2 = 1 heart).

## DONE — Hatchet, parry & custom effects (complete)

Hatchet: faster/weaker than sword, cuts sword-tier blocks. Parry: right-click `use()` block stance
(0.5s window via `getTicksUsingItem`, perfect = first ~3 ticks, directional dot-product facing check,
flattened to horizontal). Negates damage (`LivingIncomingDamageEvent.setCanceled`, skips explosions),
knockback, durability ~1:1, cooldown scaled by damage (heavy >10 = 5s) showing the vanilla HUD overlay,
whiff penalty. Perfect parry = `player.attack()` counter + Adrenaline. Immunity effect: marker MobEffect
+ `MobEffectEvent.Applicable` blocks harmful at source + one-time cleanse helper (no per-tick scan).
Adrenaline (`AdrenalineEffect`, 3s on perfect parry): +30%/amp attack speed via effect attribute modifier,
+20%/amp crit damage via `CriticalHitEvent`. Verified-from-jar gotchas: `hurtServer(ServerLevel,...)`,
`ItemUseAnimation` (not UseAnim), `ENTITY_INTERACTION_RANGE`, `MobEffect` ctor is protected (needs subclass).

## DONE — vial potions, syringes, hatchet cooldown (complete)

Potion vials (water_vial + `potion_contents`, rides the vanilla brewing tree via `addContainer`, water-fill
from source + cauldron via an AT, potion-tint rendering). Honey vial (reuses vanilla `Foods`/`Consumables`).
Syringes: `SyringeItem` + `potion_contents`, melee `hurtEnemy` applies effect, separate empty-syringe item,
custom "Syringe Pierce" sound (3 random variants), dynamic `%s Syringe` names, gradual injection over 9s
(transient `SyringeInjection` attachment + `EntityTickEvent.Post`, 3 ramping charges, immunity/resistance
rejects, consumes for all attackers incl. mobs). Hatchet cooldown shared across all hatchets via a
cooldown-group `use_cooldown` component. Deferred from syringes: the "sticks in the entity like an arrow"
visual (needs a custom entity — Tyler's first entity project, for later).

## Backlog
- Optional polish: collapse the two-component design to one via a custom `SelectItemModelProperty`
  that reads `herb_vial_combination` directly (removes the custom_model_data redundancy). Advanced; later.
- Optional: per-herb `ChatFormatting` colors on the tooltip names.
- Build-order-realism textures (Option B, 34 textures) if ever wanted — additive, not a rewrite.
- Visual (future): a "being affected" shake on entities mid-injection (or other effects), like the
  zombie-villager curing shake. That shake is client-side render code tied to the entity's render state —
  pairs naturally with the deferred custom-entity / rendering work.
- Audio (future): "Secure" safe-room track (friend's RE2 safe-room cover, the `secure` sound) plays as
  ambient music when no monsters are nearby in a safe, lit area. Conditional music playback feature.

## Parked — revisit later

### EMF Visualizer: highlight applicable blocks through walls
**Status:** Parked intentionally — good idea, wrong time. Revisit after more reps with
client-side code, event subscribers, and ideally after the course covers rendering.

**Goal:** While holding the EMF Visualizer, draw a colored aura around applicable blocks
within RANGE, visible through walls.
- Green aura around `REDSTONE_BLOCK`
- Red aura around `DEACTIVATED_REDSTONE_BLOCK`

**Why parked:** This is world *rendering*, a separate subsystem from the game logic built
so far, and it's the most version-volatile part of modern Minecraft (the render API names
shift between versions). High frustration / low transferable payoff this early. It's polish,
not a foundation for the entities, mobs, effects, and blocks that come next.

**When picking it back up — the plan we sketched:**
1. Client-side only — lives in client event code (e.g. `ProjectBiohazardClient`), not `EMFItem`.
2. Scanner (throttled): on a client tick, while holding the item, scan a cube of RANGE around
   the player and cache a `List` of (BlockPos, color). Do NOT scan in the render loop.
3. Drawer: subscribe to `RenderLevelStageEvent` (stage ~`AFTER_TRANSLUCENT_BLOCKS`); each frame,
   draw a box per cached position while holding the item.
4. Big gotcha: rendering is camera-relative. Translate the PoseStack by the NEGATIVE camera
   position before drawing at world coords, or boxes appear in the wrong place.
5. Box helper: `LevelRenderer.renderLineBox(...)` style (may be renamed/moved to a
   `ShapeRenderer`-type class in 26.1 — verify).
6. Through walls: render with depth test disabled — ideally a custom `RenderType` with
   `NO_DEPTH_TEST` (copy vanilla debug/line render types).
7. Aura: start with a colored wireframe box; optionally add a slightly-inflated translucent
   filled box behind it for a cheap "glow." True bloom is out of scope.

**Build order:** scanner → one hardcoded wireframe box (nail camera-relative translate) →
feed cached list + color by type → swap to no-depth RenderType → optional translucent fill.

## Future item idea
- Consumable "grenade" variant of the EMF Visualizer that *temporarily* changes a block's
  state, then reverts. Note: "temporary" needs a revert timer — `level.scheduleTick(pos, block,
  delayTicks)` with the revert in the block's scheduled-tick callback, or a counting-down block
  entity. The `useOn` shape carries over; the timed revert is the new part.
  (Old `useOn` code is currently commented in `EMFItem.java` as a placeholder — move it into
  the new item's own class when you build this.)
