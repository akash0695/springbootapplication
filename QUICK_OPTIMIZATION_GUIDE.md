# Quick Asset Optimization Guide

## 🚀 Quick Start

### Step 1: Analyze Current Asset Sizes
```batch
optimize-assets.bat
```

This will show you:
- All image files and their sizes
- Which files need optimization (>200KB)
- CSS/JS file sizes and minification status
- Total asset size

### Step 2: Optimize Images

**Option A: Automatic (if ImageMagick installed)**
1. Install ImageMagick: https://imagemagick.org/script/download.php
2. Run `optimize-assets.bat` again
3. Choose 'Y' when prompted to optimize

**Option B: Manual (Online Tools)**
1. Go to https://tinypng.com/ or https://squoosh.app/
2. Upload images larger than 200KB
3. Download optimized versions
4. Replace original files

**Recommended Settings:**
- Quality: 80-85%
- Max width: 1920px (hero images), 1200px (content images)

### Step 3: Rebuild Application
```batch
mvnw.cmd clean package
```

## ✅ What's Already Optimized

- ✅ Lazy loading added to below-the-fold images
- ✅ Memory settings optimized (384MB max heap)
- ✅ Connection pool optimized (5 max connections)
- ✅ Hibernate query cache optimized

## 📊 Expected Results

After optimization:
- **Image size reduction**: 40-60%
- **Application JAR size**: 30-50% smaller
- **Memory usage**: Reduced by ~50%
- **Load time**: Faster initial page load

## 🎯 Priority Files to Optimize

Focus on these first:
1. Images in `images/slide/` (hero images)
2. Images in `images/services/` (service images)
3. Images in `images/projects/` (project thumbnails)

## 💡 Tips

- **Before adding new images**: Always optimize them first
- **Use WebP format**: 25-35% better compression (with JPEG fallback)
- **Check file sizes**: Keep images under 200KB when possible
- **Monitor JAR size**: Compare before/after optimization

## 🔧 Troubleshooting

**Script won't run?**
- Right-click `optimize-assets.bat` → Run as Administrator
- Or run: `powershell -ExecutionPolicy Bypass -File optimize-assets.ps1`

**Images still too large?**
- Reduce image dimensions (width/height)
- Lower quality to 75-80%
- Convert to WebP format

**Build fails?**
- Make sure all image paths in HTML are correct
- Check for missing image files
