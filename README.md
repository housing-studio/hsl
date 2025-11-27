# Housing Scripting Language (HSL)

**Hypixel Housing editor with superpowers.**

HSL is a powerful scripting language frontend for [Hypixel Housing](https://hypixel.fandom.com/wiki/Housing), allowing you to write clean, structured code that compiles into native Hypixel in-game actions.

Instead of fighting with the in-game UI, write expressive scripts that generate housing actions and logic — all with syntax inspired by modern programming languages.

---

## ✨ Features

- 🧠 Simple, expressive syntax with type inference
- 🛠️ Custom functions, variables, and events
- 🔁 Built-in support for loops, timers, and conditionals
- 📦 Code-to-Housing compiler that turns your logic into real Housing actions
- 💬 String interpolation, item manipulation, stats tracking, and more

---

## 🚀 Example

```rust
@loop(1m30s)
fn myTimerTask() {
  giveItem(Material::Stone, slot=Slot::Hand)

  stat stones: int
  stones++

  chat($"You have total {stones} stone blocks")
}
```

This script gives the player a stone block every 90 seconds, tracks how many they've received, and announces it in chat.

---

## 📥 Installation

> **Note**: HSL is in active development. Expect bugs, and not all features to be fully implemented.

Use the experimental installer to set everything up on your local machine in order to use HSL.

Please refer to the [installation page](https://docs.housing-studio.org/documentation/getting-started/installer).

---

## 📚 Documentation

Full documentation is available at [docs.housing-studio.org](https://docs.housing-studio.org/).

For additional examples and source code, see:

- [examples/](https://github.com/housing-studio/hsl/tree/master/examples)
- [src/](https://github.com/housing-studio/hsl/tree/master/src/main)
---

## 🤝 Contributing

We welcome contributions!

If you'd like to report bugs, suggest features, or open a pull request, please read our [CONTRIBUTING.md](./CONTRIBUTING.md) and [CODE_OF_CONDUCT.md](./CODE_OF_CONDUCT.md) first.

---

## 🧠 Why HSL?

Hypixel Housing offers powerful in-game logic tools, but the experience can be limiting when building larger or more complex projects. HSL gives you:

- A **clean programming environment** that compiles directly into Hypixel Housing actions.
- **Custom syntax** that simplifies logic and avoids repetitive clicking.
- The ability to **version control** your creations, collaborate, and scale your builds.

---

## 📌 Status

> 🚧 HSL is a work in progress. Expect breaking changes as we iterate toward a stable version.

We're currently focusing on:

- [x] Core syntax & parser
- [x] Compiler backend to Hypixel actions
- [x] Robust error messages
- [x] Testing & documentation

---

## 📄 License

[GPL-3.0](./LICENSE)

---

## ❤️ Acknowledgements

Made with love by [Housing Studio](https://github.com/housing-studio).

Special thanks to the Hypixel community for inspiring creative tools like this.
