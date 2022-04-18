# One Chunk World

Survive and gather resources, all in the confinement of just one chunk. Think you can do it? Inspired by Call Me Kevin on [YouTube](https://www.youtube.com/c/callmekevin).

Compatible with [Spigot](https://spigotmc.org/) and [PaperMC](https://papermc.io/). Should be compatible with other Bukkit-compatible servers, but I haven't tested.

## Install

Just drop the compiled JAR file on your `plugins/` folder.

## Usage

### `/getmonitoringstatus`

- Description: Prints information of the plugin status.
- Usage: `/<command>`
- Permission: `onechunkworld.getmonitoringstatus`

### `/setchunk`

- Description: Sets the selected chunk according to the parameters received.
- Usage: `/<command> (<player>|<level_name>) [<chunk_x>] [<chunk_z>] [<height>]`
- Permission: `onechunkworld.setchunk`

### `/togglechunk`

- Description: Toggles the player detection of their location to prevent them from leaving the chunk.
- Usage: `/<command>`
- Permission: `onechunkworld.togglechunk`

### `/togglesound`

- Description: Toggles the sound effect played when a player leaves the selected chunk
- Usage: `/<command>`
- Permission: `onechunkworld.togglesound`

## License

[MIT](LICENSE)
