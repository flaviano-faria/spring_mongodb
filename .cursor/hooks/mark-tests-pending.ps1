# afterFileEdit hook: flags that the test suite must run when the agent edits code or the build.
$ErrorActionPreference = 'Stop'

$root = Resolve-Path (Join-Path $PSScriptRoot '..\..')
$stateDir = Join-Path $root '.cursor\hooks\.state'
$marker = Join-Path $stateDir 'tests-pending'

try {
    $payload = [Console]::In.ReadToEnd() | ConvertFrom-Json
    $path = [string]$payload.file_path

    if ($path -match '[\\/]src[\\/](main|test)[\\/]' -or $path -match '[\\/]pom\.xml$') {
        New-Item -ItemType Directory -Force -Path $stateDir | Out-Null
        Set-Content -Path $marker -Value $path -Encoding UTF8
    }
} catch {
    [Console]::Error.WriteLine("mark-tests-pending: $_")
}

Write-Output '{}'
exit 0
