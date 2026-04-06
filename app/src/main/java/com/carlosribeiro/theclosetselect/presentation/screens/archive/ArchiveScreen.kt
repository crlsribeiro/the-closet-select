package com.carlosribeiro.theclosetselect.presentation.screens.archive

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.carlosribeiro.theclosetselect.domain.model.Garment
import com.carlosribeiro.theclosetselect.domain.model.GarmentCategory
import com.carlosribeiro.theclosetselect.domain.model.GarmentType
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

private val Cream       = Color(0xFFF5F2ED)
private val CreamDark   = Color(0xFFEDEAE3)
private val Gold        = Color(0xFFB8972A)
private val TextPrimary = Color(0xFF1A1A18)
private val TextMuted   = Color(0xFF888780)
private val TextHint    = Color(0xFFB4B2A9)
private val BorderColor = Color(0xFFD3D1C7)
private val BorderLight = Color(0xFFB4B2A9)

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ArchiveScreen(onNavigateBack: () -> Unit = {}) {
    val context = LocalContext.current
    val viewModel: WardrobeViewModel = viewModel(factory = WardrobeViewModelFactory(context))
    val uiState by viewModel.uiState.collectAsState()
    val cameraPermission = rememberPermissionState(android.Manifest.permission.CAMERA)

    when (uiState.step) {
        WardrobeStep.CAMERA -> CameraScreen(
            onPhotoCaptured = viewModel::onPhotoCaptured,
            onBack = viewModel::onBackToList
        )
        WardrobeStep.REVIEW -> ReviewScreen(
            photoUri = uiState.capturedPhotoUri,
            onPhotoApproved = viewModel::onPhotoApproved,
            onRetake = viewModel::onRetakePhoto,
            onBack = viewModel::onBackToList
        )
        WardrobeStep.CLASSIFY -> ClassifyScreen(
            uiState = uiState,
            onTypeChange = viewModel::onGarmentTypeChange,
            onCategoryChange = viewModel::onGarmentCategoryChange,
            onConfirm = viewModel::onConfirmGarment,
            onBack = viewModel::onBackToList
        )
        WardrobeStep.LIST -> WardrobeListScreen(
            uiState = uiState,
            onCategorySelected = viewModel::onCategorySelected,
            onNavigateBack = onNavigateBack,
            onOpenCamera = {
                if (cameraPermission.status.isGranted) viewModel.onOpenCamera()
                else cameraPermission.launchPermissionRequest()
            }
        )
    }

    uiState.errorMessage?.let { message ->
        AlertDialog(
            onDismissRequest = viewModel::onDismissError,
            title = { Text("Attention") },
            text = { Text(message) },
            confirmButton = {
                TextButton(onClick = viewModel::onDismissError) {
                    Text("OK", color = Gold)
                }
            }
        )
    }
}

