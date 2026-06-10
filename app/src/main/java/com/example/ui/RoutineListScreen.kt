package com.example.ui

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.data.Routine
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineListScreen(
    viewModel: RoutineViewModel,
    modifier: Modifier = Modifier
) {
    val routines by viewModel.allRoutines.collectAsState()
    val isPremium by viewModel.isPremium.collectAsState()
    val showInterstitial by viewModel.interstitialTriggerState.collectAsState()

    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    var showAddDialog by remember { mutableStateOf(false) }

    // Statistics calculations
    val totalRoutines = routines.size
    val completedRoutines = routines.count { it.isCompleted }
    val progress = if (totalRoutines > 0) completedRoutines.toFloat() / totalRoutines else 0f
    val remainingRoutines = totalRoutines - completedRoutines

    // Smooth animator for progress indicator bar
    val smoothProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "ProgressBarAnimation"
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Terrain,
                            contentDescription = "Island Icon",
                            tint = FocusSagePrimary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.app_title),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = MujiDarkText,
                            modifier = Modifier.testTag("app_title_text")
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.togglePremium()
                            val msg = if (!isPremium) {
                                "Premium Upgrade Success! 🌟 Ads Removed & Unlimited Widgets."
                            } else {
                                "Demo Premium deactivated."
                            }
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.testTag("premium_toggle_btn")
                    ) {
                        Icon(
                            imageVector = if (isPremium) Icons.Filled.Star else Icons.Outlined.StarBorder,
                            contentDescription = "Toggle Premium Mode",
                            tint = if (isPremium) Color(0xFFF1C40F) else MujiGrayText
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    showAddDialog = true
                },
                containerColor = FocusSagePrimary,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier
                    .padding(bottom = 60.dp) // Leave spacer above ad banner
                    .testTag("add_routine_fab")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Create new routine button"
                )
            }
        },
        bottomBar = {
            // Static Google Banner Ad simulation (disappears instantly if Premium is active)
            AnimatedVisibility(
                visible = !isPremium,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .background(Color(0xFFEDEDF0))
                        .border(1.dp, Color(0xFFDCDCE0))
                        .padding(12.dp)
                        .testTag("banner_ad_container"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .background(Color(0xFF999999), RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "AD",
                                fontSize = 9.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Google Mobile Ads • Test Ad Banner • Focus Island Premium to Remove",
                            fontSize = 11.sp,
                            color = Color(0xFF555555),
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 120.dp)
            ) {
                // 1. Scenic Visual Island Growth Card
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                            .shadow(2.dp, RoundedCornerShape(24.dp)),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = stringResource(R.string.today_progress),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = MujiGrayText,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Interactive scenic island growth representation
                            FocusIslandVisual(completionPercentage = progress)

                            Spacer(modifier = Modifier.height(14.dp))

                            // Smooth linear achievement progress bar
                            LinearProgressIndicator(
                                progress = { smoothProgress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(10.dp)
                                    .clip(CircleShape)
                                    .testTag("achievement_progress_bar"),
                                color = FocusSagePrimary,
                                trackColor = MujiLightGray,
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${(progress * 100).toInt()}% Done",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = FocusSagePrimary
                                )
                                Text(
                                    text = if (totalRoutines > 0) {
                                        if (remainingRoutines == 0) "All completed today! 🏝️" else "$remainingRoutines remaining"
                                    } else "0 routines created",
                                    fontSize = 12.sp,
                                    color = MujiGrayText
                                )
                            }
                        }
                    }
                }

                // Analytics Status Quick Indicator
                item {
                    Text(
                        text = "My Routines",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MujiDarkText,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                // 2. Empty State View
                if (totalRoutines == 0) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 40.dp)
                                .testTag("empty_state_view"),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Eco,
                                contentDescription = "Plant icon",
                                tint = MujiGrayText.copy(alpha = 0.5f),
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = stringResource(R.string.no_routines),
                                color = MujiGrayText,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 24.dp)
                            )
                        }
                    }
                }

                // 3. Routines List Items
                items(routines, key = { it.id }) { routine ->
                    RoutineItemCard(
                        routine = routine,
                        onToggle = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            viewModel.toggleRoutineCompletion(routine) {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.complete_toast),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        },
                        onDelete = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            viewModel.deleteRoutine(routine)
                            Toast.makeText(context, "Routine removed", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }

            // Standard Premium indicator status visible above ads
            AnimatedVisibility(
                visible = isPremium,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 12.dp)
            ) {
                Surface(
                    color = FocusSageLight,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .padding(horizontal = 30.dp)
                        .shadow(1.dp, RoundedCornerShape(12.dp))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.LocalActivity,
                            contentDescription = "Active Indicator",
                            tint = FocusSageDark,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.premium_active),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = FocusSageDark
                        )
                    }
                }
            }
        }
    }

    // Interactive add routine dialog overlays
    if (showAddDialog) {
        AddRoutineDialog(
            onDismiss = { showAddDialog = false },
            onSave = { title, colorHex ->
                viewModel.addRoutine(title, colorHex)
                showAddDialog = false
            }
        )
    }

    // Interactive Fullscreen Interstitial Ad Simulator (Only shown if NOT Premium)
    if (showInterstitial) {
        Dialog(onDismissRequest = { viewModel.dismissInterstitial() }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.7f)
                    .testTag("interstitial_ad_dialog"),
                shape = RoundedCornerShape(20.dp),
                color = Color.White
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFFE5E5E7), RoundedCornerShape(4.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "TEST INTERSTITIAL",
                                    fontSize = 10.sp,
                                    color = Color(0xFF666666),
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            IconButton(
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    viewModel.dismissInterstitial()
                                },
                                modifier = Modifier.testTag("dismiss_ad_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close ad and continue app"
                                )
                            }
                        }

                        // Playful island-style ad illustration
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.NaturePeople,
                                contentDescription = "Ad Graphics",
                                tint = FocusSagePrimary,
                                modifier = Modifier.size(90.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Grow Larger Islands!",
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                                color = MujiDarkText
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Focus Premium gives you multiple islands, visual themes, detail metrics, and 100% ad-free flow.",
                                fontSize = 13.sp,
                                color = MujiGrayText,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 12.dp)
                            )
                        }

                        // Action Button inside ad
                        Button(
                            onClick = {
                                viewModel.togglePremium()
                                viewModel.dismissInterstitial()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = FocusSagePrimary),
                            shape = RoundedCornerShape(50),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Upgrade to Focus Premium ($0 - Free Trial)", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

/**
 * Multi-adaptive Growth Visualizer Canvas. Represents the growing Focus Island
 */
@Composable
fun FocusIslandVisual(completionPercentage: Float) {
    // Canvas animation elements (e.g. constant subtle water ripple or palm tree sway)
    val infiniteTransition = rememberInfiniteTransition(label = "scenic sway")
    val swayOffset by infiniteTransition.animateFloat(
        initialValue = -3f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "wind_sway"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFE3EFF5), Color(0xFFEFF5FA))
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .border(1.dp, Color(0xFFE5ECEF), RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            val cX = canvasWidth / 2f
            val cY = canvasHeight / 2f + 25f

            // 1. Draw Ocean Water ripple arcs (always visible)
            drawArc(
                color = Color(0xFFAFD0E1),
                startAngle = 10f,
                sweepAngle = 160f,
                useCenter = false,
                style = Stroke(width = 3f),
                size = Size(220.dp.toPx(), 40.dp.toPx()),
                topLeft = Offset(cX - 110.dp.toPx(), cY + 15.dp.toPx())
            )

            // 2. Beach Island structure (Always visible, changes color to rich golden sand based on progress)
            val sandColor = Color(
                red = 0.90f + (completionPercentage * 0.08f).coerceAtMost(0.08f),
                green = 0.85f + (completionPercentage * 0.08f).coerceAtMost(0.08f),
                blue = 0.78f + (completionPercentage * 0.07f).coerceAtMost(0.07f),
                alpha = 1.0f
            )
            drawOval(
                color = sandColor,
                topLeft = Offset(cX - 80.dp.toPx(), cY - 10.dp.toPx()),
                size = Size(160.dp.toPx(), 45.dp.toPx())
            )

            // Draw clean shadow under beach
            drawOval(
                color = Color(0xFFD6E2EB),
                topLeft = Offset(cX - 70.dp.toPx(), cY + 14.dp.toPx()),
                size = Size(140.dp.toPx(), 18.dp.toPx())
            )

            // 3. Level 1: Tiny Sprout Plant (Progress > 0%)
            if (completionPercentage > 0f) {
                // Sprout stem
                drawArc(
                    color = Color(0xFF8AA682),
                    startAngle = 180f,
                    sweepAngle = 90f,
                    useCenter = false,
                    style = Stroke(width = 4f),
                    size = Size(16.dp.toPx(), 24.dp.toPx()),
                    topLeft = Offset(cX - 38.dp.toPx(), cY - 26.dp.toPx())
                )
                // Left leaf
                drawOval(
                    color = Color(0xFF9CB894),
                    topLeft = Offset(cX - 46.dp.toPx(), cY - 28.dp.toPx()),
                    size = Size(9.dp.toPx(), 6.dp.toPx())
                )
                // Right leaf
                drawOval(
                    color = Color(0xFF9CB894),
                    topLeft = Offset(cX - 33.dp.toPx(), cY - 32.dp.toPx()),
                    size = Size(9.dp.toPx(), 6.dp.toPx())
                )
            }

            // 4. Level 2: Majestic Palm Tree (Progress >= 30%)
            if (completionPercentage >= 0.30f) {
                // Sway trunk coordinates using swayOffset animation
                val swayX = swayOffset.dp.toPx()

                // Palm Trunk
                drawArc(
                    color = Color(0xFFAC8A64),
                    startAngle = 190f,
                    sweepAngle = 75f,
                    useCenter = false,
                    style = Stroke(width = 8f),
                    size = Size(50.dp.toPx() + swayX, 80.dp.toPx()),
                    topLeft = Offset(cX + 12.dp.toPx() - (swayX / 2f), cY - 76.dp.toPx())
                )

                // Coconuts
                if (completionPercentage >= 0.50f) {
                    drawCircle(
                        color = Color(0xFF7A624E),
                        radius = 4.dp.toPx(),
                        center = Offset(cX + 32.dp.toPx() + swayX, cY - 75.dp.toPx())
                    )
                    drawCircle(
                        color = Color(0xFF6B5542),
                        radius = 4.5.dp.toPx(),
                        center = Offset(cX + 38.dp.toPx() + swayX, cY - 76.dp.toPx())
                    )
                }

                // Palm Leaves
                val leavesColor = Color(0xFF698F65)
                // Leaf 1 (Left)
                drawOval(
                    color = leavesColor,
                    topLeft = Offset(cX + 10.dp.toPx() + swayX, cY - 94.dp.toPx()),
                    size = Size(26.dp.toPx(), 14.dp.toPx())
                )
                // Leaf 2 (Right)
                drawOval(
                    color = leavesColor,
                    topLeft = Offset(cX + 36.dp.toPx() + swayX, cY - 84.dp.toPx()),
                    size = Size(28.dp.toPx(), 13.dp.toPx())
                )
                // Leaf 3 (Top)
                drawOval(
                    color = Color(0xFF7AA575),
                    topLeft = Offset(cX + 22.dp.toPx() + swayX, cY - 102.dp.toPx()),
                    size = Size(18.dp.toPx(), 22.dp.toPx())
                )
            }

            // 5. Level 3: Tropical Flowers and Grass (Progress >= 60%)
            if (completionPercentage >= 0.60f) {
                // Flower 1 (Pink)
                drawCircle(
                    color = Color(0xFFE08686),
                    radius = 3.dp.toPx(),
                    center = Offset(cX - 12.dp.toPx(), cY - 12.dp.toPx())
                )
                drawCircle(
                    color = Color(0xFFF0A0A0),
                    radius = 1.5.dp.toPx(),
                    center = Offset(cX - 12.dp.toPx(), cY - 12.dp.toPx())
                )

                // Flower 2 (Orange)
                drawCircle(
                    color = Color(0xFFE29F58),
                    radius = 2.5.dp.toPx(),
                    center = Offset(cX + 8.dp.toPx(), cY - 10.dp.toPx())
                )
                drawCircle(
                    color = Color(0xFFFFF1C4),
                    radius = 1.2.dp.toPx(),
                    center = Offset(cX + 8.dp.toPx(), cY - 10.dp.toPx())
                )
            }

            // 6. Level 4: Fully populated Starry Skies (Progress >= 100%)
            if (completionPercentage >= 1.0f) {
                // Draw warm shiny stars using little circles/cross stars
                val starColor = Color(0xFFECC45C)

                // Star 1
                drawCircle(
                    color = starColor,
                    radius = 2.5.dp.toPx(),
                    center = Offset(cX - 70.dp.toPx(), cY - 70.dp.toPx())
                )
                // Star 2
                drawCircle(
                    color = starColor,
                    radius = 3.2.dp.toPx(),
                    center = Offset(cX + 66.dp.toPx(), cY - 95.dp.toPx())
                )
                // Star 3 (twinkling mini)
                drawCircle(
                    color = Color.White,
                    radius = 1.5.dp.toPx(),
                    center = Offset(cX - 20.dp.toPx(), cY - 110.dp.toPx())
                )

                // Scenic Mini Rainbow arching over Focus Island
                drawArc(
                    color = Color(0xFF7EB7BD).copy(alpha = 0.5f),
                    startAngle = 180f,
                    sweepAngle = 180f,
                    useCenter = false,
                    style = Stroke(width = 3f),
                    size = Size(100.dp.toPx(), 60.dp.toPx()),
                    topLeft = Offset(cX - 50.dp.toPx(), cY - 60.dp.toPx())
                )
            }
        }

        // Floating Scenic label displaying state details
        Surface(
            color = Color.White.copy(alpha = 0.85f),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(10.dp)
                .shadow(1.dp, RoundedCornerShape(10.dp))
        ) {
            Text(
                text = when {
                    completionPercentage <= 0.0f -> "Quiet Island 🪨"
                    completionPercentage < 0.30f -> "Sprouting Island 🌱"
                    completionPercentage < 0.60f -> "Growing Palm Island 🌴"
                    completionPercentage < 1.0f -> "Blooming Island 🌸"
                    else -> "Perfect Oasis Island! 🏝️⭐️"
                },
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = FocusSageDark,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
            )
        }
    }
}

