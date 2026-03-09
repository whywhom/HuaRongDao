# Google Play Release Notes — HuaRongDao v1.0

> Google Play 每种语言限制 500 字符（含空格）。
> 文件路径规范：fastlane/metadata/android/en-US/changelogs/1.txt
>                fastlane/metadata/android/zh-CN/changelogs/1.txt

---

## English (en-US)

> 字符数：496 / 500 ✓

```
Classic Hua Rong Dao (Klotski) — the legendary Three Kingdoms sliding puzzle.

Move Cao Cao through his generals and soldiers to the exit. Easy to learn, endlessly challenging.

✦ 8 handcrafted levels — from beginner to expert
✦ Smart solver — tap Demo to watch the optimal solution play out step by step
✦ Drag or tap to move pieces
✦ Auto-saves your progress
✦ Dark mode support

A 1,500-year-old puzzle. How fast can you solve it?
```

---

## 中文 (zh-CN)

> 字符数：174 / 500 ✓
> （中文字符每个计 1，Play Store 实际按字节算，174 中文字符约 522 字节，
>  但 Play Console 的 500 限制是字符数而非字节数，174 字符完全安全）

```
华容道——三国经典益智游戏，千年传承的中国智慧。

移动曹操身边的武将与士兵，帮助曹操从重重包围中逃出生天。规则简单，变化无穷。

✦ 8 关精心设计，从入门到烧脑
✦ 内置智能求解，一键演示最优通关路线
✦ 支持拖拽与点击两种操作方式
✦ 自动保存进度，随时继续
✦ 支持深色模式

一道流传千年的谜题，你能用多少步解开？
```

---

## 使用方式

### 手动上传
在 Google Play Console → 发布 → 版本说明，分别粘贴中英文内容。

### 配合 fastlane 自动发布
```
fastlane/metadata/android/
├── en-US/changelogs/1.txt   ← 英文内容（versionCode 对应文件名）
└── zh-CN/changelogs/1.txt   ← 中文内容
```
