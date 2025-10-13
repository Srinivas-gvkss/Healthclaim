# Git Push Guide - Healthcare Insurance Application

## Overview
This guide will help you push your code to Git while keeping `node_modules`, build files, and sensitive data excluded.

---

## ✅ What's Already Protected

### **Root `.gitignore` (NEW)**
Created comprehensive `.gitignore` that excludes:
- ✅ `node_modules/` (all directories)
- ✅ `target/` (Maven build files)
- ✅ `*.class` (compiled Java files)
- ✅ `.idea/`, `.vscode/` (IDE files)
- ✅ `.env`, `*.key`, `*.keystore` (sensitive files)
- ✅ `.DS_Store`, `Thumbs.db` (OS files)
- ✅ `logs/`, `*.log` (log files)
- ✅ And many more...

### **Existing `.gitignore` Files**
- `backend/.gitignore` - Java/Spring Boot exclusions
- `frontend/.gitignore` - React Native/Expo exclusions
- `backend/user-service/.gitignore` - Service-specific exclusions

---

## 📋 Step-by-Step Push Instructions

### **Step 1: Review Changes**
```bash
# See what will be committed
git status

# See detailed changes
git diff
```

### **Step 2: Add All Changes**
```bash
# Add all tracked and untracked files
git add .

# OR add specific files/folders
git add .gitignore
git add MOBILE_APP_FIXES_SUMMARY.md
git add frontend/
git add backend/
```

### **Step 3: Verify No Sensitive Files**
```bash
# Check staged files (should NOT see node_modules, target, .env, etc.)
git status

# If you see sensitive files, unstage them:
git reset HEAD path/to/sensitive/file
```

### **Step 4: Commit Changes**
```bash
# Commit with a descriptive message
git commit -m "Fix mobile app issues and add comprehensive gitignore"

# OR use a detailed message
git commit -m "Mobile app fixes:
- Fixed phone field mapping bug
- Added AuthContext for state management
- Created role-based dashboards
- Added environment configuration
- Fixed navigation structure
- Updated comprehensive .gitignore"
```

### **Step 5: Push to Remote**
```bash
# Push to main branch
git push origin main

# OR if you're on a different branch
git push origin <branch-name>
```

---

## 🔍 Verify What Will Be Pushed

### **Check Ignored Files**
```bash
# See what's being ignored
git status --ignored

# Check if specific file is ignored
git check-ignore -v node_modules/
git check-ignore -v frontend/node_modules/
git check-ignore -v backend/user-service/target/
```

### **Check Tracked Files**
```bash
# List all files that will be committed
git ls-files

# Check if node_modules is tracked (should return nothing)
git ls-files | grep node_modules

# Check if target is tracked (should return nothing)
git ls-files | grep target
```

---

## ⚠️ Files That Should NEVER Be Committed

### **Already Excluded by `.gitignore`**
- ❌ `node_modules/` - All dependencies
- ❌ `target/` - Maven build files
- ❌ `*.class` - Compiled Java files
- ❌ `.env` - Environment variables
- ❌ `*.key`, `*.keystore` - Security keys
- ❌ `application-local.properties` - Local config with passwords
- ❌ `.idea/`, `.vscode/` - IDE settings
- ❌ `logs/`, `*.log` - Log files
- ❌ `.expo/` - Expo cache
- ❌ `build/`, `dist/` - Build outputs

### **Safe to Commit**
- ✅ Source code (`.js`, `.java`, `.tsx`)
- ✅ Configuration templates (`.example` files)
- ✅ Documentation (`.md` files)
- ✅ `package.json`, `pom.xml` - Dependency definitions
- ✅ `application.properties` - Non-sensitive config
- ✅ Git files (`.gitignore`, `.gitattributes`)

---

## 🚨 Emergency: If You Accidentally Committed Sensitive Files

### **Remove from Git History**
```bash
# Remove file from git but keep local copy
git rm --cached path/to/sensitive/file

# Commit the removal
git commit -m "Remove sensitive file from tracking"

# Push changes
git push origin main
```

### **Remove node_modules if Accidentally Committed**
```bash
# Remove from git
git rm -r --cached node_modules
git rm -r --cached frontend/node_modules
git rm -r --cached backend/user-service/target

# Commit
git commit -m "Remove build files and dependencies from tracking"

# Push
git push origin main
```

---

## 📊 Current Git Status

### **Modified Files**
- Backend services (AuthController, User entity, etc.)
- Frontend screens (converted from TypeScript to JavaScript)
- Configuration files (application.properties, etc.)
- Documentation (README, etc.)

