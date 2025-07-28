Here is your **fully regenerated and refined final requirement spec** for the **Lending Tracker Android App**
---

# 📱 Lending Tracker – Final Requirements Spec (July 2025)

---

## ✅ Objective

Track and manage money you lend to others, log repayments (partial or full), and retain full context through categories, tags, attachments, contact info, and reasoning.

---

## 🧠 Core Concepts

### 📌 Transaction Types

* `LEND`: You lent money to someone
* `REPAYMENT`: You received money back against a specific `LEND`

---

### 📌 Transaction Linkage

* Every `REPAYMENT` row links to a `LEND` using `ParentID`
* One `LEND` can have multiple `REPAYMENT`s

---

### 📌 Status (For LEND Only)

| Status    | Meaning                                                  |
| --------- | -------------------------------------------------------- |
| `OPEN`    | No repayments received yet                               |
| `PARTIAL` | One or more repayments received, but total < lent amount |
| `CLOSED`  | Fully repaid (total repayment ≥ lent amount)             |

---

## 🧾 Google Sheet Structure

### 📘 Sheet 1: `LendingTracker`

| Column          | Description                                           |
| --------------- | ----------------------------------------------------- |
| `ID`            | Unique transaction ID (e.g., `TXN_0001`)              |
| `ParentID`      | For `REPAYMENT`, ID of the LEND it's repaying         |
| `Type`          | `LEND` or `REPAYMENT`                                 |
| `Person`        | Name (selected from contact list or entered manually) |
| `Phone`         | Mobile number (auto-filled from contact, optional)    |
| `Amount`        | Decimal                                               |
| `Date`          | ISO format (`YYYY-MM-DD`)                             |
| `Category`      | Optional (e.g., Travel, Family, Emergency)            |
| `Tags`          | Optional (comma-separated, free-form)                 |
| `Reason`        | What the borrower told you ("needed for rent", etc.)  |
| `Note`          | Your internal comments or context                     |
| `AttachmentURL` | Optional Drive link to receipt, chat, agreement, etc. |
| `Status`        | For `LEND` only: `OPEN`, `PARTIAL`, `CLOSED`          |

---

### 📗 Sheet 2: `Metadata`

| Type     | Value  |
| -------- | ------ |
| category | Travel |
| tag      | urgent |

* Used for in-app dropdowns and autocomplete
* App adds new categories/tags dynamically if not already present

---

## 🔐 Google Services Integration

### Google Sign-In

* Auth required to access user's Google Sheets & Drive

### Google Sheets

* Stores all lending data in `LendingTracker`
* Uses `Metadata` sheet to manage dynamic categories/tags

### Google Drive

* Stores image/PDF/file attachments
* App saves file URLs in `AttachmentURL` column

---

## 📲 App Features

### 🔹 Add LEND

* Select contact or enter person name manually
* Enter amount, date, reason, category, tags, and optional attachment
* App creates a new row in `LendingTracker` with status `OPEN`

### 🔹 Add REPAYMENT

* Select existing LEND to repay
* Add partial/full amount, date, note, attachment
* App appends a `REPAYMENT` row with `ParentID` linked to LEND
* Auto-calculates total repaid and updates `Status` of LEND

### 🔹 Dashboard View

* Group by person
* Show:

  * Total Lent
  * Total Repaid
  * Balance Due
  * Status summary

### 🔹 Person Detail View

* View all LENDs and REPAYMENTs for a person
* Add new REPAYMENT from this screen
* Tap to call/message if phone is saved

### 🔹 Contact Integration

* Use system contact picker
* Store name and mobile number in the sheet

### 🔹 Metadata Management

* Category and Tag dropdowns driven by `Metadata` sheet
* If new value entered, app appends it to metadata

### 🔹 Tap to Call / Message

* Tap on phone to open dialer or messaging app

---

## 🔍 Optional Search & Filters

* Search by person name or phone
* Filter by:

  * Status (OPEN, PARTIAL, CLOSED)
  * Category or Tag
  * Date range

---

## 🧰 Future Enhancements (Out of Scope for MVP)

* Reminders for due repayments
* CSV export or JSON backup
* Currency selection or conversion
* Offline mode
* Contact profile view
* Sync with WhatsApp chat or reminders

---

## ✅ Current Scope: Confirmed

* ✅ LEND & REPAYMENT only
* ✅ Google Sheets as primary store
* ✅ Google Drive for attachments
* ✅ Dynamic tag/category metadata
* ✅ Phone number support
* ✅ Contact picker
* ✅ Full app-managed write/update logic


