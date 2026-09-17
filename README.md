# Catch on Defeat

v1.6.1-1.5

[Modrinth](https://modrinth.com/mod/cobblemon-catch-on-defeat)

[CurseForge](https://www.curseforge.com/minecraft/mc-mods/cobblemon-catch-on-defeat)

[GitHub](https://github.com/timinc-cobble/cobblemon-catch-on-defeat)

## What if…

…Pokémon could be captured when defeated, instead of via PokéBall?

## Features

- Spawn specific Pokémon that can be defeated in order to be caught, or set it so that everybody can be “caught” this way.
- Optional confirmation screen when a defeated Pokémon wants to join your team.
- Optionally heal Pokémon that join the party through this mechanic.
- Optionally prevent regular captures, and make it so that Pokémon must be “caught” this way.
- Optionally connect/disconnect individual events regarding the joins, including capture for accepted join and release for rejected joins.

## Dependencies

- [Cobblemon](https://www.notion.so/Cobblemon-22157e0d4afd80a49896c70a775a3c7f?pvs=21)
- [Cobblemon Tim Core](https://www.notion.so/Tim-Core-22057e0d4afd809b9c02e78f26805376?pvs=21)
- [owo Lib](https://www.notion.so/owo-Lib-24957e0d4afd80a99b80d40fa16ffa40?pvs=21)

## Testing

With access to commands, run the following command in your world:

`/pokespawn aipom catch_on_defeat=yes`

Then, try throwing any old type of PokeBall at the Aipom that was spawned. By default, it will refuse to be caught and give you a feedback message saying you must defeat it to catch it. Get into battle with it, beat it up until it faints, and it’ll join your team (healed, by default).

## Player Help

[How it works](https://www.notion.so/How-it-works-24557e0d4afd803696f7e293cdafed86?pvs=21)

[Config Options](https://www.notion.so/Config-Options-24557e0d4afd80fb97abd56636c27a86?pvs=21)

## Addon Dev Help

### Data Pack Help

[Pokémon Custom Properties](https://www.notion.so/Pok-mon-Custom-Properties-24557e0d4afd80908ee2f95089348671?pvs=21)

### Resource Pack Help

[Translations](https://www.notion.so/Translations-24557e0d4afd8032a3ffeac565e8fe51?pvs=21)

[Resources](https://www.notion.so/Resources-24557e0d4afd80fc8deacff74fef78ea?pvs=21)

## Mod Dev Help

[Events](https://www.notion.so/Events-24557e0d4afd8007bd1ecfb6d17704bb?pvs=21)

## Known Issues

- As this uses Cobblemon Tim Core’s custom Pokémon properties logic, there is a known issue where loading up the first world in a single player session will not show the autocomplete for the custom Pokémon properties in commands. The property still works, it’s just the autocomplete that’s weird.
- v1.6.1-1.5.0 on Fabric had an overly-specific version requirement for Tim Core. Loosened it up to that or later in v1.6.1-1.5.1 on Fabric.
- v1.6.1-1.5.1 and below would still add the Pokémon to your PC even if you declined it, but had a full party, and had `rejectsCountAsRelease` turned on. v1.6.1-1.5.2 remedies this.

## Roadmap

If you’d like to keep up with the work being done on the mod, please join [the Discord](https://discord.com/invite/WKAR27SdSv) and subscribe to notifications on the channel for this content. You can also keep track of the to do list available on [the mod’s main page](https://www.notion.so/Catch-On-Defeat-21d57e0d4afd803b8458e012ecbfdc6c?pvs=21).

## Feedback

If you have any questions or requests concerning the mod, or just want to drop by and say hi, visit us over at [the Discord](https://discord.com/invite/WKAR27SdSv)!

## Support

If I've made something you enjoyed or helped you make something, please consider [dropping a tip in the cup](https://ko-fi.com/timsminecraftmods) and mention how I helped if you'd like!