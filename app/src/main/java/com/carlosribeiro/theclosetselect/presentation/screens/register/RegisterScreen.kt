package com.carlosribeiro.theclosetselect.presentation.screens.register

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carlosribeiro.theclosetselect.presentation.components.AuraButton

private val BackgroundColor = Color(0xFF0D0D0D)
private val GoldColor = Color(0xFFB8972A)
private val GoldLight = Color(0xFFD4A847)
private val SubtitleColor = Color(0xFF888888)
private val FieldBackground = Color(0xFF1A1A1A)
private val FieldBorder = Color(0xFF2C2C2C)

private val zodiacSigns = listOf(
    "Áries", "Touro", "Gêmeos", "Câncer",
    "Leão", "Virgem", "Libra", "Escorpião",
    "Sagitário", "Capricórnio", "Aquário", "Peixes"
)

@Composable
fun RegisterScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var selectedSign by remember { mutableStateOf("SELECIONE") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(top = 32.dp, bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RegisterHeader()

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                RegisterField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = "NOME",
                    placeholder = "NOME",
                    modifier = Modifier.weight(1f),
                    imeAction = ImeAction.Next
                )
                RegisterField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = "SOBRENOME",
                    placeholder = "SOBRENOME",
                    modifier = Modifier.weight(1f),
                    imeAction = ImeAction.Next
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                FieldLabel("E-MAIL")
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text("contato@thecloset.com", color = SubtitleColor, fontSize = 13.sp)
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Email, contentDescription = null, tint = SubtitleColor)
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    shape = RoundedCornerShape(16.dp),
                    colors = registerFieldColors(),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    FieldLabel("DATA DE NASCIMENTO")
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = birthDate,
                        onValueChange = { birthDate = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text("DD/MM/AAAA", color = SubtitleColor, fontSize = 13.sp)
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        shape = RoundedCornerShape(16.dp),
                        colors = registerFieldColors(),
                        singleLine = true
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    FieldLabel("SIGNO")
                    Spacer(modifier = Modifier.height(6.dp))
                    ZodiacDropdown(
                        selected = selectedSign,
                        onSelected = { selectedSign = it }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                FieldLabel("SENHA")
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        PasswordToggleIcon(passwordVisible) { passwordVisible = !passwordVisible }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    ),
                    shape = RoundedCornerShape(16.dp),
                    colors = registerFieldColors(),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                FieldLabel("REPETIR SENHA")
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        PasswordToggleIcon(confirmPasswordVisible) { confirmPasswordVisible = !confirmPasswordVisible }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    shape = RoundedCornerShape(16.dp),
                    colors = registerFieldColors(),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            AuraButton(
                text = "SALVAR",
                onClick = onNavigateToHome
            )

            Spacer(modifier = Modifier.height(16.dp))

            TermsFooter()
        }
    }
}

@Composable
private fun RegisterHeader() {
    Box(
        modifier = Modifier
            .width(48.dp)
            .height(3.dp)
            .background(GoldColor, RoundedCornerShape(2.dp))
    )
    Spacer(modifier = Modifier.height(20.dp))
    Text(
        text = "Bem-vindo ao",
        color = GoldLight,
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )
    Text(
        text = "THE CLOSET SELECT",
        color = Color.White,
        fontSize = 18.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 2.sp,
        textAlign = TextAlign.Center
    )
    Spacer(modifier = Modifier.height(12.dp))
    Text(
        text = "CRIE SUA CONTA PARA CRIAR O SEU ACERVO EXCLUSIVO.",
        color = SubtitleColor,
        fontSize = 11.sp,
        letterSpacing = 1.sp,
        textAlign = TextAlign.Center,
        lineHeight = 18.sp
    )
}

@Composable
private fun RegisterField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    imeAction: ImeAction = ImeAction.Next
) {
    Column(modifier = modifier) {
        FieldLabel(label)
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder, color = SubtitleColor, fontSize = 13.sp) },
            keyboardOptions = KeyboardOptions(imeAction = imeAction),
            shape = RoundedCornerShape(16.dp),
            colors = registerFieldColors(),
            singleLine = true
        )
    }
}

@Composable
private fun ZodiacDropdown(
    selected: String,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(FieldBackground, RoundedCornerShape(16.dp))
            .border(1.dp, FieldBorder, RoundedCornerShape(16.dp))
            .clickable { expanded = true }
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(text = selected, color = Color.White, fontSize = 13.sp)
        Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = null,
            tint = SubtitleColor,
            modifier = Modifier.align(Alignment.CenterEnd)
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(FieldBackground)
        ) {
            zodiacSigns.forEach { sign ->
                DropdownMenuItem(
                    text = { Text(sign, color = Color.White, fontSize = 13.sp) },
                    onClick = {
                        onSelected(sign)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun FieldLabel(text: String) {
    Text(
        text = text,
        color = SubtitleColor,
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 1.5.sp
    )
}

@Composable
private fun PasswordToggleIcon(visible: Boolean, onToggle: () -> Unit) {
    IconButton(onClick = onToggle) {
        Icon(
            imageVector = if (visible) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
            contentDescription = if (visible) "Ocultar senha" else "Mostrar senha",
            tint = SubtitleColor
        )
    }
}

@Composable
private fun TermsFooter() {
    Text(
        text = buildAnnotatedString {
            withStyle(SpanStyle(color = SubtitleColor, fontSize = 10.sp, letterSpacing = 1.sp)) {
                append("AO SE REGISTRAR, VOCÊ CONCORDA COM OS NOSSOS\n")
            }
            withStyle(
                SpanStyle(
                    color = Color.White,
                    fontSize = 10.sp,
                    letterSpacing = 1.sp,
                    textDecoration = TextDecoration.Underline
                )
            ) {
                append("TERMOS DE USO")
            }
            withStyle(SpanStyle(color = SubtitleColor, fontSize = 10.sp, letterSpacing = 1.sp)) {
                append(" E ")
            }
            withStyle(
                SpanStyle(
                    color = Color.White,
                    fontSize = 10.sp,
                    letterSpacing = 1.sp,
                    textDecoration = TextDecoration.Underline
                )
            ) {
                append("POLÍTICA DE PRIVACIDADE")
            }
            withStyle(SpanStyle(color = SubtitleColor, fontSize = 10.sp)) {
                append(".")
            }
        },
        textAlign = TextAlign.Center,
        lineHeight = 18.sp
    )
}

@Composable
private fun registerFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = GoldColor,
    unfocusedBorderColor = FieldBorder,
    focusedContainerColor = FieldBackground,
    unfocusedContainerColor = FieldBackground,
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    cursorColor = GoldColor
)