/**
 * Routine List item card styled exactly following the MUJI minimalist visual guidelines
 */
@Composable
fun RoutineItemCard(
    routine: Routine,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    val cardBg = Color(android.graphics.Color.parseColor(routine.colorHex))
    val checkMarkColor = if (routine.isCompleted) FocusSagePrimary else Color.Transparent

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(1.dp, RoundedCornerShape(16.dp))
            .testTag("routine_card_${routine.id}"),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Checked Circle indicator (Haptic on actions)
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(2.dp, FocusSagePrimary, CircleShape)
                    .clickable { onToggle() }
                    .testTag("routine_check_box_${routine.id}"),
                contentAlignment = Alignment.Center
            ) {
                if (routine.isCompleted) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Routine complete status icon",
                        tint = checkMarkColor,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Routine title details
            Text(
                text = routine.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MujiDarkText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .testTag("routine_title_${routine.id}")
            )

            // Remove option
            IconButton(
                onClick = onDelete,
                modifier = Modifier
                    .size(32.dp)
                    .testTag("routine_delete_btn_${routine.id}")
            ) {
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = "Delete routine action",
                    tint = MujiGrayText.copy(alpha = 0.7f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

/**
 * Beautiful Overlay dialog to create a new routine
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddRoutineDialog(
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var titleText by remember { mutableStateOf("") }
    var selectedColorHex by remember { mutableStateOf("#DFEADF") } // Sage as default

    val availablePastelColors = listOf(
        "#DFEADF" to "Sage",
        "#DCE6F5" to "Sky",
        "#F5DFDF" to "Rose",
        "#F8EAD6" to "Sand",
        "#E6DFF5" to "Lilac",
        "#F5E4DC" to "Peach"
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .shadow(8.dp, RoundedCornerShape(20.dp))
                .testTag("add_routine_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(
                    text = stringResource(R.string.add_routine),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MujiDarkText
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Text Input
                OutlinedTextField(
                    value = titleText,
                    onValueChange = { titleText = it },
                    placeholder = { Text(stringResource(R.string.routine_placeholder), fontSize = 14.sp) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = FocusSagePrimary,
                        unfocusedBorderColor = MujiBorder,
                        focusedLabelColor = FocusSagePrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("routine_input_field")
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Color tags selection
                Text(
                    text = stringResource(R.string.routine_color),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MujiGrayText
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Row of Circle colors
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    availablePastelColors.forEach { (hexCode, label) ->
                        val parsedColor = Color(android.graphics.Color.parseColor(hexCode))
                        val isSelected = selectedColorHex == hexCode

                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(parsedColor)
                                .border(
                                    width = if (isSelected) 3.dp else 0.dp,
                                    color = if (isSelected) FocusSagePrimary else Color.Transparent,
                                    shape = CircleShape
                                )
                                .clickable { selectedColorHex = hexCode }
                                .testTag("color_choice_$label"),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = FocusSagePrimary,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Call-To-Action Row Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel", color = MujiGrayText)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (titleText.isNotBlank()) {
                                onSave(titleText, selectedColorHex)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = FocusSagePrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("dialog_save_btn")
                    ) {
                        Text(stringResource(R.string.create_routine_btn), color = Color.White)
                    }
                }
            }
        }
    }
}
