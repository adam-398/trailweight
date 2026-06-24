# Trailweight

> Gear management for people who care about every gram.

Trailweight is a native Android app for hikers, backpackers, and ultralight enthusiasts who want to build, manage, and share gear lists with precision weight tracking. Built with Kotlin, Jetpack Compose, and Supabase.

---
## Screenshots

<p float="left">
  <img src="https://github.com/user-attachments/assets/e076f09f-d3cb-43e4-97cb-95f11b8080c9" width="200"/>
  <img src="https://github.com/user-attachments/assets/1762ddcb-78aa-4a85-baee-241738f7e2cb" width="200"/>
  <img src="https://github.com/user-attachments/assets/8955e749-c67e-46be-b6aa-89b3945e0333" width="200"/>
  <img src="https://github.com/user-attachments/assets/60dd9d01-e799-4c99-8669-0fc6d14a2083" width="200"/>
  <img src="https://github.com/user-attachments/assets/cd66b006-b091-4414-af27-15773dff87a0" width="200"/>
</p>
---

## Features

- **Gear lists** — create multiple lists for different trips or loadouts
- **Item management** — add, edit, and delete gear items with weight, category, and notes
- **Weight tracking** — automatic total weight calculation per list, sorted heaviest first
- **Unit switching** — toggle between metric (g) and imperial (oz) globally, with live conversion
- **Category breakdown** — visual pie chart showing weight distribution by category
- **Sharing** — share any gear list with other Trailweight users via a deep link
- **Dark mode** — full dark/light theme support with a custom nature-inspired palette
- **Secure auth** — email/password sign up and login, with password reset via email deep link
- **Custom email** — transactional emails sent from `trailweight@auroralaboratories.dev` via Resend
- **Crash reporting** — Firebase Crashlytics integration for production error tracking

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Navigation | Navigation Compose |
| Backend | Supabase (Auth, Postgrest, Storage) |
| Networking | Ktor (Android engine) |
| Serialization | kotlinx.serialization |
| Email | Resend (custom SMTP) |
| Crash reporting | Firebase Crashlytics |
| Session storage | SharedPreferences via SettingsSessionManager |

---

## Getting Started

### Prerequisites

- Android Studio Hedgehog or later
- Android SDK 28+
- A Supabase project
- A Resend account with a verified domain (for email)
- A Firebase project with Crashlytics enabled

### Setup

1. **Clone the repo**
   ```bash
   git clone https://github.com/adam-398/trailweight.git
   cd trailweight
   ```

2. **Add your secrets to `local.properties`**
   ```
   SUPABASE_URL=your_supabase_project_url
   SUPABASE_API_KEY=your_supabase_anon_key
   ```

3. **Add your `google-services.json`**
   Download from Firebase Console and place at `app/google-services.json`.
   This file is gitignored — never commit it.

4. **Set up the database**
   Run the following in Supabase SQL Editor:
```sql
   -- Gear lists table
   CREATE TABLE lists (
     id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
     user_id UUID NOT NULL REFERENCES auth.users,
     name TEXT NOT NULL,
     notes TEXT,
     share_id UUID DEFAULT gen_random_uuid() UNIQUE,
     created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
   );

   -- Items table
   CREATE TABLE items (
     id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
     list_id UUID NOT NULL REFERENCES lists ON DELETE CASCADE,
     name TEXT NOT NULL,
     weight DOUBLE PRECISION,
     category TEXT NOT NULL,
     notes TEXT,
     created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
   );
```

5. **Enable Row Level Security**
```sql
   ALTER TABLE lists ENABLE ROW LEVEL SECURITY;
   ALTER TABLE items ENABLE ROW LEVEL SECURITY;

   -- Lists policies
   CREATE POLICY "Users can view their own lists"
     ON lists FOR SELECT USING (auth.uid() = user_id);
   CREATE POLICY "Users can insert their own lists"
     ON lists FOR INSERT WITH CHECK (auth.uid() = user_id);
   CREATE POLICY "Users can update their own lists"
     ON lists FOR UPDATE USING (auth.uid() = user_id);
   CREATE POLICY "Users can delete their own lists"
     ON lists FOR DELETE USING (auth.uid() = user_id);
   CREATE POLICY "Anyone can view a list by share_id"
     ON lists FOR SELECT TO public USING (share_id IS NOT NULL);

   -- Items policies
   CREATE POLICY "Users can view items in their own lists"
     ON items FOR SELECT USING (
       EXISTS (SELECT 1 FROM lists WHERE lists.id = items.list_id AND lists.user_id = auth.uid()));
   CREATE POLICY "Users can insert items into their own lists"
     ON items FOR INSERT WITH CHECK (
       EXISTS (SELECT 1 FROM lists WHERE lists.id = items.list_id AND lists.user_id = auth.uid()));
   CREATE POLICY "Users can update items in their own lists"
     ON items FOR UPDATE USING (
       EXISTS (SELECT 1 FROM lists WHERE lists.id = items.list_id AND lists.user_id = auth.uid()));
   CREATE POLICY "Users can delete items in their own lists"
     ON items FOR DELETE USING (
       EXISTS (SELECT 1 FROM lists WHERE lists.id = items.list_id AND lists.user_id = auth.uid()));
   CREATE POLICY "Anyone can view items in a shared list"
     ON items FOR SELECT TO public USING (
       EXISTS (SELECT 1 FROM lists WHERE lists.id = items.list_id AND lists.share_id IS NOT NULL));
```

6. **Build and run**
   Open in Android Studio and press Run (▶).

---

## Deep Links

Trailweight handles three custom URI schemes:

| URI | Purpose |
|---|---|
| `trailweight://reset-password#...` | Password reset flow from email link |
| `trailweight://confirm-signup#...` | Email confirmation for new accounts |
| `trailweight://list/{share_id}` | Open a shared gear list |

---


