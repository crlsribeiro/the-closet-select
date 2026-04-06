package com.carlosribeiro.theclosetselect.presentation.screens.archive

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlosribeiro.theclosetselect.data.repository.GarmentRepositoryImpl
import com.carlosribeiro.theclosetselect.domain.model.Garment
import com.carlosribeiro.theclosetselect.domain.model.GarmentCategory
import com.carlosribeiro.theclosetselect.domain.model.GarmentType
import com.carlosribeiro.theclosetselect.domain.usecase.GetGarmentsUseCase
import com.carlosribeiro.theclosetselect.domain.usecase.SaveGarmentUseCase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class WardrobeState(
    val garments: List<Garment> = emptyList(),
    val selectedCategory: GarmentCategory = GarmentCategory.ALL,
    val isLoading: Boolean = false,
    val capturedPhotoUri: Uri? = null,
    val garmentType: GarmentType = GarmentType.BLUSA,
    val garmentCategory: GarmentCategory = GarmentCategory.ALL,
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
    val step: WardrobeStep = WardrobeStep.LIST
) {
    val filteredGarments: List<Garment>
        get() = when (selectedCategory) {
            GarmentCategory.ALL -> garments
            else -> garments.filter { it.category == selectedCategory }
        }
}

enum class WardrobeStep {
    LIST, CAMERA, REVIEW, CLASSIFY
}

class WardrobeViewModel(context: Context) : ViewModel() {

    private val garmentRepository = GarmentRepositoryImpl(context)
    private val getGarmentsUseCase = GetGarmentsUseCase(garmentRepository)
    private val saveGarmentUseCase = SaveGarmentUseCase(garmentRepository)

    private val _uiState = MutableStateFlow(WardrobeState())
    val uiState: StateFlow<WardrobeState> = _uiState.asStateFlow()

    private val userId: String = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()

    init {
        loadGarments()
    }

    private fun loadGarments() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getGarmentsUseCase(userId)
                .catch { error ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = error.message) }
                }
                .collect { garments ->
                    _uiState.update { it.copy(isLoading = false, garments = garments) }
                }
        }
    }

    fun onCategorySelected(category: GarmentCategory) =
        _uiState.update { it.copy(selectedCategory = category) }

    fun onOpenCamera() =
        _uiState.update { it.copy(step = WardrobeStep.CAMERA) }

    fun onPhotoCaptured(uri: Uri) =
        _uiState.update { it.copy(capturedPhotoUri = uri, step = WardrobeStep.REVIEW) }

    fun onPhotoApproved() =
        _uiState.update { it.copy(step = WardrobeStep.CLASSIFY) }

    fun onRetakePhoto() =
        _uiState.update { it.copy(step = WardrobeStep.CAMERA, capturedPhotoUri = null) }

    fun onGarmentTypeChange(type: GarmentType) =
        _uiState.update { it.copy(garmentType = type) }

    fun onGarmentCategoryChange(category: GarmentCategory) =
        _uiState.update { it.copy(garmentCategory = category) }

    fun onConfirmGarment() {
        val state = _uiState.value
        val photoUri = state.capturedPhotoUri ?: return

        val garment = Garment(
            userId = userId,
            name = state.garmentType.displayName,
            type = state.garmentType,
            category = state.garmentCategory
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            saveGarmentUseCase(garment, photoUri)
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            step = WardrobeStep.LIST,
                            capturedPhotoUri = null,
                            garmentType = GarmentType.BLUSA,
                            garmentCategory = GarmentCategory.ALL
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isSaving = false, errorMessage = error.message) }
                }
        }
    }

    fun onDismissError() =
        _uiState.update { it.copy(errorMessage = null) }

    fun onBackToList() =
        _uiState.update {
            it.copy(
                step = WardrobeStep.LIST,
                capturedPhotoUri = null,
                garmentType = GarmentType.BLUSA,
                garmentCategory = GarmentCategory.ALL
            )
        }
}