@Composable
private fun WardrobeListScreen(
    uiState: WardrobeState,
    onCategorySelected: (GarmentCategory) -> Unit,
    onOpenCamera: () -> Unit,
    onNavigateBack: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Text(
                    text = "The Atelier",
                    color = TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.3.sp
                )
                Spacer(modifier = Modifier.width(20.dp))
            }
        }

        // Capture card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(CreamDark)
                    .clickable { onOpenCamera() }
                    .padding(vertical = 40.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Gold),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = androidx.compose.ui.res.painterResource(id = android.R.drawable.ic_menu_camera),
                            contentDescription = "Camera",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Text("DIGITAL CAPTURE", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Medium, letterSpacing = 2.sp)
                    Text("DOCUMENT YOUR NEW ACQUISITION", color = TextMuted, fontSize = 10.sp, letterSpacing = 1.5.sp)
                }
            }
        }

        // Divider + Archive header
        item {
            Spacer(modifier = Modifier.height(28.dp))
            HorizontalDivider(color = BorderColor, modifier = Modifier.padding(horizontal = 24.dp))
            Spacer(modifier = Modifier.height(24.dp))
            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                Text(text = "Your Archive", color = TextPrimary, fontSize = 32.sp, fontWeight = FontWeight.Normal, fontStyle = FontStyle.Italic)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "A curated selection of your finest acquisitions,\nmaintained with digital precision.",
                    color = TextMuted, fontSize = 11.sp, lineHeight = 17.sp
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Filtros com scroll horizontal
        item {
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                GarmentCategory.entries.forEach { category ->
                    val isSelected = uiState.selectedCategory == category
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) TextPrimary else Color.Transparent)
                            .clickable { onCategorySelected(category) }
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = category.displayName.uppercase(),
                            color = if (isSelected) Cream else TextMuted,
                            fontSize = 10.sp,
                            letterSpacing = 1.2.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Lista de peças
        when {
            uiState.isLoading -> item {
                Box(modifier = Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Gold, modifier = Modifier.size(32.dp))
                }
            }
            uiState.filteredGarments.isEmpty() -> item {
                Box(modifier = Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                    Text(
                        text = "No garments yet.\nCapture your first piece.",
                        color = TextMuted, fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        fontStyle = FontStyle.Italic, lineHeight = 20.sp
                    )
                }
            }
            else -> {
                items(
                    items = uiState.filteredGarments,
                    key = { garment -> garment.id }
                ) { garment ->
                    GarmentListItem(garment = garment)
                    HorizontalDivider(
                        color = BorderColor,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // Footer
        item {
            Spacer(modifier = Modifier.height(32.dp))
            Column(
                modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "The Atelier", color = TextPrimary, fontSize = 18.sp, fontStyle = FontStyle.Italic)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "YOUR PERSONAL LEGACY, DIGITISED", color = TextHint, fontSize = 9.sp, letterSpacing = 2.sp)
            }
        }
    }
}

@Composable
private fun GarmentListItem(garment: Garment) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(width = 88.dp, height = 110.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(CreamDark)
        ) {
            if (garment.photoUrl.isNotEmpty()) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(garment.photoUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = garment.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        Column(modifier = Modifier.padding(top = 4.dp)) {
            Text(text = garment.category.displayName.uppercase(), color = TextMuted, fontSize = 9.sp, letterSpacing = 1.5.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = garment.name, color = TextPrimary, fontSize = 17.sp, fontStyle = FontStyle.Italic, lineHeight = 22.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = garment.type.displayName.uppercase(), color = TextHint, fontSize = 10.sp, letterSpacing = 1.sp)
        }
    }
}

