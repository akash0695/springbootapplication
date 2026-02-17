# Asset Optimization Guide

This guide explains how to optimize images and other assets to reduce the application size.

## Quick Start

### 1. Run the Asset Analysis Script

```batch
optimize-assets.bat
```

This script will:
- Analyze all image files and show their sizes
- Identify large files (>200KB) that need optimization
- Check CSS/JS files for minification status
- Provide optimization recommendations

### 2. Manual Image Optimization

#### Option A: Using ImageMagick (Recommended)

1. Install ImageMagick from: https://imagemagick.org/script/download.php
2. Run the optimization script again - it will automatically optimize images if ImageMagick is installed

#### Option B: Using Online Tools

For images larger than 200KB, use these free online tools:
- **TinyPNG**: https://tinypng.com/ (for PNG/JPG)
- **Squoosh**: https://squoosh.app/ (Google's tool)
- **ImageOptim**: https://imageoptim.com/ (Mac only)

**Recommended Settings:**
- Quality: 80-85% (good balance between quality and size)
- Max width: 1920px (for full-width images)
- Max width: 1200px (for content images)
- Format: JPEG for photos, PNG for graphics with transparency

#### Option C: Using Command Line (if ImageMagick installed)

```batch
# Optimize a single image
magick convert input.jpg -quality 85 -strip -interlace Plane output.jpg

# Batch optimize all JPG images in images folder
for %f in (src\main\resources\static\images\**\*.jpg) do magick convert "%f" -quality 85 -strip -interlace Plane "%f"
```

### 3. Convert to WebP Format (Advanced)

WebP provides 25-35% better compression than JPEG/PNG:

```batch
# Convert to WebP (requires ImageMagick or cwebp tool)
magick convert input.jpg -quality 85 output.webp
```

**Note:** Update HTML files to use WebP with fallback:
```html
<picture>
  <source srcset="images/photo.webp" type="image/webp">
  <img src="images/photo.jpg" alt="Description">
</picture>
```

## Optimization Targets

### Images
- **Hero/Slider images**: Max 1920px width, 85% quality
- **Content images**: Max 1200px width, 85% quality
- **Thumbnails**: Max 400px width, 80% quality
- **Target size**: < 200KB per image

### CSS/JS
- Already minified files (`.min.css`, `.min.js`) are optimized
- Non-minified files are automatically compressed during build

## Build-Time Optimization

The Maven build process now includes:
- Automatic CSS/JS minification (excluding already minified files)
- Resource filtering for optimal packaging

## Current Asset Sizes

Run `optimize-assets.bat` to see current sizes and optimization opportunities.

## Best Practices

1. **Use appropriate image dimensions**: Don't use 4K images for thumbnails
2. **Choose the right format**: 
   - JPEG for photos
   - PNG for graphics with transparency
   - WebP for modern browsers (with fallback)
3. **Lazy load images**: Add `loading="lazy"` to img tags
4. **Use responsive images**: Use `srcset` for different screen sizes
5. **Optimize before adding**: Optimize images before adding them to the project

## Expected Size Reduction

After optimization, you can expect:
- **Images**: 40-60% size reduction
- **CSS/JS**: 20-30% size reduction (for non-minified files)
- **Total application size**: 30-50% reduction

## Monitoring

After optimization, rebuild and check the JAR size:
```batch
mvnw.cmd clean package
```

Compare the JAR size before and after optimization.
