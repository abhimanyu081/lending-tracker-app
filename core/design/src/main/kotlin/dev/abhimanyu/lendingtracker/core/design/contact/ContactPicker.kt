package dev.abhimanyu.lendingtracker.core.design.contact

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

data class ContactInfo(
    val name: String,
    val phone: String
)

@Composable
fun rememberContactPicker(
    onContactSelected: (ContactInfo) -> Unit,
    onPermissionDenied: () -> Unit = {}
): ContactPickerState {
    val context = LocalContext.current
    
    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Permission granted, we'll launch contact picker when user clicks
        } else {
            onPermissionDenied()
        }
    }
    
    // Contact picker launcher
    val contactPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            result.data?.data?.let { contactUri ->
                val contactInfo = getContactInfo(context, contactUri)
                contactInfo?.let { onContactSelected(it) }
            }
        }
    }
    
    return remember {
        ContactPickerState(
            context = context,
            permissionLauncher = permissionLauncher,
            contactPickerLauncher = contactPickerLauncher
        )
    }
}

class ContactPickerState(
    private val context: Context,
    private val permissionLauncher: ManagedActivityResultLauncher<String, Boolean>,
    private val contactPickerLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>
) {
    fun launchContactPicker() {
        when {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Permission already granted, launch contact picker
                launchContactPickerIntent()
            }
            else -> {
                // Request permission
                permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
            }
        }
    }
    
    private fun launchContactPickerIntent() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
        }
        contactPickerLauncher.launch(intent)
    }
}

private fun getContactInfo(context: Context, contactUri: Uri): ContactInfo? {
    val projection = arrayOf(
        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
        ContactsContract.CommonDataKinds.Phone.NUMBER
    )
    
    var cursor: Cursor? = null
    return try {
        cursor = context.contentResolver.query(
            contactUri,
            projection,
            null,
            null,
            null
        )
        
        if (cursor?.moveToFirst() == true) {
            val nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            
            val name = if (nameIndex >= 0) cursor.getString(nameIndex) ?: "" else ""
            val phone = if (phoneIndex >= 0) cursor.getString(phoneIndex) ?: "" else ""
            
            ContactInfo(
                name = name,
                phone = phone.replace(Regex("[^+\\d]"), "") // Clean phone number
            )
        } else {
            null
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    } finally {
        cursor?.close()
    }
}