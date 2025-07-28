
# 📐 Lending Tracker App – Incremental Development Prompts

---

## 🧱 **Step 1: Scaffold Base Android App**

> Scaffold a new Android app in Kotlin using Jetpack Compose and Material 3.
> Use package name `dev.abhimanyu.lendingtracker`.
> Organize using Clean Architecture with `app`, `data`, `domain`, and `core` modules.
> Set minimum SDK to 24.
> Create a blank `MainActivity` that loads a placeholder Compose screen.

---

## 🔐 **Step 2: Add Google Sign-In**

> Integrate Google Sign-In using `play-services-auth`.
> Configure Android OAuth client ID in Google Cloud Console with SHA-1 and package name restrictions.
> Request scopes:

```
https://www.googleapis.com/auth/spreadsheets
https://www.googleapis.com/auth/drive.file
```

On successful login, get ID token or access token and display signed-in user’s email.

---

## 🧾 **Step 3: Set Up Google Sheets & Drive**

> On first login:

* Check or create a Google Sheet named `LendingTracker`
* Add a second sheet named `Metadata` if not present
* Create a Drive folder named `LendingTrackerAttachments` and get its folder ID
  Store all these IDs in a repository or preference store for reuse.

---

## 🧩 **Step 4: Define Data Models**

> Create Kotlin data classes:

* `LendingTransaction` with fields: `id`, `parentId`, `type`, `person`, `phone`, `amount`, `date`, `category`, `tags`, `reason`, `note`, `attachmentUrl`, `status`
* `TransactionType` enum: `LEND`, `REPAYMENT`
* `TransactionStatus` enum: `OPEN`, `PARTIAL`, `CLOSED`
* `MetadataEntry` with `type`, `value`
* `MetadataType` enum: `TAG`, `CATEGORY`

---

## 🔌 **Step 5: Define Repository Interfaces**

> Define the following interface:

```kotlin
interface LendingRepository {
    suspend fun addTransaction(txn: LendingTransaction)
    suspend fun getTransactions(): List<LendingTransaction>
    suspend fun addMetadataEntry(type: MetadataType, value: String)
    suspend fun getMetadata(): Map<MetadataType, List<String>>
    suspend fun uploadAttachment(file: File): String
}
```

Make this generic so that backend can be switched (e.g., Google Sheets, Firebase, or JSON).

---

## 📤 **Step 6: Implement Google Sheets & Drive Repository**

> Create a concrete implementation of `LendingRepository` that:

* Writes/reads transactions to/from `LendingTracker` sheet
* Appends new tags/categories to `Metadata` sheet
* Uploads files to Drive folder and returns shareable URL
* Automatically updates LEND status based on REPAYMENT sum

---

## 📱 **Step 7: Add LEND Transaction Screen**

> Build a UI screen to add a LEND entry using Jetpack Compose:

* Person input (with contact picker)
* Phone number field
* Amount, date
* Category dropdown
* Tags input (chip-style)
* Reason (borrower’s reason)
* Note (your own)
* File picker (for uploading to Drive)
* Submit button that writes to Google Sheet and Metadata

---

## 🔄 **Step 8: Add REPAYMENT Screen**

> Create a screen to add a REPAYMENT for a selected LEND entry:

* Select a LEND from dropdown or search
* Input amount, date, note, file
* Save to Sheet with `parentId = LEND.id`
* Auto-update LEND status to:

  * `OPEN` if 0 repaid
  * `PARTIAL` if < amount
  * `CLOSED` if >= amount

---

## 👤 **Step 9: Build Dashboard Screen**

> Show a list of all people you've lent to:

* Person name + phone
* Total lent, total repaid, balance due
* Status summary
* Tap to open person detail screen
* Tap to call/message the person via phone intent

---

## 📋 **Step 10: Person Detail Screen**

> For each person:

* Show LEND entries and their associated REPAYMENTs
* Show current balance and status
* Allow adding a new REPAYMENT inline

---

## 🔍 **Step 11: Add Metadata Management**

> Load all tags and categories from `Metadata` sheet at app startup
> If user enters a new tag or category not already listed, append it to `Metadata`.

---

## 📁 **Step 12: Add ID Generator Utility**

> Create a simple utility that:

* Generates unique IDs like `TXN_0001` for LEND
* For repayments, generates `TXN_0001_1`, `TXN_0001_2`, etc.

---

## 🧪 **Step 13: Testing**

> Test the following:

* Adding a LEND and multiple REPAYMENTs
* Partial vs full repayment status handling
* Sheet and Drive integration with real API keys
* Contact picker integration and phone actions
* Edge cases like duplicate tags, large attachments, no internet