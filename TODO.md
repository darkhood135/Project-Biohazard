# Project Biohazard — To-Do & Parked Ideas

A running list of things to build, fix, or revisit. Newest priorities near the top.

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

## Now / Next — goals (in order)

1. **Vial potions + brewing stand.** Vial variants that carry potion effects, brewed in a brewing stand.
   Will involve brewing recipe registration (check NeoForge's brewing API in the jar) + a potion-carrying
   vial representation. Bigger feature — may span multiple sittings.
2. **Injections / syringes.** Use a vial-potion to craft a syringe; on hitting an entity with it, apply the
   stored status effect for a long duration. Custom item with on-hit-entity behavior (`hurtEnemy` /
   attack hook) that reads the stored effect and applies it. Depends on #1 (needs vial potions first).
3. **Hatchet cooldown applies to ALL hatchets in inventory** — stop multi-hatchet parry abuse (swapping
   hatchets to parry constantly = near-immortal). Likely uses the 1.21.2+ cooldown-GROUP system: give the
   hatchet a cooldown-group component and call the `addCooldown(Identifier groupId, int)` overload (we saw
   it in the jar) instead of `addCooldown(ItemStack,...)`, so the cooldown covers the whole group.

## Backlog
- Optional polish: collapse the two-component design to one via a custom `SelectItemModelProperty`
  that reads `herb_vial_combination` directly (removes the custom_model_data redundancy). Advanced; later.
- Optional: per-herb `ChatFormatting` colors on the tooltip names.
- Build-order-realism textures (Option B, 34 textures) if ever wanted — additive, not a rewrite.

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
