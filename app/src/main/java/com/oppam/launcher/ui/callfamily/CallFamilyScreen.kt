package com.oppam.launcher.ui.callfamily

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.content.Intent
import android.net.Uri
import com.oppam.launcher.ui.components.ElderHeading
import com.oppam.launcher.ui.components.LargeButton
import com.oppam.launcher.ui.components.LargeOutlinedButton
import com.oppam.launcher.ui.voice.VoiceAssistant

/**
 * Call Family Screen
 * Simple screen with quick dial contacts
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CallFamilyScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    
    // Voice assistant
    val voiceAssistant = remember {
        VoiceAssistant(context).apply {
            initialize { success ->
                if (success) {
                    speak("Call Family")
                }
            }
        }
    }
    
    DisposableEffect(Unit) {
        onDispose {
            voiceAssistant.shutdown()
        }
    }
    
    // Sample contacts (in real app, these would come from phone contacts)
    val contacts = remember {
        listOf(
            Contact("Son - Ravi", "+91 98765 43210"),
            Contact("Daughter - Priya", "+91 98765 43211"),
            Contact("Emergency - 108", "108")
        )
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Call Family",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier.size(36.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header icon
            Icon(
                imageVector = Icons.Filled.Phone,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Quick Dial",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            
            Text(
                text = "Tap to call your loved ones",
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Contact list
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(contacts) { contact ->
                    ContactCard(
                        contact = contact,
                        onCall = {
                            voiceAssistant.speak("Calling ${contact.name}")
                            // Make actual phone call
                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                data = Uri.parse("tel:${contact.phoneNumber}")
                            }
                            context.startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}

/**
 * Contact data class
 */
data class Contact(
    val name: String,
    val phoneNumber: String
)

/**
 * Contact Card Component
 */
@Composable
fun ContactCard(
    contact: Contact,
    onCall: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Contact info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = contact.name,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = contact.phoneNumber,
                    fontSize = 22.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            
            // Call button
            FilledIconButton(
                onClick = onCall,
                modifier = Modifier.size(80.dp),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.Phone,
                    contentDescription = "Call",
                    modifier = Modifier.size(40.dp),
                    tint = Color.White
                )
            }
        }
    }
}
