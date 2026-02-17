# Asset Optimization Script for Spring Boot Application
# This script analyzes and optimizes images and other assets

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Asset Optimization Script" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

$staticPath = "src\main\resources\static"
$imagesPath = "$staticPath\images"

# Function to get file size in KB
function Get-FileSizeKB {
    param($file)
    return [math]::Round($file.Length / 1KB, 2)
}

# Analyze image files
Write-Host "Analyzing image files..." -ForegroundColor Yellow
$imageFiles = Get-ChildItem -Path $imagesPath -Recurse -Include *.jpg,*.jpeg,*.png,*.gif,*.webp -File
$totalImageSize = 0

Write-Host "`nImage Files Found:" -ForegroundColor Green
Write-Host ("{0,-60} {1,15} {2,10}" -f "File Name", "Size (KB)", "Status") -ForegroundColor Gray
Write-Host ("-" * 85) -ForegroundColor Gray

foreach ($img in $imageFiles) {
    $sizeKB = Get-FileSizeKB $img
    $totalImageSize += $sizeKB
    $status = if ($sizeKB -gt 200) { "LARGE" } elseif ($sizeKB -gt 100) { "MEDIUM" } else { "OK" }
    $color = if ($sizeKB -gt 200) { "Red" } elseif ($sizeKB -gt 100) { "Yellow" } else { "Green" }
    Write-Host ("{0,-60} {1,15:N2} {2,10}" -f $img.Name, $sizeKB, $status) -ForegroundColor $color
}

Write-Host ("-" * 85) -ForegroundColor Gray
Write-Host ("{0,-60} {1,15:N2} KB" -f "Total Image Size:", $totalImageSize) -ForegroundColor Cyan
Write-Host ""

# Analyze CSS files
Write-Host "Analyzing CSS files..." -ForegroundColor Yellow
$cssFiles = Get-ChildItem -Path "$staticPath\css" -Include *.css -File -ErrorAction SilentlyContinue
$totalCSSSize = 0

if ($cssFiles) {
    Write-Host "`nCSS Files:" -ForegroundColor Green
    foreach ($css in $cssFiles) {
        $sizeKB = Get-FileSizeKB $css
        $totalCSSSize += $sizeKB
        $isMinified = $css.Name -like "*.min.css"
        $status = if ($isMinified) { "MINIFIED" } else { "NOT MINIFIED" }
        Write-Host ("  {0,-50} {1,10:N2} KB - {2}" -f $css.Name, $sizeKB, $status) -ForegroundColor $(if ($isMinified) { "Green" } else { "Yellow" })
    }
    Write-Host ("  Total CSS Size: {0:N2} KB" -f $totalCSSSize) -ForegroundColor Cyan
}

# Analyze JS files
Write-Host "`nAnalyzing JavaScript files..." -ForegroundColor Yellow
$jsFiles = Get-ChildItem -Path "$staticPath\js" -Include *.js -File -ErrorAction SilentlyContinue
$totalJSSize = 0

if ($jsFiles) {
    Write-Host "`nJavaScript Files:" -ForegroundColor Green
    foreach ($js in $jsFiles) {
        $sizeKB = Get-FileSizeKB $js
        $totalJSSize += $sizeKB
        $isMinified = $js.Name -like "*.min.js"
        $status = if ($isMinified) { "MINIFIED" } else { "NOT MINIFIED" }
        Write-Host ("  {0,-50} {1,10:N2} KB - {2}" -f $js.Name, $sizeKB, $status) -ForegroundColor $(if ($isMinified) { "Green" } else { "Yellow" })
    }
    Write-Host ("  Total JS Size: {0:N2} KB" -f $totalJSSize) -ForegroundColor Cyan
}

# Summary
Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "Summary" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ("Total Images: {0:N2} KB" -f $totalImageSize) -ForegroundColor White
Write-Host ("Total CSS: {0:N2} KB" -f $totalCSSSize) -ForegroundColor White
Write-Host ("Total JS: {0:N2} KB" -f $totalJSSize) -ForegroundColor White
Write-Host ("Grand Total: {0:N2} KB ({1:N2} MB)" -f ($totalImageSize + $totalCSSSize + $totalJSSize), (($totalImageSize + $totalCSSSize + $totalJSSize) / 1024)) -ForegroundColor Cyan
Write-Host ""

# Recommendations
Write-Host "Recommendations:" -ForegroundColor Yellow
Write-Host "1. Optimize images larger than 200KB using tools like:" -ForegroundColor White
Write-Host "   - ImageMagick (magick convert input.jpg -quality 85 -resize 1920x1080> output.jpg)" -ForegroundColor Gray
Write-Host "   - Online tools: TinyPNG, Squoosh, ImageOptim" -ForegroundColor Gray
Write-Host "2. Convert images to WebP format for better compression" -ForegroundColor White
Write-Host "3. Minify non-minified CSS/JS files" -ForegroundColor White
Write-Host "4. Consider lazy loading for images" -ForegroundColor White
Write-Host ""

# Check for ImageMagick
$magickPath = Get-Command magick -ErrorAction SilentlyContinue
if ($magickPath) {
    Write-Host "ImageMagick found! Would you like to optimize images now? (Y/N)" -ForegroundColor Green
    $response = Read-Host
    if ($response -eq "Y" -or $response -eq "y") {
        Write-Host "`nOptimizing images..." -ForegroundColor Yellow
        $optimizedCount = 0
        foreach ($img in $imageFiles) {
            if ((Get-FileSizeKB $img) -gt 100) {
                $backupPath = $img.FullName + ".bak"
                Copy-Item $img.FullName $backupPath
                $outputPath = $img.FullName
                
                # Optimize based on file type
                if ($img.Extension -match "\.(jpg|jpeg)$") {
                    & magick $backupPath -quality 85 -strip -interlace Plane $outputPath
                } elseif ($img.Extension -eq ".png") {
                    & magick $backupPath -quality 85 -strip $outputPath
                }
                
                $originalSize = Get-FileSizeKB (Get-Item $backupPath)
                $newSize = Get-FileSizeKB (Get-Item $outputPath)
                $savings = $originalSize - $newSize
                $percent = ($savings / $originalSize) * 100
                
                if ($savings -gt 0) {
                    Write-Host ("  Optimized: {0} - Saved {1:N2} KB ({2:N1}%)" -f $img.Name, $savings, $percent) -ForegroundColor Green
                    Remove-Item $backupPath
                    $optimizedCount++
                } else {
                    Copy-Item $backupPath $outputPath -Force
                    Remove-Item $backupPath
                }
            }
        }
        Write-Host "`nOptimized $optimizedCount images." -ForegroundColor Green
    }
} else {
    Write-Host "ImageMagick not found. Install it from: https://imagemagick.org/script/download.php" -ForegroundColor Yellow
    Write-Host "Or use online tools to optimize images manually." -ForegroundColor Yellow
}

Write-Host "`nDone!" -ForegroundColor Green
