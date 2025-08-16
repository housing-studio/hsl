# Hypixel Studio Language

<strong>Hypixel Housing editor with superpowers.</strong>

HLS is a scripting language frontend for Hypixel Housing, that converts code to hypixel actions.

```rust
@loop(1m30s)
fn myTimerTask() {
  giveItem(Material::Stone, slot=Slot::Hand)

  stat player stones: int
  stones++
  
  chat($"You have total {stones} stone blocks")
}
```
