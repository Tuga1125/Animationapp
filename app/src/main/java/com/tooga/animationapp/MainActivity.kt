package com.tooga.animationapp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main") {
        composable(route = "main") {
            MainScreen(navController)
        }
        composable(route = "screen1") {
            Screen1()
        }
        composable(route = "screen2") {
            Screen2()
        }
        composable(route = "screen3") {
            Screen3()
        }
        composable(route = "screen4") {
            Screen4()
        }
    }
}

@Composable
fun MainScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        NavigationButton(navController = navController, destination = "screen1", label = "Screen 1")
        NavigationButton(navController = navController, destination = "screen2", label = "Screen 2")
        NavigationButton(navController = navController, destination = "screen3", label = "Screen 3")
        NavigationButton(navController = navController, destination = "screen4", label = "Screen 4")
        Spacer(modifier = Modifier.height(40.dp))

        Text("Name: Tooga Garanja Magar")
        Text("ID: 301369368")
    }
}

@Composable
fun NavigationButton(navController: NavController, destination: String, label: String) {
    Button(onClick = { navController.navigate(destination) }) {
        Text(label)
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun Screen1() {
    var expanded by remember { mutableStateOf(false) }

    Surface(
        color = MaterialTheme.colorScheme.primary,
        onClick = { expanded = !expanded },
        modifier = Modifier.fillMaxSize()
    ) {
        AnimatedContent(
            targetState = expanded,
            transitionSpec = {
                fadeIn(animationSpec = tween(150, 150)) with
                        fadeOut(animationSpec = tween(150)) using
                        SizeTransform { initialSize, targetSize ->
                            if (targetState) {
                                keyframes {
                                    // Expand horizontally first.
                                    IntSize(targetSize.width, initialSize.height) at 150
                                    durationMillis = 300
                                }
                            } else {
                                keyframes {
                                    // Shrink vertically first.
                                    IntSize(initialSize.width, targetSize.height) at 150
                                    durationMillis = 300
                                }
                            }
                        }
            }
        ) { targetExpanded ->
            if (targetExpanded) {
                Expanded()
            } else {
                ContentIcon()
            }
        }
    }
}

@Composable
fun Expanded() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.Green)
    ) {
        // Content to be displayed when expanded
    }
}

@Composable
fun ContentIcon() {
    Box(
        modifier = Modifier
            .size(48.dp)
            .padding(16.dp)
            .background(Color.Blue)
    ) {
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Screen2() {
    var selected by remember { mutableStateOf(false) }

    // Animates changes when `selected` is changed.
    val transition = updateTransition(selected, label = "selected state")
    val borderColor by transition.animateColor(label = "border color") { isSelected ->
        if (isSelected) Color.Magenta else Color.White
    }
    val elevation by transition.animateDp(label = "elevation") { isSelected ->
        if (isSelected) 10.dp else 2.dp
    }
    val contentOpacity by transition.animateFloat(label = "content opacity") { isSelected ->
        if (isSelected) 1f else 0f
    }

    Surface(
        onClick = { selected = !selected },
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(2.dp, borderColor),
        shadowElevation = elevation,
        modifier = Modifier.padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .animateContentSize()
                .padding(16.dp)
                .fillMaxWidth()
                .clickable { selected = !selected }
        ) {
            Text(text = "Click Me!", color = Color.Black)

            Spacer(modifier = Modifier.height(16.dp))

            AnimatedVisibility(
                visible = selected,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Text(
                    text = AnnotatedString.Builder("It is fine today.")
                        .apply {
                            addStyle(
                                style = SpanStyle(color = Color.Blue),
                                start = 0,
                                end = length
                            )
                        }
                        .toAnnotatedString(),
                    modifier = Modifier.alpha(contentOpacity)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // AnimatedContent as a part of the transition.
            transition.AnimatedContent { targetState ->
                if (targetState) {
                    Text(text = "Selected", color = Color.Green)
                } else {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "Phone",
                        tint = Color.Red,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun Screen3() {
    val infiniteTransition = rememberInfiniteTransition()
    val color by infiniteTransition.animateColor(
        initialValue = Color.Red,
        targetValue = Color.Green,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(modifier = Modifier.fillMaxSize().background(color)) {
    }
}

@Composable
fun Screen4() {
    var offset by remember { mutableStateOf(Offset(0f, 0f)) }

    Box(
        modifier = Modifier.fillMaxSize().pointerInput(Unit) {
            detectTapGestures { offset = it }
        }
    ) {
        Circle(offset = offset)
    }
}

@Composable
fun Circle(offset: Offset) {
    Box(
        modifier = Modifier
            .offset(offset.x.dp, offset.y.dp)
            .graphicsLayer {
                translationX = offset.x
                translationY = offset.y
                rotationZ = offset.x + offset.y
            }
            .size(100.dp)
            .background(Color.Blue),
        content = {}
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApp()
}