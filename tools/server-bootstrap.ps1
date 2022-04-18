$project_dir = Split-Path -Parent $PSScriptRoot
$server_dir = "$project_dir/server"

if (!(Test-Path -Path "$server_dir/paper.jar" -PathType Leaf)) {
  Write-Output "Server JAR not found. Downloading latest version from PaperMC..."

  $endpoint = "https://papermc.io/api/v2"
  $project_name = "paper"
  
  $current_endpoint = "$endpoint/projects/$project_name"
  $paper_data = Invoke-RestMethod -Uri $current_endpoint
  $paper_versions = $paper_data.versions
  $latest_version = $paper_versions[$paper_versions.Count - 1]
  
  $current_endpoint = "$current_endpoint/versions/$latest_version"
  $version_data = Invoke-RestMethod -Uri $current_endpoint
  $version_builds = $version_data.builds
  $latest_build = $version_builds[$version_builds.Count - 1]
  
  $current_endpoint = "$current_endpoint/builds/$latest_build"
  $build_data = Invoke-RestMethod -Uri $current_endpoint
  $download_name = $build_data.downloads.application.name
  
  $current_endpoint = "$current_endpoint/downloads/$download_name"
  Invoke-WebRequest -Uri $current_endpoint -OutFile "$server_dir/paper.jar"   
}

Set-Location -Path $server_dir
java -jar paper.jar nogui