### **New Files**
- `MOBILE_APP_FIXES_SUMMARY.md` - Fix summary
- `frontend/src/config/environment.js` - Environment config
- `frontend/src/contexts/AuthContext.js` - Auth state management
- `frontend/src/navigation/` - Navigation files
- `frontend/src/screens/Dashboard/` - Dashboard screens
- Database migrations (V8, V9)

### **Deleted Files**
- Old TypeScript files (`.tsx`)
- Unused documentation files
- Old frontend files

---

## 🎯 Recommended Commit Strategy

### **Option 1: Single Commit (Simple)**
```bash
git add .
git commit -m "Mobile app fixes and improvements"
git push origin main
```

### **Option 2: Organized Commits (Better)**
```bash
# Commit 1: Backend changes
git add backend/
git commit -m "Backend: Update auth service and add new migrations"

# Commit 2: Frontend fixes
git add frontend/
git commit -m "Frontend: Fix phone field bug and add AuthContext"

# Commit 3: Documentation
git add *.md
git commit -m "Docs: Add mobile app fixes summary and guides"

# Commit 4: Root config
git add .gitignore
git commit -m "Config: Add comprehensive .gitignore"

# Push all commits
git push origin main
```

---

## 📝 Pre-Push Checklist

- [ ] Reviewed `git status` - no sensitive files
- [ ] Checked `node_modules` not in commit
- [ ] Checked `target/` not in commit
- [ ] Checked `.env` files not in commit
- [ ] Verified `.gitignore` is updated
- [ ] Wrote meaningful commit message
- [ ] Tested app still works locally
- [ ] Backend services running
- [ ] Frontend app running
- [ ] No breaking changes

---

## 🔄 Create a New Branch (Optional)

If you want to push to a new branch instead of main:

```bash
# Create and switch to new branch
git checkout -b feature/mobile-app-fixes

# Add and commit
git add .
git commit -m "Mobile app fixes and improvements"

# Push to new branch
git push origin feature/mobile-app-fixes

# Create pull request on GitHub/GitLab
```

---

## 📦 What's Included in This Push

### **Backend**
- ✅ User service updates
- ✅ Auth controller improvements
- ✅ Database migrations (V8, V9)
- ✅ Security config updates
- ❌ Build files (excluded)
- ❌ Compiled classes (excluded)

### **Frontend**
- ✅ All source code (`.js` files)
- ✅ Navigation structure
- ✅ Auth context
- ✅ Dashboard screens
- ✅ Components
- ✅ Services
- ❌ `node_modules/` (excluded)
- ❌ `.expo/` cache (excluded)
- ❌ Build outputs (excluded)

### **Documentation**
- ✅ README files
- ✅ API documentation
- ✅ Setup guides
- ✅ Fix summary

### **Configuration**
- ✅ `package.json`
- ✅ `pom.xml`
- ✅ `application.properties` (non-sensitive)
- ✅ `.gitignore`
- ❌ `.env` files (excluded)
- ❌ Local config (excluded)

---

## 🎉 Quick Push (One Command)

If you've reviewed everything and are ready:

```bash
git add . && git commit -m "Mobile app fixes: AuthContext, navigation, phone field bug, environment config, dashboards" && git push origin main
```

---

## ✅ Post-Push Verification

After pushing, verify on GitHub/GitLab:

1. Go to your repository
2. Check file tree - should NOT see:
   - `node_modules/`
   - `target/`
   - `.env` files
   - `.idea/` or `.vscode/`
   - `*.class` files
3. Verify new files are there:
   - `frontend/src/config/environment.js`
   - `frontend/src/contexts/AuthContext.js`
   - `MOBILE_APP_FIXES_SUMMARY.md`

---

## 🆘 Common Issues

### **Issue: "Everything up-to-date"**
```bash
# Make sure you have changes
git status

# If no changes, create one
echo "test" >> test.txt
git add test.txt
git commit -m "Test commit"
git push
```

### **Issue: "Permission denied"**
```bash
# Set up SSH key or use HTTPS with personal access token
git remote -v  # Check remote URL
git remote set-url origin https://github.com/yourusername/repo.git
```

### **Issue: "Merge conflict"**
```bash
# Pull first
git pull origin main

# Resolve conflicts
# Then push
git push origin main
```

---

## 📞 Need Help?

- Check git status: `git status`
- See what's ignored: `git status --ignored`
- Verify file is ignored: `git check-ignore -v <filename>`
- See commit history: `git log --oneline`

---

**Ready to push? Follow the steps above!** 🚀

