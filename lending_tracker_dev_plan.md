# 📐 Lending Tracker Android App – Development Plan

---

## 🧱 1. Project Setup

### 1.1 Scaffold Base App
- **Language**: Kotlin
- **UI**: Jetpack Compose with Material 3
- **Architecture**: MVVM + Clean Architecture (modular + interface-driven)
- **Min SDK**: 24+
- **Package Name**: `dev.abhimanyu.lendingtracker`
- **Modules**:
  ```
  app/
  ├── presentation/     → UI (Jetpack Compose)
  ├── domain/           → Use cases + business logic interfaces
  ├── data/             → Repositories, DTOs, data sources
  └── core/             → Common utils, DI, error handling
  ```

---

## 🔐 2. Authentication Setup (Google Sign-In)

### 2.1 Google OAuth (Secure Setup)
- Use **Android OAuth Client ID** (not web)
- Configure in Google Cloud Console:
  - App type: Android
  - Package name: `dev.abhimanyu.lendingtracker`
  - Debug + Release SHA-1 fingerprints
- **No client secret stored** in app

### 2.2 OAuth Scope Permissions
- Required scopes:
  ```
  https://www.googleapis.com/auth/spreadsheets
  https://www.googleapis.com/auth/drive.file
  ```
- Store `google_client_id` in `res/values/secrets.xml` or `BuildConfig`
- Do **not** commit secrets — use `.gitignore`

### 2.3 Auth Flow
- Integrate `GoogleSignInClient`
- Retrieve access token for Google Sheets and Drive API calls

---

## 📁 3. Google Sheets + Drive Setup

### 3.1 On First Login
- Create or locate:
  - Google Sheet named `LendingTracker`
  - Sheet tab `Metadata`
  - Drive folder `LendingTrackerAttachments`

### 3.2 Sheet Structure: `LendingTracker`

| Column          | Description                                         |
|------------------|-----------------------------------------------------|
| `ID`             | Unique transaction ID (e.g. TXN_0001)               |
| `ParentID`       | Links REPAYMENT to LEND                             |
| `Type`           | LEND or REPAYMENT                                   |
| `Person`         | Name (contact picker or manual)                     |
| `Phone`          | Mobile number                                       |
| `Amount`         | Decimal                                             |
| `Date`           | ISO format                                          |
| `Category`       | Optional                                            |
| `Tags`           | Comma-separated                                     |
| `Reason`         | Borrower's reason for borrowing                     |
| `Note`           | Internal comment                                    |
| `AttachmentURL`  | Google Drive shareable link                         |
| `Status`         | OPEN, PARTIAL, CLOSED (for LEND only)              |

### 3.3 Sheet Structure: `Metadata`

| Type     | Value      |
|----------|------------|
| category | Travel     |
| tag      | urgent     |

- Dynamically updated from app when user enters new tag/category

---

## 📦 4. Domain Layer – Interfaces (Backend Agnostic)

Define these key abstractions:
```kotlin
interface LendingRepository {
    suspend fun addTransaction(txn: LendingTransaction)
    suspend fun getTransactions(): List<LendingTransaction>
    suspend fun addMetadataEntry(type: MetadataType, value: String)
    suspend fun getMetadata(): Map<MetadataType, List<String>>
    suspend fun uploadAttachment(file: File): String
}
```

This allows easy switching between:
- Google Sheets
- Firebase
- Local Room DB
- JSON storage

---

## 📊 5. Data Models

### 5.1 Kotlin Data Classes
- `LendingTransaction`
- `TransactionType` (LEND / REPAYMENT)
- `Status` (OPEN / PARTIAL / CLOSED)
- `MetadataEntry`
- `MetadataType` (tag / category)

---

## 📋 6. Add LEND Flow

### 6.1 UI Form
- Person (contact picker or manual)
- Phone number
- Amount, Date
- Category (dropdown)
- Tags (chips with autocomplete)
- Reason (borrower’s reason)
- Note (internal)
- Attachment (upload to Drive)

### 6.2 On Submit
- Generate unique `ID`
- Upload file (if present) → get URL
- Write row to Sheet via `LendingRepository`
- Add metadata if needed

---

## 🔄 7. Add REPAYMENT Flow

### 7.1 UI
- Select existing `LEND` entry
- Input amount, date, note, attachment

### 7.2 Logic
- Generate sub-ID (e.g. TXN_0001_1)
- Store `ParentID`
- Write REPAYMENT row to Sheet
- Auto-update `Status` of parent LEND based on total repaid:
  - 0 → `OPEN`
  - < amount → `PARTIAL`
  - ≥ amount → `CLOSED`

---

## 👤 8. Person Dashboard View

- Group transactions by person
- Show:
  - Person name + phone
  - Total lent, total repaid, balance
  - Tap to view all transactions
  - Tap to call/message via Intent

---

## 🔍 9. Search and Filters (Optional)

- Search by name, number
- Filter by:
  - Status
  - Category
  - Tag
  - Date range

---

## 📤 10. Export/Backup (Optional)

- Manual CSV export
- JSON export/import
- Save to Drive or local storage

---

## 🧪 11. Testing

- Test flows:
  - Add LEND
  - Add partial and full REPAYMENT
  - Status updates
  - Sheet/Drive errors
  - Contact picker
- Unit test the repository layer (mock backend)