@Composable
private fun ReviewScreen(
    photoUri: Uri?,
    onPhotoApproved: () -> Unit,
    onRetake: () -> Unit,
    onBack: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().background(Cream)) {
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 20.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
            }
            Text("Review Photo", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Medium)
        }
        Box(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp).aspectRatio(0.85f)
                .clip(RoundedCornerShape(16.dp)).background(CreamDark)
        ) {
            if (photoUri != null) {
                AsyncImage(model = photoUri, contentDescription = "Captured garment", contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = onPhotoApproved,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp).height(52.dp),
            shape = RoundedCornerShape(4.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Gold)
        ) {
            Text("LOOKS GOOD", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Medium, letterSpacing = 2.sp)
        }
        Spacer(modifier = Modifier.height(12.dp))
        TextButton(onClick = onRetake, modifier = Modifier.fillMaxWidth()) {
            Text("Retake photo", color = TextMuted, fontSize = 13.sp)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ClassifyScreen(
    uiState: WardrobeState,
    onTypeChange: (GarmentType) -> Unit,
    onCategoryChange: (GarmentCategory) -> Unit,
    onConfirm: () -> Unit,
    onBack: () -> Unit
) {
    var typeExpanded by remember { mutableStateOf(false) }
    var categoryExpanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().background(Cream).verticalScroll(rememberScrollState())) {
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 20.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
            }
            Text("The Atelier", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Medium)
        }

        if (uiState.capturedPhotoUri != null) {
            Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp).height(200.dp).clip(RoundedCornerShape(16.dp)).background(CreamDark)) {
                AsyncImage(model = uiState.capturedPhotoUri, contentDescription = "Garment photo", contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text(text = "CLASSIFICATION", color = TextMuted, fontSize = 10.sp, letterSpacing = 2.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Identify the essence\nof your garment.", color = TextPrimary, fontSize = 24.sp, fontStyle = FontStyle.Italic, lineHeight = 30.sp)
        }

        Spacer(modifier = Modifier.height(28.dp))

        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text(text = "TYPE", color = TextMuted, fontSize = 10.sp, letterSpacing = 2.sp)
            Spacer(modifier = Modifier.height(8.dp))
            ExposedDropdownMenuBox(expanded = typeExpanded, onExpandedChange = { typeExpanded = it }) {
                OutlinedTextField(
                    value = uiState.garmentType.displayName,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable),
                    shape = RoundedCornerShape(4.dp),
                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 15.sp, fontStyle = FontStyle.Italic, color = TextPrimary),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Gold, unfocusedBorderColor = BorderLight,
                        focusedContainerColor = Cream, unfocusedContainerColor = Cream,
                        focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary
                    )
                )
                ExposedDropdownMenu(expanded = typeExpanded, onDismissRequest = { typeExpanded = false }, modifier = Modifier.background(Cream)) {
                    GarmentType.entries.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type.displayName, color = TextPrimary, fontSize = 14.sp) },
                            onClick = { onTypeChange(type); typeExpanded = false },
                            modifier = Modifier.background(Cream)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text(text = "CATEGORY", color = TextMuted, fontSize = 10.sp, letterSpacing = 2.sp)
            Spacer(modifier = Modifier.height(8.dp))
            ExposedDropdownMenuBox(expanded = categoryExpanded, onExpandedChange = { categoryExpanded = it }) {
                OutlinedTextField(
                    value = uiState.garmentCategory.displayName,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable),
                    shape = RoundedCornerShape(4.dp),
                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 15.sp, fontStyle = FontStyle.Italic, color = TextPrimary),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Gold, unfocusedBorderColor = BorderLight,
                        focusedContainerColor = Cream, unfocusedContainerColor = Cream,
                        focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary
                    )
                )
                ExposedDropdownMenu(expanded = categoryExpanded, onDismissRequest = { categoryExpanded = false }, modifier = Modifier.background(Cream)) {
                    GarmentCategory.entries.filter { it != GarmentCategory.ALL }.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category.displayName, color = TextPrimary, fontSize = 14.sp) },
                            onClick = { onCategoryChange(category); categoryExpanded = false },
                            modifier = Modifier.background(Cream)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(36.dp))

        if (uiState.isSaving) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Gold, modifier = Modifier.size(36.dp))
            }
        } else {
            Button(
                onClick = onConfirm,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp).height(52.dp),
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Gold)
            ) {
                Text("ADD TO ARCHIVE", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Medium, letterSpacing = 2.sp)
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
        Text(text = "THE ATELIER", color = TextHint, fontSize = 9.sp, letterSpacing = 2.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        Text(text = "YOUR PERSONAL LEGACY, DIGITISED", color = TextHint.copy(alpha = 0.6f), fontSize = 8.sp, letterSpacing = 1.5.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun WardrobeListPreview() {
    WardrobeListScreen(
        uiState = WardrobeState(
            garments = listOf(
                Garment(name = "Silk Blouse", type = GarmentType.BLUSA, category = GarmentCategory.ESSENTIALS),
                Garment(name = "Wool Coat", type = GarmentType.CASACO, category = GarmentCategory.OUTERWEAR),
                Garment(name = "Silk Gown", type = GarmentType.VESTIDO_LONGO, category = GarmentCategory.EVENING)
            )
        ),
        onCategorySelected = {},
        onOpenCamera = {},
        onNavigateBack = {}
    )
}