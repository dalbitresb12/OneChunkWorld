$project_dir = Split-Path -Parent $PSScriptRoot

Copy-Item "$project_dir/target/OneChunkWorld.jar" "$project_dir/server/plugins"
