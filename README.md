# ⚔ Hero's Chronicle

> Turn-based RPG game berbasis Java dengan GUI Swing — Tugas PBO

---

## Daftar Isi

- [Deskripsi Game](#deskripsi-game)
- [Struktur Folder](#struktur-folder)
- [Cara Menjalankan](#cara-menjalankan)
- [UML Class Diagram](#uml-class-diagram)
- [Penjelasan Class](#penjelasan-class)
    - [Entity](#1-entity--package-entity)
    - [Level](#2-level--package-level)
    - [Items](#3-items--package-items)
    - [Player](#4-player--package-player)
    - [Assassin](#5-assassin)
    - [Healer](#6-healer)
    - [Swordman](#7-swordman)
    - [Tank](#8-tank)
    - [Enemy](#9-enemy--package-enemy)
    - [SmallEnemy](#10-smallenemy)
    - [LargeEnemy](#11-largeenemy)
    - [BossEnemy](#12-bossenemy)
    - [GameState](#13-gamestate--package-battle)
    - [GameFrame](#14-gameframe--package-gui)
    - [UI](#15-ui--package-gui)
    - [MainMenuPanel](#16-mainmenupanel)
    - [CampaignPanel](#17-campaignpanel)
    - [BattlePanel](#18-battlepanel)
    - [ExplorationPanel](#19-explorationpanel)
    - [ShopPanel](#20-shoppanel)
    - [InventoryPanel](#21-inventorypanel)
- [Dependency Map](#dependency-map)
- [Fitur Game](#fitur-game)
- [Anggota](#anggota)

---

## Deskripsi Game

**Hero's Chronicle** adalah game turn-based RPG berbasis Java dengan tampilan GUI menggunakan Java Swing. Player dapat membentuk party berisi hingga 4 karakter dengan role berbeda untuk melawan musuh di berbagai mode permainan.

### Mode Permainan

| Menu | Deskripsi |
|---|---|
| **Campaign** | Lawan musuh di 6 stage bertingkat. Stage dikunci berdasarkan Exploration Points (EP) |
| **Exploration** | Lawan musuh bebas tanpa batas untuk farming EP dan Gold |
| **Shop** | Beli item spesial menggunakan Gold untuk memperkuat karakter |
| **Inventory** | Lihat stat semua karakter dan item yang dimiliki |

### Sistem Battle (Turn-based)

- Giliran player → pilih **Attack / Skill / Ultimate**
- Giliran musuh → menyerang secara otomatis
- **Attack** — gratis, damage dasar
- **Skill** — biaya **20 MP**, damage lebih besar atau efek khusus
- **Ultimate** — biaya **50 MP**, damage terbesar / efek area

---

## Struktur Folder

```
HeroChronicle/
├── Main.java                    ← Entry point
└── src/
    ├── entity/
    │   └── Entity.java          ← Abstract base class semua makhluk
    ├── level/
    │   └── Level.java           ← Sistem level & EXP karakter
    ├── items/
    │   └── Items.java           ← Data item yang bisa dibeli di Shop
    ├── player/
    │   ├── Player.java          ← Abstract class semua karakter player
    │   ├── Assassin.java        ← Role: DPS tinggi, HP rendah
    │   ├── Healer.java          ← Role: Support & healer party
    │   ├── Swordman.java        ← Role: DPS seimbang
    │   └── Tank.java            ← Role: Defender, HP & DEF tertinggi
    ├── enemy/
    │   ├── Enemy.java           ← Abstract class semua musuh
    │   ├── SmallEnemy.java      ← Musuh lemah (HP 80)
    │   ├── LargeEnemy.java      ← Musuh menengah (HP 220)
    │   └── BossEnemy.java       ← Musuh terkuat (HP 500)
    ├── battle/
    │   └── GameState.java       ← Singleton penyimpan data global game
    └── gui/
        ├── UI.java              ← Helper warna, font, dan komponen Swing
        ├── GameFrame.java       ← JFrame utama & CardLayout manager
        ├── MainMenuPanel.java   ← Layar menu utama
        ├── CampaignPanel.java   ← Layar pilih stage
        ├── BattlePanel.java     ← Layar pertempuran (turn-based)
        ├── ExplorationPanel.java← Layar exploration gratis
        ├── ShopPanel.java       ← Layar toko item
        └── InventoryPanel.java  ← Layar inventori & stat karakter
```

---

## Cara Menjalankan

### Prasyarat
- Java JDK 11 atau lebih baru
- IDE: IntelliJ IDEA (direkomendasikan), Eclipse, atau NetBeans

### Setup di IntelliJ IDEA

1. Buka IntelliJ → **New Project** → pilih **Java**
2. Hapus folder `src` bawaan IntelliJ
3. Copy semua isi folder `HeroChronicle/` ke project
4. Klik kanan folder `src` → **Mark Directory as → Sources Root**
5. Klik kanan `Main.java` → **Run 'Main.main()'**

### Compile via Terminal

```bash
# Masuk ke folder project
cd HeroChronicle

# Compile semua file Java
find src -name "*.java" > sources.txt
javac -d bin @sources.txt
javac -d bin Main.java -cp bin

# Jalankan
java -cp bin Main
```

---

## UML Class Diagram

```
                          ┌─────────────────────┐
                          │     «abstract»      │
                          │       Entity        │
                          │─────────────────────│
                          │ - nama: String      │
                          │ - tipe: String      │
                          │ - hp: double        │
                          │ - maxHp: double     │
                          │─────────────────────│
                          │ + getNama()         │
                          │ + getTipe()         │
                          │ + isAlive()         │
                          │ + attack()          │
                          │ + skill()           │
                          │ + ultimate()        │
                          └──────────┬──────────┘
                                     │ extends
                   ┌─────────────────┴─────────────────┐
                   │                                   │
       ┌───────────▽───────────┐           ┌───────────▽───────────┐
       │      «abstract»       │           │      «abstract»       │
       │        Player         │           │         Enemy         │
       │───────────────────────│           │───────────────────────│
       │ - mp: double          │           │ - atkPower: double    │
       │ - maxMp: double       │           │ - defPower: double    │
       │ - atkPower: double    │           │ - expReward: int      │
       │ - defPower: double    │           │ - goldReward: int     │
       │ - levelObj: Level     │           │───────────────────────│
       │ - inventory: List     │           │ + attack()            │
       │───────────────────────│           │ + skill()             │
       │ + tambahItem()        │           │ + ultimate()          │
       │ + addExp()            │           │ + getExpReward()      │
       │ + pulihkan()          │           │ + getGoldReward()     │
       └──────────┬────────────┘           └──────────┬────────────┘
                  │ extends                           │ extends
        ┌─────────┼─────────┐              ┌──────────┼──────────┐
        │         │         │              │          │          │
   ┌────▽──┐ ┌────▽──┐ ┌────▽──┐    ┌─────▽──┐ ┌────▽───┐ ┌────▽────┐
   │Assassin│ │Healer │ │Sword- │    │ Small  │ │ Large  │ │  Boss   │
   │       │ │       │ │ man   │    │ Enemy  │ │ Enemy  │ │  Enemy  │
   └───────┘ └───────┘ └───┬───┘    └────────┘ └────────┘ └─────────┘
                            │
                       ┌────▽──┐
                       │ Tank  │
                       └───────┘

  ┌────────────────┐     ┌────────────────┐     ┌────────────────┐
  │     Level      │     │     Items      │     │   GameState    │
  │────────────────│     │────────────────│     │────────────────│
  │ - level: int   │     │ - nama         │     │ - gold: int    │
  │ - exp: int     │     │ - price        │     │ - EP: int      │
  │ - multiplier   │     │ - damage       │     │ - party        │
  │────────────────│     │ - health       │     │ - shopItems    │
  │ + addExp()     │     │────────────────│     │────────────────│
  │ + upgradeLevel │     │ + addDamage()  │     │ + getInstance()│
  └────────────────┘     └────────────────┘     │ + buatMusuh()  │
                                                 └────────────────┘
         Player ──has-a──► Level
         Player ──has-a──► List<Items>
         GameState ──uses──► Player, Enemy, Items
```

---

## Penjelasan Class

### 1. `Entity` — `package entity`

**Class induk abstrak dari semua makhluk dalam game.** Karena `abstract`, tidak bisa dibuat objeknya langsung — harus di-extend oleh `Player` atau `Enemy`.

| Attribute | Tipe | Keterangan |
|---|---|---|
| `nama` | String | Nama makhluk |
| `tipe` | String | Role/jenis, misal "Assassin", "Boss Enemy" |
| `hp` | double | HP saat ini, berkurang saat diserang |
| `maxHp` | double | HP maksimum, untuk menghitung persentase bar HP |

| Method | Keterangan |
|---|---|
| `getNama()` | Mengembalikan nama makhluk |
| `getTipe()` | Mengembalikan tipe/role |
| `getHp()` / `setHp()` | Getter dan setter HP. `setHp` otomatis pastikan HP ≥ 0 |
| `isAlive()` | Cek apakah makhluk masih hidup (`hp > 0`) |
| `attack(Entity target)` | **Abstract** — wajib diimplementasi subclass |
| `skill(Entity target)` | **Abstract** — wajib diimplementasi subclass |
| `ultimate(Entity target)` | **Abstract** — wajib diimplementasi subclass |

---

### 2. `Level` — `package level`

**Mengelola sistem level dan EXP karakter.** Setiap objek `Player` memiliki satu objek `Level`.

| Attribute | Keterangan |
|---|---|
| `level` | Level saat ini, mulai dari 1 |
| `exp` | EXP yang sudah terkumpul di level ini |
| `multiplier` | Pengali damage. Level 1 = `1.0x`, Level 2 = `1.1x`, dst. |

| Method | Keterangan |
|---|---|
| `addExp(int tambahan)` | Tambah EXP. Jika cukup, otomatis panggil `upgradeLevel()` |
| `upgradeLevel()` | Naikkan level +1 dan multiplier +0.1 |
| `getExpNeeded()` | Hitung EXP yang dibutuhkan: `level × 50` |
| `getMultiplier()` | Dipakai `hitungDamage()` di Player |

---

### 3. `Items` — `package items`

**Merepresentasikan satu item yang bisa dibeli di Shop.** Setelah dibeli dan diberikan ke karakter, bonus ATK dan HP langsung ditambahkan secara permanen.

| Attribute | Keterangan |
|---|---|
| `nama`, `deskripsi` | Ditampilkan di Shop dan Inventory |
| `price` | Harga dalam Gold |
| `damage` | Bonus ATK yang ditambah ke karakter penerima |
| `health` | Bonus HP yang ditambah ke `maxHp` dan `hp` karakter |

| Method | Keterangan |
|---|---|
| `addDamage(double d)` | Tambah bonus damage item |
| `addHealth()` | Placeholder sesuai UML |

---

### 4. `Player` — `package player`

**Abstract class induk semua karakter yang dimainkan.** Extends `Entity`, menambahkan sistem MP, ATK, DEF, Level, dan Inventory.

| Attribute | Keterangan |
|---|---|
| `mp` / `maxMp` | Mana Points — dikurangi saat pakai Skill (–20) atau Ultimate (–50) |
| `atkPower` | Kekuatan serangan dasar, bertambah jika beli item |
| `defPower` | Pertahanan — mengurangi damage dari musuh |
| `levelObj` | Objek `Level` karakter ini |
| `inventory` | Daftar `Items` yang sudah diberikan ke karakter ini |

| Method | Keterangan |
|---|---|
| `hitungDamage(double base)` | `protected` — kalikan base damage dengan `levelObj.getMultiplier()` |
| `tambahItem(Items item)` | Tambah item ke inventory, langsung tambah bonus ATK dan HP |
| `addExp(int exp)` | Teruskan EXP ke `levelObj.addExp()` |
| `pulihkan()` | Reset `hp` dan `mp` ke maksimum sebelum battle |

---

### 5. `Assassin`

**DPS tinggi, HP rendah.** ATK 35 · HP 180 · MP 120

| Method | Skill | Keterangan |
|---|---|---|
| `attack()` | Shadow Strike | Damage = `hitungDamage(atkPower + 5)` |
| `skill()` | Blade Rush | Damage = `hitungDamage(atkPower × 1.8)` · biaya 20 MP |
| `ultimate()` | Death Mark | Damage = `hitungDamage(atkPower × 3.0)` · biaya 50 MP |

---

### 6. `Healer`

**Support, fokus menyembuhkan ally.** ATK 12 · HP 200 · MP 160

| Method | Skill | Keterangan |
|---|---|---|
| `attack()` | Holy Light | Damage kecil ke musuh |
| `skill(target)` | Holy Mend | Sembuhkan 1 ally: `hp += hitungDamage(50)` · biaya 20 MP |
| `ultimate()` | Divine Shield | Versi 1 target |
| `ultimateParty(List<Player>)` | Divine Shield | Sembuhkan **semua** ally: `hp += hitungDamage(80)` masing-masing · biaya 50 MP |

---

### 7. `Swordman`

**DPS seimbang.** ATK 28 · HP 240 · MP 90

| Method | Skill | Keterangan |
|---|---|---|
| `attack()` | Quick Slash | Damage = `hitungDamage(atkPower + 8)` |
| `skill()` | Blade Dance | Damage = `hitungDamage(atkPower × 2.0)` · biaya 20 MP |
| `ultimate()` | Tempest Slash | Damage = `hitungDamage(atkPower × 3.5)` · biaya 50 MP. Multiplier tertinggi! |

---

### 8. `Tank`

**Defender.** ATK 18 · HP 380 · DEF 35 · MP 70

| Method | Skill | Keterangan |
|---|---|---|
| `attack()` | Shield Bash | Damage sedang |
| `skill()` | Iron Fortress | **Bukan damage!** Tambah `defPower +15` permanen · biaya 20 MP |
| `ultimate()` | Earthbreaker | Damage ke 1 target · biaya 50 MP |
| `ultimateSemuaMusuh(List<Enemy>)` | Earthbreaker AoE | Serang **semua** musuh yang masih hidup · biaya 50 MP |

---

### 9. `Enemy` — `package enemy`

**Abstract class induk semua musuh.** Extends `Entity`. Berbeda dengan Player, Enemy tidak punya MP — semua serangan bisa langsung dipakai. Menyimpan reward EXP dan Gold.

| Attribute | Keterangan |
|---|---|
| `atkPower` / `defPower` | Kekuatan dan pertahanan musuh |
| `expReward` | EXP yang diterima player setelah musuh ini dikalahkan |
| `goldReward` | Gold yang diterima player setelah musuh ini dikalahkan |

| Method | Keterangan |
|---|---|
| `hitungDamage(base, target)` | `protected` — kurangi damage dengan DEF target: `Math.max(5, base – defTarget × 0.3)`. Minimal damage = 5 |

---

### 10. `SmallEnemy`

Musuh paling lemah. HP 80 · ATK 12 · DEF 6 · Reward: +20 EXP, +30 Gold

| Method | Nama | Multiplier |
|---|---|---|
| `attack()` | Serangan dasar | ×1.0 |
| `skill()` | Swarm Attack | ×1.5 |
| `ultimate()` | Frenzy Bite | ×2.0 |

---

### 11. `LargeEnemy`

Musuh menengah. HP 220 · ATK 22 · DEF 15 · Reward: +50 EXP, +70 Gold

| Method | Nama | Multiplier |
|---|---|---|
| `attack()` | Brute Slam | ×1.0 |
| `skill()` | Ground Smash | ×1.8 |
| `ultimate()` | Earthquake | ×2.5 |

---

### 12. `BossEnemy`

Musuh terkuat. HP 500 · ATK 35 · DEF 20 · Reward: +150 EXP, +200 Gold

| Method | Nama | Multiplier |
|---|---|---|
| `attack()` | Dark Claw | ×1.0 |
| `skill()` | Soul Drain | ×2.0 |
| `ultimate()` | Chaos Nova | ×3.5 |

---

### 13. `GameState` — `package battle`

**Singleton — hanya ada satu objek selama game berjalan.** Berfungsi sebagai pusat data global game. Semua panel GUI mengambil dan menyimpan data melalui class ini.

| Attribute | Keterangan |
|---|---|
| `gold` | Jumlah Gold player saat ini |
| `explorationPoint` | Total EP — menentukan stage yang terbuka |
| `currentStage` | Stage berikutnya yang belum diselesaikan |
| `party` | List 4 karakter: Raven, Lyria, Aldric, Gorrath |
| `shopItems` | Daftar item yang tersedia di toko |
| `monsterSlain` / `runs` | Statistik exploration |

| Method | Keterangan |
|---|---|
| `getInstance()` | Cara satu-satunya mendapat objek GameState. Buat baru jika belum ada |
| `buatMusuh(String config)` | Parse string `"small,large,boss"` menjadi `List<Enemy>` |
| `pulihkanParty()` | Panggil `pulihkan()` untuk semua karakter sebelum battle |

---

### 14. `GameFrame` — `package gui`

**JFrame utama dan manager navigasi antar layar.** Menggunakan `CardLayout` sehingga perpindahan menu instan tanpa membuka jendela baru.

| Method | Keterangan |
|---|---|
| `showPanel(String name)` | Tampilkan panel tertentu, panggil `.refresh()` agar data selalu terbaru |
| `startBattle(config, label, isExplore, ep, gold)` | Inisialisasi `BattlePanel` lalu pindah ke layar battle |

---

### 15. `UI` — `package gui`

**Helper class berisi konstanta warna, font, dan factory method komponen Swing.** Tujuannya agar semua panel memiliki tampilan yang konsisten tanpa mengulang kode styling.

| Method | Keterangan |
|---|---|
| `goldButton(String text)` | Buat `JButton` bertema emas |
| `actionButton(text, fg, border)` | Buat tombol aksi battle dengan warna kustom |
| `label(text, color, font)` | Buat `JLabel` siap pakai |
| `bar(val, max, color)` | Buat `JProgressBar` untuk HP/MP bar |
| `darkCard()` | Buat `JPanel` dengan background tema game |

---

### 16. `MainMenuPanel`

Layar menu utama. Menampilkan 4 tombol navigasi dan info Gold + EP terkini.

---

### 17. `CampaignPanel`

Menampilkan grid 6 stage. Setiap stage dicek statusnya:
- **Terkunci** — EP belum cukup
- **Terbuka** — bisa dimainkan
- **Cleared** — sudah selesai

| Method | Keterangan |
|---|---|
| `refresh()` | Rebuild grid berdasarkan EP terkini dari `GameState` |
| `buildStageCard(String[] data)` | Buat satu kartu stage dengan nama, reward, status, dan tombol mulai |

---

### 18. `BattlePanel`

**Panel terpanjang dan terkompleks.** Mengurus seluruh alur pertempuran turn-based.

| Method | Keterangan |
|---|---|
| `initBattle()` | Reset semua state, buat musuh dari config, render ulang UI |
| `playerAction(int aksi)` | Dipanggil saat klik Attack/Skill/Ultimate. Jalankan method karakter, tambah log, cek selesai |
| `giliranMusuh()` | Dijalankan otomatis via `javax.swing.Timer`. Setiap musuh yang hidup serang target acak |
| `cekBattleSelesai()` | Cek menang (semua musuh mati) atau kalah (semua party mati). Update `GameState` |
| `renderCombatants()` | Gambar ulang kartu HP/MP setiap karakter dan musuh |
| `addLog(String pesan)` | Tambah baris teks ke area log battle, auto-scroll ke bawah |

---

### 19. `ExplorationPanel`

| Method | Keterangan |
|---|---|
| `refresh()` | Update statistik EP, `monsterSlain`, `runs` dari `GameState` |
| `startExploration()` | Tentukan zona dan config musuh secara acak, panggil `GameFrame.startBattle()` dengan `isExplore=true` |

---

### 20. `ShopPanel`

| Method | Keterangan |
|---|---|
| `refresh()` | Tampilkan semua item dari `GameState.shopItems` beserta harga dan status Gold |
| `beliItem(Items item, Player target)` | Kurangi Gold, panggil `Player.tambahItem()`, refresh tampilan |

---

### 21. `InventoryPanel`

| Method | Keterangan |
|---|---|
| `refresh()` | Render kartu setiap karakter dari `GameState.party`: nama, level, HP, ATK, DEF, dan daftar item |

---

## Dependency Map

Urutan dari class yang paling mandiri hingga yang paling banyak bergantung:

```
Level          ──► (tidak butuh siapa pun)
Items          ──► (tidak butuh siapa pun)
Entity         ──► (tidak butuh siapa pun)
    │
    ├──► Player        ──► Entity + Level + Items
    │       ├──► Assassin
    │       ├──► Healer      ──► (tambahan: List<Player>)
    │       ├──► Swordman
    │       └──► Tank        ──► (tambahan: Enemy)
    │
    └──► Enemy         ──► Entity + Player (untuk cast DEF)
            ├──► SmallEnemy
            ├──► LargeEnemy
            └──► BossEnemy

GameState      ──► Player + Enemy + Items
UI             ──► (hanya javax.swing)
GameFrame      ──► semua Panel GUI
BattlePanel    ──► GameState + Player subclass + Enemy subclass + UI
CampaignPanel  ──► GameState + GameFrame + UI
ShopPanel      ──► GameState + Items + Player + UI
ExplorationPanel──► GameState + GameFrame + UI
InventoryPanel ──► GameState + Player + Items + UI
Main           ──► GameFrame (satu baris saja)
```

---

## Fitur Game

### Party Default

| Karakter | Role | HP | MP | ATK | DEF |
|---|---|---|---|---|---|
| Raven | Assassin | 180 | 120 | 35 | 12 |
| Lyria | Healer | 200 | 160 | 12 | 16 |
| Aldric | Swordman | 240 | 90 | 28 | 20 |
| Gorrath | Tank | 380 | 70 | 18 | 35 |

### Daftar Stage Campaign

| Stage | Nama | Musuh | EP Reward | Gold Reward | EP Diperlukan |
|---|---|---|---|---|---|
| 1 | Hutan Kelam | 2× Small | +30 | 80 | 0 |
| 2 | Rawa Terkutuk | Small + Large | +50 | 120 | 30 |
| 3 | Kastil Reruntuhan | 2× Large | +80 | 180 | 80 |
| 4 | Puncak Bayangan | 2× Large + Small | +120 | 250 | 160 |
| 5 | Gerbang Infernal | Boss | +200 | 400 | 280 |
| 6 | Singgasana Chaos | Boss + Large | +350 | 600 | 480 |

### Daftar Item Shop

| Item | Bonus | Harga |
|---|---|---|
| Elixir Kehidupan | +80 HP | 120 Gold |
| Rune Kekuatan | +10 ATK | 180 Gold |
| Vial Mana | +50 HP | 100 Gold |
| Baju Zirah | +30 HP | 150 Gold |
| Pedang Kuno | +15 ATK | 300 Gold |
| Jimat Pelindung | +100 HP | 250 Gold |

---

## Anggota

| Nama | NIM |
|---|---|
| (isi nama) | (isi NIM) |

---

> Dibuat untuk Tugas Pemrograman Berbasis Objek (PBO)