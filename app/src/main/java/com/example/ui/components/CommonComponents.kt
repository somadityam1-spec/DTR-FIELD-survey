package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NaturalGreenPrimary
import com.example.ui.theme.PhaseBlue
import com.example.ui.theme.PhaseNeutral
import com.example.ui.theme.PhaseRed
import com.example.ui.theme.PhaseYellow
import com.example.ui.theme.StatusModerateAmber
import com.example.ui.theme.StatusModerateContainer
import com.example.ui.theme.StatusNormalGreen
import com.example.ui.theme.StatusOverloadRed

@Composable
fun SectionHeader(
    title: String,
    subtitle: String? = null,
    icon: ImageVector,
    iconColor: Color = MaterialTheme.colorScheme.primary,
    iconBgColor: Color = iconColor.copy(alpha = 0.14f),
    badgeText: String? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f, fill = false)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconBgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        letterSpacing = 0.2.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        if (badgeText != null) {
            Surface(
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.7f),
                border = BorderStroke(1.dp, Color.Black.copy(alpha = 0.08f))
            ) {
                Text(
                    text = badgeText,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 10.sp,
                        letterSpacing = 0.5.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SingleOptionSelector(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    optionColors: Map<String, Color> = emptyMap(),
    testTagPrefix: String = "option"
) {
    Column(modifier = modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                letterSpacing = 0.3.sp
            ),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
            modifier = Modifier.padding(bottom = 6.dp)
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            options.forEach { option ->
                val isSelected = option.equals(selectedOption, ignoreCase = true)
                val baseColor = optionColors[option] ?: NaturalGreenPrimary

                val chipBg = if (isSelected) {
                    baseColor
                } else {
                    Color.White.copy(alpha = 0.65f)
                }
                val chipBorder = if (isSelected) {
                    BorderStroke(1.5.dp, baseColor)
                } else {
                    BorderStroke(1.dp, Color.Black.copy(alpha = 0.12f))
                }
                val textColor = if (isSelected) {
                    Color.White
                } else {
                    MaterialTheme.colorScheme.onSurface
                }

                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onOptionSelected(option) }
                        .testTag("${testTagPrefix}_${option.replace(" ", "_").lowercase()}"),
                    shape = RoundedCornerShape(16.dp),
                    color = chipBg,
                    border = chipBorder
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                        }
                        Text(
                            text = option,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 13.sp
                            ),
                            color = textColor
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MultiOptionSelector(
    label: String,
    options: List<String>,
    selectedOptionsString: String,
    onOptionsChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    testTagPrefix: String = "multi_option"
) {
    val selectedList = selectedOptionsString.split(",")
        .map { it.trim() }
        .filter { it.isNotBlank() }
        .toSet()

    Column(modifier = modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                letterSpacing = 0.3.sp
            ),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
            modifier = Modifier.padding(bottom = 6.dp)
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            options.forEach { option ->
                val isSelected = selectedList.contains(option)
                val baseColor = NaturalGreenPrimary

                val chipBg = if (isSelected) {
                    baseColor
                } else {
                    Color.White.copy(alpha = 0.65f)
                }
                val chipBorder = if (isSelected) {
                    BorderStroke(1.5.dp, baseColor)
                } else {
                    BorderStroke(1.dp, Color.Black.copy(alpha = 0.12f))
                }

                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .clickable {
                            val newList = if (isSelected) {
                                selectedList - option
                            } else {
                                selectedList + option
                            }
                            onOptionsChanged(newList.joinToString(", "))
                        }
                        .testTag("${testTagPrefix}_${option.replace(" ", "_").lowercase()}"),
                    shape = RoundedCornerShape(16.dp),
                    color = chipBg,
                    border = chipBorder
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                        }
                        Text(
                            text = option,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 13.sp
                            ),
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PhaseCurrentInput(
    phaseLabel: String,
    value: String,
    onValueChange: (String) -> Unit,
    badgeColor: Color,
    modifier: Modifier = Modifier,
    testTag: String = "phase_input"
) {
    Column(
        modifier = modifier.padding(horizontal = 3.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(badgeColor)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = phaseLabel,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                ),
                color = badgeColor
            )
        }
        OutlinedTextField(
            value = value,
            onValueChange = { input ->
                if (input.isEmpty() || input.all { it.isDigit() || it == '.' }) {
                    onValueChange(input)
                }
            },
            placeholder = { Text("0 A", fontSize = 12.sp) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = badgeColor,
                unfocusedBorderColor = badgeColor.copy(alpha = 0.35f),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White.copy(alpha = 0.75f)
            ),
            modifier = Modifier
                .width(76.dp)
                .testTag(testTag)
        )
    }
}

@Composable
fun LoadStatusBadge(status: String, modifier: Modifier = Modifier) {
    val (bgColor, textColor, icon) = when (status) {
        "Over Load" -> Triple(StatusOverloadRed.copy(alpha = 0.14f), StatusOverloadRed, Icons.Default.Warning)
        "Moderate load" -> Triple(StatusModerateAmber.copy(alpha = 0.14f), StatusModerateAmber, Icons.Default.CheckCircle)
        "Under Load" -> Triple(StatusNormalGreen.copy(alpha = 0.14f), StatusNormalGreen, Icons.Default.CheckCircle)
        else -> Triple(Color(0xFFE2E8F0), Color(0xFF475569), Icons.Default.CheckCircle)
    }

    Surface(
        modifier = modifier,
        shape = CircleShape,
        color = bgColor,
        border = BorderStroke(1.dp, textColor.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = status,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                ),
                color = textColor
            )
        }
    }
}

