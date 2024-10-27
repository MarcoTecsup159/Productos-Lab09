package com.example.productos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.productos.ui.theme.ProductosTheme
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProductosTheme {
                ProductApp()
            }
        }
    }
}

@Composable
fun ProductApp() {
    val urlBase = "https://dummyjson.com/"
    val retrofit = Retrofit.Builder().baseUrl(urlBase)
        .addConverterFactory(GsonConverterFactory.create()).build()
    val servicio = retrofit.create(ProductApiService::class.java)
    val navController = rememberNavController()

    Scaffold(
        topBar = { AppBar() },
        content = { paddingValues -> AppContent(paddingValues, navController, servicio) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar() {
    TopAppBar(
        title = { Text("Product Browser", color = Color.White) },
    )
}

@Composable
fun AppContent(
    paddingValues: PaddingValues,
    navController: NavHostController,
    servicio: ProductApiService
) {
    Box(modifier = Modifier.padding(paddingValues)) {
        NavHost(
            navController = navController,
            startDestination = "products"
        ) {
            composable("products") { ProductListScreen(navController, servicio) }
            composable(
                "productDetail/{id}",
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) {
                val productId = it.arguments?.getInt("id") ?: 0
                ProductDetailScreen(navController, servicio, productId)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun Preview() {
    ProductosTheme {
        AppBar()
    }
}