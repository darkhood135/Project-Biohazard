# Project Biohazard — To-Do & Parked Ideas

A running list of things to build, fix, or revisit. Newest priorities near the top.

## Now / Next — Herb Vial system (in progress)

**Done so far:**
- `HERB_VIAL_COMBINATION` data component (String, canonical sorted key e.g. "gyb").
- `Herb` enum inside `VialItem` (key char, `unique` flag, translation key; `parse`/`toKey`/`fromKey`).
- Dynamic `getName` + `appendHoverText` tooltip (reads the component; guard on `!isEmpty()`, not null).
- Texture system: **Option A** chosen — one texture per *mixture* (20 total: 8 two-herb + 12 three-herb),
  build-order ignored. Sorted keys stay simple. Two-component design:
  `herb_vial_combination` (gameplay/name/tooltip) + `custom_model_data` (texture select), kept in sync.
- `items/glass_vial.json` uses `minecraft:select` on `custom_model_data` strings. Do NOT point it at
  the custom component — no built-in select property reads arbitrary components.

**Next steps (in order):**
1. **Crafting recipes — automate via datagen.** Write a loop in `ModRecipeProvider` that enumerates all
   legal mixtures (size 2-3, Red<=1, Blue<=1) and emits shapeless recipes: empty vial + N herbs -> filled
   vial, AND 2-herb vial + 1 herb -> 3-herb vial. The recipe output must set BOTH components from one
   `toKey` result (gameplay key + matching custom_model_data) so texture/name stay in sync.
   - Consider whether a single custom dynamic `CraftingRecipe` (like SuspiciousStewRecipe) is cleaner than
     N generated static recipes — revisit the trade-off when we start.
2. **Drinkable effects.** Vial becomes consumable; drinking applies effects based on the herbs inside
   (parse the component -> List<Herb> -> apply per-herb effects). Reuse the `finishUsingItem` + server-guard
   pattern from `GreenHerbItem`. No more 1-herb vials, so every vial is a 2-3 herb blend.

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
