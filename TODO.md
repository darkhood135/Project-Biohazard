# Project Biohazard — To-Do & Parked Ideas

A running list of things to build, fix, or revisit. Newest priorities near the top.

## Now / Next
- Continue course lessons and keep adding core content (items, blocks, mechanics).

## Backlog
- _(add as they come up)_

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
