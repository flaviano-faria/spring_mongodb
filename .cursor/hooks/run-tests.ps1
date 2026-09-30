# stop hook: runs the Maven test suite when mark-tests-pending.ps1 flagged a code change,
# and sends failures back to the agent as a follow-up message.
$ErrorActionPreference = 'Stop'
[Console]::OutputEncoding = New-Object System.Text.UTF8Encoding $false

$root = Resolve-Path (Join-Path $PSScriptRoot '..\..')
$stateDir = Join-Path $root '.cursor\hooks\.state'
$marker = Join-Path $stateDir 'tests-pending'
$log = Join-Path $stateDir 'last-test-run.log'

function Send-Result([string]$followup) {
    if ($followup) {
        Write-Output (@{ followup_message = $followup } | ConvertTo-Json -Compress)
    } else {
        Write-Output '{}'
    }
    exit 0
}

try {
    $payload = [Console]::In.ReadToEnd() | ConvertFrom-Json
} catch {
    Send-Result $null
}

if ($payload.status -ne 'completed' -or -not (Test-Path $marker)) {
    Send-Result $null
}

cmd /c "docker info >nul 2>&1"
if ($LASTEXITCODE -ne 0) {
    # The marker stays so the suite runs once Docker is back; only notify on the first stop of a conversation.
    if ([int]$payload.loop_count -gt 0) { Send-Result $null }
    Send-Result ("[test hook] Source files changed, but Docker is not running, so the Testcontainers suite " +
        "(.\mvnw.cmd test) could not run. Tell the user the tests were NOT verified and that they should " +
        "start Docker Desktop and run .\mvnw.cmd test. Do not claim the tests passed.")
}

if (-not $env:JAVA_HOME -and (Get-Command java -ErrorAction SilentlyContinue)) {
    $javaHomeLine = cmd /c "java -XshowSettings:properties -version 2>&1" | Where-Object { $_ -match '^\s*java\.home\s*=' }
    if ($javaHomeLine) { $env:JAVA_HOME = ($javaHomeLine -split '=', 2)[1].Trim() }
}

Push-Location $root
try {
    cmd /c "mvnw.cmd -B test > .cursor\hooks\.state\last-test-run.log 2>&1"
    $exitCode = $LASTEXITCODE
} finally {
    Pop-Location
}

if ($exitCode -eq 0) {
    Remove-Item $marker -Force
    Send-Result $null
}

$summary = Get-Content $log -Encoding UTF8 |
    Where-Object { $_ -match '^\[ERROR\]\s+\S' -or $_ -match '^\[INFO\] (Tests run:|BUILD)' } |
    ForEach-Object { if ($_.Length -gt 300) { $_.Substring(0, 300) + '...' } else { $_ } } |
    Select-Object -First 40
if (-not $summary) { $summary = Get-Content $log -Encoding UTF8 -Tail 30 }

Send-Result ("[test hook] .\mvnw.cmd test failed after your changes (exit code $exitCode). " +
    "Fix the production code or the tests so the suite passes. Follow the unit test policy in AGENTS.md: " +
    "do not delete or weaken assertions, add @Disabled, or skip tests.`n`n" +
    "Summary:`n" + ($summary -join "`n") + "`n`nFull log: .cursor/hooks/.state/last-test-run.log")
