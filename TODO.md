# Project Biohazard — To-Do & Parked Ideas

A running list of things to build, fix, or revisit. Newest priorities near the top.

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
