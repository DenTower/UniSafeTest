package org.example.project.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.example.project.presentation.viewmodel.ShoppingViewModel

@Composable
fun LoginScreen(
    viewModel: ShoppingViewModel,
    onNavigateToMain: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.isAuth) {
        if(state.isAuth) {
            onNavigateToMain()
        }
    }

    LoginContent(
        key = state.key,
        onKeyChange = viewModel::onKeyChanged,
        onRegister = { viewModel.register() },
        onLogin = { viewModel.login() }
    )
}

@Composable
fun LoginContent(
    key: String,
    onKeyChange: (String) -> Unit,
    onRegister: () -> Unit,
    onLogin: () -> Unit
) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onRegister
            ) {
                Text("Свой список покупок")
            }

            Spacer(Modifier.height(24.dp))

            HorizontalDivider()

            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                value = key,
                onValueChange = onKeyChange,
                label = { Text("Введите ключ") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onLogin
            ) {
                Text("Присоединиться")
            }
        }
    }
}

@Preview
@Composable
fun LoginPreview() {
    LoginContent(
        key = "e7255d",
        onKeyChange = { },
        onRegister = { },
        onLogin = { }
    )
}