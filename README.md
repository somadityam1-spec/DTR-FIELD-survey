# DTR SURVEY - Field Inspection & Audit Web Application

[![Deploy to GitHub Pages](https://github.com/actions/deploy-pages/actions/workflows/pages.yml/badge.svg)](https://github.com)

**DTR SURVEY** is a fast, offline-ready web application designed for electrical distribution field engineers, supervisors, and linemen to record Distribution Transformer (DTR) health audits, LT structure status, consumer breakdown, and phase current measurements.

---

## 🚀 How to Deploy on GitHub Pages

Follow these simple 3 steps to host this web application for free on GitHub:

### Step 1: Push Code to GitHub
Push your repository to your GitHub account (on the `main` or `master` branch).

### Step 2: Enable GitHub Pages in Settings
1. Open your repository on **[GitHub.com](https://github.com)**.
2. Click on **Settings** (tab at the top right of your repo).
3. In the left sidebar, click on **Pages** (under the *Code and automation* section).
4. Under **Build and deployment**:
   - **Source**: Select **GitHub Actions** (recommended) OR select **Deploy from a branch** (`Branch: main`, `Folder: / (root)`).
5. Click **Save**.

### Step 3: Access Your Live Website
Once GitHub finishes building (takes ~30 seconds), your live website URL will be displayed:
```
https://<your-github-username>.github.io/<your-repo-name>/
```

---

## 🔧 Fix: Seeing an Old Version After Pushing to GitHub?

If your deployed website on GitHub Pages still shows the previous version or doesn't reflect your newest changes:

1. **Check GitHub Actions Tab**:
   - Go to your repository on GitHub and click the **Actions** tab.
   - Verify that the workflow `Deploy to GitHub Pages` is green (✅ Success). If it is in progress (🟡 Yellow), wait 30 seconds for it to finish.

2. **Verify Pages Source in GitHub Settings**:
   - Go to **Settings** ➔ **Pages**.
   - Under **Build and deployment** ➔ **Source**, ensure **GitHub Actions** is selected. (This uses the clean, instant `.github/workflows/pages.yml` deployment instead of the old legacy Jekyll builder).

3. **Bypass Browser Cache**:
   - Browsers aggressively save HTML files in cache.
   - In your browser on the live site, press **`Ctrl + Shift + R`** (Windows/Linux) or **`Cmd + Shift + R`** (Mac).
   - On mobile or desktop, you can also simply click the top header button: **`⚡ Check Update`** which clears cached assets and reloads the latest version with cache-busting parameters.

---

## ⚡ Key Features

1. **DTR Infrastructure Audit**:
   - DTR Code, Substation/Feeder location, Capacity (16 kVA to 1000 kVA), Mounting structure, and 1-tap **Live GPS coordinates**.

2. **DTR Structure Diagnostics**:
   - **LT Kiosk Status**: `Yes`, `No`, `Damaged`
   - **Lightning Arrester Status**: `Yes`, `No`, `Damaged`
   - **Male Female Status**: `Ok`, `Damaged`, `adjustment needed`
   - **Socket Status**: `Ok`, `Damaged`, `adjustment needed`
   - **Earthing**: `Yes`, `No`, `Damaged`
   - **DTR Cable Status**: `OK`, `damaged`
   - **LT Line Status**: `AB cable`, `Bare conductor`, `PVC cable`

3. **Consumer Breakdown**:
   - **Consumer Types**: `Village`, `IND`, `STW`, `COM`, `Single user`
   - Individual count tracking for **1 PH Consumer**, **IND**, **STW**, **3PH Consumer**, and automatic sum calculation.

4. **Real-time Load Analytics**:
   - Input phase currents ($I_R, I_Y, I_B, I_N$).
   - Automatic calculation of rated full-load current ($I_{rated} = \text{kVA} \times 1.3912$), percentage loading, and neutral imbalance alerts.

5. **Device User Profile**:
   - Save your Name and Designation on your own device; automatically tags all your inspection records.

6. **Instant Export & Field Dispatch**:
   - **Excel / CSV Export**: Instant spreadsheet download.
   - **WhatsApp 1-Tap Share**: Auto-formats a field report with emojis and metrics.
   - **Print / PDF**: Ready for hardcopy audit filing.
   - **Offline Persistence**: Uses `localStorage` to work seamlessly without internet connectivity.
