# GitHub Pages Setup Guide

This repository is configured to use GitHub Pages for documentation hosting.

## 🌐 Live Site

Once configured, your documentation will be available at:
**https://stiksy.github.io/advent-of-code-2025/**

## 📋 Setup Steps

Follow these steps to enable GitHub Pages for this repository:

### 1. Enable GitHub Pages

1. Go to your repository on GitHub: https://github.com/stiksy/advent-of-code-2025
2. Click on **Settings** (gear icon in the top menu)
3. Scroll down to the **Pages** section in the left sidebar
4. Under **Source**, select:
   - **Branch**: `main`
   - **Folder**: `/ (root)`
5. Click **Save**

### 2. Wait for Deployment

- GitHub will automatically build and deploy your site
- This usually takes 1-2 minutes
- You'll see a green checkmark and the URL when it's ready
- The URL will be: `https://stiksy.github.io/advent-of-code-2025/`

### 3. Verify Deployment

- Click on the **Actions** tab to see the deployment workflow
- Once complete, visit the site URL
- You should see your Advent of Code solutions documentation

## 📁 What's Configured

This repository includes:

- **`_config.yml`**: Jekyll configuration with Cayman theme
- **`index.md`**: Landing page (home page)
- **`docs/`**: Solution documentation with Jekyll front matter
  - `day01.md` - Day 1: Secret Entrance
  - `day02.md` - Day 2: Gift Shop
  - `day03.md` - Day 3: Lobby

## 🎨 Theme

The site uses the **Cayman** theme. You can change this in `_config.yml`:

Available themes:
- `jekyll-theme-cayman` (current - clean, modern)
- `jekyll-theme-minimal` (simple, sidebar navigation)
- `jekyll-theme-slate` (dark theme)
- `minima` (default Jekyll theme)

To change the theme, edit `_config.yml`:
```yaml
theme: jekyll-theme-minimal
```

## 🔄 Updating the Site

Every time you push to `main`, GitHub Pages will automatically rebuild the site. Changes typically appear within 1-2 minutes.

## 📝 Adding New Days

When you add a new day's solution:

1. Create `docs/dayXX.md` with this front matter:
```yaml
---
layout: default
title: Day X - Puzzle Name
nav_order: X
---
```

2. Update `index.md` to add the new day to the solutions table

3. Commit and push - the site will update automatically!

## 🐛 Troubleshooting

**Site not showing up?**
- Check Settings → Pages to ensure it's enabled
- Verify the branch is set to `main` and folder is `/ (root)`
- Check the Actions tab for build errors

**Styling looks wrong?**
- Wait a few minutes for the build to complete
- Try a hard refresh (Ctrl+Shift+R / Cmd+Shift+R)
- Check `_config.yml` for syntax errors

**404 errors?**
- Ensure file paths in links use `.html` extension (Jekyll converts `.md` to `.html`)
- Check that `baseurl` in `_config.yml` matches your repo name

## 📚 Resources

- [GitHub Pages Documentation](https://docs.github.com/en/pages)
- [Jekyll Documentation](https://jekyllrb.com/docs/)
- [Supported Themes](https://pages.github.com/themes/)
